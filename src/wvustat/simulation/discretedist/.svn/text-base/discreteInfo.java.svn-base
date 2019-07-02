package wvustat.simulation.discretedist;

public class discreteInfo {

  // Define variable
  public String	paraType[] =  new String[2];
  public double	paraValue[] = new double[2];
  public double	paraUppBd[] = new double[2];
  public double	paraLowBd[] = new double[2];
  public double	paraIncre[] = new double[2];
  public double	paraInit [] = new double[2];
  public int		questionUpper;
  public int		questionLower;
  public int		intervalType = 0;
  
  public double	quantile = 0.5;
  public double	quantileInc = 0.05;	
  
  public boolean	changeInterval = false;
  public boolean	doRescale = true;
  public boolean	isBinomial = true;
  public boolean	isPoisson = false;
  
  public String probString = new String("");
  public String probStringValue = new String("");
  public String approxProbString = new String("");
  public String approxProbValue = new String("");
  
  public String distName;
  public String fOrF = new String(); 
  
  public String paraSign[] = new String[2];
  
  private int xmin, xmax;
  private double ymin, ymax;
  private String approxDistName = new String("");
	
  private	boolean needPoissonApprox = false; 
  private	boolean needNormalApprox = false;
  
  // Constructor
  public discreteInfo ( String tmpString, String display ) {

	distName = new String(tmpString);
	xmin = xmax = 0; ymin = ymax = 0.0;
	if ( distName.equals("Binomial")) {

  		isBinomial	= true;
  		isPoisson	= false;
		
		paraValue[0] = 15.0;
  		paraValue[1] = 0.50;
		paraInit[0] = 15.0;
  		paraInit[1] = 0.50;
  		
  		paraUppBd[0] = 400.000000001;
  		paraUppBd[1] = 1.000000001;
  		paraLowBd[0] = 0.999999999;
  		paraLowBd[1] = -0.000000001;
  		quantile = 0.50;
  		
  		questionUpper = -1;
  		questionLower = -1;
  		
  		paraIncre[0] = 1.0;
  		paraIncre[1] = 0.05;

  		paraType[0] = "integer";
  		paraType[1] = "double";

  	}
  			
  	else {
  		isBinomial	= false;
  		isPoisson	= true;

		paraValue[0] = 3.0;
		paraInit[0] = 3.0;
		paraUppBd[0] = 40.000000001;
		paraLowBd[0] = 0.000000001;

		questionUpper = -1;
		questionLower = -1;
  		
		paraIncre[0] = 1.0;

  		paraType[0] = "double";
	}
	
	if ( new String(display).equals("CDF"))	fOrF = "F";
	else	fOrF = "f";
	
	
  }
  
  public void reset () {
		
	doRescale = true;
  	changeInterval = false;
  	fOrF = "f";
  	intervalType = 0;
  	probString = "";
  	probStringValue = "";
  	approxProbString = "";
  	approxProbValue = "";
	
	if (isBinomial){
		paraValue[0] = paraInit[0];
  		paraValue[1] = paraInit[1];
  		
  		paraUppBd[0] = 400.000000001;
  		paraUppBd[1] = 1.000000001;
  		paraLowBd[0] = 0.999999999;
  		paraLowBd[1] = -0.000000001;
  		quantile = 0.50;
  		
  		questionUpper = -1;
  		questionLower = -1;
  		
  		paraIncre[0] = 1.0;
  		paraIncre[1] = 0.05;
  	}
  		
  	else{
  		isBinomial	= false;
  		isPoisson	= true;

		paraValue[0] = paraInit[0];
		paraUppBd[0] = 40.000000001;
		paraLowBd[0] = 0.000000001;

		questionUpper = -1;
		questionLower = -1;
  		
		paraIncre[0] = 1.0;

	}
  }

  
  // isPDF
  public boolean isPDF( )	{
  
	if ( fOrF.equals("f"))
  		return(true);
  	else
  		return(false);
  }
  
  public void setInitPara(String paraString, int index ) {

  	
  	double	paraValueTmp = Double.valueOf(paraString).doubleValue();
  	
  	if ( ( paraValueTmp > paraLowBd[index] ) && ( paraValueTmp < paraUppBd[index] ) )
  		paraValue[index] = paraValueTmp;
  		paraInit[index] = paraValueTmp;
  
  }

  // ifHavePara2

  public boolean ifHavePara2 ( ) {
	if ( distName.equals("Binomial"))
  		return(true);
  	else
  		return(false);
  }
  
