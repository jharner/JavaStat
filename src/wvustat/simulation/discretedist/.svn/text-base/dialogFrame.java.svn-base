package wvustat.simulation.discretedist;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class dialogFrame extends JDialog implements ActionListener{

    JTextField  upperBound, lowerBound, xValue;
    JButton buttonOK, buttonCancel;
    JPanel panelUpper, panelLower;
    discreteDist bApplet;

    double accumuProb = 0;
    public int indexError = 0;
    
    int		formulaIndex;
    discretePanel distPanel;
    int 	index;			

    public dialogFrame( JFrame parent, discreteDist anApplet, int tmpIndex ){

        super( parent, "Set Lower and Upper Limit", true);
        bApplet = anApplet;
        this.getContentPane().setLayout(new GridLayout(2,0,1,1));
		index = tmpIndex;
		
		
        panelUpper = new JPanel();
        panelLower = new JPanel();

        buttonOK = new JButton("   OK  ");
        buttonCancel = new JButton("Cancel");
      
		
		upperBound	= new JTextField(4);
		lowerBound	= new JTextField(4);
		xValue		= new JTextField(4);


        panelUpper.setLayout(new FlowLayout());
        panelLower.setLayout(new FlowLayout());

        if  ( 	( index == 1 ) || ( index == 3 ) ){
            panelUpper.add(new JLabel("Lower Limit ="));
            panelUpper.add(lowerBound);
        }
        if  ( 	( index == 2 ) || ( index == 3 ) ){
            panelUpper.add(new JLabel("Upper Limit ="));
            panelUpper.add(upperBound);
        }
        if ( index == 4 ) {
            panelUpper.add(new JLabel("x ="));
            panelUpper.add(xValue);
        }
        
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

    public void actionPerformed(ActionEvent e) { 
	
		boolean emptyInputFlag = false;
		
		try{	
			if ( e.getSource() == buttonOK ) {

            	int	uppBd = 0;
            	int	lowBd = 0; 
				if  ( 	( index == 1 ) || ( index == 3 ) ){
            		if ( new String(lowerBound.getText()).length() == 0 )
            			emptyInputFlag = true;
 					lowBd =	Integer.valueOf(lowerBound.getText()).intValue();
     			}
        		if  ( 	( index == 2 ) || ( index == 3 ) ){
	            	if ( new String(upperBound.getText()).length() == 0 )
     	        		emptyInputFlag = true;
 					uppBd =	Integer.valueOf(upperBound.getText()).intValue();
				}
        		if  ( index == 4 ){
    	        	if ( new String(xValue.getText()).length() == 0 )
        	    		emptyInputFlag = true;
					uppBd =	Integer.valueOf(xValue.getText()).intValue();
 					lowBd = uppBd;
				}
				if ( !emptyInputFlag ) {
					bApplet.dInfo.changeInterval = true;
           			bApplet.dInfo.setInterval(lowBd, uppBd, index);
           			bApplet.distPanel.repaint();
           		}
           		dispose();
			}
            
			if ( e.getSource() == buttonCancel ) dispose();
			
		} catch ( Exception ex ) {dispose();}
          	
    }

}
