

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    /* none */


public class GenericMCMCEvent implements MCMCEvent
{
    
    public Object source;         /* The object generating the event */
    public String description;    /* Description of the event */
    
    public Object getSource()
    {
        return source;
    }

    public void setSource( Object source )
    {
        this.source = source;
    }


    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }
     

    
    /** Default constructor */
    protected GenericMCMCEvent() {} 

    /** Construct with all details */
    public GenericMCMCEvent( Object source,
                             String description)
    {
        this.source = source;
        this.description = description;
    }


    
        /* none */

}
