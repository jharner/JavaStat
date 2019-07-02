package wvustat.dist;


import wvustat.interfaces.*;

/**
 * Chi2Dist implements Chi-square distribution.
 *
 * @author: Hengyi Xue
 * @version: 1.0, May 10, 2000
 */

public class Chi2Dist  implements Distribution{

    private int df;
    private double lowLimit, highLimit;

    public Chi2Dist(int df){
	this.df=df;
	lowLimit=0;
	if(df<5)
	    highLimit=40;
	else if(df<10)
	    highLimit=60;
	else
	    highLimit=80;
    }


    private double Gamma(double x){
	double result;
	
	if(x<=1){
	    
	    if(x>=0.99999){
		result=1.0;		
	    }
	    else{
		result=Math.sqrt(Math.PI);
	    }
	}
	else
	    result=(x-1)*Gamma(x-1);
	
	return result;
    }

    public double pdf(double x) {
    
	double result;
	
	result=Math.pow(x,df/2.0-1)*Math.exp(-x/2.0)/(Gamma(df/2.0)*Math.pow(2,df/2.0));
	return result;
    }

 
    public double cdf(double x) {
	int n=500; //too small?
	double delta=(x-lowLimit)/n;
	double integral=0.0;
	for (int i=0;i<n;i++){
	    double x0=lowLimit+(i+1)*delta;
	    
	    integral=integral+delta*(pdf(x0)+pdf(x0+delta))/2.0;
	}
		
	return integral;
    }

    public double quantile(double y){
	return 0.0;
    }
}
