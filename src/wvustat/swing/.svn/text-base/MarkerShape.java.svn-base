package wvustat.swing;

import java.awt.*;
import java.awt.geom.*;

public class MarkerShape implements Shape {

	private Shape path;
	
	public MarkerShape(int x, int y, int type) {
		switch (type) {
			case 1:  path = getDot(x, y, 1); break;
			case 2:  path = getDot(x, y, 2); break;
			case 3:  path = getDot(x, y, 3); break;
			case 4:  path = getRectangle(x, y, 6, 6); break;
			case 5:  path = getRectangle(x, y, 6, 4); break;
			case 6:  path = getRectangle(x, y, 4, 6); break;
			case 7:  path = getPlusSign(x, y); break;
			case 8:  path = getXSign(x, y); break;
			case 9:  path = getYSign(x, y); break;
			case 10: path = getStarSign(x, y); break;
			case 11: path = getDiamond(x, y); break;
			case 12: path = getTriangle(x, y); break;
			default: path = getDot(x, y, 2); 
		}
	}
	
	private static Shape getDot(int x, int y, int radius) {
		Shape circle = new Ellipse2D.Float(x-radius,y-radius,radius*2,radius*2);
		return circle;
	}
	
	private static Shape getRectangle(int x, int y, int w, int h) {
		Shape r = new Rectangle2D.Float(x-w/2, y-h/2, w, h);
		return r;
	}
	
	private static GeneralPath getPlusSign(int x, int y) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x, y-3);
		p.lineTo(x, y+3);
		p.moveTo(x-3, y);
		p.lineTo(x+3, y);
		return p;
	}
	
	private static GeneralPath getXSign(int x, int y) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x-3, y-3);
		p.lineTo(x+3, y+3);
		p.moveTo(x+3, y-3);
		p.lineTo(x-3, y+3);
		return p;
	}
	
	private static GeneralPath getYSign(int x, int y) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x-2, y-3);
		p.lineTo(x, y);
		p.moveTo(x+2, y-3);
		p.lineTo(x, y);
		p.moveTo(x, y+3);
		p.lineTo(x, y);
		return p;
	}
	
	private static GeneralPath getStarSign(int x, int y) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x-2, y-3);
		p.lineTo(x+2, y+3);
		p.moveTo(x+2, y-3);
		p.lineTo(x-2, y+3);
		p.moveTo(x-3, y);
		p.lineTo(x+3, y);
		return p;
	}
	
	private static GeneralPath getDiamond(int x, int y) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x, y-3);
		p.lineTo(x-3, y);
		p.lineTo(x, y+3);
		p.lineTo(x+3, y);
		p.closePath();
		return p;
	}
	
	private static GeneralPath getTriangle(int x, int y) {
		GeneralPath p = new GeneralPath();
		p.moveTo(x, y-3);
		p.lineTo(x-3, y+3);
		p.lineTo(x+3, y+3);
		p.closePath();
		return p;
	}
	
	public boolean contains(double x, double y) {
		return path.contains(x, y);
	}
	
	public boolean contains(double x, double y, double w, double h) {
		return path.contains(x, y, w, h);
	}
	
	public boolean contains(Point2D p) {
		return path.contains(p);
	}
	
	public boolean contains(Rectangle2D r) {
		return path.contains(r);
	}
	
	public Rectangle getBounds() {
		return path.getBounds();
	}
	
	public Rectangle2D getBounds2D() {
		return path.getBounds2D();
	}
	
	public PathIterator getPathIterator(AffineTransform at) {
		return path.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return path.getPathIterator(at, flatness);
	}
	
	public boolean intersects(double x, double y, double w, double h) {
		return path.intersects(x, y, w, h);
	}
	
	public boolean intersects(Rectangle2D r) {
		return path.intersects(r);
	}
}
