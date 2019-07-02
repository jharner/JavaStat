package wvustat.modules;

import wvustat.interfaces.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.plot.ManualAxisModel;
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
 * HistogramPlot plots data as a histogram. That is, it separates observations into groups and
 * plots bars to represent frequency of each group. It is used in HistogramModule.
 */
public class HistogramPlot extends JPanel implements ChangeListener, ActionListener 
{
	private static final long serialVersionUID = 1L;

	public static final int FREQ_AXIS = 0;
    public static final int REL_FREQ_AXIS = 1;
    public static final int DENSITY_AXIS = 2;

    private DataSet data;
    private int width = 250, height = 200;
    private transient int[][] freqs;
    private Variable y_var = null, z_var = null;
    private Vector vz;
    private boolean plotState = true;
    private int numOfBars = 5, maxFreq;
    private int z_level = 0;
    private CoordConverter coord = null;
    private double ymin, ymax, barWidth, ticWidth;
    private Rectangle2D[][] tiles;
    private Rectangle2D[][] bars;
    private Line2D[][] rugs;

    private Rectangle2D selectionRange = new Rectangle2D.Float(0, 0, 0, 0);

    //keep track of positions of mouse movement
    private int last_x, last_y, first_x, first_y, brushWidth = 10, brushHeight = 10;
    private boolean mouseDragging = false, drawNull = false, drawCI = false, DragLine = false;

    private Vector varList, zvals;

    //private BarAdjuster adjuster;

    private boolean drawDensity = false;
    private boolean drawKernel = false;
    private boolean drawRug = false;
    private boolean isAreaLinking = false;
    private int[][] tileIndices;  // the index of bar a tile belongs to
    
    //private boolean relFreqAxis=false, densityAxis=false;
    private int axisOption = FREQ_AXIS;

    private EqualCountGrouper grouper;

    private Hypothesis hypo;
 
    private GeneralPath popupArrow;
    
    private SimpleSlider slider, barSlider, kernelSlider;
    private final int sliderTop = 5;
    private Rectangle labelRegion;
    
    private static final int MIN_BAR_NUM = 2, MAX_BAR_NUM = 30;
    private static final double BW_MIN = 0.1, BW_MAX = 2.5;
    
    private JPopupMenu popup;
    
    private GUIModule module;
    private int[][] rank;

    private double freqSum;
    private double unitFreq;
    private Vector yValues = new Vector();
    private double[] yvals;
    private Insets insets = new Insets(60, 50, 40, 40);
    private BaseAxisModel xAxisModel;
    private BaseAxisModel yAxisModel;
    private Rectangle xAxisRegion = new Rectangle();
    private Rectangle yAxisRegion = new Rectangle();
    private double[][] normalDensityArray;
    private double[][] kernelDensityArray;
    private double kernelBandwidth = 0;
    private double defaultKernelBandwidth = 0;
    private Variable init_y_var = null;

    /**
     * Constructor
     * <p/>
     * HistogramPlot is the actual plot, it does not have buttons, reports, etc. Don't forget to set clientID
     * after instantiating a new object.
     */
    public HistogramPlot(DataSet data, Variable y_var, Vector vz) throws IllegalArgumentException 
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

        //This vector is used to hold all names of variables
        //that are of interest to this object
        /*varList = new Vector();

        varList.addElement(y_var.getName());

        if (z_var != null) {
            varList.addElement(z_var.getName());
        }*/

        popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Bars");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Kernal Bandwidth");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        
        barSlider = new SimpleSlider(MIN_BAR_NUM, MAX_BAR_NUM, numOfBars);
		barSlider.setLabel("Bars");
		barSlider.addChangeListener(this);	
        //barSlider.setPopup(null);
        
        kernelSlider = new SimpleSlider();
        kernelSlider.setFractionDigits(3);
        kernelSlider.setLabel("Kernel Bandwidth");
 	    //kernelSlider.setPopup(popup);
 	    kernelSlider.setVisible(false);
        
        setLayout(null);
		add(barSlider);
		add(kernelSlider);
		slider = barSlider;
        
        //adjuster=new BarAdjuster();
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
		kernelSlider.setBounds((w - size.width) / 2, sliderTop, size.width, size.height);
		
        super.setBounds(x, y, w, h);
    }
    
    
    /**
     * set the Hypothesis
     */
    public void setHypothesis(Hypothesis h) {
        hypo = h;
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
        ticWidth = barWidth;

        numOfBars = (int) Math.round((ymax - ymin) / barWidth);
        barSlider.setValue(numOfBars);
        
        countFreqs();
        repaint();

    }

    public BaseAxisModel getXAxisModel() {
        return xAxisModel;
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
            
            //added by djluo. When data change, we need to make sure the range of axis contains the new data values,
            //even if the axis model is set as manual mode. 
            if (ymin < xAxisModel.getStartValue() || ymax >= xAxisModel.getEndValue())  // > to >=
            	xAxisModel = new BaseAxisModel(ymin, ymax, numOfBars);

            ymin = xAxisModel.getStartValue();
            ymax = xAxisModel.getEndValue();
            barWidth = xAxisModel.getInterval();
            ticWidth = barWidth;

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
        freqSum = 0;
        int localMax = 0;
        
        rank = null;
        yvals = y_var.getNumValues();

        if (z_var == null) 
        {

            freqs = new int[1][numOfBars];
            bars = new Rectangle2D[1][numOfBars];

            for (int i = 0; i < freqs[0].length; i++)
                freqs[0][i] = 0;

            for (int i = 0; i < freqs[0].length; i++) {
                double lower = ymin + i * barWidth;
                double upper = lower + barWidth;

                for (int j = 0; j < yvals.length; j++) {
                    /*
                 	if (data.getMask(j) == false && i == 0 && yvals[j] >= lower && yvals[j] <= upper)
                    {
                        freqs[0][i]++;
                    }
                    else if (data.getMask(j) == false && yvals[j] > lower && yvals[j] <= upper)
                    {
                        freqs[0][i]++;
                    }
                    */
                	if (data.getMask(j) == false && yvals[j] >= lower && yvals[j] < upper)
                		freqs[0][i]++;
                 	
                }

                if (freqs[0][i] > maxFreq) maxFreq = freqs[0][i];
            }

            rank = data.getRanks((EqualCountGrouper)null, y_var.getName());

        } else {
            if (grouper == null)
                grouper = new EqualCountGrouper(vz, data);

            zvals = z_var.getValues();
            freqs = new int[grouper.getGroupCount()][numOfBars];
            bars = new Rectangle2D[grouper.getGroupCount()][numOfBars];

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
                for (int j = 0; j < freqs[i].length; j++) {
                    double lower = ymin + j * barWidth;
                    double upper = lower + barWidth;

                    for (int k = 0; k < yvals.length; k++) {
                        if (!data.getMask(k) && grouper.getGroupIndex(k).contains(new Integer(i))) {
                            /*if (j == 0 && yvals[k] >= lower && yvals[k] <= upper)
                                freqs[i][j]++;
                            else if (yvals[k] > lower && yvals[k] <= upper)
                                freqs[i][j]++;
                            */
                        	if (yvals[k] >= lower && yvals[k] < upper)
                        		freqs[i][j]++;
                        }
                    }

                }
            }

            for (int i = 0; i < freqs[z_level].length; i++) {
                if (freqs[z_level][i] > localMax)
                    localMax = freqs[z_level][i];
            }

            for (int i = 0; i < freqs.length; i++) {
                for (int j = 0; j < freqs[i].length; j++) {
                    if (freqs[i][j] > maxFreq)
                        maxFreq = freqs[i][j];
                }
            }

            rank = data.getRanks(grouper, y_var.getName());

        }

        /*for (int i = 0; i < freqs.length; i++) {
            for (int j = 0; j < freqs[i].length; j++) {
                freqSum += freqs[i][j];
            }
        }*/

        for (int i = 0; i < freqs[z_level].length; i++) {
           	freqSum += freqs[z_level][i];
        }
            
        if(z_var == null) {
            tiles = new Rectangle2D[1][yvals.length];
            tileIndices = new int[1][yvals.length];
            rugs = new Line2D[1][yvals.length];
        } else {
            tiles = new Rectangle2D[grouper.getGroupCount()][yvals.length];
            tileIndices = new int[grouper.getGroupCount()][yvals.length];
            rugs = new Line2D[grouper.getGroupCount()][yvals.length];
        }
        
        /* Initialize tileIndices.
         * for (int i = 0; i < tileIndices.length; i++) {
         	  for (int j = 0; j < tileIndices[i].length; j++) {
            		tileIndices[i][j] = -1;
            	}
        }*/
            
        Dimension d = getSize();
        double tmpMax;

        int ticCount = 5;
        if (axisOption == REL_FREQ_AXIS) {
            tmpMax = maxFreq / freqSum;
            unitFreq = 1 / freqSum;
            insets.left = 70;

            yAxisModel = new BaseAxisModel(0, tmpMax, ticCount, BaseAxisModel.MIN_FIXED);
            tmpMax = yAxisModel.getEndValue();
        } 
        else if (axisOption == DENSITY_AXIS) 
        {
            if (yAxisModel instanceof ManualAxisModel)
                tmpMax = yAxisModel.getEndValue();
            else
                tmpMax = maxFreq / freqSum / barWidth;
            unitFreq = tmpMax / maxFreq;
            insets.left = 70;
            generateNormalDensityArray();
            generateKernelDensityArray();
            /*
            for (int i = 0; i < normalDensityArray[1].length; i++) {
                if (normalDensityArray[1][i] > tmpMax)
                    tmpMax = normalDensityArray[1][i];
            }
            */
               
            if (!(yAxisModel instanceof ManualAxisModel)) {
                yAxisModel = new BaseAxisModel(0, tmpMax, ticCount, BaseAxisModel.MIN_FIXED);
              	tmpMax = yAxisModel.getEndValue();
            }
        } 
        else 
        {
           	tmpMax = maxFreq;
           	unitFreq = 1;
           	insets.left = 50;
            yAxisModel = new BaseAxisModel(0, tmpMax, ticCount, BaseAxisModel.MIN_FIXED | BaseAxisModel.INT_SCALE);
            tmpMax = yAxisModel.getEndValue();
        }


        coord = new CoordConverter(d.width, d.height, ymin, ymax, 0, tmpMax, insets);

        int w = coord.x(ymin + barWidth) - coord.x(ymin);
        double h = coord.y(0) - coord.y(unitFreq);

        int[][] partial_sum = null;

        if (z_var == null)
            partial_sum = new int[1][numOfBars];
        else
            partial_sum = new int[grouper.getGroupCount()][numOfBars];

        //copy frequency values into this array
        for (int i = 0; i < partial_sum.length; i++)
            for (int j = 0; j < partial_sum[i].length; j++)
                partial_sum[i][j] = freqs[i][j];

        for (int i = 0; i < partial_sum.length; i++) {
            for (int j = 1; j < partial_sum[i].length; j++) {

                partial_sum[i][j] += partial_sum[i][j - 1];

            }
        }

        for (int i = 0; i < bars.length; i++) {
            for (int j = 0; j < bars[i].length; j++) {
                w = coord.x(ymin + (j + 1) * barWidth) - coord.x(ymin + j * barWidth);

                double tmpFreq = freqs[i][j];

                if (axisOption == REL_FREQ_AXIS) {
                    tmpFreq = freqs[i][j] / freqSum;
                } else if (axisOption == DENSITY_AXIS) {
                    tmpFreq = freqs[i][j] / freqSum / barWidth;
                }

                bars[i][j] = new Rectangle2D.Float(coord.x(ymin + j * barWidth), coord.y(tmpFreq), w, coord.y(0) - coord.y(tmpFreq));

            }
        }
            
        for (int j = 0; j < tiles.length; j++) {
            for (int i = 0; i < tiles[j].length; i++) {
              	
                if(data.getMask(i)) continue;   //considering missing obs for yvals, added by djluo
                
                if(grouper != null && !grouper.getGroupIndex(i).contains(new Integer(j))) continue;
            	
                int barIndex = (int) ((yvals[i] - ymin) / barWidth); 

                /*if (barIndex > 0 && yvals[i] == ymin + barIndex * barWidth)
                    barIndex--;*/
                
                tileIndices[j][i] = barIndex;
                
                /*int zIndex = 0;
                if (z_var != null) {
                    zIndex = grouper.getGroupIndex(zvals.elementAt(i));
                }*/
                int zIndex = j;

                double tmpx = ymin + barIndex * barWidth;
                w = coord.x(tmpx + barWidth) - coord.x(tmpx);

                double tmpy;
                if (barIndex > 0) {
                    tmpy = rank[j][i] - partial_sum[zIndex][barIndex - 1];
                    
                } else {
                    tmpy = rank[j][i];
                }

                if (axisOption == REL_FREQ_AXIS) {
                    tmpy = tmpy / freqSum;
                } else if (axisOption == DENSITY_AXIS) {
                    tmpy = tmpy / freqSum / barWidth;
                }

                h = bars[zIndex][barIndex].getHeight() * 1.0 / freqs[zIndex][barIndex];

                tiles[j][i] = new Rectangle2D.Double(coord.x(tmpx) + 1, coord.y(tmpy), w - 1, h);
                
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
    
    public int getBarNum() {
    	return numOfBars;
    }

    /**
     * Get the GroupMaker object used in this HistogramPlot object
     */
    public EqualCountGrouper getGroupMaker() {
    	return grouper;
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
        
        g2.setColor(Color.black);
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
		
		if (axisOption == DENSITY_AXIS)
			yAxisRegion.setBounds(0, insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		else
			yAxisRegion.setBounds(0,0,0,0);

		if (!drawRug) {
			if (!isAreaLinking) {
				for (int i = 0; i < tiles[0].length; i++) {
					if (z_var == null) {
						g2.setColor(data.getColor(i));
						if (data.getState(i) && !data.getMask(i)) {
							g2.fill(tiles[0][i]);
							if (data.getLabel(i) != null) {
								g2.setColor(Color.black);
								g2.drawString(data.getLabel(i), (int) tiles[0][i].getMaxX() + 2, (int) tiles[0][i].getMinY() - 2);
							}
						}
					} else {
						if (grouper.getGroupIndex(i).contains(new Integer(z_level))) {
							g2.setColor(data.getColor(i));

							if (data.getState(i) && !data.getMask(i)) {
								g2.fill(tiles[z_level][i]);
								if (data.getLabel(i) != null) {
									g2.drawString(data.getLabel(i), (int) tiles[z_level][i].getMaxX() + 2, (int) tiles[z_level][i].getMinY() - 2);
								}
							}
						}
					}
				}
			} else {
        		
				int[] tileCount = new int[bars[z_level].length];
				for (int i = 0; i < tileCount.length; i++)
					tileCount[i] = 0;
        		
				for (int i = 0; i < tiles[0].length; i++) {
					if (z_var == null) {
						if (data.getState(i) && !data.getMask(i)) {
							tileCount[tileIndices[0][i]]++;
						}
					} else {
						if (grouper.getGroupIndex(i).contains(new Integer(z_level))) {
							if (data.getState(i) && !data.getMask(i)) {
								tileCount[tileIndices[z_level][i]]++;
							}
						}
					}
				}
        		
				g2.setColor(Color.black);
				for (int i = 0; i < tileCount.length; i++) {
					if (tileCount[i] > 0) {
						double h = bars[z_level][i].getHeight() * tileCount[i] / freqs[z_level][i];
						Rectangle2D rect = new Rectangle2D.Double(bars[z_level][i].getMinX(), bars[z_level][i].getMaxY() - h, bars[z_level][i].getWidth(), h);
						g2.fill(rect);
					}
				}
			}
		}
		else { //draw rug
       		for (int i = 0; i < rugs[0].length; i++) {
       			if (z_var == null) {
       				g2.setColor(data.getColor(i));
       				if (!data.getMask(i)) {
       					g2.draw(rugs[0][i]);
       				}
       			} else {
       				if (grouper.getGroupIndex(i).contains(new Integer(z_level))) {
						g2.setColor(data.getColor(i));
						if (!data.getMask(i)) {
							g2.draw(rugs[z_level][i]);
						}
       				}
       			}
       		}
       	}

        if (drawDensity) {
            g2.setColor(Color.red);
            int top = slider.getLocation().y + slider.getSize().height + 1;

            for (int i = 0; i < normalDensityArray[0].length - 1; i++) {
           	    if (coord.y(normalDensityArray[1][i])>top && coord.y(normalDensityArray[1][i + 1])>top)
                    g2.drawLine(coord.x(normalDensityArray[0][i]), coord.y(normalDensityArray[1][i]), coord.x(normalDensityArray[0][i + 1]), coord.y(normalDensityArray[1][i + 1]));

            }
        }
            
        if (drawKernel) {
            g2.setColor(Color.blue);
            int top = slider.getLocation().y + slider.getSize().height + 1;

            for (int i = 0; i < kernelDensityArray[0].length - 1; i++) {
           	    if (coord.y(kernelDensityArray[1][i])>top && coord.y(kernelDensityArray[1][i + 1])>top)
                    g2.drawLine(coord.x(kernelDensityArray[0][i]), coord.y(kernelDensityArray[1][i]), coord.x(kernelDensityArray[0][i + 1]), coord.y(kernelDensityArray[1][i + 1]));

            }
        }

        //Draw bars
        if (!drawRug) {
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

        //Draw a green line to indicate null value if current report is "t-test"
        if (drawNull && (z_var == null || yValues.size() > 0)) {
            double nullVal = hypo.getNull();
            g2.setStroke(new BasicStroke(2.0f));
            g2.setColor(Color.green);
            g2.drawLine(coord.x(nullVal), coord.y(0), coord.x(nullVal), coord.y(0) + 8);
        }

        if (drawCI && (z_var == null || yValues.size() > 0)) {
            double lo = hypo.getLowerLimit();
            double hi = hypo.getUpperLimit();

            g2.setStroke(new BasicStroke(1.0f));
            g2.setColor(Color.red);
            g2.drawLine(coord.x(lo), coord.y(0), coord.x(lo), coord.y(0) + 8);
            g2.drawLine(coord.x(hi), coord.y(0), coord.x(hi), coord.y(0) + 8);
            g2.drawLine(coord.x(lo), coord.y(0) + 4, coord.x(hi), coord.y(0) + 4);
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
        	yAxisModel = null;
        	initPlot();
        	countFreqs();
        	repaint();
        	
        }

    }


    public void setAxisOption(int option) {
        axisOption = option;
        if (option != DENSITY_AXIS)
        {
            drawDensity = false;
            drawKernel = false;
            slider = barSlider;
            //barSlider.setPopup(null);
            barSlider.setVisible(true);
    		kernelSlider.setVisible(false);
        }
        else {
        	//barSlider.setPopup(popup);
        }
        countFreqs();
        repaint();
    }

    /**
     * Toggle the "normal density" option to on/off
     */
    public void enableNormalDensity(boolean val) {
        drawDensity = val;
        countFreqs();
        repaint();
    }

    /**
     * Toggle the "kernel density" option to on/off
     */
    public void enableKernelDensity(boolean val) {
        if (val) { 
        	slider = kernelSlider;
        	barSlider.setVisible(false);
        	kernelSlider.setVisible(true);
        } else { 
        	slider = barSlider;
            barSlider.setVisible(true);
    		kernelSlider.setVisible(false);
        }
        
    	drawKernel = val;
        countFreqs();
        repaint();
    }
    
    public void enableAreaLinking(boolean val) {
    	isAreaLinking = val;
    	repaint();
    }
    
    public void enableRugDrawing(boolean val) {
    	drawRug = val;
    	repaint();    	
    }
    
    public void setKernelBandwidth(double val) {
    	if( val >= BW_MIN * getDefaultKernelBandwidth() && val <= BW_MAX * getDefaultKernelBandwidth()){
    		kernelBandwidth = val;
    		countFreqs();
    		repaint();
    	}
    }
    
    public double getKernelBandwidth() {
        return kernelBandwidth;
    }
    
    public double getDefaultKernelBandwidth() {
        return defaultKernelBandwidth;
    }
    
    private void generateNormalDensityArray() {
        StatEngine se = new StatEngineImpl();
        double mu, s;
        try {


            if (z_var == null) {
                mu = y_var.getMean();
                s = y_var.getStdDev();
            } else {
                mu = se.getMean(yValues);
                s = se.getStdDev(yValues);
            }

            Distribution normal = se.getDistribution("normal", new double[]{mu, s});


            double delta = (ymax - ymin) / 100;
            normalDensityArray = new double[2][101];
            for (int i = 0; i < normalDensityArray[0].length; i++) {
                normalDensityArray[0][i] = ymin + delta * i;
                normalDensityArray[1][i] = normal.pdf(normalDensityArray[0][i]);
            }
        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void generateKernelDensityArray() {
        StatEngine se = new StatEngineImpl();
        double[] vals;
        try {


            if (z_var == null) {
                vals = y_var.getSortedNumValues();
            } else {
            	   vals = new double[yValues.size()];
            	   for(int i=0; i<yValues.size(); i++)
            	       vals[i] = ((Double)yValues.elementAt(i)).doubleValue();
            }

            double[] params = new double[vals.length + 1];
            
            if (kernelBandwidth == 0){
            	   double[] quantiles = se.getQuantiles(vals);
            	   double iqr = quantiles[3] - quantiles[1];
            	   kernelBandwidth = 1.06 * Math.min(se.getStdDev(vals), iqr/1.34) * Math.pow(vals.length, -1.0/5.0);  //initial value for bandwidth
            	   defaultKernelBandwidth = kernelBandwidth;
            	   
            	   kernelSlider.setMinimum((float)(BW_MIN * getDefaultKernelBandwidth()));
            	   kernelSlider.setMaximum((float)(BW_MAX * getDefaultKernelBandwidth()));
            	   kernelSlider.setValue((float)kernelBandwidth);
            	   kernelSlider.addChangeListener(this);	
            }
            
            params[0] = kernelBandwidth;
            
            for(int i=0; i<vals.length; i++)
    		    		params[i+1] = vals[i];
    		
            Distribution kernel = se.getDistribution("kernel", params);


            double delta = (ymax - ymin) / 100;
            kernelDensityArray = new double[2][101];
            for (int i = 0; i < kernelDensityArray[0].length; i++) {
                kernelDensityArray[0][i] = ymin + delta * i;
                kernelDensityArray[1][i] = kernel.pdf(kernelDensityArray[0][i]);
            }
        }
        catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    
    

    public Dimension getSize() {
        return new Dimension(width, height);
    }


    public void enableNull(boolean value) {
        drawNull = value;
        drawCI = false; //added by djluo
        repaint();
    }

    public void enableCI(boolean value) {
        drawNull = value;
        drawCI = value;
        repaint();
    }

    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();

        if (obj instanceof ReportBoard) {
            ReportBoard b = (ReportBoard) obj;
            String title = b.getCurrentReportTitle();
            if (title.equals("t-test")) {
                //draw a red line to indicate mean
                drawNull = true;
                drawCI = false;
                repaint();
            } else if (title.equals("C.I.")) {
                //draw a red rectangle to indicate confidence band
                drawNull = true;
                drawCI = true;
                repaint();
            } else {
                drawNull = false;
                drawCI = false;
                repaint();
            }
        } else if (obj instanceof Hypothesis) {
            repaint();
        } else if (obj instanceof SimpleSlider) {
        	if (slider == barSlider) {
    			setBarNum(Math.round(slider.getValue()));
    		} else {
    			setKernelBandwidth(slider.getValue());
    		}
        }
    }
    
    public void actionPerformed(ActionEvent ae){
    	String arg = ae.getActionCommand();
    	Object src = ae.getSource();
    		
    	if (arg.equals("Bars")){
    		((HistogramModule) module).enableKernelDensity(new Boolean(false));
    	}
    	else if (arg.equals("Kernal Bandwidth")){
    		((HistogramModule) module).enableKernelDensity(new Boolean(true));
    	}
    	return;
    }
    

    class MouseHandler extends MouseAdapter implements MouseMotionListener {

        public void mousePressed(MouseEvent e) {

            int x = e.getX(), y = e.getY();
            
            if(labelRegion.contains(x, y) && axisOption == DENSITY_AXIS){
            	popup.show(e.getComponent(), x, labelRegion.getLocation().y + labelRegion.getSize().height);
            	return;
            }
           
            if (popupArrow.getBounds().contains(x, y)) {
            		JPopupMenu popupOptionMenu = ((HistogramModule) module).getOptionMenu().getPopupMenu();
            		popupOptionMenu.show(e.getComponent(), x, y);
            		return;
            }
            
            
            if(drawNull && y>coord.y(0) && y<(coord.y(0) + 8) && Math.abs(x - coord.x(hypo.getNull())) < 5){
            		DragLine=true;
            		return;
            }
            
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
                    
                for (int i = 0; i < tiles[0].length; i++) {
                    boolean rightGroup = false;
                    if (z_var == null)
                        rightGroup = true;
                    else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                        rightGroup = true;

                    if (rightGroup) {
                    	if (!drawRug) {
                    		if (!isAreaLinking)
                    			found = tiles[z_level][i]!=null && selectionRange.intersects(tiles[z_level][i]);
                    		else
                    			found = tiles[z_level][i]!=null && selectionRange.intersects(bars[z_level][tileIndices[z_level][i]]);
                    	} else {
                    		found = rugs[z_level][i]!=null && selectionRange.intersectsLine(rugs[z_level][i]);
                    	}
                    	
                        if (found) 
                            states[i] = true; //data.setState(true, i);
                    }
                }
                
                data.setStates(states);
            	
                mouseDragging = true;
                HistogramPlot.this.repaint();
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
            
            while (i < tiles[0].length /*&& !found*/) {
                boolean found = false;
              	boolean rightGroup = false;
                if (z_var == null)
                    rightGroup = true;
                else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                    rightGroup = true;

                if (rightGroup) {
                	if (!drawRug) {
                		if (!isAreaLinking)
                			found = tiles[z_level][i]!=null && tiles[z_level][i].contains(x, y);
                		else
                			found = tiles[z_level][i]!=null && bars[z_level][tileIndices[z_level][i]].contains(x, y);
                	} else {
                		found = rugs[z_level][i]!=null && rugs[z_level][i].intersectsLine(x-1, y, x+1, y);
                	}
                	
                    if (found) {
                            
                        if (e.getClickCount() == 2 && !isAreaLinking) {
                            Color c = JColorChooser.showDialog(HistogramPlot.this, "Palette", Color.black);
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
                int option = JOptionPane.showOptionDialog(HistogramPlot.this, configPanel,
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
                        JOptionPane.showMessageDialog(HistogramPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getClickCount() == 2 && yAxisRegion.contains(e.getPoint())) {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(yAxisModel);
                int option = JOptionPane.showOptionDialog(HistogramPlot.this, configPanel,
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
                        JOptionPane.showMessageDialog(HistogramPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } 
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
            
            boolean found = false;
            
            boolean[] states = data.getStates();
            for (int i = 0; i < tiles[0].length; i++) {
                boolean rightGroup = false;
                if (z_var == null)
                    rightGroup = true;
                else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                    rightGroup = true;

                if (rightGroup) {
                	if (!drawRug) {
                		if (!isAreaLinking)
                			found = tiles[z_level][i]!=null && selectionRange.intersects(tiles[z_level][i]);
                		else
                			found = tiles[z_level][i]!=null && selectionRange.intersects(bars[z_level][tileIndices[z_level][i]]);
                	} else {
                		found = rugs[z_level][i]!=null && selectionRange.intersectsLine(rugs[z_level][i]);
                	}
                	
                    if (found) { 
                        states[i] = true; //data.setState(true, i);
                    }

                }

            }
            data.setStates(states);

            mouseDragging = false;
            selectionRange.setRect(0, 0, 0, 0);
            DragLine = false;
            
            repaint();
            

        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            last_x = e.getX();
            last_y = e.getY();

            if(DragLine){
            		hypo.setNull(coord.inverseX(last_x));
            		repaint();
            		return;
            }
            
            
            
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
                    
                for (int i = 0; i < tiles[0].length; i++) {
                    boolean rightGroup = false;
                    if (z_var == null)
                        rightGroup = true;
                    else if (z_var != null && grouper.getGroupIndex(i).contains(new Integer(z_level)))
                        rightGroup = true;

                    if (rightGroup) {
                    	if (!drawRug) {
                    		if (!isAreaLinking)
                    			found = tiles[z_level][i]!=null && selectionRange.intersects(tiles[z_level][i]);
                    		else
                    			found = tiles[z_level][i]!=null && selectionRange.intersects(bars[z_level][tileIndices[z_level][i]]);
                    	} else {
                    		found = rugs[z_level][i]!=null && selectionRange.intersectsLine(rugs[z_level][i]);
                    	}
                    	
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

}
