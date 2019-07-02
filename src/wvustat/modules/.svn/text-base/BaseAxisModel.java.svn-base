package wvustat.modules;

/**
 *	BaseAxisModel is used to find the 'best' ticmarks for an axis.
 */

public class BaseAxisModel 
{
    //Constants for scaleType
	public static final int NORMAL = 0;     //00
    public static final int MIN_FIXED = 1;  //01
    public static final int INT_SCALE = 2;  //10

    protected double startValue;
    protected double endValue;
    protected double interval;
    

    public BaseAxisModel()
    {}

    /**
     * Constructor
     * Creates a new BaseAxisModel with the specified range and initial number of sections
     */
    public BaseAxisModel(double min, double max, int NumOfIntervals)
    {
        this(min, max, NumOfIntervals, NORMAL);
    }
    
    public BaseAxisModel(double min, double max, int NumOfIntervals, int scaleType)
    {
    	double factor;
        
        if (NumOfIntervals == 0)
        	NumOfIntervals = 1;
        
        if (Math.abs(min - max) < 0.00001) //if min == max
            interval = 1;
        else
            interval = (max - min) / NumOfIntervals;

        if (interval < 1 || interval > 10)
        {
            factor = Math.floor(Math.log(interval) / Math.log(10));
            interval = roundDown(interval * Math.pow(10, -factor)) * Math.pow(10, factor);
        }
        else {
            interval = roundDown(interval);
        }
        
        if ((scaleType & INT_SCALE) == INT_SCALE)
        	interval = Math.max(Math.round(interval), 1);

        if ((scaleType & MIN_FIXED) != MIN_FIXED)
        {
        	int dgs = sigDigits(interval);

        	if (min > 0 && min < interval)
        	{
        		startValue = 0;
        	}
        	else if (Math.abs(min) > 1)
        	{
        		double startVal1 = (int) (min * Math.pow(10, -dgs)) * Math.pow(10, dgs);
        		double startVal2 = (int) (min * Math.pow(10, -dgs + 1)) * Math.pow(10, dgs - 1);
        		startValue = Math.abs(min - startVal1) < Math.abs(min - startVal2) ? startVal1 : startVal2;
        		while (startValue > min) //This should only happen when min is negative
        			startValue = startValue - interval;
        	}
        	else
        	{
        		double startVal1 = (int) (min * Math.pow(10, dgs)) * Math.pow(10, -dgs);
        		double startVal2 = (int) (min * Math.pow(10, dgs + 1)) * Math.pow(10, -1 - dgs);
        		startValue = Math.abs(min - startVal1) < Math.abs(min - startVal2) ? startVal1 : startVal2;
        		while (startValue > min) //This should only happen when min is negative
        			startValue = startValue - interval;
        	}
        }
        else
        	startValue = min;

        
        endValue = startValue;
        if (Math.abs(min - max) >= 0.00001) { //if min != max
        	while (endValue <= max) // < to <=
        		endValue += interval;
        } else {
        	for (int i = 0; i < NumOfIntervals; i++)
        		endValue += interval;
        }        
    }
    
    /**
     * Get the lower limit on the scale
     */
    public double getStartValue()
    {
        return startValue;
    }

    public void setStartValue(double startValue)
    {
        this.startValue = startValue;
    }

    /**
     * Get the upper limit on the scale
     */
    public double getEndValue()
    {
        return endValue;
    }
    
    public void setEndValue(double endValue)
    {
        this.endValue = endValue;
    }

    public void setInterval(double interval)
    {
        this.interval = interval;
    }

    /**
     * Get the size of a section on the scale
     */
    public double getInterval()
    {
        return interval;
    }

    public int getNumOfIntervals()
    {
        int ret = (int) Math.round((endValue - startValue) / interval);

        return ret;
    }
    
    public int getNumOfMinorTicks()
    {
        return 0;
    }

    public void setNumOfMinorTicks(int numOfMinorTicks)
    {

    }
    
    public boolean isManual()
    {
    	return false;
    }

    // Round decimal to closest integer or integer+0.5
    private double roundDown(double d)
    {
        if ((d - (int) d) < 0.01)
            return (int) d;

        int m = (int) (d / 0.5);
        double ret = Math.abs(m * 0.5 - d) < Math.abs((m + 1) * 0.5 - d) ? m * 0.5 : (m + 1) * 0.5;
        return ret;
    }

    // Return the number of (significant) digits in the integer part of d. 
    // eg. When d=10 or 20, returns 2. When d=100 or 200, returns 3.
    private int sigDigits(double d)
    {
        //if d is a number like 10 or 100, we have to deal with it separately
        if (d > 1 && Math.abs(Math.log(d) / Math.log(10) - (int) (Math.log(d) / Math.log(10))) < 0.01)
            return (int) (Math.log(d) / Math.log(10)) + 1;
        else
            return (int) Math.abs(Math.ceil(Math.log(d) / Math.log(10)));
    }

}


