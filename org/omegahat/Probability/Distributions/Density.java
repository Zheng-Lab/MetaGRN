// line 10 "Density.jweb"
  

// line 4 "Density.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Probability/Distributions/Density.java,v 1.1.1.1 2001/04/04 17:16:09 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 28 "Density.jweb"
package org.omegahat.Probability.Distributions;


// line 34 "Density.jweb"
import java.util.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.Simulation.MCMC.*;


// line 15 "Density.jweb"
public interface Density // extends UnnormalizedDensity
{
    //    public double unnormalizedPDF   ( Object state ){ return PDF( state )    };
    //    public double logUnnormalizedPDF( Object state ){ return logPDF( state ) };
    public double PDF   ( Object state );    // { return Math.exp(logPDF(state)); }
    public double logPDF( Object state );    
}

