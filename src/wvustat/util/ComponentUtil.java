/*
 * ComponentUtil.java
 *
 * Created on August 3, 2001, 10:48 AM
 */

package wvustat.util;

import java.awt.*;
import javax.swing.JOptionPane;

/**
 *
 * @author  hxue
 * @version 
 */
public class ComponentUtil extends Object {

    /** Creates new ComponentUtil */
    public ComponentUtil() {
    }
    
    public static Frame getTopComponent(Component comp){
        Component parent=comp.getParent();
        if(parent instanceof Frame){
            return (Frame)parent;
        }
        else{
            return getTopComponent(parent);
        }
    }
    
    public static void setLocationRelativeTo(Component comp, Component baseComp){
        Dimension d1=baseComp.getPreferredSize();
        Dimension d2=comp.getPreferredSize();
        
        Point pt=baseComp.getLocationOnScreen();
        
        comp.setLocation(pt.x+d1.width/2-d2.width/2, pt.y+d1.height/2-d2.height/2);
    }
    
    public static void showErrorMessage(Component parentComponent, String message) {
    	String err = message.replaceAll("\t", "");
        err = err.substring(err.lastIndexOf(':') + 1);
		JOptionPane.showMessageDialog(parentComponent, "Error occured on server: "+err, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showErrorMessage(Component parentComponent, String message, String title) {
    	String err = message.replaceAll("\t", "");
        err = err.substring(err.lastIndexOf(':') + 1);
		JOptionPane.showMessageDialog(parentComponent, title + ": " + err, "Error", JOptionPane.ERROR_MESSAGE);
    }

}