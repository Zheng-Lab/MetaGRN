//package birc.grni.rf;
//import java.awt.FileDialog;
//import java.io.*;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.regex.*;
//
//import javax.swing.*;
//
//import birc.grni.gui.GrnRf;
//import birc.grni.gui.GrnRfDisplay;
//import birc.grni.gui.visulization.GrnVisulizeNetwork;
//
////TEST
//public class Genie3Time extends SwingWorker<Void, Void>{
////public class Genie3Time {
//	
//	//TEST
//	private Genie3TimeParam params;
//	
//	//TEST
//	private double[][] VIM;	/* the final result*/
//	
//	//TEST
////	public static double[][] run(String inputFilePath, String treeMethod, int nbTrees) throws IOException, FileNotFoundException
////	{
////		/* get input experiment data matrix from file*/
////		ArrayList<ArrayList<Double>> exprMatrix = new ArrayList<ArrayList<Double>>();
////		BufferedReader brInputFile = new BufferedReader(new FileReader(inputFilePath));
////		String oneLine = "";
////		while ((oneLine = brInputFile.readLine()) != null) {
////			ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
////			Scanner sc = new Scanner(oneLine);
////			while(sc.hasNext()) {
////				oneLineArrayList.add(sc.nextDouble());
////			}
////			exprMatrix.add(oneLineArrayList);
////			sc.close();
////		}
////		brInputFile.close();
////		
////		/* all genes as input*/
////		ArrayList<Integer> inputIdx = new ArrayList<Integer>();
////		int nbGenes = exprMatrix.get(0).size();
////		for(int i = 1; i<= nbGenes; i++)
////			inputIdx.add(i);
////		
////		/* construct parameter structure for gene3time algorithm*/
////		Genie3TimeParam params = new Genie3TimeParam(exprMatrix, inputIdx, treeMethod, "sqrt"/* use the default value, don't change*/, nbTrees);
////		
////		/* result*/
////		double[][] VIM = Genie3Time.genie3Time(params);
////		
////		return VIM;
////	}
//	
//	public void initLogic(String inputFilePath, String treeMethod, int nbTrees) throws IOException, FileNotFoundException
//	{
//		/* get input experiment data matrix from file*/
//		ArrayList<ArrayList<Double>> exprMatrix = new ArrayList<ArrayList<Double>>();
//		BufferedReader brInputFile = new BufferedReader(new FileReader(inputFilePath));
//		String oneLine = "";
//		while ((oneLine = brInputFile.readLine()) != null) {
//			ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
//			Scanner sc = new Scanner(oneLine);
//			while(sc.hasNext()) {
//				oneLineArrayList.add(sc.nextDouble());
//			}
//			exprMatrix.add(oneLineArrayList);
//			sc.close();
//		}
//		brInputFile.close();
//		
//		/* all genes as input*/
//		ArrayList<Integer> inputIdx = new ArrayList<Integer>();
//		int nbGenes = exprMatrix.get(0).size();
//		for(int i = 1; i<= nbGenes; i++)
//			inputIdx.add(i);
//		
//		/* construct parameter structure for gene3time algorithm*/
//		this.params = new Genie3TimeParam(exprMatrix, inputIdx, treeMethod, "sqrt"/* use the default value, don't change*/, nbTrees);
//	}
//	
//	//TEST
//	public void init(String inputFilePath, String treeMethod, int nbTrees) throws IOException, FileNotFoundException
//	{
////		/* get input experiment data matrix from file*/
////		ArrayList<ArrayList<Double>> exprMatrix = new ArrayList<ArrayList<Double>>();
////		BufferedReader brInputFile = new BufferedReader(new FileReader(inputFilePath));
////		String oneLine = "";
////		while ((oneLine = brInputFile.readLine()) != null) {
////			ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
////			Scanner sc = new Scanner(oneLine);
////			while(sc.hasNext()) {
////				oneLineArrayList.add(sc.nextDouble());
////			}
////			exprMatrix.add(oneLineArrayList);
////			sc.close();
////		}
////		brInputFile.close();
////		
////		/* all genes as input*/
////		ArrayList<Integer> inputIdx = new ArrayList<Integer>();
////		int nbGenes = exprMatrix.get(0).size();
////		for(int i = 1; i<= nbGenes; i++)
////			inputIdx.add(i);
////		
////		/* construct parameter structure for gene3time algorithm*/
////		this.params = new Genie3TimeParam(exprMatrix, inputIdx, treeMethod, "sqrt"/* use the default value, don't change*/, nbTrees);
//		
//		/* initial progress bar*/
//		GrnRfDisplay.progressBarRf.setMaximum(exprMatrix.get(0).size());	/* the maximum value is the total number of genes*/
//	}
//	
//	//TEST
////	public static double[][] genie3Time(Genie3TimeParam params) {
////		int nargin = params.getNargin();
////		ArrayList<ArrayList<Double>> exprMatrix = params.getExprMatrix();
////		int nbGenes = exprMatrix.get(0).size();					/* number of genes*/
////		int numTimePoints = exprMatrix.size();					/* number of time points*/
////		ArrayList<Integer> inputIdx = params.getInputIdx();		/* we only use genes whose indexes in this list as input genes*/
////		String treeMethod = params.getTreeMethod();				/* tree procedure we used: RF(Random Forests) or ET(Extra Trees)*/
////		String K = params.getK();								/* number K of randomly selected attributes at each node of one tree*/
////		int nbTrees = params.getNbTrees();						/* number of trees grown in the ensemble. Default value: 1000*/
////		
////		double[][] VIM = new double[nbGenes][nbGenes];			/* result matrix*/
////		
////		/* check input*/
////		//TODO: 
////		if(nargin > 5 || nargin < 1) {
////			System.out.println("The number of input parameters should >=1 and <=5!");
////			System.exit(1);
////		}
////		if(nargin > 1 && !Util.range(1, nbGenes).containsAll(inputIdx))
////			System.out.println("Input argument input_idx must be a vector containing integers between 1 and p, where p is the number of genes in expr_matrix.");
////		if(nargin > 2 && !treeMethod.equalsIgnoreCase("RF") && !treeMethod.equalsIgnoreCase("ET"))
////			System.out.println("Input argument tree_method must be \"RF\" or \"ET\".");
////		
////		Pattern pNumber = Pattern.compile("(\\d+(\\.\\d+)?)|(\\.\\d+)");
////		Matcher mNumber = null;
////		if(nargin > 3) {
////			mNumber = pNumber.matcher(K);
////			if(!mNumber.matches() && !K.equalsIgnoreCase("sqrt") && !K.equals("all"))
////				System.out.println("Input argument K must be \"sqrt\", \"all\" or a numerical value.");
////		}
////		
////		for(int i = 1; i<= nbGenes; i++) {	
////			Genie3SingleTimeParam singleParams = null;
////			if(nargin == 1) {
////				singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), i);
////			} else if(nargin == 2) {
////				singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), i, params.getInputIdx());
////			} else if(nargin == 3) {
////				singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), i, params.getInputIdx(), params.getTreeMethod());
////			} else if(nargin == 4) {
////				singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), i, params.getInputIdx(), params.getTreeMethod(), params.getK());
////			} else if(nargin == 5) {
////				singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), i, params.getInputIdx(), params.getTreeMethod(), params.getK(), params.getNbTrees());
////			}
////			
////			Genie3SingleTimeResult result = Genie3SingleTime.genie3SingleTime(singleParams);
////			
////			double[] vi = result.getVi();
////			double[][] viWithPosition = new double[vi.length][2];
////			for(int j = 0; j< viWithPosition.length; j++) {
////				viWithPosition[j][0] = vi[j];
////				viWithPosition[j][1] = j+1;
////			}
////			
////			double[][] sortedViWithPosition = mySortDescend(viWithPosition);		/*descend*/
////			
////			int[] order = new int[sortedViWithPosition.length];
////			for(int j = 0; j< sortedViWithPosition.length; j++)
////				order[j] = (int)sortedViWithPosition[j][1];
////			
////			double stopPoint = Math.floor(Math.sqrt(nbGenes));
////			double[] outputVector = new double[numTimePoints-1];
////			for(int j = 1; j<= numTimePoints-1; j++) {
////				outputVector[j-1] = exprMatrix.get(j).get(i-1);
////			}
////			
////			double yMean = Statistic.mean(outputVector);
////			double currentARSquare = Double.NEGATIVE_INFINITY;
////			double ssTotal = 0;
////			for(int j = 1; j<= numTimePoints-1; j++)
////				ssTotal += Math.pow(outputVector[j-1] - yMean, 2);
////			
////			int numInputGenes = 1;
////			
////			ArrayList<Integer> defaultIdx = new ArrayList<Integer>();
////			defaultIdx.add((int)sortedViWithPosition[0][1]);
////			ArrayList<Integer> removeIdx = new ArrayList<Integer>();
////			removeIdx.add(0);
////			int removeTag = 1;
////			int orderTag = 1;
////			while(numInputGenes <= stopPoint) {
////				ArrayList<Integer> testIdx = new ArrayList<Integer>(defaultIdx);	/* deep copy of defaultIdx*/
////				testIdx.removeAll(removeIdx);
////				HashSet<Integer> testIdxSet = new HashSet<Integer>(testIdx);
////				testIdx = new ArrayList<Integer>(testIdxSet); /* no repetitions*/
////				Collections.sort(testIdx);	/* sorted*/
////				
////				Genie3PredictYTimeParam paramsPredictYTime = new Genie3PredictYTimeParam(exprMatrix, i, testIdx, result.getTreeEnsemble());
////				float[][] YHat = Genie3PredictYTime.genie3PredictYTime(paramsPredictYTime);
////				double ssError = 0;
////				for(int k = 1; k <= numTimePoints-1; k++)
////				{					
////					ssError += Math.pow(outputVector[k-1] - YHat[k-1][0], 2);
////				}
////				
////				double RSquare = 1 - (ssError/ssTotal);
////				int numRegressors = testIdx.size();
////				
////				/* find adjusted r-square*/
////				double ARSquare = (RSquare - (1 - RSquare)*(numRegressors/(numTimePoints - numRegressors - 1)));
////			
////				orderTag++;
////				if(orderTag - 1 < defaultIdx.size())
////					defaultIdx.set(orderTag-1, order[orderTag-1]);
////				else
////				{
////					for(int k = defaultIdx.size(); k< orderTag - 1; k++)
////						defaultIdx.add(0);
////					defaultIdx.add(order[orderTag-1]);
////				}
////				
////				if(ARSquare >= currentARSquare)
////				{				
////					currentARSquare = ARSquare;
////				}
////				else
////				{	
////					if((removeTag + 1) - 1 < removeIdx.size())
////					{
////						removeIdx.set(removeTag + 1 - 1, order[orderTag -1]);
////					}
////					else
////					{
////						for(int k = removeIdx.size(); k< removeTag + 1 -1; k++)
////							removeIdx.add(0);
////						removeIdx.add(order[orderTag - 1]);
////					}
////				}
////				numInputGenes++;
////			}
////			
////			/* end finding regulators of gene i*/
////			defaultIdx.remove(numInputGenes-1);/* remove the last value, this is added automatically because while loop*/
////			ArrayList<Integer> regulatorId = new ArrayList<Integer>(defaultIdx);
////			regulatorId.removeAll(removeIdx);
////			HashSet<Integer> regulatorIdSet = new HashSet<Integer>(regulatorId);	/* no repetitions*/
////			regulatorId = new ArrayList<Integer>(regulatorIdSet);
////			Collections.sort(regulatorId);	/* sorted*/
////			
////			int regSize = regulatorId.size();
////			for(int k = 1; k<= regSize; k++)
////			{
////				int regulator = regulatorId.get(k-1);
////				VIM[regulator-1][i-1] = 1;
////			}
////		}
////		
////		return VIM;
////	}
//	
//	public void doInBackgroundLogic() {
//		int nargin = params.getNargin();
//		ArrayList<ArrayList<Double>> exprMatrix = params.getExprMatrix();
//		int nbGenes = exprMatrix.get(0).size();					/* number of genes*/
//		int numTimePoints = exprMatrix.size();					/* number of time points*/
//		int nbTrees = params.getNbTrees();						/* number of trees grown in the ensemble. Default value: 1000*/
//		
//		/* check input*/
//		ArrayList<Integer> inputIdx = params.getInputIdx();		/* we only use genes whose indexes in this list as input genes*/
//		String treeMethod = params.getTreeMethod();				/* tree procedure we used: RF(Random Forests) or ET(Extra Trees)*/
//		String K = params.getK();								/* number K of randomly selected attributes at each node of one tree*/
//		//TODO: 
//		if(nargin > 5 || nargin < 1) {
//			System.out.println("The number of input parameters should >=1 and <=5!");
//			System.exit(1);
//		}
//		if(nargin > 1 && !Util.range(1, nbGenes).containsAll(inputIdx))
//			System.out.println("Input argument input_idx must be a vector containing integers between 1 and p, where p is the number of genes in expr_matrix.");
//		if(nargin > 2 && !treeMethod.equalsIgnoreCase("RF") && !treeMethod.equalsIgnoreCase("ET"))
//			System.out.println("Input argument tree_method must be \"RF\" or \"ET\".");
//		
//		Pattern pNumber = Pattern.compile("(\\d+(\\.\\d+)?)|(\\.\\d+)");
//		Matcher mNumber = null;
//		if(nargin > 3) {
//			mNumber = pNumber.matcher(K);
//			if(!mNumber.matches() && !K.equalsIgnoreCase("sqrt") && !K.equals("all"))
//				System.out.println("Input argument K must be \"sqrt\", \"all\" or a numerical value.");
//		}
//		
//		for(int i = 1; i<= nbGenes; i++) {	
//			oneIteration(nargin, exprMatrix, nbGenes, numTimePoints, i);
//		}
//	}
//	
//	//TEST
//	public Void doInBackground() {
//		int nargin = params.getNargin();
//		ArrayList<ArrayList<Double>> exprMatrix = params.getExprMatrix();
//		int nbGenes = exprMatrix.get(0).size();					/* number of genes*/
//		int numTimePoints = exprMatrix.size();					/* number of time points*/
//		ArrayList<Integer> inputIdx = params.getInputIdx();		/* we only use genes whose indexes in this list as input genes*/
//		String treeMethod = params.getTreeMethod();				/* tree procedure we used: RF(Random Forests) or ET(Extra Trees)*/
//		String K = params.getK();								/* number K of randomly selected attributes at each node of one tree*/
//		int nbTrees = params.getNbTrees();						/* number of trees grown in the ensemble. Default value: 1000*/
//		
//		VIM = new double[nbGenes][nbGenes];						/* initialize the final result matrix VIM*/
//		
//		/* check input*/
//		//TODO: 
//		if(nargin > 5 || nargin < 1) {
//			System.out.println("The number of input parameters should >=1 and <=5!");
//			System.exit(1);
//		}
//		if(nargin > 1 && !Util.range(1, nbGenes).containsAll(inputIdx))
//			System.out.println("Input argument input_idx must be a vector containing integers between 1 and p, where p is the number of genes in expr_matrix.");
//		if(nargin > 2 && !treeMethod.equalsIgnoreCase("RF") && !treeMethod.equalsIgnoreCase("ET"))
//			System.out.println("Input argument tree_method must be \"RF\" or \"ET\".");
//		
//		Pattern pNumber = Pattern.compile("(\\d+(\\.\\d+)?)|(\\.\\d+)");
//		Matcher mNumber = null;
//		if(nargin > 3) {
//			mNumber = pNumber.matcher(K);
//			if(!mNumber.matches() && !K.equalsIgnoreCase("sqrt") && !K.equals("all"))
//				System.out.println("Input argument K must be \"sqrt\", \"all\" or a numerical value.");
//		}
//		
//		for(int i = 1; i<= nbGenes; i++) {	
//			
//			
//			/* update progress bar*/
//			GrnRfDisplay.progressBarRf.setValue(i);
//		}
//		
//		return null;
//	}
//	
//	public void oneIteration(int nargin, ArrayList<ArrayList<Double>> exprMatrix, int nbGenes, int numTimePoints, int geneIndex) {
//		Genie3SingleTimeParam singleParams = null;
//		if(nargin == 1) {
//			singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), geneIndex);
//		} else if(nargin == 2) {
//			singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), geneIndex, params.getInputIdx());
//		} else if(nargin == 3) {
//			singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), geneIndex, params.getInputIdx(), params.getTreeMethod());
//		} else if(nargin == 4) {
//			singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), geneIndex, params.getInputIdx(), params.getTreeMethod(), params.getK());
//		} else if(nargin == 5) {
//			singleParams = new Genie3SingleTimeParam(params.getExprMatrix(), geneIndex, params.getInputIdx(), params.getTreeMethod(), params.getK(), params.getNbTrees());
//		}
//		
//		Genie3SingleTimeResult result = Genie3SingleTime.genie3SingleTime(singleParams);
//		
//		double[] vi = result.getVi();
//		double[][] viWithPosition = new double[vi.length][2];
//		for(int j = 0; j< viWithPosition.length; j++) {
//			viWithPosition[j][0] = vi[j];
//			viWithPosition[j][1] = j+1;
//		}
//		
//		double[][] sortedViWithPosition = birc.grni.util.CommonUtil.mySort(viWithPosition, false);		/*descend*/
//		
//		int[] order = new int[sortedViWithPosition.length];
//		for(int j = 0; j< sortedViWithPosition.length; j++)
//			order[j] = (int)sortedViWithPosition[j][1];
//		
//		double stopPoint = Math.floor(Math.sqrt(nbGenes));
//		double[] outputVector = new double[numTimePoints-1];
//		for(int j = 1; j<= numTimePoints-1; j++) {
//			outputVector[j-1] = exprMatrix.get(j).get(geneIndex-1);
//		}
//		
//		double yMean = Statistic.mean(outputVector);
//		double currentARSquare = Double.NEGATIVE_INFINITY;
//		double ssTotal = 0;
//		for(int j = 1; j<= numTimePoints-1; j++)
//			ssTotal += Math.pow(outputVector[j-1] - yMean, 2);
//		
//		int numInputGenes = 1;
//		
//		ArrayList<Integer> defaultIdx = new ArrayList<Integer>();
//		defaultIdx.add((int)sortedViWithPosition[0][1]);
//		ArrayList<Integer> removeIdx = new ArrayList<Integer>();
//		removeIdx.add(0);
//		int removeTag = 1;
//		int orderTag = 1;
//		while(numInputGenes <= stopPoint) {
//			ArrayList<Integer> testIdx = new ArrayList<Integer>(defaultIdx);	/* deep copy of defaultIdx*/
//			testIdx.removeAll(removeIdx);
//			HashSet<Integer> testIdxSet = new HashSet<Integer>(testIdx);
//			testIdx = new ArrayList<Integer>(testIdxSet); /* no repetitions*/
//			Collections.sort(testIdx);	/* sorted*/
//			
//			Genie3PredictYTimeParam paramsPredictYTime = new Genie3PredictYTimeParam(exprMatrix, geneIndex, testIdx, result.getTreeEnsemble());
//			float[][] YHat = Genie3PredictYTime.genie3PredictYTime(paramsPredictYTime);
//			double ssError = 0;
//			for(int k = 1; k <= numTimePoints-1; k++)
//			{					
//				ssError += Math.pow(outputVector[k-1] - YHat[k-1][0], 2);
//			}
//			
//			double RSquare = 1 - (ssError/ssTotal);
//			int numRegressors = testIdx.size();
//			
//			/* find adjusted r-square*/
//			double ARSquare = (RSquare - (1 - RSquare)*(numRegressors/(numTimePoints - numRegressors - 1)));
//		
//			orderTag++;
//			if(orderTag - 1 < defaultIdx.size())
//				defaultIdx.set(orderTag-1, order[orderTag-1]);
//			else
//			{
//				for(int k = defaultIdx.size(); k< orderTag - 1; k++)
//					defaultIdx.add(0);
//				defaultIdx.add(order[orderTag-1]);
//			}
//			
//			if(ARSquare >= currentARSquare)
//			{				
//				currentARSquare = ARSquare;
//			}
//			else
//			{	
//				if((removeTag + 1) - 1 < removeIdx.size())
//				{
//					removeIdx.set(removeTag + 1 - 1, order[orderTag -1]);
//				}
//				else
//				{
//					for(int k = removeIdx.size(); k< removeTag + 1 -1; k++)
//						removeIdx.add(0);
//					removeIdx.add(order[orderTag - 1]);
//				}
//			}
//			numInputGenes++;
//		}
//		
//		/* end finding regulators of gene i*/
//		defaultIdx.remove(numInputGenes-1);/* remove the last value, this is added automatically because while loop*/
//		ArrayList<Integer> regulatorId = new ArrayList<Integer>(defaultIdx);
//		regulatorId.removeAll(removeIdx);
//		HashSet<Integer> regulatorIdSet = new HashSet<Integer>(regulatorId);	/* no repetitions*/
//		regulatorId = new ArrayList<Integer>(regulatorIdSet);
//		Collections.sort(regulatorId);	/* sorted*/
//		
//		int regSize = regulatorId.size();
//		for(int k = 1; k<= regSize; k++)
//		{
//			int regulator = regulatorId.get(k-1);
//			VIM[regulator-1][geneIndex-1] = 1;
//		}
//	}
//	
//	@Override
//	/**
//	 * handle the final result, save it into a file or visualize it into a network
//	 */
//	public void done()
//	{
//		/* convert result into integer type which fits to our network visualization method*/
//		int genes = VIM.length;	/* number of genes*/
//		int[][] network = new int[genes][genes];
//		for(int i = 0; i< genes; i++)
//			for(int j = 0; j< genes; j++)
//				network[i][j] = (int) VIM[i][j];
//		GrnRf.resultPrinter(network, genes);
//	}
//}