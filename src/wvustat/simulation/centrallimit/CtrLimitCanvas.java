package wvustat.simulation.centrallimit;

import java.awt.*;
import javax.swing.*;
 

public class CtrLimitCanvas extends JPanel {
	
	CtrLimit	 host;
	histogram	h;
	random		r;
	
	private final double SQRT2 = Math.sqrt(2.0), SQRT3 = Math.sqrt(3.0);
	
	public int[]	parameter = new int[4];
	boolean	needNormalCurve = false, needHistogram = true;
	final int	SAMPLE_SIZE = 0, TOTAL_SAMPLE = 1, SAMPLE_PER_SECOND = 2, BAR_NUMBER = 3 ;
	
	private int sampleSize;
	private int counter = 0;
	private boolean samplingFlag = false;
	boolean isClear,isBuildHist;
	
	int	CANVAS_WIDTH = 300+90;
	int xmin = 40+20;
	int ymax = 160+100;
	int ymin = 20+20;
	int xmax = xmin + CANVAS_WIDTH;
	int maxFreq = 50;
	int bars = 10;
	
	private final double[] distLowerBd = { -6, 0, -1, -1, 0, 0, -1};
	private final double[] distUpperBd = { 6, 10.0, 1, 1, 2, 2, 1};
	private final double[] distMu = { 0, 4, 0, 0, 2.0/3.0, 4.0/3.0, 0.0};
	private final double[] distSigma = { 1.0, 2*SQRT2, 1/SQRT3, 1/SQRT2, SQRT2/3.0, SQRT2/3.0, 1/SQRT2/SQRT3 };
	private PlotCoord pc;
	
	int 	distIndex;
    	double  mu_x_bar, sigma_x_bar;
	tickmarkManager tickY, tickX;
	
	double x_min_value;
	double x_max_value;
	
	double 	newxmin_value, newxmax_value;
	int		xprevcoord;
	int		yprevcoord;
 	int		xcoord;
	int		ycoord;
 	double	stepWidth;
 	double	xpos, proportion;

	
	CtrLimitCanvas(CtrLimit tmp){
		
		super();
		host = tmp;
		setBackground(Color.black);
		r = new random();
		h = new histogram();
		setBounds();
		
	}
	
	public void setPaintState(boolean b1, boolean b2) {
	
		needNormalCurve = b1;
		needHistogram = b2;
	}
	
	public void setBounds() {
		
		distIndex = host.pData.getDistIndex();
		double bar_width;
		
		
		parameter = host.pData.getAllParameter();
		CANVAS_WIDTH = ((300 + 90)/parameter[BAR_NUMBER])*parameter[BAR_NUMBER]; 
		xmax = xmin + CANVAS_WIDTH;
		
		try {
			sampleSize = parameter[SAMPLE_SIZE];
		} catch (Exception e) { sampleSize = 3; }
		
		r.setDistribution(host.distName[distIndex]);
		
		x_min_value = getBarLowerBd(distIndex, sampleSize);
		x_max_value = getBarUpperBd(distIndex, sampleSize);
		h.setMinMaxValue(x_min_value, x_max_value);

		maxFreq = getYmax();
		bar_width = ( h.getMaxValue() - h.getMinValue() ) / parameter[BAR_NUMBER];
		
		tickY = new tickmarkManager(((double)(maxFreq))/parameter[TOTAL_SAMPLE]/bar_width);
		tickX = new tickmarkManager(x_min_value, x_max_value);

		maxFreq = (int)(tickY.getMaxValue()*parameter[TOTAL_SAMPLE]*bar_width+0.5);
		pc = new PlotCoord(h.getMaxValue(), tickY.getMaxValue(), h.getMinValue(), 0, 
					this.getSize().width, this.getSize().height, 
					new Insets(ymin, xmin, this.getSize().height-ymax, this.getSize().width-xmax));
	}
	
