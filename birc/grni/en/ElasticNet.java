package birc.grni.en;

import java.util.*;
import Jama.Matrix;

public class ElasticNet
{	
	/* normalized original data matrix X and Y*/
	private double[][] xOriginalNorm = null;
	private double[][] yOriginalNorm = null;
	
//	/* path of input file*/
//	private String inputFilePath = null;
	
	/* final network*/
	private int[][] finalNetwork = null;
	private int[][] normalizedNetworkEN =null;
	
	
//	/**
//	 * Constructor, read input data of elastic net algorithm
//	 * @param inputFilePath
//	 */
//	public ElasticNet(String inputFilePath)
//	{
//		this.inputFilePath = inputFilePath;
//	}
	
	public int[][] getNormalizedNetworkEN() {
		return normalizedNetworkEN;
	}

	public void setNormalizedNetworkEN(int[][] normalizedNetworkEN) {
		this.normalizedNetworkEN = normalizedNetworkEN;
	}

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
	
	public ElasticNet(ArrayList<ArrayList<Double>> inputDataMatrix) {
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
	
//	/**
//	 * read data and do normalization
//	 */
//	public void init()
//	{
//		birc.grni.util.Logging.logger.log(Level.FINE, "Beginning of ElasticNet init().");
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
//			GrnElasticNetDisplay.progressBarElasticNet.setMaximum(originalDataArrayListMatrix.get(0).size());	/* the maximum value is the total number of genes*/
//			
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//		}
//		birc.grni.util.Logging.logger.log(Level.FINE, "End of ElasticNet init().");
//	}
	
	public void run() {
		/* transpose matrix X*/
		Matrix matrixX = new Matrix(this.xOriginalNorm);
		Matrix matrixTX = matrixX.transpose();	
		
		CVErr cvErr = null;
//		double alp = 0;
//		double lamb = 0;
		GlmnetOptions options = null;
		GlmnetResult model = null;
		double[][] Ben = null;
		ArrayList<double[]> columnListOfBen = new ArrayList<double[]>();
		double[][] Brid = null;
		double[] B1 = null;
		double[][] Bn = null;
		int[] dof = new int[this.yOriginalNorm[0].length];	/* horizontal */
		double[][] T = null;
		ArrayList<double[]>	columnListOfT = new ArrayList<double[]>(); 
		
		/* tX * X*/
		Matrix matrixA = matrixTX.times(matrixX);  
		double[][] A = matrixA.getArray();
		
		int gene = this.yOriginalNorm[0].length;
		double[] a = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
		
		for(int ii = 1; ii<= this.yOriginalNorm[0].length; ii++)
		{	
			oneIteration(a, options, cvErr, columnListOfBen, model, Brid, B1, Bn, dof, columnListOfT, A, gene, ii);
		}
		
		/* set Matrix Ben*/
		Ben = new double[columnListOfBen.get(0).length][columnListOfBen.size()];
		for(int j = 0; j< columnListOfBen.size(); j++)
			for(int i = 0; i< columnListOfBen.get(j).length; i++)
				Ben[i][j] = columnListOfBen.get(j)[i];
		
		/* set matrix T*/
		T = new double[columnListOfT.get(0).length][columnListOfT.size()];
		for(int j = 0; j< columnListOfT.size(); j++)
			for(int i = 0; i< columnListOfT.get(j).length; i++)
				T[i][j] = columnListOfT.get(j)[i];
		
		/* based on the T value and degree of freedom(dof) calculate the final network matrix*/
		/* convert degree of freedom (dof) into two-dimension version*/
		int[][] dofTwoDim = new int[1][];					/* horizontal, 1 * n*/
		dofTwoDim[0] = Arrays.copyOf(dof, dof.length);
		System.out.println("run in Elastic Net");
		FalseDiscoveryRate fdr=new FalseDiscoveryRate(T, dofTwoDim);
		this.finalNetwork = fdr.networkConstructor();
		/*this.normalizedNetworkEN=fdr.generateNormalizedNetwork();
		if(normalizedNetworkEN ==null){
			System.out.println("normalized network is null");
		}*/
		
	}
	
	public void oneIteration(double[] a, GlmnetOptions options, CVErr cvErr, ArrayList<double[]> columnListOfBen, 
							GlmnetResult model, double[][] Brid, double[] B1, double[][] Bn, int[] dof, ArrayList<double[]>	columnListOfT, 
							double[][] A, int gene, int ii /*index*/) 
	{
		double alp = 0;
		double lamb = 0;
		
		double[][] Parmt = birc.grni.util.CommonUtil.zeros(a.length, 3);	
		for(int jj = 1; jj<= a.length; jj++)
		{
			options = new GlmnetOptions();
			options.setAlpha(a[jj-1]);
			/* select iith column of Y*/
			double[] iithColumnOfY = new double[this.yOriginalNorm.length];
			for(int i = 0; i< this.yOriginalNorm.length; i++)
				iithColumnOfY[i] = this.yOriginalNorm[i][ii-1];
			
			cvErr = birc.grni.en.CommonFunction.cvglmnet(this.xOriginalNorm, iithColumnOfY, this.yOriginalNorm.length, new int[0], "response", "gaussian", options, new double[0][0]);
			
			Parmt[jj-1][0] = a[jj-1];
			Parmt[jj-1][1] = cvErr.getLambda_1se();
			
			double[] copyOfCvm = Arrays.copyOf(cvErr.getCvm(), cvErr.getCvm().length);
			Arrays.sort(copyOfCvm);
			Parmt[jj-1][2] = copyOfCvm[0];
		}
		
		for(int kk = 1; kk <= a.length; kk++)
		{
			double[] copyOf3rdColumnParmt = new double[Parmt.length];
			for(int i = 0; i< Parmt.length; i++)
				copyOf3rdColumnParmt[i] = Parmt[i][2];
			Arrays.sort(copyOf3rdColumnParmt);
			double min3rdColumnParmt = copyOf3rdColumnParmt[0];
			if(Parmt[kk-1][2] == min3rdColumnParmt)
			{
				alp = Parmt[kk-1][0];
				lamb = Parmt[kk-1][1];
			}
		}
		
		options = new GlmnetOptions();
		options.setAlpha(alp);
		options.setLambda(new double[]{lamb});
		/* select iith column of Y*/
		double[] iithColumnOfY = new double[this.yOriginalNorm.length];
		for(int i = 0; i< this.yOriginalNorm.length; i++)
			iithColumnOfY[i] = this.yOriginalNorm[i][ii-1];
		model = birc.grni.en.CommonFunction.glmnet(new GlmnetParameters(this.xOriginalNorm, iithColumnOfY, "gaussian", options));
		
		/* Ben(:,ii) = model.beta;*/
		//Q: it seems that model.beta is a n * 1 column vector
		double[] beta = new double[model.getBeta().length];
		for(int i = 0; i< model.getBeta().length; i++)
			beta[i] = model.getBeta()[i][0];
		columnListOfBen.add(beta);
		
		/* Brid = ridge(Y(:,ii),X,options.lambda), by default the forth parameter is true*/
		iithColumnOfY = new double[this.yOriginalNorm.length];
		for(int i = 0; i< this.yOriginalNorm.length; i++)
			iithColumnOfY[i] = this.yOriginalNorm[i][ii-1];
		Brid = ridge(iithColumnOfY, this.xOriginalNorm, options.getLambda(), true);
		
		/* B1 = repmat(1, gene, 1)./abs(Brid)*/
		B1 = new double[Brid.length];	/* B1 is vertical vector*/
		for(int i = 0; i< Brid.length; i++)
		{
			B1[i] = 1 / Math.abs(Brid[i][0]);
			if((B1[i]+"").equals("Infinity") || (B1[i]+"").equals("-Infinity"))	/* B1(isinf(B1)) = 0*/
				B1[i] = 0;
		}
		
		/* Bn = repmat(B1*options.lambda,1,gene).*eye(gene)*/
		double[][] B1TwoDim = new double[B1.length][1];
		for(int i = 0; i< B1.length; i++)
			B1TwoDim[i][0]  = B1[i];
		
		double[][] optionsLambdaTwoDim = new double[1][options.getLambda().length];	/* lambda is honrizontal vector*/
		for(int i = 0; i< options.getLambda().length; i++)
			optionsLambdaTwoDim[0][i] = options.getLambda()[i]; 
		
		/* eye(gene), the identity matrix*/
		double[][] eyeGene = new double[gene][gene];
		for(int i = 0; i< gene; i++)
		{
			Arrays.fill(eyeGene[i], 0);
			eyeGene[i][i] = 1;
		}
		
		Bn = new Matrix(birc.grni.en.CommonFunction.repmat(new Matrix(B1TwoDim).times(new Matrix(optionsLambdaTwoDim)).getArray(), 1, gene)).arrayTimes(new Matrix(eyeGene)).getArray();
		
		/* dof(:,ii) = gene - size(find(Ben(:,ii) == 0),1)*/
		
		/* size(find(Ben(:,ii) == 0),1)*/
		int nbZerosIIthColumnBen = 0;	/* the number of zeros in iith column of Ben*/
		for(double e: columnListOfBen.get(ii-1))
			if(e == 0)
				nbZerosIIthColumnBen++;
		dof[ii-1] = gene - nbZerosIIthColumnBen;
		
		/* Er = Y(:,ii) - X*Ben(:,ii)- mean(Y(:,ii));*/
		double[] Er = new double[this.yOriginalNorm.length];		/* Er is a vertical vector*/
		iithColumnOfY = new double[this.yOriginalNorm.length];
		for(int i = 0; i< this.yOriginalNorm.length; i++)
			iithColumnOfY[i] = this.yOriginalNorm[i][ii-1];
		
		double[][] iithColumnBenTwoDim = new double[columnListOfBen.get(ii-1).length][1];
		for(int i = 0; i< columnListOfBen.get(ii-1).length; i++)
			iithColumnBenTwoDim[i][0] = columnListOfBen.get(ii-1)[i];

		/* X*Ben(:,ii)*/
		double[][] XTimeIIthColumnOfBen = new Matrix(this.xOriginalNorm).times(new Matrix(iithColumnBenTwoDim)).getArray();	/* vertical vector*/
		
		double meanIIthColumnOfY = birc.grni.util.Statistic.mean(iithColumnOfY);
		for(int i = 0; i< this.yOriginalNorm.length; i++)
			Er[i] = iithColumnOfY[i] - XTimeIIthColumnOfBen[i][0] - meanIIthColumnOfY;
		
		/*Va = (Er'*Er)/(size(Y,1));*/
		double Va = 0;
		/* Er' * Er, Er is a vertical vector, so Er' * Er is square sum of all numbers in Er*/
		double sqSumEr = 0;
		for(double e: Er)
			sqSumEr += e * e;
		Va = sqSumEr/this.yOriginalNorm.length;
		
		/* W = diag(inv(A+Bn)*A*inv(A+Bn))*/
		Matrix AMatrix = new Matrix(A);
		Matrix BnMatrix = new Matrix(Bn);
		Matrix invAPlusBn = (AMatrix.plus(BnMatrix)).inverse();	/* inverse of A + Bn*/
		double[][] mMatrix = invAPlusBn.times(AMatrix).times(invAPlusBn).getArray();	/*media result*/
		double[] W = null;
		if(mMatrix.length != mMatrix[0].length)	/* not square, it shouldn't happen here*/
		{
			System.out.println("WARNING: main_EN_sparsVAR_FDR: unsquare matrix");
			System.exit(1);
		}
		else
		{
			W = new double[mMatrix.length];
			for(int i = 0; i< mMatrix.length; i++)
				W[i] = mMatrix[i][i];
		}
		
		/* T(:,ii) = Ben(:,ii)./sqrt(Va*W);*/
		double[] iithColumnOfT = new double[columnListOfBen.get(0).length];	/* same length of column of Ben matrix*/
		double[] sqrtOfVaTimesW = new double[W.length];/* sqrt(Va * W), same length of W*/
		for(int i = 0; i< W.length; i++)
			sqrtOfVaTimesW[i] = Math.sqrt(Va * W[i]);
		for(int i = 0; i< columnListOfBen.get(0).length; i++)
			iithColumnOfT[i] = columnListOfBen.get(ii-1)[i]/sqrtOfVaTimesW[i];
		columnListOfT.add(iithColumnOfT);
	}
	
//	@Override
//	/**
//	 * do original main_EN_sparsVAR_FDR method and set progress bar current value
//	 */
//	public Void doInBackground()
//	{
//		birc.grni.util.Logging.logger.log(Level.FINE, "Beginning of ElasticNet doInBackgroud().");
//		/* transpose matrix X*/
//		Matrix matrixX = new Matrix(this.xOriginalNorm);
//		Matrix matrixTX = matrixX.transpose();	
//				
//		CVErr cvErr = null;
//		double alp = 0;
//		double lamb = 0;
//		GlmnetOptions options = null;
//		GlmnetResult model = null;
//		double[][] Ben = null;
//		ArrayList<double[]> columnListOfBen = new ArrayList<double[]>();
//		double[][] Brid = null;
//		double[] B1 = null;
//		double[][] Bn = null;
//		int[] dof = new int[this.yOriginalNorm[0].length];	/* horizontal */
//		double[][] T = null;
//		ArrayList<double[]>	columnListOfT = new ArrayList<double[]>(); 
//		
//		/* tX * X*/
//		Matrix matrixA = matrixTX.times(matrixX);  
//		double[][] A = matrixA.getArray();
//		
//		int gene = this.yOriginalNorm[0].length;
//		double[] a = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
//		
//		birc.grni.util.Logging.logger.log(Level.FINE, "Stub 0.");
//		
//		for(int ii = 1; ii<= this.yOriginalNorm[0].length; ii++)
//		{	
//			
//			
//			/* set progress bar current value*/
//			GrnElasticNetDisplay.progressBarElasticNet.setValue(ii);
//		}
//		
//		/* set Matrix Ben*/
//		Ben = new double[columnListOfBen.get(0).length][columnListOfBen.size()];
//		for(int j = 0; j< columnListOfBen.size(); j++)
//			for(int i = 0; i< columnListOfBen.get(j).length; i++)
//				Ben[i][j] = columnListOfBen.get(j)[i];
//		
//		/* set matrix T*/
//		T = new double[columnListOfT.get(0).length][columnListOfT.size()];
//		for(int j = 0; j< columnListOfT.size(); j++)
//			for(int i = 0; i< columnListOfT.get(j).length; i++)
//				T[i][j] = columnListOfT.get(j)[i];
//		
//		/* based on the T value and degree of freedom(dof) calculate the final network matrix*/
//		/* convert degree of freedom (dof) into two-dimension version*/
//		int[][] dofTwoDim = new int[1][];					/* horizontal, 1 * n*/
//		dofTwoDim[0] = Arrays.copyOf(dof, dof.length);
//		
//		this.finalNetwork = new FalseDiscoveryRate(T, dofTwoDim).networkConstructor();
//		
//		birc.grni.util.Logging.logger.log(Level.FINE, "End of ElasticNet doInBackgroud().");
//		
//		return null;
//	}
	
//	@Override
//	/**
//	 * handle the final result, save it into a file or visualize it into a network
//	 */
//	public void done()
//	{
//		birc.grni.util.Logging.logger.log(Level.FINE, "Beginning of ElasticNet done().");
//		int genes = this.finalNetwork.length;	/* number of genes*/
//		GrnElasticNet.resultPrinter(this.finalNetwork, genes);
//		birc.grni.util.Logging.logger.log(Level.FINE, "End of ElasticNet done().");
//		/* on windows platform, our elastic net native library file depends on some other .dlls, 
//		 * we need load them in advance and we delete them when algorithm is over*/
////		try {
////			for(String dependentLibPath : CommonFunction.dependentLibs)
////				org.apache.commons.io.FileUtils.deleteQuietly(new File(dependentLibPath));
////		} catch (Exception ex) {
////			ex.printStackTrace();
////		}
//	}
	
//	public static void main(String[] args) throws IOException, FileNotFoundException
//	{	
//		/* get input*/
////		float parm = 0.1f;
//		
//		/* original data matrix*/
//		BufferedReader brOriginalData = new BufferedReader(new FileReader("data/data10G30T.txt"));
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
//		double[][] xOriginalNorm = Util.zeroMean(xOriginal);
//		double[][] yOriginalNorm = Util.zeroMean(yOriginal);
//		
//		main_EN_sparsVAR_FDRResult result = main_EN_sparsVAR_FDR(yOriginalNorm, xOriginalNorm);
//		
//		/* based on the T value and degree of freedom(dof) calculate the final network matrix*/
//		/* convert degree of freedom (dof) into two-dimension version*/
//		int[] dof = result.getDof();
//		int[][] dofTwoDim = new int[1][];					/* horizontal, 1 * n*/
//		dofTwoDim[0] = Arrays.copyOf(dof, dof.length);
//		int[][] finalMatrix = new FalseDiscoveryRate(result.getT(), dofTwoDim).networkConstructor();
//		
//		//TEST: print result
////		System.out.printf("a0:\n");
////		double[] a0 = elnetResult.getA0();
////		for(double e: a0)
////			System.out.printf("%20.15f\n", e);
////
////		System.out.printf("ca:\n");
////		double[][] ca = elnetResult.getCa();
////		for(int i = 0; i< ca.length; i++)
////		{
////			for(int j = 0; j< ca[0].length; j++)
////				System.out.printf("%20.15f\n", ca[i][j]);
////			//System.out.printf("\n");
////		}
////
////		System.out.printf("ia:\n");
////		double[] ia = elnetResult.getIa();
////		for(double e: ia)
////			System.out.printf("%20.15f\n", e);
////
////		System.out.printf("nin:\n");
////		double[] nin = elnetResult.getNin();
////		for(double e: nin)
////			System.out.printf("%20.15f\n", e);
////
////		System.out.printf("rsq:\n");
////		double[] rsq = elnetResult.getRsq();
////		for(double e: rsq)
////			System.out.printf("%20.15f\n", e);
////
////		System.out.printf("alm:\n");
////		double[] alm = elnetResult.getAlm();
////		for(double e: alm)
////			System.out.printf("%20.15f\n", e);
////
////		System.out.printf("nlp:\n");
////		int nlp = elnetResult.getNlp();
////		System.out.printf("%20d\n", nlp);
////
////		System.out.printf("jerr:\n");
////		int jerr = elnetResult.getJerr();
////		System.out.printf("%20d\n", jerr);
//		
//	}
	
	//TEST: this method has already been implemented in SwingWorker.doInBackground method
//	public static main_EN_sparsVAR_FDRResult main_EN_sparsVAR_FDR(double[][] Y, double[][] X)
//	{
//		/* transpose matrix X*/
//		Matrix matrixX = new Matrix(X);
//		Matrix matrixTX = matrixX.transpose();	
//		double[][] tX = matrixTX.getArray();	
//		CVErr cvErr = null;
//		double alp = 0;
//		double lamb = 0;
//		GlmnetOptions options = null;
//		GlmnetResult model = null;
//		double[][] Ben = null;
//		ArrayList<double[]> columnListOfBen = new ArrayList<double[]>();
//		double[][] Brid = null;
//		double[] B1 = null;
//		double[][] Bn = null;
//		int[] dof = new int[Y[0].length];	/* horizontal */
//		double[][] T = null;
//		ArrayList<double[]>	columnListOfT = new ArrayList<double[]>(); 
//		
//		/* tX * X*/
//		Matrix matrixA = matrixTX.times(matrixX);  
//		double[][] A = matrixA.getArray();
//		
//		int gene = Y[0].length;
//		double[] a = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
//		
//		for(int ii = 1; ii<= Y[0].length; ii++)
//		{	
//			double[][] Parmt = Util.zeros(a.length, 3);
//			
//			for(int jj = 1; jj<= a.length; jj++)
//			{
//				options = new GlmnetOptions();
//				options.setAlpha(a[jj-1]);
//				/* select iith column of Y*/
//				double[] iithColumnOfY = new double[Y.length];
//				for(int i = 0; i< Y.length; i++)
//					iithColumnOfY[i] = Y[i][ii-1];
//				
//				cvErr = cvglmnet(X, iithColumnOfY, Y.length, new int[0], "response", "gaussian", options, new double[0][0]);
//				
//				Parmt[jj-1][0] = a[jj-1];
//				Parmt[jj-1][1] = cvErr.getLambda_1se();
//				
//				double[] copyOfCvm = Arrays.copyOf(cvErr.getCvm(), cvErr.getCvm().length);
//				Arrays.sort(copyOfCvm);
//				Parmt[jj-1][2] = copyOfCvm[0];
//			}
//			
//			for(int kk = 1; kk <= a.length; kk++)
//			{
//				double[] copyOf3rdColumnParmt = new double[Parmt.length];
//				for(int i = 0; i< Parmt.length; i++)
//					copyOf3rdColumnParmt[i] = Parmt[i][2];
//				Arrays.sort(copyOf3rdColumnParmt);
//				double min3rdColumnParmt = copyOf3rdColumnParmt[0];
//				if(Parmt[kk-1][2] == min3rdColumnParmt)
//				{
//					alp = Parmt[kk-1][0];
//					lamb = Parmt[kk-1][1];
//				}
//			}
//			
//			options = new GlmnetOptions();
//			options.setAlpha(alp);
//			options.setLambda(new double[]{lamb});
//			/* select iith column of Y*/
//			double[] iithColumnOfY = new double[Y.length];
//			for(int i = 0; i< Y.length; i++)
//				iithColumnOfY[i] = Y[i][ii-1];
//			model = glmnet(new GlmnetParameters(X, iithColumnOfY, "gaussian", options));
//			
//			/* Ben(:,ii) = model.beta;*/
//			//Q: it seems that model.beta is a n * 1 column vector
//			double[] beta = new double[model.getBeta().length];
//			for(int i = 0; i< model.getBeta().length; i++)
//				beta[i] = model.getBeta()[i][0];
//			columnListOfBen.add(beta);
//			
//			/* Brid = ridge(Y(:,ii),X,options.lambda), by default the forth parameter is true*/
//			iithColumnOfY = new double[Y.length];
//			for(int i = 0; i< Y.length; i++)
//				iithColumnOfY[i] = Y[i][ii-1];
//			Brid = ridge(iithColumnOfY, X, options.getLambda(), true);
//			
//			/* B1 = repmat(1, gene, 1)./abs(Brid)*/
//			B1 = new double[Brid.length];	/* B1 is vertical vector*/
//			for(int i = 0; i< Brid.length; i++)
//			{
//				B1[i] = 1 / Math.abs(Brid[i][0]);
//				if((B1[i]+"").equals("Infinity") || (B1[i]+"").equals("-Infinity"))	/* B1(isinf(B1)) = 0*/
//					B1[i] = 0;
//			}
//			
//			/* Bn = repmat(B1*options.lambda,1,gene).*eye(gene)*/
//			double[][] B1TwoDim = new double[B1.length][1];
//			for(int i = 0; i< B1.length; i++)
//				B1TwoDim[i][0]  = B1[i];
//			
//			double[][] optionsLambdaTwoDim = new double[1][options.getLambda().length];	/* lambda is honrizontal vector*/
//			for(int i = 0; i< options.getLambda().length; i++)
//				optionsLambdaTwoDim[0][i] = options.getLambda()[i]; 
//			
//			/* eye(gene), the identity matrix*/
//			double[][] eyeGene = new double[gene][gene];
//			for(int i = 0; i< gene; i++)
//			{
//				Arrays.fill(eyeGene[i], 0);
//				eyeGene[i][i] = 1;
//			}
//			
//			Bn = new Matrix(repmat(new Matrix(B1TwoDim).times(new Matrix(optionsLambdaTwoDim)).getArray(), 1, gene)).arrayTimes(new Matrix(eyeGene)).getArray();
//			
//			/* dof(:,ii) = gene - size(find(Ben(:,ii) == 0),1)*/
//			
//			/* size(find(Ben(:,ii) == 0),1)*/
//			int nbZerosIIthColumnBen = 0;	/* the number of zeros in iith column of Ben*/
//			for(double e: columnListOfBen.get(ii-1))
//				if(e == 0)
//					nbZerosIIthColumnBen++;
//			dof[ii-1] = gene - nbZerosIIthColumnBen;
//			
//			/* Er = Y(:,ii) - X*Ben(:,ii)- mean(Y(:,ii));*/
//			double[] Er = new double[Y.length];		/* Er is a vertical vector*/
//			iithColumnOfY = new double[Y.length];
//			for(int i = 0; i< Y.length; i++)
//				iithColumnOfY[i] = Y[i][ii-1];
//			
//			double[][] iithColumnBenTwoDim = new double[columnListOfBen.get(ii-1).length][1];
//			for(int i = 0; i< columnListOfBen.get(ii-1).length; i++)
//				iithColumnBenTwoDim[i][0] = columnListOfBen.get(ii-1)[i];
//
//			/* X*Ben(:,ii)*/
//			double[][] XTimeIIthColumnOfBen = new Matrix(X).times(new Matrix(iithColumnBenTwoDim)).getArray();	/* vertical vector*/
//			
//			double meanIIthColumnOfY = Statistic.mean(iithColumnOfY);
//			for(int i = 0; i< Y.length; i++)
//				Er[i] = iithColumnOfY[i] - XTimeIIthColumnOfBen[i][0] - meanIIthColumnOfY;
//			
//			/*Va = (Er'*Er)/(size(Y,1));*/
//			double Va = 0;
//			/* Er' * Er, Er is a vertical vector, so Er' * Er is square sum of all numbers in Er*/
//			double sqSumEr = 0;
//			for(double e: Er)
//				sqSumEr += e * e;
//			Va = sqSumEr/Y.length;
//			
//			/* W = diag(inv(A+Bn)*A*inv(A+Bn))*/
//			Matrix AMatrix = new Matrix(A);
//			Matrix BnMatrix = new Matrix(Bn);
//			Matrix invAPlusBn = (AMatrix.plus(BnMatrix)).inverse();	/* inverse of A + Bn*/
//			double[][] mMatrix = invAPlusBn.times(AMatrix).times(invAPlusBn).getArray();	/*media result*/
//			double[] W = null;
//			if(mMatrix.length != mMatrix[0].length)	/* not square, it shouldn't happen here*/
//			{
//				System.out.println("WARNING: main_EN_sparsVAR_FDR: unsquare matrix");
//				System.exit(1);
//			}
//			else
//			{
//				W = new double[mMatrix.length];
//				for(int i = 0; i< mMatrix.length; i++)
//					W[i] = mMatrix[i][i];
//			}
//			
//			/* T(:,ii) = Ben(:,ii)./sqrt(Va*W);*/
//			double[] iithColumnOfT = new double[columnListOfBen.get(0).length];	/* same length of column of Ben matrix*/
//			double[] sqrtOfVaTimesW = new double[W.length];/* sqrt(Va * W), same length of W*/
//			for(int i = 0; i< W.length; i++)
//				sqrtOfVaTimesW[i] = Math.sqrt(Va * W[i]);
//			for(int i = 0; i< columnListOfBen.get(0).length; i++)
//				iithColumnOfT[i] = columnListOfBen.get(ii-1)[i]/sqrtOfVaTimesW[i];
//			columnListOfT.add(iithColumnOfT);
//		}
//		
//		/* set Matrix Ben*/
//		Ben = new double[columnListOfBen.get(0).length][columnListOfBen.size()];
//		for(int j = 0; j< columnListOfBen.size(); j++)
//			for(int i = 0; i< columnListOfBen.get(j).length; i++)
//				Ben[i][j] = columnListOfBen.get(j)[i];
//		
//		/* set matrix T*/
//		T = new double[columnListOfT.get(0).length][columnListOfT.size()];
//		for(int j = 0; j< columnListOfT.size(); j++)
//			for(int i = 0; i< columnListOfT.get(j).length; i++)
//				T[i][j] = columnListOfT.get(j)[i];
//		
//		/* return result*/
//		return new main_EN_sparsVAR_FDRResult(Ben, T, dof);
//	}
	
	
//	public static int[][] FDR_signi(double[][] T, int[] dof)
//	{
//		/* g = size(X, 1)*/
//		int g = T.length;
//		
//		/* Q2 = zeros(g,g)*/
//		double[][] Q2 = Util.zeros(g, g);
//		
//		
//	}
	
	/**
	 * self-implement version of ridge funtion in Matlab
	 * 
	 * @param y
	 * @param X
	 * @param k
	 * @param flag
	 * @return
	 */
	public static double[][] ridge(double[] y, double[][] X, double[] k, boolean flag)
	{
		//TODO:
//		if nargin < 3,              
//	    error(message('stats:ridge:TooFewInputs'));      
//		end 
//	
//		if nargin<4 || isempty(flag) || isequal(flag,1)
//		   unscale = false;
//		elseif isequal(flag,0)
//		   unscale = true;
//		else
//		   error(message('stats:ridge:BadScalingFlag'));
//		end
		
		//[n, p] = size(X)
		int n = X.length;
		int p = X[0].length;
		
		//[n1,collhs] = size(y);
		int n1 = y.length;
		int collhs = 1;			/* it seems y here is a n * 1 column vector*/
		
		if(n != n1) {
			System.out.println("ERROR: stats:ridge:InputSizeMismatch");
			System.exit(1);
		}
		
		/* remove any missing values*/
		//TODO
//		wasnan = (isnan(y) | any(isnan(X),2));
//		if (any(wasnan))
//		   y(wasnan) = [];
//		   X(wasnan,:) = [];
//		   n = length(y);
//		end
		
		/* normalize the columns of X to mean zero, and standard deviation one*/
		double[] mx = birc.grni.util.StatisticMatrix.mean(X, 1);
		double[] stdx = birc.grni.util.StatisticMatrix.std(X, 0, 1);
		/* idx = find(abs(stdx) < sqrt(eps(class(stdx)))); */
		ArrayList<Integer> idxList = new ArrayList<Integer>();
		double threshold = Math.sqrt(Math.pow(2, -52));/* 
														* stdx is a double array
														* eps('double') = 2^-52
														* eps('single') = 2^-23
		 												*/
		for(int i = 0; i< stdx.length; i++)
			if(Math.abs(stdx[i]) < threshold)
				idxList.add(i+1);	/* index start from 1 in matlab*/
		/* transfer to array*/
		int[] idx = new int[idxList.size()];
		for(int i = 0; i< idxList.size(); i++)
			idx[i] = idxList.get(i);
		
		if(idx.length != 0)	/* any(idx)*/
		{
			/* stdx(idx) = 1*/
			for(int i = 0; i< idx.length; i++)
				stdx[idx[i] - 1] = 1;	/* indices in idx starts from 1*/
		}
		
		/* MX = mx(ones(n,1),:), copy mx n times*/
		double[][] MX = new double[n][mx.length];
		for(int i = 0; i< n; i++)
			for(int j = 0; j< mx.length; j++)
				MX[i][j] = mx[j];
		
		/* STDX = stdx(ones(n,1),:)*/
		double[][] STDX = new double[n][stdx.length];
		for(int i = 0; i< n; i++)
			for(int j = 0; j< stdx.length; j++)
				STDX[i][j] = stdx[j];
		
		/* Z = (X - MX) ./ STDX*/
		double[][] Z = new double[X.length][X[0].length];
		for(int i = 0; i< X.length; i++)
			for(int j = 0; j< X[0].length; j++)
				Z[i][j] = (X[i][j] - MX[i][j])/STDX[i][j];
		
		if(idx.length != 0)	/* any(idx)*/
		{
			/* Z(:, idx) = 1*/
			for(int j = 0; j< idx.length; j++)
				for(int i = 0; i< Z.length; i++)
					Z[i][idx[j] - 1] = 1;
		}
		
		/*
		 * Compute the ridge coefficient estimates using the technique of
		 * adding pseudo observations having y=0 and X'X = k*I.
		 * */
		/* pseudo = sqrt(k(1)) * eye(p)*/
		double[][] pseudo = new double[p][p];
		for(int i = 0; i<p; i++)
		{
			Arrays.fill(pseudo[i], 0);
			pseudo[i][i] = Math.sqrt(k[0]);
		}
		
		/* Zplus = [Z;pseudo]*/
		double[][] Zplus = new double[Z.length + pseudo.length][pseudo[0].length];
		for(int i = 0; i< Z.length; i++)
			for(int j = 0; j< Z[0].length; j++)
				Zplus[i][j] = Z[i][j];
		for(int i = Z.length; i< Z.length + pseudo.length; i++)
			for(int j = 0; j< pseudo[0].length; j++)
				Zplus[i][j] = pseudo[i - Z.length][j];
		
		/* yplus  = [y;zeros(p,1)], yplus is vertical actually*/
		double[] yplus = new double[y.length + p];
		for(int i = 0; i< y.length; i++)
			yplus[i] = y[i];
		for(int i = y.length; i< y.length + p; i++)
			yplus[i] = 0;
		
		/*
		 * Set up an array to hold the results
		 * nk = numel(k)
		 * */
		int nk = k.length;
		
		/* compute the coefficient estimates
		 * b = Zplus\yplus, that Zplus * b = yplus
		 * */
		double[][] yplusTwoDim = new double[yplus.length][1];
		for(int i = 0; i< yplus.length; i++)
			yplusTwoDim[i][0] = yplus[i];
		double[][] b = (new Matrix(Zplus)).solve(new Matrix(yplusTwoDim)).getArray();
		
		if(nk > 1)
		{
			/*
			 * Fill in more entries after first expanding b.  We did not pre-
   			 * allocate b because we want the backslash above to determine its class.
			 * */
			//TODO: 
			//Q: it seems that in our implementation, nk cannot greater than 1, b is just a vertical vector
//			b(end,nk) = 0;
//			for j=2:nk
//				Zplus(end-p+1:end,:) = sqrt(k(j)) * eye(p);
//		      	b(:,j) = Zplus\yplus;
//		    end
		}
		
		/* Put on original scale if requested*/
		/*if unscale
		   b = b ./ repmat(stdx',1,nk);
		   b = [mean(y)-mx*b; b];
		end*/
		if(!flag)	/*  unscale = true*/
		{
			double[][] stdxTwoDim = new double[1][stdx.length];
			double[][] stdxTwoDimTranpose = new Matrix(stdxTwoDim).transpose().getArray();
			b = new Matrix(b).arrayRightDivide(new Matrix(birc.grni.en.CommonFunction.repmat(stdxTwoDimTranpose, 1, nk))).getArray();
			
			double[][] newB = new double[b.length+1][b[0].length];	/* one more row than b*/
		
			double[][] mxTwoDim = new double[1][mx.length];	/* mx is a horizontal vector*/
			for(int i = 0; i< mx.length; i++)
				mxTwoDim[0][i] = mx[i];
			/* the newly added element in b*/
			newB[0][0] = birc.grni.util.Statistic.mean(y) - new Matrix(mxTwoDim).times(new Matrix(b)).getArray()[0][0];	/* mean(y)-mx*b, 
																											 * Q: it seems that mx * b only gets one number*/
			for(int i = 0; i< b.length; i++)	/* copy the original b into newB*/
				newB[i+1][0] = b[i][0];
			b = newB;
		}
			
		return b;
	}
}
//	
//	public static CVErr cvglmnet(double[][] x, double[] y, int nfolds, int[] foldid, String type, String family, GlmnetOptions options, double[][] verbous)
//	{
//		int N = 0;
//		double[][] predmat = null;
//		int[] which = null;
//		GlmnetResult cvfit = null;
//		double[][] YY = null;
//		double[][] cvraw = null;
//		double[] semin = null;
//		
//		GlmnetParameters glmnetParameters = new GlmnetParameters(x, y, family,options);
//		GlmnetResult glmnetObject = glmnet(glmnetParameters); 
//		options.setLambda(glmnetObject.getLambda());
//		options.setNlambda(options.getLambda().length);
//		
//		N = x.length;
//		
//		if(foldid.length == 0)
//		{
//			/* foldid = randsample([repmat(1:nfolds,1,floor(N/nfolds)) 1:mod(N,nfolds)],N)*/
//			/* according to original matlab code, N and nfolds are rows of input Matrix X and Y
//			 * seperately. Since X and Y have same number of rows, so, N and nfolds must be same
//			 * so repmat(1:nfolds,1,floor(N/nfolds)) 1:mod(N,nfolds) just equals 1:nfolds vector.
//			 * 
//			 * randsample(1:nfolds, N) is same as randperm(N) when N equals nfolds 
//			 **/ 
//			foldid = randperm(N, mtRd);
//		}
//		else
//		{
//			int[] foldidCopy = Arrays.copyOf(foldid, foldid.length);
//			Arrays.sort(foldidCopy);
//			nfolds = foldidCopy[foldidCopy.length - 1];
//		}
//		
//		predmat = glmnetPredict(new GlmnetPredictParameters(glmnetObject, type, x, options.getLambda()));
//		
//		for(int i = 1; i<= nfolds; i++)
//		{	
//			/* which=foldid==i;*/
//			which = new int[foldid.length];
//			Arrays.fill(which, 0);
//			for(int j = 0; j< foldid.length; j++)
//				if(foldid[j] == i)
//					which[j] = 1;
//			
//			//TODO:
//			//if verbous, disp(['Fitting fold # ' num2str(i) ' of ' num2str(nfolds)]);end
//			
//			/* cvfit = glmnet(x(~which,:), y(~which),family, options);*/
//			/* count the number of 1s in ~which*/
//			int nbOnesWhich = 0;
//			for(int j = 0; j< which.length; j++)
//				nbOnesWhich += which[j];
//			int nbOnesReverseWhich = which.length - nbOnesWhich;
//			double[][] partialXReverseWhich = new double[nbOnesReverseWhich][x[0].length];
//			int counter = 0;
//			for(int j = 0; j< which.length; j++)
//			{
//				if((1 - which[j]) == 1)
//				{
//					for(int k = 0; k< x[0].length; k++)
//						partialXReverseWhich[counter][k] = x[j][k];
//					counter++;
//				}
//			}
//			
//			counter = 0;			//reset counter
//			double[] partialYReverseWhich = new double[nbOnesReverseWhich];
//			for(int j = 0; j< which.length; j++)
//			{
//				if((1 - which[j]) == 1)
//				{
//					partialYReverseWhich[counter] = y[j];
//					counter++;
//				}
//			}
//			
//			cvfit = glmnet(new GlmnetParameters(partialXReverseWhich, partialYReverseWhich, family, options));
//			
//			/* predmat(which,:) = glmnetPredict(cvfit, type,x(which,:),options.lambda);*/
//			double[][] partialXWhich = new double[nbOnesWhich][x[0].length];
//			
//			counter = 0;				/* reset counter*/
//			for(int j = 0; j< which.length; j++)
//			{
//				if(which[j] == 1)
//				{
//					for(int k = 0; k< x[0].length; k++)
//						partialXWhich[counter][k] = x[j][k];
//					counter++;
//				}
//			}
//			
//			double[][] glmnetPredictResult = glmnetPredict(new GlmnetPredictParameters(cvfit, type, partialXWhich, options.getLambda()));
//			counter = 0;				/* reset counter*/
//			for(int j = 0; j< predmat.length; j++)
//			{
//				if(which[j] == 1)
//				{
//					for(int k = 0; k< predmat[0].length; k++)
//						predmat[j][k] = glmnetPredictResult[counter][k];
//					counter++;
//				}
//			}
//		}
//		
//		double[][] yTwoDim = new double[y.length][1];
//		for(int i = 0; i< y.length; i++)
//			yTwoDim[i][0] = y[i];
//		YY = repmat(yTwoDim, 1, options.getLambda().length);
//		
//		if(family.equals("gaussian"))
//		{
//			if(YY.length != predmat.length || YY[0].length != predmat[0].length)
//			{
//				System.out.println("ERROR: YY and predmat don't have same dimension");
//				System.exit(1);
//			}
//			cvraw = new double[YY.length][YY[0].length];
//			for(int i = 0; i< cvraw.length; i++)
//			{
//				for(int j = 0; j< cvraw[0].length; j++)
//					cvraw[i][j] = Math.pow(YY[i][j] - predmat[i][j], 2);
//			}
//		}
//		else if(family.equals("binomial"))
//			;//TODO
//		else if(family.equals("multinomial"))
//			;//TODO
//		
//		CVErr cvErr = new CVErr();
//		/* CVerr.cvm=mean(cvraw);*/
//		double[] meanCVRaw = new double[cvraw[0].length];				/* row of means of each column of cvraw*/
//		for(int j = 0; j< cvraw[0].length; j++)
//		{
//			double columnSum = 0;
//			for(int i = 0; i< cvraw.length; i++)
//				columnSum += cvraw[i][j];
//			meanCVRaw[j] = columnSum/cvraw.length;
//		}
//		cvErr.setCvm(meanCVRaw);
//		
//		/* CVerr.stderr=sqrt(var(cvraw)/N);*/
//		double[] varCVRaw = new double[cvraw[0].length];
//		for(int j = 0; j< cvraw[0].length; j++)
//		{
//			double columnSum = 0;
//			for(int i = 0; i< cvraw.length; i++)
//				columnSum += cvraw[i][j];
//			double columnMean = columnSum/cvraw.length;
//			
//			double columnVar = 0;
//			for(int i = 0; i< cvraw.length; i++)
//				columnVar += Math.pow((cvraw[i][j] - columnMean), 2);
//			columnVar = columnVar/(cvraw.length - 1);				/* unbiased*/
//			
//			varCVRaw[j] = columnVar;
//		}
//		
//		double[] varCVRawByN = new double[varCVRaw.length];
//		for(int i = 0; i< varCVRaw.length; i++)
//			varCVRawByN[i] = varCVRaw[i]/N;
//		
//		double[] stderr = new double[varCVRawByN.length];
//		for(int i = 0; i< stderr.length; i++)
//			stderr[i] = Math.sqrt(varCVRawByN[i]);
//		cvErr.setStderr(stderr);
//		
//		/* CVerr.cvlo=CVerr.cvm-CVerr.stderr;*/
//		double[] cvlo = new double[cvErr.getCvm().length];
//		for(int i = 0; i< cvlo.length; i++)
//			cvlo[i] = cvErr.getCvm()[i] - cvErr.getStderr()[i];
//		cvErr.setCvlo(cvlo);
//		
//		/* CVerr.cvup=CVerr.cvm+CVerr.stderr;*/
//		double[] cvup = new double[cvErr.getCvm().length];
//		for(int i = 0; i< cvlo.length; i++)
//			cvup[i] = cvErr.getCvm()[i] + cvErr.getStderr()[i];
//		cvErr.setCvup(cvup);
//		
//		/* CVerr.lambda_min=max(options.lambda(CVerr.cvm<=min(CVerr.cvm)));*/
//		//TODO: implement new_vector = vector(logic_vector)
//		/* if there are several minima, choose largest lambda of the smallest cvm*/
//		double[] copyOfCvm = Arrays.copyOf(cvErr.getCvm(), cvErr.getCvm().length); 			/* copy of CVerr.cvm for sort and find min*/
//		Arrays.sort(copyOfCvm);
//		double minCvm = copyOfCvm[0];
//		ArrayList<Integer> minPositionsList = new ArrayList<Integer>();			/* positions(start from one) of which the value is equal the minimum value*/
//		for(int i = 0; i< cvErr.getCvm().length; i++)
//			if(cvErr.getCvm()[i] <= minCvm)
//				minPositionsList.add(i+1);
//		double[] partialLambda = new double[minPositionsList.size()];			
//		for(int i = 0; i< minPositionsList.size(); i++)
//			partialLambda[i] = options.getLambda()[minPositionsList.get(i) - 1];	/* positions start from 1*/
//		double[] copyOfPartialLambda = Arrays.copyOf(partialLambda, partialLambda.length);
//		Arrays.sort(copyOfPartialLambda);
//		cvErr.setLambda_min(copyOfPartialLambda[copyOfPartialLambda.length - 1]);	/* maximum of lambda*/
//		
//		/* semin=CVerr.cvup(options.lambda==CVerr.lambda_min);*/
//		/* Find stderr for lambda(min(sterr))*/
//		ArrayList<Integer> equalPositionsList = new ArrayList<Integer>();
//		for(int i = 0; i< options.getLambda().length; i++)
//			if(options.getLambda()[i] == cvErr.getLambda_min())
//				equalPositionsList.add(i + 1);		/* start from 1*/
//		semin = new double[equalPositionsList.size()];
//		for(int i = 0; i< equalPositionsList.size(); i++)
//			semin[i] = cvErr.getCvup()[equalPositionsList.get(i) - 1];
//		
//		/* CVerr.lambda_1se=max(options.lambda(CVerr.cvm<semin));*/
//		/*
//		 * find largest lambda which has a smaller mse than the stderr belonging to
//		 * the largest of the lambda belonging to the smallest mse
//		 * In other words, this defines the uncertainty of the min-cv, and the min
//		 * cv-err could in essence be any model in this interval.
//		 * */
//		ArrayList<Integer> lessPostionsList = new ArrayList<Integer>();	/* on which postion the value of cvm is less than semin*/
//		for(int i = 0; i< cvErr.getCvm().length; i++)
//			if(cvErr.getCvm()[i] < semin[0])		//Q: it seems that semin array only has one number
//				lessPostionsList.add(i + 1);		/* start from 1*/
//		
//		double[] partialLambdaUnderLessPostionsList = new double[lessPostionsList.size()];
//		for(int i = 0; i< lessPostionsList.size(); i++)
//			partialLambdaUnderLessPostionsList[i] = options.getLambda()[lessPostionsList.get(i) - 1];
//		double copyOfPartialLambdaUnderLessPostionsList[] = Arrays.copyOf(partialLambdaUnderLessPostionsList, partialLambdaUnderLessPostionsList.length);
//		Arrays.sort(copyOfPartialLambdaUnderLessPostionsList);
//		double lambda_1se = copyOfPartialLambdaUnderLessPostionsList[copyOfPartialLambdaUnderLessPostionsList.length - 1];	/* max*/
//		cvErr.setLambda_1se(lambda_1se);
//		
//		/* CVerr.glmnetOptions=options;*/
//		cvErr.setGlmnetOptions(options);
//		
//		/* CVerr.glmnet_object = glmnet_object;*/
//		cvErr.setGlmnet_object(glmnetObject);
//		
//		return cvErr;
//	}
//	
//	/**
//	 * 
//	 * @param params
//	 * @return result
//	 */
//	public static double[][] glmnetPredict(GlmnetPredictParameters params)
//	{
//		/* result of this method*/
//		double[][] result = null;
//		
//		/* get input parameters*/
//		GlmnetResult object = params.getObject();
//		double[] s = params.getS();
//		String type = params.getType();
//		double[][] newx = params.getNewx();
//		
//		double[] a0 = object.getA0();
//		double[][] nbeta = null;
//		double[] lambda = null;
//		lambda_interpResult lamlist = null;
//		
//		if(params.getNargin() < 2)
//		{
//			params.setType("link");
//		}
//		
//		if(params.getNargin() < 3)
//		{
//			params.setNewx(new double[0][0]);
//		}
//		
//		if(params.getNargin() < 4)
//		{
//			params.setS(params.getObject().getLambda());
//		}
//		
//		if(object.getClassString().equals("elnet"))
//		{
//			/* add a0 vector to the front of "beta" field of "object" as "nbeta" matrix,
//			 * in this implementation, a0 has already been horizontal, no need to be transposed
//			 * */
//			nbeta = new double[object.getBeta().length+1][a0.length];
//			
//			/* put a0 as first row*/
//			for(int i = 0; i< nbeta[0].length ;i++)
//				nbeta[0][i] = a0[i];
//			
//			/* remaining will be the original "beta" field of "object"*/
//			for(int i = 1; i< nbeta.length; i++)
//				for(int j = 0; j< nbeta[0].length; j++)
//					nbeta[i][j] = object.getBeta()[i-1][j];
//			
//			if(params.getNargin() == 4)
//			{
//				lambda = object.getLambda();
//				lamlist = lambda_interp(lambda, s);
//				
//				/* nbeta=nbeta(:,lamlist.left).*repmat(lamlist.frac',size(nbeta,1),1) +nbeta(:,lamlist.right).*(1-repmat(lamlist.frac',size(nbeta,1),1));*/
//				double[][] fracTwoDim = new double[1][lamlist.getFrac().length];
//				for(int i = 0; i< lamlist.getFrac().length; i++)
//					fracTwoDim[0][i] = lamlist.getFrac()[i];
//				double[][] repmat = repmat(fracTwoDim, nbeta.length, 1);
//				double[][] partialLeftNbeta = new double[nbeta.length][lamlist.getLeft().length];
//				for(int i = 0; i< nbeta.length; i++)
//				{
//					for(int j = 0; j< lamlist.getLeft().length; j++)
//					{
//						partialLeftNbeta[i][j] = nbeta[i][(int)lamlist.getLeft()[j]-1];
//					}
//				}
//				double[][] partialRightNbeta = new double[nbeta.length][lamlist.getRight().length];
//				for(int i = 0; i< nbeta.length; i++)
//				{
//					for(int j = 0; j< lamlist.getRight().length; j++)
//					{
//						partialRightNbeta[i][j] = nbeta[i][(int)lamlist.getRight()[j]-1];
//					}
//				}
//				
//				/* media result*/
//				
//				/* nbeta(:,lamlist.left).*repmat(lamlist.frac',size(nbeta,1),1)
//				 * nbeta(:,lamlist.left) and repmat(lamlist.frac',size(nbeta,1),1), 
//				 * as well as the result matrix has the same row and column, 
//				 * and .* means two numbers at same position of two matrix do multiplication 
//				 * and put the result at the same position of result matrix
//				 */	
//				double[][] m1 = new double[partialLeftNbeta.length][partialLeftNbeta[0].length];
//				for(int i = 0; i< m1.length; i++)
//					for(int j = 0; j< m1[0].length; j++)
//						m1[i][j] = partialLeftNbeta[i][j] * repmat[i][j];
//				/*
//				 * nbeta(:,lamlist.right).*(1-repmat(lamlist.frac',size(nbeta,1),1)
//				 * nbeta(:,lamlist.right) and (1-repmat(lamlist.frac',size(nbeta,1),1))
//				 * as well as the result matrix has the same row and column
//				 * */
//				double[][] m2 = new double[partialRightNbeta.length][partialRightNbeta[0].length];
//				double[][] oneMinusRepmat = new double[repmat.length][repmat[0].length];
//				for(int i = 0; i< repmat.length; i++)	
//					for(int j = 0; j< repmat[0].length; j++)
//						oneMinusRepmat[i][j] = 1 - repmat[i][j];
//				for(int i = 0; i< m2.length; i++)
//					for(int j = 0; j< m2[0].length; j++)
//						m2[i][j] = partialRightNbeta[i][j] * oneMinusRepmat[i][j];
//				
//				/* final result*/
//				double[][] newNbeta = new double[m1.length][m1[0].length];
//				for(int i = 0; i< newNbeta.length; i++)
//					for(int j = 0; j< newNbeta[0].length; j++)
//						newNbeta[i][j] = m1[i][j] + m2[i][j];
//				nbeta = newNbeta;
//			}
//			
//			if(type.equals("coefficients"))
//				result = nbeta;
//			else if (type.equals("link") || type.equals("response"))
//			{
//				/* result = [ones(size(newx,1),1), newx] * nbeta;*/
//				/* put one column whose values are all 1s at the front of newx matrix*/
//				double[] newColumn = new double[newx.length];	/* new column of all 1s*/
//				Arrays.fill(newColumn, 1);
//				double[][] newNewx = new double[newx.length][newx[0].length + 1];
//				/* add new column to the front*/
//				for(int i = 0; i< newx.length; i++)
//					newNewx[i][0] = newColumn[i];
//				for(int i = 0; i< newx.length; i++)
//					for(int j = 0; j< newx[i].length; j++)
//						newNewx[i][j+1] = newx[i][j];
//				
//				/* matrix multiplication: newly-generated newx matrix times nbeta matrix*/
//				Matrix newNewxMatrix = new Matrix(newNewx);
//				Matrix nbetaMatrix = new Matrix(nbeta);
//				result = newNewxMatrix.times(nbetaMatrix).getArray();
//			}
//			else if(type.equals("nonzero"))
//			{
//				double[][] partialNbeta = new double[nbeta.length - 1][nbeta[0].length];			/* nbeta(2:size(nbeta,1),:*/
//				for(int i = 1; i< nbeta.length; i++)
//				{
//					for(int j = 0; j< nbeta[0].length; j++)
//						partialNbeta[i-1][j] = nbeta[i][j];
//				}
//				result = nonzeroCoef(new nonzeroCoefParameters(partialNbeta, true));
//			}
//			else
//			{
//				System.out.println("ERROR: Unrecognized type");
//				System.exit(1);
//			}
//		}
//		else if(object.getClassString().equals("lognet"))
//		{
//			System.out.println("ERROR: lognet unimplemented!");
//			System.exit(1);
//			//TODO
//		}
//		else if(object.getClassString().equals("multnet"))
//		{
//			System.out.println("ERROR: multnet unimplemented!");
//			System.exit(1);
//			//TODO
//		}
//		else
//		{
//			System.out.println("ERROR: Unrecognized type");
//			System.exit(1);
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * 
//	 * @param params
//	 * @return result
//	 */
//	private static double[][] nonzeroCoef(nonzeroCoefParameters params)
//	{
//		/* input parameters*/
//		double[][] beta = params.getBeta();
//		boolean bystep = params.getBystep();
//		
//		double[][] result = null;
//		
//		if(params.getNargin() < 2)
//			params.setBystep(false);
//		
//		/* result = abs(beta)>0;*/
//		result = new double[beta.length][beta[0].length];
//		for(int i = 0; i< beta.length; i++)
//			for(int j = 0; j< beta[0].length; j++)
//				if(Math.abs(beta[i][j]) > 0)
//					result[i][j] = 1;
//				else
//					result[i][j] = 0;
//		if(!bystep)
//			/* result = any(result, 2)*/
//		{
//			double[][] newResult = new double[result.length][1];			/* newResult is actually a column vector*/
//			/* initialize to all 0s*/
//			for(int i = 0; i< newResult.length; i++)
//				newResult[i][0] = 0;
//			
//			for(int i = 0; i< result.length; i++)
//			{
//				for(int j = 0; j< result[i].length; j++)
//					if(result[i][j] != 0)
//					{
//						newResult[i][0] = 1;
//						break;
//					}
//			}
//			
//			result = newResult;
//		}
//		
//		return result;
//	}
//	
//	/**
//	 * 
//	 * @param originalMatrix
//	 * @param row
//	 * @param column
//	 * @return repeatMatrix
//	 */
//	private static double[][] repmat(double[][] originalMatrix, int row, int column)
//	{
//		double[][] repeatMatrix = new double[row * originalMatrix.length][column * originalMatrix[0].length];
//		for(int i = 0; i< row; i++)
//		{
//			for(int j = 0; j< column; j++)
//			{
//				for(int m = 0; m< originalMatrix.length; m++)
//				{
//					for(int n = 0; n< originalMatrix[0].length; n++)
//					{
//						repeatMatrix[i * originalMatrix.length + m][j * originalMatrix[0].length + n] = originalMatrix[m][n];
//					}
//				}
//			}
//		}
//		
//		return repeatMatrix;
//	}
//	
//	private static lambda_interpResult lambda_interp(double[] lambda, double[] s)
//	{
//		int nums = -1;
//		double[] left = null;
//		double[] right = null;
//		double[] sfrac = null;
//		double[] coord = null;
//		
//		if(lambda.length == 1) /* degenerate case of only one lambda*/
//		{
//			nums = s.length;
//			/* left = ones(nums, 1), left is actually a vertical vector*/
//			left = new double[nums];
//			Arrays.fill(left, 1);
//			right = left;
//			/* sfrac = ones(nums, 1)*/
//			sfrac = new double[nums];
//			Arrays.fill(sfrac, 1);
//		}
//		else
//		{
//			/* find the maximum and minimum number in lambda*/
//			double[] copyOfLambda = Arrays.copyOf(lambda, lambda.length);
//			Arrays.sort(copyOfLambda);
//			double maxLambda = copyOfLambda[copyOfLambda.length - 1];
//			double minLambda = copyOfLambda[0];
//			/*  
//			 * s(s > max(lambda)) = max(lambda);
//			 * s(s < min(lambda)) = min(lambda);
//			 * */
//			for(int i = 0; i< s.length; i++)
//				if(s[i] > maxLambda)
//					s[i] = maxLambda;
//			for(int i = 0; i< s.length; i++)
//				if(s[i] < minLambda)
//					s[i] = minLambda;
//			
//			int k = lambda.length;
//			
//			/* sfrac =(lambda(1)-s)/(lambda(1) - lambda(k));*/
//			sfrac = new double[s.length];
//			for(int i = 0; i< s.length; i++)
//				sfrac[i] = (lambda[0] - s[i]) / (lambda[0] - lambda[k-1]);
//			
//			/* lambda = (lambda(1) - lambda)/(lambda(1) - lambda(k));*/
//			double[] newLambda = new double[lambda.length];
//			for(int i = 0; i< lambda.length; i++)
//				newLambda[i] = (lambda[0] - lambda[i]) / (lambda[0] - lambda[k-1]);
//			lambda = newLambda;
//			
//			double[][] lambdaTwoDim = new double[lambda.length][1];	/* lambda is a vertical vector*/
//			for(int i = 0; i< lambda.length; i++)
//				lambdaTwoDim[i][0] = lambda[i];
//			double[][] oneToLengthLambda = new double[1][lambda.length]; /* 1:length(lambda)*/
//			for(int i = 0; i< lambda.length; i++)
//				oneToLengthLambda[0][i] = i+1;
//			double[][] sfracTwoDim = new double[lambda.length][1];	/* sfrac is a vertical vector*/
//			for(int i = 0; i< sfrac.length; i++)
//				sfracTwoDim[i][0] = sfrac[i];
//			
//			double[][] originalCoord = interp1(lambdaTwoDim, oneToLengthLambda, sfracTwoDim);
//			
//			/* convert interp1 two dimension result vector into one dimension one*/
//			if(originalCoord.length >= originalCoord[0].length) {
//				coord = new double[originalCoord.length];
//				for(int i = 0; i< originalCoord.length; i++)
//					coord[i] = originalCoord[i][0];
//			} else {
//				coord = new double[originalCoord[0].length];
//				for(int i = 0; i< originalCoord[0].length; i++)
//					coord[i] = originalCoord[0][i];
//			}
//			
//			/* left = floor(coord);*/
//			left = new double[coord.length];
//			for(int i = 0; i< coord.length; i++)
//				left[i] = Math.floor(coord[i]);
//			/* right = ceil(coord);*/
//			right = new double[coord.length];
//			for(int i = 0; i< coord.length; i++)
//				right[i] = Math.ceil(coord[i]);
//			
//			/* sfrac=(sfrac-lambda(right))./(lambda(left) - lambda(right));*/
//			double[] newSfrac = new double[sfrac.length];
//			for(int i = 0; i< sfrac.length; i++)
//				newSfrac[i] = (sfrac[i] - lambda[(int)right[i]-1])/(lambda[(int)left[i]-1] - lambda[(int)right[i]-1]);
//			sfrac = newSfrac;
//			
//			/* sfrac(left==right)=1;*/
//			for(int i = 0; i< sfrac.length; i++)
//				if(left[i] == right[i])
//					sfrac[i] = 1;
//		}
//		
//		lambda_interpResult result = new lambda_interpResult();
//		result.setLeft(left);
//		result.setRight(right);
//		result.setFrac(sfrac);
//		return result;
//	}
//	
//	public static GlmnetResult glmnet(GlmnetParameters parameters)
//	{
//		GlmnetResult result = new GlmnetResult();
//		
//		GlmnetOptions options = parameters.getOptions();
//		int nlam = options.getNlambda();
//		double[][] x = parameters.getX();
//		double[] y = parameters.getY();
//		double[][] yTwoDim = new double[y.length][1];
//		for(int i = 0; i< y.length; i++)
//			yTwoDim[i][0] = y[i];
//		
//		//TODO: check sparse of x and y
//		
//		int nobs = x.length;
//		int nvars = x[0].length;
//		
//		if(nobs <= 1)
//		{
//			System.out.println("ERROR: at least two observations should be provided.");
//			System.exit(1);
//		}
//		
//		double[] weights = options.getWeights();
//		if(weights.length == 0)	/* weights is empty*/
//		{
//			weights = new double[nobs];
//			Arrays.fill(weights, 1);	/* weights = ones(nobs,1), weights is a vertical vector*/
//		}
//		
//		double[][] maxit = options.getMaxit();
//		
//		double nulldev = 0;
//		int ka = 0;
//		int ne = 0;
//		double[] exclude = null;
//		double[][] jd = null;				
//		double[] vp = null;
//		double isd = 0;
//		double thresh = 0;
//		double[] lambda = null;
//		double lambda_min = 0;
//		double flmin = 0;
//		double[][] ulam = null;
//		double parm = 0;
//		ElnetResult elnetResult = null;
//		int lmu = 0;
//		double ninmax = 0;
//		double[] lam = null;
//		ErrOutput errmsg = null;
//		int[] dd = null;
//		double[][] beta = null;
//		int[] df = null;
//		double[][] ca = null;
//		double[] ia = null;
//		double[] ja = null;
//		
//		String family = parameters.getFamily();
//		if(family.equals("binomial") || family.equals("multinomial"))
//		{
//			//TODO:
//		}
//		else if(family.equals("gaussian"))
//		{
//			/* compute the null deviance*/
//			
//			/*ybar = y' * weights/ sum(weights);*/
//			double[][] weightsTwoDim = new double[weights.length][1];
//			for(int i = 0; i< weights.length; i++)
//				weightsTwoDim[i][0] = weights[i];
//			
//			Matrix matrixWeights = new Matrix(weightsTwoDim);
//			Matrix matrixY = new Matrix(yTwoDim);
//			Matrix matrixTY = matrixY.transpose();	/* transpose matrix of y*/
//			
//			/* sum(weights), sum of weights*/
//			double sumWeights = 0;
//			for(int i = 0; i< weights.length; i++)
//				sumWeights += weights[i];
//			
//			Matrix matrixYbar = matrixTY.times(matrixWeights).times(1/sumWeights);
//			double ybar = matrixYbar.getArray()[0][0];//ybar is a 1 * 1 matrix
//			
//			/* nulldev = (y' - ybar).^2 * weights / sum(weights);*/
//			double[][] tY = matrixTY.getArray();
//			
//			/* median result: (y' - ybar).^2*/
//			double[][] m = new double[1][tY[0].length]; 
//			for(int i = 0; i< tY[0].length; i++)
//				m[0][i] = Math.pow(tY[0][i] - ybar, 2);
//			
//			Matrix matrixM = new Matrix(m);
//			Matrix matrixNulldev = matrixM.times(matrixWeights).times(1/sumWeights);
//			nulldev = matrixNulldev.getArray()[0][0];//Q: 1 * 1
//			
//			ka = -1;
//			if(options.getType().equals("covariance"))
//				ka = 1;
//			else if (options.getType().equals("naive"))
//				ka = 2;
//			else
//			{
//				System.out.println("ERROR: unrecognized type");
//				System.exit(1);
//			}
//			
//			ne = options.getDfmax();
//			if(ne == 0)
//				ne = nvars + 1;
//			
//			double nx = options.getPmax();
//			if((int)nx == 0)
//			{
//				if(ne * 1.2 < nvars)
//					nx = ne * 1.2;
//				else
//					nx = nvars;
//			}
//			
//			exclude = options.getExclude();			/*exclude is a vertical vector*/
//			jd = null;								/*jd is a vertical vector*/
//			if(exclude.length != 0) 			/* exclude is not empty*/
//			{	
//				/* unique*/
//				HashSet<Double> uniqueExcludeSet = new HashSet<Double>();
//				for(double eExclude : exclude)
//					uniqueExcludeSet.add(eExclude);
//				ArrayList<Double> uniqueExcludeList = new ArrayList<Double>(uniqueExcludeSet);
//				/* sort*/
//				Collections.sort(uniqueExcludeList);
//				
//				/* copy back*/
//				double[] uniqueSortedExclude = new double[uniqueExcludeList.size()];
//				for(int j = 0; j< uniqueExcludeList.size(); j++)
//					uniqueSortedExclude[j] = uniqueExcludeList.get(j);
//				exclude = uniqueSortedExclude;
//				
//				for(double eExclude : exclude)
//					if(!(eExclude > 0 && eExclude <= nvars))
//					{
//						System.out.println("ERROR: Some excluded variables out of range");
//						System.exit(1);
//					}
//				jd = new double[exclude.length + 1][1];
//				/* connect*/
//				jd[0][0] = exclude.length;
//				for(int j = 0; j< exclude.length; j++)
//					jd[j+1][0] = exclude[j];
//			}
//			else
//			{
//				jd = new double[][]{{0}};
//			}
//			
//			vp = options.getPenalty_factor();				/* vp is a vertical vector*/
//			if(vp.length == 0)
//			{
//				vp = new double[nvars];
//				for(int j = 0; j< vp.length; j++)
//					vp[j] = 1;
//			}
//			
//			isd = -1;
//			if(options.getStandardize())
//				isd = 1;
//			else
//				isd = 0;
//			
//			thresh = options.getThresh();
//			lambda = options.getLambda();
//			lambda_min = options.getLambda_min();
//			if(lambda_min == 0)
//				if(nobs < nvars)
//					lambda_min = 5e-2;
//				else
//					lambda_min = 1e-4;
//			
//			flmin = 0;
//			ulam = null;
//			if(lambda.length == 0)
//			{
//				if(lambda_min >= 1)
//				{
//					System.out.println("ERROR: lambda_min should be less than 1");
//					System.exit(1);
//				}
//				flmin = lambda_min;
//				ulam = new double[][]{{0}};
//			}
//			else
//			{
//				flmin = 1.0;
//				for(double eLambda: lambda)
//					if(eLambda < 0)
//					{
//						System.out.println("ERROR: lambdas should be non-negative");
//						System.exit(1);
//					}
//				/* sort lambda in descending order, store it to ulam*/
//				double[] ulamAsc = Arrays.copyOf(lambda, lambda.length);	/* copy lambda to ulamAsc*/
//				double[] ulamDesc = new double[lambda.length];
//				Arrays.sort(ulamAsc);
//				for(int j = 0; j< ulamAsc.length; j++)
//					ulamDesc[j] = ulamAsc[ulamAsc.length - j - 1];
//				/* ulam = -sort(-lambda), lambda and ulam are both vertical vector*/
//				ulam = new double[ulamDesc.length][1];
//				for(int j = 0; j< ulamDesc.length; j++)
//					ulam[j][0] = ulamDesc[j];
//				nlam = lambda.length;	
//			}
//			
//			/* get input*/
//			
//			parm = options.getAlpha();
//			float parm_elnet = (float)parm;
//			
//			/*convert two dimension matrix x into one dimension vector (column-wise)*/
//			float[] x_elnet = new float[x.length * x[0].length];
//			for(int j = 0; j< x[0].length; j++)
//				for(int i = 0; i< x.length; i++)
//				x_elnet[j * x.length + i] = (float)x[i][j];
//			
//			float[] y_elnet = Util.doubleToSingleVector(y, y.length);
//			
//			/*convert two dimension matrix jd into one dimension vector (column-wise)*/
//			int[] jd_elnet = new int[jd.length * jd[0].length];
//			for(int j = 0; j< jd[0].length; j++)
//				for(int i = 0; i< jd.length; i++)
//					jd_elnet[j * x.length + i] = (int)jd[i][j];
//			
//			float[] vp_elnet = Util.doubleToSingleVector(vp, vp.length);
//			int ne_elnet = ne;
//			int nx_elnet = (int)nx;
//			int nlam_elnet = nlam;
//			float flmin_elnet = (float)flmin;
//			
//			float[] ulam_elnet = new float[ulam.length * ulam[0].length];
//			for(int j = 0; j< ulam[0].length; j++)
//				for(int i = 0; i< ulam.length; i++)
//					ulam_elnet[j * x.length + i] = (float)ulam[i][j];
//			
//			float thr_elnet = (float)thresh;
//			int isd_elnet = (int)isd;
//			float[] w_elnet = Util.doubleToSingleVector(weights, weights.length);
//			int ka_elnet = ka;
//			
//			/* call native*/
//			elnetResult = new ElnetResult();
//			if(family.equals("gaussian"))
//				elnet(parm_elnet, x_elnet, x.length, x[0].length, y_elnet, jd_elnet, vp_elnet, ne_elnet, nx_elnet, nlam_elnet, flmin_elnet, ulam_elnet, thr_elnet, isd_elnet, w_elnet, ka_elnet, elnetResult);
//			else
//				;//TODO:
//			
//			/* prepare output*/
//			lmu = elnetResult.getAlm().length;
//			double[] copyOfNin = Arrays.copyOf(elnetResult.getNin(), elnetResult.getNin().length);
//			Arrays.sort(copyOfNin);
//			ninmax = copyOfNin[copyOfNin.length-1];
//			
//			lam = elnetResult.getAlm();
//			if(options.getLambda().length == 0)
//				lam = fix_lam(lam);
//			
//			errmsg = err(elnetResult.getJerr(), options.getMaxit(), (int)nx);
//			if(errmsg.getN() == 1)
//			{
//				System.out.println("ERROR: " + errmsg.getMsg());
//				System.exit(1);
//			}
//			else if(errmsg.getN() == -1)
//			{
//				System.out.println("WARNING: "+errmsg.getMsg());
//			}
//			
//			if(family.equals("multinomial"))
//				;//TODO
//			else
//			{
//				dd = new int[]{nvars, lmu};
//				if(ninmax > 0)
//				{
//					/* extract 0 - (ninmax-1) rows of original ca*/
//					ca = elnetResult.getCa();
//					double[][] newCa = new double[(int)ninmax][ca[0].length];
//					for(int k = 0; k< (int)ninmax; k++)
//					{
//						for(int kk = 0; kk< ca[0].length; kk++)
//							newCa[k][kk] = ca[k][kk];
//					}
//					ca = newCa;
//					
//					/* df = sum(abs(ca) > 0, 1)*/
//					double[][] absCa = new double[ca.length][ca[0].length];
//					for(int k = 0; k< ca.length; k++)
//						for(int kk = 0; kk< ca[0].length; kk++)
//							absCa[k][kk] = Math.abs(ca[k][kk]);
//					int[][] gtZero = new int[absCa.length][absCa[0].length];
//					for(int k = 0; k< absCa.length; k++)
//						for(int kk = 0; kk< absCa[0].length; kk++)
//							if(absCa[k][kk] > 0)
//								gtZero[k][kk] = 1;
//							else
//								gtZero[k][kk] = 0;
//					
//					df = new int[gtZero[0].length];
//					for(int k = 0; k< gtZero.length; k++)
//						for(int kk = 0; kk< gtZero[0].length; kk++)
//							df[kk] += gtZero[k][kk];
//					
//					/* ja = ia(1:ninmax)*/
//					ia = elnetResult.getIa();
//					ja = new double[(int)ninmax];
//					for(int k = 0; k< (int)ninmax; k++)
//						ja[k] = ia[k];
//					double[][] jaAndPos = new double[ja.length][2];
//					for(int k = 0; k< ja.length; k++)
//					{
//						jaAndPos[k][0] = ja[k];
//						jaAndPos[k][1] = k+1;
//					}
//					double[][] sortedJaAndPos = mySort(jaAndPos, true);				/* ascending order*/
//					double[] ja1 = new double[sortedJaAndPos.length]; 				/* sorted ja elements*/
//					int[] oja = new int[sortedJaAndPos.length];						/* sorted ja elements postions*/
//					for(int k = 0; k< sortedJaAndPos.length; k++)
//						ja1[k] = sortedJaAndPos[k][0];
//					for(int k = 0; k< sortedJaAndPos.length; k++)
//						oja[k] = (int)sortedJaAndPos[k][1];
//					
//					beta = Util.zeros(nvars, lmu);
//					/* beta (ja1, :) = ca(oja,:)*/
//					for(int k = 0; k< ja1.length; k++)
//						for(int kk = 0; kk< ca[0].length; kk++)
//							beta[(int)ja1[k]-1][kk] = ca[oja[k]-1][kk];
//				}
//				else
//				{
//					/* beta = zeros(nvars,lmu);*/
//					beta = Util.zeros(nvars, lmu);
//					/* df = zeros(1,lmu);*/
//					df = new int[lmu];
//					Arrays.fill(df, 0);
//				}
//				
//				if(family.equals("binomial"))
//					;//TODO:
//				else
//				{
//					result.setA0(elnetResult.getA0());
//					result.setBeta(beta);
//					result.setDev(elnetResult.getRsq());
//					result.setNulldev(nulldev);
//					result.setDf(df);
//					result.setLambda(lam);
//					result.setNpasses(elnetResult.getNlp());
//					result.setJerr(elnetResult.getJerr());
//					result.setDim(dd);
//					result.setClassString("elnet");
//				}
//			}
//		}
//		else
//		{
//			System.out.println("ERROR: unrecognized family");
//			System.exit(1);
//		}
//		
//		return result;
//	}
//	
//	/** 
//	 * We found a method of random permutation that can generate same result as matlab 
//	 * randperm does, that is, assigns a random number to each element of the set to be 
//	 * shuffled and then sorts the set according to the assigned numbers, the original 
//	 * position of each assigned number in the newly-sorted list is a random permutation
//	 * we want. However, this method has worse asymptotic time complexity: sorting is O(n log n) 
//	 * which is worse than O(n) of Fisher-Yates algorithm which is used in java <code> Collections.shuffle(List<?> list, Random rnd) </code> method.
//	 * 
//	 * It seems that randperm in more recent version of Matlab, say, R2013b, has O(n) time-complexity. 
//	 * However, it is a build-in function, we cannot access the source code, so, we decide to use <code> Collections.shuffle(List<?> list, Random rnd) </code> 
//	 * method instead, since we have already tested our program when we generated same random permuation with original matlab program, the final result
//	 * is same, therefore, though the final result is not same after we use Collections.shuffle(), 
//	 * it is just caused by different random permutation of same set of numbers, it's not an error.
//	 * 
//	 * BTW, the original Matlab code uses MersenneTwister random number generation algorihtm 
//	 * which is different with current algorithm used in java 1.7 source code, we use the same one
//	 * 
//	 * The final thing is, Collections.shuffle(List<?> list, Random rnd) in java 1.7 source
//	 * is designed for List<?>, we just implement the same algorithm on our array input
//	 * 
//	 * @param N
//	 * @param mtRd MersenneTwister
//	 * @return random permuation of number set of 1 to N (both inclusive)
//	 */
//	public static int[] randperm(int N, MersenneTwister mtRd)
//	{
//		//TEST: to test other parts of program, still use the O(n log n) algorithm which can generate same random permutation as matlab randperm()
//		double[][] vectorWithPos = new double[N][2];
//		for(int i = 0; i< N; i++)
//		{
//			vectorWithPos[i][0] = mtRd.nextDouble();
//			vectorWithPos[i][1] = i+1;
//		}
//		
//		double[][] sortedVectorWithPos = mySort(vectorWithPos, true/* ascending*/);
//		
//		int[] result = new int[N];
//		for(int i = 0; i< N; i++)
//			result[i] = (int)sortedVectorWithPos[i][1];
//		
//		return result;
//		
////		/* generate initial number set from 1 to N*/
////		int[] vector = new int[N];
////		for(int i = 0; i< N; i++)
////			vector[i] = i+1;
////		
////		/* shuffle*/
////		shuffle(vector, mtRd);
////		
////		return vector;
//	}
//	
//	private static void shuffle(int[] vector, MersenneTwister mtRd)
//	{
//		int size = vector.length;
//		// Shuffle array
//        for (int i=size; i>1; i--)
//            swap(vector, i-1, mtRd.nextInt(i));
//	}
//	
//	/**
//     * Swaps the two specified elements in the specified array.
//     */
//    private static void swap(int[] arr, int i, int j) {
//        int tmp = arr[i];
//        arr[i] = arr[j];
//        arr[j] = tmp;
//    }
//	
//	/**
//	 * implement interp1 in matlab (1-D interpolation or table look-up) with 
//	 * three parameters and linear method, that is what we use in the original 
//	 * elastic net matlab code
//	 * 
//	 * @param X X is a vector
//	 * @param V V is a vector
//	 * @param Xq
//	 * @return
//	 */
//	public static double[][] interp1(double[][] X, double[][] V, double[][] Xq)
//	{
//		/* input argument check, X and V must both be vector*/
//		if(!(X.length == 1 || X[0].length == 1))
//		{
//			System.out.println("ERROR: interp1: first input X must be a vector");
//			System.exit(1);
//		}
//		
//		if(!(V.length == 1 || V[0].length == 1))
//		{
//			System.out.println("ERROR: interp1: second input V must be a vector");
//			System.exit(1);
//		}
//		
//		/* variables*/
//		int[][] idx = null;
//		int[] siz_vq = new int[2];					/* size(Xq)*/
//		
//		String method = "linear";					/* only implement this method*/
//		
//		/* orig_size_v = size(V)*/
//		int[] orig_size_v = new int[]{V.length, V[0].length};				
//		
//		/* A = A(:), if A is a horizontal vector, it will be transformed to a vertical one
//		 * otherwise, if will not change*/
//		/* V = V(:)*/
//		if(V.length < V[0].length)	/* horizontal*/
//			V = new Matrix(V).transpose().getArray();
//		/* X = X(:)*/
//		if(X.length < X[0].length)	/* horizontal*/
//			X = new Matrix(X).transpose().getArray();
//		
//		/*if any(diff(X)<0)
//	      	[X, idx] = sort(X);
//	      	V = V(idx,:);
//	      end
//	     */
//		if(X.length > X[0].length)					/* X is a vertical vector*/
//		{
//			/* diff(X)*/
//			double[] diffX = new double[X.length - 1];
//			for(int i = 0; i< X.length-1; i++)
//				diffX[i] = X[i+1][0] - X[i][0];
//			
//			/* any(diff(X)) < 0*/
//			boolean anyDiffXLessZero = false;
//			for(int i = 0; i< diffX.length; i++)
//				if(diffX[i] < 0) {
//					anyDiffXLessZero = true;
//					break;
//				}
//			
//			if(anyDiffXLessZero) {
//				double[][] XWithPos = new double[X.length][2];		/* X with position*/
//				for(int i = 0; i< X.length; i++) {
//					XWithPos[i][0] = X[i][0];
//					XWithPos[i][1] = i+1;	/* position start from 1*/
//				}
//				/* [X, idx] = sort(X)*/
//				double[][] sortedXWithPos = mySort(XWithPos, true);	/* sorted X with position, ascending*/
//				/* assign sorted X to current X (X is vertical), as well as idx*/
//				for(int i = 0; i< sortedXWithPos.length; i++)
//					X[i][0] = sortedXWithPos[i][0];
//				idx = new int[X.length][1];	/* same as X, also vertical*/
//				for(int i = 0; i< sortedXWithPos.length; i++)
//					idx[i][0] = (int)sortedXWithPos[i][1];
//				
//				/* V = V(idx,:), pick rows indicated in idx from V to compose a new V*/
//				double[][] newV = new double[idx.length][];
//				for(int i = 0; i< idx.length; i++)
//					newV[i] = Arrays.copyOf(V[idx[i][0] - 1], V[idx[i][0] - 1].length);
//				V = newV;
//			}
//		}
//		else										/* X is a horizontal vector*/
//		{
//			//TODO:
//			System.out.println("ERROR: interp1: havn't implemented when first input X is a horizontal vector");
//			System.exit(1);
//		}
//		
//		/*if isscalar(X)
//		   if isempty(Xq)
//		      varargout{1} = zeros(size(Xq));
//		      return
//		   end
//		end*/
//		if(X.length == 1 && X[0].length == 1) 
//		{
//			if(Xq.length == 0)
//				return new double[][]{};	/* in java, it is impossible for a multi-dimension array having one or more dimensions whose length are 0
//				 						 * so, just return an empty array
//				 						 */
//		}
//		
//		/* Vq = Interp1D(X,V,Xq,method), with method is linear*/
//		double[][] Vq = Interp1D(X,V,Xq);
//		
//		if(V.length == 1 || V[0].length == 1)	/* isvector(V)*/
//		{
//			siz_vq[0] = Xq.length;
//			siz_vq[1] = Xq[0].length;
//		}
//		else
//			;//TODO: 
//		
//		//TODO:varargout{1} = cast(reshape(Vq,siz_vq),superiorfloat(X,V,Xq));
//		//Q: it seems no need to implement this sentence in our application context.
//		return Vq;
//	}
//	
//	private static double[][] Interp1D(double[][] X, double[][] V, double[][] Xq/*, String method, use "linear" only*/)
//	{
//		/* based on original matlab code, in this application, X is a vertical vector, so, add a check here*/
//		if(X[0].length >1)
//		{
//			System.out.println("ERROR: Interp1D: the first argument X must be a vertical vector");
//			System.exit(1);
//		}
//		
//		/* variables*/
//		double[][] Xext = null;
//		double[][] Xqext = null;
//		
//		/* Xqcol = Xq(:)*/
//		/* A = A(:), if A is a horizontal vector, it will be transformed to a vertical one
//		 * otherwise, if will not change*/
//		double[][] Xqcol = null;
//		if(Xq.length < Xq[0].length)	/* horizontal*/
//			Xqcol = new Matrix(Xq).transpose().getArray();
//		else	/* vertical, just copy Xq*/
//		{
//			Xqcol = new double[Xq.length][];
//			for(int i = 0; i< Xq.length; i++)
//				Xqcol[i] = Arrays.copyOf(Xq[i], Xq[i].length);
//		}
//		
//		/* num_vals = size(V,2)*/
//		int num_vals = V[0].length;
//		
//		/*if (num_vals>1)
//		   Xext = {cast(X,'double'),(1:num_vals)'};
//		   Xqext = {cast(Xqcol,class(Xext{1})),Xext{2:end}};
//		else
//		   Xext = {X};
//		   Xqext = {Xqcol};
//		end*/
//		if(num_vals > 1)
//		{
//			/* Xext = {cast(X,'double'),(1:num_vals)'}*/
//			/* X is already double type, no need to cast, just connect two vectors*/
//			//TODO: current implementation dosn't allow Xext to be not a vector
//			System.out.println("ERROR: Interp1D: current implementation dosn't allow V to be not a vector");
//			System.exit(1);
////			Xext = new double[num_vals][2];	/* first column is X, second column is (1:num_vals)'*/
////			for(int i = 0; i< X.length; i++)
////				Xext[i][0] = X[i][0];
////			for(int i = 0; i< num_vals; i++)
////				Xext[i][1] = i+1;	/* start from 1*/
//		}
//		else
//		{
//			Xext = new double[X.length][];
//			Xqext = new double[Xqcol.length][];
//			/* Xext = {X}
//			 * Xqext = {Xqcol}
//			 * */
//			for(int i = 0; i< X.length; i++) {
//				Xext[i] = Arrays.copyOf(X[i], X[i].length);
//			}
//			for(int i = 0; i< Xqcol.length; i++) {
//				Xqext[i] = Arrays.copyOf(Xqcol[i], Xqcol[i].length);
//			}
//		}
//		
//		/*if ~strcmpi(method,'pchip')
//		   F = griddedInterpolant(Xext,V,method);
//		end*/
//		//TODO: use linear regression, so Xext (also V) must be a vector
//		double[] XextOneDim = null;	/* transfer to one dimension*/
//		if(Xext.length > Xext[0].length) /* vertical*/
//		{
//			XextOneDim = new double[Xext.length];
//			for(int i = 0; i< Xext.length; i++)
//				XextOneDim[i] = Xext[i][0];
//		}
//		else	/* horizontal*/
//			XextOneDim = Arrays.copyOf(Xext[0], Xext[0].length);
//		double[] VOneDim = null;	/* transfer to one dimension*/
//		if(V.length > V[0].length) /* vertical*/
//		{
//			VOneDim = new double[V.length];
//			for(int i = 0; i< V.length; i++)
//				VOneDim[i] = V[i][0];
//		}
//		else	/* horizontal*/
//			VOneDim = Arrays.copyOf(V[0], V[0].length);
//		
//		/* F = griddedInterpolant(Xext,V,method); where method is linear
//		 * Vq = F(Xqext);*/
//		LinearInterpolation F = new LinearInterpolation(XextOneDim, VOneDim);
//		double[][] Vq = null;
//		if(Xqext.length > Xqext[0].length)
//		{
//			double[] XqextOneDim = new double[Xqext.length];
//			for(int i = 0; i< Xqext.length; i++)
//				XqextOneDim[i] = Xqext[i][0];
//			double[] VqOneDim = F.getVector(XqextOneDim);
//			Vq = new double[Xqext.length][1];
//			for(int i = 0; i< Xqext.length; i++)
//				Vq[i][0] = VqOneDim[i];
//		}
//		else
//		{
//			double[] XqextOneDim = Arrays.copyOf(Xqext[0], Xqext[0].length);
//			double[] VqOneDim = F.getVector(XqextOneDim);
//			Vq = new double[1][Xqext[0].length];
//			for(int i = 0; i< Xqext[0].length; i++)
//				Vq[0][i] = VqOneDim[i];
//		}
//		
//		return Vq;
//	}
//	
//	private static double[] fix_lam(double[] lam)
//	{
//		double[] newLam = lam;
//		double[] llam = new double[lam.length];
//		for(int i = 0; i< llam.length; i++)
//			llam[i] = Math.log(lam[i]);
//		newLam[0] = Math.exp(2 * llam[1] - llam[2]);
//		return newLam;
//	}
//	
//	private static ErrOutput err(int n, double[][] maxit, int pmax)
//	{
//		ErrOutput output = new ErrOutput();
//		if(n == 0)
//		{
//			output.setN(0);
//			output.setMsg("");
//		}
//		else if (n > 0)
//		{
//			String msg = "";
//			if (n < 7777)
//				msg = "Memory allocation error; contact package maintainer";
//			else if (n == 7777) 
//				msg = "All used predictors have zero variance";
//			else if ((8000<n) && (n<9000))
//			{
//				msg = String.format("Null probability for class %d < 1.0e-5", n-8000);
//			}
//			else if ((9000<n) && (n<10000))
//				msg = String.format("Null probability for class %d > 1.0 - 1.0e-5", n-9000);
//			else if (n==10000)
//				msg = "All penalty factors are <= 0";
//			output.setN(1);
//			output.setMsg(String.format("in glmnet fortran code - %s", msg));
//		}
//		else if (n < 0) /* non fatal error*/
//		{
//			String msg = "";
//			if (n > -10000) 
//				msg = String.format("Convergence for %dth lambda value not reached after maxit=%d iterations; solutions for larger lambdas returned", -n, maxit);
//			else if (n < -10000) 
//				msg = String.format("Number of nonzero coefficients along the path exceeds pmax=%d at %dth lambda value; solutions for larger lambdas returned", pmax, -n-10000);
//			output.setN(-1);
//			output.setMsg(String.format("from glmnet fortran code - ", msg));
//		}
//		
//		return output; 
//	}
//	
//	/**
//	 *	Stable array sort, can keep positions of elements in original array, like what Matlab "sort" does	
//	 *	@param array a n * 2 array, n is the number of elements need to be sort, the second column keeps positions of elements in original array
//	 *	@return
//	 */
//	private static double[][] mySort(double[][] unSortedArray, boolean asc/* ascend or not*/) {
//        if (unSortedArray.length == 1) {
//            return unSortedArray;
//        } else {
//            double[][] arrayL = new double[unSortedArray.length / 2][];
//            double[][] arrayR = new double[unSortedArray.length - unSortedArray.length / 2][];
//            int center = unSortedArray.length / 2;
//            for (int i = 0; i < center; i++) {
//                arrayL[i] = unSortedArray[i];
//            }
//            for (int i = center, j = 0; i < unSortedArray.length; i++, j++) {
//                arrayR[j] = unSortedArray[i];
//            }
// 
//            double[][] sortedArrayL=mySort(arrayL, asc);
//            double[][] sortedArrayR=mySort(arrayR, asc);
//            double[][] sortedArray = mergeTwoList(sortedArrayL, sortedArrayR, asc);
//            return sortedArray;
//        }
//    }
// 
//	/* merge two list*/
//    private static double[][] mergeTwoList(double[][] arrayL, double[][] arrayR, boolean asc/* ascend or not*/) {
//        int i = 0, j = 0;
//        double[][] sortedArray = new double[arrayL.length + arrayR.length][];
//        int foot = 0;
//        
//        if(asc)
//        {
//        	while (i < arrayL.length && j < arrayR.length) {
//                if (arrayL[i][0] <= arrayR[j][0]) {
//                    sortedArray[foot] = arrayL[i];
//                    i++;
//                } else {
//                    sortedArray[foot] = arrayR[j];
//                    j++;
//                }
//                foot++;
//            }
//        }
//        else
//        {
//        	while (i < arrayL.length && j < arrayR.length) {
//                if (arrayL[i][0] >= arrayR[j][0]) {
//                    sortedArray[foot] = arrayL[i];
//                    i++;
//                } else {
//                    sortedArray[foot] = arrayR[j];
//                    j++;
//                }
//                foot++;
//            }
//        }
//        
//        if (i == arrayL.length) {
//            while (j < arrayR.length) {
//                sortedArray[foot++] = arrayR[j++];
//            }
//        } else { // j==arrayR.length
//            while (i < arrayL.length) {
//                sortedArray[foot++] = arrayL[i++];
//            }
//        }
//        return sortedArray;
//    }
//	
//	/* native function*/
//	public native static void elnet(
//		/* input*/
//		float parm, float[] x, int xRow, int xColumn, float[] y, int[] jd, float[] vp, int ne, int nx, int nlam, float flmin, float[] ulam, float thr, int isd, float[] w, int ka, 
//		/* output*/
//		ElnetResult elnetResult
//		);
//	
//	static
//	{
//		birc.grni.util.CommonUtil.copyDependentLibs();
//		birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "birc_grni_en_ElasticNet.dll");
//	}
//}
//
/**
 * @author liu xingliang
 *
 */
class LinearInterpolation
{
	private double[] X = null;
	private double[] Y = null;
	
