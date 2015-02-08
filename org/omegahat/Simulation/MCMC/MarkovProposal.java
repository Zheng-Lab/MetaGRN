

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/MarkovProposal.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import org.omegahat.Probability.Distributions.*;


/** 
 *  A Markov proposal distribution generates a new state based only on
 *  information from the current state
 */
public interface MarkovProposal extends ConditionalGenerator
{
}

