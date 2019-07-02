package wvustat.table;

import java.awt.*;
import javax.swing.*;

import wvustat.interfaces.*;
/**
*	This class implements an icon that can be used to indicate the role of a variable
*	in a dataset.
*/ 

public class RoleIcon implements Icon{
	private int w=12, h=12;
	private int role;
	private int type=Variable.NUMERIC;
	
	public RoleIcon(int role){
		this.role=role;
	}
	
	public RoleIcon(int role, int type){
		this.role=role;
		this.type=type;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2=(Graphics2D)g;
		
		x = x + 2; //gap on left side of icon
		
        if(role==DataSet.L_ROLE){
            g2.setColor(Color.red);
            //g2.setFont(new Font("Dialog", Font.BOLD, 12));
            g2.drawString("L", x+1, y+10);
            //g2.drawOval(x+1,y+1,w-2,h-2);
            return;
         }
         
         if(role==DataSet.F_ROLE){
            g2.setColor(Color.red);
            //g2.setFont(new Font("Dialog", Font.BOLD, 12));
            g2.drawString("F", x+1, y+10);
            //g2.drawOval(x+1,y+1,w-2,h-2);
            return;
         }
                
		BasicStroke dashed = new BasicStroke(1.0f, 
                                   BasicStroke.CAP_BUTT, 
                                   BasicStroke.JOIN_MITER, 
                                   10.0f, new float[]{2.5f}, 0.0f);
		
		if(type==Variable.CATEGORICAL)
			g2.setStroke(dashed);

		g2.setColor(Color.black);
			
		int x0=x+(int)(w/3.0);
		int y0=y+(int)(2*h/3.0);
		//draw x axis
		g2.drawLine(x0,y0,x+w,y0);
		//draw y axis
		g2.drawLine(x0,y,x0,y0);
		//draw z axis
		g2.drawLine(x,y+h, x0, y0);
		
		g2.setColor(Color.red);
		switch(role){
			case DataSet.X_ROLE:
				g2.drawLine(x0,y0,x+w, y0);
				break;
			case DataSet.Y_ROLE:
				g2.drawLine(x0,y, x0, y0);
				break;
			case DataSet.Z_ROLE:
				g2.drawLine(x,y+h, x0, y0);
				break;
                                
		}
	}
	
	public int getIconWidth(){
		return w + 2;
	}
	
	public int getIconHeight(){
		return h;
	}
	
	public static Rectangle getXAxisArea(){
		return new Rectangle(7, 9, 7, 3);
	}
	
	public static Rectangle getYAxisArea(){
		return new Rectangle(6, 2, 3, 7);
	}
	
	public static Rectangle getZAxisArea(){
		return new Rectangle(0, 10, 7, 7);
	}
	
	public static Rectangle getArea(){
		return new Rectangle(0, 2, 15, 15);
	}
}
