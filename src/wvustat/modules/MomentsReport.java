package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import java.rmi.*;
import java.text.NumberFormat;
import java.util.*;

import wvustat.interfaces.*;

/**
* MomentsReport displays moments for each group of data. It is used in HistogramModule.
*
* @author: Hengyi Xue
* @version: 1.2, Mar. 14, 2000
*/
public class MomentsReport extends Report{
	private DataSet data;
	private Variable y_var, z_var;
	private Vector vz;
	private EqualCountPolicy policy;
	private NumberFormat formatter;
	//observation count, mean, std. dev. and std. err. for each group
	private double gmean, gdev, gerr, gskew;
	//observation count, mean, std. dev. and std. err for the overall population
	private double omean, odev, oerr, oskew;
	private int on, gn;
	
	private int currentIndex=0;
	private String groupName;
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,8,8);
	private Variable init_y_var = null;
	
	/**
	* Constructor
	* Creates a new MomentsReport with given data set and variables.
	*/
	public MomentsReport(DataSet data, Variable y_var, Vector vz, EqualCountPolicy policy){
		this.data=data;
		this.y_var=y_var;
		this.init_y_var = y_var;
		this.vz = vz;
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
		this.policy=policy;
	
		setBackground(Color.white);
		setToolTipText("Moments");
	
		formatter=NumberFormat.getInstance();

		initOverallStat();
		initGroupStat();
		
		metrics=getFontMetrics(basicFont);
		rowHeader=metrics.stringWidth("Std Dev:");
		cellWidth=metrics.stringWidth("0000.000");
		cellHeight=metrics.getMaxAscent();
		
		int w,h;
		if(z_var==null){	
			h=insets.top+insets.bottom+5*(vgap+cellHeight)-vgap;

			w=insets.left+insets.right+rowHeader+hgap+cellWidth;
		}
		else{
			h=insets.top+insets.bottom+6*(vgap+cellHeight)-vgap;

			w=insets.left+insets.right+rowHeader+2*hgap+2*cellWidth;
		}
			
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
	
			on=v.size();
			
			StatEngine se=new StatEngineImpl();
			
			double[] quantiles=se.getQuantiles(v);
			double median=quantiles[2];

			omean=se.getMean(v);
			odev=se.getStdDev(v);
			oerr=odev/Math.sqrt(on);
			oskew=3*(omean-median)/odev;
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
			
			gn=vec.size();
			StatEngine se=new StatEngineImpl();
	
			double[] quantiles=se.getQuantiles(vec);
			double median=quantiles[2];
			
			gmean=se.getMean(vec);
			gdev=se.getStdDev(vec);
			gerr=gdev/Math.sqrt(gn);
			gskew=3*(gmean-median)/gdev;
	
			/*if(z_var.getType()==Variable.NUMERIC){
				
				double d1=policy.getLowerLimit(currentIndex);
				double d2=policy.getUpperLimit(currentIndex);

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
	
	private String getText(){

		String src="<html>\n";
		
		formatter.setMaximumFractionDigits(3);


		if(z_var==null){
			//first cell first row
			src+="<table><tr height=20><td width=\"40%\"><font size=2>N:</font></td>";

			//second cell first row
			src+="<td width=60%>"+String.valueOf(on)+"</td></tr>";
			
			//first cell second row
			src+="<tr height=20><td width=40%><font size=2>Mean:</font></td>";
			//second cell second row
			src+="<td width=60%>"+formatter.format(omean)+"</td></tr>";
			
			//first cell third row
			src+="<tr height=20><td width=40%><font size=2>Std Err:</font></td>";
			//second cell third row
			src+="<td width=60%>"+formatter.format(oerr)+"</td></tr>";
			
			//first cell fourth row
			src+="<tr height=20><td width=40%><font size=2>Std Dev:</font></td>";
			//second cell fourth row
			src+="<td width=60%>"+formatter.format(odev)+"</td></tr></table>";
		}
		else{
			//first cell first row
			src+="<table><tr height=20><td width=\"30%\"></td>";
			//second cell first row
			src+="<td width=40%>for "+groupName+" </td>";
			//third cell first row
			src+="<td width=40%>Overall</td></tr>";
			
			//first cell second row
			src+="<tr><td width=30%>N:</td>";
			//second cell second row
			src+="<td width=40%>"+String.valueOf(gn)+"</td>";
			//third cell second row
			src+="<td width=40%>"+String.valueOf(on)+"</td></tr>";


			//first cell third row
			src+="<tr height=20><td width=30%>Mean:</td>";
			//second cell third row
			src+="<td width=40%>"+formatter.format(gmean)+"</td>";
			//third cell third row
			src+="<td width=40%>"+formatter.format(omean)+"</td></tr>";
			
			//first cell fourth row
			src+="<tr height=20><td width=30%>Std Err:</td>";
			//second cell fourth row
			src+="<td width=40%>"+formatter.format(gerr)+"</td>";
			//third cell fourth row
			src+="<td width=40%>"+formatter.format(oerr)+"</td></tr>";
			
			//first cell fifth row
			src+="<tr height=20><td width=30%>Std Dev:</td>";
			//second cell fifth row
			src+="<td width=40%>"+formatter.format(gdev)+"</td>";
			//third cell fifth row
			src+="<td width=40%>"+formatter.format(odev)+"</td></tr></table>";
		}
			
			
		src+="\n</html>\n";
		
		return src;
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
		g2.drawString("N:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Mean:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Std Dev:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Std Err:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Skewness:",x0,y0);
		
		//render the first column for the current group (if the observations are divided
		//into groups
		if(z_var!=null){
			x0+=rowHeader+hgap;
			y0=insets.top+ma+vgap+ma;
			g2.drawString(formatter.format(gn), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gmean), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gdev), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gerr),x0,y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gskew),x0,y0);
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
		
		g2.drawString(formatter.format(on), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(omean), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(odev), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(oerr), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(oskew),x0,y0);

	}
		
		
}
		
				
		
		
