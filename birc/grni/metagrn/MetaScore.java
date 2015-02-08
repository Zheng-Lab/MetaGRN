package birc.grni.metagrn;

import java.util.*;
import java.io.*;
import Jama.Matrix;
import org.apache.commons.math3.stat.correlation.*;
import com.google.common.primitives.Doubles;

public class MetaScore {

	public static void main(String[] args) throws IOException {
//		String testDataFolder = "C:/Users/liuxl/Desktop/tmp/MetaGRN_R";
//		//TEST
//		BufferedReader br = new BufferedReader(new FileReader(testDataFolder + "/Yeast-regulators-1/DBN/DBN1_dream4_timeseries1.tsv"));
//		String line = null;
//		br.readLine();
//		br.readLine();
//		
//		ArrayList<ArrayList<Double>> exp1 = new ArrayList<ArrayList<Double>>();
//		while((line = br.readLine()) != null) {
//			String[] arr = line.split("\t");
//			ArrayList<Double> list = new ArrayList<Double>();
//			for(int i = 1; i< arr.length; i++) {
//				list.add(Double.parseDouble(arr[i]));
//			} exp1.add(list);
//		}
//		
//		br.close();
//		
//		br = new BufferedReader(new FileReader(testDataFolder + "/Yeast-regulators-1/DBN/DBN1_dream4_timeseries2.tsv"));
//		br.readLine();
//		br.readLine();
//		ArrayList<ArrayList<Double>> exp2 = new ArrayList<ArrayList<Double>>();
//		while((line = br.readLine()) != null) {
//			String[] arr = line.split("\t");
//			ArrayList<Double> list = new ArrayList<Double>();
//			for(int i = 1; i< arr.length; i++) {
//				list.add(Double.parseDouble(arr[i]));
//			}
//			exp2.add(list);
//		}
//		
//		br.close();
	}
	
