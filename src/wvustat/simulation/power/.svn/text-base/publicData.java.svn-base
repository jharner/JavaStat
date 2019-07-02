package wvustat.simulation.power;


public class publicData {

	Power cApplet;
	
	double	intervalWidth = 1.0;
	double	sigPoint = 2.776;
	double	PI = 3.1415926;
	
	int powerType = 0;

	publicData(Power tmpApplet) {
	
		cApplet = tmpApplet;
		
  	}

	public void setPara(adjustCanvas aCanvas) {
		
		if ( aCanvas.symbolString == "Delta" ) {
			  	aCanvas.paraType = "double";
				aCanvas.paraValue = cApplet.initDelta;
				aCanvas.paraUpperBound = 200;
				aCanvas.paraLowerBound = -200;
				aCanvas.paraInc = 0.1;
		}

		if ( aCanvas.symbolString == "N" ) {
			  	aCanvas.paraType = "int";
				aCanvas.paraValue = cApplet.initN;
				aCanvas.paraUpperBound = 100;
				aCanvas.paraLowerBound = 1.1;
				aCanvas.paraInc = 1;
		}

		if ( aCanvas.symbolString == "Sigma" ) {
			  	aCanvas.paraType = "double";
				aCanvas.paraValue = cApplet.initSigma;
				aCanvas.paraUpperBound = 100;
				aCanvas.paraLowerBound = 0.1;
				aCanvas.paraInc = 0.10;
		}

		if ( aCanvas.symbolString == "Alpha" ) {
			  	aCanvas.paraType = "double";
				aCanvas.paraValue = cApplet.initAlpha;
				aCanvas.paraUpperBound = 1.00;
				aCanvas.paraLowerBound = 1E-6;
				aCanvas.paraInc = 0.01;
		}
	}

	public final void reset() {

		cApplet.aCanvasDelta.paraValue = cApplet.initDelta;
		cApplet.aCanvasDelta.paraInc = 0.1;
		cApplet.aCanvasDelta.repaint();
		
		cApplet.aCanvasSigma.paraValue = cApplet.initSigma;
		cApplet.aCanvasSigma.paraInc = 0.1;
		cApplet.aCanvasSigma.repaint();
		
		cApplet.aCanvasN.paraValue = cApplet.initN;
		cApplet.aCanvasN.paraInc = 1.0;
		cApplet.aCanvasN.repaint();
		
		cApplet.aCanvasAlpha.paraValue = cApplet.initAlpha;
		cApplet.aCanvasAlpha.paraInc = 0.01;
		cApplet.aCanvasAlpha.repaint();
		
		cApplet.labelTitle.setText("Power - Upper Tailed"); 
		powerType = 0;
		
		cApplet.pCanvas.firstTimePaint = true;
		cApplet.pCanvas.rescaleNeeded = true;
		cApplet.pCanvas.repaint();
	}

	
	public double computeTail() { 
	
		double	Mu = 0.0;
		double	Delta = cApplet.aCanvasDelta.paraValue;
		double	N = cApplet.aCanvasN.paraValue;
		double	Sigma = cApplet.aCanvasSigma.paraValue/Math.sqrt(N);
		double	Alpha = cApplet.aCanvasAlpha.paraValue;
  		
		
		double lowerBd	= Mu - 5 * Sigma;
  		double upperBd	= Mu + 5 * Sigma;
  		double middle	= (lowerBd + upperBd)/2.0;

  		if ( ( Alpha > 0.5 ) && ( powerType != 2 ) ) Alpha = 1.0 - Alpha;
  		if ( Math.abs(Alpha-1.0) < 1E-4) return(upperBd);
  		if ( Math.abs(Alpha-0.0) < 1E-4) return(lowerBd);
		if ( powerType == 2 ) Alpha = Alpha / 2.0;	
  	
  		while (Math.abs(1-Alpha-accumYvalue(middle, Mu, Sigma))>1E-4) {

  			if	( accumYvalue(middle, Mu, Sigma) > (1-Alpha)) upperBd = middle;
  			else lowerBd = middle;
  		
  			middle	= (lowerBd + upperBd)/2.0;
  		}
  		if ( powerType == 1 ) return(Mu-middle);
  		else return(Mu+middle);
 	}
  	
  	public double accumYvalue ( double pos, double Mu, double Sigma ){

 		double v = Math.abs((pos-Mu)/Sigma);
		double p;
	
		p = Math.exp(-(((83*v)+351)*v+562)*v/(703+165*v));
		if ((pos-Mu)>0)	return(1-0.5*p);
		else		return(0.5*p);

	}

  	public void setPower(double power) {
  	
  		double	Delta = cApplet.aCanvasDelta.paraValue;
  		double	N = cApplet.aCanvasN.paraValue;
  		double	Alpha = cApplet.aCanvasAlpha.paraValue;
		double	Sigma = cApplet.aCanvasSigma.paraValue/Math.sqrt(N);
		double powerValue = 0.0;
		
		switch ( powerType ) {
			case 0: powerValue =	1.0 - accumYvalue(power, Delta, Sigma); break;
			case 1: powerValue =	accumYvalue(power, Delta, Sigma); break;
			case 2: powerValue =	1.0 - accumYvalue(power, Delta, Sigma)
						+ accumYvalue( - power, Delta, Sigma); break;
		}
		
		powerValue = (int)(powerValue * 10000 + 0.5)/10000.0;
  		if ( Math.abs(powerValue-Alpha) < 1E-3 ) powerValue = Alpha;
		

  		String tmpString = new String(new Double(powerValue).toString());
  		tmpString = tmpString.substring(0, Math.min(6, tmpString.length()));
  		
		cApplet.powerLabel.setText("Power = "+tmpString+"   ");
	}
  	
  	public void setPowerType(int type) {
  	
  		powerType = type;
  	}
	
	
}

