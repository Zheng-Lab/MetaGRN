/*
Copyright (c) 2008-2010 Daniel Marbach & Thomas Schaffter

We release this software open source under an MIT license (see below). If this
software was useful for your scientific work, please cite our paper(s) listed
on http://gnw.sourceforge.net.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package ch.epfl.lis.gnwgui.windows;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.Box;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import ch.epfl.lis.gnwgui.GnwGuiSettings;
import ch.epfl.lis.gnwgui.SearchBox;
import ch.epfl.lis.gnwgui.NetworkGraph;

/** Graphic components that control the graph representation.
 * 
 * @author Thomas Schaffter (firstname.name@gmail.com)
 *
 */
public class GraphViewerController extends JPanel {
	
	/** Serialization */
	private static final long serialVersionUID = 1L;
	
	/** Controller panel */
	protected JPanel controlerPanel_;
	
	/** Switch "Move graph"/"Move nodes" */
	public JComboBox<String> interactionMode_;
	/** Selection of different graph layouts */
	protected JComboBox<String> layoutCombo_;
	
	/** Search node by id */
	protected SearchBox search_;
	
	/** To distinguish interactions by arrow shape */
	protected JCheckBox distinguishByArrowHead_;
	/** To distinguish interactions by color */
	protected JCheckBox distinguishByColor_;
	/** Enable/disable curved edges */
	protected JCheckBox curvedEdges_;
	/** Show/hide nodes' label */
	protected JCheckBox displayLabels_;
	
	/**Delete selected edge*/
	protected JButton deleteEdgeButton;
	
	/** Delete selected node */
	protected JButton deleteNodeButton;
	
	/**Create edge*/
	protected JButton createEdgeButton;
	
	/**Save user-edited graph*/
	public static JButton saveEditButton;
	
	/** Export graph as image */
	protected JButton exportButton_;
	
    /** Logger for this class */
    @SuppressWarnings("unused")
	private static Logger log_ = Logger.getLogger(GraphViewerController.class.getName());


	// ============================================================================
	// PUBLIC METHODS
    
