package wvustat.contdist;

import java.awt.*;
import java.util.*;
import javax.swing.JApplet;

public class continuousInfo {

  // Define variable
  public String	paraType[] =  new String[2];
  public String	paraLatinName[] =  new String[2];

  public double	paraValue[] = new double[2];
  public double	paraUppBd[] = new double[2];
  public double	paraLowBd[] = new double[2];
  public double	paraIncre[] = new double[2];
  public double	paraInit [] = new double[2];
  public double		questionUpper;
  public double		questionLower;
  public double		chi_sq_cont;
  public int		intervalType = 0;
  
  public double	quantile = 0.5;
  public double	quantileInc = 0.05;	
  
  public boolean	changeInterval = false;
  public boolean	doRescale = true;
  public boolean	isNormal = true;
  public boolean	isChi_Sq = false;
  
  public String probString = new String("");
  public String probStringValue = new String("");
  public String approxProbString = new String("");
  public String approxProbValue = new String("");
  
  public String distName;
  public String fOrF = new String("f"); 
  
  public String paraSign[] = new String[2];
  
  final	double	PI = 3.1415926;
  
  private double xmin, xmax, ymin, ymax;
  
  // Constructor
  public continuousInfo ( String tmpString, String display ) {

	distName = new String(tmpString);
	
	xmin = xmax = ymin = ymax = 0.0;
	
	if ( new String(display).equals("CDF"))
			fOrF = "F";
	else	fOrF = "f";
	
	if ( distName.equals("Normal")) {

  		isNormal	= true;
  		isChi_Sq	= false;
		
		paraValue[0] = 0.0;
  		paraValue[1] = 1.0;
		paraInit[0] = 0.0;
  		paraInit[1] = 1.0;
  		
  		paraUppBd[0] = 99999.000000001;
  		paraUppBd[1] = 99999.000000001;
  		paraLowBd[0] = -99999.000000001;
  		paraLowBd[1] = 1e-8;
  		quantile = 0.50;
  		
  		questionUpper = -2E8;
  		questionLower = -2E8;
  		
  		paraIncre[0] = 0.2;
  		paraIncre[1] = 0.2;

  		paraType[0] = "double";
  		paraType[1] = "double";
  		
  		paraLatinName[0] = "mu";
  		paraLatinName[1] = "sd";

  	}
  			
  	else {
  		isNormal	= false;
  		isChi_Sq	= true;

		paraValue[0] = 1.0;
		paraInit[0] = 1.0;
		paraUppBd[0] = 40.000000001;
		paraLowBd[0] = 1-1E-8;

		questionUpper = -2E8;
		questionLower = -2E8;
  		
		paraIncre[0] = 1.0;

  		paraType[0] = "double";
  		paraLatinName[0] = "df";
	}

  }
  
  public void reset () {
		
	doRescale = true;
  	changeInterval = false;
  	fOrF = "f";
  	intervalType = 0;
  	probString = "";
	
	if (isNormal){

		paraValue[0] = paraInit[0];
  		paraValue[1] = paraInit[1];
  		quantile = 0.50;
  		questionUpper = -2E8;
  		questionLower = -2E8;
  		paraIncre[0] = 0.2;
  		paraIncre[1] = 0.2;
  	}
  		
  	else{

	//	paraValue[0] = paraInit[0];
	//	questionUpper = -1;
	//	questionLower = -1;
	//	paraIncre[0] = 1.0;
	}
  }

  
  // isPDF
  public boolean isPDF( )	{
  
	if ( fOrF.equals("f"))
  		return(true);
  	else
  		return(false);
  }
  
  public void setChiSqCont() {
  	
  	chi_sq_cont = Gamma((int)(paraValue[0]+1E-3))*Math.pow(2.0,paraValue[0]/2.0);
  	
  }
  
  public void setInitPara(String paraString, int index ) {

  	
  	double	paraValueTmp = Double.valueOf(paraString).doubleValue();
  	
  	if ( ( paraValueTmp > paraLowBd[index] ) && ( paraValueTmp < paraUppBd[index] ) )
  		paraValue[index] = paraValueTmp;
  		paraInit[index] = paraValueTmp;
  
  }

  // ifHavePara2

  public boolean ifHavePara2 ( ) {
	if ( distName.equals("Normal"))
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
  	
  	if (isChi_Sq)	return(new String("r"));
  	else	return("");
  }


  public String getImageFileName(int index) {
  	
 	return(new String(paraLatinName[index] + ".jpg"));
  
  }
  
  // Count Xmin,xmax,ymin,ymax
  public double countXmin ( ){
	
  	if ( !doRescale ) return(xmin);
	
	if ( isNormal ) 
			xmin = 	paraValue[0] - 6*paraValue[1];
	else 	xmin = 1E-6;
	
	return(xmin);
  }

  public double countYmin ( ){
	return(1E-6);
  }
  
  public double countXmax ( ){
  	
  	if ( !doRescale ) return(xmax);
	
	if ( isNormal )
			xmax = paraValue[0] + 6*paraValue[1];
	else 	xmax = paraValue[0] * 2.0 + 2.0;
	
	return(xmax);		
  }

  public double countYmax ( ){
		
 	if ( !isPDF())	{ ymax = 0.9999; return(0.9999); }
 	
 	if ( !doRescale ) return(ymax);
  		
	if ( isNormal )	
			ymax = pointYvalue(paraValue[0]);//Normal
	else	{
		if ( paraValue[0] == 1 ) 
			ymax = 20.0;//Chi_square
		else if ( paraValue[0] == 2 ) 
			ymax = 10.0;//Chi_square
		else
			ymax = pointYvalue(paraValue[0]-2.0);//Chi_square
	}
	
	return(ymax);
  }
  
