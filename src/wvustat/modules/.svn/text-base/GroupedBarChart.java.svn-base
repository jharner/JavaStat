package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

import wvustat.interfaces.*;

/**
 * GroupedBarChart is similar to BarChart except that for a given x group, only one z group is displayed. A component is 
 * provided to let user choose between z groups. 
 *
 * @author: Hengyi Xue
 * @version: 1.0, June 1, 2000
 */

public class GroupedBarChart extends JPanel{
    private DataSet data;

    private Variable x_var, y_var, z_var;

    private Vector varNames;

    private String clientId;

    private int width=250, height=200;

    private FontMetrics metrics;

    private Insets insets=new Insets(20,40,40,20);

    private double[] yvals;

    private String[] xvals, zvals, xlevels, zlevels;

    private double ymin, ymax, xmin, xmax;

    private BaseAxisModel ytm;

    private CoordConverter coord;

    private NumberFormat nf;

    private Color[] colors;

    private Rectangle2D[] bars;

    private Rectangle2D focusedBar;

    private int focusedID;

    //width of the bar and the distance between two bars with the same x value
    private int barWidth=20, offSet=12;

    private Hashtable rank_table;

    private int zIndex=0;

    private double[] current;
  
    public GroupedBarChart(DataSet data, Variable x, Variable y, Variable z){
	this.data=data;
	this.x_var=x;
	this.y_var=y;
	this.z_var=z;

	retrieveData();

	setBackground(Color.white);
	setPreferredSize(new Dimension(width, height));

        addMouseMotionListener(new MouseHandler());

        varNames = new Vector();

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());
        if (z_var != null)
            varNames.addElement(z_var.getName());

