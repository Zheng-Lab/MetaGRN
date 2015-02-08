package birc.grni.rf;

import java.io.*;
import java.util.*;
import javax.swing.*;
import birc.grni.gui.*;
import birc.grni.util.*;

/**
 * 
 * @author liu xingliang
 *
 */
public class ProgressBarAdaptorRF extends SwingWorker<Void, Void> {
	
	/* the logic part of the algorithm*/
	private RandomForest algorithm = null;
	
	private InputData inputData = null;
	
	public ProgressBarAdaptorRF(InputData inputData, String treeMethod, int nbTrees) throws IOException{
		//TEST
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
		
		this.inputData = inputData;
		
		ArrayList<ArrayList<Double>> exprMatrix = this.inputData.getData();
		/* all genes as input*/
		ArrayList<Integer> inputIdx = new ArrayList<Integer>();
		int nbGenes = exprMatrix.get(0).size();
		for(int i = 1; i<= nbGenes; i++)
			inputIdx.add(i);
		
		algorithm = new RandomForest(exprMatrix, inputIdx, treeMethod, "sqrt", nbTrees);
		
		GrnRfDisplay.progressBarRf.setMaximum(exprMatrix.get(0).size());	/* the maximum value is the total number of genes*/
	}
	
	public Void doInBackground() {
		Genie3TimeParam params = this.algorithm.getParams();
		int nargin = params.getNargin();
		ArrayList<ArrayList<Double>> exprMatrix = params.getExprMatrix();
		int nbGenes = exprMatrix.get(0).size();					/* number of genes*/
		int numTimePoints = exprMatrix.size();					/* number of time points*/
		
		//TEST
//		ArrayList<Integer> inputIdx = params.getInputIdx();		/* we only use genes whose indexes in this list as input genes*/
//		String treeMethod = params.getTreeMethod();				/* tree procedure we used: RF(Random Forests) or ET(Extra Trees)*/
//		String K = params.getK();								/* number K of randomly selected attributes at each node of one tree*/
//		int nbTrees = params.getNbTrees();						/* number of trees grown in the ensemble. Default value: 1000*/
		
		//TODO:
//		/* check input*/
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
		
		for(int i = 1; i<= nbGenes; i++) {	
			algorithm.oneIteration(nargin, exprMatrix, nbGenes, numTimePoints, i);
			/* update progress bar*/
			GrnRfDisplay.progressBarRf.setValue(i);
		}
		
		return null;
	}
	
	/**
	 * set current value of progress bar in GUI
	 */
	public void done() {
		double[][] finalNetwork = algorithm.getFinalNetwork();
		/* convert result into integer type which fits to our network visualization method*/
		int genes = finalNetwork.length;	/* number of genes*/
		int[][] network = new int[genes][genes];
		for(int i = 0; i< genes; i++)
			for(int j = 0; j< genes; j++)
				network[i][j] = (int) finalNetwork[i][j];
		GrnRf.resultPrinter(network, genes, this.inputData.getGeneNames());
	}
}
