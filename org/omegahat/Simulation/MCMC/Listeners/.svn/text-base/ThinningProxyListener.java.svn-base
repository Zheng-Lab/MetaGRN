

// line 9 "ThinningProxyListener.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Listeners/ThinningProxyListener.java,v 1.1.1.1 2001/04/04 17:16:31 warneg Exp $ */
/* (c) 1999 by the Omegahat Project */
/* (c) 2000 by the Gregory R. Warnes */


// line 132 "ThinningProxyListener.jweb"
    package org.omegahat.Simulation.MCMC.Listeners;


// line 138 "ThinningProxyListener.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import java.util.*;


// line 21 "ThinningProxyListener.jweb"
/** 
 * This object serves as a proxy <code>MCMCListener</code> that only
 * forwards 1 out of every <code>thinningFactor</code> events to its
 * subscribers 
 **/
public class ThinningProxyListener implements NotifyingObject, MCMCListener
{
    
// line 37 "ThinningProxyListener.jweb"
    
protected Hashtable listeners = new Hashtable();  

protected int thinningFactor = 1;
protected int totalEvents = 0;
// line 29 "ThinningProxyListener.jweb"
    
// line 47 "ThinningProxyListener.jweb"
public int getTotalEvents() { return totalEvents; }
public void resetTotalEvents() { totalEvents=0; }

public int getThinningFactor() { return thinningFactor; }
public int setThinningFactor( int factor ) 
{ 
    if(factor<1) 
        throw new RuntimeException("Attempt to set thinning factor to value less that 1");

    return thinningFactor = factor; 
}

// line 30 "ThinningProxyListener.jweb"
    
// line 65 "ThinningProxyListener.jweb"
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
    
       Enumeration iterator = listeners.elements();
       while( iterator.hasMoreElements() )
       {   
          l = (MCMCListener) iterator.nextElement();
          if( l != null) l.notify( e );
       }
     }




// line 31 "ThinningProxyListener.jweb"
    
// line 102 "ThinningProxyListener.jweb"
/** Function to be called for notification */
public void notify( MCMCEvent event )
{
    totalEvents++;

    // only forward 1 of every thinningFactor events
    if( ((totalEvents) % thinningFactor) == 0 )
       notifyAll(event);
}


// line 32 "ThinningProxyListener.jweb"
    
// line 117 "ThinningProxyListener.jweb"
public ThinningProxyListener()
{
}

public ThinningProxyListener(int factor)
{
    thinningFactor = factor;
}

// line 33 "ThinningProxyListener.jweb"
}

