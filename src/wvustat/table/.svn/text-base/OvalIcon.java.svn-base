package wvustat.table;

import wvustat.swing.MarkerShape;
import java.awt.*;

import javax.swing.*;

/**
*	OvalIcon implements a simple oval shaped icon. It is used for the table cell renderer for the 'Color' variable.
*
*	@author: Hengyi Xue
*	@version: 1.0, Dec. 7, 1999
*
*/

public class OvalIcon implements Icon{
	
	private Color color;
	private int marker = 0;
	private int w=10,h=10;
	
	public OvalIcon(MyColor color){
		this.color=color.getColor();
		this.marker = color.getMarker().intValue();
	}
	
	public OvalIcon(Color color) {
		this.color = color;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2=(Graphics2D)g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
		
		g2.setColor(color);
		//g2.fillOval(x,y,w-1,h-1);
		Shape s = new MarkerShape(x+w/2, y+h/2, marker);
		g2.draw(s);
		g2.fill(s);
	}
	
	public int getIconWidth(){
		return w;
	}
	
	public int getIconHeight(){
		return h;
	}
}
