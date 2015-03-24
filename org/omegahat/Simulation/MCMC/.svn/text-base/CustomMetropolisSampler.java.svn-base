

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/CustomMetropolisSampler.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.*;


/** 
 *  A Markov Sampler that uses an internal
 *  <code>SymmetricProposal</code> and
 *  <code>UnnormalizedDensity</code> to generate the next Markov
 *  state.  
 */
public class CustomMetropolisSampler extends CustomMetropolisHastingsSampler
{

    
        /* Inherited from CustomMetropolisHastingsChain */

    
    public CustomMetropolisSampler( ContainerState state, UnnormalizedDensity target, SymmetricProposal proposal, PRNG prng )
    {
        super( state, target, proposal, prng);
    }


    public CustomMetropolisSampler( Object state, UnnormalizedDensity target, SymmetricProposal proposal, PRNG prng   )
    {
        this( new ContainerState(state), target, proposal, prng );
    }

    

    protected double acceptanceProb( Object current, Object proposed )
    {
      return  Math.exp( logAcceptanceProb( current, proposed ) );
    }



    protected double logAcceptanceProb( Object current, Object proposed )
    {
      double numer = target.logUnnormalizedPDF ( proposed );
      double denom = target.logUnnormalizedPDF ( current );
      
      if( Double.isNaN(numer) )
            return Double.NEGATIVE_INFINITY;
      else if ( Double.isInfinite(denom) || Double.isNaN(denom) )
        return 0.0;
      else
        return  Math.min( 0.0, numer - denom );
    }


    

    public static void main( String[] argv )
    {
        PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );

        double[] state = new double[2];
        state[0] = 0.0 ;
        state[1] = 0.0 ;

        CustomMetropolisSampler test = new CustomMetropolisSampler( state, 
                                                                    new BivariateNormalDensity(),
                                                                    new NormalMetropolisProposal(2,prng),
                                                                    prng );
        listenerPrinter l = new listenerPrinter();

        test.registerListener(l);
        test.iterate(10);
    }

        

}

