

// line 199 "AdaptiveNormalKernelProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 205 "AdaptiveNormalKernelProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
//   import Jama.*;



// line 14 "AdaptiveNormalKernelProposal.jweb"
public class AdaptiveNormalKernelProposal extends AdaptiveKernelProposal 
{

    
// line 28 "AdaptiveNormalKernelProposal.jweb"
protected double inflationFactor = 1.0;
protected double optimalAdjust = Double.NaN;

protected boolean DEBUG = false;

// line 18 "AdaptiveNormalKernelProposal.jweb"
    
// line 50 "AdaptiveNormalKernelProposal.jweb"
static public double computeOptimalAdjust( int dim, int nsamplers)
{  
  return  Math.pow( ( 1. / (double) nsamplers ) * 
		    ( 4. / ( 2. + (double) dim ) )  , 
		    2. / (4.+(double) dim ) ); 

}
    
// line 19 "AdaptiveNormalKernelProposal.jweb"
    
// line 37 "AdaptiveNormalKernelProposal.jweb"
public double getInflationFactor() { return this.inflationFactor; }
public double setInflationFactor( double factor ) { return this.inflationFactor = factor; }

public boolean getDEBUG() { return this.DEBUG; }
public boolean setDEBUG( boolean flag ) { return this.DEBUG = flag; }

public double getOptimalAdjust() { return this.optimalAdjust; }
public double  setOptimalAdjust( double value ) { return this.optimalAdjust = value; }

// line 20 "AdaptiveNormalKernelProposal.jweb"
    
// line 63 "AdaptiveNormalKernelProposal.jweb"
/** Constructor for normal increment proposal with specified
 * covariance matrix, no variance inflation @param var variance matrix
**/
public AdaptiveNormalKernelProposal( double[][] var, PRNG prng )
{
    this(var, 1.0, prng );
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public AdaptiveNormalKernelProposal( double[][] var, double inflationFactor , PRNG prng )
{
    kernel = new NormalMetropolisProposal(var, prng );
    this.inflationFactor = inflationFactor;
    this.prng = prng;
}

/** Constructor for a sperical independent standard normal increments
 *  No variance inflation.
 *  @param length number of dimensions
**/
public AdaptiveNormalKernelProposal( int length , PRNG prng )
{
    this( length, 1.0, prng );
}



/** Constructor for a spherical independent standard normal increments 
 * @param length number of dimensions
 * @param inflationFactor factor to inflate observed variance when adapting.
**/
public AdaptiveNormalKernelProposal( int length, double inflationFactor , PRNG prng )
{
    kernel = new NormalMetropolisProposal( length, prng );
    this.inflationFactor = inflationFactor;
    this.prng = prng; 
}


// line 21 "AdaptiveNormalKernelProposal.jweb"
    
// line 108 "AdaptiveNormalKernelProposal.jweb"
public void adapt( MultiState mstate, int which )
{
  MultiDoubleState states  = new MultiDoubleState(mstate);
  double[][]       var     = states.correctedVar();

  int              dim     = ((NormalMetropolisProposal) kernel).dim();
  int              nchains = states.size();


  //  System.err.println("Adapting...");

  if (Double.isNaN(optimalAdjust))
     optimalAdjust = computeOptimalAdjust( dim, nchains );


  /* inflate the covariance matrix by the specified factor */
  for(int i=0; i<dim; i++)
  {
      for(int j=0; j<dim; j++)
          {
              var[i][j] = var[i][j] * optimalAdjust * inflationFactor;
          }
  }
  

  boolean success = false; 
  int tries = 0;
  while(!success && tries < 10 )
    {
      try 
	{
	  ((NormalProposal) kernel).setCovariance(var);
	  success = true;
	}
      catch ( RuntimeException re )
	{
	  success = false;
	  tries++;

	  for(int i=0; i < dim; i++)
	    var[i][i] *= 1.05;
	}
      }

}      
// line 22 "AdaptiveNormalKernelProposal.jweb"
    
// line 157 "AdaptiveNormalKernelProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: AdaptaveNormalProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho},{rho,1.0,rho},{rho,rho,1.0}}; 
    Double[]   start = new Double[]{ new Double(0.),
                                     new Double(0.),
                                     new Double(0.)};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    AdaptiveNormalKernelProposal example = new AdaptiveNormalKernelProposal( var, 1.0, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         Double[] retval = (Double[]) ((NormalMetropolisProposal) example.kernel).generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i].doubleValue();
             }

         System.out.println( ((MVNormal) example.kernel).PDF( retval ) );
     }

}

// line 23 "AdaptiveNormalKernelProposal.jweb"
}
