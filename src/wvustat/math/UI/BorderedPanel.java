/*
 * BorderedPanel.java
 *
 * Created on November 27, 2001, 11:06 AM
 */

package wvustat.math.UI;

import java.awt.*;
/**
 *
 * @author  Hengyi Xue
 * @version 
 */
public class BorderedPanel extends Panel {

    /** Creates new BorderedPanel */
    public BorderedPanel() {
    }
    
    public void paint(Graphics g){
        super.paint(g);
        
        g.setColor(Color.gray);
        g.drawRect(0,0,this.getSize().width-1, this.getSize().height-1);
    }

}