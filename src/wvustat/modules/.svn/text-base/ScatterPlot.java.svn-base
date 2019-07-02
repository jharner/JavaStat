package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.rmi.*;
import java.text.*;
import java.util.*;

import wvustat.interfaces.*;
import wvustat.util.MathUtils;
import wvustat.dist.StudentT;
import wvustat.math.expression.ExecError;
import wvustat.math.expression.Expression;
import wvustat.plot.AxisConfigPanel;
import wvustat.plot.ManualAxisModel;
import wvustat.statistics.Loess;
import wvustat.statistics.InvalidDataError;
import wvustat.swing.SimpleSlider;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;
import wvustat.swing.MarkerShape;


/**
 *	ScatterPlot is used in the RegressionModule. It plots data points as discs and optionally draws
 *	regression line or correlation ellipse. Discs are be dragged using a mouse and the underlying
 * data will be changed. However, the change doesn't affect the remote data set.
 *
 *
 */
public class ScatterPlot extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	
    private DataSet data;
    private Variable y_var, x_var, z_var;
    private Vector vz;

    private int width = 320, height = 220;

    private double xmin, xmax, ymin, ymax;
    private BaseAxisModel xAxisModel, yAxisModel;

    private CoordConverter coord;

    //private Ellipse2D[] discs = new Ellipse2D[0];
    private Shape[] discs = new Shape[0];

    private double yvals[], xvals[];

    private int[] indices;

    private Insets insets;

    private FontMetrics metrics;

    //private GroupMaker grouper;
    private EqualCountGrouper grouper;

    private int currentIndex = 0;

    //Mouse selection range
    private int first_x, first_y, last_x, last_y, brushWidth = 10, brushHeight = 10;

    private boolean draggingStarted = false;

    private boolean draggingLine = false;

    private double scannerX, upperY, lowerY, midY;

    //pointId is used to store the index of the point (within array discs) being dragged, obsId
    //is used to store the index of the observation in the dataset.
    private int pointId = -1, obsId = -1; 
    
    private boolean showRegression = false, showCorrelation = false,showNonlinear = false,showLoess = false;

    //Regression coefficients and correlation coefficient for original and current data
    private double a, b, a0, b0;

    //Standard deviation for x, y and average sum of residual
    private double Sx, Sy, Sr;

    private double xbar, ybar, rou;

    //This array stores all regression statistics
    private double[] regStat = new double[11];

    private boolean firstRun = true;

    private EventListenerList eList = new EventListenerList();
    private ChangeEvent event;

    //This boolean variable correponds to the "95% Confidence Limits" option
    private boolean drawCI = false;

    //This variable is the 97.5% quantile of t distribution
    private double t0025;

    private double alpha = 0.025;

    private boolean drawEllipse = false;

    private boolean allowPointDrag = false;

    private Expression fittedFunc;
    private Hashtable paramTable;
    private Rectangle xAxisRegion, yAxisRegion;
    private Loess loessFit;
    private boolean loessFitExist = false;
    private double loessSpan = 0.75;  //initial value for smoothing parameter of Loess
    
    private final double maxLoessSpan = 1.0;  //maximum value for smoothing parameter of Loess
    private final double minLoessSpan = 0.0;  //minimum value for smoothing parameter of Loess
    
    private SimpleSlider slider;
    private final int sliderTop = 5;
    
    private Variable init_y_var = null, init_x_var = null; //added by djluo for transformation
    
    private GUIModule module;
    private GeneralPath popupArrow;
    
    private boolean plotState = true;
    private long time;

    /**
     * Constructor
     * Creates a new ScatterPlot with the given data set and its variables
     */
    public ScatterPlot(DataSet data, Variable y_var, Variable x_var, Vector vz)
    {
        this.data = data;
        this.y_var = y_var;
        this.x_var = x_var;
        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        
        this.init_y_var = y_var;
        this.init_x_var = x_var;
        
        Font font = new Font("Monospaced", Font.PLAIN, 11);
        metrics = getFontMetrics(font);
        //metrics = getFontMetrics(getFont());

        retrieveData();

        setBackground(Color.white);

        slider = new SimpleSlider((float)minLoessSpan, (float)maxLoessSpan, (float)loessSpan);
		slider.setFractionDigits(2);
		slider.setLabel("Loess Bandwidth");
		slider.addChangeListener(this);	
		slider.setVisible(false);
		
		setLayout(null);
		add(slider);		

        MouseEventHandler handler = new MouseEventHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);

        initPlot();
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }

    public Expression getFittedFunction()
    {
        return fittedFunc;
    }

    public void enablePointDrag(boolean value)
    {
        this.allowPointDrag = value;
    }

    public void addActionListener(ActionListener l)
    {
        eList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l)
    {
        eList.remove(ActionListener.class, l);
    }

    protected void fireStateChanged()
    {
        Object[] list = eList.getListenerList();
        for (int i = list.length - 2; i >= 0; i -= 2)
        {
            if (list[i] == ChangeListener.class)
            {
                if (event == null)
                    event = new ChangeEvent(this);
                ((ChangeListener) list[i + 1]).stateChanged(event);
            }
        }
    }

    private void fireActionEvent(String actionCmd)
    {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCmd);
        Object[] list = eList.getListenerList();
        for (int i = list.length - 2; i >= 0; i -= 2)
        {
            if (list[i] == ActionListener.class)
            {
                ((ActionListener) list[i + 1]).actionPerformed(evt);
            }
        }
    }


    /**
     * Get the size of this component
     */
    public Dimension getSize()
    {
        return new Dimension(width, height);
    }

    public Dimension getPreferredSize()
    {
        return getSize();
    }

    /**
     * This method is overriden so that this component can be resized dynamically
     */

    public void setBounds(int x, int y, int w, int h)
    {
        width = w;
        height = h;
        initPlot();
        
        Dimension size = slider.getPreferredSize();
		slider.setBounds((w - size.width) / 2, sliderTop, size.width, size.height);
		
        super.setBounds(x, y, w, h);
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }



    private void retrieveData()
    {
        try
        {
        	plotState = true;
        	
            double[] ytmp = y_var.getNumValues();
            double[] xtmp = x_var.getNumValues();

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
                    Double dx = new Double(xtmp[i]);
                    if (grouper == null)
                    {
                        vy.addElement(dy);
                        vx.addElement(dx);
                        iv.addElement(new Integer(i));
                    }
                    else if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
                    {
                        vy.addElement(dy);
                        vx.addElement(dx);
                        iv.addElement(new Integer(i));
                    }
                }
            }

            yvals = new double[vy.size()];
            xvals = new double[vx.size()];
            indices = new int[iv.size()];

            for (int i = 0; i < yvals.length; i++)
            {
                yvals[i] = ((Double) vy.elementAt(i)).doubleValue();
                xvals[i] = ((Double) vx.elementAt(i)).doubleValue();
                indices[i] = ((Integer) iv.elementAt(i)).intValue();
            }

            StatEngine se = new StatEngineImpl();
            Distribution t = se.getDistribution("t", new double[]{yvals.length - 2});

            t0025 = t.quantile(1 - alpha);

        }
        catch (RemoteException re)
        {
        	plotState = false;
        	System.err.println(re.getMessage());
        }
    }

    public String getMomentsRpt()
    {
        double yMean = MathUtils.getMean(yvals);
        double xMean = MathUtils.getMean(xvals);
        double ySE = MathUtils.getSE(yvals);
        double xSE = MathUtils.getSE(xvals);
        double ySD=MathUtils.getStdDev(yvals);
        double xSD=MathUtils.getStdDev(xvals);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        StringBuffer buf = new StringBuffer();
        buf.append("<html><body><table width=\"100%\">\n");
        buf.append("<tr><td width=\"25%\"></td><td width=\"25%\"><font size=3>Mean</font></td><td width=\"25%\"><font size=3>SD</font></td><td width=\"25%\"><font size=3>SE</font></td></tr>");

        buf.append("<tr><td><font size=3>").append( x_var.getName()).append("</font></td><td><font size=3>");
        buf.append(nf.format(xMean)).append("</font></td><td><font size=3>");
        buf.append(nf.format(xSD)).append("</font></td><td><font size=3>");
        buf.append(nf.format(xSE)).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>");
        buf.append(y_var.getName());
        buf.append("</font></td><td><font size=3>");
        buf.append(nf.format(yMean)).append("</font></td><td><font size=3>");
        buf.append(nf.format(ySD)).append("</font></td><td><font size=3>");
        buf.append(nf.format(ySE)).append("</font></td></tr>");

        buf.append("</table>\n");
        buf.append("<font size=3>n = ").append(yvals.length);
        buf.append("</font></body></html>");
        return buf.toString();
    }
    
    public String getLoessRpt()
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        StringBuffer buf = new StringBuffer();
        buf.append("<html><body><table width=\"100%\">\n");
        buf.append("<tr><td width=\"40%\"><font size=3>Loess Bandwidth:</font></td>");
        buf.append("<td width=\"60%\"><font size=3>").append(nf.format(loessSpan)).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>Degree of Polynomials:</font></td><td><font size=3>1</font></td></tr>");
        buf.append("<tr><td><font size=3>Loess Fit:</font></td>");
        buf.append("<td><font size=3>").append(loessFitExist?"Estimable":"Not Estimable").append("</font></td></tr>");
        buf.append("</table>\n");
        buf.append("</body></html>");
        return buf.toString();
    }

    public String getRegressionRpt()
    {
        StringBuffer buf = new StringBuffer();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);

        buf.append("<html><body bgcolor=\"white\">\n");
        buf.append("<font size=3><b>Model</b></font>");
        buf.append("<br><font size=3>" + y_var.getName() + "=b<sub><font size=2>0</font></sub>+b<sub><font size=2>1</font></sub>*" + x_var.getName());
        buf.append(" (R Square=" + nf.format(regStat[2] * regStat[2]) + ")</font>");

        StudentT tdist = new StudentT(yvals.length - 2);

        buf.append("<table width=\"100%\">\n");
        buf.append("<tr><th width=\"20%\" align=\"left\"><font size=3>Parameter</font></th>");
        buf.append("<th width=\"22%\" align=\"right\"><font size=3>Estimate</font></th>");
        buf.append("<th width=\"18%\" align=\"right\"><font size=3>SE</font></th>");
        buf.append("<th width=\"20%\" align=\"right\"><font size=3>t</font></th>");
        buf.append("<th width=\"20%\" align=\"right\"><font size=3>P-value</font></th></tr>\n");

        double se0 = /*regStat[8] / Math.sqrt(yvals.length);*/
        			Math.sqrt(regStat[9]*(1.0/yvals.length + regStat[5]*regStat[5]/regStat[7]/regStat[7]/(yvals.length-1)));
        
        double tval = a / se0;
        double pval = 2 * (1 - tdist.cdf(Math.abs(tval)));
        buf.append("<tr><td align=\"left\"><font size=3>Intercept</font></td><td align=\"right\"><font size=3>" + nf.format(a) + "</font></td><td align=\"right\"><font size=3>" + nf.format(se0) + "</font></td><td align=\"right\"><font size=3>" + nf.format(tval) + "</font></td><td align=\"right\"><font size=3>");
        if (pval > 1e-6)
        {
            nf.setMaximumFractionDigits(6);
            buf.append(nf.format(pval) + "</font></td></tr>");
            nf.setMaximumFractionDigits(3);
        }
        else
            buf.append("&lt 1e-6</font></td></tr>");

        tval = b / regStat[3];
        buf.append("<tr><td align=\"left\"><font size=3>Slope</font></td><td align=\"right\"><font size=3>" + nf.format(b) + "</font></td><td align=\"right\"><font size=3>" + nf.format(regStat[3]) + "</font></td><td align=\"right\"><font size=3>" + nf.format(tval) + "</font></td><td align=\"right\"><font size=3>");
        if (regStat[4] > 1e-6)
        {
            nf.setMaximumFractionDigits(6);
            buf.append(nf.format(regStat[4]) + "</font></td></tr>");
        }
        else
            buf.append("&lt 1e-6</font></td></tr>");
        
        buf.append("</table>\n");
        buf.append("</body></html>");

        return buf.toString();
    }

    public double[] getYValues()
    {
        return yvals;
    }

    public double[] getXValues()
    {
        return xvals;
    }

    private void initPlot()
    {
        try
        {
            StatEngine se = new StatEngineImpl();

            xmin = x_var.getMin();
            xmax = x_var.getMax();
            
            ymin = y_var.getMin();
            ymax = y_var.getMax();

            //Ask the server to perform regression
            regStat = se.regress(yvals, xvals);
            a = regStat[0];
            b = regStat[1];
            rou = regStat[2];

            xbar = regStat[5];
            ybar = regStat[6];

            Sx = regStat[7];
            Sy = regStat[8];
            Sr = regStat[9];

            if (firstRun)
            {
                a0 = a;
                b0 = b;
                scannerX = xbar;
            }

            if(xAxisModel == null || !xAxisModel.isManual())
                xAxisModel = new BaseAxisModel(xmin, xmax, 5);
            if(yAxisModel == null || !yAxisModel.isManual())
                yAxisModel = new BaseAxisModel(ymin, ymax, 5);

            configElements();
        }
        catch (RemoteException re)
        {
        	plotState = false;
            System.err.println(re.getMessage());
        }
    }

    private void configElements()
    {
        xmin = xAxisModel.getStartValue();
        xmax = xAxisModel.getEndValue();

        ymin = yAxisModel.getStartValue();
        ymax = yAxisModel.getEndValue();


        //Define margins around the plot
        NumberFormat inst = NumberFormat.getInstance();
        insets = new Insets(30, 40, 40, 20);
        insets.left = metrics.stringWidth(inst.format(ymax)) + 8;
        //Calculate left margin for each ticmark
        double tmp = ymax;
        while(tmp >= ymin){
        	int len = metrics.stringWidth(inst.format(tmp)) + 8;
        	if(insets.left < len) insets.left = len;
        	tmp -= yAxisModel.getInterval();
        }
        if (showLoess) insets.top = 50;

        coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);

        
    }

    public BaseAxisModel getXAxisModel()
    {
        return xAxisModel;
    }

    public void setXAxisModel(BaseAxisModel axisModel)
    {
        xAxisModel = axisModel;
        configElements();
        repaint();
    }

    public BaseAxisModel getYAxisModel()
    {
        return yAxisModel;
    }

    public void setYAxisModel(BaseAxisModel axisModel)
    {
        yAxisModel = axisModel;
        configElements();
        repaint();
    }

    /**
     *	This method returns regression statistics in an array. The array contains
     *	reg. coefficients a, b, corr. coeff. rou and standard error of b.
     *
     */
    public double[] getRegressionStatistics()
    {
        return regStat;
    }

    public void movePoint(int index, double x, double y)
    {
        xvals[index] = x;
        yvals[index] = y;
        initPlot();
        repaint();
    }

    public void moveScannerLine(double x)
    {
        this.scannerX = x;
        repaint();
    }

    /**
     * Rese the plot to its original stat
     */
    public void resetPlot()
    {
        retrieveData();
        initPlot();
        repaint();
    }

    /**
     * Set the current group to be the indexed group
     */
    public void setGroup(int index)
    {

        if (index < 0 || index >= grouper.getGroupCount())
            return;

        currentIndex = index;
        retrieveData();
        firstRun = true;
        initPlot();
        enableCI(drawCI);
        repaint();
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));

        Stroke thinLine = new BasicStroke(1.0f);
        //Stroke thickLine = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

        //Draw two lines (for decoration purpose, maybe), one at the top. one at the bottom
        //g2.drawLine(0, 0, width - 1, 0);
        //g2.drawLine(0, height - 1, width - 1, height - 1);
        
        if (!plotState) {
            String errString = "Error getting data, please try again later.";
            g2.drawString(errString, (int) (getSize().width / 2.0 - metrics.stringWidth(errString) / 2.0),
                    (int) (getSize().height / 2.0 - metrics.getHeight() / 2.0));
            return;
        }
                
        g2.setColor(Color.black);
        g2.fill(popupArrow);

        //draw x axis
        XAxisRenderer xaxis = new XAxisRenderer(xAxisModel, getSize(), insets);
        xaxis.setRightGap(10);
        xaxis.setLabel(x_var.getName());
        SwingUtilities.paintComponent(g2, xaxis, this, insets.left - xaxis.getLeftGap(), 
				getSize().height - insets.bottom, xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        xAxisRegion = new Rectangle(insets.left - xaxis.getLeftGap(), getSize().height - insets.bottom, 
        		xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        
        //draw y axis
        YAxisRenderer yaxis = new YAxisRenderer(yAxisModel, getSize(), insets);
        yaxis.setTopGap(10);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		g2.drawString(y_var.getName(), yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());
		yAxisRegion = new Rectangle(0, insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
        
		
        NumberFormat nf = new DecimalFormat("#######.###");
        nf.setMaximumFractionDigits(3);

        //discs = new Ellipse2D[yvals.length];
        discs = new Shape[yvals.length];

        for (int i = 0; i < discs.length; i++)
        {
            //discs[i] = new Ellipse2D.Float(coord.x(xvals[i]) - 2, coord.y(yvals[i]) - 2, 4, 4);
        	discs[i] = new MarkerShape(coord.x(xvals[i]), coord.y(yvals[i]), data.getMarker(indices[i]).intValue());
        }
        
        //Draw individual data points
        
        for (int i = 0; i < discs.length; i++)
        {
            g2.setColor(data.getColor(indices[i]));
            if (data.getState(indices[i]))
            {
            	g2.draw(discs[i]);
                g2.fill(discs[i]);
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
        

        if (draggingStarted)
        {
            Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);
            g2.setColor(Color.lightGray);
            g2.draw(rect);
        }

        if (pointId != -1)
        {
            g2.setColor(Color.gray);
            g2.drawLine(coord.x(xvals[pointId]), coord.y(yvals[pointId]), coord.x(xvals[pointId]), coord.y(ymin));
            g2.drawLine(coord.x(xvals[pointId]), coord.y(yvals[pointId]), coord.x(xmin), coord.y(yvals[pointId]));

            g2.drawString(nf.format(xvals[pointId]), coord.x(xvals[pointId]) + 4, coord.y(ymin) - metrics.getHeight());

            g2.drawString(nf.format(yvals[pointId]), coord.x(xmin) + 4, coord.y(yvals[pointId]) - 2);
        }

        //Draw regression line
        if (showRegression)
        {
            g2.setColor(Color.gray);

            drawRangedLine(g2, a0, b0);

            g2.setColor(Color.red);

            drawRangedLine(g2, a, b);
        }

        //Optionally draw confidence band for y
        if (drawCI)
        {

            g2.setColor(Color.green);

            int cnt = 20;

            int x0, y01, y02, x1, y11, y12;

            double[] ci = getLimits(xmin);
            x0 = coord.x(xmin);
            y01 = coord.y(ci[0]);
            y02 = coord.y(ci[1]);

            double seg = (xmax - xmin) / cnt;

            //Draw segments of confidence band lines
            for (int i = 1; i <= cnt; i++)
            {
                double tmpx = xmin + i * seg;
                x1 = coord.x(tmpx);
                ci = getLimits(tmpx);
                y11 = coord.y(ci[0]);
                y12 = coord.y(ci[1]);

                g2.drawLine(x0, y01, x1, y11);
                g2.drawLine(x0, y02, x1, y12);

                x0 = x1;
                y01 = y11;
                y02 = y12;
            }

            ci = getLimits(scannerX);
            upperY = ci[0];
            lowerY = ci[1];

            g2.setColor(Color.blue);

            //Draw a vertical line between confidence lines to reveal values
            g2.drawLine(coord.x(scannerX), coord.y(upperY), coord.x(scannerX), coord.y(lowerY));
            //Show the values
            nf.setMaximumFractionDigits(3);
            g2.drawString(nf.format(upperY), coord.x(scannerX), coord.y(upperY) - 2);
            g2.drawString(nf.format(lowerY), coord.x(scannerX), coord.y(lowerY) + metrics.getHeight());

            BasicStroke dashed = new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, new float[]{10.0f}, 0.0f);

            if (draggingLine)
            {
                g2.setStroke(dashed);
                midY = scannerX * b + a;
                g2.drawLine(coord.x(scannerX), coord.y(midY), coord.x(xmin), coord.y(midY));

                g2.drawLine(coord.x(scannerX), coord.y(midY), coord.x(scannerX), coord.y(ymin));

                g2.setStroke(thinLine);
                g2.drawString(nf.format(scannerX), coord.x(scannerX) + 2, coord.y(ymin) - 2);

                g2.drawString(nf.format(midY), coord.x(xmin) + 2, coord.y(midY));
            }
        }

        //Draw correlation ellipse
        if (drawEllipse)
        {
            g2.setColor(Color.green);

            int cnt = 40;

            int x0, y01, y02, x1 = 0, y11 = 0, y12 = 0;

            //determin end points for the ellipse
            double c = 7.38;
            double exmin = -Math.sqrt(c) * Sx + xbar;
            double exmax = Math.sqrt(c) * Sx + xbar;

            double[] ci = getEllipse(exmin);
            x0 = coord.x(exmin);
            //double tmpy = 2 * rou * (exmin - xbar) / Sx * Sy + ybar;

            y01 = coord.y(ci[0]);
            y02 = coord.y(ci[1]);

            double seg = (exmax - exmin) / cnt;

            for (int i = 1; i <= cnt; i++)
            {
                double tmpx = exmin + i * seg;
                x1 = coord.x(tmpx);
                ci = getEllipse(tmpx);
                y11 = coord.y(ci[0]);
                y12 = coord.y(ci[1]);

                if (y01 <= coord.y(ymin) && y11 <= coord.y(ymin) && x0 > coord.x(xmin) && x1 < coord.x(xmax))
                    g2.drawLine(x0, y01, x1, y11);
                if (y02 <= coord.y(ymin) && y12 <= coord.y(ymin) && x0 > coord.x(xmin) && x1 < coord.x(xmax))
                    g2.drawLine(x0, y02, x1, y12);

                x0 = x1;
                y01 = y11;
                y02 = y12;
            }

        }

        if (showLoess && !slider.getValueIsAdjusting()){
            g2.setPaint(Color.blue);
            int segments = 100;
            double seg_len = (xmax - xmin) / segments;
            double tmpx0, tmpx1, tmpy0, tmpy1;
            
            try{
                loessFit = new Loess(loessSpan, xvals, yvals); 
            	
                tmpx0 = xmin;
                tmpy0 = loessFit.getFit(tmpx0);

                double[][] loessArray = new double[2][101];
                loessArray[0][0] = tmpx0;
                loessArray[1][0] = tmpy0;
                
                for (int i = 0; i < segments; i++)
                {
                    tmpx1 = tmpx0 + seg_len;
                    tmpy1 = loessFit.getFit(tmpx1);
                    
                    loessArray[0][i+1] = tmpx1;
                    loessArray[1][i+1] = tmpy1;
                    
                    tmpx0 = tmpx1;
                    tmpy0 = tmpy1;
                }
                
                for (int i = 0; i < loessArray[0].length - 1; i++) {
                	   tmpx0 = loessArray[0][i];
                	   tmpy0 = loessArray[1][i];
                    tmpx1 = loessArray[0][i+1];
                    tmpy1 = loessArray[1][i+1];
                                
                    if ((tmpy0 <= ymax && tmpy0 >= ymin) || 
             	       (tmpy1 <= ymax && tmpy1 >= ymin))
                        //g2.drawLine(coord.x(tmpx0), coord.y(tmpy0), coord.x(tmpx1), coord.y(tmpy1));
                        drawRangedSegment(g2, tmpx0, tmpy0, tmpx1, tmpy1);
                }
                
                loessFitExist = true;
                 
            }catch(InvalidDataError err){
            		loessFitExist = false; //cannot fit, do not draw loess
            }
            
            String actionCmd = "move loess";
            fireActionEvent(actionCmd);
            
        }

        
        if (showLoess){
            g2.setPaint(Color.black);
            Font font = new Font("Monospaced", Font.PLAIN, 11);
            FontMetrics metrics = getFontMetrics(font);
            
            g2.drawString(slider.getLabel(), (getSize().width + slider.getSize().width) / 2 + slider.getGap(), slider.getLocation().y + slider.getSize().height);
    		g2.drawString(slider.getTextValue(), slider.getLocation().x - metrics.stringWidth(slider.getTextValue()) - slider.getGap(), slider.getLocation().y + slider.getSize().height);
            
        }
        
        
        
        if (showNonlinear)
        {
            g2.setPaint(Color.blue);
            int segments = 200;
            double seg_len = (xmax - xmin) / segments;
            double tmpx0, tmpx1, tmpy0, tmpy1;
            tmpx0 = xmin;

            try
            {
                paramTable.put(x_var.getName(), new Double(tmpx0));
                tmpy0 = fittedFunc.value(paramTable);

                for (int i = 0; i < segments; i++)
                {
                    tmpx1 = tmpx0 + seg_len;
                    paramTable.put(x_var.getName(), new Double(tmpx1));
                    tmpy1 = fittedFunc.value(paramTable);
                    g2.drawLine(coord.x(tmpx0), coord.y(tmpy0), coord.x(tmpx1), coord.y(tmpy1));
                    tmpx0 = tmpx1;
                    tmpy0 = tmpy1;
                }
            }
            catch (ExecError err)
            {
                err.printStackTrace();
            }

        }


        firstRun = false;

    }

    /**
     * Toggle the "Draw Confidence limits" option on/off
     */
    public void enableCI(boolean val)
    {
        drawCI = val;
        if(val && !(yAxisModel instanceof ManualAxisModel))
        {
            double[] leftPair=getLimits(xmin);
            double[] rightPair=getLimits(xmax);
            ymin=leftPair[1]<rightPair[1]?leftPair[1]:rightPair[1];
            ymax=leftPair[0]>rightPair[0]?leftPair[0]:rightPair[0];
            yAxisModel=new BaseAxisModel(ymin, ymax, 5);
            setYAxisModel(yAxisModel);
        }
        repaint();
    }

    /**
     * Toggle the draw "Confidence ellipse" option on/off
     */
    public void enableEllipse(boolean val)
    {
        drawEllipse = val;
        repaint();
    }

    /**
     *	This method draws a line within the coordinates axis
     */
    private void drawRangedLine(Graphics g, double a, double b)
    {

        int x0,y0,x1,y1;
        double tmpx;
        tmpx = (ymin - a) / b;

        if (tmpx < xmin)
        {
            x0 = coord.x(xmin);
            y0 = coord.y(a + b * xmin);
        }
        else if (tmpx > xmax)
        {
            x0 = coord.x(xmax);
            y0 = coord.y(a + b * xmax);
        }
        else
        {
            x0 = coord.x(tmpx);
            y0 = coord.y(ymin);
        }

        tmpx = (ymax - a) / b;

        if (tmpx < xmin)
        {
            x1 = coord.x(xmin);
            y1 = coord.y(a + b * xmin);
        }
        else if (tmpx > xmax)
        {
            x1 = coord.x(xmax);
            y1 = coord.y(a + b * xmax);
        }
        else
        {
            x1 = coord.x(tmpx);
            y1 = coord.y(ymax);
        }


        g.drawLine(x0, y0, x1, y1);

    }
    
    private void drawRangedSegment(Graphics g2, double px0, double py0, double px1, double py1)
    {
    	double a,b;
    	b = (py0 - py1) / (px0 - px1);
    	a = py0 - b * px0;
    		
        int x0,y0,x1,y1;
        double tmpx;
        tmpx = (ymin - a) / b;

        if (tmpx < px0)
        {
            x0 = coord.x(px0);
            y0 = coord.y(a + b * px0);
        }
        else if (tmpx > px1)
        {
            x0 = coord.x(px1);
            y0 = coord.y(a + b * px1);
        }
        else
        {
            x0 = coord.x(tmpx);
            y0 = coord.y(ymin);
        }

        tmpx = (ymax - a) / b;

        if (tmpx < px0)
        {
            x1 = coord.x(px0);
            y1 = coord.y(a + b * px0);
        }
        else if (tmpx > px1)
        {
            x1 = coord.x(px1);
            y1 = coord.y(a + b * px1);
        }
        else
        {
            x1 = coord.x(tmpx);
            y1 = coord.y(ymax);
        }

        g2.drawLine(x0, y0, x1, y1);        		
    }

    /**
     * Plot regression specific stuff
     */
    public void showRegression()
    {
        showRegression = true;
        showCorrelation = false;
        showNonlinear = false;
        showLoess = false;
        slider.setVisible(false);
        drawEllipse = false;
        initPlot();
        repaint();
    }

    public void showLoessFit()
    {
        showLoess = true;
        slider.setVisible(true);
        showRegression = false;
        showCorrelation = false;
        showNonlinear = false;
        drawEllipse = false;
        drawCI = false;
        initPlot();
        repaint();    	
    }
    
    public void setLoessSpan(double span){
    	if (span <= maxLoessSpan && span >= getMinLoessSpan()){
            loessSpan = span;
            initPlot();
            repaint();    	
    	}
    }
    
    public double getLoessSpan(){
        return loessSpan;
    }   
    
    public double getMinLoessSpan(){
        //return loessFit.getMinSpan();
    	return minLoessSpan;
    }
    
    /**
     * Plot regression specific stuff
     */
    public void showCorrelation()
    {
        showRegression = false;
        showCorrelation = true;
        showNonlinear = false;
        showLoess = false;
        slider.setVisible(false);
        drawCI = false;
        initPlot();
        repaint();
    }

    public void showNonlinearFit()
    {
        showNonlinear = true;
        showRegression = false;
        showCorrelation = false;
        showLoess = false;
        slider.setVisible(false);
        drawEllipse = false;
        drawCI = false;
        initPlot();
        repaint();
    }

    public void setNonlinearFit(Expression function, Hashtable paramTable)
    {
        this.fittedFunc = function;
        this.paramTable = paramTable;
        showNonlinear = true;
        showRegression = false;
        showCorrelation = false;
        showLoess = false;
        slider.setVisible(false);
        drawEllipse = false;
        drawCI = false;
        initPlot();
        repaint();
    }

    /**
     * Get the GroupMaker object
     */
    public EqualCountGrouper getGroupMaker()
    {
        return grouper;
    }

    public void stateChanged(ChangeEvent e) {
    	Object obj = e.getSource();
    	
    	if (obj instanceof SimpleSlider) 
    		setLoessSpan(slider.getValue());
    }
    
    
    /**
     * update plot according to message
     *
     * @param arg the message about what has changed.
     */
    public void updatePlot(String arg)
    {
        if (arg.equals("yystate"))
        {
            repaint();
        }
        else if (arg.equals("yycolor"))
        {
            repaint();
        }
        else if (arg.equals("yymask"))
        {
            retrieveData();
            firstRun = true;
            initPlot();
            repaint();
        }
        else if (arg.equals("obs_deleted"))
        {
            retrieveData();
            firstRun = true;
            initPlot();
            repaint();
        }
        else if (arg.equals("add_variable")||arg.equals("delete_variable")) {
            
    	    if(data.getTransformedVar(init_y_var.getName()) != null)
    		    y_var = data.getTransformedVar(init_y_var.getName());
    	    else
    		    y_var = init_y_var;
    	    if(data.getTransformedVar(init_x_var.getName()) != null)
    		    x_var = data.getTransformedVar(init_x_var.getName());
    	    else
    		    x_var = init_x_var;
    	    retrieveData();
    	    firstRun = true;
    	    initPlot();
    	    repaint();        
        }
    }

    //This method is used to compute confidence limits for predicted y based on x
    private double[] getLimits(double x)
    {
        double[] ret = null;

        int n = yvals.length;

        double band = t0025 * Math.sqrt(Sr / n + (x - xbar) * (x - xbar) * Sr / (Sx * Sx * (n - 1)));

        ret = new double[2];
        ret[0] = a + b * x + band;
        ret[1] = a + b * x - band;

        return ret;

    }

    //This method finds the two y values corresponding to a given x on a ellipse
    private double[] getEllipse(double x)
    {
        double[] ret = new double[2];

        double c = 7.38;

        x = (x - xbar) / Sx;

        double det = Math.pow(2 * rou * x, 2) - 4 * (x * x - (1 - rou * rou) * c);

        if (Math.abs(det) < 1e-6)
            det = 0;
        else
            det = Math.sqrt(det);

        ret[0] = (2 * rou * x + det) / 2.0 * Sy + ybar;
        ret[1] = (2 * rou * x - det) / 2.0 * Sy + ybar;

        return ret;
    }
    
    

    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2 && xAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(xAxisModel);
                int option = JOptionPane.showOptionDialog(ScatterPlot.this, configPanel,
                        "x-axis settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if (option == JOptionPane.OK_OPTION)
                {
                    BaseAxisModel model = configPanel.getAxisModel();
                    if (model != null)
                    {
                        setXAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(ScatterPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getClickCount() == 2 && yAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(yAxisModel);
                int option = JOptionPane.showOptionDialog(ScatterPlot.this, configPanel,
                        "y-axis settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if (option == JOptionPane.OK_OPTION)
                {
                    BaseAxisModel model = configPanel.getAxisModel();
                    if (model != null)
                    {
                        setYAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(ScatterPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        }

        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
        			JPopupMenu popupOptionMenu = ((RegressionModule) module).getOptionMenu().getPopupMenu();
        			popupOptionMenu.show(e.getComponent(), x, y);
        			return;
            }

            
            if (drawCI && Math.abs(x - coord.x(scannerX)) < 2 && y > coord.y(upperY) && y < coord.y(lowerY))
            {
                draggingLine = true;
                return;
            }
            
            time = (new Date()).getTime();
            
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
                ScatterPlot.this.repaint();
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
                        Color c = JColorChooser.showDialog(ScatterPlot.this, "Palette", Color.black);
                        if (c != null && data.getState(indices[i]))
                            data.setColor(c, indices[i]);
                        return;
                    }

                    if (!multipleSelection)
                        data.clearStates();

                    boolean bl = data.getState(indices[i]);
                    data.setState(!bl, indices[i]);
                    obsId = indices[i];
                    pointId = i;
                    return;
                    
                }
            }

            // when no point is selected
            
            data.clearStates();
            

            draggingStarted = true;
        }

        public void mouseReleased(MouseEvent e)
        {

            if (pointId != -1 && allowPointDrag)
            {
                double xval = coord.inverseX(e.getX());
                double yval = coord.inverseY(e.getY());
                String actionCmd = "move point:" + pointId + "," + (float) xval + "," + (float) yval;

                fireActionEvent(actionCmd);
                //fireStateChanged();
            }
            else if (draggingLine)
            {
                String actionCmd = "move scanner:" + (float) scannerX;
                fireActionEvent(actionCmd);
            }
            draggingLine = false;
            pointId = -1;
            obsId = -1;

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
            ScatterPlot.this.repaint();
        }

        public void mouseDragged(MouseEvent e)
        {
            if (draggingLine)
            {
                double val = coord.inverseX(e.getX());
                moveScannerLine(val);
                return;
            }

            if (pointId != -1 && allowPointDrag)
            {
                double xval = coord.inverseX(e.getX());
                double yval = coord.inverseY(e.getY());

                movePoint(pointId, xval, yval);
                return;
            }

            long now = (new Date()).getTime();
            if ((now - time) > 300) 
            	time = now;
            else
            	return;
            
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
                        states[indices[i]] = true;//data.setState(true, indices[i]);
                    }
                }
                data.setStates(states);
                
            }            

            ScatterPlot.this.repaint();
        }

        public void mouseMoved(MouseEvent e)
        {
        }

    }
    

}