	/**
	 * euclidean distance, exp2 - exp1, the lower the better 
	 * column name of matrix is gene
	 * row name of matrix is time
	 * @param exp1
	 * @param exp2
	 */
	public static double sampleOverlap(ArrayList<ArrayList<Double>> exp1_in, ArrayList<ArrayList<Double>> exp2_in) {
		// exp1 and exp2 can have different lines, that is, different numbers of time points in the time series experiment
		int size1 = exp1_in.size();
		int size2 = exp2_in.size();
		ArrayList<ArrayList<Double>> exp1 = null;
		ArrayList<ArrayList<Double>> exp2 = null;
		if(size1 > size2) {
			exp1 = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< size2; i++) {
				exp1.add(exp1_in.get(i));
			}
			exp2 = exp2_in;
		} else {
			exp1 = exp1_in;
			exp2 = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< size1; i++) {
				exp2.add(exp2_in.get(i));
			}
		}
		
		ArrayList<ArrayList<Double>> INTER = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i< exp1.size(); i++) { // expr1.size() is the number of rows
			ArrayList<Double> list = new ArrayList<Double>();
			for(int j = 0; j< exp2.size(); j++) { // expr2.size() is the number of columns
				list.add((double)0);
			}
			INTER.add(list);
		}
		
		for(int i = 0; i< exp1.size(); i++) {
			for(int j = 0; j< exp2.size(); j++) {
				INTER.get(i).set(j, distance(exp1.get(i), exp2.get(j))); // ith row, jth column of the result matrix 
			}
		}
		
		ArrayList<Integer> arrindMinINTER = minCoord(INTER);
		int col = arrindMinINTER.get(1);
		ArrayList<Double> column = new ArrayList<Double>();
		for(ArrayList<Double> list : INTER) {
			column.add(list.get(col));
		}
		
		double avedist1 = mean(column);
		
		for(int i = 0; i< exp2.size(); i++) {
			for(int j = 0; j< exp1.size(); j++) {
				INTER.get(i).set(j, distance(exp2.get(i), exp1.get(j))); // ith row, jth column of the result matrix 
			}
		}
		
		arrindMinINTER = minCoord(INTER);
		col = arrindMinINTER.get(1);
		column = new ArrayList<Double>();
		for(ArrayList<Double> list : INTER) {
			column.add(list.get(col));
		}
		
		double avedist2= mean(column);
		
		return (avedist1 + avedist2)/2;
	}
	
	
	
	/**
	 * v2 - v1
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double distance(ArrayList<Double> v1, ArrayList<Double> v2) {
		double sum = 0;
		for(int i = 0; i< v1.size(); i++) {
			sum += Math.pow(v2.get(i) - v1.get(i), 2);
		}
		return Math.sqrt(sum);
	}
	
	/**
	 * coordinates of minimum value in the matrix
	 * @param matrix
	 * @return
	 */
    public static ArrayList<Integer> minCoord(ArrayList<ArrayList<Double>> matrix) {
        double min = matrix.get(0).get(0);
        int minCol = 0;
        int minRow = 0;
        for (int row = 0; row < matrix.size(); row++) {
            for (int col = 0; col < matrix.get(row).size(); col++) {
                if (min > matrix.get(row).get(col)) {
                    min = matrix.get(row).get(col);
                    minRow = row;
                    minCol = col;
                }
            }
        }
        ArrayList<Integer> coord = new ArrayList<Integer>();
        coord.add(minRow);
        coord.add(minCol);
        
        return coord;
    }
    
    public static double mean(ArrayList<Double> list) {
    	double sum = 0;
    	for(double e : list) {
    		sum += e;
    	}
    	return sum / list.size();
    }
  
    
    /**
     * integrative correlation, the higher the better 
     * @param exp1
     * @param exp2
     * @return
     */
    public static ArrayList<Double> cintCor(ArrayList<ArrayList<Double>> exp1_in, ArrayList<ArrayList<Double>> exp2_in) {
    	// exp1 and exp2 can have different lines, that is, different numbers of time points in the time series experiment
		int size1 = exp1_in.size();
		int size2 = exp2_in.size();
		ArrayList<ArrayList<Double>> exp1 = null;
		ArrayList<ArrayList<Double>> exp2 = null;
		if(size1 > size2) {
			exp1 = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< size2; i++) {
				exp1.add(exp1_in.get(i));
			}
			exp2 = exp2_in;
		} else {
			exp1 = exp1_in;
			exp2 = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< size1; i++) {
				exp2.add(exp2_in.get(i));
			}
		}
    	
		ArrayList<Double> resultList = new ArrayList<Double>();
    	for(int i = 0; i< exp1.size(); i++) {
    		resultList.add(intCor(exp1, exp2, i));
    	}
    	return resultList;
    }
    
    public static double intCor(ArrayList<ArrayList<Double>> exp1, ArrayList<ArrayList<Double>> exp2, int ix) {
    	
    	ArrayList<ArrayList<Double>> A = new ArrayList<ArrayList<Double>>();
    	for(int i = 0; i< exp1.size(); i++) {
    		if(i != ix) {
    			A.add(exp1.get(i));
    		}
    	}
    	
    	ArrayList<Double> xa = exp1.get(ix);
    	
    	double[][] AArray = new double[A.size()][A.get(0).size()];
    	for(int i = 0; i< A.size(); i++) {
    		for(int j = 0; j< A.get(0).size(); j++) {
    			AArray[i][j] = A.get(i).get(j);
    		}
    	}
    	double[] xaArray = new double[xa.size()];
    	for(int i = 0; i< xa.size(); i++) {
    		xaArray[i] = xa.get(i);
    	}
    	
    	Matrix AMatrix = new Matrix(AArray);
    	Matrix xaMatrix = new Matrix(xaArray, 1);
    	
    	ArrayList<ArrayList<Double>> B = new ArrayList<ArrayList<Double>>();
    	for(int i = 0; i< exp2.size(); i++) {
    		if(i != ix) {
    			B.add(exp2.get(i));
    		}
    	}
    	
    	ArrayList<Double> xb = exp2.get(ix);
    	
    	double[][] BArray = new double[B.size()][B.get(0).size()];
    	for(int i = 0; i< B.size(); i++) {
    		for(int j = 0; j< B.get(0).size(); j++) {
    			BArray[i][j] = B.get(i).get(j);
    		}
    	}
    	double[] xbArray = new double[xb.size()];
    	for(int i = 0; i< xb.size(); i++) {
    		xbArray[i] = xb.get(i);
    	}
    	
    	Matrix BMatrix = new Matrix(BArray);
    	Matrix xbMatrix = new Matrix(xbArray, 1);
   
    	// after here, all are same with R code
    	double first = xaMatrix.times(AMatrix.transpose()).times(Matrix.identity(exp1.size() - 1, exp1.size() - 1)).times(BMatrix).times(xbMatrix.transpose()).get(0, 0);
    	double second = xaMatrix.times(AMatrix.transpose()).times(Matrix.identity(exp1.size() - 1, exp1.size() - 1)).times(AMatrix).times(xaMatrix.transpose()).get(0, 0);
    	double third = xbMatrix.times(BMatrix.transpose()).times(Matrix.identity(exp1.size() - 1, exp1.size() - 1)).times(BMatrix).times(xbMatrix.transpose()).get(0, 0);
    	
    	return first / (Math.sqrt(second) * Math.sqrt(third));
    }
   
   /** 
    * correlation of correlation, the higher the better
    * @param exp1
    * @param exp2
    * @return
    */
    public static double corCor(ArrayList<ArrayList<Double>> exp1_in, ArrayList<ArrayList<Double>> exp2_in) {
    	// exp1 and exp2 can have different lines, that is, different numbers of time points in the time series experiment
		int size1 = exp1_in.size();
		int size2 = exp2_in.size();
		ArrayList<ArrayList<Double>> exp1 = null;
		ArrayList<ArrayList<Double>> exp2 = null;
		if(size1 > size2) {
			exp1 = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< size2; i++) {
				exp1.add(exp1_in.get(i));
			}
			exp2 = exp2_in;
		} else {
			exp1 = exp1_in;
			exp2 = new ArrayList<ArrayList<Double>>();
			for(int i = 0; i< size1; i++) {
				exp2.add(exp2_in.get(i));
			}
		}
    	
    	ArrayList<Double> cor1List = new ArrayList<Double>();
    	ArrayList<Double> cor2List = new ArrayList<Double>();
    	PearsonsCorrelation pearsonCor = new PearsonsCorrelation();
    	for(int i1 = 0; i1< (exp1.size() - 1); i1++) {
    		for(int i2 = (i1 + 1); i2 < exp1.size(); i2++) {
    			ArrayList<Double> list1 = exp1.get(i1);
    			ArrayList<Double> list2 = exp1.get(i2);
    			ArrayList<Double> list3 = exp2.get(i1);
    			ArrayList<Double> list4 = exp2.get(i2);
    			
    			double[] arr1 = Doubles.toArray(list1); 
    			double[] arr2 = Doubles.toArray(list2);
    			double[] arr3 = Doubles.toArray(list3);
    			double[] arr4 = Doubles.toArray(list4);
    			
    			double cor1 = pearsonCor.correlation(arr1, arr2);
    			double cor2 = pearsonCor.correlation(arr3, arr4);
    			
    			cor1List.add(cor1);
    			cor2List.add(cor2);
    		}
    	}
    	
    	double[] cor1Arr = Doubles.toArray(cor1List);
    	double[] cor2Arr = Doubles.toArray(cor2List);
    	
    	double corcor = pearsonCor.correlation(cor1Arr, cor2Arr);
    	return corcor;
    }
    
    public static void FScore() {
    	
    }
}