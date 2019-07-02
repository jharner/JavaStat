package wvustat.simulation.centrallimit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class selectImageDialog extends JDialog implements ActionListener,MouseListener{

    	JPanel	panelUpper, panelLower;
    	publicData	pData;
	CtrLimit	hostApplet;
	JButton		buttonOK, buttonCancel;
	Class c=getClass();
	Icon 		disticon[]={new ImageIcon(c.getResource("normal1.jpg")), new ImageIcon(c.getResource("chisq1.jpg")), new ImageIcon(c.getResource("uniform1.jpg")), new ImageIcon(c.getResource("bowtie1.jpg")), new ImageIcon(c.getResource("wedge_L1.jpg")), new ImageIcon(c.getResource("wedge_R1.jpg")), new ImageIcon(c.getResource("triangle1.jpg")) };;
	JLabel          distlabel[]={new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel(),new JLabel()};
	int		currentSelection=0;
	
    public selectImageDialog( JFrame parent, CtrLimit anApplet, publicData tmpData){

        super( parent, false );
        this.setTitle("Select Parent Distribution");
      	this.getContentPane().setLayout(new FlowLayout());
      	
      	
        pData = tmpData;
        hostApplet = anApplet;
		
    	panelUpper = new JPanel(); panelLower = new JPanel();
	
	
	for (int i=0; i<7;i++){
		distlabel[i].setIcon(disticon[i]);
		distlabel[i].addMouseListener(this);
		if(i>0){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		else{
			distlabel[i].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		}
		panelUpper.add(distlabel[i]);
	};	
       	buttonOK = new JButton("   OK  ");
        buttonCancel = new JButton("Cancel");
        panelLower.add(buttonOK);
        panelLower.add(buttonCancel);

	buttonOK.addActionListener(this);
	buttonCancel.addActionListener(this);
	enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
        this.getContentPane().add(panelUpper);
        this.getContentPane().add(panelLower);
   	this.setLocationRelativeTo(null);	
   	this.setSize(430, 200);
    	this.show();
    }
    
    public void actionPerformed(ActionEvent e) { 

	  try {
         	
         	if  ( e.getSource() == buttonOK ){
         		hostApplet.reset(currentSelection);
         		hostApplet.pData.reset(currentSelection);
         		hostApplet.distIndex=currentSelection;
          		dispose();
			}
         	if ( e.getSource() == buttonCancel ) dispose();
			dispose();
       } catch (Exception ex) {dispose();}
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
	if (e.getSource()==distlabel[0]){
		distlabel[0].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		for (int i=1;i<7;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=0;
	}
	if (e.getSource()==distlabel[1]){
		distlabel[1].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		distlabel[0].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		for (int i=2;i<7;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=1;
	}
	if (e.getSource()==distlabel[2]){
		distlabel[2].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		for (int i=0;i<2;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		for (int i=3;i<7;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=2;
	}
	if (e.getSource()==distlabel[3]){
		distlabel[3].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		for (int i=0;i<3;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		for (int i=4;i<7;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=3;
	}
	if (e.getSource()==distlabel[4]){
		distlabel[4].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		for (int i=0;i<4;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		for (int i=5;i<7;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=4;
	}
	if (e.getSource()==distlabel[5]){
		distlabel[5].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		for (int i=0;i<5;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		for (int i=6;i<7;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=5;
	}
	if (e.getSource()==distlabel[6]){
		distlabel[6].setBorder(new javax.swing.border.LineBorder(new Color(153,153,255),3));
		for (int i=0;i<6;i++){
			distlabel[i].setBorder(new javax.swing.border.LineBorder(Color.lightGray,3));
		}
		currentSelection=6;
	}
}
}		
