package birc.grni.rf;
import java.util.*;

/**
 *	The structure which packages input parameters of 
 *	Genie3Time.genie3Time function into one with some 
 *	additional information, such as number of parameters 
 *	user input
 *
 *	@author liu xingliang
 */
public class Genie3TimeParam {
	
	private ArrayList<ArrayList<Double>> exprMatrix; 	/* expression data matrix*/
	private ArrayList<Integer> inputIdx; 				/* columns of input genes*/
	
	private	String treeMethod; 							/* specifies which tree procedure is used. Available methods are: 
															* 'RF' - Random Forests (Default method), 
															* 'ET' - Extra Trees
															*/
														 
	private String K; 									/* the number of randomly selected attributes at each node. 
															* Possible values of K: 
															* 'sqrt' - K = square root of the number of input genes (Default value),
															* 'all' - K = number of input genes, or any numerical value
															*/
														 
	private int nbTrees;								/* the number of trees grown in the ensemble. Default value: 1000*/
	private int nargin;
	
	public Genie3TimeParam(ArrayList<ArrayList<Double>> exprMatrix) {
		this.exprMatrix = exprMatrix;
		this.nargin = 1;
	}
	
	public Genie3TimeParam(ArrayList<ArrayList<Double>> exprMatrix, ArrayList<Integer> inputIdx) {
		this.exprMatrix = exprMatrix;
		this.inputIdx = inputIdx;
		this.nargin = 2;
	}
	
	public Genie3TimeParam(ArrayList<ArrayList<Double>> exprMatrix, ArrayList<Integer> inputIdx, String treeMethod) {
		this.exprMatrix = exprMatrix;
		this.inputIdx = inputIdx;
		this.treeMethod = treeMethod;
		this.nargin = 3;
	}
	
	public Genie3TimeParam(ArrayList<ArrayList<Double>> exprMatrix, ArrayList<Integer> inputIdx, String treeMethod, String K) {
		this.exprMatrix = exprMatrix;
		this.inputIdx = inputIdx;
		this.treeMethod = treeMethod;
		this.K = K;
		this.nargin = 4;
	}
	
	public Genie3TimeParam(ArrayList<ArrayList<Double>> exprMatrix, ArrayList<Integer> inputIdx, String treeMethod, String K, int nbTrees) {
		this.exprMatrix = exprMatrix;
		this.inputIdx = inputIdx;
		this.treeMethod = treeMethod;
		this.K = K;
		this.nbTrees = nbTrees;
		this.nargin = 5;
	}
	
	public ArrayList<ArrayList<Double>> getExprMatrix() {
		return exprMatrix;
	}

	public ArrayList<Integer> getInputIdx() {
		return inputIdx;
	}

	public String getTreeMethod() {
		return treeMethod;
	}

	public String getK() {
		return K;
	}

	public int getNbTrees() {
		return nbTrees;
	}

	public int getNargin() {
		return nargin;
	}
}