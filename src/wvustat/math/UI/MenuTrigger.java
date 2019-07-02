package wvustat.math.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class MenuTrigger extends Component{
    
    private String title;
    private Dimension size=new Dimension(84,20);
    private Point origin=new Point(0,0);
    private Point offset=new Point(3,3);
    private Point currentPt;
    private int width=80, height=15;
    private boolean pressed=false;
    private Vector actionListeners=new Vector(5);

    public MenuTrigger(String title)
    {
        super();
        this.title=title;
        setBackground(Color.white);
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);

        currentPt=origin;
        setSize(size);
    }
    
    public void addActionListener(ActionListener listener){
        actionListeners.addElement(listener);
    }
    
    public void removeActionListener(ActionListener listener){
        actionListeners.remove(listener);
    }
    
    protected void fireActionEvent(ActionEvent evt){
        for(int i=0;i<actionListeners.size();i++){
            ((ActionListener)actionListeners.elementAt(i)).actionPerformed(evt);
        }
    }

    public synchronized void pressTrigger()
    {
        pressed=true;
        Graphics g=this.getGraphics();
        if(g!=null){
            update(g);
            g.dispose();
        }       
    }

    public synchronized boolean isPressed()
    {
        return pressed;
    }

    public synchronized void releaseTrigger()
    {
        pressed=false;
        Graphics g=this.getGraphics();
        if(g!=null){
            update(g);
            g.dispose();
        }       
    }

    public Dimension getMinimumSize()
    {
        return size;
    }

    public Dimension getPreferredSize()
    {
        return size;
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0,0,getSize().width,getSize().height-1);
        if(pressed)
            currentPt=offset;
        else
            currentPt=origin;
        
        g.setColor(Color.gray);
        g.fillRect(offset.x, offset.y, width, height);

        g.setColor(Color.white);
        g.fillRect(currentPt.x, currentPt.y, width,height);

        g.setColor(Color.black);
        g.drawRect(currentPt.x, currentPt.y, width, height);
        Font font=new Font("Dialog", Font.PLAIN, 11);
        g.setFont(font);
        FontMetrics metrics=g.getFontMetrics();
        g.drawString(title,currentPt.x+10, currentPt.y+10);
        if(!pressed)
        {
            int dist=metrics.stringWidth(title)+20;
            g.drawLine(currentPt.x+dist,currentPt.y, currentPt.x+dist, currentPt.y+height);
            int tx=currentPt.x+dist+(int)((width-dist)/2.0);
            int ty=currentPt.y+5;
            //g.fillRect(tx,ty,4,10);
            int[] xs=new int[4];
            int[] ys=new int[4];
            xs[0]=tx-6;
            xs[1]=tx+6;
            xs[2]=tx;
            xs[3]=tx-6;
            ys[0]=ty;
            ys[1]=ty;
            ys[2]=ty+4;
            ys[3]=ty;
            g.fillPolygon(xs,ys,4);
        }
    }

   
    protected void processMouseEvent(MouseEvent evt){
        if(evt.getID()==MouseEvent.MOUSE_PRESSED){
            pressed=true;
            //repaint();
            pressTrigger();
            fireActionEvent(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"Show"));
        }
        else if(evt.getID()==MouseEvent.MOUSE_RELEASED){
            pressed=false;
            releaseTrigger();
            fireActionEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Hide"));
        }
        
        super.processMouseEvent(evt);           
    }

    public static void main(String[] args)
    {
        Frame f=new Frame("Test");
        MenuTrigger trigger=new MenuTrigger("Options");
        f.add(trigger);
        f.setSize(220, 100);
        f.show();
    }

}
