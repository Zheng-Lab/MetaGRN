package birc.grni.ridge;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import birc.grni.util.CommonUtil;
import birc.grni.util.InputData;
import birc.grni.util.exception.BadInputFormatException;


public class InputPreProcessRidge {
	
	private double [][] xData;
	private double [][] yData;
	private double [][] zeroMeanXdata; 
	private double [][] zeroMeanYdata;
	private int numGeneInFile;
	private int numSamplesInFile;
	
	// CHANGE BY LIU:
//	public void processInput(String filePath) throws FileNotFoundException , IOException{
//		
//			ArrayList<ArrayList<Double>> expressArray = new ArrayList<ArrayList<Double>>();
//			FileReader fr = new FileReader(filePath);
//			BufferedReader br = new BufferedReader(fr);
//			
//			String line = "";
//			while((line=br.readLine()) != null){
//				ArrayList <Double> lineArray = new ArrayList<Double>();
//				Scanner sc =new Scanner(line);
//				while(sc.hasNext()){
//					lineArray.add(sc.nextDouble());
//				}
//				expressArray.add(lineArray);
//				sc.close();
//			}
//			br.close();
//			
//			numSamplesInFile= expressArray.size();
//			numGeneInFile = expressArray.get(0).size();
//			double [][] expressionData = new double [numSamplesInFile][numGeneInFile];
//			for(int i=0 ;i<numSamplesInFile;i++){
//				for(int j=0;j<numGeneInFile;j++){
//					expressionData[i][j]=expressArray.get(i).get(j);
//				}
//			}
//			dataSeparation(expressionData);
//			zeroMeanXdata = zeroMean(xData);
//			zeroMeanYdata = zeroMean(yData);
//			
//			
//			
//	}
	
	public void processInput(FileReader inputFileReader, boolean withHeader, boolean geneNameAreColumnHeader) throws BadInputFormatException, IOException{
			
			InputData inputData = CommonUtil.readInput(inputFileReader, withHeader, geneNameAreColumnHeader);
			ProgressBarAdaptorRidge.inputData = inputData;	/*Q: better method */
			ArrayList<ArrayList<Double>> expressArray = inputData.getData();
			
			numSamplesInFile= expressArray.size();
			numGeneInFile = expressArray.get(0).size();
			double [][] expressionData = new double [numSamplesInFile][numGeneInFile];
			for(int i=0 ;i<numSamplesInFile;i++){
				for(int j=0;j<numGeneInFile;j++){
					expressionData[i][j]=expressArray.get(i).get(j);
				}
			}
			dataSeparation(expressionData);
			zeroMeanXdata = zeroMean(xData);
			zeroMeanYdata = zeroMean(yData);
	}
	
	public void dataSeparation(double [][] data){
		int samples = data.length;
		int numGene = data[0].length;
		 xData = new double[samples-1][numGene];
		 yData = new double[samples-1][numGene];
		for(int i=0;i<samples-1;i++){
			for(int j=0;j<numGene;j++){
				xData[i][j]=data[i][j];
				yData[i][j] =data[i+1][j];
			}
		}
		
	}
	
	public double [][] zeroMean(double [][] data){
		int row = data.length;
		int col = data[0].length;
		double [][] normalizeData= new double [row][col];
		double [] colArray = new double[row];
		for(int colvalue=0;colvalue<col;colvalue++){
			for(int i=0;i<row;i++){
				colArray[i]=data[i][colvalue]; 
			}
			for(int rowvalue=0;rowvalue<row;rowvalue++){
				double meanColumn = RidgeStat.mean(colArray);
				double stdColumn  = RidgeStat.unbiasedStd(colArray);
				normalizeData [rowvalue][colvalue] = (data[rowvalue][colvalue] - meanColumn)/(Math.sqrt(row -1)*stdColumn); 
			}
		}
		return normalizeData;
	}

	public double[][] getxData() {
		return xData;
	}

	public double[][] getyData() {
		return yData;
	}

	public double[][] getZeroMeanXdata() {
		return zeroMeanXdata;
	}

	public double[][] getZeroMeanYdata() {
		return zeroMeanYdata;
	}

	public int getNumGeneInFile() {
		return numGeneInFile;
	}

	public int getNumSamplesInFile() {
		return numSamplesInFile;
	}
}

