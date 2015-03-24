package birc.grni.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

import birc.grni.dbn.ProgressBarAdaptorDBN;
import birc.grni.gui.visulization.GrnVisulizeNetwork;


public class GrnDbn extends GrnDbnDisplay {
	
	//protected JFrame grn_frame;
	
	//CHANGE BY LIU: seems no need
//	private  static String resultPath;
	
	private static Logger logger = Logger.getLogger(GrnDbn.class.getName());
//	protected Binomial_HDBNf hdb ;
	
	// ADD BY LIU:
	public static boolean runByMeta = false;
	
	// ADD BY LIU:
	public JTextField getDataFilePathDbn() {
		return this.dataFilePathDbn;
	}
	
	public GrnDbn(JFrame frame_1) {
		super(frame_1);
		
		header_dbn.setTitle("Set parameters for DBN");
		header_dbn.setInfo("Enter parameters and click start button ");
		//LIU
//		header_dbn.setTopColor(Color.RED);
//		header_dbn.setBottomColor(Color.PINK);
		
		// add spinner model to beta
		SpinnerNumberModel spinModel = new SpinnerNumberModel(1, 1, 5, 1);
		betaSpinner.setModel(spinModel);
		
		// add spinner model to iterations
		SpinnerNumberModel iterModel = new SpinnerNumberModel(150000,100000,300000,25000);
		iterationSpinner.setModel(iterModel);
		
		priorDataCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				enablePrior();
			}
		});
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputFile = dataFilePathDbn.getText();
				//String s = numGenesDbn.getText();
				//int genes = Integer.parseInt(numGenesDbn.getText());
				//int samples = Integer.parseInt(numTimePoints.getText());
				//String resultPath = resultSavePath.getText();
				//resultPath = resultSavePath.getText();
				logger.log(Level.INFO, "dbn start button click");
				startButton.setEnabled(false);
				int iterations =(Integer)iterationSpinner.getModel().getValue();
				boolean select =priorDataCheckBox.isSelected();
				
				// ADD BY LIU:
				/* input data file with or without header*/
				boolean withHeader = withheaderCheckBox.isSelected();
				
				/* row header and column header, which one is gene name*/
				boolean geneNameAreColumnHeader = true;
				if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
					geneNameAreColumnHeader = true;
				else
					geneNameAreColumnHeader = false;
				
				// CHANGE BY LIU:
//				ProgressBarAdaptorDBN dbn = new ProgressBarAdaptorDBN(inputFile, iterations);
				ProgressBarAdaptorDBN dbn = new ProgressBarAdaptorDBN(inputFile, iterations, withHeader, geneNameAreColumnHeader);
				
				if(select){
					int beta =(Integer)betaSpinner.getModel().getValue();
					String priorFile =priorDataTextField.getText();
					//hdb = new Binomial_HDBNf(genes, inputFile, iterations, beta, priorFile);
					dbn.dbnMcmcWithPrior(beta, priorFile);
					
				}else{
					//hdb = new Binomial_HDBNf(genes, inputFile, iterations);
					dbn.dbnMcmcWithOutPrior();
				}
							       
				dbn.execute();		// start swingWorker as a worker thread
				logger.log(Level.INFO, "End of Dbn ");
			}
		});
	}
	
	private void enablePrior(){
		boolean select =priorDataCheckBox.isSelected();
		priorDataTextField.setEnabled(select);
		priorSelectButton.setEnabled(select);
		betaSpinner.setEnabled(select);
	}
	
	// CHANGE BY LIU:
