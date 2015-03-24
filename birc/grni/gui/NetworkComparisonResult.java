package birc.grni.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetworkComparisonResult{
	
	private static final long serialVersionUID = 1L;
	
	private double precision;
	private double recall;
	//private double f_score;
	private BigDecimal bd;
	protected JFrame frame;
	private JPanel headerPanel;
	private JPanel textFieldPanel;
	protected GrnGuiHeader result_header=new GrnGuiHeader();
	
	public NetworkComparisonResult(double precision,double recall,BigDecimal bd,JFrame frame){
		
		this.precision=precision;
		this.recall=recall;
		//this.f_score=f_score;
		this.bd=bd;
		this.frame=frame;
		gui_initialize();
		
	}
	public void gui_initialize(){
		
		//initialize frame
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setSize(new Dimension(0,0));
		frame.setBounds(200,200,600,400);
		frame.setTitle("Network Comparison Result");
		
		//new header
		//result_header =new GrnGuiHeader();
		//result_header.setTitle("Network Comparison Result");
		//result_header.setInfo("Enter parameters and click start button ");
		
		
		//header panel
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.WHITE);
		final CardLayout cards_ = new CardLayout();
		headerPanel.setLayout(cards_);
		frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
		headerPanel.add(result_header, "header");

		
		
		//textfield panel
		textFieldPanel = new JPanel();
		textFieldPanel.setBackground(Color.WHITE);
		textFieldPanel.setLayout(new GridBagLayout());
		
		frame.getContentPane().add(textFieldPanel,BorderLayout.CENTER);
		
		
		
		
		//label
		JLabel titleLabel= new JLabel("Network Comparison Result");
		JLabel precisionLabel = new JLabel("Precision");
		JLabel recallLabel = new JLabel("Recall");
		JLabel fscoreLabel = new JLabel("Fscore");
		
		//content
		JLabel precisionContentLabel = new JLabel(precision+"");
		JLabel recallContentLabel = new JLabel(recall+"");
		JLabel fscoreContentLabel = new JLabel(bd+"");
		
		textFieldPanel.setName("Network Comparison Result Panel");
		
		/* set layout of this panel*/
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints(); 
		
				
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(titleLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints(); 
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(precisionLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints(); 
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(precisionContentLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints(); 
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(recallLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints(); 
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(recallContentLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints(); 
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(fscoreLabel, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints(); 
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1;
		//gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.insets = new Insets(15,15,0,10);
		textFieldPanel.add(fscoreContentLabel, gridBagConstraints);
		
	}
}
