package birc.grni.en;

import java.util.*;
import java.util.logging.*;
import Jama.Matrix;
import org.apache.commons.lang3.*;

public class CommonFunction {
	
	/* our lasso and elastic net algorithms on windows platform need some other .dlls to run,
	 * we need to delete those dependent .dlls when algorithm is over, so we keep record of those
	 * .dlls in this set for deleting them later*/
	//public static HashSet<String> dependentLibs = new HashSet<String>();
	
	/* set random number seed*/
	private static MersenneTwister mtRd = new MersenneTwister(0);	/* just like matlab rng('default'), which is used in original matlab code*/
	
	private static Logger logger = Logger.getLogger(CommonFunction.class.getName());
	
	public static CVErr cvglmnet(double[][] x, double[] y, int nfolds, int[] foldid, String type, String family, GlmnetOptions options, double[][] verbous)
	{
		logger.log(Level.FINE, "Begining of CommonFunction.cvglmnet");
		
		int N = 0;
		double[][] predmat = null;
		int[] which = null;
		GlmnetResult cvfit = null;
		double[][] YY = null;
		double[][] cvraw = null;
		double[] semin = null;
				
		GlmnetParameters glmnetParameters = new GlmnetParameters(x, y, family,options);
		GlmnetResult glmnetObject = glmnet(glmnetParameters); 
		options.setLambda(glmnetObject.getLambda());
		options.setNlambda(options.getLambda().length);
		
		N = x.length;
		
		if(foldid.length == 0)
		{
			/* foldid = randsample([repmat(1:nfolds,1,floor(N/nfolds)) 1:mod(N,nfolds)],N)*/
			/* according to original matlab code, N and nfolds are rows of input Matrix X and Y
			 * seperately. Since X and Y have same number of rows, so, N and nfolds must be same
			 * so repmat(1:nfolds,1,floor(N/nfolds)) 1:mod(N,nfolds) just equals 1:nfolds vector.
			 * 
			 * randsample(1:nfolds, N) is same as randperm(N) when N equals nfolds 
			 **/ 
			
			//TEST
//			MersenneTwister mtRd = new MersenneTwister(0);	/* just like matlab rng('default'), which is used in original matlab code*/
			foldid = randperm(N, mtRd);
//			foldid = new int[]{22, 6, 3, 16, 11, 7, 28, 17, 14, 8, 5, 29, 21, 25, 27, 26, 19, 15, 1, 23, 2, 4, 18, 24, 13, 9, 20, 10, 12};
		}
		else
		{
			int[] foldidCopy = Arrays.copyOf(foldid, foldid.length);
			Arrays.sort(foldidCopy);
			nfolds = foldidCopy[foldidCopy.length - 1];
		}
		
		predmat = glmnetPredict(new GlmnetPredictParameters(glmnetObject, type, x, options.getLambda()));
		
		for(int i = 1; i<= nfolds; i++)
		{	
			/* which=foldid==i;*/
			which = new int[foldid.length];
			Arrays.fill(which, 0);
			for(int j = 0; j< foldid.length; j++)
				if(foldid[j] == i)
					which[j] = 1;
			
			//TODO:
			//if verbous, disp(['Fitting fold # ' num2str(i) ' of ' num2str(nfolds)]);end
			
			/* cvfit = glmnet(x(~which,:), y(~which),family, options);*/
			/* count the number of 1s in ~which*/
			int nbOnesWhich = 0;
			for(int j = 0; j< which.length; j++)
				nbOnesWhich += which[j];
			int nbOnesReverseWhich = which.length - nbOnesWhich;
			double[][] partialXReverseWhich = new double[nbOnesReverseWhich][x[0].length];
			int counter = 0;
			for(int j = 0; j< which.length; j++)
			{
				if((1 - which[j]) == 1)
				{
					for(int k = 0; k< x[0].length; k++)
						partialXReverseWhich[counter][k] = x[j][k];
					counter++;
				}
			}
			
			counter = 0;			//reset counter
			double[] partialYReverseWhich = new double[nbOnesReverseWhich];
			for(int j = 0; j< which.length; j++)
			{
				if((1 - which[j]) == 1)
				{
					partialYReverseWhich[counter] = y[j];
					counter++;
				}
			}
					
			cvfit = glmnet(new GlmnetParameters(partialXReverseWhich, partialYReverseWhich, family, options));
			
			/* predmat(which,:) = glmnetPredict(cvfit, type,x(which,:),options.lambda);*/
			double[][] partialXWhich = new double[nbOnesWhich][x[0].length];
			
			counter = 0;				/* reset counter*/
			for(int j = 0; j< which.length; j++)
			{
				if(which[j] == 1)
				{
					for(int k = 0; k< x[0].length; k++)
						partialXWhich[counter][k] = x[j][k];
					counter++;
				}
			}
			
			double[][] glmnetPredictResult = glmnetPredict(new GlmnetPredictParameters(cvfit, type, partialXWhich, options.getLambda()));
			counter = 0;				/* reset counter*/
			for(int j = 0; j< predmat.length; j++)
			{
				if(which[j] == 1)
				{
					for(int k = 0; k< predmat[0].length; k++)
						predmat[j][k] = glmnetPredictResult[counter][k];
					counter++;
				}
			}
		}
		
		double[][] yTwoDim = new double[y.length][1];
		for(int i = 0; i< y.length; i++)
			yTwoDim[i][0] = y[i];
		YY = repmat(yTwoDim, 1, options.getLambda().length);
		
		if(family.equals("gaussian"))
		{
			if(YY.length != predmat.length || YY[0].length != predmat[0].length)
			{
				System.out.println("ERROR: YY and predmat don't have same dimension");
				System.exit(1);
			}
			cvraw = new double[YY.length][YY[0].length];
			for(int i = 0; i< cvraw.length; i++)
			{
				for(int j = 0; j< cvraw[0].length; j++)
					cvraw[i][j] = Math.pow(YY[i][j] - predmat[i][j], 2);
			}
		}
		else if(family.equals("binomial"))
			;//TODO
		else if(family.equals("multinomial"))
			;//TODO
		
		CVErr cvErr = new CVErr();
		/* CVerr.cvm=mean(cvraw);*/
		double[] meanCVRaw = new double[cvraw[0].length];				/* row of means of each column of cvraw*/
		for(int j = 0; j< cvraw[0].length; j++)
		{
			double columnSum = 0;
			for(int i = 0; i< cvraw.length; i++)
				columnSum += cvraw[i][j];
			meanCVRaw[j] = columnSum/cvraw.length;
		}
		cvErr.setCvm(meanCVRaw);
		
		/* CVerr.stderr=sqrt(var(cvraw)/N);*/
		double[] varCVRaw = new double[cvraw[0].length];
		for(int j = 0; j< cvraw[0].length; j++)
		{
			double columnSum = 0;
			for(int i = 0; i< cvraw.length; i++)
				columnSum += cvraw[i][j];
			double columnMean = columnSum/cvraw.length;
			
			double columnVar = 0;
			for(int i = 0; i< cvraw.length; i++)
				columnVar += Math.pow((cvraw[i][j] - columnMean), 2);
			columnVar = columnVar/(cvraw.length - 1);				/* unbiased*/
			
			varCVRaw[j] = columnVar;
		}
		
		double[] varCVRawByN = new double[varCVRaw.length];
		for(int i = 0; i< varCVRaw.length; i++)
			varCVRawByN[i] = varCVRaw[i]/N;
		
		double[] stderr = new double[varCVRawByN.length];
		for(int i = 0; i< stderr.length; i++)
			stderr[i] = Math.sqrt(varCVRawByN[i]);
		cvErr.setStderr(stderr);
		
		/* CVerr.cvlo=CVerr.cvm-CVerr.stderr;*/
		double[] cvlo = new double[cvErr.getCvm().length];
		for(int i = 0; i< cvlo.length; i++)
			cvlo[i] = cvErr.getCvm()[i] - cvErr.getStderr()[i];
		cvErr.setCvlo(cvlo);
		
		/* CVerr.cvup=CVerr.cvm+CVerr.stderr;*/
		double[] cvup = new double[cvErr.getCvm().length];
		for(int i = 0; i< cvlo.length; i++)
			cvup[i] = cvErr.getCvm()[i] + cvErr.getStderr()[i];
		cvErr.setCvup(cvup);
		
		/* CVerr.lambda_min=max(options.lambda(CVerr.cvm<=min(CVerr.cvm)));*/
		//TODO: implement new_vector = vector(logic_vector)
		/* if there are several minima, choose largest lambda of the smallest cvm*/
		double[] copyOfCvm = Arrays.copyOf(cvErr.getCvm(), cvErr.getCvm().length); 			/* copy of CVerr.cvm for sort and find min*/
		Arrays.sort(copyOfCvm);
		double minCvm = copyOfCvm[0];
		ArrayList<Integer> minPositionsList = new ArrayList<Integer>();			/* positions(start from one) of which the value is equal the minimum value*/
		for(int i = 0; i< cvErr.getCvm().length; i++)
			if(cvErr.getCvm()[i] <= minCvm)
				minPositionsList.add(i+1);
		double[] partialLambda = new double[minPositionsList.size()];			
		for(int i = 0; i< minPositionsList.size(); i++)
			partialLambda[i] = options.getLambda()[minPositionsList.get(i) - 1];	/* positions start from 1*/
		double[] copyOfPartialLambda = Arrays.copyOf(partialLambda, partialLambda.length);
		Arrays.sort(copyOfPartialLambda);
		cvErr.setLambda_min(copyOfPartialLambda[copyOfPartialLambda.length - 1]);	/* maximum of lambda*/
		
		/* semin=CVerr.cvup(options.lambda==CVerr.lambda_min);*/
		/* Find stderr for lambda(min(sterr))*/
		ArrayList<Integer> equalPositionsList = new ArrayList<Integer>();
		for(int i = 0; i< options.getLambda().length; i++)
			if(options.getLambda()[i] == cvErr.getLambda_min())
				equalPositionsList.add(i + 1);		/* start from 1*/
		semin = new double[equalPositionsList.size()];
		for(int i = 0; i< equalPositionsList.size(); i++)
			semin[i] = cvErr.getCvup()[equalPositionsList.get(i) - 1];
		
		/* CVerr.lambda_1se=max(options.lambda(CVerr.cvm<semin));*/
		/*
		 * find largest lambda which has a smaller mse than the stderr belonging to
		 * the largest of the lambda belonging to the smallest mse
		 * In other words, this defines the uncertainty of the min-cv, and the min
		 * cv-err could in essence be any model in this interval.
		 * */
		ArrayList<Integer> lessPostionsList = new ArrayList<Integer>();	/* on which postion the value of cvm is less than semin*/
		for(int i = 0; i< cvErr.getCvm().length; i++)
			if(cvErr.getCvm()[i] < semin[0])		//Q: it seems that semin array only has one number
				lessPostionsList.add(i + 1);		/* start from 1*/
		
		double[] partialLambdaUnderLessPostionsList = new double[lessPostionsList.size()];
		for(int i = 0; i< lessPostionsList.size(); i++)
			partialLambdaUnderLessPostionsList[i] = options.getLambda()[lessPostionsList.get(i) - 1];
		double copyOfPartialLambdaUnderLessPostionsList[] = Arrays.copyOf(partialLambdaUnderLessPostionsList, partialLambdaUnderLessPostionsList.length);
		Arrays.sort(copyOfPartialLambdaUnderLessPostionsList);
		double lambda_1se = copyOfPartialLambdaUnderLessPostionsList[copyOfPartialLambdaUnderLessPostionsList.length - 1];	/* max*/
		cvErr.setLambda_1se(lambda_1se);
		
		/* CVerr.glmnetOptions=options;*/
		cvErr.setGlmnetOptions(options);
		
		/* CVerr.glmnet_object = glmnet_object;*/
		cvErr.setGlmnet_object(glmnetObject);
		
		return cvErr;
	}
	
