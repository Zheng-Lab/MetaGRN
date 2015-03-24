

// line 7 "AdaptiveMultiKernelProposal.jweb"
    /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/AdaptiveMultiKernelProposal.java,v 1.1.1.1 2001/04/04 17:16:26 warneg Exp $ */
    /* (c) 2000 Gregory R. Warnes */
    /* May include code (c) 1999 by the Omegahat Project */


// line 200 "AdaptiveMultiKernelProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 206 "AdaptiveMultiKernelProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;
    import org.omegahat.Probability.Distributions.*;

    import org.omegahat.Simulation.RandomGenerators.PRNG;
    import org.omegahat.GUtilities.ArrayTools;


// line 19 "AdaptiveMultiKernelProposal.jweb"
abstract public class AdaptiveMultiKernelProposal implements HastingsCoupledProposal 
{
    
// line 36 "AdaptiveMultiKernelProposal.jweb"
protected GeneralProposal[] kernels;

protected PRNG              prng;

public    boolean DEBUG = false;

// line 22 "AdaptiveMultiKernelProposal.jweb"
    
// line 46 "AdaptiveMultiKernelProposal.jweb"
public GeneralProposal[] getKernels() 
{
    return this.kernels; 
}
public GeneralProposal[] setKernels( GeneralProposal[] kernels ) 
{ 
    return this.kernels = kernels; 
}


public PRNG getPRNG()            { return this.prng; }
public PRNG setPRNG( PRNG prng ) { return this.prng = prng; }



// line 24 "AdaptiveMultiKernelProposal.jweb"
    // implementing Hastings Coupled Proposal //
    
// line 67 "AdaptiveMultiKernelProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector)

{
    //    DEBUG = true;

    if (DEBUG) System.err.println("DEBUG in AdaptiveMultiKernelProposal.conditionalPDF");
    if (DEBUG) System.err.println("StateVector=" + stateVector);
    if (DEBUG) System.err.println("State=" +       state);
    if (DEBUG) System.err.println("Which=" +       which);

    adapt(stateVector, which);

    double retval = 0.0;
    double tmpval = 0.0;
    for(int i=0; i < stateVector.size(); i++)
    {
	tmpval += kernels[i].conditionalPDF( state, stateVector.get(i) );

	if (DEBUG) System.err.println("state=" +       ArrayTools.arrayToString(ArrayTools.Otod(state)) );
	if (DEBUG) System.err.println("condition=" +   ArrayTools.arrayToString(ArrayTools.Otod(stateVector.get(i))));
	if (DEBUG) System.err.println("tmpval=" +       tmpval);

	retval += tmpval;

    }
    retval = retval / (double) stateVector.size();

    if (DEBUG) System.err.println("Retval = " + retval );
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
    adapt(stateVector, which);

    return Math.log(conditionalPDF(state,conditions,which,stateVector) );
}

// line 26 "AdaptiveMultiKernelProposal.jweb"
    
// line 121 "AdaptiveMultiKernelProposal.jweb"
/** Generate a new component state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public Object   generate( Object conditionals, int which, MultiState stateVector  )
{
    MultiState newStateVector = new MultiState();
    for(int i=0; i < stateVector.size(); i++)
    {
	newStateVector.add( stateVector.get(i) );
    }

    adapt(stateVector, which);

    if (DEBUG) System.err.println("StateVector = " + stateVector  );
    int from = (int) ( ((double) stateVector.size()) * prng.nextDouble() );  // uniform on [0, size-]1
    int to   = (int) ( ((double) stateVector.size()) * prng.nextDouble() );  // uniform on [0, size-1]
    if (DEBUG) System.err.println("From = " + from );

    Object newState;
    newState =  kernels[from].generate( stateVector.get(from) ) ;


    newStateVector.set(which, newState );

    if (DEBUG) System.err.println("New state = " + newState );
    if (DEBUG) System.err.println("New StateVector = " + newStateVector );

    //return newStateVector;
    return newState;
}


// line 27 "AdaptiveMultiKernelProposal.jweb"
    
// line 160 "AdaptiveMultiKernelProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double transitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    adapt(stateVector, which);

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
    adapt(stateVector, which);

    return logConditionalPDF( to, from, which, stateVector );
}



// line 28 "AdaptiveMultiKernelProposal.jweb"
     
    
// line 192 "AdaptiveMultiKernelProposal.jweb"
/** modify the state of the enclosed proposal distribution using information from the provided state vector **/
abstract public void adapt( MultiState mstate, int which );

// line 30 "AdaptiveMultiKernelProposal.jweb"
}

