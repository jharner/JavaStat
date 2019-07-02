package wvustat.network;

import java.io.*;
import javax.swing.event.*;
import wvustat.util.MathUtils;
import java.util.Arrays;

public class MTP extends Biobase implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected transient double[] adjp;
	protected transient double[] rawp;
	protected transient double[] log2fc;
	protected transient String[] genes;
	protected transient double[] statistic;
	protected transient String[] sampNames;
	protected transient String[] sampTypes;
	
	protected String objName;
	protected String exprObjName;
	protected double alpha;
	protected int numHypo;
	protected int numSample;
	protected String typeone;
	protected String method;
	
	//local variables
	protected transient String[] rejectedGenes;
	protected transient double[] rejectedAdjp;
	protected transient double[] rejectedRawp;
	protected transient double[] rejectedLog2fc;
	protected transient int numRejection;
	protected transient EventListenerList eList=new EventListenerList();
	protected transient ChangeEvent event;
	
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		
		stream.writeInt(adjp.length);
		for (int i=0; i<adjp.length; i++)
			stream.writeDouble(adjp[i]);
		
		stream.writeInt(rawp.length);
		for (int i=0; i<rawp.length; i++)
			stream.writeDouble(rawp[i]);
		
		stream.writeInt(log2fc.length);
		for (int i=0; i<log2fc.length; i++)
			stream.writeDouble(log2fc[i]);
		
		stream.writeInt(genes.length);
		for (int i=0; i<genes.length; i++)
			stream.writeObject(genes[i]);
		
		stream.writeInt(statistic.length);
		for (int i=0; i<statistic.length; i++)
			stream.writeDouble(statistic[i]);
		
		stream.writeInt(sampNames.length);
		for (int i=0; i<sampNames.length; i++)
			stream.writeObject(sampNames[i]);
		
		stream.writeInt(sampTypes.length);
		for (int i=0; i<sampTypes.length; i++)
			stream.writeObject(sampTypes[i]);
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		int size = in.readInt();
		adjp = new double[size];
		for (int i=0; i<adjp.length; i++)
			adjp[i] = in.readDouble();
		
		size = in.readInt();
		rawp = new double[size];
		for (int i=0; i<rawp.length; i++)
			rawp[i] = in.readDouble();
		
		size = in.readInt();
		log2fc = new double[size];
		for (int i=0; i<log2fc.length; i++)
			log2fc[i] = in.readDouble();
		
		size = in.readInt();
		genes = new String[size];
		for (int i=0; i<genes.length; i++)
			genes[i] = (String)in.readObject();		
		
		size = in.readInt();
		statistic = new double[size];
		for (int i=0; i<statistic.length; i++)
			statistic[i] = in.readDouble();
		
		size = in.readInt();
		sampNames = new String[size];
		for (int i=0; i<sampNames.length; i++)
			sampNames[i] = (String)in.readObject();		
		
		size = in.readInt();
		sampTypes = new String[size];
		for (int i=0; i<sampTypes.length; i++)
			sampTypes[i] = (String)in.readObject();		
		
		if (eList == null)
			eList=new EventListenerList();
		
		rejectedGenes = new String[genes.length];
		System.arraycopy(genes, 0, rejectedGenes, 0, genes.length);
		
		rejectedAdjp = new double[adjp.length];
		System.arraycopy(adjp, 0, rejectedAdjp, 0, adjp.length);
		
		rejectedRawp = new double[rawp.length];
		System.arraycopy(rawp, 0, rejectedRawp, 0, rawp.length);
		
		rejectedLog2fc = new double[log2fc.length];
		System.arraycopy(log2fc, 0, rejectedLog2fc, 0, log2fc.length);
		
		MathUtils.InsertionSort(rejectedGenes, rejectedAdjp); //rejectedAdjp unchanged
		MathUtils.InsertionSort(rejectedRawp, rejectedAdjp); //rejectedAdjp unchanged
		MathUtils.InsertionSort(rejectedLog2fc, rejectedAdjp); //rejectedAdjp unchanged
		MathUtils.InsertionSort(rejectedAdjp);
		setNull(alpha);		
	}
	
	public MTP(){
		adjp = new double[0];
		rawp = new double[0];
		log2fc = new double[0];
		genes = new String[0];
		statistic = new double[0];
		rejectedGenes = new String[0];
		rejectedAdjp = new double[0];
		rejectedRawp = new double[0];
		rejectedLog2fc = new double[0];
		objName = "";
		exprObjName = "";
		typeone = "";
		method = "";
	}
	
	private void fireStateChanged(){
		Object[] list=eList.getListenerList();
		for(int i=list.length-2;i>=0;i-=2){
			if(list[i]==ChangeListener.class){
				if(event==null)
					event=new ChangeEvent(this);
				((ChangeListener)list[i+1]).stateChanged(event);
			}
		}
	}
	
	public void addChangeListener(ChangeListener l){
		eList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l){
		eList.remove(ChangeListener.class, l);
	}
	
	public void setNull(double val){
		alpha=val;
		int index = Arrays.binarySearch(rejectedAdjp, alpha);
		numRejection = (index >= 0) ?  (index + 1) : -(index + 1);
		
		fireStateChanged();
	}
	
	public double[] getAdjp(){
		return adjp;
	}
	
	public void setAdjp(double[] adjp){
		this.adjp = adjp;
	}
	
	public double[] getRawp(){
		return rawp;
	}
	
	public void setRawp(double[] rawp){
		this.rawp = rawp;
	}
	
	public double[] getLog2fc(){
		return log2fc;
	}
	
	public void setLog2fc(double[] log2fc){
		this.log2fc = log2fc;
	}
	
	public String[] getGenes(){
		return genes;
	}
	
	public void setGenes(String[] genes){
		this.genes = genes;
	}
	
	public String[] getSampNames(){
		return sampNames;
	}
	
	public void setSampNames(String[] sampNames){
		this.sampNames = sampNames;
	}
	
	public String[] getSampTypes(){
		return sampTypes;
	}
	
	public void setSampTypes(String[] sampTypes){
		this.sampTypes = sampTypes;
	}
	
	public double[] getStatistic(){
		return statistic;
	}
	
	public void setStatistic(double[] statistic){
		this.statistic = statistic;
	}
	
	public double[] getRejectedAdjp(){
		return rejectedAdjp;
	}
	
	public double[] getRejectedRawp(){
		return rejectedRawp;
	}
	
	public double[] getRejectedLog2fc(){
		return rejectedLog2fc;
	}
	
	public String[] getRejectedGenes(){
		return rejectedGenes;
	}
	
	public int getNumRejection(){
		return numRejection;
	}
	
	public String getObjName(){
		return objName;
	}
	
	public void setObjName(String objName){
		this.objName = objName;
	}
	
	public String getExprObjName(){
		return exprObjName;
	}
	
	public void setExprObjName(String objName){
		this.exprObjName = objName;
	}
	
	public double getAlpha(){
		return alpha;
	}
	
	public void setAlpha(double alpha){
		this.alpha = alpha;
	}
	
	public int getNumHypo(){
		return numHypo;
	}
	
	public void setNumHypo(int numHypo){
		this.numHypo = numHypo;
	}
	
	public int getNumSample(){
		return numSample;
	}
	
	public void setNumSample(int numSample){
		this.numSample = numSample;		
	}
	
	public String getTypeone(){
		return typeone;
	}
	
	public void setTypeone(String typeone){
		this.typeone = typeone;		
	}
	
	public String getMethod(){
		return method;
	}
	
	public void setMethod(String method){
		this.method = method;		
	}

}
