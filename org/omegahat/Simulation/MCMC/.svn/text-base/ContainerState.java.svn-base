

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;


    import java.lang.reflect.Array;


/**
 * A generic MCMCState that can hold arbitrary Objects 
 */
public class ContainerState implements MCMCState
{
    
    protected Object contents;

    
    public Object getContents() { return contents; }

    
    public ContainerState( Object contents )
    {
        this.contents = contents;
    }

    
    protected String arrayToString()
    {
        Object[] array = (Object[]) contents;

        String retval = "[ ";
        for(int i = 0; i < array.length; i++)
            retval += array[i].toString() + " ";
        retval += "]";

        return retval;
    }

    public String toString()
    {

      String contentsString = "ContainerState: " ;
           
      try
        {
          int len = Array.getLength( contents );
          
          contentsString += "[ ";
          
          for(int j = 0; j < len ; j++)
            {
              contentsString += Array.get(contents,j) + " ";
            }
          
          contentsString += "] ";
        }
      catch ( Throwable e )
        {
                  contentsString = contents.toString();
        }
      
      
      return contentsString;
      
    }

    
        /* none */

}
