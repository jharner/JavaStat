package wvustat.modules.genome;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import javax.swing.*;
import javax.swing.event.*;
import java.text.NumberFormat;

import wvustat.network.MTP;

/**
*	MTPRpt is a subcomponent in the MTPModule. It displays the summary information of an MTP object
*
*	@author: Dajie Luo
*	@version: 1.0, Jun. 29, 2007
*/

public class MTPRpt extends JPanel implements ChangeListener{
	private static final long serialVersionUID = 1L;

	private MTP mtp;

	private NumberFormat formatter;
	
	//the following variables are used for render the reports in 
	//a table like structure	
	private int vgap=4, hgap=12, rowHeader, cellWidth, cellHeight;
	private Font basicFont=new Font("Monospaced", Font.PLAIN, 11);
	private FontMetrics metrics;
	private Insets insets=new Insets(8,8,4,4);
	private Rectangle2D activeAlpha;
	
	/**
	* Constructor
	* Creates a new MTPRpt based on the object MTP
	*
	* @param mtp - the MTP object which contains summary statistics
	*/
	public MTPRpt(MTP mtp){
		this.mtp=mtp;
		
		formatter=NumberFormat.getInstance();
		
		setBackground(Color.white);
		setToolTipText("Summary");

		metrics=getFontMetrics(basicFont);
		rowHeader=metrics.stringWidth("Multiple Test Method");
		cellWidth=metrics.stringWidth(mtp.getExprObjName());
		cellHeight=metrics.getHeight();
		
		int w,h;
		//how many rows of text we have in the report
		int rowNum=6;

		h=insets.top+insets.bottom+rowNum*(vgap+cellHeight)-vgap;
		w=insets.left+insets.right+rowHeader+hgap+cellWidth;
			
		setSize(new Dimension(w,h));
		setPreferredSize(new Dimension(w,h));
		
		addMouseListener(new ClickHandler());
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
		
		
		//This is the first column
		g2.drawString("Expression Object", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString("alpha", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString("Number of Hypotheses", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString("Number of Sample", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString("Type I Error Rate", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString("Multiple Test Method", x0, y0);
		y0+=vgap+ma;
		
		//this is the second column
		x0+=hgap+rowHeader;
		y0=starty;
		
		g2.drawString(mtp.getExprObjName(), x0, y0);
		y0+=vgap+ma;
		
		g2.setColor(Color.blue);
		g2.drawString(formatter.format(mtp.getAlpha()), x0, y0);
		g2.setColor(Color.black);
		activeAlpha = new Rectangle2D.Float(x0,y0-ma, metrics.stringWidth(formatter.format(mtp.getAlpha())), cellHeight);
		y0+=vgap+ma;
		
		g2.drawString(mtp.getNumHypo() + " ( " + mtp.getNumRejection() + " rejected )", x0, y0);
		y0+=vgap+ma;
		
		g2.drawString(String.valueOf(mtp.getNumSample()), x0, y0);
		y0+=vgap+ma;
		
		g2.drawString(mtp.getTypeone(), x0, y0);
		y0+=vgap+ma;
		
		g2.drawString(mtp.getMethod(), x0, y0);
		y0+=vgap+ma;
	}
	
	
	public void stateChanged(ChangeEvent ce){
		repaint();
	}
	
	class ClickHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent me){
			if(activeAlpha.contains(me.getPoint())){
				String input=JOptionPane.showInputDialog(MTPRpt.this, "Please input the new alpha:", "alpha input", JOptionPane.QUESTION_MESSAGE);
				if(input==null) return;
				
				try{
					double tmp=Double.parseDouble(input);
					mtp.setNull(tmp);
				}
				catch(NumberFormatException e){
					return;	
				}
			}
		}
	}
		
}
