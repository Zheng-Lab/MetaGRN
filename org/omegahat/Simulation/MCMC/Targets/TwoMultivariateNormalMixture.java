

// line 5 "TwoMultivariateNormalMixture.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Targets/TwoMultivariateNormalMixture.java,v 1.1.1.1 2001/04/04 17:16:28 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 136 "TwoMultivariateNormalMixture.jweb"
    package org.omegahat.Simulation.MCMC.Targets;


// line 142 "TwoMultivariateNormalMixture.jweb"
    import org.omegahat.GUtilities.ArrayTools;
    import org.omegahat.Probability.Distributions.MVNormal;
    import org.omegahat.Probability.Distributions.UnnormalizedDensity;
    import org.omegahat.Simulation.RandomGenerators.PRNG;



// line 19 "TwoMultivariateNormalMixture.jweb"
/**
 * A UpperWeight of Two Unit Multivariate normal densities
 */
public class TwoMultivariateNormalMixture implements UnnormalizedDensity
{
    
// line 34 "TwoMultivariateNormalMixture.jweb"
protected double           upperWeight = 0.5; // mixing proportion

protected MVNormal         mvnorm0;  
protected MVNormal         mvnorm1;  

public boolean DEBUG = false;

// line 25 "TwoMultivariateNormalMixture.jweb"
    
// line 45 "TwoMultivariateNormalMixture.jweb"
public double getUpperWeight() { return upperWeight; }
public MVNormal getMVNorm0() { return mvnorm0; }
public MVNormal getMVNorm1() { return mvnorm1; }


// line 26 "TwoMultivariateNormalMixture.jweb"
    
// line 54 "TwoMultivariateNormalMixture.jweb"
protected double density( double[] state )
{
    return (1.0 - upperWeight) * mvnorm0.PDF( state ) +
           upperWeight         * mvnorm1.PDF( state );
}

public double unnormalizedPDF( Object state )  // function is unnormalized wrt parameters in state
                                               // but must be otherwise normalized
{
    double retval = (1.0 - upperWeight) * mvnorm0.PDF( state ) +   
                           upperWeight  * mvnorm1.PDF( state ) ;
    return retval;
}

public double logUnnormalizedPDF( Object state )
{
    return Math.log( unnormalizedPDF( state ) );
}

// line 27 "TwoMultivariateNormalMixture.jweb"
    
// line 75 "TwoMultivariateNormalMixture.jweb"
public TwoMultivariateNormalMixture( PRNG prng )
{
  this( 6.0, 10 , prng);
}

public TwoMultivariateNormalMixture( double upper_center, int dim, PRNG prng )
{
    this( upper_center, dim, 0.5, prng );
}

public TwoMultivariateNormalMixture( double upper_center, int dim , double upperWeight, PRNG prng )
{
    double[] mean0 = new double[dim];
    double[] mean1 = new double[dim];
    double[][] cov0  = new double[dim][dim];
    double[][] cov1  = new double[dim][dim];

    for(int i=0; i<dim; i++)
        {
            mean0[i] = 0.0;
            mean1[i] = upper_center;

            for(int j=0; j<dim; j++)
                {
                    if(i==j) 
                        cov0[i][j] = cov1[i][j] = 1.0;
                    else
                        cov0[i][j] = cov1[i][j] = 0.0;
                }
        }

    this.upperWeight = upperWeight;

    mvnorm0 = new MVNormal(mean0, cov0, prng );
    mvnorm1 = new MVNormal(mean1, cov1, prng );

}

public TwoMultivariateNormalMixture( double[] mean0, double[][] cov0,
                                     double[] mean1, double[][] cov1,
                                     double upperWeight,
                                     PRNG prng)
{
    this.upperWeight = upperWeight;

    mvnorm0 = new MVNormal( mean0, cov0, prng );
    mvnorm1 = new MVNormal( mean1, cov1, prng );
}

// line 28 "TwoMultivariateNormalMixture.jweb"
    
// line 129 "TwoMultivariateNormalMixture.jweb"
    /* none */

// line 29 "TwoMultivariateNormalMixture.jweb"
}
