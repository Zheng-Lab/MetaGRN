

// line 162 "HistogramWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 168 "HistogramWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;


// line 9 "HistogramWriter.jweb"
public class HistogramWriter implements MCMCListenerWriter, ResettableListener
{
    
// line 21 "HistogramWriter.jweb"
protected PrintWriter out;
protected boolean storeTime = false;
protected Histogram[] hists;
protected double min;
protected double max;
protected int    bins;

// line 12 "HistogramWriter.jweb"
    
// line 32 "HistogramWriter.jweb"
/*none*/

// line 13 "HistogramWriter.jweb"
    
// line 39 "HistogramWriter.jweb"
public HistogramWriter( String filename, 
			boolean append, 
			boolean storeTime, 
			double min, 
			double max, 
			int bins ) 
     throws java.io.IOException
{
  out = new PrintWriter( 
            new BufferedWriter ( 
                new FileWriter( filename, append) ));

  this.storeTime = storeTime;

  if(storeTime) out.println( "Started at: " + new Date() );

  this.min=min;
  this.max=max;
  this.bins=bins;

}

public HistogramWriter( String filename, boolean append, double min, double max, int bins ) throws java.io.IOException
{
  this( filename, false, true, min, max, bins);
}


public HistogramWriter( String filename, double min, double max, int bins ) throws java.io.IOException
{
  this( filename, false, min, max, bins );
}

protected HistogramWriter()  /* for use in sub-classes only */
{
};


// line 14 "HistogramWriter.jweb"
    
// line 81 "HistogramWriter.jweb"
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
      throw new RuntimeException( "HistogramWriter only works on MultiDoubleState or MultiState objects "+
				  "containing only Double[] or double[] components.");
    }
    
    if(hists==null)
    {
      hists = state.histogram( min, max, bins );
    }
    else
    hists = state.histogram( hists );
  }

  // else ignore the event

}

public void flush()
{
  if(hists!=null)
    for(int i=0; i<hists.length; i++)
      out.println( hists[i].toString( "",      // header 
				      " " + i + " ", // prefix
				      " ",     // rangeSep,
				      " ",     // histSep,
				      "\n",    // postfix,
				      "")      // trailer
		   ); 
  out.flush();
}

public void reset()
{
    hists = null;
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

// line 15 "HistogramWriter.jweb"
    
// line 156 "HistogramWriter.jweb"
    /* none */

// line 16 "HistogramWriter.jweb"
}
