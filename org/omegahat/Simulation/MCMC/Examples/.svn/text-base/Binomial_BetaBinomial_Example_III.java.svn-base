
// line 11 "Binomial_BetaBinomial_Example_III.jweb"
/**
 * This class fits a
 * Binomial-BetaBinomial model to a the fequency of
 * Loss-of-Heterozygosity for 40 Chromosome arms  (REFERENCE?)
 * and is intended to serve as a simple example for the HYDRA MCMC library.
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2001/04/04 17:16:19 $
 * @version $Revision: 1.1.1.1 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;


//import java.util.Vector;
//import java.util.Date;

public class Binomial_BetaBinomial_Example_III 
{
 
  static public void main( String[] argv ) throws Throwable
  {
    
    //
    // First, we need a random number generator
    //
    
    CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
    PRNG prng = new CollingsPRNG( a.registerPRNGState() );
    
    //
    // Second, setup the model or 'target' distribution.
    //
    // In this case, we're using a Binomial-BetaBinomial Mixture Model
    //
    
    UnnormalizedDensity target = new Binomial_BetaBinomial_Likelihood();
    
    //
    // Third, setup the proposal distribution 
    //
    // In this case, we'll use the a variable at a time Normal proposal
    //
    
    double[][] Var = new double[][]
                      {{ 0.003, 0.0  , 0.0  , 0.0  },
                       { 0.0  , 0.001, 0.0  , 0.0  },
                       { 0.0  , 0.0  , 0.012, 0.0  },
                       { 0.0  , 0.0  , 0.0  , 0.007}};

    
    HastingsCoupledProposal proposal = new NormalKernelProposal(Var, prng );

    
    //
    // Fourth, create an initial state for the sampler
    //
    
    int numComponents = 200;

    MultiDoubleState state0 = new MultiDoubleState ( numComponents );
    for(int i=0; i < numComponents/2; i++)
	state0.add( new double[]{0.903, 0.228, 0.708, 0.486 } );

    for(int i=numComponents/2; i < numComponents; i++)
	state0.add( new double[]{0.078, 0.831, 0.230, 4.5e-9 } );
    

    //
    // Fifth, we construct the MCMC sampler
    //

    double[] minb = {0.0, 0.0, 0.0, Double.NEGATIVE_INFINITY}; // 0.0};
    double[] maxb = {1.0, 1.0, 1.0, Double.POSITIVE_INFINITY}; // 0.5};

    CustomHastingsCoupledSampler mcmc = 
	mcmc = new BoundedHastingsCoupledSampler( state0,   
						  numComponents,
						  target,
						  proposal,
						  prng,
						  minb,
						  maxb,
						  true);

    //
    // Sixth, we need to add a listener to collect the output of the MCMC run
    //

    // Create the listener 
    ListenerWriter l = new StrippedListenerWriter("MCMC.output");

    // Hook it up to the MCMC object
    MCMCListenerHandle lh = mcmc.registerListener(l);

    // 
    // Finally, everything is ready to run.  
    //

    mcmc.iterate( 10000 );

    //
    // Close the listener (Saves all cached data)
    //
    
    l.close();

  }

}
 
