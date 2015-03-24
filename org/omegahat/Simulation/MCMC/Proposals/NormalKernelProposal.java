

// line 7 "NormalKernelProposal.jweb"
    /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/NormalKernelProposal.java,v 1.3 2002/11/02 23:10:21 warnes Exp $ */
    /* (c) 2000 Gregory R. Warnes */
    /* May include code (c) 1999 by the Omegahat Project */


// line 110 "NormalKernelProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 116 "NormalKernelProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 19 "NormalKernelProposal.jweb"
public class NormalKernelProposal extends KernelProposal
{
  
// line 31 "NormalKernelProposal.jweb"
    // *inherited*
    // GeneralProposal kernel;
    // PRNG            prng;

// line 22 "NormalKernelProposal.jweb"
  
// line 39 "NormalKernelProposal.jweb"
public double[][] getCovariance() { return ((NormalMetropolisProposal) kernel).getCovariance(); }
public double[][] setCovariance(double[][] cov ) { return ((NormalMetropolisProposal) kernel).setCovariance( cov ); }
public int        dim()           { return ((NormalMetropolisProposal) kernel).dim(); }

// line 23 "NormalKernelProposal.jweb"
  
// line 47 "NormalKernelProposal.jweb"
static public double computeOptimalAdjust( int dim, int nsamplers)
{  
    double n_factor = Math.pow( (double) nsamplers, -2. / (4.+(double) dim ) ); 

    double mult     = Math.pow(  4. / ( 2. + (dim) )   , 
                                 2. / (4.+(double) dim ) ); 

    System.err.println("Optimal Scaling Factor Computed as: " + n_factor * mult );

    return n_factor * mult;

}
    
static public double computeTerrellAdjust( int d, int n)
{  
    double dim = (double) d;
    double num = (double) n;

    double base = (( Math.pow((dim+8.0),(dim+6.0)/2.0) * 
                     Math.pow( Math.PI  , dim/2.0   )
                    ) / 
                   ( 16.0 * num * 
                     com.imsl.math.Sfun.gamma((dim+8.0)/2.0) *
                     (dim+2.0)
                    )
                   );

    double retval = Math.pow( base, 2.0/(dim+4.0) );

    System.err.println("Terrell Scaling Factor Computed as: " + retval );

    return retval;

}
// line 24 "NormalKernelProposal.jweb"
  
// line 89 "NormalKernelProposal.jweb"
protected NormalKernelProposal()
{
}


public NormalKernelProposal(int dimensions, PRNG prng)
{
  this.prng   = prng;
  this.kernel = new NormalMetropolisProposal(dimensions, prng);
}

public NormalKernelProposal(double[][] var, PRNG prng )
{
  this.prng   = prng;
  this.kernel = new NormalMetropolisProposal(var, prng);
}

// line 25 "NormalKernelProposal.jweb"
}