        metrics = getFontMetrics(getFont());

    }

    private void retrieveData(){

    	rank_table=new Hashtable();
    	Vector sortNames=new Vector();

    	yvals=y_var.getNumValues();
    	xvals=x_var.getCatValues();
    	xlevels=x_var.getDistinctCatValues();
	    
    	sortNames.addElement(x_var.getName());

    	if(z_var!=null){
    		zvals=z_var.getCatValues();
    		zlevels=z_var.getDistinctCatValues();
    		sortNames.addElement(z_var.getName());
    	}

    	int[] ranks=data.getRanks(sortNames);

    	for(int i=0;i<ranks.length;i++){
    		rank_table.put(new Integer(ranks[i]), new Integer(i));
    	}

    	colors=new Color[xlevels.length];
		
    	float d=1.0f/(xlevels.length+1);
		
    	for(int i=0;i<colors.length;i++){
    		float r=d*(i+1);
    		if(i%3==0)
    			colors[i]=new Color(1-r, i/3.0f*d,0);
    		else if(i%3==1)
    			colors[i]=new Color(i/3.0f*d, r, 1.0f);
    		else if(i%3==2)
    			colors[i]=new Color(1-i/3.0f*d, 1-r, 0);
    	}
    }

    private void initPlot(){
	StatEngine se=new StatEngineImpl();

	current=new double[xlevels.length];

	int stepIndex=0;
	for(int i=0;i<yvals.length;i++){
	    if(z_var==null || i%zlevels.length==zIndex){
		int tmpID=((Integer)rank_table.get(new Integer(i))).intValue();
		current[stepIndex++]=yvals[tmpID];
	    }
	}

	ymin=current[0];
	ymax=current[0];

	for(int i=1;i<current.length;i++){
	    if(ymin>current[i]) ymin=current[i];
	    if(ymax<current[i]) ymax=current[i];
	}

	ytm=new BaseAxisModel(ymin, ymax, 5);
			
	//ymin=ytm.getMin();
	ymin=0;
	ymax=ytm.getEndValue();
		
	xmin=0;
	xmax=xlevels.length+1;

	nf=NumberFormat.getInstance();
	nf.setMaximumFractionDigits(3);
	insets.left=metrics.stringWidth(nf.format(ymax))+10;
	if(insets.left<40) insets.left=40;
			
	coord=new CoordConverter(width, height, xmin, xmax, ymin, ymax, insets);
	

	//Initialize bars
	bars=new Rectangle2D[xlevels.length];
	int ticWidth=coord.x(xmin+1)-coord.x(xmin);

	barWidth=ticWidth/2;
	    
	for(int i=0;i<bars.length;i++){

	    bars[i]=new Rectangle2D.Float(coord.x(i+1)-barWidth/2, coord.y(current[i]),barWidth, coord.y(ymin)-coord.y(current[i]));
	}
 
	
		
    }

   /**
     * Set the identifier string for this object
     */
    public void setClientId(String id){
	clientId=id;
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

    public void paintComponent(Graphics g){
	super.paintComponent(g);

	Graphics2D g2=(Graphics2D)g;
	RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHints(rh);

	BasicStroke thin=new BasicStroke(1.0f);
	BasicStroke thick=new BasicStroke(2.0f);
	
	//Draw two lines, one at the top of the plot, another at the bottom of the plot
	g2.drawLine(0,0, width-1, 0);
	g2.drawLine(0,height-1, width-1, height-1);
		
	//Draw x and y axis
	g2.setStroke(thick);
	g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmax), coord.y(ymin));
	g2.drawLine(coord.x(xmin), coord.y(ymin), coord.x(xmin), coord.y(ymax));

	g2.setStroke(thin);

	//draw x axis ticmarks
	for(int i=0;i<xlevels.length;i++){
	    String label=xlevels[i];
	    g2.drawLine(coord.x(i+1), coord.y(ymin), coord.x(i+1), coord.y(ymin)+4);
	    if(i%2==0)
		g2.drawString(label, coord.x(i+1)-metrics.stringWidth(label)/2, coord.y(ymin)+metrics.getHeight());
	    else 
		g2.drawString(label, coord.x(i+1)-metrics.stringWidth(label)/2, coord.y(ymin)+2*metrics.getHeight());
	}

	//Draw y ticmarks
	double tmp=ymin;
	double step=ytm.getInterval();
	while(tmp<=ymax){
	    g2.drawLine(coord.x(xmin), coord.y(tmp), coord.x(xmin)-4, coord.y(tmp));
	    String label=nf.format(tmp);
	    
	    int len=metrics.stringWidth(label);
	    
	    g2.drawString(label, coord.x(xmin)-4-len, coord.y(tmp)+metrics.getMaxAscent()/2);
	    
	    tmp+=step;
	}
	

	for(int i=0;i<bars.length;i++){
	    
	    g2.setPaint(colors[i]);

	    g2.fill(bars[i]);
	}

	if(focusedBar!=null){
	    g2.setPaint(colors[focusedID]);

	    g2.fill(focusedBar);

	    g2.setPaint(Color.black);
	    g2.draw(focusedBar);

	    NumberFormat nf=NumberFormat.getInstance();

	    int tmpX=(int)focusedBar.getX();
	    int tmpY=(int)focusedBar.getY()-20;
	    g2.drawString(nf.format(current[focusedID]), tmpX, tmpY);

	}

    }

    public Color[] getColors(){
	return colors;
    }

    public String[] getColorLabels(){
	return xlevels;
    }

    public String[] getGroupNames(){
	return zlevels;
    }

    private int findIndex(String[] array, String elem){
	int i=0;

	while(i<array.length && !array[i].equals(elem))
	    i++;

	return i;
    }

    public void setZIndex(int i){
	zIndex=i;
	initPlot();
	repaint();
    }

    class MouseHandler extends MouseMotionAdapter{
	public void mouseMoved(MouseEvent me){
	    Point pt=me.getPoint();

	    boolean found=false;
	    int i=0;
	    while(i<bars.length&&!found){
		found=bars[i].contains(pt);
		i++;
	    }

	    if(found){
		focusedBar=bars[i-1];
		focusedID=i-1;
		repaint();
	    }
	    else{
		focusedBar=null;
		repaint();
	    }
	}
    }
	
	
}
