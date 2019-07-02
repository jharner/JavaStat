package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

/**
* Palette implements a color palette that users can choose color from. It has a property
* color. If this property changes, it will fire a ChangeEvent to its ChangeListener.
*
*	@author: Hengyi Xue
* @version: 1.0, Mar. 15, 2000
*/
public class Palette extends JComponent implements MouseListener{
	private static Color[] colors={Color.black, Color.white, Color.red,
		Color.green, Color.blue, Color.cyan, Color.magenta, Color.yellow};
		
	private Shape[] blocks;
	private int width=10, height=10;
	private Color currentColor=Color.black;
	private EventListenerList eList=new EventListenerList();
	private ChangeEvent event;
	
	/**
	* Constructor
	*/	
	public Palette(){
		setBackground(Color.white);
		setSize(new Dimension(43,21));
		setMinimumSize(new Dimension(43,21));
		setPreferredSize(new Dimension(43, 21));
		
		setToolTipText("Click to select a color");
	
		addMouseListener(this);
		
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		blocks=new Shape[8];
		int x=0,y=0;
		for(int i=0;i<blocks.length;i++){
			if(i==4){
				x=0;
				y+=height+1;
			}
			
			blocks[i]=new Rectangle2D.Float(x,y,width,height);
			x+=width+1;
		}
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2=(Graphics2D)g;
		
		for(int i=0;i<blocks.length;i++){
			g2.setColor(colors[i]);
			g2.fill(blocks[i]);
		}
	}
	
	public void addChangeListener(ChangeListener l){
		eList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l){
		eList.remove(ChangeListener.class, l);
	}
	
	private void fireStateChanged(){
		Object[] list=eList.getListenerList();
		for(int i=list.length-2;i>=0;i-=2){
			if(list[i]==ChangeListener.class){
				if(event==null)
					event=new ChangeEvent(this);
				((ChangeListener)list[i+1]).stateChanged(event);
			}
		}
	}
	
	public Color getColor(){
		return currentColor;
	}
	
	public void mousePressed(MouseEvent e){
	
		int x=e.getX(),y=e.getY();
		int i=0;
		boolean found=false;
		while(i<blocks.length && !found){
			found=blocks[i].contains(x,y);
			i++;
		}
			
		if(found){
			currentColor=colors[i-1];
			fireStateChanged();
		}
	}
	
	public void mouseReleased(MouseEvent e){}
	
	public void mouseClicked(MouseEvent e){}
	
	public void mouseEntered(MouseEvent e){}
	
	public void mouseExited(MouseEvent e){}
}
