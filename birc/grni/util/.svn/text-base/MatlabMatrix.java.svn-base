package birc.grni.util;

import java.lang.reflect.Array;

/**
 * A java implementation of Matlab matrix, similar with java two-dimension array,
 * except that, when transferred to one-dimension array, it is column-major
 * 
 * @author liu xingliang
 */
public class MatlabMatrix<T> 
{
	private T[][] matrix;
	private T[] content;					/* one-dimension, column-major representation of this matrix*/
	
	/**
	 * initialize this Matlab matrix using a Java two-dimension array,
	 * and fill the one-dimension content of this matrix based on column-major way
	 * @param newMatrix
	 */
	@SuppressWarnings("unchecked")
	public MatlabMatrix(T[][] newMatrix) 
	{
		if(newMatrix.length > 0)
		{
			this.matrix = newMatrix;
			/* fill the one-dimension content of this matrix*/
			this.content = (T[]) Array.newInstance(newMatrix[0].getClass().getComponentType(), newMatrix.length * newMatrix[0].length);
			for(int columnIndex = 0; columnIndex< this.matrix[0].length; columnIndex++)
				for(int rowIndex = 0; rowIndex< this.matrix.length; rowIndex++)
					this.content[columnIndex * this.matrix.length + rowIndex] = this.matrix[rowIndex][columnIndex];
				
		}
		else
		{
			System.out.println("Exception: MatlabMatrix(T[][] newMatrix): newMatrix must not be empty.");
			System.exit(1);
		}
	}
    
    /**
     * get number of rows 
     * */
    public int getRows() 
    {
        return matrix.length;
    }

    /**
     * get number of columns
     * */
    public int getColumns() 
    {
        return matrix[0].length;
    }
    
    /**
     * get the element at (row-1, column-1) position of the matrix
     * 
     * @param row
     * @param column
     * @return
     */
    public T get(int row, int column)
    {
       return matrix[row][column]; 
    }
    
    /**
     * get one-dimension, column-major representation (vector) of the matrix 
     * 
     * @return
     */
    public T[] getContent()
    {
    	return this.content;
    }
}