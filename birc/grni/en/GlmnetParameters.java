package birc.grni.en;

/**
 * Parameters of glmnet method
 */
public class GlmnetParameters
{
	private double[][] x;
	private double[] y;
	private String family;
	private GlmnetOptions options;
	private int nargin;
	
	public GlmnetParameters(double[][] x, double[] y)
	{
		this.x = x;
		this.y = y;
		this.family = "gaussian";
		this.options = new GlmnetOptions();
		this.nargin = 2;
	}
	
	public GlmnetParameters(double[][] x, double[] y, String family)
	{
		this.x = x;
		this.y = y;
		this.family = family;
		this.options = new GlmnetOptions();
		this.nargin = 3;
	}
	
	public GlmnetParameters(double[][] x, double[] y, String family, GlmnetOptions options)
	{
		this.x = x;
		this.y = y;
		this.family = family;
		this.options = options;
		this.nargin = 4;
	}	
	
	public double[][] getX() {
		return x;
	}

	public double[] getY() {
		return y;
	}

	public String getFamily() {
		return family;
	}

	public GlmnetOptions getOptions() {
		return options;
	}

	public int getNargin() {
		return nargin;
	}
}
