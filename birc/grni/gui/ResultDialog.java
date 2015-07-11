package birc.grni.gui;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import birc.grni.gui.visulization.GrnVisulizeNetwork;
import birc.grni.util.Logging;

 /**
 * 
 * custom dialog without closing after choosing one option 
 * 
 * Implementation uses David Kroukamp's answer in
 * http://stackoverflow.com/questions/13055107/joptionpane-check-user-input-and-prevent-from-closing-until-conditions-are-met
 * 
 */
public class ResultDialog extends JDialog implements ActionListener, PropertyChangeListener {
	   
	// GUI
    private final String saveButtonString = "Save Result";
    private final String visualizeButtonString = "Visualize Result";
    private final Object[] options = {saveButtonString, visualizeButtonString};
    private final String title = "Inference Result";
    private final String message = "What do you like to do for the results?";
    
    private final JOptionPane optionPane = new JOptionPane(message,
    		JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION,
            null,
            options,
            options[0]);
    
    // logic
    private int network [][];
    private int numberOfGenes;
    private ArrayList<String> geneNames;
    private int[][] timeDelay;

    /**
     * Creates the reusable dialog.
     */
    
    public ResultDialog(Frame parentComponent, int[][] network, int numberOfGenes, ArrayList<String> geneNames, int[][] timeDelay) {

    	// GUI

        super(parentComponent, false); // must set model to false, otherwise will block input to it's patent component (e.g., visualization window)
        setTitle(this.title);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2, screenSize.height/2);

        //Make this dialog display it.
        setContentPane(optionPane);
        //Handle window closing correctly.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        pack();
        
        // logic
        
        this.numberOfGenes = numberOfGenes;
        this.geneNames = geneNames;
        this.network = network;
        this.timeDelay = timeDelay;
    }

    /**
     * This method handles events for the text field.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(this.options[0]);
    }

    /**
     * This method reacts to state changes in the option pane.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
                && (e.getSource() == optionPane)
                && (JOptionPane.VALUE_PROPERTY.equals(prop)
                || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);

            if (this.saveButtonString.equals(value)) {
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
						
						if(timeDelay != null)
						{

							String resultSavePathNoExt = "";
							if(resultSavePath.contains(".")) {
								resultSavePathNoExt = resultSavePath.substring(0, resultSavePath.lastIndexOf("."));
							} else {
								resultSavePathNoExt = resultSavePath;
							}
							
							PrintStream timeDelayPrinter = new PrintStream(new File(resultSavePathNoExt + "_timedelay.tsv"));

							// LIU
							if (geneNames != null) {
								for (int m = 0; m < numberOfGenes; m++) {
									for (int n = 0; n < numberOfGenes; n++) {
										if (timeDelay[m][n] != 0) {
											int delay = timeDelay[m][n];
											timeDelayPrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + delay);
											timeDelayPrinter.println();
										}
									}
								}
							} else {
								for (int m = 0; m < numberOfGenes; m++) {
									for (int n = 0; n < numberOfGenes; n++) {
										if (timeDelay[m][n] != 0) {
											int delay = timeDelay[m][n];
											timeDelayPrinter.print("G" + (m + 1) + "\t" + "G" + (n + 1) + "\t" + delay);
											timeDelayPrinter.println();
										}
									}
								}
							}
							timeDelayPrinter.close();
						}

					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
            } else if (this.visualizeButtonString.equals(value)) { //user closed dialog or clicked cancel
            	Logging.logger.log(Level.INFO, "visualization has selected");
				// create a visualization class object and pass the network
				try {
					GrnVisulizeNetwork visualization = new GrnVisulizeNetwork(this.network, this.numberOfGenes, this.geneNames);
				} catch (Exception ex) {
					Logging.logger.log(Level.SEVERE, "exception has thrown - " + e.toString());
					ex.printStackTrace();
				}
            } else {
            	exit();
            }
        }
    }
    
    /**
     * This method clears the dialog and hides it.
     */
    public void exit() {
        dispose();
    }
}