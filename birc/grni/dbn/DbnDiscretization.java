package birc.grni.dbn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import birc.grni.util.CommonUtil;
import birc.grni.util.InputData;

public class DbnDiscretization {
	
	String inputPath;
	ArrayList<ArrayList<Double>> input = new ArrayList<ArrayList<Double>>();;
	int [][] discretizeInput;
	int [][] dbnData;
	int numGenes;
	int numSamples;
	// Arrf string constant
	String relation ="@relation name";
	String attribute = "@attribute node";
	String state = "{0,1,2}";
	String data ="@data";
	
	//CHANGE BY LIU:
//	public DbnDiscretization(String path) {
//		// TODO Auto-generated constructor stub
//		this.inputPath = path;
//		readInput();
//		numGenes = input.get(0).size();
//		numSamples = input.size();
//		discretizeInput = new int [numSamples][numGenes];
//		dbnData = new int [numSamples -1 ][numGenes*2];
//	}
	
	public DbnDiscretization(String path, boolean withHeader, boolean geneNameAreColumnHeader) {
		// TODO Auto-generated constructor stub
		this.inputPath = path;
		readInput(withHeader, geneNameAreColumnHeader);
		numGenes = input.get(0).size();
		numSamples = input.size();
		discretizeInput = new int [numSamples][numGenes];
		dbnData = new int [numSamples -1 ][numGenes*2];
	}
	
	// CHANGE BY LIU:
//	public void readInput(){
//		try {
//			FileReader fr = new FileReader(inputPath);
//			BufferedReader br = new BufferedReader(fr);
//			
//			String line = "";
//			while((line=br.readLine())!=null){
//				ArrayList <Double> list = new ArrayList<Double>();
//				Scanner sc = new Scanner(line);
//				sc.useDelimiter("[,\\s]");
//				
//				while(sc.hasNext()){
//					Double d= sc.nextDouble();
//					list.add(d);
//				}
//				input.add(list);
//				sc.close();
//				
//			}
//			br.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	public void readInput(boolean withHeader, boolean geneNameAreColumnHeader) {
		try
		{
			InputData inputData = CommonUtil.readInput(inputPath, withHeader, geneNameAreColumnHeader);
			ProgressBarAdaptorDBN.inputData = inputData;	/* Q: better method*/
			this.input = inputData.getData();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	/* input data is discretized in to 3 state using equal width method*/
	public void discretize(){
		
		for(int geneNumber=0; geneNumber<numGenes;geneNumber++){
			//copy  gene expression data of selected gene in to geneData array
			ArrayList<Double> geneData = new ArrayList<Double>(); // to copy individual gene expression data
			for(int sampleValue=0;sampleValue<numSamples;sampleValue++){
				double d1 = input.get(sampleValue).get(geneNumber);
				geneData.add(d1);
			}
			
			Collections.sort(geneData);
			double min = geneData.get(0);
			double max = geneData.get(numSamples-1);
			
			double diff = (max-min)/3;
			double cutmin = min + diff;
			double cutmax = max - diff;
			
			for (int i=0 ;i<numSamples;i++){
				double value = input.get(i).get(geneNumber);
				if(value <= cutmin){
					discretizeInput[i][geneNumber] = 0;
				}
				else if (value > cutmax) {
					discretizeInput[i][geneNumber] = 2;
				}
				else{
					discretizeInput[i][geneNumber] = 1;
				}
			} 
		}
	}
	
	public void convertToDbn(){
		// copy data from t=0 to t=T-1
		for(int t=0;t<numSamples-1;t++){
			for(int gene=0;gene<numGenes;gene++){
				dbnData[t][gene] = discretizeInput[t][gene];
			}
		}
		
		// copy data from t=1 to t=T
		for(int t=1;t<numSamples;t++){
			for(int gene=0;gene<numGenes;gene++){
				dbnData[t-1][numGenes + gene] = discretizeInput[t][gene];
			}
		}
	}
	
	public void printDataInArrfFormat(String path){
		// if file exists , delete it
		File file = new File(path);
		try{
			boolean exists = file.exists();
			if(exists){
				file.delete();
			}
		}
		catch(SecurityException e){
			e.printStackTrace();
		}
		
		// write dbn input data according to arrf format
		 try {
			FileWriter arrfWriter = new FileWriter(path , true);
			arrfWriter.write(relation + "\n");
			arrfWriter.write("\n");
			
			//write attribute data
			int totalGenes = numGenes*2;
			for(int i=0;i<totalGenes;i++){
				arrfWriter.write(attribute + i + " " + state);
				arrfWriter.write("\n");
			}
			arrfWriter.write("\n");
			
			//write numeric data
			arrfWriter.write(data);
			arrfWriter.write("\n");
			for(int t=0;t<numSamples-1;t++){
				for(int k=0;k<totalGenes;k++){
					if(k<totalGenes-1){
						arrfWriter.write(dbnData[t][k] + ",");
					}else{
						arrfWriter.write(dbnData[t][k] + "\n");
					}
					
				}
				//arrfWriter.write("\n");
			}
			arrfWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
