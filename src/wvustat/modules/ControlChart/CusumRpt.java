/*
 * CusumRpt.java
 *
 * Created on September 22, 2000, 11:09 AM
 */

package wvustat.modules.ControlChart;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import wvustat.modules.Report;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

/**
 *
 * @author  Hengyi Xue
 * @version 1.0, Sep. 22, 2000
 */
public class CusumRpt extends Report implements MouseListener {

    protected ControlChart chart;
    protected double alpha, beta, delta;
    protected int height;
    protected FontMetrics metrics;
    protected Insets insets=new Insets(20,20,20,20);
    protected GeneralPath[] leftArrows, rightArrows;
    protected Rectangle2D[] rects;
    protected int arrowLen=10;
    protected java.text.NumberFormat nf;
    protected int blankSpace=24;
    protected double aIncre=0.01, bIncre=0.01,dIncre=0.1;
    /** Creates new CusumRpt */
    public CusumRpt(ControlChart chart) {
        this.chart=chart;
        alpha=chart.getAlpha();
        beta=chart.getBeta();
        delta=chart.getDelta();
        metrics=getFontMetrics(getFont());
        height=insets.top+insets.bottom+metrics.getMaxAscent();
        setPreferredSize(new Dimension(150,height));
        setMinimumSize(new Dimension(150,height));
        setBackground(Color.white);

        leftArrows=new GeneralPath[3];
        rightArrows=new GeneralPath[3];
        rects=new Rectangle2D[3];
        nf=java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        
        defineHotSpots();
        addMouseListener(this);

    }

