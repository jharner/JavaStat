/*
 * Launcher.java
 * 
 * Created on May 26, 2005
 *
 */
package wvustat.simulation.fivestep;

import java.awt.*;
import javax.swing.*;


/**
 * @author djluo
 *
 * Launch the fivestep module of myJavaStat as a stand alone application
 *
 */
public class Launcher {
	private JFrame mainWindow;
	private JPanel modulePane;
	
	public Launcher(){
		mainWindow = new JFrame("Five Step");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(900, 680);
		
		modulePane = new JPanel(new BorderLayout());
		modulePane.setMinimumSize(new Dimension(200, 200));
		
		mainWindow.getContentPane().add(modulePane, BorderLayout.CENTER);
		
		mainWindow.setVisible(true);
		
		FiveStep panel = new FiveStep();
		modulePane.add(panel, BorderLayout.CENTER);

	
		panel.invalidate();
		
	}

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
	}
}
