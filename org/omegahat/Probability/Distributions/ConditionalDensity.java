

// line 4 "ConditionalDensity.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Probability/Distributions/ConditionalDensity.java,v 1.1.1.1 2001/04/04 17:16:09 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 30 "ConditionalDensity.jweb"
package org.omegahat.Probability.Distributions;


// line 36 "ConditionalDensity.jweb"
import java.util.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.Simulation.MCMC.*;


// line 16 "ConditionalDensity.jweb"
public interface ConditionalDensity 
{
    //    public double unnormalizedConditionalDensity( Object state ) { return density( state, conditions ) };
    //    public double logUnnormalizedConditionalDensity( Object state ){ return logConditionalDensity( state, conditions ) };
    public double conditionalPDF   ( Object stat, Object conditions); // { return Math.exp(logConditioanlPDF(state, conditions); }
    public double logConditionalPDF( Object state, Object conditions );   
}

