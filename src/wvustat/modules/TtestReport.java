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
*	TtestReport is a GUI component that is used to show the results of a t-test.It is used in
* HistogramModule.
*
*	@author: Hengyi Xue
*	@version: 1.0, Jan. 26, 2000
*/
public class TtestReport extends Report implements ChangeListener{
	private DataSet data;
	private Variable y_var, z_var;
	private Vector vz;
	private EqualCountPolicy policy;
	private JEditorPane jep;
	private NumberFormat formatter;
	
	private int currentIndex=0;
	private String groupName;
	
	private double mu;
	//statistics for overall population
	
	private double ot,//t value 
				  op2,//two sided p-value
				  opl,//p-value to the left of t
				  opr;//p-value to the right of t
	//statistics for the current group
	private double gt,gp2, gpl, gpr;

	//the following variables are used for render the reports in 
	// a table like structure	
	private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,4,4);
	private Rectangle2D activeMu;
	
	private Hypothesis hypo;
	private Variable init_y_var = null;

	/**
	* Constructor
	* Creates a new TtestReport with the given data set and its variables.
	*/
	public TtestReport(DataSet data, Variable y_var, Vector vz, EqualCountPolicy policy){
		this.data=data;
		this.y_var=y_var;
		this.init_y_var = y_var;
		this.vz = vz;
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
		this.policy=policy;
		
		formatter=NumberFormat.getInstance();
		
		setBackground(Color.white);
		setOpaque(true);
		setToolTipText("t-test");

		
		
		mu=y_var.getMean();
		
		
		initOverallStat();
		initGroupStat();

		metrics=getFontMetrics(basicFont);
		rowHeader=metrics.stringWidth("P(|t|>t-value):");
		cellWidth=metrics.stringWidth("0000.0000");
		cellHeight=metrics.getMaxAscent();
		
		int w,h;
		h=insets.top+insets.bottom+5*(vgap+cellHeight);
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
	* Set the Hypothesis object for this component
	*/
	public void setHypothesis(Hypothesis h){
		hypo=h;
		hypo.setNull(mu);
	}
	
	public void stateChanged(ChangeEvent e){
		Object obj = e.getSource();
		if (obj instanceof Hypothesis){
			mu = hypo.getNull();
			initOverallStat();
			initGroupStat();
		}
		repaint();
	}
	
	/**
	* Set the null value
	*/
	public void setMu(double m){
		mu=m;
		hypo.setNull(mu);
		initOverallStat();
		initGroupStat();
	}
	
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

			ot=(se.getMean(v)-mu)/se.getStdDev(v)*Math.sqrt(n);
			
			Distribution d=se.getDistribution("t", new double[]{n-1});
			
			opl=d.cdf(ot);
			opr=1-opl;
			
			if(opl>opr)
				op2=2*opr;
			else
				op2=2*opl;
		}
		catch(RemoteException re){
			re.printStackTrace();
		}
	}
	
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
			
			
			int n=vec.size();
			if(n<=1){
				gt=0;
				gpl=0;
				gpr=0;
				gp2=0;
			}
			else{
				StatEngine se=new StatEngineImpl();
				double gmean=se.getMean(vec);
				double gdev=se.getStdDev(vec);

				gt=(gmean-mu)/gdev*Math.sqrt(n);
			
				Distribution d=se.getDistribution("t", new double[]{n-1});
			
				gpl=d.cdf(gt);
				gpr=1-gpl;
			
				if(gpl>gpr)
					gp2=2*gpr;
				else
					gp2=2*gpl;
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
	
	private String getText(){
		String src="<html>\n";
		
		formatter.setMaximumFractionDigits(3);

		//if(z_var==null){
			//first cell first row
			src+="<table><tr height=20><td width=\"40%\"><font size=2>H0: mu=</font></td>";

			//second cell first row
			src+="<td width=60%><a href=\"input_mu\">"+formatter.format(mu)+"</a></td></tr>";
			
			//first cell second row
			src+="<tr height=20><td width=40%><font size=2>t-value</font></td>";
			//second cell second row
			src+="<td width=60%>"+formatter.format(ot)+"</td></tr>";
			
			//first cell third row
			src+="<tr height=20><td width=40%><font size=2>P(|t|>t-valaue)</font></td>";
			//second cell third row
			src+="<td width=60%>"+formatter.format(op2)+"</td></tr>";
			
			//first cell fourth row
			src+="<tr height=20><td width=40%><font size=2>P(t>t-value)</font></td>";
			//second cell fourth row
			src+="<td width=60%>"+formatter.format(opr)+"</td></tr>";
			
			//first cell fifth row
			src+="<tr height=20><td width=40%><font size=2>P(t<t-value)</font></td>";
			src+="<td width=60%>"+formatter.format(opl)+"</td></tr></table>";
		//}

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
		//mu=y_var.getMean();
		//setMu(mu);
		
		
		initOverallStat();
		initGroupStat();
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2=(Graphics2D)g;
				
		formatter.setMaximumFractionDigits(3);
		g2.setFont(basicFont);
		
		//get the position where we should start to render text
		int ma=metrics.getMaxAscent();
		int x0=insets.left, y0=insets.top+ma;
		
		//render the "H0: mu=xxx" item
		g2.drawString("H  mu=", x0, y0);
		
		//render the subscript style "0"
		g2.setFont(new Font("Monospaced", Font.PLAIN, 9));
		g2.drawString("0", x0+metrics.stringWidth("H"), y0+2);
		
		//restore the regular font
		g2.setFont(basicFont);
		
		x0+=metrics.stringWidth("H0 mu= ");
		g2.setColor(Color.blue);
		g2.drawString(formatter.format(hypo.getNull()), x0, y0);
		g2.setColor(Color.black);
		//define the active region for "mu"
		activeMu=new Rectangle2D.Float(x0,y0-ma, metrics.stringWidth(formatter.format(hypo.getNull())), cellHeight);
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
		g2.drawString("t-value:", x0, y0);
		y0+=vgap+ma;
		g2.drawString("P(|t|>t-value)", x0, y0);
		y0+=vgap+ma;
		g2.drawString("P(t>t-value)", x0, y0);
		y0+=vgap+ma;
		g2.drawString("P(t<t-value)", x0, y0);
		
		//render the first column for the current group (if the observations are divided
		//into groups
		if(z_var!=null){
			x0+=rowHeader+hgap;
			y0=insets.top+3*ma+2*vgap;
			g2.drawString(formatter.format(gt), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gp2), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gpr), x0, y0);
			y0+=vgap+ma;
			g2.drawString(formatter.format(gpl),x0,y0);
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
		
		g2.drawString(formatter.format(ot), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(op2), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(opr), x0, y0);
		y0+=vgap+ma;
		g2.drawString(formatter.format(opl), x0, y0);
	
		
	}
	
	class ClickHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent me){
			if(activeMu.contains(me.getPoint())){
				String input=JOptionPane.showInputDialog(TtestReport.this, "Please input the new mu:", "mu input", JOptionPane.QUESTION_MESSAGE);
				if(input==null) return;
				
				try{
					double tmp=Double.parseDouble(input);
					setMu(tmp);
				}
				catch(Exception e){
					return;	
				}
			}
		}
	}
		
}
