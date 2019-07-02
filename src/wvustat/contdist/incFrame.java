package wvustat.contdist;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class incFrame extends JDialog implements ActionListener{
	   	   JTextField  xValue, xInc;
    	   JButton buttonOK, buttonCancel;
           JPanel panelUpper, panelLower;
           continuousDist cApplet;
           int index;
           
           String titleOfButton, defaultString=new String();
	
	public incFrame(JFrame parent, continuousDist anApplet, int tmpIndex){
		super(parent, "set Value and Increment", true);
		cApplet=anApplet;
		index=tmpIndex;
		
		this.getContentPane().setLayout(new GridLayout(2,0,1,1));
		String tmpString = new String("");
		String defaultIncString = new String("");
		
       		panelUpper = new JPanel();
        	panelLower = new JPanel();

        	buttonOK = new JButton("   OK  ");
        	buttonCancel = new JButton("Cancel");
		
		if ( tmpIndex == 0 ) {
			defaultString =	new Double(Math.round(cApplet.cInfo.paraValue[0]*100)/100.0).toString();
			tmpString = "mu";
			defaultIncString = new Double(Math.round(cApplet.cInfo.paraIncre[0]*100)/100.0).toString();
		}
		if ( tmpIndex == 1 ) {
			defaultString = new Double(Math.round(cApplet.cInfo.paraValue[1]*100)/100.0).toString();
			tmpString = "sd";
			defaultIncString = new Double(Math.round(cApplet.cInfo.paraIncre[1]*100)/100.0).toString();
		}
		if ( tmpIndex == 99 ) {
			defaultString = new Double(Math.round(cApplet.cInfo.quantile*100)/100.0).toString();
			defaultIncString = new Double(Math.round(cApplet.cInfo.quantileInc*100)/100.0).toString();
			tmpString = "quantile";
		}
		
        	panelUpper.setLayout(new FlowLayout());
        	panelLower.setLayout(new FlowLayout());
        	
        	panelUpper.add(new JLabel(tmpString+" = "));
        	xValue		= new JTextField(defaultString, 4);
 			xInc		= new JTextField(defaultIncString, 4);

		panelUpper.add(xValue);
		panelUpper.add(new JLabel("Increment ="));
		panelUpper.add(xInc);
        	this.getContentPane().add(panelUpper);

        	panelLower.add(buttonOK);
        	panelLower.add(buttonCancel);
        	buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
        	this.getContentPane().add(panelLower);
        
        	this.setSize(330, 160);
        	this.show();
        
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

	}

    public void actionPerformed(ActionEvent e){
    	   try {
          	if  ( e.getSource() == buttonOK ){
           
            	int	uppBd = 0;
            	int	lowBd = 0; 
            
			
				if  ( index == 99 ){
 					cApplet.cInfo.setQuantile(Double.valueOf(xValue.getText()).doubleValue());
					cApplet.cInfo.setQuantileInc(Double.valueOf(xInc.getText()).doubleValue());
 				}
 				else {	
 					cApplet.cInfo.setValue(Double.valueOf(xValue.getText()).doubleValue(), index);
					cApplet.cInfo.setInc(Double.valueOf(xInc.getText()).doubleValue(), index);
     				}
     			
				cApplet.cInfo.changeInterval = true;
           			cApplet.cPanel.repaint();
           			cApplet.qPanel.repaint();
           			dispose();
          	}
          	
          	if ( e.getSource() == buttonCancel ) dispose();
		} catch (Exception ex) {dispose();}
		
		

	}
}