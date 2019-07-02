/*
 * TableCell.java
 *
 * Created on March 7, 2002, 10:36 AM
 */

package wvustat.awtTable;

import java.awt.Label;
import java.awt.Color;
import java.text.NumberFormat;
/**
 *
 * @author  hxue
 * @version 
 */
public class TableCell extends Label {
    int row, col;
    /** Creates new TableCell */
    public TableCell(double value, int row, int col) {
        super();
        this.row=row;
        this.col=col;
        
        if(!Double.isNaN(value)){
            NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            this.setText(nf.format(value));
        }
        
        setAlignment(RIGHT);
        setBackground(Color.white);        
    }
    
    public TableCell(String text, int row, int col) {
        super(text);
        this.row=row;
        this.col=col;
        
        setAlignment(CENTER);
        setBackground(Color.lightGray);
    }
    
    
    public void setValue(double value){
        if(!Double.isNaN(value)){
            NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            this.setText(nf.format(value));
        }        
    }
    
    public int getRow(){
        return row;
    }
    
    public int getColumn(){
        return col;
    }
}