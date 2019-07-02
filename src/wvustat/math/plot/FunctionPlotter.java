/*
 * FunctionPlotter.java
 *
 * Created on November 2, 2001, 10:44 AM
 */

package wvustat.math.plot;

import wvustat.math.expression.*;
import wvustat.swing.table.DataSet;
import wvustat.math.UI.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.text.DecimalFormat;

/**
 *
 *
 *
 * @author  Hengyi Xue
 *
 * @version
 *
 */

public class FunctionPlotter extends JComponent implements ChangeListener, TableModelListener, MouseListener, KeyListener, MouseMotionListener
{
    private Color bgrColor = Color.white, fgrColor = Color.black;
    private double initXMax = 8, initYMax = 8;
    private double xMax = 8, yMax = 8, xMin, yMin;  //Plotting range
    private int width = 536, height = 554;
    private Insets insets = new Insets(40, 40, 50, 40);
    private CoordConverter coord;
    private int numOfSegments = 1000;  //The curve for the function is divided into this many segments
    private int xOrigin, yOrigin; //The point where x, y axises intersect.
    private int numOfGridlines = 4; //This is only the number at one side of the axis
    private double ratioX, ratioY;
    private int gridWidth, gridHeight;
    private boolean isMovingOrigin = false;
    private float zoomRatio = 2.0f;
    private double xInterval, yInterval;
    private Polygon[] arrows = new Polygon[4];
    private AxisMover aMover;
    private double mouseX = 0, mouseY = 0; //Current coordinates of the mouse cursor
    private int init_x, init_y, last_x, last_y; //This four values keeps track of mouse movement
    private java.util.List functions;
    private java.util.List models;
    private boolean zoomModeOn = false;

    //my private var for enablePointClickZoom
    private boolean enablePointClick = false;
    private double tracerX = Double.NaN, tracerY = Double.NaN;
    private boolean isTracing = false;
    private ExpressionObject traceFunc; //The function that is being "traced", that is to say that it has a slider on its curve
    private double minYHat, maxYHat; //These two values keep track of the min and max of values on function curves.
    private DataSet dataSet;
    private PlotOptions plotOptions;
    private Stroke thinLine = new BasicStroke(1.0f);
    private Stroke thickLine = new BasicStroke(2.0f);
    private Image bufferImage;
    private Vector changeListeners;
    private int pointsAdded = 0;
    private ListSelectionModel pointsSelectionModel = new DefaultListSelectionModel();

    private double interval;
    private int tmpIndex = 0;
    private int itWorks = 0;

    private String xTic = "";
    private String yTic = "";
    private String xTicTemp = "";
    private String yTicTemp = "";

