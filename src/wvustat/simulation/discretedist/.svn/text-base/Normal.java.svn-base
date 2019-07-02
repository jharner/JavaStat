package wvustat.simulation.discretedist;


public class Normal
{
	
	double param1, param2; 
	double lowLimit, highLimit;  
	double precision=0.001; 
	
	Normal(double mu, double sigma)
	{
		param1=mu;
		param2=sigma;
		lowLimit=mu-5*sigma;
		highLimit=mu+5*sigma;
	}
	
	public double pdf(double x)
	{
		double y;
			
		double z=(x-param1)/param2; //calculate z score
		y=Math.exp(-z*z/2.0)/(Math.sqrt(2.0*Math.PI)*param2);
		return y;
	}

	
	public double cdf(double x)
	{
		double v = Math.abs((x-param1)/param2);
		double p;
	
		p = Math.exp(-(((83*v)+351)*v+562)*v/(703+165*v));
		if ((x-param1)>0)	return(1-0.5*p);
		else		return(0.5*p);
	
	}
	
	public double Quantile(double q)
	{
		double x0=(highLimit+lowLimit)/2; 
		
		while(Math.abs(cdf(x0)-q)>precision)
			x0=x0-(cdf(x0)-q)/pdf(x0);
			
		return x0;
	}
}
