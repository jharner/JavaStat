package wvustat.swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;
import javax.swing.event.*;

public class TourBox extends JComponent implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	public final static int A=0;
	public final static int X=1;
	public final static int Y=2;
	public final static int O=3;
	
	
	private final int width = 60, owidth = 20, gap = 4;;
	private int height;

	private String name;
	private int type = 0;
	private boolean enabled = true;
	private boolean entered = false;
	private int position;
	private double axisX, axisY;
	
	private Ellipse2D outCircle, inCircle;
	private GeneralPath upperTri, rightTri, bottomTri;
	
	private EventListenerList eList = new EventListenerList();
    private ChangeEvent event;
	
	public TourBox(String name) {
		this.name = name;
		init();
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int t) {
		this.type = t;
		repaint();
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int p) {
		this.position = p;
	}
	
	public void setEnabled(boolean b) {
		this.enabled = b;
	}
	
	public void setAxisIndicator(double x, double y) {
		this.axisX = x;
		this.axisY = y;
		repaint();
	}
	
	protected void init() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		Font font = new Font("Monospaced", Font.PLAIN, 11);
		FontMetrics metrics = getFontMetrics(font);		
		
		height = metrics.getHeight();
		setMinimumSize(new Dimension(width+1, width + height * 2 + gap));
		setPreferredSize(new Dimension(width+1, width + height * 2 + gap));	
		
		outCircle = new Ellipse2D.Float(0, height, width, width);
		inCircle = new Ellipse2D.Float(width/2-owidth/2, width/2-owidth/2+height, owidth, owidth);
		
		upperTri = new GeneralPath();
		upperTri.moveTo(0, height);
		upperTri.lineTo(width/2, width/2+height);
		upperTri.lineTo(width, height);
		upperTri.closePath();
		
		rightTri = new GeneralPath();
		rightTri.moveTo(width, height);
		rightTri.lineTo(width/2, width/2+height);
		rightTri.lineTo(width, width+height);
		rightTri.closePath();
		
		bottomTri = new GeneralPath();
		bottomTri.moveTo(0, height);
		bottomTri.lineTo(0, width+height);
		bottomTri.lineTo(width, width+height);
		bottomTri.closePath();
		
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
	
		g2.setFont(new Font("Monospaced", Font.PLAIN, 11));		
		g2.setStroke(new BasicStroke(1.0f));
		
		g2.drawRect(0, 0, getSize().width-1, getSize().height-1);
		g2.draw(outCircle);
		g2.drawLine(width/2, height+width/2, (int)(width/2+width/2*axisX), (int)(height+width/2-width/2*axisY));
		
		if (entered) {
			g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0));
			g2.draw(inCircle);
			float f = 0.707f;
			g2.drawLine((int)(width/2-width/2*f), (int)(width/2-width/2*f+height), (int)(width/2-owidth/2*f), (int)(width/2-owidth/2*f+height));
			g2.drawLine((int)(width/2+width/2*f), (int)(width/2-width/2*f+height), (int)(width/2+owidth/2*f), (int)(width/2-owidth/2*f+height));
			g2.drawLine((int)(width/2+width/2*f), (int)(width/2+width/2*f+height), (int)(width/2+owidth/2*f), (int)(width/2+owidth/2*f+height));
		}
		
		g2.drawString(type2String(type), 2, height);
		g2.drawString(name, 2, getSize().height-2);
	}
	
	private String type2String(int type) {
		switch (type) {
			case A: return "A";
			case X: return "X";
			case Y: return "Y";
			case O: return "O";
			default: return "O";
		}
	}
	
	public void addChangeListener(ChangeListener l) {
        eList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        eList.remove(ChangeListener.class, l);
    }

    private void fireStateChanged() {
        Object[] list = eList.getListenerList();
        for (int i = list.length - 2; i >= 0; i -= 2) {
            if (list[i] == ChangeListener.class) {
                if (event == null)
                    event = new ChangeEvent(this);
                ((ChangeListener) list[i + 1]).stateChanged(event);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    	if (!enabled) return;
    	
    	Point p = e.getPoint();
    	
    	if (inCircle.contains(p))
    		setType(O);
    	else {
    		if (upperTri.contains(p))
    			setType(Y);
    		else if (rightTri.contains(p))
    			setType(X);
    		else if (bottomTri.contains(p))
    			setType(A);
    	}
    	
    	fireStateChanged();
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}    
    public void mouseDragged(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {
    	if (!enabled) return;
    	entered = true;
    	repaint();
    }
    public void mouseExited(MouseEvent e) {
    	if (!enabled) return;
    	entered = false;
    	repaint();
    }
    public void mouseMoved(MouseEvent e) {}
    
    
}
