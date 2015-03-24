package birc.grni.en;

/**
Returning structure of elnet function
*/
public class ElnetResult
{
	private double[] a0;//plhs_1
	private double[][] ca;
	private double[] caOneDim;//plhs_2
	private double[] ia;//plhs_3
	private double[] nin;//plhs_4
	private double[] rsq;//plhs_5
	private double[] alm;//plhs_6
	private int nlp;//plhs_7
	private int jerr;//plhs_8
		
	private int nlhs;//preserved, no use until now
		
	public ElnetResult()
	{
	}
	
	public void setA0(double[] v, int len)
	{	
		this.a0 = new double[len];
		if(len != v.length)
		{
			System.out.printf("ElnetResult: setA0: a0 doesn't have same length with input");
			System.exit(1);
		}
		for(int i = 0; i< len; i++)
			this.a0[i] = v[i];
	}

	public void setCaOneDim(double[] v, int caRow, int caColumn)
	{
		this.caOneDim = new double[caRow * caColumn];
		this.ca = new double[caRow][caColumn];
		if((caRow * caColumn) != v.length)
		{
			System.out.printf("ElnetResult: setCaOneDim: caOneDim doesn't have same length with input");
			System.exit(1);
		}
		for(int i = 0; i< caRow * caColumn; i++)
			this.caOneDim[i] = v[i];
		
		/* fill ca automatically (column-wise)*/
		for(int i = 0; i< caOneDim.length; i++)
			ca[i%ca.length][i/ca.length] = caOneDim[i];
	}
	
	public void setIa(double[] v, int len)
	{
		this.ia = new double[len];
		if(len != v.length)
		{
			System.out.printf("ElnetResult: setIa: ia doesn't have same length with input");
			System.exit(1);
		}
		for(int i = 0; i< len; i++)
			this.ia[i] = v[i];
	}
	
	public void setNin(double[] v, int len)
	{
		this.nin = new double[len];
		if(len != v.length)
		{
			System.out.printf("ElnetResult: setNin: nin doesn't have same length with input");
			System.exit(1);
		}
		for(int i = 0; i< len; i++)
			this.nin[i] = v[i];
	}
	
	public void setRsq(double[] v, int len)
	{
		this.rsq = new double[len];
		if(len != v.length)
		{
			System.out.printf("ElnetResult: setRsq: rsq doesn't have same length with input");
			System.exit(1);
		}
		for(int i = 0; i< len; i++)
			this.rsq[i] = v[i];
	}
	
	public void setAlm(double[] v, int len)
	{
		this.alm = new double[len];
		if(len != v.length)
		{
			System.out.printf("ElnetResult: setAlm: alm doesn't have same length with input");
			System.exit(1);
		}
		for(int i = 0; i< len; i++)
			this.alm[i] = v[i];
	}
	
	public void setNlp(int nlp)
	{
		this.nlp = nlp;
	}
	
	public void setJerr(int jerr)
	{
		this.jerr = jerr;
	}
	
	public double[] getA0()
	{
		return this.a0;
	}
	
	public double[][] getCa()
	{
		return this.ca;
	}
	
	public double[] getIa()
	{
		return this.ia;
	}
	
	public double[] getNin()
	{
		return this.nin;
	}
	
	public double[] getRsq()
	{
		return this.rsq;
	}
	
	public double[] getAlm()
	{
		return this.alm;
	}
	
	public int getNlp()
	{
		return this.nlp;
	}
	
	public int getJerr()
	{
		return this.jerr;
	}
	
	public int getNlhs()
	{
		return this.nlhs;
	}
}