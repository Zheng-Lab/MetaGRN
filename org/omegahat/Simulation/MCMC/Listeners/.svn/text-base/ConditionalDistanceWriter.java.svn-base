

// line 136 "ConditionalDistanceWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 142 "ConditionalDistanceWriter.jweb"
    import org.omegahat.Simulation.MCMC.*; 

import java.io.*;
import java.util.Date;
import org.omegahat.GUtilities.Distance;
import org.omegahat.GUtilities.ArrayTools;


// line 8 "ConditionalDistanceWriter.jweb"
public class ConditionalDistanceWriter implements MCMCListenerWriter
{
    
// line 20 "ConditionalDistanceWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;
protected double cumsum = 0.0;
protected int    niter = 0;
protected int    numChain = 0;

// line 11 "ConditionalDistanceWriter.jweb"
    
// line 30 "ConditionalDistanceWriter.jweb"
/*none*/

// line 12 "ConditionalDistanceWriter.jweb"
    
// line 37 "ConditionalDistanceWriter.jweb"
public ConditionalDistanceWriter( String filename, 
                   boolean append, 
                   boolean storeTime )
     throws java.io.IOException
{
  out = new PrintWriter( 
            new BufferedWriter ( 
                new FileWriter( filename, append) ));

  this.storeTime = storeTime;

  if(storeTime) out.println( "Started at: " + new Date() );
}

public ConditionalDistanceWriter( String filename, boolean append  ) throws java.io.IOException
{
  this( filename, false, false);
}


public ConditionalDistanceWriter( String filename ) throws java.io.IOException
{
  this( filename, false );
}

protected ConditionalDistanceWriter()  /* for use in sub-classes only */
{
};


// line 13 "ConditionalDistanceWriter.jweb"
    
// line 71 "ConditionalDistanceWriter.jweb"
public void notify( MCMCEvent e )
{
  MultiDoubleState state = null;


  if( e instanceof DetailHastingsCoupledStepEvent )
  {
    DetailHastingsCoupledStepEvent ev = ((DetailHastingsCoupledStepEvent) e);

    if(ev.accepted)
	{
	    cumsum = Distance.euclidean( ArrayTools.Otod(ev.currentComponent), 
					 ArrayTools.Otod(ev.lastComponent ));
	    
	    niter++;
	}
  }

  // else ignore the event

}

public void flush( double pCouple )
{
    flush();
}

public void flush()
{
    out.print( cumsum / (double) niter );

    out.println();
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


// line 14 "ConditionalDistanceWriter.jweb"
    
// line 130 "ConditionalDistanceWriter.jweb"
    /* none */

// line 15 "ConditionalDistanceWriter.jweb"
}
