
// line 4 "PRNGAdministratorEvent.jweb"
package org.omegahat.Simulation.RandomGenerators;


public class PRNGAdministratorEvent // extends java.awt.AWTEvent {
{ 

// line 18 "PRNGAdministratorEvent.jweb"
protected  PRNGState state;
protected  PRNGAdministrator source;

// line 23 "PRNGAdministratorEvent.jweb"
public PRNGAdministratorEvent(PRNGState state, PRNGAdministrator admin) {
  source = admin;;
  state(state);
}

public PRNGAdministratorEvent() {
}


// line 33 "PRNGAdministratorEvent.jweb"
public PRNGState state() {
 return(state);
}

public PRNGState state(PRNGState s) { 
  state = s;
 return(state());
}
// line 14 "PRNGAdministratorEvent.jweb"
}

