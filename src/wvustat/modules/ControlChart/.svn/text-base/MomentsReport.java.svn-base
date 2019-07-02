/*
 * MomentsReport.java
 *
 * Created on September 13, 2000, 10:41 AM
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
 * @author  Hengyi Xue
 * @version
 */
public class MomentsReport extends Report implements MouseListener{

    protected double K;
    protected int height;
    protected FontMetrics metrics;
    protected Insets insets=new Insets(20,20,20,20);
    protected ControlChart chart;
    protected GeneralPath leftArrow, rightArrow;
    protected Rectangle2D kRect;
    protected int arrowLen=10;
    /** Creates new MomentsReport */
    public MomentsReport(ControlChart chart) {
        this.chart=chart;
        K=chart.getK();
        metrics=getFontMetrics(getFont());
        height=insets.top+insets.bottom+3*metrics.getMaxAscent();
        setPreferredSize(new Dimension(150,height));
        setMinimumSize(new Dimension(150,height));
        setBackground(Color.white);

        defineHotSpots();
        addMouseListener(this);
    }

    public void defineHotSpots(){
        int arrowHt=metrics.getMaxAscent();
        int x0=insets.left+metrics.stringWidth("Limits: ");
        int y0=insets.top+2*metrics.getHeight()+metrics.getMaxAscent()+4;

        leftArrow=new GeneralPath();
        leftArrow.moveTo(x0,y0-arrowHt/2.0f);
        leftArrow.lineTo(x0+arrowLen,y0-arrowHt);
        leftArrow.lineTo(x0+arrowLen,y0);
        leftArrow.closePath();

        x0+=arrowLen;
        kRect=new Rectangle2D.Float(x0,y0-metrics.getHeight(),metrics.stringWidth(" "+K+" "),metrics.getHeight());

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
        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);

        int x0=insets.left,y0=insets.top+metrics.getMaxAscent();
        g2.drawString("Mean: "+nf.format(chart.getMean()),x0,y0);
        y0+=metrics.getHeight();
        g2.drawString("Std Dev: "+nf.format(chart.getStdDev()), x0, y0);
        y0+=metrics.getHeight();
        /*
        g2.drawString("Limits: ",x0,y0);
        x0+=metrics.stringWidth("Limits: ");
        g2.drawString(" standard errors",x0+2*arrowLen+(int)kRect.getWidth(),y0);

        g2.setPaint(Color.blue);
        g2.drawString(" "+K+" ",x0+arrowLen,y0);

        g2.fill(leftArrow);
        g2.fill(rightArrow);
        */
    }


    public void setGroup(int index){
    }

    public void updateReport(){
        repaint();
    }

    public void mouseReleased(final java.awt.event.MouseEvent p1) {
    }
    public void mouseEntered(final java.awt.event.MouseEvent p1) {
    }
    public void mouseClicked(final java.awt.event.MouseEvent p1) {
        Point pt=p1.getPoint();
        if(chart.getDisplayOption()==ControlChart.SHOW_INDIVIDUAL|| chart.getDisplayOption()==ControlChart.SHOW_MEAN){
            if(leftArrow.contains(pt) && K>1){
                K--;
                chart.setK(K);
                defineHotSpots();
                repaint();
            }
            else if(rightArrow.contains(pt)){
                K++;
                chart.setK(K);
                defineHotSpots();
                repaint();
            }
            else if(kRect.contains(pt)){
                String input=JOptionPane.showInputDialog(this,"Please input the new k:","Input k",JOptionPane.INFORMATION_MESSAGE);
                try{
                    double tmpD=Double.parseDouble(input);
                    K=tmpD;
                    chart.setK(K);
                    defineHotSpots();
                    repaint();
                }
                catch(NumberFormatException nfe){
                }
            }
        }
    }
    public void mousePressed(final java.awt.event.MouseEvent p1) {
    }
    public void mouseExited(final java.awt.event.MouseEvent p1) {
    }
 }