    protected void defineHotSpots(){
        int x0=insets.left+metrics.stringWidth("Alpha: ");
        int y0=insets.top+metrics.getMaxAscent();
        int arrowHt=metrics.getMaxAscent();

        leftArrows[0]=new GeneralPath();
        leftArrows[0].moveTo(x0,y0-arrowHt/2.0f);
        leftArrows[0].lineTo(x0+arrowLen,y0-arrowHt);
        leftArrows[0].lineTo(x0+arrowLen,y0);
        leftArrows[0].closePath();

        x0+=arrowLen;
        rects[0]=new Rectangle2D.Float(x0,y0-metrics.getMaxAscent(), metrics.stringWidth(" "+nf.format(alpha)+" "), metrics.getMaxAscent());

        x0+=(int)rects[0].getWidth();
        rightArrows[0]=new GeneralPath();
        rightArrows[0].moveTo(x0,y0-arrowHt);
        rightArrows[0].lineTo(x0+arrowLen, y0-arrowHt/2.0f);
        rightArrows[0].lineTo(x0,y0);
        rightArrows[0].closePath();

        x0+=arrowLen+blankSpace+metrics.stringWidth("Beta: ");

        leftArrows[1]=new GeneralPath();
        leftArrows[1].moveTo(x0,y0-arrowHt/2.0f);
        leftArrows[1].lineTo(x0+arrowLen,y0-arrowHt);
        leftArrows[1].lineTo(x0+arrowLen,y0);
        leftArrows[1].closePath();

        x0+=arrowLen;
        rects[1]=new Rectangle2D.Float(x0,y0-metrics.getMaxAscent(), metrics.stringWidth(" "+nf.format(beta)+" "), metrics.getMaxAscent());

        x0+=(int)rects[1].getWidth();
        rightArrows[1]=new GeneralPath();
        rightArrows[1].moveTo(x0,y0-arrowHt);
        rightArrows[1].lineTo(x0+arrowLen, y0-arrowHt/2.0f);
        rightArrows[1].lineTo(x0,y0);
        rightArrows[1].closePath();

        x0+=arrowLen+blankSpace+metrics.stringWidth("Delta: ");

        leftArrows[2]=new GeneralPath();
        leftArrows[2].moveTo(x0,y0-arrowHt/2.0f);
        leftArrows[2].lineTo(x0+arrowLen,y0-arrowHt);
        leftArrows[2].lineTo(x0+arrowLen,y0);
        leftArrows[2].closePath();

        x0+=arrowLen;
        rects[2]=new Rectangle2D.Float(x0,y0-metrics.getMaxAscent(), metrics.stringWidth(" "+nf.format(delta)+" "), metrics.getMaxAscent());

        x0+=(int)rects[2].getWidth();
        rightArrows[2]=new GeneralPath();
        rightArrows[2].moveTo(x0,y0-arrowHt);
        rightArrows[2].lineTo(x0+arrowLen, y0-arrowHt/2.0f);
        rightArrows[2].lineTo(x0,y0);
        rightArrows[2].closePath();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int x0=insets.left;
        int y0=insets.top+metrics.getMaxAscent();

        Graphics2D g2=(Graphics2D)g;
        g2.drawString("Alpha: ", x0, y0);

        g2.setPaint(Color.blue);
        x0+=metrics.stringWidth("Alpha: ")+arrowLen;
        g2.drawString(" "+nf.format(alpha)+" ",x0,y0);

        g2.setPaint(Color.black);
        x0+=arrowLen+(int)rects[0].getWidth()+blankSpace;
        g2.drawString("Beta: ", x0, y0);

        g2.setPaint(Color.blue);
        x0+=metrics.stringWidth("Beta: ")+arrowLen;
        g2.drawString(" "+nf.format(beta)+" ",x0,y0);

        g2.setPaint(Color.black);
        x0+=arrowLen+rects[1].getWidth()+blankSpace;
        g2.drawString("Delta: ", x0, y0);

        g2.setPaint(Color.blue);
        x0+=metrics.stringWidth("Delta: ")+arrowLen;
        g2.drawString(" "+nf.format(delta)+" ",x0,y0);

        g2.setPaint(Color.blue);
        for(int i=0;i<leftArrows.length;i++){
            g2.fill(leftArrows[i]);
            g2.fill(rightArrows[i]);
        }

    }
    public void mouseReleased(final java.awt.event.MouseEvent p1) {
    }
    public void mouseEntered(final java.awt.event.MouseEvent p1) {
    }
    public void mouseClicked(final java.awt.event.MouseEvent p1) {
        Point pt=p1.getPoint();
        int lIndex=-1,rIndex=-1;
        for(int i=0;i<leftArrows.length;i++){
            if(leftArrows[i].contains(pt)){
                lIndex=i;
                break;
            }
            if(rightArrows[i].contains(pt)){
                rIndex=i;
                break;
            }
        }

        switch(lIndex){
            case 0:
            alpha-=aIncre;
            chart.setAlpha(alpha);
            break;
            case 1:
            beta-=bIncre;
            chart.setBeta(beta);
            break;
            case 2:
            delta-=dIncre;
            chart.setDelta(delta);
            break;
        }

        switch(rIndex){
            case 0:
            alpha+=aIncre;
            chart.setAlpha(alpha);
            break;
            case 1:
            beta+=bIncre;
            chart.setBeta(beta);
            break;
            case 2:
            delta+=dIncre;
            chart.setDelta(delta);
            break;
        }

        if(lIndex!=-1 || rIndex!=-1){
            defineHotSpots();
            repaint();
            return;
        }
        
        int index=-1;
        double tmpD=0;
        boolean inputValid=true;
        for(int i=0;i<rects.length;i++){
            if(rects[i].contains(pt)){
                index=i;
                String input=JOptionPane.showInputDialog(this,"Please input the new lambda:","Input Lambda",JOptionPane.INFORMATION_MESSAGE);
                try{
                    tmpD=Double.parseDouble(input);
                    inputValid=(tmpD>0);
                }
                catch(NumberFormatException nfe){
                    inputValid=false;
                }
                break;
            }
        }
        
        if(!inputValid)
            return;
        
        switch(index){
            case 0:
            alpha=tmpD;
            chart.setAlpha(alpha);
            break;
            case 1:
            beta=tmpD;
            chart.setBeta(beta);
            break;
            case 2:
            delta=tmpD;
            chart.setDelta(delta);
            break;
        }
        
        defineHotSpots();
        repaint();       

    }
    public void mousePressed(final java.awt.event.MouseEvent p1) {
    }
    public void mouseExited(final java.awt.event.MouseEvent p1) {
    }

    public void setGroup(int index){
    }

    public void updateReport(){
    }
}
