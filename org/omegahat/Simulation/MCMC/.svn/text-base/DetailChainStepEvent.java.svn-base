

  package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

  import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;


public class DetailChainStepEvent extends GenericChainStepEvent 
{
    
    // public MCMCState current; // inherited
    public MCMCState proposed;
    public MCMCState last;  
    public double    lastProb;
    public double    proposedProb;
    public double    forwardProb;
    public double    reverseProb;
    public double    probAccept;
    public double    acceptRand;
    public boolean   accepted;
    public double    acceptRate;

    
        /* none, everything is public */

    
        protected DetailChainStepEvent(){};
        

        public DetailChainStepEvent( Object source, 
                                     MCMCState current,
                                     MCMCState proposed,
                                     MCMCState last,
                                     double    lastProb,
                                     double    proposedProb,
                                     double    forwardProb,
                                     double    reverseProb,
                                     double    probAccept,
                                     double    acceptRand,
                                     boolean   accepted,
                                     double    acceptRate)
        {
            this.source = source;
            this.description = "Chain Step Event (with details)";

            this.current = current;
            this.proposed = proposed;
            this.last = last;
            this.lastProb  = lastProb;
            this.proposedProb = proposedProb;
            this.forwardProb  = forwardProb;
            this.reverseProb  = reverseProb;
            this.probAccept   = probAccept;
            this.acceptRand   = acceptRand;
            this.accepted     = accepted;
            this.acceptRate   = acceptRate;
        }


        /* none */

    

    public String toString()
    {
        String retval = description + "\n";
        retval += ( "Last            = " + last         + "\n" );
        retval += ( "Last     Prob   = " + lastProb     + "\n" );

        retval += ( "Proposed State  = " + proposed     + "\n" );
        retval += ( "Proposed Prob   = " + proposedProb + "\n" );

        retval += ( "Current  State  = " + current      + "\n" );

        retval += ( "Forward Prob    = " + forwardProb  + "\n" );
        retval += ( "Reverse Prob    = " + reverseProb  + "\n" );

        retval += ( "Acceptance Prob = " + probAccept   + "\n" );
        retval += ( "Acceptance Val  = " + acceptRand   + "\n" );

        retval += ( "Accepted?       = " + accepted     + "\n" );
        retval += ( "Acceptance Rate = " + acceptRate   + "\n" );
        return retval;
    }
     

}
