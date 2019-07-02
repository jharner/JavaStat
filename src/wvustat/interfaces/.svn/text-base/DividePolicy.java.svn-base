package wvustat.interfaces;



/**
*	This interface is used to categorize observations into groups based on a sinagle
*	continuous or categorical variable. It extends remote interface because it will
*	be passed to the server by reference.
*	
*	@author: Hengyi Xue
*	@version: 1.0, Jan. 18, 2000
*/

public interface DividePolicy {

	/**
	*	Constants used to specify if the variable involved is continous or categorical
	*/
	public static final int CONTINUOUS=0;
	public static final int DISCRETE=1;

	/**
	*	Get the name of the DividePolicy
	*/
	public String getName();
	
	/**
	*	Get the total number of groups this policy creats
	*/
	public int getGroupCount();
	
	/**
	*	Get the group index for a given value
	*/
	public int getGroupIndex(Object obj);
	
	/**
	*	Get the smallest value in a group, this only makes sense when the variable is continuous
	*
	*	@param index the index of the group
	*/
	public double getLowerLimit(int index);

	/**
	*	Get the greatest value in a group, this only makes sense when the variable is continuous
	*
	*	@param index the index of the group
	*/		
	public double getUpperLimit(int index);
	
	/**
	*	Get the name of the group at index, for a continous variable, this will return a string like
	*	"10-20" which represents the range of the group. For categorical variable, it returns the values inside
	*	the variable.
	*	
	*	@param index the index of the group
	*/
	public String getGroupName(int index);
}