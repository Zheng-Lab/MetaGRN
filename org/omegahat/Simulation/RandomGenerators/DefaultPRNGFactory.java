package org.omegahat.Simulation.RandomGenerators;

/**
 * a factory class to create PRNG's from PRNGStates.
 *
 * @author  Greg Warnes
 * @author  $Author: warneg $
 * @version $Revision: 1.1.1.1 $
 */
public class DefaultPRNGFactory implements PRNGFactory
{

  /**
   * Instantiate a PRNG of the correct class
   *
   * @param state State information used to create the PRNG
   *
   */
    public PRNG instantiate( PRNGState state ) throws Exception
    {
        String name     = state.getPRNGName();

        Class cl        = Class.forName(name);

        Class classes[] = new Class[1];
        classes[0]      = Class.forName(
                "org.omegahat.Simulation.RandomGenerators.PRNGState");

        java.lang.reflect.Constructor   c = cl.getConstructor(classes);

        Object[] args = new Object[1];
        args[0] = state;

        PRNG prng = (PRNG)
          c.newInstance(args);

        return prng;
    }

  /** 
   * Test PRNGFactory class by creating a CollingsPRNGState,
   * instantiating it using the Factory, and then generating 10 random
   * integers with the result.
   *
   */
  public static void main(String argv[]) throws Exception
    {
      CollingsPRNGAdministrator admin = new CollingsPRNGAdministrator();
      
      PRNGState state = admin.registerPRNGState();
      
      PRNGFactory factory = new DefaultPRNGFactory();
      
      PRNG prng = factory.instantiate( state );
      
      for(int a=1; a<10; a++)
        System.out.println(prng.nextInt());
      
    }
}

