
package wvustat.dist;

import wvustat.interfaces.*;

/**
 * @author dajieluo
 *
 */
public class Kernel implements Distribution{
	private double param1;
	private double[] param2;
	
	public Kernel(double bandwidth, double[] x) {
		param1=bandwidth;
		param2=x;
	}
	
	public double pdf(double x) {
		double y=0;
		
		for(int i=0; i<param2.length; i++){
			double z=(x-param2[i])/param1;
			y = y + gaussian(z);
		}
		 
		y = y/(param2.length*param1);
		
		return y;
	}
	
	private double gaussian(double z){
		double y = Math.exp(-z*z/2.0)/(Math.sqrt(2.0*Math.PI));
		return y;
	}
	
	public double cdf(double x) {
		return -1;
	}
	
	public double quantile(double q) {
		return -1;
	}
}
