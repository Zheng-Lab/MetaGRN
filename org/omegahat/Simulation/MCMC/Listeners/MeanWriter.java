

// line 162 "MeanWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 168 "MeanWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;


// line 9 "MeanWriter.jweb"
public class MeanWriter implements MCMCListenerWriter, ResettableListener
{
    
// line 21 "MeanWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;
protected double[] cumsums = null;
protected int    niter = 0;
protected int    numChain = 0;

// line 12 "MeanWriter.jweb"
    
// line 31 "MeanWriter.jweb"
/*none*/

// line 13 "MeanWriter.jweb"
    
// line 38 "MeanWriter.jweb"
public MeanWriter( String filename, 
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

public MeanWriter( String filename, boolean append  ) throws java.io.IOException
{
  this( filename, false, false);
}


public MeanWriter( String filename ) throws java.io.IOException
{
  this( filename, false );
}

protected MeanWriter()  /* for use in sub-classes only */
{
};


// line 14 "MeanWriter.jweb"
    
// line 72 "MeanWriter.jweb"
public void notify( MCMCEvent e )
{
  MultiDoubleState state = null;


  if( e instanceof GenericChainStepEvent )
  {
    GenericChainStepEvent ev = ((GenericChainStepEvent) e);
    MultiState current = null;
    
    if (ev.current instanceof ContainerState)
      current = (MultiState) ((ContainerState) ev.current).getContents();      
    else if (ev.current instanceof MultiState)
      current = (MultiState) ev.current;

    state = new MultiDoubleState( current );

    if(false)
    {
      throw new RuntimeException( "MeanWriter only works on MultiDoubleState or MultiState objects "+
			      "containing only Double[] components.");
    }
    
    if(cumsums==null)
    {
	cumsums = state.cumsum();
	numChain = state.size();
    }
    else
	cumsums = state.cumsum( cumsums );

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
    if(cumsums != null)
	{
	    for(int i=0; i<cumsums.length; i++)
		out.print( ( cumsums[i] / (double) niter / (double) numChain  )
			   + " " ); 
	}
    out.println();
    out.flush();
}


public void reset()
{
    cumsums = null;
    niter = 0;
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


// line 15 "MeanWriter.jweb"
    
// line 156 "MeanWriter.jweb"
    /* none */

// line 16 "MeanWriter.jweb"
}
