

// line 5 "BivariateNormalDensity.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Targets/BivariateNormalDensity.java,v 1.1.1.1 2001/04/04 17:16:27 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 103 "BivariateNormalDensity.jweb"
    package org.omegahat.Simulation.MCMC.Targets;


// line 109 "BivariateNormalDensity.jweb"
    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.*;



// line 18 "BivariateNormalDensity.jweb"
/**
 * A Bivariate normal density 
 */
public class BivariateNormalDensity implements UnnormalizedDensity
{
    
// line 32 "BivariateNormalDensity.jweb"
protected double              sigma = 1.0;  // standard deviation
protected double              rho   = 0.95;  // correlation
protected Normal              norm;

// line 24 "BivariateNormalDensity.jweb"
    
// line 47 "BivariateNormalDensity.jweb"
protected double square(double x) { return x*x; }

protected double bivariateDensity( double[] bvstate )
{
    double x = bvstate[0];
    double y = bvstate[1];

    return  // 1/Math.sqrt(2 * Math.PI ) * square( 1 / (1 - square( rho ) ) )  
            //  * 
            Math.exp( -1./2. * ( square(x) - 2. * rho * x * y + square(y) ) / ( 1. - square(rho) ) );
}

public double unnormalizedPDF( Object state )
{
    Double[] Dstate = (Double[]) state;
    double[] dstate = new double[ Dstate.length ];

    for(int i=0; i< dstate.length; i++)
        dstate[i] = Dstate[i].doubleValue();

    return bivariateDensity( dstate );
}

public double logUnnormalizedPDF( Object state )
{
    return Math.log( unnormalizedPDF( state ) );
}

// line 25 "BivariateNormalDensity.jweb"
    
// line 77 "BivariateNormalDensity.jweb"
public BivariateNormalDensity()
{
    this.sigma = 1.0;
    this.rho = 0.0;
    this.norm = new Normal( null );
}

public BivariateNormalDensity( double sigma, double rho )
{
    this.sigma = sigma;
    this.rho = rho;
    this.norm = new Normal( null );
}

// line 26 "BivariateNormalDensity.jweb"
    
// line 96 "BivariateNormalDensity.jweb"
    /* none */

// line 27 "BivariateNormalDensity.jweb"
}
