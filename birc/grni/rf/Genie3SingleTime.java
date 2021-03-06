package birc.grni.rf;

import java.util.*;
import java.util.logging.*;
import java.util.regex.*;
import org.apache.commons.lang3.*;

public class Genie3SingleTime {	
	
	private static Logger logger = Logger.getLogger(Genie3SingleTime.class.getName());
	
	public static Genie3SingleTimeResult genie3SingleTime(Genie3SingleTimeParam params) {
		double vi[];											/* final result*/
		ArrayList<ArrayList<Double>> exprMatrix = params.getExprMatrix();
		int nbGenes = exprMatrix.get(0).size();					/* number of genes*/
		int nbSamples = exprMatrix.size();						/* number of time points*/
		int nargin = params.getNargin();						/* number of input parameters*/
		int outputIdx = params.getOutputIdx();					/* target gene index*/
		ArrayList<Integer> inputIdx = params.getInputIdx();		/* we only use genes whose indexes in this list as input genes*/
		String treeMethod = params.getTreeMethod();				/* tree procedure we used: RF(Random Forests) or ET(Extra Trees)*/
		String K = params.getK();								/* number K of randomly selected attributes at each node of one tree*/
		int nbTrees = params.getNbTrees();						/* number of trees grown in the ensemble. Default value: 1000*/
		
		/* check input*/
		if(nargin < 2 || nargin > 6) {
			System.out.println("\nThe number of input parameters should >=2 and <=6!");
			System.exit(1);
		}
		if(outputIdx > nbGenes || outputIdx < 1) {
			System.out.println("\nInput argument output_idx must be an integer between 1 and p, where p is the number of genes in expr_matrix.");
			System.exit(1);
		}
		if(nargin > 2 && !Util.range(1, nbGenes).containsAll(inputIdx)) {
			System.out.println("\nInput argument input_idx must be a vector containing integers between 1 and p, where p is the number of genes in expr_matrix.");
			System.exit(1);
		}
		if(nargin > 3 && !treeMethod.equalsIgnoreCase("RF") && !treeMethod.equalsIgnoreCase("ET")) {
			System.out.println("\nInput argument tree_method must be \"RF\" or \"ET\".");
			System.exit(1);
		}
		
		Pattern pNumber = Pattern.compile("(\\d+(\\.\\d+)?)|(\\.\\d+)");
		Matcher mNumber = null;
		if(nargin > 4) {
			mNumber = pNumber.matcher(K);
			if(!mNumber.matches() && !K.equalsIgnoreCase("sqrt") && !K.equals("all")) {
				System.out.println("\nInput argument K must be \"sqrt\", \"all\" or a numerical value.");
				System.exit(1);
			}
		}
		
		if(nargin > 5 && nbTrees <= 0) {
			System.out.println("\nInput argument nbTrees (the number of trees) must be an positive integer.");
			System.exit(1);
		}
		
		/* convert input data matrix into single precision*/
		ArrayList<ArrayList<Float>> singleExprMatrix = Util.doubleToSingleMatrix(exprMatrix);
		/* unique inputIdx list*/
		if(params.getNargin() >= 3) {
			HashSet<Integer> uniqueSet = new HashSet<Integer>(inputIdx);
			inputIdx = new ArrayList<Integer>(uniqueSet);
			Collections.sort(inputIdx);
		} else {
			inputIdx = Util.range(1, nbGenes);
		}
		
		/* make sure the outputIdx is not in inputIdx list*/
		inputIdx.remove(new Integer(outputIdx));
		/* get column of input matrix after removing outputIdx*/
		int nbInputs = inputIdx.size();
		
		/* change of expression values to evaluate the time series data
		 * weight of gi ! gj is the importance of expression of gi at time
		 * t for the prediction of expression of gj at time t+1*/
		int timeSamples = nbSamples-1;
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
		
		//float[][] singleExpressionTime = Util.doubleToSingleMatrix(expressionTime, timeSamples, nbGenes);
		
		/* output vector*/
		System.out.printf("\nTarget gene %d...\n", outputIdx);
		float[] output = new float[timeSamples];
		for(int i = 0; i< timeSamples; i++)
			output[i] = expressionTime[i][outputIdx-1];
		/* normalize*/
		float[] outputNorm = new float[timeSamples];
		float mean = Statistic.mean(output);
		float biasedStd = Statistic.biasedStd(output);
		for(int i = 0; i< timeSamples; i++)
			outputNorm[i] = (output[i] - mean)/biasedStd;
		
		/* tree parameters*/
		
		/* Default parameters: Random Forests, K=sqrt(number of input genes),
		 * 1000 trees in the ensemble*/
		RtEnsParam ok3EnsParam = new RtEnsParam();
		String printNTrees = "";
		String printMethod = "";
		String printK = "";
		
		if(nargin < 4 || (nargin >=4 && treeMethod.equalsIgnoreCase("RF"))) {
			ok3EnsParam = initRf();
			printMethod = "Random Forests";
			printK = Math.round(Math.sqrt(nbInputs))+"";
			if (nargin >= 5) {
				if (K.equalsIgnoreCase("all")) {
					ok3EnsParam = initRf(nbInputs);
					printK = nbInputs+"";
				} else if ((mNumber = pNumber.matcher(K)).matches()) {
					if(mNumber.group(2) == null && mNumber.group(3) == null) /* integer*/ {
						ok3EnsParam = initRf(Integer.parseInt(K));
						printK = K;
					} else {
						ok3EnsParam = initRf(Math.round(Double.parseDouble(K)));
						printK = K;
					}
				}
			}
		} else if (nargin >=4 && treeMethod.equalsIgnoreCase("ET")) {
			ok3EnsParam = initExtraTrees();
			printMethod = "Extra Trees";
			printK = Math.round(Math.sqrt(nbInputs))+"";
			if (nargin >= 5) {
				if (K.equalsIgnoreCase("all")) {
					ok3EnsParam = initExtraTrees(nbInputs);
					printK = nbInputs+"";
				} else if ((mNumber = pNumber.matcher(K)).matches()) {
					if(mNumber.group(2) == null && mNumber.group(3) == null) /* integer*/ {
						ok3EnsParam = initExtraTrees(Integer.parseInt(K));
						printK = K;
					} else {
						ok3EnsParam = initExtraTrees(Math.round(Double.parseDouble(K)));
						printK = K;
					}
				}
			}
		}
		
		/* Number of trees in the ensemble*/
		if(nargin < 6) {
			ok3EnsParam.setNbTerms(1000);
			printNTrees = 1000+"";
		} else {
			ok3EnsParam.setNbTerms(nbTrees);
			printNTrees = nbTrees+"";
		}
		
		System.out.printf("Tree method = %s, K = %s, %s trees\n", printMethod, printK, printNTrees);
		System.out.printf("\n");
		
		/* get new input matrix according to inputIdx, this matrix is a single precision matrix, 
		 * and only contains columns of expression time matrix whose indexes are in inputIdx list 
		 */
		float[][] newSingleExpressionTime = new float[timeSamples][nbInputs];
		for(int i = 0; i< timeSamples; i++) {
			float[] oneRow = new float[nbInputs];
			for(int j = 0; j< inputIdx.size(); j++) {
				int k = inputIdx.get(j) - 1; /* input indexes are from 1*/
				oneRow[j] = singleExprMatrix.get(i).get(k);
			}
			newSingleExpressionTime[i] = oneRow;
		}
		
		/* put the input matrix into a single dimension array in "column major"(like original mex function does) way, 
		 * for the invocation of the native method*/
		float[] singleDimInput;
		singleDimInput = new float[timeSamples * nbInputs];	
		for(int j = 0; j< nbInputs; j++)
			for(int i = 0; i< timeSamples; i++)
				singleDimInput[j * timeSamples + i] = newSingleExpressionTime[i][j];
			
		/* lsData: the array whose elements are 1, 2, ...,number of samples*/
		int[] lsData = new int[timeSamples];
		for(int i = 0; i< timeSamples; i++)	
			lsData[i] = i+1;
		
		Plhs_Genie3SingleTime_Rtenslearncless plhs = new Plhs_Genie3SingleTime_Rtenslearncless();
			
		rtenslearncless(timeSamples, nbInputs, singleDimInput, 1 /* normalOutput is a vertical vector*/, outputNorm, timeSamples, lsData, 0, new double[0], ok3EnsParam, 0, plhs);
				
		vi = new double[nbGenes];								 /* vi should always be nbGenes length, the value in the location of target gene, as well as those which are not chosen as input genes should be 0*/
		for(int i = 0; i<nbGenes; i++)			
			vi[i] = 0;
		
		for(int i = 0; i< inputIdx.size(); i++)
			vi[inputIdx.get(i)-1] = plhs.getVarimp()[i][0];
		
		for(int i = 0; i< vi.length; i++)
			vi[i] = vi[i]/timeSamples;
		
		Genie3SingleTimeResult result = new Genie3SingleTimeResult(vi, plhs.getTreeEnsemble());
		
		return result;
	}
	
