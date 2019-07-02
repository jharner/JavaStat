/*
 * EWMARpt.java
 *
 * Created on September 22, 2000, 10:21 AM
 */

package wvustat.modules.ControlChart;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import wvustat.modules.Report;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
/**
 *
 * @author  hxue
 * @version
 */
public class EWMARpt extends Report implements MouseListener{

    protected ControlChart chart;
    protected double lambda;
    protected int height;
    protected FontMetrics metrics;
    protected Insets insets=new Insets(20,20,20,20);
    protected GeneralPath leftArrow, rightArrow;
    protected Rectangle2D kRect;
    protected int arrowLen=10;
    protected java.text.NumberFormat nf;
    /** Creates new EWMARpt */
    public EWMARpt(ControlChart chart) {
        this.chart=chart;
        lambda=chart.getLambda();
        metrics=getFontMetrics(getFont());
        height=insets.top+insets.bottom+metrics.getMaxAscent();
        setPreferredSize(new Dimension(150,height));
        setMinimumSize(new Dimension(150,height));
        setBackground(Color.white);

        defineHotSpots();
        addMouseListener(this);
 
    }

    protected void defineHotSpots(){
        int x0=insets.left;
        int y0=insets.top+metrics.getMaxAscent();
        int arrowHt=metrics.getMaxAscent();

        x0+=metrics.stringWidth("Lambda: ");

        leftArrow=new GeneralPath();
        leftArrow.moveTo(x0,y0-arrowHt/2.0f);
        leftArrow.lineTo(x0+arrowLen,y0-arrowHt);
        leftArrow.lineTo(x0+arrowLen,y0);
        leftArrow.closePath();

        x0+=arrowLen;
        nf=java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        kRect=new Rectangle2D.Float(x0,y0-metrics.getMaxAscent(),metrics.stringWidth(" "+nf.format(lambda)+" "),metrics.getHeight());

        x0+=(int)kRect.getWidth();

        rightArrow=new GeneralPath();
        rightArrow.moveTo(x0,y0-arrowHt);
        rightArrow.lineTo(x0+arrowLen, y0-arrowHt/2.0f);
        rightArrow.lineTo(x0,y0);
        rightArrow.closePath();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;

        int x0=insets.left;
        int y0=insets.top+metrics.getMaxAscent();

        g2.drawString("Lambda: ",x0,y0);
        x0+=metrics.stringWidth("Lambda: ");
        g2.setPaint(Color.blue);
        nf.setMaximumFractionDigits(2);
        g2.drawString(" "+nf.format(lambda)+" ", x0+arrowLen, y0);
        g2.fill(leftArrow);
        g2.fill(rightArrow);
    }

    public void setGroup(int index){
    }

    public void updateReport(){
    }


    public void mouseReleased(final java.awt.event.MouseEvent p1) {
    }
    public void mouseEntered(final java.awt.event.MouseEvent p1) {
    }
    public void mouseClicked(final java.awt.event.MouseEvent p1) {
        Point pt=p1.getPoint();

        if(leftArrow.contains(pt) && lambda>0.01){
            lambda-=0.01;
            chart.setLambda(lambda);
            defineHotSpots();
            repaint();
        }
        else if(rightArrow.contains(pt)){
            lambda+=0.01;
            chart.setLambda(lambda);
            defineHotSpots();
            repaint();
        }
        else if(kRect.contains(pt)){
            String input=JOptionPane.showInputDialog(this,"Please input the new lambda:","Input Lambda",JOptionPane.INFORMATION_MESSAGE);
            try{
                double tmpD=Double.parseDouble(input);
                lambda=tmpD;
                chart.setLambda(lambda);
                defineHotSpots();
                repaint();
            }
            catch(NumberFormatException nfe){
            }
        }

    }
    public void mousePressed(final java.awt.event.MouseEvent p1) {
    }
    public void mouseExited(final java.awt.event.MouseEvent p1) {
    }
}
