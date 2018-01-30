package birc.grni.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import birc.grni.metagrn.MySimulation;
import birc.grni.util.CommonUtil;
import birc.grni.util.InputData;
import birc.grni.util.exception.BadInputFormatException;
import ch.epfl.lis.gnwgui.DynamicalModelElement;
import ch.epfl.lis.gnwgui.GnwGuiSettings;
import ch.epfl.lis.gnwgui.NetworkElement;
import ch.epfl.lis.gnwgui.StructureElement;
import ch.epfl.lis.gnwgui.idesktop.IElement;
import ch.epfl.lis.gnwgui.windows.Wait;
import ch.epfl.lis.imod.ImodNetwork;
import ch.epfl.lis.networks.Structure;
import ch.epfl.lis.networks.ios.ParseException;


public class MetaGrnDisplay extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected JLabel inputFileLabel;
	protected JLabel emptyLabel;
	protected JTextField inputFileTextField;
	protected JButton inputFileButton;

	private String filepath;
	private NetworkElement element;
	
	protected ButtonGroup rowColumnChooseButtonGroup;
	private JLabel rowColumnChooseLabel;
	private JPanel rowColumnChooseButtonPanel;
	
	protected JCheckBox withheaderCheckBox;
	
	private JLabel chooseAlgorithmLabel;
	private JCheckBox dbnAlgorithmCheckBox;				/* choose to investigate dynamic bayesian network algorithm*/
	private JCheckBox rfAlgorithmCheckBox;				/* choose to investigate random forest algorithm*/
	private JCheckBox enAlgorithmCheckBox;				/* choose to investigate elastic net algorithm*/
	private JCheckBox lassoAlgorithmCheckBox;			/* choose to investigate lasso algorithm*/
	private JCheckBox lassoDelayAlgorithmCheckBox;		/* choose to investigate lasso delay algorithm*/
	private JCheckBox ridgeAlgorithmCheckBox;			/* choose to investigate ridge algorithm*/
	
	//ZMX
	//original user network
	private JLabel originalUserNetworkLabel;
	private JCheckBox originalUserNetworkCheckBox;				/*choose to import user modified network*/
	private JTextField originalUserNetworkInputFileTextField;	
	private JButton originalUserNetworkInputFileButton;
	
	//modified user network
	private JLabel modifiedUserNetworkLabel;
	private JCheckBox modifiedUserNetworkCheckBox;				/*choose to import user modified network*/
	private JTextField modifiedUserNetworkInputFileTextField;	
	private JButton modifiedUserNetworkInputFileButton;
	
	/* how many times of simulation we will do*/
	private JLabel numberOfSimulationLabel;
	private JSpinner numberOfSimulationSpinner;			
	
	private JButton generateNetworkButton;
	protected JButton metaGrnButton;
	private JPanel bottomButtonPanel;					/* in order to keep buttons at the bottom symmetrical, use a panel to contain them*/
	
	/* whether store intermediate results (generated networks) of Meta-GRN*/
	private JCheckBox storeNetworkCheckBox;
	
	// /* only generate synthetic data directly from existing network, not generate networks from original data*/
	// private JCheckBox generateDataOnlyCheckBox;
	
	// /* format of input networks, only matrix and TSV format are accepted currently*/
	// private ButtonGroup networkFormatButtonGroup;
	// private JLabel networkFormatLabel;
	// private JPanel networkFormatButtonPanel;
	
	private ArrayList<ArrayList<Double>> inputDataMatrix = new ArrayList<ArrayList<Double>>();
	//ZMX
	//private ArrayList<ArrayList<Double>> userNetworkInputDataMatrix = new ArrayList<ArrayList<Double>>();
	
	public MetaGrnDisplay() {
		
		super();
		this.setName("Ensemble Method Panel");			//edit to show Ensemble Method instead
		this.setBackground(Color.WHITE);
		
		/* set layout of this panel*/
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints(); 
		
		// this.generateDataOnlyCheckBox = new JCheckBox("Synthesize Data Only"); 
		// this.generateDataOnlyCheckBox.setBackground(Color.WHITE);
		
		// gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.weightx = 1;
		// gridBagConstraints.gridx = 0;
		// gridBagConstraints.gridy = 0;
		// gridBagConstraints.gridwidth = 3;
		// gridBagConstraints.insets = new Insets(0,15,0,10);
		// this.add(this.generateDataOnlyCheckBox, gridBagConstraints);
		
		// gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		// /* format of input networks*/
		// this.networkFormatLabel = new JLabel();
		// this.networkFormatLabel.setText("Network Format");
		
		// this.networkFormatButtonGroup = new ButtonGroup();
		// this.networkFormatButtonPanel = new JPanel();
		// this.networkFormatButtonPanel.setBackground(Color.WHITE);
		// addRadioButton("Matrix", this.networkFormatButtonGroup, this.networkFormatButtonPanel, true, false);
		// addRadioButton("TSV", this.networkFormatButtonGroup, this.networkFormatButtonPanel, false, false);
		
		// gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.weightx = 1;
		// gridBagConstraints.gridx = 3;
		// gridBagConstraints.gridy = 0;
		// gridBagConstraints.gridwidth = 1;
		// gridBagConstraints.insets = new Insets(0,0,0,10);
		// this.add(this.networkFormatLabel, gridBagConstraints);
		
		// gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		// gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		// gridBagConstraints.weightx = 1;
		// gridBagConstraints.gridx = 4;
		// gridBagConstraints.gridy = 0;
		// gridBagConstraints.gridwidth = 3;
		// this.add(this.networkFormatButtonPanel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		inputFileLabel = new JLabel("Choose data file:  ");
		inputFileTextField = new JTextField("upload the data file for Ensemble Method:   ");
		inputFileButton = new JButton("Choose");
		inputFileButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
							JFrame frame = new JFrame();
							FileDialog fileDialog = new FileDialog(frame);
							fileDialog.setVisible(true);
							String selectedDir = fileDialog.getDirectory();
							String selectedFile = fileDialog.getFile();							
							if(selectedFile != null)							
								inputFileTextField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
					    }
				}
		);
		
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		this.add(inputFileLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 1;									/* put all extra horizontal space in the file path field*/
		gridBagConstraints.gridwidth = 5;
		this.add(inputFileTextField, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0,10,0,10);
		this.add(inputFileButton, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		/* whether the input file includes header or not*/
		withheaderCheckBox = new JCheckBox("Input with header");
		withheaderCheckBox.setBackground(Color.WHITE);
		
		
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		this.add(withheaderCheckBox, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		/* radio button to choose gene names are row names or column names, 
		 * if gene names are row names, then time series are column names,
		 * so each column is a sample of a time point; otherwise, if
		 * gene names are column names, then time series are row names, then
		 * each row are a sample of a time point*/
		rowColumnChooseLabel = new JLabel();
		rowColumnChooseLabel.setText("Gene names are ");
		
		rowColumnChooseButtonGroup = new ButtonGroup();
		rowColumnChooseButtonPanel = new JPanel();
		rowColumnChooseButtonPanel.setBackground(Color.WHITE);
		addRadioButton("column header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, true, true);
		addRadioButton("row header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, false, true);
		
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
//		upPanelConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		this.add(rowColumnChooseLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		this.add(rowColumnChooseButtonPanel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		/* which algorithms we need to investigate or compare*/
		
		chooseAlgorithmLabel = new JLabel("Algorithms to compare:");
		dbnAlgorithmCheckBox = new JCheckBox("DBN");
		dbnAlgorithmCheckBox.setBackground(Color.WHITE);
		rfAlgorithmCheckBox = new JCheckBox("Random Forests");
		rfAlgorithmCheckBox.setBackground(Color.WHITE);
		enAlgorithmCheckBox = new JCheckBox("ElasticNet");
		enAlgorithmCheckBox.setBackground(Color.WHITE);
		lassoAlgorithmCheckBox = new JCheckBox("Lasso");
		lassoAlgorithmCheckBox.setBackground(Color.WHITE);
		this.lassoDelayAlgorithmCheckBox = new JCheckBox("Lasso Delay");
		this.lassoDelayAlgorithmCheckBox.setBackground(Color.WHITE);
		ridgeAlgorithmCheckBox = new JCheckBox("Ridge Rigression");
		ridgeAlgorithmCheckBox.setBackground(Color.WHITE);
		
		
		
		
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		this.add(chooseAlgorithmLabel, gridBagConstraints);
		
		
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridy = 4;
		
		gridBagConstraints.gridx = 1;
		this.add(dbnAlgorithmCheckBox, gridBagConstraints);
		
		gridBagConstraints.gridx = 2;
		this.add(rfAlgorithmCheckBox, gridBagConstraints);
		
		gridBagConstraints.gridx = 3;
		this.add(enAlgorithmCheckBox, gridBagConstraints);
		
		gridBagConstraints.gridx = 4;
		this.add(lassoAlgorithmCheckBox, gridBagConstraints);
		
		gridBagConstraints.gridx = 5;
		this.add(lassoDelayAlgorithmCheckBox, gridBagConstraints);
		
		gridBagConstraints.gridx = 6;
		this.add(ridgeAlgorithmCheckBox, gridBagConstraints);
		
		
		//zmx original user network
		gridBagConstraints = new GridBagConstraints();						/* restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		
		
		originalUserNetworkCheckBox = new JCheckBox("User Network");
		originalUserNetworkCheckBox.setBackground(Color.WHITE);
		this.add(originalUserNetworkCheckBox, gridBagConstraints);
		
		
		originalUserNetworkInputFileTextField = new JTextField("Upload data file for original user network:");
		originalUserNetworkInputFileTextField.setEnabled(false);
		originalUserNetworkInputFileButton = new JButton("Choose");
		originalUserNetworkInputFileButton.setEnabled(false);
		
		originalUserNetworkInputFileButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
							JFrame frame = new JFrame();
							FileDialog fileDialog = new FileDialog(frame);
							fileDialog.setVisible(true);
							String selectedDir = fileDialog.getDirectory();
							String selectedFile = fileDialog.getFile();							
							if(selectedFile != null)							
								originalUserNetworkInputFileTextField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
					    }
				}
		);
		
		gridBagConstraints= new GridBagConstraints();							/*restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx=1;
		gridBagConstraints.gridwidth=5;
		this.add(originalUserNetworkInputFileTextField,gridBagConstraints);
		
		gridBagConstraints= new GridBagConstraints();							/*restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		//gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0,10,0,10);
		this.add(originalUserNetworkInputFileButton, gridBagConstraints);
		
		originalUserNetworkCheckBox.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Boolean originalUserNetworkSelect=originalUserNetworkCheckBox.isSelected();
						originalUserNetworkInputFileTextField.setEnabled(originalUserNetworkSelect);
						originalUserNetworkInputFileButton.setEnabled(originalUserNetworkSelect);
					}
				});
		
		
		emptyLabel = new JLabel("   ");
		gridBagConstraints = new GridBagConstraints();						/* restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		this.add(emptyLabel,gridBagConstraints);
		
		//zmx modified user network
		gridBagConstraints = new GridBagConstraints();						/* restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		
		
		modifiedUserNetworkCheckBox = new JCheckBox("Modified User Network");
		modifiedUserNetworkCheckBox.setBackground(Color.WHITE);
		this.add(modifiedUserNetworkCheckBox, gridBagConstraints);
		
		
		modifiedUserNetworkInputFileTextField = new JTextField("Upload data file for modified user network:");
		modifiedUserNetworkInputFileTextField.setEnabled(false);
		modifiedUserNetworkInputFileButton = new JButton("Choose");
		modifiedUserNetworkInputFileButton.setEnabled(false);
		
		modifiedUserNetworkInputFileButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
							JFrame frame = new JFrame();
							FileDialog fileDialog = new FileDialog(frame);
							fileDialog.setVisible(true);
							String selectedDir = fileDialog.getDirectory();
							String selectedFile = fileDialog.getFile();							
							if(selectedFile != null)							
								modifiedUserNetworkInputFileTextField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
					    }
				}
		);
		
		gridBagConstraints= new GridBagConstraints();							/*restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx=1;
		gridBagConstraints.gridwidth=5;
		gridBagConstraints.gridy=7;
		this.add(modifiedUserNetworkInputFileTextField,gridBagConstraints);
		
		gridBagConstraints= new GridBagConstraints();							/*restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		//gridBagConstraints.gridx = 6;
		//gridBagConstraints.gridy = 5;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0,10,0,10);
		this.add(modifiedUserNetworkInputFileButton, gridBagConstraints);
		
		modifiedUserNetworkCheckBox.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Boolean modifiedUserNetworkSelect=modifiedUserNetworkCheckBox.isSelected();
						modifiedUserNetworkInputFileTextField.setEnabled(modifiedUserNetworkSelect);
						modifiedUserNetworkInputFileButton.setEnabled(modifiedUserNetworkSelect);
					}
				});
		
		
		
		
		
		
		
		
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		/* number of simulations*/
		this.numberOfSimulationLabel = new JLabel("Number of simulations:");
		this.numberOfSimulationSpinner = new JSpinner();
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 10000, 1);
		this.numberOfSimulationSpinner.setModel(spinnerModel);
		
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		//ZMX
		//gridBagConstraints.gridy = 5;
		//gridBagConstraints.gridy = 6;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		this.add(this.numberOfSimulationLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		//ZMX
		//gridBagConstraints.gridy = 5;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridx = 1;
		this.add(this.numberOfSimulationSpinner, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/

		/* whether store intermediate results (generated networks) of Meta-GRN*/
		this.storeNetworkCheckBox = new JCheckBox("Store Networks");
		this.storeNetworkCheckBox.setBackground(Color.WHITE);
		
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//ZMX
		//gridBagConstraints.gridy = 6;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		this.add(this.storeNetworkCheckBox, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		this.generateNetworkButton = new JButton("Generate Networks");
		this.generateNetworkButton.setEnabled(true);
		this.metaGrnButton = new JButton("Rank Networks");
		this.metaGrnButton.setEnabled(false); /* only after generating networks, we can use Meta-GRN*/
		this.bottomButtonPanel = new JPanel();
		this.bottomButtonPanel.setBackground(Color.WHITE);
		this.bottomButtonPanel.add(this.generateNetworkButton);
		this.bottomButtonPanel.add(this.metaGrnButton);
		
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
//		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.insets = new Insets(0,0,15,0);
		//ZMX
		//gridBagConstraints.gridy = 7;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		this.add(this.bottomButtonPanel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/* restore default*/
		
		
		// /** 
		//  * Actions
		//  * */
		
		// this.generateDataOnlyCheckBox.addActionListener(
		// 	new ActionListener() {
		// 		public void actionPerformed(ActionEvent e) {
		// 			if(generateDataOnlyCheckBox.isSelected())
		// 			{
		// 				generateNetworkButton.setEnabled(false);
		// 				metaGrnButton.setEnabled(true);
		// 				Component[] components = networkFormatButtonPanel.getComponents();
		// 				for(Component component : components)
		// 					component.setEnabled(true);
		// 			}
		// 			else
		// 			{
		// 				generateNetworkButton.setEnabled(true);
		// 				metaGrnButton.setEnabled(false);
		// 				Component[] components = networkFormatButtonPanel.getComponents();
		// 				for(Component component : components)
		// 					component.setEnabled(false);
		// 			}
		// 		}
		// 	}
		// );
		
		this.generateNetworkButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					generateNetworkButton.setEnabled(false);
					String inputFilePath = inputFileTextField.getText();
					InputData originalData = null;
					try {
						FileReader inputFileReader = new FileReader(inputFilePath);
						originalData = CommonUtil.readInput(inputFileReader, withheaderCheckBox.isSelected()/*withHeader*/, rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header")/*geneNameAreColumnHeader*/);
						inputDataMatrix = originalData.getData();
						System.out.println("generateNetworkButton: inputdataMatrix size: "+inputDataMatrix.get(0).size());
						
						if(dbnAlgorithmCheckBox.isSelected())
						{
							GrnDbn.runByMeta = true;
							GrnDbn grnDbn = new GrnDbn(new JFrame());
							grnDbn.getDataFilePathDbn().setText(inputFilePath);
							grnDbn.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
							if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
								grnDbn.columnHeaderRadioButton.setSelected(true);
							else
								grnDbn.rowHeaderRadioButton.setSelected(true);
							//grnDbn.
							grnDbn.frame_dbn.setVisible(true);
						}
						
						if(rfAlgorithmCheckBox.isSelected())
						{
							GrnRf.runByMeta = true;
							GrnRf grnRf = new GrnRf(new JFrame());
							grnRf.getDataFilePathField().setText(inputFilePath);
							grnRf.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
							if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
								grnRf.columnHeaderRadioButton.setSelected(true);
							else
								grnRf.rowHeaderRadioButton.setSelected(true);
							grnRf.frameRf.setVisible(true);
						}
						
						if(enAlgorithmCheckBox.isSelected())
						{
							GrnElasticNet.runByMeta = true;
							GrnElasticNet grnElasticNet = new GrnElasticNet(new JFrame());
							grnElasticNet.getDataFilePathField().setText(inputFilePath);
							grnElasticNet.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
							if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
								grnElasticNet.columnHeaderRadioButton.setSelected(true);
							else
								grnElasticNet.rowHeaderRadioButton.setSelected(true);
							grnElasticNet.frameElasticNet.setVisible(true);
						}
						
						if(lassoAlgorithmCheckBox.isSelected())
						{
							GrnLasso.runByMeta = true;
							GrnLasso grnLasso = new GrnLasso(new JFrame());
							grnLasso.getDataFilePathField().setText(inputFilePath);
							grnLasso.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
							if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
								grnLasso.columnHeaderRadioButton.setSelected(true);
							else
								grnLasso.rowHeaderRadioButton.setSelected(true);
							grnLasso.frameLasso.setVisible(true);
						}
						
						if(lassoDelayAlgorithmCheckBox.isSelected())
						{
							GrnTimeDelayLasso.runByMeta = true;
							GrnTimeDelayLasso grnTimeDelayLasso = new GrnTimeDelayLasso(new JFrame());
							grnTimeDelayLasso.getInputFilePathTextField().setText(inputFilePath);
							grnTimeDelayLasso.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
							if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
								grnTimeDelayLasso.columnHeaderRadioButton.setSelected(true);
							else
								grnTimeDelayLasso.rowHeaderRadioButton.setSelected(true);
							grnTimeDelayLasso.frame_lassoDelay.setVisible(true);
						}
						
						if(ridgeAlgorithmCheckBox.isSelected())
						{
							GrnRidge.runByMeta = true;
							GrnRidge grnRidge = new GrnRidge(new JFrame());
							grnRidge.getInputFilePathField().setText(inputFilePath);
							grnRidge.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
							if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
								grnRidge.columnHeaderRadioButton.setSelected(true);
							else
								grnRidge.rowHeaderRadioButton.setSelected(true);
							grnRidge.frame_ridge.setVisible(true);
						}
						
						/* only after generating networks, we can use Meta-GRN*/
						//TODO: not reasonable, need change
						metaGrnButton.setEnabled(true);
						
					} catch(FileNotFoundException fnfex) {
						JOptionPane.showMessageDialog(null, fnfex.getMessage(), "FileNotFound", JOptionPane.ERROR_MESSAGE);
					} catch(BadInputFormatException badInputFormatEx) {
						JOptionPane.showMessageDialog(null, badInputFormatEx.getMessage(), "BadInputFormat", JOptionPane.ERROR_MESSAGE);
					} catch(IOException ioex) {
						JOptionPane.showMessageDialog(null, ioex.getMessage(), "IOException", JOptionPane.ERROR_MESSAGE);
					} finally {
						generateNetworkButton.setEnabled(true);
					}
				}
			}
		);
		
//		this.metaGrnButton.addActionListener(
//			new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					try {
//						/* perform all algorithms on the input data and save each output to a separate file*/
//						ArrayList<ArrayList<Double>> inputDataMatrix = new ArrayList<ArrayList<Double>>();
//						String inputFilePath = inputFileTextField.getText();
//						BufferedReader brInputFile = new BufferedReader(new FileReader(inputFilePath));
//						String oneLine = "";
//						while ((oneLine = brInputFile.readLine()) != null) {
//							ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
//							Scanner sc = new Scanner(oneLine);
//							while(sc.hasNext()) {
//								oneLineArrayList.add(sc.nextDouble());
//							}
//							inputDataMatrix.add(oneLineArrayList);
//							sc.close();
//						}
//						brInputFile.close();
//						
//						//TODO: do not use default setting 
//						
//						/* random forest*/
//						ArrayList<Integer> inputIdx_RF = new ArrayList<Integer>();
//						int nbGenes_RF = inputDataMatrix.get(0).size();
//						for(int i = 1; i<= nbGenes_RF; i++)
//							inputIdx_RF.add(i);
//						RandomForest rf = new RandomForest(inputDataMatrix, inputIdx_RF, "RF", "sqrt", 1000);
//						rf.run();
//						
//						/* elastic net*/
//						ElasticNet en = new ElasticNet(inputDataMatrix);
//						en.run();
//						
//						/* lasso*/
//						Lasso lasso = new Lasso(inputDataMatrix);
//						lasso.run();
//						
//						//TEST
//						/* ridge*/
//						//TODO: extract ridge algorithm logic part and use data structure instead of file path as argument
////						RidgeRegression ridge = new RidgeRegression(inputDataMatrix);
////						ridge.run();
//						
//						/* dbn*/
//						//TODO: extract dbn algorithm logic part and use data structure instead of file path as argument
////						DynamicBayesianNetwork dbn = new DynamicBayesianNetwork(inputFilePath ,150000);
////						dbn.dbnMcmcWithOutPrior();
////						dbn.run();
//						
//						/* save output of each algorithm to a seperate file*/
//						double[][] network_RF = rf.getFinalNetwork();
//						int numberOfGenes_RF = network_RF.length;
//						
//						PrintStream resultFilePrinter_RF = new PrintStream(new File("RF.tsv"));
//						for(int m=0; m<numberOfGenes_RF; m++)
//						{
//							for(int n=0; n<numberOfGenes_RF; n++) 
//							{   
//								if((int)network_RF[m][n] == 1) 
//									resultFilePrinter_RF.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
//								else
//									resultFilePrinter_RF.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
//								
//								resultFilePrinter_RF.println();
//							} //						} //						resultFilePrinter_RF.close(); //						//						int[][] network_EN = en.getFinalNetwork();
//						int numberOfGenes_EN = network_EN.length;
//						
//						PrintStream resultFilePrinter_EN = new PrintStream(new File("EN.tsv"));
//						for(int m=0; m<numberOfGenes_EN; m++)
//						{
//							for(int n=0; n<numberOfGenes_EN; n++) 
//							{   
//								if(network_EN[m][n] == 1) 
//									resultFilePrinter_EN.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
//								else
//									resultFilePrinter_EN.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
//								
//								resultFilePrinter_EN.println();
//							}
//						}
//						resultFilePrinter_EN.close();
//						
//						int[][] network_LASSO = lasso.getFinalNetwork();
//						int numberOfGenes_LASSO = network_LASSO.length;
//						
//						PrintStream resultFilePrinter_LASSO = new PrintStream(new File("LASSO.tsv"));
//						for(int m = 0; m< numberOfGenes_LASSO; m++)
//						{
//							for(int n = 0; n< numberOfGenes_EN; n++)
//							{
//								if(network_EN[m][n] == 1)
//									resultFilePrinter_LASSO.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
//								else
//									resultFilePrinter_LASSO.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
//								
//								resultFilePrinter_LASSO.println();
//							}
//						}
//						resultFilePrinter_LASSO.close();
//						
////						int[][] network_RIDGE = ridge.getFinalNetwork();
////						int numberOfGenes_RIDGE = network_RIDGE.length;
////						
////						PrintStream resultFilePrinter_RIDGE = new PrintStream(new File("RIDGE.tsv"));
////						
////						for(int m = 0; m< numberOfGenes_RIDGE; m++)
////						{
////							for(int n = 0; n< numberOfGenes_RIDGE; n++)
////							{
////								if(network_RIDGE[m][n] == 1)
////									resultFilePrinter_RIDGE.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
////								else
////									resultFilePrinter_RIDGE.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
////								
////								resultFilePrinter_RIDGE.println();
////							}
////						}
////						resultFilePrinter_RIDGE.close();
//						
////						int[][] network_DBN = dbn.getDbnNetwork();
////						int numberOfGenes_DBN = network_DBN.length;
////						
////						PrintStream resultFilePrinter_DBN = new PrintStream(new File("DBN.tsv"));
////						
////						for(int m = 0; m< numberOfGenes_DBN; m++)
////						{
////							for(int n = 0; n< numberOfGenes_DBN; n++)
////							{
////								if(network_DBN[m][n] == 1)
////									resultFilePrinter_DBN.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
////								else
////									resultFilePrinter_DBN.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
////								
////								resultFilePrinter_DBN.println();
////							}
////						}
////						resultFilePrinter_DBN.close();
		this.metaGrnButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						String networkName = "";
						try {			
						// if(generateDataOnlyCheckBox.isSelected())	/* synthesize data from network directly*/
						// {
						// 	if(networkFormatButtonGroup.getSelection().getActionCommand().equals("Matrix"))
						// 	{
						// 		/* transfer to tsv format first*/
						// 		//TODO: check with gene name or not
						// 		String inputNetworkFilePath = inputFileTextField.getText();
						// 		BufferedReader brInputNetworkFile = new BufferedReader(new FileReader(inputNetworkFilePath));
						// 		String line = "";
						// 		ArrayList<ArrayList<Integer>> networkMatrix = new ArrayList<ArrayList<Integer>>();
						// 		while((line = brInputNetworkFile.readLine()) != null) {
						// 			if(!line.equals(""))
						// 			{
						// 				ArrayList<Integer> oneLineOfNetworkMatrix = new ArrayList<Integer>();
						// 				Scanner sc = new Scanner(line);
						// 				while(sc.hasNext()) {
						// 					Integer ele = sc.nextInt();	/* network is 0-1 matrix*/
						// 					oneLineOfNetworkMatrix.add(ele);
						// 				}
						// 				networkMatrix.add(oneLineOfNetworkMatrix);
						// 			}
						// 		}
						// 		brInputNetworkFile.close();
						// 	
						// 		PrintStream psTsvFormatNetworkFile = new PrintStream("TSV");
						// 		File tsvInputNetworkFile = new File("TSV");
						// 		tsvInputNetworkFile.deleteOnExit();
						// 		int numberOfGenes = networkMatrix.size();
						// 		for(int m=0; m<numberOfGenes; m++)
						// 		{
						// 			for(int n=0; n<numberOfGenes; n++) 
						// 			{   
						// 				if(networkMatrix.get(m).get(n).intValue() == 1) 
						// 					psTsvFormatNetworkFile.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
						// 				else
						// 					psTsvFormatNetworkFile.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
						// 				
						// 				psTsvFormatNetworkFile.println();
						// 			}
						// 		}
						// 		psTsvFormatNetworkFile.close();
						// 		
						// 		/* load TSV format network file*/
						// 		StructureElement metaNetwork = loadStructureItem("NETWORK", new File("TSV").toURI().toURL(), ImodNetwork.TSV);
						// 		metaNetwork.getNetwork().removeAutoregulatoryInteractions();
						// 		
						// 		/* Create a new dynamic network from a static one and initialize its parameters.*/
						// 		DynamicalModelElement metaDynamicNetwork = new DynamicalModelElement(metaNetwork);
						// 		metaDynamicNetwork.getGeneNetwork().randomInitialization();
						// 		
						// 		if (metaNetwork.hasChildren())
						// 		{
						// 			for (IElement element : metaNetwork.getChildren())
						// 				metaDynamicNetwork.addChild(element);
						// 		}
						// 		
						// 		HashMap<String, NetworkElement> dynamicNeworks = new HashMap<String, NetworkElement>();
						// 		
						// 		dynamicNeworks.put("NETWORK", metaDynamicNetwork);
						// 		
						// 		generateDREAM3GoldStandard(dynamicNeworks, Integer.parseInt(numberOfSimulationSpinner.getValue().toString()), inputDataMatrix);
						// 	}
						// 	else if(networkFormatButtonGroup.getSelection().getActionCommand().equals("TSV"))
						// 	{
						// 		/* load directly*/
						// 		String inputNetworkFilePath = inputFileTextField.getText();
						// 		StructureElement metaNetwork = loadStructureItem("NETWORK", new File(inputNetworkFilePath).toURI().toURL(), ImodNetwork.TSV);
						// 		metaNetwork.getNetwork().removeAutoregulatoryInteractions();
						// 		
						// 		/* Create a new dynamic network from a static one and initialize its parameters.*/
						// 		DynamicalModelElement metaDynamicNetwork = new DynamicalModelElement(metaNetwork);
						// 		metaDynamicNetwork.getGeneNetwork().randomInitialization();
						// 		
						// 		if (metaNetwork.hasChildren())
						// 		{
						// 			for (IElement element : metaNetwork.getChildren())
						// 				metaDynamicNetwork.addChild(element);
						// 		}
						// 		
						// 		HashMap<String, NetworkElement> dynamicNeworks = new HashMap<String, NetworkElement>();
						// 		
						// 		dynamicNeworks.put("NETWORK", metaDynamicNetwork);
						// 		
						// 		generateDREAM3GoldStandard(dynamicNeworks, Integer.parseInt(numberOfSimulationSpinner.getValue().toString()), inputDataMatrix);
						// 	}
						// } 
						// else
						// {
							DynamicalModelElement metaDynamicNetwork_RF = null;
							DynamicalModelElement metaDynamicNetwork_EN = null;
							DynamicalModelElement metaDynamicNetwork_LASSO = null;
							DynamicalModelElement metaDynamicNetwork_LASSO_DELAY = null;
							DynamicalModelElement metaDynamicNetwork_DBN = null;
							DynamicalModelElement metaDynamicNetwork_RIDGE = null;
							//ZMX
							DynamicalModelElement metaDynamicNetwork_USER_Original = null;
							DynamicalModelElement metaDynamicNetwork_USER_Modified = null;
							
							if(rfAlgorithmCheckBox.isSelected())
							{
								networkName = "RF";
								StructureElement metaNetwork_RF = loadStructureItem(networkName, new File("RF.tsv").toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_RF.getNetwork().removeAutoregulatoryInteractions();
								
								/* Create a new dynamic network from a static one and initialize its parameters.*/
								metaDynamicNetwork_RF = new DynamicalModelElement(metaNetwork_RF);
								metaDynamicNetwork_RF.getGeneNetwork().randomInitialization();
								
								if (metaNetwork_RF.hasChildren())
								{
									for (IElement element : metaNetwork_RF.getChildren())
										metaDynamicNetwork_RF.addChild(element);
								}
							}
							
							if(enAlgorithmCheckBox.isSelected())
							{
								networkName = "EN";
								StructureElement metaNetwork_EN = loadStructureItem(networkName, new File("EN.tsv").toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_EN.getNetwork().removeAutoregulatoryInteractions();
								
								metaDynamicNetwork_EN = new DynamicalModelElement(metaNetwork_EN);
								metaDynamicNetwork_EN.getGeneNetwork().randomInitialization();
								
								if(metaNetwork_EN.hasChildren())
								{
									for (IElement element : metaNetwork_EN.getChildren())
										metaDynamicNetwork_EN.addChild(element);
								}
							}
							
							if(lassoAlgorithmCheckBox.isSelected())
							{
								networkName = "LASSO";
								StructureElement metaNetwork_LASSO = loadStructureItem(networkName, new File("LASSO.tsv").toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_LASSO.getNetwork().removeAutoregulatoryInteractions();
								
								metaDynamicNetwork_LASSO = new DynamicalModelElement(metaNetwork_LASSO);
								metaDynamicNetwork_LASSO.getGeneNetwork().randomInitialization();
								
								if(metaNetwork_LASSO.hasChildren())
								{
									for (IElement element : metaNetwork_LASSO.getChildren())
										metaDynamicNetwork_LASSO.addChild(element);
								}
							}

							if(lassoDelayAlgorithmCheckBox.isSelected())
							{
								networkName = "LASSO_DELAY";
								StructureElement metaNetwork_LASSO_DELAY = loadStructureItem(networkName, new File("LASSO_DELAY.tsv").toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_LASSO_DELAY.getNetwork().removeAutoregulatoryInteractions();
								
								metaDynamicNetwork_LASSO_DELAY = new DynamicalModelElement(metaNetwork_LASSO_DELAY);
								metaDynamicNetwork_LASSO_DELAY.getGeneNetwork().randomInitialization();
								
								if(metaNetwork_LASSO_DELAY.hasChildren())
								{
									for (IElement element : metaNetwork_LASSO_DELAY.getChildren())
										metaDynamicNetwork_LASSO_DELAY.addChild(element);
								}
							}
							
							if(dbnAlgorithmCheckBox.isSelected())
							{
								networkName = "DBN";
								StructureElement metaNetwork_DBN = loadStructureItem(networkName, new File("DBN.tsv").toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_DBN.getNetwork().removeAutoregulatoryInteractions();
								
								/* Create a new dynamic network from a static one and initialize its parameters.*/
								metaDynamicNetwork_DBN = new DynamicalModelElement(metaNetwork_DBN);
								metaDynamicNetwork_DBN.getGeneNetwork().randomInitialization();
								
								if (metaNetwork_DBN.hasChildren())
								{
									for (IElement element : metaNetwork_DBN.getChildren())
										metaDynamicNetwork_DBN.addChild(element);
								}
							}
							
							if(ridgeAlgorithmCheckBox.isSelected())
							{
								networkName = "RIDGE";
								StructureElement metaNetwork_RIDGE = loadStructureItem(networkName, new File("RIDGE.tsv").toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_RIDGE.getNetwork().removeAutoregulatoryInteractions();
								
								/* Create a new dynamic network from a static one and initialize its parameters.*/
								metaDynamicNetwork_RIDGE = new DynamicalModelElement(metaNetwork_RIDGE);
								metaDynamicNetwork_RIDGE.getGeneNetwork().randomInitialization();
								
								if (metaNetwork_RIDGE.hasChildren())
								{
									for (IElement element : metaNetwork_RIDGE.getChildren())
										metaDynamicNetwork_RIDGE.addChild(element);
								}
							}
							
							
							//ZMX original user network
							if(originalUserNetworkCheckBox.isSelected())
							{
								// liuxingliang
								networkName = "Original_USER_NETWORK";
								/* load directly*/
								String originalUserNetworkInputFilePath = originalUserNetworkInputFileTextField.getText();
								StructureElement metaNetwork_USER_Original = loadStructureItem(networkName, new File(originalUserNetworkInputFilePath).toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_USER_Original.getNetwork().removeAutoregulatoryInteractions();
								
								/* Create a new dynamic network from a static one and initialize its parameters.*/
								metaDynamicNetwork_USER_Original = new DynamicalModelElement(metaNetwork_USER_Original);
								metaDynamicNetwork_USER_Original.getGeneNetwork().randomInitialization();
								
								if (metaNetwork_USER_Original.hasChildren())
								{
									for (IElement element : metaNetwork_USER_Original.getChildren())
										metaDynamicNetwork_USER_Original.addChild(element);
								}
							}
							
							//ZMX modified user network
							if(modifiedUserNetworkCheckBox.isSelected())
							{
								// liuxingliang
								networkName = "Modified_USER_NETWORK";
								
								/* load directly*/
								String modifiedUserNetworkInputFilePath = modifiedUserNetworkInputFileTextField.getText();
								StructureElement metaNetwork_USER_modified = loadStructureItem(networkName, new File(modifiedUserNetworkInputFilePath).toURI().toURL(), ImodNetwork.TSV);
								metaNetwork_USER_modified.getNetwork().removeAutoregulatoryInteractions();
								
								/* Create a new dynamic network from a static one and initialize its parameters.*/
								metaDynamicNetwork_USER_Modified = new DynamicalModelElement(metaNetwork_USER_modified);
								metaDynamicNetwork_USER_Modified.getGeneNetwork().randomInitialization();
								
								if (metaNetwork_USER_modified.hasChildren())
								{
									for (IElement element : metaNetwork_USER_modified.getChildren())
										metaDynamicNetwork_USER_Modified.addChild(element);
								}
								
							}
							
							HashMap<String, NetworkElement> dynamicNeworks = new HashMap<String, NetworkElement>();
							
							dynamicNeworks.put("RF", metaDynamicNetwork_RF);
							dynamicNeworks.put("EN", metaDynamicNetwork_EN);
							dynamicNeworks.put("LASSO", metaDynamicNetwork_LASSO);
							dynamicNeworks.put("LASSO_DELAY", metaDynamicNetwork_LASSO_DELAY);
							dynamicNeworks.put("DBN", metaDynamicNetwork_DBN);
							dynamicNeworks.put("RIDGE", metaDynamicNetwork_RIDGE);
							//ZMX
							dynamicNeworks.put("Original_USER_NETWORK", metaDynamicNetwork_USER_Original);
							dynamicNeworks.put("Modified_USER_NETWORK", metaDynamicNetwork_USER_Modified);
							
							generateDREAM3GoldStandard(dynamicNeworks, Integer.parseInt(numberOfSimulationSpinner.getValue().toString()), inputDataMatrix);
					// 	}
						
					} catch (MalformedURLException malFormEx) {
						JOptionPane.showMessageDialog(null, malFormEx.getMessage(), networkName + ":MalformedURLException", JOptionPane.ERROR_MESSAGE);
					} catch (ParseException parseEx) {
						JOptionPane.showMessageDialog(null, parseEx.getMessage(), networkName + ":BadNetworkFormat", JOptionPane.ERROR_MESSAGE);
					} catch(FileNotFoundException fnfex) {
						JOptionPane.showMessageDialog(null, fnfex.getMessage(), networkName + ":FileNotFound", JOptionPane.ERROR_MESSAGE);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), networkName + ":Exception", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		);

	}
	
	/**
	 * Open a dialog to generate benchmarks.
	 * @throws Exception
	 */
	public static void generateDREAM3GoldStandard(HashMap<String, NetworkElement> networks, int numberOfSimulations, ArrayList<ArrayList<Double>> inputData /*genename is column header*/) throws Exception
	{
		System.out.println("METAGRNDISPLAY: matrix size is: "+inputData.get(0).size());
		MySimulation rd = new MySimulation(new Frame(), networks, numberOfSimulations, inputData);
		rd.setVisible(true);
	}
	
	public static StructureElement loadStructureItem(String name, URL absPath, Integer format) throws 	FileNotFoundException,
	ParseException,
	Exception
	{
		StructureElement network = new StructureElement(name);

		network.load(absPath, format);
		network.getNetwork().setId(name);

		// As DOT format has a place where network Id is defined, we take it
		// as label for the item displayed on the desktop.
		if (format == Structure.DOT)
			network.setText(network.getNetwork().getId());

		return network;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public NetworkElement getElement() {
		return element;
	}

	public void setElement(NetworkElement element) {
		this.element = element;
	}
	
	/**
	 * add radio button with <b>buttonText</b> to a the <b>buttonPanel</b> and <b>buttonGroup</b>
	 * @param buttonText
	 * @param buttonGroup
	 * @param buttonPanel 
	 * @param selected the radio button is selected or not
	 * @param isEnable whether the radio button is enable or not
	 */
	private void addRadioButton(String buttonText, ButtonGroup buttonGroup, JPanel buttonPanel, boolean selected, boolean isEnable) {
		JRadioButton radioButton = new JRadioButton(buttonText, selected);
		radioButton.setBackground(Color.WHITE);
		radioButton.setEnabled(isEnable);
		
		radioButton.setActionCommand(buttonText);
		
		buttonGroup.add(radioButton);
		buttonPanel.add(radioButton);
	}

	/**
	 * This class allow to generate a dynamical gene network model in a new thread.
	 */
	private static class MyKineticModelGeneration extends SwingWorker<Void, Void>
	{
		private static Logger log_ = Logger.getLogger(MyKineticModelGeneration.class.getName());
		
		/** Dialog displayed during the process */
		private Wait wDialog_;
		/** Do not display waiting box for network size < 200 */
		private int wDialogMinNetworkSize_ = 1000;
		
		/** Element to export */
		private NetworkElement element_;
		
		// ----------------------------------------------------------------------------
		// PUBLIC METHODS
	  
		public MyKineticModelGeneration(Wait gui)
		{
			this.wDialog_ = gui;
		}
		
		// ----------------------------------------------------------------------------
		// PROTECTED METHODS
		
		protected boolean displayWaitingBox()
		{
			int N = 0;
			if (element_ instanceof StructureElement)
				N = ((StructureElement) element_).getNetwork().getSize();
			else if (element_ instanceof DynamicalModelElement)
				N = ((DynamicalModelElement) element_).getGeneNetwork().getSize();
			else
				log_.log(Level.WARNING, "IONetwork::displayWaitingBox(): Unknown item type");
			
			return (N > wDialogMinNetworkSize_);
		}
		
		// ----------------------------------------------------------------------------
	  
		@Override
		protected Void doInBackground() throws Exception
		{
			GnwGuiSettings global = GnwGuiSettings.getInstance();
			if (element_ instanceof StructureElement)
			{
				StructureElement staticNetwork = (StructureElement) element_;
				
				// Create a new dynamic network from a static one and initialize its parameters.
				DynamicalModelElement grnItem = new DynamicalModelElement(staticNetwork);
				grnItem.getGeneNetwork().randomInitialization();
				
				if (element_.hasChildren())
				{
					for (IElement e : element_.getChildren())
						grnItem.addChild(e);
				}
				
				// Delete the structure item and replace it by a new dynamical
				// network item on the desktop
				if (element_.getFather() != null) {
					int index = staticNetwork.getFather().getChildren().indexOf(element_);
					staticNetwork.getFather().getChildren().remove(element_);
					staticNetwork.getFather().getChildren().add(index, grnItem);
				}
				
				global.getNetworkDesktop().replaceItem(staticNetwork, grnItem);
			}
			else if (element_ instanceof DynamicalModelElement)
			{
				DynamicalModelElement grn = (DynamicalModelElement) element_;
				grn.getGeneNetwork().randomInitialization();
				
				//TEST
				grn.setNetworkViewer(null);
//				grn.networkViewer_ = null; // delete the viewer to regenerate it next time
			}	
			
			return null;
		}
		
		// ----------------------------------------------------------------------------
	  
		@Override
		protected void done()
		{
			wDialog_.stop();
		}
	}
}


	


