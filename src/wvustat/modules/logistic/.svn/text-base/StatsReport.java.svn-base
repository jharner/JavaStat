/*
 * StatsReport.java
 *
 * Created on January 3, 2002, 12:21 PM
 */

package wvustat.modules.logistic;

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;
import java.rmi.RemoteException;

import wvustat.interfaces.*;
import wvustat.dist.Chi2Dist;
/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class StatsReport extends JPanel {
    public static final int WHOLE_MODEL_REPORT=0;
    public static final int PARAMETER_REPORT=1;
    private RegressionSolver solver;
    //the following variables are used for render the reports in
    // a table like structure
    private int vgap=4, hgap=12,headerHeight=24, cellWidth=60, cellHeight=20;
    private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
    private Font boldFont=new Font("Monospaced", Font.BOLD, 12);
    private Insets insets=new Insets(8,8,4,4);

    private double L0, L1, D, Chi2, Rsquare, prob1;
    private double stdErr1, stdErr2, prob2, prob3;
    private String xVarName;

    private int rptIndex=0;
    /** Creates new StatsReport */
    public StatsReport(RegressionSolver solver) {
        this.solver=solver;
        this.setBackground(Color.white);
        measure();
        compute();
    }

    public void setReportIndex(int index){
        this.rptIndex=index;
        repaint();
    }

    private void measure(){

        FontMetrics metrics1=this.getFontMetrics(basicFont);
        FontMetrics metrics2=this.getFontMetrics(boldFont);
        if(metrics1==null)
        return;
        headerHeight=metrics2.getMaxAscent();
        cellHeight=metrics1.getMaxAscent();
        if(rptIndex==0)
        cellWidth=metrics1.stringWidth("Difference");
        else
        cellWidth=metrics1.stringWidth("Intercept");
    }

    public void updateReport(){
        compute();
        repaint();
    }
    
    private void compute(){

        L0=-1*solver.logLikelihoodForNull();
        L1=-1*solver.logLikelihood();
        D=L0-L1;
        Chi2=2*D;

        Rsquare=1-L1/L0;

        double[] errs=solver.getStdErrors();
        stdErr1=errs[0];
        stdErr2=errs[1];

        Chi2Dist chi=new Chi2Dist(1);
        prob1=1-chi.cdf(Chi2);
        prob2=1-chi.cdf(Math.pow(solver.getBeta0()/stdErr1, 2));
        prob3=1-chi.cdf(Math.pow(solver.getBeta1()/stdErr2, 2));

    }

    public void setXVarName(String str){
        this.xVarName=str;
        if(xVarName!=null && xVarName.length()>9){
            xVarName=xVarName.substring(0,6);
            xVarName+="...";
        }
    }

    public Dimension getSize(){
        int w=insets.left+insets.right;
        int h=insets.top+insets.bottom+headerHeight;

        h+=6*cellHeight+5*vgap;

        w+=5*cellWidth+4*hgap;

        return new Dimension(w,h);
    }

    public Dimension getPreferredSize(){
        return new Dimension(420,120);
    }
    
    public Dimension getMinimumSize(){
        return getSize();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        FontMetrics metrics1=this.getFontMetrics(basicFont);
        FontMetrics metrics2=this.getFontMetrics(boldFont);

        int x=insets.left, y=insets.top+metrics2.getAscent();
        g2.setFont(boldFont);

        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(5);


        if(rptIndex==0){
            g2.drawString("Model", x, y);
            x+=2*cellWidth+hgap;
            rightJustify(g2, metrics2, "-L", x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics2,"DF", x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics2,"ChiSquare", x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics2,"Prob>ChiSq", x, y);

            x=insets.left;
            y+=vgap+metrics1.getMaxAscent();
            g2.setFont(basicFont);
            g2.drawString("Difference", x, y);
            x+=cellWidth+hgap+cellWidth;
            rightJustify(g2, metrics1, nf.format(D), x, y);
            x+=hgap+cellWidth;
            rightJustify(g2, metrics1, "1", x, y);
            x+=hgap+cellWidth;
            rightJustify(g2, metrics1, nf.format(Chi2), x, y);
            x+=hgap+cellWidth;
            rightJustify(g2, metrics1, nf.format(prob1),x, y);

            x=insets.left;
            y+=vgap+metrics1.getMaxAscent();
            g2.drawString("Full", x, y);
            x+=cellWidth+hgap+cellWidth;
            rightJustify(g2, metrics1, nf.format(L1), x, y);

            x=insets.left;
            y+=vgap+metrics1.getMaxAscent();
            g2.drawString("Reduced", x, y);
            x+=hgap+cellWidth*2;
            rightJustify(g2,metrics1, nf.format(L0), x, y);

            x=insets.left;
            y+=2*vgap+2*metrics1.getMaxAscent();

            g2.drawString("R Square", x, y);
            x+=hgap+cellWidth*2;
            rightJustify(g2, metrics1, nf.format(1-L1/L0), x, y);

            x=insets.left;
            y+=vgap+metrics1.getMaxAscent();
            g2.drawString("Observations", x, y);
            x+=cellWidth*2+hgap;
            rightJustify(g2, metrics1, String.valueOf(solver.getObservationCount()), x, y);
        }
        else{
            g2.drawString("Term", x, y);
            x+=2*cellWidth+hgap;
            rightJustify(g2, metrics2, "Estimate", x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics2, "Std Err", x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics2, "ChiSquare", x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics2, "Prob>ChiSq", x, y);

            g2.setFont(basicFont);
            x=insets.left;
            y+=vgap+metrics1.getMaxAscent();
            g2.drawString("Intercept", x, y);
            x+=2*cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(solver.getBeta0()), x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(stdErr1), x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(Math.pow(solver.getBeta0()/stdErr1,2)),x,y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(prob2), x, y);

            x=insets.left;
            y+=vgap+metrics1.getMaxAscent();
            g2.drawString(xVarName, x, y);
            x+=2*cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(solver.getBeta1()), x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(stdErr2), x, y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(Math.pow(solver.getBeta1()/stdErr2,2)),x,y);
            x+=cellWidth+hgap;
            rightJustify(g2, metrics1, nf.format(prob3), x, y);
        }
    }

    private void rightJustify(Graphics g, FontMetrics metrics, String str, int x, int y){
        int wid=metrics.stringWidth(str);
        g.drawString(str, x-wid, y);
    }

}