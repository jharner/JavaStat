/*
 * DataSet.java
 *
 * Created on December 17, 2001, 2:03 PM
 */

package wvustat.swing.table;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class DataSet extends AbstractTableModel
{
    public static final int MAX_ROWS = 100;
    public static final int MAX_COLUMNS = 30;

    public static final int UNDEFINED_ROLE = 0;
    public static final int X_ROLE = 1;
    public static final int Y_ROLE = 2;
    public static final int Z_ROLE = 3;
    public static final int FREQ_ROLE=4;

    protected double[][] data = new double[300][20];
    protected int rowDim, colDim;
    protected List colNames;
    protected List colRoles;
    protected File dataFile; //The associated file on disk, if there is any
    protected List colors = new ArrayList();

    /** Creates new DataSet */
    public DataSet(int rowDim, int colDim, String[] colNameArray)
    {
        this.rowDim = rowDim;
        this.colDim = colDim;
        this.colNames = new ArrayList(Arrays.asList(colNameArray));
        this.colRoles = new ArrayList();
        colNames.add(0, "Color");

        for (int i = 0; i < rowDim; i++)
        {
            for (int j = 0; j < colDim; j++)
            {
                data[i][j] = 0;
            }
            colors.add(Color.black);
        }


        colRoles.add(new Integer(X_ROLE));
        colRoles.add(new Integer(Y_ROLE));
        for (int i = 2; i < colDim; i++)
            colRoles.add(new Integer(UNDEFINED_ROLE));
    }

    public DataSet()
    {
        this(0, 2, new String[]{"x", "y"});
    }

    public DataSet(int rows)
    {
        this(rows, 2, new String[]{"x", "y"});
    }

    public DataSet(double[][] values, List colNames)
    {
        rowDim = values.length;
        colDim = values[0].length;
        this.colNames = colNames;
        colNames.add(0, "Colors");

        for (int i = 0; i < values.length; i++)
        {
            for (int j = 0; j < values.length; j++)
            {
                data[i][j] = values[i][j];
            }
            colors.add(Color.black);
        }
    }

    public void clearAllRows()
    {
        for (int i = 0; i < rowDim; i++)
        {
            for (int j = 0; j < colDim; j++)
            {
                data[i][j] = 0;
            }
        }
        rowDim = 0;
        colDim = 2;
        colNames.clear();
        colNames.add("Color");
        colNames.add("x");
        colNames.add("y");
        colors.clear();
    }

    public void clear()
    {
        for (int i = 0; i < rowDim; i++)
        {
            for (int j = 0; j < colDim; j++)
            {
                data[i][j] = 0;
            }
        }
        rowDim = 0;
        colDim = 0;
        colNames.clear();
        colRoles.clear();
        colNames.add("Color");
        colors.clear();
    }

    public DataSet(double[] x, double[] y)
    {
        this(x.length, 2, new String[]{"x", "y"});

        for (int i = 0; i < x.length; i++)
        {
            data[i][0] = x[i];
            data[i][1] = y[i];
        }
    }

    public synchronized double[][] getPlotData()
    {
        double[][] array = new double[2][];

        int xRoleCol = getFirstColumnForRole(X_ROLE);
        int yRoleCol = getFirstColumnForRole(Y_ROLE);

        if (xRoleCol == -1 || yRoleCol == -1)
            return null;

        array[0] = getColumnData(xRoleCol-1);
        array[1] = getColumnData(yRoleCol-1);

        return array;
    }

    public List getColors()
    {
        return colors;
    }

    public void setColor(Color color, int index)
    {
        colors.set(index, color);
    }

    private int getFirstColumnForRole(int role)
    {
        for (int i = 0; i < colDim+1; i++)
        {
            if (getColumnRole(i) == role)
            {
                return i;
            }
        }

        return -1;
    }

    public synchronized void addPoint(double x, double y)
    {
        rowDim++;

        data[rowDim - 1][0] = x;
        data[rowDim - 1][1] = y;

        colors.add(Color.black);
        this.fireTableRowsInserted(rowDim-1,rowDim);
    }

    public double getMaximumX()
    {
        int xRoleCol = getFirstColumnForRole(X_ROLE);
        if (xRoleCol == -1)
            return 0;

        double[] array = getColumnData(xRoleCol);
        double max = Double.MIN_VALUE;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > max)
                max = array[i];
        }

        return max;
    }

    public double getMinimumX()
    {
        int xRoleCol = getFirstColumnForRole(X_ROLE);
        if (xRoleCol == -1)
            return 0;

        double[] array = getColumnData(xRoleCol);
        double min = Double.MAX_VALUE;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] < min)
                min = array[i];
        }

        return min;
    }

    public double getMaximumY()
    {
        int yRoleCol = getFirstColumnForRole(Y_ROLE);
        if (yRoleCol == -1)
            return 0;
        double[] array = this.getColumnData(yRoleCol);
        double max = Double.MIN_VALUE;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > max)
                max = array[i];
        }

        return max;
    }

    public double getMinimumY()
    {
        int yRoleCol = getFirstColumnForRole(Y_ROLE);
        if (yRoleCol == -1)
            return 0;
        double[] array = this.getColumnData(yRoleCol);
        double min = Double.MAX_VALUE;

        for (int i = 0; i < array.length; i++)
        {
            if (array[i] < min)
                min = array[i];
        }

        return min;
    }

    public synchronized double[] getXValues()
    {
        int xRoleCol = getFirstColumnForRole(X_ROLE);
        if (xRoleCol == -1)
            return null;

        return getColumnData(xRoleCol-1);
    }

    public synchronized double[] getYValues()
    {
        int yRoleCol = getFirstColumnForRole(Y_ROLE);
        if (yRoleCol == -1)
            return null;
        return getColumnData(yRoleCol-1);
    }

    public synchronized int getSize()
    {
        return rowDim;
    }

    public Class getColumnClass(int columnIndex)
    {
        if (columnIndex == 0)
            return Color.class;
        else
            return Double.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    public String getColumnName(int column)
    {
        return colNames.get(column).toString();
    }

    public synchronized void setValueAt(double value, int row, int col)
    {
        setValueAt(new Double(value), row, col);
    }


    public double[] getColumnData(int col)
    {
        double[] ret = new double[rowDim];

        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = data[i][col];
        }

        return ret;
    }


    public void addRow(double[] rowData)
    {
        rowDim++;

        data[rowDim - 1] = rowData;
        colors.add(Color.black);

        this.fireTableRowsInserted(rowDim - 1, rowDim - 1);
    }

    public int getColumnRole(int col)
    {
        if (col > 0)
        {
            Integer intObj = (Integer) colRoles.get(col - 1);
            return intObj.intValue();
        }
        else
            return DataSet.UNDEFINED_ROLE;
    }

    public void setColumnRole(int role, int col)
    {
        Integer intObj = new Integer(role);
        colRoles.set(col-1, intObj);
    }

    public void addRows(int numOfRows)
    {
        for (int i = 0; i < numOfRows; i++)
        {
            for (int j = 0; j < colDim; j++)
            {
                data[rowDim + i][j] = 0;
            }
            colors.add(Color.black);
        }
        int oldRowCount = rowDim;
        rowDim += numOfRows;

        this.fireTableRowsInserted(oldRowCount, rowDim - 1);
    }

    public int getRowCount()
    {
        return rowDim;
    }

    public int getColumnCount()
    {
        return colDim + 1;
    }

    public Object getValueAt(int row, int col)
    {
        if (col == 0)
            return colors.get(row);
        else
            return new Double(data[row][col-1]);
    }

    public void setValueAt(Object value, int row, int col)
    {
        if (col == 0)
        {
            colors.set(row, value);
        }
        else
        {
            data[row][col-1] = ((Double) value).doubleValue();
        }

        fireTableCellUpdated(row, col);
    }

    public void addColumn(String colName, double[] colData)
    {
        colDim++;
        rowDim = colData.length;
        colNames.add(colName);
        colRoles.add(new Integer(UNDEFINED_ROLE));

        boolean initializingColor=colors.size()==0;

        for (int i = 0; i < rowDim; i++)
        {

            if (colData != null)
                data[i][colDim - 1] = colData[i];
            else
                data[i][colDim - 1] = 0;

            if(initializingColor)
                colors.add(Color.black);
        }

        this.fireTableStructureChanged();
    }

    public int getColumnIndex(String colName)
    {
        return colNames.indexOf(colName);
    }

    public double[] getColumnData(String colName)
    {
        return getColumnData(getColumnIndex(colName));
    }

    public void setColumnName(String name, int col)
    {
        colNames.set(col, name);
        this.fireTableCellUpdated(-1, col);
    }

    public void deleteRows(int startRow, int endRow)
    {
        int delta = endRow - startRow + 1;

        for (int i = startRow; i < rowDim - delta; i++)
        {
            data[i] = data[i + delta];

        }

        for(int i=endRow;i>=startRow;i--)
        {
             colors.remove(i);
        }
        rowDim -= delta;
        this.fireTableRowsDeleted(startRow, endRow);
    }

    public void deleteColumns(int startCol, int endCol)
    {
        int delta = endCol - startCol + 1;
        for (int i = startCol; i < colDim - delta; i++)
        {
            for (int j = 0; j < rowDim; j++)
            {
                data[j][i] = data[j][i + delta];
            }
        }
        colDim -= delta;
        this.fireTableStructureChanged();
    }

    public File getDataFile()
    {
        return dataFile;
    }

    public void setDataFile(File dataFile)
    {
        this.dataFile = dataFile;
    }

    public double[] getFrequencies()
    {
        int index=getFirstColumnForRole(FREQ_ROLE);
        if (index == -1)
            return null;
        return getColumnData(index-1);

    }

    public static void main(String[] args)
    {
        DataSet dataSet=new DataSet();

        JTable table=new JTable(dataSet);
        JFrame jf=new JFrame();
        jf.getContentPane().add(new JScrollPane(table));
        jf.pack();
        jf.show();
    }
}
