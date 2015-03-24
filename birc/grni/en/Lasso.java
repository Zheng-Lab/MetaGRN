package birc.grni.en;

import java.util.ArrayList;


public class Lasso{
	
	/* normalized original data matrix X and Y*/
	private double[][] xOriginalNorm = null;
	private double[][] yOriginalNorm = null;
	
	/* path of input file*/
//	private String inputFilePath = null;
	
	/* final network*/
	private int[][] finalNetwork = null;
	
//	public Lasso(String inputFilePath)
//	{
//		this.inputFilePath = inputFilePath;
//	}
	
	/* getter*/
	public double[][] getxOriginalNorm() {
		return xOriginalNorm;
	}

	public double[][] getyOriginalNorm() {
		return yOriginalNorm;
	}
	
	public int[][] getFinalNetwork() {
		return this.finalNetwork;
	}
	
	/* setter*/
	public void setFinalNetwork(int[][] finalNetwork) {
		this.finalNetwork = finalNetwork;
	}
	
	public Lasso(ArrayList<ArrayList<Double>> inputDataMatrix) {
		/* transfer into java array*/
		double[][] xOriginal = new double[inputDataMatrix.size()-1][inputDataMatrix.get(0).size()];
		double[][] yOriginal = new double[inputDataMatrix.size()-1][inputDataMatrix.get(0).size()];
		
		for(int i = 0; i< inputDataMatrix.size()-1; i++)
			for(int j = 0; j< inputDataMatrix.get(0).size(); j++)
				xOriginal[i][j] = inputDataMatrix.get(i).get(j);
		
		for(int i = 1; i< inputDataMatrix.size(); i++)
			for(int j = 0; j< inputDataMatrix.get(0).size(); j++)
				yOriginal[i-1][j] = inputDataMatrix.get(i).get(j);
		
		/* normalization*/
		this.xOriginalNorm = birc.grni.util.CommonUtil.zeroMean(xOriginal);
		this.yOriginalNorm = birc.grni.util.CommonUtil.zeroMean(yOriginal);
	}

	/**
	 * read data and do normalization
	 */
//	public void init()
//	{
//		birc.grni.util.Logging.logger.log(Level.FINE, "Beginning of Lasso init().");
//		/* get input*/
////		float parm = 0.1f;
//		
//		try
//		{
//			/* original data matrix*/
//			BufferedReader brOriginalData = new BufferedReader(new FileReader(this.inputFilePath));
//			String originalDataLine = "";
//			ArrayList<ArrayList<Double>> originalDataArrayListMatrix = new ArrayList<ArrayList<Double>>();
//			while((originalDataLine = brOriginalData.readLine()) != null)
//			{
//				Scanner scanner = new Scanner(originalDataLine);
//				ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
//				while(scanner.hasNext())
//					oneLineArrayList.add(scanner.nextDouble());
//				originalDataArrayListMatrix.add(oneLineArrayList);
//				scanner.close();
//			}
//			brOriginalData.close();
//			
//			/* transfer into java array*/
//			double[][] xOriginal = new double[originalDataArrayListMatrix.size()-1][originalDataArrayListMatrix.get(0).size()];
//			double[][] yOriginal = new double[originalDataArrayListMatrix.size()-1][originalDataArrayListMatrix.get(0).size()];
//			
//			for(int i = 0; i< originalDataArrayListMatrix.size()-1; i++)
//				for(int j = 0; j< originalDataArrayListMatrix.get(0).size(); j++)
//					xOriginal[i][j] = originalDataArrayListMatrix.get(i).get(j);
//			
//			for(int i = 1; i< originalDataArrayListMatrix.size(); i++)
//				for(int j = 0; j< originalDataArrayListMatrix.get(0).size(); j++)
//					yOriginal[i-1][j] = originalDataArrayListMatrix.get(i).get(j);
//			
//			/* normalization*/
//			this.xOriginalNorm = birc.grni.util.CommonUtil.zeroMean(xOriginal);
//			this.yOriginalNorm = birc.grni.util.CommonUtil.zeroMean(yOriginal);
//			
//			/* initialize progress bar*/
//			birc.grni.gui.GrnLassoDisplay.progressBarLasso.setMaximum(originalDataArrayListMatrix.get(0).size());	/* the maximum value is the total number of genes*/
//			
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
//		birc.grni.util.Logging.logger.log(Level.FINE, "End of Lasso init().");
//	}
	
