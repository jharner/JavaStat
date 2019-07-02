/*
 * SummaryTableModel.java
 *
 * Created on January 30, 2002, 3:22 PM
 */

package wvustat.modules;

/**
 *
 * @author  James Harner
 * @version 
 */
public class SummaryTableModel extends javax.swing.table.AbstractTableModel {
    public static final int SHOW_SUM=0;
    public static final int SHOW_MEAN=1;
    public static final int SHOW_COUNT=2;
    
    private String[] statNames={"Sum", "Mean", "Count"};
    private double[][] data;
    private String[] rowNames, colNames;
    private int statOption=0;

    /** Creates new SummaryTableModel */
    public SummaryTableModel(double[][] data, String[] rowNames, String[] colNames) {
        this.data=data;
        this.rowNames=rowNames;
        this.colNames=colNames;
    }

    public java.lang.Class getColumnClass(int param) {
        if(param==0)
            return String.class;
        else
            return Double.class;
                
    }
    
    public int getColumnCount() {
        if(colNames==null)
            return 2;
        else
            return colNames.length+1;
    }
    
    public java.lang.String getColumnName(int param) {
        if(param==0)
           return null;
        else{
            if(colNames==null)
                return statNames[this.statOption];
            else
                return colNames[param-1];
        }
        
    }
    
    public int getRowCount() {
        return rowNames.length;
    }
    
    public java.lang.Object getValueAt(int row,int col) {
        if(col==0)
            return rowNames[row];
        else if(colNames==null)
            return new Double(data[row][row]);
        else
            return new Double(data[row][col-1]);
    }
    
    public boolean isCellEditable(int param,int param1) {
        return false;
    }
    
    public void setValueAt(java.lang.Object p1,int p2,int p3) {
    }
    
    public void removeTableModelListener(javax.swing.event.TableModelListener p1) {
    }
    
    public void addTableModelListener(javax.swing.event.TableModelListener p1) {
    }
    
}
