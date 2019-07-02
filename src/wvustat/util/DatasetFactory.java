package wvustat.util;

import wvustat.data.*;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.table.DataSetTM;
import wvustat.network.GDataSet;

import java.awt.*;
import java.util.Arrays; //keep
import java.util.List;
import java.util.Vector;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: May 9, 2004
 * Time: 2:13:28 PM
 * To change this template use Options | File Templates.
 */
public class DatasetFactory
{	
    public static DataSetTM createDataSet(String name, List columns) throws Exception
    {
        int size = ((List)columns.get(0)).size() - 1; //deduct 1 to size, modified by djluo 10/10/2005 
        BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(Color.black);

        Vector vars=new Vector();

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);

        boolean[] boolArray=new boolean[columns.size()]; //flag for integer
        for(int i=0;i<columns.size();i++)
        {
            List column=(List)columns.get(i);
            String colName=(String)column.get(0);
            column.remove(0);
            String firstValue=(String)column.get(0);
            boolean isColNumeric=false;
            try
            {
                double d=Double.parseDouble(firstValue);
                isColNumeric=true;
                boolArray[i]=isWholeNumber(d);
            }
            catch(NumberFormatException ex)
            {

            }
            Vector values;
            if(isColNumeric)
            {
                values=new Vector();
                for(int j=0;j<column.size();j++)
                {
                    try
                    {
                        Double d=new Double((String)column.get(j));
                        boolArray[i]=boolArray[i]&&isWholeNumber(d.doubleValue());
                        values.add(d);
                    }
                    catch(NumberFormatException e)
                    {
                        values.add(new Double(Double.NaN));
                    }
                }
            }
            else
                values=new Vector(column);
            if(isColNumeric)
            {
                NumVariable numVar=new NumVariable(colName, values);
                vars.add(numVar);
            }
            else
            {
                CatVariable var=new CatVariable(colName, values);
                vars.add(var);
            }
        }

        DataSet ds = new DataSetImpl(name, vars);
        DataSetTM dataSetTM=new DataSetTM(ds);
        for(int i=2;i<dataSetTM.getColumnCount();i++)
        {
            Variable v=ds.getVariable(i-2);
            if(v instanceof NumVariable && boolArray[i-2])
            {
                dataSetTM.setColumnFormat(0,i);
            }
        }

