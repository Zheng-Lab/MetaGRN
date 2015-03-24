package birc.grni.util;

/**
 * apply statistic function on matrix
 * @author liu xingliang
 *
 */
public class StatisticMatrix
{
	//array
	
	/*--------------------------mean----------------------------*/
	
	/**
	 * 
	 * @param X input matrix
	 * @param DIM takes the mean along the dimension DIM of X
	 * 			1: along column
	 * 			2: along row
	 * @return
	 */
	public static float[] mean(float[][] X, int DIM/*1: first dimension, 2: second dimension*/)
	{
		float[] result = null;
		
		if(!(DIM == 1 || DIM == 2))
		{
			System.out.println("ERROR: StatisticMatrix.mean(float[][] matrix, int dimension), wrong dimension");
			System.exit(1);
		}
		
		/* along column*/
		if(DIM == 1)
		{
			result = new float[X[0].length];
			for(int j = 0; j< X[0].length; j++)
			{
				float[] oneColumn = new float[X.length];
				for(int i = 0; i< X.length; i++)
					oneColumn[i] = X[i][j];
				result[j] = Statistic.mean(oneColumn);
			}		
			return result;
		}
		else	/* along row*/
		{
			result = new float[X.length];
			for(int i = 0; i< X.length; i++)
				result[i] = Statistic.mean(X[i]);
			return result;
		}
	}
	
	/**
	 * 
	 * @param X input matrix
	 * @param DIM takes the mean along the dimension DIM of X
	 * 			1: along column
	 * 			2: along row
	 * @return
	 */
	public static double[] mean(double[][] X, int DIM/*1: first dimension, 2: second dimension*/)
	{
		double[] result = null;
		
		if(!(DIM == 1 || DIM == 2))
		{
			System.out.println("ERROR: StatisticMatrix.mean(double[][] matrix, int dimension), wrong dimension indicator");
			System.exit(1);
		}
		
		/* along column*/
		if(DIM == 1)
		{
			result = new double[X[0].length];
			for(int j = 0; j< X[0].length; j++)
			{
				double[] oneColumn = new double[X.length];
				for(int i = 0; i< X.length; i++)
					oneColumn[i] = X[i][j];
				result[j] = Statistic.mean(oneColumn);
			}		
			return result;
		}
		else	/* along row*/
		{
			result = new double[X.length];
			for(int i = 0; i< X.length; i++)
				result[i] = Statistic.mean(X[i]);
			return result;
		}
	}
	
	/*--------------------------std-----------------------*/
	/**
	 * 
	 * @param X
	 * @param FLAG FLAG = 0, use N-1, FLAG = 1, use N
	 * @param DIM DIM = 1, calculate standard deviation of each column, DIM = 2, calculate standard deviation of each row
	 * @return
	 */
	public static double[] std(double[][] X, int FLAG, int DIM)
	{
		if(!(FLAG == 0 || FLAG == 1))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong FLAG parameter");
			System.exit(1);
		}
		
		if(!(DIM == 1 || DIM == 2))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong dimension indicator");
			System.exit(1);
		}
		
		double[] result = null;
		if(FLAG == 0)	/* unbiased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new double[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					double[] oneColumn = new double[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.unbiasedStd(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new double[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.unbiasedStd(X[i]);
				return result;
			}
		}
		else	/* biased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new double[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					double[] oneColumn = new double[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.biasedStd(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new double[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.biasedStd(X[i]);
				return result;
			}
		}
	}
	
	/**
	 * 
	 * @param X
	 * @param FLAG FLAG = 0, use N-1, FLAG = 1, use N
	 * @param DIM DIM = 1, calculate standard deviation of each column, DIM = 2, calculate standard deviation of each row
	 * @return
	 */
	public static float[] std(float[][] X, int FLAG, int DIM)
	{
		if(!(FLAG == 0 || FLAG == 1))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong FLAG parameter");
			System.exit(1);
		}
		
		if(!(DIM == 1 || DIM == 2))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong dimension indicator");
			System.exit(1);
		}
		
		float[] result = null;
		if(FLAG == 0)	/* unbiased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new float[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					float[] oneColumn = new float[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.unbiasedStd(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new float[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.unbiasedStd(X[i]);
				return result;
			}
		}
		else	/* biased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new float[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					float[] oneColumn = new float[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.biasedStd(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new float[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.biasedStd(X[i]);
				return result;
			}
		}
	}
	
	/*-------------------------var----------------------*/
	/**
	 * 
	 * @param X
	 * @param W W = 0, use N-1, W = 1, use N
	 * @param DIM DIM = 1, calculate standard deviation of each column, DIM = 2, calculate standard deviation of each row
	 * @return
	 */
	public static double[] var(double[][] X, int W, int DIM)
	{
		if(!(W == 0 || W == 1))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong FLAG parameter");
			System.exit(1);
		}
		
		if(!(DIM == 1 || DIM == 2))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong dimension indicator");
			System.exit(1);
		}
		
		double[] result = null;
		if(W == 0)	/* unbiased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new double[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					double[] oneColumn = new double[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.unbiasedVar(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new double[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.unbiasedVar(X[i]);
				return result;
			}
		}
		else	/* biased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new double[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					double[] oneColumn = new double[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.biasedVar(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new double[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.biasedVar(X[i]);
				return result;
			}
		}
	}
	
	/**
	 * 
	 * @param X
	 * @param W W = 0, use N-1, W = 1, use N
	 * @param DIM DIM = 1, calculate standard deviation of each column, DIM = 2, calculate standard deviation of each row
	 * @return
	 */
	public static float[] var(float[][] X, int W, int DIM)
	{
		if(!(W == 0 || W == 1))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong FLAG parameter");
			System.exit(1);
		}
		
		if(!(DIM == 1 || DIM == 2))
		{
			System.out.println("ERROR: StatisticMatrix.std(double[][] X, int FLAG, int DIM), wrong dimension indicator");
			System.exit(1);
		}
		
		float[] result = null;
		if(W == 0)	/* unbiased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new float[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					float[] oneColumn = new float[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.unbiasedVar(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new float[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.unbiasedVar(X[i]);
				return result;
			}
		}
		else	/* biased*/
		{
			if(DIM == 1)	/* for each column*/
			{
				result = new float[X[0].length];
				for(int j = 0; j< X[0].length; j++)
				{
					float[] oneColumn = new float[X.length];
					for(int i = 0; i< X.length; i++)
						oneColumn[i] = X[i][j];
					result[j] = Statistic.biasedVar(oneColumn);
				}
				return result;
			}
			else	/* for each row*/
			{
				result = new float[X.length];
				for(int i = 0; i< X.length; i++)
					result[i] = Statistic.biasedVar(X[i]);
				return result;
			}
		}
	}
}
