/*
 * IntColumnModel.java
 *
 * Created on October 4, 2000, 11:46 AM
 */

package wvustat.table;

import javax.swing.table.AbstractTableModel;
/**
 * IntColumnModel implements a simple javax.swing.table.AbstractTableModel. It has only one column of
 * data, which is integer from 1..rowCount. It is used as a model for table row header.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 4, 2000
 */
public class IntColumnModel extends AbstractTableModel {

    protected int rowCount;

    public IntColumnModel(int rowCnt){
        rowCount=rowCnt;
    }

    public int getColumnCount(){
        return 1;
    }
    
    public int getRowCount(){
        return rowCount;
    }

    public Object getValueAt(int row, int col){
        return new Integer(row+1);
    }

    public String getColumnName(int col){
        return "Obs";
    }

    public Class getColumnClass(int col){
        return Integer.class;
    }
 
    public boolean isCellEditable(int row, int col){
        return false;
    }
    
    public void addRows(int n){
        int oldRowCount=rowCount;
        rowCount+=n;
        fireTableRowsInserted(oldRowCount,rowCount);
    }
    
    public void deleteRow(int index){
        rowCount--;
    }
    
    public void deleteRows(int cnt){
    	rowCount=rowCount-cnt;
    }
    
    public static void main(String[] args){
        javax.swing.JTable jt=new javax.swing.JTable(new IntColumnModel(10));
        
        javax.swing.JFrame jf=new javax.swing.JFrame("Test table");
        jf.getContentPane().add(new javax.swing.JScrollPane(jt));
        jf.pack();
        jf.show();
    }
}