        return dataSetTM;
    }

    private static boolean isWholeNumber(double value)
    {
        return value==(int)value;
    }
    
    public static DataSetTM convertGDataSet(GDataSet data) throws Exception
    {
    	List columns = data.getColumns();
        int size = ((List)columns.get(0)).size() - 3; //deduct 3 to size 
        BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(Color.black);

        Vector vars=new Vector();

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);

        boolean[] boolArray=new boolean[columns.size()];
        int[] roleArray = new int[columns.size()];
        
        for(int i=0;i<columns.size();i++)
        {
            List column=(List)columns.get(i);
            String colName=(String)column.get(0);
            column.remove(0);
            Class colClass=(Class)column.get(0);
            column.remove(0);
            roleArray[i] = ((Integer)column.get(0)).intValue();
            column.remove(0);
           
            boolean isColNumeric = colClass==Double.class||colClass==Integer.class;
            boolArray[i] = colClass==Integer.class;
            
            Vector values=new Vector(column);
            
            if(isColNumeric)
            {
                NumVariable numVar=new NumVariable(colName, values);
                vars.add(numVar);
            }
            else
            {
                CatVariable var=new CatVariable(colName, values);
                vars.add(var);
            }
        }

        DataSet ds = new DataSetImpl(data.getName(), vars);
        DataSetTM dataSetTM=new DataSetTM(ds);
        for(int i=2;i<dataSetTM.getColumnCount();i++)
        {
            Variable v=ds.getVariable(i-2);
            if(v instanceof NumVariable && boolArray[i-2])
            {
                dataSetTM.setColumnFormat(0,i);
            }
            if(v instanceof NumVariable && !boolArray[i-2])
            {
                dataSetTM.setColumnFormat(4,i);
            }
            
            dataSetTM.setColumnRole(roleArray[i-2], i);
        }

        dataSetTM.save(); //avoid warning when close table
        return dataSetTM;
    }
    
    public static DataSetTM subsetDataSet(DataSetTM dataSetTM, int[] rowRange, int[] colRange, String tname, boolean linked) throws Exception
    {
    	DataSet data = dataSetTM.getDataSet();
    	int size = rowRange.length;
    	BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        for (int i = 0; i < size; i++)
        	maskVar.setValueAt(data.getTrueMask(rowRange[i]), i);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(data.getColor(rowRange[i]));

        Vector vars=new Vector();

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);
        
        boolean found = false;
        
        for(int i=0;i<colRange.length;i++) 
        {
        	if (colRange[i] < 2) continue;
        	found = true;
        	
        	Vector values = new Vector();
        	Variable v = data.getVariable(colRange[i] - 2);
        	for (int j = 0; j < rowRange.length; j++)
        	{
        		values.addElement(v.getValue(rowRange[j]));
        	}
        	if (v instanceof NumVariable) {
        		NumVariable numVar = new NumVariable(v.getName(), values);
        		vars.add(numVar);
        	}
        	else 
        	{
        		CatVariable var = new CatVariable(v.getName(), values);
        		vars.add(var);
        	}
        }
        if (!found) throw new Exception("No columns are selected.");
        
        if (tname.trim().equals("")) tname = DataSetTM.getDefaultName();
        
        // append 0 and 1 to the beginning of colRange
        int k = Arrays.binarySearch(colRange, 0);
        if (k<0) {
        	int[] temp = new int[colRange.length + 1];
        	temp[0] = 0;
        	System.arraycopy(colRange, 0, temp, 1, colRange.length);
        	colRange = temp;
        }
        k = Arrays.binarySearch(colRange, 1);
        if (k<0) {
        	int[] temp = new int[colRange.length + 1];
        	temp[1] = 1;
        	System.arraycopy(colRange, 1, temp, 2, colRange.length - 1);
        	colRange = temp;
        }
 
        
        DataSet ds;
        if (!linked)
        	ds = new DataSetImpl(tname, vars);
        else
        	ds = new DataSetImpl(tname, vars, data, rowRange, colRange);
        
        DataSetTM dst=new DataSetTM(ds);
    	
        int j = 2;
        for (int i=0; i<colRange.length; i++)
        {
        	if (colRange[i] < 2) continue;
        	Variable v = data.getVariable(colRange[i] - 2);
        	if (v instanceof NumVariable) {
        		dst.setColumnFormat(dataSetTM.getColumnFormat(colRange[i]), j);
        	}
        	dst.setColumnRole(v.getRole(), j);
        	j++;
        }
        
        
        return dst;
    }
    
    
    public static DataSetTM sortDataSet(DataSetTM dataSetTM, int[] orders, String tname) throws Exception
    {
    	DataSet data = dataSetTM.getDataSet();
    	int size = data.getSize();
    	BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        for (int i = 0; i < size; i++)
        	maskVar.setValueAt(data.getTrueMask(orders[i]), i);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(data.getColor(orders[i]));

        Vector vars=new Vector();

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);
        
        for(int i=2; i<dataSetTM.getColumnCount(); i++) 
        {
        	Vector values = new Vector();
        	Variable v = data.getVariable(i - 2);
        	for (int j = 0; j < size; j++)
        	{
        		values.addElement(v.getValue(orders[j]));
        	}
        	if (v instanceof NumVariable) {
        		NumVariable numVar = new NumVariable(v.getName(), values);
        		vars.add(numVar);
        	}
        	else 
        	{
        		CatVariable var = new CatVariable(v.getName(), values);
        		vars.add(var);
        	}
        }

        if (tname.trim().equals("")) tname = DataSetTM.getDefaultName();
        
        DataSet ds = new DataSetImpl(tname, vars);
        DataSetTM dst=new DataSetTM(ds);
    	
        for (int i=2; i<dataSetTM.getColumnCount(); i++)
        {
        	Variable v = data.getVariable(i - 2);
        	if (v instanceof NumVariable) {
        		dst.setColumnFormat(dataSetTM.getColumnFormat(i), i);
        	}
        	dst.setColumnRole(v.getRole(), i);
        }       
        
        return dst;
    }
    
    public static DataSetTM mergeDataSetSideBySide(DataSetTM dataSetTM1, DataSetTM dataSetTM2, String tname, boolean isDupRemoved) throws Exception
    {
    	DataSet data1 = dataSetTM1.getDataSet();
    	DataSet data2 = dataSetTM2.getDataSet();
    	int size = data1.getSize();
    	BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(Color.black);

        Vector vars=new Vector();

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);
        
        HashSet varnames = new HashSet();
        
        for(int i=2; i<dataSetTM1.getColumnCount(); i++) 
        {
        	Vector values = new Vector();
        	Variable v = data1.getVariable(i - 2);
        	varnames.add(v.getName());
        	for (int j = 0; j < size; j++)
        	{
        		values.addElement(v.getValue(j));
        	}
        	if (v instanceof NumVariable) {
        		NumVariable numVar = new NumVariable(v.getName(), values);
        		vars.add(numVar);
        	}
        	else 
        	{
        		CatVariable var = new CatVariable(v.getName(), values);
        		vars.add(var);
        	}
        }
        
        for(int i=2; i<dataSetTM2.getColumnCount(); i++) 
        {
        	Vector values = new Vector();
        	Variable v = data2.getVariable(i - 2);
        	
        	if (isDupRemoved && varnames.contains(v.getName())) 
        		continue;
        	
        	for (int j = 0; j < size; j++)
        	{
        		values.addElement(v.getValue(j));
        	}
        	if (v instanceof NumVariable) {
        		NumVariable numVar = new NumVariable(v.getName(), values);
        		vars.add(numVar);
        	}
        	else 
        	{
        		CatVariable var = new CatVariable(v.getName(), values);
        		vars.add(var);
        	}
        }
        
        
        if (tname.trim().equals("")) tname = DataSetTM.getDefaultName();
        
        DataSet ds = new DataSetImpl(tname, vars);
        DataSetTM dst=new DataSetTM(ds);
    	
        for (int i=2; i<dataSetTM1.getColumnCount(); i++)
        {
        	Variable v = data1.getVariable(i - 2);
        	if (v instanceof NumVariable) {
        		dst.setColumnFormat(dataSetTM1.getColumnFormat(i), i);
        	}
        	dst.setColumnRole(v.getRole(), i);
        }       
        
        int numDupNames = 0;
        for (int i=2; i<dataSetTM2.getColumnCount(); i++)
        {
        	Variable v = data2.getVariable(i - 2);
        	if (isDupRemoved && varnames.contains(v.getName())) {
        		numDupNames++;
        		continue;
        	}
        	
        	if (v instanceof NumVariable) {
        		dst.setColumnFormat(dataSetTM2.getColumnFormat(i), i+dataSetTM1.getColumnCount()-2-numDupNames);
        	}
        	dst.setColumnRole(v.getRole(), i+dataSetTM1.getColumnCount()-2-numDupNames);
        }     
        
        return dst;
    }
    
    public static DataSetTM mergeDataSetByLabel(DataSetTM dataSetTM1, DataSetTM dataSetTM2, String tname, boolean isDupRemoved) throws Exception
    {
    	DataSet data1 = dataSetTM1.getDataSet();
    	DataSet data2 = dataSetTM2.getDataSet();
    	
    	Vector label1 = data1.getLabelVariable().getValues();
    	Vector label2 = data2.getLabelVariable().getValues();

    	Vector index1 = new Vector();
    	Vector index2 = new Vector();
    	
    	for (int i = 0; i < label1.size(); i++) {
    		Object o1 = label1.elementAt(i);
    		for (int j = 0; j < label2.size(); j++) {
    			Object o2 = label2.elementAt(j);
    			if (o1.equals(o2)) {
    				index1.addElement(new Integer(i));
    				index2.addElement(new Integer(j));
    				break;
    			}
    		}
    	}
    	
    	//similar process as the side by side merge here:
    	
    	int size = index1.size();
    	BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(Color.black);

        Vector vars=new Vector();

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);
        
        HashSet varnames = new HashSet();
        
        for(int i=2; i<dataSetTM1.getColumnCount(); i++) 
        {
        	Vector values = new Vector();
        	Variable v = data1.getVariable(i - 2);
        	varnames.add(v.getName());
        	for (int j = 0; j < size; j++)
        	{
        		values.addElement(v.getValue(  ((Integer)index1.get(j)).intValue()  ));
        	}
        	if (v instanceof NumVariable) {
        		NumVariable numVar = new NumVariable(v.getName(), values);
        		vars.add(numVar);
        	}
        	else 
        	{
        		CatVariable var = new CatVariable(v.getName(), values);
        		vars.add(var);
        	}
        }
        
        for(int i=2; i<dataSetTM2.getColumnCount(); i++) 
        {
        	Vector values = new Vector();
        	Variable v = data2.getVariable(i - 2);
        	
        	if (isDupRemoved && varnames.contains(v.getName())) 
        		continue;
        	
        	for (int j = 0; j < size; j++)
        	{
        		values.addElement(v.getValue(  ((Integer)index2.get(j)).intValue()  ));
        	}
        	if (v instanceof NumVariable) {
        		NumVariable numVar = new NumVariable(v.getName(), values);
        		vars.add(numVar);
        	}
        	else 
        	{
        		CatVariable var = new CatVariable(v.getName(), values);
        		vars.add(var);
        	}
        }
        
        
        if (tname.trim().equals("")) tname = DataSetTM.getDefaultName();
        
        DataSet ds = new DataSetImpl(tname, vars);
        DataSetTM dst=new DataSetTM(ds);
    	
        for (int i=2; i<dataSetTM1.getColumnCount(); i++)
        {
        	Variable v = data1.getVariable(i - 2);
        	if (v instanceof NumVariable) {
        		dst.setColumnFormat(dataSetTM1.getColumnFormat(i), i);
        	}
        	dst.setColumnRole(v.getRole(), i);
        }       
        
        int numDupNames = 0;
        for (int i=2; i<dataSetTM2.getColumnCount(); i++)
        {
        	Variable v = data2.getVariable(i - 2);
        	if (isDupRemoved && varnames.contains(v.getName())) {
        		numDupNames++;
        		continue;
        	}
        	
        	if (v instanceof NumVariable) {
        		dst.setColumnFormat(dataSetTM2.getColumnFormat(i), i+dataSetTM1.getColumnCount()-2-numDupNames);
        	}
        	dst.setColumnRole(v.getRole(), i+dataSetTM1.getColumnCount()-2-numDupNames);
        }     
        
        return dst;
    }
}
