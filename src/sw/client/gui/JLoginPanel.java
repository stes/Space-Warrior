package sw.client.gui;

import java.awt.Dimension;

import javax.swing.WindowConstants;
import javax.swing.JFrame;

public class JLoginPanel extends javax.swing.JPanel {

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
		
	public JLoginPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
