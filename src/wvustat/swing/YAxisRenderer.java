package wvustat.swing;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import wvustat.modules.*;


public class YAxisRenderer extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private BaseAxisModel yAxisModel;
	private Dimension dim;
	private Insets insets;
	private int width, height, topGap, rightGap;
	private FontMetrics metrics;
	
	public YAxisRenderer(BaseAxisModel model, Dimension d, Insets in) {
		this.yAxisModel = model;
		this.dim = d;
		this.insets = in;
		this.metrics = getFontMetrics(new Font("Monospaced", Font.PLAIN, 11));
		init();
	}
	
	protected void init() {
		width = insets.left - rightGap;
		height = dim.getSize().height - insets.top - insets.bottom + topGap + metrics.getHeight();
		setSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
	}
	
	public void setGap(int w) {
		topGap = w;
		rightGap = w;
		init();
	}
	
	public void setTopGap(int w) {
		topGap = w;
		init();
	}
	
	public void setRightGap(int w) {
		rightGap = w;
		init();
	}
	
	public int getTopGap() {
		return topGap;
	}
	
	public int getRightGap() {
		return rightGap;
	}
		
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		NumberFormat formatter = NumberFormat.getInstance();
		g2.setFont(metrics.getFont());
		//g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        //FontMetrics metrics = getFontMetrics(getFont());
		
		g2.drawLine(width - 1, 0, width - 1, height - 1 - metrics.getHeight());
		
		double tmp = yAxisModel.getStartValue();
		double step = (height - topGap - metrics.getHeight()) * 1.0 / yAxisModel.getNumOfIntervals(); 
		double minorStep = step/(yAxisModel.getNumOfMinorTicks()+1);
		int i = 0;
		
		while (tmp <= yAxisModel.getEndValue()) {
			int pos = (int)Math.round(height - 1 - metrics.getHeight() - i * step);
			g2.drawLine(width - 5, pos, width - 1, pos);
			g2.drawString(formatter.format(tmp), width - 7 - metrics.stringWidth(formatter.format(tmp)), pos + metrics.getHeight() / 2);

			int pos2 = (int)Math.round(pos - minorStep);
			int j = 1;
			while (j <= yAxisModel.getNumOfMinorTicks() && pos2 > 0) {
				g2.drawLine(width - 5, pos2, width - 1, pos2);
				pos2= (int)Math.round(pos2 - minorStep);
				j++;
			}
			
			i++;
			tmp += yAxisModel.getInterval();
		}
		
	}

}
