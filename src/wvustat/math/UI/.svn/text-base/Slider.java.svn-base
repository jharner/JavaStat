/*
 * Slider.java
 *
 * Created on November 5, 2001, 11:23 AM
 */

package wvustat.math.UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.Vector;
/**
 *
 * @author  hxue
 * @version 
 */
public class Slider extends JComponent {
    private float min, max;
    private float value;
    private int axisW=200, axisH=8;
    private Insets insets=new Insets(2,10,2,10);
    private int sliderW=6, sliderH=16;
    private int numOfTics=20;
    
    private Color mainColor=new Color(204,204,204);
    //private Color textColor=new Color(102,102,153);
    
    private int currentX;
    
    private int width, height;
    
    private int ticLen=4, vgap=2;
    
    private boolean movingSlider=false;
    
    private Vector changeListeners=new Vector();
    
    private Image bufferImage;
    
    /** Creates new Slider */
    public Slider() {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        width=axisW+insets.left+insets.right;   
        height=axisH+insets.top+insets.bottom+ticLen+2*vgap+24;

    }
    
    public Slider(float min, float max, float value){
        this();
        this.min=min;
        this.max=max;
        setValue(value);
    }
    
    public void addChangeListener(ChangeListener listener){       
        changeListeners.addElement(listener);
    }
    
    public void removeChangeListener(ChangeListener listener){
        changeListeners.removeElement(listener);
    }
    
    protected void fireChangeEvent(ChangeEvent evt){
        for(int i=0;i<changeListeners.size();i++){
            ((ChangeListener)changeListeners.elementAt(i)).stateChanged(evt);
        }
    }

    public void addNotify()
    {
        super.addNotify();
        measure();
    }

    private void measure(){
        FontMetrics metrics;
         metrics=this.getFontMetrics(getFont());
        
        if(metrics!=null){             
            height=axisH+insets.top+insets.bottom+ticLen+metrics.getAscent()+2*vgap+10;
        }
    }        
    
    public void setValue(float value){
        this.value=value;
        currentX=this.convertReal(value);
        if(this.isShowing())
        update(getGraphics());
    }
    
    public Dimension getSize(){
        return getPreferredSize();
    }
    
    public Dimension getPreferredSize(){
        return new Dimension(width,height);
    }
    
    public Dimension getMinimumSize(){
        return new Dimension(width, height);
    }
    
    public float getValue(){
        return value;
    }
    
    public void setBounds(int x, int y, int w, int h){
        axisW=w-insets.left-insets.right;
        measure();
        currentX=convertReal(value);
        
        super.setBounds(x,y,w,h);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(this.getBackground());
        g.fillRect(0,0,width,height);
        
        int x0=insets.left;
        int y0=insets.top;
        
        y0=y0+(sliderH-axisH)/2;
        g.setColor(mainColor);
        g.fill3DRect(x0, y0, axisW, axisH,false);
        g.setColor(new Color(102,102,102));
        g.drawRect(x0, y0, axisW, axisH);                
        
        y0=insets.top;
        g.setColor(mainColor);
        g.fill3DRect(currentX-sliderW/2,y0,sliderW,sliderH, true);        
        g.setColor(new Color(102,102,102));
        g.drawRect(currentX-sliderW/2, y0, sliderW, sliderH);
        
        //Draw tics
        float Interval=(max-min)/numOfTics;
        y0=insets.top+sliderH+vgap;        
        
        NumberFormat nf=NumberFormat.getInstance();        
        FontMetrics metrics=g.getFontMetrics();
        
        for(int i=0;i<=numOfTics;i++){
            x0=convertReal(min+i*Interval);
            if(i%5==0){
                g.drawLine(x0,y0,x0,y0+ticLen);
                String label=nf.format(min+i*Interval);
                g.drawString(label, x0-metrics.stringWidth(label)/2, y0+ticLen+vgap+metrics.getAscent());
            }
            else
                g.drawLine(x0,y0,x0,y0+ticLen/2);
            //x0+=ticInterval;
            
        }
    }
    
    public void update(Graphics g){
        Graphics gr;

        if(bufferImage==null || bufferImage.getWidth(this)!=this.getSize().width || bufferImage.getHeight(this)!=this.getSize().height)
        bufferImage=this.createImage(getSize().width, getSize().height);

        gr=bufferImage.getGraphics();

        paint(gr);

        g.drawImage(bufferImage, 0,0,this);
    }    
    
    public float convertPixel(int x){
        float ratio=(max-min)/axisW; //value per pixel
        return (x-insets.left)*ratio+min;
    }
    
    public int convertReal(float x){
        float ratio=axisW/(max-min); //pixel per value
        return Math.round((x-min)*ratio)+insets.left;
    }
    
    protected void processMouseEvent(MouseEvent evt){
        Point pt=evt.getPoint();
        
        switch(evt.getID()){
            case MouseEvent.MOUSE_PRESSED:
                if(Math.abs(pt.x-currentX)<sliderW/2){
                    movingSlider=true;                    
                }
                break;
            case MouseEvent.MOUSE_RELEASED:
                if(movingSlider==true){
                    movingSlider=false;                    
                }
                break;
         }
        
        super.processMouseEvent(evt);
    }
    
    protected void processMouseMotionEvent(MouseEvent evt){
        Point pt=evt.getPoint();
        
        switch(evt.getID()){
            case MouseEvent.MOUSE_DRAGGED:
                if(movingSlider && pt.x>insets.left && pt.x<axisW+insets.left){
                    currentX=pt.x;                    
                    update(getGraphics());

                    value=convertPixel(currentX);
                    ChangeEvent chEvt=new ChangeEvent(this);
                    fireChangeEvent(chEvt);                    
                }
            break;            
        }
        
        super.processMouseMotionEvent(evt);
    }                    
    
    public static void main(String[] args){
        Slider slider=new Slider(-10,10,2);
        
        Frame fr=new Frame();
        fr.add(slider);
        
        fr.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent evt){
                System.exit(0);
            }
        }
        );        
        
        fr.pack();
        fr.show();
    }    

}