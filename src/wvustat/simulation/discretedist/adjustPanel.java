package wvustat.simulation.discretedist;

import java.awt.*;    
import java.awt.event.*; 
import javax.swing.*;

public class adjustPanel extends JPanel implements Runnable,MouseListener{

  private JLabel label1,label2,label3,label4;
  discreteInfo	dInfo;
  discreteDist	distApplet;
  String labelstring;
  int paraIndex;

 
  public adjustPanel(discreteInfo tmpInfo,discreteDist tmpApplet,int tmpIndex) {
	super();						
	
	distApplet = tmpApplet;
  	paraIndex = tmpIndex;
  	dInfo = tmpInfo;

	Class c=getClass();
   	setLayout(new FlowLayout());
  	Icon lefticon=new ImageIcon(c.getResource("left.jpg"));
  	label1=new JLabel(lefticon,SwingConstants.LEFT);
  	label1.addMouseListener(this);
  	Icon righticon=new ImageIcon(c.getResource("right.jpg"));
  	label3=new JLabel(righticon,SwingConstants.RIGHT);
  	label3.addMouseListener(this);
  	
  	add(label1);
  	
  	if (dInfo.isBinomial){
		labelstring=dInfo.getParaSign(paraIndex);
	}
	else {
		
		labelstring="m";
	}
	
	label2=new JLabel(labelstring);
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
	  
		if (e.getSource()==label1) {
			distApplet.dInfo.decPara(paraIndex-1);
			distApplet.distPanel.repaint();
		}
		else if (e.getSource()==label3) {
			distApplet.dInfo.incPara(paraIndex-1);
			distApplet.distPanel.repaint();
		}
		else if (e.getSource()==label2) {
			Thread newThread = new Thread(this);
			try {
				Thread.sleep(100);
				newThread.start();
			} catch (Exception ex) { }
		}

	  
	}catch (NumberFormatException e1) {}
	
  }
  
 
  
  public void run() {
	
		incFrame iFrame = new incFrame(new JFrame(), distApplet, paraIndex-1);
  }
	
 
  	 

}
