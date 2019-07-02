package wvustat.interfaces;

import java.util.*;
import java.rmi.RemoteException;

/**
*	Variable defines an interface for a 1-dimensional data collection.
*
*	@author: Hengyi Xue
*	@version: 1.0, Oct. 25, 1999
*/

public interface Variable{

	public static final Double NUM_MISSING_VAL=new Double(Double.NaN);
	public static final String CAT_MISSING_VAL="";


    /**
	* Constant for property 'Type'
	*/
	public static final int NUMERIC=0;
	
	/**
	* Constant for property 'Type'
	*/
	public static final int CATEGORICAL=1;	
	
	/**
	 * Get the ID of the variable
	 */
	public int getId();
	
	/**
	* Get the name of the variable
	*/
	public String getName() ;
	
	/**
	* Get the property 'type'
	*/
	public int getType();
	
	/**
	* Get the property 'role'
	*/
	public int getRole();
	
	/**
	* Get values contained in the variable
	*/
	public Vector getValues();
	
	/**
	* Get the indexed value contained in the variable
	*/
    public Object getValue(int index);

	/**
	* Get the numerical values if the variable is numeric
	*/
	public double[] getNumValues() ;
	
	/**
	* Get sorted numerical values(missing values excluded) if the variable is numeric
	*/
	public double[] getSortedNumValues() ;
	
	/**
	* Get categorical values if the variable is categorical
	*/
	public String[] getCatValues() ;
	
	/**
	* Get sorted categorical values(missing values excluded) if the variable is categorical
	*/
	public String[] getSortedCatValues();

	/**
	* Get distinct categorical values if the variable is categorical
	*/
    public String[] getDistinctCatValues() ;
	
    public int getNumOfDistinctCatValues();
    
	/**
	* Get the size of this variable
	*/
	public int getSize();
	
	/**
	* set the property 'role' for this variable
	*/
	public void setRole(int newRole);
        
        
    public void setName(String name);
	
	/**
	* Change the indexed value
	*/
	public void setValue(Object obj, int index);
	
	/**
	* Append the variable by adding one new value
	*/
	public void addValue(Object obj);    
        
    /**
     * Remove one value from the variable
     *
     * @param index the index of the value to be removed
     */
    public void removeValueAt(int index);

	/**
	* Get maximum value contained
	*/
	public double getMax() throws RemoteException;
	
	/**
	* Get minimum value contained
	*/
	public double getMin() throws RemoteException;
	
	/**
	* Get mean value contained
	*/
	public double getMean();
	
	/**
	* Get standard deviation contained
	*/
	public double getStdDev(); 
	
	/**
	* Print the values
	*/
	public void print();
	
	/**
	* Get quantiles for values contained
	* 
	* @return an double array that contains lower quartile,median and upper quartile in that order 
	*/
	public double[] getQuantiles();

    /**
     * Get a duplicate copy of this variable
     */
    public Variable getClonedCopy();

    /**
     * Get the dummy variable values, only meaningful for a Categorical variable.
     */
    public double[][] getDummyValues();
    
    /**
     * Set a categorical variable to ordinal 
     */
    public void setOrdinal(boolean b);
    
    /**
     * Get ordinal property for a categorical variable
     */
    public boolean isOrdinal();
    
    /**
     * Set to check the levels of this variable
     */
    public void setLevelCheck(boolean b);
    
    /**
     * Get level checking property of this variable
     */
    public boolean isLevelCheck();
    
    /**
     * Set levels for this variable
     */
    public void setLevels(List levels);
    
    /**
     * Get levels of this variable
     */
    public List getLevels();
     
} 