	private double minX = Double.MIN_VALUE;
	private double maxX = Double.MAX_VALUE;
	
	private double minY = Double.MIN_VALUE;
	private double maxY = Double.MAX_VALUE;
	
	public LinearInterpolation(double[] X, double[] Y)
	{
		this.X = X;
		this.Y = Y;
		
		double[] XCopy = Arrays.copyOf(X, X.length);
		Arrays.sort(XCopy);
		minX = XCopy[0];
		maxX = XCopy[XCopy.length - 1];
		
		double[] YCopy = Arrays.copyOf(Y, Y.length);
		Arrays.sort(YCopy);
		minY = YCopy[0];
		maxY = YCopy[YCopy.length - 1];
	}
	
	public double getValue(double x)
	{
		if(x> maxX || x <minX)
			return Double.NaN;
		else
		{
			int indexOfX = Arrays.binarySearch(X, x);
			if(indexOfX >= 0)
				return Y[indexOfX];
			
			/* find the nearest smaller and larger number of x and corresponding y*/
			double nearestSmallerX = minX;
			double nearestLargerX = maxX;
			double nearestSmallerY = minY;
			double nearestLargerY = maxY;
			
			double[] XCopy = Arrays.copyOf(X, X.length);
			double[] YCopy = Arrays.copyOf(Y, Y.length);
			Arrays.sort(XCopy);
			Arrays.sort(YCopy);
			
			int i = 0;
			for(double e: XCopy)
				if(e >= x)
					break;
				else
					i++;
			
			if(i - 1 >= 0)
			{
				nearestSmallerX = XCopy[i - 1];
				nearestSmallerY = YCopy[i - 1];
			}
			else
			{
				nearestSmallerX = XCopy[0];
				nearestSmallerY = YCopy[0];
			}
				
			
			int j = XCopy.length - 1;
			for(; j>=0; j--)
				if(XCopy[j] <= x)
					break;
			
			if(j + 1 <= XCopy.length - 1)
			{
				nearestLargerX = XCopy[j + 1];
				nearestLargerY = YCopy[j + 1];
			}
			else
			{
				nearestLargerX = XCopy[XCopy.length - 1];
				nearestLargerY = YCopy[XCopy.length - 1];
			}
			
			return nearestSmallerY + (nearestLargerY - nearestSmallerY) * (x - nearestSmallerX) / (nearestLargerX - nearestSmallerX);
		}
	}
	
