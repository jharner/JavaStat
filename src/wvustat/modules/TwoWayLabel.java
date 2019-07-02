package wvustat.modules;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * TwoWayLabel implements a label that can display text both horizontally or
 * vertically. 
 *
 * @author: Hengyi Xue
 * @version: 1.0, May 18, 2000
 */

public class TwoWayLabel extends JComponent{

    private String text;
    private boolean verticalAligned=false;
    private int width, height;
    private Insets insets=new Insets(4,4,4,4);
    private Font font=new Font("Serif", Font.PLAIN, 12);

    private FontMetrics metrics;

    public TwoWayLabel(String text){
	this.text=text;

	metrics=getFontMetrics(font);

	width=insets.left+insets.right+metrics.stringWidth(text);
	height=insets.top+insets.bottom+metrics.getHeight();
    }

    public TwoWayLabel(String text, boolean verticalAligned){
	this.text=text;
	this.verticalAligned=verticalAligned;

	setBackground(Color.white);

	metrics=getFontMetrics(font);

	if(verticalAligned){
	    width=insets.left+insets.right+metrics.getHeight();
	    height=insets.top+insets.bottom+metrics.stringWidth(text);
	}
	else{
	    width=insets.left+insets.right+metrics.stringWidth(text);
	    height=insets.top+insets.bottom+metrics.getHeight();
	}

    }
    
    public void setText(String text){
    	this.text=text;
    	
    	if(verticalAligned){
	    	width=insets.left+insets.right+metrics.getHeight();
	    	height=insets.top+insets.bottom+metrics.stringWidth(text);
		}
		else{
	    	width=insets.left+insets.right+metrics.stringWidth(text);
	   	 	height=insets.top+insets.bottom+metrics.getHeight();
		}
		
		revalidate();
	}
    	

    public Dimension getSize(){
	return new Dimension(width, height);
    }

    public Dimension getPreferredSize(){
		return new Dimension(width, height);
    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);

	Graphics2D g2=(Graphics2D)g;

	g2.setFont(font);

	

	if(verticalAligned){
	    g2.translate(insets.left, insets.top);
	    AffineTransform af=AffineTransform.getRotateInstance(Math.PI/2);
	    g2.transform(af);
	}
	else
	    g2.translate(insets.left, insets.top+metrics.getMaxAscent());

	g2.drawString(text, 0, 0);
    }

    public static void main(String[] args){

	JFrame jf=new JFrame("Label test");

	TwoWayLabel label=new TwoWayLabel("Hello, world", true);
	TwoWayLabel label2=new TwoWayLabel("Hello, earth", false);

	jf.getContentPane().setLayout(new BorderLayout());
	jf.getContentPane().add(label, BorderLayout.WEST);
	jf.getContentPane().add(label2, BorderLayout.CENTER);

	System.out.println(label.getPreferredSize());
	jf.pack();
	jf.show();
    }
}
