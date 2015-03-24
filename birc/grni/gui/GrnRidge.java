package birc.grni.gui;

import birc.grni.gui.visulization.GrnVisulizeNetwork;
import birc.grni.ridge.ProgressBarAdaptorRidge;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class GrnRidge extends GrnRidgeDisplay {
	
	// ADD BY LIU:
	public static boolean runByMeta = false;

	public GrnRidge(JFrame frameR) {
		
		super(frameR);
		header_ridge.setTitle("Select Input Data File for for Ridge Regression");
		header_ridge.setInfo("After selecting input data file, click start button ");
		//LIU
//		header_ridge.setTopColor(Color.RED);
//		header_ridge.setBottomColor(Color.PINK);
		
		startButtonRidge.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//ADD BY LIU:
				startButtonRidge.setEnabled(false);
				
				// TODO Auto-generated method stub
				//int numGenes = Integer.parseInt(geneText.getText());
				//int samples = Integer.parseInt(sampleTextField.getText());
				String filePath = inputFilePathTextField.getText();
				
				// ADD BY LIU:
				/* input data file with or without header*/
				boolean withHeader = withheaderCheckBox.isSelected();
				
				/* row header and column header, which one is gene name*/
				boolean geneNameAreColumnHeader = true;
				if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
					geneNameAreColumnHeader = true;
				else
					geneNameAreColumnHeader = false;
				
				//RidgeRegression ridge = new RidgeRegression (numGenes,samples,filePath);
				try {
					// CHANGE BY LIU:
//					ProgressBarAdaptorRidge progressBarAdaptor = new ProgressBarAdaptorRidge (filePath);
					ProgressBarAdaptorRidge progressBarAdaptor = new ProgressBarAdaptorRidge (filePath, withHeader, geneNameAreColumnHeader);
					
					progressBarAdaptor.execute();
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		});
			
	}
	
	// CHANGE BY LIU:
//	public static void ridgeResultPrinter (int [][] network, int genes) {
//		
//		Object [] options = {"Save Result" , "Visualize Result"};
//		int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
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
//			
//			// save result
////			JFileChooser chooser = new JFileChooser();
////		    int returnVal = chooser.showSaveDialog(null);
////		    if(returnVal == JFileChooser.APPROVE_OPTION) {
////		    	String resultSavePath = chooser.getSelectedFile().getAbsolutePath();
////		    	try {
////					FileWriter resultFileWriter = new FileWriter(resultSavePath , true);
////					// write results as a matrix // 
////					/*for(int i=0; i<genes; i++){
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
	
	public static void ridgeResultPrinter (int [][] network, int numberOfGenes, ArrayList<String> geneNames) {
		
		startButtonRidge.setEnabled(true);
		
		if(!runByMeta)
		{
			Object [] options = {"Save Result" , "Visualize Result"};
			int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			
			if(optionValue == JOptionPane.OK_OPTION){
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
				// create a visualization class object and pass the network
				try {
					GrnVisulizeNetwork visualization = new GrnVisulizeNetwork(network, numberOfGenes);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			try {
				File resultFile = new File("RIDGE.tsv");
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
	
	private void printReslt(){
		
	}
}
