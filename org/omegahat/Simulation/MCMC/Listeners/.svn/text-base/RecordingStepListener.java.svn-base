

// line 91 "RecordingStepListener.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 97 "RecordingStepListener.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.util.Vector;


// line 10 "RecordingStepListener.jweb"
/**
 * Displays contents and counter of <code>RecordingStepEvent<code>s when one is recieved.
 */
public class RecordingStepListener extends ListenerPrinter
{
    
// line 25 "RecordingStepListener.jweb"
protected int step=0;
protected boolean counter=true;
protected Vector data;
protected boolean debug;

// line 16 "RecordingStepListener.jweb"
    
// line 34 "RecordingStepListener.jweb"
public Vector data() { return data; }
public Vector data( Vector data ) { return this.data=data; }

public boolean debug() { return debug; }
public boolean debug( boolean debug ) { return this.debug = debug; }



// line 17 "RecordingStepListener.jweb"
    
// line 47 "RecordingStepListener.jweb"
public RecordingStepListener()
{
  data = new Vector(10000);
}

public RecordingStepListener ( int size )
{
  data = new Vector( size );
}

// line 18 "RecordingStepListener.jweb"
    
// line 61 "RecordingStepListener.jweb"
public void notify( MCMCEvent e )
{
    if( e instanceof GenericChainStepEvent )
    {
	if (counter) ++step ;

	MCMCState current =  ((GenericChainStepEvent ) e).getCurrent();
	
		if( current instanceof ContainerState )
	{
	  Object contents = ((ContainerState) current).getContents();
	  data.add( contents );
	}
	else
          data.add(current);
    }
    else 
    if ( debug() ) System.err.println(" Unrecognized event: " + e );
}

// line 19 "RecordingStepListener.jweb"
    
// line 85 "RecordingStepListener.jweb"
    /* none */

// line 20 "RecordingStepListener.jweb"
}
