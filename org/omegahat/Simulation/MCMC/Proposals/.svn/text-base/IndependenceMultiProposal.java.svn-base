

// line 6 "IndependenceMultiProposal.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/IndependenceMultiProposal.java,v 1.1.1.1 2001/04/04 17:16:24 warneg Exp $  */


// line 113 "IndependenceMultiProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 119 "IndependenceMultiProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;


// line 16 "IndependenceMultiProposal.jweb"
public class IndependenceMultiProposal extends MultiProposal
{
  
// line 32 "IndependenceMultiProposal.jweb"
/* inherited */

// line 19 "IndependenceMultiProposal.jweb"
  
// line 38 "IndependenceMultiProposal.jweb"
/* inheritied */


// line 21 "IndependenceMultiProposal.jweb"
  
// line 46 "IndependenceMultiProposal.jweb"
/* inherited */

// line 22 "IndependenceMultiProposal.jweb"
  

// line 53 "IndependenceMultiProposal.jweb"
public double logConditionalPDF( Object state, Object conditions )
{
  MultiState states = (MultiState) state;
  MultiState conds  = (MultiState) conditions;
  
  double retval =0.0;
  
  for(int i=0; i<proposals.length; i++)
    retval += proposals[i].logConditionalPDF( states.get(i),
                                              conds.get(i) );
  
  return retval;
}

// line 23 "IndependenceMultiProposal.jweb"
  
// line 71 "IndependenceMultiProposal.jweb"
// generate a single random value 
public Object   generate( Object conditionals )
{
   MultiState states = (MultiState) conditionals;

   MultiState retval = new MultiState( proposals.length );

 
  for(int i=0; i<proposals.length; i++)
   retval.add( proposals[i].generate( states.get(i) ));
  
  return retval;
}



// line 25 "IndependenceMultiProposal.jweb"
  
// line 92 "IndependenceMultiProposal.jweb"
public IndependenceMultiProposal( int nchain )
{
    proposals = new GeneralProposal[nchain];
}

public IndependenceMultiProposal( int nchain, GeneralProposal singleProposal )
{
    proposals = new GeneralProposal[nchain];
    for(int i=0; i < proposals.length; i++)
        proposals[i] = singleProposal;
}

public IndependenceMultiProposal( GeneralProposal[] proposalList )
{
    proposals = proposalList;
}

// line 26 "IndependenceMultiProposal.jweb"
}

