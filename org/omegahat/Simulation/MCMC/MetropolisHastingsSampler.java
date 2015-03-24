

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/MetropolisHastingsSampler.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    /* none */


/** 
 *  A Metropolis-Hastings Markov Chain sampler.
 * 
 *  <p> Hastings generalized the Metropolis by introducing an
 *  acceptance function that permits non-symmetric proposal
 *  distributions. Other than this change to the acceptance ratio, it
 *  is the same as a Metropolis sampler.  
 */
public interface MetropolisHastingsSampler extends MetropolisSampler
{
}

