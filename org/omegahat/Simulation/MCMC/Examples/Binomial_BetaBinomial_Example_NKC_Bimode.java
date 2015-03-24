
// line 11 "Binomial_BetaBinomial_Example_NKC_Bimode.jweb"
/**
 * This class fits a
 * Binomial-BetaBinomial model to a the fequency of
 * Loss-of-Heterozygosity for 40 Chromosome arms  (REFERENCE?)
 * and is intended to serve as a simple example for the HYDRA MCMC library.
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2001/04/04 17:16:23 $
 * @version $Revision: 1.1.1.1 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.GUtilities.ReadData;

//import java.util.Vector;
//import java.util.Date;

public class Binomial_BetaBinomial_Example_NKC_Bimode
{
 
  static public void main( String[] argv ) throws java.io.IOException
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
    
    UnnormalizedDensity target = new Binomial_BetaBinomial_Likelihood_Bimode();
    //    UnnormalizedDensity target = new Binomial_BetaBinomial_Likelihood();
    
    
    //
    // Third, create an initial states for the sampler
    //
    
    double[][] stateMat = ReadData.readDataAsColumnMatrix( "NKC.states.in", 
							   4,    /* 4 columns */
							   true, /* byRow */ 
							   true  /* verbose */);

    int numComponents = stateMat.length;

    MultiDoubleState state0 = new MultiDoubleState ( numComponents );
    for(int i=0; i < numComponents; i++)
	state0.add( stateMat[i] );

    //
    // Fourth, setup the proposal distribution 
    //
    //
    
    // read the the variance for each dimension from the file "variance.NKC"
    //
    double[][] var = ReadData.readDataAsColumnMatrix( "NKC.variance.in", 
						      4,    /* 4 columns */
						      true, /* byRow */ 
						      true  /* verbose */);
    
    double scaleFactor = 1.4 * Math.pow( numComponents, -2.0 / (4.0 + 4.0 ) );

    for(int i=0; i < 4; i++)
	for( int j=0; j<4; j++)
	    var[i][j] = var[i][j] * scaleFactor;


    HastingsCoupledProposal proposal = new NormalKernelProposal(var, prng );
    
    
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


    //
    // Create the a listener that writes out the MCMC chain 
    //
    // We want to "thin" the output so we only see the output once per "scan"
    //
    ThinningProxyListener pL = new ThinningProxyListener(numComponents);  // create the proxy
    MCMCListenerHandle pLh = mcmc.registerListener(pL);                   // connect it to the MCMC sampler

    MCMCListenerWriter l1 = new StrippedListenerWriter("NKC.states.out");
    MCMCListenerHandle lh1 = pL.registerListener(l1);

    //
    // Create a listener that reports some useful information
    //
    MCMCListenerWriter l2 = new DistanceWriter("NKC.distance.out");
    MCMCListenerHandle lh2 = mcmc.registerListener(l2);

    // 
    // Finally, everything is ready to run.  
    //

    mcmc.iterate( Integer.parseInt( argv[0] ) );

    //
    // Cleanup 
    //

    l1.flush();
    l1.close();

    l2.flush();
    l2.close();

  }

}
 
