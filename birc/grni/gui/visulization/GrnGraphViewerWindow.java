package birc.grni.gui.visulization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ch.epfl.lis.gnwgui.NetworkElement;
import ch.epfl.lis.gnwgui.NetworkGraph;

public class GrnGraphViewerWindow extends GrnGenericWindow {

	private static final long serialVersionUID = 1L;
	protected JPanel leftPanel;
	protected JPanel visualPanel;

	public GrnGraphViewerWindow(JFrame aFrame, NetworkElement element) {
		super(aFrame, false);

		leftPanel = new JPanel();
		visualPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		
		if (element.getNetworkViewer() == null) {
			element.setNetworkViewer(new NetworkGraph(element));
		}		
		
		visualPanel.add(element.getNetworkViewer().getScreen(), BorderLayout.EAST);
		
		// Add and display the network viewer controls
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		leftPanel.add(element.getNetworkViewer().getControl(), gridBagConstraints);
		
		aFrame.add(leftPanel, BorderLayout.WEST);
		aFrame.add(visualPanel);
	}



}
