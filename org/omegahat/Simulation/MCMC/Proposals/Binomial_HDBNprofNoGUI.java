//
//
//// line 265 "NormalMetropolisComponentProposal.jweb"
//  package org.omegahat.Simulation.MCMC.Proposals;
//
//
//// line 271 "NormalMetropolisComponentProposal.jweb"
//    import org.omegahat.Simulation.MCMC.*;
//
//  import org.omegahat.Probability.Distributions.*;
//import org.omegahat.Simulation.RandomGenerators.*;
//import org.omegahat.GUtilities.ArrayTools;
//import org.omegahat.Simulation.MCMC.Examples.*;
//import java.io.*;	
//import java.lang.*;
//import java.util.*;
//
//
//// line 11 "NormalMetropolisComponentProposal.jweb"
//public class Binomial_HDBNprofNoGUI  
//	implements GeneralProposal,
//		   SymmetricProposal, 
//		   TimeDependentProposal
//		
//{
//
//    
//// line 32 "NormalMetropolisComponentProposal.jweb"
//protected PRNGDistributionFunctions  prob;
//
//protected boolean DEBUG = false;
//protected double  sigma[];
//
//protected int     time  = 0;
//protected int     which = 0;
//
//int nogenes = 0, order = 0, rno = 1;
//int maxitr = 0;
//boolean path[][];
//int[][] dagglob;
//int iterationcuttoff=0;
//
//public static final int 
//		MAXNUMCHANGES = 10,		
//		REVERSED = 1,
//		REMOVED = 2,
//		ADDED = 3,
//		NA = -1;
//
//protected int 
//		numOfNodes,
//		numOfChanges = 1,
//		totalNumOfChanges = 0;	
//	protected double 
//		tempScore = 0,
//		previousScore = -10000,
//	  maxScore = -10000;
//	protected int recentChange;  
//	public static final boolean CYCLE  =true;
//	public boolean containsCycle = false;
//
//// line 19 "NormalMetropolisComponentProposal.jweb"
//    
//// line 44 "NormalMetropolisComponentProposal.jweb"
//
//public void setCovariance( double[][] cov )
//{
//  if( cov.length != sigma.length ) 
//	throw new RuntimeException( "Specified covariance has incorrect number of dimensions: Length is " + 
//	                             cov.length + " but should be " + sigma.length );
//
//  for(int i=0; i < sigma.length; i++)
//	sigma[i] = Math.sqrt(cov[i][i]);
//
//}	
//
//
//public void setCovariance( double[] cov )
//{
//  if( cov.length != sigma.length ) 
//	throw new RuntimeException( "Specified covariance has incorrect number of dimensions: Length is " + 
//	                             cov.length + " but should be " + sigma.length );
//
//  for(int i=0; i < sigma.length; i++)
//	sigma[i] = Math.sqrt(cov[i]);
//}	
//
//
//// line 20 "NormalMetropolisComponentProposal.jweb"
//    
//// line 73 "NormalMetropolisComponentProposal.jweb"
///** 
// * Constructor for normal increments with identity covariance matrix.
// * @param length number of dimensions
//**/
//public void NormalMetropolisComponentProposal( int length, PRNG prng )
//{
//  sigma = new double[length];
//  for(int i=0; i < length; i++)
//	sigma[i] = 1.0;
//
//  prob     = new PRNGDistributionFunctions(prng);
//
//}
//
///** 
// *  Constructor for normal increments with specified covariance
// *  matrix. Note that off diagonal elements will be ignored.
// * 
// *  @param var   variance matrix
//**/
//
////constructor
///*public Binomial_HDBNprof( double[] var, int ord, int rnum, PRNG prng, int itr)
//{
//  nogenes = (int)Math.sqrt(var.length);
//  maxitr = itr;
//  //maxpcnt = nogenes;
//  order = ord;
//  rno = rnum;
//  path = new boolean[nogenes][nogenes];
//  dagglob = new int[nogenes][nogenes];
//
//  sigma = new double[var.length];
//  for(int i=0; i < var.length; i++)
//	sigma[i] = Math.sqrt(var[i]);
//
//  prob = new PRNGDistributionFunctions(prng);
//}*/
//
//// new constructor
//public Binomial_HDBNprofNoGUI( double[] var, int ord, PRNG prng, int itr)
//{
//nogenes = (int)Math.sqrt(var.length);
//maxitr = itr;
////maxpcnt = nogenes;
//order = ord;
//
//path = new boolean[nogenes][nogenes];
//dagglob = new int[nogenes][nogenes];
//
//sigma = new double[var.length];
//for(int i=0; i < var.length; i++)
//	sigma[i] = Math.sqrt(var[i]);
//
//prob = new PRNGDistributionFunctions(prng);
//}
//
//// line 21 "NormalMetropolisComponentProposal.jweb"
//    
//// line 153 "NormalMetropolisComponentProposal.jweb"
//// These really belong somewhere else!
//
//protected double normalPDF( double x, double mu, double sigma )
//{
//    return 1. / ( Math.sqrt(2 * Math.PI) * sigma ) * Math.exp( - 1.0/2.0 * (x-mu) * (x-mu) / ( sigma * sigma ) );
//}
//
//protected double normalLogPDF( double x, double mu, double sigma )
//{
//    return Math.log( 1. / ( Math.sqrt(2 * Math.PI) * sigma) ) +  -1.0/2.0 * (x-mu) * (x-mu) / ( sigma * sigma );
//}
//
//// line 22 "NormalMetropolisComponentProposal.jweb"
//    
//// line 106 "NormalMetropolisComponentProposal.jweb"
//public void timeInc()
//{
//    time++;
//    which++;
//    if(which >= sigma.length) which = 0;
//
//    if(DEBUG) System.err.println("Time Incremented to " + time + " Which now " + which );
//
//}
//
//public void resetTime()
//{
//    time = -1;
//    timeInc();
//}
//
//public int getTime()
//{
//    return time;
//}
//
//
//// line 23 "NormalMetropolisComponentProposal.jweb"
//    
//// line 132 "NormalMetropolisComponentProposal.jweb"
//public Object generate( Object center )
//{
//    double[] mean   = ArrayTools.Otod( center );
//    double[] retval = new double[mean.length];
//
//    int pcnt = 0;
//    
//    Random r2 = new Random();
//    r2.setSeed(System.currentTimeMillis());
//    which = r2.nextInt(nogenes*nogenes);
//    which = which%nogenes;
//    for(int x=0; x<nogenes; x++)
//                  for(int y=0; y<nogenes; y++)
//                           dagglob[x][y] = (int)mean[x*nogenes+y];
// 
//    learnGraph();
//    
//    for(int x=0; x<nogenes; x++){
//                  for(int y=0; y<nogenes; y++)
//                         {     
//                              retval[x*nogenes+y]=dagglob[x][y];
//                          }
//      } 
//
//    return retval;
//}
//
//// line 24 "NormalMetropolisComponentProposal.jweb"
//    
//// line 212 "NormalMetropolisComponentProposal.jweb"
//public double conditionalPDF( Object state, Object conditionals ) 
//{
//	return transitionProbability( conditionals, state );
//}
//
//
//
//public double logConditionalPDF( Object state, Object conditionals )
//{
//	return logTransitionProbability( conditionals, state );
//}
//
//// line 25 "NormalMetropolisComponentProposal.jweb"
//    
//// line 169 "NormalMetropolisComponentProposal.jweb"
//    public double transitionProbability( Object from, Object to )
//    {
//        double[] x = ArrayTools.Otod( to );
//        double[] mean = ArrayTools.Otod( from );
//        int spuc = 0;
//	double retval = 1.0, nhood1 = 1.0, nhood2 = 1.0;
//        double proposal = 1.0;
//        int pcnt = 0;  
//        double a = 0, b = 0;
//        
//        retval = 1;
//
//        // prior on dbn 
//           double q = 5.0/(nogenes);
//           q = 0.3; 
//           if(recentChange == ADDED) retval = q/(1-q);
//           if(recentChange == REMOVED) retval =(1-q)/q;
//
//        retval *= nhood1/nhood2;            
//
//            return retval;
//    }
//
//    public double logTransitionProbability( Object from, Object to )
//    { 
//	return Math.log(transitionProbability(from,to));	
//  
//    }
//    
//
//// line 26 "NormalMetropolisComponentProposal.jweb"
//    
//// line 230 "NormalMetropolisComponentProposal.jweb"
//static public void main( String[] argv )
//{
//    if(argv.length < 3) 
//    {
//        System.err.println("Usage: NormalMetropolisComponentProposal [numSamples] [offDiagonalCovariance] [debug]");
//        return;
//    }
//
//    double[] var = {1.0,2.0,3.0};
//
//    double[]   start = new double[]{ 0., 0., 0. };
//    
//    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
//    
//    NormalMetropolisComponentProposal example = new NormalMetropolisComponentProposal( var, prng );
//
//    for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
//     {
//         Double[] retval = (Double[]) example.generate( start );
//         double[] tmp = new double[3];
//
//         for(int i=0; i<retval.length; i++)
//             {
//                 System.out.print( retval[i] + " " );
//                 tmp[i] = retval[i].doubleValue();
//             }
//
//     }
//
//}
//
//static double logGamma(double x) {
//      double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
//      double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
//                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
//                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
//      return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
//   }
//
//   static double gamma(double x) { return Math.exp(logGamma(x)); }
//
//private boolean isCyclic()
//	{
//                if(true)return false;
//
//		boolean cycleFound = false;
//
//                for (int i = 0 ; i < nogenes; i++)
//                 for (int j =0; j < nogenes; j++)
//                          path[i][j]=false;
//      		
//		findClosure();
//		for (int i = 0; i < nogenes; i++)
//		{
//			if (path[i][i])
//			{
//                		cycleFound = true;
//				break;
//			}
//		}
//		return cycleFound;
//	}
//	
//
//	private void findClosure()
//	{
//		for (int i = 0 ; i < nogenes; i++)
//		{
//                              int[] parents = new int[1];
//                              int pcnt = 0;
//                              for(int p=0; p<nogenes; p++)
//                              {
//                               if(dagglob[i][p]>0)
//                               {
//                                  if(pcnt>0)parents=(int[])resizeArray(parents,parents.length+1);
//                                  parents[parents.length-1]=p;
//                                  pcnt++;
//                               }
//                              }
//
//			for (int j =0; j < nogenes; j++)
//			{			
//		                path[i][j] = false;
//		                int test = Arrays.binarySearch(parents,j); 
//                		if(test>0)path[i][j] = true;
//			}	
//		}
//			
//		for (int k = 0; k < nogenes; k ++)
//		{
//			for (int i = 0; i < nogenes; i++)
//			{
//				if (path[i][k])
//				{		
//					for (int j = 0; j < nogenes; j++)				
//						path[i][j] = path[i][j]  ||  path [k][j] ;
//				}		
//			}
//		}
//	}
//
//
//  public static Object resizeArray (Object oldArray, int newSize)
//        {
//         int oldSize = java.lang.reflect.Array.getLength(oldArray);
//         Class elementType = oldArray.getClass().getComponentType();
//         Object newArray = java.lang.reflect.Array.newInstance( elementType,newSize);
//         int preserveLength = Math.min(oldSize,newSize); if (preserveLength > 0)
//         System.arraycopy (oldArray,0,newArray,0,preserveLength);
//         return newArray;
//        }
//
// protected void learnGraph()
//	{
//                numOfNodes = nogenes;
//                numOfChanges=0;
//                Random r = new Random();
//		while (numOfChanges < 1)
//		{			
//			numOfChanges = 0;
//		        //int node = r.nextInt(nogenes);
//                        int node = r.nextInt(nogenes/(order+1));
//                        
//                //	for(int node = 0; node < numOfNodes; node++)
//			{ 
//                                
//                              int[] parents = new int[1];
//                              int pcnt = 0;
//                              for(int p=0; p<nogenes; p++)
//                              {
//                               if(dagglob[node][p]>0)
//                               {
//                                  if(pcnt>0)parents=(int[])resizeArray(parents,parents.length+1);
//                                  parents[parents.length-1]=p;
//                                  pcnt++;
//                               }
//                              }
//                         //int parent = r.nextInt(nogenes);
//                              //int parent =  r.nextInt(nogenes/(order+1));
//                         int parent = r.nextInt((order*nogenes)/(order+1))+nogenes/(order+1);
//         
//		//		for (int parent = 0; parent < numOfNodes; parent++)
//				{
//
//                                     //  System.out.println(node+" "+parent);
//					if (node == parent) continue;
//                                         if(node == parent%(nogenes/2))continue;
//                                       // if(node%(nogenes/2) == parent)continue; 
//					if (Arrays.binarySearch(parents,parent)>0)				
//						reverseOrRemoveEdge(node, parent);				
//					else{	
//                                              
////System.out.println(node+" "+parent+" "+mi[node][parent-nogenes/2][0]);
//                                              
////if(mi[node][parent-nogenes/2][0]>=th && parents.length < 12)  			
//                                        //if(parents.length < nogenes)
//                                        if(parents.length < nogenes)
//						addEdge(node, parent);
//                                         }
//                                         if(numOfChanges>=1)return;
//											
//				}		
//		}
//		
//                	totalNumOfChanges+= numOfChanges;
//                        //System.out.println(totalNumOfChanges);
//			if (totalNumOfChanges > MAXNUMCHANGES)
//				break;
//		}
//	}
//	
// private void addEdge(int node, int parent)
//	{
//		
//             previousScore = getScore(node, parent);
//            
//             dagglob[node][parent]=1;
//		if (!isCyclic())
//		{
//			tempScore = getScore(node, parent);
//                     //System.out.println(tempScore+" "+previousScore);
//			if (tempScore > previousScore)
//			{	
//		                 System.out.println("add");		
//				numOfChanges++; recentChange = ADDED; return;
//			}
//			
//			
//		}	
//             dagglob[node][parent]=0;
//	}
//	
// private void reverseOrRemoveEdge(int node, int parent)
//	{		
//		previousScore = getScore (node, parent);
//	//	reverseEdge(node,parent);		// node <----parent to node----->parent
//	//	if(!isCyclic())		
//	//		tempScore = getScore(node, parent);				
//	  //      else	
//	//	{	
//	//		reverseEdge(parent, node);
//			removeEdge(node,parent); // since edge is already inversed
//			tempScore = getScore(node, parent);
//	//	}
//              	
//		if (previousScore > tempScore)
//			undoChange(node, parent);
//		else		
//			{ numOfChanges++; recentChange = REMOVED; }
//			
//			
//	}
//	
//	private void reverseEdge (int node, int parent)
//	{
//                dagglob[node][parent]=0;
//                dagglob[parent][node]=1;
//		recentChange = REVERSED;
//	}
//	
//	private void removeEdge(int node, int parent)
//	{
//		dagglob[node][parent]=0;
//		recentChange = REMOVED;
//               System.out.println("remove");
//	}
//	
//	private void undoChange(int node, int parent)
//	{
//			if (recentChange == REVERSED)
//				reverseEdge(parent, node);             
//			if (recentChange == REMOVED)
//                                dagglob[node][parent]=1;
//	}
//
//        private double getScore(int i, int j)
//	{
//		double score = Binomial_HDBNmlfNoGUI.getNodePotential(i,dagglob);
//		score += Binomial_HDBNmlfNoGUI.getNodePotential(j,dagglob);
//                //System.out.println(i+" "+j+" "+score);
//		return score;
//                
//	}	
//
//// line 27 "NormalMetropolisComponentProposal.jweb"
//}
