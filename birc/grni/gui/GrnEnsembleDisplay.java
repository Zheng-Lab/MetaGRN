package birc.grni.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GrnEnsembleDisplay {
	protected JFrame frameEnsemble;
	protected GrnGuiHeader headerEnsemble = new GrnGuiHeader();
	
	protected JPanel headerPanelEnsemble;
	protected JPanel textFieldPanel;
	protected JPanel ensembleMethodPanel;
	protected JPanel bottomPanel;
	
	protected JTextField dataFilePathField;
	
//	protected JTextField nbTreesField;
	protected JComboBox<String> treeMethodComboBox;
	
	protected JButton selectDataFilePathButton;
	
	protected JLabel dataFilePathLabel;
	protected JLabel nbTreesLabel;
	protected JLabel treeMethodLabel;
	
	protected static JButton startButton;
	
	protected ButtonGroup rowColumnChooseButtonGroup;
	private JLabel rowColumnChooseLabel;
	private JPanel rowColumnChooseButtonPanel;
	
	protected JRadioButton rowHeaderRadioButton;
	protected JRadioButton columnHeaderRadioButton;
	
	protected JCheckBox withheaderCheckBox;
	
	public static JProgressBar progressBarEnsemble;			/* progress bar*/
	
	protected JLabel chooseAlgorithmLabel;
	protected JCheckBox dbnAlgorithmCheckBox;				/* choose to investigate dynamic bayesian network algorithm*/
	protected JCheckBox rfAlgorithmCheckBox;				/* choose to investigate random forest algorithm*/
	protected JCheckBox enAlgorithmCheckBox;				/* choose to investigate elastic net algorithm*/
	protected JCheckBox lassoAlgorithmCheckBox;			/* choose to investigate lasso algorithm*/
	protected JCheckBox lassoDelayAlgorithmCheckBox;		/* choose to investigate lasso delay algorithm*/
	protected JCheckBox ridgeAlgorithmCheckBox;			/* choose to investigate ridge algorithm*/
	
	public JTextField getDataFilePathField() {
		return this.dataFilePathField;
	}
	
	public GrnEnsembleDisplay(JFrame frame)
	{
		frameEnsemble = frame;
		frameEnsemble.setBackground(Color.WHITE);
		frameEnsemble.setBounds(200, 200, 900, 500);
		
		frameEnsemble.setTitle("Ensemble Inference");
		
		// add header panel
		headerPanelEnsemble = new JPanel();
		headerPanelEnsemble.setBackground(Color.WHITE);
		CardLayout cardLayout = new CardLayout();
		headerPanelEnsemble.setLayout(cardLayout);
		frameEnsemble.getContentPane().add(headerPanelEnsemble, BorderLayout.NORTH);
		headerPanelEnsemble.add(headerEnsemble, "header");
		
		// create text field panel and add component into it
		textFieldPanel = new JPanel();
		textFieldPanel.setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		textFieldPanel.setLayout(gridBagLayout);
		
		frameEnsemble.getContentPane().add(textFieldPanel, BorderLayout.CENTER);
		
		/* grid layout constraints for textFieldPanel*/
		GridBagConstraints textFieldLayoutConstraints = new GridBagConstraints(); 
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		
		
		/* input file path components*/
		dataFilePathLabel = new JLabel();
		dataFilePathLabel.setText("Full path of the input file");
		
		dataFilePathField = new JTextField();
		
		selectDataFilePathButton = new JButton();
		selectDataFilePathButton.setText("Select");
		selectDataFilePathButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e)
						{
							JFrame frame = new JFrame();							
							FileDialog fileDialog = new FileDialog(frame);
							fileDialog.setVisible(true);
							String selectedDir = fileDialog.getDirectory();
							String selectedFile = fileDialog.getFile();							
							if(selectedFile != null)							
								dataFilePathField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
							
							
						}
					}
				);
		
		textFieldLayoutConstraints.gridwidth = 1;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,10);
		textFieldPanel.add(dataFilePathLabel, textFieldLayoutConstraints);
		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
		
		textFieldLayoutConstraints.weightx = 1;									/* put all extra horizontal space in the file path field*/
		textFieldLayoutConstraints.gridwidth = 5;
		textFieldPanel.add(dataFilePathField, textFieldLayoutConstraints);
		textFieldLayoutConstraints.weightx = 0;									/* restore default*/
		
		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
		textFieldPanel.add(selectDataFilePathButton, textFieldLayoutConstraints);
		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
		textFieldLayoutConstraints.gridwidth = 1;								/* restore default*/
		
		/* whether the input file includes header or not*/
		withheaderCheckBox = new JCheckBox("Input with header");
		withheaderCheckBox.setBackground(Color.WHITE);
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 1;
//		textFieldLayoutConstraints.gridwidth = 1;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		textFieldPanel.add(withheaderCheckBox, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		
		/* radio button to choose gene names are row names or column names, 
		 * if gene names are row names, then time series are column names,
		 * so each column is a sample of a time point; otherwise, if
		 * gene names are column names, then time series are row names, then
		 * each row are a sample of a time point*/
		rowColumnChooseLabel = new JLabel();
		rowColumnChooseLabel.setText("Gene names are ");
		rowColumnChooseLabel.setBackground(Color.WHITE);
		
		rowColumnChooseButtonGroup = new ButtonGroup();
		rowColumnChooseButtonPanel = new JPanel();
		rowColumnChooseButtonPanel.setBackground(Color.WHITE);
		
		//column header radio button
		columnHeaderRadioButton = new JRadioButton("column header", true);
		rowColumnChooseButtonGroup.add(columnHeaderRadioButton);
		rowColumnChooseButtonPanel.add(columnHeaderRadioButton);
		columnHeaderRadioButton.setBackground(Color.WHITE);
		columnHeaderRadioButton.setActionCommand("column header");
		
		rowColumnChooseButtonGroup.add(columnHeaderRadioButton);
		rowColumnChooseButtonPanel.add(columnHeaderRadioButton);
		
		//row header radio button
		rowHeaderRadioButton = new JRadioButton("row header", true);
		rowColumnChooseButtonGroup.add(rowHeaderRadioButton);
		rowColumnChooseButtonPanel.add(rowHeaderRadioButton);
		rowHeaderRadioButton.setBackground(Color.WHITE);
		rowHeaderRadioButton.setActionCommand("row header");
		
		rowColumnChooseButtonGroup.add(rowHeaderRadioButton);
		rowColumnChooseButtonPanel.add(rowHeaderRadioButton);
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 2;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		textFieldPanel.add(rowColumnChooseLabel, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 1;
		textFieldLayoutConstraints.gridy = 2;
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
		textFieldPanel.add(rowColumnChooseButtonPanel, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		/* which algorithms we need to include in ensemble method*/
		
		chooseAlgorithmLabel = new JLabel("Algorithms included in ensemble method:");
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
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 3;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		textFieldPanel.add(chooseAlgorithmLabel, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();	 			/* restore default*/
		textFieldLayoutConstraints.fill = GridBagConstraints.BOTH;
		textFieldLayoutConstraints.weightx = 1;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 4;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		
		
		textFieldPanel.add(dbnAlgorithmCheckBox, textFieldLayoutConstraints);
		//textFieldLayoutConstraints.insets = new Insets(0,0,0,0);
		
		textFieldLayoutConstraints.gridx = 1;
		//textFieldLayoutConstraints.weightx=1;
		//textFieldLayoutConstraints.insets = new Insets(0,0,0,0);
		textFieldPanel.add(rfAlgorithmCheckBox, textFieldLayoutConstraints);
		
		
		textFieldLayoutConstraints.gridx = 2;
		textFieldPanel.add(enAlgorithmCheckBox, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints.gridx = 3;
		textFieldPanel.add(lassoAlgorithmCheckBox, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints.gridx = 4;
		textFieldPanel.add(lassoDelayAlgorithmCheckBox, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints.gridx = 5;
		textFieldPanel.add(ridgeAlgorithmCheckBox, textFieldLayoutConstraints);
		
		//TEST
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		frameEnsemble.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	
		Component componentStruct = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraintsStruct = new GridBagConstraints();
		gridBagConstraintsStruct.gridy = 0;
		gridBagConstraintsStruct.gridx = 0;
		bottomPanel.add(componentStruct, gridBagConstraintsStruct);
		
		
		GridBagConstraints bagConstraintsbottom = new GridBagConstraints();
		
		
		startButton = new JButton();
		startButton.setText("Start");
		//bottomPanel.add(startButton);
		bagConstraintsbottom = new GridBagConstraints();
		bagConstraintsbottom .fill = GridBagConstraints.CENTER;
		bagConstraintsbottom .weightx = 0.5;
		bagConstraintsbottom .gridy = 0;
		bagConstraintsbottom .gridx = 0;
		bagConstraintsbottom .insets = new Insets(10,30,10,50);
		bottomPanel.add(startButton, bagConstraintsbottom );
		
		/*progressBarEnsemble = new JProgressBar();
		progressBarEnsemble.setStringPainted(true);
		//bagConstraintsbottom.ipady =15;
		bagConstraintsbottom .gridx = 1;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .insets = new Insets(0,10,0,100);
		bottomPanel.add(progressBarEnsemble, bagConstraintsbottom);*/
		
		Component componentStruct2 = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraintsStruct2 = new GridBagConstraints();
		gridBagConstraintsStruct2.gridy = 2;
		gridBagConstraintsStruct2.gridx = 2;
		bottomPanel.add(componentStruct2, gridBagConstraintsStruct2);
	}
	
	/**
	 * add radio button with <b>buttonText</b> to a the <b>buttonPanel</b> and <b>buttonGroup</b>
	 * @param buttonText
	 * @param buttonGroup
	 * @param buttonPanel 
	 * @param selected the button is selected or not
	 */
	
	/**
	 * get the button group in the panel
	 * @return
	 */
	public ButtonGroup getRowColumnChooseButtonGroup() {
		return this.rowColumnChooseButtonGroup;
	}

}
