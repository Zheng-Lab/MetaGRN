package birc.grni.en;

/**
 * cross validation error of glmnet model
 */

//TODO:

public class CVErr
{
	private double[] cvm;
	private double[] stderr;
	private double[] cvlo;
	private double[] cvup;
	private double lambda_min;
	private double lambda_1se;
	private GlmnetOptions glmnetOptions;
	private GlmnetResult glmnet_object;
	
	public CVErr()
	{
		
	}
	
	public CVErr(double[] cvm, double[] stderr, double[] cvlo,
			double[] cvup, double lambda_min, double lambda_1se,
			GlmnetOptions glmnetOptions, GlmnetResult glmnet_object) {
		super();
		this.cvm = cvm;
		this.stderr = stderr;
		this.cvlo = cvlo;
		this.cvup = cvup;
		this.lambda_min = lambda_min;
		this.lambda_1se = lambda_1se;
		this.glmnetOptions = glmnetOptions;
		this.glmnet_object = glmnet_object;
	}

	public double[] getCvm() {
		return cvm;
	}

	public void setCvm(double[] cvm) {
		this.cvm = cvm;
	}

	public double[] getStderr() {
		return stderr;
	}

	public void setStderr(double[] stderr) {
		this.stderr = stderr;
	}

	public double[] getCvlo() {
		return cvlo;
	}

	public void setCvlo(double[] cvlo) {
		this.cvlo = cvlo;
	}

	public double[] getCvup() {
		return cvup;
	}

	public void setCvup(double[] cvup) {
		this.cvup = cvup;
	}

	public double getLambda_min() {
		return lambda_min;
	}

	public void setLambda_min(double lambda_min) {
		this.lambda_min = lambda_min;
	}

	public double getLambda_1se() {
		return lambda_1se;
	}

	public void setLambda_1se(double lambda_1se) {
		this.lambda_1se = lambda_1se;
	}

	public GlmnetOptions getGlmnetOptions() {
		return glmnetOptions;
	}

	public void setGlmnetOptions(GlmnetOptions glmnetOptions) {
		this.glmnetOptions = glmnetOptions;
	}

	public GlmnetResult getGlmnet_object() {
		return glmnet_object;
	}

	public void setGlmnet_object(GlmnetResult glmnet_object) {
		this.glmnet_object = glmnet_object;
	}
}