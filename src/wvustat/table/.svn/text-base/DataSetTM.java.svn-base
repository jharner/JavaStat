/*
 * DataSetTM.java
 *
 * Revised on June 2, 2008
 */

package wvustat.table;

import wvustat.data.BoolVariable;
import wvustat.data.CatVariable;
import wvustat.data.DataSetImpl;
import wvustat.data.NumVariable;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.interfaces.RemoteObserver;

import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.File;
import java.util.*;


/**
 *
 * @author  
 * @version
 */
public class DataSetTM extends AbstractTableModel implements RemoteObserver
{
	private static final long serialVersionUID = 1L;
	
	protected Vector colNames, colClasses, colData; //colNames, colClasses, colData are only used for constructor DataSetTM(int,int)
    protected int colCount, rowCount;
    protected String name;
    protected DataSet dataSet;
    protected boolean isChanged = false, isSaved = false;
    protected File saveFile;
    protected Map formatMap=new HashMap();
    protected static int numUntitled = 0;
    
    
    public static String getDefaultName()
    {
    	return "untitled" + " " + String.valueOf(++numUntitled);
    }
    
    /** Creates new DataSetTM */
    public DataSetTM(int rows, int cols)
    {
        this.rowCount = rows;
        this.colCount = 2 + cols;
        
        name = getDefaultName();

        init();
        dataSet = convertToDataSet();
        
        dataSet.addRemoteObserver(this);
    }