	/**
	 * 
	 * @param params
	 * @return result
	 */
	public static double[][] glmnetPredict(GlmnetPredictParameters params)
	{	
		/* result of this method*/
		double[][] result = null;
		
		/* get input parameters*/
		GlmnetResult object = params.getObject();
		double[] s = params.getS();
		String type = params.getType();
		double[][] newx = params.getNewx();
		
		double[] a0 = object.getA0();
		double[][] nbeta = null;
		double[] lambda = null;
		lambda_interpResult lamlist = null;
		
		if(params.getNargin() < 2)
		{
			params.setType("link");
		}
		
		if(params.getNargin() < 3)
		{
			params.setNewx(new double[0][0]);
		}
		
		if(params.getNargin() < 4)
		{
			params.setS(params.getObject().getLambda());
		}
		
		if(object.getClassString().equals("elnet"))
		{
			/* add a0 vector to the front of "beta" field of "object" as "nbeta" matrix,
			 * in this implementation, a0 has already been horizontal, no need to be transposed
			 * */
			nbeta = new double[object.getBeta().length+1][a0.length];
			
			/* put a0 as first row*/
			for(int i = 0; i< nbeta[0].length ;i++)
				nbeta[0][i] = a0[i];
			
			/* remaining will be the original "beta" field of "object"*/
			for(int i = 1; i< nbeta.length; i++)
				for(int j = 0; j< nbeta[0].length; j++)
					nbeta[i][j] = object.getBeta()[i-1][j];
			
			if(params.getNargin() == 4)
			{
				lambda = object.getLambda();
				lamlist = lambda_interp(lambda, s);
				
				/* nbeta=nbeta(:,lamlist.left).*repmat(lamlist.frac',size(nbeta,1),1) +nbeta(:,lamlist.right).*(1-repmat(lamlist.frac',size(nbeta,1),1));*/
				double[][] fracTwoDim = new double[1][lamlist.getFrac().length];
				for(int i = 0; i< lamlist.getFrac().length; i++)
					fracTwoDim[0][i] = lamlist.getFrac()[i];
				double[][] repmat = repmat(fracTwoDim, nbeta.length, 1);
				double[][] partialLeftNbeta = new double[nbeta.length][lamlist.getLeft().length];
				for(int i = 0; i< nbeta.length; i++)
				{
					for(int j = 0; j< lamlist.getLeft().length; j++)
					{
						partialLeftNbeta[i][j] = nbeta[i][(int)lamlist.getLeft()[j]-1];
					}
				}
				double[][] partialRightNbeta = new double[nbeta.length][lamlist.getRight().length];
				for(int i = 0; i< nbeta.length; i++)
				{
					for(int j = 0; j< lamlist.getRight().length; j++)
					{
						partialRightNbeta[i][j] = nbeta[i][(int)lamlist.getRight()[j]-1];
					}
				}
				
				/* media result*/
				
				/* nbeta(:,lamlist.left).*repmat(lamlist.frac',size(nbeta,1),1)
				 * nbeta(:,lamlist.left) and repmat(lamlist.frac',size(nbeta,1),1), 
				 * as well as the result matrix has the same row and column, 
				 * and .* means two numbers at same position of two matrix do multiplication 
				 * and put the result at the same position of result matrix
				 */	
				double[][] m1 = new double[partialLeftNbeta.length][partialLeftNbeta[0].length];
				for(int i = 0; i< m1.length; i++)
					for(int j = 0; j< m1[0].length; j++)
						m1[i][j] = partialLeftNbeta[i][j] * repmat[i][j];
				/*
				 * nbeta(:,lamlist.right).*(1-repmat(lamlist.frac',size(nbeta,1),1)
				 * nbeta(:,lamlist.right) and (1-repmat(lamlist.frac',size(nbeta,1),1))
				 * as well as the result matrix has the same row and column
				 * */
				double[][] m2 = new double[partialRightNbeta.length][partialRightNbeta[0].length];
				double[][] oneMinusRepmat = new double[repmat.length][repmat[0].length];
				for(int i = 0; i< repmat.length; i++)	
					for(int j = 0; j< repmat[0].length; j++)
						oneMinusRepmat[i][j] = 1 - repmat[i][j];
				for(int i = 0; i< m2.length; i++)
					for(int j = 0; j< m2[0].length; j++)
						m2[i][j] = partialRightNbeta[i][j] * oneMinusRepmat[i][j];
				
				/* final result*/
				double[][] newNbeta = new double[m1.length][m1[0].length];
				for(int i = 0; i< newNbeta.length; i++)
					for(int j = 0; j< newNbeta[0].length; j++)
						newNbeta[i][j] = m1[i][j] + m2[i][j];
				nbeta = newNbeta;
			}
			
			if(type.equals("coefficients"))
				result = nbeta;
			else if (type.equals("link") || type.equals("response"))
			{
				/* result = [ones(size(newx,1),1), newx] * nbeta;*/
				/* put one column whose values are all 1s at the front of newx matrix*/
				double[] newColumn = new double[newx.length];	/* new column of all 1s*/
				Arrays.fill(newColumn, 1);
				double[][] newNewx = new double[newx.length][newx[0].length + 1];
				/* add new column to the front*/
				for(int i = 0; i< newx.length; i++)
					newNewx[i][0] = newColumn[i];
				for(int i = 0; i< newx.length; i++)
					for(int j = 0; j< newx[i].length; j++)
						newNewx[i][j+1] = newx[i][j];
				
				/* matrix multiplication: newly-generated newx matrix times nbeta matrix*/
				Matrix newNewxMatrix = new Matrix(newNewx);
				Matrix nbetaMatrix = new Matrix(nbeta);
				result = newNewxMatrix.times(nbetaMatrix).getArray();
			}
			else if(type.equals("nonzero"))
			{
				double[][] partialNbeta = new double[nbeta.length - 1][nbeta[0].length];			/* nbeta(2:size(nbeta,1),:*/
				for(int i = 1; i< nbeta.length; i++)
				{
					for(int j = 0; j< nbeta[0].length; j++)
						partialNbeta[i-1][j] = nbeta[i][j];
				}
				result = nonzeroCoef(new nonzeroCoefParameters(partialNbeta, true));
			}
			else
			{
				System.out.println("ERROR: Unrecognized type");
				System.exit(1);
			}
		}
		else if(object.getClassString().equals("lognet"))
		{
			System.out.println("ERROR: lognet unimplemented!");
			System.exit(1);
			//TODO
		}
		else if(object.getClassString().equals("multnet"))
		{
			System.out.println("ERROR: multnet unimplemented!");
			System.exit(1);
			//TODO
		}
		else
		{
			System.out.println("ERROR: Unrecognized type");
			System.exit(1);
		}
		
		return result;
	}
	
