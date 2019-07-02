package wvustat.dist;

import wvustat.interfaces.*;
/**
*	StudentT is an implementation of Student's t distribution.
*
*	@author: Hengyi Xue
*	@version: 1.01, Jan. 26, 2000
*/

public class StudentT implements Distribution {
	
	private int df;
	
	/**
	*	Because the client will use an array of double as parameters to request
	*	a distribution from StatEngine. We have to use double here.
	*/
	public StudentT(double v) {
		df=(int)v;
	}
	
	private double Gamma(double x){
		double result;
		
		if(x<=1)
		{
			if(x>=0.99999)
			{
				result=1.0;		
			}
			else
			{
				result=Math.sqrt(Math.PI);
			}
		}
		else result=(x-1)*Gamma(x-1);
		
		return result;
	}
	
	private double Gf(double y, double a){
		double s=Math.pow(y,a-1)*Math.exp(-y);
		return s;
	}
	
    public double pdf(double x) {
		double y1=Gamma((df+1)/2.0)/Gamma(df/2.0);
		double y2=Math.pow(Math.PI*df,0.5);
		double y3=Math.pow(1+x*x/df,(df+1)/2.0);
		return y1/y2/y3;
	}
	
	public double cdf(double t) {
		double lb=0.0;
		int Num;
		if(df>1) Num=100;
		Num=1000;
		double ub;
		if(t>0) ub=t;
		else ub=-t;
		double Seg=(ub-lb)/Num;
		double Integr=0.0;
		double x0=lb;
		
		for(int i=0;i<Num;i++){
			double Area=(pdf(x0)+pdf(x0+Seg))*Seg/2.0;
			Integr=Integr+Area;
			x0=x0+Seg;
		}
		
		if(t>0) Integr=Integr+0.5;
		else Integr=0.5-Integr;
		return Integr;
	}
	
	public double quantile(double x) {
		double precision=0.0001;//0.001
		double lowLimit, highLimit;
		
		if(df==1){
			lowLimit=-100;
			highLimit=100;
		}
		else if(df==2){
			lowLimit=-15;
			highLimit=15;
		}
		else{
			lowLimit=-10;
			highLimit=10;
		}
		
		double x0=(highLimit+lowLimit)/2; //start from the middle of the distribution
		
		while(Math.abs(cdf(x0)-x)>precision){
			x0=x0-(cdf(x0)-x)/pdf(x0);
		}
			
		return x0;		
	}
		
}
