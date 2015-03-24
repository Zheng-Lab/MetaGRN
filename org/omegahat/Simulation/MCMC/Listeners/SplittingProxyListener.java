

// line 16 "SplittingProxyListener.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Listeners/SplittingProxyListener.java,v 1.1.1.1 2001/04/04 17:16:33 warneg Exp $ */
/* (c) 1999 by the Omegahat Project */
/* (c) 2000 by the Gregory R. Warnes */


// line 199 "SplittingProxyListener.jweb"
    package org.omegahat.Simulation.MCMC.Listeners;


// line 205 "SplittingProxyListener.jweb"
    import org.omegahat.GUtilities.ArrayTools;

    import org.omegahat.Simulation.MCMC.*;

    import java.util.*;


// line 28 "SplittingProxyListener.jweb"
/** 
 * This object serves as a proxy <code>MCMCListener</code> that only
 * forwards 1 out of every <code>thinningFactor</code> events to its
 * subscribers 
 **/
public class SplittingProxyListener implements NotifyingObject, MCMCListener
{
    
// line 45 "SplittingProxyListener.jweb"
    
protected Hashtable listeners = new Hashtable();  
protected Hashtable minRanges = new Hashtable();
protected Hashtable maxRanges = new Hashtable();

// line 36 "SplittingProxyListener.jweb"
    
// line 55 "SplittingProxyListener.jweb"
    /* none */

// line 37 "SplittingProxyListener.jweb"
    
// line 63 "SplittingProxyListener.jweb"
public class MyHandle implements MCMCListenerHandle
{
  public MyHandle() {};
}

// we can always register and unregister listeners
public MCMCListenerHandle registerListener  ( MCMCListener  listener ) 
{ 
  
  MCMCListenerHandle handle = new MyHandle();
  
  listeners.put( handle,  listener); 
  return handle;
}

public void    unregisterListener( MCMCListenerHandle handle   )
{ 
  listeners.remove( handle );
}
    
protected void   notifyAll( MCMCEvent e )
{
  MCMCListener l = null;
    
  Enumeration iterator = listeners.keys();
  while( iterator.hasMoreElements() )
    outer: {   

      MyHandle hash = (MyHandle) iterator.nextElement();

      inner: {
	

	if( e instanceof GenericChainStepEvent)
	  {
	    double[] state;
	    //	    try 
	    //		{
	    if (e instanceof DetailHastingsCoupledStepEvent)
		{
		    state = ArrayTools.Otod( ((DetailHastingsCoupledStepEvent) e).currentComponent );
		}
	    else 
		{
		    throw new RuntimeException( "This class only designed to handle 'DetailHastingsCoupledStepEvent'");
		    
		}


		//	      }
	      //	    catch( Throwable t)
	      //	      {
	      //		break inner;
	      //	      }
	    
	    double[] min = (double[]) minRanges.get(hash);
	    double[] max = (double[]) maxRanges.get(hash);
	    
	    if(min != null)
	      {
		if( state.length != min.length ) 
		  throw new RuntimeException("Min Range does not conform to current state");
		
		for(int i=0; i < min.length; i++)
		  if( state[i] < min[i] ) break inner;
	      }
	    
	    if(max != null)
	      {
		if( state.length != max.length ) 
		  throw new RuntimeException("Max Range does not conform to current state");
		
		for(int i=0; i < max.length; i++)
		  if( state[i] > max[i] ) break inner;
	      }

	    GenericChainStepEvent ev = new GenericChainStepEvent( this, new MultiState(1,state) );

	    l = (MCMCListener) listeners.get(hash);
	    if( l != null) l.notify( ev );
	  }
	else
	  {
	    l = (MCMCListener) listeners.get(hash);
	    if( l != null) l.notify( e );
	  }
      }// inner

  } // outer
}




// line 38 "SplittingProxyListener.jweb"
    
// line 160 "SplittingProxyListener.jweb"
/** Function to be called for notification */
public void notify( MCMCEvent event )
{
    notifyAll(event);
}


// line 39 "SplittingProxyListener.jweb"
    
// line 170 "SplittingProxyListener.jweb"
public void setRange( MCMCListenerHandle handle, double[] min, double[] max )
{
  if( ! (handle instanceof MyHandle) )
    throw new RuntimeException("Illegal listener handle.");

  if( !listeners.containsKey(handle) )
    throw new RuntimeException("Specified Listener not registered.");  

  minRanges.put( handle, min );
  maxRanges.put( handle, max );
}

		  

// line 40 "SplittingProxyListener.jweb"
    
// line 189 "SplittingProxyListener.jweb"
public SplittingProxyListener()
{
}

// line 41 "SplittingProxyListener.jweb"
}

