package wvustat.simulation.power;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuTrigger extends JPanel {
	
	private String title;
	private Dimension size = new Dimension(105,28);
	private JLabel label1;
	private boolean triggerPressed=false;
	
	
	public MenuTrigger() {
		super();
		Class c=getClass();
		setLayout(new FlowLayout());
		Icon menuicon=new ImageIcon(c.getResource("powertype.jpg"));
		label1=new JLabel(menuicon,SwingConstants.CENTER);
		this.setSize(105,28);
		this.add(label1);
	}
	
	public void pressTrigger() {
		triggerPressed = true;
		
	}
	
	public void releaseTrigger() {
		triggerPressed = false;
		
	}
	
	public Dimension getPreferredSize() {
		return size;
	}
}	