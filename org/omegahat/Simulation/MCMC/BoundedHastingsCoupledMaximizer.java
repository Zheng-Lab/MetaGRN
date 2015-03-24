

// line 9 "BoundedHastingsCoupledMaximizer.jweb"
 /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/BoundedHastingsCoupledMaximizer.java,v 1.1.1.1 2001/04/04 17:16:18 warneg Exp $ */

 /* (c) 2000 Gregory R. Warnes */ 
 /* May also contain code (c) 1999 The Omegahat Project */




// line 148 "BoundedHastingsCoupledMaximizer.jweb"
    package org.omegahat.Simulation.MCMC;



// line 155 "BoundedHastingsCoupledMaximizer.jweb"
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.GUtilities.ArrayTools;



// line 24 "BoundedHastingsCoupledMaximizer.jweb"
/**  
 * This extends <code>CustomHastingsCoupledSampler</code> by allowing boundaries to be set for each 
 * dimension.  If a proposal generates a point outside the boundaries, the proposal is rejected.
**/
public class BoundedHastingsCoupledMaximizer extends BoundedHastingsCoupledSampler
{

    
// line 42 "BoundedHastingsCoupledMaximizer.jweb"
    /* inherited */

// line 32 "BoundedHastingsCoupledMaximizer.jweb"
    
// line 49 "BoundedHastingsCoupledMaximizer.jweb"
    /* inherited */

// line 33 "BoundedHastingsCoupledMaximizer.jweb"
    
// line 114 "BoundedHastingsCoupledMaximizer.jweb"
public BoundedHastingsCoupledMaximizer( MultiState                  state, 
                                      int                         numSamplers,
                                      UnnormalizedDensity         target, 
                                      HastingsCoupledProposal     proposal, 
                                      PRNG                        prng,
                                      double[]                    minVals,
                                      double[]                    maxVals,
                                      boolean                     detailed)
{
  super( state, numSamplers, target, proposal, prng, minVals, maxVals, detailed );
}

public BoundedHastingsCoupledMaximizer( MultiState               state, 
				      int                      numSamplers,
				      UnnormalizedDensity      target, 
				      HastingsCoupledProposal  proposal, 
				      double[]                 minVals,
				      double[]                 maxVals,
				      PRNG                     prng)
{
  this(state, numSamplers, target, proposal, prng, minVals, maxVals, false);
}


// line 34 "BoundedHastingsCoupledMaximizer.jweb"
    
// line 57 "BoundedHastingsCoupledMaximizer.jweb"
/* inherited */   


// line 35 "BoundedHastingsCoupledMaximizer.jweb"
    
// line 106 "BoundedHastingsCoupledMaximizer.jweb"
/* inherited */

// line 36 "BoundedHastingsCoupledMaximizer.jweb"
    
// line 64 "BoundedHastingsCoupledMaximizer.jweb"
protected double acceptanceProb( Object current,  MultiState currentStateVector,
                                    Object proposed, MultiState proposedStateVector,
                                    int which
                                 )
{
    return Math.exp( logAcceptanceProb( current,  currentStateVector,
					proposed, proposedStateVector,
					which ) );
}

protected double logAcceptanceProb( Object current,  MultiState currentStateVector,
                                    Object proposed, MultiState proposedStateVector,
                                    int which
                                 )
{
    double[] proposedArr = ArrayTools.Otod( proposed );

    for(int i=0; i < proposedArr.length; i++)
      if( (proposedArr[i] < minVals[i]) || (proposedArr[i] > maxVals[i]) ) return Double.NEGATIVE_INFINITY;


    log_p_X = target.logUnnormalizedPDF( current  );
    log_p_Y = target.logUnnormalizedPDF( proposed ) ;

    if (DEBUG) System.err.println("log(P(X)) = " + log_p_X + "   log(P(Y)) = " + log_p_Y  ); 

    if(log_p_Y > log_p_X) 
	return 0.0; 
    else
	return Double.NEGATIVE_INFINITY;


}





// line 37 "BoundedHastingsCoupledMaximizer.jweb"
     }
