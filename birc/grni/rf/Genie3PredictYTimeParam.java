package birc.grni.rf;
import java.util.*;

public class Genie3PredictYTimeParam {
	private ArrayList<ArrayList<Double>> exprMatrix; 	/* expression data matrix*/
	private int outputIdx; 								/* target gene column in expression data matrix*/
	private ArrayList<Integer> testIdx;								
	private TreeEnsembleStruct[][] model;				/* tree ensemble*/
	private int nargin;
	
	public Genie3PredictYTimeParam(ArrayList<ArrayList<Double>> exprMatrix, int outputIdx, ArrayList<Integer> testIdx, TreeEnsembleStruct[][] model) {
		this.exprMatrix = exprMatrix;
		this.outputIdx = outputIdx;
		this.testIdx = testIdx;
		this.model = model;
		this.nargin = 4;
	}
	
	public ArrayList<ArrayList<Double>> getExprMatrix() {
		return exprMatrix;
	}

	public int getOutputIdx() {
		return outputIdx;
	}
	
	public ArrayList<Integer> getTestIdx() {
		return testIdx;
	}
	
	public TreeEnsembleStruct[][] getModel() {
		return model;
	}

	public int getNargin() {
		return nargin;
	}
}