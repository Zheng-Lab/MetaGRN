

// line 381 "B_BB_Mixture.jweb"
package org.omegahat.Simulation.MCMC.Examples;


// line 387 "B_BB_Mixture.jweb"
import com.imsl.math.Sfun;
import org.omegahat.GUtilities.ReadData;
import org.omegahat.GUtilities.ArrayTools;
import org.omegahat.Probability.Distributions.UnnormalizedDensity;
import org.omegahat.Probability.Distributions.Density;


// line 8 "B_BB_Mixture.jweb"
public class B_BB_Mixture implements UnnormalizedDensity, Density
{
    
// line 23 "B_BB_Mixture.jweb"
static public boolean ignoreRangeErrors = true;

protected String   inputFile;
protected int      dataLength;

protected int[] dataX;
protected int[] dataN;

// line 11 "B_BB_Mixture.jweb"
    
// line 34 "B_BB_Mixture.jweb"
    
 public String getInputFile() { return inputFile; }

 public int[] getDataX()           { return this.dataX; }
 public int[] setDataX( int[] data ) { return this.dataX = data; }

 public int[] getDataN()           { return this.dataN; }
 public int[] setDataN( int[] data ) { return this.dataN = data; }

// line 12 "B_BB_Mixture.jweb"
    
// line 48 "B_BB_Mixture.jweb"
public B_BB_Mixture() {}


public B_BB_Mixture( String inputFile )
{
    try{
	readData(inputFile);
    }
    catch (java.io.IOException io)
    {
	io.printStackTrace();
	throw new RuntimeException("Error reading data file " + inputFile + ": " + io);
    }
}
    
// line 13 "B_BB_Mixture.jweb"
    
// line 67 "B_BB_Mixture.jweb"
static public double lgamma( int x )
{
    return Sfun.logGamma( (double) x );
}

static public double lgamma( double x )
{
    return Sfun.logGamma( x );
}

static public double lchoose( int n, int x)
{
    if(x>n) return Double.NEGATIVE_INFINITY;

    return lgamma( n+1 ) - lgamma( x+1 ) - lgamma( (n-x)+1 );
}

static public double choose( int n, int x)
{
    return Math.exp( lchoose(n,x) );
}

static public double log_beta_binom( int    x,
                                     int    n,
                                     double pi,
                                     double omega )
{
    double retval = lchoose(n,x) + unnorm_log_beta_binom(x,n,pi,omega);

    return retval;
}

static public double log_binom ( int x,
				 int n,
				 double pi )
{
    double retval = lchoose(n,x) + unnorm_log_binom(x,n,pi);

    return retval;
}


static public double unnorm_log_beta_binom( int    x,
                                     int    n,
                                     double pi,
                                     double omega )
{
    if( (x<0) || (n<0) || (pi<0.) || (pi>1.) || (omega<0.) )
	if( ignoreRangeErrors )
	    return Double.NEGATIVE_INFINITY;
	else
	    throw new RuntimeException("Argument out of range.");

    
    double retval = lgamma( 1.0 / omega ) - lgamma( pi / omega  ) - lgamma( (1.0 - pi) / omega ) +
                    lgamma( x + pi/omega ) + lgamma( n - x + (1.0 - pi) / omega ) - lgamma( 1/omega + n );

    return retval;
}

static public double unnorm_log_binom( int    x,
				       int    n,
				       double pi)

{
    if( (x<0) || (n<0) || (pi<0.) || (pi>1.) )
	if( ignoreRangeErrors )
	    return Double.NEGATIVE_INFINITY;
	else
	    throw new RuntimeException("Argument out of range.");
    
    double retval = ((double) x) * Math.log(pi) + ((double) (n-x)) * Math.log( 1.0 - pi );

    return retval;
}




static public double beta_binom( int x, int n, double pi, double omega )
{
    return Math.exp(log_beta_binom(x,n,pi,omega));
}

static public double binom( int x, int n, double pi )
{
    return Math.exp(log_binom(x,n,pi));
}


static public double unnorm_beta_binom( int x, int n, double pi, double omega )
{
    return Math.exp(unnorm_log_beta_binom(x,n,pi,omega));
}

static public double unnorm_binom( int x, int n, double pi )
{
    return Math.exp(unnorm_log_binom(x,n,pi));
}



static public double mixture_binom_beta_binom( int x, 
					      int n, 
					      double eta, 
					      double pi0,
					      double pi1, double omega1 )
{
    if (eta==1.0)
	return /*(     eta )*/   binom(x,n,pi0); 
    else if(eta==0.0)
	return /*( 1. - eta )*/  beta_binom(x,n,pi1,omega1);
    else
	return (      eta ) * binom(x,n,pi0) + ( 1. - eta ) * beta_binom(x,n,pi1,omega1);

}

static public double unnorm_mixture_binom_beta_binom( int x, int n, 
                                         double eta, 
                                         double pi0,
                                         double pi1, double omega1 )
{
    if (eta==1.0)
	return /*(     eta )*/   unnorm_binom(x,n,pi0); 
    else if(eta==0.0)
	return /*( 1. - eta )*/  unnorm_beta_binom(x,n,pi1,omega1);
    else
	return (      eta ) * unnorm_binom(x,n,pi0) + 
               ( 1. - eta ) * unnorm_beta_binom(x,n,pi1,omega1);

}


static public double log_mixture_binom_beta_binom( int x, int n, 
						   double eta, 
						   double pi0, 
						   double pi1, double omega1 )
{
    return Math.log( mixture_binom_beta_binom ( x, n, eta, pi0, pi1, omega1 ) );
}


static public double unnorm_log_mixture_binom_beta_binom( int x, int n, 
							  double eta, 
							  double pi0,
							  double pi1, double omega1 )
{
    return Math.log( unnorm_mixture_binom_beta_binom ( x, n, eta, pi0, pi1, omega1 ) );
}




static public double log_mixture_binom_beta_binom( int[] x, int[] n,
                                             double eta, 
                                             double pi0, 
                                             double pi1, double omega1 )
{
    if(x.length != n.length) throw new RuntimeException("Length of x and n vectors must match.");
    
    double retval = 0.0;
    
    for(int i=0; i < x.length; i++)
        {
            retval += log_mixture_binom_beta_binom( x[i], n[i], eta, pi0, pi1, omega1 );
        }

    return retval;
}

static public double unnorm_log_mixture_binom_beta_binom( int[] x, int[] n,
							  double eta, 
							  double pi0,
							  double pi1, double omega1 )
{
    if(x.length != n.length) throw new RuntimeException("Length of x and n vectors must match.");
    
    double retval = 0.0;
    
    for(int i=0; i < x.length; i++)
        {
            retval += unnorm_log_mixture_binom_beta_binom( x[i], n[i], eta, pi0, pi1, omega1 );
        }

    return retval;
}

static public double mixture_binom_beta_binom( int[] x, int[] n,
                                         double eta, 
                                         double pi0, 
                                         double pi1, double omega1 )
{
    return Math.exp( log_mixture_binom_beta_binom( x, n, eta, pi0, pi1, omega1 ) );
}

static public double unnorm_mixture_binom_beta_binom( int[] x, int[] n,
                                         double eta, 
                                         double pi0, 
                                         double pi1, double omega1 )
{
    return Math.exp( log_mixture_binom_beta_binom( x, n, eta, pi0, pi1, omega1 ) );
}

// line 14 "B_BB_Mixture.jweb"
    
// line 274 "B_BB_Mixture.jweb"
public void readData( String filename ) throws java.io.IOException
{
    double[][] data = ReadData.readDataAsColumnMatrix( filename, 2, 
                                              true, /* byRow */ 
                                              true  /* verbose */);

    System.err.println( data.length + " rows read from " + filename );
    
    dataX = new int[data.length];
    dataN = new int[data.length];

    for(int i=0; i<data.length; i++)
    {
        dataX[i] = (int) data[i][0];
        dataN[i] = (int) data[i][1];
    }
}

// line 15 "B_BB_Mixture.jweb"
    


// line 330 "B_BB_Mixture.jweb"
public double logPDF( Object paramOb )
{
    double[] params = ArrayTools.Otod( paramOb );
    if(params.length != 4 ) throw new RuntimeException("Wrong number of parameters!");

    double eta    = params[0];
    double pi0    = params[1];
    double pi1    = params[2];
    double omega1 = params[3];

    return log_mixture_binom_beta_binom( dataX, dataN, eta, pi0, pi1, omega1 );
}


public double PDF( Object paramObj )
{
    double[] params = ArrayTools.Otod( paramObj );
    if(params.length != 4 ) throw new RuntimeException("Wrong number of parameters!");

    double eta    = params[0];
    double pi0    = params[1];
    double pi1    = params[2];
    double omega1 = params[3];

    return mixture_binom_beta_binom( dataX, dataN, eta, pi0, pi1, omega1 );
}

// line 16 "B_BB_Mixture.jweb"
    
// line 296 "B_BB_Mixture.jweb"
public double logUnnormalizedPDF( Object paramOb )
{
    double[] params = ArrayTools.Otod( paramOb );
    if(params.length != 4 ) throw new RuntimeException("Wrong number of parameters!");

    double eta    = params[0];
    double pi0    = params[1];
    double pi1    = params[2];
    double omega1 = params[3];

    return unnorm_log_mixture_binom_beta_binom( dataX, dataN, eta, pi0, pi1, omega1 );
}


public double unnormalizedPDF( Object paramObj )
{
    double[] params = ArrayTools.Otod( paramObj );
    if(params.length != 4 ) throw new RuntimeException("Wrong number of parameters!");

    double eta    = params[0];
    double pi0    = params[1];
    double pi1    = params[2];
    double omega1 = params[3];

    return unnorm_mixture_binom_beta_binom( dataX, dataN, eta, pi0, pi1, omega1 );
}


// line 17 "B_BB_Mixture.jweb"
    
// line 361 "B_BB_Mixture.jweb"
static public void main(String[] argv )
{
    String DatafileName = argv[0];

    Double[] params = new Double[4];
    params[0] = new Double( Double.parseDouble( argv[1]) ); // eta
    params[1] = new Double( Double.parseDouble( argv[2]) ); // pi0
    params[2] = new Double( Double.parseDouble( argv[4]) ); // pi1
    params[3] = new Double( Double.parseDouble( argv[5]) ); // omega1

    B_BB_Mixture bbl = new B_BB_Mixture( DatafileName );

    System.err.println("unnormalized logPDF = " + bbl.logUnnormalizedPDF(params) + ".");
    System.err.println("normalized   logPDF = " + bbl.logPDF(params) + ".");
}

// line 18 "B_BB_Mixture.jweb"
}
