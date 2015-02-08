package birc.grni.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class GrnTimeDelayLassoDisplay {
	
	protected JFrame frame_lassoDelay;
	protected GrnGuiHeader header_lassoDelay = new GrnGuiHeader();
	
	protected JPanel headerPanelLassoDelay;
	protected JPanel textFieldPanelLassoDelay;
	protected JPanel bottomPanel;
	
	protected JLabel inputFilePathLable;
	private JLabel rowColumnChooseLabel;
	protected JLabel delayValue;
	
	protected JCheckBox withheaderCheckBox;
	
	protected ButtonGroup rowColumnChooseButtonGroup;
	
	private JPanel rowColumnChooseButtonPanel;
	
	protected JTextField inputFilePathTextField;
	protected JSpinner delaySpinner;
	
	protected JButton fileBrowse;
	//LIU: change to static for use of GrnTimeDelayLasso.timeDelayLassoResult method
	protected static JButton startButton;
	
	public static JProgressBar LassoDelayProgressBar;
	
	//LIU
	public JTextField getInputFilePathTextField() {
		return this.inputFilePathTextField;
	}
	
	public GrnTimeDelayLassoDisplay(JFrame frameR){
		frame_lassoDelay= frameR;
		
		frame_lassoDelay.setBackground(Color.WHITE);
		frame_lassoDelay.getContentPane().setFocusCycleRoot(true);
		frame_lassoDelay.getContentPane().setBackground(Color.WHITE);
		frame_lassoDelay.setSize(new Dimension(0,0));
		frame_lassoDelay.setBounds(200,200,600,400);
		
		frame_lassoDelay.setTitle("Time-delayed Lasso regression");
		
		// adding header panel 
		headerPanelLassoDelay = new JPanel();
		headerPanelLassoDelay.setBackground(Color.WHITE);
		final CardLayout cards_ = new CardLayout();
		headerPanelLassoDelay.setLayout(cards_);
		frame_lassoDelay.getContentPane().add(headerPanelLassoDelay, BorderLayout.NORTH);
		headerPanelLassoDelay.add(header_lassoDelay, "header");
		
		// create text field panel and add component into it
		textFieldPanelLassoDelay = new JPanel();
		textFieldPanelLassoDelay.setBackground(Color.WHITE);
		textFieldPanelLassoDelay.setLayout(new GridBagLayout());
		GridBagConstraints textFieldLayoutConstraints = new GridBagConstraints();
		frame_lassoDelay.getContentPane().add(textFieldPanelLassoDelay,BorderLayout.CENTER);
		
		/* input file*/
		inputFilePathLable = new JLabel();
		inputFilePathLable.setText("Full path of the input file");
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 0;
		textFieldLayoutConstraints.gridwidth = 1;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,10);
		textFieldPanelLassoDelay.add(inputFilePathLable, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		inputFilePathTextField = new JTextField();

		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 1;
		textFieldLayoutConstraints.gridy = 0;
		textFieldLayoutConstraints.weightx = 1;									/* put all extra horizontal space in the file path field*/
		textFieldLayoutConstraints.gridwidth = 5;
		textFieldPanelLassoDelay.add(inputFilePathTextField, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		fileBrowse = new JButton();
		fileBrowse.setText("Browse");
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 6;
		textFieldLayoutConstraints.gridy = 0;
		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
		textFieldPanelLassoDelay.add(fileBrowse, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		/* whether the input file includes header or not*/
		withheaderCheckBox = new JCheckBox("Input with header");
		withheaderCheckBox.setBackground(Color.WHITE);
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 1;
//		textFieldLayoutConstraints.gridwidth = 1;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		textFieldPanelLassoDelay.add(withheaderCheckBox, textFieldLayoutConstraints);
		
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
		addRadioButton("column header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, true);
		addRadioButton("row header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, false);
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 2;
//		textFieldLayoutConstraints.gridwidth = 1;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		textFieldPanelLassoDelay.add(rowColumnChooseLabel, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 1;
		textFieldLayoutConstraints.gridy = 2;
//		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
		textFieldPanelLassoDelay.add(rowColumnChooseButtonPanel, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		delayValue = new JLabel();
		delayValue.setText("Maximum Time-Delay");
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 3;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,0);
		textFieldPanelLassoDelay.add(delayValue, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		delaySpinner = new JSpinner();
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 1;
		textFieldLayoutConstraints.gridy = 3;
		textFieldLayoutConstraints.insets = new Insets(0,10,0,0);
		textFieldPanelLassoDelay.add(delaySpinner, textFieldLayoutConstraints);
		delaySpinner.setEnabled(true);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		/* Actions*/
		fileBrowse.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//CHANGE BY LIU: use native file dialog
				JFrame frame = new JFrame();
				FileDialog fileDialog = new FileDialog(frame);
				fileDialog.setVisible(true);
				
				String selectedDir = fileDialog.getDirectory();
				String selectedFile = fileDialog.getFile();
				if(selectedFile != null)
					inputFilePathTextField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
				// TODO Auto-generated method stub
//				JFileChooser ch = new JFileChooser();
//				int value = ch.showOpenDialog(null);
//				if(value == JFileChooser.APPROVE_OPTION){
//					inputFilePath.setText(ch.getSelectedFile().getAbsolutePath());
//				}
				
			}
		});

		//bottom panel
		bottomPanel = new JPanel();
		GridBagLayout gridBagLayoutbottom = new GridBagLayout();
		bottomPanel.setLayout(gridBagLayoutbottom);
		frame_lassoDelay.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		
		Component componentStruct = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraintsStruct = new GridBagConstraints();
		gridBagConstraintsStruct.gridy = 0;
		gridBagConstraintsStruct.gridx = 1;
		bottomPanel.add(componentStruct, gridBagConstraintsStruct);
		
		
		startButton = new JButton();
		startButton.setText("Start");
		GridBagConstraints bagConstraintsbottom = new GridBagConstraints();
		bagConstraintsbottom .fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsbottom .weightx = 0.5;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .gridx = 0;
		bagConstraintsbottom .insets = new Insets(10,30,10,50);
		bottomPanel.add(startButton, bagConstraintsbottom );
		
		LassoDelayProgressBar = new JProgressBar();
		LassoDelayProgressBar.setStringPainted(true);
		//bagConstraintsbottom.ipady =15;
		bagConstraintsbottom .gridx = 1;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .insets = new Insets(0,10,0,100);
		bottomPanel.add( LassoDelayProgressBar, bagConstraintsbottom );
		
		Component componentStruct22 = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraintsStruct22 = new GridBagConstraints();
		gridBagConstraintsStruct22.gridy = 2;
		gridBagConstraintsStruct22.gridx = 2;
		bottomPanel.add(componentStruct22, gridBagConstraintsStruct22);
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

}
