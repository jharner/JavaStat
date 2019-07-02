package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.rmi.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

import Jama.Matrix;

//Importing self defined packages.
import wvustat.interfaces.*;
import wvustat.table.MainPanel;
import wvustat.util.ComponentUtil;
import wvustat.util.MathUtils;
import wvustat.util.TourUtil;
import wvustat.swing.SimpleSlider;
import wvustat.swing.TourBox;
import wvustat.swing.StopControl;
import wvustat.swing.MarkerShape;
import wvustat.network.PCAModel;
import wvustat.network.client.JRIClient;

/**
 * Tour views multi-dimension data in 2-D with dynamic rotation effects.
 * Grand tour uses random selection to choose the target plane.  
 *
 */
public class GrandTour extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static final int LEFT = 0;
	private static final int RIGHT = 1;
	
	private static final int nBox = 4; //number of tourBox in a column
	private static final double step = 0.1; //increment for time
	
	private int axisLen = 50;
	private int width = 250, height = 200;
	private double time = 0.0; //0 to 1, increased by step
	private int operateOn = LEFT; //LEFT or RIGHT tourBox
	
	private DataSet data;
	private Vector vy, vz;
	private Variable z_var;
			
	private int[] indices; //track non-missing observations
	private String[] ynames;	
	private String[] axisNames;
	private String[] pcnames;
	
	//values in multi-dimension
	private double[][] allvals; //centered original data. First index for y axes, second for observations
	
	//values in 2D dimension
	private double yvals[], xvals[];
	private double[][] axes; //p*2, values on coordinate axes, same as projection matrix.
	private double[][] otherAxes; //p*2
		
	private int currentIndex = 0;
	private EqualCountGrouper grouper;
	
    private CoordConverter coord;
    private Insets insets = new Insets(40, 40, 40, 40);
    
    private boolean plotState = true;
    
    private TourUtil tour; 
    private Shape[] discs = new Shape[0];
    
    
    private TourBox[] tourBoxes;
    private TourBox[] pcTourBoxes;
    private StopControl stopCtrl;
    
    private SimpleSlider slider;
    private final int sliderTop = 5;
    
    //Mouse selection range
    private int first_x, first_y, last_x, last_y, brushWidth = 10, brushHeight = 10;
    private boolean draggingStarted = false;
    
    private boolean enableAxis = true;
    private boolean enablePCA = false;
    
    private PCAModel pcamdl;
    private double scale = 1.0; // range from 0 to 1
    
    public GrandTour(DataSet data, Vector vy, Vector vz)
    {
    	this.data = data;
		this.vy = vy;
		this.vz = vz;
		this.z_var = null;
			
		ynames = new String[vy.size()];
		for (int i = 0; i < ynames.length; i++) {
			Variable var = (Variable)vy.elementAt(i);
			ynames[i] = var.getName();
		} 
    
		setLayout(null);
		tourBoxes = new TourBox[vy.size()];
        for (int i = 0; i < vy.size(); i++) {
        	tourBoxes[i] = new TourBox(ynames[i]);
        	tourBoxes[i].setPosition(LEFT);
        	tourBoxes[i].addChangeListener(this);
        	add(tourBoxes[i]);
        }
        
        pcnames = new String[vy.size()];
        for (int i = 0; i < vy.size(); i++) {
        	pcnames[i] = "PC" + (i+1);
        }        
        
        pcTourBoxes = new TourBox[vy.size()];
        for (int i = 0; i < vy.size(); i++) {
        	pcTourBoxes[i] = new TourBox(pcnames[i]);
        	pcTourBoxes[i].setPosition(RIGHT);
        	pcTourBoxes[i].addChangeListener(this);
        	add(pcTourBoxes[i]);
        }
        
        for (int i = 0; i < pcTourBoxes.length; i++)
    		pcTourBoxes[i].setVisible(false);
        
        stopCtrl = new StopControl();
        add(stopCtrl);
        
        slider = new SimpleSlider(10, 400, 310);
        slider.setLabel("millisecond (from 10 to 400)");
        add(slider);
        
        int nx = data.getXVariables().size();
        int ny = data.getYVariables().size();
        if (nx > 0 && ny > 0) {
        	for (int i = 0; i < vy.size(); i++) {
        		Variable var = (Variable)vy.elementAt(i);
        		if (var.getRole() == DataSet.X_ROLE)
        			tourBoxes[i].setType(TourBox.X);
        		else if (var.getRole() == DataSet.Y_ROLE)
        			tourBoxes[i].setType(TourBox.Y);
        	}
        }
        
		tour = new TourUtil(vy.size());
		//tour.setTargetFrame();
		
		MouseEventHandler handler = new MouseEventHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
		
		retrieveData();
				
		setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));	      
    }
    
    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h)
    {
        width = w;
        height = h;
        
        //Layout boxes
        Dimension size = tourBoxes[0].getPreferredSize();
        int n = tourBoxes.length;
        int i = 0;
        while (i < n) {
        	tourBoxes[i].setBounds((i / nBox) * size.width, (i % nBox) * size.height, size.width, size.height);
        	i++;
        }	
        insets.left = (((n-1) / nBox + 1) * size.width) + 40;
        insets.right = insets.left;
        i = 0;
        while (i < n) {
        	pcTourBoxes[i].setBounds(w - insets.right + (i / nBox) * size.width + 40, (i % nBox) * size.height, size.width, size.height);
        	i++;
        }        
        
        //Layout slider
        size = slider.getPreferredSize();
        slider.setBounds((w - size.width) / 2, sliderTop, size.width, size.height);
        
        size = stopCtrl.getPreferredSize();
        stopCtrl.setBounds(slider.getLocation().x - size.width - 5, sliderTop, size.width, size.height);
                
        initPlot();
        
        super.setBounds(x, y, w, h);
    }

    public void setGUIModule(GUIModule module) {
    	this.stopCtrl.addChangeListener((GrandTourModule)module);
    	this.slider.addChangeListener((GrandTourModule)module);
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
    	try
        {
    		plotState = true;
    	
    		if (z_var != null) {
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
        
    		if (iv.size() <= 0)
    			throw new RemoteException("empty variable");
    	
    		allvals = new double[vy.size()][iv.size()];
    		indices = new int[iv.size()];
    		double[] mean = new double[vy.size()];
    		double[] stdDev = new double[vy.size()];
        
    		for (int i = 0; i < indices.length; i++) {
    			indices[i] = ((Integer) iv.elementAt(i)).intValue();
    		}
        
    		for (int i = 0; i < vy.size(); i++) {
    			Variable var = (Variable)vy.elementAt(i);
    			double[] ytmp = var.getNumValues();
    		    		
    			for (int j = 0; j < indices.length; j++) {
    				allvals[i][j] = ytmp[indices[j]];
    			}
    		
    			mean[i] = MathUtils.getMean(allvals[i]);
    			stdDev[i] = MathUtils.getMean(allvals[i]);
    			
    			for (int j = 0; j < indices.length; j++) {
    				allvals[i][j] = (allvals[i][j] - mean[i]) / stdDev[i];
    			}
    		}
    		    
    		//calculate PCA
    		if (enablePCA) {
	    		ArrayList ycolumns=new ArrayList();
	    		for (int i = 0; i < vy.size(); i++) {
	    			ArrayList column = new ArrayList();
	    			Variable var = (Variable)vy.elementAt(i);
	    			double[] ytmp = var.getNumValues();
	    			
	    			for (int j = 0; j < indices.length; j++) {
	    				column.add(new Double(ytmp[indices[j]]));
	    			}
	    			ycolumns.add(column);
	    		}
	    		
	    		ArrayList zcolumns=null;
	    		if (vz.size() > 0) {
	    			zcolumns=new ArrayList();
		    		for (int i = 0; i < vz.size(); i++) {
		    			ArrayList column = new ArrayList();
		    			Variable var = (Variable)vz.elementAt(i);
		    			double[] ztmp = var.getNumValues();
		    			
		    			for (int j = 0; j < indices.length; j++) {
		    				column.add(new Double(ztmp[indices[j]]));
		    			}
		    			zcolumns.add(column);
		    		}
	    		}
	    		
	    		pcamdl = JRIClient.pca(ycolumns, zcolumns);
	    		
	    		calcPCA();
    		}
    		else {
    			pcamdl = new PCAModel();
    			pcamdl.HI = new double[vy.size()][vy.size()]; //any values
    		}
    		
    		calcPlot2D();    		
    	    
        }
        catch (RemoteException re)
        {
        	plotState = false;
        	
        	this.enablePCA = false;
        	for (int i = 0; i < pcTourBoxes.length; i++)
        		pcTourBoxes[i].setVisible(false);
        	this.operateOn = LEFT;
        	
        	System.err.println(re.getMessage());
        	ComponentUtil.showErrorMessage(MainPanel.getDesktopPane(), re.getMessage());
        }
    }
    
    private void calcPlot2D()
    {
    	if (plotState == false) return;
    	
    	Matrix A = tour.interpolate(time);
    	axes = A.getArray();
    	
    	if (operateOn == LEFT) {
    	
	    	Matrix XT = new Matrix(allvals);
			Matrix YT = A.transpose().times(XT);
			xvals = YT.getArray()[0];
			yvals = YT.getArray()[1];
		
			//XT.print(XT.getRowDimension(), XT.getColumnDimension());
			//YT.print(YT.getRowDimension(), YT.getColumnDimension());
			
			otherAxes = (new Matrix(pcamdl.HI)).times(A).getArray();
			
			axisNames = ynames;
	    
    	} else {
    		
    		Matrix XT = new Matrix(pcamdl.G);
			Matrix YT = A.transpose().times(XT);
			xvals = YT.getArray()[0];
			yvals = YT.getArray()[1];			
			otherAxes = (new Matrix(pcamdl.H)).times(A).getArray();
			axisNames = pcnames;
    	}
    }
    
    private void initPlot()
    {
    	try {    		
    		if (plotState == false) return;
    		
    		double ymin, ymax, xmin, xmax;
    		if (yvals.length <= 0 || xvals.length <= 0)
				throw new RemoteException("empty variable");
    		
    		ymin = MathUtils.getMin(yvals);
    		ymax = MathUtils.getMax(yvals);
    		xmin = MathUtils.getMin(xvals);
    		xmax = MathUtils.getMax(xvals);
    		
    		ymax = Math.max(Math.abs(ymin), Math.abs(ymax));
    		xmax = Math.max(Math.abs(xmin), Math.abs(xmax));
    		
    		ymin = -ymax;
    		xmin = -xmax;
                                    
            //System.out.println(xmin + " " + xmax + " " + ymin + " " + ymax);

    		axisLen = Math.max(50, Math.max(width, height) / 8); //make length of axis comparable to the size of graph
    		
    		
    		if (operateOn == LEFT) {
	    		for (int i = 0; i < tourBoxes.length; i++)
	    			tourBoxes[i].setAxisIndicator(axes[i][0], axes[i][1]);
	    		
	    		for (int i = 0; i < pcTourBoxes.length; i++)
	    			pcTourBoxes[i].setAxisIndicator(otherAxes[i][0], otherAxes[i][1]);
    		} 
    		else {
    			for (int i = 0; i < pcTourBoxes.length; i++)
	    			pcTourBoxes[i].setAxisIndicator(axes[i][0], axes[i][1]);
	    		
	    		for (int i = 0; i < tourBoxes.length; i++)
	    			tourBoxes[i].setAxisIndicator(otherAxes[i][0], otherAxes[i][1]);
    		}
    		
            coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);            
            
    	} catch (RemoteException re) {
    		plotState = false;
        	System.err.println(re.getMessage());
    	}
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
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    	
    	//Draw two lines, one at the top of the plot, another at the bottom of the plot
        g2.drawLine(0, 0, width - 1, 0);
        g2.drawLine(0, height - 1, width - 1, height - 1);
        
        Font font = new Font("Monospaced", Font.PLAIN, 10);
        g2.setFont(font);
        FontMetrics metrics = getFontMetrics(font);
        
        if (!plotState) {
            String errString = "Error getting data, please try again later.";
            g2.drawString(errString, (int) (getSize().width / 2.0 - metrics.stringWidth(errString) / 2.0),
                    (int) (getSize().height / 2.0 - metrics.getHeight() / 2.0));
            return;
        }
        
        
        if (enableAxis) {
	        for (int i = 0; i < axes.length; i++)
	        {
	        	int x = (int)Math.round(coord.x(0) + axes[i][0] * axisLen);
	        	int y = (int)Math.round(coord.y(0) - axes[i][1] * axisLen);
	        	g2.drawLine(x, y, coord.x(0), coord.y(0));
	        	if (getTourBoxTypes()[i] != TourBox.O)
	        		g2.drawString(axisNames[i], x + 2, y - 2);
	        	//System.out.println(axes[i][0] + " " + axes[i][1] + " " + coord.x(0) + " " + coord.y(0));
	        }
        }
        
        discs = new Shape[yvals.length];

        for (int i = 0; i < discs.length; i++)
        {
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
    
    public void rotatePlot()
    {
    	
    	if (time == 0) {    		
    		/* 
    		 * The interpolated frame may not accurately reach the target frame, which means
    		 * tour.interpolate(1.0) != tour.getTargetFrame(). So we cannot use tour.getTargetFrame() here.
    		 */
    		//tour.setStartFrame(tour.interpolate(1.0));
    		tour.setStartFrame(tour.getTargetFrame());
    		/*
    		 * Randomly set next target frame.
    		 */
    		tour.setConstraints(getTourBoxTypes());
    		tour.setTargetFrame();
    	}
    	
    	calcPlot2D();
    	initPlot();
    	repaint();    	
    	
    	time += step;
    	if (time > 1)
    		time = 0;
    }
    
    public void setTourBoxStatus(boolean enabled) {
    	for(int i = 0; i < tourBoxes.length; i++) {
			tourBoxes[i].setEnabled(enabled);
			pcTourBoxes[i].setEnabled(enabled);
    	}
    }
    
    public int[] getTourBoxTypes() {
    	TourBox[] boxes;
    	if (operateOn == LEFT) boxes = tourBoxes;
    	else boxes = pcTourBoxes;
    	
    	int[] result = new int[boxes.length];
    	for(int i = 0; i < boxes.length; i++)
    		result[i] = boxes[i].getType();
    	return result;
    }
    
    
    public void enableAxisDrawing(boolean b) {
    	this.enableAxis = b;
    	repaint();
    }
    
    public void enablePCAControlling(boolean b) {
    	this.enablePCA = b;
    	for (int i = 0; i < pcTourBoxes.length; i++)
    		pcTourBoxes[i].setVisible(b);
    	this.operateOn = LEFT;
    	
    	retrieveData();
        initPlot();
        repaint();
    }
    
    public double getScale() {
    	return scale;
    }
    
    public void setScale(double d) {    
    	if (d > 1) d = 1;
    	if (d < 0) d = 0;
    	this.scale = d;
    	
    	retrieveData();
        initPlot();
        repaint();
    }
    
    private void calcPCA() {
    	Matrix U = new Matrix(pcamdl.U);
		//U.print(U.getRowDimension(), U.getColumnDimension());
		Matrix A = new Matrix(pcamdl.A);
		//A.print(A.getRowDimension(), A.getColumnDimension());
		Matrix D = new Matrix(pcamdl.d.length, pcamdl.d.length);
		for (int i = 0; i < D.getRowDimension(); i++)
			D.set(i, i, pcamdl.d[i]);
		//D.print(D.getRowDimension(), D.getColumnDimension());
		
		Matrix G, H;
		if (scale == 1) {
			G = U.times(D);
			H = A;
		} else if (scale == 0) {
			G = U;
			H = A.times(D);
		} else {
			//scale > 0 && scale < 1
			Matrix tmp = D.copy();
			for (int i = 0; i < tmp.getRowDimension(); i++)
				tmp.set(i, i, Math.pow(tmp.get(i, i), scale));
			G = U.times(tmp);
			tmp = D.copy();
			for (int i = 0; i < tmp.getRowDimension(); i++)
				tmp.set(i, i, Math.pow(tmp.get(i, i), 1 - scale));
			H = A.times(tmp);
		}
		
		pcamdl.G = G.transpose().getArray();
		pcamdl.H = H.getArray();
		pcamdl.HI = H.inverse().getArray();
		
		//G.print(G.getRowDimension(), G.getColumnDimension());
		//H.print(H.getRowDimension(), H.getColumnDimension());
		//H.inverse().print(H.getRowDimension(), H.getColumnDimension());
    }
    
    public void stateChanged(ChangeEvent e)
	{
		Object obj = e.getSource();
		
		if (obj instanceof TourBox) {
			TourBox box = (TourBox)obj;
			operateOn = box.getPosition();
			if (operateOn == LEFT) 
				for (int i = 0; i < pcTourBoxes.length; i++)
					pcTourBoxes[i].setType(TourBox.A);
			else
				for (int i = 0; i < tourBoxes.length; i++)
					tourBoxes[i].setType(TourBox.A);
		}
	}
    
    class MouseEventHandler extends MouseAdapter implements MouseMotionListener
    {
    	public void mouseClicked(MouseEvent e) {}
    	
    	public void mousePressed(MouseEvent e) {
    		if (GrandTour.this.stopCtrl.isStopped() == false) return;
    		
    		int x = e.getX(), y = e.getY();
    		
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
                GrandTour.this.repaint();
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
                        Color c = JColorChooser.showDialog(GrandTour.this, "Palette", Color.black);
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

            // when no point is selected
            
            data.clearStates();
            

            draggingStarted = true;
    		
    	}
    	
    	public void mouseReleased(MouseEvent e)
        {
    		if (GrandTour.this.stopCtrl.isStopped() == false) return;
    		
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
            GrandTour.this.repaint();
        }
    	
    	public void mouseDragged(MouseEvent e)
        {
    		if (GrandTour.this.stopCtrl.isStopped() == false) return;
    		
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

            GrandTour.this.repaint();
        }
    	
    	public void mouseMoved(MouseEvent e) {}
        
    }
    
}
