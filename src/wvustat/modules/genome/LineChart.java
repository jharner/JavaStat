package wvustat.modules.genome;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.rmi.*;
import java.text.*;

import wvustat.interfaces.*;
import wvustat.plot.*;
import wvustat.network.*;
import wvustat.modules.*;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;
import wvustat.util.MathUtils;


/**
 *	LineChart is used in the MTPModule. It plots related to adjusted p-values as linked points.
 *
 */
public class LineChart extends JPanel implements ChangeListener
{
	private static final long serialVersionUID = 1L;

	/**
	* Constant for property 'Type'
	*/
	public static final int REJECTION_VS_PVALUE = 0;
	public static final int PVALUE_VS_REJECTION = 1;
	public static final int PVALUE_VS_STATISTICS = 2;
	
	private int type = 0;
	
	private MTP mtp;
	
    private int width = 320, height = 220;

    private double xmin, xmax, ymin, ymax;
    private BaseAxisModel xAxisModel, yAxisModel;
    private Rectangle xAxisRegion, yAxisRegion;
    
    private CoordConverter coord;

    private Ellipse2D[] discs;
    private double yvals[], xvals[];

    private Insets insets;
    private FontMetrics metrics;

    private boolean DragLine = false;

    private GUIModule module;
    private GeneralPath popupArrow;
    
    
    /**
     * Constructor
     * Creates a new chart
     */
    public LineChart(MTP mtp, int type)
    {
    	this.mtp = mtp;
    	this.type = type;
        
        Font font = new Font("Monospaced", Font.PLAIN, 11);
        metrics = getFontMetrics(font);

        retrieveData();

        setBackground(Color.white);

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
        super.setBounds(x, y, w, h);
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }
    
    
    private void retrieveData()
    {
    	double[] ytmp = null;
        double[] xtmp = null; 
        
    	xtmp = new double[mtp.getNumHypo()]; 
    	System.arraycopy(mtp.getAdjp(), 0, xtmp, 0, xtmp.length); //mtp.getAdjp() keep unchanged
    	MathUtils.InsertionSort(xtmp);
    	
    	ytmp = new double[xtmp.length];
        for (int i = 0; i < ytmp.length; i++)
            ytmp[i] = i + 1;

        if (type == REJECTION_VS_PVALUE) {
        	yvals = ytmp;
        	xvals = xtmp;
        }
        else if (type == PVALUE_VS_REJECTION) {
        	yvals = xtmp;
        	xvals = ytmp;
        }
        else if (type == PVALUE_VS_STATISTICS) {
        	yvals = mtp.getAdjp();
        	xvals = mtp.getStatistic();
        }

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

            ymin = se.getMin(yvals);
            ymax = se.getMax(yvals);
            
       		xmin = se.getMin(xvals);
       		xmax = se.getMax(xvals);

            if(xAxisModel == null)
            {
            	if (type == REJECTION_VS_PVALUE)
            		xAxisModel = new BaseAxisModel(0, 1, 5);
            	else
            		xAxisModel = new BaseAxisModel(xmin, xmax, 5); 
            }
            if(yAxisModel == null)
            {
            	if (type == PVALUE_VS_REJECTION || type == PVALUE_VS_STATISTICS)
            		yAxisModel = new BaseAxisModel(0, 1, 5);
            	else
            		yAxisModel = new BaseAxisModel(ymin, ymax, 5);
            }

            configElements();
        }
        catch (RemoteException re)
        {
            re.printStackTrace();
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
        while (tmp >= ymin){
        	int len = metrics.stringWidth(inst.format(tmp)) + 8;
        	if(insets.left < len) insets.left = len;
        	tmp -= yAxisModel.getInterval();
        }

        coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);

        discs = new Ellipse2D[yvals.length];

