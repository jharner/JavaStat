package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.rmi.*;
import javax.swing.*;

import java.util.Vector;
import java.util.Hashtable;


//Importing self defined packages.
import wvustat.interfaces.*;
import wvustat.util.MathUtils;

/**
 * Parallel Coordinate Plot
 * 
 * @author Dajie Luo
 * @version 1.0, Jan 30, 2009
 */
public class ParallelCoordPlot extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private DataSet data;
	private Vector vy, vz;
	
	private int width = 250, height = 200;
	
	private double[][] yvals; //first index for y axes, second for observations
	private int[] indices; //track non-missing observations
	private String[] ynames;
	private int[] ytypes;
	
	private int currentIndex = 0;
	private EqualCountGrouper grouper;
	
	private BaseAxisModel ytm;
    private CoordConverter coord;
	
	private Insets insets = new Insets(40, 40, 40, 40);

	private Point[][] points; //first index for y axes, second for observations
	private Rectangle[] labelRegion;
	private Hashtable reverseOrder; //Mapping variable name to reverse flag
	
	private boolean plotState = true;
	
	//These four variables are used to keep track of mouse movement
    private int first_x, first_y, last_x, last_y;
    private boolean draggingStarted = false; //selecting
    private boolean dragAxis = false;
    private int dragFrom, dragTo; //drag axis from position to another.
	
	
	public ParallelCoordPlot(DataSet data, Vector vy, Vector vz)
	{
		this.data = data;
		this.vy = vy;
		this.vz = vz;
		
		labelRegion = new Rectangle[vy.size()];
    	for (int i = 0; i < labelRegion.length; i++) {
    		labelRegion[i] = new Rectangle();
    	}
		
    	reverseOrder = new Hashtable();
    	for (int i = 0; i < vy.size(); i++) {
    		Variable var = (Variable)vy.elementAt(i);
    		reverseOrder.put(var.getName(), new Boolean(false));
    	}
    	
    	ytypes = new int[vy.size()];
    	
		retrieveData();
		
		MouseEventHandler handler = new MouseEventHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));	
	}
	
	/**
     * Get the class that divides observations into groups.
     */
    public EqualCountGrouper getGroupMaker()
    {
        return grouper;
    }
    
    private void retrieveData()
    {
    	plotState = true;
    	
    	if (vz.size() > 0) {
    		if (grouper == null)
                grouper = new EqualCountGrouper(vz, data);
    	}
    	
    	//This vector keeps indices
        Vector iv = new Vector();
    	
        for (int i = 0; i < data.getSize(); i++)
        {
        	if (!data.getMask(i))
            {
        		if (grouper == null)
                {
        			iv.addElement(new Integer(i));
                }
        		else if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
        		{
        			iv.addElement(new Integer(i));
        		}
            }
        }
    	
        yvals = new double[vy.size()][iv.size()];
        indices = new int[iv.size()];
        
        for (int i = 0; i < indices.length; i++) {
        	indices[i] = ((Integer) iv.elementAt(i)).intValue();
        }
        
    	for (int i = 0; i < yvals.length; i++) {
    		Variable var = (Variable)vy.elementAt(i);
    		double[] ytmp = var.getNumValues();
    		ytypes[i] = var.getType();
    		
    		for (int j = 0; j < indices.length; j++) {
    			yvals[i][j] = ytmp[indices[j]];
    		}
    	}
    	
    	ynames = new String[vy.size()];
    	for (int i = 0; i < ynames.length; i++) {
    		Variable var = (Variable)vy.elementAt(i);
    		ynames[i] = var.getName();
    	}    	
    }
    
    private void initPlot()
    {
    	try {    		
    		double ymin, ymax, xmin, xmax;
    		points = new Point[yvals.length][];
    	
    		for (int i = 0; i < yvals.length; i++) {
    			if (yvals[i].length <= 0)
    				throw new RemoteException("empty variable");
    			
    			ymin = MathUtils.getMin(yvals[i]);
    			ymax = MathUtils.getMax(yvals[i]);
    			ytm = new BaseAxisModel(ymin, ymax, 5);
    			ymin = ytm.getStartValue();
    			ymax = ytm.getEndValue();
    			xmin = 0;
    			xmax = ynames.length - 1;
            
    			insets.left = width / 10; // reserve 10% margin to show label. 
    			if (insets.left < 40) insets.left = 40;
    			insets.right = width / 10; 
    			if (insets.right < 40) insets.right = 40;
    			
    			coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);
        
    			points[i] = new Point[yvals[i].length];
    			for (int j = 0; j < yvals[i].length; j++) {
    				if (((Boolean)reverseOrder.get(ynames[i])).booleanValue()) {
    					points[i][j] = new Point(coord.x(i), coord.y(ymax - yvals[i][j] + ymin));
    				} else
    					points[i][j] = new Point(coord.x(i), coord.y(yvals[i][j]));
    			}
    		}
    	} catch (RemoteException re) {
    		plotState = false;
        	System.err.println(re.getMessage());
    	}
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

    /**
     * Get the dimension of this plot
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
    	
    	//Draw two lines, one at the top of the plot, another at the bottom of the plot
        g2.drawLine(0, 0, width - 1, 0);
        g2.drawLine(0, height - 1, width - 1, height - 1);
    	
        Font font = new Font("Monospaced", Font.PLAIN, 11);
        g2.setFont(font);
        FontMetrics metrics = getFontMetrics(font);
        
        if (!plotState) {
            String errString = "Error getting data, please try again later.";
            g2.drawString(errString, (int) (getSize().width / 2.0 - metrics.stringWidth(errString) / 2.0),
                    (int) (getSize().height / 2.0 - metrics.getHeight() / 2.0));
            return;
        }
        
        //Draw axes
        g2.setStroke(thick);
        for (int i = 0; i < ynames.length; i++) {
        	if (!(dragAxis && i == dragFrom)) {
        		g2.setColor(Color.gray);
        		g2.drawLine(coord.x(i), coord.y(ytm.getStartValue()), coord.x(i), coord.y(ytm.getEndValue()));
        		g2.setColor(Color.black);
        		
        		boolean b = ((Boolean)reverseOrder.get(ynames[i])).booleanValue();
        		if (!b) {
        			g2.drawString(ynames[i], coord.x(i) - metrics.stringWidth(ynames[i]) / 2, coord.y(ytm.getEndValue()) - metrics.getHeight());
        			labelRegion[i].setBounds(coord.x(i) - 5, coord.y(ytm.getEndValue()) - 6, 10, 6);
        			drawArrow(g2, labelRegion[i].x, labelRegion[i].y, !b);        			
        		} else {
        			g2.drawString(ynames[i], coord.x(i) - metrics.stringWidth(ynames[i]) / 2, coord.y(ytm.getStartValue()) + metrics.getHeight() + 6);
        			labelRegion[i].setBounds(coord.x(i) - 5, coord.y(ytm.getStartValue()), 10, 6);
        			drawArrow(g2, labelRegion[i].x, labelRegion[i].y, !b);
        		}
        	}
        }
        
        //Draw moving axis when dragging
        if (dragAxis)
        {
        	g2.setColor(Color.red);
        	g2.drawLine(last_x, coord.y(ytm.getStartValue()), last_x, coord.y(ytm.getEndValue()));
        	
        	boolean b = ((Boolean)reverseOrder.get(ynames[dragFrom])).booleanValue();
        	if (!b) 
        		g2.drawString(ynames[dragFrom], last_x - metrics.stringWidth(ynames[dragFrom]) / 2, coord.y(ytm.getEndValue()) - metrics.getHeight());
        	else
        		g2.drawString(ynames[dragFrom], last_x - metrics.stringWidth(ynames[dragFrom]) / 2, coord.y(ytm.getStartValue()) + metrics.getHeight() + 6);
        	
        	drawArrow(g2, last_x - 5, labelRegion[dragFrom].y, !b);
        }
        
        //Draw folding line for each observation
        for (int i = 0; i < yvals[0].length; i++) {
        	g2.setColor(data.getColor(indices[i]));
        	if (data.getState(indices[i])) {
        		g2.setStroke(thick);
        	} else {
        		g2.setStroke(thin);
        	}
        	
        	int j;
        	for (j = 1; j < yvals.length; j++) {
        		g2.drawLine(points[j-1][i].x, points[j-1][i].y, points[j][i].x, points[j][i].y);
        	}
        	
        	if (data.getState(indices[i])) {
        		if (data.getLabel(indices[i]) != null) {
        			g2.setColor(Color.black);
        			g2.drawString(data.getLabel(indices[i]), points[j-1][i].x + 2, points[j-1][i].y - 2);
        		}
        	}
        }
        
        //Draw gray rectangle when selecting
        if (draggingStarted)
        {
            Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);
            g2.setColor(Color.lightGray);
            g2.draw(rect);
        }
    }
    
    private void drawArrow(Graphics2D g2, int x, int y, boolean isUp) {
    	if (isUp)
    		drawUpArrow(g2, x, y);
    	else
    		drawDownArrow(g2, x, y);
    }
    
    private void drawDownArrow(Graphics2D g2, int x, int y) {
    	GeneralPath arrow = new GeneralPath();
    	arrow.moveTo(x,y);
    	arrow.lineTo(x+10,y);
    	arrow.lineTo(x+5,y+6);
    	arrow.closePath();
    	g2.fill(arrow);
    }
    
    private void drawUpArrow(Graphics2D g2, int x, int y) {
    	GeneralPath arrow = new GeneralPath();
    	arrow.moveTo(x+5,y);
    	arrow.lineTo(x+10,y+6);
    	arrow.lineTo(x,y+6);
    	arrow.closePath();
    	g2.fill(arrow);
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
        else if (arg.equals("new_obs_added") || arg.equals("obs_deleted"))
        {
            retrieveData();
            initPlot();
            repaint();
        }
    }
    
    private boolean isPointOnSegment(Point p, Point start, Point end)
    {
    	Line2D seg = new Line2D.Double(start, end);
    	double d = seg.ptSegDist(p.x, p.y);
    	return d <= 2.0;
    }
    
    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {
    	public void mousePressed(MouseEvent e)
    	{
    		int x = e.getX(), y = e.getY();
    		
    		for (int i = 0; i < labelRegion.length; i++) {
    			if (labelRegion[i].contains(e.getPoint())) {
    				boolean b = ((Boolean)reverseOrder.get(ynames[i])).booleanValue();
    				reverseOrder.put(ynames[i], new Boolean(!b));
    				ParallelCoordPlot.this.initPlot();
    				ParallelCoordPlot.this.repaint();
    				return;
    			}
    		}
    		
    		for (int i = 0; i < ynames.length; i++) {
    			if (y > insets.top && y < height - insets.bottom && Math.abs(x - coord.x(i)) < 5) {
    				dragAxis = true;
    				dragFrom = i;
    				dragTo = i;
    				return;
    			}
    		}
    		    		
    		first_x = x;
            first_y = y;
            last_x = first_x;
            last_y = first_y;
    		
    		boolean multipleSelection = false;
            if (e.getModifiers() == 17)
            {
                multipleSelection = true;
            }
            
            for (int i = 0; i < yvals[0].length; i++) {
            	
            	boolean found = false;
            	for (int j = 1; j < yvals.length; j++) {
            		if (isPointOnSegment(e.getPoint(), points[j-1][i], points[j][i])) {
            			found = true;
            			break;
            		}
            	}
            	
            	if (found) {
            		if (e.getClickCount() == 2)
                    {
                        Color c = JColorChooser.showDialog(ParallelCoordPlot.this, "Palette", Color.black);
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
    		if (dragAxis && dragFrom != dragTo) {
    			vy.add(dragTo, vy.remove(dragFrom));
    			retrieveData();
    			initPlot();
    		}
    		
    		dragAxis = false;
    		
            if (draggingStarted)
            {
                Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);

                boolean[] states = data.getStates();
                for (int i = 0; i < yvals[0].length; i++) {
                	for (int j = 1; j < yvals.length; j++) {
                		if (rect.intersectsLine(new Line2D.Float(points[j-1][i], points[j][i])))
                			states[indices[i]] = true;
                	}
                }
                data.setStates(states);
            }
            
            draggingStarted = false;
            ParallelCoordPlot.this.repaint();
        }
    	
    	public void mouseDragged(MouseEvent e)
        {
    		last_x = e.getX();
            last_y = e.getY();
            
            if (dragAxis) {
            	dragTo = dragFrom;
            	for (int i = 0; i < ynames.length; i++) 
        			if (Math.abs(last_x - coord.x(i)) < 5) { 
        				dragTo = i;
        				break;
        			}
            }
            
            ParallelCoordPlot.this.repaint();
        }
    	
    	public void mouseMoved(MouseEvent e)
        {
        }
    	
    }
}
