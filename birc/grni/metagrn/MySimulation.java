package birc.grni.metagrn;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import birc.grni.gui.MetaScoreRank;
import birc.grni.util.GLOBALVAR;
import ch.epfl.lis.gnw.BenchmarkGeneratorDream4;
import ch.epfl.lis.gnw.CancelException;
import ch.epfl.lis.gnw.GeneNetwork;
import ch.epfl.lis.gnw.GnwSettings;
import ch.epfl.lis.gnwgui.DynamicalModelElement;
import ch.epfl.lis.gnwgui.NetworkElement;
import ch.epfl.lis.gnwgui.windows.SimulationWindow;


public class MySimulation extends SimulationWindow {
	
	/** Serialization */
	private static final long serialVersionUID = 1L;

	/** NetworkItems to simulate */
	private HashMap<String, NetworkElement> networks;
	
	private NetworkElement item_ = null;
	
    /** Logger for this class */
    private static Logger log_ = Logger.getLogger(MySimulation.class.getName());
    
    // ADD BY LIU:
    private int numberOfSimulations;		/* number of simluations we do for each network*/

    private ArrayList<ArrayList<Double>> inputDataMatrix = null;
	// ============================================================================
	// PUBLIC METHODS
    
	/**
	 * Constructor
	 */
	public MySimulation(Frame aFrame, HashMap<String, NetworkElement> networks, int n /* number of simulations*/, ArrayList<ArrayList<Double>> inputDataMatrix) {
		
		super(aFrame);
		this.networks = networks;
		// ADD BY LIU:
		this.numberOfSimulations = n;
		
		this.inputDataMatrix = inputDataMatrix;
		
		GnwSettings settings = GnwSettings.getInstance();
		// liuxingliang
		settings.generateTsDREAM4TimeSeries(true);
		settings.setNumTimeSeries(1);
		settings.setLoadPerturbations(false);
		
		// Model
		model_.setModel(new DefaultComboBoxModel<String>(new String[] {"Deterministic (ODEs)", "Stochastic (SDEs)", "Run both (ODEs and SDEs)"}));
		if (settings.getSimulateODE() && !settings.getSimulateSDE())
			model_.setSelectedIndex(0);
		else if (!settings.getSimulateODE() && settings.getSimulateSDE())
			model_.setSelectedIndex(1);
		else if (settings.getSimulateODE() && settings.getSimulateSDE())
			model_.setSelectedIndex(2);
		
		// liuxingliang
		// // Experiments
		// wtSS_.setSelected(true);
		// wtSS_.setEnabled(false);
		
		// liuxingliang
		// knockoutSS_.setSelected(settings.generateSsKnockouts());
		// knockdownSS_.setSelected(settings.generateSsKnockdowns());
		// multifactorialSS_.setSelected(settings.generateSsMultifactorial());
		// dualKnockoutSS_.setSelected(settings.generateSsDualKnockouts());
		
		// knockoutTS_.setSelected(settings.generateTsKnockouts());
		// knockdownTS_.setSelected(settings.generateTsKnockdowns());
		// multifactorialTS_.setSelected(settings.generateTsMultifactorial());
		// dualKnockoutTS_.setSelected(settings.generateTsDualKnockouts());
		
		// timeSeriesAsDream4_.setSelected(settings.generateTsDREAM4TimeSeries());
		
		// Set model of "number of time series" spinner
		SpinnerNumberModel model = new SpinnerNumberModel();
		// liuxingliang
		// model.setMinimum(1);
		// model.setMaximum(10000);
		// model.setStepSize(1);
		// model.setValue(settings.getNumTimeSeries());
		// numTimeSeries_.setModel(model);
		
		// Set model of "duration" spinner
		model = new SpinnerNumberModel();
		model.setMinimum(1);
		model.setMaximum(100000);
		model.setStepSize(10);
		model.setValue((int) settings.getMaxtTimeSeries());
		tmax_.setModel(model);
		
		// Set model of "number of points per time series" spinner
		model = new SpinnerNumberModel();
		model.setMinimum(3);
		model.setMaximum(100000);
		model.setStepSize(10);
		
		double dt = settings.getDt();
		double maxt = settings.getMaxtTimeSeries();
		int numMeasuredPoints = (int)Math.round(maxt/dt) + 1;

		if (dt*(numMeasuredPoints-1) != maxt)
			throw new RuntimeException("Duration of time series (GnwSettings.maxtTimeSeries_) must be a multiple of the time step (GnwSettings.dt_)");
		
		model.setValue(numMeasuredPoints);
		numPointsPerTimeSeries_.setModel(model);
		
		// liuxingliang
		// perturbationNew_.setSelected(!settings.getLoadPerturbations());
		// perturbationLoad_.setSelected(settings.getLoadPerturbations());
		
		// Noise
		
		// Diffusion multiplier (SDE only)
		model = new SpinnerNumberModel();
		model.setMinimum(0.0);
		model.setMaximum(10.);
		model.setStepSize(0.01);
		model.setValue(settings.getNoiseCoefficientSDE());
		sdeDiffusionCoeff_.setModel(model);
		
		noNoise_.setSelected(!settings.getAddMicroarrayNoise() && !settings.getAddNormalNoise() && !settings.getAddLognormalNoise());
		useMicroarrayNoise_.setSelected(settings.getAddMicroarrayNoise());
		useLogNormalNoise_.setSelected(settings.getAddNormalNoise() || settings.getAddLognormalNoise());
		addGaussianNoise_.setSelected(settings.getAddNormalNoise());
		addLogNormalNoise_.setSelected(settings.getAddLognormalNoise());
		
		// Set model of "Gaussian noise std" spinner
		model = new SpinnerNumberModel();
		model.setMinimum(0.000001);
		model.setMaximum(10.);
		model.setStepSize(0.01);
		model.setValue(settings.getNormalStdev());
		gaussianNoise_.setModel(model);
		
		// Set model of "log-normal noise std" spinner
		model = new SpinnerNumberModel();
		model.setMinimum(0.000001);
		model.setMaximum(10.);
		model.setStepSize(0.01);
		model.setValue(settings.getLognormalStdev());
		logNormalNoise_.setModel(model);
		
		normalizeNoise_.setSelected(settings.getNormalizeAfterAddingNoise());
		
		// Set the text field with the user path
		// liuxingliang
		//userPath_.setText(GnwSettings.getInstance().getOutputDirectory());
		
		setModelAction();
		// liuxingliang
		// setExperimentAction();
		setNoiseAction();
		
		//TEST
//		String title1, title2;
//		title1 = title2 = "";
//		if (item_ instanceof StructureElement) {
//			ImodNetwork network = ((StructureElement)item_).getNetwork();
//			title1 = item_.getLabel();
//			title2 = network.getSize() + " nodes, " + network.getNumEdges() + " edges";
//		} else if (item_ instanceof DynamicalModelElement) {
//			GeneNetwork geneNetwork = ((DynamicalModelElement)item_).getGeneNetwork();
//			title1 = item_.getLabel();
//			title2 = geneNetwork.getSize() + " genes, " + geneNetwork.getNumEdges() + " interactions";
//		}
//		setHeaderInfo(title1 + " (" + title2 + ")");
		
		//TEST
		// Set tool tips for all elements of the window
//		addTooltips();
	
		/**
		 * ACTIONS
		 */
		
		model_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setModelAction();
			}
		});
		
		// liuxingliang
		// dream4Settings_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
		// 		setDream4Settings();
		// 	}
		// });
		
		// liuxingliang
		// browse_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		
		// 		IODialog dialog = new IODialog(new Frame(""), "Select Target Folder", 
		// 				GnwSettings.getInstance().getOutputDirectory(), IODialog.LOAD);
		// 		
		// 		dialog.selectOnlyFolder(true);
		// 		dialog.display();
		// 		
		// 		if (dialog.getSelection() != null)
		// 			userPath_.setText(dialog.getSelection());
		// 	}
		// });
		
		runButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				// liuxingliang
				myCardLayout_.show(runButtonAndSnakePanel_, snakePanel_.getName());
				snake_.start();

				for(int i = 0; i< numberOfSimulations; i++)
					enterAllActions(i);

				// liuxingliang
				finalizeAfterSuccess();

				try {
					metaScoreRank();
				} catch(IOException ioex) {
					ioex.printStackTrace();
				}
				
				
			}
		});
		
		cancelButton_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				GnwSettings.getInstance().stopBenchmarkGeneration(true);
				escapeAction();
			}
		});
		
		// liuxingliang
		// knockoutSS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// knockdownSS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// multifactorialSS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// dualKnockoutSS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// knockoutTS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// knockdownTS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// multifactorialTS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// dualKnockoutTS_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		// timeSeriesAsDream4_.addActionListener(new ActionListener() {
		// 	public void actionPerformed(final ActionEvent arg0) {
		// 		setExperimentAction();
		// 	}
		// });
		
		noNoise_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setNoiseAction();
			}
		});
		
		useMicroarrayNoise_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setNoiseAction();
			}
		});
		
		useLogNormalNoise_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setNoiseAction();
			}
		});
		
		addGaussianNoise_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setNoiseAction();
			}
		});
		
		addLogNormalNoise_.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				setNoiseAction();
			}
		});
	}
	
	
	// ----------------------------------------------------------------------------
	
	
	public void setModelAction() {
		boolean useSDE = model_.getSelectedIndex() == 1 || model_.getSelectedIndex() == 2;
		sdeDiffusionCoeff_.setEnabled(useSDE);
	}
	
	
	// ----------------------------------------------------------------------------
	
	// liuxingliang
	// /** Set parameters as we did for the DREAM4 benchmarks */
	// public void setDream4Settings() {
	// 		
	// 	// Model
	// 	model_.setSelectedIndex(2);
	// 	
	// 	// Experiments
	// 	knockoutSS_.setSelected(true);
	// 	knockdownSS_.setSelected(true);
	// 	multifactorialSS_.setSelected(true);
	// 	dualKnockoutSS_.setSelected(true);
	// 	
	// 	knockoutTS_.setSelected(false);
	// 	knockdownTS_.setSelected(false);
	// 	multifactorialTS_.setSelected(false);
	// 	dualKnockoutTS_.setSelected(false);
	// 	
	// 	timeSeriesAsDream4_.setSelected(true);
	// 	
	// 	// Set "number of time series" spinner
	// 	//numTimeSeries_.getModel().setValue(99);
	// 	numTimeSeries_.setValue(10);
	// 	
	// 	// "duration" spinner
	// 	tmax_.setValue(1000);
	// 	
	// 	// Set model of "number of points per time serie" spinner
	// 	numPointsPerTimeSeries_.setValue(21);
	// 	
	// 	perturbationNew_.setSelected(true);
	// 	perturbationLoad_.setSelected(false);
	// 	
	// 	// Noise
	// 	
	// 	// Diffusion multiplier (SDE only)
	// 	sdeDiffusionCoeff_.setValue(0.05);
	// 	
	// 	noNoise_.setSelected(false);
	// 	useMicroarrayNoise_.setSelected(true);
	// 	useLogNormalNoise_.setSelected(false);
	// 	addGaussianNoise_.setSelected(false);
	// 	addLogNormalNoise_.setSelected(false);
	// 	
	// 	normalizeNoise_.setSelected(true);
	// 	
	// 	// Enables/disables stuff in the GUI (num time points etc., which may be disabled/gray otherwise)
	// 	setExperimentAction();
	// 	setNoiseAction();
	// 	setModelAction();
	// }
	
	
	// ----------------------------------------------------------------------------
	
	// liuxingliang
	// public void setExperimentAction() {		
	// 	boolean useTimeSeries = (knockoutTS_.isSelected() || knockdownTS_.isSelected() || multifactorialTS_.isSelected() || dualKnockoutTS_.isSelected() || timeSeriesAsDream4_.isSelected());
	// 	numTimeSeriesLabel_.setEnabled(timeSeriesAsDream4_.isSelected());
	// 	numTimeSeries_.setEnabled(timeSeriesAsDream4_.isSelected());
	// 	durationOfSeriesLabel_.setEnabled(useTimeSeries);
	// 	numPointsPerSeriesLabel_.setEnabled(useTimeSeries);
	// 	tmax_.setEnabled(useTimeSeries);
	// 	numPointsPerTimeSeries_.setEnabled(useTimeSeries);
	// }
	
	
	// ----------------------------------------------------------------------------
	
	
	public void setNoiseAction() {
		addGaussianNoise_.setEnabled(useLogNormalNoise_.isSelected());
		addLogNormalNoise_.setEnabled(useLogNormalNoise_.isSelected());
		gaussianNoise_.setEnabled(useLogNormalNoise_.isSelected() && addGaussianNoise_.isSelected());
		logNormalNoise_.setEnabled(useLogNormalNoise_.isSelected() && addLogNormalNoise_.isSelected());
		normalizeNoise_.setEnabled(!noNoise_.isSelected());
	}
	
	
	// ----------------------------------------------------------------------------
		
	public void escapeAction() {
		
		super.escapeAction();
	}
	
	// ADD BY LIU
	public void enterAllActions(int simulationIndex){
		Set<Entry<String, NetworkElement>> entrySet = this.networks.entrySet();
		Iterator<Entry<String, NetworkElement>> itr = entrySet.iterator();
		while(itr.hasNext()) {
			Entry<String, NetworkElement> entry = (Entry<String, NetworkElement>)itr.next();
			this.item_ = entry.getValue();
			if(this.item_ != null)
				enterAction(entry.getKey(), simulationIndex);
		}
		
	}
	
	// ----------------------------------------------------------------------------
	
	// CHANGE BY LIU:
