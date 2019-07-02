package wvustat.swing;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.event.*;


public class SimpleSlider extends JComponent implements ChangeListener, MouseListener, MouseMotionListener, DelayedDispatcher {
	private static final long serialVersionUID = 1L;

	private int width = 100, height = 9, gap = 4;

	private DecimalBoundedRangeModel model;
	private String label;
	private int fractionDigits = 0;
	private Rectangle rect/*, labelRegion*/;
	private int pos;
	private boolean dragLine = false;
	private DelayedDispatch dispatcher;
	/*private JPopupMenu popup;*/
	
	private EventListenerList eList = new EventListenerList();
    private ChangeEvent event;
    
	
	public SimpleSlider() {
		init(new DecimalBoundedRangeModel());
	}
	
	public SimpleSlider(DecimalBoundedRangeModel m) {
		init(m);
	}
	
	public SimpleSlider(float min, float max, float value) {
		init(new DecimalBoundedRangeModel(value, min, max));
	}
	
	protected void init(DecimalBoundedRangeModel m) {
		setModel(m);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		Font font = new Font("Monospaced", Font.PLAIN, 11);
		FontMetrics metrics = getFontMetrics(font);		
		setMinimumSize(new Dimension(width + 1, height + 1 /*metrics.getHeight() + gap*/));
		setPreferredSize(new Dimension(width + 1, height + 1 /*metrics.getHeight() + gap*/));		

		//labelRegion = new Rectangle(0, 0, width - 1, metrics.getHeight() + gap);
	}
	
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		width = w;
		height = h;
	}
	
	public Font getFont() {
		return new Font("Monospaced", Font.PLAIN, 11);
	}
	
	public void setModel(DecimalBoundedRangeModel m) {
		DecimalBoundedRangeModel old = model;
		if (old != null)
			old.removeChangeListener(this);
		
		if (m == null)
			model = new DecimalBoundedRangeModel();
		else
			model = m;
		model.addChangeListener(this);
		
		fireStateChanged();
	}
	
	public DecimalBoundedRangeModel getModel() {
		return model;
	}
	
	/*
	 * Executed when model changes
	 */
	public void stateChanged(ChangeEvent e) {
		repaint();
	}
	
	public float getMinimum() {
		return model.getMinimum();
	}
	
	public void setMinimum(float m) {
		float old = getMinimum();
		if (m != old) {
			model.setMinimum(m);
			fireStateChanged();
		}
	}
	
	public float getMaximum() {
		return model.getMaximum();
	}
	
	public void setMaximum(float m) {
		float old = getMaximum();
		if (m != old) {
			model.setMaximum(m);
			fireStateChanged();
		}
	}
	
	public float getValue() {
		return model.getValue();
	}
	
	public void setValue(float v) {
		float old = getValue();
		if (v != old) {
			model.setValue(v);
			fireStateChanged();
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String l) {
		String old = getLabel();
		if (!l.equals(old)) {
			label = l;
			fireStateChanged();
		}
		repaint();
	}
	
	public void setFractionDigits(int d) {
		this.fractionDigits = d;
	}
	
	public String getTextValue() {
		NumberFormat nf = new DecimalFormat("########.###");
		nf.setMaximumFractionDigits(fractionDigits);
		return nf.format(getValue());
	}
	
	/*public Rectangle getLabelRegion() {
		return labelRegion;
	}*/
	
	public int getGap() {
		return gap;
	}
	
	/*public void setPopup(JPopupMenu p) {
		popup = p;
	}*/
	
	public boolean getValueIsAdjusting() {
		return dragLine;
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(1.0f));
		rect = new Rectangle(0, 0 /*labelRegion.getSize().height*/, width - 1, height - 1);
		g2.draw(rect);
		
		pos = (int)((getValue() - getMinimum()) * width / (getMaximum() - getMinimum()));		
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        g2.drawLine(pos, 0 /*labelRegion.getSize().height*/, pos, getSize().height);
       
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
    
    public void respondToSingleClick(MouseEvent e){
    	float value = (getMaximum() - getMinimum()) * e.getX() / width + getMinimum();
    	setValue(value);
    }
    
    public void mousePressed(MouseEvent e) {
    	int x = e.getX(), y = e.getY();
    	
    	/*if(labelRegion.contains(x, y) && popup != null){
        	popup.show(e.getComponent(), x, labelRegion.getLocation().y + labelRegion.getSize().height);
        	return;
        }*/
    	
    	if(y >= getSize().height - rect.height && y <= getSize().height && Math.abs(x - pos) <= 4) 
    		dragLine = true;
    }
    
    public void mouseClicked(MouseEvent e) {
    	if (e.getClickCount() == 1 && rect.contains(e.getPoint())) {
    		dispatcher = new DelayedDispatch(e, this);
    		dispatcher.start();
    	} 
    	else if (e.getClickCount() == 2 && rect.contains(e.getPoint())) {
    		dispatcher.isDoubleClick = true;
    		
    		String input=JOptionPane.showInputDialog(this, "Please input the number of " + getLabel().toLowerCase() + ":", "input", JOptionPane.QUESTION_MESSAGE);
    		if(input==null) return;
    		try {
    			setValue(Float.parseFloat(input));
    		}
    		catch(NumberFormatException ex){
	        	return;	
	        }
    	}
    }
    
    public void mouseReleased(MouseEvent e) {
    	dragLine = false;
    	fireStateChanged();
    }
    
    public void mouseDragged(MouseEvent e) {
    	if (dragLine) {
    		float value = (getMaximum() - getMinimum()) * e.getX() / width + getMinimum();
    		setValue(value);
    	}
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

}
