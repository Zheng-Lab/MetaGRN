package birc.grni.util;

import java.util.*;

public class Statistic {
	
	/*---------------------------------mean--------------------------------*/
	
	/* array version*/
	
	public static float mean(float[] inputVector) {
		float sum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			sum += inputVector[i];
		}
		return sum/inputVector.length;
	}
	
	public static double mean(double[] inputVector) {
		double sum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			sum += inputVector[i];
		}
		return sum/inputVector.length;
	}
	
	/* ArrayList version*/
	
	public static float mean(ArrayList<Float> inputVector) {
		float sum = 0;
		for(Float n : inputVector) {
			sum += n.floatValue();
		}
		return sum/inputVector.size();
	}
	
	public static double meanDoubleArrayList(ArrayList<Double> vector)
	{
		double sum = 0;
		for(Double e : vector)
			sum += e.doubleValue();
		return sum/vector.size();
	}
	
	/*---------------------------------var------------------------------*/
	
	/* array version*/
	public static float unbiasedVar(float[] inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		float var = (float)squareSum/(inputVector.length - 1);
		return var;
	}
	
	public static double unbiasedVar(double[] inputVector) {
		double mean = mean(inputVector);
		double squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		double var = squareSum/(inputVector.length - 1);
		return var;
	}
	
	public static float biasedVar(float[] inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		float var = (float)squareSum/inputVector.length;
		return var;
	}
	
	public static double biasedVar(double[] inputVector) {
		double mean = mean(inputVector);
		double squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		double var = squareSum/inputVector.length;
		return var;
	}
	
	/* ArrayList version*/
	
	public static float unbiasedVar(ArrayList<Float> inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(Float n : inputVector) {
			squareSum += (n.floatValue() - mean) * (n.floatValue() - mean);
		}
		float var = squareSum/(inputVector.size() -1); /* square deviation*/
		return var;
	}
	
	public static double unbiasedVarDoubleArrayList(ArrayList<Double> inputVector) {
		double mean = meanDoubleArrayList(inputVector);
		double squareSum = 0;
		for(Double n : inputVector) {
			squareSum += (n.doubleValue() - mean) * (n.doubleValue() - mean);
		}
		double var = squareSum/(inputVector.size() -1); /* square deviation*/
		return var;
	}
	
	public static float biasedVar(ArrayList<Float> inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(Float n : inputVector) {
			squareSum += (n.floatValue() - mean) * (n.floatValue() - mean);
		}
		float var = squareSum/inputVector.size(); /* square deviation*/
		return var;
	}
	
	public static double biasedVarDoubleArrayList(ArrayList<Double> inputVector) {
		double mean = meanDoubleArrayList(inputVector);
		double squareSum = 0;
		for(Double n : inputVector) {
			squareSum += (n.doubleValue() - mean) * (n.doubleValue() - mean);
		}
		double var = squareSum/inputVector.size(); /* square deviation*/
		return var;
	}
	
	/*------------------------------std----------------------------*/
	
	/* array version*/
	public static float unbiasedStd(float[] inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		float sd = squareSum/(inputVector.length - 1);
		return (float)Math.sqrt(sd);
	}
	
	public static double unbiasedStd(double[] inputVector) {
		double mean = mean(inputVector);
		double squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		double sd = squareSum/(inputVector.length - 1);
		return Math.sqrt(sd);
	}
	
	public static float biasedStd(float[] inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		float sd = squareSum/inputVector.length ;
		return (float)Math.sqrt(sd);
	}
	
	public static double biasedStd(double[] inputVector) {
		double mean = mean(inputVector);
		double squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		double sd = squareSum/inputVector.length ;
		return Math.sqrt(sd);
	}
	
	/* ArrayList version*/

	public static float unbiasedStd(ArrayList<Float> inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(Float n : inputVector) {
			squareSum += (n.floatValue() - mean) * (n.floatValue() - mean);
		}
		float sd = squareSum/(inputVector.size() -1); /* square deviation*/
		return (float)Math.sqrt(sd);
	}
	
	public static double unbiasedStdDoubleArrayList(ArrayList<Double> inputVector) {
		double mean = meanDoubleArrayList(inputVector);
		double squareSum = 0;
		for(Double n : inputVector) {
			squareSum += (n.doubleValue() - mean) * (n.doubleValue() - mean);
		}
		double sd = squareSum/(inputVector.size() - 1); /* square deviation*/
		return Math.sqrt(sd);
	}
	
	public static float biasedStd(ArrayList<Float> inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(Float n : inputVector) {
			squareSum += (n.floatValue() - mean) * (n.floatValue() - mean);
		}
		float sd = squareSum/inputVector.size(); /* square deviation*/
		return (float)Math.sqrt(sd);
	}
	
	public static double biasedStdDoubleArrayList(ArrayList<Double> inputVector) {
		double mean = meanDoubleArrayList(inputVector);
		double squareSum = 0;
		for(Double n : inputVector) {
			squareSum += (n.doubleValue() - mean) * (n.doubleValue() - mean);
		}
		double sd = squareSum/inputVector.size(); /* square deviation*/
		return Math.sqrt(sd);
	}
}