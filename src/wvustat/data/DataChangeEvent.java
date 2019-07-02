package wvustat.data;

import java.util.*;

public class DataChangeEvent extends EventObject{
	private static final long serialVersionUID = 1L;
	
	public final static int INVALID = -1;
	public final static int SET_STATE = 0;
	public final static int UPDATE_VALUE = 1;
	public final static int DELETE_ROWS = 2;
	public final static int DELETE_COLUMN = 3;
	public final static int SET_MASKS = 4;
	public final static int SET_STATES = 5;
	
	private int rows[], column, type;
	private Object value;
	private boolean states[];
	
	/**
	 * INVALID
	 */
	public DataChangeEvent(Object source){
		super(source);
		this.type = INVALID;
	}
	
	/**
	 * UPDATE_VALUE
	 */
	public DataChangeEvent(Object source, int row, int column, Object value){
		super(source);
		this.rows = new int[1];
		this.rows[0] = row;
		this.column = column;
		this.value = value;
		this.type = UPDATE_VALUE;
	}
	
	/**
	 * SET_STATE
	 */
	public DataChangeEvent(Object source, int row, boolean state) {
		super(source);
		this.rows = new int[1];
		this.rows[0] = row;
		this.value = new Boolean(state);
		this.type = SET_STATE;
	}
	
	/**
	 * SET_STATES
	 */
	public DataChangeEvent(Object source, int[] rows, boolean[] states) {
		super(source);
		this.rows = rows;
		this.states = states;
		this.type = SET_STATES;
	}
	
	/**
	 * SET_MASKS 
	 */
	public DataChangeEvent(Object source, int[] rows, boolean state) {
		super(source);
		this.rows = rows;
		this.value = new Boolean(state);
		this.type = SET_MASKS;
	}
	
	/**
	 * DELETE_COLUMN 
	 */
	public DataChangeEvent(Object source, int index){
		super(source);
		this.column = index;
		this.type = DELETE_COLUMN;
	}
	
	/**
	 * DELETE_ROWS
	 */
	public DataChangeEvent(Object source, int[] index){
		super(source);
		this.rows = index;
		this.type = DELETE_ROWS;
	}
	
	public int getType(){
		return type;
	}
	
	public int getRow(){
		return rows[0];
	}
	
	public int[] getRows(){
		return rows;
	}
	
	public int getColumn(){
		return column;
	}
	
	public Object getValue(){
		return value;
	}
	
	public boolean getState(){
		return ((Boolean)value).booleanValue();
	}
	
	public boolean[] getStates(){
		return states;
	}

}
