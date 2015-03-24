

// line 4 "IndependenceProposal.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/IndependenceProposal.java,v 1.1.1.1 2001/04/04 17:16:24 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 27 "IndependenceProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 33 "IndependenceProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;


// line 15 "IndependenceProposal.jweb"
/** 
 *  An independence Markov proposal generates a new state using
 *  a distribution that does not depend on the current state.  
 */
public interface IndependenceProposal extends SymmetricProposal, Generator, Density
{
}

