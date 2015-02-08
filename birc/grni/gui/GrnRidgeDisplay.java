package birc.grni.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GrnRidgeDisplay {

	protected JFrame frame_ridge;
	protected GrnGuiHeader header_ridge = new GrnGuiHeader();
	
	protected JPanel headerPanelRidge;
	protected JPanel textFieldPanel;
	protected JPanel bottomPanel;
	
	protected JLabel numGenesRidge;
	protected JLabel numSamples;
	protected JLabel inputFilePathLable;
	
//	protected JTextField geneText;
//	protected JTextField sampleTextField;
	protected JTextField inputFilePathTextField;
	
	protected JButton fileBrowse;
	//CHANGE BY LIU:
//	protected JButton startButtonRidge;
	protected static JButton startButtonRidge;
	
	//ADD BY LIU:
	protected ButtonGroup rowColumnChooseButtonGroup;
	private JLabel rowColumnChooseLabel;
	private JPanel rowColumnChooseButtonPanel;

	protected JCheckBox withheaderCheckBox;
	
	public static JProgressBar ridgeProgressBar;
	
	//ADD BY LIU:
	public JTextField getInputFilePathField() {
		return this.inputFilePathTextField;
	}
	
	public GrnRidgeDisplay(JFrame frameR){
		frame_ridge= frameR;
		
		frame_ridge.setBackground(Color.WHITE);
		frame_ridge.getContentPane().setFocusCycleRoot(true);
		frame_ridge.getContentPane().setBackground(Color.WHITE);
		frame_ridge.setSize(new Dimension(0,0));
		frame_ridge.setBounds(200,200,600,400);
		
		frame_ridge.setTitle("Ridge regression");
		
		// adding header panel 
		headerPanelRidge = new JPanel();
		headerPanelRidge.setBackground(Color.WHITE);
		final CardLayout cards_ = new CardLayout();
		headerPanelRidge.setLayout(cards_);
		frame_ridge.getContentPane().add(headerPanelRidge, BorderLayout.NORTH);
		headerPanelRidge.add(header_ridge, "header");
		
		// create text field panel and add component into it
		textFieldPanel = new JPanel();
		textFieldPanel.setBackground(Color.WHITE);
		textFieldPanel.setLayout(new GridBagLayout());
		GridBagConstraints textFieldLayoutConstraints = new GridBagConstraints();
		frame_ridge.getContentPane().add(textFieldPanel,BorderLayout.CENTER);
		
		inputFilePathLable = new JLabel();
		inputFilePathLable.setText("Full path of the input file");
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 0;
		textFieldLayoutConstraints.gridy = 0;
		textFieldLayoutConstraints.gridwidth = 1;
		textFieldLayoutConstraints.insets = new Insets(0,5,0,10);
		textFieldPanel.add(inputFilePathLable, textFieldLayoutConstraints);

		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		inputFilePathTextField = new JTextField();

		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 1;
		textFieldLayoutConstraints.gridy = 0;
		textFieldLayoutConstraints.weightx = 1;									/* put all extra horizontal space in the file path field*/
		textFieldLayoutConstraints.gridwidth = 5;
		textFieldPanel.add(inputFilePathTextField, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		fileBrowse = new JButton();
		fileBrowse.setText("Browse");
		
		textFieldLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		textFieldLayoutConstraints.weighty = 1;
		textFieldLayoutConstraints.gridx = 6;
		textFieldLayoutConstraints.gridy = 0;
		textFieldLayoutConstraints.gridwidth = GridBagConstraints.REMAINDER;
		textFieldLayoutConstraints.insets = new Insets(0,10,0,10);
		textFieldPanel.add(fileBrowse, textFieldLayoutConstraints);
		
		textFieldLayoutConstraints = new GridBagConstraints();					/* restore default*/
		
		/* whether the input file includes header or not*/
		withheaderCheckBox = new JCheckBox("Input with header");
		withheaderCheckBox.setBackground(Color.WHITE);
		
		/* group withheaderLabel and withheaderCheckBox into one GUI component*/
		
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
		addRadioButton("column header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, true);
		addRadioButton("row header", rowColumnChooseButtonGroup, rowColumnChooseButtonPanel, false);
		
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
		
		/* Action*/
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
		frame_ridge.getContentPane().add(bottomPanel,BorderLayout.SOUTH);
		
		Component componentStruct = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraintsStruct = new GridBagConstraints();
		gridBagConstraintsStruct.gridy = 0;
		gridBagConstraintsStruct.gridx = 1;
		bottomPanel.add(componentStruct, gridBagConstraintsStruct);
		
		
		startButtonRidge = new JButton();
		startButtonRidge.setText("Start");
		GridBagConstraints bagConstraintsbottom = new GridBagConstraints();
		bagConstraintsbottom .fill = GridBagConstraints.HORIZONTAL;
		bagConstraintsbottom .weightx = 0.5;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .gridx = 0;
		bagConstraintsbottom .insets = new Insets(10,30,10,50);
		bottomPanel.add(startButtonRidge, bagConstraintsbottom );
		
		ridgeProgressBar = new JProgressBar();
		ridgeProgressBar.setStringPainted(true);
		//bagConstraintsbottom.ipady =15;
		bagConstraintsbottom .gridx = 1;
		bagConstraintsbottom .gridy = 1;
		bagConstraintsbottom .insets = new Insets(0,10,0,100);
		bottomPanel.add( ridgeProgressBar, bagConstraintsbottom );
		
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
