package birc.grni.ridge;

import java.util.Arrays;
import LBJ2.util.*;

public class FalseDiscoveryRate {

	/* java implementation of the FDR_signi function */
	protected double [][] Tridge;
	protected int [][] dofMatrix;
	private int numGenes;
	
	public FalseDiscoveryRate(double [][] Tdata, int [][] dofdata){
		this.Tridge=Tdata;
		this.dofMatrix=dofdata;
		this.numGenes=Tdata.length;
	}
	
	public int [][] networkConstructor(){
		double [][] q2Matrix = new double[numGenes][numGenes];
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
					
			}
		}
		
		return pMatrix;
	}
}

