package wvustat.simulation.power;

import java.awt.*;
import java.util.*;
import java.applet.Applet;
import javax.swing.*;

public class PowerCanvas extends JPanel {


	private final double PI = 3.1415926;
	private int tickPixel;
	Dimension	dim = new Dimension();

	private int hlMargin = 70;
	private int hrMargin = 35;
	private int vuMargin = 10;
	private int vlMargin = 30;
	
	publicData	pData;
	Power		cApplet;
	
	private double xstrech;
	private double shift;
	private int 	curveSteps = 200;
	private int 	viewSigmas = 3;
	
	private double 	xmin, xmax, ymin, ymax;
	private double	Mu = 0.0;
	private double	Delta;
	private double	Sigma;
	private double	N;
	private double	Alpha;
	
	public boolean firstTimePaint = true;
	public boolean rescaleNeeded = true;
	
 	int recX[] = new int[4];
	int recY[] = new int[4];
	
	tickmarkManager tManager;
	
	PowerCanvas (publicData tmpData, Power tmpApplet) {
		
		super();
		pData = tmpData;
		cApplet = tmpApplet;
		this.setBackground(Color.black);
		tManager = new tickmarkManager(0.999);

	}
	
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		
		PlotCoord pc;
		String tmpString = new String();
		double tailX;
		
		if ( firstTimePaint ) {
			firstTimePaint = false;
			Delta = 1.0; N = 5; Sigma = 1.0/Math.sqrt(5.0); Alpha = 0.05;
		}
		else {
			Delta	= cApplet.aCanvasDelta.paraValue;
			N		= cApplet.aCanvasN.paraValue;
			Sigma	= cApplet.aCanvasSigma.paraValue/Math.sqrt(N);
			Alpha	= cApplet.aCanvasAlpha.paraValue;
		}
		if ( rescaleNeeded ) {
			ymin = countYmin();
			ymax = countYmax();
			xmin = countXmin();
			xmax = countXmax();
			rescaleNeeded = false;
		}

		dim = this.getSize();
		pc = new PlotCoord(xmax, ymax, xmin, ymin, dim.width, 
           dim.height, hlMargin, hrMargin, vuMargin, vlMargin);

		tickPixel = (dim.height - vuMargin - vlMargin)/tManager.tickNumber;
		
		g.setColor(Color.white);
		g.drawLine(hlMargin, vuMargin, hlMargin, dim.height-vlMargin);
		g.drawLine(hlMargin+1, vuMargin, hlMargin+1, dim.height-vlMargin);
		g.drawLine(hlMargin, dim.height-vlMargin, dim.width-hrMargin, dim.height-vlMargin);
		
		double stepWidth, xCurrent, xNext, xPrev, xMid;
		int		index = 0;
		
		tailX = pData.computeTail();
		stepWidth = Sigma*viewSigmas*2/curveSteps;
		xPrev = Mu + Delta - curveSteps * stepWidth;
		xCurrent = xPrev;
		g.setColor(Color.red);

		for (index = -curveSteps; index < curveSteps; index++) { 
		  	xPrev = xCurrent;
		  	xCurrent += stepWidth;
		  	xMid = (xPrev+xCurrent)/2.0;
		  	if ( ( pData.powerType == 0 ) && ( xMid < tailX ) ) continue;
		  	if ( ( pData.powerType == 1 ) && ( xMid > tailX ) ) continue;
		  	if ( ( pData.powerType == 2 ) && ( xMid < tailX ) && ( xMid > ( - tailX) ) ) continue;
		  	recX[0] = recX[1] = pc.xcoord(xPrev);
		  	recX[2] = recX[3] = pc.xcoord(xCurrent);
		  	recY[0] = recY[3] = pc.ycoord(ymin);
		  	recY[1] = (int)(pc.ycoord(normalPDF(xPrev, false)));	
		  	recY[2] = (int)(pc.ycoord(normalPDF(xCurrent, false)));
			g.fillPolygon(recX, recY, 4);
		}
		
		g.setColor(Color.white);
		String tickString = new String();
		for ( int i = 0; i <= tManager.getTickNumber(); i++ ) {
			tickString = tManager.getTickString(i);
			g.drawLine(hlMargin-3, vuMargin+i*tickPixel, hlMargin, vuMargin+i*tickPixel);
			g.drawString(tickString, hlMargin-10-5*tickString.length(), vuMargin+i*tickPixel+3);
		}	
		g.setColor(Color.cyan);
		for (index = -curveSteps; index < curveSteps; index++){
			xCurrent =	Mu + index * stepWidth;
			xNext =		xCurrent + stepWidth;
			if ( ( xCurrent < xmin ) || ( xNext > xmax ) ) continue;
			g.drawLine(	pc.xcoord(xCurrent), pc.ycoord(normalPDF(xCurrent, true)),
						pc.xcoord(xNext), pc.ycoord(normalPDF(xNext, true)) );
		}			
		g.setColor(Color.cyan);
		for (index = -curveSteps; index < curveSteps; index++){
			xCurrent =	Mu + Delta + index * stepWidth;
			xNext =		xCurrent + stepWidth;
			if ( ( xCurrent < xmin ) || ( xNext > xmax ) ) continue;
			g.drawLine(	pc.xcoord(xCurrent), pc.ycoord(normalPDF(xCurrent, false)),
						pc.xcoord(xNext), pc.ycoord(normalPDF(xNext, false)) );
		}

		g.setColor(Color.white);
		g.drawLine(hlMargin, vuMargin, hlMargin, dim.height-vlMargin);
		g.drawLine(hlMargin+1, vuMargin, hlMargin+1, dim.height-vlMargin);
		g.drawLine(hlMargin, dim.height-vlMargin, dim.width-hrMargin, dim.height-vlMargin);
		g.setColor(Color.yellow);
		tmpString = new String(new Double(tailX).toString());
		tmpString = tmpString.substring(0, Math.min(6, tmpString.length()));
		g.drawLine(pc.xcoord(tailX), pc.ycoord(0.0), pc.xcoord(tailX), pc.ycoord(normalPDF(tailX, true)));
		g.drawString( tmpString, pc.xcoord(tailX) - 12, pc.ycoord(0.0) + 15 );
					 
		if ( pData.powerType == 2 ) {
			g.drawLine(pc.xcoord(-tailX), pc.ycoord(0.0), pc.xcoord(-tailX), pc.ycoord(normalPDF(-tailX, true)));
			g.drawString( "-"+tmpString, pc.xcoord(-tailX) - 12, pc.ycoord(0.0) + 15 );
		}
		
		pData.setPower(tailX);
	}


	private double normalPDF(double x, boolean isHo) {
	
		if (isHo)
			return(1/Math.sqrt(2*PI)/Sigma)*Math.exp(-Math.pow(x-Mu,2)/(2*Sigma*Sigma));
		else 
			return(1/Math.sqrt(2*PI)/Sigma)*Math.exp(-Math.pow(x-Mu-Delta,2)/(2*Sigma*Sigma));
		
	}

	private double countXmin() {
		
		viewSigmas = 3;
		
		while ( ( ymax / 100.0 ) < ( normalPDF( Sigma * viewSigmas, true ) ) )
			viewSigmas ++;
			
		return( Mu - 2 * viewSigmas * Sigma );
	}
		
	private double countXmax() {

		return( Mu + Delta + 2 * viewSigmas * Sigma );
	
	}
		
	private double countYmin() {
	
		return(0.0);
	
	}
		
	private double countYmax() {
	
		double	tmpMax = normalPDF(0, true);
		
		tManager.reset(tmpMax);
		
		return(tManager.getMaxValue());
	}

}