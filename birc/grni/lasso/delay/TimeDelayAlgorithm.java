//package birc.grni.lasso.delay;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import javax.swing.SwingWorker;
//
//import birc.grni.en.CVErr;
//import birc.grni.en.CommonFunction;
//import birc.grni.en.GlmnetOptions;
//import birc.grni.en.GlmnetParameters;
//import birc.grni.en.GlmnetResult;
//import birc.grni.gui.GrnTimeDelayLasso;
//import birc.grni.gui.GrnTimeDelayLassoDisplay;
//
//
//public class TimeDelayAlgorithm extends SwingWorker<Void, Void>{
//	double [][] inputData = null;
//	double [][] inputDataX =null;
//	int maxDelay;
//	String inputPath;
//	int numGenes;
//	int numSamples;
//	int targetGene;
//	FindCrosscorrelation corr =null;
//	//int [][] matrixDelay =null;
//	int [] delayArray = null;
//	//double [] yData = null;
//	double [][] xData =null;
//	int [][] finalNetwork = null;
//	int [][] finalDelayMatrix =null;
//	
//	/* lasso components */
//	GlmnetOptions lassoOptions = null;
//	CVErr cvErr = null;
//	GlmnetResult lassoModel = null;
//	ArrayList<double[]> columnListOfBlasso = new ArrayList<double[]>();
//	double[][] Blasso = null;
//	
//	
//	public TimeDelayAlgorithm (String path, int delayMax){
//		/* Read input data */
//		//String path ="C:/Users/devamuni/Documents/D_Result/data10G10T.txt";
//		this.maxDelay = delayMax;
//		this.inputPath = path;
//		InputDataProcess dataProcess = new InputDataProcess();
//		try {
//			dataProcess.processInput(inputPath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		/* data normalization */
//		this.inputData =  birc.grni.util.CommonUtil.zeroMean(dataProcess.expressionData);
//		this.numGenes = dataProcess.numGeneInFile;
//		this.numSamples =dataProcess.numSamplesInFile;
//		this.targetGene = 0;
//		
//		/* Initialize the correlation */
//		corr = new FindCrosscorrelation(inputData, maxDelay, numGenes, numSamples);
//		/* define X and Y regression data */
//		// yData = new double [numSamples-maxDelay];
//		// xData = new double [numSamples-maxDelay][numGenes -1];
//		 inputDataX = new double [numSamples][numGenes -1];
//		 /* Initialize the output matrix */
//		 finalNetwork = new int [numGenes][numGenes];
//		 finalDelayMatrix = new int[numGenes][numGenes];
//		 GrnTimeDelayLassoDisplay.LassoDelayProgressBar.setMaximum(numGenes-1);
//	}
//	
//	public void runTimeDelayAlgorithm(int target){
//		
//		
//			corr.calculateOptimalDelay(target);
//			delayArray = corr.getDelayArray();
//			
//			/* shift Y data (target gene)*/
//			double [] yData = new double [numSamples-maxDelay];
//			for(int i= 0; i<yData.length;i++){
//				yData[i]= inputData[maxDelay+i][target];
//			}
//			
//			/* separate and shift X data according to delay */
//			double [][] xData = new double [numSamples-maxDelay][numGenes -1];
//			int sampleLenght = numSamples-maxDelay;
//			int currentColumn = 0;
//			for (int m=0; m<numGenes;m++){
//				/* obtain optimal Delay Information */
//				int optDelay = delayArray[m];
//				if(m != target){
//					for(int n=0;n<sampleLenght;n++){
//						xData[n][currentColumn] = inputData[maxDelay-optDelay+n][m];
//					}
//					currentColumn ++;
//				}
//					
//			}
//			/* setup lasso components */
//			lassoOptions = new GlmnetOptions();
//			lassoOptions.setAlpha(1);
//			
//			/* perform five-fold cross validation*/
//			//CVerr = cvglmnet(xD,y,5,[],'response','gaussian',options,[]);
//			cvErr = CommonFunction.cvglmnet(xData, yData, 5, new int[0], "response", "gaussian", lassoOptions, new double[0][0]);
//			
//			/* shift Y data (target gene)*/
//			yData = new double [numSamples-maxDelay];
//			for(int i= 0; i<yData.length;i++){
//				yData[i]= inputData[maxDelay+i][target];
//			}
//			
//			/* separate and shift X data according to delay */
//			xData = new double [numSamples-maxDelay][numGenes -1];
//			int sampleLenghtx = numSamples-maxDelay;
//			int currentColumnx = 0;
//			for (int m=0; m<numGenes;m++){
//				/* obtain optimal Delay Information */
//				int optDelay = delayArray[m];
//				if(m != target){
//					for(int n=0;n<sampleLenghtx;n++){
//						xData[n][currentColumnx] = inputData[maxDelay-optDelay+n][m];
//					}
//					currentColumnx ++;
//				}
//					
//			}
//			/* set parameters for final lasso run */
//			lassoOptions = new GlmnetOptions();
//			lassoOptions.setAlpha(1);
//			lassoOptions.setLambda(new double[]{cvErr.getLambda_1se()});
//			
//			
//			
//			/* obtain lasso model */
//			lassoModel = CommonFunction.glmnet(new GlmnetParameters(xData, yData, "gaussian", lassoOptions));
//			
//			/* model's beta values */
//			double[] delayBeta = new double[lassoModel.getBeta().length];
//			int betaLength = delayBeta.length;
//			for(int i = 0; i< lassoModel.getBeta().length; i++){
//				delayBeta[i] = lassoModel.getBeta()[i][0];
//			}
//			
//			int delayLength =delayArray.length;
//			/* infer the final network according to beta values */
//			/* using the logic that describe in the matlab code */
//			int kinx = 0;
//			int dindx = 0;
//			for(int i =0 ; i<numGenes;i++){
//				if(i==target){
//					finalNetwork[target][i] = 0; 
//					finalDelayMatrix[target][i] = 0;
//					dindx ++;
//				}else{
//					if(Math.abs(delayBeta[kinx]) != 0){
//						finalNetwork[target][i] = 1; 
//						finalDelayMatrix[target][i] = delayArray[dindx];
//					}
//					kinx ++;
//					dindx ++;
//				}
//				
//			}
//				
//	}
//	
//	public int [][] getFinalNetwork(){
//		/* perform matrix transpose */
//		/* G  = G'    ; */
//		int [][] transposeOfFinalNetwork = new int[numGenes][numGenes];
//		for(int i=0 ;i<numGenes;i++){
//			for(int j=0; j<numGenes; j++){
//				transposeOfFinalNetwork[j][i]=finalNetwork[i][j];
//			}
//		}
//		return transposeOfFinalNetwork;
//	}
//	
//	public int [][] getDelayMatrix(){
//		/* perform matrix transpose */
//		/*  Del = Del'; */
//		int [][] transposeOfDelayMatrix = new int[numGenes][numGenes];
//		for(int i=0 ;i<numGenes;i++){
//			for(int j=0; j<numGenes; j++){
//				transposeOfDelayMatrix[j][i]=finalDelayMatrix[i][j];
//			}
//		}
//		return transposeOfDelayMatrix;
//	}
//
//	@Override
//	protected Void doInBackground() throws Exception {
//		// TODO Auto-generated method stub
//		while(targetGene < numGenes){
//			runTimeDelayAlgorithm(targetGene);
//			GrnTimeDelayLassoDisplay.LassoDelayProgressBar.setValue(targetGene);
//			targetGene++;
//		}
//		return null;
//	}
//	
//	protected void done() {
//		int [][] transposeOfFinalNetworkdd = getFinalNetwork();
//		int [][] transposeOfDelayMatrixdd =getDelayMatrix();
//	
//		GrnTimeDelayLasso.timeDelayLassoResult(transposeOfFinalNetworkdd, transposeOfDelayMatrixdd, numGenes);
//	}
//
//}
