package wvustat.modules;

import java.awt.*;

import javax.swing.Icon;

public class CharIcon implements Icon {
	private char ch;
	private int w=16,h=16;
	
	public CharIcon(char c) {
		this.ch = c;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.black);
		g2.setFont(new Font("Serif",Font.BOLD,16));
		g2.drawString(Character.toString(ch), x+4, y+h-3);
	}
	
	public int getIconWidth(){
		return w;
	}
	
	public int getIconHeight(){
		return h;
	}

}
