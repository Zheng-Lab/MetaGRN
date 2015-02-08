

// line 139 "QuantileWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 145 "QuantileWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;


// line 9 "QuantileWriter.jweb"
public class QuantileWriter extends HistogramWriter 
{
    
// line 21 "QuantileWriter.jweb"
// Inherited //
// protected PrintWriter out;
// protected boolean storeTime = false;
// protected Histogram[] hists;
// protected double min;
// protected double max;
// protected int    bins;

protected double[]    quantiles;

// line 12 "QuantileWriter.jweb"
    
// line 35 "QuantileWriter.jweb"
/*none*/

// line 13 "QuantileWriter.jweb"
    
// line 42 "QuantileWriter.jweb"
public QuantileWriter( String filename, 
			boolean append, 
			boolean storeTime, 
			double min, 
			double max, 
			int bins,
			double[] quantiles) 
     throws java.io.IOException
{

    super(filename, append, storeTime, min, max, bins );

    this.quantiles = quantiles;

}

public QuantileWriter( String filename, boolean append, double min, 
			double max, int bins, double[]quantiles ) throws java.io.IOException
{
  this( filename, false, false, min, max, bins, quantiles);
}


public QuantileWriter( String filename, double min, double max, int bins, double[]quantiles ) throws java.io.IOException
{
  this( filename, false, min, max, bins, quantiles );
}

protected QuantileWriter()  /* for use in sub-classes only */
{
};


// line 14 "QuantileWriter.jweb"
    
// line 79 "QuantileWriter.jweb"
    // inherited //
    // public void notify( MCMCEvent e )

public void flush()
{
    if (hists != null && quantiles != null )
    {

	double[][] temp = new double[hists.length][quantiles.length];
	
	out.print( "Quantile ");
	for(int i=0; i<hists.length; i++)
	{
	    out.print(" Dimension." + (i+1) );
	    temp[i] = hists[i].quantiles( quantiles );
	}
	out.println();
	
	for(int j=0; j<quantiles.length; j++)
	{
	    out.print( quantiles[j] + " " );
	    for(int i=0; i<hists.length; i++)
		out.print( temp[i][j] + " " );
	    out.println();
	}
	
    }

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

// line 15 "QuantileWriter.jweb"
    
// line 133 "QuantileWriter.jweb"
    /* none */

// line 16 "QuantileWriter.jweb"
}