    /** Creates new FunctionPlotter */
    public FunctionPlotter()
    {
        //enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

        addMouseMotionListener(this);
        addKeyListener(this);
        addMouseListener(this);
        functions = new ArrayList();

        models = new ArrayList();

        preparePlot();

        pointsSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public void setPlotOptions(PlotOptions options)
    {
        this.plotOptions = options;
        repaint();
    }

    public void addChangeListener(ChangeListener listener)
    {
        if (changeListeners == null)
            changeListeners = new Vector();

        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener)
    {
        changeListeners.remove(listener);
    }

    protected void fireChangeEvent()
    {
        if (changeListeners == null)
            return;

        ChangeEvent evt = new ChangeEvent(this);
        for (int i = 0; i < changeListeners.size(); i++)
        {
            ((ChangeListener) changeListeners.get(i)).stateChanged(evt);
        }
    }

    public void addFunction(ExpressionObject func)
    {
        functions.add(func);
        func.addChangeListener(this);

        if (this.isShowing())
            update(getGraphics());
    }

    public void addModel(ExpressionObject object)
    {
        models.add(object);
        object.addChangeListener(this);

        if (this.isShowing())
            update(getGraphics());
    }

    public java.util.List getFunctions()
    {
        return functions;
    }

    public java.util.List getModels()
    {
        return models;
    }

    public void setDataSet(DataSet dataSet)
    {
        this.dataSet = dataSet;
        dataSet.addTableModelListener(this);

        if (this.isShowing())
            update(getGraphics());
    }

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public void enableZoom(boolean value)
    {
        this.zoomModeOn = value;
        isTracing = false;

        if (zoomModeOn)
        {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

            init_x = 0;
            init_y = 0;
            last_x = 0;
            last_y = 0;
        }
        else
        {
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    public void enablePointClickZoom(boolean value) {
        this.enablePointClick = value;
    }

    public void clearFunctions()
    {
        isTracing = false;
        functions.clear();
        tracerX = Double.NaN;
        tracerY = Double.NaN;
        update(getGraphics());
    }

    public void clearModels()
    {
        isTracing = false;
        models.clear();
        tracerX = Double.NaN;
        tracerY = Double.NaN;
        update(getGraphics());
    }

    public void setZoomRatio(float ratio)
    {
        this.zoomRatio = ratio;
    }

    public void setSize(int w, int h)
    {
        this.width = w;
        this.height = h;
        preparePlot();
        super.setSize(w, h);
    }

    public Dimension getSize()
    {
        return new Dimension(width, height);
    }

    public Dimension getPreferredSize()
    {
        return getSize();
    }

    public Dimension getMinimumSize()
    {
        int w = width - insets.left - insets.right;
        int h = height - insets.top - insets.bottom;

        return new Dimension(w, h);
    }

    /*
    public void setBounds(int x, int y, int w, int h)
    {
        //this.width=w<h-10?w:h-10;
        this.width = w;
        //this.height=width+10;
        this.height = h;
        preparePlot();
        super.setBounds(x, y, w, h);
    } */

    public void resetAxises()
    {
        xMax = initXMax;
        yMax = initYMax;
        isTracing = false;
        preparePlot();
        update(getGraphics());
    }

    private void preparePlot()
    {
        xOrigin = (width - insets.left - insets.right) / 2 + insets.left;
        yOrigin = (height - insets.top - insets.bottom) / 2 + insets.top;
        gridWidth = (int) Math.round((width - insets.right - xOrigin) * 1.0 / numOfGridlines);
        gridHeight = (int) Math.round((yOrigin - insets.top) * 1.0 / numOfGridlines);
        double xMin0, xMax0, yMin0, yMax0;

        if (dataSet != null && dataSet.getSize() > 0)
        {
            xMin0 = dataSet.getMinimumX();
            xMax0 = dataSet.getMaximumX();
            yMin0 = dataSet.getMinimumY();
            yMax0 = dataSet.getMaximumY();
            xMax0 = Math.abs(xMax0) > Math.abs(xMin0) ? Math.abs(xMax0) : Math.abs(xMin0);
            yMax0 = Math.abs(yMax0) > Math.abs(yMin0) ? Math.abs(yMax0) : Math.abs(yMin0);
            xMax0 = xMax0 > yMax0 ? xMax0 : yMax0;
            xMax0 = Math.ceil(xMax0 / Math.pow(10, sigDigits(xMax0) - 1)) * Math.pow(10, sigDigits(xMax0) - 1);

            if (xMax0 < 10)
                xMax0 = 10;

            ratioX = xMax0 / (width - insets.right - xOrigin);
            ratioY = xMax0 / (yOrigin - insets.top);
            xInterval = xMax0 / numOfGridlines;
            yInterval = xMax0 / numOfGridlines;
        }
        else
        {
            ratioX = xMax / (width - insets.right - xOrigin);
            ratioY = yMax / (yOrigin - insets.top);
            //ratioX=ratioY;

            xInterval = xMax / numOfGridlines;
            yInterval = yMax / numOfGridlines;
        }

        coord = new CoordConverter(xOrigin, yOrigin, ratioX, ratioY);
    }

    // Return the significant digits in d
    private int sigDigits(double d)
    {
        //if d is a number like 10 or 100, we have to deal with it separately
        if (d > 1 && Math.abs(Math.log(d) / Math.log(10) - (int) (Math.log(d) / Math.log(10))) < 0.01)
            return (int) (Math.log(d) / Math.log(10)) + 1;
        else
            return (int) Math.abs(Math.ceil(Math.log(d) / Math.log(10)));
    }

    public void zoomOut()
    {
        ratioX = ratioX * zoomRatio;
        ratioY = ratioY * zoomRatio;
        xInterval *= zoomRatio;
        yInterval *= zoomRatio;
        coord.setXRatio(ratioX);
        coord.setYRatio(ratioY);
        update(getGraphics());
    }

    public void zoomIn()
    {
        ratioX = ratioX / zoomRatio;
        ratioY = ratioY / zoomRatio;
        xInterval = xInterval / zoomRatio;
        yInterval = yInterval / zoomRatio;
        coord.setXRatio(ratioX);
        coord.setYRatio(ratioY);
        isTracing = false;
        update(getGraphics());
    }

    public void zoomOutX()
    {
        ratioX = ratioX * zoomRatio;
        xInterval *= zoomRatio;
        coord.setXRatio(ratioX);
        isTracing = false;
        update(getGraphics());
    }

    public void zoomInX()
    {
        ratioX = ratioX / zoomRatio;
        xInterval /= zoomRatio;
        coord.setXRatio(ratioX);
        isTracing = false;
        update(getGraphics());
    }

    public void zoomOutY()
    {
        ratioY = ratioY * zoomRatio;
        yInterval *= zoomRatio;
        coord.setYRatio(ratioY);
        isTracing = false;
        update(getGraphics());
    }

    public void zoomInY()
    {
        ratioY = ratioY / zoomRatio;
        yInterval /= zoomRatio;
        coord.setYRatio(ratioY);
        isTracing = false;
        update(getGraphics());
    }

    public void moveOriginTo(int x, int y)
    {
        xOrigin = x;
        yOrigin = y;
        coord = new CoordConverter(xOrigin, yOrigin, ratioX, ratioY);
        update(getGraphics());
    }

    public void paintComponent(Graphics g0)
    {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(0xed, 0xf3, 0xfe));
        g.fillRect(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom);
        g.setColor(fgrColor);

        xOrigin = coord.xcoord(0);
        yOrigin = coord.ycoord(0);

        //Draw grid lines x and y axis tick marks

        g.setColor(Color.lightGray);
        g.setFont(new Font("Arial", Font.PLAIN, 11));

        FontMetrics metrics = g.getFontMetrics();
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        //nf.setMaximumFractionDigits(2);

        double tmpX = coord.getRealX(insets.left);
        double tmpY = coord.getRealY(insets.top);

        //Draw gridlines
        //Draw vertical gridlines to the right of y axis

        int gridX = insets.left;
        int gridY = insets.top;

        while (gridX <= width - insets.right)
        {
            DecimalFormat xTicTempFormatter = new DecimalFormat("#.###E0");
            xTicTemp = xTicTempFormatter.format(tmpX);

            int tempHold = xTicTemp.indexOf('E');
            String tempHold2 = xTicTemp.substring(tempHold +1, xTicTemp.length());
            int iConvert = Integer.parseInt(tempHold2);

            //    System.out.println(iConvert);
            if (iConvert <= 4 && iConvert >= -3)
                xTic = nf.format(tmpX);
            else {
                xTic = xTicTemp;
            }
            //  String xTic = nf.format(tmpX);

            g.drawLine(gridX, insets.top, gridX, height - insets.bottom);

            g.setColor(Color.gray);

            if (yOrigin < insets.top + metrics.getHeight())
                g.drawString(xTic, gridX - metrics.stringWidth(xTic) / 2, insets.top + metrics.getHeight());
            else if (yOrigin > height - insets.bottom - metrics.getHeight())
                g.drawString(xTic, gridX - metrics.stringWidth(xTic) / 2, height - insets.bottom);
            else
                g.drawString(xTic, gridX - metrics.stringWidth(xTic) / 2, yOrigin + metrics.getHeight());

            g.setColor(Color.lightGray);

            tmpX += xInterval;
            gridX += gridWidth;
        }

        //Draw horizontal gridlines above x axis
        while (gridY <= height - insets.bottom)
        {
            //   String yTic = nf.format(tmpY);

            DecimalFormat yTicTempFormatter = new DecimalFormat("#.###E0");
            yTicTemp = yTicTempFormatter.format(tmpY);

            int tempHoldyE = yTicTemp.indexOf('E');
            String tempHold3 = yTicTemp.substring(tempHoldyE +1, yTicTemp.length());
            int iConvert = Integer.parseInt(tempHold3);

            if (iConvert <= 4 && iConvert >= -3)
                yTic = nf.format(tmpY);
            else {
                yTic = yTicTemp;
            }

            g.drawLine(insets.left, gridY, width - insets.right, gridY);

            g.setColor(Color.gray);

            if (xOrigin < insets.left + gridWidth)
                g.drawString(yTic, insets.left + 2, gridY + metrics.getAscent() / 2);
            else if (xOrigin > width - insets.right - gridWidth)
                g.drawString(yTic, width - insets.right - 2 - metrics.stringWidth(yTic), gridY + metrics.getAscent() / 2);
            else
                g.drawString(yTic, xOrigin - 2 - metrics.stringWidth(yTic), gridY + metrics.getAscent() / 2);

            g.setColor(Color.lightGray);

            tmpY -= yInterval;
            gridY += gridHeight;
        }

        g.setColor(fgrColor);

        //Draw x axis
        g.drawLine(insets.left, yOrigin, width - insets.right, yOrigin);

        //Draw y axis
        g.drawLine(xOrigin, insets.top, xOrigin, height - insets.bottom);

        if (functions.size() >= 1)
        drawCurves(g, functions, plotOptions.isShowFunctions());

        if (models.size() >=1 )
        drawCurves(g, models, plotOptions.isShowModels());

        g.setStroke(thinLine);

        //Draw data points
        if (dataSet != null && plotOptions.showData)
        {

            g.setColor(Color.black);

            double[][] points = dataSet.getPlotData();

            List colors = dataSet.getColors();

            if (points != null)
            {
                for (int i = 0; i < points[0].length; i++)
                {
                    if (!Double.isNaN(points[0][i]) && !Double.isNaN(points[1][i]))
                    {
                        g.setColor((Color) colors.get(i));

                        if(pointsSelectionModel.isSelectedIndex(i))
                            g.fillOval(coord.xcoord(points[0][i]) - 2, coord.ycoord(points[1][i]) - 2, 4, 4);
                        else
                            g.drawOval(coord.xcoord(points[0][i]) - 2, coord.ycoord(points[1][i]) - 2, 4, 4);
                    }
                }
            }
        }

        //Erase anything that goes outside of the drawing area
        g.setColor(bgrColor);

        g.fillRect(0, 0, width, insets.top - 6);
        g.fillRect(0, 0, insets.left - 4, height);
        g.fillRect(width - insets.right + 4, 0, insets.right - 4, height);
        g.fillRect(0, height - insets.bottom + 6, width, insets.bottom - 4);

        //Draw x and y axis label
        Font font = new Font("Dialog", Font.PLAIN, 14);

        g.setFont(font);
        g.setColor(Color.black);

        metrics = this.getFontMetrics(font);
        g.drawString("X", width - insets.right + 2, yOrigin + metrics.getHeight() / 2);
        g.drawString("Y", xOrigin + 4, insets.top + metrics.getAscent());

        //Draw arrows
        int arrowW = 12,arrowH = 18;
        int vgap = 10,hgap = 10;
        int xCenter = (int) ((width - insets.left - insets.right) / 2.0) + insets.left;
        int yCenter = (int) ((height - insets.top - insets.bottom) / 2.0) + insets.top;

        int[] xPts = new int[]{xCenter - arrowW / 2, xCenter + arrowW / 2, xCenter};
        int[] yPts = new int[]{insets.top - vgap, insets.top - vgap, insets.top - vgap - arrowH};

        arrows[0] = new Polygon(xPts, yPts, 3);

        xPts = new int[]{insets.left - hgap, insets.left - hgap, insets.left - hgap - arrowH};
        yPts = new int[]{yCenter + arrowW / 2, yCenter - arrowW / 2, yCenter};

        arrows[1] = new Polygon(xPts, yPts, 3);

        xPts = new int[]{xCenter - arrowW / 2, xCenter + arrowW / 2, xCenter};
        yPts = new int[]{vgap, vgap, vgap + arrowH};

        arrows[2] = new Polygon(xPts, yPts, 3);
        arrows[2].translate(0, height - insets.bottom);

        xPts = new int[]{hgap, hgap, hgap + arrowH};
        yPts = new int[]{yCenter + arrowW / 2, yCenter - arrowW / 2, yCenter};

        arrows[3] = new Polygon(xPts, yPts, 3);
        arrows[3].translate(width - insets.right, 0);

        g.setColor(Color.blue);

        for (int i = 0; i < arrows.length; i++)
        {
            g.fillPolygon(arrows[i]);
        }

        //Display current coordinates

        g.setColor(new Color(0x17, 0x55, 0xb1));

        //I changed the coordStr to display the x,y values when tracing instead
        //of displaying the x,y values of where mouse is located.
        //String coordStr = "(" + nf.format(mouseX) + ", " + nf.format(mouseY) + ")";
        String coordStr = "X-Value = " + nf.format(tracerX) + ", Y-Value = " + nf.format(tracerY);
        g.drawString(coordStr, insets.left, height - metrics.getMaxDescent() - 2);

        //Draw border

        g.setColor(fgrColor);

        g.drawRect(0, 0, width - 1, height - 1);

        //Draw a rectangle to indicate the region the mouse was dragged over

        g.setColor(Color.black);

        g.drawRect(init_x, init_y, last_x - init_x, last_y - init_y);

        //Draw a small rectangle to indicate the slider

        if (plotOptions.isShowFunctions() && traceFunc != null && !Double.isNaN(tracerX) && !Double.isNaN(tracerY)) {
            try {
                traceFunc.setXValue(tracerX);
                double expY = traceFunc.value();
                if (Math.abs(expY - tracerY) > 0.1)
                    return;
            }
            catch (Exception e) {
                return;
            }
            g.setColor(new Color(0xbf, 0x0a, 0x00));
            int tX = coord.xcoord(tracerX);
            int tY = coord.ycoord(tracerY);

            //this BIG if statement keeps the trace slider from showing after
            //moving off the graph

            if (tX > insets.left && tX < width - insets.right && tY > insets.top && tY < height - insets.bottom) {
                g.fill3DRect(tX - 2, tY - 2, 4, 4, true);

                //i changed the coordinates here to keep the line from drawing
                //outside the grid.

                float checkSign = tX - insets.left;
                float checkSign2 = tX - xOrigin;
                float checkSign2a = xOrigin - tX;
                float checkSign3a = tY - yOrigin;
                float checkSign3 = yOrigin -tY;
                float checkSign4 = (height-insets.top)-tY;
                float checkSign5 = (width-insets.right) - tX;
                float checkSign6 = tY - insets.bottom;

                //  if (tX > insets.left && tX < width - insets.right && tY > insets.top && tY < height - insets.bottom)

                if (checkSign2 > 0) {
                    if (checkSign >= checkSign2) {
                        g.drawLine(tX, tY, xOrigin, tY);
                    }
                    else {
                        g.drawLine(tX, tY, insets.left, tY);
                    }

                }
                else {
                    if (checkSign5 >= checkSign2a) {
                        g.drawLine(tX, tY, xOrigin, tY);
                    }
                    else {
                        g.drawLine(tX, tY, width-insets.right, tY);
                    }
                }

                ////end of x-axis and its working in all quadrants

                if (checkSign3 > 0) {
                    if (checkSign4 >= checkSign3) {
                        g.drawLine(tX, tY, tX, yOrigin);
                    }
                    else {
                        g.drawLine(tX, tY, tX, height-insets.bottom);
                    }

                }
                else {
                    if (checkSign6 >= checkSign3a) {
                        g.drawLine(tX, tY, tX, yOrigin);
                    }
                    else {
                        g.drawLine(tX, tY, tX, insets.top);
                    }
                }

            }
          /*  g.setFont(new Font("Helvetica", Font.PLAIN, 11));
            g.setColor(Color.black);
            NumberFormat nf2 = NumberFormat.getInstance();

            g.drawString(nf2.format(tracerX), tX + 4, yOrigin - 2);
            g.drawString(nf2.format(tracerY), xOrigin + 4, tY - 2); */
        }

    }

    //Test if val is between d1 and d2
    private boolean isBetween(double val, double d1, double d2)
    {
        double smaller = (d1 < d2) ? d1 : d2;

        double bigger = (d1 > d2) ? d1 : d2;

        return (val >= smaller) && (val <= bigger);
    }

    public void update(Graphics g)
    {
        Graphics gr;

        if (bufferImage == null || bufferImage.getWidth(this) != this.getSize().width || bufferImage.getHeight(this) != this.getSize().height)

            bufferImage = this.createImage(getSize().width, getSize().height);

        gr = bufferImage.getGraphics();

        paint(gr);

        g.drawImage(bufferImage, 0, 0, this);
    }

    /**
     *
     * Automatically scale the axis so that a function that is off-center becomes visible
     *
     */
    public void autoScale()
    {
        if (functions.size() == 1)
        {
            ExpressionObject function=(ExpressionObject)functions.get(0);

            double ratioY = (Math.ceil(maxYHat) - Math.floor(minYHat)) / (height - insets.top - insets.bottom);

            yInterval = (Math.ceil(maxYHat) - Math.floor(minYHat)) / (2 * numOfGridlines);

            /*
            double tmpD=Math.abs(maxYHat)>Math.abs(minYHat)?Math.abs(maxYHat):Math.abs(minYHat);
            tmpD=Math.ceil(tmpD);
            double ratioY=2*tmpD/(height-insets.top-insets.bottom);
            yInterval=tmpD/numOfGridlines;
             */

            coord.setYRatio(ratioY);

            coord.moveOriginTo((xMax + xMin) / 2, (Math.ceil(maxYHat) + Math.floor(minYHat)) / 2);
            //isTracing=false;

            update(getGraphics());
        }
    }

    protected void processMouseEvent(MouseEvent evt)
    {
        Point pt = evt.getPoint();

        switch (evt.getID())
        {
            case MouseEvent.MOUSE_PRESSED:

                init_x = pt.x;
                init_y = pt.y;
                requestFocus();

                if (zoomModeOn)
                    return;

                /*
                if(Math.abs(pt.x-xOrigin)<=3 && Math.abs(pt.y-yOrigin)<=3){
                    isMovingOrigin=true;
                }
                 */

                if (pt.x > insets.left && pt.x < width - insets.right && pt.y > insets.top && pt.y < height - insets.bottom)
                {
                    if (plotOptions.addingPoint)
                    {
                        if (pointsAdded < plotOptions.pointsToAdd)
                        {
                            double tmpX = coord.getRealX(pt.x);
                            double tmpY = coord.getRealY(pt.y);

                            dataSet.addPoint(tmpX, tmpY);

                            pointsAdded++;
                        }
                        else
                        {
                            plotOptions.setAddingPoint(false);
                            plotOptions.setPointsToAdd(0);
                            pointsAdded = 0;
                            repaint();
                        }
                    }
                    else if (functions.size() + models.size() > 0)
                    {
                        java.util.List combinedList = new ArrayList(functions);
                        combinedList.addAll(models);

                        double[] yhat = new double[combinedList.size()];

                        tracerX = coord.getRealX(pt.x);
                        tracerY = coord.getRealY(pt.y);

                        for (int i = 0; i < combinedList.size(); i++)
                        {

                            ExpressionObject fun = (ExpressionObject) combinedList.get(i);
                            fun.setXValue(tracerX);

                            try
                            {
                                yhat[i] = fun.value();
                            }

                            catch (ExecError e)
                            {
                                yhat[i] = Double.MAX_VALUE;
                            }
                        }

                        double tmpMin = Double.MAX_VALUE;

                       //this needs to be deleted because I am defining it
                       //as an instance variable and not locally.
                       // int tmpIndex = -1;

                        for (int i = 0; i < yhat.length; i++)
                        {
                            if (Math.abs(yhat[i] - tracerY) < tmpMin)
                            {
                                tmpMin = Math.abs(yhat[i] - tracerY);
                                tmpIndex = i;
                            }
                        }

                        if (Math.abs(coord.ycoord(yhat[tmpIndex]) - pt.y) < 5)
                        {
                            this.isTracing = true;
                            tracerY = yhat[tmpIndex];
                            traceFunc = (ExpressionObject) combinedList.get(tmpIndex);
                            update(getGraphics());
                            fireChangeEvent();
                        }
                        else
                        {
                            tracerX = Double.NaN;
                            tracerY = Double.NaN;
                            traceFunc = null;
                            fireChangeEvent();
                        }
                    }
                }
                else
                {
                    int index = 0;
                    while (index < arrows.length && !arrows[index].contains(pt)) index++;

                    if (index == 0)
                    {
                        aMover = new AxisMover(AxisMover.UP);
                    }
                    else if (index == 1)
                    {
                        aMover = new AxisMover(AxisMover.LEFT);
                    }
                    else if (index == 2)
                    {
                        aMover = new AxisMover(AxisMover.DOWN);
                    }
                    else if (index == 3)
                    {
                        aMover = new AxisMover(AxisMover.RIGHT);
                    }
                    if (aMover != null)
                    {
                        Thread animatorThread = new Thread(aMover, "Animator Thread");
                        animatorThread.start();
                    }
                }

                break;

            case MouseEvent.MOUSE_RELEASED:

                isMovingOrigin = false;

                if (aMover != null)
                    aMover.stopMoving();

                if (zoomModeOn)
                {
                    double tmpX0 = coord.getRealX(init_x);
                    double tmpY0 = coord.getRealY(init_y);
                    double tmpX1 = coord.getRealX(last_x);
                    double tmpY1 = coord.getRealY(last_y);

                    ratioX = (tmpX1 - tmpX0) / (width - insets.left - insets.right);
                    ratioY = (tmpY0 - tmpY1) / (height - insets.top - insets.bottom);

                    this.xInterval = (tmpX1 - tmpX0) / (2 * numOfGridlines);
                    this.yInterval = (tmpY0 - tmpY1) / (2 * numOfGridlines);

                    coord.setXRatio(ratioX);
                    coord.setYRatio(ratioY);
                    coord.moveOriginTo((tmpX0 + tmpX1) / 2, (tmpY0 + tmpY1) / 2);

                    isTracing = false;
                }

                double[][] points = dataSet.getPlotData();

                if (points != null)
                {

                    pointsSelectionModel.clearSelection();

                    for (int i = 0; i < points[0].length; i++)
                    {
                        if (inRange(coord.xcoord(points[0][i]), init_x, last_x) && inRange(coord.ycoord(points[1][i]), init_y, last_y))
                        {
                            //dataSet.setColor(Color.blue, i);
                            pointsSelectionModel.addSelectionInterval(i, i);
                        }
                    }
                }

                init_x = 0;
                init_y = 0;
                last_x = init_x;
                last_y = init_y;

                update(getGraphics());

                //the code below sets zoomModeOn to false and then
                // uses a boolean value gotit inside a while to propagate
                //up the hiearachy in order to find toggle parent which is
                //grapher. solution by Tan.
                zoomModeOn = false;
                boolean gotit = false;
                Container grapher = getParent();
                while(!gotit){
                    if(grapher instanceof Grapher){
                        ((Grapher)grapher).toggle.releaseButton();
                        gotit = true;
                        this.setCursor(Cursor.getDefaultCursor());
                    }
                    else
                        grapher = grapher.getParent();
                }
                //when copying my code to here..this parentheses below caused
                //a problem. I need to make sure its not needed and that the
                //begin/end brackets all match properly with the if/else stmts.

                //   }

                isTracing = false;

                break;
        }

        super.processMouseEvent(evt);
    }

    //this is code to allow for tracing to be done via arrow buttons
    //using keylistener.
    public void keyReleased(KeyEvent ke) {}

    public void keyTyped(KeyEvent ke) {}

    public void keyPressed(KeyEvent ke) {

        int key = ke.getKeyCode();
        //  MathFunction func2 = (MathFunction) functions.elementAt(tmpIndex);
        //  ExpressionObject func2 = (ExpressionObject) functions.elementAt(tmpIndex);
        ExpressionObject func2 = (ExpressionObject) functions.get(tmpIndex);
        switch(key) {
            case KeyEvent.VK_RIGHT:
                try {
                    tracerX= tracerX + interval*.1;
                    func2.setXValue(tracerX);
                    tracerY = func2.value();
                }
                catch (ExecError e) {}
                update(getGraphics());
                break;
            case KeyEvent.VK_LEFT:
                try {
                    tracerX= tracerX - interval*.1;
                    func2.setXValue(tracerX);
                    tracerY = func2.value();
                }
                catch (ExecError e) {}
                update(getGraphics());
                break;
        }

    }

    protected void processMouseMotionEvent(MouseEvent evt)
    {

        Point pt = evt.getPoint();

        switch (evt.getID())
        {
            case MouseEvent.MOUSE_DRAGGED:

                if (pt.x > insets.left && pt.x < width - insets.right && pt.y > insets.top && pt.y < height - insets.bottom)
                {
                    last_x = pt.x;
                    last_y = pt.y;

                    if (this.isMovingOrigin)
                        this.moveOriginTo(pt.x, pt.y);

                    else if (this.isTracing && traceFunc!=null)
                    {

                        tracerX = coord.getRealX(pt.x);
                        traceFunc.setXValue(tracerX);

                        try
                        {
                            tracerY = traceFunc.value();
                            //update(getGraphics());
                        }
                        catch (Exception e)
                        {
                        }
                    }

                    update(getGraphics());
                }

                break;

            case MouseEvent.MOUSE_MOVED:

                if (pt.x < width - insets.right && pt.x > insets.left && pt.y < height - insets.bottom && pt.y > insets.top)
                {
                    mouseX = coord.getRealX(pt.x);
                    mouseY = coord.getRealY(pt.y);
                    update(getGraphics());
                }

                break;
        }

        super.processMouseMotionEvent(evt);
    }

    public void stateChanged(ChangeEvent evt)
    {
        update(getGraphics());
    }

    public ExpressionObject getSelectedExpression()
    {
        return traceFunc;
    }

    public void deleteFunction(ExpressionObject function)
    {
        functions.remove(function);

        if (function == traceFunc)
            traceFunc = null;

        update(getGraphics());

        fireChangeEvent();
    }

    public void deleteModel(ExpressionObject model)
    {
        models.remove(model);

        if (model == traceFunc)
            traceFunc = null;

        update(getGraphics());
        fireChangeEvent();
    }

    public ListSelectionModel getPointsSelectionModel()
    {
        return pointsSelectionModel;
    }

    class AxisMover implements Runnable
    {

        public static final int UP = 0;
        public static final int LEFT = 1;
        public static final int DOWN = 2;
        public static final int RIGHT = 3;

        private int idleTime = 200;

        private int direction;

        private Boolean flag = Boolean.TRUE;

        AxisMover(int direction)
        {
            this.direction = direction;
        }

        public synchronized void stopMoving()
        {
            flag = Boolean.FALSE;
        }

        public void run()
        {
            synchronized (flag)
            {
                while (flag.booleanValue())
                {
                    switch (direction)
                    {
                        case UP:
                            coord.moveOrigin(0, yInterval);
                            break;

                        case LEFT:
                            coord.moveOrigin(-xInterval, 0);
                            break;

                        case DOWN:
                            coord.moveOrigin(0, -yInterval);
                            break;

                        case RIGHT:
                            coord.moveOrigin(xInterval, 0);
                            break;
                    }

                    update(getGraphics());

                    try
                    {
                        Thread.currentThread().sleep(idleTime);
                    }

                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }
    }

    private boolean inRange(double value, double min, double max)
    {
        return value >= min && value <= max;
    }

    private void drawCurves(Graphics2D g, java.util.List expressionList, boolean isShowing)
    {
        //Draw the curve

        if (isShowing)
        {
            minYHat = Double.MAX_VALUE;
            maxYHat = Double.MIN_VALUE;

            for (int j = 0; j < expressionList.size(); j++)
            {
                ExpressionObject function = (ExpressionObject) expressionList.get(j);
                if (function.isVisible())
                {
                    if (function == traceFunc)
                        g.setStroke(thickLine);
                    else
                        g.setStroke(thinLine);

                    //Hashtable varsTable=function.getVariables();
                    xMin = coord.getRealX(insets.left);
                    yMin = coord.getRealY(height - insets.bottom);
                    xMax = coord.getRealX(width - insets.right);
                    yMax = coord.getRealY(insets.top);

                    g.setColor(function.getLineColor());

                    interval = (xMax - xMin) / numOfSegments;

                    double tmpX0 = 0, tmpX1 = 0, tmpY0 = 0, tmpY1 = 0, midX, midY;

                    for (int i = 0; i < numOfSegments - 1; i++)
                    {
                        try
                        {
                            tmpX0 = xMin + i * interval;

                            //varsTable.put("x", new Double(tmpX0));
                            function.setXValue(tmpX0);

                            tmpY0 = function.value();

                            if (tmpY0 > maxYHat)
                                maxYHat = tmpY0;

                            if (tmpY0 < minYHat)
                                minYHat = tmpY0;

                            tmpX1 = tmpX0 + interval;
                            //varsTable.put("x", new Double(tmpX1));

                            function.setXValue(tmpX1);

                            tmpY1 = function.value();

                            midX = (tmpX0 + tmpX1) / 2;

                            //varsTable.put("x", new Double(midX));

                            function.setXValue(midX);

                            midY = function.value();

                            int yco1 = coord.ycoord(tmpY0);
                            int yco2 = coord.ycoord(tmpY1);
                            int xco1 = coord.xcoord(tmpX0);
                            int xco2 = coord.xcoord(tmpX1);
						
                           //Test if the function is continuous inside the interval
                            if (isBetween(midY, tmpY0, tmpY1) && yco1 > 0 && yco1 < height && yco2 > 0 && yco2 < height)
                            {
                             //   g.drawLine(coord.xcoord(tmpX0), coord.ycoord(tmpY0), coord.xcoord(tmpX1), coord.ycoord(tmpY1));
                             //  if (yco1 <= height - insets.bottom && yco1 >= insets.top && yco2 <= height - insets.bottom && yco2  >= insets.top)
                               g.drawLine(xco1, yco1, xco2, yco2);
                            }
                        }
                        catch (ExecError e)
                        {
						//	System.out.println("4");
    						//	System.err.println(e);
						//	System.out.println("maxYHat (catch): " + maxYHat);
						//	System.out.println("minYHat (catch): " + minYHat);
                        	
                        }
                    }
                }
            }
        }
    }

    public void tableChanged(TableModelEvent e)
    {
        update(getGraphics());
    }

    public void clearSelectedPoints()
    {
        List indices = new ArrayList();

        for (int i = dataSet.getRowCount() - 1; i >= 0; i--)
        {
            if (pointsSelectionModel.isSelectedIndex(i))
                indices.add(new Integer(i));
        }

        for (int i = 0; i < indices.size(); i++)
        {
            int index = ((Integer) indices.get(i)).intValue();
            dataSet.deleteRows(index, index);
        }
    }

    public static void main(String[] args)
    {

        String expr = "10*x^2";

        Expression func = null;
        ExpressionParser ep = new ExpressionParser();

        try
        {
            func = ep.parse(expr);
        }
        catch (ParseException se)
        {
            se.printStackTrace();
            System.exit(0);
        }

        FunctionPlotter plotter = new FunctionPlotter();

        plotter.addFunction(new ExpressionObject(func, expr, ExpressionObject.FUNCTION));

        Frame fr = new Frame("Grapher");

        fr.setLayout(new BorderLayout());
        fr.add(plotter, BorderLayout.CENTER);
        
        fr.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent evt)
            {
                System.exit(0);
            }
        }
        );

        fr.pack();
        fr.show();
    }

    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Point pt = mouseEvent.getPoint();
        init_x = pt.x;
        init_y = pt.y;

        //used to check the toggle buttons
        if (enablePointClick && !zoomModeOn) {
            if (pt.x > insets.left && pt.x < width - insets.right && pt.y > insets.top && pt.y < height - insets.bottom) {
                double tempxmin = coord.getRealX(insets.left);
                double tempxmax = coord.getRealX(width - insets.right);
                double tempymin = coord.getRealY(height - insets.bottom);
                double tempymax = coord.getRealY(insets.top);

                double conv_initx = coord.getRealX(init_x);
                double conv_inity = coord.getRealY(init_y);

                //PLEASE DO NOT ERASE THESE COMMENTS
                // double tempxmin = dataSet.getMinimumX();
                // double tempxmax = dataSet.getMaximumX();
                // double tempymin = dataSet.getMinimumY();
                // double tempymax = dataSet.getMaximumY();
                //  double xsize = min(tempxmax - , init_x - tempxmax);
                //  double ysize = min(tempymax - init_y, init_y - tempymax);
                //  xMax0 = Math.abs(xMax0) > Math.abs(xMin0) ? Math.abs(xMax0) : Math.abs(xMin0);
                // double tempxsize1 = tempxmax - conv_initx;
                // double tempxsize2 = onv_initx - tempxmin;
                // double tempysize1 = tempxmay - conv_initx;
                // double tempysize2 = onv_inity - tempxmin;
                // double xsize = (tempxmax - conv_initx) > (conv_initx - tempxmin) ? (conv_initx - tempxmin) : (tempxmax - conv_initx);
                //double ysize = (tempymax - conv_inity) > (conv_inity - tempymin) ? (conv_inity - tempymin) : (tempymax - conv_inity);
                //double size = (xsize) > (ysize) ? (1/2*ysize) : (1/2*xsize);
                // double size = (tempxmax - conv_initx);
                //size = coord.getRealY(height + insets.bottom);
                //double conv_initx = coord.getRealX(init_x);
                //double conv_inity = coord.getRealY(init_y);

                // double xsize = min(tempxmax - conv_initx, conv_initx - tempxmin);
                // double ysize = min(tempymax - conv_inity, conv_inity - tempymin);

                //this code below is for the point-click zoom
                double xsize = Math.min(tempxmax - conv_initx, conv_initx - tempxmin);
                double ysize = Math.min(tempymax - conv_inity, conv_inity - tempymin);
			
                double size = Math.min(xsize*1/2, ysize*1/2);
			
                double tmpX0 = (conv_initx - size);
                double tmpY0 = (conv_inity + size);
                double tmpX1 = (conv_initx + size);
                double tmpY1 = (conv_inity - size);

                ratioX = (tmpX1 - tmpX0) / (width - insets.left - insets.right);
                ratioY = (tmpY0 - tmpY1) / (height - insets.top - insets.bottom);
				
                this.xInterval = (tmpX1 - tmpX0) / (2 * numOfGridlines);
                this.yInterval = (tmpY0 - tmpY1) / (2 * numOfGridlines);

                coord.setXRatio(ratioX);
                coord.setYRatio(ratioY);
                coord.moveOriginTo((tmpX0 + tmpX1) / 2, (tmpY0 + tmpY1) / 2);

                update(getGraphics());
                //the code below sets zoomModeOn to false and then
                // uses a boolean value gotit inside a while to propagate
                //up the hiearachy in order to find toggle parent which is
                //grapher. solution by Tan.
                enablePointClick = false;
                boolean gotit2 = false;
                Container grapher = getParent();
                while(!gotit2){
                    if(grapher instanceof Grapher){
                        ((Grapher)grapher).pclickButton.releaseButton();
                        gotit2 = true;
                        //  this.setCursor(Cursor.getDefaultCursor());
                    }
                    else {
                        grapher = grapher.getParent();
                    }
                }
            }
        }
    }

