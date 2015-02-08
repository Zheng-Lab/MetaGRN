

// line 185 "NormalProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 191 "NormalProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
//   import Jama.*;



// line 11 "NormalProposal.jweb"
public class NormalProposal extends MVNormal implements GeneralProposal
{

    
// line 26 "NormalProposal.jweb"
//public boolean DEBUG = true;
public boolean DEBUG = false;

// line 15 "NormalProposal.jweb"
    
// line 32 "NormalProposal.jweb"
    
/* inherited */

// line 16 "NormalProposal.jweb"
    
// line 40 "NormalProposal.jweb"
/** Constructor for normal with specified mean and covariance matrix.
 * @param mean  mean vector
 * @param var   variance matrix
**/
public NormalProposal( double[] mean, double[][] var, PRNG prng )
{
    super(mean, var, prng );
}

/** Constructor for a spherical normal with the same mean and variances for each dimension.  
 * Each element mean will be set the specified value.
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param mean   mean of each dimension
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0, PRNG prng )
**/
public NormalProposal( int length, double mean, double var, PRNG prng )
{
  super(length, prng );
  double[] mu = new double[length];
  double[][] sigma  = new double[length][length];
  
  for(int i=0; i < length; i++)
    {
      mu[i] = mean;
      sigma[i][i] = var;
      // off diagionals reman 0.0 //
    }
  
  setMean(mu);
  setCovariance(sigma);
  checkConformity();

}

/** Constructor for a (standard) spherical normal with mean 0 and identity covariance matrix 
 * @param length number of dimensions
**/
public NormalProposal( int length , PRNG prng )
{
  super( length , prng );
  double[] mu = new double[length];
  double[][] sigma  = new double[length][length];
  
  for(int i=0; i < length; i++)
    {
      mu[i] = 0.0;
      for(int j=0; j<length; j++)
          sigma[i][j] = (i==j)?1.0:0.0;
    }
  
  setMean(mu);
  setCovariance(sigma);
  checkConformity();

}


// line 17 "NormalProposal.jweb"
    
// line 103 "NormalProposal.jweb"
public Object generate( Object ignored ) 
{
      return super.generate();
}

// line 18 "NormalProposal.jweb"
    
// line 126 "NormalProposal.jweb"
public double conditionalPDF( Object state, Object ignored )
{
    return PDF( state );
}

public double logConditionalPDF( Object state, Object conditionals )
{
    return logPDF( state );
}

// line 19 "NormalProposal.jweb"
    
// line 112 "NormalProposal.jweb"
public double transitionProbability( Object from, Object to )
{
    return conditionalPDF( to, from );
}

public double logTransitionProbability( Object from, Object to )
{ 
    return logConditionalPDF( to, from );
}
// line 20 "NormalProposal.jweb"
    
// line 142 "NormalProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: NormalProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho},{rho,1.0,rho},{rho,rho,1.0}}; 
    double[]   mean = {0.0,0.0,0.0};
    Double[]   start = new Double[]{ new Double(0.),
                                     new Double(0.),
                                     new Double(0.)};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    NormalProposal example = new NormalProposal( mean, var, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         Double[] retval = (Double[]) example.generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i].doubleValue();
             }

         System.out.println( example.PDF( retval ) );
     }

}

// line 21 "NormalProposal.jweb"
}
