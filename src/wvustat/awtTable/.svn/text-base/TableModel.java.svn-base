/*
 * TableModel.java
 *
 * Created on March 7, 2002, 9:53 AM
 */

package wvustat.awtTable;


import java.util.Vector;
/**
 *
 * @author  hxue
 * @version 
 */
public class TableModel extends Object {
    protected double[][] data;
    protected int rowDim, colDim;
    protected Vector colNames;
    protected Vector modelListeners;
    protected int xRoleCol=0, yRoleCol=1;
    
    public TableModel(){
    }

    /** Creates new TableModel */
    public TableModel(double[][] data, Vector colNames){
        this.data=data;
        this.colNames=colNames;
        rowDim=data.length;
        colDim=data[0].length;
    }
    
    public TableModel(int rowDim, int colDim, Vector colNames){
        this.rowDim=rowDim;
        this.colDim=colDim;
        this.colNames=colNames;
        
        data=new double[rowDim][colDim];
        for(int i=0;i<rowDim;i++){
            for(int j=0;j<colDim;j++){
                data[i][j]=Double.NaN;
            }
        }
    }
    
    public TableModel(int rowDim, int colDim, String[] names){
        this.rowDim=rowDim;
        this.colDim=colDim;
        this.colNames=new Vector();
        
        data=new double[rowDim][colDim];
        for(int i=0;i<rowDim;i++){
            for(int j=0;j<colDim;j++){
                data[i][j]=Double.NaN;
            }
        }
        
        for(int i=0;i<colDim;i++)
            colNames.addElement(names[i]);
    }    
    
    public int getColumnCount(){
        return colDim;
    }
    
    public int getRowCount(){
        return rowDim;
    }
    
    public void addTableModelListener(TableModelListener listener){
        if(modelListeners==null)
            modelListeners=new Vector(3);
        
        modelListeners.addElement(listener);
    }
    
    public void removeTableModelListener(TableModelListener listener){
        modelListeners.removeElement(listener);
    }
    
    protected void fireTableModelEvent(TableModelEvent evt){
        if(modelListeners==null)
            return;
        
        for(int i=0;i<modelListeners.size();i++){
            TableModelListener tml=(TableModelListener)modelListeners.elementAt(i);
            tml.tableModelChanged(evt);
        }
    }
    
    public String getColumnName(int index){
        return colNames.elementAt(index).toString();
    }
    
    public void setColumnName(String name, int index){
        colNames.setElementAt(name, index);        
    }
    
    public double getValueAt(int row, int col){
        return data[row][col];
    }
    
    public void setValueAt(double value, int row, int col){
        data[row][col]=value;
        
        TableModelEvent evt=new TableModelEvent(this, TableModelEvent.CELL_CHANGE, row, col);
        fireTableModelEvent(evt);        
    }
    
    public void addColumn(String colName, double[] colData){
        colDim++;
        colNames.addElement(colName);
        
        double[][] newData=new double[rowDim][colDim];
        for(int i=0;i<rowDim;i++){
            for(int j=0;j<colDim-1;j++){
                newData[i][j]=data[i][j];
            }
            
            if(colData!=null)
                newData[i][colDim-1]=colData[i];
            else
                newData[i][colDim-1]=Double.NaN;
        }        
        data=newData;
        
        TableModelEvent evt=new TableModelEvent(this, TableModelEvent.STRUCTURAL_CHANGE);
        fireTableModelEvent(evt);        
    }    
    
    public double[] getColumnData(int col){
        double[] ret=new double[rowDim];
        
        for(int i=0;i<ret.length;i++){
            ret[i]=data[i][col];
        }
        
        return ret;
    }
    
    
    public void addRow(double[] rowData){
        rowDim++;
        
        double[][] newData=new double[rowDim][colDim];
        for(int i=0;i<rowDim-1;i++){
            for(int j=0;j<colDim;j++){
                newData[i][j]=data[i][j];
            }
        }
        
        newData[rowDim-1]=rowData;
        data=newData;
        
        TableModelEvent evt=new TableModelEvent(this, TableModelEvent.STRUCTURAL_CHANGE);
        fireTableModelEvent(evt);        
    }
    
    public void addRows(int numOfRows){
        double[][] newData=new double[rowDim+numOfRows][colDim];
        for(int i=0;i<rowDim;i++){
            for(int j=0;j<colDim;j++){
                newData[i][j]=data[i][j];
            }
        }
        
        for(int i=0;i<numOfRows;i++){
            for(int j=0;j<colDim;j++){
                newData[rowDim+i][j]=Double.NaN;
            }
        }
        rowDim+=numOfRows;
        data=newData;
        
        TableModelEvent evt=new TableModelEvent(this, TableModelEvent.STRUCTURAL_CHANGE);
        fireTableModelEvent(evt);        
    }
    
    public void setXRole(int index){
        xRoleCol=index;
    }
    
    public void setYRole(int index){
        yRoleCol=index;
    }
    
    public int getColumnIndex(String colName){
        return colNames.indexOf(colName);
    }
    
    public double[] getColumnData(String colName){
        return getColumnData(getColumnIndex(colName));
    }
}