package wvustat.modules;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Vector;

/**
 * BarChart implements a Bar Chart component that displays observations as bars. The height of the bar indicates
 * the numerical measure of the observation. The x coordinate of the bar indicates the major group of the observation
 * and the color of the bar indicates the subgroup of the observation.
 *
 *
 */

public class BarChart extends JPanel{
    public static final int MEAN_STAT=0;
    public static final int SUM_STAT=1;
    public static final int PERCENT_STAT=2;

    public static final String[] stats_supported={"Mean", "Sum", "Percentage"};

    private int stat_option=MEAN_STAT;

    private DataSet data;

    private Variable x1_var, y_var, z1_var;

    private Variable x2_var, z2_var;

    private Vector varNames;

    private int width=250, height=200;

    private FontMetrics metrics;

    private Insets insets=new Insets(20,40,40,20);

    private String[] x1_levels, z1_levels;

    private String[] x2_levels, z2_levels;

    private double[][] sums,means; //sum matrix
    private double[][] freqs; //frequency matrix

    private double[][] summary_stats;

    private double[][] z1_dummy, z2_dummy;

    private int z1_index=0, z2_index=0;

    private double ymin=0, ymax=0, xmin=0, xmax=0;

    private BaseAxisModel ytm;

    private CoordConverter coord;

    private NumberFormat nf;

    private Color[] colors;

    private Rectangle2D[] bars;

    private Rectangle2D focusedBar;

    private int focusedID;
    
    private GUIModule module;
    
    private GeneralPath popupArrow;

    public BarChart(DataSet data, Variable x, Variable y, Variable z){
        this.data=data;
        this.x1_var=x;
        this.y_var=y;
        this.z1_var=z;

        init();
    }

    public BarChart(DataSet data, Variable x1, Variable x2, Variable y, Variable z1, Variable z2){
        this.data=data;
        this.x1_var=x1;
        this.x2_var=x2;
        this.y_var=y;
        this.z1_var=z1;
        this.z2_var=z2;

        init();
    }


    protected void init(){
        retrieveData();

        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);

        varNames=new Vector();

        varNames.addElement(x1_var.getName());
        varNames.addElement(y_var.getName());
        if (z1_var != null)
            varNames.addElement(z1_var.getName());
        if (x2_var != null)
            varNames.addElement(x2_var.getName());
        if (z2_var != null)
            varNames.addElement(z2_var.getName());