	public static GlmnetResult glmnet(GlmnetParameters parameters)
	{
		logger.log(Level.FINE, "Begining of CommonFunction.glmnet");
		
		GlmnetResult result = new GlmnetResult();
		
		GlmnetOptions options = parameters.getOptions();
		int nlam = options.getNlambda();
		double[][] x = parameters.getX();
		double[] y = parameters.getY();
		double[][] yTwoDim = new double[y.length][1];
		for(int i = 0; i< y.length; i++)
			yTwoDim[i][0] = y[i];
		
		//TODO: check sparse of x and y
		
		int nobs = x.length;
		int nvars = x[0].length;
		
		if(nobs <= 1)
		{
			System.out.println("ERROR: at least two observations should be provided.");
			System.exit(1);
		}
		
		double[] weights = options.getWeights();
		if(weights.length == 0)	/* weights is empty*/
		{
			weights = new double[nobs];
			Arrays.fill(weights, 1);	/* weights = ones(nobs,1), weights is a vertical vector*/
		}
		
		double[][] maxit = options.getMaxit();
		
		double nulldev = 0;
		int ka = 0;
		int ne = 0;
		double[] exclude = null;
		double[][] jd = null;				
		double[] vp = null;
		double isd = 0;
		double thresh = 0;
		double[] lambda = null;
		double lambda_min = 0;
		double flmin = 0;
		double[][] ulam = null;
		double parm = 0;
		ElnetResult elnetResult = null;
		int lmu = 0;
		double ninmax = 0;
		double[] lam = null;
		ErrOutput errmsg = null;
		int[] dd = null;
		double[][] beta = null;
		int[] df = null;
		double[][] ca = null;
		double[] ia = null;
		double[] ja = null;
		
		String family = parameters.getFamily();
		if(family.equals("binomial") || family.equals("multinomial"))
		{
			//TODO:
		}
		else if(family.equals("gaussian"))
		{
			/* compute the null deviance*/
			
			/*ybar = y' * weights/ sum(weights);*/
			double[][] weightsTwoDim = new double[weights.length][1];
			for(int i = 0; i< weights.length; i++)
				weightsTwoDim[i][0] = weights[i];
			
			Matrix matrixWeights = new Matrix(weightsTwoDim);
			Matrix matrixY = new Matrix(yTwoDim);
			Matrix matrixTY = matrixY.transpose();	/* transpose matrix of y*/
			
			/* sum(weights), sum of weights*/
			double sumWeights = 0;
			for(int i = 0; i< weights.length; i++)
				sumWeights += weights[i];
			
			Matrix matrixYbar = matrixTY.times(matrixWeights).times(1/sumWeights);
			double ybar = matrixYbar.getArray()[0][0];//ybar is a 1 * 1 matrix
			
			/* nulldev = (y' - ybar).^2 * weights / sum(weights);*/
			double[][] tY = matrixTY.getArray();
			
			/* median result: (y' - ybar).^2*/
			double[][] m = new double[1][tY[0].length]; 
			for(int i = 0; i< tY[0].length; i++)
				m[0][i] = Math.pow(tY[0][i] - ybar, 2);
			
			Matrix matrixM = new Matrix(m);
			Matrix matrixNulldev = matrixM.times(matrixWeights).times(1/sumWeights);
			nulldev = matrixNulldev.getArray()[0][0];//Q: 1 * 1
			
			ka = -1;
			if(options.getType().equals("covariance"))
				ka = 1;
			else if (options.getType().equals("naive"))
				ka = 2;
			else
			{
				System.out.println("ERROR: unrecognized type");
				System.exit(1);
			}
		}
		else
		{
			System.out.println("ERROR: unrecognized family");
			System.exit(1);
		}
		
		ne = options.getDfmax();
		if(ne == 0)
			ne = nvars + 1;
		
		double nx = options.getPmax();
		if((int)nx == 0)
		{
			if(ne * 1.2 < nvars)
				nx = ne * 1.2;
			else
				nx = nvars;
		}
		
		exclude = options.getExclude();			/*exclude is a vertical vector*/
		jd = null;								/*jd is a vertical vector*/
		if(exclude.length != 0) 			/* exclude is not empty*/
		{	
			/* unique*/
			HashSet<Double> uniqueExcludeSet = new HashSet<Double>();
			for(double eExclude : exclude)
				uniqueExcludeSet.add(eExclude);
			ArrayList<Double> uniqueExcludeList = new ArrayList<Double>(uniqueExcludeSet);
			/* sort*/
			Collections.sort(uniqueExcludeList);
			
			/* copy back*/
			double[] uniqueSortedExclude = new double[uniqueExcludeList.size()];
			for(int j = 0; j< uniqueExcludeList.size(); j++)
				uniqueSortedExclude[j] = uniqueExcludeList.get(j);
			exclude = uniqueSortedExclude;
			
			for(double eExclude : exclude)
				if(!(eExclude > 0 && eExclude <= nvars))
				{
					System.out.println("ERROR: Some excluded variables out of range");
					System.exit(1);
				}
			jd = new double[exclude.length + 1][1];
			/* connect*/
			jd[0][0] = exclude.length;
			for(int j = 0; j< exclude.length; j++)
				jd[j+1][0] = exclude[j];
		}
		else
		{
			jd = new double[][]{{0}};
		}
		
		vp = options.getPenalty_factor();				/* vp is a vertical vector*/
		if(vp.length == 0)
		{
			vp = new double[nvars];
			for(int j = 0; j< vp.length; j++)
				vp[j] = 1;
		}
		
		isd = -1;
		if(options.getStandardize())
			isd = 1;
		else
			isd = 0;
		
		thresh = options.getThresh();
		lambda = options.getLambda();
		lambda_min = options.getLambda_min();
		if(lambda_min == 0)
			if(nobs < nvars)
				lambda_min = 5e-2;
			else
				lambda_min = 1e-4;
		
		flmin = 0;
		ulam = null;
		if(lambda.length == 0)
		{
			if(lambda_min >= 1)
			{
				System.out.println("ERROR: lambda_min should be less than 1");
				System.exit(1);
			}
			flmin = lambda_min;
			ulam = new double[][]{{0}};
		}
		else
		{
			flmin = 1.0;
			for(double eLambda: lambda)
				if(eLambda < 0)
				{
					System.out.println("ERROR: lambdas should be non-negative");
					System.exit(1);
				}
			/* sort lambda in descending order, store it to ulam*/
			double[] ulamAsc = Arrays.copyOf(lambda, lambda.length);	/* copy lambda to ulamAsc*/
			double[] ulamDesc = new double[lambda.length];
			Arrays.sort(ulamAsc);
			for(int j = 0; j< ulamAsc.length; j++)
				ulamDesc[j] = ulamAsc[ulamAsc.length - j - 1];
			/* ulam = -sort(-lambda), lambda and ulam are both vertical vector*/
			ulam = new double[ulamDesc.length][1];
			for(int j = 0; j< ulamDesc.length; j++)
				ulam[j][0] = ulamDesc[j];
			nlam = lambda.length;	
		}
		
		/* get input*/
		
		parm = options.getAlpha();
		float parm_elnet = (float)parm;
		
		/*convert two dimension matrix x into one dimension vector (column-wise)*/
		float[] x_elnet = new float[x.length * x[0].length];
		for(int j = 0; j< x[0].length; j++)
			for(int i = 0; i< x.length; i++)
				x_elnet[j * x.length + i] = (float)x[i][j];
		
		float[] y_elnet = birc.grni.util.CommonUtil.doubleToSingleVector(y, y.length);
		
		/*convert two dimension matrix jd into one dimension vector (column-wise)*/
		int[] jd_elnet = new int[jd.length * jd[0].length];
		for(int j = 0; j< jd[0].length; j++)
			for(int i = 0; i< jd.length; i++)
				jd_elnet[j * x.length + i] = (int)jd[i][j];
		
		float[] vp_elnet = birc.grni.util.CommonUtil.doubleToSingleVector(vp, vp.length);
		int ne_elnet = ne;
		int nx_elnet = (int)nx;
		int nlam_elnet = nlam;
		float flmin_elnet = (float)flmin;
		
		float[] ulam_elnet = new float[ulam.length * ulam[0].length];
		for(int j = 0; j< ulam[0].length; j++)
			for(int i = 0; i< ulam.length; i++)
				ulam_elnet[j * x.length + i] = (float)ulam[i][j];
		
		float thr_elnet = (float)thresh;
		int isd_elnet = (int)isd;
		float[] w_elnet = birc.grni.util.CommonUtil.doubleToSingleVector(weights, weights.length);
		int ka_elnet = ka;
		
		logger.log(Level.FINE, "Before call native funtion elnet");
		
		/* call native*/
		elnetResult = new ElnetResult();
		if(family.equals("gaussian")) {
			//TEST: print all parameters of elnet
//			String newLine = System.getProperty("line.separator");
//			try {
//				PrintWriter pw = new PrintWriter(new java.io.FileWriter("java.txt", true));
//				pw.printf("parm:"+newLine);
//				pw.printf("%20.15f"+newLine, parm_elnet);
//				pw.printf("x:"+newLine);
//				for(int i = 0; i< x_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, x_elnet[i]);
//				pw.printf("y:"+newLine);
//				for(int i = 0; i< y_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, y_elnet[i]);
//				pw.printf("jd:"+newLine);
//				for(int i = 0; i< jd_elnet.length; i++)
//					pw.printf("%4d"+newLine, jd_elnet[i]);
//				pw.printf("vp:"+newLine);
//				for(int i = 0; i< vp_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, vp_elnet[i]);
//				pw.printf("ne:"+newLine);
//				pw.printf("%4d"+newLine, ne_elnet);
//				pw.printf("nx:"+newLine);
//				pw.printf("%4d"+newLine, nx_elnet);
//				pw.printf("nlam:"+newLine);
//				pw.printf("%4d"+newLine, nlam_elnet);
//				pw.printf("flmin:"+newLine);
//				pw.printf("%20.15f"+newLine, flmin_elnet);
//				pw.printf("ulam:"+newLine);
//				for(int i = 0; i< ulam_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, ulam_elnet[i]);
//				pw.printf("thresh:"+newLine);
//				pw.printf("%20.15f"+newLine, thr_elnet);
//				pw.printf("isd:"+newLine);
//				pw.printf("%4d"+newLine, isd_elnet);
//				pw.printf("weights:"+newLine);
//				for(int i = 0; i< w_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, w_elnet[i]);
//				pw.printf("ka:"+newLine);
//				pw.printf("%4d"+newLine, ka_elnet);
//				
//				pw.close();
//			} catch(IOException ioe) {
//				ioe.printStackTrace();
//			}
			elnet(parm_elnet, x_elnet, x.length, x[0].length, y_elnet, jd_elnet, vp_elnet, ne_elnet, nx_elnet, nlam_elnet, flmin_elnet, ulam_elnet, thr_elnet, isd_elnet, w_elnet, ka_elnet, elnetResult);
		}
		else
			;//TODO:
		
		logger.log(Level.FINE, "After call native funtion elnet");
		
		/* prepare output*/
		lmu = elnetResult.getAlm().length;
		double[] copyOfNin = Arrays.copyOf(elnetResult.getNin(), elnetResult.getNin().length);
		Arrays.sort(copyOfNin);
		ninmax = copyOfNin[copyOfNin.length-1];
		
		//TEST: print all parameters of elnet
//		if(ninmax == 1 && counter++ == 1)
//		{
//			String newLine = System.getProperty("line.separator");
//			try {
//				PrintWriter pw = new PrintWriter(new java.io.FileWriter("java.txt", true));
//				pw.printf("parm:"+newLine);
//				pw.printf("%20.15f"+newLine, parm_elnet);
//				pw.printf("x:"+newLine);
//				for(int i = 0; i< x_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, x_elnet[i]);
//				pw.printf("y:"+newLine);
//				for(int i = 0; i< y_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, y_elnet[i]);
//				pw.printf("jd:"+newLine);
//				for(int i = 0; i< jd_elnet.length; i++)
//					pw.printf("%4d"+newLine, jd_elnet[i]);
//				pw.printf("vp:"+newLine);
//				for(int i = 0; i< vp_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, vp_elnet[i]);
//				pw.printf("ne:"+newLine);
//				pw.printf("%4d"+newLine, ne_elnet);
//				pw.printf("nx:"+newLine);
//				pw.printf("%4d"+newLine, nx_elnet);
//				pw.printf("nlam:"+newLine);
//				pw.printf("%4d"+newLine, nlam_elnet);
//				pw.printf("flmin:"+newLine);
//				pw.printf("%20.15f"+newLine, flmin_elnet);
//				pw.printf("ulam:"+newLine);
//				for(int i = 0; i< ulam_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, ulam_elnet[i]);
//				pw.printf("thresh:"+newLine);
//				pw.printf("%20.15f"+newLine, thr_elnet);
//				pw.printf("isd:"+newLine);
//				pw.printf("%4d"+newLine, isd_elnet);
//				pw.printf("weights:"+newLine);
//				for(int i = 0; i< w_elnet.length; i++)
//					pw.printf("%20.15f"+newLine, w_elnet[i]);
//				pw.printf("ka:"+newLine);
//				pw.printf("%4d"+newLine, ka_elnet);
//				
//				pw.close();
//			} catch(IOException ioe) {
//				ioe.printStackTrace();
//			}
//		}
		
		lam = elnetResult.getAlm();
		if(options.getLambda().length == 0)
			lam = fix_lam(lam);
		
		errmsg = err(elnetResult.getJerr(), options.getMaxit(), (int)nx);
		if(errmsg.getN() == 1)
		{
			System.out.println("ERROR: " + errmsg.getMsg());
			System.exit(1);
		}
		else if(errmsg.getN() == -1)
		{
			System.out.println("WARNING: "+errmsg.getMsg());
		}
		
		if(family.equals("multinomial"))
			;//TODO
		else
		{
			dd = new int[]{nvars, lmu};
			if(ninmax > 0)
			{
				/* extract 0 - (ninmax-1) rows of original ca*/
				ca = elnetResult.getCa();
				
				//TEST: print ca before extraction
//				try {
//					PrintWriter pwCa = new PrintWriter(new java.io.FileWriter("java.txt", true));
//					pwCa.println("ca:");
//					for(int k = 0; k< ca.length; k++) {
//						for(int kk = 0; kk< ca[0].length; kk++)
//							pwCa.printf("%20.15f", ca[k][kk]);
//						pwCa.println();
//					}
//					pwCa.close();
//				} catch(IOException ioe) {
//					ioe.printStackTrace();
//				}
				
				double[][] newCa = new double[(int)ninmax][ca[0].length];
				for(int k = 0; k< (int)ninmax; k++)
				{
					for(int kk = 0; kk< ca[0].length; kk++)
						newCa[k][kk] = ca[k][kk];
				}
				ca = newCa;
				
				/* df = sum(abs(ca) > 0, 1)*/
				double[][] absCa = new double[ca.length][ca[0].length];
				for(int k = 0; k< ca.length; k++)
					for(int kk = 0; kk< ca[0].length; kk++)
						absCa[k][kk] = Math.abs(ca[k][kk]);
				int[][] gtZero = new int[absCa.length][absCa[0].length];
				for(int k = 0; k< absCa.length; k++)
					for(int kk = 0; kk< absCa[0].length; kk++)
						if(absCa[k][kk] > 0)
							gtZero[k][kk] = 1;
						else
							gtZero[k][kk] = 0;
				
				df = new int[gtZero[0].length];
				for(int k = 0; k< gtZero.length; k++)
					for(int kk = 0; kk< gtZero[0].length; kk++)
						df[kk] += gtZero[k][kk];
				
				/* ja = ia(1:ninmax)*/
				ia = elnetResult.getIa();
				ja = new double[(int)ninmax];
				for(int k = 0; k< (int)ninmax; k++)
					ja[k] = ia[k];
				double[][] jaAndPos = new double[ja.length][2];
				for(int k = 0; k< ja.length; k++)
				{
					jaAndPos[k][0] = ja[k];
					jaAndPos[k][1] = k+1;
				}
				double[][] sortedJaAndPos = birc.grni.util.CommonUtil.mySort(jaAndPos, true);				/* ascending order*/
				double[] ja1 = new double[sortedJaAndPos.length]; 				/* sorted ja elements*/
				int[] oja = new int[sortedJaAndPos.length];						/* sorted ja elements postions*/
				for(int k = 0; k< sortedJaAndPos.length; k++)
					ja1[k] = sortedJaAndPos[k][0];
				for(int k = 0; k< sortedJaAndPos.length; k++)
					oja[k] = (int)sortedJaAndPos[k][1];
				
				beta = birc.grni.util.CommonUtil.zeros(nvars, lmu);
				/* beta (ja1, :) = ca(oja,:)*/
				for(int k = 0; k< ja1.length; k++)
					for(int kk = 0; kk< ca[0].length; kk++)
						beta[(int)ja1[k]-1][kk] = ca[oja[k]-1][kk];
			}
			else
			{
				/* beta = zeros(nvars,lmu);*/
				beta = birc.grni.util.CommonUtil.zeros(nvars, lmu);
				/* df = zeros(1,lmu);*/
				df = new int[lmu];
				Arrays.fill(df, 0);
			}
			
			if(family.equals("binomial"))
				;//TODO:
			else
			{
				result.setA0(elnetResult.getA0());
				result.setBeta(beta);
				result.setDev(elnetResult.getRsq());
				result.setNulldev(nulldev);
				result.setDf(df);
				result.setLambda(lam);
				result.setNpasses(elnetResult.getNlp());
				result.setJerr(elnetResult.getJerr());
				result.setDim(dd);
				result.setClassString("elnet");
			}
		}
		
		return result;
	}
	
