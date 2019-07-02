package wvustat.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class TourBox_TEST extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private TourBox box;
	
	public TourBox_TEST() {
		box = new TourBox("Variable");

		setSize(new Dimension(300, 300)); // call setBounds()
		setPreferredSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(300, 300));		
		
		setLayout(null);
		add(box);
	}
	
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		
		Dimension size = box.getPreferredSize();
		box.setBounds(0, 0, size.width, size.height);
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
	}
	
	public static void main(String[] args) {
		TourBox_TEST s = new TourBox_TEST();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(s);
		frame.pack();
		frame.setVisible(true);
	}
}
