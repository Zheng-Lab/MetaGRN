

// line 115 "StepListenerPrinter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 121 "StepListenerPrinter.jweb"
    import org.omegahat.Simulation.MCMC.*;

    /* none */


// line 8 "StepListenerPrinter.jweb"
/**
 * Displays contents and counter of <code>ChainStepEvent<code>s when one is recieved.
 */
public class StepListenerPrinter extends ListenerPrinter
{
    
// line 23 "StepListenerPrinter.jweb"
    public int step=0;
    public boolean counter=true;

// line 14 "StepListenerPrinter.jweb"
    
// line 29 "StepListenerPrinter.jweb"
    
    /* none */

// line 15 "StepListenerPrinter.jweb"
    
// line 37 "StepListenerPrinter.jweb"
    /* none */

// line 16 "StepListenerPrinter.jweb"
    

// line 44 "StepListenerPrinter.jweb"
protected String arrayToString( Object contents )
{
    Object[] array = (Object[]) contents;

    String retval = "[ ";
    for(int i = 0; i < array.length; i++)
        retval += array[i].toString() + " ";
    retval += "]";

    return retval;
}


public String doString( Object current )
{
	if( current instanceof ContainerState )
	{
	    Object contents = ((ContainerState) current).getContents();

	    if(contents instanceof Object[] )
		return ( arrayToString(contents) );
	    else
		return ( contents.toString() );
	}
	else return current.toString();
}

public void notify( MCMCEvent e )
{
  if( e instanceof DetailChainStepEvent )
    {
      System.out.println( e.toString() );
    }
  else
    if( e instanceof GenericChainStepEvent )
    {
	if (counter) ++step ;

	MCMCState current =  ((GenericChainStepEvent ) e).getCurrent();

	if( current instanceof ContainerState )
	{
	    System.out.println( doString(current) );

	}
	else if (current  instanceof MultiState )
	{
	    MultiState me = (MultiState) current;
	    
	    for(int i=0; i < me.size() ; i++)
		{
		    System.out.println( step + " " + i + " " + doString(me.get(i)) );
		}
	}
	else
	    System.out.println( current );
    }
    else
      System.out.println("Unrecognized Event: " + e );
}

// line 17 "StepListenerPrinter.jweb"
    
// line 109 "StepListenerPrinter.jweb"
    /* none */

// line 18 "StepListenerPrinter.jweb"
}
