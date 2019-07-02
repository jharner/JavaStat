package wvustat.data;

import java.util.*;
import java.rmi.RemoteException;

import wvustat.interfaces.*;
import wvustat.util.MathUtils;

/**
 *	NumVariable implements numerical variable.
 *
 *
 */

public class NumVariable extends VariableImpl
{

    private transient double stdDev;
    private transient double[] sorted;

    /**
     * Constructor
     *
     * @param n the name of the variable
     * @param v the values of the variable
     */
    public NumVariable(String n, Vector v)
    {
        super(n, v);
        type = Variable.NUMERIC;
        //calcFields();
    }

    /**
     * Get the numerical values contained in the variable
     */
    public synchronized double[] getNumValues()
    {
        int numValues = objValues.size();
        double[] values = new double[numValues];
        for (int i = 0; i < numValues; i++)
        {
            values[i] = ((Double) objValues.elementAt(i)).doubleValue();
        }
        return values;
    }

    /**
     * Get indexed value contained in the variable
     */
    public synchronized Object getValue(int index)
    {
        if (index >= objValues.size())
        {
            System.err.println("Request non-existing object");
            return null;
        }
        else
            return objValues.elementAt(index);
    }

    /**
     * Get sorted numerical values contained in the variable
     */
    public synchronized double[] getSortedNumValues()
    {
    	calcFields();
        return sorted;
    }

    /**
     * Get categorical values for the numerical values contained in the variable
     */
    public String[] getCatValues()
    {
        double[] values = this.getNumValues();
        String[] catValues = new String[values.length];
        for (int i = 0; i < catValues.length; i++)
        {
            if (Double.isNaN(values[i]))
                catValues[i] = "";
            else
                catValues[i] = String.valueOf(values[i]);

        }

        return catValues;
    }

    /**
     * This method returns null
     */
    public String[] getSortedCatValues()
    {
        return null;
    }

    /**
     * Get distinct categorical values contained in the variable
     */
    public String[] getDistinctCatValues()
    {
        Set hashSet = new HashSet(Arrays.asList(getCatValues()));
        hashSet.remove(Variable.CAT_MISSING_VAL); //added by djluo
        Object[] array = hashSet.toArray();
        String[] sArray = new String[array.length];
        for (int i = 0; i < array.length; i++)
        {
            sArray[i] = (String) array[i];
        }
        return sArray;
    }

    public synchronized int getNumOfDistinctCatValues() {
    	Set hashSet = new HashSet(Arrays.asList(getCatValues()));
        hashSet.remove(Variable.CAT_MISSING_VAL); //added by djluo
        return hashSet.size();
    }
    
    /**
     * Get the minimum value
     */
    public synchronized double getMin() throws RemoteException
    {
        double min=Double.MAX_VALUE;
        for(int i=0;i<getSize();i++){
            Double obj=(Double)objValues.get(i);
            if(!Double.isNaN(obj.doubleValue()) && obj.doubleValue()<min
                    && (parent == null || !parent.getMask(i)))
                min=obj.doubleValue();
        }
        if (min == Double.MAX_VALUE) 
        	throw new RemoteException("empty variable"); //added by djluo
        return min;
    }

    /**
     * Get the maximum value
     */
    public synchronized double getMax() throws RemoteException
    {
        double max=-Double.MAX_VALUE;
        for(int i=0;i<getSize();i++){
            Double obj=(Double)objValues.get(i);
            if(!Double.isNaN(obj.doubleValue()) && obj.doubleValue()>max
                    && (parent == null || !parent.getMask(i)))
                max=obj.doubleValue();
        }
        if (max == -Double.MAX_VALUE) 
        	throw new RemoteException("empty variable"); //added by djluo
        return max;
    }

    /**
     * Get the mean value
     */
    public synchronized double getMean()
    {
        double mean=0;
        int count=0;
        for(int i=0;i<getSize();i++){
            Double obj=(Double)objValues.get(i);            
            if(!Double.isNaN(obj.doubleValue()) && (parent == null || !parent.getMask(i))){
                mean+=obj.doubleValue();
                count++;
            }
        }
        if(count!=0)
            return mean/count;
        else
            return Double.NaN;
    }

