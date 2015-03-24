package birc.grni.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.*;

public class GrnElasticNetDisplay {
	
	protected JFrame frameElasticNet;
	protected GrnGuiHeader headerElasticNet = new GrnGuiHeader();
	
	protected JPanel headerPanelElasticNet;
	protected JPanel textFieldPanel;
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
	
	public static JProgressBar progressBarElasticNet;			/* progress bar*/
	
	public JTextField getDataFilePathField() {
		return this.dataFilePathField;
	}
	
	public GrnElasticNetDisplay(JFrame frame)
	{
		frameElasticNet = frame;
		frameElasticNet.setBackground(Color.WHITE);
		frameElasticNet.setBounds(200, 200, 600, 400);
		
		frameElasticNet.setTitle("ElasticNet Inference");
		
		// add header panel
		headerPanelElasticNet = new JPanel();
		headerPanelElasticNet.setBackground(Color.WHITE);
		CardLayout cardLayout = new CardLayout();
		headerPanelElasticNet.setLayout(cardLayout);
		frameElasticNet.getContentPane().add(headerPanelElasticNet, BorderLayout.NORTH);
		headerPanelElasticNet.add(headerElasticNet, "header");
		
		// create text field panel and add component into it
		textFieldPanel = new JPanel();
		textFieldPanel.setBackground(Color.WHITE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		textFieldPanel.setLayout(gridBagLayout);
		
		frameElasticNet.getContentPane().add(textFieldPanel, BorderLayout.CENTER);
		
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
							
							/* pop up the file chooser window*/
							//JFileChooser chooser = new JFileChooser();
							//chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							//chooser.setSelectedFile(new File("/Users/liuxingliang/Documents/workspace/GRNInfer_All/data/data_en.txt"));
						    //int returnVal = chooser.showOpenDialog(null);
//						    if(returnVal == JFileChooser.APPROVE_OPTION) {
//						            dataFilePathField.setText(chooser.getSelectedFile().getAbsolutePath());
//						    }
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
		
		//addRadioButton("column header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, true);
		//addRadioButton("row header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, false);
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 2;
//		textFieldLayoutConstraints.gridwidth = 1;
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
		
		/* number of trees components*/
//		nbTreesLabel = new JLabel();
//		nbTreesLabel.setText("Number of trees");
//		nbTreesField = new TextField();
//		nbTreesField.setText("1000");											/* by default 1000 trees*/
//		
//		textFieldLayoutConstraints.insets = new Insets(0,5,0,10);
//		textFieldPanel.add(nbTreesLabel, textFieldLayoutConstraints);
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
//		
//		textFieldLayoutConstraints.ipadx = 20;
//		textFieldPanel.add(nbTreesField, textFieldLayoutConstraints);
//		textFieldLayoutConstraints.ipadx = 0;									/* restore default*/
//		
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.RELATIVE;
//		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
//		textFieldPanel.add(new JLabel("(<= 10000)"), textFieldLayoutConstraints);
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
//
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
//		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
//		textFieldPanel.add(new JPanel(), textFieldLayoutConstraints);			/* fill the space*/
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
//		textFieldLayoutConstraints.gridwidth = 1;								/* restore default*/
//		
//		/* tree method components*/
//		treeMethodLabel = new JLabel();
//		treeMethodLabel.setText("Tree method");
//		treeMethodComboBox = new JComboBox<String>();
//		for(String method: GrnRf.treeMethods)
//			treeMethodComboBox.addItem(method);
//		
//		textFieldLayoutConstraints.insets = new Insets(0,5,0,10);
//		textFieldPanel.add(treeMethodLabel, textFieldLayoutConstraints);
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
//
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.RELATIVE;
//		textFieldPanel.add(treeMethodComboBox, textFieldLayoutConstraints);
//		
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
//		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
//		textFieldPanel.add(new JPanel(), textFieldLayoutConstraints);			/* fill the space*/
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
//		textFieldLayoutConstraints.gridwidth = 1;								/* restore default*/
		
		/* result save path components*/
//		resultSavePathLabel = new JLabel();
//		resultSavePathLabel.setText("<html>Output path where <br>result will be saved</html>");
//		resultSavePathField = new TextField();
//		selectResultSavePathButton = new JButton();
//		selectResultSavePathButton.setText("Select");
//		selectResultSavePathButton.addActionListener(
//					new ActionListener() {
//						public void actionPerformed(ActionEvent e)
//						{
//							/* pop up the file chooser window*/
//							JFileChooser chooser = new JFileChooser();
//						    int returnVal = chooser.showOpenDialog(null);
//						    if(returnVal == JFileChooser.APPROVE_OPTION) {
//						    	resultSavePathField.setText(chooser.getSelectedFile().getAbsolutePath());
//						    }
//						}
//					}
//				);
//		
//		textFieldLayoutConstraints.insets = new Insets(0,5,0,10);
//		textFieldPanel.add(resultSavePathLabel, textFieldLayoutConstraints);
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
//		
//		textFieldLayoutConstraints.weightx = 1;									
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.RELATIVE;
//		textFieldPanel.add(resultSavePathField, textFieldLayoutConstraints);
//		textFieldLayoutConstraints.weightx = 0;									/* restore default*/
//		
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
//		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
//		textFieldPanel.add(selectResultSavePathButton, textFieldLayoutConstraints);
//		textFieldLayoutConstraints.insets = new Insets(0,0,0,0);				/* restore default*/
		
		// bottom panel
		
		//TEST
//		bottomPanel = new JPanel();
//		bottomPanel.setBackground(Color.WHITE);
//		bottomPanel.setLayout(new GridLayout(1, 2));
//		frameRf.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
//		
//		startButton = new JButton();
//		startButton.setText("Start");
//		bottomPanel.add(startButton);
//		
//		exitButton = new JButton();
//		exitButton.setText("Exit");
//		bottomPanel.add(exitButton);	
		
		//TEST
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		frameElasticNet.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	
		Component componentStruct = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraintsStruct = new GridBagConstraints();
		gridBagConstraintsStruct.gridy = 0;
		gridBagConstraintsStruct.gridx = 1;
		bottomPanel.add(componentStruct, gridBagConstraintsStruct);
		
		startButton = new JButton();
		startButton.setText("Start");
		//bottomPanel.add(startButton);
		GridBagConstraints bagConstraintsbottom = new GridBagConstraints();
		bagConstraintsbottom .fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsbottom .weightx = 0.5;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .gridx = 0;
		bagConstraintsbottom .insets = new Insets(10,30,10,50);
		bottomPanel.add(startButton, bagConstraintsbottom );
		
		progressBarElasticNet = new JProgressBar();
		progressBarElasticNet.setStringPainted(true);
		//bagConstraintsbottom.ipady =15;
		bagConstraintsbottom .gridx = 1;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .insets = new Insets(0,10,0,100);
		bottomPanel.add(progressBarElasticNet, bagConstraintsbottom);
		
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
	private void addRadioButton(String buttonText, ButtonGroup buttonGroup, JPanel buttonPanel, boolean selected) {
		JRadioButton radioButton = new JRadioButton(buttonText, selected);
		radioButton.setBackground(Color.WHITE);
		
		radioButton.setActionCommand(buttonText);
		
		buttonGroup.add(radioButton);
		buttonPanel.add(radioButton);
	}
	
	/**
	 * get the button group in the panel
	 * @return
	 */
	public ButtonGroup getRowColumnChooseButtonGroup() {
		return this.rowColumnChooseButtonGroup;
	}
}