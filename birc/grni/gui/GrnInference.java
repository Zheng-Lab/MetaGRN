package birc.grni.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;





public class GrnInference extends GrnInferencePanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GrnInference(){
		
		dbnButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				//analysis_.start();
				dbnInference();
			}
		});
		
		rfButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				rfInference();
			}
		});
		
		elasticNetButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					elasticNetInference();
			}
		});
		
		ridgeButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				ridgeRegression();
				
			}
		});
		
		lassoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lassoInference();
		}
	});
		
		delayLassoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeDelayLassoInference();
			}
		});
	}
	
	public void dbnInference() {
		
		//ADD BY LIU:
		GrnDbn.runByMeta = false;
		
		//JFrame dbnFrame = new JFrame();
		GrnDbn grn_d = new GrnDbn(new JFrame());
		
		grn_d.frame_dbn.setVisible(true);
		
		//dbnFrame.setVisible(true);
	}
	
	public void rfInference() {
		GrnRf.runByMeta = false;
		GrnRf grnRf = new GrnRf(new JFrame());
		grnRf.frameRf.setVisible(true);
	}
	
	public void elasticNetInference() {
		GrnElasticNet.runByMeta = false;
		GrnElasticNet grnElasticNet = new GrnElasticNet(new JFrame());
		grnElasticNet.frameElasticNet.setVisible(true);
	}
	
	public void ridgeRegression() {
		GrnRidge.runByMeta = false;
		GrnRidge grnRidge = new GrnRidge(new JFrame());
		grnRidge.frame_ridge.setVisible(true);
	}
	
	public void lassoInference() {
		GrnLasso.runByMeta = false;
		GrnLasso grnLasso = new GrnLasso(new JFrame());
		grnLasso.frameLasso.setVisible(true);
	}
	
	public void timeDelayLassoInference() {
		//LIU
		GrnTimeDelayLasso.runByMeta = false;
		GrnTimeDelayLasso delayL = new GrnTimeDelayLasso(new JFrame());
		delayL.frame_lassoDelay.setVisible(true);
	}
}
