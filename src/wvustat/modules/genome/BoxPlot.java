package wvustat.modules.genome;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.rmi.*;
import javax.swing.*;

import java.util.Vector;
import java.text.NumberFormat;

import wvustat.interfaces.*;
import wvustat.modules.*;
import wvustat.network.*;
import wvustat.swing.YAxisRenderer;

/**
 *	BoxPlot is a component that represents a microarray object, such as AffyBatch or exprSet. 
 *
 *	@author: Dajie Luo
 *	@version: 1.0, Jun. 20, 2007
 */

public class BoxPlot extends JPanel
{
	private static final long serialVersionUID = 1L;

	private exprSet eset;

    private int width = 250, height = 200;
    private FontMetrics metrics;

    private double[] yvals;
    private double[][] grouped;
    private String[] xvals;
    private Vector xlevels;

    private BaseAxisModel ytm;
    private CoordConverter coord;
    private Insets insets = new Insets(30, 40, 40, 20);
    private double ymin, ymax, xmin, xmax;
   
    private GUIModule module;
    private GeneralPath popupArrow;
    
    private boolean log2scale = true;
    private boolean log2enabled = true;

    /**
     * Constructor
     * Creates a new BoxPlot based on the exprSet or AffyBatch object
     * in the parameters.
     *
     * @param eset the object this BoxPlot is based on.
     */
    public BoxPlot(exprSet eset)
    {
        this.eset = eset;

        for (int i = 0; i < eset.getNumSample(); i++)
        	for (int j = 0; j < 5; j++)
        		if (eset.getIntensityQtl()[i][j] <= 0) log2scale = log2enabled = false;       
        
        retrieveData();

        setBackground(Color.white);
        MouseEventHandler handler = new MouseEventHandler();
        addMouseListener(handler);

        Font font = new Font("Monospaced", Font.PLAIN, 11);
        metrics = getFontMetrics(font);

        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));

        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }

    
    /**
     * Retrieve data from the object.
     */
    private void retrieveData()
    {
    	yvals = new double[eset.getNumSample() * 5];
        
        for (int i = 0; i < eset.getNumSample(); i++)
        	for (int j = 0; j < 5; j++)
        		yvals[i * 5 + j] = eset.getIntensityQtl()[i][j];
        
        if (log2scale) {
        	for (int i = 0; i < yvals.length; i++)
        		yvals[i] = Math.log(yvals[i]) / Math.log(2);
        }
        
        xvals = eset.getSampleNames();
    	
    	xlevels = new Vector();
        for (int i = 0; i < eset.getNumSample(); i++)
        {
        	xlevels.addElement(xvals[i]);
        }
        
        grouped = eset.getIntensityQtl();
        
        if (log2scale) {
        	grouped = new double[eset.getNumSample()][5];
        	for (int i = 0; i < eset.getNumSample(); i++)
        		for (int j = 0; j < 5; j++) 
        			grouped[i][j] = Math.log(eset.getIntensityQtl()[i][j]) / Math.log(2);
        }
    }

    /**
     * Initialize properties such as ticmarks, coordinate converter for the plot
     */
    private void initPlot()
    {
        try
        {
            StatEngine se = new StatEngineImpl();

            ymin = se.getMin(yvals);
            ymax = se.getMax(yvals);
            
            ytm = new BaseAxisModel(ymin, ymax, 5);

            ymin = ytm.getStartValue();
            ymax = ytm.getEndValue();

            xmin = 0;
            xmax = xlevels.size() + 1;

            NumberFormat nf = NumberFormat.getInstance();
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
    
    public void setLog2scale(boolean b) {
    	if (log2enabled == false) return;
    	this.log2scale = b;
    	retrieveData();
    	initPlot();
    	repaint();
    }
    
    public boolean isLog2scale() {
    	return log2scale;
    }
    
    public boolean isLog2enabled() {
    	return log2enabled;
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
        
        g2.setFont(metrics.getFont());

        BasicStroke thin = new BasicStroke(1.0f);
        
        g2.fill(popupArrow);

        //Draw two lines, one at the top of the plot, another at the bottom of the plot
        g2.drawLine(0, 0, width - 1, 0);
        g2.drawLine(0, height - 1, width - 1, height - 1);

        //Draw x axis
        g2.setStroke(thin);
        g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
        
        String xname = "array";
        int len = metrics.stringWidth(xname);
        g2.drawString(xname, (width - len) / 2, coord.y(ymin) + 2 * metrics.getHeight());

        //draw x axis ticmarks
        for (int i = 0; i < xlevels.size(); i++)
        {
            String label = String.valueOf(i + 1);
            g2.drawLine(coord.x(i + 1), coord.y(ymin), coord.x(i + 1), coord.y(ymin) + 4);
            g2.drawString(label, coord.x(i + 1) - metrics.stringWidth(label) / 2, coord.y(ymin) + metrics.getHeight());
        }

        
        //draw y axis
        String yname = log2scale ? "log2 intensity" : "intensity";
        YAxisRenderer yaxis = new YAxisRenderer(ytm, getSize(), insets);
        yaxis.setTopGap(10);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		g2.drawString(yname, yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());

		
        //Draw Boxplot
        g2.setColor(Color.black);
        int w = (int) ((coord.x(1) - coord.x(0)) / 2.0f);
                
        for (int i = 0; i < grouped.length; i++)
        {
            double[] q = grouped[i];

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


    /**
     * Inner class
     *	Handles mouse event
     */
    class MouseEventHandler extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            int x = e.getX(), y = e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
                JPopupMenu popupOptionMenu = module.getOptionMenu().getPopupMenu();
                popupOptionMenu.show(e.getComponent(), popupArrow.getBounds().x + popupArrow.getBounds().width, 
                	popupArrow.getBounds().y + popupArrow.getBounds().height);
                return;
            }
        }
    }
}


