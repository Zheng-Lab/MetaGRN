package birc.grni.ridge;


public class RidgeStat {
	

	public static double mean(double[] inputVector) {
		double sum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			sum += inputVector[i];
		}
		return sum/inputVector.length;
	}
	
	
	public static double unbiasedStd(double[] inputVector) {
		double mean = mean(inputVector);
		double squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		double sd = squareSum/(inputVector.length -1);
		return Math.sqrt(sd);
	}
	
	public static double [] mean(double [][] inputMatrix, int row, int col){
		
		double [] result = new double[col];
		for(int i=0 ;i<col;i++){
			double [] colValues= new double [row];
			for(int j=0 ;j<row;j++){
				colValues[j]=inputMatrix[j][i];
			}
			result[i]=mean(colValues);
		}
		return result;
	}
	
	public static double [] unbiasedStd(double [][] inputMatrix, int row, int col){
		double [] result = new double[col];
		for(int i=0 ;i<col;i++){
			double [] colValues= new double [row];
			for(int j=0 ;j<row;j++){
				colValues[j]=inputMatrix[j][i];
			}
			result[i]=unbiasedStd(colValues);
		}
		return result;
	}


}

