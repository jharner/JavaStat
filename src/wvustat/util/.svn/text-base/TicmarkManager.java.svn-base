package wvustat.util;
/**
 *	BaseAxisModel is used to find the 'best' ticmarks for an axis.
 */

public class TicmarkManager implements java.io.Serializable{
    //Constants for scaleType
    public static final int MIN_FIXED=0;
    public static final int MAX_FIXED=1;    
    private double start, end;
    private double interval;
    private int NumOfTics;

    private double min, max;
    private int scaleType;

    /**
     * Constructor
     * Creates a new BaseAxisModel with the specified range and initial number of sections
     */
    public TicmarkManager(double min, double max, int NumOfTics)
    {
        this.min=min;
        this.max=max;
        this.NumOfTics=NumOfTics;
        
        double factor;
        interval=(max-min)/NumOfTics;  

        if(interval<1|| interval>10){
            factor=Math.floor(Math.log(interval)/Math.log(10));
            interval=roundDown(interval*Math.pow(10,-factor))*Math.pow(10, factor);
        }
        else
        interval=roundDown(interval);

        
        int dgs=sigDigits(interval);


        if(min>0 && min< interval){
            start=0;
        }
        else if(Math.abs(min)>1){
            double startVal1=(int)(min*Math.pow(10,-dgs))*Math.pow(10,dgs);
            double startVal2=(int)(min*Math.pow(10,-dgs+1))*Math.pow(10, dgs-1);
            start=Math.abs(min-startVal1)<Math.abs(min-startVal2)?startVal1:startVal2;
            if(start>min) //This should only happen when min is negative
                start=start-interval;
        }
        else{
            double startVal1=(int)(min*Math.pow(10,dgs))*Math.pow(10, -dgs);
            double startVal2=(int)(min*Math.pow(10, dgs+1))*Math.pow(10, -1-dgs);
            start=Math.abs(min-startVal1)<Math.abs(min-startVal2)?startVal1:startVal2;
            if(start>min) //This should only happen when min is negative
                start=start-interval;
        }

        end=start;
        while(end<max)
        end+=interval; 
        
        
    }
    
    public TicmarkManager(double min, double max, int NumOfTics, int scaleType)
    {
        this.min=min;
        this.max=max;
        this.NumOfTics=NumOfTics;

        interval=(max-min)/NumOfTics;

        double factor;

        if(interval<1|| interval>10){
            factor=Math.floor(Math.log(interval)/Math.log(10));
            interval=roundDown(interval*Math.pow(10,-factor))*Math.pow(10, factor);
        }
        else
        interval=roundDown(interval);

        if(scaleType!=MIN_FIXED){
            int dgs=sigDigits(interval);

            if(min>0 && min< interval){
                start=0;
            }
            else if(min>1){
                double startVal1=(int)(min*Math.pow(10,-dgs))*Math.pow(10,dgs);
                double startVal2=(int)(min*Math.pow(10,-dgs+1))*Math.pow(10, dgs-1);
                start=Math.abs(min-startVal1)<Math.abs(min-startVal2)?startVal1:startVal2;
            }
            else{
                double startVal1=(int)(min*Math.pow(10,dgs))*Math.pow(10, -dgs);
                double startVal2=(int)(min*Math.pow(10, dgs+1))*Math.pow(10, -1-dgs);
                start=Math.abs(min-startVal1)<Math.abs(min-startVal2)?startVal1:startVal2;
            }
        }
        
        if(scaleType!=MAX_FIXED){
            end=start;
            while(end<max)
            end+=interval;
        }

    }    

    /**
     * Get the lower limit on the scale
     */
    public double getMin(){
        return start;
    }

    /**
     * Get the upper limit on the scale
     */
    public double getMax(){

            return end;
    }

    /**
     * Get the size of a section on the scale
     */
    public double getInterval(){
        return interval;
    }
    
    public int getNumOfIntervals(){
        int ret=(int)((end-start)/interval)+1;
        
        return ret;
    }
    
    private double roundDown(double d){
        if((d-(int)d)<0.01)
        return (int)d;

        int m=(int)(d/0.5);
        double ret=Math.abs(m*0.5-d)<Math.abs((m+1)*0.5-d)?m*0.5:(m+1)*0.5;
        return ret;
    }

    // Return the significant digits in d
    private int sigDigits(double d){
        //if d is a number like 10 or 100, we have to deal with it separately
        if(d>1 && Math.abs(Math.log(d)/Math.log(10)-(int)(Math.log(d)/Math.log(10)))<0.01)
        return (int)(Math.log(d)/Math.log(10))+1;
        else
        return (int)Math.abs(Math.ceil(Math.log(d)/Math.log(10)));
    }    

}


