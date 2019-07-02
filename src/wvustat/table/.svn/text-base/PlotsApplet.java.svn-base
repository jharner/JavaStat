/*
 * Created on Jul 20, 2005
 *
 */
package wvustat.table;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import wvustat.interfaces.*;
import wvustat.modules.*;
import wvustat.modules.ControlChart.ControlChartModule;
import wvustat.modules.logistic.LogisticModule;
import wvustat.util.DatasetFactory;
import wvustat.plot.ManualAxisModel;

/**
 * @author djluo
 *
 */
public class PlotsApplet extends JApplet{
	private static final long serialVersionUID = 1L;
	private DataSetTM activeData = null;
	private SimpleDataEntryTable activeTable = null;
	
	public void init(){
		javax.swing.text.html.parser.ParserDelegator workaround = new javax.swing.text.html.parser.ParserDelegator();
		
		String plotType, rptType, xVals, yVals, zVals, xValName, yValName, zValName;
		String xValType, yValType, zValType;
		String fVals, fValName;
		String lVals, lValName;
		boolean hasL=false, hasX=false, hasZ = false, hasF = false;
		String mu, xStart, xInterval;
		
		// Read parameters
		plotType = getParameter("Plot Type");
		rptType = getParameter("Report Type");
		
		lVals = getParameter("L Values");
		lValName = getParameter("L Variable Name");
		
		xVals = getParameter("X Values");
		xValName = getParameter("X Variable Name");
		xValType = getParameter("X Variable Type");
		
		yVals = getParameter("Y Values");
		yValName = getParameter("Y Variable Name");
		yValType = getParameter("Y Variable Type");
		
		zVals = getParameter("Z Values");
		zValName = getParameter("Z Variable Name");
		zValType = getParameter("Z Variable Type");
		
		fVals = getParameter("F Values");
		fValName = getParameter("F Variable Name");
		
		mu = getParameter("mu");
		xStart = getParameter("X-axis Start Value");
		xInterval = getParameter("X-axis Increment");
		
		if(plotType == null){ 
			JOptionPane.showMessageDialog(this, "Missing Parameter: Plot Type");
			return;
		}		
		
		if(yVals == null){
			JOptionPane.showMessageDialog(this, "Missing Parameter: Y Values");
			return;
		}
		
		if(yValName == null){
			JOptionPane.showMessageDialog(this, "Missing Parameter: Y Variable Name");
			return;
		}

		// Create dataset
		ArrayList columns=new ArrayList();
		ArrayList column;
		
		if(lVals != null && lValName != null){
			hasL = true;
			column=new ArrayList();
    		column.add(lValName);
    		columns.add(column);
    		decodeValues(lVals, column);
		}
		
		
		column=new ArrayList();
        column.add(yValName);
        columns.add(column);
        decodeValues(yVals, column);

        	
        if(xVals != null && xValName != null){
        	hasX = true;
        	column=new ArrayList();
        	column.add(xValName);
        	columns.add(column);
        	decodeValues(xVals, column);
        }
        	
        if(zVals != null && zValName != null){
        	hasZ = true;
        	column=new ArrayList();
        	column.add(zValName);
        	columns.add(column);
        	decodeValues(zVals, column);
        }
        	
        if(fVals != null && fValName != null){
        	hasF = true;
        	column=new ArrayList();
        	column.add(fValName);
        	columns.add(column);
        	decodeValues(fVals, column);
        }
        	
        //Check if variables' size are equal
        int numOfRows = ((ArrayList)columns.get(0)).size();
        for (int k = 0; k < columns.size(); k++) {
        	if (((ArrayList)columns.get(k)).size() != numOfRows) {
        		showWarning("The variables you defined do not have equal number of values!", this);
        		return;
        	}
        }
        	
        	
        try{
         	activeData = DatasetFactory.createDataSet("Data", columns);
         	
         	int index;
         	if(hasL){
         		index = activeData.getColumnIndex(lValName);
         		activeData.setColumnClass(String.class,index);
             	activeData.setColumnRole(DataSet.L_ROLE,index);
         	}
         	
         	index = activeData.getColumnIndex(yValName);
         	if("Categorical".equals(yValType)) activeData.setColumnClass(String.class,index);
         	activeData.setColumnRole(DataSet.Y_ROLE, index);
         	
         	if(hasX){ 
         		index = activeData.getColumnIndex(xValName);
         		if("Categorical".equals(xValType)) activeData.setColumnClass(String.class,index);
         		activeData.setColumnRole(DataSet.X_ROLE, index);
         	}
         	
         	if(hasZ){
         		index = activeData.getColumnIndex(zValName);
         		if("Categorical".equals(zValType)) activeData.setColumnClass(String.class,index);
         		activeData.setColumnRole(DataSet.Z_ROLE, index);
         	}         	
         	
         	if(hasF){
         		index = activeData.getColumnIndex(fValName);
         		activeData.setColumnRole(DataSet.F_ROLE,index);
         	}
         	
        }
        catch(Exception e)
		{
           	JOptionPane.showMessageDialog(this, e.getMessage());
           	e.printStackTrace();
		}

		// Start to draw plot
		DataSet dataSet = activeData.getDataSet();
        Vector vz = dataSet.getZVariables();
        GUIModule gmodule = null;
        	
        // Draw plot depending on different type
        if(plotType.equals("Histogram") || plotType.equals("Quantile Plot") || plotType.equals("Normal Plot")){
        		
    		Vector vy = dataSet.getYVariables();
    		if (vy.size() == 0)
    		{
    			showWarning("You must define a y variable.", this);
    			return;
    		}

    		Variable yVar, zVar;
        	yVar = (Variable) vy.elementAt(0);

        	if (yVar.getType() != Variable.NUMERIC)
        	{
        		showWarning("You must define y variable as numeric.", this);
                return;
        	}
        		
        	if(vz.size() == 0) 
        		zVar = null;
        	else 
        		zVar = (Variable) vz.elementAt(0);
    			
        	dataSet.scanVariable(null, null, null, yVar, zVar, null, null, null, GUIModule.getInstanceCnt());
        	HistogramModule module = new HistogramModule(dataSet, yVar, zVar);
        	        		
        	gmodule = module;
    				
    		module.showPlot(plotType);
    		//plotType: Histogram, Quantile Plot, Normal Plot
            			
    		if(plotType.equals("Histogram"))
    			module.showHistogramReport(rptType);
    		//rptType: Moments, t-test, Confidence Interval
    			
    		if (mu != null) {
    			try{
    				double tmp=Double.parseDouble(mu);
    				module.getHypothesis().setNull(tmp);
    			}
    			catch(NumberFormatException e){}
    		}
    		
    		if (xStart != null && xInterval != null) {
    			try {
    				ManualAxisModel model = new ManualAxisModel();
    				
    				double d = Double.parseDouble(xStart);
    				model.setStartValue(d);
    				d = Double.parseDouble(xInterval);
    				model.setInterval(d);
    				if (model.getInterval() <= 0)
    					throw new NumberFormatException();
    				d = yVar.getMax();
    				model.setEndValue(d);
    				d = model.getStartValue();
    	            while (d <= model.getEndValue()) //< to <=
    	        		d += model.getInterval();
    	            model.setEndValue(d);
    	            model.setManual(true);
    				
    	            module.setXAxisModel(model);
    			}
    			catch (Exception e){}
    		}
        }
        
        else if(plotType.equals("Regression") || plotType.equals("Correlation")){
        	Vector vy = dataSet.getYVariables();
        	Vector vx = dataSet.getXVariables();

            if (vy.size() == 0 || vx.size() == 0)
            {
             	showWarning("You must define a y variable and x variable.", this);
                return;
            }
             
            Variable yVar, xVar, zVar;
     		yVar = (Variable) vy.elementAt(0);
     		xVar = (Variable) vx.elementAt(0);

     		if (yVar.getType() != Variable.NUMERIC || xVar.getType() != Variable.NUMERIC)
     		{
             	showWarning("You must define y variable and x variable as numeric.", this);
             	return;
            }
     		     		
     		if(vz.size() == 0) 
     			zVar = null;
     		else 
     			zVar = (Variable) vz.elementAt(0);
             
     		dataSet.scanVariable(xVar, null, null, yVar, zVar, null, null, null, GUIModule.getInstanceCnt());
     		RegressionModule module = new RegressionModule(dataSet, yVar, xVar, zVar);
     		     		
     		gmodule = module;
             
     		if(!"Moments".equals(rptType)){
     			// when report type is moments, draw scatterplot 
     			if(plotType.equals("Regression"))
     				module.setAnalysisMode("LS Fit");
     			else 
     				module.setAnalysisMode(plotType);	
     			//plotType: Regression, Correlation
     		}
 			
        }
        
        else if(plotType.equals("Control Chart")){
        		
        	Vector vy = dataSet.getYVariables();
        	Vector vx = dataSet.getXVariables();

            if (vy.size() == 0 || vx.size() == 0)
            {
             	showWarning("You must define a y variable and x variable.", this);
                return;
            }
             
            Variable yVar, xVar;
     		yVar = (Variable) vy.elementAt(0);
     		xVar = (Variable) vx.elementAt(0);
     		
     		if (yVar.getType() != Variable.NUMERIC || xVar.getType() != Variable.CATEGORICAL)
     		{
             	showWarning("You must define y variable numeric and x variable categorical.", this);
             	return;
            }
     		
     		dataSet.scanVariable(xVar, null, null, yVar, null, null, null, null, GUIModule.getInstanceCnt());
     		ControlChartModule module = new ControlChartModule(dataSet, yVar, xVar);
     		     		
     		gmodule = module;
        		
        }
        else if(plotType.equals("Freq Chart")){
        		
    		Vector vy = dataSet.getYVariables();
    		if (vy.size() == 0)
    		{
    			showWarning("You must define a y variable.", this);
    			return;
    		}

    		Variable yVar, zVar;
        	yVar = (Variable) vy.elementAt(0);

        	if (yVar.getType() != Variable.CATEGORICAL)
        	{
        		showWarning("You must define y variable as categorical.", this);
                return;
        	}
        		
        	if(vz.size() == 0) 
        		zVar = null;
        	else 
        		zVar = (Variable) vz.elementAt(0);
        		
        	Variable fVar = dataSet.getFreqVariable();
         	
        	if(hasF && fVar.getType() != Variable.NUMERIC) 
         	{
         		showWarning("You must define frequency variable as numeric.", this);
         		return;
         	}
        			
         	dataSet.scanVariable(null, null, null, yVar, zVar, null, null, fVar, GUIModule.getInstanceCnt());
        	FreqModule module = new FreqModule(dataSet, yVar, zVar);
        	        		
        	gmodule = module;
    				
        }
        
        else if(plotType.equals("K Sample")){
        	Vector vy = dataSet.getYVariables();
        	Vector vx = dataSet.getXVariables();

            if (vy.size() == 0 || vx.size() == 0)
            {
             	showWarning("You must define a y variable and x variable.", this);
                return;
            }
             
            Variable yVar, xVar, zVar;
     		yVar = (Variable) vy.elementAt(0);
     		xVar = (Variable) vx.elementAt(0);

     		if (yVar.getType() != Variable.NUMERIC || xVar.getType() != Variable.CATEGORICAL)
     		{
             	showWarning("You must define y variable as numeric and x variable as categorical.", this);
             	return;
            }
     		     		
     		if(vz.size() == 0) 
     			zVar = null;
     		else 
     			zVar = (Variable) vz.elementAt(0);
             
     		dataSet.scanVariable(xVar, null, null, yVar, zVar, null, null, null, GUIModule.getInstanceCnt());
     		KSampleModule module = new KSampleModule(dataSet, yVar, xVar, zVar);
     		
     		
     		gmodule = module;
        }
        
        else if(plotType.equals("Contingency Table")){
        	Vector vy = dataSet.getYVariables();
        	Vector vx = dataSet.getXVariables();

            if (vy.size() == 0 || vx.size() == 0)
            {
             	showWarning("You must define a y variable and x variable.", this);
                return;
            }
             
            Variable yVar, xVar, zVar;
     		yVar = (Variable) vy.elementAt(0);
     		xVar = (Variable) vx.elementAt(0);

     		if (yVar.getType() != Variable.CATEGORICAL || xVar.getType() != Variable.CATEGORICAL)
     		{
             	showWarning("You must define y variable and x variable as categorical.", this);
             	return;
            }
     		     		
     		if(vz.size() == 0) 
     			zVar = null;
     		else 
     			zVar = (Variable) vz.elementAt(0);
     		
     		Variable fVar = dataSet.getFreqVariable();
     		if(hasF && fVar.getType() != Variable.NUMERIC) 
     		{
     			showWarning("You must define frequency variable as numeric.", this);
     			return;
     		}
             
     		dataSet.scanVariable(xVar, null, null, yVar, zVar, null, null, fVar, GUIModule.getInstanceCnt());
     		CTableModule module = new CTableModule(dataSet, yVar, xVar, zVar);
     		     		
     		gmodule = module;

        }
        else if(plotType.equals("Logistic")){
        	Vector vy = dataSet.getYVariables();
        	Vector vx = dataSet.getXVariables();

            if (vy.size() == 0 || vx.size() == 0)
            {
             	showWarning("You must define a y variable and x variable.", this);
                return;
            }
             
            Variable yVar, xVar, zVar;
     		yVar = (Variable) vy.elementAt(0);
     		xVar = (Variable) vx.elementAt(0);

     		if (yVar.getType() != Variable.CATEGORICAL || xVar.getType() != Variable.NUMERIC)
     		{
             	showWarning("You must define y variable as categorical and x variable as numeric.", this);
             	return;
            }
     		     		
     		if(vz.size() == 0) 
     			zVar = null;
     		else 
     			zVar = (Variable) vz.elementAt(0);
             
     		dataSet.scanVariable(xVar, null, null, yVar, zVar, null, null, null, GUIModule.getInstanceCnt());
     		LogisticModule module = new LogisticModule(dataSet, yVar, xVar, zVar);
     		     		
     		gmodule = module;
        }
        
        else if(plotType.equals("Chart")){
        	Vector vy = dataSet.getYVariables();
        	Vector vx = dataSet.getXVariables();

            if (vy.size() == 0 || vx.size() == 0)
            {
             	showWarning("You must define a y variable and x variable.", this);
                return;
            }
             
            Variable yVar, xVar, zVar;
     		yVar = (Variable) vy.elementAt(0);
     		xVar = (Variable) vx.elementAt(0);

     		if (yVar.getType() != Variable.NUMERIC || xVar.getType() != Variable.CATEGORICAL)
     		{
             	showWarning("You must define y variable as numeric and x variable as categorical.", this);
             	return;
            }
     		     		
     		if(vz.size() == 0) 
     			zVar = null;
     		else 
     		{ 
     			zVar = (Variable) vz.elementAt(0);
     			if (zVar.getType() != Variable.CATEGORICAL)
         		{
                 	showWarning("Z variable must be categorical to construct a chart.", this);
                 	return;
                }
     		}
             
     		dataSet.scanVariable(xVar, null, null, yVar, zVar, null, null, null, GUIModule.getInstanceCnt());
     		ChartModule module = new ChartModule(dataSet, yVar, xVar, null);
     		     		
     		gmodule = module;
        }
        	
        	
        Border bd = BorderFactory.createLineBorder(Color.black);
        gmodule.setBorder(bd);
        setContentPane(gmodule);
        setJMenuBar(gmodule.getJMenuBar());        	
        	
        //setSize(420, 490);
        	
        //Add menu for displaying data
        JMenu dataMenu = new JMenu("Data");
        getJMenuBar().add(dataMenu);
        getJMenuBar().setBorder(bd);

        dataMenu.addMouseListener(new MouseAdapter() {
        	public void mouseClicked(MouseEvent e)
        	{
        		LaunchOption.trustedEnv = false;
        		if(activeTable == null)
        			activeTable = new SimpleDataEntryTable(activeData);
            	activeTable.show();    
        	}	
        });
	}

	private void decodeValues(String values, ArrayList column){
		
		StringTokenizer tokenizer=new StringTokenizer(values, " ");
    	while(tokenizer.hasMoreTokens())
    	{
    		String value=tokenizer.nextToken().trim();
    		column.add(value);
    	}

	}
	
	private void showWarning(String msg, Component rootComp)
	{
		JOptionPane.showMessageDialog(rootComp, msg);
	}
}
