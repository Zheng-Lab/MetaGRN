package birc.grni.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import birc.grni.gui.visulization.*;
import birc.grni.rf.*;
import birc.grni.util.*;
import birc.grni.util.exception.BadInputFormatException;


public class GrnRf extends GrnRfDisplay
{	
	public static final HashMap<String, String> treeMethodMap;
	public static final String[] treeMethods = 
		{
			"RF-Random Forests", 
			"ET-Extra Trees"
		};
	
	static
	{
		treeMethodMap = new HashMap<String, String>();
		treeMethodMap.put("RF-Random Forests", "RF");
		treeMethodMap.put("ET-Extra Trees", "ET");
	}
	
	public static boolean runByMeta = false;
	
	public GrnRf(JFrame frame)
	{
		super(frame);
		
		headerRf.setTitle("Set parameters for Random Forests");
		headerRf.setInfo("Enter parameters and click start button");
//		headerRf.setTopColor(Color.RED);
//		headerRf.setBottomColor(Color.PINK);
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				startButton.setEnabled(false);
				
				String inputFilePath = dataFilePathField.getText();
				String treeMethod = treeMethodMap.get((String)treeMethodComboBox.getSelectedItem());
				int nbTrees = Integer.parseInt(nbTreesField.getText());
				
				/* input data file with or without header*/
				boolean withHeader = withheaderCheckBox.isSelected();
				
				/* row header and column header, which one is gene name*/
				boolean geneNameAreColumnHeader = true;
				if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
					geneNameAreColumnHeader = true;
				else
					geneNameAreColumnHeader = false;
				
				try
				{
					FileReader inputFileReader = new FileReader(inputFilePath);
					InputData inputData = CommonUtil.readInput(inputFileReader, withHeader, geneNameAreColumnHeader);
					new ProgressBarAdaptorRF(inputData, treeMethod, nbTrees).execute();
				}
				catch(FileNotFoundException fnfex) {
					JOptionPane.showMessageDialog(null, fnfex.getMessage(), "FileNotFound", JOptionPane.ERROR_MESSAGE);
				} catch(BadInputFormatException badInputFormatEx) {
					JOptionPane.showMessageDialog(null, badInputFormatEx.getMessage(), "BadInputFormat", JOptionPane.ERROR_MESSAGE);
				} catch(IOException ioex) {
					JOptionPane.showMessageDialog(null, ioex.getMessage(), "IOException", JOptionPane.ERROR_MESSAGE);
				} finally {
					startButton.setEnabled(true);
				}
			}
		});
	}
	
	/**
	 * 
	 * @param network network to visualize
	 * @param numberOfGenes number of genes
	 */
	public static void resultPrinter(int[][] network, int numberOfGenes, ArrayList<String> geneNames) {
		
		startButton.setEnabled(true);
		CommonUtil.resultPrinter(network, numberOfGenes, geneNames, runByMeta, "RF.tsv", null);

		// if(!runByMeta)
		// {
		// 	Object [] options = {"Save Result" , "Visualize Result"};
		// 	int optionValue = JOptionPane.showOptionDialog(null, "What do you like to do for the results?", "Inference Result", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		// 	
		// 	if(optionValue == JOptionPane.OK_OPTION) 
		// 	{
		// 		JFrame frame = new JFrame();
		// 		FileDialog saveFileDialog = new FileDialog(frame, "Save", FileDialog.SAVE);
		// 		saveFileDialog.setVisible(true);
		// 		String selectedDir = saveFileDialog.getDirectory();
		// 		String selectedFile = saveFileDialog.getFile();
		// 		if(selectedFile != null)
		// 		{
		// 			String resultSavePath = new File(selectedDir, selectedFile).getAbsolutePath();
		// 			/* write result (converted) into the file indicated by resultSavePath*/
		// 			try 
		// 			{
		// 				PrintStream resultFilePrinter = new PrintStream(new File(resultSavePath));
		// 				if(geneNames != null) {
		// 					for(int m=0; m<numberOfGenes; m++)
		// 					{
		// 						for(int n=0; n<numberOfGenes; n++) 
		// 						{   
		// 							if(network[m][n] == 1) 
		// 								resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 1);
		// 							else
		// 		    				   	resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 0);
		// 							
		// 							resultFilePrinter.println();
		// 						}
		// 					}
		// 				} else {
		// 					for(int m=0; m<numberOfGenes; m++)
		// 					{
		// 						for(int n=0; n<numberOfGenes; n++) 
		// 						{   
		// 							if(network[m][n] == 1) 
		// 								resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
		// 							else
		// 		    				   	resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
		// 							
		// 							resultFilePrinter.println();
		// 						}
		// 					}
		// 				}
		// 				resultFilePrinter.close();
		// 			} catch (FileNotFoundException e) {
		// 				e.printStackTrace();
		// 			}
		// 		}
		// 	}
		// 	else if(optionValue == JOptionPane.NO_OPTION){
		// 		/* create a visualization class object and pass the network*/
		// 		try {
		// 			GrnVisulizeNetwork visualization = new GrnVisulizeNetwork(network, numberOfGenes);
		// 		} catch (Exception e) {
		// 			e.printStackTrace();
		// 		}
		// 	}
		// } 
		// else
		// {
		// 	try 
		// 	{
		// 		File resultFile = new File("RF.tsv");
//		// 		resultFile.deleteOnExit();
		// 		PrintStream resultFilePrinter = new PrintStream(resultFile);
		// 		if(geneNames != null) {
		// 			for(int m=0; m<numberOfGenes; m++)
		// 			{
		// 				for(int n=0; n<numberOfGenes; n++) 
		// 				{   
		// 					if(network[m][n] == 1) 
		// 						resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 1);
		// 					else
		//     				   	resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 0);
		// 					
		// 					resultFilePrinter.println();
		// 				}
		// 			}
		// 		} else {
		// 			for(int m=0; m<numberOfGenes; m++)
		// 			{
		// 				for(int n=0; n<numberOfGenes; n++) 
		// 				{   
		// 					if(network[m][n] == 1) 
		// 						resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
		// 					else
		//     				   	resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
		// 					
		// 					resultFilePrinter.println();
		// 				}
		// 			}
		// 		}
		// 		resultFilePrinter.close();
		// 	} catch (FileNotFoundException e) {
		// 		e.printStackTrace();
		// 	}
		// }
	}
}