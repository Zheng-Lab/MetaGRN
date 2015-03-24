package birc.grni.rf;
import java.util.*;
/**
 * statistic functions
 * @author liu xingliang
 *
 */
public class  Statistic{
	/**
	 * mean
	 * @param inputVector
	 * @return
	 */
	public static float mean(ArrayList<Float> inputVector) {
		float sum = 0;
		for(Float n : inputVector) {
			sum += n.floatValue();
		}
		return sum/inputVector.size();
	}
	
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
	
	/**
	 * normalized by sample size, biased
	 * @param inputVector
	 * @return
	 */
	public static float biasedStd(ArrayList<Float> inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(Float n : inputVector) {
			squareSum += (n.floatValue() - mean) * (n.floatValue() - mean);
		}
		float sd = squareSum/inputVector.size(); /* square deviation*/
		return (float)Math.sqrt(sd);
	}
	
	public static float biasedStd(float[] inputVector) {
		float mean = mean(inputVector);
		float squareSum = 0;
		for(int i = 0; i< inputVector.length; i++) {
			squareSum += (inputVector[i] - mean) * (inputVector[i] - mean);
		}
		float sd = squareSum/inputVector.length;
		return (float)Math.sqrt(sd);
	}
	
	/**
	 * normalized by sample size - 1, unbiased
	 * @param inputVector
	 * @return
	 */
//	public static float unbiasedStd(ArrayList<Float> inputVector) {
//		float mean = mean(inputVector);
//		float squareSum = 0;
//		for(Float n : inputVector) {
//			squareSum += Math.pow((n.floatValue() - mean), 2);
//		}
//		float sd = squareSum/(inputVector.size() -1); /* square deviation*/
//		return (float)Math.sqrt(sd);
//	}
}
