package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.*;
import java.rmi.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import wvustat.interfaces.*;

/**
 * PieChart is similar to a Pie chart except that arcs instead of bars are used to display data.
 *
 * @author: Hengyi Xue
 * @version: 1.0, June 1, 2000
 */

public class PieChart extends JPanel{

    private double[] values;

    private String[] labels;

    private int width=250, height=200;

    private FontMetrics metrics;

    private Insets insets=new Insets(20,20,20,20);

    private NumberFormat nf;

    private Color[] colors;

    private Arc2D[] arcs;

    private Arc2D focusedArc;

    private int focusedID;

    private double sum; //The sum of all the values in array values

    private int diameter=160; //The diameter of the pie

    public PieChart(){
        init();
    }

    public PieChart(double[] values, String[] labels){
        this.values=values;
        this.labels=labels;

        init();
    }

    protected void init(){
        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
        addMouseMotionListener(new MouseHandler());
        metrics=getFontMetrics(getFont());
    }

    public void setValues(double[] values){
        this.values=values;
        initPlot();
        repaint();
    }

    public void setLabels(String[] labels){
        this.labels=labels;
        repaint();
    }
    
    protected void initPlot(){
        sum=0;

        for(int i=0;i<values.length;i++){
            sum+=values[i];
        }
        //Initialize colors
        colors=new Color[values.length];

        float d=1.0f/(values.length*1.8f);

        for(int i=0;i<colors.length;i++){
            float r=d*(i+1);
            if(i%3==0)
            colors[i]=new Color(1-r, i/3.0f*d,0);
            else if(i%3==1)
            colors[i]=new Color(i/3.0f*d, r, 1.0f);
            else if(i%3==2)
            colors[i]=new Color(1-i/3.0f*d, 1-r, 0);
        }

        //Initialize arcs
        arcs=new Arc2D[values.length];

        //center of the pie
        float x0=width/2.0f-diameter/2.0f;
        float y0=height/2.0f-diameter/2.0f;
        float angSt=0;

        for(int i=0;i<arcs.length;i++){
            float angExt=(float)(values[i]*360/sum);
            arcs[i]=new Arc2D.Float(x0, y0, diameter, diameter, angSt, angExt, Arc2D.PIE);
            angSt+=angExt;
        }

    }

    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        diameter=height-insets.top-insets.bottom;
        if(values!=null)
            initPlot();
        super.setBounds(x,y,w,h);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
 
        for(int i=0;i<arcs.length;i++){

            g2.setPaint(colors[i]);

            g2.fill(arcs[i]);
        }

        if(focusedArc!=null){
            g2.setPaint(Color.black);
            g2.draw(focusedArc);
            //Display the current group
            NumberFormat nf=NumberFormat.getInstance();
            
            String display=labels[focusedID];
            display+=": "+nf.format(values[focusedID]);
            nf.setMaximumFractionDigits(2);
            display+=" ("+nf.format(values[focusedID]/sum*100)+"%)";

            g2.drawString(display, insets.left, insets.top);

        }

    }

    public Color[] getColors(){
        return colors;
    }

    public String[] getColorLabels(){
        return labels;
    }
 
    class MouseHandler extends MouseMotionAdapter{
        public void mouseMoved(MouseEvent me){
            Point pt=me.getPoint();

            boolean found=false;
            int i=0;
            while(i<arcs.length&&!found){
                found=arcs[i].contains(pt);
                i++;
            }

            if(found && arcs[i-1]!=focusedArc){
                focusedArc=arcs[i-1];
                focusedID=i-1;
                repaint();
            }
            else if(!found){
                focusedArc=null;
                repaint();
            }
        }
    }


}
