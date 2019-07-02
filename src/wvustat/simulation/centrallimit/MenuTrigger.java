package wvustat.simulation.centrallimit;

import java.awt.*;
import javax.swing.*;

public class MenuTrigger extends JPanel{
	
	private String title;
	private Dimension size = new Dimension(94,28);
	private JLabel label1;
	private boolean triggerPressed=false;

	public MenuTrigger() {
		super();
		Class c = getClass();
   		setLayout(new FlowLayout());
  		Icon optionicon=new ImageIcon(c.getResource("options2.jpg"));
  		label1=new JLabel(optionicon,SwingConstants.CENTER);
  		this.setSize(94,25);
  		this.add(label1);
		}
		
		public void pressTrigger(){
			triggerPressed = true;
		}
		
		public void releaseTrigger(){
			triggerPressed = false;
		}
		
		public Dimension getPreferredSize(){
			return size;
		}
		
		
			
}	
