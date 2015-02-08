//package org.omegahat.Simulation.MCMC.Examples;
//
//import java.lang.*;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.io.*;
//import birc.grni.dbn.*;
//import oracle.jdbc.ttc7.Odscrarr;
//
//import org.omegahat.GUtilities.ArrayTools;
//import org.omegahat.Probability.Distributions.UnnormalizedDensity;
//import edu.ksu.cis.kdd.data.Table;
//import edu.ksu.cis.bnj.bbn.learning.scorebased.gradient.GreedySL;
//
//public class Binomial_HDBNmlfNoGUI implements UnnormalizedDensity
//{
//  static int itrno =0; 
//  static boolean gibbsEnable =false;
//  static int gibbs =0;
//  public static double gibbs_prior =0;
//  String [] cell;
//  //public static double print_log_accept=0;
//  static int beta;
//  static int k_level=3;
//  static int nogenes=0,order=0,randn=0;
//  static int network[],edge=0;
//  static int dag[][], data[][];// alphaa[][];
//  static Hashtable numbers = new Hashtable(), numberst = new Hashtable(), numberst2 = new Hashtable();
//  double ansprev=0;
//  
//  static Table tuples;
//  private static Logger logger =Logger.getLogger(Binomial_HDBNmlfNoGUI.class.getName());
//  static double sigma1 = 1, sigma2 = 20;
//  static double[] convg, target, avgdag; 
//  static String result_path ;
//  int[] avgdag_net;
//  
//  double bic() {
//    //Binomial_HDBNf.iteration++;
//	  DynamicBayesianNetworkNoGUI.iteration++;
//    // ouput graph     
//    double ans = 0;
//    dag = new int[nogenes][nogenes];
//  
//  try{  
//       for(int i=0; i<nogenes; i++)
//         for(int j=0; j<nogenes; j++)
//                           dag[i][j] = network[i*nogenes+j];
//
//      ans = bic2(dag);
//
//       String dags = ans+" ";
//   
//    if(DynamicBayesianNetworkNoGUI.iteration > itrno){
//    	
//       for(int i=0; i<nogenes; i++)
//         for(int j=0; j<nogenes; j++)
//                           avgdag[i*nogenes+j] += (double)network[i*nogenes+j];
//    }
//    
//	
//      
//    /*  if(Binomial_HDBNf.iteration == (itrno*2-1))
//      {
//       int cnt = 1;
//       try {
//    	   
//    	   FileWriter os2 = new FileWriter(result_path , true);
//    	   for(int i=0; i<nogenes; i++){
//    	         for(int j=0; j<nogenes; j++){
//    	        	 		
//    	        	 			//String value = Double.toString(avgdag[i*nogenes+j]);
//    	        	 		
//    	                        avgdag[i*nogenes+j]/=itrno;		
//    	                       
//    	         }
//    	          
//    	          }
//    	   for(int i=0; i<nogenes/(order+1); i++){
//    		   for(int j=0; j<nogenes/(order+1); j++){
//    			   
//    			   if(avgdag[i*nogenes + (j+(nogenes/(order+1)))] > 0.5)
//                       os2.write("1"+"  ");
//                       else
//                       os2.write("0"+"  ");
//    		   }
//    		 
// 	          os2.write("\n");
//    	   }
//    	  
//    	       os2.close();
//	} catch (IOException e) {
//		// TODO: handle exception
//	}
//          
//
//      } // end of if  */
//    
//    if(DynamicBayesianNetworkNoGUI.iteration == (itrno*2 - 1)){
//    	   for(int i=0; i<nogenes; i++){
//    		   
//  	         	for(int j=0; j<nogenes; j++){
//  	         		
//  	         		avgdag[i*nogenes+j]/=itrno;
//  	         	}
//    	   }
//    	   int actualgenes=nogenes/(order+1);
//    	   int m=0; int n=0; 
// 	       for(m=0; m<nogenes/(order+1); m++){
//    		   for( n=0; n<nogenes/(order+1); n++){
//    			   
//    			   if(avgdag[m*nogenes + (n+(nogenes/(order+1)))] >= 0.5){
//    				   //Binomial_HDBNf.finalNetwork[m*actualgenes + n]=1;
//    				   DynamicBayesianNetworkNoGUI.finalNetwork[m*actualgenes + n]=1;
//    				//os1.write("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
//    			   }
//    			   
//    		   }
// 	          
//    	   }
// 	       
// 	       /*
// 	       /// result writing verification
// 	      FileWriter os2 = new FileWriter("E:\\soft_project\\TestData\\results\\resutHard.txt" , true);
// 	     for(int i=0; i<nogenes/(order+1); i++){
//  		   for(int j=0; j<nogenes/(order+1); j++){
//  			   
//  			   if(avgdag[i*nogenes + (j+(nogenes/(order+1)))] > 0.5)
//                     os2.write("1"+"  ");
//                     else
//                     os2.write("0"+"  ");
//  		   }
//  		 
//	          os2.write("\n");
//  	   }
//  	  
//  	       os2.close(); */
//    }
//  	         			           	          	           
//  }
//  catch(Exception e){}
//
//  return ans;
//  }
//
// 
//  public static double bic2(int[][] dag){
//
//     double ans = 0;
//
//     try{
//     numberst2 = new Hashtable();
//     for(int i=0; i<nogenes; i++){
//          ans+=getNodePotential(i,dag);
//     } 
//
//       System.out.println(ans+"all");
//
//      if(DynamicBayesianNetworkNoGUI.iteration%2==0)convg[DynamicBayesianNetworkNoGUI.iteration/2-1]=ans;
//
//      numberst = new Hashtable(); 
//      Enumeration e = numberst2.keys();
//       while(e.hasMoreElements())
//       {
//        String keys = (String)e.nextElement();
//        Double score = (Double)numberst2.get(keys);
//        numberst.put(keys,new Double(score));
//       }
//      }
//     catch(Exception e){}   
//     // add gibbs
//     double sumg = 0;
//     if(gibbsEnable){
//	     for(int i=0; i<nogenes; i++)
//	       for(int j=0; j<nogenes; j++)
//	         sumg += Math.abs(target[i*nogenes+j]-dag[i][j]);
//	     
//	     gibbs_prior=sumg*beta*(-1);
//	     //System.out.println("Beta is - " + beta);
//     }
//     else{
//    	 gibbs_prior=0;
//     }
//
//     //return ans+(sumg*(-1)); 
//     //add by me
//    // double ans_new = Math.log(ans)+ (sumg*(-1));
//     return ans;
//  }
//
//  // Constructor //
//  /*public Binomial_HDBNmlf(int n, int ord, int rnum, int itr, String inputfile, int gib) 
//  {
//   nogenes = n; order = ord; randn = rnum; 
//   network = new int[nogenes*nogenes];
//   avgdag = new double[nogenes*nogenes];
//   target = new double[nogenes*nogenes];
//   gibbs = gib;
//   //// add by kasun
//	System.out.println("inside constructor - " + randn);
//	
//	///// end
//	
//   //data = new int[nogenes][data1[0].length]; 
//   itrno = itr;
//   convg = new double[itrno];
//
//   tuples = Table.load(inputfile);
//
//   // read in gibbs
//   if(gibbs == 1 ){
//    // read in the structure
//    try{
//        Random r = new Random();
//        FileReader fr = new FileReader("D:\\Epigenetic project\\Yeast_S\\10G\\1\\prior_artificial.txt");
//      
//        
//        BufferedReader br = new BufferedReader(fr);
//        String s = "";
//        int j=0;
//        s=br.readLine();
//        String[] cell = s.split(",");
//        do
//        {
//                cell = s.split(",");
//                for(int i=nogenes/2; i<nogenes; i++)
//                {
//                	
//                	double y1 = Double.parseDouble(cell[i-nogenes/2]);
//                     target[j*nogenes+i]=y1;
//                    		 
//                     //if(r.nextFloat()>0.5)target[j*nogenes+i]=0; 
//                      
//             }
//                j++;
//                
//				
//        }
//        while((s=br.readLine())!=null);
//    }
//    catch(Exception e){}
//   }
//
//  }*/
//
//  // new constructor without prior
//  public Binomial_HDBNmlfNoGUI(int n, int ord,  int itr, int[][] inputData) 
//  {
//   nogenes = n; order = ord;  
//   network = new int[nogenes*nogenes];
//   avgdag = new double[nogenes*nogenes];
//   target = new double[nogenes*nogenes];
//  // result_path=resultPath;
//   
//  
//	
//   //data = new int[nogenes][data1[0].length]; 
//   itrno = itr;
//   convg = new double[itrno];
//
//  // tuples = Table.load(inputfile,"arff");
//   tuples = Table.createTableFromArray(inputData);
//   logger.log(Level.INFO, "Tuples loading done");
//  }
//  
//  // new constructor with prior
//  public Binomial_HDBNmlfNoGUI(int n, int ord,  int itr, int[][] inputData, int betaValue, String priorFile) 
//  {
//	  
//	   nogenes = n; order = ord;  
//	   network = new int[nogenes*nogenes];
//	   avgdag = new double[nogenes*nogenes];
//	   target = new double[nogenes*nogenes];
//	
//	   this.gibbsEnable=true;
//	   this.beta = betaValue;
//	   this.gibbs=1;
//		
//	   //data = new int[nogenes][data1[0].length]; 
//	   itrno = itr;
//	   convg = new double[itrno];
//	
//	  // tuples = Table.load(inputfile,"arff");
//	   tuples = Table.createTableFromArray(inputData);
//	   logger.log(Level.INFO, "Tuples loading done");
//	   
//	   // read prior file
//	   try {
//		   
//			FileReader fr = new FileReader(priorFile);
//			BufferedReader br = new BufferedReader(fr);
//			String line =null;
//			int j=0;
//			while((line=br.readLine())!=null){
//				
//				cell = line.split(",");
//                for(int i=nogenes/2; i<nogenes; i++)
//                {
//                	double y1 = Double.parseDouble(cell[i-nogenes/2]);
//                    target[j*nogenes+i]=y1;
//                      
//                }
//                j++;
//			}
//			br.close();
//		
//	   } catch (FileNotFoundException e) {
//		   logger.log(Level.SEVERE, " Prior file is not found", e);
//			e.printStackTrace();
//	   } catch (IOException e) {
//		   logger.log(Level.SEVERE, "IO exception during prior file reading - " + e);
//		   e.printStackTrace();
//	   }
//	   logger.log(Level.INFO, "prior data reading is completed" + Arrays.toString(target));
//   
//  }
//  // Log Unnormalized Density //
//  public double logUnnormalizedPDF( Object params)  {
//     double[] param = ArrayTools.Otod( params );
//   // System.out.println("start");
//     edge = 0;
//     for(int i=0; i<nogenes*nogenes; i++)
//     {    network[i] =(int)param[i];
//          if(network[i]>0)edge++; 
//     //    System.out.println(network[i]+" "+param[i]);
//     }//System.out.println("calling bic \n");
//   // System.out.println("end");
//    return bic();
//  }
//
//  
//  // Unnormalized Density //
//  public double unnormalizedPDF( Object paramObj ) {  
//       return(logUnnormalizedPDF(paramObj));
//  }
//
//  public static double getNodePotential(int nNode, int[][] dag)
//    {
//                // determine cardinality of parent set & reserve space for frequency counts
//         int[] parents = new int[1];
//        int pcnt = 0;
//
//        String score1=""+nNode, score2;
//        double ans = 0; 
//        for(int i=0; i<dag.length; i++)
//        {
//                if(dag[nNode][i]>0 && dag[nNode][i]<= order)
//                {
//                    if(pcnt>0)parents=(int[])resizeArray(parents,parents.length+1);
//                        parents[parents.length-1]=i;
//               //         score1+="-"+i+"-"+dag[nNode][i];  
//                        score1+="-"+i;
//                        pcnt++;
//                }
//
//        }
//        int numValue = k_level;
//
//        try{
//        ans = 1;
//        Random r = new Random();
//        ans = (Double)numberst.get(score1);
//        numberst2.put(score1,new Double(ans));  
//        if(ans < 0)return ans;
//        }
//        catch(Exception e){}
//
//        try{
//        ans = 1;
//        Random r = new Random();
//        ans = (Double)numbers.get(score1);
//        numberst2.put(score1,new Double(ans));
//        if(ans < 0)return ans;
//        }
//        catch(Exception e){}
//
//
//         GreedySL greedylearner = new GreedySL(tuples);
//         if(gibbs == 2) ans = greedylearner.getbdeg(nNode, dag, nogenes);
//         else if(gibbs == 2) ans = greedylearner.getbdegs(nNode, dag, nogenes);
//         else ans = greedylearner.getbde(nNode, dag, nogenes);
//         
//         //if(gibbs == 2) ans = greedylearner.getbdeg(nNode,dag,nogenes);
//         //else if(gibbs == 3) ans = greedylearner.getbdegs(nNode,dag,nogenes);
//         //else  ans = greedylearner.getbde(nNode,dag,nogenes);   
//         //ans = greedylearner.getbic(nNode,dag,nogenes);          
//         // System.out.println(ans);
//         numberst2.put(score1,new Double(ans));
//         numbers.put(score1,new Double(ans));
//  
//               // System.out.println(nNode+" "+ans);
//                return ans;
//        }
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
//}