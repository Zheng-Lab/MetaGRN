package birc.grni.en;

public class GlmnetResult
{
	private double[] a0;
	private double[][] beta;
	private double[] dev;
	private double nulldev;
	private int[] df;
	private double[] lambda;
	private int npasses;
	private int jerr;
	private int[] dim;
	private String classString;
	
	public GlmnetResult()
	{
		
	}
	
	public double[] getA0() {
		return a0;
	}

	public void setA0(double[] a0) {
		this.a0 = a0;
	}

	public double[][] getBeta() {
		return beta;
	}

	public void setBeta(double[][] beta) {
		this.beta = beta;
	}

	public double[] getDev() {
		return dev;
	}

	public void setDev(double[] dev) {
		this.dev = dev;
	}

	public double getNulldev() {
		return nulldev;
	}

	public void setNulldev(double nulldev) {
		this.nulldev = nulldev;
	}

	public int[] getDf() {
		return df;
	}

	public void setDf(int[] df) {
		this.df = df;
	}

	public double[] getLambda() {
		return lambda;
	}

	public void setLambda(double[] lambda) {
		this.lambda = lambda;
	}

	public int getNpasses() {
		return npasses;
	}

	public void setNpasses(int npasses) {
		this.npasses = npasses;
	}

	public int getJerr() {
		return jerr;
	}

	public void setJerr(int jerr) {
		this.jerr = jerr;
	}

	public int[] getDim() {
		return dim;
	}

	public void setDim(int[] dim) {
		this.dim = dim;
	}

	public String getClassString() {
		return classString;
	}

	public void setClassString(String classString) {
		this.classString = classString;
	}
}
