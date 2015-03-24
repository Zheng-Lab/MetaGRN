package birc.grni.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import ch.epfl.lis.gnwgui.DynamicalModelElement;
import ch.epfl.lis.gnwgui.NetworkElement;
import ch.epfl.lis.gnwgui.StructureElement;
import ch.epfl.lis.gnwgui.idesktop.IElement;
import ch.epfl.lis.imod.ImodNetwork;

public class IndirectCompareDisplay extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	//standard network
	protected JLabel standardLabel;
	protected JLabel standardInputFileLabel;
	protected JTextField standardInputFileTextField;
	protected JButton standardInputFileButton;

	private String standardFilepath;
	private NetworkElement standardElement;
	
	/* format of input networks, only matrix and TSV format are accepted currently*/
	private ButtonGroup standardNetworkFormatButtonGroup;
	private JLabel standardNetworkFormatLabel;
	private JPanel standardNetworkFormatButtonPanel;
	private JCheckBox standardNetworkCheckBox;				

	
	
	//input network
	protected JLabel inputLabel;
	protected JLabel inputFileLabel;
	protected JTextField inputFileTextField;
	protected JButton inputFileButton;

	private String inputFilepath;
	private NetworkElement inputElement;
	
	/* format of input networks, only matrix and TSV format are accepted currently*/
	private ButtonGroup inputNetworkFormatButtonGroup;
	private JLabel inputNetworkFormatLabel;
	private JPanel inputNetworkFormatButtonPanel;
	private JCheckBox inputNetworkCheckBox;	
	
	protected JButton compareButton;
	
	public IndirectCompareDisplay() {
		
		super();
		this.setName("Network Comparison Panel");
		this.setBackground(Color.WHITE);
		
		/* set layout of this panel*/
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints(); 
		
		//standard network
		this.standardLabel= new JLabel("Standard Network");
				
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		this.add(this.standardLabel, gridBagConstraints);
		
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.standardInputFileLabel= new JLabel("Input File Path:");
		this.add(this.standardInputFileLabel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.standardInputFileTextField = new JTextField("Upload data file for standard network:  ");
		this.add(this.standardInputFileTextField,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.standardInputFileButton=new JButton("Choose");
		this.add(this.standardInputFileButton,gridBagConstraints);
		standardInputFileButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
							JFrame frame = new JFrame();
							FileDialog fileDialog = new FileDialog(frame);
							fileDialog.setVisible(true);
							String selectedDir = fileDialog.getDirectory();
							String selectedFile = fileDialog.getFile();							
							if(selectedFile != null)							
								standardInputFileTextField.setText(new File(selectedDir, selectedFile).getAbsolutePath());
					    }
				}
		);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.standardNetworkFormatLabel = new JLabel("Data Format:");
		this.add(this.standardNetworkFormatLabel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		
		this.standardNetworkFormatButtonGroup = new ButtonGroup();
		this.standardNetworkFormatButtonPanel = new JPanel();
		this.standardNetworkFormatButtonPanel.setBackground(Color.WHITE);
		
		//add matrix radio button
		JRadioButton matrixRadioButton = new JRadioButton("Matrix", true);
		matrixRadioButton.setBackground(Color.WHITE);
		matrixRadioButton.setEnabled(true);
		matrixRadioButton.setActionCommand("Matrix");
		standardNetworkFormatButtonGroup.add(matrixRadioButton);
		standardNetworkFormatButtonPanel.add(matrixRadioButton);
		
		//add tsv radio button
		JRadioButton tsvRadioButton = new JRadioButton("TSV", true);
		tsvRadioButton.setBackground(Color.WHITE);
		tsvRadioButton.setEnabled(true);
		tsvRadioButton.setActionCommand("TSV");
		standardNetworkFormatButtonGroup.add(tsvRadioButton);
		standardNetworkFormatButtonPanel.add(tsvRadioButton);
		
		this.add(this.standardNetworkFormatLabel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		
		this.add(this.standardNetworkFormatButtonPanel,gridBagConstraints);
		 
		
		JLabel emptyLabel=new JLabel("");
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		
		this.add(emptyLabel,gridBagConstraints);
		
		//input network
		this.inputLabel= new JLabel("Input Network");
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		//gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		this.add(this.inputLabel, gridBagConstraints);
		
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.inputFileLabel= new JLabel("Input File Path:");
		this.add(this.inputFileLabel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 5;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.inputFileTextField = new JTextField("Upload data file for input network:  ");
		this.add(this.inputFileTextField,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.inputFileButton=new JButton("Choose");
		this.add(this.inputFileButton,gridBagConstraints);
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
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.inputNetworkFormatLabel = new JLabel("Data Format:");
		this.add(this.inputNetworkFormatLabel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		//gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.gridwidth=1;
		gridBagConstraints.insets = new Insets(0,15,0,0);
		
		this.inputNetworkFormatButtonGroup = new ButtonGroup();
		this.inputNetworkFormatButtonPanel = new JPanel();
		this.inputNetworkFormatButtonPanel.setBackground(Color.WHITE);
		
		//add matrix radio button
		JRadioButton inputMatrixRadioButton = new JRadioButton("Matrix", true);
		inputMatrixRadioButton.setBackground(Color.WHITE);
		inputMatrixRadioButton.setEnabled(true);
		inputMatrixRadioButton.setActionCommand("Matrix");
		inputNetworkFormatButtonGroup.add(inputMatrixRadioButton);
		inputNetworkFormatButtonPanel.add(inputMatrixRadioButton);
		
		//add tsv radio button
		JRadioButton inputTsvRadioButton = new JRadioButton("TSV", true);
		inputTsvRadioButton.setBackground(Color.WHITE);
		inputTsvRadioButton.setEnabled(true);
		inputTsvRadioButton.setActionCommand("TSV");
		inputNetworkFormatButtonGroup.add(inputTsvRadioButton);
		inputNetworkFormatButtonPanel.add(inputTsvRadioButton);
		
		this.add(this.inputNetworkFormatLabel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		
		this.add(this.inputNetworkFormatButtonPanel,gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();					/*restore default*/
		gridBagConstraints.fill = GridBagConstraints.CENTER;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth=1;
		gridBagConstraints.insets = new Insets(0,15,0,10);
		
		this.compareButton=new JButton("Compare");
		this.add(this.compareButton,gridBagConstraints);
		compareButton.setEnabled(true);
		
		this.compareButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try{
							int numberOfGenes=0;
							ArrayList<ArrayList<Integer>> standardNetworkMatrix = null;
							ArrayList<ArrayList<Integer>> inputNetworkMatrix	= null;
							//standard network format checking
							if(standardNetworkFormatButtonGroup.getSelection().getActionCommand().equals("Matrix")){
								System.out.println("false");
								//load matrix
								String standardInputNetworkFilePath = standardInputFileTextField.getText();
								BufferedReader brInputNetworkFile = new BufferedReader(new FileReader(standardInputNetworkFilePath));
								String line = "";
								standardNetworkMatrix = new ArrayList<ArrayList<Integer>>();
								while((line = brInputNetworkFile.readLine()) != null) {
									if(!line.equals(""))
									{
										ArrayList<Integer> oneLineOfNetworkMatrix = new ArrayList<Integer>();
										Scanner sc = new Scanner(line);
										while(sc.hasNext()) {
											Integer ele = sc.nextInt();	/* network is 0-1 matrix*/
											oneLineOfNetworkMatrix.add(ele);
										}
										standardNetworkMatrix.add(oneLineOfNetworkMatrix);
									}
								}
								brInputNetworkFile.close();
								//int numberOfGenes = standardNetworkMatrix.size();
								
								
								
							}
							else if(standardNetworkFormatButtonGroup.getSelection().getActionCommand().equals("TSV"))
							{
								System.out.println("true");
								/* transform from TSV to matrix*/
								String standardInputNetworkFilePath = standardInputFileTextField.getText();
								
								
								BufferedReader brInputNetworkFile = new BufferedReader(new FileReader(standardInputNetworkFilePath));
								String line = "";
								
								ArrayList<Integer> oneLineOfNetworkMatrix = new ArrayList<Integer>();
								while((line = brInputNetworkFile.readLine()) != null) {
									if(!line.equals(""))
									{
										
										Scanner sc = new Scanner(line);
										
										while(sc.hasNext()) {
											
											sc.next();
											sc.next();
											Integer ele = sc.nextInt();	/* network is 0-1 matrix*/
											oneLineOfNetworkMatrix.add(ele);
											//System.out.println(ele);
										}
										//standardNetworkMatrix.add(oneLineOfNetworkMatrix);
									}
								}
								//System.out.println(oneLineOfNetworkMatrix.);
								brInputNetworkFile.close();
								double genes_squre=oneLineOfNetworkMatrix.size();
								double genes=Math.sqrt(genes_squre);
								numberOfGenes=(int) genes;
								standardNetworkMatrix = new ArrayList<ArrayList<Integer>>();
								ArrayList<Integer> temp_arr=new ArrayList<Integer>();
								for (int i=0;i<numberOfGenes;i++){
									for(int j=0;j<numberOfGenes;j++){
										temp_arr.add(oneLineOfNetworkMatrix.get(i*numberOfGenes+j));
									}
									standardNetworkMatrix.add(temp_arr);
								}
							}
							//input network format checking
							if(inputNetworkFormatButtonGroup.getSelection().getActionCommand().equals("Matrix")){
								System.out.println("false");
								//load matrix
								String inputNetworkFilePath = inputFileTextField.getText();
								BufferedReader brInputNetworkFile = new BufferedReader(new FileReader(inputNetworkFilePath));
								String line = "";
								inputNetworkMatrix = new ArrayList<ArrayList<Integer>>();
								while((line = brInputNetworkFile.readLine()) != null) {
									if(!line.equals(""))
									{
										ArrayList<Integer> oneLineOfNetworkMatrix = new ArrayList<Integer>();
										Scanner sc = new Scanner(line);
										while(sc.hasNext()) {
											Integer ele = sc.nextInt();	/* network is 0-1 matrix*/
											oneLineOfNetworkMatrix.add(ele);
										}
										inputNetworkMatrix.add(oneLineOfNetworkMatrix);
									}
								}
								brInputNetworkFile.close();
								//int numberOfGenes = standardNetworkMatrix.size();
								
								
								
							}
							else if(inputNetworkFormatButtonGroup.getSelection().getActionCommand().equals("TSV"))
							{
								System.out.println("true");
								/* transform from TSV to matrix*/
								String inputNetworkFilePath = inputFileTextField.getText();
								
								
								BufferedReader brInputNetworkFile = new BufferedReader(new FileReader(inputNetworkFilePath));
								String line = "";
								
								ArrayList<Integer> oneLineOfNetworkMatrix = new ArrayList<Integer>();
								while((line = brInputNetworkFile.readLine()) != null) {
									if(!line.equals(""))
									{
										
										Scanner sc = new Scanner(line);
										
										while(sc.hasNext()) {
											
											sc.next();
											sc.next();
											Integer ele = sc.nextInt();	/* network is 0-1 matrix*/
											oneLineOfNetworkMatrix.add(ele);
											//System.out.println(ele);
										}
										//standardNetworkMatrix.add(oneLineOfNetworkMatrix);
									}
								}
								//System.out.println(oneLineOfNetworkMatrix.);
								brInputNetworkFile.close();
								double genes_squre=oneLineOfNetworkMatrix.size();
								double genes=Math.sqrt(genes_squre);
								numberOfGenes=(int) genes;
								inputNetworkMatrix = new ArrayList<ArrayList<Integer>>();
								ArrayList<Integer> temp_arr=new ArrayList<Integer>();
								for (int i=0;i<numberOfGenes;i++){
									for(int j=0;j<numberOfGenes;j++){
										temp_arr.add(oneLineOfNetworkMatrix.get(i*numberOfGenes+j));
									}
									inputNetworkMatrix.add(temp_arr);
								}
							}
							//standard
							for(int i=0;i<numberOfGenes;i++){
								for(int j=0;j<numberOfGenes;j++){
									System.out.print(standardNetworkMatrix.get(i).get(j));
								}
								System.out.println("");
							}
							//input
							System.out.println("");
							for(int i=0;i<numberOfGenes;i++){
								for(int j=0;j<numberOfGenes;j++){
									System.out.print(inputNetworkMatrix.get(i).get(j));
								}
								System.out.println("");
							}
							//calculate f score
							double true_pos=0;
							double true_neg=0;
							double false_pos=0;
							double false_neg=0;
							
							for(int i=0;i<numberOfGenes;i++){
								for(int j=0;j<numberOfGenes;j++){
									//true
									if(standardNetworkMatrix.get(i).get(j)==inputNetworkMatrix.get(i).get(j)){
										if(standardNetworkMatrix.get(i).get(j)==1)
											true_pos++;
										else
											true_neg++;
										
									}
									//false
									else{
										if(standardNetworkMatrix.get(i).get(j)==1)
											false_pos++;
										else
											false_neg++;
									}
								}
							}
							networkComparisonResult(true_pos,true_neg,false_pos,false_neg);
							
							
						}
						catch(Exception ex){
							ex.printStackTrace();
						};
						
						
					}
				}
				);
		
		
	}
	public void networkComparisonResult(double true_pos,double true_neg,double false_pos,double false_neg){
		double precision=true_pos/(true_pos+false_pos);
		double recall = true_pos/(true_pos+true_neg);
		double f_score=(2*true_pos)/(2*true_pos+true_neg+false_pos);
		MathContext mc = new MathContext(2);
		BigDecimal bd = new BigDecimal(f_score,mc);
		
		NetworkComparisonResult ncr = new NetworkComparisonResult(precision, recall,bd,new JFrame());
		ncr.frame.setVisible(true);
		ncr.result_header.setTitle("Network Comparison Result");
	}
	
	
	
	

}
