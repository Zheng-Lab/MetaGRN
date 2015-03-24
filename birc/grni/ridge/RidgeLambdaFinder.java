package birc.grni.ridge;

import birc.grni.ridge.RidgeAlgorithm;
import birc.grni.ridge.RidgeStat;

import java.util.ArrayList;


public class RidgeLambdaFinder {

	// parameter selection part has implemented here
	protected double [][] yValues; // to keep individual y values
	protected double [][] xInputs;
	protected double [] aLambdaArray = {0.001, 0.1, 1, 10, 100};
	protected double [][] parmt ;
	protected int nFolds;
	protected int numGenes;
	protected double [][] testX;
	protected double [][] y1Data;
	protected double [][] x1Data;
	protected double [] cvfit;
	protected double [][] predmat;
	protected double [][] cvraw;
	protected double alpValue;
	
	
	public void Initialize(){
		int lambdaSize = aLambdaArray.length;
		parmt = new double [lambdaSize][2];
		testX = new double [1][numGenes];
		y1Data  = new double [nFolds-1][1];
		x1Data = new double [nFolds-1][numGenes];
		cvfit = new double [numGenes];
		predmat = new double [nFolds][1];
		cvraw = new double [nFolds][1];
	}
	
	public double lambdaFinder(){
		for(int jj=0;jj<aLambdaArray.length;jj++){
			for(int i=0;i<nFolds;i++){
				double testY = yValues[i][0];
				// construction of testX matrix
				for(int k=0;k<numGenes;k++){
					testX[0][k]=xInputs[i][k];
				}
				// construction of Y1 and X1 matrix
				inputModification(i);
				// apply data in to the ridge algorithm
				cvfit=RidgeAlgorithm.ridge(y1Data, x1Data, aLambdaArray[jj], 1);
				double predmatValue=0;
				for(int q=0;q<numGenes;q++){
					predmatValue=predmatValue + testX[0][q]*cvfit[q]; 
				}
				predmat[i][0]=predmatValue;
			}
			// construction of cvraw matrix
			for(int h=0;h<nFolds;h++){
				cvraw[h][0]=Math.pow(yValues[h][0]-predmat[h][0], 2);
			}
			double [] cvErrMean= RidgeStat.mean(cvraw, nFolds, 1);
			parmt [jj][0] = aLambdaArray[jj] ;
			parmt [jj][1] = cvErrMean[0]; 
		}
		// find best lambda value
		double minValue=Double.MAX_VALUE;
		int minIndex=0;
		for(int kk=0;kk<aLambdaArray.length;kk++){
			if(parmt[kk][1]<minValue){
				minValue=parmt[kk][1];
				minIndex=kk;
			}
		}
		alpValue=parmt[minIndex][0];
		return alpValue;
	}
	
	private void inputModification(int index){
		for(int i=0;i<index;i++){
			y1Data[i][0]=yValues[i][0];
			for(int j=0;j<numGenes;j++){
				x1Data[i][j]=xInputs[i][j];
			}
		}
		
		for(int i=index+1;i<nFolds;i++){
			y1Data[i-1][0]=yValues[i][0];
			for(int j=0;j<numGenes;j++){
				x1Data[i-1][j]=xInputs[i][j];
			}
		}
	}
	
	
	
}

