

// line 10 "IteratedProposal.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/IteratedProposal.java,v 1.1.1.1 2001/04/04 17:16:23 warneg Exp $  */


// line 216 "IteratedProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 222 "IteratedProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 20 "IteratedProposal.jweb"
public class IteratedProposal implements GeneralProposal, TimeDependentProposal
{
  
// line 37 "IteratedProposal.jweb"
protected GeneralProposal[] proposals;
protected int[]             proposalFreqs;
protected int               currentProposal = 0;
protected int               currentCount = 0;
protected int               time = 0;
public    boolean           debug = false; 

// line 23 "IteratedProposal.jweb"
  
// line 47 "IteratedProposal.jweb"
// read-only
public  int numChains() { return proposals.length; }

public void setProposal( int which, GeneralProposal what, int freq )
{
  proposals[which] = what; 
  proposalFreqs[which] = freq;
}

public void setProposals( GeneralProposal[] what, int[] freqs )
{
  if(what.length != freqs.length) 
    throw new RuntimeException("The length of the mixture proportions vector must " + 
			       " match the number of proposals.");

  proposals = what;
  proposalFreqs = freqs;
}



// line 25 "IteratedProposal.jweb"
  
// line 117 "IteratedProposal.jweb"
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

// line 26 "IteratedProposal.jweb"
  
// line 138 "IteratedProposal.jweb"
public double conditionalPDF   ( Object state, Object conditions)
{
  return Math.exp(logConditionalPDF(state, conditions)); 
}

//public double unnormalizedConditionalPDF   ( Object state, Object conditions)
//{
//  return conditionalPDF(state, conditions); 
//}

//public double logUnnormalizedConditionalPDF   ( Object state, Object conditions)
//{
//  return logConditionalPDF(state, conditions); 
//}

public double logConditionalPDF( Object state, Object conditions )
{  
  return proposals[currentProposal].conditionalPDF( state, conditions ); 
}
// line 27 "IteratedProposal.jweb"
  
// line 161 "IteratedProposal.jweb"
// generate a single random value 
public Object generate( Object conditionals )
{ 
    return proposals[currentProposal].generate( conditionals );
} 


// line 28 "IteratedProposal.jweb"
  
// line 71 "IteratedProposal.jweb"
public void timeInc()
{
  int seenStart = 0; // how many times have we looped past our starting location?

  time++;
  currentCount++;
  if(currentCount >= proposalFreqs[currentProposal])
  {
    currentCount = 0;

    // set currentProposal to the next proposal with nonzero frequency
    currentProposal = (currentProposal+1) % proposals.length;
    while ( proposalFreqs[currentProposal] < 1 )
      {
	currentProposal++;
	if(currentProposal >= proposals.length) 
	  {
	    currentProposal = 0;
	    seenStart++;
	    if(seenStart > 1) 
              throw new RuntimeException("Infinite loop detected. " + 
                                         "Probably due to all proposalFreqs being zero.");
	  }
      }
  }

  if(debug) System.err.println("Incrementing time to " + time + " setting proposal to " + currentProposal + ".");
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



// line 30 "IteratedProposal.jweb"
  
// line 174 "IteratedProposal.jweb"
protected IteratedProposal( int nProposal )
{
     proposals = new GeneralProposal[nProposal]; 
     proposalFreqs = new int[nProposal];
     currentProposal = 0;
     currentCount = 0;
     time = 0;
}

public IteratedProposal( GeneralProposal[] proposalList, 
			 int[]             proposalFreqs ) 
{ 
    if(proposalList.length != proposalFreqs.length) 
	throw new RuntimeException("proposalList and proposalFreqs must have the same length.");

    proposals = proposalList;
    this.proposalFreqs = proposalFreqs;
    currentProposal = 0;
    currentCount = 0;
    time = 0;
    
    int cumsum = 0;
    for(int i=0; i<proposalFreqs.length; i++)
      {
	if (proposalFreqs[i] < 0) 
	  throw new RuntimeException("proposalFreqs must be non negative.");
	cumsum += proposalFreqs[i];
      }

    if( cumsum <= 0 )
      throw new RuntimeException("at least one proposalFreqs must be nonzero.");

    // set currentProposal to the first proposal with nonzero frequency
    while(proposalFreqs[currentProposal] < 1)
	currentProposal++;

} 

// line 31 "IteratedProposal.jweb"
}

