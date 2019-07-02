package wvustat.simulation.confint;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JApplet;

 
	public class confintCanvas extends JPanel {

		private int sampleSize = 100;
		private int tickPixel;
		Dimension	dim;
		private int margin = 24;
		publicData	pData;
		confint		cApplet;
	
		private double xstrech;
		private double shift;
		public boolean firstTimePaint = true;
		private double unitWidth = 0;

		private double	Mu;
		private double	Sigma;
		private double	N;
		private double	Alpha;
		
	

	confintCanvas (publicData tmpData, confint tmpApplet) {
	
		super();
		pData = tmpData;
		cApplet = tmpApplet;
		setBackground(Color.black);
		

	}
	


	public void paintComponent (Graphics g) {
		
		double aSample;
		double lBound, uBound;
		int outBoundCount = 0;
		String	tmpString;
		
		super.paintComponent(g);
		Mu	= cApplet.aCanvasMu.paraValue;
		Sigma	= cApplet.aCanvasSigma.paraValue;
		N	= cApplet.aCanvasN.paraValue;
		Alpha	= cApplet.aCanvasAlpha.paraValue;
		
		dim = this.getSize();
		tickPixel = (dim.width - margin*2)/4;
		
				
		g.setColor(Color.black);
		g.fillRect(0,0,dim.width,dim.height);

		cApplet.pCanvas.setType(0);
		cApplet.pCanvas.probLabel.setText(new Integer(cApplet.pData.probContain).toString() + "% contain ");

		g.setColor(Color.white);
		g.drawLine(margin, dim.height-margin, dim.width-margin, dim.height-margin);
		
		//setXcoord(); if need to change image ratio by itself, add this sentence and delete "if...." below.
		
		if( firstTimePaint ) {
			setXcoord();
			firstTimePaint = false;
		}
		
		for ( int i = 0; i < 5; i++ ) {
			g.drawLine(	margin+i*tickPixel, dim.height-margin-3,
						margin+i*tickPixel, dim.height-margin+3);
			tmpString = new String(new Double(shift+(i-2)*unitWidth).toString());
			tmpString = tmpString.substring(0,Math.min(5, tmpString.length()));
			g.drawString(tmpString, margin+i*tickPixel-10, dim.height-margin+14);
		}
		
				
		for ( int i = 0; i < sampleSize; i++ ) {
			aSample=cApplet.WholeSample[i];
			lBound = pData.getLowerBound(aSample,cApplet.AllIntervalWidth[i]);		
			uBound = pData.getUpperBound(aSample,cApplet.AllIntervalWidth[i]);
			if(	( lBound > Mu ) ||
				( uBound < Mu ) ) {
				g.setColor(Color.lightGray);
				outBoundCount++;
			}
			else
				g.setColor(Color.blue);
				
			
				g.drawLine(xcoord(lBound),margin+i*(dim.height-2*margin)/sampleSize
				, xcoord(uBound),margin+i*(dim.height-2*margin)/sampleSize);
				g.setColor(Color.magenta);
				g.drawOval(xcoord(aSample),margin+i*(dim.height-2*margin)/sampleSize-1,1,1);
			
			
		}
		
		pData.probContain = 100 - outBoundCount;
		cApplet.pCanvas.setType(1);
		cApplet.pCanvas.probLabel.setText(new Integer(cApplet.pData.probContain).toString() + "% contain ");
		
		g.setColor(Color.red);
		g.drawLine(	xcoord(Mu), dim.height-margin, 
					xcoord(Mu), margin);
		g.fillRect(0, dim.height-2, 2, 2); 
						
	}
	


	public final void setXcoord() {
	
		xstrech = ( dim.width - 2 * margin )/getmyWidth();
		shift = Mu;
	}


	private final int xcoord(double x){
	
		return((int)(dim.width/2 + (x - shift) * xstrech));
	
	}
	


	public double getmyWidth() {
		
		double width = Sigma*(3+pData.sigPoint*Math.sqrt(1+4/Math.sqrt(N-1)))/Math.sqrt(N);
		
		if ( Sigma > 1.5 )
			width = width/(Math.abs(Math.log(Sigma)/Math.log(2.0)));
		if ( Sigma < 0.5 )
			width = width*(Math.abs(Math.log(Sigma)/Math.log(2.0)));
		
		if	( width < 0.02 ) {
			width = 0.02;
			unitWidth = 0.01;
		}
		else if ( width < 0.1 ) {
			width = 0.1;
			unitWidth = 0.05;
		}
		else if ( width < 0.4 ) {
			width = 0.4;
			unitWidth = 0.2;
		}
		else if ( width < 1.0 ) {
			width = 1.0;
			unitWidth = 0.5;
		}
		else	{
			width = ((int)(width/2.0+0.99))*2.0;
			unitWidth = width/2.0;
		};
			
		return(width*2.0);  
	} 
}