  // getParaString
  public String getParaString(int index) {
  	
  	if (paraType[index].equals("integer"))
  		return(new Integer((int)(paraValue[index]+0.01)).toString());
  	
  	if (paraType[index].equals("double"))
  		return((new Double(Math.round(paraValue[index]*100)/100.0).toString()));
  		
  	return("");
  }
  
  // getParaSign
  public String getParaSign(int index) {
  	
  	if (index == 1)	return(new String("n"));
	else			return(new String("p"));
  }
  
  // Count Xmin,xmax,ymin,ymax
  
   public int countXmin (){
  
  	if ( !doRescale ) return(xmin);
	else { xmin = countXmin(true); return xmin; }  	

  }
 
  
  public int countXmin (boolean needRescale){
	
	if ( distName.equals("Binomial") ) {
		
		if ( needNormalApprox && paraValue[0] > 60.1 ) {
			double mu = paraValue[0] * paraValue[1];
    		double sigma = Math.sqrt( mu * ( 1 - paraValue[1] ));
			return (Math.max(0, (int)(mu-5*sigma)));
		}
		else return(0);
	}
	else return(0);
  }

  public double countYmin (){
	return(0.0);
  }
  
  public int countXmax (boolean needRescale){
  
  	int tmpXmax = 10;
	if ( distName.equals("Binomial") ) {
		if ( !needNormalApprox || paraValue[0] < 59.9 ) 
			tmpXmax = (int)(paraValue[0]+0.1);
	
		else  {
			double mu = paraValue[0] * paraValue[1];
    		double sigma = Math.sqrt( mu * ( 1 - paraValue[1] ) );
			tmpXmax = Math.min((int)(paraValue[0]+0.1), (int)(mu+5*sigma) );
		}
	}
		
	else {
		int j = -1;
		boolean	outFlag = false;
		double	prevProb = 0.0;
		
		while ( outFlag != true ){
			j++;
			if ( pointYvalue(j) < prevProb ) outFlag = true;
			prevProb = pointYvalue(j);
		}
		tmpXmax = Math.max(10, (j--)*3);
	}
 	return(tmpXmax);
  }
 
  public int countXmax (){
  
  	if ( !doRescale ) return(xmax);
	else { xmax = countXmax(true); return xmax;}  	
  }

  public double countYmax (){
  	
  	if ( !doRescale ) return(ymax);
	else {
		ymax = countYmax(true);
		return(ymax);
	} 
  } 	
  
  public double countYmax ( boolean b ){
	
	double part1 = 1, part2 = 1, tmpYmax = 1.0;
	boolean outFlag = false;
	int     i, j;
	double  prevProb = 0;

	if ( !isPDF() ) { tmpYmax = 0.9999; return(tmpYmax); }
	
	i = j = 0;
	if ( isBinomial ) {
		if ( Math.abs(paraValue[1] - 1.0 ) < 1E-8 ) 
			{ tmpYmax = 0.9999; return(tmpYmax); }
		if ( Math.abs(paraValue[1] - 0.0 ) < 1E-8 ) 
			{ tmpYmax = 0.9999; return(tmpYmax); }
			 
		while ( outFlag != true ){

			part2 = 1.0;
			part1 = Math.exp( j * Math.log(paraValue[1]) +
           	        (paraValue[0]  - j) * Math.log(1 - paraValue[1]));
			for ( i = 0; i < j; i ++)
				part2 = 1.0 * part2 * (paraValue[0]  - i) / ( i + 1);
			if ((part1 * part2) < prevProb) outFlag = true;
			else {
				j ++;
				prevProb = part1 * part2;
			}
		}
	} // End of Binomial
	
	else {
		while ( outFlag != true ){
			double poissonValue = pointYvalue(j);	
			if ( poissonValue < prevProb ) outFlag = true;
			else {
				j++;
				prevProb = poissonValue;
			}
		}
	}
	tmpYmax = prevProb;	
	if ( needNormalApprox ) {
		double mu = paraValue[0] * paraValue[1];
    	double sigma = Math.sqrt( mu * ( 1 - paraValue[1] ) );
    	Normal normal = new Normal(mu,sigma);
   		tmpYmax = Math.max(tmpYmax, normal.pdf(mu) * 1.05);
    }

	return ( tmpYmax );

  }
  
