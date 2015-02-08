package birc.grni.en;

import java.io.*;
import java.util.*;
import birc.grni.util.*;
import javax.swing.*;
import birc.grni.gui.*;

public class ProgressBarAdaptorLasso extends SwingWorker<Void, Void>{

	/* the logic part of algorithm*/
	private Lasso algorithm = null;
	private InputData inputData = null;
	
	public ProgressBarAdaptorLasso(InputData inputData) throws IOException{
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
		this.algorithm = new Lasso(originalDataArrayListMatrix);
		
		/* initialize progress bar*/
		birc.grni.gui.GrnLassoDisplay.progressBarLasso.setMaximum(originalDataArrayListMatrix.get(0).size());	/* the maximum value is the total number of genes*/
	}
	
	public Void doInBackground() {
		double[][] yOriginalNorm = this.algorithm.getyOriginalNorm();
		
		GlmnetOptions options = null;
		CVErr cvErr = null;
		GlmnetResult model = null;
		ArrayList<double[]> columnListOfBlasso = new ArrayList<double[]>();
		double[][] Blasso = null;
		
		/* gene = size(Y,2)*/
		int gene = yOriginalNorm[0].length;
		
		/* for ii = 1:gene*/
		for(int ii = 1; ii<= gene; ii++)
		{
			algorithm.oneIteration(options, cvErr, model, columnListOfBlasso, ii);
			
			/* set progress bar current value*/
			birc.grni.gui.GrnLassoDisplay.progressBarLasso.setValue(ii);
		}
		
		/* set Matrix Blasso*/
		Blasso = new double[columnListOfBlasso.get(0).length][columnListOfBlasso.size()];
		for(int j = 0; j< columnListOfBlasso.size(); j++)
			for(int i = 0; i< columnListOfBlasso.get(j).length; i++)
				Blasso[i][j] = columnListOfBlasso.get(j)[i];
		
		int[][] finalNetwork = new int[Blasso.length][Blasso[0].length];
		for(int i = 0; i< Blasso.length; i++)
			for(int j = 0; j< Blasso[0].length; j++)
				if(Math.abs(Blasso[i][j]) != 0)
					finalNetwork[i][j] = 1;
				else
					finalNetwork[i][j] = 0;
		
		algorithm.setFinalNetwork(finalNetwork);
		
		return null;
	}
	
	public void done()
	{
		int[][] finalNetwork = this.algorithm.getFinalNetwork();
		int genes = finalNetwork.length;	/* number of genes*/
		GrnLasso.resultPrinter(finalNetwork, genes, this.inputData.getGeneNames());
	}
}
