

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/GeneralProposal.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import org.omegahat.Probability.Distributions.*;


/** 
 *  A General proposal distribution generates generates the next state using function of the current state. It must 
 *  Provide the methods defined in the interface <code>ConditionalDensity</code> so that the Metropolis-Hastings style generators
 *  can compute the acceptance probability.
 */
    public interface GeneralProposal extends MarkovProposal, ConditionalDensity
{

    
    /** 
     * Convenience method for computing the probability of proposing a move.  
     * <p>
     * Probably implemented as:<p>
     * <code>
     * double transitionProbability( Object from, Object to );<p>
     * {<p>
     *    return conditionalPDF( to, from );<p>
     * }<p>
     * </code><p>
     */
    double transitionProbability( Object from, Object to );

    /** 
     * Convenience method for computing the log probability of proposing a move.  
     * <p>
     * Probably implemented as:<p>
     * <code>
     * double logTransitionProbability( Object from, Object to );<p>
     * {<p>
     *    return logConditionalPDF( to, from );<p>
     * }<p>
     * </code><p>
     */
    double logTransitionProbability( Object from, Object to );



}

