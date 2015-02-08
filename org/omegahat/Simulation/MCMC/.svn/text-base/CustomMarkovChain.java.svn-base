

// line 9 "CustomMarkovChain.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/CustomMarkovChain.java,v 1.1.1.1 2001/04/04 17:16:12 warneg Exp $  */
/* (c) 1999 by the Omegahat Project */


// line 149 "CustomMarkovChain.jweb"
    package org.omegahat.Simulation.MCMC;


// line 155 "CustomMarkovChain.jweb"
    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.*;


// line 20 "CustomMarkovChain.jweb"
/** 
 *  Markov Chain that uses an internal <code>MarkovProposal</code> to generate the next Markov state.
 */
public class CustomMarkovChain extends BaseMarkovChain
{

    
// line 35 "CustomMarkovChain.jweb"
    MarkovProposal generator;

// line 27 "CustomMarkovChain.jweb"
    
// line 64 "CustomMarkovChain.jweb"
public CustomMarkovChain( ContainerState state, MarkovProposal generator )
{
    this.state = (MCMCState) state;
    this.generator = generator;
}


public CustomMarkovChain( Object state, MarkovProposal generator )
{
    this.state = (MCMCState) new ContainerState(state);
    this.generator = generator;
}

protected CustomMarkovChain()
{}


// line 28 "CustomMarkovChain.jweb"
    
// line 48 "CustomMarkovChain.jweb"
/** 
 * Generate the next state from the current one. This method assumes
 * that <code>current</code> is a <code>ContainerState</code>, and
 * uses this to pass a "raw" object to the <code>generator</code>
 */
protected MCMCState generate( MCMCState current )
{
    Object value = ((ContainerState) current ).getContents();
    return (MCMCState) new ContainerState ( generator.generate( value ) );
}

// line 29 "CustomMarkovChain.jweb"
    
// line 88 "CustomMarkovChain.jweb"
static public class NormalGenerator extends Normal implements MarkovProposal
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
    CustomMarkovChain test = new CustomMarkovChain( state, new NormalGenerator(prng) );
    listenerPrinter l = new listenerPrinter();

    test.registerListener(l);
    test.iterate(10);
}

    

// line 30 "CustomMarkovChain.jweb"
}