	private final double getBarLowerBd( int dist, int ssize ) {
		
		if ( host.pData.getDistIndex() == 0 ) 
			return (distMu[dist] - 3.0 / Math.sqrt(ssize));
		if ( host.pData.getDistIndex() == 1 ) 
			return (getChiSqMin(ssize));
		if ( !largeSample(ssize) ) return (distLowerBd[dist]);
		else	return ( distMu[dist] - 3.0 * distSigma[dist] / Math.sqrt(ssize)); 
	}
	
	private final double getBarUpperBd( int dist, int ssize ) {
		
		if ( host.pData.getDistIndex() == 0 ) 
			return (distMu[dist] + 3.0 / Math.sqrt(ssize));
		if ( host.pData.getDistIndex() == 1 ) 
			return (getChiSqMax(ssize));
		if ( !largeSample(ssize) ) return (distUpperBd[dist]);
		else	return ( distMu[dist] + 3.0 * distSigma[dist] / Math.sqrt(ssize) ); 
	}
	
	//This method is added on Mar 30 2004 by Jun Tan
	//In order to seperate generating samples from drawing the histogram.
	public void addSamples(){
		parameter = host.pData.getAllParameter();
		int startSampling = h.getHistSize() + 1;		
		int	thisSampling = parameter[SAMPLE_PER_SECOND];
		
		if ( ( startSampling + thisSampling - 1) > parameter[TOTAL_SAMPLE])
			thisSampling = parameter[TOTAL_SAMPLE] - startSampling + 1;
		

		for ( int i = 0; i < thisSampling; i++ ){
			int destBar = 0;
			double sum = 0;
		
			for ( int j = 0; j < parameter[SAMPLE_SIZE]; j++ ) 
				sum += r.getRandomNumber();
			h.addOneSample(sum/sampleSize);
		}
		repaint();
	}
	
	public void rescaleYOnly(){
		int highestBarFreq = 0;
		parameter = host.pData.getAllParameter();
		int firstBarIndex = ( parameter[BAR_NUMBER] - bars ) / 2;
		for(int i = firstBarIndex; i < bars; i++){
			if(h.getBarHeight(i) > highestBarFreq)
				highestBarFreq = h.getBarHeight(i);
		}
		
		maxFreq = Math.max(highestBarFreq, getYmax());
		double bar_width = ( h.getMaxValue() - h.getMinValue() ) / parameter[BAR_NUMBER];
		tickY = new tickmarkManager(((double)maxFreq)/parameter[TOTAL_SAMPLE]/bar_width);
		repaint();
	}
	
