

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    /* none */


public class GenericManagerChainEvent extends GenericManagerEvent
{
    
    public int chain;                /** Which chain generated the event */
    public GenericChainEvent event;  /** The event that was generated */
    // public String description;     // inherited from GenericMCMCEvent
    // public Object source;          // inherited from GenericMCMCEvent

    
    int getChain()
    {
        return chain;
    }

    void setChain(int chain)
    {
        this.chain = chain;
    }

    GenericChainEvent getEvent()
    {
        return event ;
    }


    void setEvent( GenericChainEvent event )
    {
        this.event = event;
    }

    
    /** Default constructor */
    protected GenericManagerChainEvent() {} 

    /** Construct with all details */
    public GenericManagerChainEvent( Object            source,
                                     int               chain,
                                     GenericChainEvent event )
    {
        this.source = source;
        this.description = "Manager Chain Event";
        this.chain = chain;
        this.event = event;
    }




        /* none */

    

    public String toString()
    {
        String retval = description + "\n";
        retval += "Source= " + source + "\n";
        retval += "Chain = #" + chain  + "\n";
        retval += "Event = " + event + "\n";
        return retval;
    }
}
