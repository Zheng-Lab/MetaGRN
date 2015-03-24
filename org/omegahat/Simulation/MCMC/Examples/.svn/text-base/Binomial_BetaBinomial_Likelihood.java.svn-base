
// line 15 "Binomial_BetaBinomial_Likelihood.jweb"
/**
 * This class computes a Binomial-BetaBinomial likelihood for the Barretts LOH data
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2002/10/25 19:43:53 $
 * @version $Revision: 1.2 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import java.lang.Math;
import com.imsl.math.Sfun;
import org.omegahat.GUtilities.ArrayTools;
import org.omegahat.Probability.Distributions.UnnormalizedDensity;


public class Binomial_BetaBinomial_Likelihood implements UnnormalizedDensity
{

/* unnormalized binomial density */
double unnormalized_density_binomial(int x, int n, double pi)
{
  return /*choose(n,x)*/ Math.pow(pi,x) * Math.pow(1.0-pi, n-x );
}

/* unnormalized beta-binomial density */
double unnormalized_density_betabinomial(int x, int n, double pi, double omega)
  {
    int r;
    double tmp0 = 1.0; 
    double tmp1 = 1.0;  
    double tmp2 = 1.0;  

    for(r=0; r <= ( x     - 1 ); r++) tmp0 *= (       pi + ((double) r) * omega );
    for(r=0; r <= ( n - x - 1 ); r++) tmp1 *= ( 1.0 - pi + ((double) r) * omega );
    for(r=0; r <= ( n     - 1 ); r++) tmp2 *= ( 1.0 +      ((double) r) * omega );


    return ( tmp0 * tmp1 / tmp2 );

  }

double unormalized_density_binomial_betabinomial_mixture( int x[], int n[], int len,
                                                          double eta,
                                                          double pi0,
                                                          double pi1,
                                                          double omega1 )
{
  double retval = 1.0;
  int i;

  for(i=0; i < len; i++)
    retval *= (      eta) * unnormalized_density_binomial    ( x[i], n[i], pi0) +
              (1.0 - eta) * unnormalized_density_betabinomial( x[i], n[i], pi1, omega1) ;

  return retval;
}



int   dataLength = 40;
    
int   loh[] =   { 7, 3, 4, 3, 5, 4, 5, 3, 6, 12, 5, 3,
                  1, 3, 5, 3, 11, 2, 2, 2, 3, 5, 3, 4,
                  6, 3, 1, 4, 5, 19, 5, 5, 6, 5, 6, 2,
                  0, 0, 6, 4 } ;

int   n[]  =   { 17, 15, 17, 18, 15, 15, 15, 19,
                 16, 15, 18, 19, 18, 19, 19, 21,
                 17, 16, 12, 17, 18, 18, 19, 19,
                 15, 12, 16, 19, 16, 19, 21, 15,
                 13, 20, 16, 17, 8, 7, 18, 15 };


double unnormalized_density_loh(  double eta,
                               double pi0,
                               double pi1,
                               double omega1 )
{
  return unormalized_density_binomial_betabinomial_mixture( loh, n, dataLength,
                                                            eta,
                                                            pi0, 
                                                            pi1, omega1 );
}

    // Constructor
    public Binomial_BetaBinomial_Likelihood() {}

    // Methods Implementing Unnormalized Density

    public double logUnnormalizedPDF( Object paramOb )
    {
        return Math.log( unnormalizedPDF( paramOb ) ) ;
    }


    public double unnormalizedPDF( Object paramObj )
    {
        double[] params = ArrayTools.Otod( paramObj );
        if(params.length != 4 ) 
	    throw new RuntimeException("Wrong number of parameters!");
        
        double eta    = params[0];
        double pi0    = params[1];
        double pi1    = params[2];
        double omega1 = params[3];
        
        // check range 
        if( (eta < 0.0) || (pi0 < 0.0) || (pi1 < 0.0) || (omega1 < 0.0) ||
            (eta > 1.0) || (pi0 > 1.0) || (pi1 > 1.0) || (omega1 > 0.5) )
            return 0.0;
        else 
            return unnormalized_density_loh(eta, pi0, pi1, omega1 );
    }

}
