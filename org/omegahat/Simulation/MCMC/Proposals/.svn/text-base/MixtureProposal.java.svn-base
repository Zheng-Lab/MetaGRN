

// line 10 "MixtureProposal.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/MixtureProposal.java,v 1.1.1.1 2001/04/04 17:16:24 warneg Exp $  */


// line 293 "MixtureProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 299 "MixtureProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 20 "MixtureProposal.jweb"
    public class MixtureProposal implements GeneralProposal, 
                                            TimeDependentProposal
{
  
// line 39 "MixtureProposal.jweb"
protected GeneralProposal[] proposals;
protected double[]          proposal_probs;
protected double[]          normalized_probs;
protected double[]          cumulative_probs;
protected PRNG              prng;

// line 24 "MixtureProposal.jweb"
  
// line 48 "MixtureProposal.jweb"
// read-only
public    int numChains() { return proposals.length; }

public void setProposal( int which, GeneralProposal what, double prob )
{
  proposals[which] = what; 
  proposal_probs[which] = prob;

  normalize();

}

public void setProposals( GeneralProposal[] what, double[] probs )
{
  if(what.length != probs.length) 
    throw new RuntimeException("The length of the mixture proportions vector must " + 
                               " match the number of proposals.");

  proposals = what;
  proposal_probs = probs;
  
  normalize();
}



public GeneralProposal[] getProposals()
{
    return proposals;
}

public double[] getProposalProbs()
{
    return proposal_probs;
}


// line 26 "MixtureProposal.jweb"
  
// line 88 "MixtureProposal.jweb"
protected void normalize()
{

  double probs_sum = 0.0;
  for(int i = 0; i < proposal_probs.length; i++)
  {
    if(proposal_probs[i] < 0.0 ) throw new RuntimeException( "Relative probabilities must be non-negative.");
    probs_sum += proposal_probs[i];
  }

  if(probs_sum <= 0.0 ) throw new RuntimeException( "Relative probabilities must sum to non-zero value.");

  double cumsum = 0.0;

  for(int i=0; i < proposal_probs.length; i++)
  {
    normalized_probs[i] = proposal_probs[i] / probs_sum;
    cumsum += normalized_probs[i];
    cumulative_probs[i] = cumsum;
  }

}


// line 27 "MixtureProposal.jweb"
  
// line 117 "MixtureProposal.jweb"
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

// line 28 "MixtureProposal.jweb"
  
// line 138 "MixtureProposal.jweb"
public double conditionalPDF   ( Object state, Object conditions)
{
  double retval = 0.0;
  double mult;
  double inner;

  int numProposals = proposals.length;

  retval = 0.0;

  for(int i=0; i<numProposals; i++)
  {
    retval += normalized_probs[i] * proposals[i].conditionalPDF( state,
                                                  conditions ); 
  }
  
  return retval;
}

public double logConditionalPDF( Object state, Object conditions )
{    
  return Math.log(conditionalPDF(state, conditions)); 
}




// line 29 "MixtureProposal.jweb"
  
// line 169 "MixtureProposal.jweb"
// generate a single random value 
public Object generate( Object conditionals )
{ 

  double rand = prng.nextDouble();

  for(int i=0; i<proposals.length; i++) 
    {
      if(rand < cumulative_probs[i])
        return proposals[i].generate( conditionals );
    }

  // If we get this far, something has gone wrong and we've not been able to select 
  // one of the component distributions.
  
  throw new RuntimeException("Internal Error." + 
                             "Unable to randomize between proposal distributions. " + 
                             "Check cumulative_probs[].");

} 


// line 30 "MixtureProposal.jweb"
  
// line 195 "MixtureProposal.jweb"
/** Increment time **/
public void timeInc()
{
  for(int i=0; i<proposals.length; i++) 
  {
    if(proposals[i] instanceof TimeDependentProposal)
      {
	((TimeDependentProposal) proposals[i]).timeInc();
      }
  }

}

/** Reset time to 0 **/
public void resetTime()
{
  for(int i=0; i<proposals.length; i++) 
  {
    if(proposals[i] instanceof TimeDependentProposal)
      {
	((TimeDependentProposal) proposals[i]).resetTime();
      }
  }

}

/** Get the time **/
public int getTime()
{
  // all TimeDependentProposal components should have the same time, so
  // return time remembered by first 
  for(int i=0; i<proposals.length; i++) 
  {
    if(proposals[i] instanceof TimeDependentProposal)
      {
	return ((TimeDependentProposal) proposals[i]).getTime();
      }
  }

  return 0;
}

// line 32 "MixtureProposal.jweb"
  
// line 253 "MixtureProposal.jweb"
public MixtureProposal( int nProposal, PRNG prng ) 
{
     proposals = new GeneralProposal[nProposal]; 
     proposal_probs = new double[nProposal];
     normalized_probs = new double[nProposal];
     cumulative_probs = new double[nProposal];
     this.prng = prng;
}

public MixtureProposal( GeneralProposal[] proposalList, 
                        double[] proposal_probs,
                        PRNG prng ) 
{ 
    proposals = proposalList;
    this.proposal_probs = proposal_probs;
    normalized_probs = new double[proposal_probs.length];
    cumulative_probs = new double[proposal_probs.length];
    normalize();
    this.prng = prng;
} 

public MixtureProposal( GeneralProposal[] proposalList, 
                        PRNG prng ) 
{ 
    double[] probList = new double[proposalList.length];
    for(int i=0; i<probList.length; i++)
	probList[i] = 1.0 / (double) probList.length;

    proposals = proposalList;
    this.proposal_probs = probList;
    normalized_probs = new double[proposal_probs.length];
    cumulative_probs = new double[proposal_probs.length];
    normalize();
    this.prng = prng;
} 

// line 33 "MixtureProposal.jweb"
}

