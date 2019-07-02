/*
 * ParetoChart.java
 *
 * Created on August 10, 2000, 9:43 AM
 */

package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.text.NumberFormat;
import javax.swing.*;
import java.rmi.RemoteException;

import wvustat.interfaces.*;
import wvustat.util.MathUtils;
/**
 * ParetoChart displays frequencies of a categorical variable as bars. Bars are adjacent to each other,
 * there is no space between them like in a bar chart. The frequencies are also ranked. The bar representing
 * the highest frequency is at the leftmost position.
 *
 * @author  Hengyi Xue
 * @version 1.0, August 10, 2000
 */
public class ParetoChart extends FreqChart {

    private double[] yValue;
    private double freqSum=0;
    private String[] x1_labels;
    /** Creates new ParetoChart */
    public ParetoChart(DataSet data, Variable x1) {
        super(data,x1);
        isChildOfThisClass=true;
    }

    protected void generateColors(){
        if(x2==null){
            colors=new Color[x1_levels.length];

            float d=1.0f/(x1_levels.length*1.5f);
            for(int i=0;i<colors.length;i++){
                float r=d*(i+1);
                if(i%3==0)
                colors[i]=new Color(1-r, i/3.0f*d,0);
                else if(i%3==1)
                colors[i]=new Color(i/3.0f*d, r, 1.0f);
                else if(i%3==2)
                colors[i]=new Color(1-i/3.0f*d, 1-r, 0);
            }
        }
    }

    public double[] getValues(){
        return yValue;
    }

    protected void initPlot(){
        freqSum=0;
        if(x2==null){
            yValue=new double[x1_levels.length];
            for(int i=0;i<yValue.length;i++){
                yValue[i]=freqs[i][i];
                freqSum+=yValue[i];
            }
        }
        else{
            yValue=new double[x1_levels.length*x2_levels.length];
            for(int i=0;i<yValue.length;i++){
                yValue[i]=freqs[i/x2_levels.length][i%x2_levels.length];
                freqSum+=yValue[i];
            }
        }

        x1_labels=new String[x1_levels.length];
        for(int i=0;i<x1_labels.length;i++) x1_labels[i]=x1_levels[i];
        
        MathUtils.InsertionSort(x1_labels, yValue);
        MathUtils.reverse(x1_labels);
        
        StatEngine se=new StatEngineImpl();
        try{
            yValue=se.sortArray(yValue);
        }
        catch(RemoteException re){
            re.printStackTrace();
        }   
        
        /*
        ymax=0;
        for(int i=0;i<freqs.length;i++){
        for(int j=0;j<freqs[i].length;j++){
        if(freqs[i][j]>ymax)
        ymax=freqs[i][j];
        }
        }
         */
        ymax=90;

        ymin=0;
        xmin=0;
        xmax=freqs.length;

        tm=new BaseAxisModel(ymin, ymax, 5);
        ymax=tm.getEndValue();

        nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        insets.left=metrics.stringWidth(nf.format(ymax))+10;
        if(insets.left<40) insets.left=40;

        coord=new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);

        if(x2==null)
        bars=new Rectangle2D[x1_levels.length];
        else
        bars=new Rectangle2D[x1_levels.length*x2_levels.length];

        int ticWidth=coord.x(xmin+1)-coord.x(xmin);

        int barWidth=0;
        if(x2==null){
            barWidth=ticWidth;

            for(int i=0;i<bars.length;i++){
                int yco=coord.y(yValue[x1_levels.length-1-i]/freqSum*100);
                int h=coord.y(ymin)-yco;
                bars[i]=new Rectangle2D.Float(coord.x(i),yco, barWidth, h);
            }
        }
        else{
            barWidth=(int)(ticWidth*0.8/x2_levels.length);

            int xco=0;
            int totalWidth=x2_levels.length*barWidth;
            int h=0;
            for(int i=0;i<bars.length;i++){
                int x1_index=i/x2_levels.length;
                int x2_index=i%x2_levels.length;

                if(x2_index==0){
                    xco=coord.x(x1_index+1)-totalWidth/2;
                }
                else{
                    xco=xco+barWidth;
                }

                h=coord.y(ymin)-coord.y(freqs[x1_index][x2_index]);
                bars[i]=new Rectangle2D.Float(xco, coord.y(freqs[x1_index][x2_index]),barWidth,h);
            }
        }
    }

    public String[] getLabels(){
        return x1_labels; //x1_levels;
    }

    protected void drawTicmarks(Graphics2D g2){
        nf.setMaximumFractionDigits(1);
        //draw x axis ticmarks
        for(int i=0;i<x1_levels.length;i++){
            String label=x1_labels[i]; //x1_levels[i]
            g2.drawLine(coord.x(i+0.5), coord.y(ymin), coord.x(i+0.5), coord.y(ymin)+4);
            //if(i%2==0)
            g2.drawString(label, coord.x(i+0.5)-metrics.stringWidth(label)/2, coord.y(ymin)+metrics.getHeight());
            //else
            //g2.drawString(label, coord.x(i+0.5)-metrics.stringWidth(label)/2, coord.y(ymin)+2*metrics.getHeight());
        }
        //Draw y axis ticmarks
        double tmp=ymin;
        double step=tm.getInterval();
        while(tmp<=ymax){
            g2.drawLine(coord.x(xmin), coord.y(tmp), coord.x(xmin)-4, coord.y(tmp));
            String label=nf.format(tmp);

            int len=metrics.stringWidth(label);

            g2.drawString(label, coord.x(xmin)-4-len, coord.y(tmp)+metrics.getMaxAscent()/2);

            tmp+=step;
        }

        String xLabel = x1.getName();

        g2.drawString(xLabel, width / 2 - metrics.stringWidth(xLabel) / 2, coord.y(ymin) + metrics.getHeight() * 2);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        BasicStroke thin=new BasicStroke(1.0f);
        BasicStroke thick=new BasicStroke(2.0f);
        //Draw x and y axis
        g2.setPaint(Color.black);
        g2.setStroke(thick);
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));

        g2.drawLine(coord.x(xmin), coord.y(ymax), coord.x(xmax), coord.y(ymax));
        g2.drawLine(coord.x(xmax), coord.y(ymax), coord.x(xmax), coord.y(ymin));

        g2.setStroke(thin);

        drawTicmarks(g2);

        //Draw the bars and cumulative curve
        double prevX=xmin,prevY=ymin;
        double cumY=ymin; //cumulative y
        for(int i=0;i<bars.length;i++){
            if(x2==null)
            g2.setColor(colors[i]);
            g2.fill(bars[i]);
            g2.setPaint(Color.black);
            cumY+=yValue[x1_levels.length-1-i]/freqSum*100;
            if(i>0)
            g2.drawLine(coord.x(prevX), coord.y(prevY),coord.x(prevX+1),coord.y(cumY));
            prevX++;
            prevY=cumY;
        }

        if(focusedBar!=null){
            g2.setPaint(Color.black);
            g2.draw(focusedBar);

            int tmpX=(int)focusedBar.getX();
            int tmpY=(int)focusedBar.getY()-5;
            nf.setMaximumFractionDigits(2);
            if(x2!=null){
                int x1_index=focusedID/x2_levels.length;
                int x2_index=focusedID%x2_levels.length;
                g2.drawString(nf.format(freqs[x1_index][x2_index]), tmpX, tmpY);
            }
            else{
                g2.drawString(nf.format(yValue[x1_levels.length-1-focusedID]/freqSum*100)+"%", tmpX, tmpY);
            }
        }

    }

}
