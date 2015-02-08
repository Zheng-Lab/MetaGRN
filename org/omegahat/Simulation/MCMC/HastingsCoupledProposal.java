

// line 9 "HastingsCoupledProposal.jweb"
 /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/HastingsCoupledProposal.java,v 1.1.1.1 2001/04/04 17:16:13 warneg Exp $  */

 /* (c) 2000 Gregory R. Warnes */ 
 /* May also contain code (c) 1999 The Omegahat Project */






// line 101 "HastingsCoupledProposal.jweb"
    package org.omegahat.Simulation.MCMC;


// line 107 "HastingsCoupledProposal.jweb"
    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import org.omegahat.Probability.Distributions.*;


// line 26 "HastingsCoupledProposal.jweb"
/** This class extends the <code>GeneralProposal</code> interface by
 * adding methods appropriate for generating states for a
 * <code>HastingsCoupledSampler</code>.  This interface replaces to
 * <code>AdaptiveProposal</code>, which is now depreciated.
 *
 **/
public interface HastingsCoupledProposal // extends MarkovProposal
{
    
// line 44 "HastingsCoupledProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector); 

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param state        Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logConditionalPDF( Object state, Object conditions, int which, MultiState stateVector );   


// line 35 "HastingsCoupledProposal.jweb"
    
// line 65 "HastingsCoupledProposal.jweb"
/** Generate a new component state given the entire state vector 
 *  @param conditions   Current value of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public Object   generate( Object conditions, int which, MultiState stateVector  );


// line 36 "HastingsCoupledProposal.jweb"
    
// line 78 "HastingsCoupledProposal.jweb"
/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double transitionProbability( Object from, Object to, int which, MultiState stateVector );

/** Computes the HastingsCoupled transition probability for one sub-state given the entire state vector 
 *  @param from         Current value of the component state being updated
 *  @param to           Proposed state of the component state being updated
 *  @param which        index of the component state being updated
 *  @param stateVector  entire set of states, including the state being updated
 **/
public double logTransitionProbability( Object from, Object to, int which, MultiState stateVector );



// line 37 "HastingsCoupledProposal.jweb"
        
}

