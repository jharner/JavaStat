package wvustat.network;

import java.io.*;


public class HClustModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public transient int[][] varMerge;
	public transient int[] varOrder;
	public transient int[][] obsMerge;
	public transient int[] obsOrder;

	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		
		//var
		stream.writeInt(varMerge.length);
		stream.writeInt(varMerge[0].length);
		for (int i=0; i<varMerge.length; i++)
			for (int j=0; j<varMerge[i].length; j++)
				stream.writeInt(varMerge[i][j]);
		
		stream.writeInt(varOrder.length);
		for (int i=0; i<varOrder.length; i++)
			stream.writeInt(varOrder[i]);
		
		//obs
		stream.writeInt(obsMerge.length);
		stream.writeInt(obsMerge[0].length);
		for (int i=0; i<obsMerge.length; i++)
			for (int j=0; j<obsMerge[i].length; j++)
				stream.writeInt(obsMerge[i][j]);
		
		stream.writeInt(obsOrder.length);
		for (int i=0; i<obsOrder.length; i++)
			stream.writeInt(obsOrder[i]);
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		//var
		int nrow = in.readInt();
		int ncol = in.readInt();
		varMerge = new int[nrow][ncol];
		for (int i=0; i<nrow; i++)
			for (int j=0; j<ncol; j++)
				varMerge[i][j] = in.readInt();
		
		int size = in.readInt();
		varOrder = new int[size];
		for (int i=0; i<varOrder.length; i++)
			varOrder[i] = in.readInt();
		
		
		//obs
		nrow = in.readInt();
		ncol = in.readInt();
		obsMerge = new int[nrow][ncol];
		for (int i=0; i<nrow; i++)
			for (int j=0; j<ncol; j++)
				obsMerge[i][j] = in.readInt();
		
		size = in.readInt();
		obsOrder = new int[size];
		for (int i=0; i<obsOrder.length; i++)
			obsOrder[i] = in.readInt();
	}
}
