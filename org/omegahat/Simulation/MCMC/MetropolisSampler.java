

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/MetropolisSampler.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    /* none */


/** 
 *  A Metropolis Markov Chain sampler.
 * 
 *  <p> Metropolis samplers are Markov Chains that generate the next
 *  state by making generating a prospective next state from a known
 *  distribution, and then deciding whether to use prospective state
 *  by computing the Metropolis acceptance function.  This function
 *  depends only on the ratio of the target distribution's probability
 *  density at the current and proposed points.  Hence
 *  normalization constants are not required. 
 *
 *  <p> the Metropolis sampler is only valid for proposal
 * distributions are symmetric in that P(X|Y) = P(Y|X).
 */
public interface MetropolisSampler extends MarkovChain
{
}
