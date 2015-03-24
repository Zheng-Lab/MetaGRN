

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/NotifyingObject.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    /* none */


/** 
 *  Indicates that this MCMC Object can register and unregister
 *  listeners and will notify them of events.
 */
public interface NotifyingObject 
{
    /** Register a listener to be notified of events. */
    public MCMCListenerHandle registerListener( MCMCListener l );

    /** Unregister a listener to be notified of events. */
    public void unregisterListener( MCMCListenerHandle h );
}