  // compute Y value given x
  public double pointYvalue ( double pos ){

	if ( isNormal ) {	
		return(	1.0/(Math.exp(Math.pow((pos-paraValue[0])/paraValue[1], 2.0)/2.0)*
				Math.sqrt(2*PI)*paraValue[1]));
	}

	else	return(Math.exp((paraValue[0]/2.0-1)*Math.log(pos)-pos/2.0)/chi_sq_cont);

		
  }
  
  // compute accumulate Y value
  public double accumYvalue ( double pos ){

	double v = Math.abs((pos-paraValue[0])/paraValue[1]);
	double p;
	
	p = Math.exp(-(((83*v)+351)*v+562)*v/(703+165*v));
	if ((pos-paraValue[0])>0)	return(1-0.5*p);
	else		return(0.5*p);
	
  }
  
  // putParaValue
  public void putParaValue(continuousDist tmpApplet) {
	
	double tmp[] = new double[2];
	  	
	tmp[0] =	Double.valueOf(tmpApplet.
				textFieldPara1.getText()).doubleValue();
	if ( ( tmp[0] <= paraUppBd[0] ) && ( tmp[0] >= paraLowBd[0] ) )
		paraValue[0] = tmp[0];
	
	if (isNormal) {
		tmp[1] =	Double.valueOf(tmpApplet.
					textFieldPara2.getText()).doubleValue();
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
  
  public void decQuantile () {
  	
  	if ( ( quantile - quantileInc ) > 0.0 )
			quantile -= quantileInc;
	else 	quantile = 0.0;
  }	  		 

  public void incQuantile () {
  	
  	if ( ( quantile + quantileInc ) < 1.0)
			quantile += quantileInc;
	else	quantile = 1.0;
  }	  		 
  
  public void setInterval(double lowBd, double uppBd, int tmpType){
  	
  	intervalType = tmpType;
  	double	tmpProb = 0;
  	String 	tmpString = new String("");

  	if ( changeInterval ) {
  		changeInterval = false;
  	}
  	
  	if ( tmpType == 1 ) {
  		if ( isNormal ) 
			uppBd = paraValue[0] + 6*paraValue[1];
		else 	uppBd = paraValue[0] * 2.0 + 2.0;
	}
	
  	if ( tmpType == 2 ) {
  		if ( isNormal ) 
			lowBd = paraValue[0] - 6*paraValue[1];
		else 	lowBd = 1E-6;
  	}

  	if  ( ( lowBd > -1E8) && ( uppBd > -1E8)
  			&& (uppBd >= lowBd) ) {
  		tmpProb = Math.round((accumYvalue(uppBd) - accumYvalue(lowBd))*10000)/10000.0;
  		switch ( intervalType ) {
  			case 1:
  				tmpString = "  P( X >= " + new Double(lowBd).toString() + " ) = ";
  				approxProbString = "  P( Z >= " + StandNormalString(lowBd, 6) + " ) = ";
  				break;
  			case 2:
  				tmpString = "  P( X <= " + new Double(uppBd).toString() + " ) = ";
  				approxProbString = "  P( Z <= " + StandNormalString(uppBd, 6) + " ) = ";
  				break;
  			case 3:
  				tmpString = "  P(" + new Double(lowBd).toString() + "<=X<=" +
  						new Double(uppBd).toString() + ") = ";
   				approxProbString = "  P(" + StandNormalString(lowBd, 6) + "<=Z<=" +
   						StandNormalString(uppBd, 6) + ") = ";
 				break;
  			case 4:
  				tmpString = "  P( X = " + new Double(lowBd).toString() + " ) = ";
  				approxProbString = "  P( Z = " + StandNormalString(lowBd, 6) + " ) = ";
  				break;
  		}
   		probString = tmpString;
  		if (intervalType == 4) 
   			tmpString = "0.0";
   		else {
   			String tmpProbString = new Double(tmpProb).toString();
   			probStringValue = tmpProbString.substring(0, Math.min(tmpProbString.length(), 6));
   			approxProbValue = probStringValue;
   		}
   			
  		questionUpper = uppBd;
  		questionLower = lowBd;
  		probString = tmpString;
  	}
  	else {
   		questionUpper = -2E8;
  		questionLower = -2E8;
  		probString = ""; approxProbString = "";
  		probStringValue = ""; approxProbValue = "";
  	}
  	if ( !isPDF() || !isNormal )	{ approxProbString = ""; approxProbValue = ""; }
  }

  public String StandNormalString(double v, int decimal) {
  	
  		double value = StandNormal(v, paraValue[0], paraValue[1]);
  		String tmpString = new Double(value).toString();
  		return tmpString.substring(0, Math.min(tmpString.length(), decimal));
  }
  
  public double StandNormal(double value, double mu, double sigma) {
  
  		return( (value-mu)/sigma );
  }
  
  
  public String getProbString() {
  	
 		setInterval(questionLower, questionUpper, intervalType);
		return( probString);
 
  }
 
  public String getProbStringValue() {
  	
		return( probStringValue);
  }
 
  public double setXTickMarkInterval(double diff) {

		return(Math.round(diff*10/4+0.499)/10.0);		
  
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
  		
  public double computeQuantile() {
  	
  	double lowerBd	= countXmin();
  	double upperBd	= countXmax();
  	double middle	= (lowerBd + upperBd)/2.0;

  	if ( Math.abs(quantile-1.0) < 1E-4) return(upperBd);
  	if ( Math.abs(quantile-0.0) < 1E-4) return(lowerBd);
  	
  	while (Math.abs(accumYvalue(middle)-quantile)>1E-4) {

  		if	( accumYvalue(middle) > quantile ) upperBd = middle;
  		else lowerBd = middle;
  		
  		middle	= (lowerBd + upperBd)/2.0;
  	}
  	return(middle);
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
