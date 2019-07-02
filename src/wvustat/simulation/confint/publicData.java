package wvustat.simulation.confint;


public class publicData {

	confint cApplet;
	
	double	intervalWidth = 1.0;
	double	sigPoint = 2.776;
	double	PI = 3.1415926;
	
	public int probContain = 0;

	publicData(confint tmpApplet) {
	
		cApplet = tmpApplet;
		
  	}

	public void setPara(adjustCanvas aCanvas) {
		
		if ( aCanvas.symbolString == "mu" ) {
			  	aCanvas.paraType = "double";
				aCanvas.paraValue = cApplet.initMu;
				aCanvas.paraUpperBound = 1000;
				aCanvas.paraLowerBound = -1000;
				aCanvas.paraInc = 1;
		}

		if ( aCanvas.symbolString == "n" ) {
			  	aCanvas.paraType = "int";
				aCanvas.paraValue = cApplet.initN;
				aCanvas.paraUpperBound = 100;
				aCanvas.paraLowerBound = 1.1;
				aCanvas.paraInc = 1;
		}

		if ( aCanvas.symbolString == "sigma" ) {
			  	aCanvas.paraType = "double";
				aCanvas.paraValue = cApplet.initSigma;
				aCanvas.paraUpperBound = 100;
				aCanvas.paraLowerBound = 1E-3;
				aCanvas.paraInc = 1;
		}

		if ( aCanvas.symbolString == "alpha" ) {
			  	aCanvas.paraType = "double";
				aCanvas.paraValue = cApplet.initAlpha;
				aCanvas.paraUpperBound = 1.00;
				aCanvas.paraLowerBound = 1E-6;
				aCanvas.paraInc = 0.05;
		}
	}
	public final void reset() {

		cApplet.reset();		
		setNormalInterval(cApplet.initAlpha);
		cApplet.cCanvas.firstTimePaint = true;
		cApplet.cCanvas.repaint();
	}
		
		
	public final double getASample() {
		
		double	uniform = 0;
		int 	sampleSize = (int)(cApplet.aCanvasN.paraValue+0.001);
		double	popMean = cApplet.aCanvasMu.paraValue;
		double	popVariance = cApplet.aCanvasSigma.paraValue;
		
		double	sum = 0.0;
		double	mean = 0.0;
		double	variance = 0.0;
		double	eleSample[] = new double[100];
		
		for ( int i = 0; i < sampleSize; i++ ) {
			
			uniform = Math.min(0.99, Math.random());
			eleSample[i] = uni2normal(uniform, popMean, popVariance);
			sum += eleSample[i];
		
		}
		
		mean = sum / sampleSize;


		for ( int i = 0; i < sampleSize; i++ ) 		
			variance += ( ( eleSample[i] - mean ) * ( eleSample[i] - mean ) );
			
		variance = variance / sampleSize;
		intervalWidth = sigPoint*Math.sqrt(variance)/Math.sqrt(sampleSize-1);
		
		return(mean);
	}
		
	
	public final double getLowerBound(double x,double width) {
	
		return( x - width );
	
	}

	public final double getUpperBound(double x,double width) {
	
		return( x + width );
	
	}
	
	private final double uni2normal(double value, double mu, double variance) {
		
		double integrateStart = mu - 5 * Math.sqrt(variance);
		double step = Math.sqrt(variance)/20.0;
		double integrateValue = 0.0;
		double constantPart = 1.0/Math.sqrt(2*PI*variance);
		
		if ( value > 0.50 ) {
			integrateStart = mu;
	 		integrateValue = 0.50;
	 	}
	 	
	 	while (integrateValue <= value) {
	 		
	 		integrateValue += step*constantPart/Math.exp((integrateStart-mu)*(integrateStart-mu)/2/variance);
	 		integrateStart += step;

		}
		
		return(integrateStart-step);
	}
	
	public final void setNormalInterval(double alpha) {
	
		//This is a computation for normal(0,1)
		double	t = 0.0;
		double	integrateEnd = 0.5 - alpha/2;
		double	integrateValue = 0.0;
		double	step = cApplet.aCanvasSigma.paraValue/1000.0;
		int		df = (int)(cApplet.aCanvasN.paraValue-1+0.001);
		double	constantPart = Gamma(df+1)/Math.sqrt(PI*df)/Gamma(df);
		
		while( integrateValue < integrateEnd ){
		
			integrateValue	+= step*constantPart/Math.pow(1.0+t*t/df, (df+1.0)/2.0);
			t				+= step;
		}
		
		sigPoint = t;

	}
	
	private final double Gamma(int paraDouble) {
		
		int			i = paraDouble/2;
		double		result = 1.0;
		boolean	oddPara = true;

		if ( (paraDouble / 2.0 - i) < 1E-3 ) oddPara = false;
		else	oddPara = true;
		
		while( i > 1 ){
			if ( oddPara ) result *= (i-0.5);
			else result *= (i-1.0);
			i = i - 1;
		}
		
		if ( oddPara )	result = result * 0.5 * Math.sqrt(PI);
		return(result);
	}

}

