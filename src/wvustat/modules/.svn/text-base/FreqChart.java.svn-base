/*
 * FreqChart.java
 *
 * Created on August 8, 2000, 3:22 PM
 */

package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.*;

import wvustat.interfaces.*;

/**
 * FreqChart is used to display the frequency of one or two categorical x variables graphically.
 *
 * @author  Hengyi Xue
 * @version 1.0, August 8, 2000
 */
public class FreqChart extends JPanel
{
    public static final int PLOT_FREQ = 0;
    public static final int PLOT_REL_FREQ = 1;
   
    protected DataSet data;
    protected Variable x1, x2;
    protected Vector vx2;

    protected double[][] freqs = null;
    protected Color[] colors;
    protected int width = 250, height = 200;
    protected Insets insets = new Insets(25, 40, 40, 20);
    protected CoordConverter coord;
    protected BaseAxisModel tm;
    protected double xmin, xmax, ymin, ymax;
    protected String[] x1_levels, x2_levels;

    protected Rectangle2D[] bars;
    
    protected double[][] selectedFreqs = null;

    protected FontMetrics metrics;
    protected NumberFormat nf;

    protected Rectangle2D focusedBar;
    protected int focusedID;
    protected boolean isChildOfThisClass = false;

    private double freqSum = 0;
    private int x2Index;
    private EqualCountGrouper groupMaker;

    private int plotOption;
    
    private GUIModule module;
    protected GeneralPath popupArrow;

    /** Creates new FreqChart */
    public FreqChart(DataSet data, Variable x1, Vector vx)
    {
        this.data = data;
        this.x1 = x1;
        
        vx2 = vx;
        if(vx2.size() > 0)
            this.x2 = (Variable)vx2.elementAt(0);
        //this.x2 = x2;

        init();
    }

    public FreqChart(DataSet data, Variable x1)
    {
        this.data = data;
        this.x1 = x1;

        init();
    }

    protected void retrieveData()
    {
        String name1 = null, name2 = null;
        double[][] tmpFreqs = null;
        
        name1 = x1.getName();
        x1_levels = x1.getDistinctCatValues();
        if (x2 != null)
        {
            /*name2 = x2.getName();
            freqs = data.getFreqMatrix(name1, name2);
            groupMaker = new GroupMaker(x2);
            if (x2.getType() == Variable.CATEGORICAL)
                x2_levels = x2.getDistinctCatValues();
            else
                x2_levels = groupMaker.getGroupNames();*/
            	        
            if(groupMaker == null)
                groupMaker = new EqualCountGrouper(vx2, data);
            
            freqs = data.getFreqMatrix(name1, groupMaker);
            x2_levels = groupMaker.getGroupNames();
            
        }
        else
            freqs = data.getFreqMatrix(name1, name1);

        tmpFreqs = data.getFreqMatrix(name1, name1);//add by djluo for multiple conditioning
        //printMatrix(freqs);
        

        freqSum = 0;
        for (int i = 0; i < tmpFreqs.length; i++)
        {
            for (int j = 0; j < tmpFreqs[i].length; j++)
            {
                freqSum += tmpFreqs[i][j];
            }
        }
        //freqSum is the total of frequency

        if (plotOption == PLOT_REL_FREQ)
        {
            for (int i = 0; i < freqs.length; i++)
            {
                for (int j = 0; j < freqs[i].length; j++)
                {
                    freqs[i][j] = 1.0 * freqs[i][j] / freqSum;
                }
            }
        }
        //printMatrix(freqs);

        
        //count highlight frequency of observations
        
        selectedFreqs = new double[freqs.length][freqs[0].length];
        for (int i = 0; i < selectedFreqs.length; i++)
        	for (int j = 0; j < selectedFreqs[i].length; j++)
        		selectedFreqs[i][j] = 0;
        
        for (int i = 0; i < data.getSize(); i++) {
        	
        	if (data.getMask(i)) continue;
        	if (!data.getState(i)) continue;
        		
        	String str = (String)x1.getValue(i);
        	int k = Arrays.asList(x1_levels).indexOf(str);
        		
        	if (x2 == null) {
        		if (data.getFreqVariable() == null)
        			selectedFreqs[k][k]++;
        		else
        			selectedFreqs[k][k] += data.getFreqVariable().getNumValues()[i];
        		
         	} else {
        		Iterator it = groupMaker.getGroupIndex(i).iterator();
        		while (it.hasNext()) {
        			int j = ((Integer)it.next()).intValue();
        			if (data.getFreqVariable() == null)
        				selectedFreqs[k][j]++;
        			else
        				selectedFreqs[k][j] += data.getFreqVariable().getNumValues()[i];
        		}
        	}
        	
        }
        
        if (plotOption == PLOT_REL_FREQ)
        {
            for (int i = 0; i < selectedFreqs.length; i++)
            {
                for (int j = 0; j < selectedFreqs[i].length; j++)
                {
                    selectedFreqs[i][j] = 1.0 * selectedFreqs[i][j] / freqSum;
                }
            }
        }
        
        //printMatrix(selectedFreqs);
    }

