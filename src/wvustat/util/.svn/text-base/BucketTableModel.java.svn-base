/*
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Oct 29, 2002
 * Time: 8:44:58 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package wvustat.util;

import javax.swing.table.AbstractTableModel;

public class BucketTableModel extends AbstractTableModel
{
    public static final int FRACTION_IN_CELL=0;
    public static final int COUNT_IN_CELL=1;

    private Bucket[] buckets;
    private int cellOption=FRACTION_IN_CELL;
    private int sum;

    public BucketTableModel(Bucket[] buckets)
    {
        this.buckets=buckets;

        for(int i=0;i<buckets.length;i++)
        {
            sum+=buckets[i].getSize();
        }
    }

    public void setCellOption(int option)
    {
        cellOption=option;
        this.fireTableDataChanged();
    }

    public Class getColumnClass(int col)
    {
        if(col<3)
            return Double.class;
        else
        {
            if(cellOption==FRACTION_IN_CELL)
                return Double.class;
            else
                return Integer.class;
        }
    }

    public String getColumnName(int col)
    {
        if(col==0)
            return "Outcome";
        else if(col==1)
            return "Lower class limit";
        else if(col==2)
            return "Upper class limit";
        else
            return "Probability";
    }

    public int getRowCount()
    {
        return buckets.length;
    }

    public int getColumnCount()
    {
        return 4;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if(columnIndex==0)
        {
            /*
            NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            double endValue=buckets[rowIndex].getMinValue()+buckets[rowIndex].getWidth();
            String str=nf.format(buckets[rowIndex].getMidPoint());
            str+=" ("+nf.format(buckets[rowIndex].getMinValue());
            str+=" to "+nf.format(endValue)+")";
            */
            return new Double(buckets[rowIndex].getMidPoint());
        }
        else if(columnIndex==1)
        {
            return new Double(buckets[rowIndex].getMinValue());
        }
        else if(columnIndex==2)
        {
            return new Double(buckets[rowIndex].getMinValue()+buckets[rowIndex].getWidth());
        }

        switch(cellOption)
        {
            case FRACTION_IN_CELL:
                double val=buckets[rowIndex].getSize()*1.0/sum;
                return new Double(val);
            case COUNT_IN_CELL:
                return new Integer(buckets[rowIndex].getSize());
        }

        return null;
    }

}
