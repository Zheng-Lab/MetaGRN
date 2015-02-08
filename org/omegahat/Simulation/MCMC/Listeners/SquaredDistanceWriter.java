

// line 137 "SquaredDistanceWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 143 "SquaredDistanceWriter.jweb"
    import org.omegahat.Simulation.MCMC.*; 

import java.io.*;
import java.util.Date;
import org.omegahat.GUtilities.Distance;
import org.omegahat.GUtilities.ArrayTools;


// line 9 "SquaredDistanceWriter.jweb"
public class SquaredDistanceWriter implements MCMCListenerWriter
{
    
// line 21 "SquaredDistanceWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;
protected double cumsum = 0.0;
protected int    niter = 0;
protected int    numChain = 0;

// line 12 "SquaredDistanceWriter.jweb"
    
// line 31 "SquaredDistanceWriter.jweb"
/*none*/

// line 13 "SquaredDistanceWriter.jweb"
    
// line 38 "SquaredDistanceWriter.jweb"
public SquaredDistanceWriter( String filename, 
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

public SquaredDistanceWriter( String filename, boolean append  ) throws java.io.IOException
{
  this( filename, false, false);
}


public SquaredDistanceWriter( String filename ) throws java.io.IOException
{
  this( filename, false );
}

protected SquaredDistanceWriter()  /* for use in sub-classes only */
{
};


// line 14 "SquaredDistanceWriter.jweb"
    
// line 72 "SquaredDistanceWriter.jweb"
public void notify( MCMCEvent e )
{
  MultiDoubleState state = null;


  if( e instanceof DetailHastingsCoupledStepEvent )
  {
    DetailHastingsCoupledStepEvent ev = ((DetailHastingsCoupledStepEvent) e);
    MultiState current = null;
 
    double distance = Distance.euclidean( ArrayTools.Otod(ev.currentComponent), 
				          ArrayTools.Otod(ev.lastComponent ));


    cumsum += distance * distance;
    niter++;
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


// line 15 "SquaredDistanceWriter.jweb"
    
// line 131 "SquaredDistanceWriter.jweb"
    /* none */

// line 16 "SquaredDistanceWriter.jweb"
}
