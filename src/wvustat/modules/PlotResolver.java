/*
 * PlotResolver.java
 *
 * Created on July 25, 2000, 10:54 AM
 */

package wvustat.modules;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.modules.ControlChart.ControlChartModule;
import wvustat.modules.logistic.LogisticModule;
import wvustat.modules.rmodels.LinearModelModule;
import wvustat.modules.genome.*;
import wvustat.network.*;
import java.util.Vector;

/**
 * PlotResolver construct a plot for given data and display it.
 *
 * @author  Hengyi Xue
 * @version 1.0, July 25, 2000
 */
public class PlotResolver extends Object
{
    //The following constants defines the categories of plots supported
    public static final int Y_ZPLOT = 0;
    public static final int XY_ZPLOT = 1;
    public static final int CHART = 2;
    public static final int CONTROL_CHART = 3;
    public static final int LINEAR_MODEL = 4;
    public static final int PARALLEL_COORDINATES = 5;
    public static final int GRAND_TOUR = 6;
    public static final int HEATMAP = 7;

    private static PlotResolver instance;
    private String inputModel;
    private String modelFamily;
    private String modelLink;

    /** Creates new PlotResolver */
    protected PlotResolver()
    {
    }

    public static PlotResolver getInstance()
    {
        if (instance == null)
            instance = new PlotResolver();

        return instance;
    }

