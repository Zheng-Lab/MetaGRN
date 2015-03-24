package birc.grni.en;

public class GlmnetOptions
{
	private double[] weights = new double[0];
	private double alpha = 0.1;
	private int nlambda = 100;
	private double lambda_min = 0;
	private double[] lambda = new double[0];
	private boolean standardize = true;
	private double thresh = 1E-4;
	private int dfmax = 0;
	private double pmax = 0;
	private double[] exclude = new double[0];
	private double[] penalty_factor = new double[0];
	private double[][] maxit = {{100}};
	private boolean HessianExact = false;
	private String type = "covariance";
	
	public GlmnetOptions()
	{
	}
	
	public GlmnetOptions(GlmnetOptions opts)
	{
		this.weights = opts.getWeights();
		this.alpha = opts.getAlpha();
		this.nlambda = opts.getNlambda();
		this.lambda_min = opts.getLambda_min();
		this.lambda = opts.getLambda();
		this.standardize = opts.getStandardize();
		this.thresh = opts.getThresh();
		this.dfmax = opts.getDfmax();
		this.pmax = opts.getPmax();
		this.exclude = opts.getExclude();
		this.penalty_factor = opts.getPenalty_factor();
		this.maxit = opts.getMaxit();
		this.HessianExact = opts.getHessianExact();
		this.type = opts.getType();
	}
	
	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public int getNlambda() {
		return nlambda;
	}

	public void setNlambda(int nlambda) {
		this.nlambda = nlambda;
	}

	public double getLambda_min() {
		return lambda_min;
	}

	public void setLambda_min(double lambda_min) {
		this.lambda_min = lambda_min;
	}

	public double[] getLambda() {
		return lambda;
	}

	public void setLambda(double[] lambda) {
		this.lambda = lambda;
	}

	public boolean getStandardize() {
		return standardize;
	}

	public void setStandardize(boolean standardize) {
		this.standardize = standardize;
	}

	public double getThresh() {
		return thresh;
	}

	public void setThresh(double thresh) {
		this.thresh = thresh;
	}

	public int getDfmax() {
		return dfmax;
	}

	public void setDfmax(int dfmax) {
		this.dfmax = dfmax;
	}

	public double getPmax() {
		return pmax;
	}

	public void setPmax(double pmax) {
		this.pmax = pmax;
	}

	public double[] getExclude() {
		return exclude;
	}

	public void setExclude(double[] exclude) {
		this.exclude = exclude;
	}

	public double[] getPenalty_factor() {
		return penalty_factor;
	}

	public void setPenalty_factor(double[] penalty_factor) {
		this.penalty_factor = penalty_factor;
	}

	public double[][] getMaxit() {
		return maxit;
	}

	public void setMaxit(double[][] maxit) {
		this.maxit = maxit;
	}

	public boolean getHessianExact() {
		return HessianExact;
	}

	public void setHessianExact(boolean hessianExact) {
		HessianExact = hessianExact;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
