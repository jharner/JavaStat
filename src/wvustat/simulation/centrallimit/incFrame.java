package wvustat.simulation.centrallimit;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class incFrame extends JDialog implements ActionListener{

    JTextField  xValue, xInc;
    JButton buttonOK, buttonCancel;
    JPanel panelUpper, panelLower;
    adjustCanvas host1;
    DFCanvas host2;
    String paraName;

    String titleOfButton, defaultString = new String();
    
    public incFrame( JFrame parent, adjustCanvas aCanvas, String paraName, String title ){

        super( parent, true );
        this.setTitle(title);
        host1 = aCanvas;
        this.paraName=paraName;
        
    
        this.getContentPane().setLayout(new GridLayout(2,0,1,1));
	String tmpString = new String("");
	String defaultIncString = new String();
		
        panelUpper = new JPanel();
        panelLower = new JPanel();

        panelUpper.setLayout(new FlowLayout());
        panelLower.setLayout(new FlowLayout());
       	buttonOK = new JButton("   OK  ");
        buttonCancel = new JButton("Cancel");
		
	panelUpper.add(new JLabel(paraName));
	panelUpper.add(new JLabel(" = "));
	
	if (paraName=="n") {	
		defaultString =	new Integer(host1.getPara()).toString();
		defaultIncString=new Integer(host1.getInc()).toString();
	}
	else {
		defaultString = new Integer(host2.getPara()).toString();
		defaultIncString=new Integer(host2.getInc()).toString();
	}
	
			
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
        this.setSize(330,170);
	this.setLocationRelativeTo(null);
	this.show();
	

    }

public incFrame( JFrame parent, DFCanvas dCanvas, String paraName, String title ){

        super( parent, true );
        this.setTitle(title);
        host2 = dCanvas;
        this.paraName=paraName;
        
    
        this.getContentPane().setLayout(new GridLayout(2,0,1,1));
	String tmpString = new String("");
	String defaultIncString = new String();
		
        panelUpper = new JPanel();
        panelLower = new JPanel();

        panelUpper.setLayout(new FlowLayout());
        panelLower.setLayout(new FlowLayout());
       	buttonOK = new JButton("   OK  ");
        buttonCancel = new JButton("Cancel");
		
	panelUpper.add(new JLabel(paraName));
	panelUpper.add(new JLabel(" = "));
	
	if (paraName=="n") {	
		defaultString =	new Integer(host1.getPara()).toString();
		defaultIncString=new Integer(host1.getInc()).toString();
	}
	else {
		defaultString = new Integer(host2.getPara()).toString();
		defaultIncString=new Integer(host2.getInc()).toString();
	}
	
			
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
        this.setSize(330,170);
	this.setLocationRelativeTo(null);
	this.show();
	

    }


   
    public void actionPerformed(ActionEvent e) { 
		try{
           	if  ( e.getSource() == buttonOK ){
           		try{
          		if(paraName=="n") host1.setPara(Integer.parseInt(xValue.getText()));
          		if(paraName=="n") host1.setInc(Integer.parseInt(xInc.getText()));
          		if(paraName=="df") host2.setPara(Integer.parseInt(xValue.getText()));
          		if(paraName=="df") host2.setInc(Integer.parseInt(xInc.getText()));
          		dispose();
          		}catch(Exception ex) {dispose();}
     		}
     	  	
          	if ( e.getSource() == buttonCancel ) dispose();
      		} catch (Exception ex) { dispose(); }
		
		
		
    }

}
