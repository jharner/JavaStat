package wvustat.modules;

import java.awt.*;
import java.rmi.*;
import java.util.Vector;
import java.text.NumberFormat;

import wvustat.interfaces.*;

/**
 *	RegressionRpt is a component that displays the regression statistics for a particular
 *	data set in a table like structure.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Feb. 15, 2000
 */
public class RegressionRpt extends Report{
    public static final int REGRESSION=0;
    public static final int CORRELATION=1;
    

    private DataSet data;

    private Variable y_var, x_var, z_var;
    private Vector vz;

    private EqualCountPolicy policy;

    private double[] regStat, oldStat; //regression stat for original and current data

    private int currentIndex=0;
    private String groupName;
    private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
    private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
    private FontMetrics metrics;
    private Insets insets=new Insets(8,20,8,8);

    private int rptType=REGRESSION;

    private boolean firstRun=true;

    /**
     * Constructor
     * Creates a new RegressionRpt with the given data set and its variables.
     */
    public RegressionRpt(DataSet data, Variable y_var, Variable x_var, Vector vz, EqualCountPolicy policy){

        this.data=data;
        this.y_var=y_var;
        this.x_var=x_var;
        this.vz = vz;
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);

        this.policy=policy;

        setBackground(Color.white);

        metrics=getFontMetrics(basicFont);

        rowHeader=metrics.stringWidth("Corr. Coeff.:");
        cellWidth=metrics.stringWidth("0000.0000");
        cellHeight=metrics.getMaxAscent();

        int w,h;
        if(z_var!=null)
        h=insets.top+insets.bottom+6*(vgap+cellHeight)-vgap;
        else
        h=insets.top+insets.bottom+5*(vgap+cellHeight)-vgap;

        w=insets.left+insets.right+rowHeader+hgap+cellWidth;

        setSize(new Dimension(w,h));
        setPreferredSize(new Dimension(w,h));

