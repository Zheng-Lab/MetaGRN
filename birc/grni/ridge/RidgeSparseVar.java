
package birc.grni.ridge;

import birc.grni.ridge.MatrixOperations;
import birc.grni.ridge.RidgeAlgorithm;
import birc.grni.ridge.RidgeLambdaFinder;
import birc.grni.ridge.RidgeStat;
import Jama.*;

public class RidgeSparseVar {
	protected double [][] yData;
	protected double [][] xData;
	protected RidgeLambdaFinder lambda;
	protected int numGenes;
	protected int numSamples;
	protected double [][] matrixA;
	protected int [][] dof;
	protected double [][] tValue;
	protected double [][] bRidge;
	protected double [][] individual_Ydata; 
	protected double alp; // best value for lambda
	protected double [][] eyeMatrix;
	protected Matrix eye;
	protected Matrix xDataMat;
	protected Matrix A;
		
	public RidgeSparseVar(double[][] x, double[][] y){
		yData=y;
		xData=x;
		numSamples=yData.length;
		numGenes=yData[0].length;
		/* construct of matrixA
		Matrix xMatrix= new Matrix(xData); */
		
		lambda = new RidgeLambdaFinder();
		lambda.xInputs=xData;
		lambda.numGenes=numGenes;
		lambda.nFolds=numSamples;
		individual_Ydata = new double[numSamples][1];
		bRidge = new double[numGenes][numGenes];
		tValue = new double[numGenes][numGenes];
		dof= new int[1][numGenes]; 
		eyeMatrix = new double[numGenes][numGenes];
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				if(i==j){
					eyeMatrix[i][j]=1;
				}
			}
		}
	   eye =new Matrix(eyeMatrix);
	   xDataMat = new Matrix(xData);
	   Matrix xTranspose=xDataMat.transpose();
	   double [][] dataX = xTranspose.getArray();
	   matrixA = MatrixOperations.multiplication(dataX, xData);
	   A = new Matrix(matrixA);
	}
	
	public void runRidge(int geneId){
		
		// read individual Ydata for each gene
		for(int k=0;k<numSamples;k++){
			individual_Ydata[k][0]=yData[k][geneId];
		}
		lambda.yValues=individual_Ydata;
		lambda.Initialize();
		alp = lambda.lambdaFinder();
		double [] betaValues =RidgeAlgorithm.ridge(individual_Ydata, xData, alp, 1);
		if(betaValues.length != numGenes){
			System.out.println("ridge:length of beta array should equal to number of genes");
			System.exit(1);
		}
		// construction of bRidge matrix
		for(int a=0;a<betaValues.length;a++){
			bRidge[a][geneId]=betaValues[a];
		}
		findDegreeOfFreedom(geneId,betaValues);
		tValaueCalculate(geneId, betaValues);
			
	}
	
	public void findDegreeOfFreedom(int col, double [] beta){
		/* implementation of below code segment
		 * dof(:,ii) = gene - size(find(Bridge(:,ii)==0),1);
		 * */
		int [] index=MatrixOperations.find(beta, 0);
		int size= index.length;
		int dofValue = numGenes - size;
		dof[0][col]=dofValue;
		
	}
	
	public void tValaueCalculate(int col , double [] betaArray){
		double [][] repMat1 = MatrixOperations.repmat(1, numGenes, 1);
		//construction of B1 matrix and correction of Inf values
		double [][] B1matrix = new double [numGenes][1];
		for(int i=0;i<numGenes;i++){
			double value= Math.abs(betaArray[i]);
			if(value==0){
				B1matrix[i][0]= 0;
			}else{
				B1matrix[i][0]= repMat1[i][0]/value;
			}		
		}
		// construction of Bn matrix
		double [][] b1Alp = new double [numGenes][1];
		for(int k=0;k<numGenes;k++){
			b1Alp [k][0]=B1matrix[k][0]*alp;
		}
		double [][] repMat2 =MatrixOperations.repmat(b1Alp, 1, numGenes);
		//Matrix rep2 = new Matrix(repMat2);
		//Matrix bnMatrix =rep2.arrayTimes(eye);
		//double [][] bn = bnMatrix.getArray();
		double [][] bn =MatrixOperations.multiplicationByElement(repMat2, eyeMatrix);
		Matrix bnMatrix = new Matrix(bn);
		
		//construction of Er matrix
		double  [] betaMeanMatrix =RidgeStat.mean(individual_Ydata, numSamples, 1);
		double betaMean=betaMeanMatrix[0];
		double [][] beta1 = new double[numGenes][1];
		for(int i=0;i<numGenes;i++){
			beta1[i][0]=betaArray[i] - betaMean;
		}
		//Matrix beta1Mat = new Matrix(beta1);
		//Matrix xBetaMat = xDataMat.arrayTimes(beta1Mat);
        //double [][] xBeta = xBetaMat.getArray();
		double [][] xBeta =MatrixOperations.multiplication(xData, beta1); 
		double [][] Er = new double[numSamples][1];
		for(int i=0;i<numSamples;i++){
			Er[i][0] = individual_Ydata[i][0] - xBeta[i][0];
		}
		//calculation of Va
		Matrix ErMat = new Matrix(Er);
		Matrix transposeEr=ErMat.transpose();
		//Matrix multEr = transposeEr.arrayTimes(ErMat);
		//double [][] multErArray = multEr.getArray();
		double [][] ErT = transposeEr.getArray();
		double [][] multEr = MatrixOperations.multiplication(ErT, Er);
		//double erValue = multErArray[0][0];
		double erValue = multEr[0][0];
		double va =erValue/numSamples;
		
		// calculation of W
		
		Matrix inter1 = A.plus(bnMatrix);    //A+Bn
		Matrix inter2 = inter1.inverse();    //inv(A+Bn)
		//Matrix inter3 = A.arrayTimes(inter2); //A*inv(A+Bn)
		//Matrix inter4 = inter2.arrayTimes(inter3);
		double [][] inter2mat = inter2.getArray();
		double [][] inter3 = MatrixOperations.multiplication(matrixA, inter2mat);
		//double [][] inter4Array =inter4.getArray();
		double [][] inter4Array = MatrixOperations.multiplication(inter2mat, inter3);
		double [][] Wmat = MatrixOperations.diag(inter4Array);
		
		// calculation of tValues
		double [][] matVaW = new double [numGenes][1];
		for(int j=0;j<numGenes;j++){
			matVaW [j][0] = Math.sqrt(va*Wmat[j][0]);
		}
		for(int i=0;i<numGenes;i++){
			tValue[i][col]=(betaArray[i])/(matVaW[i][0]);
		}
		
	}
	
	public int [][] getDofMatrix(){
		return dof;
	}
	
	public double [][] getTvalueMatrix(){
		return tValue;
	}
	

}

