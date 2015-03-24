package birc.grni.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class MetaScoreRankDisplay {
	
	public JFrame metaScoreRankFrame;
	
	protected JPanel headerPanel = new JPanel();
	protected GrnGuiHeader header = new GrnGuiHeader();
	protected JTable table = null;
	protected JPanel metaScoreRankPanel = new JPanel();
	
	public MetaScoreRankDisplay(JFrame frame, String[][] rankTable)
	{
		metaScoreRankFrame = frame;
		metaScoreRankFrame.setBackground(Color.WHITE);
		metaScoreRankFrame.setBounds(200, 200, 600, 400);
		metaScoreRankFrame.setTitle("MetaScore Rank");
	
		// add header panel
		headerPanel = new JPanel();
		headerPanel.setBackground(Color.WHITE);
		CardLayout cardLayout = new CardLayout();
		headerPanel.setLayout(cardLayout);
		headerPanel.add(header, "header");
		metaScoreRankFrame.getContentPane().add(headerPanel, BorderLayout.NORTH);
		//zmx
		table = new JTable(rankTable, new String[]{"Algorithms","Rank","Distance"});
		table.setEnabled(false);
		metaScoreRankPanel.setLayout(new BorderLayout());
		metaScoreRankPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
		metaScoreRankPanel.add(table, BorderLayout.CENTER);
		metaScoreRankPanel.add(table);
		metaScoreRankFrame.getContentPane().add(metaScoreRankPanel, BorderLayout.CENTER);
	}
}