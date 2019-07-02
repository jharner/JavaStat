package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.rmi.*;
import java.text.*;

import wvustat.interfaces.*;

/**
 * Chi2Report displays statistics for a contigency table, including
 * degreee of freedom, chi square and p-value.
 *
 * @author: Hengyi Xue
 * @version: 1.0, May 10, 2000
 */

public class Chi2Report extends Report{
    
    private NumberFormat formatter;

    private MosaicPlot plot;

    private int currentIndex=0;
    private String[] groupName;
	
  
    //the following variables are used for render the reports in 
    // a table like structure	
    private int vgap=4, hgap=12,rowHeader, cellWidth, cellHeight;
    private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
    private FontMetrics metrics;
    private Insets insets=new Insets(8,20,4,4);
    
    private double pValue=0;
  
 
    /**
     * Constructor
     */
    public Chi2Report(MosaicPlot plot, String[] list){
	this.plot=plot;
	this.groupName=list;

	formatter=NumberFormat.getInstance();
		
	setBackground(Color.white);
	setBorder(new LineBorder(Color.black));

	metrics=getFontMetrics(basicFont);
	rowHeader=metrics.stringWidth("Chi Square");
	cellWidth=metrics.stringWidth("0000.0000");
	cellHeight=metrics.getHeight();
		
	int w,h;
	//how many rows of text we have in the report
	
	int rowNum=3;
	if(groupName!=null && groupName.length>0)
	    rowNum=4;

	h=insets.top+insets.bottom+rowNum*(vgap+cellHeight)-vgap;
	w=insets.left+insets.right+rowHeader+hgap+cellWidth;
	
	setSize(new Dimension(w,h));
	setPreferredSize(new Dimension(w,h));
	
	init();
    }

    public void init(){

	StatEngine se=new StatEngineImpl();

	try{
	    Distribution d=se.getDistribution("chi square", new double[]{plot.getDf()});
	    
	    pValue=1-d.cdf(plot.getChiSquare());
	}
	catch(RemoteException re){
	    re.printStackTrace();
	}
    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);

	Graphics2D g2=(Graphics2D)g;

	g2.setFont(basicFont);
		
	//get the position where we should start to render text
	int ma=metrics.getMaxAscent();
	int startx=insets.left, starty=insets.top+ma;
	int x0=startx, y0=starty;
		
	if(groupName!=null && groupName.length>0){
	    g2.drawString("For "+groupName[currentIndex], x0, y0);
	    y0+=vgap+ma;
	    starty=y0;
	}
		
	//This is the first column
	g2.drawString("Chi Square", x0, y0);
	y0+=vgap+ma;
		
	g2.drawString("df", x0, y0);
	y0+=vgap+ma;
        
        g2.drawString("P value",x0,y0);
	y0+=vgap+ma;
		
	//this is the second column
	x0+=hgap+rowHeader;
	y0=starty;
		
	formatter.setMaximumFractionDigits(3);
	g2.drawString(formatter.format(plot.getChiSquare()), x0, y0);
	y0+=vgap+ma;
		
	formatter.setMaximumFractionDigits(6);
	g2.drawString(String.valueOf(plot.getDf()), x0, y0);
        y0+=vgap+ma;
        
        g2.drawString(formatter.format(pValue), x0, y0);
	y0+=vgap+ma;
    }

    public void updateReport(){
	init();
	repaint();
    }

    public void setGroup(int index){
	currentIndex=index;
	init();
	repaint();
    }
	
}
	

