package wvustat.simulation.power;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class incFrame extends JDialog implements ActionListener{

    JTextField  xValue, xInc;
    JButton buttonOK, buttonCancel;
    JPanel panelUpper, panelLower;
    int		index;
    adjustCanvas	aCanvas;
    String defaultString, defaultIncString;
    JLabel	valueLabel;
    Icon	valueicon;
    
    public incFrame( JFrame parent, adjustCanvas tmpCanvas ){
    
	super(parent, true);
	this.setTitle("Dialog: " + tmpCanvas.symbolString + ", inc"); 
       	aCanvas = tmpCanvas;
        
        this.getContentPane().setLayout(new GridLayout(2,0,1,1));
	String tmpString = new String("");
	String defaultIncString = new String("");
		
        panelUpper = new JPanel();
        panelLower = new JPanel();

        panelUpper.setLayout(new FlowLayout());
        panelLower.setLayout(new FlowLayout());
       	buttonOK = new JButton("   OK  ");
        buttonCancel = new JButton("Cancel");
        
         Class c=getClass();
        if (aCanvas.symbolString=="Delta") {
  		valueicon=new ImageIcon(c.getResource("Delta.jpg"));
  		valueLabel=new JLabel(valueicon,SwingConstants.CENTER);
  	}
  	else if (aCanvas.symbolString=="N"){
  		valueicon=new ImageIcon(c.getResource("N.jpg"));
  		valueLabel=new JLabel(valueicon,SwingConstants.CENTER);
  	}
  	else if (aCanvas.symbolString=="Sigma"){
  		valueicon=new ImageIcon(c.getResource("Sigma.jpg"));
  		valueLabel=new JLabel(valueicon,SwingConstants.CENTER);
  	}
  	else if (aCanvas.symbolString=="Alpha"){
  		valueicon=new ImageIcon(c.getResource("Alpha.jpg"));
  		valueLabel=new JLabel(valueicon,SwingConstants.CENTER);
  	}
		
		if (aCanvas.paraType == "double"){
			defaultString =	new Double(Math.round((aCanvas.paraValue)*100)
							/100.0).toString();
			defaultIncString = new Double(Math.round((aCanvas.paraInc)*100)
							/100.0).toString();
		}
		else{
			defaultString =	new Integer((int)(aCanvas.paraValue+0.01)).toString();
			defaultIncString = new Integer((int)(aCanvas.paraInc+0.01)).toString();
		}
		
			
		xValue		= new JTextField(defaultString, 4);
 		xInc		= new JTextField(defaultIncString, 4);
		
		panelUpper.add(valueLabel);
		panelUpper.add(new JLabel(" ="));
		panelUpper.add(xValue);
		panelUpper.add(new JLabel("Increment ="));
		panelUpper.add(xInc);
        	this.getContentPane().add(panelUpper);

		buttonOK.addActionListener(this);
		buttonCancel.addActionListener(this);
		panelLower.add(buttonOK);
        	panelLower.add(buttonCancel);
        	this.getContentPane().add(panelLower);

		this.setSize(330,170);
		this.setLocationRelativeTo(null);
		this.show();
    }


    public void actionPerformed(ActionEvent e) { 

		if  ( e.getSource() == buttonOK ){
           
            int	uppBd = 0;
            int	lowBd = 0; 
            
			
		aCanvas.setParaValue(Double.valueOf(xValue.getText()).doubleValue());
		aCanvas.setInc(Double.valueOf(xInc.getText()).doubleValue());
           	aCanvas.cApplet.pCanvas.repaint();
           	dispose();
		}
		if  ( e.getSource() == buttonCancel ) dispose();
	}

}
