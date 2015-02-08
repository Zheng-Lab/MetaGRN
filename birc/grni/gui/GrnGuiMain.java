package birc.grni.gui;


import java.awt.event.*;
//import java.rmi.server.LogStream;
import java.util.logging.*;

import javax.swing.*;
import com.jgoodies.looks.plastic.*;
import com.jgoodies.looks.plastic.theme.*;

public class GrnGuiMain extends GrnGui {

	private final static Logger logger = Logger.getLogger(GrnGuiMain.class.getName());
//	private static FileHandler fh = null;
	
	public  GrnGuiMain() {
		// TODO Auto-generated constructor stub
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//CHANGE BY LIU
//		try {
//			fh=new FileHandler("Grn Software Logging", true);
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		fh.setFormatter(new SimpleFormatter());
//		Logger l = Logger.getLogger("");
//		l.addHandler(fh);
//		l.setLevel(Level.CONFIG);
		
		logger.log(Level.INFO, "Start point of the programme");
		
		try 
		{
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
		} catch (Exception e) {
			
		}
		
		GrnGuiMain grnGuiMain = new GrnGuiMain();
		grnGuiMain.initialize();
		
		//CHANGE BY LIU
		//logger.log(Level.INFO, "End of main method");
	}
	
	private void initialize(){
	
		aboutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainLayout.show(mainPanel, aboutPanel.getName());
				header.setTitle("Gene Regulatory Network Inference and Visualization");
				header.setInfo("General overview ");
				// TODO Auto-generated method stub	
			}
		});
		
		grnInferenceButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//inferenceAlgoPanel.refreshAllModels();
				mainLayout.show(mainPanel, grnInferencePanel.getName());
				header.setTitle("Gene Regulatory Network Inference");
				header.setInfo("Inferring GRN from time-series gene expression ");
			}
		});
		
		// ADD BY LIU
		
		metaGrnButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//inferenceAlgoPanel.refreshAllModels();
				mainLayout.show(mainPanel, metaGrnPanel.getName());
				header.setTitle("Meta-GRN");
//				header.setInfo("Use meta-algorithm to choose best algoirthm for the data ");
			}
		});
		
		visualizationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mainLayout.show(mainPanel, visualizationPanel.getName());
				header.setTitle(" Network Visualization");
				header.setInfo("Visualization of the Inferred networks ");
			}
		});
		
		helpButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed( final ActionEvent e) {
				// TODO Auto-generated method stub
				mainLayout.show(mainPanel, helpPanel.getName());
				header.setTitle("Help");
				header.setInfo("Software manual and Documentation");
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(shouldExitOrNot()) {
					closeSoftware();
				}
			}
		});
	}
	
	public boolean shouldExitOrNot() {
		
		int n = JOptionPane.showConfirmDialog(
				frame,
				"Do you Want to Exit ?",
				"Message",
				JOptionPane.YES_NO_OPTION
				
				);

		if (n == JOptionPane.YES_OPTION)
			return true; // If the user selected YES
		else
			return false;
	}
	
	public void closeSoftware() {
		System.exit(0);
	}

}
