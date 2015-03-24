package birc.grni.ridge;

import java.util.ArrayList;
import java.util.List;


public class MatrixOperations {
	
	/* creates a large matrix that consisting of an m-by-n tiling of copies of aMatrix.
	 * this is the java implementation of the repmat matlab fucntion.
	 * B = repmat(A,m,n) creates a large matrix B consisting of an m-by-n tiling of copies of A*/
	public static double[][] repmat (double [][] aMatrix, int m,int n){
		
		// aMatrix is row major matrix
		int row_aMatrix=aMatrix.length;
		int col_aMatrix = aMatrix[0].length;
		double [][] bMatrix = new double [m*row_aMatrix][n*col_aMatrix];
		
		for(int outerRow=0;outerRow<m;outerRow++){
			for(int outerCol=0;outerCol<n;outerCol++){
				
				for(int innerRow=0;innerRow<row_aMatrix;innerRow++){
					for(int innerCol=0;innerCol<col_aMatrix;innerCol++){
						bMatrix[outerRow*row_aMatrix + innerRow][outerCol*col_aMatrix + innerCol]=aMatrix[innerRow][innerCol];
					}
				}
			}
		}
		
		return bMatrix;
		
	}
	
	public static double[][] repmat(int value, int a, int b){
		double [][] mat = new double[a][b];
		for(int i=0;i<a;i++){
			for(int j=0;j<b;j++){
				mat[i][j]=value;
			}
		}
		return mat;
	}
	
	public static int [] find(double [] array, int k){
		/* find function identify indexes where array elements are equal to k */
		int size= array.length;
		List <Integer> indexList = new ArrayList<Integer>();
		for(int i=0;i<size;i++){
			if(array[i]==k){
				indexList.add(i);
			}
		}
		int listSize = indexList.size();
		int [] index = new int[listSize];
		for(int i=0;i<listSize;i++){
			index[i]=indexList.get(i);
		}
		return index;
	}
	
	public static double [] sign(double [] arrayInput){
		/* java implementation of the Matlab sign function
		 * Y = sign(X) returns an array Y the same size as X, where each element of Y is:
		1 if the corresponding element of X is greater than zero
		0 if the corresponding element of X equals zero
		-1 if the corresponding element of X is less than zero*/
		int size = arrayInput.length;
		double [] array =arrayInput;
		for(int i=0;i<size;i++){
			if(array[i]>0){
				array[i]=1;
			}
			else if(array[i]<0){
				array[i]=-1;
			}
			else if(array[i]==0){
				array[i]=0;
			}
		}
		return array;
	}
	
	public static double [][] diag(double [][] mat){
		int row =mat.length;
		double [][] diagMat = new double[row][1];
		for(int i=0;i<row;i++){
			for(int j=0;j<row;j++){
				if(i==j){
					diagMat[i][0]=mat[i][j];
				}
			}
		}
		return diagMat;
	}
	
	public static double [][] multiplication(double [][] matA, double [][] matB){
		int rowA = matA.length;
		int colA = matA[0].length;
		int rowB = matB.length;
		int colB = matB[0].length;
		double sum;
		
		if(colA != rowB){
			System.out.println("ridge:Matrix multiplication error");
			System.exit(1);
		}
		double [][] result = new double [rowA][colB];
		for(int a=0;a<rowA;a++){
			for(int b=0;b<colB;b++){
				sum =0;
				for(int k =0;k<colA;k++){
					sum=sum + matA[a][k]*matB[k][b];
				}
				result[a][b]=sum;
			}
		}
		return result;
	}
	
	public static double [][] multiplicationByElement(double [][] matA, double [][] matB){
		int rowA = matA.length;
		int colA = matA[0].length;
		int rowB = matB.length;
		int colB = matB[0].length;
		
		if((rowA != rowB) & (colA != colB)){
			System.out.println("ridge:Matrix multiplication error");
			System.exit(1);
		}
		double [][] mult = new double [rowA][colA];
		for(int i=0;i<rowA;i++){
			for(int j=0;j<colA;j++){
				mult[i][j]=matA[i][j]*matB[i][j];
			}
		}
		return mult;
	}

}
