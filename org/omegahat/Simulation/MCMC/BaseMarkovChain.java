

// line 9 "BaseMarkovChain.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/BaseMarkovChain.java,v 1.1.1.1 2001/04/04 17:16:11 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 85 "BaseMarkovChain.jweb"
    package org.omegahat.Simulation.MCMC;


// line 91 "BaseMarkovChain.jweb"
    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import java.util.*;


// line 20 "BaseMarkovChain.jweb"
/** 
 *  An abstract class providing methods implementing the interface <code>MarkovChain</code>.
 */
abstract public class BaseMarkovChain extends NotifyingMCMCObject implements MarkovChain, Runnable
{

    
// line 35 "BaseMarkovChain.jweb"
    MCMCState state;
    
// line 27 "BaseMarkovChain.jweb"
    
// line 40 "BaseMarkovChain.jweb"
    /** Get the current state of the Markov Chain. */
    public MCMCState getState() { return state; }

    /** Generate the next state from the current state. */
    public void  step()
    {
        state = generate( state );
        notifyAll( new GenericChainStepEvent( this, state ) );
    }

    /** 
     * Perform several <code>step</code>s. 
     *
     * @param n how many steps 
     */
    public void iterate( int n )
    {
        for( int i = 0; i < n ; i++)
            step();
    }


// line 28 "BaseMarkovChain.jweb"
    
// line 66 "BaseMarkovChain.jweb"
public void run()
{
  while(true) step();
}

// line 29 "BaseMarkovChain.jweb"
    
// line 76 "BaseMarkovChain.jweb"
    /** Generate the next state from the current one */
    abstract protected MCMCState generate( MCMCState current );

// line 30 "BaseMarkovChain.jweb"
}

