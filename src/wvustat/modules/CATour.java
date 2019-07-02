package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.rmi.*;

import javax.swing.*;
import javax.swing.event.*;

import wvustat.swing.*;
import wvustat.table.MainPanel;
import wvustat.util.*;
import wvustat.interfaces.*;
import wvustat.network.*;
import wvustat.network.client.*;

import java.util.*;

import Jama.Matrix;

public class CATour extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int nBox = 3; //number of tourBox in a column
	private static final double step = 0.1; //increment for time
	
	private int axisLen = 50;
	private int width = 250, height = 200;
	private double time = 0.0; //0 to 1, increased by step
	
	private double[][] allvals;
	private String[] xlevels, ylevels;
	
	//values in 2D dimension
	private double yvals[], xvals[];
	private double[][] axes; //p*2, values on coordinate axes, same as projection matrix.
	
	private CoordConverter coord;
    private Insets insets = new Insets(40, 40, 40, 40);
    
    private boolean plotState = true;
    
    private TourUtil tour; 
    private Ellipse2D[] discs = new Ellipse2D[0];

    private TourBox[] tourBoxes;
    private StopControl stopCtrl;
    
    private SimpleSlider slider;
    private final int sliderTop = 5;
    
    private boolean enableAxis = true;
    private CAModel camdl;
    private double scale = 1.0; // range from 0 to 1
    
    public CATour(double[][] freqs, String[] xlevels, String[] ylevels) {
    	allvals = freqs;
    	this.xlevels = xlevels;
    	this.ylevels = ylevels;
    	
    	setLayout(null);
    	
    	int min = Math.min(xlevels.length, ylevels.length);
    	
    	tourBoxes = new TourBox[min];
        for (int i = 0; i < min; i++) {
        	tourBoxes[i] = new TourBox("CA" + (i+1));
        	add(tourBoxes[i]);
        }
    	    	
    	stopCtrl = new StopControl();
        add(stopCtrl);
        
        slider = new SimpleSlider(10, 400, 310);
        slider.setLabel("millisecond (from 10 to 400)");
        add(slider);
    	
        tour = new TourUtil(min);
        
        retrieveData();
        
        setBackground(Color.white);
		setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));	
    }
    
    public void setData(double[][] freqs, String[] xlevels, String[] ylevels) {
    	allvals = freqs;
    	this.xlevels = xlevels;
    	this.ylevels = ylevels;
    	
    	//Matrix tmp = new Matrix(allvals);
    	//tmp.print(tmp.getRowDimension(), tmp.getColumnDimension());
    	//System.out.println(xlevels.length);
    	//System.out.println(ylevels.length);
    	    	   	
    	retrieveData();
        initPlot();
        repaint();
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
                
        //Layout slider
        size = slider.getPreferredSize();
        slider.setBounds((w - size.width) / 2, sliderTop, size.width, size.height);
        
        size = stopCtrl.getPreferredSize();
        stopCtrl.setBounds(slider.getLocation().x - size.width - 5, sliderTop, size.width, size.height);
                
        initPlot();
        
        super.setBounds(x, y, w, h);
    }
    
    public void setGUIModule(GUIModule module) {
    	this.stopCtrl.addChangeListener((CTableModule)module);
    	this.slider.addChangeListener((CTableModule)module);
    }
    
    
    private void retrieveData()
    {
    	try
        {
    		plotState = true;
    		
    		ArrayList columns=new ArrayList();
    		for (int i = 0; i < allvals[0].length; i++) {
    			ArrayList column = new ArrayList();
    			    			
    			for (int j = 0; j < allvals.length; j++) {
    				column.add(new Double(allvals[j][i]));
    			}
    			columns.add(column);
    		}
    	
    		camdl = JRIClient.ca(columns);
    		
    		calcPCA();
    		
    		calcPlot2D();    	
        }
        catch (RemoteException re)
        {
        	plotState = false;	
        	System.err.println(re.getMessage());
        	ComponentUtil.showErrorMessage(MainPanel.getDesktopPane(), re.getMessage());
        }
    }
    
    private void calcPlot2D()
    {
    	if (plotState == false) return;
    	
    	Matrix A = tour.interpolate(time);
    	axes = A.getArray();
    	
    	Matrix X = new Matrix(camdl.G);
		Matrix Y = X.times(A);
		Matrix YT = Y.transpose();
		xvals = YT.getArray()[0];
		yvals = YT.getArray()[1];			
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
    		
	    	for (int i = 0; i < tourBoxes.length; i++)
	    		tourBoxes[i].setAxisIndicator(axes[i][0], axes[i][1]);
    		
            coord = new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);            
            
    	} catch (RemoteException re) {
    		plotState = false;
        	System.err.println(re.getMessage());
    	}
    }
    
    private void calcPCA() {
    	Matrix U = new Matrix(camdl.U);
		//U.print(U.getRowDimension(), U.getColumnDimension());
		Matrix A = new Matrix(camdl.A);
		//A.print(A.getRowDimension(), A.getColumnDimension());
		Matrix D = new Matrix(camdl.D.length, camdl.D.length);
		for (int i = 0; i < D.getRowDimension(); i++)
			D.set(i, i, camdl.D[i]);
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
		
		camdl.G = G.getArray();
		camdl.H = H.getArray();
				
		//G.print(G.getRowDimension(), G.getColumnDimension());
		//H.print(H.getRowDimension(), H.getColumnDimension());
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
	        		g2.drawString("CA"+(i+1), x + 2, y - 2);
	        	//System.out.println(axes[i][0] + " " + axes[i][1] + " " + coord.x(0) + " " + coord.y(0));
	        }
        }
        
        discs = new Ellipse2D[yvals.length];

        for (int i = 0; i < discs.length; i++)
        {
            discs[i] = new Ellipse2D.Float(coord.x(xvals[i]) - 2, coord.y(yvals[i]) - 2, 4, 4);
        }
        
        //Draw individual data points
        for (int i = 0; i < discs.length; i++)
        {
            g2.setColor(Color.black);
            g2.draw(discs[i]);
            //g2.fill(discs[i]);
            g2.drawString(xlevels[i], (int) discs[i].getBounds().getMaxX() + 2, (int) discs[i].getBounds().getMinY() - 2);
        }
    }
    
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
    	}
    }
    
    public int[] getTourBoxTypes() {
    	TourBox[] boxes;
    	boxes = tourBoxes;
    	
    	int[] result = new int[boxes.length];
    	for(int i = 0; i < boxes.length; i++)
    		result[i] = boxes[i].getType();
    	return result;
    }
    
    
    public void enableAxisDrawing(boolean b) {
    	this.enableAxis = b;
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
}

