// line 9 "NotifyingPRNG.jweb"
package org.omegahat.Simulation.RandomGenerators;


abstract public class NotifyingPRNG 
  implements 
	org.omegahat.Simulation.RandomGenerators.PRNG,
        Runnable	
{
// line 57 "NotifyingPRNG.jweb"
    protected java.util.Vector listeners;


// line 31 "NotifyingPRNG.jweb"
public int addListener(PRNGAdministratorListener l) {
 if(listeners() == null) {
    listeners( new java.util.Vector(1));
 }

  listeners().addElement(l);

 return(listeners().size());
}

public boolean removeListener(PRNGAdministratorListener l) {

 if(listeners() == null || listeners().contains(l))
  return(false);

 listeners().remove(l);

return(true);
}



// line 88 "NotifyingPRNG.jweb"
public void run() {
  while(true) {
    nextDouble();
  }
}







// line 63 "NotifyingPRNG.jweb"
public java.util.Vector listeners() {
  return(listeners);
}

public java.util.Vector listeners(java.util.Vector v) {
   listeners = v;
  return(listeners());
}


// line 23 "NotifyingPRNG.jweb"
}


 
