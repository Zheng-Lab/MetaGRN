

// line 7 "AdaptiveKernelProposal.jweb"
    /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/AdaptiveKernelProposal.java,v 1.1.1.1 2001/04/04 17:16:24 warneg Exp $ */
    /* (c) 2000 Gregory R. Warnes */
    /* May include code (c) 1999 by the Omegahat Project */


// line 122 "AdaptiveKernelProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 128 "AdaptiveKernelProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;
    import org.omegahat.Probability.Distributions.*;

    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 19 "AdaptiveKernelProposal.jweb"
abstract public class AdaptiveKernelProposal extends KernelProposal
{
    
// line 36 "AdaptiveKernelProposal.jweb"
    /* inherited */
    // protected GeneralProposal kernel;
    // protected PRNG            prng;

    public    boolean DEBUG = false;

// line 22 "AdaptiveKernelProposal.jweb"
    
// line 46 "AdaptiveKernelProposal.jweb"
    /* inherited */
    //public GeneralProposal getKernel()                         { return this.kernel; }
    //public GeneralProposal setKernel( GeneralProposal kernel ) { return this.kernel = kernel; }

    //public PRNG getPRNG()            { return this.prng; }
    //public PRNG setPRNG( PRNG prng ) { return this.prng = prng; }



// line 24 "AdaptiveKernelProposal.jweb"
    // implementing Hastings Coupled Proposal //
    
// line 61 "AdaptiveKernelProposal.jweb"
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector)

{
    adapt(stateVector, which);
    return(super.conditionalPDF(state,conditions,which,stateVector));
}

public double logConditionalPDF( Object state, Object conditions, int which, MultiState stateVector )
{
    adapt(stateVector, which);
    return(super.logConditionalPDF(state,conditions,which,stateVector));
}

// line 26 "AdaptiveKernelProposal.jweb"
    
// line 78 "AdaptiveKernelProposal.jweb"
/** Generate a new component state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public Object   generate( Object conditionals, int which, MultiState stateVector  )
{
    adapt(stateVector, which);
    return super.generate( conditionals, which, stateVector );
}


// line 27 "AdaptiveKernelProposal.jweb"
    
// line 95 "AdaptiveKernelProposal.jweb"
public double transitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    adapt(stateVector,which);
    return super.transitionProbability( from, to, which, stateVector );
}

public double logTransitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    adapt(stateVector,which);
    return super.logTransitionProbability( from, to, which, stateVector );
    
}




// line 29 "AdaptiveKernelProposal.jweb"
    
// line 114 "AdaptiveKernelProposal.jweb"
/** modify the state of the enclosed proposal distribution using information from the provided state vector **/
abstract public void adapt( MultiState mstate, int which );

// line 30 "AdaptiveKernelProposal.jweb"
}

