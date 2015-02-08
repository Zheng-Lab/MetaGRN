

// line 59 "TimeDependentProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 65 "TimeDependentProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    /* none */


// line 9 "TimeDependentProposal.jweb"
public interface TimeDependentProposal
{
    
// line 21 "TimeDependentProposal.jweb"
    /* none */

// line 12 "TimeDependentProposal.jweb"
    
// line 26 "TimeDependentProposal.jweb"
    
    /* none */

// line 13 "TimeDependentProposal.jweb"
    
// line 34 "TimeDependentProposal.jweb"
    /* none */

// line 14 "TimeDependentProposal.jweb"
    
// line 40 "TimeDependentProposal.jweb"
/** Increment time **/
public void timeInc();

/** Reset time to 0 **/
public void resetTime();

/** Get the time **/
public int getTime();

// line 15 "TimeDependentProposal.jweb"
    
// line 53 "TimeDependentProposal.jweb"
    /* none */

// line 16 "TimeDependentProposal.jweb"
}
