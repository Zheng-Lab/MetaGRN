package birc.grni.gui.visulization;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import ch.epfl.lis.gnwgui.*;
import ch.epfl.lis.imod.*;
import ch.epfl.lis.networks.ios.*;


public class GrnVisualDisplay extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JLabel inputFileLabel;
	protected TextField inputTextField;
	protected JButton chooseButton;
	protected JButton generateNetworkButton;

	private String filepath;
	private NetworkElement element;
	
	//LIU
	public GrnVisualDisplay() {
		
		super();

		// ADD BY LIU
		this.setBackground(Color.WHITE);
		
		this.setLayout(new GridLayout(3,1,5,5));
		
		JPanel emptyPanel = new JPanel(new FlowLayout());
		JPanel upPanel = new JPanel(new FlowLayout());
		JPanel lowPanel = new JPanel(new FlowLayout());
		
		// ADD BY LIU
		emptyPanel.setBackground(Color.WHITE);
		upPanel.setBackground(Color.WHITE);
		lowPanel.setBackground(Color.WHITE);
		
		this.setName("visulizationPanel");

		inputFileLabel = new JLabel("Upload a file:  ");
		inputTextField = new TextField("upload the file for network visulization:   ");
		chooseButton = new JButton("Choose");
		chooseButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						/* pop up the file chooser window*/
						JFileChooser chooser = new JFileChooser();
					    int returnVal = chooser.showOpenDialog(null);
					    if(returnVal == JFileChooser.APPROVE_OPTION) {
					    	filepath = chooser.getSelectedFile().getAbsolutePath();
					    	inputTextField.setText(chooser.getSelectedFile().getAbsolutePath());
		
					    	URL inputfileURL = null;
							
							try {
								inputfileURL = new File(filepath).toURI().toURL();
							} catch (MalformedURLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							String networkName = "Network Name";	

							try {
								element = IONetwork.loadItem(networkName, inputfileURL, ImodNetwork.TSV);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	
					    }
						
					}
				}
	);
		
		generateNetworkButton = new JButton("generate network");
	
		lowPanel.add(generateNetworkButton);
		
		upPanel.add(inputFileLabel);
		upPanel.add(inputTextField);
		upPanel.add(chooseButton);
		lowPanel.add(generateNetworkButton);
		this.add(emptyPanel);
		this.add(upPanel);
		this.add(lowPanel);
		
		generateNetworkButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
					GrnGraphViewerWindow window = new GrnGraphViewerWindow(new JFrame(), element);	
					}
				}
		);

	}
	
//	public GrnVisualDisplay() {
//		
//		super();
//
//		this.setBackground(Color.WHITE);		
//		this.setName("visulizationPanel");
//		
//		this.setLayout(new GridBagLayout());
//
//		inputFileLabel = new JLabel("Upload a file:  ");
//		inputTextField = new TextField("upload the file for network visulization:   ");
//		chooseButton = new JButton("Choose");
//		chooseButton.addActionListener(
//				new ActionListener(){
//					public void actionPerformed(ActionEvent e){
//						/* pop up the file chooser window*/
//						JFileChooser chooser = new JFileChooser();
//					    int returnVal = chooser.showOpenDialog(null);
//					    if(returnVal == JFileChooser.APPROVE_OPTION) {
//					    	filepath = chooser.getSelectedFile().getAbsolutePath();
//					    	inputTextField.setText(chooser.getSelectedFile().getAbsolutePath());
//		
//					    	URL inputfileURL = null;
//							
//							try {
//								inputfileURL = new File(filepath).toURI().toURL();
//							} catch (MalformedURLException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//							
//							String networkName = "Network Name";	
//
//							try {
//								element = IONetwork.loadItem(networkName, inputfileURL, ImodNetwork.TSV);
//							} catch (FileNotFoundException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							} catch (ParseException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							} catch (Exception e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
//	
//					    }
//						
//					}
//				}
//	);
//		
//		generateNetworkButton = new JButton("generate network");
//	
//		lowPanel.add(generateNetworkButton);
//		
//		upPanel.add(inputFileLabel);
//		upPanel.add(inputTextField);
//		upPanel.add(chooseButton);
//		lowPanel.add(generateNetworkButton);
//		this.add(emptyPanel);
//		this.add(upPanel);
//		this.add(lowPanel);
//		
//		generateNetworkButton.addActionListener(
//				new ActionListener(){
//					public void actionPerformed(ActionEvent e) {
//					GrnGraphViewerWindow window = new GrnGraphViewerWindow(new JFrame(), element);	
//					}
//				}
//		);
//
//	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	public NetworkElement getElement() {
		return element;
	}

	public void setElement(NetworkElement element) {
		this.element = element;
	}

}


	

