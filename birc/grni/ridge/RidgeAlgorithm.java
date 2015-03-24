package birc.grni.ridge;

import birc.grni.ridge.RidgeStat;
import Jama.*;

public class RidgeAlgorithm {
	protected static boolean scalable;
	
	public static double [] ridge(double [][] yData, double [][] xData, double ridgeParameter, int flag){
		
		// set the scale parameter
		if(flag==1){
			scalable = false;
		}
		else if (flag==0) {
			scalable = true;
		}
		else{
			System.out.println("ridge:BadScalingFlag");
			System.exit(1);
		}
		
		//Check that matrix (xData) and left hand side (yData) have compatible dimensions
		int nValue = xData.length;     //number of time points
		int pValue = xData[0].length;  // number of genes
		int n1Value =yData.length;		 //number of time points
		int collhsValue = yData[0].length;
		if(nValue != n1Value){
			System.out.println("ridge:InputSizeMismatch");
			System.exit(1);
		}
		if(collhsValue != 1){
			System.out.println("ridge:InvalidData");
			System.exit(1);
		}
		
		//Remove any missing values
		/* ASSUME THAT THERE IS NO MISSING VALUES */
		
		//Normalize the columns of xData to mean zero, and standard deviation one
		double [] mxValue = RidgeStat.mean(xData, n1Value, pValue);
		double [] stdx = RidgeStat.unbiasedStd(xData, n1Value, pValue);
		
		// implementation of below code line 
		/* idx = find(abs(stdx) < sqrt(eps(class(stdx)))); */
		
		/* in Matlab implementation all input have double precision.
		 * therefore, output of class(stdx) is equal to double
		 * then ouput of eps(double) is become a fixed and value equal to 2^(-52)  */
		double  sqrtEpsClass = Math.sqrt(Math.pow(2, -52));
		
		for (int i=0;i<pValue;i++){
			if(Math.abs(stdx[i]) < sqrtEpsClass){
				stdx[i]=1;
			}
			
		}
		
		
		double [][] MX = new double [nValue][pValue];
		double [][] STDX = new double [nValue][pValue];
		for(int i=0; i<pValue;i++){
			for(int j=0; j<nValue;j++){
				MX[j][i]=mxValue[i];
				STDX[j][i]=stdx[i];
			}	
		}
		
		// Z matrix calculation
		double [][] zMatrix = new double [nValue][pValue];
		for(int i=0; i<nValue;i++){
			for(int j=0; j<pValue;j++){
				zMatrix[i][j]= (xData[i][j]-MX[i][j])/STDX[i][j];
			}
		}
		
		//Compute the ridge coefficient estimates using the technique of
		// adding pseudo observations having y=0 and X'X = k*I.
		double kSquare=Math.sqrt(ridgeParameter);
		double [][] pseudo = new double[pValue][pValue];
		for(int i=0; i<pValue;i++){
			for(int j=0; j<pValue;j++){
				if(i==j){
					pseudo[i][j]=1;  // set diagonals to 1 (eye(p))
				}
				pseudo[i][j]=pseudo[i][j]*kSquare;
			}
		}
		// calculate zPlus matrix
		double [][] zPlus = new double[nValue+pValue][pValue];
		for(int i=0; i<nValue;i++){
			for(int j=0; j<pValue;j++){
				zPlus[i][j]=zMatrix[i][j];
			}
		}
		for(int m=nValue;m<nValue+pValue;m++){
			for(int n=0;n<pValue;n++){
				zPlus[m][n]=pseudo[m-nValue][n];
			}
		}
		//yPlus matrix calculation
		double [][] yPlus = new double[nValue+pValue][1];
		for(int i=0; i<nValue;i++){
			yPlus[i][0]=yData[i][0];
		}
		for(int k=nValue;k<nValue+pValue;k++){
			yPlus[k][0]=0;
		}
		
		int nkValue = 1; // nk = numel(k); /* in this case k is not a array it is a single value so nk=1  */
		
		//Compute the coefficient estimates
		Matrix zPlusMat= new Matrix(zPlus);
		Matrix yPlusMat= new Matrix(yPlus);
		QRDecomposition qr = new QRDecomposition(zPlusMat);
		Matrix bMat = qr.solve(yPlusMat);
		double [][] bResultMat = bMat.getArray();
		
		/* in this implementation nk always equal to 1 and unscale property is false
		 * therefore we have not implement some cases */
		
		int resultRow = bResultMat.length;
		int resultCol = bResultMat[0].length;
		
		if(resultCol !=1){
			System.out.println("ridge:result matrix only have 1 column");
			System.exit(1);
		}
		double [] beta = new double[resultRow];
		for(int i=0;i<resultRow;i++){
			beta[i]=bResultMat[i][0];
		}
		return beta;
	}
	

}
