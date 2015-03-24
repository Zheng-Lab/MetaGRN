

// line 158 "IndependentHastingsCoupledProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 164 "IndependentHastingsCoupledProposal.jweb"
  import org.omegahat.Simulation.MCMC.*;
  import org.omegahat.Probability.Distributions.*;


// line 10 "IndependentHastingsCoupledProposal.jweb"
/**
 * This class implements an Hastings-Coupled proposal that generates
 * states independently using a common <code>GeneralProposal</code>.  It simply
 * refers appropriate method calls to the encapsulated
 * <code>GeneralProposal</code>.
 **/
public class IndependentHastingsCoupledProposal implements HastingsCoupledProposal, TimeDependentProposal
{

    
// line 29 "IndependentHastingsCoupledProposal.jweb"
protected GeneralProposal proposal;

protected int time       = 0;
protected int timeCalls  = 0;
protected int dim        = 1;

// line 20 "IndependentHastingsCoupledProposal.jweb"
    
// line 76 "IndependentHastingsCoupledProposal.jweb"
/* read-only */
public GeneralProposal getProposal() { return this.proposal;}

// line 21 "IndependentHastingsCoupledProposal.jweb"
    
// line 40 "IndependentHastingsCoupledProposal.jweb"
public void timeInc()
{
  time++;

  if( proposal instanceof TimeDependentProposal )
     {
	// time for the encapsulated proposal should only increment
	// once it has iterated through all of the $C$ components.
	// this is implemented here by eating $C-1$ of every $C$
        //  timeInc() calls.

	timeCalls++;
	if( timeCalls >= dim )
        {
	   ((TimeDependentProposal) proposal).timeInc();
	   timeCalls = 0;
        }
     }
}

public void resetTime()
{
    time = -1;
    timeInc();
}

public int getTime()
{
   return time;
}

// line 22 "IndependentHastingsCoupledProposal.jweb"
    
// line 94 "IndependentHastingsCoupledProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector)
{
    return proposal.conditionalPDF(state,conditions);
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logConditionalPDF( Object state, Object conditions, int which, MultiState stateVector )
{
    return proposal.logConditionalPDF(state,conditions);
}


/** Generate a new component state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public Object generate( Object conditions, int which, MultiState stateVector  )
{
    dim = stateVector.size();
    return proposal.generate(conditions);
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double transitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    return proposal.transitionProbability( from, to);
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logTransitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    return proposal.logTransitionProbability( from, to);
}


// line 23 "IndependentHastingsCoupledProposal.jweb"
    
// line 84 "IndependentHastingsCoupledProposal.jweb"
public IndependentHastingsCoupledProposal( GeneralProposal proposal )
{
    this.proposal = proposal;
}

// line 24 "IndependentHastingsCoupledProposal.jweb"
}
