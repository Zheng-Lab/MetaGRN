

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Probability/Distributions/CumulativeConditionalDensity.java,v 1.1.1.1 2001/04/04 17:16:09 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


package org.omegahat.Probability.Distributions;


import java.util.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.Simulation.MCMC.*;


public interface CumulativeConditionalDensity 
{
    //    public double unnormalizedConditionalDensity( Object state, Object conditions ) 
    //    { 
    //      return density( state, conditions ) 
    //    };
    //
    //    public double logUnnormalizedConditionalDensity( Object state, Object conditions )
    //    { 
    //      return logConditionalDensity( state, conditions ) 
    //    };

    public double cumulativeConditionalDensity( Object state, Object conditions );  
    // { 
    //   return Math.exp( logCumulativeConditionalDensity(state), conditions ); 
    // }   

    public double logCumulativeConditionalDensity( Object state, Object conditions );
}

