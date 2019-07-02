package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.*;
import java.rmi.*;
import java.util.Vector;

import wvustat.interfaces.*;

/**
*	CIReport is a GUI component that is used to show the confidence interval for the mean.It is
* used in HistogramModule.
*
*	@author: Hengyi Xue
*	@version: 1.0, Jan. 26, 2000
*/

public class CIReport extends Report implements ChangeListener{
	private DataSet data;
	private Variable y_var, z_var;
	private Vector vz;
	private EqualCountPolicy policy;

	private NumberFormat formatter;
	
	private int currentIndex=0;
	private String groupName;
	
	//confidence level
	private double alpha=0.05;
	//statistics for overall population
	
	private double olow,//lower limit for overall 
								ohi;//higher limit for overall
	//statistics for the current group
	private double glow,//lower limit for group
								ghi;//higher limit for group

	//the following variables are used for render the reports in 
	// a table like structure	
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,4,4);
	private Rectangle2D activeAlpha;
	
	private Hypothesis hypo;
	private Variable init_y_var = null;
	
	/**
	* Constructor
	* Creates a new CIReport
	*
	* @param data the DataSet this report is based on
	* @param y_var the y Variable within the data set, c.i. is build for the mean of this var
	* @param z_var the z Variable with the data set, it is used to group observations.
	* @param policy the algorithm used to separate obs into groups.
	*/ 
	public CIReport(DataSet data, Variable y_var, Vector vz, EqualCountPolicy policy){
		this.data=data;
		this.y_var=y_var;
		this.init_y_var = y_var;
		//this.z_var=z_var;
		this.vz = vz;
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
		this.policy=policy;
		
		formatter=NumberFormat.getInstance();
		
		setBackground(Color.white);
		setOpaque(true);
		setToolTipText("Confidence limits");

		initOverallStat();
		initGroupStat();

		metrics=getFontMetrics(basicFont);
		rowHeader=metrics.stringWidth("Lower limit:");
		cellWidth=metrics.stringWidth("0000.0000");
		cellHeight=metrics.getHeight();
		
		int w,h;
		//how many rows of text we have in the report
		int rowNum=3;
		h=insets.top+insets.bottom+rowNum*(vgap+cellHeight);
		if(z_var==null)
			w=insets.left+insets.right+rowHeader+hgap+cellWidth;
		else
			w=insets.left+insets.right+rowHeader+2*hgap+2*cellWidth;
			
		setSize(new Dimension(w,h));
		setPreferredSize(new Dimension(w,h));
		setMinimumSize(new Dimension(w,h));
		
		addMouseListener(new ClickHandler());
		
	}
	
	/**
	* Set the confidence level used in the report
	*/	
	public void setAlpha(double a){
		if(a<=0.0001 || a>=0.9999 ) return;
		
		alpha=1-a;
		initOverallStat();
		initGroupStat();
	}
	
	/**
	* Set the hypothesis this report is based on
	*/
	public void setHypothesis(Hypothesis h){
		hypo=h;
		hypo.setLowerLimit(olow);
		hypo.setUpperLimit(ohi);
		
		if(z_var!=null){
			hypo.setLowerLimit(glow);
			hypo.setUpperLimit(ghi);
		}
	}
	
	/**
	* If the hypothesis changes, this method will be envoked.
	*/
	public void stateChanged(ChangeEvent e){
		repaint();
	}
	
	/**
	* Initialize all overall statistics for the data set.
	*/
	private void initOverallStat(){
		try{
			double[] yvals=y_var.getNumValues();
			
			Vector v=new Vector();
			for(int i=0;i<yvals.length;i++){
				if(!data.getMask(i))
					v.addElement(new Double(yvals[i]));
			}

			int n=v.size();

			StatEngine se=new StatEngineImpl();

			Distribution d=se.getDistribution("t", new double[]{n-1});
			
			double sd=se.getStdDev(v)/Math.sqrt(n);
			double mu=se.getMean(v);
			double qt=d.quantile(alpha/2.0);
			olow=mu+qt*sd;		
			ohi=mu-qt*sd;
			
			if(hypo!=null){
				hypo.setLowerLimit(olow);
				hypo.setUpperLimit(ohi);
			}
		}
		catch(RemoteException re){
			re.printStackTrace();
		}
	}
	
	/**
	* Initialize statistics for the current group
	*/
	private void initGroupStat(){

		if(z_var==null) return;
		
		try{
			double[] yvals=y_var.getNumValues();
			
			Vector zvals=z_var.getValues();
		
			Vector vec=new Vector();
	
			for(int i=0;i<yvals.length;i++){
				if(!data.getMask(i) &&policy.getGroupIndex(i).contains(new Integer(currentIndex))){
					vec.add(new Double(yvals[i]));
				}
			}
			
			StatEngine se=new StatEngineImpl();
			

			int n=vec.size();
			if(n<=1){
				glow=0;
				ghi=0;
			}
			else{
			
				Distribution d=se.getDistribution("t", new double[]{n-1});
				
				double sd=se.getStdDev(vec)/Math.sqrt(n);
				double mu=se.getMean(vec);

				double qt=d.quantile(alpha/2.0);

				glow=mu+qt*sd;
			
				ghi=mu-qt*sd;
				
				if(hypo!=null){
					hypo.setLowerLimit(glow);
					hypo.setUpperLimit(ghi);
				}

			}
			
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
	
	/**
	* Set the current group to the indexed group
	*/
	public void setGroup(int index){
		if(z_var==null) return;
		
		currentIndex=index;
		initGroupStat();
		repaint();
	}
	
	/**
	* update this report to reflect changes.
	*/
	public void updateReport(){
		
		if(data.getTransformedVar(init_y_var.getName()) != null)
	        y_var = data.getTransformedVar(init_y_var.getName());
		else
	        y_var = init_y_var;
		
		initOverallStat();
		initGroupStat();
		repaint();
	}
	
	/**
	* Render this report
	*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D)g;
				
		formatter.setMaximumFractionDigits(3);
		g2.setFont(basicFont);
		
		//get the position where we should start to render text
		int ma=metrics.getMaxAscent();
		int x0=insets.left, y0=insets.top+ma;
		
		//render the "Level=##%" item
		g2.drawString("Level= ", x0, y0);
		
		x0+=metrics.stringWidth("Level= ");
		g2.setColor(Color.blue);
		NumberFormat nf=NumberFormat.getPercentInstance();
		g2.drawString(nf.format(1-alpha), x0, y0);
		g2.setColor(Color.black);
		//define the active region for "mu"
		activeAlpha=new Rectangle2D.Float(x0,y0-ma, metrics.stringWidth(nf.format(1-alpha)), cellHeight);
		x0=insets.left;
		y0+=vgap+ma;
		
		//optionally render the column headers
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
		g2.drawString("Lower limit:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("Upper limit:", x0, y0);
		
		//render the first column for the current group (if the observations are divided
		//into groups
		if(z_var!=null){
			x0+=rowHeader+hgap;
			y0=insets.top+3*ma+2*vgap;
			g2.drawString(formatter.format(glow), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(ghi), x0, y0);
		}

		//render the second column
		if(z_var!=null){
			x0+=hgap+tempi;
			y0=insets.top+3*ma+2*vgap;
		}
		else{
			y0=insets.top+2*ma+vgap;
			x0+=hgap+rowHeader;
		}
		
		g2.drawString(formatter.format(olow), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(ohi), x0, y0);
	
		
	}
	
	/**
	* Inner class ClickHander enables a user to click on the confidence level and change it in 
	* the pop up dialog window.
	*/
	class ClickHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent me){
			if(activeAlpha.contains(me.getPoint())){
				String input=JOptionPane.showInputDialog(CIReport.this, "Confidence level: (0.xx)", "C.I.", JOptionPane.QUESTION_MESSAGE);
				if(input==null) return;
				
				try{
					double tmp=Double.parseDouble(input);
					setAlpha(tmp);
				}
				catch(Exception e){
					return;	
				}
			}
		}
	}
		
}
