

// line 145 "StrippedListenerWriter.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 151 "StrippedListenerWriter.jweb"
    import org.omegahat.Simulation.MCMC.*;

import java.io.*;
import java.util.Date;
import java.util.zip.GZIPOutputStream;
import org.omegahat.GUtilities.ArrayTools;


// line 8 "StrippedListenerWriter.jweb"
public class StrippedListenerWriter extends ListenerWriter 
{
    
// line 20 "StrippedListenerWriter.jweb"
/*inherited*/

// line 11 "StrippedListenerWriter.jweb"
    
// line 27 "StrippedListenerWriter.jweb"
/*inherited*/

// line 12 "StrippedListenerWriter.jweb"
    
// line 34 "StrippedListenerWriter.jweb"
protected StrippedListenerWriter() throws java.io.IOException
{
}

public StrippedListenerWriter( String filename, boolean append, boolean storeTime) throws java.io.IOException
{
  super( filename, append, storeTime);
}

public StrippedListenerWriter( String filename, boolean append ) throws java.io.IOException
{
  this( filename, append, false );
}

public StrippedListenerWriter( String filename ) throws java.io.IOException
{
  this( filename, false, false);
}


// line 13 "StrippedListenerWriter.jweb"
    
// line 58 "StrippedListenerWriter.jweb"
public String doString( Object current )
{
  String retval = "";

  if( current instanceof ContainerState )
    {
      Object contents = ((ContainerState) current).getContents();
  
    
      if (contents instanceof MultiState )
        {
          MultiState me = (MultiState) contents;
          for(int i=0; i < me.size() ; i++)
            {
              retval += ( i + " " + doString(me.get(i)) + "\n");
            }
          // chop off extra "\n" 
          retval = retval.substring(0, retval.length()-2);
        }
      else if(contents instanceof Object[] || contents instanceof double[] || contents instanceof Double[] 
              || contents instanceof Integer[] || contents instanceof int[] )
        retval = ArrayTools.arrayToString(contents, ""," ", "") ;
      else
        retval = "Unknown type: " + contents.toString() ;
        }
  else if(current instanceof Object[] )
    retval = ArrayTools.arrayToString(current, ""," ", "") ;
  else if(current instanceof double[] )
      {
          double[] dbl  = (double[]) current;
          for(int i=0; i<dbl.length; i++)
              retval += dbl[i] + " ";
      }
  else retval = current.toString();

  return retval;

}

public void notify( MCMCEvent e )
{
    if( e instanceof GenericChainStepEvent )
    {
        MCMCState current =  ((GenericChainStepEvent ) e).getCurrent();

        if( current instanceof ContainerState )
        {
            out.println( doString(current) );

        }
        else if (current instanceof MultiState )
        {
            MultiState me = (MultiState) current;
            
            for(int i=0; i < me.size() ; i++)
                {
                    // out.println( i + " " + doString(me.get(i)) );
                    out.println( i + " " + ArrayTools.arrayToString(me.get(i), ""," ", "" ));
                }
        }
        else
            out.println("Unknown class" + current.getClass() + " = " +  current );
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

// line 14 "StrippedListenerWriter.jweb"
    
// line 139 "StrippedListenerWriter.jweb"
    /* none */

// line 15 "StrippedListenerWriter.jweb"
}