//	/**
//	 * Run the simulation process and benchmark generation.
//	 * Save the simulation parameters defined by the user in the settings of GNW.
//	 * The the simulation thread reads these values in the settings of GNW.
//	 */
//	public void enterAction() {
//		
//		try {
//			GeneNetwork grn = ((DynamicalModelElement) item_).getGeneNetwork();
//			SimulationThread simulation = null;
//			
//			GnwSettings settings = GnwSettings.getInstance();
//			
//			// Save the required settings
//			// Model
//			settings.setSimulateODE(model_.getSelectedIndex() == 0 || model_.getSelectedIndex() == 2);
//			settings.setSimulateSDE(model_.getSelectedIndex() == 1 || model_.getSelectedIndex() == 2);
//			
//			// Experiments
//			settings.generateSsKnockouts(knockoutSS_.isSelected());
//			settings.generateSsKnockdowns(knockdownSS_.isSelected());
//			settings.generateSsMultifactorial(multifactorialSS_.isSelected());
//			settings.generateSsDualKnockouts(dualKnockoutSS_.isSelected());
//			
//			settings.generateTsKnockouts(knockoutTS_.isSelected());
//			settings.generateTsKnockdowns(knockdownTS_.isSelected());
//			settings.generateTsMultifactorial(multifactorialTS_.isSelected());
//			settings.generateTsDualKnockouts(dualKnockoutTS_.isSelected());
//			
//			settings.generateTsDREAM4TimeSeries(timeSeriesAsDream4_.isSelected());
//			
//			if (timeSeriesAsDream4_.isSelected()) { // is saved only if "Time series as in DREAM4 is selected"
//				settings.setNumTimeSeries((Integer) numTimeSeries_.getModel().getValue());
//			}
//			
//			// TODO check that correct
//			int maxt = (Integer) tmax_.getModel().getValue();
//			int dt = maxt / ((Integer) numPointsPerTimeSeries_.getModel().getValue() - 1);
//			if (durationOfSeriesLabel_.isEnabled())
//				settings.setMaxtTimeSeries(maxt);
//			if (numPointsPerSeriesLabel_.isEnabled())
//				settings.setDt(dt);
//				//settings.setNumMeasuredPoints((Integer) numPointsPerTimeSerie_.getModel().getValue());
//			
//			settings.setLoadPerturbations(perturbationLoad_.isSelected());
//			
//			if (settings.getSimulateSDE())
//				settings.setNoiseCoefficientSDE((Double) sdeDiffusionCoeff_.getModel().getValue());
//			
//			settings.setAddMicroarrayNoise(useMicroarrayNoise_.isSelected());
//			settings.setAddNormalNoise(useLogNormalNoise_.isSelected() && addGaussianNoise_.isSelected());
//			settings.setAddLognormalNoise(useLogNormalNoise_.isSelected() && addLogNormalNoise_.isSelected());
//			
//			if (settings.getAddNormalNoise())
//				settings.setNormalStdev((Double) gaussianNoise_.getModel().getValue());
//			
//			if (settings.getAddLognormalNoise())
//				settings.setNormalStdev((Double) logNormalNoise_.getModel().getValue());
//			
//			settings.setNormalizeAfterAddingNoise(normalizeNoise_.isSelected());
//			
//			simulation = new SimulationThread(grn);
//			
//			// Perhaps make a test on the path validity
//			settings.setOutputDirectory(userPath_.getText());
//			settings.stopBenchmarkGeneration(false); // reset
//			
//			// be sure to have set the output directory before running the simulation
//			simulation.start();
//		}
//		catch (Exception e)
//		{
//			log_.log(Level.WARNING, "Simulation::enterAction(): " + e.getMessage(), e);
//		}
//	}
	
	/**
	 * Run the simulation process and benchmark generation.
	 * Save the simulation parameters defined by the user in the settings of GNW.
	 * The the simulation thread reads these values in the settings of GNW.
	 */
	public void enterAction(String networkName, int simulationIndex) {
	
		try {
			GeneNetwork grn = ((DynamicalModelElement) item_).getGeneNetwork();
			//LIU
//			SimulationThread simulation = null;
			MySimulationThread simulation = null;
			
			GnwSettings settings = GnwSettings.getInstance();
			
			// Save the required settings
			// Model
			settings.setSimulateODE(model_.getSelectedIndex() == 0 || model_.getSelectedIndex() == 2);
			settings.setSimulateSDE(model_.getSelectedIndex() == 1 || model_.getSelectedIndex() == 2);
			
			// liuxingliang
			// // Experiments
			// settings.generateSsKnockouts(knockoutSS_.isSelected());
			// settings.generateSsKnockdowns(knockdownSS_.isSelected());
			// settings.generateSsMultifactorial(multifactorialSS_.isSelected());
			// settings.generateSsDualKnockouts(dualKnockoutSS_.isSelected());
			
			// settings.generateTsKnockouts(knockoutTS_.isSelected());
			// settings.generateTsKnockdowns(knockdownTS_.isSelected());
			// settings.generateTsMultifactorial(multifactorialTS_.isSelected());
			// settings.generateTsDualKnockouts(dualKnockoutTS_.isSelected());
			
			// settings.generateTsDREAM4TimeSeries(timeSeriesAsDream4_.isSelected());
			
			// if (timeSeriesAsDream4_.isSelected()) { // is saved only if "Time series as in DREAM4 is selected"
			// 	settings.setNumTimeSeries((Integer) numTimeSeries_.getModel().getValue());
			// }
			
			// TODO check that correct
			int maxt = (Integer) tmax_.getModel().getValue();
			int dt = maxt / ((Integer) numPointsPerTimeSeries_.getModel().getValue() - 1);
			if (durationOfSeriesLabel_.isEnabled())
				settings.setMaxtTimeSeries(maxt);
			if (numPointsPerSeriesLabel_.isEnabled())
				settings.setDt(dt);
				//settings.setNumMeasuredPoints((Integer) numPointsPerTimeSerie_.getModel().getValue());
			
			// liuxingliang
			// settings.setLoadPerturbations(perturbationLoad_.isSelected());
			
			if (settings.getSimulateSDE())
				settings.setNoiseCoefficientSDE((Double) sdeDiffusionCoeff_.getModel().getValue());
			
			settings.setAddMicroarrayNoise(useMicroarrayNoise_.isSelected());
			settings.setAddNormalNoise(useLogNormalNoise_.isSelected() && addGaussianNoise_.isSelected());
			settings.setAddLognormalNoise(useLogNormalNoise_.isSelected() && addLogNormalNoise_.isSelected());
			
			if (settings.getAddNormalNoise())
				settings.setNormalStdev((Double) gaussianNoise_.getModel().getValue());
			
			if (settings.getAddLognormalNoise())
				settings.setNormalStdev((Double) logNormalNoise_.getModel().getValue());
			
			settings.setNormalizeAfterAddingNoise(normalizeNoise_.isSelected());
			
			// LIU
//			simulation = new SimulationThread(grn);
			simulation = new MySimulationThread(grn);
			
			// Perhaps make a test on the path validity
			//File destDir = new File(userPath_.getText()+"/"+networkName+"/"+simulationIndex);
			File destDir = new File(GLOBALVAR.simulationNetworkTmpDir+ File.separator +networkName+ File.separator +simulationIndex);
			if(!destDir.exists())
				destDir.mkdirs();
			settings.setOutputDirectory(destDir.getAbsolutePath());
			settings.stopBenchmarkGeneration(false); // reset
			
			// be sure to have set the output directory before running the simulation
			// LIU
//			simulation.start();
			
			simulation.run();
		}
		catch (Exception e)
		{
			log_.log(Level.WARNING, "Simulation::enterAction(): " + e.getMessage(), e);
		}
	}
	public static double[][] ToDD(ArrayList<ArrayList<Double>> input){
		int numrow = input.size();
		int numcol = input.get(0).size();
		double[][] dd = new double[numrow][numcol];
		for(int i = 0; i<numrow; i++){
			for(int j = 0; j<numcol; j++)
				dd[i][j] = input.get(i).get(j);
		}
		return dd;
	}
	public static ArrayList<ArrayList<Double>> ToAA(RealMatrix m){
		ArrayList<ArrayList<Double>> aa = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i<m.getRowDimension(); i++){
			ArrayList<Double> a = new ArrayList<Double>();
			for(int j = 0; j<m.getColumnDimension(); j++)
				a.add(m.getEntry(i, j));
			aa.add(a);
		}
		return aa;
	}
	public static RealMatrix ToCOV(ArrayList<ArrayList<Double>> input){
		// assuming non-zero sizes
		return new Covariance(ToDD(input)).getCovarianceMatrix();
	}
	public static void generateCov(ArrayList<ArrayList<Double>> input, ArrayList output){
		RealMatrix cov = ToCOV(input);
		int numInputDims = cov.getColumnDimension();
		SingularValueDecomposition svd = new SingularValueDecomposition(cov);
		double[] svalues = svd.getSingularValues();
		double threshold = 0.95;
		double sum = 0;
		for(int i = 0; i<numInputDims; i++)
			sum += svalues[i];
		double accu = 0;
		int firstN = 0;
		for(int i = 0; i<numInputDims; i++){
			accu += svalues[i];
			if (accu/sum > threshold){
				firstN = i;
				break;
			}
		}
		RealMatrix transform = svd.getU().getSubMatrix(0, cov.getColumnDimension()-1, 0, firstN);
		output.add(transform);
		// compute mean
		RealMatrix inputdd = new Array2DRowRealMatrix(ToDD(input));
		double[] mean = new double[inputdd.getColumnDimension()];
		for(int j = 0; j<inputdd.getColumnDimension(); j++){
			mean[j] = new Mean().evaluate(inputdd.getColumn(j));
		}
		output.add(mean);
		//return transform;
	}
	public static ArrayList getMeanAndVar(RealMatrix inputdd){
		double[] mean = new double[inputdd.getColumnDimension()];
		double[] var  = new double[inputdd.getColumnDimension()];
		for(int j = 0; j<inputdd.getColumnDimension(); j++){
			mean[j] = new Mean().evaluate(inputdd.getColumn(j));
			var [j] = new Variance().evaluate(inputdd.getColumn(j));
		}
		System.out.println(mean.toString());
		System.out.println(var.toString());
		ArrayList ar = new ArrayList();
		ar.add(mean);
		ar.add(var);
		return ar;
		//return mean;
	}
	public static ArrayList<ArrayList<Double>> applyPCA(ArrayList<ArrayList<Double>> input, ArrayList PCAoutput){
		RealMatrix m = new Array2DRowRealMatrix(ToDD(input));
		RealMatrix transform = (RealMatrix)PCAoutput.get(0);
		double[] mean = (double[])PCAoutput.get(1);
		for(int i = 0; i < m.getRowDimension(); i++){
			for(int j = 0; j<m.getColumnDimension(); j++){
				m.setEntry(i, j, m.getEntry(i, j) - mean[j]);
			}
		}
		//ArrayList ar = getMeanAndVar(m);
		return ToAA(m.multiply(transform));
	}
	public void metaScoreRank() throws IOException {
		
		
		//ZMX
		log_.log(Level.WARNING, "establish meta score rank table begin" );
		// LIU
		// ranks of MetaScore
		HashMap<String, Double> algoScoreMap = new HashMap<String, Double>();

		//String simulatedDataDir = userPath_.getText(); 
		String simulatedDataDir = GLOBALVAR.simulationNetworkTmpDir; 

		Set<Map.Entry<String, NetworkElement>> chosenNetworkSet = networks.entrySet();
		for(Map.Entry<String, NetworkElement> entry : chosenNetworkSet) {
			if(entry.getValue() == null) {
				continue;
			}
			
			String networkName = entry.getKey();
			// get the highest of all simulations of each chosen inference algorithm
			ArrayList<Double> metaScoreList = new ArrayList<Double>();
			for(int simulation = 0; simulation< numberOfSimulations; simulation++) {
				String dream4TsPath = simulatedDataDir + "/" + networkName + "/" + simulation + "/" + networkName + "_dream4_timeseries.tsv";
				BufferedReader br = new BufferedReader(new FileReader(dream4TsPath));
				String line = null;
				br.readLine();
				br.readLine();
				ArrayList<ArrayList<Double>> dream4TsDataMatrix = new ArrayList<ArrayList<Double>>();
				while((line = br.readLine()) != null) {
					String[] arr = line.split("\t");
					ArrayList<Double> list = new ArrayList<Double>();
					for(int i = 1; i< arr.length; i++) {
						list.add(Double.parseDouble(arr[i]));
					}
					dream4TsDataMatrix.add(list);
				}
				br.close();
				//System.out.println("original input data matrix has how many nodes: "+inputDataMatrix.get(0).size());
				//System.out.println("original dream4 data matrix has how many nodes: "+dream4TsDataMatrix.get(0).size());
				//System.out.println("inputDataMatrix size: "+inputDataMatrix.size()+"DreamDataMatrix size: "+dream4TsDataMatrix.size());
				ArrayList PCAoutput = new ArrayList();
				generateCov(inputDataMatrix, PCAoutput);
				
				double metaScore = MetaScore.sampleOverlap(
						applyPCA(inputDataMatrix, PCAoutput),
						applyPCA(dream4TsDataMatrix, PCAoutput)
					); 
				//double metaScore=MetaScore.sampleOverlap(inputDataMatrix, dream4TsDataMatrix);
				metaScoreList.add(metaScore);
			}
			Collections.sort(metaScoreList);// euclidean distance, the lower the better
			algoScoreMap.put(networkName, metaScoreList.get(0));
		}
		
		Set<Map.Entry<String, Double>> algoScoreMapEntrySet = algoScoreMap.entrySet();
		ArrayList<Map.Entry<String, Double>> algoScoreMapEntryList = new ArrayList<Map.Entry<String, Double>>(algoScoreMapEntrySet); 
		Collections.sort(
			algoScoreMapEntryList, 
			new Comparator<Map.Entry<String, Double>> () {
				public int compare(Map.Entry<String, Double> entryA, Map.Entry<String, Double> entryB) {
					double valueA = entryA.getValue();
					double valueB = entryB.getValue();
					return (valueA > valueB) ? 1 : (valueA == valueB) ? 0 : -1;
				}
			} 
		);
		//zmx
		String[][] metaScoreRankTable = new String[algoScoreMapEntryList.size()][3];
		int rank = 1;
		double currentValue = algoScoreMapEntryList.get(0).getValue();
		metaScoreRankTable[0][0] = algoScoreMapEntryList.get(0).getKey();
		metaScoreRankTable[0][1] = rank + "";
		metaScoreRankTable[0][2] =currentValue+"";
		for(int i = 1; i< algoScoreMapEntryList.size();i++) {
			metaScoreRankTable[i][0] = algoScoreMapEntryList.get(i).getKey();
			if(algoScoreMapEntryList.get(i).getValue() > currentValue) {
				rank++; // euclidean distance, the lower the better
			}
			metaScoreRankTable[i][1] = rank + "";
			metaScoreRankTable[i][2] = algoScoreMapEntryList.get(i).getValue()+"";
		}
		//ZMX
		log_.log(Level.WARNING, "establish meta score rank table done" );
		MetaScoreRank metaScoreRank = new MetaScoreRank(new JFrame(), metaScoreRankTable);
		metaScoreRank.metaScoreRankFrame.setVisible(true);
	}
	
	// ----------------------------------------------------------------------------

	/** Add tooltips for all elements of the window */
	private void addTooltips() {
				
		// liuxingliang
		// dream4Settings_.setToolTipText(
		// 		"<html>Set all parameters of this window to the values<br>" +
		// 		"that were used to generate the DREAM4 challenges</html>");
		normalizeNoise_.setToolTipText(
				"<html>After adding experimental noise (measurement error), normalize<br>" +
				"by dividing all concentrations values by the maximum mRNA<br>" +
				"concentration of all datasets</html>");
		noNoise_.setToolTipText(
				"<html>Do not add any experimental noise (measurement error) after the simulation<br>" +
				"(if SDEs are used, there will still be noise in the dynamics)</html>");
		wtSS_.setToolTipText(
				"<html>Generate the steady state of the wild-type<br>" +
				"(can't be disabled)</html>");
		useLogNormalNoise_.setToolTipText(
				"<html>Select checkboxes below to add normal (Gaussian)<br>" +
				"and/or log-normal noise after the simulation</html>");
		addGaussianNoise_.setToolTipText(
				"<html>Select to add normal (Gaussian) noise</html>");
		addLogNormalNoise_.setToolTipText(
				"<html>Select to add log-normal noise</html>");
		useMicroarrayNoise_.setToolTipText(
				"<html>Select to use the model of noise in microarrays that was used for the DREAM4<br>" +
				"challenges, which is similar to a mix of normal and log-normal noise<br>" +
				"(Tu, Stolovitzky, and Klein. <i>PNAS</i>, 99:14031-14036, 2002)</html>");
		
		String networkName = "<i>" + item_.getLabel() + "</i>";
		// liuxingliang
		// perturbationLoad_.setToolTipText(
		// 		"<html>Load the perturbations from the following files<br>" +
		// 		"(they must be located in the output directory):<br>" +
		// 		"- " + networkName + "_multifactorial_perturbations.tsv<br>" +
		// 		"- " + networkName + "_dualknockouts_perturbations.tsv<br>" +
		// 		"- " + networkName + "_dream4_timeseries_perturbations.tsv</html>");
		// perturbationNew_.setToolTipText(
		// 		"<html>Generate new perturbations, select if you don't have<br>" +
		// 		"predefined perturbations that you want to use</html>");
		
		// liuxingliang
		// timeSeriesAsDream4_.setToolTipText(
		// 		"<html>Generate time series as those provided in DREAM4 (<i>in addition</i><br>" +
		// 		"to time series for knockouts, knockdowns, etc. selected above)</html>");
		// dualKnockoutTS_.setToolTipText(
		// 		"<html>Trajectories for dual knockouts (at t=0 is the<br>" +
		// 		"wild-type, at this time the dual knockout is done)</html>");
		// multifactorialTS_.setToolTipText(
		// 		"<html>Trajectories for multifactorial perturbations (at t=0 is<br>" +
		// 		"the wild-type, at this time the perturbation is applied)</html>");
		// knockdownTS_.setToolTipText(
		// 		"<html>Trajectories for knockdowns (at t=0 is the<br>" +
		// 		"wild-type, at this time the knockdown is done)</html>");
		// knockoutTS_.setToolTipText(
		// 		"<html>Trajectories for the knockouts (at t=0 is the<br>" +
		// 		" wild-type, at this time the knockout is done)</html>");
		// dualKnockoutSS_.setToolTipText(
		// 		"<html>Steady states for dual knockouts (pairs are selected<br>" +
		// 		"according to how many genes they co-regulate)</html>");
		// multifactorialSS_.setToolTipText(
		// 		"<html>Steady states for multifactorial perturbations</html>");
		logNormalNoise_.setToolTipText(
				"<html>Standard deviation of the log-normal noise</html>");
		// liuxingliang
		// numTimeSeries_.setToolTipText(
		// 		"<html>The number of time series (a different perturbation<br>" +
		// 		"is used for every time series)</html>");
		// knockdownSS_.setToolTipText(
		// 		"<html>Steady states for knockdown of every gene</html>");
		// knockoutSS_.setToolTipText(
		//		"<html>Steady states for knockout of every gene</html>");
		model_.setToolTipText(
				"<html>Select ODEs (deterministic) or SDEs (noise in dynamics) for the<br>" +
				"simulation of all experiments selected below. If you select both,<br>" +
				"they will be run one after the other using the same perturbations<br>" +
				"and the label <i>nonoise</i> will be added to the data from the ODEs.</html>");
		tmax_.setToolTipText(
				"<html>Duration of the time series experiments</html>");
		sdeDiffusionCoeff_.setToolTipText(
				"<html>Multiplicative constant of the noise term in the SDEs<br>" +
				"(if set to 0, using SDEs is equivalent to using ODEs)</html>");
		gaussianNoise_.setToolTipText(
				"<html>Standard deviation of the Gaussian noise</html>");
		numPointsPerTimeSeries_.setToolTipText(
				"<html>Number of points per time series (defines how many points are saved<br>" +
				"in the datasets, does not affect precision of numerical integration)</html>");
		runButton_.setToolTipText(
				"<html>Set parameters to the given values and run all experiments</html>");
		cancelButton_.setToolTipText(
				"<html>Abort (the thread may finish the current experiment before it exits)</html>");
		
		// tooltips disappear only after 10s
		ToolTipManager.sharedInstance().setDismissDelay(10000);
	}
	
	// ============================================================================
	// PRIVATE CLASSES
	
