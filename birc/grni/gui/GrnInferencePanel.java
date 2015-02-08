package birc.grni.gui;


import java.awt.*;
import javax.swing.*;

public class GrnInferencePanel extends JPanel{

	protected JPanel algoPanel;
	
	protected JButton dbnButton;
	protected JButton rfButton;
	protected JButton lassoButton;
	protected JButton elasticNetButton;
	protected JButton ridgeButton;
	protected JButton delayLassoButton;
	
	protected FlowLayout algoPanelLayout;
	
	public GrnInferencePanel() {
		
		super();
		setLayout(new FlowLayout());

		this.setLayout(new FlowLayout());
		this.setName("algoPanel");
		//LIU
//		this.setBackground(new Color(50, 100, 200));
		this.setBackground(Color.WHITE);
		
		
		

		dbnButton = new JButton();
		dbnButton.setMargin(new Insets(2, 0, 2, 0));
		dbnButton.setPreferredSize(new Dimension(100, 80));
		this.add(dbnButton);
		dbnButton.setText("DBN");

		rfButton = new JButton();
		rfButton.setMargin(new Insets(2, 0, 2, 0));
		rfButton.setPreferredSize(new Dimension(100, 80));
		rfButton.setText("<html><center>Random<br>Forests</center></html>");
		this.add(rfButton);
		
		elasticNetButton = new JButton();
		elasticNetButton.setMargin(new Insets(2, 0, 2, 0));
		elasticNetButton.setPreferredSize(new Dimension(100, 80));
		this.add(elasticNetButton);
		elasticNetButton.setText("ElasticNet");

		lassoButton = new JButton();
		lassoButton.setMargin(new Insets(2, 0, 2, 0));
		lassoButton.setPreferredSize(new Dimension(100, 80));
		this.add(lassoButton);
		lassoButton.setText("Lasso");
		
		delayLassoButton = new JButton();
		delayLassoButton.setMargin(new Insets(2, 0, 2, 0));
		delayLassoButton.setPreferredSize(new Dimension(100, 80));
		delayLassoButton.setText("<html><center>Time-delayed<br>Lasso</center></html>");
		this.add(delayLassoButton);
		
		ridgeButton = new JButton();
		ridgeButton.setMargin(new Insets(2, 0, 2, 0));
		ridgeButton.setPreferredSize(new Dimension(100, 80));
		ridgeButton.setText("<html><center>Ridge<br>Regression</center></html>");
		this.add(ridgeButton);
		
	}

}
