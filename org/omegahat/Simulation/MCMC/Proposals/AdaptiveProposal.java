

// line 148 "AdaptiveProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 154 "AdaptiveProposal.jweb"
  import org.omegahat.Simulation.MCMC.*;
  import org.omegahat.Probability.Distributions.*;

  import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 10 "AdaptiveProposal.jweb"
/** This class implements a generic ``adaptive'' Hastings-Coupled
 * proposal.  It provodes methods that simply call the <code>adapt</code>
 * method, and then refer the call to the encapsulated
 * <code>GeneralProposal</code>.
 **/

abstract public class AdaptiveProposal implements HastingsCoupledProposal
{

    
// line 28 "AdaptiveProposal.jweb"
protected GeneralProposal proposal;

protected PRNG prng;

// line 20 "AdaptiveProposal.jweb"
    
// line 36 "AdaptiveProposal.jweb"
/* read-only */
public GeneralProposal getProposal() { return this.proposal;}

// line 21 "AdaptiveProposal.jweb"
    
// line 52 "AdaptiveProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector)
{
    try 
      {
        adapt(stateVector);
        return proposal.conditionalPDF(state,conditions);
      }
    catch ( Throwable e )
      {
        return 0.0;
      }
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logConditionalPDF( Object state, Object conditions, int which, MultiState stateVector )
{
    try 
      {
          adapt(stateVector);
          return proposal.logConditionalPDF(state,conditions);
      }
    catch ( Throwable e )
      {
        return 0.0;
      }
}


/** Generate a new component state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public Object generate( Object conditions, int which, MultiState stateVector  )
{
    adapt(stateVector);
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
    try 
      {
        adapt(stateVector);
        return proposal.transitionProbability( from, to);
      }
    catch ( Throwable e )
      {
        return 0.0;
      }
}

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logTransitionProbability( Object from, Object to, int which, MultiState stateVector )
{
    try
      {
        adapt(stateVector);
        return proposal.logTransitionProbability( from, to);
      }
    catch ( Throwable e )
      {
        return 0.0;
      }
}


// line 22 "AdaptiveProposal.jweb"
    
// line 44 "AdaptiveProposal.jweb"
/** modify the state of the enclosed proposal distribution using information from
    the provided state vector **/
abstract public void adapt( MultiState mstate );

// line 23 "AdaptiveProposal.jweb"
}
