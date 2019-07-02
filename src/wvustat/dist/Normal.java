package wvustat.dist;


import wvustat.interfaces.*;

/**
* Normal implements normal distribution. Numerical methods are used in the computation.
*
* @author: Hengyi Xue
* @version: 1.4, Mar. 9, 2000
*/
public class Normal  implements Distribution{
	private double param1, param2, lowLimit, highLimit;
	
	private double precision=0.001;

  /**
  * Constructor
  * create a new normal distribution for given mu and sigma
  *
  * @param mu the mean
  * @param sigma the standard deviation
  */
    public Normal(double mu, double sigma) {
		param1=mu;
		param2=sigma;
		lowLimit=mu-5*sigma;
		highLimit=mu+5*sigma;
	}
	
	public double pdf(double x) {
	
		double y;
			
		double z=(x-param1)/param2; //calculate z score
		y=Math.exp(-z*z/2.0)/(Math.sqrt(2.0*Math.PI)*param2);
		return y;
	}
	
	public double cdf(double x) {
	
		int n=100;
		double delta=(x-lowLimit)/n;
		double integral=0.0;
		for (int i=0;i<n;i++){
			double x0=lowLimit+i*delta;
			
			integral=integral+delta*(pdf(x0)+pdf(x0+delta))/2.0;
		}
		
		return integral;
	}
	
	public double quantile(double q) {
		double x0=(highLimit+lowLimit)/2; //start from the middle of the distribution
		
		while(Math.abs(cdf(x0)-q)>precision)
			x0=x0-(cdf(x0)-q)/pdf(x0);
			
		return x0;
	}
}
	
	
