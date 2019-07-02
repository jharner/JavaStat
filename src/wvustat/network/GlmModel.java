package wvustat.network;

import java.io.*;

public class GlmModel extends RLinearModel{
	private static final long serialVersionUID = 1L;
		
	protected transient double[] devianceResiduals;
	protected transient double[] workingResiduals;
	protected transient double[] linearPredictors;
	
	protected String family;
	protected String link;
	
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		/* Before the serialization methods in this class execute, Java will first serialize superclass.
		 * So the writeObject(), readObject() in superclass RLinearModel are executed ahead of those 
		 * in this class. The variables in superclass are serialized by superclass. GlmModel only need to 
		 * handle its own variables.
		*/ 
		
		stream.defaultWriteObject();
		
		stream.writeInt(devianceResiduals.length);
		
		for (int i=0; i<devianceResiduals.length; i++)
			stream.writeDouble(devianceResiduals[i]);
		for (int i=0; i<workingResiduals.length; i++)
			stream.writeDouble(workingResiduals[i]);
		for (int i=0; i<linearPredictors.length; i++)
			stream.writeDouble(linearPredictors[i]);
		
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		int size;
		in.defaultReadObject();
		
		size = in.readInt();
		
		devianceResiduals = new double[size];
		for (int i=0; i<devianceResiduals.length; i++)
			devianceResiduals[i] = in.readDouble();
		
		workingResiduals = new double[size];
		for (int i=0; i<workingResiduals.length; i++)
			workingResiduals[i] = in.readDouble();
				
		linearPredictors = new double[size];
		for (int i=0; i<linearPredictors.length; i++)
			linearPredictors[i] = in.readDouble();		
	}
	
	
	
	public GlmModel(){
				
		clear();		
	}
	
	public void clear(){
		residuals = new double[0];
		fittedValues = new double[0];
		hat = new double[0];
		jackRes = new double[0];
		formula = "~";
		family = "";
		link = "";
		
		devianceResiduals = new double[0];
		workingResiduals = new double[0];
		linearPredictors = new double[0];
		
		
		for (int i=0; i<residuals.length; i++){
			residuals[i] = Double.NaN;
			devianceResiduals[i] = Double.NaN;
			workingResiduals[i] = Double.NaN;
			fittedValues[i] = Double.NaN;
			linearPredictors[i] = Double.NaN;
			hat[i] = Double.NaN;
			jackRes[i] = Double.NaN;
		}
		coefficients = new double[0][0];
				
		modelMatrix = new double[0][0];
		
		anova = new double[0][0];
	}
	
	
	public double[] getDevianceResiduals(){
		return devianceResiduals;
	}
	
	public void setDevianceResiduals(double[] devianceResiduals){
		this.devianceResiduals = devianceResiduals;
	}
	
	public double[] getWorkingResiduals(){
		return workingResiduals;
	}
	
	public void setWorkingResiduals(double[] workingResiduals){
		this.workingResiduals = workingResiduals;
	}
	
	public double[] getLinearPredictors(){
		return linearPredictors;
	}
	
	public void setLinearPredictors(double[] linearPredictors){
		this.linearPredictors = linearPredictors;
	}
	
	public String getFamily(){
		return family;
	}
	
	public void setFamily(String family){
		this.family = family;
	}
	
	public String getLink(){
		return link;
	}
	
	public void setLink(String link){
		this.link = link;
	}
}
