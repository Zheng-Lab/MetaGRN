package birc.grni.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import birc.grni.gui.visulization.GrnVisualization;


public class GrnGui {

	/**
	 * @param args
	 */
	protected JFrame frame;
	protected JPanel mainPanel;
	protected JPanel mainButtonPanel;
	protected JPanel westPanel;
	protected JPanel headerPanel;
	protected GrnGuiHeader header;
	
	protected CardLayout mainLayout;
	
	protected JPanel aboutPanel;
	protected JPanel grnInferencePanel;
	protected JPanel metaGrnPanel;//LIU
	protected JPanel visualizationPanel;
	protected JScrollPane helpPanel;
	
	protected ButtonGroup mainButtonGroup;
	protected JButton exitButton;
	protected JButton aboutButton;
	protected JButton grnInferenceButton;
	protected JButton metaGrnButton;//LIU
	protected JButton visualizationButton;
	protected JButton helpButton;
	
	protected GrnInference inferenceAlgoTypes;
	private GrnVisualization grnVisualization;
	
	private  final static Logger logger = Logger.getLogger(GrnGui.class.getName());
	
	public GrnGui() {
		
		//GrnGui gui_ = new GrnGui();
		//gui_.guiInitialize();
		
		logger.log(Level.INFO, "inside grnGui constructor");
		
		header = new GrnGuiHeader();
		guiInitialize();
		
		logger.log(Level.INFO, "end of grnGui constructor");

	}
	
	private void guiInitialize(){
		
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setFocusCycleRoot(true);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setSize(new Dimension(0,0));
		frame.setBounds(100,100,900,600);
		frame.setTitle("GRN Inference and Visualization Software");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.WHITE);
		final CardLayout cards_ = new CardLayout();
		headerPanel.setLayout(cards_);
		frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
		header.setPreferredSize(new Dimension(0, 80));
		headerPanel.add(header, "header");
		
		
		// main layout and main display panel
		mainLayout = new CardLayout();
		mainPanel = new JPanel();
		mainPanel.setLayout(mainLayout);
		mainPanel.setBackground(Color.WHITE);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		// button, west panel & button group
		westPanel = new JPanel();
		westPanel.setBackground(Color.WHITE);
		westPanel.setLayout(new BorderLayout());
		frame.getContentPane().add(westPanel, BorderLayout.WEST);
		
		mainButtonPanel = new JPanel();
		mainButtonPanel.setBackground(Color.WHITE);
		mainButtonPanel.setFocusCycleRoot(true);
		// CHANGE BY LIU
//		final GridLayout gridLayout_1 = new GridLayout(5,1,10,10);
		final GridLayout gridLayout_1 = new GridLayout(6,1,10,10);
		mainButtonPanel.setLayout(gridLayout_1);
		mainButtonPanel.setMaximumSize(new Dimension(200, 200));
		mainButtonPanel.setSize(200,300);
		mainButtonPanel.setPreferredSize(new Dimension(150, 50));
		westPanel.add(mainButtonPanel);
		
		mainButtonGroup = new ButtonGroup();
		
		// add about panel to main panel
		aboutPanel = new JPanel();
		aboutPanel.setName("About Panel");
		aboutPanel.setBackground(Color.WHITE);
		
		// CHANGE BY LIU
		aboutPanel.setLayout(new GridLayout(2, 1));
		