	/** 
	 * We found a method of random permutation that can generate same result as matlab 
	 * randperm does, that is, assigns a random number to each element of the set to be 
	 * shuffled and then sorts the set according to the assigned numbers, the original 
	 * position of each assigned number in the newly-sorted list is a random permutation
	 * we want. However, this method has worse asymptotic time complexity: sorting is O(n log n) 
	 * which is worse than O(n) of Fisher-Yates algorithm which is used in java <code> Collections.shuffle(List<?> list, Random rnd) </code> method.
	 * 
	 * It seems that randperm in more recent version of Matlab, say, R2013b, has O(n) time-complexity. 
	 * However, it is a build-in function, we cannot access the source code, so, we decide to use <code> Collections.shuffle(List<?> list, Random rnd) </code> 
	 * method instead, since we have already tested our program when we generated same random permuation with original matlab program, the final result
	 * is same, therefore, though the final result is not same after we use Collections.shuffle(), 
	 * it is just caused by different random permutation of same set of numbers, it's not an error.
	 * 
	 * BTW, the original Matlab code uses MersenneTwister random number generation algorihtm 
	 * which is different with current algorithm used in java 1.7 source code, we use the same one
	 * 
	 * The final thing is, Collections.shuffle(List<?> list, Random rnd) in java 1.7 source
	 * is designed for List<?>, we just implement the same algorithm on our array input
	 * 
	 * @param N
	 * @param mtRd MersenneTwister
	 * @return random permuation of number set of 1 to N (both inclusive)
	 */
	public static int[] randperm(int N, MersenneTwister mtRd)
	{
		//TEST: to test other parts of program, still use the O(n log n) algorithm which can generate same random permutation as matlab randperm()
		double[][] vectorWithPos = new double[N][2];
		for(int i = 0; i< N; i++)
		{
			vectorWithPos[i][0] = mtRd.nextDouble();
			vectorWithPos[i][1] = i+1;
		}
		
		double[][] sortedVectorWithPos = birc.grni.util.CommonUtil.mySort(vectorWithPos, true/* ascending*/);
		
		int[] result = new int[N];
		for(int i = 0; i< N; i++)
			result[i] = (int)sortedVectorWithPos[i][1];
		
		return result;
		
//		/* generate initial number set from 1 to N*/
//		int[] vector = new int[N];
//		for(int i = 0; i< N; i++)
//			vector[i] = i+1;
//		
//		/* shuffle*/
//		shuffle(vector, mtRd);
//		
//		return vector;
	}
	
