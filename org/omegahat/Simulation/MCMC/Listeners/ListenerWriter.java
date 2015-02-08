

// line 119 "ListenerWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 125 "ListenerWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;


// line 9 "ListenerWriter.jweb"
public class ListenerWriter implements MCMCListenerWriter
{
    
// line 21 "ListenerWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;

// line 12 "ListenerWriter.jweb"
    
// line 28 "ListenerWriter.jweb"
/*none*/

// line 13 "ListenerWriter.jweb"
    
// line 35 "ListenerWriter.jweb"
public ListenerWriter( String filename, boolean append, boolean storeTime ) 
     throws java.io.IOException
{
  out = new PrintWriter( 
            new BufferedWriter ( 
                new FileWriter( filename, append) ));

  this.storeTime = storeTime;

  if(storeTime) out.println( "Started at: " + new Date() );
}

public ListenerWriter( String filename, boolean append ) throws java.io.IOException
{
  this( filename, false, true);
}


public ListenerWriter( String filename ) throws java.io.IOException
{
  this( filename, false );
}

protected ListenerWriter()  /* for use in sub-classes only */
{
};


// line 14 "ListenerWriter.jweb"
    
// line 67 "ListenerWriter.jweb"
public void notify( MCMCEvent e )
{
  out.println( e.toString() );
}

public void flush()
{
  out.flush();
}

public void close()
{
  if(storeTime) out.println( "Finished at: " + new Date() );
  
  out.close();
}

/**
 * Closes the stream when garbage is collected.
 * Checks the file descriptor first to make sure it is not already closed.
 */
protected void finalize() throws java.lang.Throwable 
{
    if (out != null)
	{
	    flush();
	    close();
	}
    super.finalize();
}


public void println(String data)
{
    out.println(data);
}

public void print(String data)
{
    out.println(data);
}

// line 15 "ListenerWriter.jweb"
    
// line 113 "ListenerWriter.jweb"
    /* none */

// line 16 "ListenerWriter.jweb"
}