        for (int i = 0; i < discs.length; i++)
        {
            discs[i] = new Ellipse2D.Float(coord.x(xvals[i]), coord.y(yvals[i]), 1, 1);
        }
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
    

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));


        //Draw two lines (for decoration purpose, maybe), one at the top. one at the bottom
        g2.drawLine(0, 0, width - 1, 0);
        g2.drawLine(0, height - 1, width - 1, height - 1);
        g2.setColor(Color.black);
        g2.fill(popupArrow);

        
        //draw x-axis label and y-axis label
        String ylabel, xlabel;
        if (type == REJECTION_VS_PVALUE) {
        	xlabel = "Type I error rate";
        	ylabel = "Number of rejected hypotheses";
        } 
        else if (type == PVALUE_VS_REJECTION) {
        	xlabel = "Number of rejected hypotheses";
        	ylabel = "Sorted Adjusted p-values";
        }
        else {
        	xlabel = "Test statistics";
        	ylabel = "Adjusted p-values";
        }
        
        //draw x axis
        XAxisRenderer xaxis = new XAxisRenderer(xAxisModel, getSize(), insets);
        xaxis.setRightGap(10);
        xaxis.setLabel(xlabel);
        SwingUtilities.paintComponent(g2, xaxis, this, insets.left - xaxis.getLeftGap(), 
				getSize().height - insets.bottom, xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        
        if (type == REJECTION_VS_PVALUE)
        	xAxisRegion = new Rectangle(0,0,0,0);
        else
        	xAxisRegion = new Rectangle(insets.left - xaxis.getLeftGap(), getSize().height - insets.bottom, 
        		xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        
        //draw y axis
        YAxisRenderer yaxis = new YAxisRenderer(yAxisModel, getSize(), insets);
        yaxis.setTopGap(10);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		g2.drawString(ylabel, yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());
		
		if (type == PVALUE_VS_REJECTION || type == PVALUE_VS_STATISTICS)
        	yAxisRegion = new Rectangle(0,0,0,0);
        else
        	yAxisRegion = new Rectangle(0, insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
 
		
        //draw line chart
        if (type == REJECTION_VS_PVALUE || type == PVALUE_VS_REJECTION){            
            for (int i = 1; i < xvals.length; i++){
            	int x0 = coord.x(xvals[i - 1]);
            	int x1 = coord.x(xvals[i]);
            	int y0 = coord.y(yvals[i - 1]);
            	int y1 = coord.y(yvals[i]);
            	
            	if (x0 >= coord.x(xmin) && x1 <= coord.x(xmax) && y0 <= coord.y(ymin) && y1 >= coord.y(ymax))
            		g2.drawLine(x0, y0, x1, y1);
            }    
        } 
        else if (type == PVALUE_VS_STATISTICS){
        	for (int i = 0; i < discs.length; i++){
        		g2.draw(discs[i]);
        	}
        }
        
        if (type == REJECTION_VS_PVALUE){
        	double nullVal = mtp.getAlpha();
        	g2.setColor(Color.green);
        	g2.setStroke(new BasicStroke(2.0f));
        	g2.drawLine(coord.x(nullVal), coord.y(ymin), coord.x(nullVal), coord.y(ymin) + 8);
        }
 
    }

    
    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();
        if (obj instanceof MTP) {
        	repaint();
        }
    }
    

    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2 && xAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.BASIC_MODEL);
                configPanel.setAxisModel(xAxisModel);
                int option = JOptionPane.showOptionDialog(LineChart.this, configPanel,
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
                        JOptionPane.showMessageDialog(LineChart.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getClickCount() == 2 && yAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.BASIC_MODEL);
                configPanel.setAxisModel(yAxisModel);
                int option = JOptionPane.showOptionDialog(LineChart.this, configPanel,
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
                        JOptionPane.showMessageDialog(LineChart.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        }

        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
        		JPopupMenu popupOptionMenu = (module).getOptionMenu().getPopupMenu();
        		popupOptionMenu.show(e.getComponent(), popupArrow.getBounds().x + popupArrow.getBounds().width, 
        							popupArrow.getBounds().y + popupArrow.getBounds().height);
        		return;
            }
            
            if (type == REJECTION_VS_PVALUE && y>coord.y(ymin) && y<(coord.y(ymin) + 8) && Math.abs(x - coord.x(mtp.getAlpha())) < 5) {
        		DragLine=true;
        		return;
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            DragLine = false;
            LineChart.this.repaint();
        }

        public void mouseDragged(MouseEvent e)
        {            
            if (DragLine) {
            	mtp.setNull(coord.inverseX(e.getX()));
            	return;
            }

            LineChart.this.repaint();
        }

        public void mouseMoved(MouseEvent e)
        {
        }

    }
    

}