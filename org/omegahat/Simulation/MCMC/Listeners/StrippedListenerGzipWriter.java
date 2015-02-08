

// line 72 "StrippedListenerGzipWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 78 "StrippedListenerGzipWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;
import java.util.zip.GZIPOutputStream;


// line 8 "StrippedListenerGzipWriter.jweb"
public class StrippedListenerGzipWriter extends StrippedListenerWriter 
{
    
// line 20 "StrippedListenerGzipWriter.jweb"
/*inherited*/

// line 11 "StrippedListenerGzipWriter.jweb"
    
// line 27 "StrippedListenerGzipWriter.jweb"
/*inherited*/

// line 12 "StrippedListenerGzipWriter.jweb"
    
// line 34 "StrippedListenerGzipWriter.jweb"
public StrippedListenerGzipWriter( String filename, boolean append, boolean storeTime ) throws java.io.IOException
{
  out = new PrintWriter( 
           new GZIPOutputStream( (OutputStream)
                                 //                new BufferedWriter ( 
                    new FileOutputStream( filename, append) ));

  this.storeTime = storeTime;
  
  if(storeTime) out.println( "Started at: " + new Date() );
}

public StrippedListenerGzipWriter( String filename, boolean append ) throws java.io.IOException
{
  this( filename, append, false );
}

public StrippedListenerGzipWriter( String filename ) throws java.io.IOException
{
  this( filename, false);
}

// line 13 "StrippedListenerGzipWriter.jweb"
    
// line 59 "StrippedListenerGzipWriter.jweb"
  
 /* inherited */

// line 14 "StrippedListenerGzipWriter.jweb"
    
// line 66 "StrippedListenerGzipWriter.jweb"
    /* none */

// line 15 "StrippedListenerGzipWriter.jweb"
}
