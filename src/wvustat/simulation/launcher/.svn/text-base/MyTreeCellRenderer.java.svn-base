/*
 * MyTreeCellRenderer.java
 *
 * Created on June 25, 2001, 10:45 AM
 */

package wvustat.simulation.launcher;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
/**
 *
 * @author  Hengyi Xue
 * @version 
 */
public class MyTreeCellRenderer extends JLabel implements javax.swing.tree.TreeCellRenderer {
    boolean isLeaf;
    
    /** Creates new MyTreeCellRenderer */
    public MyTreeCellRenderer() {
        setFont(new Font("Dialog", Font.PLAIN, 14));
    }

    public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree jTree,java.lang.Object obj,boolean param,boolean param3,boolean param4,int param5,boolean param6) {
        if(param5==0)
            this.setFont(new Font("Arial", Font.BOLD, 16));
        else
            this.setFont(new Font("Dialog", Font.PLAIN, 14));
        if(param){
            setOpaque(true);
            setBackground(Color.gray);
            setForeground(Color.white);
        }
        else{
            setOpaque(false);
            if(param4==true)
                setForeground(Color.blue);
            else
                setForeground(Color.black);
        }
        setText(obj.toString());
        isLeaf=param4;
        return this;
    }
    
    public String getToolTipText(){
        if(isLeaf)
            return "Double click to expand";
        else
            return "Double click to open program";
    }
    
    public Dimension getPreferredSize(){
        Dimension d=super.getPreferredSize();
        if(d!=null)
            d=new Dimension(d.width+4, d.height);
        return d;
    }
}
