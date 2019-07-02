package wvustat.modules.rmodels;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;
import java.rmi.*;
import java.text.*;
import java.util.*;

import wvustat.interfaces.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.plot.ManualAxisModel;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;
import wvustat.network.*;
import wvustat.modules.*;
import wvustat.swing.MarkerShape;


/**
 *	ResidualsPlot is used in the LinearModelModule. It plots residuals as discs.
 *
 */
public class IndexPlot extends JPanel
{
	private static final long serialVersionUID = 1L;

	/**
	* Constant for property 'Type'
	*/
	public static final int INDEXPLOTLEVERAGE = 3;
	public static final int STUDENTIZEDRESIDUAL = 4;
	public static final int JACKNIFE = 5;
	
	private int type = 0;
	
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

    private double threshold; //value of 2p/n used for index plot of leverages

    private Rectangle yAxisRegion;

    private GUIModule module;
    private GeneralPath popupArrow;
    
    private boolean plotState = true;
    

    /**
     * Constructor
     * Creates a new plot with the given data set
     */
    public IndexPlot(DataSet data, Vector vx, RLinearModel rlm, EqualCountGrouper grouper, int type)
    {
        this.data = data;
        this.rlm = rlm;
        this.grouper = grouper;        
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
    
    public void setRLinearModel(RLinearModel rlm){
        this.rlm = rlm;
    }

    private void retrieveData()
    {
        double[] ytmp = null;
 
        if (type == INDEXPLOTLEVERAGE){
          	ytmp = rlm.getHat();
        }
        else if (type == STUDENTIZEDRESIDUAL){
         	ytmp = rlm.getStudRes();
        }
        else if (type == JACKNIFE){
          	ytmp = rlm.getJackRes();
        }
            
        //This vector keeps y values
        Vector vy = new Vector();
        //This vector keeps indices
        Vector iv = new Vector();

        for (int i = 0; i < ytmp.length; i++)
        {
            if (!data.getMask(i))
            {
                Double dy = new Double(ytmp[i]);
                   
                if (dy.isNaN() || dy.isInfinite()) continue;
                    
                if (grouper == null)
                {
               		vy.addElement(dy);
               		iv.addElement(new Integer(i));
                }
                else if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
                {
               		vy.addElement(dy);
               		iv.addElement(new Integer(i));
                }
            }
        }

        yvals = new double[vy.size()];
        xvals = new double[vy.size()];
        indices = new int[iv.size()];

        for (int i = 0; i < yvals.length; i++)
        {
            yvals[i] = ((Double) vy.elementAt(i)).doubleValue();
            xvals[i] = i+1;
            indices[i] = ((Integer) iv.elementAt(i)).intValue();
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
        	plotState = true;
        	
            StatEngine se = new StatEngineImpl();

            if(yvals.length > 0) { 
            	ymin = se.getMin(yvals);
            	ymax = se.getMax(yvals);
            } else 
            	throw new RemoteException("empty data");
            
            if(xvals.length > 0){ 
            	xmin = se.getMin(xvals);
            	xmax = se.getMax(xvals);
            } else
            	throw new RemoteException("empty data");
            
                        
            if (type == INDEXPLOTLEVERAGE && yvals.length > 0){
            	threshold = 2.0 * rlm.getCoefficients().length / yvals.length;
            }
            
            
            xAxisModel = new BaseAxisModel(xmin, xmax, Math.min(5, xvals.length-1), BaseAxisModel.MIN_FIXED | BaseAxisModel.INT_SCALE);            
            
            if (yAxisModel == null || !yAxisModel.isManual())
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
        insets.left += 20;

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
        if (type == INDEXPLOTLEVERAGE){
        	ylabel = "Leverages";
        	xlabel = "Index";        	
        }else if (type == STUDENTIZEDRESIDUAL){
        	ylabel = "Studentized Residuals";
        	xlabel = "Index";
        }else if (type == JACKNIFE){
        	ylabel = "Jacknife Residuals";
        	xlabel = "Index";
        }
        
        
        //draw x axis
        XAxisRenderer xaxis = new XAxisRenderer(xAxisModel, getSize(), insets);
        xaxis.setLeftGap(20);
        xaxis.setRightGap(10);
        xaxis.setLabel(xlabel);
        SwingUtilities.paintComponent(g2, xaxis, this, insets.left - xaxis.getLeftGap(), 
				getSize().height - insets.bottom, xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);

        //draw y axis
        YAxisRenderer yaxis = new YAxisRenderer(yAxisModel, getSize(), insets);
        yaxis.setTopGap(10);
        yaxis.setRightGap(20);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		g2.drawString(ylabel, yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());
		yAxisRegion = new Rectangle(0, insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);

        
        //Draw y=0 or y=2p/n line
		g2.setStroke(thinLine);
        g2.setColor(Color.gray);
        
        if (type == STUDENTIZEDRESIDUAL || type == JACKNIFE)
        	g2.drawLine(coord.x(xmin)-20, coord.y(0), coord.x(xmax), coord.y(0));
        else if (type == INDEXPLOTLEVERAGE && threshold > ymin && threshold < ymax)
        	g2.drawLine(coord.x(xmin)-20, coord.y(threshold), coord.x(xmax), coord.y(threshold));
        	
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
        
    }
    
    
    
    /**
     * Get the GroupMaker object
     */
    public EqualCountGrouper getGroupMaker()
    {
        return grouper;
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
    

    

    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2 && yAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(yAxisModel);
                int option = JOptionPane.showOptionDialog(IndexPlot.this, configPanel,
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
                        JOptionPane.showMessageDialog(IndexPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
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
                IndexPlot.this.repaint();
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
                        Color c = JColorChooser.showDialog(IndexPlot.this, "Palette", Color.black);
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
            IndexPlot.this.repaint();
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

            IndexPlot.this.repaint();
        }

        public void mouseMoved(MouseEvent e)
        {
        }

    }
    

}