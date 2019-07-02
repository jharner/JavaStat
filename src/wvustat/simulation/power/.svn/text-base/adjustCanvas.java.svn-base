package wvustat.simulation.power;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class adjustCanvas extends JPanel implements Runnable,MouseListener{

  publicData		pData;
  Power			cApplet;
  String		symbolString;
  
  String		paraType = "int";
  double		paraValue = 0;
  double		paraUpperBound = 0;
  double		paraLowerBound = 0;
  double		paraInc = 0;
  JLabel		label1,label2,label3,label4,label5;
  Icon			midicon;
 
  public adjustCanvas(publicData tmpData, Power	tmpApplet,String tmpString) {
	
	super();						
	cApplet = tmpApplet;
  	symbolString = tmpString;
  	pData = tmpData;
  	pData.setPara(this);
  	
 	Class c=getClass();
  	setLayout(new FlowLayout());
  	Icon lefticon=new ImageIcon(c.getResource("LEFT.JPG"));
  	label1=new JLabel(lefticon,SwingConstants.LEFT);
  	label1.addMouseListener(this);
  	Icon righticon=new ImageIcon(c.getResource("RIGHT.JPG"));
  	label3=new JLabel(righticon,SwingConstants.RIGHT);
  	label3.addMouseListener(this);
  	label2=new JLabel();
  	
  	if (symbolString=="Delta") {
  		midicon=new ImageIcon(c.getResource("Delta.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	else if (symbolString=="N"){
  		midicon=new ImageIcon(c.getResource("N.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	else if (symbolString=="Sigma"){
  		midicon=new ImageIcon(c.getResource("Sigma.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	else if (symbolString=="Alpha"){
  		midicon=new ImageIcon(c.getResource("Alpha.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	
  	if ( paraType == "int" )
		tmpString = new Integer((int)Math.round(paraValue)).toString();
	else {
		tmpString = new Double(Math.round(paraValue*100.0)/100.0).toString();
		tmpString = tmpString.substring(0, Math.min(tmpString.length(), 4));
	}
  	
  	label2.addMouseListener(this);
  	add(label1);
  	add(label2);
  	add(label3);
  	label4=new JLabel("=");
  	add(label4);
  	label5=new JLabel(tmpString);
  	add(label5);
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
	String tmpString;
	try{
		if (e.getSource()==label1) {
			decPara();
			cApplet.pCanvas.repaint();
			if ( paraType == "int" )
		tmpString = new Integer((int)Math.round(paraValue)).toString();
	else {
		tmpString = new Double(Math.round(paraValue*100.0)/100.0).toString();
		tmpString = tmpString.substring(0, Math.min(tmpString.length(), 4));
	}
	label5.setText(tmpString);
		}
		else if (e.getSource()==label3) {
			incPara();
			cApplet.pCanvas.repaint();
			if ( paraType == "int" )
		tmpString = new Integer((int)Math.round(paraValue)).toString();
	else {
		tmpString = new Double(Math.round(paraValue*100.0)/100.0).toString();
		tmpString = tmpString.substring(0, Math.min(tmpString.length(), 4));
	}
	label5.setText(tmpString);
		}
		else if (e.getSource()==label2) {
			Thread newThread=new Thread(this);
			try{
				newThread.sleep(100);
				newThread.start();
			} catch (Exception ex) {}
		}
	} catch (NumberFormatException el) {}
}
 

  
 
  
  public void decPara () {
  	
  	if ( ( paraValue - paraInc ) > paraLowerBound )
		paraValue -= paraInc;
	
  }	  		 

  public void incPara () {
  	
  	if ( paraUpperBound > (paraValue + paraInc) )
		paraValue += paraInc;

  }
  
  public void setParaValue (double value) {
  	
  	if ( ( value > paraLowerBound ) && ( value < paraUpperBound ) )
		paraValue = value;
		

  }	  		 

  public void setInc (double value) {
  	
  	if ( ( value > paraLowerBound ) && ( value < paraUpperBound ) )
		paraInc = value;

  }	  		 

  public void run() {
		String tmpString;
		incFrame iFrame = new incFrame(new JFrame(), this);
		if ( paraType == "int" )
		tmpString = new Integer((int)Math.round(paraValue)).toString();
		else {
			tmpString = new Double(Math.round(paraValue*100.0)/100.0).toString();
			tmpString = tmpString.substring(0, Math.min(tmpString.length(), 4));
		}
		label5.setText(tmpString);
  }
 
}
