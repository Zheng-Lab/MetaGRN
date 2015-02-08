

// line 6 "MultiProposal.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/MultiProposal.java,v 1.1.1.1 2001/04/04 17:16:24 warneg Exp $  */


// line 153 "MultiProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 159 "MultiProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;


// line 16 "MultiProposal.jweb"
abstract public class MultiProposal implements GeneralProposal
{
  
// line 32 "MultiProposal.jweb"
protected GeneralProposal[] proposals;

// line 19 "MultiProposal.jweb"
  
// line 37 "MultiProposal.jweb"
// read-only
public    int numChains() { return proposals.length; }

public void setProposal( int which, GeneralProposal what )
{
  proposals[which] = what;
}

public void setProposals( GeneralProposal[] what )
{
  proposals = what;
}



// line 21 "MultiProposal.jweb"
  
// line 57 "MultiProposal.jweb"
/** 
 * Convenience method for computing the probability of proposing a move.  
 */
public double transitionProbability( Object from, Object to )
 {
   return conditionalPDF( to, from );
 }

 /** 
 * Convenience method for computing the log probability of proposing a move.  
 */

public double logTransitionProbability( Object from, Object to )
{
   return logConditionalPDF( to, from );
}

// line 22 "MultiProposal.jweb"
  
// line 78 "MultiProposal.jweb"
public double conditionalPDF   ( Object state, Object conditions)
{
  return Math.exp(logConditionalPDF(state, conditions)); 
}

public double unnormalizedConditionalPDF   ( Object state, Object conditions)
{
  return conditionalPDF(state, conditions); 
}

public double logUnnormalizedConditionalPDF   ( Object state, Object conditions)
{
  return logConditionalPDF(state, conditions); 
}

abstract public double logConditionalPDF( Object state, Object conditions );
/* { */
/*   MultiState states = (MultiState) state; */
/*   MultiState conds  = (MultiState) conditions; */
  
/*   double retval =0.0; */
  
/*   for(int i=0; i<proposals.length; i++) */
/*     retval += proposals[i].logConditionalPDF( states.get(i), */
/*                                               conds.get(i) ); */
  
/*   return retval; */
/* } */

// line 23 "MultiProposal.jweb"
  
// line 111 "MultiProposal.jweb"
// generate a single random value 
abstract public Object   generate( Object conditionals );
/* { */
/*    MultiState states = (MultiState) conditionals; */

/*    MultiState retval = new MultiState( proposals.length ); */

 
/*   for(int i=0; i<proposals.length; i++) */
/*    retval.add( proposals[i].generate( states.get(i) )); */
  
/*   return retval; */
/* } */



// line 25 "MultiProposal.jweb"
  
// line 132 "MultiProposal.jweb"
/* public MultiProposal( int nchain ) */
/* { */
/*     proposals = new GeneralProposal[nchain]; */
/* } */

/* public MultiProposal( int nchain, GeneralProposal singleProposal ) */
/* { */
/*     proposals = new GeneralProposal[nchain]; */
/*     for(int i=0; i < proposals.length; i++) */
/*         proposals[i] = singleProposal; */
/* } */

/* public MultiProposal( GeneralProposal[] proposalList ) */
/* { */
/*     proposals = proposalList; */
/* } */

// line 26 "MultiProposal.jweb"
}