    /**
	 * 
	 * @param originalMatrix
	 * @param row
	 * @param column
	 * @return repeatMatrix
	 */
	public static double[][] repmat(double[][] originalMatrix, int row, int column)
	{
		double[][] repeatMatrix = new double[row * originalMatrix.length][column * originalMatrix[0].length];
		for(int i = 0; i< row; i++)
		{
			for(int j = 0; j< column; j++)
			{
				for(int m = 0; m< originalMatrix.length; m++)
				{
					for(int n = 0; n< originalMatrix[0].length; n++)
					{
						repeatMatrix[i * originalMatrix.length + m][j * originalMatrix[0].length + n] = originalMatrix[m][n];
					}
				}
			}
		}
		
		return repeatMatrix;
	}
	
	private static lambda_interpResult lambda_interp(double[] lambda, double[] s)
	{
		int nums = -1;
		double[] left = null;
		double[] right = null;
		double[] sfrac = null;
		double[] coord = null;
		
		if(lambda.length == 1) /* degenerate case of only one lambda*/
		{
			nums = s.length;
			/* left = ones(nums, 1), left is actually a vertical vector*/
			left = new double[nums];
			Arrays.fill(left, 1);
			right = left;
			/* sfrac = ones(nums, 1)*/
			sfrac = new double[nums];
			Arrays.fill(sfrac, 1);
		}
		else
		{
			/* find the maximum and minimum number in lambda*/
			double[] copyOfLambda = Arrays.copyOf(lambda, lambda.length);
			Arrays.sort(copyOfLambda);
			double maxLambda = copyOfLambda[copyOfLambda.length - 1];
			double minLambda = copyOfLambda[0];
			/*  
			 * s(s > max(lambda)) = max(lambda);
			 * s(s < min(lambda)) = min(lambda);
			 * */
			for(int i = 0; i< s.length; i++)
				if(s[i] > maxLambda)
					s[i] = maxLambda;
			for(int i = 0; i< s.length; i++)
				if(s[i] < minLambda)
					s[i] = minLambda;
			
			int k = lambda.length;
			
			/* sfrac =(lambda(1)-s)/(lambda(1) - lambda(k));*/
			sfrac = new double[s.length];
			for(int i = 0; i< s.length; i++)
				sfrac[i] = (lambda[0] - s[i]) / (lambda[0] - lambda[k-1]);
			
			/* lambda = (lambda(1) - lambda)/(lambda(1) - lambda(k));*/
			double[] newLambda = new double[lambda.length];
			for(int i = 0; i< lambda.length; i++)
				newLambda[i] = (lambda[0] - lambda[i]) / (lambda[0] - lambda[k-1]);
			lambda = newLambda;
			
			double[][] lambdaTwoDim = new double[lambda.length][1];	/* lambda is a vertical vector*/
			for(int i = 0; i< lambda.length; i++)
				lambdaTwoDim[i][0] = lambda[i];
			double[][] oneToLengthLambda = new double[1][lambda.length]; /* 1:length(lambda)*/
			for(int i = 0; i< lambda.length; i++)
				oneToLengthLambda[0][i] = i+1;
			double[][] sfracTwoDim = new double[lambda.length][1];	/* sfrac is a vertical vector*/
			for(int i = 0; i< sfrac.length; i++)
				sfracTwoDim[i][0] = sfrac[i];
			
			double[][] originalCoord = interp1(lambdaTwoDim, oneToLengthLambda, sfracTwoDim);
			
			/* convert interp1 two dimension result vector into one dimension one*/
			if(originalCoord.length >= originalCoord[0].length) {
				coord = new double[originalCoord.length];
				for(int i = 0; i< originalCoord.length; i++)
					coord[i] = originalCoord[i][0];
			} else {
				coord = new double[originalCoord[0].length];
				for(int i = 0; i< originalCoord[0].length; i++)
					coord[i] = originalCoord[0][i];
			}
			
			/* left = floor(coord);*/
			left = new double[coord.length];
			for(int i = 0; i< coord.length; i++)
				left[i] = Math.floor(coord[i]);
			/* right = ceil(coord);*/
			right = new double[coord.length];
			for(int i = 0; i< coord.length; i++)
				right[i] = Math.ceil(coord[i]);
			
			/* sfrac=(sfrac-lambda(right))./(lambda(left) - lambda(right));*/
			double[] newSfrac = new double[sfrac.length];
			for(int i = 0; i< sfrac.length; i++)
				newSfrac[i] = (sfrac[i] - lambda[(int)right[i]-1])/(lambda[(int)left[i]-1] - lambda[(int)right[i]-1]);
			sfrac = newSfrac;
			
			/* sfrac(left==right)=1;*/
			for(int i = 0; i< sfrac.length; i++)
				if(left[i] == right[i])
					sfrac[i] = 1;
		}
		
		lambda_interpResult result = new lambda_interpResult();
		result.setLeft(left);
		result.setRight(right);
		result.setFrac(sfrac);
		return result;
	}
	
