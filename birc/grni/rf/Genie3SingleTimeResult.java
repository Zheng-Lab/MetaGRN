package birc.grni.rf;
public class Genie3SingleTimeResult
{
	private double[] vi;
	private TreeEnsembleStruct[][] treeEnsemble;
	
	public Genie3SingleTimeResult(double[] vi, TreeEnsembleStruct[][] treeEnsemble) 
	{
		this.vi = vi;
		this.treeEnsemble = treeEnsemble;
	}
	
	public double[] getVi()
	{
		return this.vi;
	}
	
	public TreeEnsembleStruct[][] getTreeEnsemble()
	{
		return this.treeEnsemble;
	}
}