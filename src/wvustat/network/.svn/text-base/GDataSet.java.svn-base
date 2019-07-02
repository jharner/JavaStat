package wvustat.network;

import java.io.*;
import java.util.*;

public class GDataSet implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
     * constants for property 'role' of variable, @see wvustat.interfaces.DataSet
     */
	public final static transient int X_ROLE=0;
    public final static transient int Y_ROLE=1;
    public final static transient int Z_ROLE=2;
    public final static transient int L_ROLE=3;
    public final static transient int W_ROLE=4;
    public final static transient int U_ROLE=5;
    public final static transient int F_ROLE=6;
    
    
    /**
     * variables
     */	
	protected ArrayList columns;
	protected String name;
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException
	{
		stream.defaultWriteObject();
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
	}
	
	public GDataSet(String name, ArrayList columns) {
		this.name = name;
		this.columns = columns;
	}
	
	public ArrayList getColumns(){
		return columns;
	}
	
	public void setColumns(ArrayList columns){
		this.columns = columns;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
}
