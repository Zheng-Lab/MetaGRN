package birc.grni.gui.visulization;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import birc.grni.gui.GrnGuiHeader;

public class GrnGenericWindow extends JDialog {

	private static final long serialVersionUID = 167932948179146894L;
	private JPanel headerPanel;
	private GrnGuiHeader header;
	

	public GrnGenericWindow(JFrame aFrame, boolean modal) {
		super(aFrame, modal);

		header = new GrnGuiHeader();
		aFrame.setBackground(Color.WHITE);
		aFrame.getContentPane().setFocusCycleRoot(true);
		aFrame.getContentPane().setBackground(Color.WHITE);
		aFrame.setSize(new Dimension(0, 0));
		aFrame.setBounds(100, 100, 900, 600);
		aFrame.setTitle("GRN Inference and Visualization Software");
		//aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.WHITE);
		final CardLayout cards_ = new CardLayout();
		headerPanel.setLayout(cards_);
		aFrame.getContentPane().add(headerPanel, BorderLayout.NORTH);
		header.setPreferredSize(new Dimension(10, 80));
		headerPanel.add(header, "header");
		
		aFrame.setVisible(true);

	}

	

}