    public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        update(getGraphics());
    }

    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }

    public void mouseDragged(java.awt.event.MouseEvent mouseEvent) {
    }

    public static class PlotOptions
    {

        boolean showFunctions = true;
        boolean showModels = true;
        boolean showData = true;
        boolean addingPoint = false;
        int pointsToAdd = 0;

        public PlotOptions()
        {
        }

        public PlotOptions(PlotOptions options)
        {
            this.showFunctions = options.showFunctions;
            this.showData = options.showData;
            this.addingPoint = options.addingPoint;
        }

        public boolean isShowFunctions()
        {
            return showFunctions;
        }

        public void setShowFunctions(boolean showFunctions)
        {
            this.showFunctions = showFunctions;
        }

        public boolean isShowData()
        {
            return showData;
        }

        public void setShowData(boolean showData)
        {
            this.showData = showData;
        }

        public boolean isAddingPoint()
        {
            return addingPoint;
        }

        public void setAddingPoint(boolean addingPoint)
        {
            this.addingPoint = addingPoint;
        }

        public int getPointsToAdd()
        {
            return pointsToAdd;
        }

        public void setPointsToAdd(int pointsToAdd)
        {
            this.pointsToAdd = pointsToAdd;
        }

        public boolean isShowModels()
        {
            return showModels;
        }

        public void setShowModels(boolean showModels)
        {
            this.showModels = showModels;
        }
    }
}
