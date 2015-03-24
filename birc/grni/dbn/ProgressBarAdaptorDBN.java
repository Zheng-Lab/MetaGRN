package birc.grni.dbn;
import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Examples.Binomial_HDBNmlf;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;

import birc.grni.gui.GrnDbn;
import birc.grni.gui.GrnDbnDisplay;
import birc.grni.util.InputData;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressBarAdaptorDBN extends SwingWorker<Void, Void> {
	
	public static int data[][];
	public static Hashtable mlchash;
	public static SymmetricProposal proposal;
	public static UnnormalizedDensity target;
	   
	protected PRNG prng;
	protected double[] state;
	public static int nogenes;
	public static int iteration;
	public static int finalNetwork [];
	public int dbnNetwork [][];
	int orderDbn =1;
	int totalIterations;
	int numPoints;
	int current;
	boolean priorSet = false;
	int [][] dbnInputData;
	int beta = 0;
	 
	String inputFileData, resultFullPath;
	String priorFile;
	private static Logger logger = Logger.getLogger(ProgressBarAdaptorDBN.class.getName());
	
	//ADD BY LIU:
	public static InputData inputData;	/* this field will be filled in readInput method of class DbnDiscretization*/
	
	//CHANGE BY LIU:
//	public ProgressBarAdaptorDBN(String inputFile ,int iterations) {
//		
//		inputFileData = inputFile;
//		DbnDiscretization dbnData = new DbnDiscretization(inputFile);
//		dbnData.discretize();
//		dbnData.convertToDbn();
//		dbnInputData =dbnData.dbnData;
//		nogenes = (orderDbn +1 )*dbnData.numGenes;
//		
//		//resultFullPath = resultPath;
//		this.totalIterations =iterations;
//		iteration=0;
//		current=0;
//		GrnDbnDisplay.progressBarDbn.setMaximum(totalIterations);
//		
//	}
	
	public ProgressBarAdaptorDBN(String inputFile ,int iterations, boolean withHeader, boolean geneNameAreColumnHeader) {
		
		inputFileData = inputFile;
		DbnDiscretization dbnData = new DbnDiscretization(inputFile, withHeader, geneNameAreColumnHeader);
		dbnData.discretize();
		dbnData.convertToDbn();
		dbnInputData =dbnData.dbnData;
		nogenes = (orderDbn +1 )*dbnData.numGenes;
		
		//resultFullPath = resultPath;
		this.totalIterations =iterations;
		iteration=0;
		current=0;
		GrnDbnDisplay.progressBarDbn.setMaximum(totalIterations);
		
	}
	
	public void dbnMcmcWithOutPrior(){
		dbnInitialize();
	}
	
	public void dbnMcmcWithPrior(int beta, String priorFile){
		this.beta = beta;
		this.priorFile = priorFile;
		priorSet=true;
		dbnInitialize();
	}
	
	public void dbnInitialize() {
		CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
		prng = new CollingsPRNG(a.registerPRNGState());

		mlchash = new Hashtable();
		logger.log(Level.INFO, "before target intialization");
		if (priorSet) {
			target = new Binomial_HDBNmlf(nogenes, orderDbn, totalIterations,
					dbnInputData, beta, priorFile);
		} else {
			target = new Binomial_HDBNmlf(nogenes, orderDbn, totalIterations,
					dbnInputData);
		}

		logger.log(Level.INFO, "target is done");
		double var = 1;
		double[] diagVar = new double[nogenes * nogenes];

		for (int i = 0; i < diagVar.length; i++)
			diagVar[i] = var;

		logger.log(Level.INFO, "before proposal intialization");

		proposal = new Binomial_HDBNprof(diagVar, orderDbn, prng,
				totalIterations);
		logger.log(Level.INFO, "proposal is done");
		state = new double[nogenes * nogenes];
		for (int i = 0; i < nogenes; i++) {
			for (int j = 0; j < nogenes; j++) {
				state[i * nogenes + j] = 0;
			}
		}

		int actualGenes = nogenes / (orderDbn + 1);
		finalNetwork = new int[actualGenes * actualGenes];
		for (int i = 0; i < actualGenes; i++) {
			for (int j = 0; j < actualGenes; j++) {
				state[i * actualGenes + j] = 0;
			}
		}
		logger.log(Level.INFO, "dbn Initialization is done");

	}

	@Override
	protected Void doInBackground() throws Exception {
		
		// TODO Auto-generated method stub
		CustomMetropolisHastingsSampler mcmc = new CustomMetropolisHastingsSampler(state, target, proposal,prng, true);
		 
		logger.log(Level.INFO, "before calling mcmc");
		//mcmc.iterate(totalIterations);
		
		while(current <= totalIterations){
			mcmc.step();
			//Thread.sleep(10);
			GrnDbnDisplay.progressBarDbn.setValue(current);
			current++;
		}
		
		return null;
	}
	
	protected void done() {
		logger.log(Level.INFO, "Background thread is finished");
		// convert final network in to the standard format
		int genes = nogenes / (orderDbn + 1);
		dbnNetwork = new int[genes][genes];
		for (int m = 0; m < genes; m++) {
			for (int n = 0; n < genes; n++) {

				if (finalNetwork[m * genes + n] == 1) {
					dbnNetwork[m][n] = 1;

				} else {
					dbnNetwork[m][n] = 0;
				}

			}
		}
		// GrnDbn.dbnResultPrinter(finalNetwork, nogenes/(orderDbn +1 ));
		
		// CHANGE BY LIU:
//		GrnDbn.dbnResultPrinter(dbnNetwork, genes);
		GrnDbn.dbnResultPrinter(dbnNetwork, genes, inputData.getGeneNames());
	}

}