	/**
	 * do original main_Lasso_sparsVAR_FDR method and set progress bar current value
	 */
//	public Void doInBackground()
//	{
//		GlmnetOptions options = null;
//		CVErr cvErr = null;
//		GlmnetResult model = null;
//		ArrayList<double[]> columnListOfBlasso = new ArrayList<double[]>();
//		double[][] Blasso = null;
//		
//		/* gene = size(Y,2)*/
//		int gene = this.yOriginalNorm[0].length;
//		
//		/* for ii = 1:gene*/
//		for(int ii = 1; ii<= gene; ii++)
//		{
//			/*
//			 * options = glmnetSet();
//			 * options.alpha = 1;
//			 * */
//			options = new GlmnetOptions();
//			options.setAlpha(1);
//			
//			/* select iith column of Y*/
//			double[] iithColumnOfY = new double[this.yOriginalNorm.length];
//			for(int i = 0; i< this.yOriginalNorm.length; i++)
//				iithColumnOfY[i] = this.yOriginalNorm[i][ii-1];
//			
//			/* CVerr = cvglmnet(X,Y(:,ii),size(Y,1),[],'response','gaussian',options,[])*/
//			cvErr = CommonFunction.cvglmnet(this.xOriginalNorm, iithColumnOfY, this.yOriginalNorm.length, new int[0], "response", "gaussian", options, new double[0][0]);
//			
//			/*options = glmnetSet();
//			options.alpha = 1;
//			options.lambda = CVerr.lambda_1se;*/
//			options = new GlmnetOptions();
//			options.setAlpha(1);
//			options.setLambda(new double[]{cvErr.getLambda_1se()});
//			
//			/* select iith column of Y*/
//			iithColumnOfY = new double[this.yOriginalNorm.length];
//			for(int i = 0; i< this.yOriginalNorm.length; i++)
//				iithColumnOfY[i] = this.yOriginalNorm[i][ii-1];
//			
//			/* model = glmnet(X, Y(:,ii),'gaussian',options);*/
//			model = CommonFunction.glmnet(new GlmnetParameters(this.xOriginalNorm, iithColumnOfY, "gaussian", options));
//			/* Blasso(:,ii) = model.beta;*/
//			//Q: it seems that model.beta is a n * 1 column vector
//			double[] beta = new double[model.getBeta().length];
//			for(int i = 0; i< model.getBeta().length; i++)
//				beta[i] = model.getBeta()[i][0];
//			columnListOfBlasso.add(beta);
//			
//			/* set progress bar current value*/
//			birc.grni.gui.GrnLassoDisplay.progressBarLasso.setValue(ii);
//		}
//		
//		/* set Matrix Blasso*/
//		Blasso = new double[columnListOfBlasso.get(0).length][columnListOfBlasso.size()];
//		for(int j = 0; j< columnListOfBlasso.size(); j++)
//			for(int i = 0; i< columnListOfBlasso.get(j).length; i++)
//				Blasso[i][j] = columnListOfBlasso.get(j)[i];
//		
//		int[][] finalNetwork = new int[Blasso.length][Blasso[0].length];
//		for(int i = 0; i< Blasso.length; i++)
//			for(int j = 0; j< Blasso[0].length; j++)
//				if(Math.abs(Blasso[i][j]) != 0)
//					finalNetwork[i][j] = 1;
//				else
//					finalNetwork[i][j] = 0;
//		
//		this.finalNetwork = finalNetwork;
//		
//		return null;
//	}
	
	public void run()
	{
		GlmnetOptions options = null;
		CVErr cvErr = null;
		GlmnetResult model = null;
		ArrayList<double[]> columnListOfBlasso = new ArrayList<double[]>();
		double[][] Blasso = null;
		
		/* gene = size(Y,2)*/
		int gene = this.yOriginalNorm[0].length;
		
		/* for ii = 1:gene*/
		for(int ii = 1; ii<= gene; ii++)
		{
			oneIteration(options, cvErr, model, columnListOfBlasso, ii);
		}
		
		/* set Matrix Blasso*/
		Blasso = new double[columnListOfBlasso.get(0).length][columnListOfBlasso.size()];
		for(int j = 0; j< columnListOfBlasso.size(); j++)
			for(int i = 0; i< columnListOfBlasso.get(j).length; i++)
				Blasso[i][j] = columnListOfBlasso.get(j)[i];
		
		int[][] finalNetwork = new int[Blasso.length][Blasso[0].length];
		for(int i = 0; i< Blasso.length; i++)
			for(int j = 0; j< Blasso[0].length; j++)
				if(Math.abs(Blasso[i][j]) != 0)
					finalNetwork[i][j] = 1;
				else
					finalNetwork[i][j] = 0;
		
		this.finalNetwork = finalNetwork;
	}
	
