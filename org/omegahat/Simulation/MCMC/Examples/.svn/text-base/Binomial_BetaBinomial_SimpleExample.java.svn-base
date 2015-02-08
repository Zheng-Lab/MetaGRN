package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;

public class Binomial_BetaBinomial_SimpleExample {
  static public void main( String[] argv ) throws Throwable {
    
    CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
    PRNG prng = new CollingsPRNG( a.registerPRNGState() );
    
    UnnormalizedDensity target = new Binomial_BetaBinomial_SimpleLikelihood();
    
    double[] diagVar = new double[]{ 0.083, 0.083, 0.083, 0.042};
    
    SymmetricProposal proposal = 
      new NormalMetropolisComponentProposal(diagVar, prng );
    
    double[] state = new double[]{0.90, 0.23, 0.71, 0.49 };

    CustomMetropolisHastingsSampler mcmc = 
       new CustomMetropolisHastingsSampler(state, target, proposal, 
					   prng, true);    

    MCMCListener l = new ListenerPrinter();
    MCMCListenerHandle lh = mcmc.registerListener(l);

    mcmc.iterate( 10 );

   System.out.println("hi this is the end \n");

  }
}
 
