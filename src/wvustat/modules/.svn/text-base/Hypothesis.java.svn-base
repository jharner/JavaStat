package wvustat.modules;

import javax.swing.event.*;
/**
*	The Hypothesis class encapsulates essential details about a statistical hypothesis, including
*	the null value, the confidence level and the probability.
*
*	@author: Hengyi Xue
*	@version: 1.0, Feb. 3, 2000
*/

public class Hypothesis{
	private double nullVal, alpha, prob, lolimit, hilimit;
	
	private EventListenerList eList=new EventListenerList();
	private ChangeEvent event;

	private void fireStateChanged(){
		Object[] list=eList.getListenerList();
		for(int i=list.length-2;i>=0;i-=2){
			if(list[i]==ChangeListener.class){
				if(event==null)
					event=new ChangeEvent(this);
				((ChangeListener)list[i+1]).stateChanged(event);
			}
		}
	}
	
	public void addChangeListener(ChangeListener l){
		eList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l){
		eList.remove(ChangeListener.class, l);
	}
	
	public void setNull(double val){
		nullVal=val;
		fireStateChanged();
	}
	
	public void setLevel(double level){
		alpha=level;
		fireStateChanged();
	}
	
	public void setProbability(double prob){
		this.prob=prob;
		fireStateChanged();
	}
	
	public void setLowerLimit(double x){
		lolimit=x;
		fireStateChanged();
	}
	
	public void setUpperLimit(double x){
		hilimit=x;
		fireStateChanged();
	}
	
	public double getNull(){
		return nullVal;
	}
	
	public double getLevel(){
		return alpha;
	}
	
	public double getProbability(){
		return prob;
	}
	
	public double getLowerLimit(){
		return lolimit;
	}
	
	public double getUpperLimit(){
		return hilimit;
	}


}
	
		