    public GUIModule buildPlot(DataSet data, int plotCategory, int yIndex, int xIndex, Variable zVar) throws IllegalArgumentException
    {
        Variable xVar = null,  yVar = null;

        Variable x2 = null,z2 = null;
        GUIModule module = null;
        PlotMetaData pmd;
        StringBuffer desc = new StringBuffer();

        Vector xv, yv, zv;
        xv = data.getXVariables();
        yv = data.getYVariables();
        zv = data.getZVariables();

        if (yv.size() > 0)
            yVar = (Variable) (yv.elementAt(yIndex));
        if (xv.size() > 0)
            xVar = (Variable) (xv.elementAt(xIndex));


        if (xv.size() > 1)
            x2 = (Variable) xv.elementAt(xv.size() - 1);
        if (zv.size() > 1)
            z2 = (Variable) zv.elementAt(1);

        pmd = new PlotMetaData(data);
        pmd.setXVariable(xVar);
        pmd.setYVariable(yVar);
        pmd.setZVariable(zVar);
        pmd.setX2Variable(x2);
        pmd.setZ2Variable(z2);

        int plotType = -1; //This is going to take on constant values defined in PlotMetaData
        
        //data.clearRankTable(); //added by djluo

        switch (plotCategory)
        {
            case Y_ZPLOT:
                /*scanVariable(yVar, data);
                scanVariable(zv, data);
                data.scanOK();*/
                
                if (yVar.getType() == Variable.NUMERIC)
                {
                	data.scanVariable(null, null, null, yVar, null, null, zv, null, GUIModule.getInstanceCnt());
                    module = new HistogramModule(data, yVar, zv);
                    plotType = PlotMetaData.HISTOGRAM;
                    desc.append("Histogram: " + yVar.getName());
                    if (zv.size() > 0)
                    {
                    	   desc.append(" vs " + catVarNames(zv));
                    }
                }

                else
                {
                	Variable fVar = data.getFreqVariable();
             	    /*scanVariable(fVar, data);
                    data.scanOK(); */
                    data.scanVariable(null, null, null, yVar, null, null, zv, fVar, GUIModule.getInstanceCnt());
                    plotType = PlotMetaData.CHART;
                    module = new FreqModule(data, yVar, zv);
                    desc.append("Chart: " + yVar.getName());
                    
                    //data.addVarOfModule(module,fVar);
                }
                
                //data.addVarOfModule(module,yVar);
                //addModuleVars(zv, data, module);

                break;

            case XY_ZPLOT:
                /*scanVariable(xVar, data);
                scanVariable(yVar, data);
                scanVariable(zv, data);
                data.scanOK();*/
                
                if (yVar.getType() == Variable.NUMERIC)
                {
                    if (xVar.getType() == Variable.NUMERIC)
                    {
                        plotType = PlotMetaData.REGRESSION;
                        data.scanVariable(xVar, null, null, yVar, null, null, zv, null, GUIModule.getInstanceCnt());
                        module = new RegressionModule(data, yVar, xVar, zv);
                        desc.append("Regression: " + yVar.getName() + " vs " + xVar.getName());
                        if (zv.size() > 0)
                            desc.append(" by " + catVarNames(zv));
                    }
                    else
                    {
                        plotType = PlotMetaData.KSAMPLE;
                        data.scanVariable(xVar, null, null, yVar, null, null, zv, null, GUIModule.getInstanceCnt());
                        module = new KSampleModule(data, yVar, xVar, zv);
                        desc.append("KSample: " + yVar.getName() + " vs " + xVar.getName());
                        if (zv.size() > 0)
                            desc.append(" by " + catVarNames(zv));
                    }
                }

                else
                {
                    if (xVar.getType() == Variable.CATEGORICAL)
                    {
                    	Variable fVar = data.getFreqVariable();
                    	/*scanVariable(fVar, data);
                        data.scanOK();*/
                        
                        plotType = PlotMetaData.MOSAIC;
                        data.scanVariable(xVar, null, null, yVar, null, null, zv, fVar, GUIModule.getInstanceCnt());
                        module = new CTableModule(data, yVar, xVar, zv);
                        desc.append("Mosaic: " + xVar.getName() + " vs " + yVar.getName());
                        
                        //data.addVarOfModule(module,fVar);
                    }
                    else
                    {
                        plotType = PlotMetaData.LOGISTIC;
                        data.scanVariable(xVar, null, null, yVar, null, null, zv, null, GUIModule.getInstanceCnt());
                        module = new LogisticModule(data, yVar, xVar, zv);
                        desc.append("Logistic: " + yVar.getName() + " vs " + xVar.getName());
                    }
                }

                //data.addVarOfModule(module,xVar);
                //data.addVarOfModule(module,yVar);
                //addModuleVars(zv, data, module);
                break;
                
            case PARALLEL_COORDINATES:
            	plotType = PlotMetaData.PARALLELCOORDINATES;
            	data.scanVariable(null, null, yv, null, null, null, zv, null, GUIModule.getInstanceCnt());
            	module = new ParallelCoordModule(data, yv, zv);
            	desc.append("Parallel Coordinates: " + yVar.getName() + "...");
            	if (zv.size() > 0)
            		desc.append(" by " + catVarNames(zv));
            	break;
            	
            case GRAND_TOUR:
            	plotType = PlotMetaData.GRANDTOUR;
            	
            	Vector xyv = new Vector();
            	xyv.addAll(xv);
            	xyv.addAll(yv);
            	
            	data.scanVariable(null, null, xyv, null, null, null, zv, null, GUIModule.getInstanceCnt());
            	module = new GrandTourModule(data, xyv, zv);
            	desc.append("Grand Tour");
            	if (zv.size() > 0)
            		desc.append(" by " + catVarNames(zv));
            	break;

            case HEATMAP:
            	plotType = PlotMetaData.HEATMAP;
            	data.scanVariable(null, null, yv, null, null, null, zv, null, GUIModule.getInstanceCnt());
            	module = new HeatmapModule(data, yv, zv);
            	desc.append("Two way Cluster");
            	if (zv.size() > 0)
            		desc.append(" by " + catVarNames(zv));
            	break;
            	
            case CHART:
                
                if (yVar.getType() == Variable.CATEGORICAL)
                {
                	/*scanVariable(xVar, data);
                    scanVariable(yVar, data);*/
                    Variable fVar = data.getFreqVariable();
               	    /*scanVariable(fVar, data);
                    data.scanOK();   */              
  
                    plotType = PlotMetaData.CHART;
                    data.scanVariable(xVar, null, null, yVar, null, null, null, fVar, GUIModule.getInstanceCnt());
                    module = new FreqModule(data, yVar, xVar);
                    if (xVar == null)
                    {
                        desc.append("Chart: " + yVar.getName());
                    }
                    else
                    {
                        desc.append("Chart: " + yVar.getName() + " and " + xVar.getName());
                    }
                    
                    /*data.addVarOfModule(module,xVar);
                    data.addVarOfModule(module,yVar);
                    data.addVarOfModule(module,fVar);*/
                }
                else if (yVar.getType() == Variable.NUMERIC && xVar != null && xVar.getType() == Variable.CATEGORICAL
                        && ((x2 != null && x2.getType() == Variable.CATEGORICAL) || x2 == null))
                {
                	/*scanVariable(xVar, data);
                    scanVariable(yVar, data);
                    scanVariable(x2, data);
                    scanVariable(zVar, data);
                    scanVariable(z2, data);
                    data.scanOK();*/
                    
                    plotType = PlotMetaData.CHART;
                    if (zVar == null)
                    {
                    	data.scanVariable(xVar, x2, null, yVar, zVar, z2, null, null, GUIModule.getInstanceCnt());
                        module = new ChartModule(data, yVar, xVar, x2);
                        desc.append("Chart: " + yVar.getName() + " vs " + xVar.getName());
                    }
                    else if (zVar.getType() == Variable.CATEGORICAL && (z2 == null || z2.getType()==Variable.CATEGORICAL))
                    {
                    	data.scanVariable(xVar, x2, null, yVar, zVar, z2, null, null, GUIModule.getInstanceCnt());
                        module = new ChartModule(data, yVar, xVar, x2);
                        desc.append("Chart: " + yVar.getName() + " vs " + xVar.getName() + " by " + zVar.getName());
                    }
                    else
                    {
                        throw new IllegalArgumentException("Z variable must be categorical to construct a chart");
                    }
                    
                    /*data.addVarOfModule(module,xVar);
                    data.addVarOfModule(module,yVar);
                    data.addVarOfModule(module,x2);
                    data.addVarOfModule(module,zVar);
                    data.addVarOfModule(module,z2);*/
                }
                else
                {
                    throw new IllegalArgumentException("Y variable must be numeric and x variables must be categorical");
                }
                
                break;
            case CONTROL_CHART:
                
                if (yVar.getType() == Variable.NUMERIC)
                {
                    if (xVar.getType() == Variable.CATEGORICAL)
                    {
                    	/*scanVariable(xVar, data);
                        scanVariable(yVar, data);
                        data.scanOK();*/
                    	
                        plotType = PlotMetaData.CONTROLCHART;
                        data.scanVariable(xVar, null, null, yVar, null, null, null, null, GUIModule.getInstanceCnt());
                        module = new ControlChartModule(data, yVar, xVar);
                        desc.append("Control Chart: " + yVar.getName() + " vs " + xVar.getName());
                        
                        /*data.addVarOfModule(module,xVar);
                        data.addVarOfModule(module,yVar);*/
                    }
                    else
                        throw new IllegalArgumentException("Control chart can not be constructed for a numerical y and numerical x.");
                }
                else
                {
                    throw new IllegalArgumentException("Control chart can not be constructed for a categorical y variable");
                }                
                
                break;
            case LINEAR_MODEL:
            	   
            	if(xv.size() == 0)
    				throw new IllegalArgumentException("Can't do regression analysis without x variable");
            	   
            	   if (yVar.getType() == Variable.NUMERIC)
            	   {
            		   /*scanVariable(yVar, data);
                	   scanVariable(xv, data);
                	   scanVariable(zv, data);
                	   data.scanOK();*/
            		   
            		   
            	   	   plotType = PlotMetaData.REGRESSION;
            	   	   data.scanVariable(null, null, xv, yVar, null, null, zv, null, GUIModule.getInstanceCnt());
            	   	   
            	   	   if ("binomial".equals(modelFamily))
            	   		   module = new LinearModelModule(data, yVar, xv, zv, inputModel, "gaussian", "identity");
            	   	   else
            	   		   module = new LinearModelModule(data, yVar, xv, zv, inputModel, modelFamily, modelLink);
            	   	   
            	   	   if (modelFamily.equals("binomial") || (modelFamily.equals("gaussian") && modelLink.equals("identity")))
            	   		   desc.append("LM: ");
            	   	   else
            	   		   desc.append("GLM: ");
            	   	   
            	   	   desc.append(yVar.getName()); 
            	   	   if (xv.size() > 0)
                       {
                    	   desc.append(" vs " + catVarNames(xv));
                       }
            	   	   
            	   	   /*data.addVarOfModule(module,yVar);
            	   	   addModuleVars(xv, data, module);
            	   	   addModuleVars(zv, data, module);*/
            	   }
            	   else if (yVar.getDistinctCatValues().length == 2)
            	   {
            		   /*scanVariable(yVar, data);
                	   scanVariable(xv, data);
                	   scanVariable(zv, data);
                	   data.scanOK();*/
            		   
                	   data.scanVariable(null, null, xv, yVar, null, null, zv, null, GUIModule.getInstanceCnt());
            		   plotType = PlotMetaData.REGRESSION;
            		   if (!"binomial".equals(modelFamily))
            			   module = new LinearModelModule(data, yVar, xv, zv, inputModel, "binomial", "logit");
            		   else
            			   module = new LinearModelModule(data, yVar, xv, zv, inputModel, modelFamily, modelLink);
            	   	   desc.append("GLM: "  + yVar.getName()); 
            	   	   if (xv.size() > 0)
                       {
                    	   desc.append(" vs " + catVarNames(xv));
                       }
            	   	   
            	   	   /*data.addVarOfModule(module,yVar);
            	   	   addModuleVars(xv, data, module);
            	   	   addModuleVars(zv, data, module);*/
            	   }
            	   else
            	   	   throw new IllegalArgumentException("Y variable must be numeric or binary categorical to construct a linear model");
            	   
            	   break;

        } //end switch

        if (module != null)
        {
            pmd.setPlotDescription(desc.toString());
            pmd.setPlotType(plotType);

            module.setMetaData(pmd);
        }

        return module;

    }
    
