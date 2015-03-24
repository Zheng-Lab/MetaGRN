

// line 7 "AdaptiveNormalDualKernelProposal.jweb"
    /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/AdaptiveNormalDualKernelProposal.java,v 1.1.1.1 2001/04/04 17:16:26 warneg Exp $ */
    /* (c) 2000 Gregory R. Warnes */
    /* May include code (c) 1999 by the Omegahat Project */


// line 114 "AdaptiveNormalDualKernelProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 120 "AdaptiveNormalDualKernelProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;
    import org.omegahat.Simulation.MCMC.Proposals.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 19 "AdaptiveNormalDualKernelProposal.jweb"
public class AdaptiveNormalDualKernelProposal extends AdaptiveKernelProposal
{
  
// line 32 "AdaptiveNormalDualKernelProposal.jweb"
    GeneralProposal dualKernel[] = new GeneralProposal[2];

    // *inherited*
    // GeneralProposal kernel;
    // PRNG            prng;

// line 22 "AdaptiveNormalDualKernelProposal.jweb"
  
// line 42 "AdaptiveNormalDualKernelProposal.jweb"
// public double[][] getCovariance() { return ((NormalMetropolisProposal) kernel).getCovariance(); }
// public double[][] setCovariance(double[][] cov ) { return ((NormalMetropolisProposal) kernel).setCovariance( cov ); }
public int        dim()           { return ((NormalMetropolisProposal) kernel).dim(); }

// line 23 "AdaptiveNormalDualKernelProposal.jweb"
  
// line 50 "AdaptiveNormalDualKernelProposal.jweb"
static public double computeOptimalAdjust( int dim, int nsamplers)
{  
  return  Math.pow( ( 1. / (double) nsamplers ) * 
                    ( 4. / ( 2. * (dim) + 1 ) )  , 
                      1. / (4.+(double) dim ) ); 

}
    
// line 24 "AdaptiveNormalDualKernelProposal.jweb"
  
// line 62 "AdaptiveNormalDualKernelProposal.jweb"
public void adapt( MultiState mstate, int which )
{
    MultiDoubleState tmp = new MultiDoubleState(mstate);
    double[][] var  = tmp.correctedVar();
    double[]   mean = tmp.mean();

    


}

// line 25 "AdaptiveNormalDualKernelProposal.jweb"
  
// line 78 "AdaptiveNormalDualKernelProposal.jweb"
protected AdaptiveNormalDualKernelProposal()
{
}


public AdaptiveNormalDualKernelProposal(int dimensions, PRNG prng)
{
  this.prng   = prng;
  this.kernel = new NormalMetropolisProposal(dimensions, prng);
}

public AdaptiveNormalDualKernelProposal(double[] mean1, double[][] var1,   // N(mean1,var1)
                                double[][] var2,                   // Kernel N(X_i,var2)
                                double mixing,
                                PRNG prng )
{
  this.prng   = prng;
  
  double[] mean = new double[var1.length];
  for(int i=0; i<mean.length; i++)
      mean[i] = 0.0;

  dualKernel[0] = new NormalProposal(mean1,var1, prng);
  dualKernel[1] = new NormalMetropolisProposal(var2, prng);

  double[] dualProb = new double[2];
  dualProb[0] = 1.-mixing;
  dualProb[1] = mixing;

  this.kernel = new MixtureProposal( dualKernel, dualProb, prng );
}

// line 26 "AdaptiveNormalDualKernelProposal.jweb"
}

