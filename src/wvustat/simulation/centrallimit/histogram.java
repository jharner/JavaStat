package wvustat.simulation.centrallimit;

public class histogram {

	double minValue, maxValue;
	int barNumber;
	int barFreq[] = new int[100];
	int unuse = 0;
	
	histogram(double min, double max, int bars){
	
		minValue = min;
		maxValue = max;
		barNumber = bars;
		resetBars();
	}
	
	histogram(){
		
		this(0.0, 2.0, 10);
	}
	
	public double getMidPoint(int barIndex) {
		
		double unit = (maxValue - minValue ) / barNumber;
		return( barIndex * unit + minValue );
	}
		
	public String getMidPointString(int barIndex, int length) {
		
		double value = getMidPoint(barIndex);
		String tmpString = new Double(value).toString();
		return( tmpString.substring(0, Math.min(length, tmpString.length())));
	}
		

	public void resetBars() {
	
		for ( int i = 0; i < 100; i++ )
			barFreq[i] = 0;
		unuse = 0;
	}
	
	public int getIndex(double s) {
		
		double unit = (maxValue - minValue ) / barNumber;
		return((int)((s-minValue)/unit));
	}
	
	public int addOneSample(double s) {
	
		int index = getIndex(s);

		if ( index < 0 ) { unuse++;  return(-1); } //index = 0;;
		if ( index >= barNumber )  { unuse++; return(-1); } //index = barNumber-1;

		barFreq[index]++;
		return (index);
	}
	
	public void setBars(int n) {
		
		if ( ( n > 0 ) && ( n <= 100 ) ) 
			barNumber = n;
		
	}
	
	public int getBars() {
		
		return(barNumber);
	}
	
	public int getBarHeight(int index) {
	
		return(barFreq[index]);
	}
	
	public int getHistSize() {
	
		int sum = 0;
		
		for ( int i = 0; i < barNumber; i++ )
			sum += barFreq[i];
			
		return( sum + unuse );
	}
	
	public void setMinMaxValue(double min, double max) {
	
		if ( min < max ) {
			minValue = min;
			maxValue = max;
		}
		else {
			minValue = min;
			maxValue = max;
		}
		
	}
	
	public void setMinValue(double min) {
	
		if ( min < maxValue ) 
			minValue = min;
	}
	
	public void setMaxValue(double max) {
	
		if ( max > minValue ) 
			maxValue = max;
	}
		
	public double getMinValue() {
	
		return(minValue);	
	}
	
	public double getMaxValue() {
	
		return(maxValue);
	}

	public double getMidValue() {
	
		return( ( maxValue + minValue ) / 2.0 );
	}

}
