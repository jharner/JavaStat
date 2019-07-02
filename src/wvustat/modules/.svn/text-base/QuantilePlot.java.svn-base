package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.rmi.*;

import javax.swing.*;
import javax.swing.event.*;
import java.text.*;
import java.util.Arrays;
import java.util.Vector;

import wvustat.interfaces.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.plot.ManualAxisModel;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;

/**
 *	QuantilePlot is a plot where data values is plotted against their percentile.
 *	The x axis is fraction from 0.0 to 1.0. The y axis is the actual data values.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Jan. 31, 2000
 */
public class QuantilePlot extends JPanel implements MouseListener, MouseMotionListener{


    protected EventListenerList eList=new EventListenerList();
    protected transient ChangeEvent changeEvent;

    private DataSet data;
    private Variable y_var, z_var;
    private Vector vz;
    
    private boolean plotState = true;

    private int width=250, height=200;

    private CoordConverter coord=null;
    private double ymin, ymax, yScale;
    private double xmin, xmax, xScale;

    //this two arrays have increasing values
    private int[] xmap,ymap;

    private int[] indices,rank;

    private double[] values, sorted;

    private double[] quantiles;

    //Each disc here represents an observation
    private Ellipse2D[] discs;

    private Insets insets=new Insets(30,40,40,40);
    private NumberFormat nf;

    private Rectangle2D dragRange=new Rectangle2D.Float();
    private boolean isDragging=false;
    private int brushWidth = 10, brushHeight = 10;

    private EqualCountGrouper grouper;

    private int currentIndex=0;

    private Vector zval;

    //These two stores projected x and y value
    private double projx, projy;

    private Rectangle2D projArea;

    private boolean isTracing=false;
    private boolean traceStarted=false;

    //These boolean variable indicate if quantile lines will be drawn
    private boolean drawQuantLine=false;

    private BaseAxisModel xAxisModel, yAxisModel;

    private Rectangle xAxisRegion = new Rectangle();
    
    private Variable init_y_var = null;
    
    private GeneralPath popupArrow;
    private GUIModule module;

    /**
     * Constructor
     *	Creates a QuantilePlot with the given data set and its variables.
     */
    public QuantilePlot(DataSet data, Variable y, Vector vz) throws IllegalArgumentException{
        this.data=data;
        y_var=y;
        this.init_y_var = y_var;
        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        //z_var=z;

        if (y_var.getType() != Variable.NUMERIC)
            throw new IllegalArgumentException("Y variable not numeric");

        setBackground(Color.white);
        setSize(new Dimension(width,height));
        setPreferredSize(new Dimension(width,height));
        setMinimumSize(new Dimension(width,height));

        addMouseListener(this);
        addMouseMotionListener(this);
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }



    public void addChangeListener(ChangeListener l){
        eList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l){
        eList.remove(ChangeListener.class, l);
    }