    /**
     * Constructor
     */
	public GraphViewerController() {
		
		super();
		setBackground(Color.WHITE);
		
		controlerPanel_ = new JPanel();
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {7,7};
		gridBagLayout.rowHeights = new int[] {7,7,7,7,7,7,7,7,7,7,0,7,0,7,7,0,7,7};
		controlerPanel_.setLayout(gridBagLayout);
		controlerPanel_.setBorder(new TitledBorder(null, "Visualization Controls", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Sans", Font.PLAIN, 12), null));
		controlerPanel_.setBackground(Color.WHITE);
		add(controlerPanel_);

		final Component component_8 = Box.createVerticalStrut(5);
		final GridBagConstraints gridBagConstraints_17 = new GridBagConstraints();
		gridBagConstraints_17.gridx = 1;
		gridBagConstraints_17.gridy = 0;
		controlerPanel_.add(component_8, gridBagConstraints_17);

		interactionMode_ = new JComboBox<String>();
		final GridBagConstraints gridBagConstraints_2 = new GridBagConstraints();
		gridBagConstraints_2.anchor = GridBagConstraints.WEST;
		gridBagConstraints_2.gridx = 1;
		gridBagConstraints_2.gridy = 1;
		controlerPanel_.add(interactionMode_, gridBagConstraints_2);
		interactionMode_.setModel(new DefaultComboBoxModel<String>(new String[] {"Transforming", "Picking", "Selecting a Seed"}));

		final Component component = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridx = 1;
		controlerPanel_.add(component, gridBagConstraints);

		layoutCombo_ = new JComboBox<String>();
		layoutCombo_.setModel(new DefaultComboBoxModel<String>(new String[] {"Layout"}));
		layoutCombo_.setActionCommand("layoutCombo");
		final GridBagConstraints gridBagConstraints_12 = new GridBagConstraints();
		gridBagConstraints_12.anchor = GridBagConstraints.WEST;
		gridBagConstraints_12.gridy = 3;
		gridBagConstraints_12.gridx = 1;
		controlerPanel_.add(layoutCombo_, gridBagConstraints_12);

		final Component component_3 = Box.createVerticalStrut(5);
		final GridBagConstraints gridBagConstraints_19 = new GridBagConstraints();
		gridBagConstraints_19.gridy = 4;
		gridBagConstraints_19.gridx = 1;
		controlerPanel_.add(component_3, gridBagConstraints_19);
		
		final Component component_13 = Box.createVerticalStrut(20);
		final GridBagConstraints gridBagConstraints_24 = new GridBagConstraints();
		gridBagConstraints_24.gridy = 5;
		gridBagConstraints_24.gridx = 1;
		controlerPanel_.add(component_13, gridBagConstraints_24);

		final JLabel vertexSearchLabel = new JLabel();
		final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
		gridBagConstraints_4.anchor = GridBagConstraints.WEST;
		gridBagConstraints_4.gridy = 6;
		gridBagConstraints_4.gridx = 1;
		controlerPanel_.add(vertexSearchLabel, gridBagConstraints_4);
		vertexSearchLabel.setText("Node search:");

		final Component component_9 = Box.createVerticalStrut(5);
		final GridBagConstraints gridBagConstraints_18 = new GridBagConstraints();
		gridBagConstraints_18.gridy = 7;
		gridBagConstraints_18.gridx = 1;
		controlerPanel_.add(component_9, gridBagConstraints_18);
		
		search_ = new SearchBox();
		final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
		gridBagConstraints_5.anchor = GridBagConstraints.WEST;
		gridBagConstraints_5.gridy = 8;
		gridBagConstraints_5.gridx = 1;
		controlerPanel_.add(search_, gridBagConstraints_5);
		search_.setColumns(12);

		final Component component_14 = Box.createVerticalStrut(20);
		final GridBagConstraints gridBagConstraints_25 = new GridBagConstraints();
		gridBagConstraints_25.gridy = 9;
		gridBagConstraints_25.gridx = 1;
		controlerPanel_.add(component_14, gridBagConstraints_25);
		
		deleteEdgeButton = new JButton();
		final GridBagConstraints gridBagConstraints_28 = new GridBagConstraints();
		gridBagConstraints_28.insets = new Insets(0, 7, 0, 0);
		gridBagConstraints_28.anchor = GridBagConstraints.WEST;
		deleteEdgeButton.setEnabled(false);
		gridBagConstraints_28.gridy = 10;
		gridBagConstraints_28.gridx = 1;
		controlerPanel_.add(deleteEdgeButton, gridBagConstraints_28);
		deleteEdgeButton.setText("<html><center>Delete<br>Edge</center></html>");
		deleteEdgeButton.setToolTipText("<html>Select an edge between 2 nodes<br> on graph to delete (CTRL+E)</html>");
		
		deleteNodeButton = new JButton();
		final GridBagConstraints gridBagConstraints_27 = new GridBagConstraints();
		gridBagConstraints_27.insets = new Insets(0, 50, 0, 0);
		//gridBagConstraints_27.anchor = GridBagConstraints.WEST;
		deleteNodeButton.setEnabled(false);
		gridBagConstraints_27.gridy = 10;
		gridBagConstraints_27.gridx = 1;
		controlerPanel_.add(deleteNodeButton, gridBagConstraints_27);
		deleteNodeButton.setText("<html><center>Delete<br>Node</center></html>");
		deleteNodeButton.setToolTipText("Select node on graph to delete (CTRL+D)");

		final Component component_16 = Box.createVerticalStrut(20);
		final GridBagConstraints gridBagConstraints_30 = new GridBagConstraints();
		gridBagConstraints_30.gridy = 11;
		gridBagConstraints_30.gridx = 1;
		controlerPanel_.add(component_16, gridBagConstraints_30);		
		
		createEdgeButton = new JButton();
		final GridBagConstraints gridBagConstraints_29 = new GridBagConstraints();
		gridBagConstraints_29.insets = new Insets(0, 7, 0, 0);
		gridBagConstraints_29.anchor = GridBagConstraints.WEST;
		createEdgeButton.setEnabled(false);
		gridBagConstraints_29.gridy = 12;
		gridBagConstraints_29.gridx = 1;
		controlerPanel_.add(createEdgeButton, gridBagConstraints_29);
		createEdgeButton.setText("<html><center>Create<br>Edge</center></html>");
		createEdgeButton.setToolTipText("<html>Create an edge between 2 nodes (CTRL+N)</html>");
		
		saveEditButton = new JButton();
		final GridBagConstraints gridBagConstraints_31 = new GridBagConstraints();
		gridBagConstraints_31.insets = new Insets(0, 50, 0, 0);
		//gridBagConstraints_31.anchor = GridBagConstraints.WEST;
		saveEditButton.setEnabled(false);
		gridBagConstraints_31.gridy = 12;
		gridBagConstraints_31.gridx = 1;
		controlerPanel_.add(saveEditButton, gridBagConstraints_31);
		saveEditButton.setText("<html><center>Save<br>Edits</center></html>");
		saveEditButton.setToolTipText("<html>Save user-edited graph (CTRL+S)</html>");
		saveEditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//JOptionPane.showMessageDialog(new JFrame(), "Eggs are not supposed to be green.");
				// robot
				Robot r;
				try {
					r = new Robot();
					
					r.keyPress(KeyEvent.VK_CONTROL);
					r.keyPress(KeyEvent.VK_S);
					// CTRL+S is now pressed (receiving application should see a "key down" event.)
					r.keyRelease(KeyEvent.VK_S);
					r.keyRelease(KeyEvent.VK_CONTROL);
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// trigger
			}
		});
		
		interactionMode_.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String s = (String) interactionMode_.getSelectedItem();
							
						switch (s) {
						case "Edit nodes":
							deleteNodeButton.setEnabled(true);
							createEdgeButton.setEnabled(false);
							deleteEdgeButton.setEnabled(false);
							deleteNodeButton.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0) {
										//JOptionPane.showMessageDialog(new JFrame(), "Eggs are not supposed to be green.");
										// robot
										Robot r;
										try {
											r = new Robot();
											
											r.keyPress(KeyEvent.VK_CONTROL);
											r.keyPress(KeyEvent.VK_D);
											// CTRL+D is now pressed (receiving application should see a "key down" event.)
											r.keyRelease(KeyEvent.VK_D);
											r.keyRelease(KeyEvent.VK_CONTROL);
											
										} catch (AWTException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										// trigger
									}
									
							});
							break;
							
