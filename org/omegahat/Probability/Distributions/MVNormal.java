
// line 52 "MVNormal.jweb"
    

// line 24 "MVNormal.jweb"
/*
* @(#)$Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Probability/Distributions/MVNormal.java,v 1.3 2002/11/02 23:00:50 warnes Exp $
*
* Copyright (C) 1999, G. Warnes (greg@warnes.net)
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*
*/


// line 53 "MVNormal.jweb"
    
// line 753 "MVNormal.jweb"
package org.omegahat.Probability.Distributions;

// line 54 "MVNormal.jweb"
    

// line 760 "MVNormal.jweb"
import org.omegahat.Simulation.RandomGenerators.*;
//import Jama.*;
//import com.imsl.math.*;
import cern.colt.matrix.*;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.*;



import org.omegahat.GUtilities.Distance;
import org.omegahat.GUtilities.ArrayTools;


// line 56 "MVNormal.jweb"
public class MVNormal implements Distribution, UnnormalizedDensity //implements MVNormalDistribution
{

    
// line 77 "MVNormal.jweb"
/** Mean */
private DoubleMatrix1D                mu;

/** Covariance Matrix **/
private DoubleMatrix2D                sigma;
private double[]                      sigmaSqrt;
private DoubleMatrix2D                sigmaInv;
private SingularValueDecomposition    sigmaSVD;
private double                        sigmaDet;
private DoubleMatrix2D                transMat;
private boolean                       isDiagonalCov;
private double                        normalizing_constant;
private double                        log_normalizing_constant;

/** Pseudo-Random Number Generator to Use */
private PRNG prng;

/** Pull all the probability functions out of package */
private PRNGDistributionFunctions prob;


/** Object used for linear algebra computations **/
private cern.colt.matrix.linalg.Algebra Algebra = cern.colt.matrix.linalg.Algebra.DEFAULT;

public boolean DEBUG = false;
  
// line 60 "MVNormal.jweb"
    
// line 106 "MVNormal.jweb"
  
public double[] getMean()
{ 
    return mu.toArray();
}

public double[][] getCovariance()
{
    return sigma.toArray();
}

public Object setMean( Object mean_in )
{
    return setMean( ArrayTools.Otod(mean_in) );
}

public double[] setMean(double[] mean_in)
{
    mu = new DenseDoubleMatrix1D(mean_in);
    return mean_in;
}

public DoubleMatrix1D setMean(DoubleMatrix1D mean_in)
{
     mu = mean_in.copy();
     return mu;
}

// public Object setCovariance( Object var_in )
// {
//     if(var_in instanceof double[][])
// 	return setCovaraince( (double[][]) var_in );
//     else if( var_in instanceof Double[][] )
// 	{
// 	    int        dim = ((Double[][]) var_in).length;
// 	    double[][] cov = new Double[ dim ][];
// 	    for(int i=0; i < dim; i++)
// 		cov[i] = ArrayTools.Otod( ((Double[][]) var_in)[i] );
// 	    return setCovaraince( cov );
// 	}
//     else
// 	throw new RuntimeException("setCovariance requires a double[] or DoublNot yet implemented.");
// }

public double[][] setCovariance(double[][] var)
{
    DoubleMatrix2D retval = new DenseDoubleMatrix2D( var );
    setCovariance(retval);
    return(var);
}

public DoubleMatrix2D setCovariance(DoubleMatrix2D var)
{
    if (DEBUG) System.err.println("setCovariance\n");

    boolean tmpIsDiagonalCov = true;
    boolean same = true;
    for(int i=0; i<var.rows(); i++)
      for(int j=0; j<var.columns(); j++)
        {
	    if( sigma==null || var.get(i,j) != sigma.get(i,j) )
                    same = false;

            if( i!=j && ( var.get(i,j) != 0.0 ) ) 
                tmpIsDiagonalCov = false;
        }

    if(!same)
    {
	sigma = var.copy();
        isDiagonalCov = tmpIsDiagonalCov;


	if(isDiagonalCov)
	{
	    sigmaInv = null; // not needed 
	    sigmaSVD = null; // not needed 
	    transMat = null; // not needed

	    sigmaSqrt = new double[sigma.rows()];

	    sigmaDet = 1.0;
	    for(int i=0; i < var.rows(); i++)
		{
		    sigmaDet *= var.get(i,i);
		    sigmaSqrt[i] = Math.sqrt(sigma.get(i,i));
		}

	}
	else
	{
	  try 
	    {
	      sigmaInv  = Algebra.inverse(sigma);
	      sigmaDet  = Algebra.det(sigma);

	      sigmaSVD  = null;
	      transMat  = null;
	      sigmaSqrt = null;

	    }
	  catch(Exception ex )
  	    {
	      throw new RuntimeException("Singular Matrix: " + 
					 ArrayTools.arrayToString(sigma) );
	    }
	}


	normalizing_constant = 1.0 / ( Math.sqrt(Math.abs(sigmaDet)) * 
				       Math.pow( 2 * Math.PI, 
						 ((double) mu.size()) / 2.0) );

	log_normalizing_constant = Math.log(normalizing_constant);

    }

    return sigma;
    
}

public int dim()
{
    return mu.size();
}

public void setupTransMat()
{
  if(sigmaSVD==null)
     sigmaSVD = new SingularValueDecomposition(sigma);

  if(transMat==null)
  {
    
    // transMat = t( V %*% (t(U) * sqrt(S) ) ) 

    DoubleMatrix2D   S = sigmaSVD.getS();  // diagonal matrix
    DoubleMatrix2D   U = sigmaSVD.getU();
    DoubleMatrix2D   V = sigmaSVD.getV();
    
    transMat = Algebra.transpose( 
                  Algebra.mult( V, 
		    componentMultiply( 
				      Algebra.transpose( U ),
				      sqrtMat( S )
				      ) 
				)
		  );
  }
}



// line 61 "MVNormal.jweb"
    


// line 265 "MVNormal.jweb"
public MVNormal (int  dimensions, PRNG prng_in )
{
    prng = prng_in;
    prob = new PRNGDistributionFunctions(prng);

    DoubleMatrix1D mean = new DenseDoubleMatrix1D(dimensions);
    DoubleMatrix2D var  = new DenseDoubleMatrix2D(dimensions,dimensions);
    
    for(int i=0; i<dimensions; i++)
    {
        mean.set(i, 0.0);
        for(int j=0; j<dimensions; j++)
            var.set(i,j, (i==j) ? 1.0 : 0.0) ;
    }

    setMean(mean);
    setCovariance(var);
    checkConformity();
 

}

public MVNormal (double[] mean, double[][] var, PRNG prng_in )
{
    prng = prng_in;
    prob = new PRNGDistributionFunctions(prng);
    
    setMean(mean);
    setCovariance(var);
    checkConformity();
 
    
}     

public MVNormal (double[][] var, PRNG prng_in )
{

    double[] mean = new double[var.length];
    for(int i=0; i<mean.length; i++)
        mean[i] = 0.0;

    prng = prng_in;
    prob = new PRNGDistributionFunctions(prng);
    
    setMean(mean);
    setCovariance(var);
    checkConformity();
}

public MVNormal (DoubleMatrix1D mean, DoubleMatrix2D var, PRNG prng_in )
{
    prng = prng_in;
    prob = new PRNGDistributionFunctions(prng);
    
    setMean(mean);
    setCovariance(var);
    checkConformity();
 
    
}     

public MVNormal (DoubleMatrix2D var, PRNG prng_in )
{

    DoubleMatrix1D mean = new DenseDoubleMatrix1D(var.rows());
    for(int i=0; i<mean.size(); i++)
        mean.set(i, 0.0);

    prng = prng_in;
    prob = new PRNGDistributionFunctions(prng);
    
    setMean(mean);
    setCovariance(var);
    checkConformity();
}
// line 62 "MVNormal.jweb"
    
// line 344 "MVNormal.jweb"
private double[] rnorm()
{
    setupTransMat();
    int dim = mu.size();

    double[] retval = new double[dim];

    if(isDiagonalCov)
    {
        for( int i=0; i < dim; i++)
            retval[i] = prob.normalRand() * sigmaSqrt[i];
    }
    else
    {

        DoubleMatrix1D tmpArr = new DenseDoubleMatrix1D(dim);
        
        for(int i=0; i < dim; i++)
        {
            tmpArr.set(i, prob.normalRand());
        }
        
	//        retval = Algebra.mult( Algebra.transpose(transMat), tmpArr ).toArray();
        retval = Algebra.mult( transMat, tmpArr ).toArray();
    }

        
    for(int i=0; i < dim; i++)
    {
	retval[i] = retval[i] + mu.get(i);
    }
    return retval;

}

/* squared Mahalanobis distance */
private double mahalanobis_2( double[] x )
{
    double retval;

    if (isDiagonalCov)
        {
            retval = 0.0;
            for(int i=0; i < x.length; i++)
                retval += square( (x[i] - mu.get(i))/sigmaSqrt[i] ) ;
        }
    else
        {
  	    DoubleMatrix1D centered = new DenseDoubleMatrix1D(x.length);
	    for(int i=0; i<x.length; i++)
		centered.set(i , x[i] - mu.get(i) );

	    retval = xVx( centered, sigmaInv );
        }

    return retval;
}

double xVx( DoubleMatrix1D c, DoubleMatrix2D V )
{
    DoubleMatrix1D Vc  = Algebra.mult( V, c );
    double cVc = 0.0;

    for(int i=0; i < c.size(); i++)
	cVc += c.get(i) * Vc.get(i) ;

    return cVc;
}


private double dnorm( double[] x )
{
    checkConformity( x );
    double retval = Math.exp(-mahalanobis_2(x)/2);
    retval = retval * normalizing_constant;

    return retval;
}


private double unnorm_dnorm( double[] x )
{
    checkConformity( x );
    double retval = Math.exp(-mahalanobis_2(x)/2);

    return retval;
}


private double log_dnorm( double[] x)
{
    checkConformity( x );
    double retval = -mahalanobis_2(x)/2;
    retval = retval + log_normalizing_constant;

    return retval;
}

private double unnorm_log_dnorm( double[] x )
{
    checkConformity( x );
    double retval = -mahalanobis_2(x)/2;

    return retval;
}

// line 63 "MVNormal.jweb"
    
// line 454 "MVNormal.jweb"
static final private double square( double a )
{
    return a*a;
}

private DoubleMatrix2D componentMultiply( DoubleMatrix2D mat, 
					  DoubleMatrix2D vec )
{
    DoubleMatrix2D retval = mat.like(); 

    for(int i=0; i < mat.rows(); i++)
        for(int j=0; j < mat.columns(); j++)
            retval.set(i,j, mat.get(i,j) * vec.get(i,i) );

    return retval;
}

private DoubleMatrix2D sqrtMat( DoubleMatrix2D mat )
{
     DoubleMatrix2D retval = mat.like();
     
     for(int i=0; i < mat.rows(); i++)
       for(int j=0; j < mat.columns(); j++)
	   retval.set(i,j, Math.sqrt(mat.get(i,j)));

     return retval;
}

private DoubleMatrix1D sqrtVec( DoubleMatrix1D vec )
{
    DoubleMatrix1D retval = new DenseDoubleMatrix1D(vec.size());

    for(int i=0; i < vec.size(); i++)
       retval.set(i, Math.sqrt(vec.get(i)) );

    return retval;
}
   
protected void checkConformity( double[] x )
{
  if( x.length != mu.size() )
      throw new RuntimeException("Object passed to PDF does not conform to length of mean vector.");
  
  if( (x.length != sigma.rows())  || (x.length != sigma.columns()) )
      throw new RuntimeException("Object passed to PDF does not conform to covariance matrix.");
}


protected void checkConformity( DoubleMatrix1D x )
{
  if( x.size() != mu.size() )
      throw new RuntimeException("Object passed to PDF does not conform to length of mean vector.");
  
  if( (x.size() != sigma.rows())  || (x.size() != sigma.columns()) )
      throw new RuntimeException("Object passed to PDF does not conform to covariance matrix.");
}


protected void checkConformity()
{
    checkConformity(mu);
}



// line 64 "MVNormal.jweb"
    

// line 525 "MVNormal.jweb"
public double PDF( double[] x )
{
    return dnorm( x );
}


public double logPDF( double[] x )
{
    return log_dnorm( x );
}


public double unnormalizedPDF( double[] x )
{
    return unnorm_dnorm( x );
}


public double logUnnormalizedPDF( double[] x)
{
    double retval = unnorm_log_dnorm( x );
    return retval;
}



public Object generateDoubleArray()
{
    return rnorm();
}

// line 65 "MVNormal.jweb"
    
// line 598 "MVNormal.jweb"
// public String name()
// {
//     return "Multivariate Normal Distribution";
// }

// public String identifier()
// {
//     return ("N( mu , sigma )");
// } 

// public Object[] getParameters() {
//     Object[] params = new Object[2];
//     params[0] = new Double(mu);
//     params[1] = new Double(sigma);
//     return params;
// }

// public String[] getParameterNames() {
//     String[] params = new String[2];
//     params[0] = "Mean";
//     params[1] = "Standard Deviation";
//     return params;
// }

// /* I tried to make this robust. */
// public void setParameters(Object[] params) 
// {
//     if(params==null)
//      {
//             mu    = 0.0;
//             sigma = 1.0;
//      }
//     else switch(params.length)
//         {
//         default:
//         case 2:
//          if(params[1]==null)
//              sigma=1.0;
//          else
//              sigma = ((Number) params[1]).doubleValue();   
//         case 1:
//          if(params[0]==null)
//              mu=0.0;
//          else
//              mu = ((Number) params[0]).doubleValue();
//         }
// }

// public boolean isValid(Object var )
// {
//     boolean flag=true;
//     double val=0.0;
    
//     try 
//         {
//          val = ((Number) var).doubleValue();
//         }
//     catch ( Exception e )
//         {
//          flag = false;
//         }
    
//     if (Double.isNaN(val))
//      flag = false;
    
//     return flag;
// }

// public boolean[] isValid(Object[] var )
// {
//     boolean[] retval = new boolean[var.length];
//     for(int i=0; i<var.length; i++)
//     retval[i] = isValid(var[i]);
//     return retval;
// }


// line 67 "MVNormal.jweb"
    
// line 678 "MVNormal.jweb"
public Object generate()
{
    return rnorm();
}

public Object[] generateSeveral( int howmany )
{ 
    Object[] retval = new Object[howmany];
    for(int i=0; i < howmany; i++)
        retval[i] = generate();
    return retval;
}


// line 68 "MVNormal.jweb"
    

// line 561 "MVNormal.jweb"
public double PDF( Object value )
{
    double[] x = ArrayTools.Otod( value );
    return PDF( x );
} 


public double logPDF( Object value )
{
    double[] x = ArrayTools.Otod( value );
    return logPDF( x );
}
// line 69 "MVNormal.jweb"
    

// line 578 "MVNormal.jweb"
public double unnormalizedPDF( Object value )
{
    double[] x = ArrayTools.Otod( value );
    return unnormalizedPDF( x );
} 


public double logUnnormalizedPDF( Object value )
{
    double[] x = ArrayTools.Otod( value );
    double retval = logUnnormalizedPDF( x );
    return retval;
}


// line 70 "MVNormal.jweb"
    
// line 699 "MVNormal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: MVNorm [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho,rho},
                      {rho,1.0,rho,rho},
                      {rho,rho,1.0,rho},
                      {rho,rho,rho,1.0}}; 
    double[]   mean = {0.0,0.0,0.0,0.0};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    MVNormal example = new MVNormal(  mean, var, prng );

    for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
    {
        double[] tmp = (double[]) example.generate();

	for(int i=0; i<tmp.length; i++)
	    {
		System.out.print( tmp[i] + " " );
	    }
	System.out.println( example.dnorm(tmp) );
    }

	

// new double[4];
//
//          for(int i=0; i<tmp.length; i++)
//              {
//                  tmp[i] = 40.0 * prng.nextDouble() - 20.0;
//                  System.out.print( tmp[i] + " " );
//              }
//
//          System.out.print( ":  PDF = " + example.PDF(tmp) );
//          System.out.print( " unnormPDF = " + example.unnormalizedPDF(tmp) );
//          System.out.print( " logPDF = " + example.logPDF(tmp) );
//          System.out.print( " log_unnormPDF = " + example.logUnnormalizedPDF(tmp) );
//          System.out.println();
//     }
}


// line 72 "MVNormal.jweb"
}

