

// line 73 "ListenerGzipWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 79 "ListenerGzipWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;
import java.util.zip.GZIPOutputStream;


// line 8 "ListenerGzipWriter.jweb"
public class ListenerGzipWriter extends ListenerWriter 
{
    
// line 20 "ListenerGzipWriter.jweb"
/*inherited*/

// line 11 "ListenerGzipWriter.jweb"
    
// line 27 "ListenerGzipWriter.jweb"
/*inherited*/

// line 12 "ListenerGzipWriter.jweb"
    
// line 34 "ListenerGzipWriter.jweb"
public ListenerGzipWriter( String filename, boolean append, boolean storeTime ) throws java.io.IOException
{
  out = new PrintWriter( 
           new GZIPOutputStream( (OutputStream)
				 //                new BufferedWriter ( 
                    new FileOutputStream( filename, append) ));

  this.storeTime = storeTime;
  
  if(storeTime) out.println( "Started at: " + new Date() );
}

public ListenerGzipWriter( String filename, boolean append ) throws java.io.IOException
{
  this( filename, append, false );
}

public ListenerGzipWriter( String filename ) throws java.io.IOException
{
  this( filename, false);
}


// line 13 "ListenerGzipWriter.jweb"
    
// line 61 "ListenerGzipWriter.jweb"
/* inherited */

// line 14 "ListenerGzipWriter.jweb"
    
// line 67 "ListenerGzipWriter.jweb"
    /* none */

// line 15 "ListenerGzipWriter.jweb"
}