  // compute Y value given x
  public double pointYvalue ( int pos ){

	double part1, part2;

	part1 = 1.0;
	part2 = 1.0;
	
	if ( distName.equals("Binomial") ) {
		
		// deal the situation p = 1 
		if (Math.abs(paraValue[1]-1.0)<1E-8)
			if ( Math.round(paraValue[0]) == pos )	return(1.0);
			else	return(0.0);
		
		if (Math.abs(paraValue[1]-0.0)<1E-8)
			if ( pos == 0 )	return(1.0);
			else	return(0.0);
		
		part1 = Math.exp(pos * Math.log(paraValue[1]) +
			(paraValue[0]  - pos) * Math.log(1 - paraValue[1]));
		for ( int i = 0; i < pos; i ++)
            		part2 = 1.0 * part2 * (paraValue[0]  - i) / (i + 1);
		return ( part1 * part2 );
	}
	
	else {
		part1 = Math.exp(-paraValue[0]);
		for ( int i = 1; i <= pos; i ++)
			part2 *= i;
		if ( pos == 0 ) part2 = 1.0;
		part2 = Math.pow(paraValue[0], pos) / part2;
		return ( part1 * part2 );
	}	
		
  }
  
  // compute accumulate Y value
  public double accumYvalue ( int pos ){

	double accumValue = 0.0;
	
	for ( int i = 0; i <= pos; i ++)
		accumValue += pointYvalue(i);

	return(accumValue);
  }
  
  // putParaValue
  public void putParaValue(discreteDist tmpApplet) {
	
	double tmp[] = new double[2];
	  	
	tmp[0] =Double.valueOf(tmpApplet.textFieldPara1.getText()).doubleValue();
	if ( ( tmp[0] <= paraUppBd[0] ) && ( tmp[0] >= paraLowBd[0] ) )
		paraValue[0] = tmp[0];
	
	if (isBinomial) {
		tmp[1] =Double.valueOf(tmpApplet.textFieldPara2.getText()).doubleValue();
		if ( ( tmp[1] <= paraUppBd[1] ) && ( tmp[1] >= paraLowBd[1] ) )
			paraValue[1] = tmp[1];
	}
  }

  public void decPara (int index) {
  	
  	if ( ( paraValue[index] - paraIncre[index] ) > paraLowBd[index] )
		paraValue[index] -= paraIncre[index];
	
	setInterval(questionLower, questionUpper, intervalType);
  }	  		 

  public void incPara (int index) {
  	
  	if ( paraUppBd[index] > (paraValue[index] + paraIncre[index]) )
		paraValue[index] += paraIncre[index];
		
	setInterval(questionLower, questionUpper, intervalType);
  }	
  
  public void setValue (double value, int tmpIndex) {
  
  	if ( ( value < paraUppBd[tmpIndex] ) && ( value > paraLowBd[tmpIndex] ) )
  		paraValue[tmpIndex] = value;
  }
  
  public void setInc (double inc, int tmpIndex) {
  
  	if ( ( inc < paraUppBd[tmpIndex] ) && ( inc > paraLowBd[tmpIndex] ) && ( inc != 0 ) )
  		paraIncre[tmpIndex] = inc;
  }
  
  public void setQuantile (double q) {
  
  	if ( ( q < 1.0 ) && ( q > 0.0 ) )
  		quantile = q;
  		if ( ( 1.0-quantile ) < 1E-5 ) quantile = 1.0;
  	    if ( ( quantile-0.0 ) < 1E-5 ) quantile = 0.0;
  	
  }
  
  public void setQuantileInc (double qInc) {
  
  	if ( ( qInc < 1.0 ) && ( qInc > 0.0 ) )
  		quantileInc = qInc;
  }
  
  public double decQuantile () {
  	
  	if ( ( quantile - quantileInc ) > 0.0 )
			quantile -= quantileInc;
	else 	quantile = 0.0;
	return quantile;
  }	  		 

  public double incQuantile () {
  	
  	if ( ( quantile + quantileInc ) < 1.0)
			quantile += quantileInc;
	else	quantile = 1.0;
	return quantile;
  }	  		 
  
  public int countNormalUppBd(double mu, double sigma) { return((int)(mu+6*sigma+0.5)); }
  
  public int countNormalLowBd(double mu, double sigma) { return((int)(mu-6*sigma+0.5)); }
  
  public int countLowBd (){
	return(0);
  }
  