    /**
     * Get the standard deviation
     */
    public synchronized double getStdDev() 
    {
    	calcFields();
        return stdDev;
    }

    /**
     * Change the indexed value
     *
     *	@param obj the new value
     * @param i the index of the value to change
     */
    public synchronized void setValue(Object obj, int i)
    {
        if (obj instanceof Double)
        {
            synchronized (this)
            {
                objValues.setElementAt(obj, i);
                //calcFields();
            }

        }
        else {
        	System.err.println("value not number");
            throw new IllegalArgumentException("Only Double can be put into numerical variable");
        }
    }

    /**
     * Append a new value to the variable
     */
    public synchronized void addValue(Object obj)
    {
        if (obj instanceof Double)
        {
            synchronized (this)
            {
                objValues.addElement(obj);
                //calcFields();
            }
        }
        else
            throw new IllegalArgumentException("Only Double can be put into numerical variable");
    }

    /**
     * Get quantiles for this variable
     *
     * @return an double array that contains lower quartile, median, upper quartile
     */
    public synchronized double[] getQuantiles()
    {
    	calcFields();
    	
        double[] q = new double[3];
        //median, lower quartile and upper quartile
        double median, lq, uq;
        int n = sorted.length;
        if (n % 2 == 0) //even
            median = 0.5 * (sorted[n / 2 - 1] + sorted[n / 2]);
        else //odd
            median = sorted[n / 2];

        if ((n + 1) % 4 == 0)
        {
            lq = sorted[(n + 1) / 4 - 1];
            uq = sorted[(n + 1) * 3 / 4 - 1];
        }
        else
        {
            double tmpd = (n + 1) / 4.0;
            int tmpi = (int) tmpd;
            double fr1 = tmpi / (n + 1.0);
            double fr2 = (tmpi + 1) / (n + 1.0);
            if(tmpi > 0)
                lq = (sorted[tmpi] - sorted[tmpi - 1]) / (fr2 - fr1) * (0.25 - fr1) + sorted[tmpi - 1];
            else 
        		lq=sorted[0];  //added by djluo for n<=2
            
            tmpd = 3 * tmpd;
            tmpi = (int) tmpd;
            fr1 = tmpi / (n + 1.0);
            fr2 = (tmpi + 1) / (n + 1.0);
            
            if(tmpi < n )
                uq = (sorted[tmpi] - sorted[tmpi - 1]) / (fr2 - fr1) * (0.75 - fr1) + sorted[tmpi - 1];
            else
        		uq=sorted[n-1];  //added by djluo for n<=2
        }

        q[0] = lq;
        q[1] = median;
        q[2] = uq;

        return q;
    }


    /**
     * Compute and initialize private member variables
     */
    private void calcFields()
    {
    	//Modified by djluo to support missing values for GroupMaker.
    	
		ArrayList list=new ArrayList();
		
		for(int i = 0; i < objValues.size(); i++)
		{
	    	Double obj=(Double)objValues.elementAt(i);
	    			
	    	if( !Double.isNaN(obj.doubleValue()) && (parent == null || !parent.getMask(i)) )
	    		list.add(obj);	
		}
		
	    sorted = new double[list.size()];
	    for(int i = 0; i < sorted.length; i++)
	    {
	    	sorted[i]= ((Double)list.get(i)).doubleValue();
	    }

        MathUtils.InsertionSort(sorted);

        stdDev = MathUtils.getStdDev(sorted);
    }

    public void removeValueAt(int index) {
        super.removeValueAt(index);
        //calcFields();
    }
    
    public void setOrdinal(boolean b) {	
    }
    
    public boolean isOrdinal() {
    	return false;
    }
    
    public void setLevelCheck(boolean b) {
    }
    
    public boolean isLevelCheck() {
    	return false;
    }
    
    public void setLevels(List levels) {
    }
    
    public List getLevels() {
    	return null;
    }

}
