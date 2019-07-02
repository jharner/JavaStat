package wvustat.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class MarkerMenuElement extends JPanel implements MouseListener, MouseMotionListener, MenuElement {
	private static final long serialVersionUID = 1L;
	
	private int grid = 24;
	private int numMark = 12;
	private int width = 4 * grid, height = 3 * grid;
	private int currentIndex = -1;
	private Rectangle[] rects;
	private MarkerShape[] markers;
	private int markType = 0;
	
	private EventListenerList eList = new EventListenerList();
    private ActionEvent event;

	public MarkerMenuElement() {
		rects = new Rectangle[numMark];
		markers = new MarkerShape[numMark];
				
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setSize(new Dimension(width, height)); 
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		
		Graphics2D g2 = (Graphics2D) g;		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.setColor(Color.black);
		for (int i = 0; i < numMark; i++) {
			int row = i / 4;
			int col = i % 4;
			markers[i] = new MarkerShape(col*grid + grid/2, row*grid + grid/2, i+1);
			rects[i] = new Rectangle(col*grid, row*grid, grid, grid);
			g2.draw(markers[i]);
		}		
		
		if (currentIndex >= 0) {
			g2.setColor(Color.blue);
			g2.fill(rects[currentIndex]);
			g2.setColor(Color.white);
			g2.fill(markers[currentIndex]);
			g2.draw(markers[currentIndex]);
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {
		markType = currentIndex + 1;
		
		MenuSelectionManager menuSelectionManager = MenuSelectionManager.defaultManager();
		menuSelectionManager.clearSelectedPath();
		
		this.fireActionEvent();
	}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	
	public void mouseMoved(MouseEvent e) {
		for (int i = 0; i < numMark; i++) {			
			if (rects[i].contains(e.getPoint())) {
				currentIndex = i;
				break;
			}
		}
		repaint();
	}
	
	public int getSelectedMarkerType() {
		return markType;
	}
	
	public void processMouseEvent(MouseEvent e, MenuElement path[], MenuSelectionManager manager) {
		if (e.getID() == MouseEvent.MOUSE_RELEASED) {
			manager.clearSelectedPath();
		}
	}
	
	public void processKeyEvent(KeyEvent e, MenuElement path[], MenuSelectionManager manager) {}
	
	public void menuSelectionChanged(boolean isIncluded) {}
	
	public MenuElement[] getSubElements() {return new MenuElement[0];}
	
	public Component getComponent() {return this;}
	
	public void addActionListener(ActionListener l) {
        eList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        eList.remove(ActionListener.class, l);
    }

    private void fireActionEvent() {
        Object[] list = eList.getListenerList();
        for (int i = list.length - 2; i >= 0; i -= 2) {
            if (list[i] == ActionListener.class) {
                if (event == null)
                    event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "MARKER_SELECTED");
                ((ActionListener) list[i + 1]).actionPerformed(event);
            }
        }
    }
	
	
	public static void main(String[] args) {
		MarkerMenuElement s = new MarkerMenuElement();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("A menu");
		JMenu submenu = new JMenu("A submenu");
		submenu.add(s);
		menu.add(submenu);
		JMenuItem anotherMI = new JMenuItem("Another menu");
		anotherMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Another");
			}
		});
		menu.add(anotherMI);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		frame.setSize(300,300);
		frame.setVisible(true);

	}
}
