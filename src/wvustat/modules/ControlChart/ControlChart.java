/*
 * ControlChart.java
 *
 * Created on August 28, 2000, 11:27 AM
 */

package wvustat.modules.ControlChart;

import java.awt.*;
import java.awt.geom.*;
import java.text.NumberFormat;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import wvustat.interfaces.*;
import wvustat.modules.CoordConverter;
import wvustat.modules.BaseAxisModel;
import wvustat.modules.StatEngineImpl;
/**
 * ControlChart implements a statistical control chart.
 *
 * @author  Hengyi Xue
 * @version
 */
public class ControlChart extends JPanel implements MouseListener{
    
    public static final String[] display_options={"Mean", "Individual","R", "S","EWMA","Cusum"};
    
    public static final int SHOW_MEAN=0;
    public static final int SHOW_INDIVIDUAL=1;
    public static final int R_CHART=2;
    public static final int S_CHART=3;
    public static final int EWMA=4;
    public static final int CUSUM=5;
    
    protected int display_option=SHOW_MEAN;
    protected DataSet data;
    
    protected Variable y_var, x_var;
    
    protected int width=250, height=220;
    
    protected FontMetrics metrics;
    
    protected Insets insets=new Insets(40,30,60,70);
    
    protected NumberFormat nf;
    
    protected CoordConverter coord;
    
    protected Ellipse2D[] points;
    
    protected Ellipse2D focusedPt;
    
    protected Vector varNames;
    
    protected int focusedID;
    
    protected double[] yvalues;
    protected String[] x_levels;
    protected double[] means;
    protected double[][] freqs, sums;
    protected double ymin, ymax,xmin, xmax, ymean;
    protected double overall_stdDev, among_stdDev;
    protected double maxSigma;
    protected double maxFreq; //The size of the biggest group.
    
    protected double[] sigmas; //We need an array here to deal with unequal group size
    protected double[] onDisplay; //This stores the array of values being displayed.
    protected double[] R,S; //Range and within group standard deviation of each group
    protected double R_bar, R_min=Double.MAX_VALUE; //The mean and min of ranges
    protected double S_bar, S_min=Double.MAX_VALUE;
    protected BaseAxisModel ytm,xtm;
    
    protected double UCL, LCL;
    
    protected double K=3.0; //how many sigmas the limits are based on.
    protected ConstantsTable consts;
    
    protected double B3, B4,D3, D4;
    
    //variables used in 'EWMA' chart
    protected double[] Z;
    protected double[] sigmaZ; //variance of z
    protected double lambda=0.2;
    protected double L=3;
    
    //variables used in 'Cusum' chart
    protected double[] C;
    protected double alpha=0.05, beta=0.05, delta=1.0;
    protected double tan, d;
    
    protected GeneralPath[] leftArrows, rightArrows;
    protected Rectangle2D[] rects;
    protected int arrowLen=12, blankSpace=30;
    private double aIncre=0.01, bIncre=0.01, dIncre=0.1;
    
    /** Creates new ControlChart */
    public ControlChart(DataSet data, Variable x_var, Variable y_var) {
        this.data=data;
        this.x_var=x_var;
        this.y_var=y_var;
        
        init();
    }
    
    protected void init(){
        retrieveData();
        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
        addMouseListener(this);

        varNames = new Vector();


        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());

        metrics = getFontMetrics(getFont());
        nf=NumberFormat.getInstance();
        
        consts=new ConstantsTable();
        computeStats();
        
        this.setLayout(null);
        
        Component comp=null;
        switch(display_option){
            case EWMA:
                comp=new EWMARpt(this);
                break;
            case CUSUM:
                comp=new CusumRpt(this);
                break;
                default:
        }
        