	/**
	 * 
	 * @param params
	 * @return result
	 */
	private static double[][] nonzeroCoef(nonzeroCoefParameters params)
	{
		/* input parameters*/
		double[][] beta = params.getBeta();
		boolean bystep = params.getBystep();
		
		double[][] result = null;
		
		if(params.getNargin() < 2)
			params.setBystep(false);
		
		/* result = abs(beta)>0;*/
		result = new double[beta.length][beta[0].length];
		for(int i = 0; i< beta.length; i++)
			for(int j = 0; j< beta[0].length; j++)
				if(Math.abs(beta[i][j]) > 0)
					result[i][j] = 1;
				else
					result[i][j] = 0;
		if(!bystep)
			/* result = any(result, 2)*/
		{
			double[][] newResult = new double[result.length][1];			/* newResult is actually a column vector*/
			/* initialize to all 0s*/
			for(int i = 0; i< newResult.length; i++)
				newResult[i][0] = 0;
			
			for(int i = 0; i< result.length; i++)
			{
				for(int j = 0; j< result[i].length; j++)
					if(result[i][j] != 0)
					{
						newResult[i][0] = 1;
						break;
					}
			}
			
			result = newResult;
		}
		
		return result;
	}
	
	private static double[] fix_lam(double[] lam)
	{
		double[] newLam = lam;
		double[] llam = new double[lam.length];
		for(int i = 0; i< llam.length; i++)
			llam[i] = Math.log(lam[i]);
		newLam[0] = Math.exp(2 * llam[1] - llam[2]);
		return newLam;
	}
	
