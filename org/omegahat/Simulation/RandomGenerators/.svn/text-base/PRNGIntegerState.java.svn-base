package org.omegahat.Simulation.RandomGenerators;

/** 
 * Extends PRNGState to provide methods for obtaining and setting state details
 * for PRNG's that can represent thier state as a sequence of
 * ints.
 * 
 * <body> 
 *
 * <p> The main purpose for this is to allow the state to be "passed
 * between" objects using CORBA without defining a new IDL interface
 * to get access to the actual state details.
 *
 * <p> This interface <b> should not be used to change the state of a
 * PRNG! </b> To enforce this restriction, the
 * </code>setPRNGIntState</code> requires a <code>hash</code>
 * parameter that should be used to verify that the state has not been
 * modified.
 *
 * </body> */
public interface PRNGIntegerState extends PRNGState
{

  /**
   * Return an array of ints that fully captures the state of the
   * PRNG represented by this PRNGState.  
   */
  public int[] getIntegerState();

  /**
   * Return the hash value for the state of the PRNG represented by this PRNGState.  
   * <p> 
   *
   * This hash value must be included as a parameter when the <code>
   * setPRNGIntegerState </code> function is called, allowing it to
   * verify that the state has been correctly transmitted and is
   * has not been modified.
   */
  public int[] getIntegerStateHash();

   
  /** 
   * Set the internal state using an array of ints provided by
   * another <code>PRNGIntegerState</code>.  
   *
   * The <code> hash </code> parameter allows the method to check that
   * the state provided has been correctly transmitted and has not
   * been modified.
   */
  public void setIntegerState( int[] intState, int[] hash );

}