    public DataSetTM(DataSet dataSet)
    {
        this.dataSet = dataSet;
        name = dataSet.getName();
        
        dataSet.addRemoteObserver(this);
        
        rowCount = dataSet.getSize();
        colCount = dataSet.getVariableCount() + 2;
    }

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        dataSet.setName(name);
    }

    public File getFile()
    {
        return saveFile;
    }

    public void setFile(File saveFile)
    {
        this.saveFile = saveFile;
    }

    public boolean isChanged()
    {
        return isChanged;
    }

    public void save()
    {
        isSaved = true;
    }

    public boolean isSaved()
    {
        return isSaved;
    }

    private void init()
    {
        colNames = new Vector(10);
        colClasses = new Vector(10);
        colData = new Vector(10);

        colNames.addElement("C");
        colNames.addElement("M");

        colClasses.addElement(Color.class);
        colClasses.addElement(Boolean.class);

        Vector v1 = new Vector();
        Vector v2 = new Vector();


        for (int i = 0; i < rowCount; i++)
        {
            v2.addElement(Boolean.FALSE);
            v1.addElement(Color.black);

        }

        colData.addElement(v1);
        colData.addElement(v2);


        for (int i = 0; i < colCount - 2; i++)
        {
            colNames.addElement(String.valueOf((char) (89 + i)));
            colClasses.addElement(Double.class);

            Vector col = new Vector();
            for (int j = 0; j < rowCount; j++)
            {
                col.addElement(Variable.NUM_MISSING_VAL);
            }
            colData.addElement(col);
        }
    }

    private DataSet convertToDataSet()
    {
        Vector vars = new Vector(10);

        Vector colorVar = (Vector) colData.elementAt(0);
        BoolVariable maskVar = new BoolVariable((Vector) colData.elementAt(1));
        BoolVariable stateVar = new BoolVariable(rowCount);

        vars.addElement(stateVar);
        vars.addElement(colorVar);
        vars.addElement(maskVar);

        
        for (int i = 2; i < colData.size(); i++)
        {
            Vector v = (Vector) colData.elementAt(i);
            Class c = (Class) colClasses.elementAt(i);
            String n = colNames.elementAt(i).toString();
            Variable var = null;
            if (c == Double.class)
                var = new NumVariable(n, v);
            else
                var = new CatVariable(n, v);
            
            vars.addElement(var);
        }

        return new DataSetImpl(name, vars);
        
    }

    public int getColumnCount()
    {
        return dataSet.getVariableCount() + 2;
    }

    public int getRowCount()
    {
        return dataSet.getSize();
    }

    public Object getValueAt(int row, int col)
    {
        Object obj = null;
        
        if (col == 0)
            //obj = dataSet.getColor(row);
        	obj = new MyColor(dataSet.getColor(row), dataSet.getMarker(row));
        
        else if (col == 1)
            obj = new Boolean(dataSet.getTrueMask(row)); 
            				//getMask()->getTrueMask(), by djluo for missing value

        else if (col > 1)
        {
            Variable v = dataSet.getVariable(col - 2);
            obj = v.getValue(row);
            if (obj instanceof Double)
            {
                double tmpD = ((Double) obj).doubleValue();
                if (!Double.isNaN(tmpD))
                {
                    //tmpD = Math.round(tmpD * Math.pow(10, 4)) / Math.pow(10, 4);
                    int decimalPoints=getColumnFormat(col);
                    if(decimalPoints==0)
                        obj=new Integer((int)tmpD);
                    else if(decimalPoints>0)
                    {
                        tmpD = Math.round(tmpD * Math.pow(10, decimalPoints)) / Math.pow(10, decimalPoints);
                        obj = new Double(tmpD);
                    }
                    else
                        obj = new Double(tmpD);

                }
            }
        }

        return obj;
    }

    public void setValueAt(Object value, int row, int col)
    {
        
        if (col == 0)
        {
        	if (value instanceof Color) {
        		Color c = (Color) value;
        		dataSet.setColor(c, row);
        	} 
        	else if (value instanceof Integer) {
        		dataSet.setMarker((Integer)value, row);
        	}
        }
        else if (col == 1)
        {
            boolean bl = ((Boolean) value).booleanValue();
            dataSet.setMask(bl, row);
        }
        else
        {

            Variable v = dataSet.getVariable(col - 2);

            if (v.getType() == Variable.NUMERIC)
            {
                try
                {
                    Double d = new Double(value.toString());
                    dataSet.setVarValue(v.getName(), d, row);
                }
                catch (NumberFormatException error)
                {
                    dataSet.setVarValue(v.getName(), Variable.NUM_MISSING_VAL, row);
                }
            }
            else {
                if(value == null) 
               		dataSet.setVarValue(v.getName(), Variable.CAT_MISSING_VAL, row);
                else 
                	dataSet.setVarValue(v.getName(), value, row);
            }

            isChanged = true;
        }
        
        fireTableCellUpdated(row, col);

    }
    
    public void setMasks(Object value, int[] row)
    {
    	boolean bl = ((Boolean) value).booleanValue();
        dataSet.setMasks(bl, row);
        
        fireTableChanged(new TableModelEvent(this, 0, getRowCount() - 1, 1));
    }

    public String getColumnName(int col)
    {
        if (col == 0)
            return "C";
        else if (col == 1)
            return "M";
        else {
            Variable v = dataSet.getVariable(col - 2);
            return v.getName();
        }
    }

    public Class getColumnClass(int col)
    {
        if (col == 0)
            return Color.class;
        else if (col == 1)
            return Boolean.class;
        else
        {
            Variable v = dataSet.getVariable(col - 2);
            if (v.getType() == Variable.CATEGORICAL)
            {
                return String.class;
            }
            else
            {
                if (getColumnFormat(col) == 0)
                    return Integer.class;
                else
                    return Double.class;
            }
        }
    }

    public void setColumnName(String newName, int col)
    {
        if (col < 2)
            return;

        Variable v = dataSet.getVariable(col - 2);
        dataSet.setColumnName(newName, v);
        
        isChanged = true;
        
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    public Variable setColumnClass(Class cl, int col)
    {
        if (col < 2)
            return null;

        
        Variable v = dataSet.getVariable(col - 2);
        Vector vec = v.getValues();
        Vector vec2 = new Vector(vec.size());

        if (cl == Double.class)
        {
            for (int i = 0; i < vec.size(); i++)
            {
                try
                {
                    Double d = new Double(vec.elementAt(i).toString());
                    vec2.addElement(d);
                }
                catch (NumberFormatException e)
                {
                    vec2.addElement(new Double(Double.NaN));
                }
            }

            Variable numVar = new NumVariable(v.getName(), vec2);
            numVar.setRole(v.getRole());
            dataSet.setVariableAt(numVar, col - 2);
            isChanged = true;
            return numVar;
        }
        else
        {
            for (int i = 0; i < vec.size(); i++)
            {
                if (vec.elementAt(i).equals(Variable.NUM_MISSING_VAL))
                    vec2.addElement(Variable.CAT_MISSING_VAL);
                else
                {
                    Object obj=vec.elementAt(i);
                    if(obj instanceof Number)
                    {
                        Number num=(Number)obj;
                        if(isWholeNumber(num))
                            vec2.addElement(String.valueOf(num.intValue()));
                        else
                            vec2.addElement(String.valueOf(num.doubleValue()));
                    }
                    else
                        vec2.addElement(vec.elementAt(i).toString());
                }
            }

            CatVariable catVar = new CatVariable(v.getName(), vec2);
            catVar.setRole(v.getRole());
            dataSet.setVariableAt(catVar, col - 2);
            isChanged = true;
            return catVar;
        }
    }

    private boolean isWholeNumber(Number num)
    {
        return num.doubleValue()==num.intValue();
    }
    
    public void setColumnRole(int role, int col)
    {
        dataSet.setRole(role, col - 2);
        isChanged = true;
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    public void setColumnAttributes(ColumnAttributes attr, int col)
    {
        if (col < 2)
            return;

        Variable v = dataSet.getVariable(col - 2);
        dataSet.setColumnName(attr.getName(), v);

        int type = attr.getType();
        if(type!=v.getType())
        {
            Class cls = type == Variable.NUMERIC ? Double.class:String.class;
            v=setColumnClass(cls, col);//assign to v, the new variable, before setting role.
        }
        
        //v = dataSet.getVariable(col - 2);
        dataSet.setRole(v, attr.getRole());
        setColumnFormat(attr.getNumOfDigits(), col);
        
        //no effects on numeric variable
        dataSet.setVarLevels(attr.isOrdinal(), attr.isLevelCheck(), attr.getLevels(), col - 2);
                
        fireTableStructureChanged();
    }

    public void setRowNumberEnabled(boolean value)
    {
        dataSet.setRowNumberEnabled(value);
        
        fireTableStructureChanged();
    }

    public boolean isCellEditable(int row, int col)
    {
        if (col == 0) // Color
            return false;
        else
            return true;
    }

    public void addRows(int numOfRows)
    {
        int oldRowCount = rowCount;
        rowCount += numOfRows;
        Vector buffer = new Vector();

        for (int i = 0; i < numOfRows; i++)
        {
            Vector vals = new Vector();

            vals.addElement(Color.black); //add color
            vals.addElement(new Boolean(false)); //mask
                
            // the next one line was decommented by djluo 5/24/2005
            vals.addElement(new Boolean(false)); //state

            for (int j = 0; j < dataSet.getVariableCount(); j++)
            {
                Variable v = dataSet.getVariable(j); //(Variable)vars.elementAt(j);
                if (v.getType() == Variable.NUMERIC)
                {
                    vals.addElement(Variable.NUM_MISSING_VAL);
                }
                else if (v.getType() == Variable.CATEGORICAL)
                {
                    vals.addElement(Variable.CAT_MISSING_VAL);
                }
            }
            
            buffer.addElement(vals);
        }
        
        dataSet.addObservations(buffer);
        
        isChanged = true;
        fireTableRowsInserted(oldRowCount, rowCount-1);//modify to rowCount-1 by djluo
    }

    
    public void deleteRows(int[] index)
    {
    	
    	dataSet.deleteObservations(index);
    	//rowCount = dataSet.getSize();
		
    	isChanged = true;
    	this.fireTableDataChanged();
    }

    public void addColumn(ColumnAttributes attr)
    {
        Variable newVar = null;
        
        switch (attr.getType())
        {
            case Variable.NUMERIC:
                Vector v = new Vector(rowCount);
                for (int i = 0; i < rowCount; i++)
                {
                    v.addElement(Variable.NUM_MISSING_VAL);
                }

                newVar = new NumVariable(attr.getName(), v);
                break;
            case Variable.CATEGORICAL:
                Vector v2 = new Vector(rowCount);
                for (int i = 0; i < rowCount; i++)
                {
                    v2.addElement(Variable.CAT_MISSING_VAL);
                }
                newVar = new CatVariable(attr.getName(), v2);
                break;
        }
        
        dataSet.addVariable(newVar);
        //colCount = getColumnCount();
        setColumnFormat(attr.getNumOfDigits(), colCount-1);
        dataSet.setRole(newVar, attr.getRole());
        
        //no effects on numeric variable
        newVar.setOrdinal(attr.isOrdinal());
        newVar.setLevelCheck(attr.isLevelCheck());
        newVar.setLevels(attr.getLevels());
        
        isChanged = true;
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    public int getColumnFormat(int columnIndex)
    {
        Integer intObj=(Integer)formatMap.get(new Integer(columnIndex));
        if(intObj==null)
            return -1;
        else
            return intObj.intValue();
    }

    public void setColumnFormat(int decimalPoints, int columnIndex)
    {
        if(decimalPoints>-1)
        {
            formatMap.put(new Integer(columnIndex), new Integer(decimalPoints));
        }
    }

    public Variable getVariable(int col)
    {
        return dataSet.getVariable(col - 2);
    }

    public void deleteColumn(int col)
    {
        if (col < 2)
            return;

        dataSet.deleteVariable(col - 2);
        //colCount = getColumnCount();

        isChanged = true;
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    public void update(String arg) 
    {
        TableModelEvent event = null;

        if (arg.equals("yycolor"))
        {
            event = new TableModelEvent(this, 0, getRowCount() - 1, 0);
            fireTableChanged(event);
            return;
        }
        else if (arg.equals("yymask")) // masks or value changed
        {
        	//event = new TableModelEvent(this, 0, getRowCount() - 1, 1);
        	//fireTableChanged(event);
        	
        	fireTableDataChanged();
            return;
        }
        else if (arg.equals("obs_deleted"))
        {
        	rowCount = dataSet.getSize();
        	fireTableDataChanged();
        	return;
        }
        else if (arg.equals("add_variable")) // for transformation
        {
        	colCount = dataSet.getVariableCount() + 2;
            setColumnFormat(4, colCount - 1);
            fireTableStructureChanged();
            return;
        }
        else if (arg.equals("delete_variable")) //added by djluo for transformation
        {
        	colCount = dataSet.getVariableCount() + 2;
            fireTableStructureChanged();
            return;
        }
    }

    public void moveColumn(int from, int to)
    {
        if (from < 2)
            return;
        
        Variable v = dataSet.getVariable(from - 2);
        dataSet.moveVariableTo(v, to - 2);

        this.fireTableStructureChanged();
    }

    /**
     * Move columns with index in the range 'from'-'to' inclusive to the new position 'index'
     * @param from
     * @param to
     * @param index - the column index on screen - 2
     */
    public void moveColumns(int from, int to, int index)
    {

        if (from < 2)
            return;

        ArrayList variableList = new ArrayList();
        for (int i = from; i <= to; i++)
        {
            Variable v = dataSet.getVariable(i - 2);
            variableList.add(v);
        }

        dataSet.moveVariablesTo(variableList, index);

        this.fireTableStructureChanged();
    }

    public int getColumnIndex(String columnName)
    {
    	int ret = -1;
    	for (int i = 0; i < dataSet.getVariableCount(); i++)
    	{
    		Variable v = dataSet.getVariable(i);
    		if (columnName.equals(v.getName())) {
    			ret = i + 2;
    			break;
    		}
    	}
    	return ret;
    }

}