	public void paintComponent(Graphics g) {
	
		super.paintComponent(g);
		isClear=host.isClear;
		isBuildHist=host.isBuildHist;
			
		// Draw X/Y axis, processBar
		g.setColor(Color.white);
		ymax = Math.max(getHeight() - ymin, 260);
		xmax = Math.max(getWidth() - xmin, 450);
		CANVAS_WIDTH = xmax - xmin;
		double barWidth = (double)CANVAS_WIDTH / bars;
		tickX = new tickmarkManager(h.getMinValue(), h.getMaxValue());
		pc = new PlotCoord(h.getMaxValue(), tickY.getMaxValue(), h.getMinValue(), 
				0, getWidth(), getHeight(), 
				new Insets(ymin, xmin, getHeight()-ymax, getWidth()-xmax));
		
		distIndex=host.distIndex;
		for (int i = 0; i <= tickY.getTickNumber(); i++ ) {
			g.drawLine(xmin-3, freq2YCoord(maxFreq/tickY.getTickNumber()*i),
					xmin+3, freq2YCoord(maxFreq/tickY.getTickNumber()*i)); 
			String tmpString = new String(tickY.getTickString(tickY.getTickNumber()-i));
			g.drawString(tmpString, xmin-5*tmpString.length()-6, freq2YCoord(maxFreq/tickY.getTickNumber()*i)+3);
		}//end of for
		
		String tmpString = "";
		double tickValue = 1.23456;
		g.setColor(Color.white);
		g.drawLine(xmin, ymax, xmin + CANVAS_WIDTH, ymax);
		g.drawLine(xmin, ymin, xmin, ymax); 
		
		for (int i = -8; i <= 8; i++ ) {
			tmpString = new String(tickX.getTickString(i));
			tickValue = new Double(tmpString).doubleValue();

			if ( (tickValue >= x_min_value) && (tickValue <= x_max_value) ) {
				g.drawLine(pc.xcoord(tickValue), ymax, pc.xcoord(tickValue), ymax+3); 
				g.drawString(tmpString, pc.xcoord(tickValue)-(int)(5*(tmpString.length()/2.0)), ymax + 15);
			}
		}
		
		//Modified by Jun Tan 3-30-2004
		if(isBuildHist){
			parameter = host.pData.getAllParameter();
			int firstBarIndex = ( parameter[BAR_NUMBER] - bars ) / 2;
			
			for(int destBar = firstBarIndex; destBar < bars && needHistogram; destBar++){
				g.setColor(new Color(153,153,255));//blue
				g.fillRect((int)((destBar)*barWidth+xmin), 
					freq2YCoord(h.getBarHeight(destBar)), (int)barWidth, 
					ymax-freq2YCoord(h.getBarHeight(destBar)));
				g.setColor(Color.white);
				g.drawRect((int)(destBar*barWidth+xmin), 
					freq2YCoord(h.getBarHeight(destBar)), (int)barWidth, 
					ymax-freq2YCoord(h.getBarHeight(destBar)));
			}
		
			if(needNormalCurve){
				h.setBars(parameter[BAR_NUMBER]);
		        g.setColor(Color.yellow);
		        mu_x_bar = distMu[distIndex]; sigma_x_bar = distSigma[distIndex]/Math.sqrt(sampleSize);
		        //System.out.println(distIndex);
		        newxmin_value = h.getMinValue(); newxmax_value = h.getMaxValue();
				stepWidth= ( newxmax_value - newxmin_value ) / CANVAS_WIDTH;
				xprevcoord = pc.xcoord(newxmin_value);
				yprevcoord = pc.ycoord(0);
		        
				for (int i = 1; i <= CANVAS_WIDTH; i++) {
					xpos = newxmin_value + i * stepWidth;
					//System.out.println(xpos);
					xcoord = xmin + i;
					//System.out.println(xcoord);
					proportion = ((double)h.getHistSize()) / parameter[TOTAL_SAMPLE];
					//System.out.println(h.getHistSize());
					ycoord = pc.ycoord(NormalPDF(xpos, mu_x_bar, sigma_x_bar) * proportion);
				
				
					if ( i > 1 ) g.drawLine(xcoord , ycoord, xprevcoord, yprevcoord);
					xprevcoord = xcoord;
					yprevcoord = ycoord;
				}
			}//end of if(needNormalCurve)
		}//end of buildHist (end if );
		else if (isClear){
			reset(false); 
		}
	}
		
