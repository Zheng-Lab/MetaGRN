package org.omegahat.Simulation.RandomGenerators;

/** Test/Demo the Collings PRNG classes. */
public class TestCollingsPRNG
{

  /** 
   * Compute a batch of double pseudo-random variates using the
   * PRNGAdmin setup. Allow specification of the number of PRNGS used,
   * and the length of the batch. 
   */ 
  public static void main(String argv[]) throws Exception
    {
      int numGenerators = 10;
      int numValues = 10000;
      PRNG[] prngList;

      /* Read Parameters */
      switch (argv.length)
        {
        case 2:
          numValues = Integer.parseInt(argv[0]);
        case 1: 
          numGenerators = Integer.parseInt(argv[1]);
        }

      /* Create the Administrator and Factory*/
      CollingsPRNGAdministrator admin = new
        CollingsPRNGAdministrator();

      PRNGFactory factory = new DefaultPRNGFactory();

      /* Create the PRNG's */
      prngList = new PRNG[numGenerators];

      for(int counter=0; counter<numGenerators; counter++)
        {

          PRNGState state = admin.registerPRNGState();

          if( (counter % 2) == 0 )
            {
              int[] prngIntegerState = ((PRNGIntegerState) state).getIntegerState();
              int[] prngIntegerHash  = ((PRNGIntegerState) state).getIntegerStateHash();

              ///             prngIntegerState[17] = 7;  // Should cause the next line to throw an exception

              state = (PRNGState) new CollingsPRNGState(prngIntegerState, prngIntegerHash  );
              //              ((PRNGIntegerState) state).setIntegerState(prngIntegerState, prngIntegerHash);
            }

          prngList[counter] = factory.instantiate( state );

              
        }

      /* Now, output the batch in columns, one column per generator */
      for(int outer=0; outer<numValues; outer++)
        {
          for(int counter=0; counter<numGenerators; counter++)
            System.out.print(prngList[counter].nextDouble() + " ");
          System.out.println("");
        }
      
    }
}
