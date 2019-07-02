package wvustat.simulation.power;

public class tickmarkManager {
	
	double maxValue;
	
	int tickNumber;
	int virtualMax = 0;
	int virtualUnit = 0;
	
	static private int base;
	
	tickmarkManager(double max) {
	
		reset(max);
	
	}
	
	
	public double getMaxValue(){
		
		return (maxValue);
	
	}
	
	public int getTickNumber(){
		
		return (tickNumber);
	
	}
	
	public void reset(double max){
	
		int tmpPart = 0;
		base = (int)(Math.log(max+1E-6)/Math.log(10.0));
		if (max < 1-1E-6 ) base--;
		
		virtualMax = (int)(max / ( Math.pow(10.0, base-1) ) );
		tmpPart = ( virtualMax - 1 ) / 5;
		
		
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
		
		maxValue = virtualMax * Math.pow(10.0, base-1);
		virtualUnit = virtualMax/tickNumber;
		if ( virtualMax == 100 ) base++;
/*System.out.print(maxValue);
System.out.print("  Max--VirtualMax   ");
System.out.println(virtualMax);
System.out.print(virtualUnit);
System.out.print("  Unit --- base   ");
System.out.println(base);*/
	}
	
	public String getTickString(int index) {
		
		String	tickString = new String("Hai");
		String	virtualTickString;
		int 	tickBase = base;
		int		virtualTick = 0;
		
		index = tickNumber - index;
		if ( index == 0 ) return("0");
		
		else{
			virtualTick = index * virtualUnit;	
			if ( (	new String(new Integer(virtualMax).toString() ) ).length() ==
				 (	new String(new Integer(virtualTick).toString() ) ).length() )
					tickBase = base;
			else 	tickBase = base - 1;
//System.out.print(tickBase);
//System.out.print("  tickBase ---- virtualTick   ");
//System.out.println(virtualTick);

			tickString = assembleString(virtualTick, tickBase);
		}
		
		return(tickString);
	}
	
	private String assembleString( int value, int b ){
		
		String returnString = new String(new Integer(value).toString());
		
		if ( b < 0 ) {
			
			returnString = "0.";
			for ( int i = 1; i < -b; i++ ) returnString += "0";
			returnString += new Integer(value).toString();
		}
		
		else if ( b > 0 ){
			if ( ( value == 100 ) && ( b == 1 ) ) return("10.0");
			for ( int i = returnString.length(); i <= b; i++ ) returnString += "0";
		}
		
		else {
				returnString =	returnString.substring(0, 1) + "."
								+ returnString.substring(1);
		}
		
		returnString = trimString(returnString, b);
		return(returnString);
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
		