        metrics=getFontMetrics(getFont());
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }

    private void retrieveData(){
        //double[] constrt1=null,constrt2=null;

        if(z1_var!=null){
            z1_dummy=z1_var.getDummyValues();
            //constrt1=extractColumn(z1_dummy,z1_index);
        }
        if(z2_var!=null){
            z2_dummy=z2_var.getDummyValues();
            //constrt2=extractColumn(z2_dummy,z2_index);
        }

        x1_levels=x1_var.getDistinctCatValues();
        if(x2_var!=null){
            x2_levels=x2_var.getDistinctCatValues();
        }
        if(z1_var!=null){
            z1_levels=z1_var.getDistinctCatValues();
        }
        if(z2_var!=null){
            z2_levels=z2_var.getDistinctCatValues();
        }


        if(x2_var!=null){
            colors=new Color[x2_levels.length];

            float d=1.0f/(x2_levels.length*1.8f);

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
        else{
            colors=new Color[1];
            colors[0]=Color.blue;
        }
    
    }
    
    protected void setStatOption(int option){
        stat_option=option;
        
        initPlot();
        repaint();
    }
        

    protected void initPlot(){
        double[] constrt1=null,constrt2=null;
        if(z1_var!=null)
        constrt1=extractColumn(z1_dummy,z1_index);
        if(z2_var!=null)
        constrt2=extractColumn(z2_dummy,z2_index);

        
        if(x2_var==null){
            freqs=data.getFreqMatrix(x1_var.getName(), x1_var.getName(), constrt1, constrt2);
            sums=data.getSumMatrix(y_var.getName(),x1_var.getName(), x1_var.getName(),constrt1, constrt2);
        }
        else{
            freqs=data.getFreqMatrix(x1_var.getName(), x2_var.getName(), constrt1, constrt2);
            sums=data.getSumMatrix(y_var.getName(),x1_var.getName(), x2_var.getName(),constrt1, constrt2);
        }
        

        means=new double[sums.length][sums[0].length];
        double[][] perts=new double[sums.length][sums[0].length];
        double total=0;
        
        for(int i=0;i<sums.length;i++){
            for(int j=0;j<sums[i].length;j++){
                if(freqs[i][j]!=0){
                    means[i][j]=sums[i][j]/freqs[i][j];
                }
                total+=sums[i][j];
            }
        }
        
        for(int i=0;i<perts.length;i++){
            for(int j=0;j<perts[i].length;j++){
                perts[i][j]=sums[i][j]/total*100;
            }
        }

        switch(stat_option){
            case MEAN_STAT:
            summary_stats=means;
            break;
            case SUM_STAT:
            summary_stats=sums;
            break;
            case PERCENT_STAT:
            summary_stats=perts;
            break;
        }

        double[] two_ends=findMinMax(summary_stats);
        ymin=two_ends[0];
        ymax=two_ends[1];

        ytm=new BaseAxisModel(ymin, ymax, 5);

        ymin=ytm.getStartValue();
        if(ymin<0)
        ymin=0;
        ymax=ytm.getEndValue();

        xmin=0;
        xmax=x1_levels.length+1;

        nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        insets.left=metrics.stringWidth(nf.format(ymax))+10;
        if(insets.left<40) insets.left=40;

        coord=new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);

        //Initialize bars
        if(x2_var==null){
            bars=new Rectangle2D[x1_levels.length];
        }
        else{
            bars=new Rectangle2D[x1_levels.length*x2_levels.length];
        }

        int ticWidth=coord.x(xmin+1)-coord.x(xmin);
        int barWidth=0;

        if(x2_var==null){
            barWidth=ticWidth/2;

            for(int i=0;i<bars.length;i++){
                int h=coord.y(ymin)-coord.y(summary_stats[i][i]);
                bars[i]=new Rectangle2D.Float(coord.x(i+1)-barWidth/2,coord.y(summary_stats[i][i]), barWidth, h);
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

                h=coord.y(ymin)-coord.y(summary_stats[x1_index][x2_index]);
                bars[i]=new Rectangle2D.Float(xco, coord.y(summary_stats[x1_index][x2_index]),barWidth,h);
            }
        }
    }


    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        initPlot();
        super.setBounds(x,y,w,h);
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        BasicStroke thin=new BasicStroke(1.0f);
        BasicStroke thick=new BasicStroke(2.0f);

        g2.drawLine(0,0,width-1,0);
        
        g2.fill(popupArrow);

        //Draw x and y axis
        g2.setStroke(thick);
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));

        g2.setStroke(thin);

        //draw x axis ticmarks
        for(int i=0;i<x1_levels.length;i++){
            String label=x1_levels[i];
            if(metrics.stringWidth(label)>(width-insets.left-insets.right)*1.0/x1_levels.length)
                label=label.substring(0,3)+"...";
            g2.drawLine(coord.x(i+1), coord.y(ymin), coord.x(i+1), coord.y(ymin)+4);
            //if(i%2==0)
            g2.drawString(label, coord.x(i+1)-metrics.stringWidth(label)/2, coord.y(ymin)+metrics.getHeight());
            //else
            //g2.drawString(label, coord.x(i+1)-metrics.stringWidth(label)/2, coord.y(ymin)+2*metrics.getHeight());
        }

        String xLabel = x1_var.getName();
        if (x2_var != null)
            xLabel += "-" + x2_var.getName();
        g2.drawString(xLabel, width / 2 - metrics.stringWidth(xLabel) / 2, coord.y(ymin) + metrics.getHeight() * 2);

        String yLabel = y_var.getName();
        if (stat_option == MEAN_STAT)
            yLabel += "(mean)";
        else if (stat_option == SUM_STAT)
            yLabel += "(sum)";
        else
            yLabel += "(%)";

        g2.drawString(yLabel, coord.x(xmin) + 2, coord.y(ymax));

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

        //Draw the bars
        for(int i=0;i<bars.length;i++){
            if(x2_var==null)
            g2.setPaint(colors[0]);
            else
            g2.setPaint(colors[i%x2_levels.length]);
            g2.fill(bars[i]);
        }

        if(focusedBar!=null){
            g2.setPaint(Color.black);
            g2.draw(focusedBar);

            int tmpX=(int)focusedBar.getX();
            int tmpY=(int)focusedBar.getY()-5;
            nf.setMaximumFractionDigits(3);
            if(x2_var!=null){
                int x1_index=focusedID/x2_levels.length;
                int x2_index=focusedID%x2_levels.length;
                g2.drawString(nf.format(summary_stats[x1_index][x2_index]), tmpX, tmpY);
            }
            else{
                g2.drawString(nf.format(summary_stats[focusedID][focusedID]), tmpX, tmpY);
            }
        }

    }

    public Color[] getColors(){
        return colors;
    }

    public String[] getColorLabels(){
        return x2_levels;
    }

    public void setZ1Index(int index){
        z1_index=index;
        initPlot();
        repaint();
    }

    public void setZ2Index(int index){
        z2_index=index;
        initPlot();
        repaint();
    }


    private double[] findMinMax(double[][] array){
        double max=Double.NEGATIVE_INFINITY;
        double min=Double.POSITIVE_INFINITY;

        for(int i=0;i<array.length;i++){
            for(int j=0;j<array[i].length;j++){
                if(array[i][j]>max)
                max=array[i][j];
                if(array[i][j]<min)
                min=array[i][j];
            }
        }

    return new double[]{min,max};
    }

    private double[] extractColumn(double[][] matrix, int col){
        double[] vec=new double[matrix.length];

        for(int i=0;i<vec.length;i++){
            vec[i]=matrix[i][col];
        }

        return vec;
    }

    protected void printMatrix(double[][] array){
        for(int i=0;i<array.length;i++){
            for(int j=0;j<array[i].length;j++){
                System.out.print(array[i][j]);
                if(j!=array[i].length-1)
                System.out.print(",");
            }
            System.out.println();
        }
    }

    public String[] getZ1Levels(){
        return z1_levels;
    }

    public String[] getZ2Levels(){
        return z2_levels;
    }
    
    public void updatePlot(String msg){

        retrieveData();
        initPlot();
        repaint();
    }
    
    public double[] getYValues(){
        double[] ret=null;
   
        if(x2_var==null){
            ret=new double[x1_levels.length];
            for(int i=0;i<ret.length;i++){
                if(stat_option==SUM_STAT)
                ret[i]=sums[i][i];
                else if(stat_option==MEAN_STAT)
                ret[i]=means[i][i];
                else
                ret[i]=summary_stats[i][i];
            }
            
        }
        
        return ret;
    }
    
    public String[] getXValues(){
        if(x2_var==null)
            return x1_levels;
        else
            return null;
    }
    
    public String[] getX1Levels(){
        return x1_levels;
    }
    
    public String[] getX2Levels(){
        return x2_levels;
    }
    
    public double[][] getSumMatrix(){
        return sums;
    }
    
    public double[][] getMeanMatrix(){
        return means;
    }
    
    public double[][] getFreqMatrix(){
        return freqs;
    }


    //class MouseHandler extends MouseMotionAdapter{
    	class MouseHandler extends MouseAdapter implements MouseMotionListener{
        public void mouseMoved(MouseEvent me){
            Point pt=me.getPoint();

            boolean found=false;
            int i=0;
            while(i<bars.length&&!found){
                found=bars[i].contains(pt);
                i++;
            }

            if(found && bars[i-1]!=focusedBar){
                focusedBar=bars[i-1];
                focusedID=i-1;
                repaint();
            }
            else if(!found){
                focusedBar=null;
                repaint();
            }
        }
        
        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
                JPopupMenu popupOptionMenu = ((ChartModule) module).getOptionMenu().getPopupMenu();
                popupOptionMenu.show(e.getComponent(), x, y);
                return;
            }
        }
        
        public void mouseDragged(MouseEvent e)
        {}
    }


}
