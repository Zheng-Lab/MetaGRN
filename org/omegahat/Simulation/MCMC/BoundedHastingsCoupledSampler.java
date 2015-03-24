

// line 8 "BoundedHastingsCoupledSampler.jweb"
 /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/BoundedHastingsCoupledSampler.java,v 1.1.1.1 2001/04/04 17:16:17 warneg Exp $ */

 /* (c) 2000 Gregory R. Warnes */ 
 /* May also contain code (c) 1999 The Omegahat Project */




// line 184 "BoundedHastingsCoupledSampler.jweb"
    package org.omegahat.Simulation.MCMC;



// line 191 "BoundedHastingsCoupledSampler.jweb"
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.GUtilities.ArrayTools;



// line 23 "BoundedHastingsCoupledSampler.jweb"
/**  
 * This extends <code>CustomHastingsCoupledSampler</code> by allowing boundaries to be set for each 
 * dimension.  If a proposal generates a point outside the boundaries, the proposal is rejected.
**/
public class BoundedHastingsCoupledSampler extends CustomHastingsCoupledSampler
{

    
// line 41 "BoundedHastingsCoupledSampler.jweb"
protected double[] minVals;
protected double[] maxVals;

// line 31 "BoundedHastingsCoupledSampler.jweb"
    
// line 49 "BoundedHastingsCoupledSampler.jweb"
public double[] getMinVals() { return this.minVals; }
public double[] setMinVals( double[] mins ) { return this.minVals = mins; }


public double[] getMaxVals() { return this.maxVals; }
public double[] setMaxVals( double[] maxs ) { return this.maxVals = maxs; }


// line 32 "BoundedHastingsCoupledSampler.jweb"
    
// line 148 "BoundedHastingsCoupledSampler.jweb"
public BoundedHastingsCoupledSampler( MultiState                  state, 
                                      int                         numSamplers,
                                      UnnormalizedDensity         target, 
                                      HastingsCoupledProposal     proposal, 
                                      PRNG                        prng,
                                      double[]                    minVals,
                                      double[]                    maxVals,
                                      boolean                     detailed)
{
  super( state, numSamplers, target, proposal, prng, detailed );
  this.minVals = minVals;
  this.maxVals = maxVals;
}

public BoundedHastingsCoupledSampler( MultiState               state, 
				      int                      numSamplers,
				      UnnormalizedDensity      target, 
				      HastingsCoupledProposal  proposal, 
				      double[]                 minVals,
				      double[]                 maxVals,
				      PRNG                     prng)
{
    this(state, numSamplers, target, proposal, prng, minVals, maxVals, false);
}


// line 33 "BoundedHastingsCoupledSampler.jweb"
    
// line 63 "BoundedHastingsCoupledSampler.jweb"
/* inherited */   


// line 34 "BoundedHastingsCoupledSampler.jweb"
    
// line 140 "BoundedHastingsCoupledSampler.jweb"
/* inherited */

// line 35 "BoundedHastingsCoupledSampler.jweb"
    


// line 72 "BoundedHastingsCoupledSampler.jweb"
protected double acceptanceProb( Object current,  MultiState currentStateVector,
                                    Object proposed, MultiState proposedStateVector,
                                    int which
                                 )
{
    double[] proposedArr = ArrayTools.Otod( proposed );
    
     for(int i=0; i < proposedArr.length; i++)
       if( (proposedArr[i] < minVals[i]) || (proposedArr[i] > maxVals[i]) || Double.isNaN(proposedArr[i] ) )
	   {
	       if (DEBUG) System.err.println("Boundary Exceeded, acceptance prob set to 0.0.");
	       if (DEBUG) System.err.println("Proposed component " +  i + " is " + proposedArr[i] );
	       if (DEBUG) System.err.println("Boundary values are " +  minVals[i] + " to " + maxVals[i] );

	       
	       logAcceptProb = Double.NEGATIVE_INFINITY;
	       uniformRand = Double.NaN;
	       accepted = false;
	       log_p_X = Double.NaN;
	       log_q_X_to_Y = Double.NaN;
	       log_p_Y = Double.NaN;
	       log_q_Y_to_X = Double.NaN;


	      return 0.0;
	   }

    return super.acceptanceProb( current, currentStateVector,  
				 proposed, proposedStateVector, which );
}


protected double logAcceptanceProb( Object current,  MultiState currentStateVector,
                                    Object proposed, MultiState proposedStateVector,
                                    int which
                                 )
{
    double[] proposedArr = ArrayTools.Otod( proposed );
    
     for(int i=0; i < proposedArr.length; i++)
       if( (proposedArr[i] < minVals[i]) || (proposedArr[i] > maxVals[i]) || Double.isNaN(proposedArr[i] ) ) 
	   {
	       if (DEBUG) System.err.println("Boundary Exceeded, acceptance prob set to 0.0.");
	       if (DEBUG) System.err.println("Proposed component " +  i + " is " + proposedArr[i] );
	       if ( DEBUG) System.err.println("Boundary values are " +  minVals[i] + " to " + maxVals[i] );

	       logAcceptProb = Double.NEGATIVE_INFINITY;
	       uniformRand = Double.NaN;
	       accepted = false;
	       log_p_X = Double.NaN;
	       log_q_X_to_Y = Double.NaN;
	       log_p_Y = Double.NaN;
	       log_q_Y_to_X = Double.NaN;



	       return Double.NEGATIVE_INFINITY;
	   }

    return super.logAcceptanceProb( current, currentStateVector,  
				    proposed, proposedStateVector, which );
}


// line 36 "BoundedHastingsCoupledSampler.jweb"
     }
