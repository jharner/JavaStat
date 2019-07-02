package wvustat.interfaces;

import java.util.Set;

/**
*	This interface is used to categorize observations into groups based on a sinagle
*	continuous or categorical variable. It extends remote interface because it will
*	be passed to the server by reference.
*	
*	@author: Hengyi Xue
*	@version: 1.0, Jan. 18, 2000
*/

public interface EqualCountPolicy{

	/**
	*	Constants used to specify if the variable involved is continous or categorical
	*/
	public static final int CONTINUOUS=0;
	public static final int DISCRETE=1;

	/**
	*	Get the name of the EqualCountPolicy
	*/
	public String getName();
	
	/**
	*	Get the name of the EqualCountPolicy at given indexed condition
	*/
	public String getName(int condIndex);
	
	/**
	*	Get the total number of groups this policy creats
	*/
	public int getGroupCount();
	
	/**
	*	Get the group index for a given value
	*/
	public Set getGroupIndex(int index);
	
	/**
     * Get the abbreviated name of the indexed group by the name of the group at index of the first 
     * conditional variable. If the first conditional variable is continous, this will return a string like
     * "10-20 ..." which represents the range of the group. For categorical variable in first condition, 
     * it returns the values inside the variable.
     * @param index : global group index of a set of conditional variables
     */
	public String getGroupName(int index);
}