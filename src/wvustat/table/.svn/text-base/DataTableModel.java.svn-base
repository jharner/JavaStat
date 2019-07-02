/*
 * DataTableModel.java
 *
 * Created on October 2, 2000, 2:43 PM
 */

package wvustat.table;

import java.awt.Color;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.TableModelEvent;

import wvustat.data.*;
import wvustat.interfaces.*;
/**
 * DataTableModel implements the javax.swing.table.TableModel interface. It stores the data for a table which a user uses to
 * enter new data.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 2, 2000
 */
public class DataTableModel extends AbstractTableModel {
    protected Vector colNames, colClasses;
    protected Vector colData;
    protected int colCount, rowCount;
    protected String name="untitled";
    
    /** Creates new DataTableModel */
    public DataTableModel() {
        colCount=6;
        rowCount=8;
        
        init();
    }
    
    public DataTableModel(int rowCnt, int colCnt){
        rowCount=rowCnt;
        colCount=colCnt;
        
        init();
    }
    
    protected void init(){
        colNames=new Vector(10);
        colClasses=new Vector(10);
        colData=new Vector(10);
        
        colNames.addElement("State");
        colNames.addElement("Color");
        colNames.addElement("Mask");
        
        colClasses.addElement(Boolean.class);
        colClasses.addElement(Color.class);
        colClasses.addElement(Boolean.class);
        
        Vector v1=new Vector();
        Vector v2=new Vector();
        Vector v3=new Vector();
        
        for(int i=0;i<rowCount;i++){
            v1.addElement(Boolean.FALSE);
            v2.addElement(Color.black);
            v3.addElement(Boolean.FALSE);
        }
        
        colData.addElement(v1);
        colData.addElement(v2);
        colData.addElement(v3);
        
        for(int i=0;i<colCount-3;i++){
            colNames.addElement(String.valueOf((char)(65+i)));
            colClasses.addElement(String.class);
            
            Vector col=new Vector();
            for(int j=0;j<rowCount;j++){
                col.addElement("");
            }
            colData.addElement(col);
        }
    }
    
    public int getColumnCount(){
        return colCount;
    }
    
    public int getRowCount(){
        return rowCount;
    }
    
    public Object getValueAt(int row, int col){
        Vector v=(Vector)colData.elementAt(col);
        return v.elementAt(row);
    }
    
    public void setValueAt(Object aValue, int row, int col){
        
        Vector v=(Vector)colData.elementAt(col);
        v.setElementAt(aValue, row);
        fireTableCellUpdated(row,col);
    }
    
    public String getColumnName(int col){
        return colNames.elementAt(col).toString();
    }
    
    public Class getColumnClass(int col){
        return (Class)colClasses.elementAt(col);
    }
    
    public void setColumnName(String newName, int col){
        colNames.setElementAt(newName,col);
        fireTableStructureChanged();
    }
    
    public void setColumnClass(Class cl, int col){
        colClasses.setElementAt(cl,col);
        fireTableStructureChanged();
    }
    
    public boolean isCellEditable(int row, int col){
        return true;
    }
    
    public void addRows(int n){
        int oldRowCount=rowCount;
        rowCount+=n;
        for(int i=0;i<getColumnCount();i++){
            Vector v=(Vector)colData.elementAt(i);
            
            for(int j=0;j<n;j++){
                if(i==0)
                    v.addElement(Boolean.FALSE);
                else if(i==1)
                    v.addElement(Color.black);
                else if(i==2)
                    v.addElement(Boolean.FALSE);
                else
                    v.addElement("");
            }
        }
        
        fireTableRowsInserted(oldRowCount,rowCount);
    }
    
    public void deleteRow(int index){
        for(int i=0;i<colCount;i++){
            Vector v=(Vector)colData.elementAt(i);
            v.removeElementAt(index);
        }
        rowCount--;
        //fireTableRowsDeleted(first, last);
    }
    
    
    public void addColumns(int n){
        
        for(int i=0;i<n;i++){
            Vector v=new Vector(rowCount+4);
            for(int j=0;j<rowCount;j++){
                v.addElement("");
            }
            colData.addElement(v);
            colNames.addElement(String.valueOf((char)(65+colCount+i)));
            colClasses.addElement(String.class);
        }
        colCount+=n;
        fireTableChanged(new TableModelEvent(this,TableModelEvent.HEADER_ROW));
    }
    
    public void deleteColumn(int index){
        
        colData.removeElementAt(index);
        colNames.removeElementAt(index);
        colClasses.removeElementAt(index);
        
        colCount--;
        
        //fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }
    
    public DataSet convertToDataSet(){
        Vector vars=new Vector(10);
        BoolVariable stateVar=new BoolVariable((Vector)colData.elementAt(0));
        Vector colorVar=(Vector)colData.elementAt(1);
        BoolVariable maskVar=new BoolVariable((Vector)colData.elementAt(2));
        
        vars.addElement(stateVar);
        vars.addElement(colorVar);
        vars.addElement(maskVar);
        
        for(int i=3;i<colData.size();i++){
            Vector v=(Vector)colData.elementAt(i);
            Class c=this.getColumnClass(i);
            String n=this.getColumnName(i);
            Variable var=null;
            if(c==Double.class){
                var=new NumVariable(n,v);
            }
            else
                var=new CatVariable(n,v);
            vars.addElement(var);
        }
            
        return new DataSetImpl(name, vars);
        
    }
    
    
    public static void main(String[] args){
        javax.swing.JTable jt=new javax.swing.JTable(new DataTableModel(10,5));
        
        javax.swing.JFrame jf=new javax.swing.JFrame("Test table");
        
        JTable headerTable=new JTable(new IntColumnModel(10));
        headerTable.setBackground(Color.lightGray);
        JScrollPane jsp=new JScrollPane();
        JViewport jv=new JViewport();
        jv.setView(headerTable);
        jv.setPreferredSize(headerTable.getPreferredSize());
        
        jsp.setViewportView(jt);
        jsp.setRowHeader(jv);
        jf.getContentPane().add(jsp);
        jf.pack();
        jf.show();
    }
    
}
