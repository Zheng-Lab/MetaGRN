

// line 125 "AcceptanceWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 131 "AcceptanceWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;


// line 8 "AcceptanceWriter.jweb"
public class AcceptanceWriter implements MCMCListenerWriter, ResettableListener
{
    
// line 20 "AcceptanceWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;

protected double acceptanceRate = 0.0;

protected double numAccepted = 0;
protected double numEvents   = 0;

// line 11 "AcceptanceWriter.jweb"
    
// line 32 "AcceptanceWriter.jweb"
public double getAcceptanceRate() { return this.acceptanceRate; }

// line 12 "AcceptanceWriter.jweb"
    
// line 39 "AcceptanceWriter.jweb"
public AcceptanceWriter( String filename, boolean append, boolean storeTime ) 
     throws java.io.IOException
{
  out = new PrintWriter( 
            new BufferedWriter ( 
                new FileWriter( filename, append) ));

  this.storeTime = storeTime;

  if(storeTime) out.println( "Started at: " + new Date() );
}

public AcceptanceWriter( String filename, boolean append ) throws java.io.IOException
{
  this( filename, false, false);
}


public AcceptanceWriter( String filename ) throws java.io.IOException
{
  this( filename, false );
}

protected AcceptanceWriter()  /* for use in sub-classes only */
{
};


// line 13 "AcceptanceWriter.jweb"
    
// line 71 "AcceptanceWriter.jweb"
public void reset()
{
    numAccepted = 0;
    numEvents   = 0;
}
    

public void notify( MCMCEvent e )
{
    if ( e instanceof DetailChainStepEvent )
	{
	    if( ((DetailChainStepEvent) e).accepted)
		numAccepted++;
	    numEvents++;
	    acceptanceRate = numAccepted/numEvents;
	}
		    
}

public void flush()
{
  out.println( acceptanceRate );
  out.flush();
}

public void close()
{
  if(storeTime) out.println( "Finished at: " + new Date() );
  
  out.close();
}

public void println(String data)
{
    out.println(data);
}

public void print(String data)
{
    out.println(data);
}



// line 14 "AcceptanceWriter.jweb"
    
// line 119 "AcceptanceWriter.jweb"
    /* none */

// line 15 "AcceptanceWriter.jweb"
}
