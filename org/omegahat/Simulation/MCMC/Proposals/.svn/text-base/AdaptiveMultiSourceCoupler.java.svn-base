

// line 190 "AdaptiveMultiSourceCoupler.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 196 "AdaptiveMultiSourceCoupler.jweb"
  import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;

//  import org.omegahat.GUtilities.Distance;

//  import Jama.*;



// line 14 "AdaptiveMultiSourceCoupler.jweb"
public class AdaptiveMultiSourceCoupler extends AdaptiveMultiKernelProposal 
{

    
// line 27 "AdaptiveMultiSourceCoupler.jweb"
protected double inflationFactor = 1.0;

protected boolean DEBUG = false;

protected int     numNeighbors = -1; // anything less than 1 means "all"

protected int     method = MultiDoubleState.Mahalanobis;  // use Mahalanobis distance to decide nearest neighbors
// line 18 "AdaptiveMultiSourceCoupler.jweb"
    
// line 38 "AdaptiveMultiSourceCoupler.jweb"
public double getInflationFactor() { return this.inflationFactor; }
public double setInflationFactor( double factor ) { return this.inflationFactor = factor; }

public boolean getDEBUG() { return this.DEBUG; }
public boolean setDEBUG( boolean flag ) { return this.DEBUG = flag; }

public int    getDistanceMethod() { return this.method; }
public int    setDistanceMethod( int method ) { return this.method=method; }

// line 19 "AdaptiveMultiSourceCoupler.jweb"
    
// line 52 "AdaptiveMultiSourceCoupler.jweb"
/**
 * Constructor for normal increment proposal with specified
 * covariance matrix, no variance inflation 
 * @param var variance matrix
**/
public AdaptiveMultiSourceCoupler( int numStates, int numNeighbors, double[][] var, PRNG prng )
{
    this(numStates, numNeighbors, var, 1.0, prng );
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public AdaptiveMultiSourceCoupler( int numStates,  int numNeighbors, double[][] var,
                                        double inflationFactor , PRNG prng )
{
    kernels = new NormalMetropolisProposal[numStates];
    for(int i=0; i < numStates; i++)
        {
            kernels[i] = new NormalMetropolisProposal(var, prng );
        }
    this.inflationFactor = inflationFactor;
    this.prng = prng;
    this.numNeighbors = numNeighbors;

}

/** Constructor for a sperical independent standard normal increments
 *  No variance inflation.
 *  @param length number of dimensions
**/
public AdaptiveMultiSourceCoupler( int numStates,  int numNeighbors, int length , PRNG prng )
{
    this(numStates, numNeighbors, length, 1.0, prng );
}



/** Constructor for a spherical independent standard normal increments 
 * @param length number of dimensions
 * @param inflationFactor factor to inflate observed variance when adaptng.
**/
public AdaptiveMultiSourceCoupler( int numStates,  int numNeighbors, int length, double inflationFactor , PRNG prng )
{
    kernels = new NormalMetropolisProposal[numStates];
    for(int i=0; i < numStates; i++)
        {
            kernels[i] = new NormalMetropolisProposal( length, prng );
        }

    this.inflationFactor = inflationFactor;
    this.prng = prng; 
    this.numNeighbors = numNeighbors;
}


// line 20 "AdaptiveMultiSourceCoupler.jweb"
    
// line 112 "AdaptiveMultiSourceCoupler.jweb"
public void adapt( MultiState mstate, int which )
{

  int dim = ((NormalMetropolisProposal) kernels[0]).dim();

  if(numNeighbors<2) numNeighbors = mstate.size();

  if(DEBUG)  System.err.print("Adapting.");

  MultiDoubleState states = new MultiDoubleState(mstate);
    
  double[][] overallVar = states.correctedVar();

  int numState = mstate.size();
  for(int outer=0; outer < numState; outer++)
  {
      double[][] var = new double[overallVar.length][overallVar.length];
      
      double mult = Math.pow(2.0, -(numState-outer + 1) );

      for(int i=0; i<dim; i++)
          for(int j=0; j<dim; j++)
              {
                  var[i][j] = overallVar[i][j] * mult;
              }
      
      ((NormalMetropolisProposal) kernels[outer]).setCovariance(var );
  }

}      
// line 21 "AdaptiveMultiSourceCoupler.jweb"
    
// line 146 "AdaptiveMultiSourceCoupler.jweb"
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
    
    AdaptiveMultiSourceCoupler example = new AdaptiveMultiSourceCoupler( 10, 0, var, 1.0, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         Double[] retval = (Double[]) ((NormalMetropolisProposal) example.kernels[0]).generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i].doubleValue();
             }

         System.out.println( ((MVNormal) example.kernels[0]).PDF( retval ) );
     }

}

// line 22 "AdaptiveMultiSourceCoupler.jweb"
}
