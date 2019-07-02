package wvustat.simulation.fivestep;

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;

/**
*	This class implements an icon that can be used to indicate the role of a variable
*	in a dataset.
*/ 

public class NumberIcon implements Icon{
	private int w=32, h=32;
	private double num;
	
	public NumberIcon(double num){
		this.num=num;
		
		if(String.valueOf(num).length()>2){
			w=30+(String.valueOf(num).length()-2)*10;
			h=w;
		}
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2=(Graphics2D)g;

		g2.setColor(c.getForeground());
		g2.setStroke(new BasicStroke(2.0f));
		
		g2.drawOval(1,1,w-2,h-2);
		Font font=c.getFont();
		FontMetrics fm=c.getFontMetrics(font);
		g2.setFont(font);
		
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		
		String label;
		if(isWholeNumber(num)){
			label=String.valueOf((int)num);
		}
		else{
			label=nf.format(num);
		}
		
		g2.drawString(label, (w-fm.stringWidth(label))/2, h-(h-fm.getAscent())/2);
			
		
	}
	
	private boolean isWholeNumber(double val){
		return val==(int)val;
	}
	
	public int getIconWidth(){
		return w;
	}
	
	public int getIconHeight(){
		return h;
	}
}
