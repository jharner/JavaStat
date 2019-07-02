/*
 * Histogram.java
 *
 * Created on March 15, 2002, 2:41 PM
 */

package wvustat.plot;

import wvustat.util.Bucket;
import wvustat.util.CoordConverter;
import wvustat.interfaces.Distribution;
import wvustat.util.MathUtils;
import wvustat.dist.Normal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.TreeSet;

/**
 *
 * @author  hxue
 * @version
 */
public class Histogram extends JComponent implements Drawable, ActionListener
{
    private double[] data;
    //private BaseAxisModel xtm;
    private CoordConverter coord;
    //private double xmin, xmax;
    //private double ticWidth;
    private int[] freqs;
    private Insets insets = new Insets(36, 20, 40, 20);
    private int width = 250, height = 160;
    private int maxFreq = 0;
    private int freqSum = 0;
    private int numOfIntervals;
    //private boolean isDiscrete = false;
    private Shape[] bars;
    private boolean[] barSelected;
    private Rectangle2D dragRange = new Rectangle2D.Float(0, 0, 0, 0);
    //private int ticSpace = 1;
    //private int nonEmptyCount=0;
    private double average;
    private ManualAxisModel xAxisModel = new ManualAxisModel();
    private Rectangle xAxisActiveRegion;
    
    private JPopupMenu popup;
    private boolean drawDensity = false;

    /** Creates new Histogram */
    public Histogram()
    {
        setBackground(Color.white);
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new DragSelectionHandler());
        
