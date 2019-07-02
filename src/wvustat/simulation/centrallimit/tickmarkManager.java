package wvustat.simulation.centrallimit;

public class tickmarkManager {
	
	double maxValue;
	double minValue;
	double max = 0;
	double min = 0;
	
	int tickNumber;
	int virtualMax = 0;
	int virtualUnit = 0;
	boolean isStartFromZero = false;
	
	private int base;
	boolean isNegative;
	
	tickmarkManager(double max) {
	
		reset(0.0, max);
		this.max = max;
		isStartFromZero = true;
	}
	
	tickmarkManager(double min, double max) {
	
		reset(min, max);
		this.max = max;
		this.min = min;
	
	}
	
	
	public double getMaxValue(){
		
		return (maxValue);
	
	}
	
	public int getTickNumber(){
		
		return (tickNumber);
	
	}
	
	public void reset(double min, double max){
	
//System.out.println("------------ Start reset ------ min = " + min + ", max = " + max);
		int tmpPart = 0;
		base = (int)(Math.log(max-min+(1E-6))/Math.log(10.0));
		if ( (max - min) < 1-(1E-6) ) base--;
//System.out.print("------------ base = " + base);
		
		virtualMax = (int)( (max-min) / ( Math.pow(10.0, base-1) ) );
		tmpPart = ( virtualMax - 1 ) / 5;
//System.out.println(", tmpPart =  " + tmpPart);
		
		
		switch (tmpPart) {
			case 0:
			case 1:
			case 2:
						virtualMax = 15; 
						tickNumber = 3;
						break;
			case 3:
						virtualMax = 20; 
						tickNumber = 4;
						break;
			case 4:
						virtualMax = 25; 
						tickNumber = 5;
						break;
			case 5:
						virtualMax = 30; 
						tickNumber = 3;
						break;
			case 6:
			case 7:
						virtualMax = 40; 
						tickNumber = 4;
						break;
			case 8:
			case 9:
						virtualMax = 50; 
						tickNumber = 5;
						break;
			case 10:
			case 11:
			case 12:
						virtualMax = 60; 
						tickNumber = 4;
						break;
			case 13:
			case 14:
			case 15:
						virtualMax = 75; 
						tickNumber = 5;
						break;
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
						virtualMax = 100; 
						tickNumber = 5;
						break;
			
		}
//System.out.println("------------ Start (3) ------ virtualMax = " + virtualMax + ", virtualUnit = " + virtualUnit);
		
//System.out.print("------------ base  -- 2 = " + base);
		maxValue = virtualMax * Math.pow(10.0, base-1);
//System.out.println("------------ Start (4) ------ tickNumber = " + tickNumber + ", virtualMax = " + virtualMax);
		virtualUnit = virtualMax/tickNumber;
//System.out.println("------------ Start (4) ------ tickNumber = " + tickNumber + ", virtualUnit = " + virtualUnit);
//System.out.print("------------ base -- 3 = " + base);
		if ( virtualMax == 100 ) base++;
//System.out.println("------------ Start end ------ virtualMax = " + virtualMax + 
// ", virtualUnit = " + virtualUnit + ", maxValue = " + maxValue + ", base = " + base);

	}
	
	public String getTickString(int index) {
		
		String	tickString = new String("Hai");
		String	virtualTickString;
		int 	tickBase = base;
		int		virtualTick = 0;
		
		//index = tickNumber - index;
		if ( ( index == tickNumber ) && isStartFromZero ) return("0");
		
		else{
			virtualTick = virtualMax - index * virtualUnit;	
//System.out.println(index + ")****  virtualTick = " + virtualTick);
			isNegative = ( virtualTick < 0 ); virtualTick = Math.abs(virtualTick);
			if ( (	new String(new Integer(virtualMax).toString() ) ).length() <=
				 (	new String(new Integer(virtualTick).toString() ) ).length() )
					tickBase = base;
			else 	tickBase = base - 1;
//System.out.println("base = " + base + ", tickbase = " + tickBase);

//System.out.print(tickBase);
//System.out.print("  tickBase ---- virtualTick   ");
//System.out.println(virtualTick);

			tickString = assembleString(virtualTick, tickBase);
//System.out.println("tickString = " + tickString + "\n");
		}
		
		return(tickString);
	}
	
	private String assembleString( int value, int b ){
		
		String signString = ""; 
		String returnString = new String(new Integer(value).toString());
		
		if ( isNegative ) signString = "-";
		if ( value == 0 ) return("0");
				
		if ( b < 0 ) {
			returnString = "0.";
			for ( int i = 1; i < -b; i++ ) returnString += "0";
			returnString += new Integer(value).toString();
		}
		
		if ( b > 0 ){
			if ( ( value == 100 ) && ( b == 1 ) ) return(signString + "10.0");
			for ( int i = returnString.length(); i <= b; i++ ) returnString += "0";
		}
		
		if ( b == 0 ) {
			if ( value == 100 ) return(signString + "1.0");
				returnString =	returnString.substring(0, returnString.length()-1) + "."
						+ returnString.substring(returnString.length()-1);
		}
		
		returnString = trimString(returnString, b);
		return(signString + returnString);
	}
	
	private String trimString(String origString, int b) {
		
		String returnString = new String(origString);
		
		if ( ( b < 0 ) && ( (virtualMax == 30) || (virtualMax == 40)
			 || (virtualMax == 50) || (virtualMax == 100) ) )  
			returnString = returnString.substring(0, Math.max(1, returnString.length() - 1));
		
		if ( returnString.equals("1.00") )  returnString = "1.0";
		
		return(returnString);		
	}
			
	
}
		
