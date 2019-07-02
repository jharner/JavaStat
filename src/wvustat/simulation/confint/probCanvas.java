package wvustat.simulation.confint;

import java.awt.*;
import javax.swing.*;

public class probCanvas extends JPanel {

    JLabel		probLabel,imageLabel;
    confint		cApplet;
    int			type=0;

    public probCanvas(confint tmpApplet) {
		
		cApplet = tmpApplet;
		
    
  
       		FontMetrics	fm;
       		String		probString;
       		
       		setLayout(new FlowLayout());

    
        	probString = new Integer(cApplet.pData.probContain).toString() + "% contain ";
        	probLabel=new JLabel(probString);
        	probLabel.setForeground(Color.blue);
        	Class c=getClass();
        	Icon imageicon=new ImageIcon(c.getResource("mu.jpg"));
        	imageLabel=new JLabel(imageicon,SwingConstants.LEFT);
        	add(probLabel);
        	add(imageLabel);
   	
    		
    }
    
    public void setType (int t) {
    	
    	type = t;
    	
    }
}