	/**
	 * initialize random forests parameters
	 */
	public static RtEnsParam initRf() {
		RtParam rtParam = new RtParam();
		rtParam.setnMin(2);
		rtParam.setVarMin(0);
		rtParam.setSavePred(1);
		rtParam.setBestFirst(0);
		rtParam.setRf(1);
		rtParam.setExtraTrees(0);
		rtParam.setAdjustDefaultK(1);
		
		RtEnsParam rtEnsParam = new RtEnsParam();
		rtEnsParam.setNbTerms(100);
		rtEnsParam.setBootStrap(1);
		rtEnsParam.setRtParam(rtParam);
		
		return rtEnsParam;
	}
	
	/**
	 * initialize random forests parameters
	 */
	public static RtEnsParam initRf(long k) {
		RtParam rtParam = new RtParam();
		rtParam.setnMin(2);
		rtParam.setVarMin(0);
		rtParam.setSavePred(1);
		rtParam.setBestFirst(0);
		rtParam.setRf(1);
		rtParam.setExtraTrees(0);
		rtParam.setAdjustDefaultK(0);
		rtParam.setExtraTreesK(k);
		
		RtEnsParam rtEnsParam = new RtEnsParam();
		rtEnsParam.setNbTerms(100);
		rtEnsParam.setBootStrap(1);
		rtEnsParam.setRtParam(rtParam);
		
		return rtEnsParam;
	}
	
