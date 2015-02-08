package birc.grni.rf;
import java.util.*;
import java.util.regex.*;

public class Genie3PredictYTime {
	public static float[][] genie3PredictYTime(Genie3PredictYTimeParam params) {
		ArrayList<ArrayList<Double>> exprMatrix = params.getExprMatrix();
		int nbGenes = exprMatrix.get(0).size();					/* number of genes*/
		int nbSamples = exprMatrix.size();						/* number of time points*/
		ArrayList<Integer> testIdx = params.getTestIdx();		/* test set indexes list*/
		int nbTestIdx = testIdx.size();
		int outputIdx = params.getOutputIdx();					/* target gene*/
		
		Pattern pNumber = Pattern.compile("(\\d+(\\.\\d+)?)|(\\.\\d+)");
		Matcher mNumber = null;
		
		/* convert input data matrix into single precision*/
		ArrayList<ArrayList<Float>> singleExprMatrix = Util.doubleToSingleMatrix(exprMatrix);
		
		/* default: all genes are putative regulators*/
		ArrayList<Integer> inputIdxDef = Util.range(1, nbGenes);
		ArrayList<Integer> inputIdx = new ArrayList<Integer>(inputIdxDef);	/*deep copy*/
		inputIdx.remove(new Integer(outputIdx));
		int nbInputs = inputIdx.size();
		
		int timeSamples = nbSamples - 1;
		
		/* initialize all elements in expressionTime to 0*/
		float[][] expressionTime = new float[timeSamples][nbGenes];
		for(int i = 0; i< timeSamples; i++)
			for(int j = 0; j< nbGenes; j++)
				expressionTime[i][j] = 0;
				
		for(int i = 0; i< timeSamples; i++)
			expressionTime[i][outputIdx-1] = singleExprMatrix.get(i+1).get(outputIdx-1);
		
		for(int i = 0; i< nbInputs; i++) {
			int idx = inputIdx.get(i);
			for(int j = 0; j< timeSamples; j++)
				expressionTime[j][idx-1] = singleExprMatrix.get(j).get(idx-1);
		}
		
		/* separate test data set*/
		double[][] expressionTimeTest = new double[timeSamples][];
		for(int i = 0; i< timeSamples; i++) {
			double[] oneRow = new double[nbGenes];
			int k = 0;	/* index of testIdx list*/
			for(int j = 0; j< nbGenes; j++) {
				if(k< testIdx.size() && j == (testIdx.get(k)-1)) {
					oneRow[j] = expressionTime[i][j];
					k++;
				}
				else
					oneRow[j] = 0;
			}
			expressionTimeTest[i] = oneRow;
		}
		
		double[][] finalTestMatrix = new double[timeSamples][];
		for(int i = 0; i< timeSamples; i++) {
			double[] oneRow = new double[nbGenes];
			int k = 0;	/* index of testIdx list*/
			for(int j = 0; j< nbGenes; j++) {
				if(k< inputIdx.size() && j == (inputIdx.get(k)-1)) {
					oneRow[j] = expressionTime[i][j];
					k++;
				}
				else
					oneRow[j] = 0;
			}
			finalTestMatrix[i] = oneRow;
		}
		
		float[][] YPredict = RtEnsPred.rtEnsPred(params.getModel(), finalTestMatrix);
		return YPredict;
	}
}