	public double[] getVector(double[] vector)
	{
		double[] result = new double[vector.length];
		for(int i = 0; i< vector.length; i++)
			result[i] = getValue(vector[i]);
		return result;
	}
}

class main_EN_sparsVAR_FDRResult
{
	private double[][] Ben = null;
	private double[][] T = null;
	private int[] dof;
	
	private int nlhs = -1;
	
	public main_EN_sparsVAR_FDRResult(double[][] Ben, double[][] T, int[] dof)
	{
		this.Ben = Ben;
		this.T = T;
		this.dof = dof;
		
		this.nlhs = 3;
	}
	
	public double[][] getT()
	{
		return this.T;
	}
	
	public int[] getDof()
	{
		return this.dof;
	}
}

class nonzeroCoefParameters
{
	private double[][] beta = null;
	private boolean bystep = false;
	
	private int nargin = -1;
	
	public nonzeroCoefParameters(double[][] beta)
	{
		this.beta = beta;
		this.nargin = 1;
	}
	
	public nonzeroCoefParameters(double[][] beta, boolean bystep)
	{
		this.beta = beta;
		this.bystep = bystep;
		this.nargin = 2;
	}

	public double[][] getBeta() {
		return beta;
	}

	public void setBeta(double[][] beta) {
		this.beta = beta;
	}

