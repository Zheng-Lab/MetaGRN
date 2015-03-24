

// line 265 "NormalMetropolisComponentProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 271 "NormalMetropolisComponentProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
  import org.omegahat.GUtilities.ArrayTools;
	


// line 11 "NormalMetropolisComponentProposal.jweb"
public class NormalMetropolisComponentProposal 
	implements GeneralProposal,
		   SymmetricProposal, 
		   TimeDependentProposal
		
{

    
// line 32 "NormalMetropolisComponentProposal.jweb"
protected PRNGDistributionFunctions  prob;

protected boolean DEBUG = false;
protected double  sigma[];

protected int     time  = 0;
protected int     which = 0;


// line 19 "NormalMetropolisComponentProposal.jweb"
    
// line 44 "NormalMetropolisComponentProposal.jweb"
    
public void setCovariance( double[][] cov )
{
  if( cov.length != sigma.length ) 
	throw new RuntimeException( "Specified covariance has incorrect number of dimensions: Length is " + 
	                             cov.length + " but should be " + sigma.length );

  for(int i=0; i < sigma.length; i++)
	sigma[i] = Math.sqrt(cov[i][i]);

}	


public void setCovariance( double[] cov )
{
  if( cov.length != sigma.length ) 
	throw new RuntimeException( "Specified covariance has incorrect number of dimensions: Length is " + 
	                             cov.length + " but should be " + sigma.length );

  for(int i=0; i < sigma.length; i++)
	sigma[i] = Math.sqrt(cov[i]);
}	


// line 20 "NormalMetropolisComponentProposal.jweb"
    
// line 73 "NormalMetropolisComponentProposal.jweb"
/** 
 * Constructor for normal increments with identity covariance matrix.
 * @param length number of dimensions
**/
public NormalMetropolisComponentProposal( int length, PRNG prng )
{
  sigma = new double[length];
  for(int i=0; i < length; i++)
	sigma[i] = 1.0;

  prob     = new PRNGDistributionFunctions(prng);

}

/** 
 *  Constructor for normal increments with specified covariance
 *  matrix. Note that off diagonal elements will be ignored.
 * 
 *  @param var   variance matrix
**/
public NormalMetropolisComponentProposal( double[] var, PRNG prng)
{
  sigma = new double[var.length];
  for(int i=0; i < var.length; i++)
	sigma[i] = Math.sqrt(var[i]);

  prob     = new PRNGDistributionFunctions(prng);
}

// line 21 "NormalMetropolisComponentProposal.jweb"
    
// line 153 "NormalMetropolisComponentProposal.jweb"
// These really belong somewhere else!

protected double normalPDF( double x, double mu, double sigma )
{
    return 1. / ( Math.sqrt(2 * Math.PI) * sigma ) * Math.exp( - 1.0/2.0 * (x-mu) * (x-mu) / ( sigma * sigma ) );
}

protected double normalLogPDF( double x, double mu, double sigma )
{
    return Math.log( 1. / ( Math.sqrt(2 * Math.PI) * sigma) ) +  -1.0/2.0 * (x-mu) * (x-mu) / ( sigma * sigma );
}

// line 22 "NormalMetropolisComponentProposal.jweb"
    
// line 106 "NormalMetropolisComponentProposal.jweb"
public void timeInc()
{
    time++;
    which++;
    if(which >= sigma.length) which = 0;

    if(DEBUG) System.err.println("Time Incremented to " + time + " Which now " + which );

}

public void resetTime()
{
    time = -1;
    timeInc();
}

public int getTime()
{
    return time;
}


// line 23 "NormalMetropolisComponentProposal.jweb"
    
// line 132 "NormalMetropolisComponentProposal.jweb"
public Object generate( Object center )
{
    double[] mean   = ArrayTools.Otod( center );
    double[] retval = new double[mean.length];

    for(int i=0; i < mean.length; i++)
    {
	if(i==which)
	    {
		retval[i] = mean[i] + prob.normalRand() * sigma[which] ;
	    }
	else
	    retval[i] = mean[i];
    }
    
    return retval;
}

// line 24 "NormalMetropolisComponentProposal.jweb"
    
// line 212 "NormalMetropolisComponentProposal.jweb"
public double conditionalPDF( Object state, Object conditionals ) 
{
	return transitionProbability( conditionals, state );
}



public double logConditionalPDF( Object state, Object conditionals )
{
	return logTransitionProbability( conditionals, state );
}

// line 25 "NormalMetropolisComponentProposal.jweb"
    
// line 169 "NormalMetropolisComponentProposal.jweb"
    public double transitionProbability( Object from, Object to )
    {
        double[] x = ArrayTools.Otod( to );
        double[] mean = ArrayTools.Otod( from );

	double retval = 0.0;

	for(int i =0; i < x.length; i++)
	{	
	   if (i != which && x[i] != mean[i] )
		return 0.0;
	   else if (i == which )
		retval = normalPDF( x[i] , mean[i] ,sigma[i] );
	}
	
	return retval;	
    }

    public double logTransitionProbability( Object from, Object to )
    { 
        double[] x    = ArrayTools.Otod( to );
        double[] mean = ArrayTools.Otod( from );

	double retval = Double.NEGATIVE_INFINITY;

	for(int i =0; i < x.length; i++)
	{	
	   if (i != which && x[i] != mean[i] )
		return Double.NEGATIVE_INFINITY;
	   else if (i == which )
		retval = normalLogPDF( x[i] , mean[i] , sigma[i] );
	}
	
	return retval;	
  
    }
    

// line 26 "NormalMetropolisComponentProposal.jweb"
    
// line 230 "NormalMetropolisComponentProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: NormalMetropolisComponentProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double[] var = {1.0,2.0,3.0};

    double[]   start = new double[]{ 0., 0., 0. };
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    NormalMetropolisComponentProposal example = new NormalMetropolisComponentProposal( var, prng );

    for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         Double[] retval = (Double[]) example.generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i].doubleValue();
             }

     }

}

// line 27 "NormalMetropolisComponentProposal.jweb"
}
