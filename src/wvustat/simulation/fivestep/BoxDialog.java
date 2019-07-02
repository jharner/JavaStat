/*
 * BoxPanel.java
 *
 * Created on March 18, 2002, 10:45 AM
 */

package wvustat.simulation.fivestep;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author  hxue
 * @version 
 */
public class BoxDialog extends JDialog implements java.awt.event.ActionListener{
    private double[][] boxModel;
    private int columns, rows=1;
    
    /** Creates new BoxPanel */
    public BoxDialog(Frame parent,double[][] boxModel) {
        super(parent, "Box", true);        
        this.boxModel=boxModel;
        this.setBackground(Color.white);
        initComponent();
        
        pack();
        if(parent.isShowing()){
            Point pt=parent.getLocationOnScreen();
            Dimension d=this.getPreferredSize();
            Dimension d2=parent.getPreferredSize();
            this.setLocation(pt.x+d2.width/2-d.width/2, pt.y+d2.height/2-d.height/2);
        }
    }
      
    
    private void initComponent(){
        columns=boxModel[0].length;
        
        for(int i=0;i<boxModel[0].length;i++){
            if(boxModel[0][i]>1)
                    columns++;
        }
        
        JPanel panel1=new JPanel();
        panel1.setLayout(new java.awt.GridLayout(rows, columns,8,8));
        Font font=new Font("Dialog", Font.BOLD, 14);
        
        for(int i=0;i<boxModel[0].length;i++){
            if(boxModel[0][i]>1){
                JLabel count=new BoxLabel(String.valueOf((int)boxModel[0][i]), SwingConstants.RIGHT);
                
                panel1.add(count);
            }
            
            //JLabel box=new BoxLabel(String.valueOf(boxModel[1][i]), SwingConstants.CENTER);
            JLabel box=new JLabel(new NumberIcon(boxModel[1][i]));
            box.setFont(font);
            box.setForeground(Color.blue);
            //box.setBorder(new javax.swing.border.LineBorder(new Color(0x23,0x61, 0xd1), 2));
            panel1.add(box);
        }
        
        panel1.setBorder(new javax.swing.border.EtchedBorder());
        
        Container content=this.getContentPane();
        content.setLayout(new GridBagLayout());
        content.add(panel1, new GridBagConstraints(0,0,0,-1,1.0,0.2,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4,4,4,4),2,2));
        
        JButton btn1=new JButton("Close");
        btn1.addActionListener(this);
        content.add(btn1, new GridBagConstraints(0,1,0,0,0,0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
    }
        
    public void actionPerformed(java.awt.event.ActionEvent p1) {
        this.dispose();
    }    

}