    protected void fireStateChanged(){
        Object[] list=eList.getListenerList();
        for(int i=list.length-2;i>=0;i-=2){
            if(list[i]==ChangeListener.class){
                if(changeEvent==null)
                    changeEvent=new ChangeEvent(this);
                ((ChangeListener)list[i+1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Get the position of the "x" cursor when the options "show quantiles" is selected.
     * Notice that the coordinates of the point is what reflected by the axes, not the coordinates
     * in bitmap values.
     */
    public Point2D getCursorPosition(){
        return new Point2D.Double(projx, projy);
    }

    public void setCursorPosition(Point2D pt){
        projx=pt.getX();
        projy=pt.getY();
        repaint();
    }



    /**
     * Toggle the option "show quantile values" on/off
     */
    public void enableQuantileTracing(boolean val){
        isTracing=val;

        if(!isTracing) projArea=null;

        projy=0.5;
        projx=quantiles[2];

        repaint();
    }

    /**
     * Toggle the option "Draw quantile lines" on/off
     */
    public void enableQuantLine(boolean val){
        drawQuantLine=val;

        repaint();
    }

    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        initPlot();
        super.setBounds(x,y,w,h);
    }

    /**
     * Set the GroupMaker object associated
     */
    public void setGroupMaker(EqualCountGrouper gm){
        grouper=gm;
    }

    /**
     * Get the size of this component
     */
    public Dimension getSize(){
        return new Dimension(width,height);
    }


    private void initPlot(){
        try{
        	plotState = true;

            double[] yvals=y_var.getNumValues();
            if(z_var!=null)
                zval=z_var.getValues();

            //This vector keeps y values
            Vector v=new Vector();
            //This vector keeps original indices
            Vector iv=new Vector();

            for(int i=0;i<yvals.length;i++){
                if(!data.getMask(i)){
                    Double d=new Double(yvals[i]);
                    if(grouper==null){
                        iv.addElement(new Integer(i));
                        v.addElement(d);
                    }
                    else if(grouper.getGroupIndex(i).contains(new Integer(currentIndex))){
                        iv.addElement(new Integer(i));
                        v.addElement(d);
                    }
                }
            }

            if(v.size()==0){
            	discs = new Ellipse2D[0]; //added by djluo
            	throw new RemoteException("empty variable");
            }
            //this array stores the value for the current group, values that
            //are masked out are not included
            values=new double[v.size()];
            indices=new int[v.size()];

            for(int i=0;i<values.length;i++){
                values[i]=((Double)v.elementAt(i)).doubleValue();
                indices[i]=((Integer)iv.elementAt(i)).intValue();
            }

            int n=v.size();
            xmap=new int[n];
            ymap=new int[n];
            discs=new Ellipse2D[n];

            StatEngine se=new StatEngineImpl();

            rank=se.getRanks(values);
            sorted=se.sortArray(values);
            quantiles=se.getQuantiles(v);


            xmin=sorted[0];
            xmax=sorted[n-1];



            //adjust min and max so we have a nice ticmarks set
            int numOfTics=(int)Math.floor(Math.log(y_var.getSize())/Math.log(2.0));
            if(xAxisModel==null)
                xAxisModel = new BaseAxisModel(xmin, xmax, numOfTics);
            xmin=xAxisModel.getStartValue();
            xmax=xAxisModel.getEndValue();
            xScale=xAxisModel.getInterval();

            ymin=0;
            ymax=1.0;
            yScale=0.1;
            
            yAxisModel = new ManualAxisModel();
            yAxisModel.setStartValue(ymin);
            yAxisModel.setEndValue(ymax);
            yAxisModel.setInterval(yScale);

            nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);

            //This is to make sure we always have enough room to render
            //the y ticmarks
            FontMetrics metrics=getFontMetrics(getFont());
            insets.left=metrics.stringWidth(nf.format(ymax))+15;
            if(insets.left<30)
                insets.left=30;

            coord=new CoordConverter(width, height, xmin, xmax, ymin, ymax,insets);

            for(int i=0;i<n;i++){
                discs[i]=new Ellipse2D.Float(coord.x(values[i])-3,coord.y(rank[i]/(n+1.0))-3,6, 6);
                xmap[i]=coord.x(sorted[i]);
                ymap[i]=coord.y((i+1.0)/(n+1.0));
            }
            
            projx=interpolate(projy); //added by djluo

        }
        catch(RemoteException re){
        	plotState = false;
        	System.err.println(re.getMessage());
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2=(Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        FontMetrics metrics=getFontMetrics(getFont());

        //draw one line at the top and one line at the bottom of this plot
        g2.drawLine(0,0,width-1,0);
        g2.drawLine(0, height-1, width-1, height-1);
        
        if (!plotState) {
            String errString = "Error getting data, please try again later.";
            g2.drawString(errString, (int) (getSize().width / 2.0 - metrics.stringWidth(errString) / 2.0),
                    (int) (getSize().height / 2.0 - metrics.getHeight() / 2.0));
            return;
        }

        g2.setColor(Color.black);
        g2.fill(popupArrow);
        
        
        //draw x axis
        XAxisRenderer xaxis = new XAxisRenderer(xAxisModel, getSize(), insets);
        xaxis.setRightGap(20);
        xaxis.setLabel(y_var.getName());
        SwingUtilities.paintComponent(g2, xaxis, this, insets.left - xaxis.getLeftGap(), 
				getSize().height - insets.bottom, xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        xAxisRegion.setBounds(insets.left - xaxis.getLeftGap(), getSize().height - insets.bottom, 
        		xaxis.getPreferredSize().width, xaxis.getPreferredSize().height);
        
        //draw y axis
        YAxisRenderer yaxis = new YAxisRenderer(yAxisModel, getSize(), insets);
        yaxis.setTopGap(10);
		SwingUtilities.paintComponent(g2, yaxis, this, 0,
				insets.top - yaxis.getTopGap(), yaxis.getPreferredSize().width, yaxis.getPreferredSize().height);
		g2.drawString("Fraction", yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());


        
        //draw the discs representing observations
        for(int i=0;i<discs.length;i++){
            g2.setColor(Color.black);
            if(i>0){
                g2.drawLine(xmap[i], ymap[i], xmap[i-1], ymap[i-1]);
            }


            //set the proper color for this observation
            g2.setColor(data.getColor(indices[i]));
            if(data.getState(indices[i])){
                g2.fill(discs[i]);
                if(data.getLabel(indices[i])!=null){
                    g2.setColor(Color.black);
                    g2.drawString(data.getLabel(indices[i]),(int)discs[i].getMaxX()+2,(int)discs[i].getMinY()-2);
                }
            }
            else
                g2.draw(discs[i]);
        }
        

        //draw a light gray rectangle to indicate mouse dragging
        if(isDragging){
            g2.setColor(Color.lightGray);
            g2.draw(dragRange);
        }

        if(drawQuantLine && discs.length>0){
            g2.setColor(Color.black);
            double midY=(ymin+ymax)/2.0;
            int boxWidth=40;
            g2.drawLine(coord.x(quantiles[0]), coord.y(midY), coord.x(quantiles[1]), coord.y(midY));
            g2.drawLine(coord.x(quantiles[3]), coord.y(midY), coord.x(quantiles[4]), coord.y(midY));

            g2.drawLine(coord.x(quantiles[0]), coord.y(midY)-boxWidth/4, coord.x(quantiles[0]), coord.y(midY)+boxWidth/4);
            g2.drawLine(coord.x(quantiles[4]), coord.y(midY)-boxWidth/4, coord.x(quantiles[4]), coord.y(midY)+boxWidth/4);


            g2.drawLine(coord.x(quantiles[1]), coord.y(midY)-boxWidth/2, coord.x(quantiles[1]), coord.y(midY)+boxWidth/2);
            g2.drawLine(coord.x(quantiles[2]), coord.y(midY)-boxWidth/2, coord.x(quantiles[2]), coord.y(midY)+boxWidth/2);
            g2.drawLine(coord.x(quantiles[3]), coord.y(midY)-boxWidth/2, coord.x(quantiles[3]), coord.y(midY)+boxWidth/2);
            g2.drawLine(coord.x(quantiles[1]), coord.y(midY)-boxWidth/2, coord.x(quantiles[3]), coord.y(midY)-boxWidth/2);
            g2.drawLine(coord.x(quantiles[1]), coord.y(midY)+boxWidth/2, coord.x(quantiles[3]), coord.y(midY)+boxWidth/2);
        }

        if(isTracing && discs.length>0){
            g2.setColor(Color.blue);
            int xco=coord.x(projx), yco=coord.y(projy);
            //Define a 'draggable' region
            projArea=new Rectangle2D.Float(xco-3,yco-3,6,6);

            g2.drawLine(xco, yco, coord.x(xmin), yco);
            g2.drawLine(xco, yco, xco, coord.y(ymin));

            for(int i=0;i<discs.length;i++){
                if(discs[i].contains(xco, yco)){
                    projy=rank[i]/(1.0+discs.length);
                    projx=values[i];
                    break;
                }
            }


            nf.setMaximumFractionDigits(2);
            g2.drawString(nf.format(projy), coord.x(xmin)+4, yco-4);
            nf.setMaximumFractionDigits(3);
            g2.drawString(nf.format(projx), xco+4, coord.y(ymin)-4);


            g2.setStroke(new BasicStroke(1.5f));
            //Draw a 'x' to indicate current position
            g2.drawLine(xco-3, yco-3, xco+3, yco+3);
            g2.drawLine(xco-3, yco+3, xco+3, yco-3);
        }

    }

    /**
     * updates the plot to reflect changes in the data set
     */
    public void updatePlot(String arg){
        if(arg.equals("obs_deleted")){
            initPlot();
            projy=0.5;
            projx=quantiles[2];
            repaint();
        }
        else if(arg.equals("yystate")){
            repaint();
        }
        else if(arg.equals("yycolor")){
            repaint();
        }
        else if(arg.equals("yymask")){
            initPlot();
            projy=0.5;
            projx=quantiles[2];
            repaint();
        }
        /*else if(arg.equals(yname)){
            initPlot();
            repaint();
        }
        else if(zname!=null && arg.equals(zname)){
            initPlot();
            repaint();
        }
        else if(arg.equals("new_obs_added")){
            initPlot();
            repaint();
        }*/
        else if (arg.equals("add_variable")||arg.equals("delete_variable")) {
    	    
    	    if(data.getTransformedVar(init_y_var.getName()) != null)
    		    y_var = data.getTransformedVar(init_y_var.getName());
    	    else
    		    y_var = init_y_var;
    	    initPlot();
    	    repaint();
    	    
        }
    }

    public BaseAxisModel getXAxisModel() {
        return xAxisModel;
    }
    
    public void setXAxisModel(BaseAxisModel axisModel){
        this.xAxisModel=axisModel;
        initPlot();
        repaint();
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }


    /**
     *  MouseListener methods
     */
    public void mousePressed(MouseEvent me){
    	
    	if (popupArrow.getBounds().contains(me.getX(), me.getY())) {
    		JPopupMenu popupOptionMenu = ((HistogramModule) module).getOptionMenu().getPopupMenu();
    		popupOptionMenu.show(me.getComponent(), me.getX(), me.getY());
    		return;
    	}
    	
        if(projArea!=null && projArea.contains(me.getPoint())){
            traceStarted=true;
            return;
        }

        if (LinkPolicy.mode != LinkPolicy.SELECTING)
        {        	
        	
            //data.clearStates();
        	boolean[] states = new boolean[data.getSize()];
        	Arrays.fill(states, false);
        	
            dragRange.setRect(me.getX() - brushWidth, me.getY() - brushHeight, brushWidth, brushHeight);

            for(int i=0;i<discs.length;i++){
                if(dragRange.contains(discs[i].getBounds()))
                	states[indices[i]] = true; //data.setState(true, indices[i]);
            }
            data.setStates(states);
        	
            isDragging = true;
            QuantilePlot.this.repaint();
            return;
        }
        
        // when LinkPolicy.mode == SELECTING
        
        boolean found=false;
        boolean multipleSelection=false;

        if(me.getModifiers()==17){
            multipleSelection=true;
        }

        
        int i=0;
        while(i<discs.length && !found){
            if(discs[i].contains(me.getPoint())){

                if(me.getClickCount()==2){
                    Color c=JColorChooser.showDialog(QuantilePlot.this,"Palette", Color.black);
                    if(c!=null && data.getState(indices[i]))
                        data.setColor(c,indices[i]);
                    return;
                }

                if(!multipleSelection)
                    data.clearStates();

                boolean bl=data.getState(indices[i]);
                data.setState(!bl, indices[i]);
                found=true;
                //return;
            }
            i++;
        }

        if(!found) data.clearStates();
        

        dragRange.setFrame(me.getX(),me.getY(),0,0);
        isDragging=true;
    }

    public void mouseReleased(MouseEvent me){
        if(traceStarted){
            fireStateChanged();
            traceStarted=false;
            return;
        }

        boolean[] states = data.getStates();
        for(int i=0;i<discs.length;i++){
            if(dragRange.contains(discs[i].getBounds()))
            	states[indices[i]] = true; //data.setState(true, indices[i]);
        }
        data.setStates(states);

        isDragging=false;
        repaint();
    }

    public void mouseClicked(MouseEvent e){
           if (e.getClickCount() == 2 && xAxisRegion.contains(e.getPoint()))
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(xAxisModel);
                int option = JOptionPane.showOptionDialog(this, configPanel,
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
                        JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
    }
    public void mouseEntered(MouseEvent me){
    }
    public void mouseExited(MouseEvent me){
    }

    public void mouseDragged(MouseEvent me){
        if(traceStarted){
            int n=sorted.length;
            if(me.getY()<=coord.y(1.0/(n+1)) && me.getY()>=coord.y(n/(n+1.0))){
                projy=Math.round(coord.inverseY(me.getY())*100)/100.0;
                projx=interpolate(projy);
                repaint();
            }
            return;
        }

        if (isDragging && LinkPolicy.mode != LinkPolicy.SELECTING)
        {
        	if (me.isAltDown())
        	{	
        		brushWidth = (int)(me.getX()-dragRange.getX() > 10 ? me.getX()-dragRange.getX() : 10);
        		brushHeight = (int)(me.getY()-dragRange.getY() > 10 ? me.getY()-dragRange.getY() : 10);
        	}
        	
        	boolean[] states = data.getStates();
            if (LinkPolicy.mode == LinkPolicy.BRUSHING && !me.isShiftDown()) 
            	Arrays.fill(states, false); //data.clearStates();
                
            dragRange.setRect(me.getX() - brushWidth, me.getY() - brushHeight, brushWidth, brushHeight);

            for(int i=0;i<discs.length;i++){
                if(dragRange.contains(discs[i].getBounds()))
                	states[indices[i]] = true; //data.setState(true, indices[i]);
            }
            data.setStates(states);
        }    
        
        double w=me.getX()-dragRange.getX();
        double h=me.getY()-dragRange.getY();
        dragRange.setFrame(dragRange.getX(), dragRange.getY(),w,h);
        repaint();
    }

    public void mouseMoved(MouseEvent me){
    }

    private void printArray(double[] array){
        for(int i=0;i<array.length;i++){
            System.out.print(array[i]+",");
        }
    }

    public void setGroup(int index){
        if(z_var==null) return;

        currentIndex=index;
        initPlot();
        projy=0.5;
        projx=quantiles[2];

        repaint();
    }

    private double interpolate(double x){
        double result;

        int n=sorted.length;

        if(x<=1.0/(n+1))
            result=sorted[0];
        else if(x>=n/(n+1.0))
            result=sorted[n-1];
        else if(x==0.50)
            result=quantiles[2];
        else if(x==0.25)
            result=quantiles[1];
        else if(x==0.75)
            result=quantiles[3];
        else{
            int i=2;
            while(x>i/(n+1.0)) i++;

            double y1=sorted[i-2],y2=sorted[i-1];
            result=(y2-y1)*(n+1.0)*(x-(i-1.0)/(n+1.0))+y1;
        }

        return result;
    }

}

