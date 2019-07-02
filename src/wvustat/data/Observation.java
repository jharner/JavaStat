package wvustat.data;

import java.util.Arrays;

/**
 * An observation can be thought of as a 'row' in a data set. It is used here merely to
 * facilitate sorting. It has no structural significance.
 * 
 * @author: Hengyi Xue
 * @version: 1.0, Apr. 26, 2000
 */

public class Observation implements Comparable{

    private double[] values;
    private boolean[] orders; // true for ascending, false for descending.
    private int id;

    public Observation(double[] values, int id){
    	this.values=values;
    	this.orders=new boolean[values.length];
    	Arrays.fill(this.orders, true);
    	this.id=id;
    }
    
    public Observation(double[] values, boolean[] orders, int id){
    	this.values = values;
    	this.orders = orders;
    	this.id = id;
    }

    public double getValue(int index){
    	return values[index];
    }

    public int getID(){
    	return id;
    }

    public int compareTo(Object obj) throws ClassCastException{
	
    	Observation obs=(Observation)obj;

    	int ret=0;

    	for(int i=0;i<values.length;i++){
    		double v1=values[i];
    		double v2=obs.getValue(i);

    		if(v1>v2){
    			ret=orders[i]?1:-1;
    			break;
    		}
    		else if(v1<v2){
    			ret=orders[i]?-1:1;
    			break;
    		}
    		else if(Double.isNaN(v1) && !Double.isNaN(v2)){
    			ret=orders[i]?1:-1;
    			break;
    		}
    		else if(!Double.isNaN(v1) && Double.isNaN(v2)){
    			ret=orders[i]?-1:1;
    			break;
    		}
    	}

    	return ret;
    }
}
