package wvustat.simulation.confint;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class adjustCanvas extends JPanel implements Runnable,MouseListener{

 
  publicData	pData;
  confint		cApplet;
  String		symbolString;
  
  String		paraType = "int";
  double		paraValue = 0;
  double		paraUpperBound = 0;
  double		paraLowerBound = 0;
  double		paraInc = 0;
  JLabel		label1,label2,label3,label4,label5;
  Icon			midicon;
  
  public adjustCanvas(publicData tmpData, confint tmpApplet, String tmpString) {
	
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
  	
  	if (symbolString=="mu") {
  		midicon=new ImageIcon(c.getResource("mu.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	else if (symbolString=="n"){
  		midicon=new ImageIcon(c.getResource("n.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	else if (symbolString=="sigma"){
  		midicon=new ImageIcon(c.getResource("sigma.jpg"));
  		label2=new JLabel(midicon,SwingConstants.CENTER);
  	}
  	else if (symbolString=="alpha"){
  		midicon=new ImageIcon(c.getResource("alpha.jpg"));
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
			cApplet.getSample();
			cApplet.cCanvas.repaint();
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
			cApplet.getSample();
			cApplet.cCanvas.repaint();
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
	if (( symbolString == "alpha") || ( symbolString == "n") )
		pData.setNormalInterval(cApplet.aCanvasAlpha.paraValue);
	
  }	  		 

  public void incPara () {
  	
  	if ( paraUpperBound > (paraValue + paraInc) )
		paraValue += paraInc;
	if ( ( symbolString == "alpha") || ( symbolString == "n") )
		pData.setNormalInterval(cApplet.aCanvasAlpha.paraValue);

  }
  
  public void setParaValue (double value) {
  	
  	if ( ( value > paraLowerBound ) && ( value < paraUpperBound ) )
		paraValue = value;
		
	if (( symbolString == "alpha") || ( symbolString == "n") )
		pData.setNormalInterval(cApplet.aCanvasAlpha.paraValue);

  }	  		 

  public void setInc (double value) {
  	
  	if ( ( value > paraLowerBound ) && ( value < paraUpperBound ) )
		paraInc = value;

  }	  		 

  public void run(){
  	String tmpString;
	incFrame iFrame=new incFrame(new JFrame(),this);
	if ( paraType == "int" )
		tmpString = new Integer((int)Math.round(paraValue)).toString();
	else {
		tmpString = new Double(Math.round(paraValue*100.0)/100.0).toString();
		tmpString = tmpString.substring(0, Math.min(tmpString.length(), 4));
	}
	label5.setText(tmpString);
}

}
