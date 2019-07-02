package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * ColorLegendPanel is a component that is used to display legends for a plot which use
 * different color for different meaning.
 *
 * @author: Hengyi Xue
 * @version: 1.0, June 19, 2000
 */

public class ColorLegendPanel extends JPanel{
    
    private Color[] colors;
    private String[] labels;

    private int blockWidth=30;
    private int blockHeight=20;
    private int labelWidth=42;
    private int labelHeight=20;

    private int vgap=4;
    private int hgap=8;

    private FontMetrics metrics;

    private Insets insets=new Insets(10, 10, 10, 10);

    private int width, height;

    public ColorLegendPanel(Color[] colors, String[] labels){
	this.colors=colors;
	this.labels=labels;
	setBackground(Color.white);

	metrics=getFontMetrics(getFont());

	width=insets.left+insets.right+3*(blockWidth+labelWidth)+5*hgap;

	if(labels.length%3==0)
	    height=insets.top+insets.bottom+labels.length/3*blockHeight+(labels.length/3-1)*vgap;
	else
	    height=insets.top+insets.bottom+(labels.length/3+1)*blockHeight+(labels.length/3)*vgap;

	setPreferredSize(new Dimension(width, height));
	setBorder(new LineBorder(Color.black));
    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);
	Graphics2D g2=(Graphics2D)g;

	int x0=insets.left;
	int y0=insets.top;

	for(int i=0;i<colors.length;i++){
	    g2.setPaint(colors[i]);

	    g2.fillRect(x0, y0, blockWidth, blockHeight);

	    g2.setPaint(Color.black);
	    
	    x0+=blockWidth+hgap;

	    g2.drawString(labels[i], x0, y0+metrics.getHeight());

	    x0+=metrics.stringWidth(labels[i])+hgap;

	    if(x0+blockWidth+2*hgap+labelWidth>=getSize().width){
		x0=insets.left;
		y0+=blockHeight+vgap;
	    }
	}
    }
}

    