						case "Edit edges":
							deleteNodeButton.setEnabled(false);
							createEdgeButton.setEnabled(true);
							deleteEdgeButton.setEnabled(true);
							deleteEdgeButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									
									Robot r;
									try {
										r = new Robot();
										
										r.keyPress(KeyEvent.VK_CONTROL);
										r.keyPress(KeyEvent.VK_E);
										// CTRL+E is now pressed (receiving application should see a "key down" event.)
										r.keyRelease(KeyEvent.VK_E);
										r.keyRelease(KeyEvent.VK_CONTROL);
									} catch (AWTException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// trigger
								}
						});
							
							createEdgeButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {

									Robot r;
									try {
										r = new Robot();
										
										r.keyPress(KeyEvent.VK_CONTROL);
										r.keyPress(KeyEvent.VK_N);
										// CTRL+N is now pressed (receiving application should see a "key down" event.)
										r.keyRelease(KeyEvent.VK_N);
										r.keyRelease(KeyEvent.VK_CONTROL);
									} catch (AWTException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// trigger
								}
						}); 
							break;
							
						default:
							deleteNodeButton.setEnabled(false);
							createEdgeButton.setEnabled(false);
							deleteEdgeButton.setEnabled(false);
							break;
						} 	
				    }
				});

		final Component component_15 = Box.createVerticalStrut(20);
		final GridBagConstraints gridBagConstraints_26 = new GridBagConstraints();
		gridBagConstraints_26.gridy = 13;
		gridBagConstraints_26.gridx = 1;
		controlerPanel_.add(component_15, gridBagConstraints_26);		
		
		displayLabels_ = new JCheckBox();
		final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
		gridBagConstraints_6.anchor = GridBagConstraints.WEST;
		gridBagConstraints_6.gridy = 14;
		gridBagConstraints_6.gridx = 1;
		controlerPanel_.add(displayLabels_, gridBagConstraints_6);
		displayLabels_.setFocusable(false);
		displayLabels_.setBackground(Color.WHITE);
		displayLabels_.setText("Display labels");

		curvedEdges_ = new JCheckBox();
		curvedEdges_.setFocusable(false);
		final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
		gridBagConstraints_7.anchor = GridBagConstraints.WEST;
		gridBagConstraints_7.gridy = 15;
		gridBagConstraints_7.gridx = 1;
		controlerPanel_.add(curvedEdges_, gridBagConstraints_7);
		curvedEdges_.setBackground(Color.WHITE);
		curvedEdges_.setText("Curved edges");

		final Component component_4 = Box.createVerticalStrut(20);
		final GridBagConstraints gridBagConstraints_13 = new GridBagConstraints();
		gridBagConstraints_13.gridy = 16;
		gridBagConstraints_13.gridx = 1;
		controlerPanel_.add(component_4, gridBagConstraints_13);

		final JLabel inhibitoryConnectionsLabel = new JLabel();
		final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
		gridBagConstraints_8.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints_8.anchor = GridBagConstraints.WEST;
		gridBagConstraints_8.gridy = 17;
		gridBagConstraints_8.gridx = 1;
		controlerPanel_.add(inhibitoryConnectionsLabel, gridBagConstraints_8);
		inhibitoryConnectionsLabel.setBackground(Color.WHITE);
		inhibitoryConnectionsLabel.setText("Distinguish signed edges:");

		final Component component_2 = Box.createVerticalStrut(5);
		final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
		gridBagConstraints_3.gridy = 18;
		gridBagConstraints_3.gridx = 1;
		controlerPanel_.add(component_2, gridBagConstraints_3);

		distinguishByArrowHead_ = new JCheckBox();
		distinguishByArrowHead_.setFocusable(false);
		final GridBagConstraints gridBagConstraints_9 = new GridBagConstraints();
		gridBagConstraints_9.anchor = GridBagConstraints.WEST;
		gridBagConstraints_9.gridy = 19;
		gridBagConstraints_9.gridx = 1;
		controlerPanel_.add(distinguishByArrowHead_, gridBagConstraints_9);
		distinguishByArrowHead_.setBackground(Color.WHITE);
		distinguishByArrowHead_.setText("by arrow head");

		distinguishByColor_ = new JCheckBox();
		distinguishByColor_.setFocusable(false);
		final GridBagConstraints gridBagConstraints_10 = new GridBagConstraints();
		gridBagConstraints_10.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints_10.gridy = 20;
		gridBagConstraints_10.gridx = 1;
		controlerPanel_.add(distinguishByColor_, gridBagConstraints_10);
		distinguishByColor_.setBackground(Color.WHITE);
		distinguishByColor_.setText("by color");

		final Component component_10 = Box.createVerticalStrut(20);
		final GridBagConstraints gridBagConstraints_20 = new GridBagConstraints();
		gridBagConstraints_20.gridy = 21;
		gridBagConstraints_20.gridx = 1;
		controlerPanel_.add(component_10, gridBagConstraints_20);

		final Component component_5 = Box.createHorizontalStrut(10);
		final GridBagConstraints gridBagConstraints_14 = new GridBagConstraints();
		gridBagConstraints_14.gridy = 22;
		gridBagConstraints_14.gridx = 2;
		controlerPanel_.add(component_5, gridBagConstraints_14);

		final Component component_7 = Box.createHorizontalStrut(10);
		final GridBagConstraints gridBagConstraints_16 = new GridBagConstraints();
		gridBagConstraints_16.gridy = 23;
		gridBagConstraints_16.gridx = 0;
		controlerPanel_.add(component_7, gridBagConstraints_16);

		exportButton_ = new JButton();
		final GridBagConstraints gridBagConstraints_11 = new GridBagConstraints();
		gridBagConstraints_11.gridy = 23;
		gridBagConstraints_11.gridx = 1;
		controlerPanel_.add(exportButton_, gridBagConstraints_11);
		exportButton_.setBackground(UIManager.getColor("Button.background"));
		exportButton_.setText("<html><center>Export<br>as image</center></html>");

		final Component component_6 = Box.createVerticalStrut(10);
		final GridBagConstraints gridBagConstraints_15 = new GridBagConstraints();
		gridBagConstraints_15.gridy = 24;
		gridBagConstraints_15.gridx = 1;
		controlerPanel_.add(component_6, gridBagConstraints_15);
		
		GnwGuiSettings settings = GnwGuiSettings.getInstance();
		exportButton_.setIcon(new ImageIcon(settings.getSnapshotImage()));
		
		final Component component_11 = Box.createHorizontalStrut(10);
		final GridBagConstraints gridBagConstraints_21 = new GridBagConstraints();
		gridBagConstraints_21.gridy = 25;
		gridBagConstraints_21.gridx = 2;
		controlerPanel_.add(component_11, gridBagConstraints_21);

		final Component component_12 = Box.createHorizontalStrut(10);
		final GridBagConstraints gridBagConstraints_22 = new GridBagConstraints();
		gridBagConstraints_22.gridy = 26;
		gridBagConstraints_22.gridx = 0;
		controlerPanel_.add(component_12, gridBagConstraints_22);
		
