package wvustat.modules;

import javax.swing.*;
import java.awt.* ;
import java.util.*;

import wvustat.interfaces.*;

public class StemAndLeafPloter extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final DataSet data;
	private Variable y_var, z_var;
	private EqualCountGrouper grouper;
	private int currentIndex=0;
	private Variable init_y_var;
	private StemAndLeafPlotModel model;
	
	public StemAndLeafPloter(DataSet data, Variable y, Vector vz) {
		this.data = data;
		this.y_var = y;
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
		
		model = new StemAndLeafPlotModel();
//		StemAndLeafPlotDropDownControl control = new StemAndLeafPlotDropDownControl(model);
		StemAndLeafPlotSliderControl control = new StemAndLeafPlotSliderControl(model);
		StemAndLeafPlot plot = new StemAndLeafPlot(model);
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(control, BorderLayout.NORTH);
		add(plot, BorderLayout.CENTER);
		
		initPlot();
	}
	
	public void setGroupMaker(EqualCountGrouper gm){
        grouper=gm;
        initPlot();
    }
	
	private void initPlot(){
		double[] yvals=y_var.getNumValues();
		
		//This vector keeps y values
        Vector v=new Vector();
        
        for(int i=0;i<yvals.length;i++){
            if(!data.getMask(i)){
                Double d=new Double(yvals[i]);
                if(grouper==null){
                    v.addElement(d);
                }
                else if(grouper.getGroupIndex(i).contains(new Integer(currentIndex))){
                    v.addElement(d);
                }
            }
        }
        
        //this array stores the value for the current group, values that
        //are masked out are not included
        double[] values=new double[v.size()];
        for(int i=0;i<values.length;i++){
            values[i]=((Double)v.elementAt(i)).doubleValue();
        }
        
        model.setData(values);
	}
	
	public void updatePlot(String arg){
		if(arg.equals("obs_deleted") || arg.equals("yymask")){
            initPlot();
        }
        else if (arg.equals("add_variable")||arg.equals("delete_variable")) {
    	      
  	        if(data.getTransformedVar(init_y_var.getName()) != null)
  		        y_var = data.getTransformedVar(init_y_var.getName());
  	        else
  		        y_var = init_y_var;
  	        initPlot();  	      
       }
	}
	
	public void setGroup(int index){
        if(z_var==null) return;
        currentIndex=index;
        initPlot();
    }
	
}
