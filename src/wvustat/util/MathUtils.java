package wvustat.util;

import java.rmi.*;

/**
 *	MathUtils implements a utitlity class that can be used for sorting an array
 *	and other simple operations. All the methods are static and can be used
 * without instantiating a new instance.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Nov. 3, 1999
 */

public class MathUtils{
        /**
         * Get the minimum value in an array
         */
    public static double getMin(double[] vals){
        if(vals.length>0)
        {
        double result=vals[0];

        for(int i=1;i<vals.length;i++){
            if(result>vals[i])
                result=vals[i];
        }

        return result;
        }
        else
            return 0;
    }
    
        /**
         * Get the maximum value in an array
         */
    public static double getMax(double[] vals){
        double result=vals[0];
        
        for(int i=1;i<vals.length;i++){
            if(result<vals[i])
                result=vals[i];
        }
        
        return result;
    }
    
        /**
         * Sort an double array using insertion sort
         */
    public static void InsertionSort(double[] x){
        double key;
        int i;
        for(int j=1;j<x.length;j++)
        {
            key=x[j];
            i=j-1;
            while( i>=0 && x[i]>key)
            {
                x[i+1]=x[i];
                i--;
            }
            x[i+1]=key;
        }
    }

    /**
     * Sort a String array using the order of another double array by insertion sort
     */
    public static void InsertionSort(String[] y, double[] array){
    	double[] x=new double[array.length];
		for(int i=0;i<array.length;i++) x[i]=array[i];
    		
		double key;
    		String s;
    		int i;
    		for(int j=1;j<x.length;j++)
    		{
    			key=x[j]; s=y[j];
    			i=j-1;
    			while( i>=0 && x[i]>key)
    			{
    				x[i+1]=x[i]; y[i+1]=y[i];
    				i--;
    			}
    			x[i+1]=key; y[i+1]=s;
    		}
    }
    
    /**
     * Sort a String array using the order of another double array by insertion sort
     */
    public static void InsertionSort(double[] y, double[] array){
    	double[] x=new double[array.length];
		for(int i=0;i<array.length;i++) x[i]=array[i];
    		
		double key;
    		double s;
    		int i;
    		for(int j=1;j<x.length;j++)
    		{
    			key=x[j]; s=y[j];
    			i=j-1;
    			while( i>=0 && x[i]>key)
    			{
    				x[i+1]=x[i]; y[i+1]=y[i];
    				i--;
    			}
    			x[i+1]=key; y[i+1]=s;
    		}
    }
    
        /**
         * Sort an string array using isertion sort
         */
    public static void InsertionSort(String[] x){
        String key;
        
        int i;
        for(int j=1;j<x.length;j++)
        {
            key=x[j];
            i=j-1;
            while( i>=0 && x[i].compareTo(key)>0)
            {
                x[i+1]=x[i];
                i--;
            }
            x[i+1]=key;
        }
    }
    /**
     * Sort an array of Comparable using insertion sort algorithm
     */
    public static void InsertionSort(Comparable[] x){
        Comparable key;
        
        int i;
        for(int j=1;j<x.length;j++)
        {
            key=x[j];
            i=j-1;
            while( i>=0 && x[i].compareTo(key)>0)
            {
                x[i+1]=x[i];
                i--;
            }
            x[i+1]=key;
        }
    }

    		/**
    		 * Reverse a String array
    		 */
    public static void reverse(String[] b) {
    	   	int left  = 0;          // index of leftmost element
    	   	int right = b.length-1; // index of rightmost element
    	  
    	   	while (left < right) {
    	   		// exchange the left and right elements
    	   		String temp = b[left]; 
    	   		b[left]  = b[right]; 
    	   		b[right] = temp;
    	     
    	   		// move the bounds toward the center
    	   		left++;
    	   		right--;
    	   	}
    	}
    
        /**
         * Get the summation of values in an array
         */
    public static double getSum(double[] vals){
        double sum=0;
        
        for(int i=0;i<vals.length;i++)
            sum+=vals[i];
        
        return sum;
    }
    
        /**
         * Get average value for values in an array
         */
    public static double getMean(double[] vals){
        double sum=getSum(vals);
        return (sum/vals.length);
    }
    
        /**
         * Get standard deviation for values in an array
         */
    public static double getStdDev(double[] vals){
        double sum2=0,sum=0;
        int n=vals.length;
        
        for(int i=0;i<vals.length;i++){
            sum2+=vals[i]*vals[i];
            sum+=vals[i];
        }
        
        double result=1.0*(sum2-sum*sum/n)/(n-1);
        return Math.sqrt(result);
    }
    
    public static double getSS(double[] vals){
        double sum2=0,sum=0;
        int n=vals.length;
        
        for(int i=0;i<vals.length;i++){
            sum2+=vals[i]*vals[i];
            sum+=vals[i];
        }
        
        double result=sum2-sum*sum/n;
        return result;
    }    
    
    public static double getSE(double[] vals){
        return getStdDev(vals)/Math.sqrt(vals.length);
    }
    