	public void oneIteration(GlmnetOptions options, CVErr cvErr, GlmnetResult model, ArrayList<double[]> columnListOfBlasso, int index) {
		/*
		 * options = glmnetSet();
		 * options.alpha = 1;
		 * */
		options = new GlmnetOptions();
		options.setAlpha(1);
		
		/* select iith column of Y*/
		double[] iithColumnOfY = new double[this.yOriginalNorm.length];
		for(int i = 0; i< this.yOriginalNorm.length; i++)
			iithColumnOfY[i] = this.yOriginalNorm[i][index-1];
		
		/* CVerr = cvglmnet(X,Y(:,ii),size(Y,1),[],'response','gaussian',options,[])*/
		cvErr = CommonFunction.cvglmnet(this.xOriginalNorm, iithColumnOfY, this.yOriginalNorm.length, new int[0], "response", "gaussian", options, new double[0][0]);
		
		/*options = glmnetSet();
		options.alpha = 1;
		options.lambda = CVerr.lambda_1se;*/
		options = new GlmnetOptions();
		options.setAlpha(1);
		options.setLambda(new double[]{cvErr.getLambda_1se()});
		
		/* select iith column of Y*/
		iithColumnOfY = new double[this.yOriginalNorm.length];
		for(int i = 0; i< this.yOriginalNorm.length; i++)
			iithColumnOfY[i] = this.yOriginalNorm[i][index-1];
		
		/* model = glmnet(X, Y(:,ii),'gaussian',options);*/
		model = CommonFunction.glmnet(new GlmnetParameters(this.xOriginalNorm, iithColumnOfY, "gaussian", options));
		/* Blasso(:,ii) = model.beta;*/
		//Q: it seems that model.beta is a n * 1 column vector
		double[] beta = new double[model.getBeta().length];
		for(int i = 0; i< model.getBeta().length; i++)
			beta[i] = model.getBeta()[i][0];
		columnListOfBlasso.add(beta);
	}
	
