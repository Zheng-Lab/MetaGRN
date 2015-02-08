package birc.grni.lasso.delay;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class FindCrosscorrelation {
	double[][] expressionData;
	int targetGene;
	int delay;
	//private Integer numGenes;
	int numGenes;
	int timePoints;
	int [][] delayMatrix = null;
	int [] delayArray = null;
	
	public FindCrosscorrelation(double[][] data, int maxDelay, int genes , int timePoints){
		this.expressionData = data;
		this.delay = maxDelay;
		this.numGenes = genes;
		this.timePoints = timePoints;
	}
	
	public void calculateOptimalDelay(int target) {
		
		this.targetGene = target;
		delayMatrix = new int[numGenes][delay];
		delayArray = new int [numGenes];

		for (int i = 0; i < numGenes; i++) {
			for (int j = 0; j < delay; j++) {
				delayMatrix[i][j] = 0;
			}
		}

		int ii = targetGene;

		for (int jj = 0; jj < numGenes; jj++) {

			if (ii != jj) {
				/*double[] a = new double[numGenes];
				double[] b = new double[numGenes];
				for (int k = 0; k < numGenes; k++) {
					a[k] = expressionData[k][ii];
					b[k] = expressionData[k][jj];
				} */
				double[] a = new double[timePoints];
				double[] b = new double[timePoints];
				for (int k = 0; k < timePoints; k++) {
					a[k] = expressionData[k][ii];
					b[k] = expressionData[k][jj];
				}
				// double [] m1 = new double[numGenes - delay];
				double[] m = new double[delay + 1];
				for (int j = 0; j <= delay; j++) {
					m[j] = Math.abs(xcorr(a, b, j));
				}
				// System.out.println(maxIndex(m));
				if (maxIndex(m) != 0) {
					delayMatrix[jj][maxIndex(m) - 1] = 1;
				}
			}
		}
		convertDelayMatrixToArray();
		
	}

	public double xcorr(double[] a, double[] b, int i) { // i the ith delay

		/*double[] a1 = new double[numGenes - i];
		double[] b1 = new double[numGenes - i];
		for (int j = 0; j < numGenes - i; j++) {
			a1[j] = a[i + j];
			b1[j] = b[j];
		}*/
		
		double[] a1 = new double[timePoints - i];
		double[] b1 = new double[timePoints - i];
		for (int j = 0; j < timePoints - i; j++) {
			a1[j] = a[i + j];
			b1[j] = b[j];
		}

		PearsonsCorrelation cor = new PearsonsCorrelation();

		return cor.correlation(a1, b1);
	}

	public int maxIndex(double[] c) {
		double max = c[1];
		int j = 1;
		for (int i = 1; i < c.length; i++) {
			if (c[i] > max) {
				j = i;
				max = c[i];
			}
		}
		return j;
	}
	
	public int [][] getDelayMatrix() {
		return delayMatrix;
	}
	
	public void convertDelayMatrixToArray (){
		for(int p=0; p<numGenes; p++){
			if(p != targetGene){
				for(int q=0;q<delay;q++){
					if(delayMatrix [p][q] == 1){
						delayArray[p]=q+1;
						
					}
				}
			}
		}
	}
	
	public int [] getDelayArray(){
		return delayArray;
	}

	/*public void setDelayMatrix(Integer[][] delayMatrix) {
		this.delayMatrix = delayMatrix;
	} */

}
