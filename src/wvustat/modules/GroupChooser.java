package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * GroupChooser is a component used to choose a single value from a group of
 * values. It has the look and feel of an slider. It is used in data analysis modules
 * in which observations can be divided into groups.
 *
 * @author: Hengyi Xue
 * @version: 1.4, Mar. 14, 2000
 */
public class GroupChooser extends JComponent implements MouseListener{
    private String[] labels;
    private GroupMaker grouper;
    private String chooserLabel;

    private EventListenerList eList=new EventListenerList();
    private ChangeEvent event;

    private int currentIndex=0, maxIndex;

    private int width=110, height=25, textht=15, fillWidth=20;

    private Rectangle2D bar,fill;

    private java.text.NumberFormat formatter;

    /**
     * Constructor
     * Creates a new GroupChooser based on the GroupMaker object
     *
     * @param grouper the GroupMaker object which contains grouping information about a data set
     */
    public GroupChooser(GroupMaker grouper){
        this.grouper=grouper;

        setBackground(Color.white);
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(new Dimension(width,height));
        setSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));

        setToolTipText("Click to select a group");

        addMouseListener(this);

        if(grouper.getType()==GroupMaker.DISCRETE)
        labels=grouper.getGroupNames();



        bar=new Rectangle2D.Float(0,textht,width-1,height-textht-1);
        
        fillWidth=(int)(width*1.0/grouper.getGroupCount());
        chooserLabel=grouper.getName();
        maxIndex=grouper.getGroupCount();
        

        fill=new Rectangle2D.Float(0,textht,fillWidth-1,height-textht-1);

        formatter=java.text.NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(1);


            
    }

    /**
     * Construct a GroupChooser object from a given label and a string array. The label is used to name the
     * GroupChooser, the string array are the pool of values to choose from.
     */
    public GroupChooser(String chooserLabel, String[] values){
        this.chooserLabel=chooserLabel;
        this.labels=values;

        maxIndex=values.length;
        setBackground(Color.white);
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(new Dimension(width,height));
        setSize(new Dimension(width,height));
        setMaximumSize(new Dimension(width,height));

        setToolTipText("Click to select a group");

        addMouseListener(this);

        bar=new Rectangle2D.Float(0,textht,width-1,height-textht-1);

        fillWidth=(int)(width*1.0/labels.length);

        fill=new Rectangle2D.Float(0,textht,fillWidth-1,height-textht-1);

        formatter=java.text.NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(1);
    }

    /**
     * Render this component
     */
    public void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D)g;

        if(currentIndex==maxIndex-1){
            double tmpX=currentIndex*fillWidth;
            double tmpW=bar.getWidth()-tmpX;
            fill.setRect(tmpX,fill.getY(),tmpW,fill.getHeight());
        }
        else        
            fill.setRect(currentIndex*fillWidth, fill.getY(), fill.getWidth(), fill.getHeight());

        g2.draw(bar);
        g2.fill(fill);

        String name=chooserLabel;

        if(name.length()>6)
        name=name.substring(0,3)+"...";

        FontMetrics metrics=getFontMetrics(g2.getFont());

        g2.drawString(name, 0, textht-2);

        if(labels!=null){
            String tmps=labels[currentIndex];
            if(tmps.length()>9)
            tmps=tmps.substring(0,6)+"...";
            int tmpi=metrics.stringWidth(tmps);
            g2.drawString(tmps, width-tmpi-1,textht-2);
        }
        else{

                double lo=grouper.getLowerLimit(currentIndex);
                double hi=grouper.getUpperLimit(currentIndex);

                String tmps=formatter.format(lo)+"-"+formatter.format(hi);
                int tmpi=metrics.stringWidth(tmps);
                g2.drawString(tmps,width-1-tmpi,textht-2);

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

    /**
     * Get the current value the user has selected
     */
    public int getCurrentIndex(){
        return currentIndex;
    }

    /**
     * Set the value the chooser displayed to the specified index so that this
     * component can be programmatically controlled
     */
    public void setCurrentIndex(int index){
        currentIndex=index;
        repaint();
    }

    public void mousePressed(MouseEvent evt){
        int x=evt.getX(), y=evt.getY();

        if(bar.contains(x,y)&& !fill.contains(x,y)){
            if(x>fill.getX()){
                currentIndex++;
            }
            else{
                currentIndex--;
            }

            repaint();
            fireStateChanged();
        }
    }

public void mouseEntered(MouseEvent e){}
public void mouseReleased(MouseEvent e){}
public void mouseExited(MouseEvent e){}
public void mouseClicked(MouseEvent e){}
}
