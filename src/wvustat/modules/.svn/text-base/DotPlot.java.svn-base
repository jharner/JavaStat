package wvustat.modules;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.rmi.*;

import javax.swing.*;

import java.util.Arrays;
import java.util.Vector;
import java.text.NumberFormat;

//Importing self defined packages.
import wvustat.interfaces.*;
import wvustat.swing.MarkerShape;

/**
 *	DotPlot is a component that displays a data set as a plot where each observation is represented by
 *	a disc. The y variable, x variable and possibly z variable of the dataset will be used. Y variable
 *	should be numeric and x variable should be discrete. Z variable can be either discrete or numeric.
 *	The y values will be used as y coordinates and x values will be used as x coordinate. This component is used
 *  in KSampleModule.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Feb. 23, 2000
 */

public class DotPlot extends JPanel
{
    private DataSet data;

    private Variable y_var, x_var, z_var;
    private Vector vz;

    private int width = 250, height = 200;

    private FontMetrics metrics;

    private double[] yvals;

    private double[][] grouped;

    private String[] xvals;

    private int[] indices;

    private int currentIndex = 0;

    //private GroupMaker grouper;
    private EqualCountGrouper grouper;

    private Vector xlevels;

    private BaseAxisModel ytm;

    private CoordConverter coord;

    private Insets insets = new Insets(20, 40, 40, 20);

    private Shape[] discs;

    private double ymin, ymax, xmin, xmax, ymean;

    private NumberFormat nf;

    private Vector varNames = new Vector();

    //These four variables are used to keep track of mouse movement
    private int first_x, first_y, last_x, last_y, brushWidth = 10, brushHeight = 10;

    private boolean draggingStarted = false;

    private boolean fitMean = false, fitCI = false, fitBox = false;

    private boolean displayDots = true;

    private boolean fitOutlierBox;

    private SummaryStat sstat = new SummaryStat();
    
    private GUIModule module;
    
    private GeneralPath popupArrow;

    /**
     * Constructor
     * Creates a new DotPlot based on the data set, as well as y, x, z variables specified
     * in the parameters.
     *
     * @param data the data set this DotPlot is based on.
     * @param y_var the y variable
     * @param x_var the x variable
     * @param z_var the z variable
     */
    public DotPlot(DataSet data, Variable y_var, Variable x_var, Vector vz)
    {
        this.data = data;
        this.y_var = y_var;
        this.x_var = x_var;
        //this.z_var = z_var;
        
        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);

        retrieveData();

