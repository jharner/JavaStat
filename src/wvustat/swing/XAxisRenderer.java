package wvustat.swing;

import java.awt.*;
import java.text.*;
import javax.swing.*;
import wvustat.modules.*;


public class XAxisRenderer extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private BaseAxisModel xAxisModel;
	private Dimension dim;
	private Insets insets;
	private int width, height, leftGap, rightGap;
	private String label;
	
	
	public XAxisRenderer(BaseAxisModel model, Dimension d, Insets in) {
		this.xAxisModel = model;
		this.dim = d;
		this.insets = in;
		init();
	}
	
	protected void init() {
		width = dim.getSize().width - insets.left - insets.right + (leftGap + rightGap);
		height = insets.bottom;
		setSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
	}
	
	public void setGap(int w) {
		leftGap = w;
		rightGap = w;
		init();
	}
	
	public void setLeftGap(int w) {
		leftGap = w;
		init();
	}
	
	public void setRightGap(int w) {
		rightGap = w;
		init();
	}
	
	public int getLeftGap() {
		return leftGap;
	}
	
	public int getRightGap() {
		return rightGap;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
		
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		NumberFormat formatter = NumberFormat.getInstance();
		g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        FontMetrics metrics = getFontMetrics(getFont());
		
		g2.drawLine(0, 0, width - 1, 0);
		
		double tmp = xAxisModel.getStartValue();
		double step = (width - (leftGap + rightGap)) * 1.0 / xAxisModel.getNumOfIntervals(); 
		double minorStep = step/(xAxisModel.getNumOfMinorTicks()+1);
		int i = 0;
		
		while (tmp <= xAxisModel.getEndValue()) {
			int pos = (int)Math.round(leftGap + i * step);
			g2.drawLine(pos, 0, pos, 4);
			
			if (pos == 0)
				g2.drawString(formatter.format(tmp), 0, 15);
			else 
				g2.drawString(formatter.format(tmp), pos - metrics.stringWidth(formatter.format(tmp)) / 2, 15);
			
			int pos2 = (int)Math.round(pos + minorStep);
			int j = 1;
			while (j <= xAxisModel.getNumOfMinorTicks() && pos2 < width - rightGap) {
				g2.drawLine(pos2, 0, pos2, 4);
				pos2 = (int)Math.round(pos2 + minorStep);
				j++;
			}
			
			i++;
			tmp += xAxisModel.getInterval();
		}
		
		g2.drawString(label, (width - metrics.stringWidth(label)) / 2, 32);
	}

}