	private static ErrOutput err(int n, double[][] maxit, int pmax)
	{
		ErrOutput output = new ErrOutput();
		if(n == 0)
		{
			output.setN(0);
			output.setMsg("");
		}
		else if (n > 0)
		{
			String msg = "";
			if (n < 7777)
				msg = "Memory allocation error; contact package maintainer";
			else if (n == 7777) 
				msg = "All used predictors have zero variance";
			else if ((8000<n) && (n<9000))
			{
				msg = String.format("Null probability for class %d < 1.0e-5", n-8000);
			}
			else if ((9000<n) && (n<10000))
				msg = String.format("Null probability for class %d > 1.0 - 1.0e-5", n-9000);
			else if (n==10000)
				msg = "All penalty factors are <= 0";
			output.setN(1);
			output.setMsg(String.format("in glmnet fortran code - %s", msg));
		}
		else if (n < 0) /* non fatal error*/
		{
			String msg = "";
			if (n > -10000) 
				msg = String.format("Convergence for %dth lambda value not reached after maxit=%d iterations; solutions for larger lambdas returned", -n, maxit);
			else if (n < -10000) 
				msg = String.format("Number of nonzero coefficients along the path exceeds pmax=%d at %dth lambda value; solutions for larger lambdas returned", pmax, -n-10000);
			output.setN(-1);
			output.setMsg(String.format("from glmnet fortran code - ", msg));
		}
		
		return output; 
	}
	
	/**
	 * implement interp1 in matlab (1-D interpolation or table look-up) with 
	 * three parameters and linear method, that is what we use in the original 
	 * elastic net matlab code
	 * 
	 * @param X X is a vector
	 * @param V V is a vector
	 * @param Xq
	 * @return
	 */
	public static double[][] interp1(double[][] X, double[][] V, double[][] Xq)
	{
		/* input argument check, X and V must both be vector*/
		if(!(X.length == 1 || X[0].length == 1))
		{
			System.out.println("ERROR: interp1: first input X must be a vector");
			System.exit(1);
		}
		
		if(!(V.length == 1 || V[0].length == 1))
		{
			System.out.println("ERROR: interp1: second input V must be a vector");
			System.exit(1);
		}
		
		/* variables*/
		int[][] idx = null;
		int[] siz_vq = new int[2];					/* size(Xq)*/
		
		String method = "linear";					/* only implement this method*/
		
		/* orig_size_v = size(V)*/
		int[] orig_size_v = new int[]{V.length, V[0].length};				
		
		/* A = A(:), if A is a horizontal vector, it will be transformed to a vertical one
		 * otherwise, if will not change*/
		/* V = V(:)*/
		if(V.length < V[0].length)	/* horizontal*/
			V = new Matrix(V).transpose().getArray();
		/* X = X(:)*/
		if(X.length < X[0].length)	/* horizontal*/
			X = new Matrix(X).transpose().getArray();
		
		/*if any(diff(X)<0)
	      	[X, idx] = sort(X);
	      	V = V(idx,:);
	      end
	     */
		if(X.length > X[0].length)					/* X is a vertical vector*/
		{
			/* diff(X)*/
			double[] diffX = new double[X.length - 1];
			for(int i = 0; i< X.length-1; i++)
				diffX[i] = X[i+1][0] - X[i][0];
			
			/* any(diff(X)) < 0*/
			boolean anyDiffXLessZero = false;
			for(int i = 0; i< diffX.length; i++)
				if(diffX[i] < 0) {
					anyDiffXLessZero = true;
					break;
				}
			
			if(anyDiffXLessZero) {
				double[][] XWithPos = new double[X.length][2];		/* X with position*/
				for(int i = 0; i< X.length; i++) {
					XWithPos[i][0] = X[i][0];
					XWithPos[i][1] = i+1;	/* position start from 1*/
				}
				/* [X, idx] = sort(X)*/
				//TEST: combine en and lasso
				double[][] sortedXWithPos = birc.grni.util.CommonUtil.mySort(XWithPos, true);	/* sorted X with position, ascending*/
				/* assign sorted X to current X (X is vertical), as well as idx*/
				for(int i = 0; i< sortedXWithPos.length; i++)
					X[i][0] = sortedXWithPos[i][0];
				idx = new int[X.length][1];	/* same as X, also vertical*/
				for(int i = 0; i< sortedXWithPos.length; i++)
					idx[i][0] = (int)sortedXWithPos[i][1];
				
				/* V = V(idx,:), pick rows indicated in idx from V to compose a new V*/
				double[][] newV = new double[idx.length][];
				for(int i = 0; i< idx.length; i++)
					newV[i] = Arrays.copyOf(V[idx[i][0] - 1], V[idx[i][0] - 1].length);
				V = newV;
			}
		}
		else										/* X is a horizontal vector*/
		{
			//TODO:
			System.out.println("ERROR: interp1: havn't implemented when first input X is a horizontal vector");
			System.exit(1);
		}
		
		/*if isscalar(X)
		   if isempty(Xq)
		      varargout{1} = zeros(size(Xq));
		      return
		   end
		end*/
		if(X.length == 1 && X[0].length == 1) 
		{
			if(Xq.length == 0)
				return new double[][]{};	/* in java, it is impossible for a multi-dimension array having one or more dimensions whose length are 0
				 						 * so, just return an empty array
				 						 */
		}
		
		/* Vq = Interp1D(X,V,Xq,method), with method is linear*/
		double[][] Vq = Interp1D(X,V,Xq);
		
		if(V.length == 1 || V[0].length == 1)	/* isvector(V)*/
		{
			siz_vq[0] = Xq.length;
			siz_vq[1] = Xq[0].length;
		}
		else
			;//TODO: 
		
		//TODO:varargout{1} = cast(reshape(Vq,siz_vq),superiorfloat(X,V,Xq));
		//Q: it seems no need to implement this sentence in our application context.
		return Vq;
	}
	
