//package org.omegahat.Simulation.MCMC.Examples;
//import org.omegahat.Simulation.MCMC.*;
//import org.omegahat.Simulation.MCMC.Proposals.*;
//import org.omegahat.Simulation.MCMC.Listeners.*;
//import org.omegahat.Simulation.RandomGenerators.*;
//import org.omegahat.Probability.Distributions.*;
//
//import birc.grni.gui.GrnDbn;
//import birc.grni.gui.GrnDbnDisplay;
//
//import java.lang.*;
//import java.io.*;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
////import javax.swing.JProgressBar;
//import javax.swing.SwingWorker;
//
//public class Binomial_HDBNf extends SwingWorker<Void, Void> {
//	
//   public static int data[][];
//   public static Hashtable mlchash;
//   public static SymmetricProposal proposal;
//   public static UnnormalizedDensity target;
//   
//   
//   protected PRNG prng;
//   protected double[] state;
//   public static int nogenes;
//   public static int iteration;
//   public static int finalNetwork [];
//   public int dbnNetwork [][];
//   int orderDbn =1;
//   int totalIterations;
//   int numPoints;
//   int current;
//   boolean priorSet = false;
//   int beta = 0;
// 
//   String inputFileData, resultFullPath;
//   String priorFile;
//   private static Logger logger = Logger.getLogger(Binomial_HDBNf.class.getName());
//   //public static int timeseries=3;
//   //static String input_path = "E:\\Epigenetic project\\Yeast_S\\MCMC_win\\";
//
//   //Constructor with out prior info
//   public Binomial_HDBNf(int genes,String inputFile, int iterations ) {
//	// TODO Auto-generated constructor stub
//	   nogenes = (orderDbn +1 )*genes;
//	   inputFileData = inputFile;
//	   //resultFullPath = resultPath;
//	   this.totalIterations =iterations;
//	   iteration=0;
//	   current=0;
//	   GrnDbnDisplay.progressBarDbn.setMaximum(totalIterations);
//	  // logger.log(Level.INFO, "inside Hdbn constructor");
//	   dbnInitialize();
//   }
//   
//   public Binomial_HDBNf(int genes,String inputFile ,int iterations, int beta, String priorFile ) {
//		// TODO Auto-generated constructor stub
//		   nogenes = (orderDbn + 1 )*genes;
//		   inputFileData = inputFile;
//		   //resultFullPath = resultPath;
//		   this.totalIterations = iterations;
//		   iteration=0;
//		   current=0;
//		   priorSet=true;
//		   this.beta = beta;
//		   this.priorFile = priorFile;
//		   GrnDbnDisplay.progressBarDbn.setMaximum(totalIterations);
//		  // logger.log(Level.INFO, "inside Hdbn constructor");
//		   dbnInitialize();
//	   }
//   
//   public void dbnInitialize(){
//	   CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
//	   prng = new CollingsPRNG( a.registerPRNGState() );
//	    
//	   mlchash = new Hashtable(); 
//	   logger.log(Level.INFO, "before target intialization");
//	   if(priorSet){
//		//   target = new Binomial_HDBNmlf(nogenes, orderDbn, totalIterations, inputFileData, beta, priorFile);
//	   }else{
//		//   target = new Binomial_HDBNmlf(nogenes, orderDbn, totalIterations,inputFileData);
//	   }
//	   
//	   logger.log(Level.INFO, "target is done");
//	   double var = 1;
//	   double[] diagVar = new double[nogenes*nogenes];
//
//	   for(int i=0; i<diagVar.length; i++) diagVar[i]=var;
//	   logger.log(Level.INFO, "before proposal intialization");
//	   proposal =  new Binomial_HDBNprof(diagVar, orderDbn, prng, totalIterations);
//	   logger.log(Level.INFO, "proposal is done");
//	   state = new double[nogenes*nogenes];
//	    for(int i=0; i<nogenes; i++){
//	    	for(int j=0; j<nogenes; j++){
//	    		 state[i*nogenes+j]=0;
//	    	}
//	    }
//	    
//	   int actualGenes = nogenes/(orderDbn +1);
//	   finalNetwork = new int[actualGenes*actualGenes];
//	   for(int i=0; i<actualGenes; i++){
//	    	for(int j=0; j<actualGenes; j++){
//	    		 state[i*actualGenes+j]=0;
//	    	}
//	    }
//	    logger.log(Level.INFO, "dbn Initialization is done");
//	          
//	          
//   }
//   
//   /*public void dbnRun(){
//	    
//	    CustomMetropolisHastingsSampler mcmc = 
//			       new CustomMetropolisHastingsSampler(state, target, proposal, 
//								   prng, true);    
//
//			    //ListenerWriter l2 = new PosteriorProbWriter(input_path + "outputs\\MCMC.output"+rnum);
//			   // MCMCListenerHandle lh2 = mcmc.registerListener(l2);
//	    logger.log(Level.INFO, "before calling mcmc");
//			     mcmc.iterate(totalIterations);
//	    
//   }*/
//
//   @Override
//	protected Void doInBackground() throws Exception {
//	// TODO Auto-generated method stub
//		CustomMetropolisHastingsSampler mcmc = new CustomMetropolisHastingsSampler(state, target, proposal,prng, true);
//			 
//		logger.log(Level.INFO, "before calling mcmc");
//		//mcmc.iterate(totalIterations);
//		
//		while(current <= totalIterations){
//			mcmc.step();
//			//Thread.sleep(10);
//			GrnDbnDisplay.progressBarDbn.setValue(current);
//			current++;
//		}
//		return null;
//	}
// 
//   protected void done(){
//	   logger.log(Level.INFO, "Background thread is finished");
//	   // convert final network in to the standard format
//	   int genes = nogenes/(orderDbn +1 );
//	   dbnNetwork = new int[genes][genes];
//	   for(int m=0; m<genes; m++){
//		   for(int n=0; n<genes; n++){
//			   
//			   if(finalNetwork[m*genes +n] == 1){
//				   dbnNetwork[m][n]=1;
//          
//			   }
//			   else{
//				   dbnNetwork[m][n]=0;
//			   }
//			  
//		   }
//	   }
//	   //GrnDbn.dbnResultPrinter(finalNetwork, nogenes/(orderDbn +1 ));
//	   GrnDbn.dbnResultPrinter(dbnNetwork, genes);
//	}
//
//	
//	
//
//}
// 
