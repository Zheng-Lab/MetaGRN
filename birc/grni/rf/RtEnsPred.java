package birc.grni.rf;
public class RtEnsPred
{
	/**
		Make predictions with an ensemble of (multiple output) regression trees
  
		inputs:
		  tree: a tree output by the function rtenslearn_c
		  XTS: inputs for the test cases
		  YLS: outputs for the learning sample cases
		Output:
		  YTS: Predictions for the test cases
	 */
	public static float[][] rtEnsPred(TreeEnsembleStruct[][] treeEnsemble, double[][] XTSA)
	{
		int verbose = 0;								/* to print or not*/
		GlobalVar.XTS = XTSA;
		int nts = GlobalVar.XTS.length;
		int T = treeEnsemble[0][0].getTrees().length;
		int YTSRow = GlobalVar.XTS.length;
		int YTSColumn = treeEnsemble[0][0].getTrees()[0].length;
		float[][] YTS = new float[YTSRow][YTSColumn];
		
		for(int t = 0; t< T; t++)
		{
			if(verbose == 1)
				System.out.printf("t=%d\n",t);
			GlobalVar.tree = treeEnsemble[0][0].getTrees()[t][0];
			
			/* YTS=YTS+tree.weight*rtpred()*/
			for(int i = 0; i< YTS.length; i++)
			{
				float weight = GlobalVar.tree.getWeight()[0][0];
				double[][] rtPredResult = rtPred();
				for(int j = 0; j< YTS[0].length; j++)
					YTS[i][j] += (float)weight * rtPredResult[i][j];
			}
		}
		
		return YTS;
	}
	
	public static double[][] rtPred()
	{
		int nts = GlobalVar.XTS.length;
		GlobalVar.assignednodets = Util.zeros(nts, 1);
		
		int verbose = 0;
		
		double[][] YTS = Util.zeros(nts, GlobalVar.tree.getPredictions()[0].length);
		
		if(verbose == 1)
			System.out.printf("computation of indexes\n");
		
		int[] currentRows = new int[nts];
		for(int i = 0; i< nts; i++)
			currentRows[i] = i+1;
		
		getLeafts(1, currentRows);
		
		if(verbose == 1)
			System.out.printf("computation of predictions\n");
				
		for(int i = 1; i<= nts; i++)
		{
			int row = GlobalVar.tree.getIndexprediction()[(int)GlobalVar.assignednodets[i-1][0]-1][0];
			for(int j = 0; j< YTS[i-1].length; j++)
				YTS[i-1][j] = GlobalVar.tree.getPredictions()[row-1][j];
		}
		
		return YTS;
	}
	
	public static void getLeafts(int currentNode, int[] currentRows)
	{
		int testAttribute = GlobalVar.tree.getTestattribute()[currentNode-1][0];
		float testThreshold;
		int[] leftInd;			/* use int 0 and 1 to present logical type in matlab script*/
		int[] rightInd;
		
		if(testAttribute == 0)
		{
			for(int i = 0; i< currentRows.length; i++)
				GlobalVar.assignednodets[currentRows[i]-1][0] = currentNode;
		}
		else
		{
			testThreshold = GlobalVar.tree.getTestthreshold()[currentNode-1][0];
			
			/* leftind=(XTS(currentrows,testattribute)<testthreshold)*/
			leftInd = new int[currentRows.length];
			for(int i = 0; i< currentRows.length; i++)
				if(GlobalVar.XTS[currentRows[i]-1][testAttribute-1] < testThreshold)
					leftInd[i] = 1;
				else
					leftInd[i] = 0;
			rightInd = new int[currentRows.length];
			/* rightind=~leftind*/
			for(int i = 0; i< currentRows.length; i++)
				if(leftInd[i] == 1)
					rightInd[i] = 0;
				else
					rightInd[i] = 1;
			
			int numberOfOnesLeftInd = 0;
			for(int i = 0; i< leftInd.length; i++)
				numberOfOnesLeftInd += leftInd[i];
			int numberOfOnesRightInd = currentRows.length - numberOfOnesLeftInd;
			int[] newCurrentRowsLeftInd = new int[numberOfOnesLeftInd];
			for(int i = 0, j = 0; i< leftInd.length; i++)
				if(leftInd[i] == 1) 
					newCurrentRowsLeftInd[j++] = currentRows[i];
			int[] newCurrentRowsRightInd = new int[numberOfOnesRightInd];
			for(int i = 0, j = 0; i< rightInd.length; i++)
				if(rightInd[i] == 1) 
					newCurrentRowsRightInd[j++] = currentRows[i];
			getLeafts(GlobalVar.tree.getChildren()[currentNode-1][0], newCurrentRowsLeftInd);
			getLeafts(GlobalVar.tree.getChildren()[currentNode-1][1], newCurrentRowsRightInd);
		}
	}
}