		/*
		if (needNormalCurve&&isBuildHist){
			parameter = host.pData.getAllParameter();
			int startSampling = h.getHistSize() + 1;		
			int	thisSampling = parameter[SAMPLE_PER_SECOND];
			int firstBarIndex = ( parameter[BAR_NUMBER] - bars ) / 2;
		
			if ( ( startSampling + thisSampling - 1) > parameter[TOTAL_SAMPLE])
			thisSampling = parameter[TOTAL_SAMPLE] - startSampling + 1;
			

			for ( int i = 0; i < thisSampling; i++ ) {
		
				int destBar = 0;
				double sum = 0;
		
				for ( int j = 0; j < parameter[SAMPLE_SIZE]; j++ ) 
				sum += r.getRandomNumber();
			
				destBar = h.addOneSample(sum/sampleSize)-firstBarIndex;
			
				if ( ( destBar >= 0 ) && ( destBar < bars ) ) {
					h.getBarHeight(destBar);
				}//end if
			}//end for
			
			h.setBars(parameter[BAR_NUMBER]);
		        g.setColor(Color.yellow);
		        mu_x_bar = distMu[distIndex]; sigma_x_bar = distSigma[distIndex]/Math.sqrt(sampleSize);
		        
			for (int i = 1; i <= CANVAS_WIDTH; i++) {
				xpos = newxmin_value + i * stepWidth;
				//System.out.println(xpos);
				xcoord = xmin + i;
				//System.out.println(xcoord);
				proportion = ((double)h.getHistSize()) / parameter[TOTAL_SAMPLE];
				//System.out.println(h.getHistSize());
				ycoord = pc.ycoord(NormalPDF(xpos, mu_x_bar, sigma_x_bar) * proportion);
				
				
				if ( i > 1 ) g.drawLine(xcoord , ycoord, xprevcoord, yprevcoord);
				xprevcoord = xcoord;
				yprevcoord = ycoord;
			}
		}*/
		
		/*
		 Comment out by Jun Tan 3-30-2004
		 This part of code generates samples and updates the screen. This will cause
		 the graph be updated unwanted.
		  
		//buildHist
		try{
		if(isBuildHist){
			parameter = host.pData.getAllParameter();
			int startSampling = h.getHistSize() + 1;		
			int	thisSampling = parameter[SAMPLE_PER_SECOND];
			int firstBarIndex = ( parameter[BAR_NUMBER] - bars ) / 2;
		
			if ( ( startSampling + thisSampling - 1) > parameter[TOTAL_SAMPLE])
			thisSampling = parameter[TOTAL_SAMPLE] - startSampling + 1;
			

			for ( int i = 0; i < thisSampling; i++ ) {
		
				int destBar = 0;
				double sum = 0;
		
				for ( int j = 0; j < parameter[SAMPLE_SIZE]; j++ ) 
				sum += r.getRandomNumber();
			
				destBar = h.addOneSample(sum/sampleSize)-firstBarIndex;
			
				// Delete Feb-6-2000
				// host.hBar.setCurrent(h.getHistSize());
			
			
				if ( ( destBar >= 0 ) && ( destBar < bars ) && needHistogram) {
					g.setColor(new Color(153,153,255));//blue
					g.fillRect((int)((destBar)*barWidth+xmin), freq2YCoord(h.getBarHeight(destBar)),
						(int)barWidth, ymax-freq2YCoord(h.getBarHeight(destBar)));
					g.setColor(Color.red);
					g.setColor(Color.white);//lightgray
					g.drawRect((int)(destBar*barWidth+xmin), freq2YCoord(h.getBarHeight(destBar)),
						(int)barWidth, ymax-freq2YCoord(h.getBarHeight(destBar)));
				}//end if
				
			}//end for
			
			if(needNormalCurve){
			h.setBars(parameter[BAR_NUMBER]);
		        g.setColor(Color.yellow);
		        mu_x_bar = distMu[distIndex]; sigma_x_bar = distSigma[distIndex]/Math.sqrt(sampleSize);
		        //System.out.println(distIndex);
		        newxmin_value = h.getMinValue(); newxmax_value = h.getMaxValue();
			stepWidth= ( newxmax_value - newxmin_value ) / CANVAS_WIDTH;
			xprevcoord = pc.xcoord(newxmin_value);
			yprevcoord = pc.ycoord(0);
		        
			for (int i = 1; i <= CANVAS_WIDTH; i++) {
				xpos = newxmin_value + i * stepWidth;
				//System.out.println(xpos);
				xcoord = xmin + i;
				//System.out.println(xcoord);
				proportion = ((double)h.getHistSize()) / parameter[TOTAL_SAMPLE];
				//System.out.println(h.getHistSize());
				ycoord = pc.ycoord(NormalPDF(xpos, mu_x_bar, sigma_x_bar) * proportion);
				
				
				if ( i > 1 ) g.drawLine(xcoord , ycoord, xprevcoord, yprevcoord);
				xprevcoord = xcoord;
				yprevcoord = ycoord;
			}
			}//end of if(needNormalCurve)
		}//end of buildHist (end if );
		else if (isClear){
			reset(false); 
		};
	}catch (Exception el){} 
	}//end of paintComponent
	*/
	

