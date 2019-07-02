package wvustat.simulation.centrallimit;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class DFCanvas extends JPanel implements Runnable,MouseListener {

  CtrLimit hostApplet;
  int	initDF = 4;
  int	lowerBd,upperBd,initValue,incStep;
  int 	currentValue=initDF;
  public JLabel label1,label2,label3,label4,label5;
  boolean	showDF = false;
  
  public DFCanvas(CtrLimit tmpApplet,int lowerBd, int upperBd, int initValue, int incStep) {
  			
  	super ();
  	hostApplet = tmpApplet;
  	this.lowerBd=lowerBd;
  	this.upperBd=upperBd;
  	this.initValue=initValue;
  	this.incStep=incStep;
  	
  	
  	Class c=getClass();
  	setLayout(new FlowLayout());
  	Icon lefticon=new ImageIcon(c.getResource("LEFT.JPG"));
  	label1=new JLabel(lefticon,SwingConstants.LEFT);
  	label1.addMouseListener(this);
  	Icon righticon=new ImageIcon(c.getResource("RIGHT.JPG"));
  	label3=new JLabel(righticon,SwingConstants.RIGHT);
  	label3.addMouseListener(this);
  	add(label1);
  	
  	label2=new JLabel("df");
  	label2.addMouseListener(this);
  	add(label2);
  	add(label3);
  	label4=new JLabel("=");
  	add(label4);
  	label5=new JLabel(new Integer(initValue).toString());
  	add(label5);
	
  }
  
  public boolean needPaint() {
  
  	return(showDF);
  }
  
  public void updateView(){
  	hostApplet.cCanvas.setDF(getPara());
  	hostApplet.clear();
  	hostApplet.cCanvas.repaint();
  	hostApplet.getHistSize=0;
}

  public void reset(){
  	setPara(initValue);
  	label5.setText(new Integer(currentValue).toString());
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
	try{
		if (e.getSource()==label1) {
			decPara();
			updateView();
			label5.setText(new Integer(currentValue).toString());
			hostApplet.percentLabel.setText("      0%");
		}
		else if (e.getSource()==label3) {
			incPara();
			updateView();
			label5.setText(new Integer(currentValue).toString());
			hostApplet.percentLabel.setText("      0%");
		}
		else if (e.getSource()==label2) {
			Thread newThread = new Thread(this);
			try{
				Thread.sleep(100);
				newThread.start();
			} catch (Exception ex) {}
		}
	} catch (NumberFormatException el){}
}

 private boolean incPara() {
  
  	if ( ( currentValue + incStep ) <= upperBd ) {
  		currentValue += incStep;
  		return(true);
  	}
  	else {
  		currentValue = upperBd;
  		return(true);
  	}
  }

  private boolean decPara() {
  
  	if ( ( currentValue - incStep ) >= lowerBd ) {
  		currentValue = currentValue - incStep;
  		return(true);
  	}
  	else {
  		currentValue = lowerBd;
  		return(true);
  	}
  }

  public boolean setPara(int value) {
  
  	if ( ( value <= upperBd ) && ( value >= lowerBd ) ) {
  		currentValue = value;
  		repaint();
  		return(true);
  	}
  	else return(false);
  }
  
  public boolean setInc(int value) {
  
  	if ( ( value <= upperBd ) && ( value >= lowerBd ) ) {
  		incStep = value;
  		return(true);
  	}
  	else return(false);
  }
  
  public int getPara() {
  
	return(currentValue);
  }
  
  public int getInc(){
  	return(incStep);
}
  
  public void run() {
  	incFrame iFrame = new incFrame(new JFrame(),this,"df","Modify Value and Increment for df");
  	label5.setText(new Integer(currentValue).toString());
  	hostApplet.percentLabel.setText("      0%");
  	updateView();
}

}
