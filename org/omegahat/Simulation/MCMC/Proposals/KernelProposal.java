

// line 7 "KernelProposal.jweb"
    /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/KernelProposal.java,v 1.1.1.1 2001/04/04 17:16:24 warneg Exp $ */
    /* (c) 2000 Gregory R. Warnes */
    /* May include code (c) 1999 by the Omegahat Project */


// line 155 "KernelProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 161 "KernelProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 19 "KernelProposal.jweb"
public class KernelProposal implements HastingsCoupledProposal 
{
  
// line 36 "KernelProposal.jweb"
GeneralProposal kernel;
PRNG            prng;

// line 22 "KernelProposal.jweb"
  
// line 43 "KernelProposal.jweb"
public GeneralProposal getKernel()                         { return this.kernel; }
public GeneralProposal setKernel( GeneralProposal kernel ) { return this.kernel = kernel; }

public PRNG getPRNG()            { return this.prng; }
public PRNG setPRNG( PRNG prng ) { return this.prng = prng; }



// line 24 "KernelProposal.jweb"
  // implementing Hastings Coupled Proposal //
  
// line 57 "KernelProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector)
{
     double retval = 0.0;
     for(int i=0; i < stateVector.size(); i++)
        retval += kernel.conditionalPDF( state, stateVector.get(i) );
     retval = retval / (double) stateVector.size();
     return retval;
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logConditionalPDF( Object state, Object conditions, int which, MultiState stateVector )
{
    return Math.log(conditionalPDF(state,conditions,which,stateVector) );
}

// line 26 "KernelProposal.jweb"
  
// line 87 "KernelProposal.jweb"
/** Generate a new component state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public Object   generate( Object conditionals, int which, MultiState stateVector  )
{
    int from = (int) ( ((double) stateVector.size()) * prng.nextDouble() );  // uniform on [0, size-1]

    return kernel.generate( stateVector.get(from) );
}


// line 27 "KernelProposal.jweb"
  
// line 105 "KernelProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double transitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    return conditionalPDF( to, from, which, stateVector );
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logTransitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    return logConditionalPDF( to, from, which, stateVector );
}



// line 28 "KernelProposal.jweb"
 
  
// line 135 "KernelProposal.jweb"
public KernelProposal()
{
}


public KernelProposal(PRNG prng)
{
  this.prng   = prng;
}

public KernelProposal( GeneralProposal kernel, PRNG prng )
{
  this.kernel = kernel;
  this.prng   = prng;
}

// line 30 "KernelProposal.jweb"
}