        setBackground(Color.white);
        MouseEventHandler handler = new MouseEventHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);

        metrics = getFontMetrics(getFont());

        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());
        if (z_var != null)
            varNames.addElement(z_var.getName());
        
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();

    }

    /**
     * Get the class that divides observations into groups.
     */
    public EqualCountGrouper getGroupMaker()
    {
        return grouper;
    }


    /**
     * Retrieve data from the data set.
     */
    private void retrieveData()
    {
        double[] ytmp = y_var.getNumValues();
        String[] xtmp = x_var.getCatValues();

        Vector zvals = null;

        if (z_var != null)
        {
            if (grouper == null)
                grouper = new EqualCountGrouper(vz, data);
            zvals = z_var.getValues();
        }

        //This vector keeps y values
        Vector vy = new Vector();
        //This vector keeps x values
        Vector vx = new Vector();
        //This vector keeps indices
        Vector iv = new Vector();

        for (int i = 0; i < ytmp.length; i++)
        {
            if (!data.getMask(i))
            {
                Double dy = new Double(ytmp[i]);
                if (grouper == null)
                {
                    vy.addElement(dy);
                    vx.addElement(xtmp[i]);
                    iv.addElement(new Integer(i));
                }
                else if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
                {
                    vy.addElement(dy);
                    vx.addElement(xtmp[i]);
                    iv.addElement(new Integer(i));
                }
            }
        }

        yvals = new double[vy.size()];
        xvals = new String[vx.size()];
        indices = new int[iv.size()];


        for (int i = 0; i < yvals.length; i++)
        {
            yvals[i] = ((Double) vy.elementAt(i)).doubleValue();
            xvals[i] = (String) vx.elementAt(i);
            indices[i] = ((Integer) iv.elementAt(i)).intValue();
        }
        xlevels = new Vector();
        for (int i = 0; i < xvals.length; i++)
        {
            if (!xlevels.contains(xvals[i]))
            {
                xlevels.addElement(xvals[i]);
            }
        }

        String[] tmpArray = new String[xlevels.size()];
        xlevels.copyInto(tmpArray);
        sstat.setGroupNames(tmpArray);

        grouped = new double[xlevels.size()][];
        for (int i = 0; i < grouped.length; i++)
        {

            Vector tmpv = new Vector();
            for (int j = 0; j < xvals.length; j++)
            {
                if (xvals[j].equals((String) xlevels.elementAt(i)))
                    tmpv.addElement(new Double(yvals[j]));
            }

            grouped[i] = new double[tmpv.size()];
            for (int j = 0; j < tmpv.size(); j++)
            {
                grouped[i][j] = ((Double) tmpv.elementAt(j)).doubleValue();
            }
        }

        sstat.setData(grouped);

        
    }

    /**
     * Initialize properties such as ticmarks, coordinate converter for the plot
     */
    private void initPlot()
    {
        try
        {

            StatEngine se = new StatEngineImpl();

            if( yvals.length > 0 ){
                ymin = se.getMin(yvals);
                ymax = se.getMax(yvals);
                ymean = se.getMean(yvals);
            }

            ytm = new BaseAxisModel(ymin, ymax, 5);

            ymin = ytm.getStartValue();
            ymax = ytm.getEndValue();

            xmin = 0;
            xmax = xlevels.size() + 1;

            nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);
            insets.left = metrics.stringWidth(nf.format(ymax)) + 8;
            if (insets.left < 40) insets.left = 40;


            coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);

            
        }
        catch (RemoteException re)
        {
        		re.printStackTrace();
        }
    }

    /**
     * get the SummaryStat object
     */
    public SummaryStat getSummaryStat()
    {
        return sstat;
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

    /**
     * Set the current group to be the indexed group
     */
    public void setGroup(int index)
    {
        currentIndex = index;
        retrieveData();
        initPlot();
        repaint();
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }

    /**
     * Get the dimension of this DotPlot
     */
    public Dimension getSize()
    {
        return new Dimension(width, height);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        BasicStroke thin = new BasicStroke(1.0f);
        BasicStroke thick = new BasicStroke(2.0f);
        
        g2.fill(popupArrow);

        //Draw two lines, one at the top of the plot, another at the bottom of the plot
        g2.drawLine(0, 0, width - 1, 0);
        g2.drawLine(0, height - 1, width - 1, height - 1);

        //Draw x axis
        g2.setStroke(thick);
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));


        g2.setStroke(thin);
        //Draw a blue horizontal line to indicate overall mean
        g2.setColor(Color.blue);
        g2.drawLine(coord.x(xmin), coord.y(ymean), coord.x(xmax), coord.y(ymean));

        g2.setColor(Color.black);
        //Draw x and y axis labels
        g2.drawString(y_var.getName(), coord.x(xmin) + 4, coord.y(ymax) - 2);

        String xname = x_var.getName();
        int len = metrics.stringWidth(xname);
        g2.drawString(xname, (width - len) / 2, coord.y(ymin) + 2 * metrics.getHeight());

        //draw x axis ticmarks
        for (int i = 0; i < xlevels.size(); i++)
        {
            String label = (String) xlevels.elementAt(i);
            g2.drawLine(coord.x(i + 1), coord.y(ymin), coord.x(i + 1), coord.y(ymin) + 4);
            g2.drawString(label, coord.x(i + 1) - metrics.stringWidth(label) / 2, coord.y(ymin) + metrics.getHeight());
        }

        //Draw y ticmarks
        double tmp = ymin;
        double step = ytm.getInterval();
        while (tmp <= ymax)
        {
            g2.drawLine(coord.x(xmin), coord.y(tmp), coord.x(xmin) - 4, coord.y(tmp));
            String label = nf.format(tmp);

            int len2 = metrics.stringWidth(label);

            g2.drawString(label, coord.x(xmin) - 4 - len2, coord.y(tmp) + metrics.getMaxAscent() / 2);

            tmp += step;
        }

        //Draw individual data points
        discs = new Shape[yvals.length];

        for (int i = 0; i < yvals.length; i++)
        {
            int index = xlevels.indexOf(xvals[i]);
            discs[i] = new MarkerShape(coord.x(index + 1), coord.y(yvals[i]), data.getMarker(indices[i]).intValue());
        }
        
        if (displayDots)
        {
            for (int i = 0; i < discs.length; i++)
            {
                g2.setColor(data.getColor(indices[i]));
                if (data.getState(indices[i]))
                {
                    g2.fill(discs[i]);
                    g2.draw(discs[i]);
                    if (data.getLabel(indices[i]) != null)
                    {
                        g2.setColor(Color.black);
                        g2.drawString(data.getLabel(indices[i]), (int) discs[i].getBounds().getMaxX() + 2, (int) discs[i].getBounds().getMinY() - 2);
                    }
                }
                else
                {
                    g2.draw(discs[i]);
                }
            }
        }

        if (draggingStarted)
        {
            Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);
            g2.setColor(Color.lightGray);
            g2.draw(rect);
        }

        //optionally draw a dics representing the mean and a line representing +/- std error
        if (fitMean)
        {
            g2.setColor(Color.blue);
            for (int i = 0; i < grouped.length; i++)
            {
                int n = sstat.getGroupSize(i);
                double m = sstat.getMean(i);
                double error = sstat.getStdDev(i) / Math.sqrt(n);
                g2.fillOval(coord.x(i + 1) - 3, coord.y(m) - 3, 6, 6);
                //draw line through mean
                g2.drawLine(coord.x(i + 1), coord.y(m - error), coord.x(i + 1), coord.y(m + error));
                //draw two short lines
                g2.drawLine(coord.x(i + 1) - 2, coord.y(m + error), coord.x(i + 1) + 2, coord.y(m + error));
                g2.drawLine(coord.x(i + 1) - 2, coord.y(m - error), coord.x(i + 1) + 2, coord.y(m - error));
            }

        }

        if (fitCI)
        {
            g2.setColor(Color.green);
            StatEngine se = new StatEngineImpl();
            int w = coord.x(1) - coord.x(0);
            try
            {
                for (int i = 0; i < grouped.length; i++)
                {
                    int n = grouped[i].length;
                    Distribution t = se.getDistribution("t", new double[]{n - 1});
                    double t0 = t.quantile(0.025);
                    double m = sstat.getMean(i);
                    double error = sstat.getStdDev(i) / Math.sqrt(n);
                    double low = m + error * t0;
                    double hi = m - error * t0;

                    //draw a line going through group mean
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(m), coord.x(i + 1) + w / 2, coord.y(m));

                    //draw the diamond
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(m), coord.x(i + 1), coord.y(low));
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(m), coord.x(i + 1), coord.y(hi));
                    g2.drawLine(coord.x(i + 1) + w / 2, coord.y(m), coord.x(i + 1), coord.y(low));
                    g2.drawLine(coord.x(i + 1) + w / 2, coord.y(m), coord.x(i + 1), coord.y(hi));


                }
            }
            catch (RemoteException re)
            {
            }
        }

        if (fitBox)
        {
            g2.setColor(Color.gray);
            int w = (int) ((coord.x(1) - coord.x(0)) / 2.0f);
            StatEngine se = new StatEngineImpl();
            try
            {
                for (int i = 0; i < grouped.length; i++)
                {
                    double[] q = se.getQuantiles(grouped[i]);

                    //Draw the box
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[2]), coord.x(i + 1) + w / 2, coord.y(q[2]));
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[1]), coord.x(i + 1) - w / 2, coord.y(q[3]));
                    g2.drawLine(coord.x(i + 1) + w / 2, coord.y(q[1]), coord.x(i + 1) + w / 2, coord.y(q[3]));

                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[1]), coord.x(i + 1) + w / 2, coord.y(q[1]));
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[3]), coord.x(i + 1) + w / 2, coord.y(q[3]));

                    //Draw the line that runs through the min to max
                    g2.drawLine(coord.x(i + 1), coord.y(q[0]), coord.x(i + 1), coord.y(q[1]));
                    g2.drawLine(coord.x(i + 1), coord.y(q[3]), coord.x(i + 1), coord.y(q[4]));


                }
            }
            catch (RemoteException re)
            {
            }
        }


        if (fitOutlierBox)
        {
            g2.setColor(Color.gray);
            int w = (int) ((coord.x(1) - coord.x(0)) / 2.0f);
            StatEngine se = new StatEngineImpl();
            try
            {
                for (int i = 0; i < grouped.length; i++)
                {
                    double[] q = se.getQuantiles(grouped[i]);
                    double IQR = q[3] - q[1];
                    double outlierMin = q[1] - 1.5 * IQR;
                    double outlierMax = q[3] + 1.5 * IQR;
                    double outlierMin2 = q[1] - 3.0 * IQR;
                    double outlierMax2 = q[3] + 3.0 * IQR;
                    boolean drawOutlierMin = false;
                    boolean drawOutlierMax = false;
                    boolean drawOutlierMin2 = false;
                    boolean drawOutlierMax2 = false;
                    double lineStart = q[4];
                    double lineEnd = q[0];
                    for (int j = 0; j < grouped[i].length; j++)
                    {
                        if (grouped[i][j] > outlierMin && grouped[i][j] < lineStart)
                            lineStart = grouped[i][j];

                        if (grouped[i][j] < outlierMax && grouped[i][j] > lineEnd)
                            lineEnd = grouped[i][j];
                        if (grouped[i][j] < outlierMin)
                            drawOutlierMin = true;
                        if (grouped[i][j] < outlierMin2)
                            drawOutlierMin2 = true;
                        if (grouped[i][j] > outlierMax)
                            drawOutlierMax = true;
                        if (grouped[i][j] > outlierMax2)
                            drawOutlierMax2 = true;

                    }
                    g2.setColor(Color.black);

                    //Draw the box
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[2]), coord.x(i + 1) + w / 2, coord.y(q[2]));
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[1]), coord.x(i + 1) - w / 2, coord.y(q[3]));
                    g2.drawLine(coord.x(i + 1) + w / 2, coord.y(q[1]), coord.x(i + 1) + w / 2, coord.y(q[3]));

                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[1]), coord.x(i + 1) + w / 2, coord.y(q[1]));
                    g2.drawLine(coord.x(i + 1) - w / 2, coord.y(q[3]), coord.x(i + 1) + w / 2, coord.y(q[3]));

                    //Draw the line that runs through min to max
                    g2.drawLine(coord.x(i + 1), coord.y(lineStart), coord.x(i + 1), coord.y(q[1]));
                    g2.drawLine(coord.x(i + 1), coord.y(q[3]), coord.x(i + 1), coord.y(lineEnd));


                    g2.setColor(Color.red);
                    if (drawOutlierMin)
                        g2.drawLine(coord.x(i + 1) - w / 2, coord.y(outlierMin), coord.x(i + 1) + w / 2, coord.y(outlierMin));
                    if (drawOutlierMax)
                        g2.drawLine(coord.x(i + 1) - w / 2, coord.y(outlierMax), coord.x(i + 1) + w / 2, coord.y(outlierMax));
                    if (drawOutlierMin2)
                        g2.drawLine(coord.x(i + 1) - w / 2, coord.y(outlierMin2), coord.x(i + 1) + w / 2, coord.y(outlierMin2));
                    if (drawOutlierMax2)
                        g2.drawLine(coord.x(i + 1) - w / 2, coord.y(outlierMax2), coord.x(i + 1) + w / 2, coord.y(outlierMax2));

                }
            }
            catch (RemoteException re)
            {
            }
        }


    }

    /**
     * Toggle the "Fit means" option on/off
     */
    public void enableFitMean(boolean val)
    {
        fitMean = val;
        repaint();
    }

    /**
     * Toggle the "Fit CI" option on/off
     */
    public void enableFitCI(boolean val)
    {
        fitCI = val;
        repaint();
    }

    public boolean isDisplayDots()
    {
        return displayDots;
    }

    public void setDisplayDots(boolean displayDots)
    {
        this.displayDots = displayDots;
        repaint();
    }

    /**
     * Toggle the "Fit Box" option on/off
     */
    public void enableFitBox(boolean val)
    {
        fitBox = val;
        repaint();
    }

    public boolean isFitOutlierBox()
    {
        return fitOutlierBox;
    }

    public void setFitOutlierBox(boolean fitOutlierBox)
    {
        this.fitOutlierBox = fitOutlierBox;
        repaint();
    }

    /**
     * update the plot based on the message
     *
     * @param arg tells what has changed
     */
    public void updatePlot(String arg)
    {
        if (arg.equals("yystate") || arg.equals("yycolor"))
        {
            repaint();
        }
        else if (arg.equals("yymask"))
        {
            retrieveData();
            initPlot();
            repaint();
        }
        else if (varNames.contains(arg))
        {
            retrieveData();
            initPlot();
            repaint();
        }
        else if (arg.equals("new_obs_added") || arg.equals("obs_deleted"))
        {
            retrieveData();
            initPlot();
            repaint();
        }
    }

    /**
     * Inner class
     *	Handles mouse event
     */
    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {

        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
                JPopupMenu popupOptionMenu = ((KSampleModule) module).getOptionMenu().getPopupMenu();
                popupOptionMenu.show(e.getComponent(), x, y);
                return;
            }
            
            if (LinkPolicy.mode != LinkPolicy.SELECTING)
            {
            	last_x = x;
            	last_y = y;
            	first_x = x - brushWidth;
            	first_y = y - brushHeight;
            	
            	
                //data.clearStates();
                boolean[] states = new boolean[data.getSize()];
            	Arrays.fill(states, false);
            	
                Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);

                for (int i = 0; i < discs.length; i++)
                {
                    if (rect.contains(discs[i].getBounds()))
                    {
                        //data.setState(true, indices[i]);
                        states[indices[i]] = true;
                    }
                }
                data.setStates(states);
            	
                draggingStarted = true;
                DotPlot.this.repaint();
                return;
            }
            
            // when LinkPolicy.mode == SELECTING

            first_x = x;
            first_y = y;
            last_x = first_x;
            last_y = first_y;

            boolean multipleSelection = false;
            if (e.getModifiers() == 17)
            {
                multipleSelection = true;
            }

            for (int i = 0; i < discs.length; i++)
            {
                if (discs[i].contains(first_x, first_y))
                {
                    
                    if (e.getClickCount() == 2)
                    {
                        Color c = JColorChooser.showDialog(DotPlot.this, "Palette", Color.black);
                        if (c != null && data.getState(indices[i]))
                            data.setColor(c, indices[i]);
                        return;
                    }

                    if (!multipleSelection)
                        data.clearStates();

                    boolean bl = data.getState(indices[i]);
                    data.setState(!bl, indices[i]);
                    return;
                }
            }

            
            data.clearStates();

            draggingStarted = true;
        }

        public void mouseReleased(MouseEvent e)
        {

            if (draggingStarted)
            {
                Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);

                boolean[] states = data.getStates();
                for (int i = 0; i < discs.length; i++)
                {
                    if (rect.contains(discs[i].getBounds()))
                    {
                        //data.setState(true, indices[i]);
                    	states[indices[i]] = true;
                    }
                }
                data.setStates(states);
            }

            draggingStarted = false;
            DotPlot.this.repaint();
        }

        public void mouseDragged(MouseEvent e)
        {

            last_x = e.getX();
            last_y = e.getY();
            
            if (draggingStarted && LinkPolicy.mode != LinkPolicy.SELECTING)
            {
            	if (!e.isAltDown())
            	{	
            		first_x = last_x - brushWidth;
            		first_y = last_y - brushHeight;
            	} else {
            		brushWidth = (last_x - first_x > 10 ? last_x - first_x : 10);
            		brushHeight = (last_y - first_y > 10 ? last_y - first_y : 10);
            	}
            	
            	boolean[] states = data.getStates();
                if (LinkPolicy.mode == LinkPolicy.BRUSHING && !e.isShiftDown()) 
                	Arrays.fill(states, false); //data.clearStates();
                    
                Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);

                for (int i = 0; i < discs.length; i++)
                {
                    if (rect.contains(discs[i].getBounds()))
                    {
                        //data.setState(true, indices[i]);
                        states[indices[i]] = true;
                    }
                }
                data.setStates(states);
                
            }     

            DotPlot.this.repaint();
        }

        public void mouseMoved(MouseEvent e)
        {
        }
    }


}


