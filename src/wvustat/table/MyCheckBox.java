/*
 * MyCheckBox.java
 *
 * Created on May 29, 2002, 10:59 AM
 */

package wvustat.table;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author  James Harner
 * @version
 */
public class MyCheckBox extends javax.swing.JComponent {   
    
    private boolean value=false;
    private int width=10, height=10;
    private java.util.List actionListeners=new ArrayList();

    public MyCheckBox(){
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }
    
    public MyCheckBox(boolean val){
        this();
        value=val;
    }
    
    public void setState(boolean val){
        value=val;
    }
    
    public boolean getState(){
        return value;
    }
    
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        
        super.setBounds(x,y,w,h);
    }
    
    
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }
    
    public Dimension getSize(){
        return new Dimension(width, height);
    }
    
    public Dimension getMinimumSize(){
        return new Dimension(width, height);
    }

    protected void processMouseEvent(MouseEvent evt)
    {
        super.processMouseEvent(evt);
        if(evt.getID()==MouseEvent.MOUSE_PRESSED)
        {
            boolean state=this.getState();
            setState(!state);
            repaint();
            fireActionEvent(new ActionEvent(this,ActionEvent.ACTION_PERFORMED, ""));
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        
        g2.setColor(getBackground());
        g2.fillRect(0,0,width,height);
        
        g2.setColor(getForeground());
        Stroke sk1=new BasicStroke(2.0f);
        Stroke sk0=g2.getStroke();
        
        if(value){
            int x=width/2-4;
            int y=height/2-4;
            g2.drawLine(x,y+8,x+8,y);
            g2.setStroke(sk1);
            g2.drawLine(x,y+3,x,y+7);
            g2.drawLine(x,y+3,x-1,y+3);
        }
    }
    
    public void addActionListener(ActionListener l)
    {
        actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l)
    {
        actionListeners.remove(l);
    }

    public void fireActionEvent(ActionEvent evt)
    {
        for(int i=0;i<actionListeners.size();i++)
        {
            ActionListener l=(ActionListener)actionListeners.get(i);
            l.actionPerformed(evt);
        }
    }
}