        initGroupStat();
    }

    /**
     *	This method sets the new regression statistics to display as report.
     *
     *	@param: stat: the new regression statistics
     *	@param: changeOriginal: true if the original stat should be changed, false otherwise.
     */
    public void setRegressionStatistics(double[] stat, boolean changeOriginal){
        regStat=stat;
        if(changeOriginal)
        oldStat=regStat;

        if(!firstRun)
        repaint();
    }

    /**
     * Show different type of report (based on the model, "regression" or "correlation"
     */
    public void showReport(int report_type){
        if(report_type==REGRESSION){
            rptType=REGRESSION;
            repaint();
        }
        else if(report_type==CORRELATION){
            rptType=CORRELATION;
            repaint();
        }
    }

    /**
     * Reset the report to its original state
     */
    public void resetReport(){
        regStat=oldStat;
        repaint();
    }

    /**
     * this method actually only gets the group name
     */
    private void initGroupStat(){
        if(z_var==null) return;

        
        /*if(z_var.getType()==Variable.NUMERIC){
            double d1=policy.getLowerLimit(currentIndex);
            double d2=policy.getUpperLimit(currentIndex);
            NumberFormat formatter=NumberFormat.getInstance();
            formatter.setMaximumFractionDigits(1);
            groupName=formatter.format(d1)+"-"+formatter.format(d2);
        }
        else*/
        groupName=policy.getGroupName(currentIndex);
        
    }

    /**
     * Set the current group to be the indexed group
     */
    public void setGroup(int index){
        if(z_var==null) return;

        currentIndex=index;
        initGroupStat();
        oldStat=regStat;
        repaint();

    }

    /**
     * update the report to reflect changes in the data set
     */
    public void updateReport(){
        initGroupStat();
        repaint();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        g2.setFont(basicFont);
        metrics=g2.getFontMetrics();
        
        NumberFormat formatter=NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(3);

        //get the position where we should start to render text
        int ma=metrics.getMaxAscent();
        int x0=insets.left, y0=insets.top+ma;

        if(z_var!=null){
            g2.drawString("for "+groupName, x0, y0);
            y0+=vgap+ma;
        }

        //x0+=rowHeader+hgap;
        //g2.drawString("Current", x0, y0);
        //x0+=cellWidth+hgap;
        //g2.drawString("Original", x0, y0);
        //y0+=vgap+ma;
        x0=insets.left;

        if(rptType==REGRESSION){
            //render the row headers
            g2.drawString("Intercept:", x0, y0);
            y0+=vgap+ma;
            g2.drawString("Slope:", x0, y0);
            y0+=vgap+ma;
            g2.drawString("R square:", x0, y0);
            y0+=vgap+ma;
            g2.drawString("P-value:", x0, y0);
            y0+=vgap+ma;
            g2.drawString("SE of b1:",x0,y0);
            y0+=vgap+ma;

            x0+=rowHeader+hgap;

            if(z_var!=null)
            y0=insets.top+2*(ma+vgap)-vgap;
            else
            y0=insets.top+ma;

            g2.drawString(formatter.format(regStat[0]), x0, y0); //intercept
            y0+=vgap+ma;
            g2.drawString(formatter.format(regStat[1]), x0, y0);//slope
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(4);
            g2.drawString(formatter.format(regStat[2]*regStat[2]), x0, y0); //R square
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(6);
            if(regStat[4]>1e-6)
                g2.drawString(formatter.format(regStat[4]),x0,y0);
            else
                g2.drawString("< 1E-6", x0,y0);
            
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(4);
            g2.drawString(formatter.format(regStat[3]),x0,y0);
            y0+=vgap+ma;

            //render the second column
            /*
            x0+=hgap+cellWidth;
            if(z_var!=null)
            y0=insets.top+3*(ma+vgap)-vgap;
            else
            y0=insets.top+ma+vgap+ma;

            formatter.setMaximumFractionDigits(3);
            g2.drawString(formatter.format(oldStat[0]), x0, y0);
            y0+=vgap+ma;
            g2.drawString(formatter.format(oldStat[1]), x0, y0);
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(4);
            g2.drawString(formatter.format(oldStat[2]*oldStat[2]), x0, y0);
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(6);
            g2.drawString(formatter.format(oldStat[4]), x0, y0);
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(4);
            g2.drawString(formatter.format(oldStat[3]),x0,y0);
            y0+=vgap+ma;
             */
        }
        else{
            //render the row headers
            g2.drawString("R square:", x0, y0);
            y0+=vgap+ma;
            g2.drawString("Corr. Coeff.:", x0, y0);
            y0+=vgap+ma;
            g2.drawString("P-value:", x0, y0);
            y0+=vgap+ma;


            x0+=rowHeader+hgap;
            if(z_var!=null)
            y0=insets.top+2*(ma+vgap)-vgap;
            else
            y0=insets.top+ma;

            formatter.setMaximumFractionDigits(4);
            g2.drawString(formatter.format(regStat[2]*regStat[2]), x0, y0); //R square
            y0+=vgap+ma;
            g2.drawString(formatter.format(regStat[2]),x0,y0);
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(6);
            if(regStat[4]>1E-6)
                g2.drawString(formatter.format(regStat[4]),x0,y0);
            else
                g2.drawString("< 1E-6",x0,y0);
            
            y0+=vgap+ma;

            //render the second column
            /*
            x0+=hgap+cellWidth;
            if(z_var!=null)
            y0=insets.top+3*(ma+vgap)-vgap;
            else
            y0=insets.top+ma+vgap+ma;

            formatter.setMaximumFractionDigits(4);
            g2.drawString(formatter.format(oldStat[2]*oldStat[2]), x0, y0);
            y0+=vgap+ma;
            g2.drawString(formatter.format(oldStat[2]), x0, y0);
            y0+=vgap+ma;

            formatter.setMaximumFractionDigits(6);
            g2.drawString(formatter.format(oldStat[4]),x0,y0);
            y0+=vgap+ma;
            */
        }

        firstRun=false;

    }
}




