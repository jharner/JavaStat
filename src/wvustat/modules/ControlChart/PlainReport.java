/*
 * PlainReport.java
 *
 * Created on September 18, 2000, 9:56 AM
 */

package wvustat.modules.ControlChart;

import java.awt.*;
import javax.swing.*;

/**
 * Plain report is used to display plain text.
 *
 * @author  Hengyi Xue
 * @version 
 */
public class PlainReport extends JPanel {
    
    protected int height;
    protected FontMetrics metrics;
    protected Insets insets=new Insets(20,20,20,20);
    protected String[] text;
    /** Creates new PlainReport */
    public PlainReport(int lineOfText) {
        setBackground(Color.white);
        metrics=getFontMetrics(getFont());
        height=insets.top+insets.bottom+lineOfText*metrics.getHeight();
        setPreferredSize(new Dimension(150,height));
        setMinimumSize(new Dimension(150,height));
    }
    
    public void setText(String[] text){
        this.text=text;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if(text==null)
            return;
        
        Graphics2D g2=(Graphics2D)g;
        
        int x0=insets.left;
        int y0=insets.top+metrics.getMaxAscent();
        for(int i=0;i<text.length;i++){
            g2.drawString(text[i],x0,y0);
            y0+=metrics.getHeight();
        }
    }

}