	private static double[][] Interp1D(double[][] X, double[][] V, double[][] Xq/*, String method, use "linear" only*/)
	{
		/* based on original matlab code, in this application, X is a vertical vector, so, add a check here*/
		if(X[0].length >1)
		{
			System.out.println("ERROR: Interp1D: the first argument X must be a vertical vector");
			System.exit(1);
		}
		
		/* variables*/
		double[][] Xext = null;
		double[][] Xqext = null;
		
		/* Xqcol = Xq(:)*/
		/* A = A(:), if A is a horizontal vector, it will be transformed to a vertical one
		 * otherwise, if will not change*/
		double[][] Xqcol = null;
		if(Xq.length < Xq[0].length)	/* horizontal*/
			Xqcol = new Matrix(Xq).transpose().getArray();
		else	/* vertical, just copy Xq*/
		{
			Xqcol = new double[Xq.length][];
			for(int i = 0; i< Xq.length; i++)
				Xqcol[i] = Arrays.copyOf(Xq[i], Xq[i].length);
		}
		
		/* num_vals = size(V,2)*/
		int num_vals = V[0].length;
		
		/*if (num_vals>1)
		   Xext = {cast(X,'double'),(1:num_vals)'};
		   Xqext = {cast(Xqcol,class(Xext{1})),Xext{2:end}};
		else
		   Xext = {X};
		   Xqext = {Xqcol};
		end*/
		if(num_vals > 1)
		{
			/* Xext = {cast(X,'double'),(1:num_vals)'}*/
			/* X is already double type, no need to cast, just connect two vectors*/
			//TODO: current implementation dosn't allow Xext to be not a vector
			System.out.println("ERROR: Interp1D: current implementation dosn't allow V to be not a vector");
			System.exit(1);
//			Xext = new double[num_vals][2];	/* first column is X, second column is (1:num_vals)'*/
//			for(int i = 0; i< X.length; i++)
//				Xext[i][0] = X[i][0];
//			for(int i = 0; i< num_vals; i++)
//				Xext[i][1] = i+1;	/* start from 1*/
		}
		else
		{
			Xext = new double[X.length][];
			Xqext = new double[Xqcol.length][];
			/* Xext = {X}
			 * Xqext = {Xqcol}
			 * */
			for(int i = 0; i< X.length; i++) {
				Xext[i] = Arrays.copyOf(X[i], X[i].length);
			}
			for(int i = 0; i< Xqcol.length; i++) {
				Xqext[i] = Arrays.copyOf(Xqcol[i], Xqcol[i].length);
			}
		}
		
		/*if ~strcmpi(method,'pchip')
		   F = griddedInterpolant(Xext,V,method);
		end*/
		//TODO: use linear regression, so Xext (also V) must be a vector
		double[] XextOneDim = null;	/* transfer to one dimension*/
		if(Xext.length > Xext[0].length) /* vertical*/
		{
			XextOneDim = new double[Xext.length];
			for(int i = 0; i< Xext.length; i++)
				XextOneDim[i] = Xext[i][0];
		}
		else	/* horizontal*/
			XextOneDim = Arrays.copyOf(Xext[0], Xext[0].length);
		double[] VOneDim = null;	/* transfer to one dimension*/
		if(V.length > V[0].length) /* vertical*/
		{
			VOneDim = new double[V.length];
			for(int i = 0; i< V.length; i++)
				VOneDim[i] = V[i][0];
		}
		else	/* horizontal*/
			VOneDim = Arrays.copyOf(V[0], V[0].length);
		
		/* F = griddedInterpolant(Xext,V,method); where method is linear
		 * Vq = F(Xqext);*/
		LinearInterpolation F = new LinearInterpolation(XextOneDim, VOneDim);
		double[][] Vq = null;
		if(Xqext.length > Xqext[0].length)
		{
			double[] XqextOneDim = new double[Xqext.length];
			for(int i = 0; i< Xqext.length; i++)
				XqextOneDim[i] = Xqext[i][0];
			double[] VqOneDim = F.getVector(XqextOneDim);
			Vq = new double[Xqext.length][1];
			for(int i = 0; i< Xqext.length; i++)
				Vq[i][0] = VqOneDim[i];
		}
		else
		{
			double[] XqextOneDim = Arrays.copyOf(Xqext[0], Xqext[0].length);
			double[] VqOneDim = F.getVector(XqextOneDim);
			Vq = new double[1][Xqext[0].length];
			for(int i = 0; i< Xqext[0].length; i++)
				Vq[0][i] = VqOneDim[i];
		}
		
		return Vq;
	}
	
	/* native function*/
	public native static void elnet(
		/* input*/
		float parm, float[] x, int xRow, int xColumn, float[] y, int[] jd, float[] vp, int ne, int nx, int nlam, float flmin, float[] ulam, float thr, int isd, float[] w, int ka, 
		/* output*/
		ElnetResult elnetResult
		);
	
	static 
	{
		String archOs = System.getProperty("os.arch");
		if(archOs.contains("64"))
		{
			if(SystemUtils.IS_OS_WINDOWS) {
				//For Windows64
				try {
					birc.grni.util.CommonUtil.copyDependentLibs();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "windows64" + "/" + "birc_grni_en_CommonFunction.dll");
			} else if(SystemUtils.IS_OS_LINUX) {
				//For Linux64 
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "linux64" + "/" + "libbirc_grni_en_CommonFunction.so");
			} else if(SystemUtils.IS_OS_MAC_OSX) {
				//For Mac OS X 64
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "osx64" + "/" + "libbirc_grni_en_CommonFunction.jnilib");
			}
		}
		else
		{
			if(SystemUtils.IS_OS_WINDOWS) {
				//For Windows32
				try {
					birc.grni.util.CommonUtil.copyDependentLibs();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "windows32" + "/" + "birc_grni_en_CommonFunction.dll");
			} else if(SystemUtils.IS_OS_LINUX) {
				//For Linux32
				birc.grni.util.CommonUtil.loadJarNativeLib(birc.grni.util.GLOBALVAR.nativeLibFolder + "/" + "linux32" + "/" + "libbirc_grni_en_CommonFunction.so");
			}
		}
	}
}
