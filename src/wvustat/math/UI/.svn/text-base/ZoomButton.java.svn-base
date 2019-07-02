/*
 * ReboundButton.java
 *
 * Created on November 26, 2001, 11:33 AM
 */

package wvustat.math.UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.*;

import wvustat.math.plot.*;
/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class ZoomButton extends JComponent implements Runnable{
    public static final int ZOOM_IN_XY=0;
    public static final int ZOOM_OUT_XY=1;
    public static final int ZOOM_IN_X=2;
    public static final int ZOOM_OUT_X=3;
    public static final int ZOOM_IN_Y=4;
    public static final int ZOOM_OUT_Y=5;
    public static final int RESET=6;

    private Image image;
    private int offsetx=5, offsety=5;
    private Boolean pressed=Boolean.FALSE;
    private int imageWidth=32, imageHeight=32;
    private int zoomMode;
    private int sleepTime=200;
    private FunctionPlotter plotter;

    /** Creates new ReboundButton */
    public ZoomButton(String imageFileName, int zoomMode){
        this.zoomMode=zoomMode;
        loadImage(imageFileName);
        //addMouseListener(new ClickHandler());
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public ZoomButton(Image image, int zoomMode){
        this.image=image;
        this.zoomMode=zoomMode;
        forceLoadImage(image);
        //addMouseListener(new ClickHandler());
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }


    public void setFunctionPlotter(FunctionPlotter plotter){
        this.plotter=plotter;
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


        public void processMouseEvent(MouseEvent evt)
        {
            if(evt.getID()==MouseEvent.MOUSE_PRESSED)
            {
                pressButton();
                Thread thread=new Thread(ZoomButton.this);
                thread.start();
            }
            else if(evt.getID()==MouseEvent.MOUSE_RELEASED)
            {
                releaseButton();
            }
        }


    public void run() {
        if(zoomMode==RESET){
            plotter.resetAxises();
            return;
        }

        while(true){
            synchronized(pressed){
                if(pressed.booleanValue()==false)
                return;
                else{
                    switch(zoomMode){
                        case ZOOM_OUT_XY:
                        plotter.zoomOut();
                        break;
                        case ZOOM_IN_XY:
                        plotter.zoomIn();
                        break;
                        case ZOOM_OUT_X:
                        plotter.zoomOutX();
                        break;
                        case ZOOM_IN_X:
                        plotter.zoomInX();
                        break;
                        case ZOOM_OUT_Y:
                        plotter.zoomOutY();
                        break;
                        case ZOOM_IN_Y:
                        plotter.zoomInY();
                        break;
                    }
                }
            }


            try{
                Thread.currentThread().sleep(sleepTime);
            }
            catch(InterruptedException e){
            }


        }
    }

    public static void main(String[] args){
        Frame fr=new Frame("Test");
        ZoomButton button=new ZoomButton(".\\images\\reset.gif", ZoomButton.ZOOM_OUT_XY);

        fr.add(button);
        fr.pack();
        fr.show();
    }



}