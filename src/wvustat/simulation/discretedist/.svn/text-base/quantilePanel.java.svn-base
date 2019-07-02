package wvustat.simulation.discretedist;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class quantilePanel extends JPanel implements Runnable,MouseListener{

 
  discreteInfo	dInfo;
  discreteDist	distApplet;
  JLabel label1,label2,label3,label4;
  String str,str2;

  public quantilePanel(discreteInfo tmpInfo,String initQ,discreteDist tmpApplet){

	distApplet = tmpApplet;
  	dInfo = tmpInfo;
	distApplet.dInfo.setQuantile(new Double(initQ).doubleValue());
	
	Class c=getClass();
	setLayout(new FlowLayout());
	Icon lefticon=new ImageIcon(c.getResource("left.jpg"));
  	label1=new JLabel(lefticon,SwingConstants.LEFT);
  	label1.addMouseListener(this);
  	label2=new JLabel("q");
  	label2.addMouseListener(this);
  	Icon righticon=new ImageIcon(c.getResource("right.jpg"));
  	label3=new JLabel(righticon,SwingConstants.RIGHT);
  	label3.addMouseListener(this);
  	String str=new Double (Math.round(distApplet.dInfo.quantile*100)/100.0).toString();
  	if (str.length()<=3) str=str+"0";
  	label4=new JLabel();
  	label4.setText("X ( "+str+" )=");
  	
  	add(label1);
  	add(label2);
  	add(label3);
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
			distApplet.dInfo.decQuantile();
			str=new Double (Math.round(distApplet.dInfo.quantile*100)/100.0).toString();
			if (str.length()<=3) str=str+"0";
			//System.out.println(str);
			label4.setText("X ( "+str+" )=");
			distApplet.distPanel.repaint();
			
		}
		if (e.getSource()==label3) {
			distApplet.dInfo.incQuantile();
			str=new Double (Math.round(distApplet.dInfo.quantile*100)/100.0).toString();
			if (str.length()<=3) str=str+"0";
			//System.out.println(str);
			label4.setText("X ( "+str+" )=");
			distApplet.distPanel.repaint();
			
		}
		if (e.getSource()==label2) {
			Thread newThread = new Thread(this);
			try {
				Thread.sleep(100);
				newThread.start();
			} catch (Exception ex) { }
		}

	  
	}catch (NumberFormatException e1) {}
	
  }
  
  
  public void run() {
	
		incFrame iFrame = new incFrame(new JFrame(), distApplet, 99);
  }
	
  	 

}
