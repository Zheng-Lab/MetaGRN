

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/CustomGibbsSampler.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */
/* (c) 1999 by the Omegahat Project */


    package org.omegahat.Simulation.MCMC;


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.*;


/** 
 *  A Gibbs Sampler that uses an internal <code>FullConditionalProposal</code> to generate the next Markov state.
 */
public class CustomGibbsSampler extends CustomMarkovChain
{

    
        /* none */

    
    public CustomGibbsSampler( ContainerState state, FullConditionalProposal generator )
    {
        super( state, generator );
    }


    public CustomGibbsSampler( Object state, FullConditionalProposal generator )
    {
        super( state, generator );
    }

    
        /* none */

    
    static public class NormalGenerator extends Normal implements FullConditionalProposal
    {
        public Object generate( Object ignored )
        {
            return super.generate();
        }


        public Object[] generateSeveral( int howmany, Object[] ignored )
        {
            return super.generateSeveral( howmany );
        }

        public NormalGenerator( PRNG prng )
        {
            super( prng );
        }
    }




    public static class listenerPrinter implements MCMCListener
    {
        public int step=0;
        public void notify( MCMCEvent e ){
            if( e instanceof GenericChainStepEvent )
                {
                    step++;
                    System.out.println("** Notify : Chain Step Event #" + step + " ** ");
                }
            else
                System.out.println("** Notify: Unrecognized Event ** ");
            System.out.println("** Details:");
            System.out.println( e );
        }
    }



    public static void main( String[] argv )
    {
        PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );

        Double[] state = new Double[1];
        state[0] = new Double( 0.0 );
        CustomGibbsSampler test = new CustomGibbsSampler( state, new NormalGenerator(prng) );
        listenerPrinter l = new listenerPrinter();

        test.registerListener(l);
        test.iterate(10);
    }

        

}

