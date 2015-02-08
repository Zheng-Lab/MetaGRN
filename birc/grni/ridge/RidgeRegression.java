package birc.grni.ridge;

import java.io.*;
import java.util.*;

import birc.grni.gui.GrnRidge;
import birc.grni.gui.GrnRidgeDisplay;

public class RidgeRegression {
	
	//TEST
	private RidgeSparseVar ridgeVar;
	private double [][] normalizedXdata; 
	private double [][] normalizedYdata;
	private int geneIdx;
	private int numGenes;
	private int numGeneInFile;
	private int numSamplesInFile;
	
	private int [][] dofValues;
	private double [][] ridgeTvalues;
	
	public RidgeRegression(ArrayList<ArrayList<Double>> expressArray) {
		numSamplesInFile= expressArray.size();
		numGeneInFile = expressArray.get(0).size();
		double [][] expressionData = new double [numSamplesInFile][numGeneInFile];
		for(int i=0 ;i<numSamplesInFile;i++){
			for(int j=0;j<numGeneInFile;j++){
				expressionData[i][j]=expressArray.get(i).get(j);
			}
		}
		
		int samples = expressionData.length;
		int numGenes = expressionData[0].length;
		double[][] originalXData = new double[samples-1][numGenes];
		double[][] originalYData = new double[samples-1][numGenes];
		for(int i=0;i<samples-1;i++){
			for(int j=0;j<numGenes;j++){
				originalXData[i][j] = expressionData[i][j];
				originalXData[i][j] = expressionData[i+1][j];
			}
		}

		this.normalizedXdata = birc.grni.util.CommonUtil.zeroMean(originalXData);
		this.normalizedYdata = birc.grni.util.CommonUtil.zeroMean(originalYData);
		this.ridgeVar = new RidgeSparseVar(normalizedXdata, normalizedYdata);
		this.geneIdx = 0;
	}
	
	public void run() {
		if(this.ridgeVar == null) 
			throw new NullPointerException();
		else
			while(this.geneIdx < this.numGenes){
				this.ridgeVar.runRidge(this.geneIdx);
				this.geneIdx++;
			}
	}
	
	//ADD BY LIU
	public int[][] getFinalNetwork()
	{
		dofValues = ridgeVar.getDofMatrix();
		ridgeTvalues = ridgeVar.getTvalueMatrix();
		FalseDiscoveryRate fd = new FalseDiscoveryRate(ridgeTvalues, dofValues);
		int[][] network = fd.networkConstructor();
		
		return network;
	}
	
	//TEST
	public static void main(String[] args) throws FileNotFoundException, IOException{
		ArrayList<ArrayList<Double>> inputDataMatrix = new ArrayList<ArrayList<Double>>();
		BufferedReader brInputFile = new BufferedReader(new FileReader("E:/data/data_10G30T_ridge.txt"));
		String oneLine = "";
		while ((oneLine = brInputFile.readLine()) != null) {
			ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
			Scanner sc = new Scanner(oneLine);
			while(sc.hasNext()) {
				oneLineArrayList.add(sc.nextDouble());
			}
			inputDataMatrix.add(oneLineArrayList);
			sc.close();
		}
		brInputFile.close();
		
		RidgeRegression ridge = new RidgeRegression(inputDataMatrix);
		ridge.run();
		ridge.getFinalNetwork();
	}
}
