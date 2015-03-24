package org.omegahat.Simulation.RandomGenerators;

/**
 * The administrator interface provides methods to define
 * successive PRNG's intended to be used in parallel
 * simulations. The classes implementing this interface should
 * choose generator parameters such that each generator returned
 * is expected to produce numbers appearing to have little
 * inter-series dependence.
 *
 * @author  Greg Warnes
 * @author  $Author: warneg $
 * @version $Revision: 1.1.1.1 $
 *
 */

public interface PRNGAdministrator {
    /*
     * Obtain and Register a PRNGSate 
     */
    PRNGState registerPRNGState();
    
    /*
     * Obtain and Register a PRNGState using some parmater(s)
     *
     * <body>
     * Note that any parameters accepted by registerGenerator(Object)
     * should not invalidate the assurance that returned PRNGs have
     * minimal inter-series dependence.
     * </body>
     *
     * @param params Optional parameters, <b>may be ignored</b> */
    PRNGState registerPRNGState(Object params);

}
