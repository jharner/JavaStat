/*

 * LinkLabel2.java

 * author: Ximing Zhao

 * 

 */



package wvustat.simulation.chisquare;



import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

import java.util.Vector;





public class LinkLabel2 extends JComponent {

    private String text;

    private boolean enabled=false, visited=false;

    private Color disabledColor, enabledColor, visitedColor;

    private Insets margin=new Insets(4,4,4,4);

    private int width, height;

    private Font font=new Font("Serif", Font.BOLD, 14);

    private Vector actionListeners=new Vector();



    /** Creates new LinkLabel2 */

    public LinkLabel2(String text) {

        this.text=text;

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

        

        disabledColor=Color.gray;

        enabledColor=Color.blue;

        visitedColor=new Color(0x9d, 0x11, 0xb3);

        measure();

        setBorder(new javax.swing.border.EtchedBorder());

    }

    

    public LinkLabel2(String text, boolean enabled){

        this(text);

        this.enabled=enabled;

        if(enabled)

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }

    

    public void addActionListener(ActionListener listener){

        actionListeners.addElement(listener);

    }

    

    public void removeActionListener(ActionListener listener){

        actionListeners.removeElement(listener);

    }

    

    protected void fireActionEvent(ActionEvent evt){

        for(int i=0;i<actionListeners.size();i++){

            ActionListener al=(ActionListener)actionListeners.elementAt(i);

            al.actionPerformed(evt);

        }

    }

    

    public void setEnabled(boolean value){

        enabled=value;

        if(enabled)

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        repaint();

    }

   

    private void measure(){

        FontMetrics metrics;

        try{

            metrics=this.getFontMetrics(font);

            if(metrics==null)

                return;

        }

        catch(Exception e){

            return;

        }

        

        width=margin.left+margin.right+metrics.stringWidth(text);

        height=metrics.getHeight()+margin.top+margin.bottom;

    }

    

    public Dimension getPreferredSize(){

        measure();

        return new Dimension(width, height);

    }

        

    

    public void paintComponent(Graphics g){

        FontMetrics metrics=getFontMetrics(font);

        

        int x=margin.left;

        int y=margin.top+metrics.getAscent();

        

        if(!enabled)

            g.setColor(disabledColor);

        else if(visited)

            g.setColor(visitedColor);

        else

            g.setColor(enabledColor);

        

        g.setFont(font);

        g.drawString(text, x, y);

        y+=2;

        g.drawLine(x, y, x+metrics.stringWidth(text), y);

    }

    

    protected void processMouseEvent(java.awt.event.MouseEvent evt){

        if(!enabled)

            return;

        int id=evt.getID();

        

        if(id==MouseEvent.MOUSE_CLICKED){

            visited=true;

            repaint();

            ActionEvent ae=new ActionEvent(this,ActionEvent.ACTION_PERFORMED,text);

            fireActionEvent(ae);            

        }

        

        super.processMouseEvent(evt);

    }

}