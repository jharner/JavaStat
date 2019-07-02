/*
 * ToggleButton.java
 *
 * Created on January 15, 2002, 10:00 AM
 */

package wvustat.math.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

import wvustat.math.plot.*;
/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class ToggleButton extends JComponent{
    private Image image;
    private int offsetx=5, offsety=5;
    private Boolean pressed=Boolean.FALSE;
    private int imageWidth=32, imageHeight=32;

    private int sleepTime=200;
    private Vector actionListeners=new Vector();    
    private boolean alwaysUp=false;       //If this is true, the button will always bounce back after being pushed

    /** Creates new ReboundButton */
    public ToggleButton(String imageFileName){
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        loadImage(imageFileName);
    }

    public ToggleButton(Image image){
        this.image=image;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        forceLoadImage(image);
    }
    
    public ToggleButton(Image image, boolean alwaysUp){
        this(image);
        this.alwaysUp=alwaysUp;
    }
    
    public void addActionListener(ActionListener listener){
        actionListeners.addElement(listener);
    }
    
    public void removeActionListener(ActionListener listener){
        actionListeners.removeElement(listener);
    }
    
    protected void fireActionEvent(ActionEvent evt){
        for(int i=0;i<actionListeners.size();i++){
            ((ActionListener)actionListeners.elementAt(i)).actionPerformed(evt);
        }
    }

    private void loadImage(String imageFileName){
        Toolkit kit=Toolkit.getDefaultToolkit();
        image=kit.getImage(imageFileName);
        forceLoadImage(image);
    }

    private void forceLoadImage(Image image){
        MediaTracker tracker=new MediaTracker(this);
        synchronized(tracker) {
            tracker.addImage(image, 0);
            try {
                tracker.waitForID(0, 0);
            } catch (InterruptedException e) {
                System.out.println("INTERRUPTED while loading Image");
            }
            tracker.removeImage(image, 0);
        }

        imageWidth=image.getWidth(this);
        imageHeight=image.getHeight(this);
    }

    public Dimension getSize(){
        return new Dimension(imageWidth+offsetx+1, imageHeight+offsety+1);
    }

    public Dimension getPreferredSize(){
        return getSize();
    }

    public Dimension getMinimumSize(){
        return getSize();
    }

    public void paintComponent(Graphics g){
        g.setColor(this.getBackground());
        g.fillRect(0,0,imageWidth+offsetx+1, imageHeight+offsety+1);
        int x0=1,y0=1;
        if(pressed.booleanValue()==true){
            x0=offsetx;
            y0=offsety;
        }

        g.setColor(Color.gray);
        g.fillRect(offsetx, offsety, imageWidth, imageHeight);

        g.setColor(Color.black);
        g.drawRect(x0-1,y0-1,imageWidth+1, imageHeight+1);
        g.drawImage(image, x0,y0,this);
    }

    public void pressButton(){
        synchronized(pressed){
            pressed=Boolean.TRUE;
        }
        Graphics g=this.getGraphics();
        if(g!=null){
            update(g);
            g.dispose();
        }
    }

    public void releaseButton(){
        synchronized(pressed){
            pressed=Boolean.FALSE;
        }

        Graphics g=this.getGraphics();
        if(g!=null){
            update(g);
            g.dispose();
        }
    }
    
    public boolean isPressed(){
        return pressed.booleanValue();
    }

    protected void processMouseEvent(MouseEvent evt){
        if(evt.getID()==MouseEvent.MOUSE_PRESSED){
            if(alwaysUp==false){
                if(pressed.booleanValue())
                    releaseButton();
                else
                    pressButton();
            }
            else
                pressButton();
            
            ActionEvent act=new ActionEvent(this,ActionEvent.ACTION_PERFORMED, "Button pushed");
            this.fireActionEvent(act);
        }
        else if(evt.getID()==MouseEvent.MOUSE_RELEASED){
            if(alwaysUp)
                releaseButton();
        }

        super.processMouseEvent(evt);
    }

    public static void main(String[] args){
        Frame fr=new Frame("Test");
        ToggleButton button=new ToggleButton(".\\images\\magnify.gif");

        fr.add(button);
        fr.pack();
        fr.show();
    }
}