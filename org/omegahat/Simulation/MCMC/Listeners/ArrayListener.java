

// line 107 "ArrayListener.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 113 "ArrayListener.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.util.Vector;


// line 10 "ArrayListener.jweb"
/** Stores contents of <code>RecordingIndexEvent<code>s in an array
 * when one is recieved.  */
public class ArrayListener extends ListenerPrinter
{
    
// line 24 "ArrayListener.jweb"
protected int index=0;
protected boolean debug;
String[] varNames; // list of variables to be plotted;

protected double[][] data;
double[] time;

// line 15 "ArrayListener.jweb"
    
// line 35 "ArrayListener.jweb"
public double[][] data() { return data; }
public double[] data(int which) { return data[which]; }
public String[] varNames() { return varNames; }
public double[] time() { return time; }


public boolean debug() { return debug; }
public boolean debug( boolean debug ) { return this.debug = debug; }

// line 16 "ArrayListener.jweb"
    
// line 49 "ArrayListener.jweb"
public ArrayListener( String[] varNames )
{
  this(varNames, 1000);
}

public ArrayListener (String[] varNames, int size )
{
  this.varNames = varNames;
  data = new double[this.varNames.length][size];
  time = new double[size];

  // zero entries explicitly
  for(int i=0; i< size; i++ )
    {
      time[ i ] = (double) i;
      for(int j=0; j< this.varNames.length; j++)
	data[j][i] = 0.0; //Double.NaN;
    }

}

// line 17 "ArrayListener.jweb"
    
// line 74 "ArrayListener.jweb"
public void notify( MCMCEvent e )
{
  if( e instanceof GenericChainStepEvent )
  {
    
    
    MCMCState current =  ((GenericChainStepEvent ) e).getCurrent();
    
    if( current instanceof ContainerState )
    {
     // MCMCStateDatabase contents = (MCMCStateDatabase) ((ContainerState) current).getContents();

    //  for(int i=0; i<varNames.length; i++)
	//data[i][index] = ((Double) ( contents).get(varNames[i])).doubleValue();

      index = (index + 1) % time.length;
    }
  }
  else 
  if ( debug() ) System.err.println(" Unrecognized event: " + e );
}

// line 18 "ArrayListener.jweb"
    
// line 100 "ArrayListener.jweb"
    /* none */


// line 19 "ArrayListener.jweb"
}
