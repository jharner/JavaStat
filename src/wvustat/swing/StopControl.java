package wvustat.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class StopControl extends JComponent implements MouseListener{
	private static final long serialVersionUID = 1L;
	private final int width = 11, height = 11;
	
	private boolean stopped = true;
	//private Image stopImg, startImg;
	
	private EventListenerList eList = new EventListenerList();
    private ChangeEvent event;
	
	public StopControl() {
		addMouseListener(this);
		
		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));	
		
		//stopImg = Toolkit.getDefaultToolkit().getImage(StopControl.class.getResource("/jlfgr/24x24/Stop.gif"));
		//startImg = Toolkit.getDefaultToolkit().getImage(StopControl.class.getResource("/jlfgr/24x24/Start.gif"));
	}
	
	public void setStopped(boolean state) {
		this.stopped = state;
		repaint();
	}
	
	public boolean isStopped() {
		return stopped;
	}
	
	protected void paintComponent(Graphics g) {
		/*if (stopped)
			g.drawImage(startImg, 0, 0, this);
		else
			g.drawImage(stopImg, 0, 0, this);*/
		
		if (stopped) {
			g.setColor(new Color(0, 100, 0));
			g.fillRect(0, 0, width-1, height-1);
		} 
		else {
			g.setColor(Color.red);
			g.fillRect(0, 0, width-1, height-1);
		}		
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		fireStateChanged();
	}
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    
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
