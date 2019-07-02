package wvustat.modules;

import wvustat.interfaces.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.swing.SimpleSlider;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;

import java.awt.*;
import java.util.*;
import java.rmi.RemoteException;
import java.awt.geom.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 * ASHistogramPlot plots data as an average shifted histogram. That is, it averages several histograms and
 * plots fine bars to represent density of data. It is used in HistogramModule.
 */
public class ASHistogramPlot extends JPanel implements ChangeListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private DataSet data;
    private int width = 250, height = 200;
    private transient double[][] freqs;
    private Variable y_var = null, z_var = null;
    private Vector vz;
    private boolean plotState = true;
    private int numOfBars = 5, numOfShifts = 5;
    private double maxFreq;
    private int z_level = 0;
    private CoordConverter coord = null;
    private double ymin, ymax, barWidth;
    private Ellipse2D[][] discs;
    private Rectangle2D[][] bars;
    private Line2D[][] rugs;

    private Rectangle2D selectionRange = new Rectangle2D.Float(0, 0, 0, 0);

    //keep track of positions of mouse movement
    private int last_x, last_y, first_x, first_y, brushWidth = 10, brushHeight = 10;
    private boolean mouseDragging = false;

    private Vector zvals;

    private EqualCountGrouper grouper;

    private GeneralPath popupArrow;

    private SimpleSlider slider, barSlider, shiftSlider;
    private final int sliderTop = 5;
    private static final int MIN_BAR_NUM = 2, MAX_BAR_NUM = 30;
    private static final int MIN_SHIFT = 1, MAX_SHIFT = 10;
    private Rectangle labelRegion;
    
    private JPopupMenu popup;
    private boolean drawBar = true, drawRug = false;

    private GUIModule module;

    private Vector yValues = new Vector();
    private double[] yvals;
    private Insets insets = new Insets(60, 50, 40, 40);
    private BaseAxisModel xAxisModel;
    private BaseAxisModel yAxisModel;
    private Rectangle xAxisRegion = new Rectangle();
    private Rectangle yAxisRegion = new Rectangle();
    
    private Variable init_y_var = null;

    /**
     * Constructor
     * <p/>
     * HistogramPlot is the actual plot, it does not have buttons, reports, etc. Don't forget to set clientID
     * after instantiating a new object.
     */
    public ASHistogramPlot(DataSet data, Variable y_var, Vector vz) throws IllegalArgumentException 
	{
        this.data = data;
        this.y_var = y_var;
        this.init_y_var = y_var;
        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);

        setBackground(Color.white);

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);

        popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Bars");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Shifts");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        
        barSlider = new SimpleSlider(MIN_BAR_NUM, MAX_BAR_NUM, numOfBars);
		barSlider.setLabel("Bars");
		barSlider.addChangeListener(this);	
        //barSlider.setPopup(popup);
        
        shiftSlider = new SimpleSlider(MIN_SHIFT, MAX_SHIFT, numOfShifts);
		shiftSlider.setLabel("Shifts");
		shiftSlider.addChangeListener(this);	
        //shiftSlider.setPopup(popup);
        shiftSlider.setVisible(false);
        
        setLayout(null);
		add(barSlider);
		add(shiftSlider);
		slider = barSlider;

		initPlot();
        //countFreqs();
		
        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }

    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h) {
        width = w;
        height = h;
        countFreqs();
        
        Dimension size = slider.getPreferredSize();
		barSlider.setBounds((w - size.width) / 2, sliderTop, size.width, size.height);
		shiftSlider.setBounds((w - size.width) / 2, sliderTop, size.width, size.height);
		
        super.setBounds(x, y, w, h);
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setGUIModule(GUIModule module) {
        this.module = module;
    }

    public void setYAxisModel(BaseAxisModel axisModel) {
        yAxisModel = axisModel;
        countFreqs();
        repaint();

    }

    public void setXAxisModel(BaseAxisModel axisModel) {
    	
    	try {    		
    		if (y_var.getMin() < axisModel.getStartValue() || y_var.getMax() >= axisModel.getEndValue())
    			return;
    	}
    	catch (RemoteException error) {}
    	
        xAxisModel = axisModel;
        ymin = xAxisModel.getStartValue();
        ymax = xAxisModel.getEndValue();
        barWidth = xAxisModel.getInterval();

        numOfBars = (int) Math.round((ymax - ymin) / barWidth);
        barSlider.setValue(numOfBars);
        
        countFreqs();
        repaint();

    }

    public BaseAxisModel getXAxisModel() {
        return xAxisModel;
    }

    public void enableBarDrawing(boolean val) {
    	drawBar = val;
        repaint();
    }
    
    public void enableRugDrawing(boolean val) {
    	drawRug = val;
    	repaint();    	
    }
    
    /**
     * This method finds the best ticmarks for x axis, this should only
     * be called once for a histogram unless the data changes.
     */
    private void initPlot() 
    {
        try 
		{
        	plotState = true;
        	
            ymin = y_var.getMin();
            ymax = y_var.getMax();

            numOfBars = (int) Math.floor(Math.log(y_var.getSize()) / Math.log(2.0));
      
            if (xAxisModel == null || !xAxisModel.isManual())
                xAxisModel = new BaseAxisModel(ymin, ymax, numOfBars);
            
            if (ymin < xAxisModel.getStartValue() || ymax >= xAxisModel.getEndValue())
            	xAxisModel = new BaseAxisModel(ymin, ymax, numOfBars);

            ymin = xAxisModel.getStartValue();
            ymax = xAxisModel.getEndValue();
            barWidth = xAxisModel.getInterval();

            numOfBars = (int) Math.round((ymax - ymin) / barWidth);
            barSlider.setValue(numOfBars);
        }
        catch (RemoteException error) {
        	System.err.println(error.getMessage());
            plotState = false;
        }
    }


    /**
     * This method counts the frequency of observations accoording to
     * current setting. This should be called every time the number of
     * bars in the histogram needs to be changed.
     */
    private void countFreqs() {
        maxFreq = 0;
        
        yvals = y_var.getNumValues();

        if (grouper == null) 
        {

            freqs = new double[1][numOfBars*numOfShifts];
            bars = new Rectangle2D[1][numOfBars*numOfShifts];

            for (int i = 0; i < freqs[0].length; i++)
                freqs[0][i] = 0;

            for (int k = 0; k < numOfShifts; k++) {
             	for (int i = 0; i < numOfBars; i++) {
               		double lower = ymin + i * barWidth + barWidth * k / numOfShifts;
               		double upper = lower + barWidth;

               		for (int j = 0; j < yvals.length; j++) {
                        
               			if (data.getMask(j) == false && yvals[j] >= lower && yvals[j] < upper)
               			{
               				int m = i * numOfShifts + k;
               				int step = 0;
               				while (m < freqs[0].length && step < numOfShifts) {
               					freqs[0][m]++;
               					step++;
               					m++;
               				}
               			}
               		}
               	}
            }

        } else {

            zvals = z_var.getValues();
            freqs = new double[grouper.getGroupCount()][numOfBars*numOfShifts];
            bars = new Rectangle2D[grouper.getGroupCount()][numOfBars*numOfShifts];

            yValues.removeAllElements();

            for (int i = 0; i < yvals.length; i++) {
                if (data.getMask(i) == false && grouper.getGroupIndex(i).contains(new Integer(z_level))) {
                    yValues.addElement(new Double(yvals[i]));
                }
            }

            for (int i = 0; i < freqs.length; i++) {
                for (int j = 0; j < freqs[i].length; j++)
                    freqs[i][j] = 0;
            }

            for (int i = 0; i < freqs.length; i++) {
              	for (int h = 0; h < numOfShifts; h++){
               		for (int j = 0; j < numOfBars; j++) {
               			double lower = ymin + j * barWidth + barWidth * h / numOfShifts;
               			double upper = lower + barWidth;

               			for (int k = 0; k < yvals.length; k++) {
               				if (!data.getMask(k) && grouper.getGroupIndex(k).contains(new Integer(i))) {
               					if (yvals[k] >= lower && yvals[k] < upper) {
                						
               						int m = j * numOfShifts + h;
               						int step = 0;
               						while (m < freqs[i].length && step < numOfShifts) {
               							freqs[i][m]++;
               							step++;
               							m++;
               						}
               					}
               				}
               			}
               		}
               	}
            }

        }
            
        for (int i = 0; i < freqs.length; i++) {
            for (int j = 0; j < freqs[i].length; j++) {
                freqs[i][j] = freqs[i][j] / numOfShifts;
            }
        }
            
            
        for (int i = 0; i < freqs.length; i++) {
            for (int j = 0; j < freqs[i].length; j++) {
                if (freqs[i][j] > maxFreq)
                    maxFreq = freqs[i][j];
            }
        }
            
            
        Dimension d = getSize();
        double tmpMax = maxFreq;

        int ticCount = 5;
        yAxisModel = new BaseAxisModel(0, tmpMax, ticCount, BaseAxisModel.MIN_FIXED | BaseAxisModel.INT_SCALE);
        tmpMax = yAxisModel.getEndValue();
            
        coord = new CoordConverter(d.width, d.height, ymin, ymax, 0, tmpMax, insets);

        int w = coord.x(ymin + barWidth) - coord.x(ymin);

        for (int i = 0; i < bars.length; i++) {
            for (int j = 0; j < bars[i].length; j++) {
                w = coord.x(ymin + (j + 1) * barWidth / numOfShifts) - coord.x(ymin + j * barWidth / numOfShifts);

                double tmpFreq = freqs[i][j];

                bars[i][j] = new Rectangle2D.Float(coord.x(ymin + j * barWidth / numOfShifts), coord.y(tmpFreq), w, coord.y(0) - coord.y(tmpFreq));
            }
        }
            
        // compute location of discs
        if(grouper == null) {
            discs = new Ellipse2D[1][yvals.length];
            rugs = new Line2D[1][yvals.length];
        } else {
            discs = new Ellipse2D[grouper.getGroupCount()][yvals.length];     
            rugs = new Line2D[grouper.getGroupCount()][yvals.length];
        }
        
        for (int j = 0; j < discs.length; j++) {
            for (int i = 0; i < discs[j].length; i++) {
              	
                if(data.getMask(i)) continue;   //considering missing obs for yvals, added by djluo
                
                if(grouper != null && !grouper.getGroupIndex(i).contains(new Integer(j))) continue;
            	
                int barIndex = (int) ((yvals[i] - ymin) / (barWidth / numOfShifts)); 

                //if (barIndex > 0 && yvals[i] == ymin + barIndex * (barWidth / numOfShifts))
                //    barIndex--;
                
                //int zIndex = j;

                double tmpx = yvals[i];
                double tmpy = freqs[j][barIndex];

                discs[j][i] = new Ellipse2D.Double(coord.x(tmpx) - 3, coord.y(tmpy) - 3, 6, 6);
                rugs[j][i] = new Line2D.Double(coord.x(yvals[i]), coord.y(0), coord.x(yvals[i]), coord.y(0) - 8);
            }
        }
        
        
    }

    /**
     * Reset the number of bars to the original value
     */
    public void resetPlot() {
        initPlot();
        countFreqs();
        repaint();
    }
    
    public void setBarNum(int num){
    	if(num >= MIN_BAR_NUM && num <= MAX_BAR_NUM){
    		numOfBars = num;
    		barWidth = (ymax - ymin) / numOfBars;
    		countFreqs();
    		repaint();
    	}
    }
    
    public void setShiftNum(int num){
    	if (num >= MIN_SHIFT && num <= MAX_SHIFT) {
    		numOfShifts = num;
    		countFreqs();
    		repaint();
    	}
    }
    
    public int getBarNum() {
    	return numOfBars;
    }

    /**
     * Set the GroupMaker object associated
     */
    public void setGroupMaker(EqualCountGrouper gm){
        grouper=gm;
    }
    

    /**
     * Change the current group to the index group
     */
    public void setGroup(int index) {

        if (index < 0 || index >= grouper.getGroupCount())
            return;


        z_level = index;
        //initPlot();

        countFreqs();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dim = getSize();

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        FontMetrics metrics = getFontMetrics(getFont());
        
        g2.drawString(slider.getLabel(), (getSize().width + slider.getSize().width) / 2 + slider.getGap(), slider.getLocation().y + slider.getSize().height);
		g2.drawString(slider.getTextValue(), slider.getLocation().x - metrics.stringWidth(slider.getTextValue()) - slider.getGap(), slider.getLocation().y + slider.getSize().height);
		labelRegion = new Rectangle((getSize().width + slider.getSize().width) / 2 + slider.getGap(), slider.getLocation().y, metrics.stringWidth(slider.getLabel()), metrics.getHeight());
		
        //Draw one horizontal line at the top, one at the bottom
        g2.drawLine(0, 0, dim.width - 1, 0);
        g2.drawLine(0, dim.height - 1, dim.width - 1, dim.height - 1);

        
        if (!plotState) {
            String errString = "Error getting data, please try again later.";
            g2.drawString(errString, (int) (dim.width / 2.0 - metrics.stringWidth(errString) / 2.0),
                    (int) (dim.height / 2.0 - metrics.getHeight() / 2.0));
            return;
        }
        
        g2.fill(popupArrow);

        //draw x axis
        XAxisRenderer xaxis = new XAxisRenderer(xAxisModel, getSize(), insets);
        xaxis.setLeftGap(20);
        xaxis.setRightGap(20);
        xaxis.setLabel(y_var.getName());
        SwingUtilities.paintComponent(g2, xaxis, this, insets.left - xaxis.getLeftGap(), 
				getSize().height - insets.bottom, xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        xAxisRegion.setBounds(insets.left - xaxis.getLeftGap(), getSize().height - insets.bottom, 
        		xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        
        //draw y axis
        YAxisRenderer yaxis = new YAxisRenderer(yAxisModel, getSize(), insets);
        yaxis.setTopGap(20);
		yaxis.setRightGap(20);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		yAxisRegion.setBounds(0,0,0,0);
        

        if (!drawBar) {
        	g2.setColor(Color.gray);
        	for (int i = 0; i < bars[z_level].length; i++) {
        		Rectangle r = bars[z_level][i].getBounds();
                g2.drawLine(r.x, r.y, r.x + r.width, r.y);
                if (i > 0)
                  	g2.drawLine((int)bars[z_level][i-1].getMaxX(), (int)bars[z_level][i-1].getMinY(), r.x, r.y);
            }
        	g2.setColor(Color.black);
        		
        	for (int i = 0; i < discs[0].length; i++) {
        		if (data.getMask(i)) continue;
        			
        		if (!drawRug) {
        			if (z_var == null) {
        				g2.setColor(data.getColor(i));
        				if (data.getState(i)) {
        					g2.fill(discs[0][i]);
        					if(data.getLabel(i)!=null){
        						g2.setColor(Color.black);
        						g2.drawString(data.getLabel(i),(int)discs[0][i].getMaxX()+2,(int)discs[0][i].getMinY()-2);
        					}
        				}
        				else
        					g2.draw(discs[0][i]);
        				
        			} else {
        				if (grouper.getGroupIndex(i).contains(new Integer(z_level))) {
        					g2.setColor(data.getColor(i));

        					if (data.getState(i)) {
        						g2.fill(discs[z_level][i]);
        						if(data.getLabel(i)!=null){
        							g2.setColor(Color.black);
        							g2.drawString(data.getLabel(i),(int)discs[z_level][i].getMaxX()+2,(int)discs[z_level][i].getMinY()-2);
        						}
        					}
        					else
        						g2.draw(discs[z_level][i]);
        				}
        			}
        		} else {
        			if (z_var == null) {
           				g2.setColor(data.getColor(i));
           				g2.draw(rugs[0][i]);           				
           			} else {
           				if (grouper.getGroupIndex(i).contains(new Integer(z_level))) {
    						g2.setColor(data.getColor(i));
    						g2.draw(rugs[z_level][i]);
           				}
           			}        			
        		}
        	}
        }
        else { //drawBar
        	g2.setColor(Color.black);
            for (int i = 0; i < bars[z_level].length; i++) {
                if (freqs[z_level][i] > 0)
                    g2.draw(bars[z_level][i]);
            }
        }
        

        
        if (mouseDragging) {
            g2.setColor(Color.lightGray);
            g2.draw(selectionRange);
        }
        
    }


    public void updatePlot(String arg) {

        if (arg.equals("obs_deleted")) {
            initPlot();
            countFreqs();
            repaint();
        } else if (arg.equals("yystate")) {
            repaint();

        } else if (arg.equals("yycolor")) {
            repaint();
        } else if (arg.equals("yymask")) {
            initPlot();//add djluo
            countFreqs();
            repaint();

            //following code is added by djluo for transformation
        } else if (arg.equals("add_variable")||arg.equals("delete_variable")) {
        	
        	if(data.getTransformedVar(init_y_var.getName()) != null)
        	    y_var = data.getTransformedVar(init_y_var.getName());
        	else
        	    y_var = init_y_var;
        	initPlot();
        	countFreqs();
        	repaint();
        }

    }
    
    public void stateChanged(ChangeEvent e) {
		if (slider == barSlider) {
			setBarNum(Math.round(slider.getValue()));
		} else {
			setShiftNum(Math.round(slider.getValue()));
		}
	}
    
    public void actionPerformed(ActionEvent ae){
    	String arg = ae.getActionCommand();
    	Object src = ae.getSource();
    		
    	if (arg.equals("Bars")){
    		slider = barSlider;
    		barSlider.setVisible(true);
    		shiftSlider.setVisible(false);
    		countFreqs();
            repaint();
    	}
    	else if (arg.equals("Shifts")){
    		slider = shiftSlider;
    		barSlider.setVisible(false);
    		shiftSlider.setVisible(true);
    		countFreqs();
            repaint();	
    	}
    	return;
    }
    

    class MouseHandler extends MouseAdapter implements MouseMotionListener {

        public void mousePressed(MouseEvent e) {

            int x = e.getX(), y = e.getY();

            if(labelRegion.contains(x, y)){
            	popup.show(e.getComponent(), x, labelRegion.getLocation().y + labelRegion.getSize().height);
            	return;
            }
            
            if (popupArrow.getBounds().contains(x, y)) {
            	JPopupMenu popupOptionMenu = ((HistogramModule) module).getOptionMenu().getPopupMenu();
            	popupOptionMenu.show(e.getComponent(), x, y);
            	return;
            }
 
            if (drawBar) return;
            
            boolean[] states = new boolean[data.getSize()];
            
            if (LinkPolicy.mode != LinkPolicy.SELECTING)
            {
            	last_x = x;
            	last_y = y;
            	first_x = x - brushWidth;
            	first_y = y - brushHeight;
            	
            	
                //data.clearStates();
            	Arrays.fill(states, false);
                selectionRange.setRect(first_x, first_y, last_x - first_x, last_y - first_y);

                boolean found = false;
                    
                for (int i = 0; i < discs[0].length; i++) {
                    boolean rightGroup = false;
                    if (z_var == null)
                        rightGroup = true;
                    else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                        rightGroup = true;

                    if (rightGroup) {
                    	if (!drawRug)
                    		found = discs[z_level][i]!=null && selectionRange.contains(discs[z_level][i].getBounds());
                    	else
                    		found = rugs[z_level][i]!=null && selectionRange.intersectsLine(rugs[z_level][i]);
                    		
                        if (found) 
                        	states[i] = true; //data.setState(true, i);
                    }
                }
                data.setStates(states);
            	
                mouseDragging = true;
                ASHistogramPlot.this.repaint();
                return;
            }
            
            // when LinkPolicy.mode == SELECTING

            first_x = x;
            first_y = y;
            mouseDragging = true;

            boolean multipleSelection = false;

            if (e.getModifiers() == 17) { //Shift Key on Mac
                multipleSelection = true;
            }

            int i = 0;
            boolean foundone = false;
            states = data.getStates();

            while (i < discs[0].length /*&& !found*/) {
                boolean found = false;
              	boolean rightGroup = false;
                if (z_var == null)
                    rightGroup = true;
                else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                    rightGroup = true;

                if (rightGroup) {
                	if (!drawRug)
                		found = discs[z_level][i]!=null && discs[z_level][i].contains(x, y);
                	else
                		found = rugs[z_level][i]!=null && rugs[z_level][i].intersectsLine(x-1, y, x+1, y);
               		
                    if (found) {
                            
                        if (e.getClickCount() == 2) {
                            Color c = JColorChooser.showDialog(ASHistogramPlot.this, "Palette", Color.black);
                            if (c != null && data.getState(i))
                                data.setColor(c, i);

                            return;
                        }

                        if (!multipleSelection && !foundone) {
                            //data.clearStates();
                        	Arrays.fill(states, false);
                        }

                        //boolean bl = data.getState(i);
                        //data.setState(!bl, i);
                        states[i] = !states[i];
                                
                        foundone = true;
                            
                    }

                }
                i++;
            }

            

            if (!foundone) 
                data.clearStates();
            else
            	data.setStates(states);
            

        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && xAxisRegion.contains(e.getPoint())) {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(xAxisModel);
                int option = JOptionPane.showOptionDialog(ASHistogramPlot.this, configPanel,
                        "x-axis settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if (option == JOptionPane.OK_OPTION) {
                    BaseAxisModel model = configPanel.getAxisModel();
                    if (model != null) {
                        setXAxisModel(model);
                    } else
                        JOptionPane.showMessageDialog(ASHistogramPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getClickCount() == 2 && yAxisRegion.contains(e.getPoint())) {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(yAxisModel);
                int option = JOptionPane.showOptionDialog(ASHistogramPlot.this, configPanel,
                        "y-axis settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if (option == JOptionPane.OK_OPTION) {
                    BaseAxisModel model = configPanel.getAxisModel();
                    if (model != null) {
                        setYAxisModel(model);
                    } else
                        JOptionPane.showMessageDialog(ASHistogramPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } 
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {

        	if (drawBar) return;

            boolean found = false;
            
            boolean[] states = data.getStates();
            for (int i = 0; i < discs[0].length; i++) {
                boolean rightGroup = false;
                if (z_var == null)
                    rightGroup = true;
                else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                    rightGroup = true;

                if (rightGroup) {
                	if (!drawRug)
                		found = discs[z_level][i]!=null && selectionRange.contains(discs[z_level][i].getBounds());
                	else
                		found = rugs[z_level][i]!=null && selectionRange.intersectsLine(rugs[z_level][i]);
                		
                    if (found) {
                    	states[i] = true; //data.setState(true, i);
                    }
                }
            }
            data.setStates(states);

            mouseDragging = false;
            selectionRange.setRect(0, 0, 0, 0);
            
            repaint();
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            last_x = e.getX();
            last_y = e.getY();
            
            if (drawBar) return;
            
            if (mouseDragging && LinkPolicy.mode != LinkPolicy.SELECTING)
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
                    
                selectionRange.setRect(first_x, first_y, last_x - first_x, last_y - first_y);

                boolean found = false;
                    
                for (int i = 0; i < discs[0].length; i++) {
                    boolean rightGroup = false;
                    if (z_var == null)
                        rightGroup = true;
                    else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                        rightGroup = true;

                    if (rightGroup) {
                    	if (!drawRug)
                    		found = discs[z_level][i]!=null && selectionRange.contains(discs[z_level][i].getBounds());
                    	else
                    		found = rugs[z_level][i]!=null && selectionRange.intersectsLine(rugs[z_level][i]);
                    		
                        if (found) 
                        	states[i] = true; //data.setState(true, i);
                    }
                }
                data.setStates(states);
            }    
            
            
            selectionRange.setRect(first_x, first_y, last_x - first_x, last_y - first_y);
            repaint();
            
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    private void printArray(double[] array){
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]+",");
        }
        System.out.println();
    }

    

}
