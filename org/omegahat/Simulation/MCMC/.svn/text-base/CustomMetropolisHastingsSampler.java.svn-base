

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/CustomMetropolisHastingsSampler.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import java.io.FileWriter;

import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;
    import org.omegahat.Simulation.MCMC.Examples.*;

import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.*;


/** 
 *  A Markov Sampler that uses an internal
 *  <code>GenericProposal</code> and <code>UnnormalizedDensity</code>
 *  to generate the next Markov state.  
 */
public class CustomMetropolisHastingsSampler extends CustomMarkovChain
{

    

    protected UnnormalizedDensity target;
    protected GeneralProposal     proposal;  
    protected PRNG                prng;
    public    boolean             debug;

    /* Values we may wish to monitor and report */
    protected Object              current;
    protected Object              proposed;
    protected double              logAcceptProb;
    protected double              uniformRand;
    protected boolean             accepted;
    protected double              log_p_X;
    protected double              log_q_X_to_Y;
    protected double              log_p_Y;
    protected double              log_q_Y_to_X;


    protected int                 numProposed;
    protected int                 numAccepted;

    protected boolean             detailed;

    
    public GeneralProposal     getProposal() { return proposal; }
    public UnnormalizedDensity getTarget()   { return target;   }
    public PRNG                getPRNG()     { return prng;     }

    public Object current() { return current ; }
    public Object proposed() { return proposed;}
    public double logAcceptProb() { return logAcceptProb;}
    public double uniformRand() { return uniformRand;}
    public boolean accepted() { return accepted;}
    public double log_p_X() { return log_p_X ;}
    public double log_q_X_to_Y() { return log_q_X_to_Y;}
    public double log_p_Y() { return log_p_Y;}
    public double log_q_Y_to_X() { return log_q_Y_to_X;}


    public int numProposed() { return numProposed; }
    public int numAccepted()  { return numAccepted;  } 
    public double acceptanceRatio() { return (double)  numAccepted / (double)  numProposed ; }
    public void resetCounters() { numProposed = 0; numAccepted = 0; }

    public boolean detailed() { return detailed; }
    public boolean detailed(boolean flag) { return detailed = flag; }

    
    protected CustomMetropolisHastingsSampler()
    {};


    /** Default constructor 
     * @arg state    Initial state.
     * @arg target   Target distribution
     * @arg proposal Proposal distribution
     * @arg PRNG     Random number generator used for accept/reject 
     * @arg detailed Should <code>DetailedChainStepEvent</code>s be generated instead of <code>GenericChainStepEvent</code>s?
     **/
    public CustomMetropolisHastingsSampler( ContainerState state, 
                                            UnnormalizedDensity target, 
                                            GeneralProposal proposal, 
                                            PRNG prng,
                                            boolean detailed)
    {
        this.state = state;
        this.proposal = proposal;
        this.target = target;
        this.prng = prng;

        this.numProposed = 0;
        this.numAccepted = 0;

        this.detailed = detailed;
    }

    public CustomMetropolisHastingsSampler( Object state, 
                                            UnnormalizedDensity target, 
                                            GeneralProposal proposal,
                                            PRNG prng, 
                                            boolean detailed   )
    {
        this( new ContainerState(state), target, proposal, prng, detailed );
    }


    /** Generate <code>GenericChainStepEvent</code>s at each step **/
    public CustomMetropolisHastingsSampler( ContainerState state, UnnormalizedDensity target, GeneralProposal proposal, PRNG prng   )
    {
        this( state, target, proposal, prng, false );
    }

    /** Generate <code>GenericChainStepEvent</code>s at each step **/
    public CustomMetropolisHastingsSampler( Object state, UnnormalizedDensity target, GeneralProposal proposal, PRNG prng   )
    {
        this( state, target, proposal, prng, false );
    }

    
    protected String arrayToString( Object contents )
    {
        Object[] array = (Object[]) contents;

        String retval = "[ ";
        for(int i = 0; i < array.length; i++)
            retval += array[i].toString() + " ";
        retval += "]";

        return retval;
    }



