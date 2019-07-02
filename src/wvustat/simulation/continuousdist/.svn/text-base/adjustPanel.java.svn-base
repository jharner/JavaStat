package wvustat.simulation.continuousdist;

import java.awt.*;    
import java.awt.event.*; 
import javax.swing.*;

public class adjustPanel extends JPanel implements MouseListener,Runnable{

  String labelstring;
  continuousInfo	cInfo;
  continuousDist	distApplet;
  int			paraIndex;
  JLabel label1,label2,label3,label4;
 
  
 
  public adjustPanel(continuousInfo tmpInfo, continuousDist tmpApplet,int tmpIndex,String str) {
	super();						
	labelstring=str;
	distApplet = tmpApplet;
  	paraIndex = tmpIndex;
  	cInfo = tmpInfo;

        // Important to add c = getClass();!!!!!!!
	Class c = getClass();
   	setLayout(new FlowLayout());
  	Icon lefticon=new ImageIcon(c.getResource("left.jpg"));
  	label1=new JLabel(lefticon,SwingConstants.LEFT);
  	label1.addMouseListener(this);
  	Icon righticon=new ImageIcon(c.getResource("right.jpg"));
  	label3=new JLabel(righticon,SwingConstants.RIGHT);
  	label3.addMouseListener(this);
  	add(label1);
	
	
	label2=new JLabel(labelstring);
	Icon muicon=new ImageIcon(c.getResource("mu.jpg"));
	Icon sdicon=new ImageIcon(c.getResource("sd.jpg"));
	if (labelstring=="mu") label2=new JLabel(muicon);
	if (labelstring=="sd") label2=new JLabel(sdicon);
	//label2.setForeground(Color.black);
	label2.addMouseListener(this);
	add(label2);
	add(label3);
	label4=new JLabel("=");
	//label4.setForeground(Color.black);
	add(label4);
	
   	
  }

  
public void mouseClicked(MouseEvent e){
}

public void mouseReleased(MouseEvent e){

	
}

public void mouseEntered(MouseEvent e){
}

public void mouseExited(MouseEvent e){
}

public void mousePressed(MouseEvent e){
  	

	try {
	         //     $$$$$$$$$$ Here is the problem!!
		if (e.getSource()==label1) {
			distApplet.cInfo.decPara(paraIndex);
			distApplet.cPanel.repaint();
		}
		else if (e.getSource()==label3) {
			distApplet.cInfo.incPara(paraIndex);
			distApplet.cPanel.repaint();
		}
		else if (e.getSource()==label2) {
			Thread newThread = new Thread(this);
			try {
				Thread.sleep(100);
				newThread.start();
			} catch (Exception ex) { } 
		};
	  

	  
	}catch (NumberFormatException e1) {} 
	
  }
  
 
  
  public void run() {
	
		incFrame iFrame = new incFrame(new JFrame(), distApplet, paraIndex);
  }
	
 
  	 

}
