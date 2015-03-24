package birc.grni.en;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import LBJ2.util.*;

public class FalseDiscoveryRate {

	/* java implementation of the FDR_signi function */
	protected double [][] Tridge;
	protected int [][] dofMatrix;
	private int numGenes;
	protected double [][]unnormalizedMatrix;
	protected double[] rankingList;
	//zmx
	protected int [][] normalizedNetworkEN;
	
	public FalseDiscoveryRate(double [][] Tdata, int [][] dofdata){
		this.Tridge=Tdata;
		this.dofMatrix=dofdata;
		this.numGenes=Tdata.length;
	}
	
	public int [][] networkConstructor(){
		double [][] q2Matrix = new double[numGenes][numGenes];
		unnormalizedMatrix = new double[numGenes][numGenes];
		normalizedNetworkEN = new int[numGenes][numGenes];
		rankingList = new double[numGenes*numGenes];
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				q2Matrix[i][j]=1 - StudentT.studentTcdf(Math.abs(Tridge[i][j]), dofMatrix[0][j]);
			}
		}
		
		// sorting of q2Matrix values
		double [] q2SortRs  = new double [numGenes*numGenes];
		int k=0;
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				q2SortRs[k]=q2Matrix[i][j];
				k++;
			}
		}
		
		//print q2Matrix values
		/*for(int i=0;i<q2Matrix.length;i++){
		for(int j=0;j<q2Matrix[0].length;j++)
			System.out.print(q2Matrix[i][j]+"\t");
		System.out.println("");
		}*/
		Arrays.sort(q2SortRs);
		int suValue = q2SortRs.length;
		double [] q1Matrix = new double [suValue];
		for(int i=0;i<suValue;i++){
			q1Matrix[i]=(i + 1)*0.05/suValue;
		}
		// construction of Re matrix
		/* Re matrix consists with two columns and suValue number of rows
		 * first column == q2SortRS matrix
		 * second column == q1Matrix  */
		double [][] ReMatrix = new double [suValue][2];
		
		for(int row=0;row<suValue;row++){
				ReMatrix[row][0]=q2SortRs[row];
		}
		for(int row=0;row<suValue;row++){
			ReMatrix[row][1]=q1Matrix[row];
		}
		
		// R1 matrix
		double [] r1Matrix = new double [suValue];
		for(int i=0;i<suValue;i++){
			r1Matrix[i]=ReMatrix[i][0] - ReMatrix[i][1];
		}
		
		//Aq matrix
		double [] signResult = MatrixOperations.sign(r1Matrix);
		int [] aqMatrix = MatrixOperations.find(signResult, 1);
		
		// p matrix
		int [][] pMatrix = new int [numGenes][numGenes];
		for(int i=0;i<numGenes;i++){
			for(int j=0 ; j<numGenes;j++){
				double qValue=Math.abs(q2Matrix[i][j]);
				double reValue=ReMatrix[aqMatrix[0]][0];
				if(qValue < reValue){
					pMatrix [i][j] = 1;
				}else{
					pMatrix [i][j] = 0;
				}
				unnormalizedMatrix[i][j]=1-qValue;
				rankingList[i*numGenes+j]=1-qValue;
					
			}
		}
		/*for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++)
				System.out.print(q2Matrix[i][j]+"\t");
			System.out.println("");
		}
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++)
				System.out.print(unnormalizedMatrix[i][j]+"\t");
			System.out.println("");
		}*/
		Arrays.sort(rankingList);
		normalizedNetworkEN=generateNormalizedNetwork();
/*		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				System.out.println("unnormalizedMatrix value: "+unnormalizedMatrix[i][j]);
				for(int z=0;z<numGenes*numGenes;z++){
					
					System.out.println("rankingList value: "+rankingList[z]+" z value is: "+z);
					if(Double.compare(unnormalizedMatrix[i][j], rankingList[z])==0){
						System.out.println("value equal"+z);
						normalizedNetworkEN[i][j]=z;
						break;
					}
				}
			}
		}
		System.out.println("generate normalized network finish");
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				System.out.print(normalizedNetworkEN[i][j]+"\t");
			}
			System.out.println("");
			
		}*/
		//return normalizedNetworkEN;
	
		return pMatrix;
	}
	public int[][] generateNormalizedNetwork(){
		//int [][] normalizedNetworkEN = new int [numGenes][numGenes];
		//System.out.println("generate normalized network start");
		for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				//System.out.println("unnormalizedMatrix value: "+unnormalizedMatrix[i][j]);
				for(int z=0;z<numGenes*numGenes;z++){
					
					//System.out.println("rankingList value: "+rankingList[z]+" z value is: "+z);
					try{
					if(Double.compare(unnormalizedMatrix[i][j], rankingList[z])==0){
						//System.out.println("value equal"+z);
						normalizedNetworkEN[i][j]=z;
						break;
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		//System.out.println("generate normalized network finish");
		/*for(int i=0;i<numGenes;i++){
			for(int j=0;j<numGenes;j++){
				System.out.print(normalizedNetworkEN[i][j]+"\t");
			}
			System.out.println("");
			
		}*/
		return normalizedNetworkEN;
	}
	
	
}