  public int countUppBd (){
  
  	int tmpXmax = 10;
	if ( distName.equals("Binomial") ) 
		tmpXmax = (int)(paraValue[0]+0.1);
	
	else {
		int j = -1;
		boolean	outFlag = false;
		double	prevProb = 0.0;
		
		while ( outFlag != true ){
			j++;
			if ( pointYvalue(j) < prevProb ) outFlag = true;
			prevProb = pointYvalue(j);
		}
		tmpXmax = Math.max(10, (j--)*3);
	}
 	return(tmpXmax);
  }
  
  
  
  
  public void setInterval(int lowBd, int uppBd, int tmpType){
  	
  	intervalType = tmpType;
  	double	tmpProb = 0, tmpApproxProb = 0;
  	String 	tmpString = new String("");
  	Poisson poisson = new Poisson(paraValue[0]*paraValue[1]);	
	double mu = paraValue[0] * paraValue[1];
    double sigma = Math.sqrt( mu * ( 1 - paraValue[1] ) );
    Normal normal = new Normal(mu,sigma);
	
  	int normalUppBd = uppBd, normalLowBd = lowBd;
  	
  	switch ( intervalType ) { 
  		case 1: uppBd = countUppBd(); normalUppBd = countNormalUppBd(mu, sigma); 
  				break;
  		case 2: lowBd = countLowBd(); normalLowBd = countNormalLowBd(mu, sigma);
  				break;
  		case 3:
  		case 4: break;
  	}

  	if  ( ( uppBd <= countUppBd() ) && ( lowBd >= countLowBd() ) 
  		  && ( uppBd >= lowBd ) ) {
  		tmpProb =accumYvalue(uppBd) - accumYvalue(lowBd) + pointYvalue(lowBd);
		
		if ( needPoissonApprox ) 
	 		tmpApproxProb = poisson.F(uppBd) - poisson.F(lowBd) + poisson.f(lowBd);
		else if ( needNormalApprox ) 
	 		tmpApproxProb = normal.cdf(normalUppBd + 0.5) - normal.cdf(normalLowBd - 0.5);
	 		
  		switch ( intervalType ) {
  			case 1:
  				tmpString = "P ( X >= " + new Integer(lowBd).toString() +" ) = ";
  				break;
  			case 2:
  				tmpString = "P ( X <= " + new Integer(uppBd).toString() +" ) = ";
  				break;
  			case 3:
  				tmpString = "P ( " + new Integer(lowBd).toString() + " <= X <= " +
  						new Integer(uppBd).toString() + " ) =";
  				break;
  			case 4:
  				tmpString = "P ( X = " + new Integer(lowBd).toString() + " ) = ";
  				break;
  		}
   		probString = tmpString;
   		probStringValue = new Double((int)(tmpProb*10000)/10000.0).toString();
   		if ( new String(approxDistName).length() > 0 ) {
   			approxProbString = getApproxDistName() + " approx:   "; 
   			approxProbValue = new Double((int)(tmpApproxProb*10000)/10000.0).toString();
   		}
  		else approxProbString = "";
  	
  		questionUpper = uppBd;
  		questionLower = lowBd;
  	}
  	else {
   		questionUpper = -1;
  		questionLower = -1;
  		probString = ""; approxProbString = "";
  		probStringValue = ""; approxProbValue = "";
  	}
  }
  
  public void setApproxDistName(String name) {
		approxDistName = new String(name).trim(); 
		if ( needApprox() ) {
			if ( new String(name).charAt(0) == 'P' ) needPoissonApprox = true;	
			if ( new String(name).charAt(0) == 'N' ) needNormalApprox = true;
		}	
  }
  
  public String getApproxDistName() {
		return(approxDistName);  		
  }
  
  public boolean needApprox() {
  		if ( approxDistName.length() == 0 ) 
  				return false;
  		else	return true;
  }

  public String getProbString() {
  	
 		setInterval(questionLower, questionUpper, intervalType);
		return( probString);
  }
 
  public String getProbStringValue() {
  	
		return( probStringValue);
  }
 
  public String getApproxProbString() {
  	
 		//setInterval(questionLower, questionUpper, intervalType);
		return(approxProbString);
  }
 
  public String getApproxProbValue() {
  	
 		//setInterval(questionLower, questionUpper, intervalType);
		return(approxProbValue);
  }
 
 
  public boolean isInterval(int x) {
  	
  	if ( ( x <= questionUpper ) && ( x >= questionLower ) ) 
			return(true);
	else	return(false);
  }  		
  
  public boolean isPoissonApprox() { return needPoissonApprox; }		
  public boolean isNormalApprox() { return needNormalApprox; }		
}
