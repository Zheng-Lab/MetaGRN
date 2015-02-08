package birc.grni.en;

public class GlmnetPredictParameters
{
	private GlmnetResult object = null;
	private String type = null;
	private double[][] newx = null;
	private double[] s = null;
	
	private int nargin = -1;
	
	public GlmnetPredictParameters(GlmnetResult object)
	{
		this.object = object;
		this.nargin = 1;
	}
	
	public GlmnetPredictParameters(GlmnetResult object, String type)
	{
		this.object = object;
		this.type = type;
		this.nargin = 2;
	}
	
	public GlmnetPredictParameters(GlmnetResult object, String type, double[][] newx)
	{
		this.object = object;
		this.type = type;
		this.newx = newx;
		this.nargin = 3;
	}
	
	public GlmnetPredictParameters(GlmnetResult object, String type, double[][] newx, double[] s)
	{
		this.object = object;
		this.type = type;
		this.newx = newx;
		this.s = s;
		this.nargin = 4;
	}
	
	public int getNargin()
	{
		return this.nargin;
	}
	
	public GlmnetResult getObject() {
		return object;
	}

	public String getType() {
		return type;
	}

	public double[][] getNewx() {
		return newx;
	}

	public double[] getS() {
		return s;
	}
	
	public void setObject(GlmnetResult object) {
		this.object = object;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setNewx(double[][] newx) {
		this.newx = newx;
	}

	public void setS(double[] s) {
		this.s = s;
	}

	public void setNargin(int nargin) {
		this.nargin = nargin;
	}
}