    public EqualCountGrouper getGroupMaker()
    {
        return groupMaker;
    }

    protected void generateColors()
    {
        /*
        if(x2!=null){
            colors=new Color[x2_levels.length];

            float d=1.0f/(x2_levels.length+1);

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
        */
        //else
        //{
            colors = new Color[1];
            colors[0] = Color.lightGray; //new Color(28,170,255);
            
        //}
    }


    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h)
    {
        width = w;
        height = h;
        initPlot();
        super.setBounds(x, y, w, h);
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }

    protected void init()
    {
        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
        metrics = getFontMetrics(getFont());
        MouseHandler mh = new MouseHandler();
        addMouseMotionListener(mh);
        addMouseListener(mh);

        retrieveData();
        generateColors();
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }


    void updatePlot()
    {
        retrieveData();
        generateColors();
        initPlot();
        repaint();
    }

    protected void printMatrix(double[][] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array[i].length; j++)
            {
                System.out.print(array[i][j]);
                if (j != array[i].length - 1)
                    System.out.print(",");
            }
            System.out.println();
        }
    }

    protected void initPlot()
    {
        ymax = 0;
        for (int i = 0; i < freqs.length; i++)
        {
            for (int j = 0; j < freqs[i].length; j++)
            {
                if (freqs[i][j] > ymax)
                    ymax = freqs[i][j];
            }
        }

        ymin = 0;
        xmin = 0;
        xmax = freqs.length + 1;

        tm = new BaseAxisModel(ymin, ymax, 5);

        ymax = tm.getEndValue();

        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        insets.left = metrics.stringWidth(nf.format(ymax)) + 10;
        if (insets.left < 40) insets.left = 40;

        coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);

        if (x2 == null)
            bars = new Rectangle2D[(int) xmax - 1];
        else
            bars = new Rectangle2D[x1_levels.length * x2_levels.length];

        int ticWidth = coord.x(xmin + 1) - coord.x(xmin);

        int barWidth = 0;
        if (x2 == null)
        {
            barWidth = ticWidth / 2;

            for (int i = 0; i < bars.length; i++)
            {
                int h = coord.y(ymin) - coord.y(freqs[i][i]);
                bars[i] = new Rectangle2D.Float(coord.x(i + 1) - barWidth / 2, coord.y(freqs[i][i]), barWidth, h);
            }
        }
        else
        {
            //barWidth=(int)(ticWidth*0.8/x2_levels.length);
            barWidth = ticWidth / 2;

            int xco = 0;

            int h = 0;
            for (int i = 0; i < bars.length; i++)
            {
                int x1_index = i / x2_levels.length;
                int x2_index = i % x2_levels.length;

                /*
                if (x2_index == 0)
                {
                    xco = coord.x(x1_index + 1) - totalWidth / 2;
                }
                else
                {
                    xco = xco + barWidth;
                }
                */
                xco = coord.x(x1_index + 1) - barWidth / 2;

                h = coord.y(ymin) - coord.y(freqs[x1_index][x2_index]);
                bars[i] = new Rectangle2D.Float(xco, coord.y(freqs[x1_index][x2_index]), barWidth, h);
            }
        }

    }

    protected void drawTicmarks(Graphics2D g2)
    {
        nf.setMaximumFractionDigits(1);
        //draw x axis ticmarks
        for (int i = 0; i < x1_levels.length; i++)
        {
            String label = x1_levels[i];
            g2.drawLine(coord.x(i + 1), coord.y(ymin), coord.x(i + 1), coord.y(ymin) + 4);
            //if(i%2==0)
            g2.drawString(label, coord.x(i + 1) - metrics.stringWidth(label) / 2, coord.y(ymin) + metrics.getHeight());
            //else
            //g2.drawString(label, coord.x(i+1)-metrics.stringWidth(label)/2, coord.y(ymin)+2*metrics.getHeight());
        }
        //Draw y axis ticmarks
        double tmp = ymin;
        double step = tm.getInterval();
        if (plotOption == PLOT_FREQ)
        {
            if (Math.round(step) >= 1)
            {
                step = Math.round(step);
            }
            else
                step = 1;
        }

        while (tmp <= ymax)
        {

            g2.drawLine(coord.x(xmin), coord.y(tmp), coord.x(xmin) - 4, coord.y(tmp));
            String label = nf.format(tmp);

            int len = metrics.stringWidth(label);

            g2.drawString(label, coord.x(xmin) - 4 - len, coord.y(tmp) + metrics.getMaxAscent() / 2);

            tmp += step;

        }

        if (plotOption == PLOT_FREQ)
            g2.drawString("Freq", coord.x(xmin), coord.y(ymax) - 2);
        else
            g2.drawString("Rel Freq", coord.x(xmin), coord.y(ymax) - 2);


        String xLabel = x1.getName();

        g2.drawString(xLabel, width / 2 - metrics.stringWidth(xLabel) / 2, coord.y(ymin) + metrics.getHeight() * 2);

    }

    public void setX2Index(int index)
    {
        this.x2Index = index;
        retrieveData();
        generateColors();
        initPlot();
        repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (isChildOfThisClass)
            return;

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.fill(popupArrow);

        g2.drawLine(0, 0, width - 1, 0);
        BasicStroke thin = new BasicStroke(1.0f);
        BasicStroke thick = new BasicStroke(2.0f);
        //Draw x and y axis
        g2.setStroke(thick);
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));

        g2.setStroke(thin);

        drawTicmarks(g2);

        //Draw the bars
        for (int i = 0; i < bars.length; i++)
        {

            g2.setColor(colors[0]);
            if (x2 == null || i % x2_levels.length == x2Index)
                g2.fill(bars[i]);
        }
        
        //Draw the bars for selected observations
        for (int i = 0; i < bars.length; i++)
        {
        	g2.setColor(Color.darkGray);
        	
        	double h = 0;
        	if (x2 == null) {         		
        		h = bars[i].getHeight() * selectedFreqs[i][i] / freqs[i][i];
        	} 
        	else if (i % x2_levels.length == x2Index) {
        		int x1_index = i / x2_levels.length;
        		h = bars[i].getHeight() * selectedFreqs[x1_index][x2Index] / freqs[x1_index][x2Index]; 
        	}
        	
        	
        	if (x2 == null || i % x2_levels.length == x2Index) {
        		Rectangle2D bar = new Rectangle2D.Double(bars[i].getMinX(), bars[i].getMaxY() - h, bars[i].getWidth(), h);
        		g2.fill(bar);
        	}        	      	
        }
        
        

        if (focusedBar != null)
        {
            g2.setColor(Color.black);
            g2.draw(focusedBar);

            int tmpX = (int) focusedBar.getX();
            int tmpY = (int) focusedBar.getY() - 5;
            nf.setMaximumFractionDigits(2);
            if (x2 != null)
            {
                int x1_index = focusedID / x2_levels.length;
                int x2_index = focusedID % x2_levels.length;
                String str="";
                if(plotOption==PLOT_FREQ)
                    str = nf.format(freqs[x1_index][x2_index]) + " (" + nf.format(freqs[x1_index][x2_index] * 100 / freqSum) + "%)";
                else
                    str = nf.format(freqs[x1_index][x2_index]*100)  + "%";
                g2.drawString(str, tmpX, tmpY);
            }
            else
            {
                String str="";
                if(plotOption==PLOT_FREQ)
                    str = nf.format(freqs[focusedID][focusedID]) + " (" + nf.format(freqs[focusedID][focusedID] * 100 / freqSum) + "%)";
                else
                    str= nf.format(freqs[focusedID][focusedID]*100)+"%";
                g2.drawString(str, tmpX, tmpY);
            }
        }

    }

    public Color[] getColors()
    {
        return colors;
    }
    
    /**
     * Get the labels of z values displayed
     */
    public String[] getColorLabels()
    {
        return x2_levels;
    }

    /**
     * Get the y values displayed
     */
    public double[] getValues()
    {
        double[] ret = new double[freqs.length];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = freqs[i][i];
            if(plotOption==PLOT_REL_FREQ)
                ret[i]=ret[i]*freqSum;
        }

        return ret;
    }

    public double[] getCurrentFreqs(){
        double[] array=new double[x1_levels.length];
        for(int i=0;i<array.length;i++){
            array[i]=freqs[i][x2Index];
            if(plotOption==PLOT_REL_FREQ)
                array[i]=array[i]*freqSum;
        }
        return array;
    }

    public int getGrandTotal(){
        return (int)freqSum;
    }

    /**
     * Get the labels of y values displayed
     */
    public String[] getLabels()
    {
        return x1_levels;
    }

    public void setPlotOption(int option)
    {
        this.plotOption = option;
        retrieveData();
        generateColors();
        initPlot();
        repaint();
    }

    class MouseHandler extends MouseAdapter implements MouseMotionListener
    {
        public void mouseClicked(MouseEvent me)
        {
        }

        public void mouseMoved(MouseEvent me)
        {
            Point pt = me.getPoint();

            boolean found = false;
            int i = 0;
            while (i < bars.length && !found)
            {
                found = bars[i].contains(pt) && (x2_levels==null || i % x2_levels.length == x2Index);
                i++;
            }

            if (found && bars[i - 1] != focusedBar)
            {
                focusedBar = bars[i - 1];
                focusedID = i - 1;
                repaint();
            }
            else if (!found)
            {
                focusedBar = null;
                repaint();
            }
        }

        public void mouseDragged(final java.awt.event.MouseEvent p1)
        {
        }
        
        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
                JPopupMenu popupOptionMenu = ((FreqModule) module).getOptionMenu().getPopupMenu();
                popupOptionMenu.show(e.getComponent(), x, y);
                return;
            }
            

            Point pt = e.getPoint();

            boolean found = false;
            int i = 0;
            while (i < bars.length && !found)
            {
                found = bars[i].contains(pt) && (x2 == null || i % x2_levels.length == x2Index);
                i++;
            }

            if (found)
            {
                int x1_index;
                int x2_index;
                String str1 = null, str2 = null;

                if (x2 == null)
                {
                    x1_index = i - 1;
                    str1 = getLabels()[x1_index];//x1_levels[x1_index];
                }
                else
                {
                    x1_index = (i - 1) / x2_levels.length;
                    x2_index = (i - 1) % x2_levels.length;
                    str1 = x1_levels[x1_index];
                    str2 = x2_levels[x2_index];
                }

                
                for (int j = 0; j < data.getSize(); j++)
                {
                    data.setState(false, j);
                    if (data.getMask(j)) continue;
                    if (x2 == null && x1.getValue(j).equals(str1))
                    {
                        data.setState(true, j);
                    }
                    //else if (x1.getValue(j).equals(str1) && x2.getValue(j).equals(str2))
                    else if (x2 != null && x1.getValue(j).equals(str1) && groupMaker.getGroupIndex(j).contains(new Integer(x2Index)))
                    {
                        data.setState(true, j);
                    }
                }
            }
        }
    }
}
