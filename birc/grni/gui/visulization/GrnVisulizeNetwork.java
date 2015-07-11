package birc.grni.gui.visulization;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import ch.epfl.lis.gnwgui.IONetwork;
import ch.epfl.lis.gnwgui.NetworkElement;
import ch.epfl.lis.imod.ImodNetwork;

public class GrnVisulizeNetwork {

	private NetworkElement element;
	
	private static Logger logger = Logger.getLogger(GrnVisulizeNetwork.class.getName());

	public GrnVisulizeNetwork(int [][] network, int numberOfGenes, ArrayList<String> geneNames) throws Exception {

		//String filepath = "tmp/network.txt";
		String tmpDir = System.getProperty("java.io.tmpdir");		
		File path1 = new File(tmpDir, "finalnetwork.txt");
		String filepath = path1.getAbsolutePath();
		
		logger.log(Level.INFO, "Visualization started");
		
		// liuxingliang 
		// use PrinterStream to format output better and use platform independent newline
		// add logic for real gene names
		PrintStream resultFilePrinter = new PrintStream(new File(filepath));
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
		URL inputfileURL = null;
		inputfileURL = new File(filepath).toURI().toURL();
		
		String networkName = "Network Name";	

		element = IONetwork.loadItem(networkName, inputfileURL, ImodNetwork.TSV);
		
		logger.log(Level.INFO, "Visualization network element is created");
		
		GrnGraphViewerWindow window = new GrnGraphViewerWindow(new JFrame(),
				element);

	}
}
