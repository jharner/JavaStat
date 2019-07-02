package wvustat.simulation.continuousdist;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class dialogFrame extends JDialog implements ActionListener{

    JTextField  upperBound, lowerBound, xValue;
    JButton buttonOK, buttonCancel;
    JPanel panelUpper, panelLower;
    continuousDist bApplet;

    double accumuProb = 0;
    public int indexError = 0;
    
    int		formulaIndex;
    continuousPanel  cPanel;
    int 	index;			

    public dialogFrame( Frame parent, continuousDist anApplet, int tmpIndex ){

        super(parent, "Set Lower and Upper Limit", true);
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

        if  ( 	( index == 1 ) || ( index == 3 )  ){
            panelUpper.add(new Label("Lower Limit ="));
            panelUpper.add(lowerBound);
        }
        if  ( 	( index == 2 ) || ( index == 3 )  ){
            panelUpper.add(new Label("Upper Limit ="));
            panelUpper.add(upperBound);
        }
        if ( index == 4 ) {
            panelUpper.add(new Label("x ="));
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
    }


    public void actionPerformed(ActionEvent e) { 

	  boolean emptyInputFlag = false;
	  
	  try {
          if  ( e.getSource() == buttonOK ){
           
            double	uppBd = -2E8;
            double	lowBd = -2E8; 
            
			if  ( 	( index == 1 ) || ( index == 3 )  ){
            	if ( new String(lowerBound.getText()).length() == 0 )
            		emptyInputFlag = true;
 				lowBd =	Double.valueOf(lowerBound.getText()).doubleValue();
     		}
        	if  ( 	( index == 2 ) || ( index == 3 )  ){
            	if ( new String(upperBound.getText()).length() == 0 )
            		emptyInputFlag = true;
 				uppBd =	Double.valueOf(upperBound.getText()).doubleValue();
			}
        	if  ( index == 4 ){
            	if ( new String(xValue.getText()).length() == 0 )
            		emptyInputFlag = true;
 				uppBd =	Double.valueOf(xValue.getText()).doubleValue();
 				lowBd = uppBd;
			}
			if (!emptyInputFlag){
				bApplet.cInfo.changeInterval = true;
           		bApplet.cInfo.setInterval(lowBd, uppBd, index);
           		bApplet.cPanel.repaint();
           	}
           	dispose();
           	bApplet.ifMenu=true;
          }
          if  ( e.getSource() == buttonCancel ){
            dispose();
          }
      } catch (Exception ex) {dispose();}
      
	
    }

}
