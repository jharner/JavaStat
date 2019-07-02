package wvustat.network;

import java.io.*;

public class Filterfun implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
     * constants for name of filter function
     */
	public final static transient String POVERA = "pOverA(p1,p2)";
	public final static transient String IQR = "IQR>p1";
	public final static transient String CV = "cv(p1,p2)";
	
	
	protected String name;
	protected Double arg1, arg2;
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
	}
	
	public Filterfun(String name, Double arg1, Double arg2) throws IllegalArgumentException{
		if (arg1 == null)
			throw new IllegalArgumentException("p1 of filter function '" + name + "' cannot be empty");
		
		if (!name.equals(IQR) && arg2 == null)
			throw new IllegalArgumentException("p2 of filter function '" + name + "' cannot be empty");
		
		this.name = name;
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public String getName(){
		return name;
	}
		
	public Double getArg1(){
		return arg1;
	}
	
	public Double getArg2(){
		return arg2;
	}
	
}
