package wvustat.modules.rmodels;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.rmi.*;
import java.text.*;
import java.util.*;

import wvustat.interfaces.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.statistics.InvalidDataError;
import wvustat.statistics.Loess;
import wvustat.swing.SimpleSlider;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;
import wvustat.swing.MarkerShape;
import wvustat.network.*;
import wvustat.modules.*;


/**
 *	ResidualsPlot is used in the LinearModelModule. It plots residuals as discs.
 *
 */
public class ResidualsPlot extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 1L;
	
	/**
	* Constant for property 'Type'
	*/
	public static final int RESIDUALVSFITTED = 0;
	public static final int LEVERAGEPLOT = 1;
	public static final int PARTIALRESIDUAL = 2;
		
	private int type = 0;
	
	private int termIndex = 0;
	
    private DataSet data;
    
    private RLinearModel rlm;

    private int width = 320, height = 220;

    private double xmin, xmax, ymin, ymax;
    private BaseAxisModel xAxisModel, yAxisModel;

    private CoordConverter coord;

    private Shape[] discs;

    private double yvals[], xvals[];

    private int[] indices;

    private Insets insets;

    private FontMetrics metrics;
    
    private EqualCountGrouper grouper;
    private int currentIndex = 0;

    //Mouse selection range
    private int first_x, first_y, last_x, last_y, brushWidth = 10, brushHeight = 10;

    private boolean draggingStarted = false;
    
    
    //Regression coefficients and correlation coefficient for data
    private double a, b;
    //Standard deviation for x, y and average sum of residual
    private double Sx, Sr;
    private double xbar, ybar;
    //This array stores all regression statistics
    private double[] regStat;
    private double t0025;
    private double alpha = 0.025;

    private EventListenerList eList = new EventListenerList();

    private Rectangle xAxisRegion, yAxisRegion;

    private GUIModule module;
    private GeneralPath popupArrow;
    
    //variables for loess curve
    private boolean showLoess = false;
    private Loess loessFit;
    private boolean loessFitExist = false;
    private double loessSpan = 0.75;  //initial value for smoothing parameter of Loess
    private final double maxLoessSpan = 1.0;  //maximum value for smoothing parameter of Loess
    private final double minLoessSpan = 0.0;  //minimum value for smoothing parameter of Loess
    
    private SimpleSlider slider;
    private final int sliderTop = 5;
    
    private boolean plotState = true;


    /**
     * Constructor
     * Creates a new plot with the given data set
     */
    public ResidualsPlot(DataSet data, Vector vx, RLinearModel rlm, EqualCountGrouper grouper, int type)
    {
        this.data = data;
        this.rlm = rlm;
        this.grouper = grouper;        
        this.type = type;
        
        if (type == PARTIALRESIDUAL) termIndex = 1;
                
        Font font = new Font("Monospaced", Font.PLAIN, 11);
        metrics = getFontMetrics(font);

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


    public void addActionListener(ActionListener l)
    {
        eList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l)
    {
        eList.remove(ActionListener.class, l);
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
    
    public void setRLinearModel(RLinearModel rlm){
        this.rlm = rlm;
    }

    private void retrieveData()
    {
        try
        {
        	plotState = true;
        	
            double[] ytmp = null;
            double[] xtmp = null; 
 
            if (type == RESIDUALVSFITTED){
            	if (rlm instanceof GlmModel){
            		ytmp = ((GlmModel)rlm).getDevianceResiduals();
            		xtmp = ((GlmModel)rlm).getLinearPredictors();
            	}else{
            		ytmp = rlm.getResiduals();
            		xtmp = rlm.getFittedValues();
            	}
            }
            else if (type == LEVERAGEPLOT){
            	if (rlm.getAnova().length == 0) {
            		ytmp = new double[0];
            		xtmp = new double[0];
            	} else {      	
            		ytmp = rlm.getLeveragey(termIndex);
            		xtmp = rlm.getLeveragex(termIndex);
            	}
            }
            else if (type == PARTIALRESIDUAL){
            	if (termIndex >= rlm.getCoefficients().length && rlm.getCoefficients().length > 0) 
            		termIndex = 1;
            	
            	double[] res;
            	
            	if (rlm instanceof GlmModel)
            		res = ((GlmModel)rlm).getWorkingResiduals();
            	else
            		res = rlm.getResiduals();
            	
            	if (rlm.getCoefficients().length > 0)
            		xtmp = rlm.getModelMatrix()[termIndex];
            	else
            		xtmp = new double[0];//array with NaN
            	
            	ytmp = new double[xtmp.length];
            	if (rlm.getCoefficients().length > 0)
            		for (int i=0; i<ytmp.length; i++)
            			ytmp[i] = res[i] +  rlm.getCoefficients()[termIndex][0] * xtmp[i];
            	else
            		ytmp = new double[0];//array with NaN
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
                    
                    if (dy.isNaN() || dx.isNaN()) continue;
                    
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
    	boolean isGLM = rlm instanceof GlmModel;
    	
        StringBuffer buf = new StringBuffer();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);

        buf.append("<html><body bgcolor=\"white\">\n");
        buf.append("<font size=3><b>Model</b></font>");
        buf.append("<br><font size=3>"+rlm.getFormula());
        if (!isGLM)
        	buf.append(" (R Square=" + nf.format(rlm.getRsquared()) + ")</font>");
        else
        	buf.append(" (family: " + ((GlmModel)rlm).getFamily() + ", link: " + ((GlmModel)rlm).getLink() + ")</font>");

        
        buf.append("<table width=\"100%\">\n");
        buf.append("<tr><th width=\"25%\" align=\"left\"><font size=3>Parameter</font></th>");
        if (!isGLM){
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>Estimate</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>SE</font></th>");
        	buf.append("<th width=\"15%\" align=\"right\"><font size=3>t</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>P-value</font></th></tr>\n");
        }else{
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>Estimate</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>Std. Error</font></th>");
        	buf.append("<th width=\"15%\" align=\"right\"><font size=3>z value</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>P(>|z|)</font></th></tr>\n");        	
        }
        	
        for (int i=0; i < rlm.getCoefficients().length; i++){
        	if (i == 0)
        		buf.append("<tr><td align=\"left\"><font size=3>" + rlm.getCoefRowNames()[i] + "</font></td>");
        	else
        		buf.append("<tr><td align=\"left\"><font size=3><a href=\"" + i + "\">" + rlm.getCoefRowNames()[i] + "</a></font></td>");
        	
            int j = 0;
            for (j = 0; j < rlm.getCoefColNames().length-1; j++){
            	double tmp = rlm.getCoefficients()[i][j];
                buf.append("<td align=\"right\"><font size=3>" + (!Double.isNaN(tmp)?nf.format(tmp):"") + "</font></td>");
            }
            double pval = rlm.getCoefficients()[i][j];
            buf.append("<td align=\"right\"><font size=3>");
            if (Double.isNaN(pval))
            	buf.append("</font></td></tr>");
            else if (pval > 1e-6)
            {
                nf.setMaximumFractionDigits(6);
                buf.append(nf.format(pval) + "</font></td></tr>");
                nf.setMaximumFractionDigits(3);
            }
            else
                buf.append("&lt 1e-6</font></td></tr>");
            
        }
        
        buf.append("</table>\n");
        buf.append("</body></html>");

        return buf.toString();
    }

    public String getAnovaRpt()
    {
    	boolean isGLM = rlm instanceof GlmModel;
    	
        StringBuffer buf = new StringBuffer();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);

        buf.append("<html><body bgcolor=\"white\">\n");
        buf.append("<font size=3><b>Analysis of Variance Table</b></font>");
        buf.append("<font size=3>&nbsp; (Response: " + rlm.getResponseName() + ")</font>");
        
        
        buf.append("<table width=\"100%\">\n");
        buf.append("<tr><th width=\"25%\" align=\"left\"><font size=3></font></th>");
        if (!isGLM){
        	buf.append("<th width=\"5%\" align=\"right\"><font size=3>Df</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>SS</font></th>");
        	buf.append("<th width=\"15%\" align=\"right\"><font size=3>MS</font></th>");
        	buf.append("<th width=\"15%\" align=\"right\"><font size=3>F</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>P-value</font></th></tr>\n");
        }else{
        	buf.append("<th width=\"5%\" align=\"right\"><font size=3>Df</font></th>");
        	buf.append("<th width=\"15%\" align=\"right\"><font size=3>Deviance</font></th>");
        	buf.append("<th width=\"15%\" align=\"right\"><font size=3>Resid. Df</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>Resid. Dev</font></th>");
        	buf.append("<th width=\"20%\" align=\"right\"><font size=3>P(>|Chi|)</font></th></tr>\n");
        }

        for (int i=0; i < rlm.getAnova().length; i++){
        	if(i == rlm.getAnovaRowNames().length - 1 || isGLM)
        		buf.append("<tr><td align=\"left\"><font size=3>" + rlm.getAnovaRowNames()[i] + "</font></td>");
        	else
        		buf.append("<tr><td align=\"left\"><font size=3><a href=\"" + i + "\">" + rlm.getAnovaRowNames()[i] + "</a></font></td>");
            
            nf.setMaximumFractionDigits(0);
            double tmp = rlm.getAnova()[i][0];
            buf.append("<td align=\"right\"><font size=3>" + (!Double.isNaN(tmp)?nf.format(tmp):"") + "</font></td>");
            nf.setMaximumFractionDigits(3);
            
            int j = 1;
            for (j = 1; j < rlm.getAnovaColNames().length-1; j++){
            	tmp = rlm.getAnova()[i][j];
            	buf.append("<td align=\"right\"><font size=3>" + (!Double.isNaN(tmp)?nf.format(tmp):"") + "</font></td>");
            }
            double pval = rlm.getAnova()[i][j];
            buf.append("<td align=\"right\"><font size=3>");
            if (Double.isNaN(pval))
            {
            	buf.append("</font></td></tr>");
            }
            else if (pval > 1e-6)
            {
                nf.setMaximumFractionDigits(6);
                buf.append(nf.format(pval) + "</font></td></tr>");
                nf.setMaximumFractionDigits(3);
            }
            else
                buf.append("&lt 1e-6</font></td></tr>");
        }
        
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

            if (yvals.length > 0) {
            	ymin = se.getMin(yvals);
            	ymax = se.getMax(yvals);
            } else {
            	throw new RemoteException("empty variable");
            }
                       
            	
            if(xvals.length > 0) { 
            	xmin = se.getMin(xvals);
            	xmax = se.getMax(xvals);
            } else {
            	throw new RemoteException("empty variable");	
            }

            
            if (type == LEVERAGEPLOT && yvals.length>=2){
            	regStat = se.regress(yvals, xvals);
                a = regStat[0];
                b = regStat[1];
               
                xbar = regStat[5];
                ybar = regStat[6];

                Sx = regStat[7];
                Sr = regStat[9];
            }
            
            if (type == PARTIALRESIDUAL && rlm.getCoefficients().length > termIndex){
            	a = 0;
            	b = rlm.getCoefficients()[termIndex][0];
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
        double tmp = ymax;
        while(tmp >= ymin){
        	int len = metrics.stringWidth(inst.format(tmp)) + 8;
        	if(insets.left < len) insets.left = len;
        	tmp -= yAxisModel.getInterval();
        }
        if (showLoess) insets.top = 60;
        insets.left += 10;

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
     * Rese the plot to its original stat
     */
    public void resetPlot()
    {
    	initPlot();
        repaint();
    }
    
    /**
     * Set the current group to be the indexed group
     */
    public void setGroup(int index)
    {
    	if (grouper != null &&	(index < 0 || index >= grouper.getGroupCount()) )
    		return;

        currentIndex = index;
        retrieveData();
        initPlot();
        repaint();
    }
    
    public void setTerm(int index)
    {
    	if (type == LEVERAGEPLOT) 
    		if (index < 0 || index >= rlm.getAnova().length - 1)
    			return;
    	
    	if (type == PARTIALRESIDUAL)
    		if (index <= 0 || index >= rlm.getCoefficients().length)
    			return;
    	
    	termIndex = index;
    	retrieveData();
        initPlot();
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

        
        //draw x-axis label and y-axis label
        String ylabel="", xlabel="";
        if (type == RESIDUALVSFITTED){
        	if (!(rlm instanceof GlmModel)){
        		xlabel = "Fitted values";
        		ylabel = "Residuals";
        	}else{
        		xlabel = "Linear Predictors";
        		ylabel = "Deviance Residuals";
        	}
        }else if (type == LEVERAGEPLOT){
        	ylabel = rlm.getResponseName() + " Leverage";
        	if (rlm.getAnova().length > 0)
        	xlabel = rlm.getAnovaRowNames()[termIndex] + " Leverage";
        }else if (type == PARTIALRESIDUAL){
        	if (rlm.getCoefficients().length > 0)
        		xlabel = rlm.getCoefRowNames()[termIndex];
        	ylabel = "beta*("+xlabel+")+residuals";
        }
        
        //draw x axis
        XAxisRenderer xaxis = new XAxisRenderer(xAxisModel, getSize(), insets);
        xaxis.setLeftGap(10);
        xaxis.setRightGap(20);
        xaxis.setLabel(xlabel);
        SwingUtilities.paintComponent(g2, xaxis, this, insets.left - xaxis.getLeftGap(), 
				getSize().height - insets.bottom, xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        xAxisRegion = new Rectangle(insets.left - xaxis.getLeftGap(), getSize().height - insets.bottom, 
        		xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        
        
        //draw y axis
        YAxisRenderer yaxis = new YAxisRenderer(yAxisModel, getSize(), insets);
        yaxis.setTopGap(10);
        yaxis.setRightGap(10);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		g2.drawString(ylabel, yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());
		yAxisRegion = new Rectangle(0, insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
        

        //Draw y=0 line
		g2.setStroke(thinLine);
        g2.setColor(Color.gray);
        
        if (type == RESIDUALVSFITTED)
        	g2.drawLine(coord.x(xmin)-10, coord.y(0), coord.x(xmax), coord.y(0));
        else if (type == LEVERAGEPLOT)
        	g2.drawLine(coord.x(xmin)-10, coord.y(ybar), coord.x(xmax), coord.y(ybar));
        	
        g2.setColor(Color.black);


        //Draw individual data points
        
        discs = new Shape[yvals.length];

        for (int i = 0; i < discs.length; i++)
        {
            discs[i] = new MarkerShape(coord.x(xvals[i]), coord.y(yvals[i]), data.getMarker(indices[i]).intValue());
        }
        
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
        

        if (draggingStarted)
        {
            Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);
            g2.setColor(Color.lightGray);
            g2.draw(rect);
        }
        
        //Draw regression line
        if ((type == LEVERAGEPLOT || type==PARTIALRESIDUAL) && yvals.length>=2){
        	g2.setColor(Color.red);
            drawRangedLine(g2, a, b);
        }
        
        //Optionally draw confidence band for y
        if (type == LEVERAGEPLOT && yvals.length>=3)
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
        }
        
        if (showLoess && !slider.getValueIsAdjusting()) {
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
                                
                    if ((tmpy0 <= ymax && tmpy0 >= ymin) || (tmpy1 <= ymax && tmpy1 >= ymin))
                        //g2.drawLine(coord.x(tmpx0), coord.y(tmpy0), coord.x(tmpx1), coord.y(tmpy1));
                        drawRangedSegment(g2, tmpx0, tmpy0, tmpx1, tmpy1);
                }
                
                loessFitExist = true;
                 
            } 
            catch(InvalidDataError err) {
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
    
    public void showLoessFit(boolean b)
    {
        showLoess = b;
        slider.setVisible(b);
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
    	return minLoessSpan;
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
        else if (arg.equals("yymask") || arg.equals("obs_deleted"))
        {
            retrieveData();
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

    
    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2 && xAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(xAxisModel);
                int option = JOptionPane.showOptionDialog(ResidualsPlot.this, configPanel,
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
                        JOptionPane.showMessageDialog(ResidualsPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getClickCount() == 2 && yAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(yAxisModel);
                int option = JOptionPane.showOptionDialog(ResidualsPlot.this, configPanel,
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
                        JOptionPane.showMessageDialog(ResidualsPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        }

        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
        			JPopupMenu popupOptionMenu = ((LinearModelModule) module).getOptionMenu().getPopupMenu();
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
                ResidualsPlot.this.repaint();
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
                        Color c = JColorChooser.showDialog(ResidualsPlot.this, "Palette", Color.black);
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
            ResidualsPlot.this.repaint();
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
                    	states[indices[i]] = true; //data.setState(true, indices[i]);
                    }
                }
                data.setStates(states);
            }

            ResidualsPlot.this.repaint();
        }

        public void mouseMoved(MouseEvent e)
        {
        }

    }
    

}