        if(comp!=null){
            this.add(comp);
            comp.setBounds(0,height-insets.bottom, width,insets.bottom);
            this.validate();
        }
    }
    
    protected void retrieveData(){
        
        x_levels=x_var.getDistinctCatValues();
            
        /* modified by djluo 10/12/2005 to fix bug in handling mask 
        yvalues=y_var.getNumValues();
        */
            
        Vector unmask_y = new Vector();
        for(int i = 0; i< y_var.getSize(); i++){
        	if(!data.getMask(i)) unmask_y.add((Double)y_var.getValue(i));
        }
        yvalues=new double[unmask_y.size()];
        for(int i=0; i<yvalues.length; i++)
        	yvalues[i]=((Double)unmask_y.get(i)).doubleValue();
            
        /* modify end */
            
        sums=data.getSumMatrix(y_var.getName(),x_var.getName(),x_var.getName(),null,null);
        freqs=data.getFreqMatrix(x_var.getName(), x_var.getName());
            
        means=new double[x_levels.length];
        R=new double[x_levels.length];
        S=new double[x_levels.length];
        Z=new double[yvalues.length];
        sigmaZ=new double[yvalues.length];
        C=new double[yvalues.length];
            
        for(int i=0;i<means.length;i++){
            means[i]=sums[i][i]/freqs[i][i];
        }
            
        
    }
    
    protected void computeStats(){
        StatEngine se=new StatEngineImpl();
        
        try{
            ymean=se.getMean(yvalues);
            
            Vector v=new Vector();
            v.addElement(x_var.getName());
            v.addElement(y_var.getName());
            int[] ranks=data.getUnmaskedRanks(v);
            double[] sorted=new double[yvalues.length];
            for(int i=0;i<sorted.length;i++){
                sorted[ranks[i]]=yvalues[i];
            }
            
            overall_stdDev=se.getStdDev(yvalues);

            double amongSS=0, totalR=0, withinSS=0, totalS=0;
            int k=0, j=0;
            for(int i=0;i<sorted.length;i++){
                withinSS+=(sorted[i]-means[k])*(sorted[i]-means[k]);
                if((i+1)-j==freqs[k][k]){
                    j+=freqs[k][k];
                    //Compute range value for each group
                    R[k]=sorted[i]-sorted[(int)(i+1-freqs[k][k])];
                    totalR+=R[k];
                    if(R[k]<R_min)
                        R_min=R[k];
                    //Compute within group std dev
                    S[k]=Math.sqrt(withinSS/(freqs[k][k]-1));
                    
                    totalS+=S[k];
                    if(S[k]<S_min)
                        S_min=S[k];
                    
                    amongSS+=withinSS;
                    withinSS=0;
                    k++;
                }
            }
            
            S_bar=totalS/x_levels.length;
            R_bar=totalR/x_levels.length;
            
            among_stdDev=Math.sqrt(amongSS/(yvalues.length-x_levels.length));
        
            sigmas=new double[x_levels.length];
            for(int i=0;i<sigmas.length;i++){
                sigmas[i]=among_stdDev/Math.sqrt(freqs[i][i]);
                if(sigmas[i]>maxSigma)
                    maxSigma=sigmas[i];
            }
        }
        catch(RemoteException re){
            re.printStackTrace();
            return;
        }
        
        maxFreq=0;
        for(int i=0;i<x_levels.length;i++){
            if(freqs[i][i]>maxFreq)
                maxFreq=freqs[i][i];
        }
        
        D3=consts.getD3((int)maxFreq);
        D4=consts.getD4((int)maxFreq);
        B3=consts.getB3((int)maxFreq);
        B4=consts.getB4((int)maxFreq);
        
        double Z0=ymean;
        Z[0]=lambda*yvalues[0]+(1-lambda)*Z0;  //This is actually Z1 because Java array starts from 0
        for(int i=1;i<Z.length;i++){
            Z[i]=lambda*yvalues[i]+(1-lambda)*Z[i-1];
        }
        
        for(int i=0;i<sigmaZ.length;i++){
            sigmaZ[i]=overall_stdDev*Math.sqrt(lambda/(2-lambda)*(1-Math.pow((1-lambda),2*(i+1))));
        }
        
        C[0]=(yvalues[0]-ymean)/overall_stdDev;
        for(int i=1;i<C.length;i++){
            C[i]=(yvalues[i]-ymean)/overall_stdDev+C[i-1];
        }
        
        
    }
    
    protected void initPlot(){
        switch(display_option){
            case SHOW_INDIVIDUAL:                           
                UCL=ymean+K*overall_stdDev;
                LCL=ymean-K*overall_stdDev;
                ymin=LCL-2.0*overall_stdDev;;
                ymax=UCL+2.0*overall_stdDev;
                onDisplay=yvalues;
                break;
                
            case SHOW_MEAN:
                /*
                UCL=ymean+K*maxSigma;
                LCL=ymean-K*maxSigma;
                ymin=LCL-2.0*maxSigma;
                ymax=UCL+2.0*maxSigma;
                 */
                UCL=ymean+K*overall_stdDev;
                LCL=ymean-K*overall_stdDev;
                ymin=LCL; //-2.0*overall_stdDev;;
                ymax=UCL; //+2.0*overall_stdDev;                
                onDisplay=means;
                break;
                 
            case R_CHART:
                UCL=R_bar*D4;
                LCL=R_bar*D3;
                ymin=LCL-R_min/2;
                ymax=UCL+R_min/2;
                onDisplay=R;
                break;
            case S_CHART:
                UCL=S_bar*B4;
                LCL=S_bar*B3;
                ymin=LCL-S_min/2;
                ymax=UCL+S_min/2;
                onDisplay=S;
                break;
            case EWMA:
                ymin=ymean-(L+0.5)*sigmaZ[sigmaZ.length-1];
                ymax=ymean+(L+0.5)*sigmaZ[sigmaZ.length-1];
                onDisplay=Z;
                break;
            case CUSUM:
                ymin=-10*overall_stdDev;
                ymax=10*overall_stdDev;
                onDisplay=C;
                break;
        }
        
        ytm=new BaseAxisModel(ymin, ymax, 5);
        ymin=ytm.getStartValue();
        ymax=ytm.getEndValue();
        
        xmin=0.5;
        xmax=onDisplay.length+0.5;
        xtm=new BaseAxisModel(xmin, xmax, 5);
        
        coord=new CoordConverter(width,height,xmin,xmax,ymin,ymax,insets);
        
        points=new Ellipse2D[onDisplay.length];
        
        int diam=4;
        for(int i=0;i<points.length;i++){
            points[i]=new Ellipse2D.Float(coord.x(i+1)-diam/2,coord.y(onDisplay[i])-diam/2,diam,diam);
        }
        
        if(display_option==CUSUM){
            leftArrows=new GeneralPath[3];
            rightArrows=new GeneralPath[3];
            rects=new Rectangle2D[3];
        }
        else{
            leftArrows=new GeneralPath[1];
            rightArrows=new GeneralPath[1];
            rects=new Rectangle2D[1];
        }
        
        defineHotSpots();
        
    }
    
    protected void defineHotSpots(){
        FontMetrics metrics=this.getFontMetrics(getFont());
        
        
        if(display_option==CUSUM){
            int x0=4+metrics.stringWidth("Alpha: ");
            int y0=height-metrics.getHeight()+metrics.getMaxAscent();
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
        else{
            String label="Limits: ";
            double value=K;
            if(display_option==EWMA){
                label="Lambda: ";
                value=lambda;
            }
            
            int x0=4;
            int y0=height-metrics.getHeight()+metrics.getMaxAscent();
            int arrowHt=metrics.getMaxAscent();
            
            x0+=metrics.stringWidth(label);
            
            leftArrows[0]=new GeneralPath();
            leftArrows[0].moveTo(x0,y0-arrowHt/2.0f);
            leftArrows[0].lineTo(x0+arrowLen,y0-arrowHt);
            leftArrows[0].lineTo(x0+arrowLen,y0);
            leftArrows[0].closePath();
            
            x0+=arrowLen;
            nf=java.text.NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            rects[0]=new Rectangle2D.Float(x0,y0-metrics.getMaxAscent(),metrics.stringWidth(" "+nf.format(value)+" "),metrics.getHeight());
            
            x0+=(int)rects[0].getWidth();
            
            rightArrows[0]=new GeneralPath();
            rightArrows[0].moveTo(x0,y0-arrowHt);
            rightArrows[0].lineTo(x0+arrowLen, y0-arrowHt/2.0f);
            rightArrows[0].lineTo(x0,y0);
            rightArrows[0].closePath();
        }
    }
    
    
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        initPlot();
        super.setBounds(x,y,w,h);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        double A=(coord.x(1)-coord.x(0))/(coord.y(0)-coord.y(1));
        tan=delta/2.0/A;
        d=2.0/(delta*delta)*Math.log((1-beta)/alpha);
        
        Graphics2D g2=(Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        BasicStroke thin=new BasicStroke(1.0f);
        BasicStroke thick=new BasicStroke(2.0f);
        
        g2.drawLine(0,0,width-1,0);
        
        //Draw x and y axis
        g2.setStroke(thick);
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));
        
        //Draw chart title
        FontMetrics metrics=this.getFontMetrics(getFont());
        String title=ControlChart.display_options[display_option];
        g2.drawString(title, getSize().width/2-metrics.stringWidth(title)/2, metrics.getHeight());
        
        g2.setStroke(thin);
        
        //draw x axis ticmarks
        switch(display_option){
            case SHOW_INDIVIDUAL:
            case EWMA:
            case CUSUM:
                for(int i=1;i<=x_levels.length;i++){
                    int xtmp=(int)(i*freqs[0][0]);
                    if(xtmp<=xmax){
                        String label=String.valueOf(xtmp);
                        g2.drawLine(coord.x(xtmp), coord.y(ymin), coord.x(xtmp), coord.y(ymin)+4);
                        g2.drawString(label, coord.x(xtmp)-metrics.stringWidth(label)/2, coord.y(ymin)+metrics.getHeight());
                    }
                }
                break;
                default:
                    for(int i=0;i<xmax-1;i++){
                        String label=String.valueOf(i+1);
                        g2.drawLine(coord.x(i+1), coord.y(ymin), coord.x(i+1), coord.y(ymin)+4);
                        g2.drawString(label, coord.x(i+1)-metrics.stringWidth(label)/2, coord.y(ymin)+metrics.getHeight());
                    }
                    break;
                    
        }
        
        //Draw y ticmarks
        double tmp=ymin;
        double step=ytm.getInterval();
        while(tmp<=ymax){
            g2.drawLine(coord.x(xmin), coord.y(tmp), coord.x(xmin)-4, coord.y(tmp));
            String label=nf.format(tmp);
            
            int len=metrics.stringWidth(label);
            
            g2.drawString(label, coord.x(xmin)-4-len, coord.y(tmp)+metrics.getMaxAscent()/2);
            
            tmp+=step;
        }


        g2.drawString(y_var.getName(), coord.x(xmin) + 4, coord.y(ymax) - 2);

        String xname = x_var.getName();
        int len = metrics.stringWidth(xname);
        g2.drawString(xname, (width - len) / 2, coord.y(ymin) + 2 * metrics.getHeight());

        //Draw the data points
        for (int i = 0; i < points.length;i++){
            if(display_option==SHOW_MEAN){
                if(onDisplay[i]>ymean+K*sigmas[i] || onDisplay[i]<ymean-K*sigmas[i])
                    g2.setPaint(Color.red);
            }
            else if(display_option==EWMA){
                if(onDisplay[i]>ymean+L*sigmaZ[i] || onDisplay[i]<ymean-L*sigmaZ[i])
                    g2.setPaint(Color.red);
            }
            else if(display_option==CUSUM){
                g2.setPaint(Color.blue);
            }
            else{
                if(onDisplay[i]>UCL || onDisplay[i] <LCL){
                    g2.setPaint(Color.red);
                }
            }
            
            g2.draw(points[i]);
            //Connect points
            g2.setPaint(Color.black);
            if(i>0)
                g2.drawLine((int)points[i-1].getCenterX(),(int)points[i-1].getCenterY(), (int)points[i].getCenterX(), (int)points[i].getCenterY());
        }
        
        //draw upper and lower bounds
        g2.setPaint(Color.red);
        boolean showStats=true;
        switch(display_option){
            case SHOW_MEAN:
                for(int i=0;i<x_levels.length;i++){
                    g2.drawLine(coord.x(i+0.5), coord.y(ymean-K*sigmas[i]),coord.x(i+1.5),coord.y(ymean-K*sigmas[i]));
                    g2.drawLine(coord.x(i+0.5), coord.y(ymean+K*sigmas[i]),coord.x(i+1.5),coord.y(ymean+K*sigmas[i]));
                    if(i>0 && sigmas[i]!=sigmas[i-1]){
                        showStats=false;
                        g2.drawLine(coord.x(i+0.5), coord.y(ymean-K*sigmas[i]), coord.x(i+0.5), coord.y(ymean-K*sigmas[i-1]));
                        g2.drawLine(coord.x(i+0.5), coord.y(ymean+K*sigmas[i]), coord.x(i+0.5), coord.y(ymean+K*sigmas[i-1]));
                    }
                }
                
                break;
            case EWMA:
                for(int i=0;i<sigmaZ.length;i++){
                    g2.drawLine(coord.x(i+0.5), coord.y(ymean-L*sigmaZ[i]),coord.x(i+1.5), coord.y(ymean-L*sigmaZ[i]));
                    g2.drawLine(coord.x(i+0.5), coord.y(ymean+L*sigmaZ[i]),coord.x(i+1.5), coord.y(ymean+L*sigmaZ[i]));
                    if(i>0){
                        g2.drawLine(coord.x(i+0.5), coord.y(ymean-L*sigmaZ[i]),coord.x(i+0.5), coord.y(ymean-L*sigmaZ[i-1]));
                        g2.drawLine(coord.x(i+0.5), coord.y(ymean+L*sigmaZ[i]),coord.x(i+0.5), coord.y(ymean+L*sigmaZ[i-1]));
                    }
                }
                break;
            case CUSUM:
                showStats=false;
                g2.setColor(Color.red);
                //End point for the two limit lines
                double rXe=yvalues.length+d;
                double rYe=0.0;
                if(rXe>xmax){
                    rXe=xmax;
                    rYe=tan*(xmax-yvalues.length-d);
                }
                double rXs=rXe+ymin/tan;
                double rYs=ymin;
                g2.drawLine(coord.x(rXs),coord.y(rYs),coord.x(rXe),coord.y(rYe));
                g2.drawLine(coord.x(rXs),coord.y(-rYs),coord.x(rXe),coord.y(-rYe));
                break;
                default:
                    g2.drawLine(coord.x(xmin), coord.y(LCL), coord.x(xmax), coord.y(LCL));
                    g2.drawLine(coord.x(xmin), coord.y(UCL), coord.x(xmax), coord.y(UCL));
                    break;
        }
        
        if(showStats){
            nf.setMaximumFractionDigits(2);
            switch(display_option){
                case SHOW_INDIVIDUAL:
                case SHOW_MEAN:
                    g2.setPaint(Color.green);
                    g2.drawLine(coord.x(xmin), coord.y(ymean), coord.x(xmax), coord.y(ymean));
                    g2.setPaint(Color.black);
                    String tmpStr=nf.format(ymean);
                    g2.drawString("AVG="+tmpStr, coord.x(xmax), coord.y(ymean));
                    break;
                case R_CHART:
                    g2.setPaint(Color.green);
                    g2.drawLine(coord.x(xmin), coord.y(R_bar), coord.x(xmax), coord.y(R_bar));
                    g2.setPaint(Color.black);
                    g2.drawString("AVG="+nf.format(R_bar),coord.x(xmax), coord.y(R_bar));
                    break;
                case S_CHART:
                    g2.setPaint(Color.green);
                    g2.drawLine(coord.x(xmin), coord.y(S_bar), coord.x(xmax), coord.y(S_bar));
                    g2.setPaint(Color.black);
                    g2.drawString("AVG="+nf.format(S_bar), coord.x(xmax), coord.y(S_bar));
                    break;
                case EWMA:
                case CUSUM:
                    g2.setPaint(Color.green);
                    g2.drawLine(coord.x(xmin), coord.y(ymean), coord.x(xmax), coord.y(ymean));
                    g2.setPaint(Color.black);
                    g2.drawString("AVG="+nf.format(ymean), coord.x(xmax), coord.y(ymean));
                    break;
            }
            
            if(display_option!=EWMA){
                g2.drawString("LCL="+nf.format(LCL), coord.x(xmax), coord.y(LCL));
                g2.drawString("UCL="+nf.format(UCL),coord.x(xmax), coord.y(UCL));
            }
            
        }
        
        int x0=4;
        int y0=height-metrics.getHeight()+metrics.getMaxAscent();
        g2.setColor(Color.black);
        NumberFormat nf=NumberFormat.getInstance();
        switch(display_option){
            case SHOW_MEAN:
            case SHOW_INDIVIDUAL:
                g2.drawString("Limits: ",x0,y0);
                x0+=metrics.stringWidth("Limits: ");
                g2.drawString(" SE",x0+2*arrowLen+(int)rects[0].getWidth(),y0);
                g2.setPaint(Color.blue);
                g2.drawString(" "+nf.format(K)+" ",x0+arrowLen,y0);
                g2.fill(leftArrows[0]);
                g2.fill(rightArrows[0]);
                break;
            case EWMA:
                g2.drawString("Lambda: ",x0,y0);
                x0+=metrics.stringWidth("Lambda: ");
                g2.setPaint(Color.blue);
                nf.setMaximumFractionDigits(2);
                g2.drawString(" "+nf.format(lambda)+" ", x0+arrowLen, y0);
                g2.fill(leftArrows[0]);
                g2.fill(rightArrows[0]);
                break;
            case CUSUM:
                g2.setColor(Color.black);
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
                break;
        }
        
    }
    
    
    public void setDisplayOption(int option){
        display_option=option;
        initPlot();
        repaint();
    }
    
    public int getDisplayOption(){
        return display_option;
    }
    
    public void updatePlot(String msg){
        if(varNames.contains(msg)){
            retrieveData();
            computeStats();
            initPlot();
            repaint();
        }
        else if(msg.equalsIgnoreCase("yymask")){
            retrieveData();
            computeStats();
            initPlot();
            repaint();
        }
    }
    
    public double getStdDev(){
        if(display_option==SHOW_INDIVIDUAL)
            return overall_stdDev;
        else
            return among_stdDev;
    }
    
    public void setK(double newK){
        K=newK;
        initPlot();
        repaint();
    }
    
    public double getK(){
        return K;
    }
    
    public double getMean(){
        return ymean;
    }
    
    public double getB3(){
        return B3;
    }
    
    public double getB4(){
        return B4;
    }
    
    public double getD3(){
        return D3;
    }
    
    public double getD4(){
        return D4;
    }
    
    public double getLambda(){
        return lambda;
    }
    
    public void setLambda(double l){
        lambda=l;
        computeStats();
        initPlot();
        repaint();
    }
    
    public double getAlpha(){
        return alpha;
    }
    
    public void setAlpha(double alpha){
        this.alpha=alpha;
        repaint();
    }
    
    public double getBeta(){
        return beta;
    }
    
    public void setBeta(double beta){
        this.beta=beta;
        repaint();
    }
    
    public double getDelta(){
        return delta;
    }
    
    public void setDelta(double d){
        delta=d;
        repaint();
    }
    
    public void mouseReleased(java.awt.event.MouseEvent p1) {
    }
    
    public void mouseEntered(java.awt.event.MouseEvent p1) {
    }
    
    public void mouseClicked(java.awt.event.MouseEvent p1) {
        Point pt=p1.getPoint();
        
        int index=0;
        boolean found=false;
        while(index<rects.length && !found){
            if(rects[index].contains(pt)){
                String input=null;
                switch(display_option){                    
                    case SHOW_MEAN:
                    case SHOW_INDIVIDUAL:
                        input=JOptionPane.showInputDialog(this,"Please input the new k:","Input k",JOptionPane.INFORMATION_MESSAGE);
                        try{
                            double tmpD=Double.parseDouble(input);
                            this.setK(tmpD);
                            defineHotSpots();
                            repaint();
                        }
                        catch(NumberFormatException nfe){
                        }
                        break;
                    case EWMA:
                        input=JOptionPane.showInputDialog(this,"Please input the new lambda:","Input Lambda",JOptionPane.INFORMATION_MESSAGE);
                        try{
                            double tmpD=Double.parseDouble(input);
                            
                            this.setLambda(tmpD);
                            defineHotSpots();
                            repaint();
                        }
                        catch(NumberFormatException nfe){
                        }
                        break;
                    case CUSUM:
                        if(index==0){
                            input=JOptionPane.showInputDialog(this,"Please input the new alpha:","Input Alpha",JOptionPane.INFORMATION_MESSAGE);
                            try{
                                double tmpD=Double.parseDouble(input);
                                
                                this.setAlpha(tmpD);
                                defineHotSpots();
                                repaint();
                            }
                            catch(NumberFormatException nfe){
                            }
                        }
                        else if(index==1){
                            input=JOptionPane.showInputDialog(this,"Please input the new beta:","Input Beta",JOptionPane.INFORMATION_MESSAGE);
                            try{
                                double tmpD=Double.parseDouble(input);
                                this.setBeta(tmpD);
                                defineHotSpots();
                                repaint();
                            }
                            catch(NumberFormatException nfe){
                            }
                        }
                        else{
                            input=JOptionPane.showInputDialog(this,"Please input the new delta:","Input Delta",JOptionPane.INFORMATION_MESSAGE);
                            try{
                                double tmpD=Double.parseDouble(input);
                                
                                this.setDelta(tmpD);
                                defineHotSpots();
                                repaint();
                            }
                            catch(NumberFormatException nfe){
                            }
                        }
                        break;
                }
                found=true;
            }
            if(leftArrows[index].contains(pt)){
                switch(display_option){
                    case SHOW_MEAN:
                    case SHOW_INDIVIDUAL:
                        K=K-1;
                        defineHotSpots();
                        repaint();
                        break;
                    case EWMA:
                        lambda-=0.01;
                        defineHotSpots();
                        repaint();
                        break;
                    case CUSUM:
                        if(index==0)
                            alpha-=aIncre;
                        else if(index==1)
                            beta-=bIncre;
                        else
                            delta-=dIncre;
                        defineHotSpots();
                        repaint();
                        break;
                }
                found=true;
            }
            if(rightArrows[index].contains(pt)){
                switch(display_option){
                    case SHOW_MEAN:
                    case SHOW_INDIVIDUAL:
                        K=K+1;
                        defineHotSpots();
                        repaint();
                        break;
                    case EWMA:
                        lambda+=0.01;
                        defineHotSpots();
                        repaint();
                        break;
                    case CUSUM:
                        if(index==0)
                            alpha+=aIncre;
                        else if(index==1)
                            beta+=bIncre;
                        else
                            delta+=dIncre;
                        defineHotSpots();
                        repaint();
                        break;
                }
                found=true;
            }
            index++;
        }
        
        
    }
    
    public void mousePressed(java.awt.event.MouseEvent p1) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent p1) {
    }
    
}
