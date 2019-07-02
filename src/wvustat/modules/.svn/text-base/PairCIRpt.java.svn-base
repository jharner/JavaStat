package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.rmi.*;
import java.text.NumberFormat;

import wvustat.interfaces.*;

/**
*	PairCIRpt is a subcomponent in the KSampleModule. It displays the confidence limits for
* mean difference between a group and a "base" group. The base group can be changed by mouse 
* input. If a use clicks on a group label that is not currently the "base" group, that group
* will become the base group and all statistics will be re-calculated.
*
*	@author: Hengyi Xue
*	@version: 1.0, Mar. 1, 2000
*/
public class PairCIRpt extends Report implements ChangeListener{
	private SummaryStat stat;

	private NumberFormat formatter;
	
	private String groupName;
	
	//the following variables are used for render the reports in 
	// a table like structure	
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,4,4);
	
	//the group that is used as comparison base
	private int baseIndex=0;
	
	private double[] lcl, ucl;
	
	private double alpha=0.05;
	
	//The region that contains the alpha setting
	private Rectangle2D alphaRect;
	
	//The position where the base group is rendered
	private int baseX, baseY;
	
	/**
	* Constructor
	* Creates a new PairCIRpt object using the SummaryStat supplied
	*/
	public PairCIRpt(SummaryStat stat){
		this.stat=stat;
		
		addMouseListener(new ClickHandler());
		stat.addChangeListener(this);
		
		formatter=NumberFormat.getInstance();
		
		setBackground(Color.white);
		setToolTipText("Pairwise CI");

		metrics=getFontMetrics(basicFont);
		
		String longest="    ";
		for(int i=0;i<stat.getGroupCount();i++){
			String gname=stat.getGroupName(i);
			if(gname.length()>longest.length())
				longest=gname;
		}
		
		longest=longest+" vs";
		rowHeader=metrics.stringWidth(longest);
		cellWidth=metrics.stringWidth("0,000,000.000");
		cellHeight=metrics.getHeight();
		
		int w,h;
		//how many rows of text we have in the report
		int rowNum=stat.getGroupCount()+1;
		if(stat.getName()!=null)
			rowNum++;

		h=insets.top+insets.bottom+rowNum*(vgap+cellHeight)-vgap;
		w=insets.left+insets.right+rowHeader+hgap+2*cellWidth;
			
		setSize(new Dimension(w,h));
		setPreferredSize(new Dimension(w,h));
		
		calcCI();
		
	}
	
	/**
	* calculate the confidence limits for pair wise mean difference
	*/
	private void calcCI(){
		int k=stat.getGroupCount();
		if( k == 0 ) return; //added by djluo
		
		lcl=new double[k];
		ucl=new double[k];
		
		double m0=stat.getMean(baseIndex);
		double s0=stat.getStdDev(baseIndex);
		int n0=stat.getGroupSize(baseIndex);
		
		for(int i=0;i<k;i++){
			if(i!=baseIndex){
				double m1=stat.getMean(i);
				double s1=stat.getStdDev(i);
				int n1=stat.getGroupSize(i);
				
				double ss=Math.pow(s0,2)*(n0-1)+Math.pow(s1,2)*(n1-1);
				ss=ss/(n0+n1-2)*(1.0/n0+1.0/n1);

				StatEngine se=new StatEngineImpl();
				try{
					Distribution t=se.getDistribution("t", new double[]{n0+n1-2});
					
					double q=t.quantile(alpha/2.0);
					lcl[i]=m0-m1+q*Math.sqrt(ss);
					ucl[i]=m0-m1-q*Math.sqrt(ss);
				}
				catch(RemoteException re){
					lcl[i]=0;
					ucl[i]=0;
				}
			}
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
			g2.drawString("For "+tmpstr, x0, y0);
			y0+=vgap+ma;
			starty=y0;
		}
		
		
		//This is the first row
		tmpstr="Level";
		g2.drawString(tmpstr, x0, y0);
		x0+=metrics.stringWidth(tmpstr)+4;
		
		
		g2.setColor(Color.blue);
		
		tmpstr=formatter.format((1-alpha)*100)+"%";
		
		g2.drawString(tmpstr, x0, y0);
		alphaRect=new Rectangle2D.Float(x0, y0-ma,metrics.stringWidth(tmpstr), ma); 
		
		y0+=vgap+ma;
		x0=startx;
		
		g2.setColor(Color.black);
		tmpstr=stat.getGroupName(baseIndex);
		g2.drawString(tmpstr, x0, y0);
		baseX=x0;
		baseY=y0;
		
		x0+=metrics.stringWidth(tmpstr)+4;
		
		g2.drawString("vs", x0, y0);
		
		x0=startx+hgap+rowHeader;
		g2.drawString("Lower CL", x0, y0);
		x0+=hgap+cellWidth;
		
		g2.drawString("Upper CL",x0, y0);
		x0+=hgap+cellWidth;

		y0+=vgap+ma;
		
		
		for(int i=0;i<stat.getGroupCount();i++){
			if(i!=baseIndex){
			
				g2.setColor(Color.blue);
				x0=startx;
				g2.drawString(stat.getGroupName(i), x0, y0);
			
				g2.setColor(Color.black);
				x0+=hgap+rowHeader;
				g2.drawString(formatter.format(lcl[i]), x0, y0);
			
				x0+=hgap+cellWidth;
				g2.drawString(formatter.format(ucl[i]), x0, y0);
			
				y0+=vgap+ma;
			}
		}
	}
	
	public void updateReport(){
		calcCI();
		repaint();
	}
	
	public void setGroup(int index){
	}
	
	public void stateChanged(ChangeEvent ce){
		updateReport();
	}
	
	class ClickHandler extends MouseAdapter{
		public void mousePressed(MouseEvent me){
			int x=me.getX(), y=me.getY();
			
			if(alphaRect.contains(x,y)){
				String input=JOptionPane.showInputDialog(PairCIRpt.this.getParent(), "New confidence level (0.xx):", "mu input", JOptionPane.QUESTION_MESSAGE);
				if(input==null) return;
				
				try{
					double tmp=Double.parseDouble(input);
					if(tmp>0.1 && tmp<0.9999){
						alpha=1-tmp;
						calcCI();
						repaint();
					}
				}
				catch(Exception e){
					return;	
				}
			}
			else if(x>baseX && (x-baseX)<rowHeader && y >baseY){
				int pos=(y-baseY)/(vgap+metrics.getMaxAscent())+1;
				
				if(pos<=baseIndex){
					baseIndex=pos-1;
					calcCI();
					repaint();
				}
				else{
					baseIndex=pos;
					calcCI();
					repaint();
				}
			}
		}
	}
					

		
}
