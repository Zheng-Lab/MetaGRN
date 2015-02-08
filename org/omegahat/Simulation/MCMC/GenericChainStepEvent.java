

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

  import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;


public class GenericChainStepEvent extends GenericChainEvent
{
    
    public MCMCState current;

        
    public MCMCState getCurrent() { return this.current; }
    public void      setCurrent( MCMCState current ) { this.current = current; }

    
        protected GenericChainStepEvent()
        {
        }


        public GenericChainStepEvent( Object source, MCMCState current )
        {
            this.description = "Generic Chain Step Event";
            this.source = source;
            this.current = current;
        }


        /* none */

    
    public String toString()
    {
        return this.description + "\n" + 
               "Source: " + source + "\n" + 
               "Current: " + current + "\n" ;

    }
}