	public boolean getBystep() {
		return bystep;
	}

	public void setBystep(boolean bystep) {
		this.bystep = bystep;
	}
	
	public int getNargin() {
		return this.nargin;
	}
}


class lambda_interpResult
{
	double[] left = null;
	double[] right = null;
	double[] frac = null;
	
	public lambda_interpResult()
	{
		
	}

	public double[] getLeft() {
		return left;
	}

	public void setLeft(double[] left) {
		this.left = left;
	}

	public double[] getRight() {
		return right;
	}

	public void setRight(double[] right) {
		this.right = right;
	}

	public double[] getFrac() {
		return frac;
	}

	public void setFrac(double[] frac) {
		this.frac = frac;
	}
}
//
//class GlmnetPredictParameters
//{
//	private GlmnetResult object = null;
//	private String type = null;
//	private double[][] newx = null;
//	private double[] s = null;
//	
//	private int nargin = -1;
//	
//	public GlmnetPredictParameters(GlmnetResult object)
//	{
//		this.object = object;
//		this.nargin = 1;
//	}
//	
//	public GlmnetPredictParameters(GlmnetResult object, String type)
//	{
//		this.object = object;
//		this.type = type;
//		this.nargin = 2;
//	}
//	
//	public GlmnetPredictParameters(GlmnetResult object, String type, double[][] newx)
//	{
//		this.object = object;
//		this.type = type;
//		this.newx = newx;
//		this.nargin = 3;
//	}
//	
//	public GlmnetPredictParameters(GlmnetResult object, String type, double[][] newx, double[] s)
//	{
//		this.object = object;
//		this.type = type;
//		this.newx = newx;
//		this.s = s;
//		this.nargin = 4;
//	}
//	
//	public int getNargin()
//	{
//		return this.nargin;
//	}
//	
//	public GlmnetResult getObject() {
//		return object;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public double[][] getNewx() {
//		return newx;
//	}
//
//	public double[] getS() {
//		return s;
//	}
//	
//	public void setObject(GlmnetResult object) {
//		this.object = object;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public void setNewx(double[][] newx) {
//		this.newx = newx;
//	}
//
//	public void setS(double[] s) {
//		this.s = s;
//	}
//
//	public void setNargin(int nargin) {
//		this.nargin = nargin;
//	}
//}
//
//class GlmnetResult
//{
//	private double[] a0;
//	private double[][] beta;
//	private double[] dev;
//	private double nulldev;
//	private int[] df;
//	private double[] lambda;
//	private int npasses;
//	private int jerr;
//	private int[] dim;
//	private String classString;
//	
//	public GlmnetResult()
//	{
//		
//	}
//	
//	public double[] getA0() {
//		return a0;
//	}
//
//	public void setA0(double[] a0) {
//		this.a0 = a0;
//	}
//
//	public double[][] getBeta() {
//		return beta;
//	}
//
//	public void setBeta(double[][] beta) {
//		this.beta = beta;
//	}
//
//	public double[] getDev() {
//		return dev;
//	}
//
//	public void setDev(double[] dev) {
//		this.dev = dev;
//	}
//
//	public double getNulldev() {
//		return nulldev;
//	}
//
//	public void setNulldev(double nulldev) {
//		this.nulldev = nulldev;
//	}
//
//	public int[] getDf() {
//		return df;
//	}
//
//	public void setDf(int[] df) {
//		this.df = df;
//	}
//
//	public double[] getLambda() {
//		return lambda;
//	}
//
//	public void setLambda(double[] lambda) {
//		this.lambda = lambda;
//	}
//
//	public int getNpasses() {
//		return npasses;
//	}
//
//	public void setNpasses(int npasses) {
//		this.npasses = npasses;
//	}
//
//	public int getJerr() {
//		return jerr;
//	}
//
//	public void setJerr(int jerr) {
//		this.jerr = jerr;
//	}
//
//	public int[] getDim() {
//		return dim;
//	}
//
//	public void setDim(int[] dim) {
//		this.dim = dim;
//	}
//
//	public String getClassString() {
//		return classString;
//	}
//
//	public void setClassString(String classString) {
//		this.classString = classString;
//	}
//}
//
/**
 * output of err function
 * TODO: add to error report system later
 */
class ErrOutput
{
	private int n = 0;
	private String msg = "";
	
