package birc.grni.en;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingWorker;

import Jama.Matrix;
import birc.grni.gui.GrnElasticNet;
import birc.grni.gui.GrnElasticNetDisplay;
import birc.grni.util.InputData;

public class ProgressBarAdaptorEN extends SwingWorker<Void, Void> {
	/* the logic part of the algorithm*/
	private ElasticNet algorithm = null;
	private InputData inputData = null;
	
	public ProgressBarAdaptorEN(InputData inputData) throws IOException{
		//TEST
//		/* original data matrix*/
//		BufferedReader brOriginalData = new BufferedReader(new FileReader(inputFilePath));
//		String originalDataLine = "";
//		ArrayList<ArrayList<Double>> originalDataArrayListMatrix = new ArrayList<ArrayList<Double>>();
//		while((originalDataLine = brOriginalData.readLine()) != null)
//		{
//			Scanner scanner = new Scanner(originalDataLine);
//			ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
//			while(scanner.hasNext())
//				oneLineArrayList.add(scanner.nextDouble());
//			originalDataArrayListMatrix.add(oneLineArrayList);
//			scanner.close();
//		}
//		brOriginalData.close();
		
		this.inputData = inputData;
		ArrayList<ArrayList<Double>> originalDataArrayListMatrix = this.inputData.getData();
		this.algorithm = new ElasticNet(originalDataArrayListMatrix);
		
		/* initialize progress bar*/
		GrnElasticNetDisplay.progressBarElasticNet.setMaximum(originalDataArrayListMatrix.get(0).size());	/* the maximum value is the total number of genes*/
	}
	
	public Void doInBackground() {
		
		/* transpose matrix X*/
		//TEST
		double[][] xOriginalNorm = this.algorithm.getxOriginalNorm();
		double[][] yOriginalNorm = this.algorithm.getyOriginalNorm();
		//TEST
//		Matrix matrixX = new Matrix(this.xOriginalNorm);
		Matrix matrixX = new Matrix(xOriginalNorm);
		Matrix matrixTX = matrixX.transpose();	
		
		CVErr cvErr = null;
//		double alp = 0;
//		double lamb = 0;
		GlmnetOptions options = null;
		GlmnetResult model = null;
		double[][] Ben = null;
		ArrayList<double[]> columnListOfBen = new ArrayList<double[]>();
		double[][] Brid = null;
		double[] B1 = null;
		double[][] Bn = null;
		//TEST
//		int[] dof = new int[this.yOriginalNorm[0].length];	/* horizontal */
		int[] dof = new int[yOriginalNorm[0].length];	/* horizontal */
		double[][] T = null;
		ArrayList<double[]>	columnListOfT = new ArrayList<double[]>(); 
		
		/* tX * X*/
		Matrix matrixA = matrixTX.times(matrixX);  
		double[][] A = matrixA.getArray();
		
		//TEST
//		int gene = this.yOriginalNorm[0].length;
		int gene = yOriginalNorm[0].length;
		
		double[] a = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
		
		//TEST
//		for(int ii = 1; ii<= this.yOriginalNorm[0].length; ii++)
		for(int ii = 1; ii<= yOriginalNorm[0].length; ii++)
		{	
			algorithm.oneIteration(a, options, cvErr, columnListOfBen, model, Brid, B1, Bn, dof, columnListOfT, A, gene, ii);
			/* set progress bar current value*/
			GrnElasticNetDisplay.progressBarElasticNet.setValue(ii);
		}
		
		/* set Matrix Ben*/
		Ben = new double[columnListOfBen.get(0).length][columnListOfBen.size()];
		for(int j = 0; j< columnListOfBen.size(); j++)
			for(int i = 0; i< columnListOfBen.get(j).length; i++)
				Ben[i][j] = columnListOfBen.get(j)[i];
		
		/* set matrix T*/
		T = new double[columnListOfT.get(0).length][columnListOfT.size()];
		for(int j = 0; j< columnListOfT.size(); j++)
			for(int i = 0; i< columnListOfT.get(j).length; i++)
				T[i][j] = columnListOfT.get(j)[i];
		
		/* based on the T value and degree of freedom(dof) calculate the final network matrix*/
		/* convert degree of freedom (dof) into two-dimension version*/
		int[][] dofTwoDim = new int[1][];					/* horizontal, 1 * n*/
		dofTwoDim[0] = Arrays.copyOf(dof, dof.length);
		
		//TEST
//		this.finalNetwork = new FalseDiscoveryRate(T, dofTwoDim).networkConstructor();
		
		
		FalseDiscoveryRate fdr = new FalseDiscoveryRate(T, dofTwoDim);
		int[][] finalNetwork = fdr.networkConstructor();
		//int[][] normalizedNetworkEN= fdr.generateNormalizedNetwork();
		//int[][] finalNetwork = new FalseDiscoveryRate(T, dofTwoDim).networkConstructor();
		//double[][] unnormalizedNetwork =fdr.unnormalizedMatrix;
		int[][] normalizedNetworkEN= fdr.normalizedNetworkEN;
		//zmx
		
		System.out.println("Final network structure:");
		/*for(int i=0;i<finalNetwork.length;i++){
			for(int j=0;j<finalNetwork[0].length;j++)
				System.out.print(finalNetwork[i][j]);
			System.out.println("");
		}*/
		
		algorithm.setFinalNetwork(finalNetwork);
		//algorithm.setNormalizedNetworkEN(normalizedNetworkEN);
		
		return null;
	}
	
	public void done() {
		//TEST
		int[][] finalNetwork = this.algorithm.getFinalNetwork();
		int[][] normalizedNetworkEN= this.algorithm.getNormalizedNetworkEN();
		if(finalNetwork == null)
			throw new NullPointerException("The final network of elastic net is null!");
		else {
			int genes = finalNetwork.length;	 //number of genes
			GrnElasticNet.resultPrinter(finalNetwork, genes, this.inputData.getGeneNames());
		}
		
	}
}