    public GUIModule buildPlot(Biobase obj) throws IllegalArgumentException
    {
    	GUIModule module = null;
        PlotMetaData pmd;
        StringBuffer desc = new StringBuffer();
        
        pmd = new PlotMetaData();
        int plotType = -1;
        
        if (obj instanceof AffyBatch) {
        	AffyBatch affy = (AffyBatch)obj;
        	module = new MArrayModule(affy);
        	plotType = PlotMetaData.OBJECTSUMMARY;
        	desc.append(affy.getObjName());
        }
        else if (obj instanceof exprSet) {
        	exprSet eset = (exprSet)obj;
        	module = new ExprSetModule(eset);
        	plotType = PlotMetaData.OBJECTSUMMARY;
        	desc.append(eset.getObjName());
        }
        else if (obj instanceof MTP) {
        	MTP mtp = (MTP)obj;
        	module = new MTPModule(mtp);
        	plotType = PlotMetaData.MTP;
        	desc.append(mtp.getObjName());
        }
        else
 	   	   throw new IllegalArgumentException("Unsupported object type");
        
        if (module != null)
        {
            pmd.setPlotDescription(desc.toString());
            pmd.setPlotType(plotType);

            module.setMetaData(pmd);
        }

        return module;
        
    }
    

    public void setInputModel(String model){
    	this.inputModel = model;
    }
    