	public int returnSize(){
		return(h.getHistSize()+h.getHistSize());
		}
	
	private final double NormalPDF ( double x, double mu, double sigma ) {
	
		return(Math.exp(-Math.pow(x-mu, 2.0)/2/sigma/sigma)/sigma/Math.pow(2*Math.PI, 0.5));
	}
 		
	
	private final int freq2YCoord(int f) {
	
		return((int)(ymax-(double)f/maxFreq*(ymax-ymin)));
	}
	
	private final int getYmax() {
		
		int	 distIndex = host.pData.getDistIndex();
		if ( distIndex == 0 ) {
			int ymax = (int)(1.2 * Math.sqrt(parameter[SAMPLE_SIZE]/Math.PI/2.0) *
				parameter[TOTAL_SAMPLE] * ( h.getMaxValue() - h.getMinValue() ) /
				parameter[BAR_NUMBER]);
			return(ymax);
		}
		else {
			double ymax = ((parameter[TOTAL_SAMPLE]*3.0/parameter[BAR_NUMBER]/5.0+1.0)*5.0);
			if ( parameter[SAMPLE_SIZE] <= 2 ) return((int)(ymax*0.7));
			else if ( parameter[SAMPLE_SIZE] <= 9 ) return((int)ymax);
			else return((int)(ymax*1.2));
		}
	}
	
	private boolean largeSample(int ssize) {
	
		if ( ssize >= 5 ) return true;
		else	return false;
	}
	
	public final void reset(boolean needRescale){
		
		parameter = host.pData.getAllParameter();
		bars = parameter[BAR_NUMBER]; sampleSize = parameter[SAMPLE_SIZE];
		h.setBars(bars);
		h.resetBars();
		
		CANVAS_WIDTH = ((300+90)/bars)*bars; xmax = xmin + CANVAS_WIDTH;
        	mu_x_bar = distMu[distIndex]; sigma_x_bar = distSigma[distIndex]/Math.sqrt(sampleSize);
        	host.setMuAndSigma(getPreferedString(mu_x_bar, 4), getPreferedString(sigma_x_bar, 4));

		if (needRescale) setBounds();
		//repaint();
	}
	
	public void setDF(int value){
		
		r.setDF(value);
		distMu[1] = value;
		distSigma[1] = Math.sqrt(value) * SQRT2;
	}
	

	private final double getChiSqMin(int ssize) {
	
		int df = r.getDF();
		if ( ssize <= 5 )	return(0);
		else return(Math.max(0.0, df - 3.0 * SQRT2 * Math.sqrt((double)df/ssize))); 
	}
		
	private final double getChiSqMax(int ssize) {
	
		int df = r.getDF();
		if ( ssize >= 5 ) return(df + 3.0 * SQRT2 * Math.sqrt((double)df/ssize));
		
		else if ( ssize <= 2 )	return(df*2.0);
		else return(Math.min(2*df, df + 3.0 * SQRT2 * Math.sqrt((double)df/ssize))); 
	}
		
  	
	
	
	
	public String getPreferedString(double value, int decimal) {
	
		String tmpString = new String(new Double(value).toString());	
		int decimalPos = tmpString.indexOf(".");
		
		if ( decimalPos < 0 ) return(tmpString); //not found "."
		if ( decimal == 0)
			 return(tmpString.substring(0, Math.min(decimalPos+decimal, tmpString.length())));
		else return(tmpString.substring(0, Math.min(decimalPos+decimal+1, tmpString.length())));
	}
		
	
			
}
