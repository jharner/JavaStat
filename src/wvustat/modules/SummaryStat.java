package wvustat.modules;

import java.rmi.*;
import javax.swing.event.*;

import wvustat.interfaces.*;

/**
*	SummaryStat is a class that stores summary statistics for a k sample data. The statistics
*	are temporarily stored in this class so that we don't need to recompute them every time we
*	need to access them. It is used in KSampleModule.
*
*	@author: Hengyi Xue
*	@version: 1.0, Feb. 29, 2000
*/

public class SummaryStat{
	
	private double[][] data;
	
	private String[] groupNames;
	
	private double[] dev,mean;
	
	private double overallMean;
	
	private String name;
	
	private EventListenerList eList=new EventListenerList();
	private ChangeEvent event;

	public void addChangeListener(ChangeListener l){
		eList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l){
		eList.remove(ChangeListener.class, l);
	}
	
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
	
	public void setGroupNames(String[] names){
		groupNames=names;
	}
	
	
	public void setData(double[][] rawData){
		data=rawData;
		dev=new double[data.length];
		mean=new double[data.length];
		
		StatEngine se=new StatEngineImpl();
		
		try{
			for(int i=0;i<data.length;i++){
				dev[i]=se.getStdDev(data[i]);  
				mean[i]=se.getMean(data[i]); 
			}
			
			overallMean=se.getMean(mean); 
		}
		catch(RemoteException re){
				
		}
		
		fireStateChanged();
	}
	
	public int getGroupSize(int index){
		int ret=0;
		
		if(index<data.length)
			ret=data[index].length;
			
		return ret;
	}
	
	public int getGroupCount(){
		return data.length;
	}
	
	public double getStdDev(int index){
		return dev[index];
	}
	
	public double getMean(int index){
		return mean[index];
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
		fireStateChanged();
	}
	
	public double getOverallMean(){
		return overallMean;
	}
	
	public String getGroupName(int index){
		return groupNames[index];
	}
}
	
