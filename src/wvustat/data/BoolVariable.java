package wvustat.data;

/**
 * BoolVariable implements a variable that only contains boolean values. 
 * 
 * @author: Hengyi Xue
 * @version: 1.0, Apr 25, 2000
 */

import java.util.*;

public class BoolVariable{
    private Vector values;

    /**
     * Create a new BoolVariable with all elements set as FALSE.
     * @param size of variable
     */
    public BoolVariable(int size){
	values=new Vector(size);
	for(int i=0;i<size;i++)
	    values.addElement(Boolean.FALSE);

    }
    
    public BoolVariable(Vector values){
        this.values=values;
    }

    public boolean getValue(int index){
	return ((Boolean)values.elementAt(index)).booleanValue();
    }

    public Vector getValues(){
	return values;
    }

    /**
     * 
     * @param value of type Boolean
     */
    public void addValue(Object value){
	values.addElement(value);
    }

    public void setValueAt(boolean value, int index){
	values.setElementAt(new Boolean(value), index);
    }

    public void removeValueAt(int index){
	values.removeElementAt(index);
    }

    /**
     * This method returns the relative level of each bool value. 
     * The return value for TRUE will be 1, FALSE be 0.
     */
    public int[] getDummyValues(){
	int[] ret=new int[values.size()];

	for(int i=0;i<ret.length;i++){
	    if(values.elementAt(i).equals(Boolean.TRUE))
		ret[i]=1;
	    else
		ret[i]=0;
	}

	return ret;
    }

    /**
     * Reset all boolean values to false
     */
    public void reset(){
	for(int i=0;i<values.size();i++){
	    values.setElementAt(Boolean.FALSE, i);
	}
    }
}

