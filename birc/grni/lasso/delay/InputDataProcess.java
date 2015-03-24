package birc.grni.lasso.delay;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputDataProcess {
	
	int numGeneInFile;
	int numSamplesInFile;
	double [][] expressionData;

	public void processInput(String filePath) throws FileNotFoundException , IOException{
		
		ArrayList<ArrayList<Double>> expressArray = new ArrayList<ArrayList<Double>>();
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		
		String line = "";
		while((line=br.readLine()) != null){
			ArrayList <Double> lineArray = new ArrayList<Double>();
			Scanner sc =new Scanner(line);
			while(sc.hasNext()){
				lineArray.add(sc.nextDouble());
			}
			expressArray.add(lineArray);
			sc.close();
		}
		br.close();
		numSamplesInFile= expressArray.size();
		numGeneInFile = expressArray.get(0).size();
		expressionData = new double [numSamplesInFile][numGeneInFile];
		for(int i=0 ;i<numSamplesInFile;i++){
			for(int j=0;j<numGeneInFile;j++){
				expressionData[i][j]=expressArray.get(i).get(j);
			}
		}
		
	}
		
		
}
