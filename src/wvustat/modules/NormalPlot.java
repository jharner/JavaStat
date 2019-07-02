package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.rmi.*;

import javax.swing.*;

import java.text.*;
import java.util.Vector;
import java.util.Arrays;

import wvustat.interfaces.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.plot.ManualAxisModel;
import wvustat.swing.XAxisRenderer;
import wvustat.swing.YAxisRenderer;

/**
 *	NormalPlot is a plot where data values is plotted against their equavalent normal
 *	quantiles.	The x axis is normal qunatiles from -3 to 3. The y axis is the actual data values.
 *	It is used in HistogramModule.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Feb. 2, 2000
 */

public class NormalPlot extends JPanel implements MouseListener, MouseMotionListener{

    private DataSet data;
    private Variable y_var, z_var;
    private Vector vz;
    
    private boolean plotState = true;

    private int width=250, height=200;

    private CoordConverter coord=null;
    private double ymin, ymax, yScale;
    private double xmin, xmax, xScale;

    //this two arrays have increasing values
    private int[] indices;
    private double[] sorted, quantiles;

    //Each disc here represents an observation
    private Ellipse2D[] discs;

    private Insets insets=new Insets(30,50,40,40);
    private NumberFormat nf;

    private Rectangle2D dragRange=new Rectangle2D.Float();
    private boolean isDragging=false;
    private int brushWidth = 10, brushHeight = 10;

    private EqualCountGrouper grouper;

    private int currentIndex=0;

    private Vector zval;

    private Distribution normal;

    //This boolean var is used to indicate if quantile lines will be drawn
    private boolean drawQuantLine=false;

    //this boolean var is used to indicate if a robust fit line will be drawn
    private boolean robustFit=false;

    private double IQR, outlierRule1, outlierRule2, xOutlierRule1, xOutlierRule2;
    private BaseAxisModel xAxisModel, yAxisModel;
    private Rectangle xAxisRegion = new Rectangle();
    
    private Variable init_y_var = null;
    private GeneralPath popupArrow;
    private GUIModule module;

    /**
     * Constructor
     * Creates a new NormalPlot with the given data set and variables
     */
    public NormalPlot(DataSet data, Variable y, Vector vz) throws IllegalArgumentException{
        this.data=data;
        y_var=y;
        this.init_y_var = y_var;
        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        //z_var = z;


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


    /**
     * This method is overriden so that the plot can be dynamically resized.
     */
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        initPlot();
        super.setBounds(x,y,w,h);
    }

    /**
     * Set the GroupMaker object for this NormalPlot object
     */
    public void setGroupMaker(EqualCountGrouper gm){
        grouper=gm;
    }

    /**
     * Toggle the "Quantile lines" option on/off
     */
    public void enableQuantLine(boolean val){
        drawQuantLine=val;

        repaint();
    }

    /**
     * Toggle the "Robust fit" option on/off
     */
    public void enableRobustFit(boolean val){
        robustFit=val;

        repaint();
    }


    public Dimension getSize(){
        return new Dimension(width,height);
    }


