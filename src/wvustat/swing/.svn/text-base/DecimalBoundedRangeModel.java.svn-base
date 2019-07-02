package wvustat.swing;

import javax.swing.event.*;

public class DecimalBoundedRangeModel {
	
	private EventListenerList eList = new EventListenerList();
    private ChangeEvent event;
    
    private float min, max, value;
    
    public DecimalBoundedRangeModel() {
    	this.value = 0;
    	this.min = 0;
    	this.max = 100;
    }
    
    public DecimalBoundedRangeModel(float value, float min, float max) {
    	this.value = value;
    	this.min = min;
    	this.max = max;
    }
    
    public float getMinimum() {
    	return min;
    }
    
    public void setMinimum(float m) {
    	if (min != m) {
    		min = m;
    		fireStateChanged();
    	}
    }
    
    public float getMaximum() {
    	return max;
    }
    
    public void setMaximum(float m) {
    	if (max != m) {
    		max = m;
    		fireStateChanged();
    	}
    }
    
    public float getValue() {
    	return value;
    }
    
    public void setValue(float v) {
    	if (v < min) v = min;
    	if (v > max) v = max;
    	if (value != v) {
    		value = v;
    		fireStateChanged();
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

}
