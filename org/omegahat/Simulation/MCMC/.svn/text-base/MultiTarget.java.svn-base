

/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/MultiTarget.java,v 1.2 2002/11/02 23:10:21 warnes Exp $  */



package org.omegahat.Simulation.MCMC; 


    import org.omegahat.Simulation.MCMC.Proposals.*;
    import org.omegahat.Simulation.MCMC.Targets.*;
    import org.omegahat.Simulation.MCMC.Listeners.*;

import java.util.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.Probability.Distributions.*;


public class MultiTarget implements UnnormalizedDensity
{

  
  UnnormalizedDensity[] targets;

  // read-only
  public    int numChains() { return targets.length; }

  public void setTarget( int which, UnnormalizedDensity what )
  {
    targets[which] = what;
  }

  public void setTargets( UnnormalizedDensity[] what )
  {
    targets = what;
  }




  public double unnormalizedPDF   ( Object state )
  { 
  return Math.exp( logUnnormalizedPDF(state) ); 
  } 

  public double logUnnormalizedPDF( Object state )
  {
    MultiState states = (MultiState) state;

    double retval = 0.0;

    for(int i=0; i < targets.length; i++)
        retval += targets[i].logUnnormalizedPDF( states.get(i) );

    return retval;
  }



  
  public MultiTarget( int nchain )
  {
      targets = new UnnormalizedDensity[nchain];
  }

  public MultiTarget( int nchain, UnnormalizedDensity singleTarget )
  {
      targets = new UnnormalizedDensity[nchain];
      for(int i=0; i < targets.length; i++)
          targets[i] = singleTarget;
  }

  public MultiTarget( UnnormalizedDensity[] targetList )
  {
      targets = targetList;
  }




}

