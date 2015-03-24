package birc.grni.gui;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

import birc.grni.gui.visulization.GrnVisulizeNetwork;
import birc.grni.lasso.delay.ProgressBarAdaptorLassoDelay;
import birc.grni.util.CommonUtil;
import birc.grni.util.InputData;

public class GrnTimeDelayLasso extends GrnTimeDelayLassoDisplay {
	
	//LIU
	public static boolean runByMeta = false;
	
	public GrnTimeDelayLasso(JFrame frameR) {
		
		super(frameR);
		header_lassoDelay.setTitle("Set parameters for Time-Delayed Lasso regression");
		header_lassoDelay.setInfo("Enter parameters and click start button ");
		//LIU
//		header_lassoDelay.setTopColor(Color.RED);
//		header_lassoDelay.setBottomColor(Color.PINK);
		
		// add spinner model to beta
		SpinnerNumberModel spinModel = new SpinnerNumberModel(3, 2, 5, 1);
		delaySpinner.setModel(spinModel);
			
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				startButton.setEnabled(false);
				
				//LIU: add input format options
				/* input data file with or without header*/
				boolean withHeader = withheaderCheckBox.isSelected();
				
				/* row header and column header, which one is gene name*/
				boolean geneNameAreColumnHeader = true;
				if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
					geneNameAreColumnHeader = true;
				else
					geneNameAreColumnHeader = false;
				
				//int numGenes = Integer.parseInt(geneText.getText());
				//int samples = Integer.parseInt(sampleTextField.getText());
				String filePath = inputFilePathTextField.getText();
				int delay =(Integer)delaySpinner.getModel().getValue();
				
				//RidgeRegression ridge = new RidgeRegression (numGenes,samples,filePath);
				//LIU
//				TimeDelayAlgorithm del = new TimeDelayAlgorithm(filePath, delay);
				try {
//					birc.grni.util.Logging.logger.log(Level.FINE, "Before Enterring CommonUtil.readInput.");
					InputData inputData = CommonUtil.readInput(filePath, withHeader, geneNameAreColumnHeader);
					ProgressBarAdaptorLassoDelay del = new ProgressBarAdaptorLassoDelay(inputData, delay);
					del.execute();
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		});
	}
	
	//LIU