        /**
         * This method finds the minimum, lower quartile, median, upper quartile and max  for an array
         * of values (not sorted) and return them in an array in ascending order
         */
    public static double[] getQuantiles(double[] val){
        if(val.length==0)
            return new double[]{0,0,0,0,0};

        //first, make a copy of the original array
        double[] sorted=new double[val.length];
        
        for(int i=0;i<val.length;i++)
            sorted[i]=val[i];
        
        InsertionSort(sorted);
        
        double median, lq, uq;
        int n=sorted.length;

        if(n%2==0)
            median=0.5*(sorted[n/2-1]+sorted[n/2]);
        else
            median=sorted[n/2];
        
        if((n+1)%4==0){
            lq=sorted[(n+1)/4-1];
            uq=sorted[(n+1)*3/4-1];
        }
        else{
            double tmpd=(n+1)/4.0;
            int tmpi=(int)tmpd;
            double fr1=tmpi/(n+1.0);
            double fr2=(tmpi+1)/(n+1.0);
            if(tmpi > 0)
            		lq=(sorted[tmpi]-sorted[tmpi-1])/(fr2-fr1)*(0.25-fr1)+sorted[tmpi-1];
            else 
            		lq=sorted[0]; //added by djluo for n<=2
            
            tmpd=3*tmpd;
            tmpi=(int)tmpd;
            fr1=tmpi/(n+1.0);
            fr2=(tmpi+1)/(n+1.0);
            
            if(tmpi < n )
            		uq=(sorted[tmpi]-sorted[tmpi-1])/(fr2-fr1)*(0.75-fr1)+sorted[tmpi-1];
            else
            		uq=sorted[n-1]; //added by djluo for n<=2
        }
        
        double min=sorted[0];
        double max=sorted[n-1];
        
        return new double[]{min, lq, median, uq, max};
    }
    
  /**
   * Ge the rank for each value in an array
   */
    public static int[] getRanks(double[] array){
        int[] rank=new int[array.length];
        
        for(int i=0;i<array.length;i++) rank[i]=1;
        
        for(int i=0;i<array.length;i++){
            for(int j=i+1;j<array.length;j++){
                if(array[i]>array[j])
                    rank[i]++;
                else
                    rank[j]++;
            }
        }
        
        return rank;
    }
    
    
        /**
         * Perform a linear regression on arrays y and x and return coefficients a and b,  correlation
         * coefficient rou, standard error of b, p value of b=0, sum of squared error for x, sum of residual for prediction
         * divided by degree of freedom
         */
    public static double[] regress(double[] y, double[] x) throws RemoteException{
        double[] result=new double[11];
        
        // This is y'x
        double prod=0;
        
        //this is (x-x_bar)'(x-x_bar)
        double xres=0;
        //This is (y-y_bar)'(y-y_bar);
        double yres=0;
        //This is (y-y_bar)'(x-x_bar);
        double xyres=0;
        //This is (y-y_hat)'(y-y_hat)
        double predres=0;
        
        int n=y.length;
        double sumx=getSum(x);
        double sumy=getSum(y);
        
        double xbar=sumx/n;
        double ybar=sumy/n;
        
        for(int i=0;i<n;i++){
            prod+=x[i]*y[i];
            
            xres+=(x[i]-xbar)*(x[i]-xbar);
            yres+=(y[i]-ybar)*(y[i]-ybar);
            xyres+=(y[i]-ybar)*(x[i]-xbar);
        }
        
        //This is a1 or b
        result[1]=(prod-sumx*sumy/n)/xres;
        //this is a0 or a
        result[0]=sumy/n-result[1]*sumx/n;
        //this is correlation coefficient
        result[2]=xyres/Math.sqrt(xres*yres);
        
        for(int i=0;i<n;i++){
            predres+=Math.pow((y[i]-result[0]-result[1]*x[i]),2);
        }
        
        //This is standard error for b1
        result[3]=Math.sqrt(predres/(n-2)/xres);
        
        double tval=Math.abs(result[1]/result[3]);
        
        wvustat.dist.StudentT t=new wvustat.dist.StudentT(n-2);
        
        double pval=2*(1-t.cdf(tval));
        
        //This is p value
        result[4]=pval;
        
        //This is x bar
        result[5]=sumx/n;
        
        //This is y bar
        result[6]=sumy/n;
        
        //This is the standard deviation for x
        result[7]=Math.sqrt(xres/(n-1));
        
        //This is the standard deviation for y
        result[8]=Math.sqrt(yres/(n-1));
        
        //this is sum of squared residual for prediction divided by degree of freedom
        result[9]=predres/(n-2);
        
        result[10]=Math.sqrt((yres+n*ybar*ybar)/(n-2));
        
        return result;
    }

    public static double computeChiSquare(int[] freqs, double[] probs){
        int sum=0;
        for (int i = 0; i < freqs.length; i++) {
            sum+=freqs[i];
        }

        double chi=0;
        for(int i=0;i<freqs.length;i++){
            double t=freqs[i]-sum*probs[i];
            chi+=t*t/freqs[i];
        }

        return chi;
    }



}