    /** 
     * Generate the next state from the current one. This method assumes
     * that <code>current</code> is a <code>ContainerState</code>, and
     * uses this to pass a "raw" object to the <code>proposal</code> and
     * <code>target</code>.
     */
    public MCMCState generate( MCMCState state )
    {

        String currentStr;
        String proposedStr;

        current  = ((ContainerState) state).getContents() ;

        if(debug)
            {
                if( current instanceof Object[] ) 
                  currentStr  = arrayToString( current  );
                else currentStr  = current.toString();
                System.err.println("current    = " + currentStr  + 
                                   " Prob = " + target.unnormalizedPDF ( current  ) );
            }

        if ( proposal instanceof TimeDependentProposal )
            ((TimeDependentProposal) proposal).timeInc();

        proposed = proposal.generate(current);

        if(debug)
            {
                if( proposed instanceof Object[] ) proposedStr = arrayToString( proposed );
                else proposedStr = proposed.toString();
                System.err.println("proposed   = " + proposedStr + 
                                   " Prob = " + target.unnormalizedPDF ( proposed ) );
            }

        logAcceptProb = logAcceptanceProb( current, proposed ) ;
        
        
        ///add by me
        if(logAcceptProb > 0.0)
        {
        	System.out.println("log acceptance is  - " + logAcceptProb);
        }////
        uniformRand = prng.nextDouble();

        if(debug)
            {
                System.err.println("logAcceptProb = " + logAcceptProb + "\t u=" + uniformRand );
            }


        accepted = Math.log(uniformRand) < logAcceptProb;
        if ( accepted  )
            {
                return new ContainerState( proposed );
            }
        else
          return new ContainerState( current );

    }


    protected double acceptanceProb( Object current, Object proposed )
    {
      return Math.exp(logAcceptanceProb( current, proposed ));
    }



    protected double logAcceptanceProb( Object current, Object proposed )
    {
      log_p_X = target.logUnnormalizedPDF ( current );
      log_q_X_to_Y =  proposal.logTransitionProbability( current, proposed ) ;
      double gib_prior_current = Binomial_HDBNmlf.gibbs_prior;
      
      log_p_Y = target.logUnnormalizedPDF( proposed ) ;
      log_q_Y_to_X =  proposal.logTransitionProbability( proposed, current ) ;
      double gib_prior_proposed = Binomial_HDBNmlf.gibbs_prior;
    		  
      double log_proposal = log_q_Y_to_X - log_q_X_to_Y ;
      double likelihood_ratio= log_p_Y/log_p_X;
      double log_likelihood = Math.log(likelihood_ratio);
      double log_prior = gib_prior_proposed - gib_prior_current;

      //double numer = log_p_Y  + log_q_Y_to_X;
      //double denom = log_p_X  + log_q_X_to_Y;
      double acceptance_log = log_likelihood + log_proposal + log_prior;
      System.out.println("log acceptance is  - " + acceptance_log);

      /*if( Double.isNaN(numer) )
          return Double.NEGATIVE_INFINITY;
      else if ( Double.isInfinite(denom) || Double.isNaN(denom) )
        return 0.0;
      else
        return  Math.min( 0.0,  numer - denom );*/
      
    
      
      return Math.min(0.0, acceptance_log);
    }



    /** Generate the next state from the current state. */
    public void  step()
    {
      state = generate( state );
      numProposed++;
      if (accepted) numAccepted++;
      
      if (detailed)
          {      
          notifyAll( new  DetailChainStepEvent( this,    // source object
                                                
                                                state,                          // current
                                                new ContainerState(proposed),   // proposed
                                                new ContainerState(current),    // last
                                                
                                                log_p_X,       // lastProb
                                                log_p_Y,       // proposedProb
                                                log_q_X_to_Y,  // forwardProb
                                                log_q_Y_to_X,  // reverseProb
                                                logAcceptProb, // probAccept
                                                uniformRand,   // acceptRand
                                                accepted,      // accepted
                                                (double) numAccepted / (double) numProposed // acceptRate
                                                )    );
          }
      else
          {
          notifyAll( new  GenericChainStepEvent( this,  // source object
                                                 state  // current
                                                 ) );
          }
      
    }



    
    public static void main( String[] argv )
    {
        PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );

        Double[] state = new Double[2];
        state[0] = new Double( 0.0 );
        state[1] = new Double( 0.0 );

        CustomMetropolisHastingsSampler test = new CustomMetropolisHastingsSampler( state, 
                                                                    new BivariateNormalDensity(),
                                                                    new NormalMetropolisProposal(2,prng),
                                                                    prng );
        ListenerPrinter l = new StepListenerPrinter();

    //    test.registerListener(l);
        test.iterate(Integer.parseInt(argv[0]));
    }

}

