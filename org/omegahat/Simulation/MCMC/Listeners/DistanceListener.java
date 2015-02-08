

// line 134 "DistanceListener.jweb"
  package org.omegahat.Simulation.MCMC.Listeners;


// line 140 "DistanceListener.jweb"
    import org.omegahat.Simulation.MCMC.*; 

import org.omegahat.GUtilities.Distance;
import org.omegahat.GUtilities.ArrayTools;


// line 6 "DistanceListener.jweb"
public class DistanceListener implements MCMCListener, ResettableListener
{
    
// line 18 "DistanceListener.jweb"
protected double cumExpectedAcceptance = 0.0;
protected double cumExpectedDistance = 0.0;

protected double cumsumAll = 0.0;
protected int    niterAll  = 0;

protected double cumsumAccepted = 0.0;
protected int    niterAccepted  = 0;

protected int    numChain = 0;

// line 9 "DistanceListener.jweb"
    
// line 33 "DistanceListener.jweb"
public double expectedDistance() 
{ 
    return cumExpectedDistance / (double) niterAll; 
}

public double expectedAcceptance() 
{ 
    return cumExpectedAcceptance / (double) niterAll; 
}

public double averageDistance() 
{ 
    return cumsumAll / (double) niterAll; 
}

public double conditionalDistance() 
{ 
    return cumsumAccepted / (double) niterAccepted; 
}

public double acceptance()
{
    return ((double) niterAccepted) / ((double) niterAll) ;
}

// line 10 "DistanceListener.jweb"
    
// line 63 "DistanceListener.jweb"
public DistanceListener()  
{
};


// line 11 "DistanceListener.jweb"
    
// line 72 "DistanceListener.jweb"
public void notify( MCMCEvent e )
{
  MultiDoubleState state = null;


  if( e instanceof DetailHastingsCoupledStepEvent )
  {
    DetailHastingsCoupledStepEvent ev = ((DetailHastingsCoupledStepEvent) e);
    MultiState current = null;
    
    double accept = ev.probAccept;

    double proposedDist = Distance.euclidean( ArrayTools.Otod(ev.proposedComponent), 
				     ArrayTools.Otod(ev.lastComponent ));	

    double actualDist = Distance.euclidean( ArrayTools.Otod(ev.currentComponent), 
				     ArrayTools.Otod(ev.lastComponent )); 

    cumExpectedAcceptance += Math.exp(accept);
    cumExpectedDistance   += Math.exp(accept) * proposedDist;

    cumsumAll += actualDist;
    niterAll++;

    if(ev.accepted) 
	{
	    cumsumAccepted += proposedDist;
	    niterAccepted++;
	}

    double current_d[]  = ArrayTools.Otod(ev.currentComponent);
    double last_d[]     = ArrayTools.Otod(ev.lastComponent);
    double proposed_d[] = ArrayTools.Otod(ev.proposedComponent);

  }

  // else ignore the event

}

public void reset()
{
    cumExpectedAcceptance = 0.0;
    cumExpectedDistance   = 0.0;
    cumsumAll             = 0.0;
    niterAll              = 0;
    cumsumAccepted        = 0.0;
    niterAccepted         = 0;
}



// line 12 "DistanceListener.jweb"
    
// line 128 "DistanceListener.jweb"
    /* none */

// line 13 "DistanceListener.jweb"
}
