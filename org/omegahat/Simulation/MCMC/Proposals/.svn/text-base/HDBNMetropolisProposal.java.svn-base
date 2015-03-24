

// line 197 "NormalMetropolisProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 203 "NormalMetropolisProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
// //  import Jama.*;
  import org.omegahat.GUtilities.ArrayTools;
  import java.io.*;

// line 10 "NormalMetropolisProposal.jweb"
public class HDBNMetropolisProposal extends NormalProposal implements SymmetricProposal
{

    
// line 25 "NormalMetropolisProposal.jweb"
public boolean DEBUG = false;
int nogenes = 0, order = 0;
double mi[][][];

// line 14 "NormalMetropolisProposal.jweb"
    
// line 30 "NormalMetropolisProposal.jweb"
    
/* inherited */

// line 15 "NormalMetropolisProposal.jweb"
    
// line 38 "NormalMetropolisProposal.jweb"
/** Constructor for normal increments with identity covariance matrix.
 * @param length number of dimensions
**/
public HDBNMetropolisProposal( int length, PRNG prng )
{
  super(length, prng);
  double[] mu = new double[length];
  double[][] sigma  = new double[length][length];

  for(int i=0; i < length; i++)
    {
      mu[i] = 0.0;
      for(int j=0; j < length; j++)
          sigma[i][j] = (i==j)?1.0:0.0;
    }
  
  setMean(mu);
  setCovariance(sigma);
  checkConformity();
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public HDBNMetropolisProposal( double[][] var, int ord, PRNG prng)
{
  super(var.length, prng);
  double[] mu = new double[var.length];

  for(int i=0; i < var.length; i++)
    {
      mu[i] = 0.0;
    }
  
  setMean(mu);
  setCovariance(var);
  checkConformity();

  nogenes = var.length;
  order = ord;
  mi = new double[nogenes][nogenes][order];
  // read in mutual information

 try{
   BufferedReader in   = new BufferedReader(new FileReader("Chua_mi"));
       for(int i=0; i<nogenes; i++)
         for(int j=i; j<nogenes; j++)
           for(int l=0; l<order; l++){
                   mi[j][i][l]=Double.parseDouble(in.readLine());
                   mi[i][j][l]=Double.parseDouble(in.readLine());
//              System.out.println(mi[i][j][l]);
           }
    }
  catch(Exception e){}


}

/** Constructor for a spherical normal increments with variances for each dimension.  
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0)
**/
public HDBNMetropolisProposal( int length, double var, PRNG prng)
{
  this(length, prng);
  double[][] sigma  = new double[length][length];
  
  for(int i=0; i < length; i++)
  for(int j=0; j < length; j++)
    {
      sigma[i][j] = (i==j)?var:0.0;
    }
  
  setCovariance(sigma);
  checkConformity();

}


// line 16 "NormalMetropolisProposal.jweb"
    
// line 104 "NormalMetropolisProposal.jweb"
public Object generate( Object center )
{
    double[] mean = ArrayTools.Otod( center );

    setMean(mean);
    return super.generate();
}


// line 17 "NormalMetropolisProposal.jweb"
    
// line 135 "NormalMetropolisProposal.jweb"
public double conditionalPDF( Object state, Object conditionals ) 
{
	setMean(conditionals);
	return super.conditionalPDF(state, conditionals);
}

public double logConditionalPDF( Object state, Object conditionals ) 
{
	setMean(conditionals);
	return super.logConditionalPDF(state, conditionals);
}


// line 18 "NormalMetropolisProposal.jweb"
    
// line 117 "NormalMetropolisProposal.jweb"
    public double transitionProbability( Object from, Object to )
    {
        double[] x = ArrayTools.Otod( to );
        double[] mean = ArrayTools.Otod( from );

        double proposal = 1.0;

        // add proposal probability
        for(int i =0; i < x.length; i++)
        {
            int num1 =(int) x[i]*order;
            int num2 =(int) mean[i]*order;
            if(num1 != num2){
            int indexj = i%nogenes;
            int indexi = i/nogenes;
             // calculate z
            int cnt = 0;
            for(int j=((int)Math.abs(x[indexi*nogenes+indexj]))%order;j<order;j++)
            {
             if(mi[indexi][indexj][j]>0.1)cnt++;
            }
            //System.out.println("cnt is " + cnt+"order is "+order);
            proposal = ((double)(order - cnt))/order;
            // check num of parents of i
            }
            if(num1>num2){proposal=1/proposal; break;}
        }

        setMean(from);
        return super.transitionProbability( from, to )*proposal;
    }

    public double logTransitionProbability( Object from, Object to )
    { 
      return Math.log(transitionProbability(from,to));
    }
    

// line 19 "NormalMetropolisProposal.jweb"
    
// line 154 "NormalMetropolisProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: NormalMetropolisProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho},{rho,1.0,rho},{rho,rho,1.0}}; 
    //    double[]   mean = {0.0,0.0,0.0};
    Double[]   start = new Double[]{ new Double(0.),
                                     new Double(0.),
                                     new Double(0.)};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    NormalMetropolisProposal example = new NormalMetropolisProposal( var, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         double[] retval = (double[]) example.generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i];
             }

         System.out.println( example.PDF( retval ) );
     }

}

// line 20 "NormalMetropolisProposal.jweb"
}
