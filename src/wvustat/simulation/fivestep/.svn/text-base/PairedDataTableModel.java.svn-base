package wvustat.simulation.fivestep;

import wvustat.simulation.model.DataPair;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 31, 2003
 * Time: 1:51:30 PM
 * To change this template use Options | File Templates.
 */
public class PairedDataTableModel extends AbstractTableModel
{
    private List dataPairList = new ArrayList();
    private int initialLength = 100;

    public PairedDataTableModel()
    {
        for (int i = 0; i < initialLength; i++)
        {
            dataPairList.add(new DataPair());
        }
    }

    public int getRowCount()
    {
        return dataPairList.size();
    }

    public int getColumnCount()
    {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        DataPair pair = (DataPair) dataPairList.get(rowIndex);
        switch (columnIndex)
        {
            case 0:
                return pair.getX();
            default:
                return pair.getY();
        }

    }

    public String getColumnName(int column)
    {
        if (column == 0)
            return "X";
        else
            return "Y";
    }

    public Class getColumnClass(int columnIndex)
    {
        return Double.class;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        DataPair pair = (DataPair) dataPairList.get(rowIndex);
        boolean dataValid = false;
        if (aValue != null && aValue.equals("")==false)
        {
            try
            {
                Double d=(Double)aValue;

                switch (columnIndex)
                {
                    case 0:
                        pair.setX(d);
                        break;
                    default:
                        pair.setY(d);
                        break;
                }
                dataValid = true;
            }
            catch (NumberFormatException e)
            {

            }
        }

        if (dataValid == false)
        {
            switch (columnIndex)
            {
                case 0:
                    pair.setX(null);
                    break;
                default:
                    pair.setY(null);
                    break;
            }
        }

        this.fireTableCellUpdated(rowIndex, columnIndex);
    }

    public List getSpecifiedPairs()
    {
        List returnList = new ArrayList();

        for (int i = 0; i < dataPairList.size(); i++)
        {
            DataPair pair = (DataPair) dataPairList.get(i);
            if (pair.isValid())
                returnList.add(pair);
        }

        return returnList;
    }

    public void addRows(int numOfRows)
    {
        for (int i = 0; i < numOfRows; i++)
        {
            dataPairList.add(new DataPair());
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

}
