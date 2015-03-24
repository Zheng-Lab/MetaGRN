//================================================================
//     Copyright (c) 2002, Escola Politécnica-USP
//                     All Rights Reserved
//================================================================
//
// NAME : DFGenerator
// @DATE        : 31/01/2002
// @AUTHOR      : Jaime Shinsuke Ide
//				  jaime.ide@poli.usp.br
//===============================================================

/* The DFGenerator distribution is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation (either
 * version 2 of the License or, at your option, any later version), 
 * provided that this notice and the name of the author appear in all 
 * copies. 
 * If you're using the software, please notify jaime.ide@poli.usp.br so
 * that you can receive updates and patches. DFGenerator is distributed
 * "as is", in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with the DFGenerator distribution. If not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
 
// This class generates random distribution functions.

package org.omegahat.Simulation.MCMC.Examples;

import java.io.*;
import java.util.StringTokenizer;
import java.util.*;
import java.lang.*;
import cern.jet.stat.Probability;
import cern.jet.random.*;
//import MersenneTwister;

public class DFGenerator {

private Random random = new Random();
int seed= (int)(100000*random.nextFloat());  
protected cern.jet.random.engine.RandomEngine engine = new cern.jet.random.engine.MersenneTwister(seed+5);
protected Gamma seedGamma= new Gamma(0.5,0.5,engine); // (alpha,lambda,random_engine)
protected MersenneTwister rn= new MersenneTwister(seed);

/*
* Default Constructor
*/
public DFGenerator () {
    
}

//////////// The class DFGenerator can run independly ///////////////////////////
public static void main(String args[]) {
    DFGenerator gbn = new DFGenerator(); 
	double vAlpha[]={1,1,1};  
    int dim = vAlpha.length;
	double distr[];
	double distrUni[];
	double soma=0.0,somaUni=0.0;
	int n=1000;  // number of generated distributions
	for (int j=0;j<n;j++) {
	    distr = gbn.generateDistribution(dim,vAlpha);
		distrUni = gbn.generateUniformDistribution(dim);
		System.out.print("Dirichlet: ");
		for (int i=0; i<dim; i++)  System.out.print(" " + distr[i]);
		System.out.println();
		System.out.print("Uniform: ");
		for (int i=0; i<dim; i++)  System.out.print(" " + distrUni[i]);
		soma=soma+distr[1];
		somaUni=somaUni+distrUni[1];
		System.out.println();
	}
	System.out.println("Mean value at position one (Dirichlet): " + soma/n);
	System.out.println("Mean value at position one (Uniforme): " + somaUni/n);
} // end of the main. 

public double[] generateUniformDistribution(int n) {
	double distribution[] = new double[n];
    double normalization = 0.0;
    for (int i=0; i<n; i++) {
		distribution[i] = -Math.log(rn.nextDouble());
		normalization += distribution[i];
        }
    for (int i=0; i<n; i++)
		distribution[i] /= normalization;
    return(distribution);
} // End of generateDistribution method.

public float[] generateUniformDistribution(int n, String emFloat) {
	float distribution[] = new float[n];
    float normalization = 0;
    for (int i=0; i<n; i++) {
		distribution[i] = -(float)Math.log(rn.nextFloat());
		normalization += distribution[i];
        }
    for (int i=0; i<n; i++)
		distribution[i] /= normalization;
    return(distribution);
} // End of generateDistribution method.


public double[] generateDistributionFunction(int nv, int np) {
	double distribution[] = new double[nv*np];
        int cont=0;
	double alphas[]={0.1,1};
        for (int i=0;i<np;i++) {
	  //double distr[]=generateUniformDistribution(nv);
	  double distr[]=generateDistribution(nv,alphas);
          for (int j=0;j<nv;j++) {
	    distribution[cont]=distr[j];
		cont++;
      }
	}
    return(distribution);
} // End of generateDistribution method.

public float[] generateDistributionFunction(int nv, int np, String emFloat) {
	float distribution[] = new float[nv*np];
    int cont=0;
	for (int i=0;i<np;i++) {
	  float distr[]=generateUniformDistribution(nv,"emFloat");
	  for (int j=0;j<nv;j++) {
	    distribution[cont]=distr[j];
		cont++;
      }
	}
    return(distribution);
} // End of generateDistribution method.


// This method generate distribution functions with Dirichlet priors (alphas)
public double[] generateDistribution(int n,double alphas[]) {
	double distribution[] = new double[n];
    double normalization = 0.0;
    for (int i=0; i<n; i++) {
		distribution[i] =  generateGamma(alphas[i]);
		normalization += distribution[i];
        }
    for (int i=0; i<n; i++)
		distribution[i] /= normalization;
    return(distribution);
} // End of generateDistribution method.

public double generateGamma(double alpha) {
	double gamma;
	gamma=seedGamma.nextDouble(alpha,1);
    return(gamma);
} // End of generateGamma method.

} // End of the class DFGenerator.
