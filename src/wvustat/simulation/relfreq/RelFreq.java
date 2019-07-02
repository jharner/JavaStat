package wvustat.simulation.relfreq;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class RelFreq extends JPanel implements ActionListener,Runnable{

  // Components on the applet
  JPanel panelTitle, panelBottom, panelStatus, panelRun;
  JTextField fieldN,fieldP;
  JLabel labelTitle, labelStatus, labelN, labelP, labelPHat;
  JButton buttonRun;
  PlotCanvas pCanvas; //defined in PlotCanvas.java
  JRadioButton fastButton,slowButton;
  ButtonGroup speedgr;
  sliderCanvas sCanvas;
 
  private boolean fastRun; //boolean of fast/slow
  public float usefulResult[] = new float[10001];
  boolean ifInitial = true;
  boolean overFlowFlag = false;
  boolean canRedraw = true;
  float	newP, newXmax;
  int counter;
  int iszero=1;
  int isone=1;
  int isstart=1;
  
  public RelFreq(){
      init();
  }
 
  public void init() { 
  
     GridBagLayout gbl = new GridBagLayout();
     GridBagConstraints gbc = new GridBagConstraints();
     Container content=this;
     
     
     content.setLayout(new BorderLayout(5,5));
     panelBottom = new JPanel();
    
     panelBottom.setLayout(gbl);
     
     panelTitle= new JPanel();
     
     labelTitle = new JLabel("Relative Frequency Definition of Probability");
     
     panelTitle.add(labelTitle);
     content.add("North", panelTitle);


     pCanvas = new PlotCanvas(this);
     pCanvas.setBackground(Color.black);
     content.add("Center", pCanvas);

     fastButton=new JRadioButton("Fast");
        fastButton.setActionCommand("Fast");
        fastButton.addActionListener(this);
        fastButton.setSelected(true);
        fastRun=true;
        slowButton=new JRadioButton("Slow         ");
        slowButton.setActionCommand("Slow");
        slowButton.addActionListener(this);
        speedgr=new ButtonGroup();
        speedgr.add(fastButton);
        speedgr.add(slowButton);
        
     sCanvas = new sliderCanvas(pCanvas, this);
     
 
     labelN = new JLabel("n =");
     labelN.setForeground(Color.black);
     fieldN = new JTextField("100", 4);
     pCanvas.setXmax(100);
     labelP = new JLabel("p =");
     labelP.setForeground(Color.black);
     fieldP = new JTextField("0.5", 3);
     pCanvas.setP((float)0.5);
 
     labelPHat = new JLabel("         p^ = 0.00"+"      ");
     labelPHat.setForeground(Color.black);
     buttonRun = new JButton ("RUN");
     buttonRun.addActionListener(this);

     labelStatus = new JLabel("                                              ");
     
     gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        gbc.anchor = 13;
        addComp(gbl,fastButton,gbc);
        addComp(gbl,slowButton,gbc);
        addComp(gbl, labelN, gbc);
        gbc.anchor = 17;
        addComp(gbl, fieldN, gbc);
        gbc.anchor = 13;
        gbc.insets = new Insets(0, 10, 0, 0);
        addComp(gbl, labelP, gbc);
        gbc.anchor = 17;
        gbc.insets = new Insets(0, 0, 0, 0);
        addComp(gbl, fieldP, gbc);
        gbc.anchor = 12;
        gbc.insets = new Insets(0, 10, 0, 0);
        gbc.anchor = 17;
        gbc.insets = new Insets(0, 0, 0, 0);
        addComp(gbl, labelPHat, gbc);
        gbc.gridwidth = 0;
        addComp(gbl, buttonRun, gbc);
        addComp(gbl,new JLabel("     "),gbc);
        addComp(gbl, labelStatus, gbc);
        
        content.add("South", panelBottom);


   }
   
  public void addComp(GridBagLayout gbl, Component component, GridBagConstraints gbc)
    {
        gbl.setConstraints(component, gbc);
        panelBottom.add(component);
    }

  
    public void actionPerformed(ActionEvent e){	
    	
    	if(e.getSource()==buttonRun){
    		    	
    	  		ifInitial = false;
          		pCanvas.ifRedo = true;
          		canRedraw = false;
          		labelStatus.setText("                           ");
          		newXmax = Float.valueOf(fieldN.getText()).floatValue();
          		newP = Float.valueOf(fieldP.getText()).floatValue();
 
          		if (newXmax > 9999){
            			newXmax = 9999 + 1;
            			overFlowFlag = true;
          		}
          		else overFlowFlag = false;

          		if (newP < 0 || newP > 1) {
            			labelStatus.setText("P must between 0 and 1");
          		}
         		else if (newXmax < 1) {
            			labelStatus.setText("N must be greater than 0");
          		}
          		else if(Math.abs(newXmax-Math.round(newXmax))>0.00001){
            			labelStatus.setText("N must be an integer");
          		}
          		else{
            			
           			Thread thread1=new Thread(this);
    				thread1.start();
       	    		//	pCanvas.repaint();
       	    			
           	 	}
    		
    		
    	}

            if (e.getSource()== fastButton) {
            	fastRun=true;
            
            }
            if (e.getSource()==slowButton) {
            	fastRun=false;
            	
            }
 
 
    }

    public void run(){
    	
    	pCanvas.setXmax(newXmax);
        pCanvas.setP(newP);
        pCanvas.repaint();
    	
  //  	for (int k=1;k<=Math.round(newXmax);k++){
  //  		System.out.println(k+pCanvas.accumuResult[k]);
  //  	}
    	
    	if(fastRun){
    	  
    	  int i=0;
    	  
    	  while (i<Math.round(newXmax)){
    	  	i++;
    	  counter=i;
    	  pCanvas.repaint();
    	  SwingUtilities.invokeLater(new Runnable(){
    	  	public void run(){
    	  	
			labelPHat.setText("         p^ = 0." + (new Integer(Math.round(pCanvas.accumuResult[counter] * 100F))).toString()+"      ");
       	    		
          	}
          	
        }); //end of first run
        try {
        	Thread.currentThread().sleep(10);
        }
        catch(InterruptedException e) {
        }
       
       	} //end of while
       	System.out.println(pCanvas.accumuResult[Math.round(newXmax)]);
        }// end of if fastRun
	else {
		int i=0;
		while(i<Math.round(newXmax)){
			i++;
			counter=i;
			pCanvas.repaint();
		 SwingUtilities.invokeLater(new Runnable(){
    	  	public void run(){
    	  	
			labelPHat.setText("         p^ = 0." + (new Integer(Math.round(pCanvas.accumuResult[counter] * 100F))).toString()+"      ");
       	    		
          	}
          	
        }); //end of first run
        try {
        	Thread.currentThread().sleep(100);
        }
        catch(InterruptedException e) {
        }
       
       	} //end of while
   }//end of else	
       
    }// end of run
    		
    	
    	


}

/* End of class */

