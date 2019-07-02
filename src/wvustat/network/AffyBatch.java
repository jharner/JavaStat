package wvustat.network;

import java.io.*;

public class AffyBatch extends exprSet implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected transient double[][] pmQuantile;
	protected transient double[][] mmQuantile;

	protected int numProbe;
	

	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		
		stream.writeInt(pmQuantile.length);
		for (int i=0; i<pmQuantile.length; i++)
			for (int j=0; j<5; j++)
				stream.writeDouble(pmQuantile[i][j]);
		
		for (int i=0; i<mmQuantile.length; i++)
			for (int j=0; j<5; j++)
				stream.writeDouble(mmQuantile[i][j]);		
	
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		int size = in.readInt();
		pmQuantile = new double[size][5];
		for (int i=0; i<pmQuantile.length; i++)
			for (int j=0; j<5; j++)
				pmQuantile[i][j] = in.readDouble();
		
		mmQuantile = new double[size][5];
		for (int i=0; i<mmQuantile.length; i++)
			for (int j=0; j<5; j++)
				mmQuantile[i][j] = in.readDouble();		
	
	}
	
	public AffyBatch() {
		pmQuantile = new double[0][];
		mmQuantile = new double[0][];
		intensityQtl = new double[0][];
		sampleNames = new String[0];
		objName = "";
		numGene = 0;
		numProbe = 0;
		numSample = 0;
	}
	
	public double[][] getPMQuantile(){
		return pmQuantile;
	}
	
	public void setPMQuantile(double[][] pmQuantile){
		this.pmQuantile = pmQuantile;
	}
	
	public double[][] getMMQuantile(){
		return mmQuantile;
	}
	
	public void setMMQuantile(double[][] mmQuantile){
		this.mmQuantile = mmQuantile;
	}
	
	public int getNumProbe(){
		return numProbe;
	}
	
	public void setNumProbe(int numProbe){
		this.numProbe = numProbe;		
	}
		
}