//	/** 
//	 * Thread simulating an in silico benchmark.
//	 * @author Thomas Schaffter (firstname.lastname@gmail.com)
//	 */
//	private class SimulationThread implements Runnable {
//		
//		/** Implemented types of benchmark */
//		//public static final int DREAM3 = 1;
//		//public static final int DREAM4 = 2;
//		
//		/** Main Thread */
//		private Thread myThread_;
//		/** handles the experiments */
//		private BenchmarkGeneratorDream4 benchmarkGenerator_ = null;
//	
//		// ============================================================================
//		// PUBLIC METHODS
//		
//		public SimulationThread(GeneNetwork grn)
//		{
//			
//			super();
//			myThread_ = null;
//			benchmarkGenerator_ = new BenchmarkGeneratorDream4(grn);
//		}
//		
//		// ----------------------------------------------------------------------------
//
//		public void start() {
//			// If myThread_ is null, we start it!
//			if (myThread_ == null) {
//				myThread_ = new Thread(this);
//				myThread_.start();
//			}
//		}
//		
//		// ----------------------------------------------------------------------------
//		
//		@SuppressWarnings("unused")
//		public void stop()
//		{
//			myThread_ = null;
//		}
//		
//		// ----------------------------------------------------------------------------
//
//		public void run()
//		{
//			snake_.start();
//			myCardLayout_.show(runButtonAndSnakePanel_, snakePanel_.getName());
//
//			try
//			{
//				benchmarkGenerator_.setOutputDirectory(GnwSettings.getInstance().getOutputDirectory());
//				benchmarkGenerator_.generateBenchmark(); // all the options have been saved in GnwSettings
//				finalizeAfterSuccess();
//				log_.log(Level.INFO, "Done!");
//				
//			}
//			catch (OutOfMemoryError e)
//			{
//				log_.log(Level.WARNING, "There is not enough memory available to run this program.\n" +
//						"Quit one or more programs, and then try again.\n" +
//						"If enough amounts of RAM are installed on this computer, try to run the program " +
//						"with the command-line argument -Xmx1024m to use maximum 1024Mb of memory, " +
//						"-Xmx2048m to use max 2048Mb, etc.");
//				JOptionPane.showMessageDialog(new Frame(), "Out of memory, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
//				finalizeAfterFail();
//				
//			}
//			catch (IllegalArgumentException e)
//			{
//				log_.log(Level.WARNING, e.getMessage(), e);
//				JOptionPane.showMessageDialog(new Frame(), "Illegal argument, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
//				log_.log(Level.INFO, "Potential orkaround: gene names must contain at least one char (e.g. \"5\" is not a valid gene name, but \"G5\" is)");
//				finalizeAfterFail();
//			}
//			catch (CancelException e)
//			{
//				// do not display an annoying dialog to say "cancelled!"
//				log_.log(Level.INFO, e.getMessage());
//				finalizeAfterFail();
//			}
//			catch (ConvergenceException e)
//			{
//				log_.log(Level.WARNING, "Simulation::run(): " + e.getMessage(), e);
//				JOptionPane.showMessageDialog(new Frame(), "Unable to converge, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
//				finalizeAfterFail();
//			}
//			catch (RuntimeException e)
//			{
//				log_.log(Level.WARNING, "Simulation::run(): " + e.getMessage(), e);
//				JOptionPane.showMessageDialog(new Frame(), "Runtime exception, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
//				finalizeAfterFail();
//			}
//			catch (Exception e)
//			{
//				log_.log(Level.WARNING, "Simulation::run(): " + e.getMessage(), e);
//				JOptionPane.showMessageDialog(new Frame(), "Error encountered, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
//				finalizeAfterFail();
//			}
//	    }
//			
//		// ----------------------------------------------------------------------------
//		
//		public void finalizeAfterSuccess()
//		{
//			snake_.stop();
//			myCardLayout_.show(runButtonAndSnakePanel_, runPanel_.getName());
//			escapeAction(); // close the simulation window
//		}
//		
//		// ----------------------------------------------------------------------------
//		
//		public void finalizeAfterFail()
//		{
//			snake_.stop();
//			myCardLayout_.show(runButtonAndSnakePanel_, runPanel_.getName());
//			//escapeAction(); // close the simulation window
//		}
//	}
	
	/** 
	 * Revised version of original SimulationThreadt
	 * @author liu xingliang
	 */
	private class MySimulationThread {
		
		/** Implemented types of benchmark */
		//public static final int DREAM3 = 1;
		//public static final int DREAM4 = 2;
		
		// LIU
//		/** Main Thread */
//		private Thread myThread_;
//		/** handles the experiments */
		private BenchmarkGeneratorDream4 benchmarkGenerator_ = null;
	
		// ============================================================================
		// PUBLIC METHODS
		
		public MySimulationThread(GeneNetwork grn)
		{
			// LIU
//			super();
//			myThread_ = null;
			benchmarkGenerator_ = new BenchmarkGeneratorDream4(grn);
		}
		
		// ----------------------------------------------------------------------------

		// LIU
//		public void start() {
//			// If myThread_ is null, we start it!
//			if (myThread_ == null) {
//				myThread_ = new Thread(this);
//				myThread_.start();
//			}
//		}
		
		// ----------------------------------------------------------------------------
		
		// LIU
//		@SuppressWarnings("unused")
//		public void stop()
//		{
//			myThread_ = null;
//		}
//		
		// ----------------------------------------------------------------------------

		public void run()
		{
			try
			{
				benchmarkGenerator_.setOutputDirectory(GnwSettings.getInstance().getOutputDirectory());
				benchmarkGenerator_.generateBenchmark(); // all the options have been saved in GnwSettings
				// liuxingliang
				//finalizeAfterSuccess();
				log_.log(Level.INFO, "Done!");
				
			}
			catch (OutOfMemoryError e)
			{
				log_.log(Level.WARNING, "There is not enough memory available to run this program.\n" +
						"Quit one or more programs, and then try again.\n" +
						"If enough amounts of RAM are installed on this computer, try to run the program " +
						"with the command-line argument -Xmx1024m to use maximum 1024Mb of memory, " +
						"-Xmx2048m to use max 2048Mb, etc.");
				JOptionPane.showMessageDialog(new Frame(), "Out of memory, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
				// liuxingliang
				// finalizeAfterFail();
				
			}
			catch (IllegalArgumentException e)
			{
				log_.log(Level.WARNING, e.getMessage(), e);
				JOptionPane.showMessageDialog(new Frame(), "Illegal argument, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
				log_.log(Level.INFO, "Potential orkaround: gene names must contain at least one char (e.g. \"5\" is not a valid gene name, but \"G5\" is)");
				// liuxingliang
				// finalizeAfterFail();
			}
			catch (CancelException e)
			{
				// do not display an annoying dialog to say "cancelled!"
				log_.log(Level.INFO, e.getMessage());
				// liuxingliang
				// finalizeAfterFail();
			}
			catch (ConvergenceException e)
			{
				log_.log(Level.WARNING, "Simulation::run(): " + e.getMessage(), e);
				JOptionPane.showMessageDialog(new Frame(), "Unable to converge, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
				// liuxingliang
				// finalizeAfterFail();
			}
			catch (RuntimeException e)
			{
				log_.log(Level.WARNING, "Simulation::run(): " + e.getMessage(), e);
				JOptionPane.showMessageDialog(new Frame(), "Runtime exception, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
				// liuxingliang
				// finalizeAfterFail();
			}
			catch (Exception e)
			{
				log_.log(Level.WARNING, "Simulation::run(): " + e.getMessage(), e);
				JOptionPane.showMessageDialog(new Frame(), "Error encountered, see console for details.", "GNW message", JOptionPane.WARNING_MESSAGE);
				// liuxingliang
				// finalizeAfterFail();
			}
	    }
			
		// liuxingliang
		// // ----------------------------------------------------------------------------
		
		// public void finalizeAfterSuccess()
		// {
		// 	snake_.stop();
		// 	myCardLayout_.show(runButtonAndSnakePanel_, runPanel_.getName());
		// 	escapeAction(); // close the simulation window
		// }
		
		// // ----------------------------------------------------------------------------
		
		// public void finalizeAfterFail()
		// {
		// 	snake_.stop();
		// 	myCardLayout_.show(runButtonAndSnakePanel_, runPanel_.getName());
		// 	//escapeAction(); // close the simulation window
		// }
	}
	
	// liuxingliang: move out from class: MySimulationThread
	public void finalizeAfterSuccess()
	{
		snake_.stop();
		myCardLayout_.show(runButtonAndSnakePanel_, runPanel_.getName());
		escapeAction(); // close the simulation window
	}
	
	public void finalizeAfterFail()
	{
		snake_.stop();
		myCardLayout_.show(runButtonAndSnakePanel_, runPanel_.getName());
		//escapeAction(); // close the simulation window
	}
}
