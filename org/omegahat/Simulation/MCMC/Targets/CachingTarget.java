

// line 5 "CachingTarget.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Targets/CachingTarget.java,v 1.1.1.1 2001/04/04 17:16:28 warneg Exp $  */



// line 131 "CachingTarget.jweb"
package org.omegahat.Simulation.MCMC.Targets; 


// line 137 "CachingTarget.jweb"
import java.util.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.Probability.Distributions.*;


// line 16 "CachingTarget.jweb"
public class CachingTarget implements UnnormalizedDensity
{

  
// line 32 "CachingTarget.jweb"
UnnormalizedDensity target;
Object[]            objectCache;
double[]            probCache;
boolean[]           cachedIsLogProb;
int                 index = 0;

// line 20 "CachingTarget.jweb"
  
// line 42 "CachingTarget.jweb"
public UnnormalizedDensity target() { return target; }

public Object[]  objectCache()     { return objectCache; }
public double[]  probCache()       { return probCache; }
public boolean[] cachedIsLogProb() { return cachedIsLogProb; }


// line 22 "CachingTarget.jweb"
  
// line 52 "CachingTarget.jweb"
public double unnormalizedPDF   ( Object state )
{ 
    return common(state,false);
} 

public double logUnnormalizedPDF( Object state )
{
    return common(state,true);
}

protected double common( Object state, boolean wantLog)
{
  // update the location we will replace -- so we can make sure and not overwrite a cached 
  // value that we use!
  index = (index+1) % objectCache.length;


  // iterate through cache checking for a match //
  for( int i=0; i< -1 * objectCache.length; i++)
    if ( objectCache != null && state.equals(objectCache[i]) )
      {
        if(index==i)   index = (index+1) % objectCache.length;  // make sure we won't clobber this!
        if ( cachedIsLogProb[i] )
          {
            if (wantLog) return probCache[i];
            else return Math.exp(probCache[i]);
          }
        else 
          {
            if(wantLog) return Math.log(probCache[i]);
            else return probCache[i];
          }
      }

  // no match, so do the computation and store into cache, replacing oldest entry //      
  objectCache[index] = state;
  if(wantLog) probCache[index] = target.logUnnormalizedPDF(state);
  else        probCache[index] = target.unnormalizedPDF(state);
  cachedIsLogProb[index] = wantLog;

  // return the computed value //
  return probCache[index]; 

}

// line 24 "CachingTarget.jweb"
  
// line 101 "CachingTarget.jweb"
public CachingTarget( UnnormalizedDensity target, int length )
{
  if (length < 1 ) throw new RuntimeException("Cache length must be greater than 0.");

  this.target = target;

  objectCache = new Object[length];
  probCache = new double[length];
  cachedIsLogProb = new boolean[length];

  for(int i=0; i<length; i++)
  {
    objectCache[i] = "";
    probCache[i] = Double.NaN;
    cachedIsLogProb[i] = false;
  }
      
}

public CachingTarget( UnnormalizedDensity target )
{
  this(target, 2);
}



// line 26 "CachingTarget.jweb"
}

