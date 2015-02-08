package birc.grni.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;



public class GrnDbnDisplay {
	
	protected JFrame frame_dbn;
	protected GrnGuiHeader header_dbn = new GrnGuiHeader();
	
	protected JPanel headerPanel_dbn;
	protected JPanel textFieldPanelDbn;
	protected JPanel bottomPanel;
	
	protected JTextField dataFilePathDbn;
	protected JTextField priorDataTextField;
	
	protected JLabel datafilePathLabelDbn;
	protected JLabel numGenesLabelDbn;
	protected JLabel priorDataLable;
	protected JLabel betaValueLabel;
	protected JLabel numIterationsLabel;
	protected JLabel resultPathLable;
	
	protected JCheckBox priorDataCheckBox;
	protected JButton dataFileButtonDbn;
	protected JButton priorSelectButton;
	protected static JButton startButton;
	protected JButton resultFileSelect;
	
	protected JSpinner betaSpinner;
	protected JSpinner iterationSpinner;
	
	protected ButtonGroup rowColumnChooseButtonGroup;
	private JLabel rowColumnChooseLabel;
	private JPanel rowColumnChooseButtonPanel;
	
	protected JCheckBox withheaderCheckBox;
	
	public static JProgressBar progressBarDbn;
	
	public GrnDbnDisplay(JFrame frame_1){
		
		frame_dbn = frame_1;
		frame_dbn.setBackground(Color.WHITE);
		frame_dbn.getContentPane().setBackground(Color.WHITE);
		frame_dbn.setSize(new Dimension(0,0));
		frame_dbn.setBounds(200,200,600,400);
		
		frame_dbn.setTitle("DBN Inference");
		
		/* header panel*/
		headerPanel_dbn = new JPanel();
		headerPanel_dbn.setBackground(Color.WHITE);
		final CardLayout cards_ = new CardLayout();
		headerPanel_dbn.setLayout(cards_);
		frame_dbn.getContentPane().add(headerPanel_dbn, BorderLayout.NORTH);
		headerPanel_dbn.add(header_dbn, "header");
		
		/* field panel and its components*/
		textFieldPanelDbn = new JPanel();
		textFieldPanelDbn.setBackground(Color.WHITE);
		textFieldPanelDbn.setLayout(new GridBagLayout());
		
		frame_dbn.getContentPane().add(textFieldPanelDbn,BorderLayout.CENTER);
		
		/* input file*/
		datafilePathLabelDbn = new JLabel();
		datafilePathLabelDbn.setText("Full path of the input file");
		
		GridBagConstraints bagConstraintsdbn = new GridBagConstraints();
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weightx = 0;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
		bagConstraintsdbn.gridy = 0;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.insets = new Insets(0,5,0,10);
		textFieldPanelDbn.add(datafilePathLabelDbn, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		dataFilePathDbn = new JTextField();
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weightx = 1;									/* put all extra horizontal space in the file path field*/
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 1;
		bagConstraintsdbn.gridy = 0;
		bagConstraintsdbn.gridwidth = 5;
		textFieldPanelDbn.add(dataFilePathDbn, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		dataFileButtonDbn = new JButton();
		dataFileButtonDbn.setText("Browse");								
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weightx = 0;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 6;
		bagConstraintsdbn.gridy = 0;
		bagConstraintsdbn.gridwidth = GridBagConstraints.REMAINDER;
		bagConstraintsdbn.insets = new Insets(0,10,0,10);
		
		textFieldPanelDbn.add(dataFileButtonDbn,bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		/* whether the input file includes header or not*/
		withheaderCheckBox = new JCheckBox("Input with header");
		withheaderCheckBox.setBackground(Color.WHITE);
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
		bagConstraintsdbn.gridy = 1;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.insets = new Insets(0,5,0,0);
		textFieldPanelDbn.add(withheaderCheckBox, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
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
		addRadioButton("column header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, true);
		addRadioButton("row header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, false);
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
		bagConstraintsdbn.gridy = 2;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.insets = new Insets(0,5,0,0);
		textFieldPanelDbn.add(rowColumnChooseLabel, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 1;
		bagConstraintsdbn.gridy = 2;
//		bagConstraintsdbn.gridwidth = GridBagConstraints.REMAINDER;
//		bagConstraintsdbn.insets = new Insets(0,0,0,0);
		textFieldPanelDbn.add(rowColumnChooseButtonPanel, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		// insert component to handle prior data //
		priorDataCheckBox = new JCheckBox();
		priorDataCheckBox.setText("Include prior data");
		priorDataCheckBox.setBackground(Color.WHITE);
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
		bagConstraintsdbn.gridy = 3;
		bagConstraintsdbn.insets = new Insets(0,5,0,0);
		textFieldPanelDbn.add(priorDataCheckBox, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		priorDataLable = new JLabel();
		priorDataLable.setText("Prior data file");
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weightx = 0;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.gridy = 4;
		bagConstraintsdbn.insets = new Insets(0,5,0,0);
		textFieldPanelDbn.add(priorDataLable, bagConstraintsdbn);
		//priorDataLable.setFocusable(false);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/

		priorDataTextField = new JTextField();
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weightx = 1;	/* feed with all extra space*/
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 1;
		bagConstraintsdbn.gridwidth = 5;
		bagConstraintsdbn.gridy = 4;
		bagConstraintsdbn.insets = new Insets(0,10,0,0);
		textFieldPanelDbn.add(priorDataTextField, bagConstraintsdbn);
		priorDataTextField.setEnabled(false);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		priorSelectButton = new JButton();
		priorSelectButton.setText("Browse");
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 6;
		bagConstraintsdbn.gridwidth = GridBagConstraints.REMAINDER;
		bagConstraintsdbn.gridy = 4;
		bagConstraintsdbn.insets = new Insets(0,10,0,10);
		textFieldPanelDbn.add(priorSelectButton, bagConstraintsdbn);
		priorSelectButton.setEnabled(false);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		betaValueLabel = new JLabel();
		betaValueLabel.setText("Beta value");
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.gridy = 5;
		bagConstraintsdbn.insets = new Insets(0,5,0,0);
		textFieldPanelDbn.add(betaValueLabel, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		betaSpinner = new JSpinner();
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 1;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.gridy = 5;
		bagConstraintsdbn.insets = new Insets(0,10,0,0);
		textFieldPanelDbn.add(betaSpinner, bagConstraintsdbn);
		betaSpinner.setEnabled(false);
		 // end of prior section //
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		// set number of Dbn iterations
		numIterationsLabel = new JLabel();
		numIterationsLabel.setText("Number of Iterations");
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 0;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.gridy = 6;
		bagConstraintsdbn.insets = new Insets(0,5,0,0);
		textFieldPanelDbn.add(numIterationsLabel, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		iterationSpinner = new JSpinner();
		
		bagConstraintsdbn.fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsdbn.weighty = 1;
		bagConstraintsdbn.gridx = 1;
//		bagConstraintsdbn.gridwidth = 1;
		bagConstraintsdbn.gridy = 6;
		bagConstraintsdbn.insets = new Insets(0,10,0,0);
		textFieldPanelDbn.add(iterationSpinner, bagConstraintsdbn);
		
		bagConstraintsdbn = new GridBagConstraints();					/* restore default*/
		
		/* Actions*/
		dataFileButtonDbn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						//CHANGE BY LIU: use native file dialog
						JFrame frame = new JFrame();
						FileDialog inputFileDialog = new FileDialog(frame);
						inputFileDialog.setVisible(true);
						String selectedDir = inputFileDialog.getDirectory();
						String selectedFile = inputFileDialog.getFile();
						if(selectedFile != null)
							dataFilePathDbn.setText(new File(selectedDir, selectedFile).getAbsolutePath());
						
						/* pop up the file chooser window*/
//							JFileChooser chooser = new JFileChooser();
//							chooser.setSelectedFile(new File("E:/LIU/Code/workspace/GUI_H/data/data_dbn.txt"));
//						    int returnVal = chooser.showOpenDialog(null);
//						    if(returnVal == JFileChooser.APPROVE_OPTION) {
//						    	dataFilePathDbn.setText(chooser.getSelectedFile().getAbsolutePath());
//						    }
					}
				}
			);
		
		priorSelectButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						//CHANGE BY LIU: use native file dialog
						JFrame frame = new JFrame();
						FileDialog priorFileDialog = new FileDialog(frame);
						priorFileDialog.setVisible(true);
						String selectedDir = priorFileDialog.getDirectory();
						String selectedFile = priorFileDialog.getFile();
						if(selectedFile != null)
							priorDataTextField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
						
//						/* pop up the file chooser window*/
//						if(priorDataBox.isSelected()){
//							JFileChooser chooser = new JFileChooser();
//						    int returnVal = chooser.showOpenDialog(null);
//						    if(returnVal == JFileChooser.APPROVE_OPTION) {
//						    	priorDataText.setText(chooser.getSelectedFile().getAbsolutePath());
//						    }
//						}
						
					}
				}
			);
		
		// bottom panel
		bottomPanel = new JPanel();
		//bottomPanel.setBackground( Color.WHITE);
		GridBagLayout gridBagLayoutbottom = new GridBagLayout();
		bottomPanel.setLayout(gridBagLayoutbottom);
		//bottomPanel.setLayout(new GridLayout(1, 2));
		frame_dbn.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	
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
		
		progressBarDbn = new JProgressBar();
		progressBarDbn.setStringPainted(true);
		//bagConstraintsbottom.ipady =15;
		bagConstraintsbottom .gridx = 1;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .insets = new Insets(0,10,0,100);
		bottomPanel.add( progressBarDbn, bagConstraintsbottom );
		
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
