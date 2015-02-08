package org.omegahat.Simulation.RandomGenerators;

/** Test/Demo the Collings PRNG classes. */
public class TestCollingsPRNG_Batch
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
      boolean output = true;

      /* Read Parameters */
      switch (argv.length)
        {
        case 3:
          output = Boolean.valueOf(argv[2]).booleanValue();
        case 2:
          numGenerators = Integer.parseInt(argv[1]);
        case 1: 
          numValues = Integer.parseInt(argv[0]);
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
      
      
          prngList[counter] = factory.instantiate( state );
        }

      double[][] Results = new double[numGenerators][];
        
        for(int counter=0; counter<numGenerators; counter++)
          {
            Results[counter] = prngList[counter].nextDoubleArray(numValues);
          }

      /* Now, output the batch in columns, one column per generator */
        if(output)
          for(int outer=0; outer<numValues; outer++)
            {
              for(int counter=0; counter<numGenerators; counter++)
                System.out.print(Results[counter][outer]);
              System.out.println("");
            }


      
    }
}





