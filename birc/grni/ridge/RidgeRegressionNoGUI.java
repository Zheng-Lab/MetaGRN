package birc.grni.ridge;

import java.io.FileReader;
import java.io.IOException;

import birc.grni.gui.GrnRidge;
import birc.grni.util.InputData;
import birc.grni.util.exception.BadInputFormatException;

public class RidgeRegressionNoGUI {

	private RidgeSparseVar ridgeVar;
	private int numGenes;
	private int gene;
	private int [][] dofValues;
	private double [][] ridgeTvalues;
	private int [][] network;
	
	// ADD BY LIU:
	public static InputData inputData;			/* this field will be filled in method public void processInput(String filePath, boolean withHeader, boolean geneNameAreColumnHeader) of class InputPreProcessRidge*/
	
	// CHANEG BY LIU:
//	//public RidgeRegression(int genes, int samples, String filePath){
//	public ProgressBarAdaptorRidge(String filePath) throws IOException{
//		
//		InputPreProcessRidge in = new InputPreProcessRidge();	
//		in.processInput(filePath);
//		
//		/*compare number of genes and samples in the file with the GUI inputs */
//		/*if(genes != in.numGeneInFile){
//			// add a log in the logging file
//			System.exit(0);	
//		}
//		if(samples != in.numSamplesInFile){
//			// add a log in the logging file
//			System.exit(0);	
//		}*/
//		numGenes = in.getNumGeneInFile();
//		ridgeVar = new RidgeSparseVar(in.getZeroMeanXdata(), in.getZeroMeanYdata());
//		gene=0;
//		GrnRidgeDisplay.ridgeProgressBar.setMaximum(numGenes-1);
//	}
	
	public RidgeRegressionNoGUI(FileReader inputFileReader, boolean withHeader, boolean geneNameAreColumnHeader) throws BadInputFormatException, IOException {
	
		InputPreProcessRidge in = new InputPreProcessRidge();	
		in.processInput(inputFileReader, withHeader, geneNameAreColumnHeader);
		
		/*compare number of genes and samples in the file with the GUI inputs */
		/*if(genes != in.numGeneInFile){
			// add a log in the logging file
			System.exit(0);	
		}
		if(samples != in.numSamplesInFile){
			// add a log in the logging file
			System.exit(0);	
		}*/
		numGenes = in.getNumGeneInFile();
		ridgeVar = new RidgeSparseVar(in.getZeroMeanXdata(), in.getZeroMeanYdata());
		gene=0;
//		GrnRidgeDisplay.ridgeProgressBar.setMaximum(numGenes-1);
	}

	public void run() throws Exception {
		// TODO Auto-generated method stub
		
		while(gene < numGenes){
			ridgeVar.runRidge(gene);
//			GrnRidgeDisplay.ridgeProgressBar.setValue(gene);
			gene++;
		}
	}
	
	public void done() {
		dofValues =ridgeVar.getDofMatrix();
		ridgeTvalues =ridgeVar.getTvalueMatrix();
		FalseDiscoveryRate fd = new FalseDiscoveryRate(ridgeTvalues, dofValues);
		network = fd.networkConstructor();
		
		//CHANGE BY LIU:
//		GrnRidge.ridgeResultPrinter(network,numGenes);
		GrnRidge.ridgeResultPrinter(network,numGenes, inputData.getGeneNames());
	}
	
	public static void main(String[] args) throws Exception{
		RidgeRegressionNoGUI ridgeNoGui = new RidgeRegressionNoGUI(new FileReader("E:/data/data_10G30T_ridge.txt"), false, true);
		ridgeNoGui.run();
		ridgeNoGui.done();
	}
}