        popup = new JPopupMenu();
        JCheckBoxMenuItem densityMI = new JCheckBoxMenuItem("Normal Density", false);
        densityMI.addActionListener(this);
        popup.add(densityMI);
    }

    public void setData(double[] data)
    {
        this.data = data;
        //printArray(data);
        double ticWidth;
        int barNum = 8;
        if (data.length >= 100)
            barNum = 4 * (int) (Math.log(data.length) / Math.log(10));
        numOfIntervals = barNum;

        TreeSet ts = new TreeSet();
        double sum = 0;
        for (int i = 0; i < data.length; i++)
        {
            ts.add(new Double(data[i]));
            sum += data[i];
        }

        average = sum / data.length;
        double xmin = ((Double) ts.first()).doubleValue();
        double xmax = ((Double) ts.last()).doubleValue();

        if (ts.size() <= 50)
        {
            //isDiscrete = true;
            this.numOfIntervals = (int) Math.ceil(xmax) - (int) Math.floor(xmin) + 1;

            //freqs = new int[numOfIntervals];
            ticWidth = 1;
            //xmin = xmin - 0.5;
            //xmax=xmax;
        }
        else
        {
            double delta = (average - xmin) > (xmax - average) ? (average - xmin) : (xmax - average);
            ticWidth = delta * 2 / numOfIntervals;

            ticWidth = smoothOut(ticWidth);

            //xmin = xmin - ticWidth / 2;
            if (xmin > 0)
                xmin = xmin > ticWidth ? xmin : 0;
            else
                xmin = Math.ceil(xmin / ticWidth) * ticWidth;
            //xmax=xmax;

            numOfIntervals = (int) Math.ceil((xmax - xmin) / ticWidth);

            //freqs = new int[numOfIntervals];
        }


        if (xAxisModel.isManual() == false)
        {
            xAxisModel.setStartValue(xmin);
            xAxisModel.setEndValue(xmax);
            int ticSpace;
            if (numOfIntervals < 15)
                ticSpace = 0;
            else if (numOfIntervals >= 15 && numOfIntervals < 30)
                ticSpace = 1;
            else
                ticSpace = 4;
            xAxisModel.setNumOfMinorTicks(ticSpace);
            xAxisModel.setInterval(ticWidth * (ticSpace + 1));
        }
        generateFreqs(xAxisModel);
        repaint();
    }

    public void setAxisModel(ManualAxisModel axisModel)
    {
        this.xAxisModel = axisModel;
        generateFreqs(xAxisModel);
        repaint();
    }

    public ManualAxisModel getAxisModel()
    {
        return xAxisModel;
    }

    public Bucket[] getBuckets()
    {
        Bucket[] array = new Bucket[freqs.length];

        for (int i = 0; i < array.length; i++)
        {
            array[i] = new Bucket(xAxisModel.getStartValue() + i * xAxisModel.getMinorIncrement() - 0.5 * xAxisModel.getMinorIncrement(), xAxisModel.getMinorIncrement());
            array[i].setSize(freqs[i]);
        }

        return array;
    }

    public void setData(double[] data, double center, double ticWidthIn)
    {
        this.data = data;
        double xmin, xmax;

        double ticWidth = makeNicer(ticWidthIn);
        //isDiscrete = false;

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        //double sum=0;

        for (int i = 0; i < data.length; i++)
        {
            //sum+=data[i];
            if (data[i] > max)
                max = data[i];
            if (data[i] < min)
                min = data[i];
        }
        //double avg=sum/data.length;

        xmin = Math.floor(min);
        xmax = Math.ceil(max);
        if (xmin >= 0)
            xmin = xmin > ticWidth ? xmin : 0;

        double farEnd = (xmax - center) > (center - xmin) ? xmax : xmin;
        numOfIntervals = 2 * (int) Math.ceil(Math.abs(farEnd - center) / ticWidth);


        //freqs = new int[numOfIntervals];

        if (xAxisModel.isManual() == false)
        {
            xAxisModel.setStartValue(xmin);
            xAxisModel.setEndValue(xmax);

            int ticSpace;
            if (numOfIntervals < 15)
                ticSpace = 0;
            else if (numOfIntervals >= 15 && numOfIntervals < 30)
                ticSpace = 1;
            else
                ticSpace = 4;
            xAxisModel.setNumOfMinorTicks(ticSpace);
            xAxisModel.setInterval(ticWidth * (ticSpace + 1));
        }
        generateFreqs(xAxisModel);

        repaint();
    }

    private double makeNicer(double x)
    {
        if(Double.isInfinite(x))
            return 1;

        double factor = 1;
        while (x > 10)
        {
            x = x / 10;
            factor *= 10;
        }

        while (x < 1)
        {
            x = x * 10;
            factor /= 10;
        }

        double d1 = (int) x;
        double d2 = (int) x + 0.5;
        double d3 = Math.ceil(x);

        double y = x - d1 < Math.abs(d2 - x) ? d1 : d2;
        y = Math.abs(y - x) < d3 - x ? y : d3;
        y = ((int) (y * 10)) / 10.0;
        return y * factor;
    }

    private int getSelectedFreqSum()
    {
        int ret = 0;
        for (int i = 0; i < freqs.length; i++)
        {
            if (barSelected[i] == true)
                ret += freqs[i];
        }

        return ret;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }

    public void setBounds(int x, int y, int w, int h)
    {
        this.width = w;
        this.height = h;

        super.setBounds(x, y, w, h);
    }

    private void generateFreqs(ManualAxisModel axisModel)
    {
        if(data==null)
            return;

        double endValue = axisModel.getEndValue();
        double barWidth = axisModel.getMinorIncrement();
        double startValue = axisModel.getStartValue();
        double minValue = startValue - barWidth * 0.5;
        double maxValue = endValue + barWidth * 0.5;
        int numOfBars = (int) Math.ceil((maxValue - startValue) / barWidth);
        freqs = new int[numOfBars];

        for (int i = 0; i < data.length; i++)
        {
            int index = (int) ((data[i] - minValue) / barWidth);
            if (index == freqs.length)
                index = freqs.length - 1;

            if (index > freqs.length)
            {
                System.out.println(data[i]);
            }
            else if (index < 0)
            {
                System.out.println(data[i]);
            }
            else
                freqs[index]++;
        }

        maxFreq = 0;
        freqSum = 0;
        //nonEmptyCount=0;
        for (int i = 0; i < freqs.length; i++)
        {
            //if(freqs[i]>0)
            //nonEmptyCount++;

            if (freqs[i] > maxFreq)
                maxFreq = freqs[i];
        }

        for (int i = 0; i < freqs.length; i++) {
           	freqSum += freqs[i];
        }
        
        bars = new Shape[freqs.length];
        barSelected = new boolean[freqs.length];
    }

    public void clear()
    {
        freqs = null;
        repaint();
    }

    private double smoothOut(double x)
    {
        if (x <= 1)
            return x;
        else
            return Math.round(x);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g, width, height);
    }


    public void draw(Graphics g, int width, int height)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.black);

        if (freqs == null)
            return;
        double ticWidth = xAxisModel.getMinorIncrement();
        double xmin = xAxisModel.getStartValue() - ticWidth * 0.5;
        double xmax = xAxisModel.getEndValue() + ticWidth * 0.5;
        coord = new CoordConverter(width, height, xmin, xmax + xAxisModel.getMinorIncrement(), 0, maxFreq, insets);


        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        FontMetrics fm = g2.getFontMetrics();
        //Draw the bars and ticmarks

        double distance = ticWidth * (xAxisModel.getNumOfMinorTicks() + 1);
        double ticValue = xAxisModel.getStartValue();
        for (int i = 0; i <= freqs.length; i++)
        {
            int x = coord.x(xmin + i * ticWidth);

            if (i < freqs.length)
            {
                int y = coord.y(freqs[i]);
                int barW = coord.x(xmin + (i + 1) * ticWidth) - x;
                int barH = coord.y(0) - y;
                bars[i] = new Rectangle2D.Float(x, y, barW, barH);
                g2.setStroke(new BasicStroke(1.0f));
                nf.setMaximumFractionDigits(4);
                if (barSelected[i])
                {
                    g2.setColor(Color.black);
                    String percentStr = nf.format(1.0 * freqs[i] / data.length);
                    //System.out.println(1.0*freqs[i]/data.length);
                    g2.drawString(percentStr, x + barW / 2 - fm.stringWidth(percentStr) / 2, y - 2);
                    g2.setStroke(new BasicStroke(2.0f));
                }

                if (freqs[i] > 0)
                {
                    g2.drawRect(x, y, barW, barH);
                    g2.setColor(new Color(0x72, 0xa8, 0xd0));
                    g2.fillRect(x + 1, y + 1, barW - 1, barH - 1);
                }


            }

            nf.setMaximumFractionDigits(2);
            g2.setStroke(new BasicStroke(1.0f));
            g2.setColor(Color.black);

            x = coord.x(xmin + (i + 0.5) * ticWidth);

            if (x <= width - insets.right + 4)
            {


                if (i < freqs.length && i % (xAxisModel.getNumOfMinorTicks() + 1) == 0)
                {
                    g2.drawLine(x, coord.y(0), x, coord.y(0) + 7);
                    String label = nf.format(ticValue);
                    g2.drawString(label, x - fm.stringWidth(label) / 2, coord.y(0) + 7 + fm.getAscent());
                    ticValue += distance;
                }
                else
                    g2.drawLine(x, coord.y(0), x, coord.y(0) + 4);
            }
        }

        //Draw normal curve
        if (drawDensity) {
	        double mean = MathUtils.getMean(data);
			double dev = MathUtils.getStdDev(data);
			
			Distribution normal = new Normal(mean, dev);
			double delta = (xmax - xmin) / 100;
			double[][] normalDensityArray = new double[2][101];
	        for (int i = 0; i < normalDensityArray[0].length; i++) {
	            normalDensityArray[0][i] = xmin + delta * i;
	            normalDensityArray[1][i] = normal.pdf(normalDensityArray[0][i]) * freqSum * ticWidth;
	        }
	        g2.setColor(Color.black);
            for (int i = 0; i < normalDensityArray[0].length - 1; i++)
            	g2.drawLine(coord.x(normalDensityArray[0][i]), coord.y(normalDensityArray[1][i]), coord.x(normalDensityArray[0][i + 1]), coord.y(normalDensityArray[1][i + 1]));
        }
        
        
        //draw x axis
        g2.drawLine(insets.left - 4, height - insets.bottom, width - insets.right + 4, height - insets.bottom);
        xAxisActiveRegion = new Rectangle(insets.left - 4, height - insets.bottom - 4, width - insets.right + 4, height - insets.bottom + 4);

        g2.setColor(Color.lightGray);
        g2.draw(dragRange);

        g2.setColor(Color.black);
        nf.setMaximumFractionDigits(4);
        g2.drawString("P(Selected): " + nf.format(1.0 * getSelectedFreqSum() / data.length), 4, 18);

    }

    public void configureAxis()
    {
        AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
        configPanel.setAxisModel(xAxisModel);
        int option = JOptionPane.showOptionDialog(Histogram.this, configPanel,
                "x-axis settings",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (option == JOptionPane.OK_OPTION)
        {
            ManualAxisModel model = (ManualAxisModel)configPanel.getAxisModel();
            if (model != null)
            {
                model.setManual(true);
                setAxisModel(model);
                repaint();
            }
            else
                JOptionPane.showMessageDialog(Histogram.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
    	String arg = ae.getActionCommand();
    	if (arg.equals("Normal Density")) {
    		this.drawDensity = !this.drawDensity;
    		repaint();
    	}
    }

    class MouseHandler extends MouseAdapter
    {
        Point startPoint, endPoint;

        public void mousePressed(MouseEvent evt)
        {
            startPoint = evt.getPoint();
            dragRange.setRect(evt.getX(), evt.getY(), 0, 0);
            for (int i = 0; i < bars.length; i++)
            {
                if (!bars[i].contains(evt.getPoint()))
                {
                    if (!evt.isShiftDown())
                        barSelected[i] = false;
                }
                else
                    barSelected[i] = true;
            }

            repaint();
        }

        public void mouseReleased(MouseEvent evt)
        {
            endPoint = evt.getPoint();
            double w = endPoint.getX() - startPoint.getX();
            double h = endPoint.getY() - startPoint.getY();
            dragRange.setRect(startPoint.getX(), startPoint.getY(), w, h);

            for (int i = 0; i < bars.length; i++)
            {
                if (bars[i].intersects(dragRange))
                {
                    barSelected[i] = true;
                }
            }

            dragRange.setRect(0, 0, 0, 0);
            repaint();
        }

        public void mouseClicked(MouseEvent evt)
        {
            if (evt.getClickCount() == 2 && xAxisActiveRegion.contains(evt.getPoint()))
            {
                configureAxis();
            }
            else if (SwingUtilities.isRightMouseButton(evt) || evt.isControlDown()) {
            	popup.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }

    class DragSelectionHandler extends MouseMotionAdapter
    {
        public void mouseDragged(MouseEvent evt)
        {
            double x = dragRange.getMinX();
            double y = dragRange.getMinY();
            double w = evt.getX() - x;
            double h = evt.getY() - y;
            dragRange.setRect(x, y, w, h);
            repaint();
        }
    }


    public static void main(String[] args)
    {
        double[] data = new double[]{1.1, 0.75, 0.987, 2.45, 2.78, 3.55, 2.2, 2.8, 4.2};
        //double[] data2=new double[]{1,2,3,4,5,6};

        Histogram histogram = new Histogram();
        histogram.setData(data, 1, 1);

        JFrame jf = new JFrame("Histogram");
        jf.getContentPane().add(histogram, BorderLayout.CENTER);

        jf.addWindowListener(
                new java.awt.event.WindowAdapter()
                {
                    public void windowClosing(java.awt.event.WindowEvent evt)
                    {
                        System.exit(0);
                    }
                }
        );

        jf.pack();
        jf.show();
    }

}