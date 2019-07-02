package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import java.rmi.*;
import java.text.NumberFormat;
import java.util.*;

import wvustat.interfaces.*;

/**
* QuantileReport is a component that displays the quantile values of a data set. It is used
* in HistogramModule.
*
* @author: Hengyi Xue
* @version: 1.1, Mar. 15, 2000
*/
public class QuantileReport extends Report{
	private DataSet data;
	private Variable y_var, z_var;
	private Vector vz;
	private EqualCountPolicy policy;

	//observation count, mean, std. dev. and std. err. for each group
	private double gmin,gmax,glq, guq,gmedian, gIQR, gRange;
	//observation count, mean, std. dev. and std. err for the overall population
	private double omin, omax, olq, ouq, omedian, oIQR, oRange;
	
	private int currentIndex=0;
	private String groupName;
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,8,8);
	private Variable init_y_var = null;
	
	/**
	* Constructor
	* Creates a QuantileReport based on the data set and its variables
	*
	* @param data the data set
	* @param y_var the y variable within the data set
	* @param z_var the z variable within the data set
	* @param policy the algorithm used to divide observation into groups
	*/
	public QuantileReport(DataSet data, Variable y_var, Vector vz, EqualCountPolicy policy){
		this.data=data;
		this.y_var=y_var;
		this.init_y_var = y_var;
		//this.z_var=z_var;
		this.vz = vz;
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
		this.policy=policy;
	
		setBackground(Color.white);
		setToolTipText("Quantiles");
	
		initOverallStat();
		initGroupStat();
		
		metrics=getFontMetrics(basicFont);
		rowHeader=metrics.stringWidth("Lower quartile:");
		cellWidth=metrics.stringWidth("0000.000");
		cellHeight=metrics.getMaxAscent();
		
		int w,h;
		h=insets.top+insets.bottom+8*(vgap+cellHeight)-vgap;
		if(z_var==null)
			w=insets.left+insets.right+rowHeader+hgap+cellWidth;
		else
			w=insets.left+insets.right+rowHeader+2*hgap+2*cellWidth;
			
		setSize(new Dimension(w,h));
		setPreferredSize(new Dimension(w,h));
		setMinimumSize(new Dimension(w,h));
	
			
	}
	
	/**
	* Initialize all overall statistics
	*/
	public void initOverallStat(){
		try{
			double[] yvals=y_var.getNumValues();
			
			Vector v=new Vector();
			for(int i=0;i<yvals.length;i++){
				if(!data.getMask(i))
					v.addElement(new Double(yvals[i]));
			}
	
			StatEngine se=new StatEngineImpl();
			double[] quantiles=se.getQuantiles(v);
			
			omin=quantiles[0];
			olq=quantiles[1];
			omedian=quantiles[2];
			ouq=quantiles[3];
			omax=quantiles[4];
			
			oIQR=ouq-olq;
			oRange=omax-omin;
		}
		catch(RemoteException re){
			re.printStackTrace();
		}
	}
	
	/**
	*	Initialize all statistics for the current group
	*/
	public void initGroupStat(){
		if(z_var==null) return;
		
		try{
			double[] yvals=y_var.getNumValues();
			Vector zvals=z_var.getValues();
		
			Vector vec=new Vector();
			for(int i=0;i<yvals.length;i++){
				if(!data.getMask(i)&&policy.getGroupIndex(i).contains(new Integer(currentIndex))){
					vec.add(new Double(yvals[i]));
				}
			}
			
			StatEngine se=new StatEngineImpl();
			double[] quantiles=se.getQuantiles(vec);
			
			gmin=quantiles[0];
			glq=quantiles[1];	
			gmedian=quantiles[2];
			guq=quantiles[3];
			gmax=quantiles[4];
			
			gIQR=guq-glq;
			gRange=gmax-gmin;			
	
			/*if(z_var.getType()==Variable.NUMERIC){
				
				double d1=policy.getLowerLimit(currentIndex);
				double d2=policy.getUpperLimit(currentIndex);
				NumberFormat formatter=NumberFormat.getInstance();
				formatter.setMaximumFractionDigits(1);
				groupName=formatter.format(d1)+"-"+formatter.format(d2);
			}
			else
				groupName=policy.getGroupName(currentIndex);*/
			groupName=policy.getGroupName(currentIndex);
				
		}
		catch(RemoteException re){
			re.printStackTrace();
		}
	}
		
	public void setGroup(int index){
		if(z_var==null) return;
		
		currentIndex=index;
		initGroupStat();
		repaint();
	}
	
	public void updateReport(){
		
		if(data.getTransformedVar(init_y_var.getName()) != null)
	        y_var = data.getTransformedVar(init_y_var.getName());
		else
	        y_var = init_y_var;
		
		initOverallStat();
		initGroupStat();
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setFont(basicFont);
		
		NumberFormat formatter=NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(3);

		//get the position where we should start to render text
		int ma=metrics.getMaxAscent();
		int x0=insets.left, y0=insets.top+ma;
		
		int tempi=cellWidth;
		if(z_var!=null){
			tempi=metrics.stringWidth("for "+groupName);
			if(tempi<cellWidth) tempi=cellWidth;	
			x0+=rowHeader+hgap;
			g2.drawString("for "+groupName, x0, y0);
			x0+=tempi+hgap;
			g2.drawString("Overall", x0, y0);
			y0+=vgap+ma;
			x0=insets.left;
		}
	
		//render the row headers
		g2.drawString("Minimum:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Lower quartile:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Median:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Upper quartile:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Maximum:",x0,y0);
		y0+=vgap+ma;
		g2.drawString("IQR:",x0,y0);
		y0+=vgap+ma;
		g2.drawString("Range:",x0,y0);
		
		//render the first column for the current group (if the observations are divided
		//into groups
		if(z_var!=null){
			x0+=rowHeader+hgap;
			y0=insets.top+ma+vgap+ma;
			g2.drawString(formatter.format(gmin), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(glq), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gmedian), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(guq),x0,y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gmax),x0,y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gIQR), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gRange),x0,y0);
		}

		//render the second column
		if(z_var!=null){
			x0+=hgap+tempi;
			y0=insets.top+ma+vgap+ma;
		}
		else{
			y0=insets.top+ma;
			x0+=hgap+rowHeader;
		}
		
		g2.drawString(formatter.format(omin), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(olq), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(omedian), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(ouq), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(omax),x0,y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(oIQR),x0,y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(oRange),x0,y0);

	}
		
		
}
