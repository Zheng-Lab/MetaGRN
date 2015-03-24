

// line 126 "DistanceWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 132 "DistanceWriter.jweb"
    import org.omegahat.Simulation.MCMC.*; 

import java.io.*;
import java.util.Date;
import org.omegahat.GUtilities.Distance;
import org.omegahat.GUtilities.ArrayTools;


// line 9 "DistanceWriter.jweb"
public class DistanceWriter extends DistanceListener implements MCMCListenerWriter
{
    
// line 21 "DistanceWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;

// inherited: protected double cumsumAll = 0.0;
// inherited: protected int    niterAll  = 0;
// inherited: protected double cumsumAccepted = 0.0;
// inherited: protected int    niterAccepted  = 0;
// inherited: protected int    numChain = 0;

// line 12 "DistanceWriter.jweb"
    
// line 34 "DistanceWriter.jweb"
/*none*/

// line 13 "DistanceWriter.jweb"
    
// line 41 "DistanceWriter.jweb"
public DistanceWriter( String filename, 
		   boolean append, 
		   boolean storeTime )
     throws java.io.IOException
{
  super();

  out = new PrintWriter( 
            new BufferedWriter ( 
                new FileWriter( filename, append) ));

  this.storeTime = storeTime;

  if(storeTime) out.println( "Started at: " + new Date() );
}

public DistanceWriter( String filename, boolean append  ) throws java.io.IOException
{
  this( filename, false, false);
}


public DistanceWriter( String filename ) throws java.io.IOException
{
  this( filename, false );
}

protected DistanceWriter()  /* for use in sub-classes only */
{
};


// line 14 "DistanceWriter.jweb"
    
// line 77 "DistanceWriter.jweb"
public void flush( double pCouple )
{
    flush();
}

public void flush()
{
  out.println( "Average             Acceptance: " + (double) niterAccepted / (double) niterAll );
  out.println( "Average             Distance  : " + cumsumAll              / (double) niterAll );
  out.println( "Average Conditional Distance  : " + cumsumAccepted         / (double) niterAccepted );

  out.println( "Expected             Acceptance: " + cumExpectedAcceptance  / (double) niterAll );
  out.println( "Expected             Distance  : " + cumExpectedDistance    / (double) niterAll );
  out.println( "Expected Conditional Distance  : " + cumExpectedDistance    / cumExpectedAcceptance );

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


// line 15 "DistanceWriter.jweb"
    
// line 120 "DistanceWriter.jweb"
    /* none */

// line 16 "DistanceWriter.jweb"
}