//	public static void timeDelayLassoResult (int [][] network, int [][] delayInfo, int genes) {
//		
//		/* pop up the file chooser window to save delay information*/
//		JFrame frameDelay = new JFrame();
//		FileDialog saveFileDialogDelay = new FileDialog(frameDelay, "Save the Time-delay result", FileDialog.SAVE);
//		saveFileDialogDelay.setVisible(true);
//		String selectedDirDelay = saveFileDialogDelay.getDirectory();
//		String selectedFileDelay = saveFileDialogDelay.getFile();
//		if(selectedFileDelay != null) 
//		{
//			String resultSavePath = new File(selectedDirDelay, selectedFileDelay).getAbsolutePath();
//			try {
//				PrintStream printer = new PrintStream(new File(resultSavePath));
//				// write time-delay results according to standard format
//				// 1st column - regulator gene
//				// 2nd column - target gene
//				// 3rd column - delay
//				//printer.print("Regulator" + "\t" + "Regulatee" + "\t" + "Delay");
//				//printer.println();
//				   for(int m=0; m<genes; m++){
//		    		   for(int n=0; n<genes; n++) {
//		    			   if(delayInfo[m][n] != 0){
//		    				   int delay = delayInfo[m][n];
//		    				   printer.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + delay);
//		    				   printer.println();
//		    			   }
//		    		   }
//		    	   }
//				   printer.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		Object [] options = {"Save the network" , "Visualize the network"};
//		int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the Network results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
//		
//		if(optionValue == JOptionPane.OK_OPTION){
//			//CHANGE BY LIU: use native save dialog (need to test)
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
//		}
//		
//		if(optionValue == JOptionPane.NO_OPTION){
//			// create a visualization class object and pass the network
//			try {
//				GrnVisulizeNetwork visualization = new GrnVisulizeNetwork(network, genes);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//						
//	}
	
	public static void timeDelayLassoResult(int[][] network, int[][] delayInfo, int numberOfGenes, /* LIU */ArrayList<String> geneNames) {
		
		startButton.setEnabled(true);

		if(!runByMeta)
		{
			/* pop up the file chooser window to save delay information */
			JFrame frameDelay = new JFrame();
			FileDialog saveFileDialogDelay = new FileDialog(frameDelay, "Save the Time-delay result", FileDialog.SAVE);
			saveFileDialogDelay.setVisible(true);
			String selectedDirDelay = saveFileDialogDelay.getDirectory();
			String selectedFileDelay = saveFileDialogDelay.getFile();
			if (selectedFileDelay != null) {
				String resultSavePath = new File(selectedDirDelay, selectedFileDelay).getAbsolutePath();
				try {
					PrintStream printer = new PrintStream(new File(resultSavePath));
					// write time-delay results according to standard format
					// 1st column - regulator gene
					// 2nd column - target gene
					// 3rd column - delay
					// printer.print("Regulator" + "\t" + "Regulatee" + "\t" + "Delay");
					// printer.println();
	
					// LIU
					if (geneNames != null) {
						for (int m = 0; m < numberOfGenes; m++) {
							for (int n = 0; n < numberOfGenes; n++) {
								if (delayInfo[m][n] != 0) {
									int delay = delayInfo[m][n];
									printer.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + delay);
									printer.println();
								}
							}
						}
					} else {
						for (int m = 0; m < numberOfGenes; m++) {
							for (int n = 0; n < numberOfGenes; n++) {
								if (delayInfo[m][n] != 0) {
									int delay = delayInfo[m][n];
									printer.print("G" + (m + 1) + "\t" + "G" + (n + 1) + "\t" + delay);
									printer.println();
								}
							}
						}
					}
					printer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
			Object[] options = { "Save the network", "Visualize the network" };
			int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the Network results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
	
			if (optionValue == JOptionPane.OK_OPTION) {
				// CHANGE BY LIU: use native save dialog (need to test)
				JFrame frame = new JFrame();
				FileDialog saveFileDialog = new FileDialog(frame, "Save", FileDialog.SAVE);
				saveFileDialog.setVisible(true);
				String selectedDir = saveFileDialog.getDirectory();
				String selectedFile = saveFileDialog.getFile();
				if (selectedFile != null) {
					String resultSavePath = new File(selectedDir, selectedFile).getAbsolutePath();
					try {
						PrintStream printer = new PrintStream(new File(resultSavePath));
						// write results according to standard format
						//LIU
						if(geneNames != null)
						{
							for (int m = 0; m < numberOfGenes; m++) {
								for (int n = 0; n < numberOfGenes; n++) {
									if (network[m][n] == 1) {
										printer.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 1);
									} else {
										printer.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 0);
									}
									printer.println();
								}
							}
						}
						else
						{
							for (int m = 0; m < numberOfGenes; m++) {
								for (int n = 0; n < numberOfGenes; n++) {
		
									if (network[m][n] == 1) {
										printer.print("G" + (m + 1) + "\t" + "G" + (n + 1) + "\t" + 1);
									} else {
										printer.print("G" + (m + 1) + "\t" + "G" + (n + 1) + "\t" + 0);
									}
									printer.println();
								}
							}
						}
						printer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	
			if (optionValue == JOptionPane.NO_OPTION) {
				// create a visualization class object and pass the network
				try {
					/*LIU:GrnVisulizeNetwork visualization = */new GrnVisulizeNetwork(network, numberOfGenes);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		else
		{
			try 
			{
				File resultFile = new File("LASSO_DELAY.tsv");
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
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	//TEST
	public static void main(String[] args) {
		int[][] network = {
				{1,0,1,1,0},
				{0,0,1,0,1},
				{0,1,0,1,1},
				{0,0,0,1,0},
				{0,1,0,0,1}
		};
		
		int[][] delay = {
				{3,1,2,5,4},
				{0,1,5,2,1},
				{6,0,1,2,4},
				{3,5,1,3,2},
				{2,2,1,2,3}
		};
		
		ArrayList<String> geneNames = new ArrayList<String>(Arrays.asList(new String[]{"N1", "N2", "n3", "N4", "n5"}));
//		GrnTimeDelayLasso.timeDelayLassoResult(network, delay, network.length, geneNames);
		GrnTimeDelayLasso.timeDelayLassoResult(network, delay, network.length, null);
	}
}
