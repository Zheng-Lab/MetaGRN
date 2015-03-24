package birc.grni.rf;
import java.util.*;

public class Util {	
	/**
	 * generate random double matrix
	 * @param rows
	 * @param columns
	 * @return
	 */
	public static ArrayList<ArrayList<Double>> randn(int rows, int columns){
		
		/* random matrix*/
		ArrayList<ArrayList<Double>> randomMatrix = new ArrayList<ArrayList<Double>>();
		
		/* generate random matrix*/
		Random random = new Random();
		for(int i = 0; i< rows; i++) {
			ArrayList<Double> oneRow = new ArrayList<Double>();
			for(int j = 0; j< columns; j++) {
				oneRow.add(2*random.nextDouble()-1); /* between -1 and 1*/
			}
			randomMatrix.add(oneRow);
		}
		
		return randomMatrix;
	}
	
	/**
	 * generate random float (single) matrix
	 * @param rows
	 * @param columns
	 * @return
	 */
	public static ArrayList<ArrayList<Float>> randnSingle(int rows, int columns){
		
		/* random matrix*/
		ArrayList<ArrayList<Float>> randomMatrix = new ArrayList<ArrayList<Float>>();
		
		/* generate random matrix*/
		Random random = new Random();
		for(int i = 0; i< rows; i++) {
			ArrayList<Float> oneRow = new ArrayList<Float>();
			for(int j = 0; j< columns; j++) {
				oneRow.add(2*random.nextFloat()-1); /* between -1 and 1*/
			}
			randomMatrix.add(oneRow);
		}
		
		return randomMatrix;
	}
	
	/**
	 * Validate number of input arguments and return an appropriate error message
	 * @param low
	 * @param high
	 * @param n
	 * @return
	 */
	public static String nargchk(int low, int high, int n) {
		if(n < low)
			return "Not enough input arguments.";
		else if(n > high)
			return "Too many input arguments.";
		else
			return "";
	}
	
	/**
	 * 
	 * @param low
	 * @param high
	 * @param n
	 * @param returnType
	 * @return
	 */
	public static Object nargchk(int low, int high, int n, String returnType) {
		if(returnType.equalsIgnoreCase("string"))
			return nargchk(low, high, n);
		else if(returnType.equalsIgnoreCase("struct")) {
			if (n < low)
				return new MessageStruct("0", "Not enough input arguments.");
			else if (n> high)
				return new MessageStruct("1", "Too many input arguments.");
			else
				return new MessageStruct();
		}
		else {
			System.out.println("No such return type in nargchk.");
			return null;
		}
	}
	
	/**
	 * print the error message
	 */
	public static void error() {
		
	}
	
	/**
	 * Return a list of numbers from @param low to @param high (both inclusive)
	 * @param low
	 * @param high
	 * @return
	 */
	public static ArrayList<Integer> range(int low, int high) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=low;i<=high;i++) {
			list.add(i);
		}
		return list;
	}
	
	/**
	 * transfer a double precision vector to a single precision one
	 * @param doubleVector an ArrayList object contains the double precision vector
	 * @return
	 */
	public static ArrayList<Float> doubleToSingleVector(ArrayList<Double> doubleVector) {
		ArrayList<Float> singleVector = new ArrayList<Float>();
		for(Double dNumber : doubleVector)
			singleVector.add((dNumber.floatValue()));
		return singleVector;
	}
	
	/**
	 * transfer a double precision vector to a single precision one
	 * @param doubleVector a real array, can use basic type(int, float, double, long) elements
	 * @return
	 */
	public static float[] doubleToSingleVector(double[] doubleVector, int length) {
		float[] singleVector = new float[length];
		for(int i = 0;i < length; i++)
			singleVector[i] = (float) doubleVector[i];
		return singleVector;
	}
	
	/**
	 * transfer a double precision matrix to a single precision one
	 * @param doubleMatrix
	 * @return
	 */
	public static ArrayList<ArrayList<Float>> doubleToSingleMatrix(ArrayList<ArrayList<Double>> doubleMatrix) {
		ArrayList<ArrayList<Float>> singleMatrix = new ArrayList<ArrayList<Float>>();
		for(ArrayList<Double> doubleOneRow : doubleMatrix) {
			singleMatrix.add(doubleToSingleVector(doubleOneRow));
		}
		return singleMatrix;
	}
	
	public static float[][] doubleToSingleMatrix(double[][] doubleMatrix, int row, int column) {
		float[][] singleMatrix = new float[row][];
		for(int i = 0; i< row; i++)
			singleMatrix[i] = doubleToSingleVector(doubleMatrix[i], column);
		return singleMatrix;
	}
	
	/**
		return a double precision matrix of all zeros
		@param row
		@param column
		@return double precision matrix of all zeros
	 */
	public static double[][] zeros(int row, int column)
	{
		double[][] zeroMatrix = new double[row][column];
		for(int i = 0; i< row; i++)
			for(int j = 0; j< column; j++)
				zeroMatrix[i][j] = 0;
			
		return zeroMatrix;
	}
}

/**
 * message structure
 * @author liu xingliang
 *
 */
class MessageStruct {
	
	private String identifier;
	private String message;
	
	public MessageStruct() {
		this.identifier = "";
		this.message = "";
	}
	
	public MessageStruct(String identifier, String message) {
		this.identifier = identifier;
		this.message = message;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}
	
	public String getMessage() {
		return this.message;
	}
}