    public void setModelFamily(String family){
    	this.modelFamily = family;
    }
    
    public void setModelLink(String link){
    	this.modelLink = link;
    }
    
    /*private void scanVariable(Variable v, DataSet data)
    {
        if (v == null)
            return;

        try
        {
            for (int i = 0; i < v.getSize(); i++)
            {
                Object value = v.getValue(i);
                /*if ((Variable.NUM_MISSING_VAL.equals(value) || Variable.CAT_MISSING_VAL.equals(value)) &&
                    !data.getMask(i))
                {
                    data.setMask(true, i);
                }*
                if ((Variable.NUM_MISSING_VAL.equals(value) || Variable.CAT_MISSING_VAL.equals(value)) &&
                    !data.getMissing(i))
                {
                    data.setMissing(i);
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }*/
    
    /*private void scanVariable(Vector zv, DataSet data)
    {
    	for(int i=0; i<zv.size(); i++){
    		Variable z = (Variable)(zv.elementAt(i));
    		try{
    			if (z.getRole() == DataSet.Z_ROLE && i>=EqualCountGrouper.MAXCOND) break;
    		}catch(RemoteException re){}
    		scanVariable(z, data);
    	}    	
    }

    private void addModuleVars(Vector zv, DataSet data, GUIModule module)
    {
    	for(int i=0; i<zv.size(); i++){
			Variable z = (Variable)(zv.elementAt(i));
			try{
    			if (z.getRole() == DataSet.Z_ROLE && i>=EqualCountGrouper.MAXCOND) break;
    		}catch(RemoteException re){}
			data.addVarOfModule(module,z);
		}    	
    }*/
    
    private String catVarNames(Vector zv)
    {
    	StringBuffer s = new StringBuffer();
    	for(int i=0; i<zv.size(); i++){
 	   	    Variable z = (Variable)(zv.elementAt(i));
 	   	   
 	   	    if (z.getRole() == DataSet.Z_ROLE && i>=EqualCountGrouper.MAXCOND) break;
 	       
 	   	    if (i > 0)
 	       		s.append(" + ");
 	   	    s.append(z.getName());
 	    }
    	return s.toString();
    }
}