		//ADD BY LIU
		JPanel pictureAboutPanel = new JPanel() {

		    private BufferedImage image;

		    {
		       try 
		       {                
		    	   java.net.URL imageURL = getClass().getResource(birc.grni.util.GLOBALVAR.resourceFolder + "/" + birc.grni.util.GLOBALVAR.resourcePictureFolder + "/" + "aboutpanel_up.png");
		    	   image = ImageIO.read(imageURL);
		       } 
		       catch (IOException ex) 
		       {
		    	   ex.printStackTrace();
		       }
		    }

		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // see javadoc for more info on the parameters            
		    }
		};
		
		//ADD BY LIU
		JPanel wordAboutPanel = new JPanel() {
			
			{
				JTextArea aboutTextArea = new JTextArea (
		                "This is the beta version of our gene regulatory network (GRN) inference and visualization software, MetaGRN.\n\n" +
		                "We incorporate several popular and promising GRN inference algorithms in MetaGRN, for example, Dynamic Bayesian Network, Random Forests, Elastic Net, Ridge Regression, and so on.\n\n" +
		                "We also come up with a meta algorithm to help user compare and analysis those algorithms comprehensively. Based on the result of meta algorithm, users can pick best one for their input data (gene expression data).\n\n"	+
		                "We also provide visualization module for GRN, this module can help users achieve a direct view of their networks easily."
		        );
				aboutTextArea.setEditable(false);
				aboutTextArea.setBackground(Color.WHITE);
				aboutTextArea.setLineWrap(true);
				//aboutTextArea.setFont(f);
				aboutTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	//leave space around the about information area

				this.setLayout(new BorderLayout());		//make the about information area fit to the whole panel
				this.add(aboutTextArea);
			}
		};
		
		aboutPanel.add(pictureAboutPanel, BorderLayout.NORTH);
		aboutPanel.add(wordAboutPanel, BorderLayout.SOUTH);
		
		mainPanel.add(aboutPanel, aboutPanel.getName());

		// add about button to main panel
		aboutButton = new JButton("About");
		//final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
		mainButtonPanel.add(aboutButton);
		aboutButton.setPreferredSize(new Dimension(70,50));
		mainButtonGroup.add(aboutButton);
		
		// add GRN inference panel
		grnInferencePanel = new JPanel();
		grnInferencePanel.setLayout(new BorderLayout());
		grnInferencePanel.setName("GRN Inference Panel");
		grnInferencePanel.setBackground(Color.WHITE);
		
		mainPanel.add(grnInferencePanel, grnInferencePanel.getName());
		
		inferenceAlgoTypes = new GrnInference();
		grnInferencePanel.add(inferenceAlgoTypes);
		//final FlowLayout flowLayout_3 = new FlowLayout();
		//flowLayout_3.setAlignment(FlowLayout.RIGHT);
		
		// add GRN inference Button
		grnInferenceButton = new JButton("GRN Inference");
		//final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		mainButtonPanel.add(grnInferenceButton);
		grnInferenceButton.setPreferredSize(new Dimension(70,50));
		mainButtonGroup.add(grnInferenceButton);
		
		// ADD BY LIU
		
		// add meta-grn button
		metaGrnButton = new JButton("Meta GRN");
		mainButtonPanel.add(metaGrnButton);
		metaGrnButton.setPreferredSize(new Dimension(70,50));
		mainButtonGroup.add(metaGrnButton);
		
		// add meta-grn panel
		metaGrnPanel = new JPanel();
		metaGrnPanel.setName("Meta-GRN Panel");
		metaGrnPanel.setBackground(Color.WHITE);
		metaGrnPanel.setLayout(new BorderLayout());
		mainPanel.add(metaGrnPanel, metaGrnPanel.getName());
		
		metaGrnPanel.add(new MetaGrnDisplay());
		
		// add visualization panel and button 
		visualizationPanel = new JPanel();
		visualizationPanel.setName("Visualization Panel");
		visualizationPanel.setBackground(Color.WHITE);
		visualizationPanel.setLayout(new BorderLayout());
		mainPanel.add(visualizationPanel, visualizationPanel.getName());

		grnVisualization = new GrnVisualization();
		visualizationPanel.add(grnVisualization);
		
		visualizationButton = new JButton("Visualization");
		mainButtonPanel.add(visualizationButton);
		visualizationButton.setPreferredSize(new Dimension(70,50));
		mainButtonGroup.add(visualizationButton);
		
		// add help panel and button
		
		helpPanel = new JScrollPane();
		helpPanel.setName("Help Panel");
		
		//LIU
//		helpPanel.setBackground(Color.WHITE);
		helpPanel.getViewport().setBackground(Color.WHITE);
		
		helpPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(helpPanel, helpPanel.getName());
		
		helpButton = new JButton("Help");
		mainButtonPanel.add(helpButton);
		helpButton.setPreferredSize(new Dimension(70,50));
		mainButtonGroup.add(helpButton);
		
		// add exit button
		
		exitButton = new JButton("Exit");
		mainButtonPanel.add(exitButton);
		exitButton.setPreferredSize(new Dimension(70,50));
		mainButtonGroup.add(exitButton);
		
		// add content to about panel
		
		frame.setVisible(true);
	}
}