    /**
     * initialize the component
     */
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
            	discs=new Ellipse2D[0]; //added by djluo
                throw new RemoteException("empty variable");
            }
            //this array stores the value for the current group, values that
            //are masked out are not included
            double[] values=new double[v.size()];
            indices=new int[v.size()];

            for(int i=0;i<values.length;i++){
                values[i]=((Double)v.elementAt(i)).doubleValue();
                indices[i]=((Integer)iv.elementAt(i)).intValue();
            }

            int n=v.size();
            discs=new Ellipse2D[n];

            StatEngine se=new StatEngineImpl();
            normal=se.getDistribution("normal", new double[]{0.0, 1.0});

            int[] rank=se.getRanks(values);
            sorted=se.sortArray(values);
            quantiles=se.getQuantiles(v);

            IQR=quantiles[3]-quantiles[1];
            outlierRule1=quantiles[1]-1.5*IQR;
            outlierRule2=quantiles[3]+1.5*IQR;
            xOutlierRule1=quantiles[1]-3.0*IQR;
            xOutlierRule2=quantiles[3]+3.0*IQR;

            ymin=sorted[0];
            ymax=sorted[n-1];

            //adjust min and max so we have a nice ticmarks set
            int numOfTics=(int)Math.floor(Math.log(y_var.getSize())/Math.log(2.0));
            
            if(xAxisModel == null)
                xAxisModel = new BaseAxisModel(ymin, ymax, numOfTics);

            xmin=xAxisModel.getStartValue();
            xmax=xAxisModel.getEndValue();
            xScale=xAxisModel.getInterval();

            ymin=-3;
            ymax=3;
            yScale=1;
            
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


            for(int i=0;i<discs.length;i++){
                double prob=rank[i]/(n+1.0);
                double nq=normal.quantile(prob);
                discs[i]=new Ellipse2D.Float(coord.x(values[i])-3, coord.y(nq)-3, 6, 6);
            }
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
		g2.drawString("Normal Quantiles", yaxis.getPreferredSize().width, insets.top - yaxis.getTopGap());
        
		

        double boxStart=quantiles[4], boxEnd=quantiles[0];

        
        //draw the discs representing observations
        for(int i=0;i<discs.length;i++){
            if(sorted[i]<boxStart && sorted[i]>outlierRule1)
                boxStart=sorted[i];

            if(sorted[i]>boxEnd && sorted[i]<outlierRule2)
                boxEnd=sorted[i];

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
        

        if(drawQuantLine && discs.length>0){
            g2.setColor(Color.red);
            if(sorted[sorted.length-1]>outlierRule2)
                g2.drawLine(coord.x(outlierRule2), coord.y(ymin), coord.x(outlierRule2), coord.y(ymax));
            if(sorted[sorted.length-1]>xOutlierRule2)
                g2.drawLine(coord.x(xOutlierRule2), coord.y(ymin), coord.x(xOutlierRule2), coord.y(ymax));
            if(sorted[0]<outlierRule1)
                g2.drawLine(coord.x(outlierRule1), coord.y(ymin), coord.x(outlierRule1), coord.y(ymax));
            if(sorted[0]<xOutlierRule1)
                g2.drawLine(coord.x(xOutlierRule1), coord.y(ymin), coord.x(xOutlierRule1), coord.y(ymax));
            g2.setColor(Color.black);
        }

        //draw a light gray rectangle to indicate mouse dragging
        if(isDragging){
            g2.setColor(Color.lightGray);
            g2.draw(dragRange);
        }

        g2.setColor(Color.black);
        //Draw outlier boxplot
        if(drawQuantLine && discs.length>0){
            double midY=(ymin+ymax)/2.0;
            int boxWidth=40;
            g2.drawLine(coord.x(boxStart), coord.y(midY), coord.x(quantiles[1]), coord.y(midY));
            g2.drawLine(coord.x(quantiles[3]), coord.y(midY), coord.x(boxEnd), coord.y(midY));
            g2.drawLine(coord.x(quantiles[1]), coord.y(midY)-boxWidth/2, coord.x(quantiles[1]), coord.y(midY)+boxWidth/2);
            g2.drawLine(coord.x(quantiles[2]), coord.y(midY)-boxWidth/2, coord.x(quantiles[2]), coord.y(midY)+boxWidth/2);
            g2.drawLine(coord.x(quantiles[3]), coord.y(midY)-boxWidth/2, coord.x(quantiles[3]), coord.y(midY)+boxWidth/2);
            g2.drawLine(coord.x(quantiles[1]), coord.y(midY)-boxWidth/2, coord.x(quantiles[3]), coord.y(midY)-boxWidth/2);
            g2.drawLine(coord.x(quantiles[1]), coord.y(midY)+boxWidth/2, coord.x(quantiles[3]), coord.y(midY)+boxWidth/2);
        }

        if(robustFit && discs.length>0){

            double y1=normal.quantile(0.25);
            double y2=normal.quantile(0.75);
            double x1=quantiles[1];
            double x2=quantiles[3];
            double b1=1.0*(y2-y1)/(x2-x1);
            double b0=y1-b1*x1;

            double xStart=xmin;
            double yStart=b0+b1*xStart;
            if(yStart<ymin){
                yStart=ymin;
                xStart=(ymin-b0)/b1;
            }

            double xEnd=xmax;
            double yEnd=b0+b1*xEnd;
            if(yEnd>ymax){
                yEnd=ymax;
                xEnd=(ymax-b0)/b1;
            }

            g2.setColor(Color.blue);

            g2.drawLine(coord.x(xStart), coord.y(yStart), coord.x(xEnd), coord.y(yEnd));

        }
    }

    public void updatePlot(String arg){
        if(arg.equals("obs_deleted")){
            initPlot();
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


    //The following are MouseListener methods
    public void mousePressed(MouseEvent me){
    	
    	if (popupArrow.getBounds().contains(me.getX(), me.getY())) {
			JPopupMenu popupOptionMenu = ((HistogramModule) module).getOptionMenu().getPopupMenu();
			popupOptionMenu.show(me.getComponent(), me.getX(), me.getY());
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
            NormalPlot.this.repaint();
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
                    Color c=JColorChooser.showDialog(NormalPlot.this,"Palette", Color.black);
                    if(c!=null && data.getState(indices[i]))
                        data.setColor(c,indices[i]);
                    return;
                }

                if(!multipleSelection){
                    data.clearStates();
                }
                boolean bl=data.getState(indices[i]);
                data.setState(!bl, indices[i]);

                found = true; //return;
            }
            i++;
        }

        if(!found) data.clearStates();

        dragRange.setFrame(me.getX(),me.getY(),0,0);
        isDragging=true;
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

    public void mouseReleased(MouseEvent me){

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
                int option = JOptionPane.showOptionDialog(NormalPlot.this, configPanel,
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
                        JOptionPane.showMessageDialog(NormalPlot.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
    }

    public void mouseEntered(MouseEvent me){}
    public void mouseExited(MouseEvent me){}

    public void mouseDragged(MouseEvent me){
    	
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

    public void mouseMoved(MouseEvent me){}


    public void setGroup(int index){
        if(z_var==null) return;
        currentIndex=index;
        initPlot();
        repaint();
    }

}

