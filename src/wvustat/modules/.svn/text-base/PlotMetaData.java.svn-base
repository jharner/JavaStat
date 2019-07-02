/*
 * PlotMetaData.java
 *
 * Created on July 25, 2000, 10:07 AM
 */

package wvustat.modules;

import java.util.Vector;
import wvustat.interfaces.*;
/**
 * PlotMetaData stores information about a plot. The information includes the dataset the plot is constructured
 * from, the variables used in the plot and the role of each variable.
 * 
  * @author  Hengyi Xue
 * @version 1.0, July 25, 2000
 */
public class PlotMetaData extends Object {
    //Constants for plotType
    public static final int HISTOGRAM=0;
    public static final int REGRESSION=1;
    public static final int KSAMPLE=2;
    public static final int CHART=3;
    public static final int MOSAIC=4;
    public static final int CONTROLCHART=5;
    public static final int LOGISTIC=6;
    public static final int OBJECTSUMMARY=7;
    public static final int MTP = 8;
    public static final int PARALLELCOORDINATES = 9;
    public static final int GRANDTOUR = 10;
    public static final int HEATMAP = 11;
    
    private int plotType;

    private DataSet data;
    
    private Variable xVar, yVar, zVar;
    
    private Variable x2Var, z2Var;
    
    private String desc;
    /** Creates new PlotMetaData */
    public PlotMetaData(DataSet data) {
        this.data=data;
    }
    
    public PlotMetaData() {
    }
    
    public Vector getVariables(){
        Vector v=new Vector();
        
        if(xVar!=null)
            v.addElement(xVar);
        if(x2Var!=null)
            v.addElement(x2Var);
        if(yVar!=null)
            v.addElement(yVar);
        if(zVar!=null)
            v.addElement(zVar);
        if(z2Var!=null)
            v.addElement(z2Var);
        
    
        return v;
    }
    
    public int getPlotType(){
        return plotType;
    }
    
    public void setPlotType(int type){
        plotType=type;
    }
    
    public void setPlotDescription(String idString){
        desc=idString;
    }
    
    public String getPlotDescription(){
        return desc;
    }
    
    public Variable getXVariable(){
        return xVar;
    }
    
    public void setXVariable(Variable xVar){
        this.xVar=xVar;
    }
    
    public Variable getX2Variable(){
        return x2Var;
    }
    
    public void setX2Variable(Variable var){
        x2Var=var;
    }
    
    public Variable getYVariable(){
        return yVar;
    }
    
    public void setYVariable(Variable yVar){
        this.yVar=yVar;
    }
    
    public Variable getZVariable(){
        return zVar;
    }
    
    public void setZVariable(Variable zVar){
        this.zVar=zVar;
    }
    
    public Variable getZ2Variable(){
        return z2Var;
    }
    
    public void setZ2Variable(Variable z2Var){
        this.z2Var=z2Var;
    }
}
