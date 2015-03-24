
// line 22 "Binomial_BetaBinomial_Likelihood_Bimode.jweb"
/**
 * This class computes a Binomial-BetaBinomial likelihood for the Barretts LOH data
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2002/11/02 23:10:21 $
 * @version $Revision: 1.3 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import java.lang.Math;
import com.imsl.math.Sfun;
import org.omegahat.GUtilities.ArrayTools;
import org.omegahat.Probability.Distributions.UnnormalizedDensity;


public class Binomial_BetaBinomial_Likelihood_Bimode extends Binomial_BetaBinomial_Likelihood
{

    // Constructor
    public Binomial_BetaBinomial_Likelihood_Bimode() { super(); }


    // Methods Implementing Unnormalized Density

    public double logUnnormalizedPDF( Object paramOb )
    {
        return Math.log( unnormalizedPDF( paramOb ) ) ;
    }


    public double unnormalizedPDF( Object paramObj )
    {
        double[] params = ArrayTools.Otod( paramObj );
        if(params.length != 4 ) throw new RuntimeException("Wrong number of parameters!");
        
        double eta    = params[0];
        double pi0    = params[1];
        double pi1    = params[2];
        double gamma  = params[3];
        
        // 
        double omega1 = 0.5 * Math.exp(gamma) / (1.0 + Math.exp(gamma) );

        // check range 
        if( (eta < 0.0) || (pi0 < 0.0) || (pi1 < 0.0) || (omega1 < 0.0) ||
            (eta > 1.0) || (pi0 > 1.0) || (pi1 > 1.0) || (omega1 > 0.5) )
            return 0.0;
        else 
            {

                // uniform prior on gamma over [-30,30]
                if( gamma < -30 || gamma > 30 ) return 0.0;
                double prior = 1.0;
                
                // induced logit prior
                // double prior = Math.exp(gamma) / Math.pow( 1 + Math.exp(gamma) , 2 );

                double tmp = unnormalized_density_loh( eta, pi0, pi1, omega1 );
                return tmp * prior;
            }
                       
    }

}
