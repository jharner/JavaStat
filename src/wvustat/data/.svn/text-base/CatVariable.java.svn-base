package wvustat.data;

import java.util.*;

import wvustat.interfaces.*;
import wvustat.util.MathUtils;

/**
 *	CatVariable encapsulates information about a categorical variable.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Oct. 25, 1999
 */

public class CatVariable extends VariableImpl {

    private transient String[] sorted;
    
    private boolean ordinal, levelCheck;
    private List levels;

    /**
     *	Constructor
     *
     *	@param n the name of this categorical variable
     *	@param v a vector that specify the values
     */
    public CatVariable( String n, Vector v ) {
        super( n, v );
        type=Variable.CATEGORICAL;
        ordinal = false;
        levelCheck = false;
        //calcFields();
    }

    /**
     * This method returns the relative level of each value. For
     * example, if the variable has three possible values: 'A', 'B' and 'C'.
     * The return value for 'A' will be to level 0, 'B' to 1, 'C' to 2.
     */
    public double[] getNumValues() {
        double[] ret=new double[objValues.size()];
        String[] levels=getDistinctCatValues();

        for(int i=0;i<objValues.size();i++){
            Object obj=objValues.elementAt(i);
            ret[i]=0;

            int k=0;
            while(k<levels.length && !obj.equals(levels[k])) k++;

            if(k<levels.length)
            	ret[i]=k;
            else 
            	ret[i]=Double.NaN; //added by djluo
        }

        return ret;

    }

    /**
     *	This method is inherited from the interface Variable. It returns null.
     */
    public double[] getSortedNumValues() {
        return null;
    }

    /**
     * Get the categorical values contained in an array
     */
    public synchronized String[] getCatValues() {
        int numValues = objValues.size();
        String [] values = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            values[i] = (String)objValues.elementAt(i);
        }
        return values;
    }

    /**
     *	Get the sorted categorical values
     */
    public synchronized String[] getSortedCatValues(){
    	calcFields(); //added Sept,2010
        return sorted;
    }

    /**
     * get distinct categorical values
     */
    public synchronized String[] getDistinctCatValues(){
    	
    	if (ordinal && levelCheck && levels != null) 
    		return (String[])levels.toArray(new String[0]);
    	
    	
        Vector v=new Vector();
        String[] catValues = getCatValues();

        for(int i=0;i<catValues.length;i++){
            if(!v.contains(catValues[i]) && !Variable.CAT_MISSING_VAL.equals(catValues[i])) //modified by djluo
            v.addElement(catValues[i]);
        }

        String[] ret=new String[v.size()];
        v.copyInto(ret);

        //MathUtils.InsertionSort(ret);
        Arrays.sort(ret);

        return ret;
    }
    
    public synchronized int getNumOfDistinctCatValues() {
    	if (ordinal && levelCheck && levels != null) 
    		return levels.size();
    	
        String[] catValues = getCatValues();
        HashSet hset = new HashSet();

        for(int i=0;i<catValues.length;i++){
            if(!Variable.CAT_MISSING_VAL.equals(catValues[i])) //modified by djluo
            hset.add(catValues[i]);
        }
        
        return hset.size();
    }

    /**
     *	Get minimum value. This method only makes sense to numerical variable and always return 0 in
     *	this implementation.
     */
    public double getMin(){
        return 0;
    }

    /**
     *	Get maximum value. This method only makes sense to numerical variable and always return 0 in
     *	this implementation.
     */
    public double getMax(){
        return 0;
    }

    /**
     *	Get average value. This method only makes sense to numerical variable and always return 0 in
     *	this implementation.
     */
    public double getMean(){
        return 0;
    }

    /**
     *	Get standard deviation. This method only makes sense to numerical variable and always return 0 in
     *	this implementation.
     */
    public double getStdDev(){
        return  0;
    }

    /**
     *	Change the i th value in this variable.
     *
     *	@param obj the new value
     *	@param i the index of the value to change
     */
    public synchronized void setValue(Object obj, int i) {

        if(obj instanceof String){
            synchronized(this){
                objValues.setElementAt(obj, i);
                //calcFields();
            }
        }
        else{
            System.err.println("value not string");
            throw new IllegalArgumentException("Only String can be put into categorical variable");
        }
    }

    /**
     * Append a new value to the end of the variable.
     *
     *	@param obj the new value to add
     */
    public synchronized void addValue(Object obj) {
        if(obj instanceof String){
            synchronized(this){
                objValues.addElement(obj);
                //calcFields();
            }
        }
        else
        	throw new IllegalArgumentException("Only String can be put into categorical variable");
    }


    /**
     *	Get quantile values. This method only makes sense to numerical variable and always return null in
     *	this implementation.
     */
    public double[] getQuantiles(){
        return null;
    }

    /**
     * Get levels of the variable in a 2d array. It contains the 
     * relative level of each value.  Similar to method getNumValues().
     */
    public double[][] getDummyValues(){

        String[] levels=getDistinctCatValues();

        double[][] ret=new double[objValues.size()][levels.length];

        for(int i=0;i<objValues.size();i++){
            Object obj=objValues.elementAt(i);

            int k=0;
            while(k<levels.length && !obj.equals(levels[k])) k++;

            if(k<levels.length)
            	ret[i][k]=1;
        }

        return ret;
    }



    /**
     *	Compute private member variables
     */
    private void calcFields(){
    	
    	ArrayList list=new ArrayList();
		
		for(int i = 0; i < objValues.size(); i++)
		{
	    	String obj=(String)objValues.elementAt(i);
	    		
	    	if( !Variable.CAT_MISSING_VAL.equals(obj) && (parent == null || !parent.getMask(i)) )
	    		list.add(obj);
		}
		
	    sorted = new String[list.size()];
	    for(int i = 0; i < sorted.length; i++)
	    {
	    	sorted[i]= (String)list.get(i);
	    }
    	
        MathUtils.InsertionSort(sorted);
    }

    public void removeValueAt(int index) {
        super.removeValueAt(index);
        //calcFields();
    }
    
    public void setOrdinal(boolean b) {
    	this.ordinal = b;
    }
    
    public boolean isOrdinal() {
    	return ordinal;
    }
    
    /**
     * Apply list checking on levels.
     */
    public void setLevelCheck(boolean b) {
    	this.levelCheck = b;
    	if(!levelCheck) levels=null;
    }
    
    /**
     * List checking on levels.
     */
    public boolean isLevelCheck() {
    	return levelCheck;
    }
    
    public void setLevels(List levels) {
    	this.levels = levels;
    }
    
    public List getLevels() {
    	return levels;
    }
}