//	public static void dbnResultPrinter(int network [][], int genes){
//		
//		Object [] options = {"Save Result" , "Visualize Result"};
//		int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
//		
//		if(optionValue == JOptionPane.OK_OPTION) {
//			//CHANGE BY LIU: use native save dialog
//			JFrame frame = new JFrame();
//			FileDialog saveFileDialog = new FileDialog(frame, "Save", FileDialog.SAVE);
//			saveFileDialog.setVisible(true);
//			String selectedDir = saveFileDialog.getDirectory();
//			String selectedFile = saveFileDialog.getFile();
//			if(selectedFile != null)
//			{
//				String resultSavePath = new File(selectedDir, selectedFile).getAbsolutePath();
//				try {
//					PrintStream printer = new PrintStream(new File(resultSavePath));
//					// write results according to standard format 
//					   for(int m=0; m<genes; m++){
//			    		   for(int n=0; n<genes; n++) {
//			    			   
//			    			   if(network[m][n] == 1) {
//			                       printer.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
//			    			   }
//			    			   else{
//			    				   printer.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
//			    			   }
//			    			   printer.println();
//			    		   }
//			    	   }
//					   printer.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			// save result
////			JFileChooser chooser = new JFileChooser();
////		    int returnVal = chooser.showSaveDialog(null);
////		    if(returnVal == JFileChooser.APPROVE_OPTION) {
////		    	String resultSavePath = chooser.getSelectedFile().getAbsolutePath();
////		    	try {
////					FileWriter resultFileWriter = new FileWriter(resultSavePath , true);
////					// write results as a matrix // 
////				/*	for(int i=0; i<genes; i++){
////			    		   for(int j=0; j<genes; j++){
////			    			   
////			    			   if(network[i][j] == 1){
////			    				   resultFileWriter.write("1" + "\t");
////			                       
////			    			   }
////			    			   else{
////			    				   resultFileWriter.write("0" + "\t");
////			    			   }
////			    		   }
////			    		 
////			    		   resultFileWriter.write("\n");
////			    	   }
////			    	  
////					   resultFileWriter.write("\n");
////					   resultFileWriter.write("\n");   */
////			    	      
////			    	   // write results according to standard format 
////					   for(int m=0; m<genes; m++){
////			    		   for(int n=0; n<genes; n++){
////			    			   
////			    			   if(network[m][n] == 1){
////			    				   resultFileWriter.write("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
////			                       
////			    			   }
////			    			   else{
////			    				   resultFileWriter.write("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
////			    			   }
////			    			   resultFileWriter.write("\n");
////			    		   }
////			    	   }
////					   resultFileWriter.close();
////				} catch (IOException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}			
////		    }  	
//		}
//		
//		if(optionValue == JOptionPane.NO_OPTION){
//			logger.log(Level.INFO, "visualization has selected");
//			// create a visualization class object and pass the network
//			try {
//				GrnVisulizeNetwork visualization = new GrnVisulizeNetwork(network, genes);
//			} catch (Exception e) {
//				logger.log(Level.SEVERE, "exception has thrown - " + e.toString());
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		startButton.setEnabled(true);
//	}
						
	public static void dbnResultPrinter(int network [][], int numberOfGenes, ArrayList<String> geneNames)
	{	
		startButton.setEnabled(true);
		
		if(!runByMeta)
		{
			Object [] options = {"Save Result" , "Visualize Result"};
			int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			
			if(optionValue == JOptionPane.OK_OPTION) {
				JFrame frame = new JFrame();
				FileDialog saveFileDialog = new FileDialog(frame, "Save", FileDialog.SAVE);
				saveFileDialog.setVisible(true);
				String selectedDir = saveFileDialog.getDirectory();
				String selectedFile = saveFileDialog.getFile();
				if(selectedFile != null)
				{
					String resultSavePath = new File(selectedDir, selectedFile).getAbsolutePath();
					try {
						PrintStream resultFilePrinter = new PrintStream(new File(resultSavePath));
						if(geneNames != null) {
							for(int m=0; m<numberOfGenes; m++)
							{
								for(int n=0; n<numberOfGenes; n++) 
								{   
									if(network[m][n] == 1) 
										resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 1);
									else
				    				   	resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 0);
									
									resultFilePrinter.println();
								}
							}
						} else {
							for(int m=0; m<numberOfGenes; m++)
							{
								for(int n=0; n<numberOfGenes; n++) 
								{   
									if(network[m][n] == 1) 
										resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
									else
				    				   	resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
									
									resultFilePrinter.println();
								}
							}
						}
						resultFilePrinter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(optionValue == JOptionPane.NO_OPTION){
				logger.log(Level.INFO, "visualization has selected");
				// create a visualization class object and pass the network
				try {
					GrnVisulizeNetwork visualization = new GrnVisulizeNetwork(network, numberOfGenes);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "exception has thrown - " + e.toString());
					e.printStackTrace();
				}
			}
		} else {
			try {
				File resultFile = new File("DBN.tsv");
//				resultFile.deleteOnExit();
				PrintStream resultFilePrinter = new PrintStream(resultFile);
				if(geneNames != null) {
					for(int m=0; m<numberOfGenes; m++)
					{
						for(int n=0; n<numberOfGenes; n++) 
						{   
							if(network[m][n] == 1) 
								resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 1);
							else
		    				   	resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 0);
							
							resultFilePrinter.println();
						}
					}
				} else {
					for(int m=0; m<numberOfGenes; m++)
					{
						for(int n=0; n<numberOfGenes; n++) 
						{   
							if(network[m][n] == 1) 
								resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
							else
		    				   	resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
							
							resultFilePrinter.println();
						}
					}
				}
				resultFilePrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
