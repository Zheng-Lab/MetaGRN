package edu.ksu.cis.bnj.bbn.learning.score;

/*
 * This file is part of Bayesian Network for Java (BNJ).
 *
 * BNJ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * BNJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BNJ in LICENSE.txt file; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.util.*;

import edu.ksu.cis.bnj.bbn.learning.*;
import edu.ksu.cis.kdd.data.converter.*;
import edu.ksu.cis.kdd.data.*;

/**
 * BDE Scoring class
 * 
 * @author Roby Joehanes
 */
public class BDEScoregs extends LearnerScore {

    protected double result = 0.0, result2 = 0.0 ;

    // All of these are cache
    protected int ri_minus_one;
    protected int rik_minus_one=1;
    protected double ri_minus_one_fact;
    protected double rik_minus_one_fact;
    protected double beta = 1;  // shape para
    protected double alpha = 1; // scale inverse
    protected int curNode;
    protected int nodeArity;
    protected int[] nodes;
    protected double[] lnFactorialCache;
    protected int[] arityCache;
    protected int tupleSize;

    public BDEScoregs() {
        super();
    }

	/**
	 * Constructor for K2Score.
	 * @param l
	 */
	public BDEScoregs(Learner l) {

		super(l);
	}

    protected void precache() {
        // Precache factorial and arity table
        Data tuples = tally.getUnderlyingData();
        int maxArity = 0;
        int idx = 0;
        List attributes = tuples.getAttributes();
        arityCache = new int[attributes.size()];
        for (Iterator i = attributes.iterator(); i.hasNext(); idx++) {
            Attribute attr = (Attribute) i.next();
            int arity = attr.getArity();
            arityCache[idx] = arity;
            if (maxArity < arity) maxArity = arity;
        }
        tupleSize = tuples.getTuples().size();
        int tableSize = maxArity*(rik_minus_one+1)+tupleSize;
        lnFactorialCache = new double[tableSize];
        for (int i = 2; i < tableSize; i++) {
            lnFactorialCache[i] = lnFactorialCache[i-1]+Math.log(i);
        }
    }


	/**
     * BDE scoring is:<br>
     * g(vi, parents(vi)) = \Pi_{j=1}^{qi} ((ri-1)!/(Nij+ri-1)!) \Pi_{k=1}^{ri}
     * Nijk!<br>
     * <P>With:<br>
     * vi is the node being queried<br>
     * qi is the unique instantiation of vi's parents<br>
     * ri is vi's arity<br>
     * Nij is the total number of unique cases of one unique instantiation<br>
     * Nijk is the total number of unique cases of one unique instantiation that
     * contains the value of r_ik.<br>
     * 
     * <P>Then, we do a logarithm out of g.
     * 
     * <P>In short, UTSL or RTFP.
     * 
	 * @see edu.ksu.cis.bnj.bbn.learning.LearnerScore#score(java.lang.String, java.util.Set)
	 */
	public double getScore(int curNode, int candidate, Set[] parentTable) {
        this.curNode = curNode;
          

        if (lnFactorialCache == null) { // Precache
            precache();
        }

        nodeArity = arityCache[curNode];
        //rik_minus_one = 1;
        //ri_minus_one = nodeArity - 1;
        ri_minus_one = nodeArity*(rik_minus_one+1) - 1;

        ri_minus_one_fact = lnFactorialCache[ri_minus_one];
        rik_minus_one_fact = lnFactorialCache[rik_minus_one];

        // convert candidate and parents to an array of int
        int arraySize = 0; int parentSize = 0;
        if (parentTable != null && parentTable.length > curNode && parentTable[curNode] != null) {
            parentSize = parentTable[curNode].size();
            arraySize += parentSize;
        }
        if (candidate != NO_CANDIDATES) arraySize++;

        nodes = new int[arraySize];
        int offset = 0;
        if (candidate != NO_CANDIDATES) {
            nodes[0] = candidate;
            offset = 1;
        } 
        if (parentSize > 0) {
            for (Iterator i = parentTable[curNode].iterator(); i.hasNext(); offset++) {
                int value = ((Integer) i.next()).intValue();
                nodes[offset] = value;
            }
        }
        // You can't embed children as well since the formula is
        // different. If we have parent, then the formula is unchanged.
        if (arraySize > 0) {
            result = 0.0;
            result2 = 0.0; 
            iterateUniqueInstantiation(0, tally);
        } else {
            // Otherwise, Nij = N
            result = ri_minus_one_fact - lnFactorialCache[tupleSize + ri_minus_one];
             // q = 1
            result2 = beta*(-1/alpha-Math.log(alpha))+(beta-1)*Math.log(1)+beta*Math.log(beta)-logGamma(beta); 
            for (int j = 0; j < nodeArity; j++) { 
                int n_ijk = tally.tally(curNode, j);
                //System.out.println(tbl+" = "+n_ijk);
                result += lnFactorialCache[n_ijk+rik_minus_one]-rik_minus_one_fact;
            }
        }

        return result+result2;
	}

   protected double logGamma(double x) {
      double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
      double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
      return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
   }


    /**
     * Recursively filter out the data according to the current node
     * @param depth
     * @param tally
     */
    protected void iterateUniqueInstantiation(int depth, Tally tally) {
        int node = nodes[depth];
        int curArity = arityCache[node];
        
        for (int i = 0; i < curArity; i++) {
            Tally newTally = tally.createSubTally(node, i);
            int n_ij = newTally.size();
            if (n_ij == 0) continue; // we don't need to go further because total will be added only with zeroes
            double q = n_ij/(1.0*tupleSize);
            result2 += beta*(-q/alpha-Math.log(alpha))+(beta-1)*Math.log(q)+beta*Math.log(beta)-logGamma(beta);
            if (depth == nodes.length-1) {
                double pi_n_ijk = 0;
                //System.out.println(tbl+" = "+n_ij);
                if (n_ij > 0) {
                    for (int j = 0; j < nodeArity; j++) { 
                        int n_ijk = newTally.tally(curNode, j);
                        //System.out.println(tbl+" = "+n_ijk);
                        //pi_n_ijk += lnFactorialCache[n_ijk];
                          pi_n_ijk +=lnFactorialCache[n_ijk+rik_minus_one]-rik_minus_one_fact;
                    }
                }
                result += ri_minus_one_fact + pi_n_ijk - lnFactorialCache[n_ij+ri_minus_one];
            } else {
                iterateUniqueInstantiation(depth+1, newTally);
            }
        }
    }
}
