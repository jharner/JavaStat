package wvustat.network;

import java.io.*;

public class exprSet extends Biobase implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected transient double[][] intensityQtl;
	protected transient String[] sampleNames;
	
	protected String objName;
	protected int numGene;
	protected int numSample;
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		
		stream.writeInt(intensityQtl.length);
		for (int i=0; i<intensityQtl.length; i++)
			for (int j=0; j<5; j++)
				stream.writeDouble(intensityQtl[i][j]);
		
		stream.writeInt(sampleNames.length);
		for (int i=0; i<sampleNames.length; i++)
			stream.writeObject(sampleNames[i]);
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		int size = in.readInt();
		intensityQtl = new double[size][5];
		for (int i=0; i<intensityQtl.length; i++)
			for (int j=0; j<5; j++)
				intensityQtl[i][j] = in.readDouble();
		
		size = in.readInt();
		sampleNames = new String[size];
		for (int i=0; i<sampleNames.length; i++)
			sampleNames[i] = (String)in.readObject();
	}
	
	public exprSet(){
		intensityQtl = new double[0][];
		sampleNames = new String[0];
		objName = "";
		numGene = 0;
		numSample = 0;
	}
	
	public double[][] getIntensityQtl(){
		return intensityQtl;
	}
	
	public void setIntensityQtl(double[][] intensityQtl){
		this.intensityQtl = intensityQtl;
	}
	
	public String[] getSampleNames(){
		return sampleNames;
	}
	
	public void setSampleNames(String[] sampleNames){
		this.sampleNames = sampleNames;
	}
	
	public String getObjName(){
		return objName;
	}
	
	public void setObjName(String objName){
		this.objName = objName;
	}
	
	public int getNumGene(){
		return numGene;
	}
	
	public void setNumGene(int numGene){
		this.numGene = numGene;		
	}
	
	public int getNumSample(){
		return numSample;
	}
	
	public void setNumSample(int numSample){
		this.numSample = numSample;		
	}

}
