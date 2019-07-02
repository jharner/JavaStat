package wvustat.swing;

import java.awt.*;

import javax.swing.*;

import wvustat.modules.*;
import wvustat.plot.*;

public class XAxisRenderer_TEST extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Insets insets = new Insets(60, 50, 40, 40);
	private BaseAxisModel model;
	
	public XAxisRenderer_TEST() {
		setSize(new Dimension(300, 300)); 
		setPreferredSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(300, 300));	
		
		model = new ManualAxisModel();
		model.setStartValue(0);
		model.setEndValue(80);
		model.setInterval(20);
		model.setNumOfMinorTicks(3);
	}
	
	protected void paintComponent(Graphics g) {
		
		g.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		XAxisRenderer comp = new XAxisRenderer(model, getSize(), insets);
		//comp.setGap(20);
		comp.setLeftGap(20);
		comp.setRightGap(20);
		comp.setLabel("Absorbtion");
		SwingUtilities.paintComponent(g, comp, this, insets.left - comp.getLeftGap(), 
				getSize().height - insets.bottom, comp.getPreferredSize().width, comp.getPreferredSize().height);
		
		YAxisRenderer comp2 = new YAxisRenderer(model, getSize(), insets);
		comp2.setTopGap(20);
		comp2.setRightGap(20);
		SwingUtilities.paintComponent(g, comp2, this, 0,
				insets.top - comp2.getTopGap(), comp2.getPreferredSize().width, comp2.getPreferredSize().height);
		
		g.drawString("Freq", comp2.getPreferredSize().width, insets.top - comp2.getTopGap());
	}
	
	public static void main(String[] args) {
		XAxisRenderer_TEST s = new XAxisRenderer_TEST();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(s);
		frame.pack();
		frame.setVisible(true);
	}
	
}
