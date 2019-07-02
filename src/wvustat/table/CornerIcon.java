package wvustat.table;

import java.awt.*;
import javax.swing.*;

public class CornerIcon implements Icon {
	private int w=12, h=12;
	private boolean rowToggle, colToggle;
	
	public CornerIcon() {
		rowToggle = false;
		colToggle = false;
	}
	
	public CornerIcon(boolean rowToggle, boolean colToggle) {
		this.rowToggle = rowToggle;
		this.colToggle = colToggle;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.black);
	
		Polygon upper = new Polygon();
		upper.addPoint(x, y);
		upper.addPoint(x+w, y);
		upper.addPoint(x+w, y+h);
		
		if (colToggle)
			g2.fill(upper);
		else
			g2.draw(upper);
		
		Polygon lower = new Polygon();
		lower.addPoint(x, y);
		lower.addPoint(x, y+h);
		lower.addPoint(x+w, y+h);
		
		if (rowToggle)
			g2.fill(lower);
		else
			g2.draw(lower);
		
		/*
		g2.drawLine(x, y, x, y+h);
		g2.drawLine(x, y, x+w, y);
		g2.drawLine(x+w, y, x+w, y+h);
		g2.drawLine(x, y, x+w, y+h);
		g2.drawLine(x, y+h, x+w, y+h);
		*/
	}
	
	public int getIconWidth(){
		return w + 2;
	}
	
	public int getIconHeight(){
		return h;
	}
	
	public static Polygon getUpperArea(){
		Polygon poly = new Polygon();
		poly.addPoint(1, 0);
		poly.addPoint(12, 0);
		poly.addPoint(12, 11);
		return poly;
	}
	
	public static Polygon getLowerArea(){
		Polygon poly = new Polygon();
		poly.addPoint(0, 1);
		poly.addPoint(0, 12);
		poly.addPoint(11, 12);
		return poly;
	}
	
}
