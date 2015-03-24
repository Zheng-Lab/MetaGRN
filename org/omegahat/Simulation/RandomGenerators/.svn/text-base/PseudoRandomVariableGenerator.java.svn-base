package org.omegahat.Simulation.RandomGenerators;

/**
 *   Interface to Pseudo-random variable generators.  
 *
 * <body> This interface provides access to classes that generate
 * psudo-random variables.  These variables are (in general) of
 * unknown class and thus are represented as objects.  Consequently,
 * this interface can be used to obtain pseudo-random normals or
 * psudo-random trees, or anything else. </body>
 */

public interface PseudoRandomVariableGenerator 
{
  PRNG generator( );

    /**
     * Generate a single pseudo-random variable */
    public Object   nextVariable();      /* synchronized*/

    /**
     * Generate a set of pseudo-random variables 
     * 
     * @param n Number of variables to generate.
     * @retval length <b>n</b> array of variables, stored as Objects
     */
    public Object[] nextVariable(int n); /* synchronized*/

}
