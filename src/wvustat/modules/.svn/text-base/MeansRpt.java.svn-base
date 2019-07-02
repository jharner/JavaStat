package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.*;
import java.text.NumberFormat;

import wvustat.interfaces.*;

/**
*	MeansRpt is a subcomponent in the KSampleModule. It displays the mean and standard error
* for each group separately.
*
*	@author: Hengyi Xue
*	@version: 1.0, Mar. 1, 2000
*/
public class MeansRpt extends Report implements ChangeListener{
	private SummaryStat stat;

	private NumberFormat formatter;
	
	private int currentIndex=0;
	private String groupName;
	
	//the following variables are used for render the reports in 
	// a table like structure	
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,4,4);
	
	/**
	* Constructor
	* Creates a new MeansRpt based on the given SummaryStat object
	* 
	*/
	public MeansRpt(SummaryStat stat){
		this.stat=stat;
		
		stat.addChangeListener(this);
		
		formatter=NumberFormat.getInstance();
		
		setBackground(Color.white);
		setToolTipText("Effect Test");

		metrics=getFontMetrics(basicFont);
		
		String longest=" ";
		for(int i=0;i<stat.getGroupCount();i++){
			String gname=stat.getGroupName(i);
			if(gname.length()>longest.length())
				longest=gname;
		}
		
		rowHeader=metrics.stringWidth(longest);
		cellWidth=metrics.stringWidth("0,000,000.000");
		cellHeight=metrics.getHeight();
		
		int w,h;
		//how many rows of text we have in the report
		int rowNum=stat.getGroupCount()+1;

		h=insets.top+insets.bottom+rowNum*(vgap+cellHeight)-vgap;
		w=insets.left+insets.right+rowHeader+3*hgap+3*cellWidth;
			
		setSize(new Dimension(w,h));
		setPreferredSize(new Dimension(w,h));
		
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D)g;
		
		g2.setFont(basicFont);
		
		formatter.setMaximumFractionDigits(3);
		
		//get the position where we should start to render text
		int ma=metrics.getMaxAscent();
		int startx=insets.left, starty=insets.top+ma;
		int x0=startx, y0=starty;
		
		String tmpstr=stat.getName();
		if(tmpstr!=null){
			g2.drawString(tmpstr, x0, y0);
			y0+=vgap+ma;
			starty=y0;
		}
		
		//This is the first row
		x0=startx+hgap+rowHeader;
		g2.drawString("n", x0, y0);
		x0+=hgap+hgap;
		
		g2.drawString("Mean",x0, y0);
		x0+=hgap+cellWidth;
		
		g2.drawString("Std. Err.", x0, y0);
		

		y0+=vgap+ma;
		for(int i=0;i<stat.getGroupCount();i++){
			
			x0=startx;
			g2.drawString(stat.getGroupName(i), x0, y0);
			
			x0+=hgap+rowHeader;
			g2.drawString(String.valueOf(stat.getGroupSize(i)), x0, y0);
			
			x0+=hgap+hgap;
			formatter.setMaximumFractionDigits(3);
			g2.drawString(formatter.format(stat.getMean(i)), x0, y0);
			
			x0+=hgap+cellWidth;
			double tmp=stat.getStdDev(i)/Math.sqrt(stat.getGroupSize(i));
			g2.drawString(formatter.format(tmp), x0, y0);
			
			y0+=vgap+ma;
		}
	}
	
	public void updateReport(){
		repaint();
	}
	
	/**
	* Change the current group to the indexed group
	*/
	public void setGroup(int index){
	}
	
	public void stateChanged(ChangeEvent ce){
		updateReport();
	}
		
}
