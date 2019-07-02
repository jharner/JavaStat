package wvustat.network;

import java.io.*;

/**
 * @author dajieluo
 *
 */
public class RLinearModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected transient double[] residuals;
	protected transient double[] fittedValues;
	protected transient double[][] coefficients;
	protected transient String[] coefRowNames;
	protected transient String[] coefColNames;
	
	protected transient double[][] anova;
	protected transient String[] anovaRowNames;
	protected transient String[] anovaColNames;
	
	protected transient double[][] leveragex;
	protected transient int[] assignNum;
	protected transient double[][] modelMatrix;
	
	protected transient double[] hat;
	protected transient double[] studRes;
	protected transient double[] jackRes;
	
	protected String formula;
	protected double rsquared;
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		stream.writeInt(residuals.length);
		for (int i=0; i<residuals.length; i++)
			stream.writeDouble(residuals[i]);
		
		for (int i=0; i<fittedValues.length; i++)
			stream.writeDouble(fittedValues[i]);
		
		//Coefficients
		stream.writeInt(coefRowNames.length);
		stream.writeInt(coefColNames.length);
		for (int i=0; i<coefficients.length; i++)
			for (int j=0; j<coefficients[i].length; j++)
				stream.writeDouble(coefficients[i][j]);
		
		for (int i=0; i<coefRowNames.length; i++)
			stream.writeObject(coefRowNames[i]);
		
		for (int i=0; i<coefColNames.length; i++)
			stream.writeObject(coefColNames[i]);
		
		//ANOVA table
		stream.writeInt(anovaRowNames.length);
		stream.writeInt(anovaColNames.length);
		for (int i=0; i<anova.length; i++)
			for (int j=0; j<anova[i].length; j++)
				stream.writeDouble(anova[i][j]);
		
		for (int i=0; i<anovaRowNames.length; i++)
			stream.writeObject(anovaRowNames[i]);
		
		for (int i=0; i<anovaColNames.length; i++)
			stream.writeObject(anovaColNames[i]);
		
		//leveragex
		stream.writeInt(leveragex.length);
		
		for (int i=0; i<leveragex.length; i++)
			for (int j=0; j<residuals.length; j++)
				stream.writeDouble(leveragex[i][j]);
		
		//assignNum
		stream.writeInt(assignNum.length);
		
		for (int i=0; i<assignNum.length; i++)
			stream.writeInt(assignNum[i]);
		
		//modelMatrix
		stream.writeInt(modelMatrix.length);
		
		for (int i=0; i<modelMatrix.length; i++)
			for (int j=0; j<residuals.length; j++)
				stream.writeDouble(modelMatrix[i][j]);
		
		//hat,studRes,jackRes
		stream.writeInt(hat.length);
		for (int i=0; i<hat.length; i++)
			stream.writeDouble(hat[i]);
		stream.writeInt(studRes.length);
		for (int i=0; i<studRes.length; i++)
			stream.writeDouble(studRes[i]);
		stream.writeInt(jackRes.length);
		for (int i=0; i<jackRes.length; i++)
			stream.writeDouble(jackRes[i]);
		
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		int size;
		
		in.defaultReadObject();
		
		size = in.readInt();
		residuals = new double[size];
		for (int i=0; i<residuals.length; i++)
			residuals[i] = in.readDouble();
		
		fittedValues = new double[size];
		for (int i=0; i<fittedValues.length; i++)
			fittedValues[i] = in.readDouble();
		
		//Coefficients
		int nrow = in.readInt();
		int ncol = in.readInt();
		coefficients = new double[nrow][ncol];
		for (int i=0; i<nrow; i++)
			for (int j=0; j<ncol; j++)
				coefficients[i][j] = in.readDouble();
		
		coefRowNames = new String[nrow];
		for (int i=0; i<nrow; i++)
			coefRowNames[i] = (String)in.readObject();
		
		coefColNames = new String[ncol];
		for (int i=0; i<ncol; i++)
			coefColNames[i] = (String)in.readObject();
		
		//ANOVA table
		nrow = in.readInt();
		ncol = in.readInt();
		anova = new double[nrow][ncol];
		for (int i=0; i<nrow; i++)
			for (int j=0; j<ncol; j++)
				anova[i][j] = in.readDouble();
		
		anovaRowNames = new String[nrow];
		for (int i=0; i<nrow; i++)
			anovaRowNames[i] = (String)in.readObject();
		
		anovaColNames = new String[ncol];
		for (int i=0; i<ncol; i++)
			anovaColNames[i] = (String)in.readObject();
		
		//leveragex
		int termNum = in.readInt();
		leveragex = new double[termNum][size];
		for (int i=0; i<termNum; i++)
			for (int j=0; j<size; j++)
				leveragex[i][j] = in.readDouble();
		
		//assignNum
		termNum = in.readInt();
		assignNum = new int[termNum];
		for( int i=0; i<termNum; i++)
			assignNum[i] = in.readInt();
		
		//modelMatrix
		termNum = in.readInt();
		modelMatrix = new double[termNum][size];
		for (int i=0; i<termNum; i++)
			for (int j=0; j<size; j++)
				modelMatrix[i][j] = in.readDouble();
		
		//hat,studRes,jackRes
		size = in.readInt();
		hat = new double[size];
		for (int i=0; i<hat.length; i++)
			hat[i] = in.readDouble();
		size = in.readInt();
		studRes = new double[size];
		for (int i=0; i<studRes.length; i++)
			studRes[i] = in.readDouble();
		size = in.readInt();
		jackRes = new double[size];
		for (int i=0; i<jackRes.length; i++)
			jackRes[i] = in.readDouble();
		
	}
	
	
	
	public RLinearModel(){
		
		this.clear();
	}
	
	public void clear(){
		residuals = new double[0];
		fittedValues = new double[0];
		hat = new double[0];
		studRes = new double[0];
		jackRes = new double[0];
		leveragex = new double[1][0];
		leveragex[0] = new double[0];
		formula = "~";
		anovaRowNames = new String[1];
		anovaRowNames[0] = "";
		
		for (int i=0; i<residuals.length; i++){
			residuals[i] = Double.NaN;
			fittedValues[i] = Double.NaN;
			hat[i] = Double.NaN;
			studRes[i] = Double.NaN;
			jackRes[i] = Double.NaN;
		}
		coefficients = new double[0][0];
		
		assignNum = new int[0];
		
		modelMatrix = new double[0][0];
		
		rsquared = Double.NaN;
		
		anova = new double[0][0];
		
		for (int i=0; i<leveragex.length; i++)
			for (int j=0; j<leveragex[0].length; j++)
				leveragex[i][j] = Double.NaN;
		
	}
	
	public double[] getResiduals(){
		return residuals;
	}
	
	public void setResiduals(double[] residuals){
		this.residuals = residuals;
	}
	
	public double[] getFittedValues(){
		return fittedValues;
	}
	
	public void setFittedValues(double[] fittedValues){
		this.fittedValues = fittedValues;
	}
	
	public double[][] getCoefficients(){
		return coefficients;
	}
	
	public void setCoefficients(double[][] coefficients){
		this.coefficients = coefficients;
	}
	
	public String[] getCoefRowNames(){
		return coefRowNames;
	}
	
	public void setCoefRowNames(String[] coefRowNames){
		this.coefRowNames = coefRowNames;
	}
	
	public String[] getCoefColNames(){
		return coefColNames;
	}
	
	public void setCoefColNames(String[] coefColNames){
		this.coefColNames = coefColNames;
	}
	
	public double[][] getAnova(){
		return anova;
	}
	
	public void setAnova(double[][] anova){
		this.anova = anova;
	}
	
	public String[] getAnovaRowNames(){
		return anovaRowNames;
	}
	
	public void setAnovaRowNames(String[] anovaRowNames){
		this.anovaRowNames = anovaRowNames;
	}
	
	public String[] getAnovaColNames(){
		return anovaColNames;
	}
	
	public void setAnovaColNames(String[] anovaColNames){
		this.anovaColNames = anovaColNames;
	}
	
	public String getFormula(){
		return formula;
	}
	
	public void setFormula(String formula){
		this.formula = formula;
	}
	
	public String getResponseName(){
		return formula.substring(0, getFormula().indexOf('~'));
	}
	
	public double getRsquared(){
		return rsquared;
	}
	
	public void setRsquared(double rsquared){
		this.rsquared = rsquared;
	}
	
	public double[][] getLeveragex(){
		return leveragex;
	}
	
	public double[] getLeveragex(int index){
		return leveragex[index];
	}
	
	public double[] getLeveragey(int index){
		double[] leveragey = new double[residuals.length];
		for(int i=0; i<leveragey.length; i++){
			leveragey[i] = residuals[i] + leveragex[index][i];
		}
		return leveragey;
	}
	
	public void setLeveragex(double[][] leveragex){
		this.leveragex = leveragex;
	}
	
	public int[] getAssignNum(){
		return assignNum;
	}
	
	public void setAssignNum(int[] assignNum){
		this.assignNum = assignNum;
	}
	
	public double[][] getModelMatrix(){
		return modelMatrix;
	}
	
	public void setModelMatrix(double[][] matrix){
		this.modelMatrix = matrix;
	}
	
	public double[] getHat(){
		return hat;
	}
	
	public void setHat(double[] hat){
		this.hat = hat;
	}
	
	public double[] getStudRes(){
		return studRes;
	}
	
	public void setStudRes(double[] studRes){
		this.studRes = studRes;
	}
	
	public double[] getJackRes(){
		return jackRes;
	}
	
	public void setJackRes(double[] jackRes){
		this.jackRes = jackRes;
	}
}
