package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.*;
import java.text.NumberFormat;

import wvustat.interfaces.*;

/**
*	EffectTestRpt is a subcomponent in the KSampleModule. It displays the test statistics and p value
* of the Effect test.
*
*	@author: Hengyi Xue
*	@version: 1.0, Mar. 1, 2000
*/

public class EffectTestRpt extends Report implements ChangeListener{
	private SummaryStat stat;

	private NumberFormat formatter;
	
	private int currentIndex=0;
	private String groupName;
	
	//confidence level
	private double alpha=0.05;

	//the following variables are used for render the reports in 
	// a table like structure	
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,4,4);
	
	private double test_val=0, pval=0;
	private int errorDf=0;

	/**
	* Constructor
	* Creates a new EffectTestRpt based on the SummaryStat object
	*
	* @param stat the SummaryStat object which contains summary statistics for the data set
	*/
	public EffectTestRpt(SummaryStat stat){
		this.stat=stat;
		
		stat.addChangeListener(this);
		
		formatter=NumberFormat.getInstance();
		
		setBackground(Color.white);
		setToolTipText("Effect Test");

		metrics=getFontMetrics(basicFont);
		rowHeader=metrics.stringWidth("Error df");
		cellWidth=metrics.stringWidth("0000.0000");
		cellHeight=metrics.getHeight();
		
		int w,h;
		//how many rows of text we have in the report
		int rowNum=3;

		h=insets.top+insets.bottom+rowNum*(vgap+cellHeight)-vgap;
		w=insets.left+insets.right+rowHeader+hgap+cellWidth;
			
		setSize(new Dimension(w,h));
		setPreferredSize(new Dimension(w,h));
		
		init();
	}
	
	/**
	* Initialize the component
	*/
	private void init(){
		if(stat.getGroupCount()==2){
			double s0=stat.getStdDev(0);
			int n0=stat.getGroupSize(0);
			double m0=stat.getMean(0);
			
			double s1=stat.getStdDev(1);
			int n1=stat.getGroupSize(1);
			double m1=stat.getMean(1);
			
			double ss=Math.pow(s0,2)*(n0-1)+Math.pow(s1,2)*(n1-1);
			ss=ss/(n0+n1-2)*(1.0/n0+1.0/n1);
			
			test_val=(m0-m1)/Math.sqrt(ss);
			
			StatEngine se=new StatEngineImpl();
			try{
				Distribution t=se.getDistribution("t", new double[]{n0+n1-2});
				errorDf=n0+n1-2;
			
				pval=t.cdf(test_val);
				if(pval>0.5)
					pval=2*(1-pval);
				else
					pval=2*pval;
			}
			catch(RemoteException re){}
		}
		else{
			double mu=stat.getOverallMean();
			double amongSS=0, errorSS=0;
			int N=0;
			
			int k=stat.getGroupCount();
			for(int i=0;i<k;i++){
				int n=stat.getGroupSize(i);
				amongSS+=n*Math.pow(stat.getMean(i)-mu, 2);
				errorSS+=(n-1)*Math.pow(stat.getStdDev(i), 2);
				N+=n;
			}
			
			int df1=k-1;
			int df2=N-k;
			
			errorDf=df2;
			test_val=(amongSS/df1)/(errorSS/df2);
			
			StatEngine se=new StatEngineImpl();
			try{
				Distribution F=se.getDistribution("f", new double[]{df1, df2});
			
				pval=1-F.cdf(test_val); 
			}
			catch(RemoteException re){}
			
		}	
		
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
		
		//This is the first column
		if(stat.getGroupCount()==2)
			g2.drawString("t-value", x0, y0);
		else
			g2.drawString("F value", x0, y0);
		
		y0+=vgap+ma;
		
		g2.drawString("P value", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString("Error df", x0, y0);
		
		//this is the second column
		x0+=hgap+rowHeader;
		y0=starty;
		g2.drawString(formatter.format(test_val), x0, y0);
		y0+=vgap+ma;
		
		formatter.setMaximumFractionDigits(6);
		g2.drawString(formatter.format(pval), x0, y0);
		y0+=vgap+ma;
		
		g2.drawString(String.valueOf(errorDf), x0, y0);
	}
	
	/**
	* update this report
	*/
	public void updateReport(){
		init();
		repaint();
	}
	
	/**
	* set current group to be the indexed group
	*/
	public void setGroup(int index){
	}
	
	public void stateChanged(ChangeEvent ce){
		updateReport();
	}
		
}
