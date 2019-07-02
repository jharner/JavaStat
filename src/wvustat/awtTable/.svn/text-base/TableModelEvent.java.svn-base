/*
 * TableModelEvent.java
 *
 * Created on March 7, 2002, 10:12 AM
 */

package wvustat.awtTable;

/**
 *
 * @author  hxue
 * @version 
 */
public class TableModelEvent extends java.util.EventObject {
    public static final int STRUCTURAL_CHANGE=0;
    public static final int CELL_CHANGE=1;
    
    private int eventId;
    private int row, col; //The index of the changed cell

    /** Creates new TableModelEvent */
    public TableModelEvent(Object src, int eventId) {
        super(src);
        this.eventId=eventId;
    }
    
    public TableModelEvent(Object src, int eventId, int row, int col){
        super(src);
        this.eventId=eventId;
        this.row=row;
        this.col=col;
    }
    
    public int getEventId(){
        return eventId;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getColumn(){
        return col;
    }

}