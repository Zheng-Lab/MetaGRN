package org.omegahat.Simulation.RandomGenerators;
/* Do we need these? :  It seems that most random variable generators
   require a certain number of parameters, and some (time series) also
   require a state.  Do we want to put this information in the interface? */
// public interface PRVGParameters
// {
// }

// public interface PRVGState
// {
// }

/**
 * Interface definition for classes that generate univariate pseudo-random normals.
 *
 * <body> Note that it may not be possible to modify the mean and
 * variance of the generator. </body>  
 */
public interface PseudoRandomNormalGenerator
  extends PseudoRandomUnivariateGenerator 
{
    double mean     = 0;  /* why does the java compiler demand an initializer for these? */
    double variance = 1;

    public double getMean();
    public double getVariance();
}