	/**
	 * handle the final result, save it into a file or visualize it into a network
	 */
//	public void done()
//	{
//		int genes = this.finalNetwork.length;	/* number of genes*/
//		GrnLasso.resultPrinter(this.finalNetwork, genes);
//		/* on windows platform, our lasso native library file depends on some other .dlls, 
//		 * we need load them in advance and we delete them when algorithm is over*/
////		try {
////			for(String dependentLibPath : CommonFunction.dependentLibs)
////				org.apache.commons.io.FileUtils.deleteQuietly(new File(dependentLibPath));
////		} catch (Exception ex) {
////			ex.printStackTrace();
////		}
//	}
	
//	public static void main(String[] args) throws IOException
//	{
//		/* get input*/
//		float parm = 0.1f;
//		
//		/* original data matrix*/
//		BufferedReader brOriginalData = new BufferedReader(new FileReader("data"+File.separator+"data50G30T_lasso.txt"));
//		String originalDataLine = "";
//		ArrayList<ArrayList<Double>> originalDataArrayListMatrix = new ArrayList<ArrayList<Double>>();
//		while((originalDataLine = brOriginalData.readLine()) != null)
//		{
//			Scanner scanner = new Scanner(originalDataLine);
//			ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
//			while(scanner.hasNext())
//				oneLineArrayList.add(scanner.nextDouble());
//			originalDataArrayListMatrix.add(oneLineArrayList);
//			scanner.close();
//		}
//		brOriginalData.close();
//		
//		/* transfer into java array*/
//		double[][] xOriginal = new double[originalDataArrayListMatrix.size()-1][originalDataArrayListMatrix.get(0).size()];
//		double[][] yOriginal = new double[originalDataArrayListMatrix.size()-1][originalDataArrayListMatrix.get(0).size()];
//		
//		for(int i = 0; i< originalDataArrayListMatrix.size()-1; i++)
//			for(int j = 0; j< originalDataArrayListMatrix.get(0).size(); j++)
//				xOriginal[i][j] = originalDataArrayListMatrix.get(i).get(j);
//		
//		for(int i = 1; i< originalDataArrayListMatrix.size(); i++)
//			for(int j = 0; j< originalDataArrayListMatrix.get(0).size(); j++)
//				yOriginal[i-1][j] = originalDataArrayListMatrix.get(i).get(j);
//		
//		/* zeroMean*/
//		double[][] xOriginalNorm = birc.grni.util.CommonUtil.zeroMean(xOriginal);
//		double[][] yOriginalNorm = birc.grni.util.CommonUtil.zeroMean(yOriginal);
//		
//		/* [Blasso,Tlasso,doflasso] = main_Lasso_sparsVAR_FDR(Yn2,Xn2);
//		 * Tlasso, doflasso not required*/
//		
//		main_Lasso_sparsVAR_FDRResult result = main_Lasso_sparsVAR_FDR(yOriginalNorm, xOriginalNorm);
//
//		double[][] Blasso = result.getBlasso();
//		int[][] finalNetwork = new int[Blasso.length][Blasso[0].length];
//		for(int i = 0; i< Blasso.length; i++)
//			for(int j = 0; j< Blasso[0].length; j++)
//				if(Math.abs(Blasso[i][j]) != 0)
//					finalNetwork[i][j] = 1;
//				else
//					finalNetwork[i][j] = 0;
//		
//		//TEST: print Blasso
////		PrintWriter pw = new PrintWriter("java.txt");
////		for(double[] rowOfBlasso : Blasso)
////			for(double elementOfBlasso: rowOfBlasso)
////				pw.printf("%20.15f\n", elementOfBlasso);
////		pw.close();
//				
//		/* print final network*/
////		PrintWriter pwFinalNetwork = new PrintWriter("java.txt");
////		for(int i = 0; i< finalNetwork.length; i++)
////		{
////			for(int j = 0; j< finalNetwork[0].length; j++)
////				pwFinalNetwork.printf("%4d\n", finalNetwork[i][j]);
////			pwFinalNetwork.println();
////		}
////		pwFinalNetwork.close();
//	}
	
//	public static main_Lasso_sparsVAR_FDRResult main_Lasso_sparsVAR_FDR(double[][] Y, double[][] X)
//	{
//		GlmnetOptions options = null;
//		CVErr cvErr = null;
//		GlmnetResult model = null;
//		ArrayList<double[]> columnListOfBlasso = new ArrayList<double[]>();
//		double[][] Blasso = null;
//		
//		/* gene = size(Y,2)*/
//		int gene = Y[0].length;
//		
//		/* for ii = 1:gene*/
//		for(int ii = 1; ii<= gene; ii++)
//		{
//			/*
//			 * options = glmnetSet();
//			 * options.alpha = 1;
//			 * */
//			options = new GlmnetOptions();
//			options.setAlpha(1);
//			
//			/* select iith column of Y*/
//			double[] iithColumnOfY = new double[Y.length];
//			for(int i = 0; i< Y.length; i++)
//				iithColumnOfY[i] = Y[i][ii-1];
//			
//			/* CVerr = cvglmnet(X,Y(:,ii),size(Y,1),[],'response','gaussian',options,[])*/
//			//TEST: combine en and lasso
//			cvErr = CommonFunction.cvglmnet(X, iithColumnOfY, Y.length, new int[0], "response", "gaussian", options, new double[0][0]);
//			
//			/*options = glmnetSet();
//			options.alpha = 1;
//			options.lambda = CVerr.lambda_1se;*/
//			options = new GlmnetOptions();
//			options.setAlpha(1);
//			options.setLambda(new double[]{cvErr.getLambda_1se()});
//			
//			/* select iith column of Y*/
//			iithColumnOfY = new double[Y.length];
//			for(int i = 0; i< Y.length; i++)
//				iithColumnOfY[i] = Y[i][ii-1];
//			
//			/* model = glmnet(X, Y(:,ii),'gaussian',options);*/
//			//TEST: combine en and lasso
//			model = CommonFunction.glmnet(new GlmnetParameters(X, iithColumnOfY, "gaussian", options));
//			/* Blasso(:,ii) = model.beta;*/
//			//Q: it seems that model.beta is a n * 1 column vector
//			double[] beta = new double[model.getBeta().length];
//			for(int i = 0; i< model.getBeta().length; i++)
//				beta[i] = model.getBeta()[i][0];
//			columnListOfBlasso.add(beta);
//		}
//		
//		/* set Matrix Blasso*/
//		Blasso = new double[columnListOfBlasso.get(0).length][columnListOfBlasso.size()];
//		for(int j = 0; j< columnListOfBlasso.size(); j++)
//			for(int i = 0; i< columnListOfBlasso.get(j).length; i++)
//				Blasso[i][j] = columnListOfBlasso.get(j)[i];
//		
//		/* return result*/
//		return new main_Lasso_sparsVAR_FDRResult(Blasso);
//	}
}

class main_Lasso_sparsVAR_FDRResult
{
	private double[][] Blasso = null;
	private int nlhs = -1;
	
	public main_Lasso_sparsVAR_FDRResult(double[][] Blasso)
	{
		this.Blasso = Blasso;
		this.nlhs = 1;
	}
	
	public double[][] getBlasso()
	{
		return this.Blasso;
	}
}
