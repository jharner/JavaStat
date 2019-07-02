/*
 * ComponentUtil.java
 *
 * Created on August 3, 2001, 10:48 AM
 */

package wvustat.plotUtil;

import java.awt.*;
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
    

}