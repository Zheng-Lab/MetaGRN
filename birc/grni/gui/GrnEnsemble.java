package birc.grni.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import birc.grni.en.ProgressBarAdaptorEN;
import birc.grni.util.CommonUtil;
import birc.grni.util.InputData;

public class GrnEnsemble extends GrnEnsembleDisplay{
	private ArrayList<ArrayList<Double>> inputDataMatrix = new ArrayList<ArrayList<Double>>();

	public static boolean runByMeta = false;
	public GrnEnsemble (JFrame frame){
		super(frame);
		headerEnsemble.setTitle("Select Input Data File for Ensemble");
		headerEnsemble.setInfo("After selecting input data file, click start button");
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				startButton.setEnabled(false);
				InputData originalData = null;
				String inputFilePath = dataFilePathField.getText();
				try {
					originalData = CommonUtil.readInput(inputFilePath, withheaderCheckBox.isSelected()/*withHeader*/, rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header")/*geneNameAreColumnHeader*/);
					
				} catch(IOException ioex) {
					ioex.printStackTrace();
				}
				inputDataMatrix = originalData.getData();
				System.out.println("generateNetworkButton: inputdataMatrix size: "+inputDataMatrix.get(0).size());
				
				if(dbnAlgorithmCheckBox.isSelected())
				{
					GrnDbn.runByMeta = true;
					GrnDbn grnDbn = new GrnDbn(new JFrame());
					grnDbn.getDataFilePathDbn().setText(inputFilePath);
					grnDbn.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
					if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
						grnDbn.columnHeaderRadioButton.setSelected(true);
					else
						grnDbn.rowHeaderRadioButton.setSelected(true);
					//grnDbn.
					grnDbn.frame_dbn.setVisible(true);
				}
				
				if(rfAlgorithmCheckBox.isSelected())
				{
					GrnRf.runByMeta = true;
					GrnRf grnRf = new GrnRf(new JFrame());
					grnRf.getDataFilePathField().setText(inputFilePath);
					grnRf.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
					if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
						grnRf.columnHeaderRadioButton.setSelected(true);
					else
						grnRf.rowHeaderRadioButton.setSelected(true);
					grnRf.frameRf.setVisible(true);
				}
				
				if(enAlgorithmCheckBox.isSelected())
				{
					GrnElasticNet.runByMeta = true;
					GrnElasticNet grnElasticNet = new GrnElasticNet(new JFrame());
					grnElasticNet.getDataFilePathField().setText(inputFilePath);
					grnElasticNet.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
					if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
						grnElasticNet.columnHeaderRadioButton.setSelected(true);
					else
						grnElasticNet.rowHeaderRadioButton.setSelected(true);
					grnElasticNet.frameElasticNet.setVisible(true);
				}
				
				if(lassoAlgorithmCheckBox.isSelected())
				{
					GrnLasso.runByMeta = true;
					GrnLasso grnLasso = new GrnLasso(new JFrame());
					grnLasso.getDataFilePathField().setText(inputFilePath);
					grnLasso.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
					if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
						grnLasso.columnHeaderRadioButton.setSelected(true);
					else
						grnLasso.rowHeaderRadioButton.setSelected(true);
					grnLasso.frameLasso.setVisible(true);
				}
				
				if(lassoDelayAlgorithmCheckBox.isSelected())
				{
					GrnTimeDelayLasso.runByMeta = true;
					GrnTimeDelayLasso grnTimeDelayLasso = new GrnTimeDelayLasso(new JFrame());
					grnTimeDelayLasso.getInputFilePathTextField().setText(inputFilePath);
					grnTimeDelayLasso.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
					if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
						grnTimeDelayLasso.columnHeaderRadioButton.setSelected(true);
					else
						grnTimeDelayLasso.rowHeaderRadioButton.setSelected(true);
					grnTimeDelayLasso.frame_lassoDelay.setVisible(true);
				}
				
				if(ridgeAlgorithmCheckBox.isSelected())
				{
					GrnRidge.runByMeta = true;
					GrnRidge grnRidge = new GrnRidge(new JFrame());
					grnRidge.getInputFilePathField().setText(inputFilePath);
					grnRidge.withheaderCheckBox.setSelected(withheaderCheckBox.isSelected());
					if(rowColumnChooseButtonGroup.getSelection().getActionCommand().equals("column header"))
						grnRidge.columnHeaderRadioButton.setSelected(true);
					else
						grnRidge.rowHeaderRadioButton.setSelected(true);
					grnRidge.frame_ridge.setVisible(true);
				}
				
				
				
				
			}
		});
		
	}
}

