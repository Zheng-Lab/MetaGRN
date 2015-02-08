

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/MCMCState.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    /* none */


/**
 * Marker for objects representing the current state of an MCMC
 * Chain.  In general we don't know anything about them, and we want
 * the state to be immutable except by isntantiating a MarkovChain to
 * operate on it.  
 * Consequently this interface is empty. 
**/
public interface MCMCState {};


