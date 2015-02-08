

// line 97 "PosteriorProbWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 103 "PosteriorProbWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import org.omegahat.GUtilities.ArrayTools;


// line 8 "PosteriorProbWriter.jweb"
public class PosteriorProbWriter extends ListenerWriter 
{
    
// line 20 "PosteriorProbWriter.jweb"
/*inherited*/

// line 11 "PosteriorProbWriter.jweb"
    
// line 27 "PosteriorProbWriter.jweb"
/*inherited*/

// line 12 "PosteriorProbWriter.jweb"
    
// line 34 "PosteriorProbWriter.jweb"
protected PosteriorProbWriter() throws java.io.IOException
{
}

public PosteriorProbWriter( String filename, boolean append, boolean storeTime) throws java.io.IOException
{
  super( filename, append, storeTime);
}

public PosteriorProbWriter( String filename, boolean append ) throws java.io.IOException
{
  this( filename, append, false );
}

public PosteriorProbWriter( String filename ) throws java.io.IOException
{
  this( filename, false, false);
}


// line 13 "PosteriorProbWriter.jweb"
    
// line 58 "PosteriorProbWriter.jweb"
public void notify( MCMCEvent e )
{
    if( e instanceof DetailChainStepEvent )
    {
	DetailChainStepEvent de = (DetailChainStepEvent) e;

	double currentLogProb;

	if( de.accepted )
        { currentLogProb = de.proposedProb;
          //  out.println( Math.exp(currentLogProb) + " " + currentLogProb +" "+de.current);
            out.println( Math.exp(currentLogProb) + " " + currentLogProb);
        }
	else
	    currentLogProb = de.lastProb;

        // out.println( Math.exp(currentLogProb) + " " + currentLogProb +" "+de.current);  
	//out.println( Math.exp(currentLogProb) + " " + currentLogProb );
    }
    else
      out.println("Unrecognized Event: " + e );
}

public void println( String ignored )
{
  return;
}

public void print( String ignored )
{
  return;
}

// line 14 "PosteriorProbWriter.jweb"
    
// line 91 "PosteriorProbWriter.jweb"
    /* none */

// line 15 "PosteriorProbWriter.jweb"


}
