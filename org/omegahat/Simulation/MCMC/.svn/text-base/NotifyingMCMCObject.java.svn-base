

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/NotifyingMCMCObject.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import java.util.*;


/** 
 *  A simple class providing methods implementing the interface NotifyingObject
 */
abstract public class NotifyingMCMCObject implements NotifyingObject
{

        
        protected Hashtable listeners = new Hashtable();  

    
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




    
        /** Generate the next state from the current one */
        abstract protected MCMCState generate( MCMCState current );

}

