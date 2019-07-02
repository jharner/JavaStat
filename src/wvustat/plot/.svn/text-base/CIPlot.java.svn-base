/*
 * CIPlot.java
 *
 * Created on April 24, 2002, 2:06 PM
 */

package wvustat.plot;

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;

import wvustat.util.TicmarkManager;
import wvustat.util.CoordConverter;
import wvustat.util.AppFrame;

/**
 *
 * @author  Hengyi Xue
 * @version 
 */
public class CIPlot extends javax.swing.JComponent {
    private double[] avgs, sds;
    private double mean, sd;
    private int width=250, height=160;
    private double ymin, ymax, yInterval;
    private CoordConverter coord;
    private Insets insets=new Insets(20,40,40,20);
    private double tStat;
    private int currentIndex=0;
    
    /** Creates new CIPlot */
    public CIPlot(double mean, double sd) {
        this.mean=mean;
        this.sd=sd;
        setBackground(Color.white);
        preparePlot();
    }
    
    public synchronized void setStats(double mean, double sd){
        this.mean=mean;
        this.sd=sd;
        preparePlot();
    }
    
    public synchronized void setData(double[] array1, double[] array2, double tStat){
        avgs=array1;
        sds=array2;
        this.tStat=tStat;
        currentIndex=avgs.length;
        repaint();
    }
    
    public synchronized void setXLength(int size){
        avgs=new double[size];
        sds=new double[size];
        currentIndex=0;
    }
    
    public synchronized void setTStat(double tval){
        tStat=tval;
    }
    
    
    public synchronized void addPoint(double avg, double sd){
        avgs[currentIndex]=avg;
        sds[currentIndex]=sd;
        currentIndex++;
        repaint();
    }    
    
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }
    
    public void setBounds(int x, int y, int w, int h){
        this.width=w;
        this.height=h;
        super.setBounds(x,y,w,h);
    }
    
    private void preparePlot(){
        ymin=mean-2*sd;
        ymax=mean+2*sd;
        
        TicmarkManager ytm=new TicmarkManager(ymin, ymax, 5);
        ymin=ytm.getMin();
        ymax=ytm.getMax();
        
        yInterval=ytm.getInterval();
    }
        

    protected void paintComponent(java.awt.Graphics graphics) {
        super.paintComponent(graphics);
        
        Graphics2D g2=(Graphics2D)graphics;
        g2.setColor(getBackground());
        g2.fillRect(0,0,width-1,height-1);
        
        g2.setColor(getForeground());
        g2.drawRect(0,0,width-1,height-1);
        
        if(avgs==null|| sds==null)
            return;
        
        int xmin=0;
        int xmax=avgs.length;
        
        coord=new CoordConverter(width, height, xmin, xmax+0.5, ymin, ymax, insets);
        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        
        //Draw x and y axis
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax+0.5), coord.y(ymin));
        FontMetrics metrics=g2.getFontMetrics();
        
        //Draw tic marks
        double tmp=ymin;
        while(tmp<=ymax){
            g2.drawLine(coord.x(xmin), coord.y(tmp), coord.x(xmin)-3, coord.y(tmp));
            g2.drawString(nf.format(tmp), coord.x(xmin)-metrics.stringWidth(nf.format(tmp))-3,coord.y(tmp)+metrics.getHeight()/2);
            tmp+=yInterval;
        }
        
        for(int i=1;i<=xmax;i++){
            g2.drawLine(coord.x(i), coord.y(ymin), coord.x(i), coord.y(ymin)+2);
            //g2.drawString(String.valueOf(i), coord.x(i)-metrics.stringWidth(String.valueOf(i))/2, coord.y(ymin)+3+metrics.getHeight());
        }
        
        TicmarkManager xtm=new TicmarkManager(xmin, xmax, 5);
        int xInterval=(int)xtm.getInterval();
        int tmpi=xmin;
        while(tmpi<=xmax){
            g2.drawLine(coord.x(tmpi), coord.y(ymin), coord.x(tmpi), coord.y(ymin)+4);
            g2.drawString(String.valueOf(tmpi), coord.x(tmpi)-metrics.stringWidth(String.valueOf(tmpi))/2, coord.y(ymin)+4+metrics.getHeight());
            tmpi+=xInterval;
        }
        
        //Draw center line
        g2.setColor(Color.blue);
        g2.drawLine(coord.x(xmin), coord.y(mean), coord.x(xmax+0.5), coord.y(mean));
        String tick=nf.format(mean);
        g2.drawString(tick, coord.x(xmax+0.5)-metrics.stringWidth(tick), coord.y(mean)-2);
        
        g2.setColor(Color.red);
        for(int i=0;i<currentIndex;i++){
            double w=sds[i]*tStat;
            g2.drawLine(coord.x(i+1), coord.y(avgs[i]-w), coord.x(i+1), coord.y(avgs[i]+w));
            g2.drawLine(coord.x(i+1)-2, coord.y(avgs[i]-w),coord.x(i+1)+2, coord.y(avgs[i]-w)); 
            g2.drawLine(coord.x(i+1)-2, coord.y(avgs[i]+w),coord.x(i+1)+2, coord.y(avgs[i]+w));
        }
    }
    
    public static void main(String[] args){
        double[] m={2,3,2,2,3,4,2,3};
        double[] d={1,1,1,1,1,1,1,1};
        
        JFrame jf=new AppFrame();
        CIPlot plot=new CIPlot(2.5,1);
        plot.setData(m,d,1);
        
        jf.getContentPane().add(plot);
        jf.setSize(420,360);
        jf.show();
    }
    
}
