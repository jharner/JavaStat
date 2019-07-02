package wvustat.math.expression;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 25, 2003
 * Time: 8:48:40 AM
 * To change this template use Options | File Templates.
 */
public class ParameterTableModel extends AbstractTableModel
{
    private ExpressionObject function;
    private List parameters;
    private String[] colNames = {"Name", "Value", "", "Standard Error"};

    public ParameterTableModel(ExpressionObject function)
    {
        this.function = function;
        this.parameters = function.getParameters();
    }

    public int getRowCount()
    {
        return parameters.size();
    }

    public int getColumnCount()
    {
        return colNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Parameter param = (Parameter) parameters.get(rowIndex);
        switch (columnIndex)
        {
            case 0:
                return param.getName();
            case 1:
                return new Double(param.getValue());
            case 2:
                return function;
            case 3:
                return new Double(param.getSE());
        }
        return null;
    }

    public String getColumnName(int col)
    {
        return colNames[col];
    }

    public Class getColumnClass(int colIndex)
    {
        switch (colIndex)
        {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            case 2:
                return ExpressionObject.class;
            case 3:
                return Double.class;
        }
        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if(columnIndex==2)
        {
            function.setVariableValue(getValueAt(rowIndex,0).toString(),((Double)aValue).doubleValue());
            ((Parameter)parameters.get(rowIndex)).setSE(0);
            this.fireTableChanged(new TableModelEvent(this, rowIndex, rowIndex, TableModelEvent.ALL_COLUMNS));
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if(columnIndex==2)
            return true;
        else
            return false;
    }
}