//		distinguishByColor_ = new JCheckBox();
//		distinguishByColor_.setFocusable(false);
//		final GridBagConstraints gridBagConstraints_23 = new GridBagConstraints();
//		gridBagConstraints_23.anchor = GridBagConstraints.NORTHWEST;
//		gridBagConstraints_23.gridy = 21;
//		gridBagConstraints_23.gridx = 1;
//		controlerPanel_.add(distinguishByColor_, gridBagConstraints_23);
//		distinguishByColor_.setBackground(Color.WHITE);
//		distinguishByColor_.setText("by color");
		
		final JLabel userGuideLabel = new JLabel();
		
		final GridBagConstraints gridBagConstraints_23 = new GridBagConstraints();
		//gridBagConstraints_23.anchor = GridBagConstraints.WEST;
		gridBagConstraints_23.gridy = 26;
		gridBagConstraints_23.gridx = 1;
		controlerPanel_.add(userGuideLabel, gridBagConstraints_23);
		userGuideLabel.setText("<html><u>List of Actions</u></html>");
		userGuideLabel.setToolTipText("<html>Alt+P: To print the content of the Jpanel and export Network as an image"
										+ "<br/>Crtl+D: To delete the seleted node"
										+ "<br/>Crtl+S: To save the changes made"
										+ "<br/>Crtl+E: To delete an edge between 2 selected nodes"
										+ "<br/>Crtl+N: To create a new edge between 2 selected nodes (with no existing edge between them)</html>");
		
	}

	
	// ============================================================================
	// GETTERS AND SETTERS

	public JPanel getVisualControlPanel() { return controlerPanel_; }
	public void setVisualControlPanel(JPanel visualControlPanel_) { controlerPanel_ = visualControlPanel_; }
	
	public JComboBox<String> getInteractionMode() { return interactionMode_; }
	public void setInteractionMode(JComboBox<String> interactionMode) { interactionMode_ = interactionMode; }
	
	public JComboBox<String> getLayoutCombo() { return layoutCombo_; }
	public void setLayoutCombo(JComboBox<String> jcb) { layoutCombo_ = jcb; }
	
	public SearchBox getSearch() { return search_; }
	public void setSearch(SearchBox search) { search_ = search; }

}
	