	/**
	 * initialize extra trees parameters
	 */
	public static RtEnsParam initExtraTrees() {
		RtParam rtParam = new RtParam();
		rtParam.setnMin(1);
		rtParam.setVarMin(0);
		rtParam.setSavePred(1);
		rtParam.setBestFirst(0);
		rtParam.setExtraTrees(1);
		rtParam.setAdjustDefaultK(1);
		
		RtEnsParam rtEnsParam = new RtEnsParam();
		rtEnsParam.setNbTerms(100);
		rtEnsParam.setBootStrap(0);
		rtEnsParam.setRtParam(rtParam);
		
		return rtEnsParam;
	}
	
	/**
	 * initialize extra trees parameters
	 */
	public static RtEnsParam initExtraTrees(long k) {
		RtParam rtParam = new RtParam();
		rtParam.setnMin(1);
		rtParam.setVarMin(0);
		rtParam.setSavePred(1);
		rtParam.setBestFirst(0);
		rtParam.setExtraTrees(1);
		rtParam.setAdjustDefaultK(0);
		rtParam.setExtraTreesK(k);
		
		RtEnsParam rtEnsParam = new RtEnsParam();
		rtEnsParam.setNbTerms(100);
		rtEnsParam.setBootStrap(0);
		rtEnsParam.setRtParam(rtParam);
		
		return rtEnsParam;
	}
	
	/**
	 * 
	 * @param xDataRow
	 * @param xDataColumn
	 * @param xDataStore
	 * @param yDataColumn
	 * @param yData
	 * @param lsDataColumn
	 * @param lsData
	 * @param wDataColumn
	 * @param wData
	 * @param treeparamData
	 * @param verbose
	 * @param plhs parameters, left-hand side, results of native methods actually 
	 */
	public native static void rtenslearncless(
			int xDataRow, int xDataColumn, float[] xDataStore, 
			int yDataLength, float[] yData, 
			int lsDataLength, int[] lsData, 
			int wDataLength, double[] wData, 
			RtEnsParam treeparamData, int verbose, Plhs_Genie3SingleTime_Rtenslearncless plhs);

	static
	{	
		String archOs = System.getProperty("os.arch");
		
		if(archOs.contains("64"))
		{
			if(SystemUtils.IS_OS_WINDOWS) {
				/*For Windows64*/
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "windows64" + "/" + "birc_grni_rf_Genie3SingleTime.dll");
			} else if(SystemUtils.IS_OS_LINUX) {
				/*For Linux64*/
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "linux64" + "/" + "libbirc_grni_rf_Genie3SingleTime.so");
			} else if(SystemUtils.IS_OS_MAC_OSX) {
				/*For Mac OS X 64*/
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "osx64" + "/" + "libbirc_grni_rf_Genie3SingleTime.jnilib");
			}
		}
		else
		{
			if(SystemUtils.IS_OS_WINDOWS) {
				/*For Windows32*/
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "windows32" + "/" + "birc_grni_rf_Genie3SingleTime.dll");
			} else if(SystemUtils.IS_OS_LINUX) {
				/*For Linux32*/
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "linux32" + "/" + "libbirc_grni_rf_Genie3SingleTime.so");
			} 
		}
	}
}