	public ErrOutput()
	{
		
	}
	
	public ErrOutput(int n, String msg)
	{
		this.n = n;
		this.msg = msg;
	}
	
	public int getN()
	{
		return this.n;
	}
	
	public String getMsg()
	{
		return this.msg;
	}
	
	public void setN(int n)
	{
		this.n = n;
	}
	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
//
///**
// * Parameters of glmnet method
// */
//class GlmnetParameters
//{
//	private double[][] x;
//	private double[] y;
//	private String family;
//	private GlmnetOptions options;
//	private int nargin;
//	
//	public GlmnetParameters(double[][] x, double[] y)
//	{
//		this.x = x;
//		this.y = y;
//		this.family = "gaussian";
//		this.options = new GlmnetOptions();
//		this.nargin = 2;
//	}
//	
//	public GlmnetParameters(double[][] x, double[] y, String family)
//	{
//		this.x = x;
//		this.y = y;
//		this.family = family;
//		this.options = new GlmnetOptions();
//		this.nargin = 3;
//	}
//	
//	public GlmnetParameters(double[][] x, double[] y, String family, GlmnetOptions options)
//	{
//		this.x = x;
//		this.y = y;
//		this.family = family;
//		this.options = options;
//		this.nargin = 4;
//	}	
//	
//	public double[][] getX() {
//		return x;
//	}
//
//	public double[] getY() {
//		return y;
//	}
//
//	public String getFamily() {
//		return family;
//	}
//
//	public GlmnetOptions getOptions() {
//		return options;
//	}
//
//	public int getNargin() {
//		return nargin;
//	}
//}
//
///**
// * cross validation error of glmnet model
// */
//
////TODO:
//
//class CVErr
//{
//	private double[] cvm;
//	private double[] stderr;
//	private double[] cvlo;
//	private double[] cvup;
//	private double lambda_min;
//	private double lambda_1se;
//	private GlmnetOptions glmnetOptions;
//	private GlmnetResult glmnet_object;
//	
//	public CVErr()
//	{
//		
//	}
//	
//	public CVErr(double[] cvm, double[] stderr, double[] cvlo,
//			double[] cvup, double lambda_min, double lambda_1se,
//			GlmnetOptions glmnetOptions, GlmnetResult glmnet_object) {
//		super();
//		this.cvm = cvm;
//		this.stderr = stderr;
//		this.cvlo = cvlo;
//		this.cvup = cvup;
//		this.lambda_min = lambda_min;
//		this.lambda_1se = lambda_1se;
//		this.glmnetOptions = glmnetOptions;
//		this.glmnet_object = glmnet_object;
//	}
//
//	public double[] getCvm() {
//		return cvm;
//	}
//
//	public void setCvm(double[] cvm) {
//		this.cvm = cvm;
//	}
//
//	public double[] getStderr() {
//		return stderr;
//	}
//
//	public void setStderr(double[] stderr) {
//		this.stderr = stderr;
//	}
//
//	public double[] getCvlo() {
//		return cvlo;
//	}
//
//	public void setCvlo(double[] cvlo) {
//		this.cvlo = cvlo;
//	}
//
//	public double[] getCvup() {
//		return cvup;
//	}
//
//	public void setCvup(double[] cvup) {
//		this.cvup = cvup;
//	}
//
//	public double getLambda_min() {
//		return lambda_min;
//	}
//
//	public void setLambda_min(double lambda_min) {
//		this.lambda_min = lambda_min;
//	}
//
//	public double getLambda_1se() {
//		return lambda_1se;
//	}
//
//	public void setLambda_1se(double lambda_1se) {
//		this.lambda_1se = lambda_1se;
//	}
//
//	public GlmnetOptions getGlmnetOptions() {
//		return glmnetOptions;
//	}
//
//	public void setGlmnetOptions(GlmnetOptions glmnetOptions) {
//		this.glmnetOptions = glmnetOptions;
//	}
//
//	public GlmnetResult getGlmnet_object() {
//		return glmnet_object;
//	}
//
//	public void setGlmnet_object(GlmnetResult glmnet_object) {
//		this.glmnet_object = glmnet_object;
//	}
//}
//
///**
// * options structure for glmnet
// */
//class GlmnetOptions
//{
//	private double[] weights = new double[0];
//	private double alpha = 0.1;
//	private int nlambda = 100;
//	private double lambda_min = 0;
//	private double[] lambda = new double[0];
//	private boolean standardize = true;
//	private double thresh = 1E-4;
//	private int dfmax = 0;
//	private double pmax = 0;
//	private double[] exclude = new double[0];
//	private double[] penalty_factor = new double[0];
//	private double[][] maxit = {{100}};
//	private boolean HessianExact = false;
//	private String type = "covariance";
//	
//	public GlmnetOptions()
//	{
//	}
//	
//	public GlmnetOptions(GlmnetOptions opts)
//	{
//		this.weights = opts.getWeights();
//		this.alpha = opts.getAlpha();
//		this.nlambda = opts.getNlambda();
//		this.lambda_min = opts.getLambda_min();
//		this.lambda = opts.getLambda();
//		this.standardize = opts.getStandardize();
//		this.thresh = opts.getThresh();
//		this.dfmax = opts.getDfmax();
//		this.pmax = opts.getPmax();
//		this.exclude = opts.getExclude();
//		this.penalty_factor = opts.getPenalty_factor();
//		this.maxit = opts.getMaxit();
//		this.HessianExact = opts.getHessianExact();
//		this.type = opts.getType();
//	}
//	
//	public double[] getWeights() {
//		return weights;
//	}
//
//	public void setWeights(double[] weights) {
//		this.weights = weights;
//	}
//
//	public double getAlpha() {
//		return alpha;
//	}
//
//	public void setAlpha(double alpha) {
//		this.alpha = alpha;
//	}
//
//	public int getNlambda() {
//		return nlambda;
//	}
//
//	public void setNlambda(int nlambda) {
//		this.nlambda = nlambda;
//	}
//
//	public double getLambda_min() {
//		return lambda_min;
//	}
//
//	public void setLambda_min(double lambda_min) {
//		this.lambda_min = lambda_min;
//	}
//
//	public double[] getLambda() {
//		return lambda;
//	}
//
//	public void setLambda(double[] lambda) {
//		this.lambda = lambda;
//	}
//
//	public boolean getStandardize() {
//		return standardize;
//	}
//
//	public void setStandardize(boolean standardize) {
//		this.standardize = standardize;
//	}
//
//	public double getThresh() {
//		return thresh;
//	}
//
//	public void setThresh(double thresh) {
//		this.thresh = thresh;
//	}
//
//	public int getDfmax() {
//		return dfmax;
//	}
//
//	public void setDfmax(int dfmax) {
//		this.dfmax = dfmax;
//	}
//
//	public double getPmax() {
//		return pmax;
//	}
//
//	public void setPmax(double pmax) {
//		this.pmax = pmax;
//	}
//
//	public double[] getExclude() {
//		return exclude;
//	}
//
//	public void setExclude(double[] exclude) {
//		this.exclude = exclude;
//	}
//
//	public double[] getPenalty_factor() {
//		return penalty_factor;
//	}
//
//	public void setPenalty_factor(double[] penalty_factor) {
//		this.penalty_factor = penalty_factor;
//	}
//
//	public double[][] getMaxit() {
//		return maxit;
//	}
//
//	public void setMaxit(double[][] maxit) {
//		this.maxit = maxit;
//	}
//
//	public boolean getHessianExact() {
//		return HessianExact;
//	}
//
//	public void setHessianExact(boolean hessianExact) {
//		HessianExact = hessianExact;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//}
//
///**
//	Parameters of elnet function
//*/
//// class ElnetParam
//// {
//	// private float pram;
//	// private float[] x;
//	// private float[] y;
//	// private int[] jd;
//	// private float[] vp;
//	// private int ne;
//	// private int nx;
//	// private int nlam;
//	// private float flmin;
//	// private float[] ulam;
//	// private float thr;//thresh in matlab
//	// private int isd;
//	// private float[] w;//weights in matlab
//	// private int ka;
//	
//	// private int nrhs;//preserved, no use until now
//		
//	// public ElnetParam(float pram,
//				// float[] x,
//				// float[] y,
//				// int[] jd,
//				// float[] vp,
//				// int ne,
//				// int nx,
//				// int nlam,
//				// float flmin,
//				// float[] ulam,
//				// float thr,//thresh in matlab
//				// int isd,
//				// float[] w,//weights in matlab
//				// int ka)
//	// {
//		// this.pram = pram;
//		// this.x = x;
//		// this.y = y;
//		// this.jd = jd;
//		// this.vp = vp;
//		// this.ne = ne;
//		// this.nx = nx;
//		// this.nlam = nlam;
//		// this.flmin = flmin;
//		// this.ulam = ulam;
//		// this.thr = thr;
//		// this.isd = isd;
//		// this.w = w;
//		// this.ka = ka;
//		
//		// this.nrhs = 14;
//	// }
//// }
//
///**
//	Returning structure of elnet function
//*/
//class ElnetResult
//{
//	private double[] a0;//plhs_1
//	private double[][] ca;
//	private double[] caOneDim;//plhs_2
//	private double[] ia;//plhs_3
//	private double[] nin;//plhs_4
//	private double[] rsq;//plhs_5
//	private double[] alm;//plhs_6
//	private int nlp;//plhs_7
//	private int jerr;//plhs_8
//		
//	private int nlhs;//preserved, no use until now
//		
//	public ElnetResult()
//	{
//	}
//	
//	public void setA0(double[] v, int len)
//	{	
//		this.a0 = new double[len];
//		if(len != v.length)
//		{
//			System.out.printf("ElnetResult: setA0: a0 doesn't have same length with input");
//			System.exit(1);
//		}
//		for(int i = 0; i< len; i++)
//			this.a0[i] = v[i];
//	}
//	
//	public void setCaOneDim(double[] v, int caRow, int caColumn)
//	{
//		this.caOneDim = new double[caRow * caColumn];
//		this.ca = new double[caRow][caColumn];
//		if((caRow * caColumn) != v.length)
//		{
//			System.out.printf("ElnetResult: setCaOneDim: caOneDim doesn't have same length with input");
//			System.exit(1);
//		}
//		for(int i = 0; i< caRow * caColumn; i++)
//			this.caOneDim[i] = v[i];
//		
//		/* fill ca automatically (column-wise)*/
//		for(int i = 0; i< caOneDim.length; i++)
//			ca[i%ca.length][i/ca.length] = caOneDim[i];
//	}
//	
//	public void setIa(double[] v, int len)
//	{
//		this.ia = new double[len];
//		if(len != v.length)
//		{
//			System.out.printf("ElnetResult: setIa: ia doesn't have same length with input");
//			System.exit(1);
//		}
//		for(int i = 0; i< len; i++)
//			this.ia[i] = v[i];
//	}
//	
//	public void setNin(double[] v, int len)
//	{
//		this.nin = new double[len];
//		if(len != v.length)
//		{
//			System.out.printf("ElnetResult: setNin: nin doesn't have same length with input");
//			System.exit(1);
//		}
//		for(int i = 0; i< len; i++)
//			this.nin[i] = v[i];
//	}
//	
//	public void setRsq(double[] v, int len)
//	{
//		this.rsq = new double[len];
//		if(len != v.length)
//		{
//			System.out.printf("ElnetResult: setRsq: rsq doesn't have same length with input");
//			System.exit(1);
//		}
//		for(int i = 0; i< len; i++)
//			this.rsq[i] = v[i];
//	}
//	
//	public void setAlm(double[] v, int len)
//	{
//		this.alm = new double[len];
//		if(len != v.length)
//		{
//			System.out.printf("ElnetResult: setAlm: alm doesn't have same length with input");
//			System.exit(1);
//		}
//		for(int i = 0; i< len; i++)
//			this.alm[i] = v[i];
//	}
//	
//	public void setNlp(int nlp)
//	{
//		this.nlp = nlp;
//	}
//	
//	public void setJerr(int jerr)
//	{
//		this.jerr = jerr;
//	}
//	
//	public double[] getA0()
//	{
//		return this.a0;
//	}
//	
//	public double[][] getCa()
//	{
//		return this.ca;
//	}
//	
//	public double[] getIa()
//	{
//		return this.ia;
//	}
//	
//	public double[] getNin()
//	{
//		return this.nin;
//	}
//	
//	public double[] getRsq()
//	{
//		return this.rsq;
//	}
//	
//	public double[] getAlm()
//	{
//		return this.alm;
//	}
//	
//	public int getNlp()
//	{
//		return this.nlp;
//	}
//	
//	public int getJerr()
//	{
//		return this.jerr;
//	}
//	
//	public int getNlhs()
//	{
//		return this.nlhs;
//	}
//}