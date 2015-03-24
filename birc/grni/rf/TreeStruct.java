package birc.grni.rf;
public class TreeStruct {
	private int[][] nodenumber;	// 1*1
	private int[][] testattribute;
	private float[][] testthreshold;
	private int[][] children;	// two column
	private int[][] ls;
	private float[][] objectweights;
	private float[][] weight;	// 1*1
	private int[][] savepred;	// 1*1
	private int[][] indexprediction;
	private float[][] predictions;
	private float[][] nodesize;
	
	public TreeStruct(int[][] nodenumber, int[][] testattribute, float[][] testthreshold,
							int[][] children, int[][] ls, float[][] objectweights, float[][] weight,
							int[][] savepred, int[][] indexprediction, float[][] predictions, float[][] nodesize) {
		this.nodenumber = nodenumber;
		this.testattribute = testattribute;
		this.testthreshold = testthreshold;
		this.children = children;
		this.ls = ls;
		this.objectweights = objectweights;
		this.weight = weight;
		this.savepred = savepred;
		this.indexprediction = indexprediction;
		this.predictions = predictions;
		this.nodesize = nodesize;
	}
	
	public int[][] getNodenumber() {
		return nodenumber;
	}
	public int[][] getTestattribute() {
		return testattribute;
	}
	public float[][] getTestthreshold() {
		return testthreshold;
	}
	public int[][] getChildren() {
		return children;
	}
	public int[][] getLs() {
		return ls;
	}
	public float[][] getObjectweights() {
		return objectweights;
	}
	public float[][] getWeight() {
		return weight;
	}
	public int[][] getSavepred() {
		return savepred;
	}
	public int[][] getIndexprediction() {
		return indexprediction;
	}
	public float[][] getPredictions() {
		return predictions;
	}
	public float[][] getNodesize() {
		return nodesize;
	}
}