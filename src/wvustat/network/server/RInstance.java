package wvustat.network.server;

import java.rmi.RemoteException;
import java.util.*;
import java.io.*;
import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import wvustat.network.*;

/**
 * RInstance encapsulates Java/R Interface. All methods related to R engine should be put here.
 * @author dajieluo
 *
 */
public class RInstance {
	
	private boolean debug = false;
	private String STORAGE_PATH = "";
	
	private static Rengine re = null;
	
	public static final String SEPARATOR = "|";
	public static final String IPSEPARATOR = ".";
	
	public RInstance() throws RemoteException{
		
		if (re == null){
			String[] args = {"--no-save"};
			re=new Rengine(args, false, null);

			// the engine creates R is a new thread, so we should wait until it's ready
			if (!re.waitForR()) {
				System.out.println("Cannot load R");
				throw new RemoteException("Cannot load R");
			}
		}
		
	}
	
	
	public void setDebug(boolean b){
		debug = b;
	}
	
	public void setStoragePath(String path){
		this.STORAGE_PATH = path;
	}
	
	
	public void end(){
		re.end();
		System.out.println("shutdown! R status: " + (re.waitForR()?"alive":"off") );
	}
	
	
	
	public Object lm(String formula, ArrayList columns) throws RemoteException{
				
		if(debug) System.out.println("\nserver method called\n");
		
		re.eval("rm(list=ls(all=TRUE))");
		
		int size = ((List)columns.get(0)).size() - 2;
		int[] mask = new int[size];
		for(int j=0;j<size;j++){
			mask[j] = 1;
		}
		long xp = re.rniPutIntArray(mask);
		re.rniAssign(".M", xp, 0);
		re.eval(".M<-as.logical(.M)");
		
		for(int i=0;i<columns.size();i++){
			List column=(List)columns.get(i);
			String colName=(String)column.get(0);
			column.remove(0);
			Class colClass=(Class)column.get(0);
			column.remove(0);
			
			if(debug){
			    System.out.println("\ncolName:"+colName+",colClass:"+colClass);
			    for(int h=0;h<column.size();h++)
				    System.out.print(column.get(h)+" ");
			}
			
			if (colClass == Double.class){
				double[] d = new double[size];
				for(int j=0;j<column.size();j++){
					d[j]=((Double)column.get(j)).doubleValue();
				}
				xp = re.rniPutDoubleArray(d);
				re.rniAssign(colName, xp, 0);
				
			}else if(colClass == Boolean.class){
				mask = new int[size];
				for(int j=0;j<column.size();j++){
					boolean b=((Boolean)column.get(j)).booleanValue();
					mask[j] = b==true? 1: 0;
				}
				xp = re.rniPutIntArray(mask);
				re.rniAssign(colName, xp, 0);
				re.eval(colName+"<-"+"as.logical("+colName+")");
				
			}else if(colClass == String.class){
				String[] s = new String[size];
				for(int j=0;j<column.size();j++){
					s[j]=(String)column.get(j);
				}
				xp = re.rniPutStringArray(s);
				re.rniAssign(colName, xp, 0);
				re.eval(colName+"<-"+"factor("+colName+")");
				
			}
		}
		
		RLinearModel rlm = new RLinearModel();
		
		REXP mdl = re.eval("model<-lm("+formula+",subset=.M)");
		if(mdl == null){ 
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Cannot build model " + err);
		}
		
		REXP exp = re.eval("model$residuals");
		double[] residuals = exp.asDoubleArray();
		exp = re.eval("as.integer(names(model$residuals))");
		int[] index = exp.asIntArray();
		residuals = fillMissingValues(residuals, index, size);
		
		if (residuals != null) rlm.setResiduals(residuals);
		
		exp = re.eval("model$fitted.values");
		double[] fittedValues = exp.asDoubleArray();
		exp = re.eval("as.integer(names(model$fitted.values))");
		index = exp.asIntArray();
		fittedValues = fillMissingValues(fittedValues, index, size);
		
		if (fittedValues != null) rlm.setFittedValues(fittedValues);
		
		//Coefficients
		exp = re.eval("model.summary<-summary(model)");
		exp = re.eval("coef<-model.summary$coefficients");
		double[][] coefficients = exp.asDoubleMatrix();
		exp = re.eval("dimnames(coef)[[1]]");
		String[] coefRowNames = exp.asStringArray();
		exp = re.eval("dimnames(coef)[[2]]");
		String[] coefColNames = exp.asStringArray();
		
		if (coefficients != null){ 
			rlm.setCoefficients(coefficients);
			rlm.setCoefRowNames(coefRowNames);
			rlm.setCoefColNames(coefColNames);
		}
		
		//R Square
		exp = re.eval("model.summary$r.squared");
		double rsquared = exp.asDouble();
		rlm.setRsquared(rsquared);
		
		//Get ANOVA table
		exp = re.eval("model.anova<-anova(model)");
		exp = re.eval("as.matrix(model.anova)");
		double[][] anova = exp.asDoubleMatrix();
		exp = re.eval("row.names(model.anova)");
		String[] anovaRowNames = exp.asStringArray();
		exp = re.eval("names(model.anova)");
		String[] anovaColNames = exp.asStringArray();
		
		if (anova != null){ 
			rlm.setAnova(anova);
			rlm.setAnovaRowNames(anovaRowNames);
			rlm.setAnovaColNames(anovaColNames);
		}
		
		//Leverage
		exp = re.eval("model$assign");
		int[] assignNum = exp.asIntArray();
		rlm.setAssignNum(assignNum);		
		
		int termNum = assignNum[assignNum.length - 1];
		double[][] leveragex = new double[termNum][size];
		
		exp = re.eval("{ x = model.matrix(model);" +
				"xtxi = solve(t(x) %*% x);" +
				"b = model$coefficients;" +
				"b = matrix(b, ncol=1); }");
		if(exp == null) throw new RemoteException("model matrix singular");
		
		for (int i=1; i<=leveragex.length; i++)
		{
			int firstPos = firstIndexOf(i, assignNum);
			int lastPos = lastIndexOf(i, assignNum);
			int row = lastPos - firstPos + 1;
			int[][] L = new int[row][assignNum.length];
			
			for(int j=0; j < L.length; j++)
				for(int k=0; k < L[j].length; k++)
					L[j][k] = 0;
			
			for(int j=0, k=firstPos; j < L.length && k <= lastPos; j++, k++)
				L[j][k] = 1;
			
			if(debug){
				System.out.println();
				for(int j=0; j < L.length; j++){
					for(int k=0; k < L[j].length; k++)
						System.out.print(L[j][k] + " ");
					System.out.println();
				}
			}
			
			re.assign("L", arrayToVector(L));
			re.eval("L = matrix(L, nrow=" + row + ", byrow=TRUE)");
			exp = re.eval(
					"{ lamda = solve(L %*% xtxi %*% t(L)) %*% L %*% b;" +
					"Vx = x %*% xtxi %*% t(L) %*% lamda; }");
			
			if(exp == null) throw new RemoteException("Cannot get leverage");
			leveragex[i-1] = exp.asDoubleArray(); //get value of Vx
			leveragex[i-1] = fillMissingValues(leveragex[i-1], index, size);
		}
		
		if(debug)
			for(int i=0; i < leveragex.length; i++){
				System.out.print("\nleveragex["+i+"]=");
				for(int j=0; j < leveragex[i].length; j++)
					System.out.print(leveragex[i][j] + " ");
			}
		
		rlm.setLeveragex(leveragex);
		
		//modelMatrix
		exp = re.eval("t(x)");
		double[][] modelMatrix = exp.asDoubleMatrix();
		for (int i = 0; i < modelMatrix.length; i++)
			modelMatrix[i] = fillMissingValues(modelMatrix[i], index, size);
		rlm.setModelMatrix(modelMatrix);
		
		//hat, studRes, jackRes
		exp = re.eval("lev = hat(x)");
		if(exp == null) throw new RemoteException("Cannot get leverages");
		double[] hat = exp.asDoubleArray();
		hat = fillMissingValues(hat, index, size);
		rlm.setHat(hat);
		
		exp = re.eval("model$residuals/(model.summary$sigma*sqrt(1-lev))");
		if(exp == null) throw new RemoteException("Cannot get studentized residuals");
		double[] studRes = exp.asDoubleArray();
		//replaceNaN(studRes);
		studRes = fillMissingValues(studRes, index, size);
		rlm.setStudRes(studRes);
		
		exp = re.eval("rstudent(model)");
		if(exp == null) throw new RemoteException("Cannot get jacknife residuals");
		double[] jackRes = exp.asDoubleArray();
		
		if(debug){
			System.out.println("\n jackRes " + jackRes.length);
			for(int i=0; i<jackRes.length; i++) System.out.print(jackRes[i] + " ");
			System.out.println("\n index " + index.length);
			for(int i=0; i<index.length; i++) System.out.print(index[i] + " ");
		}
		//replaceNaN(jackRes);
		jackRes = fillMissingValues(jackRes, index, size);
		rlm.setJackRes(jackRes);
		
		if(debug){
			System.out.println("\n hat " + hat.length);
			for(int i=0; i<size; i++) System.out.print(hat[i] + " ");
			System.out.println("\n studRes " + studRes.length);
			for(int i=0; i<size; i++) System.out.print(studRes[i] + " ");
			System.out.println("\n jackRes " + jackRes.length);
			for(int i=0; i<size; i++) System.out.print(jackRes[i] + " ");
		}
		
		rlm.setFormula(formula);		
				
		if(debug) System.out.println("\nserver method finished\n");
		
		return rlm;
	}
	
	public Object glm(String formula, ArrayList columns, String family, String link) throws RemoteException{
				
		if(debug) System.out.println("\nserver method glm called\n");
		
		re.eval("rm(list=ls(all=TRUE))");
		
		int size = ((List)columns.get(0)).size() - 2;
		int[] mask = new int[size];
		for(int j=0;j<size;j++){
			mask[j] = 1;
		}
		long xp = re.rniPutIntArray(mask);
		re.rniAssign(".M", xp, 0);
		re.eval(".M<-as.logical(.M)");
		
		for(int i=0;i<columns.size();i++){
			List column=(List)columns.get(i);
			String colName=(String)column.get(0);
			column.remove(0);
			Class colClass=(Class)column.get(0);
			column.remove(0);
			
			if(debug){
			    System.out.println("\ncolName:"+colName+",colClass:"+colClass);
			    for(int h=0;h<column.size();h++)
				    System.out.print(column.get(h)+" ");
			}
			
			if (colClass == Double.class){
				double[] d = new double[size];
				for(int j=0;j<column.size();j++){
					d[j]=((Double)column.get(j)).doubleValue();
				}
				xp = re.rniPutDoubleArray(d);
				re.rniAssign(colName, xp, 0);
				
			}else if(colClass == Boolean.class){
				mask = new int[size];
				for(int j=0;j<column.size();j++){
					boolean b=((Boolean)column.get(j)).booleanValue();
					mask[j] = b==true? 1: 0;
				}
				xp = re.rniPutIntArray(mask);
				re.rniAssign(colName, xp, 0);
				re.eval(colName+"<-"+"as.logical("+colName+")");
				
			}else if(colClass == String.class){
				String[] s = new String[size];
				for(int j=0;j<column.size();j++){
					s[j]=(String)column.get(j);
				}
				xp = re.rniPutStringArray(s);
				re.rniAssign(colName, xp, 0);
				re.eval(colName+"<-"+"factor("+colName+")");
				
			}
		}
		
		GlmModel glm = new GlmModel();
		
		REXP mdl = re.eval("model<-glm("+formula+",family="+family+"(link="+link+"),subset=.M)");
		if(mdl == null){ 
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Cannot build model " + err);
		}
		
		//Residuals
		REXP exp = re.eval("residuals(model,type='response')");
		double[] residuals = exp.asDoubleArray();
		exp = re.eval("as.integer(names(residuals(model,type='response')))");
		int[] index = exp.asIntArray();
		residuals = fillMissingValues(residuals, index, size);
		if (residuals != null) glm.setResiduals(residuals);
		
		exp = re.eval("residuals(model,type='deviance')");
		residuals = exp.asDoubleArray();
		residuals = fillMissingValues(residuals, index, size);
		if (residuals != null) glm.setDevianceResiduals(residuals);
		
		exp = re.eval("residuals(model,type='working')");
		residuals = exp.asDoubleArray();
		residuals = fillMissingValues(residuals, index, size);
		if (residuals != null) glm.setWorkingResiduals(residuals);
		
		//Fitted Values
		exp = re.eval("model$fitted.values");
		double[] fittedValues = exp.asDoubleArray();
		exp = re.eval("as.integer(names(model$fitted.values))");
		index = exp.asIntArray();
		fittedValues = fillMissingValues(fittedValues, index, size);
		if (fittedValues != null) glm.setFittedValues(fittedValues);
		
		exp = re.eval("model$linear.predictors");
		fittedValues = exp.asDoubleArray();
		fittedValues = fillMissingValues(fittedValues, index, size);
		if (fittedValues != null) glm.setLinearPredictors(fittedValues);
		
		//Coefficients
		exp = re.eval("model.summary<-summary(model)");
		exp = re.eval("coef<-model.summary$coefficients");
		double[][] coefficients = exp.asDoubleMatrix();
		exp = re.eval("dimnames(coef)[[1]]");
		String[] coefRowNames = exp.asStringArray();
		exp = re.eval("dimnames(coef)[[2]]");
		String[] coefColNames = exp.asStringArray();
		
		if (coefficients != null){ 
			glm.setCoefficients(coefficients);
			glm.setCoefRowNames(coefRowNames);
			glm.setCoefColNames(coefColNames);
		}
		
		
		//Get ANOVA table
		exp = re.eval("model.anova<-anova(model,test='Chi')");
		exp = re.eval("as.matrix(model.anova)");
		double[][] anova = exp.asDoubleMatrix();
		exp = re.eval("row.names(model.anova)");
		String[] anovaRowNames = exp.asStringArray();
		exp = re.eval("names(model.anova)");
		String[] anovaColNames = exp.asStringArray();
		
		if (anova != null){ 
			glm.setAnova(anova);
			glm.setAnovaRowNames(anovaRowNames);
			glm.setAnovaColNames(anovaColNames);
		}
		
		//Leverage
		int[] assignNum = new int[0];
		glm.setAssignNum(assignNum);
		double[][] leveragex = new double[0][0];
		glm.setLeveragex(leveragex);
		
		
				
		//modelMatrix
		exp = re.eval("x = model.matrix(model)");
		exp = re.eval("t(x)");
		double[][] modelMatrix = exp.asDoubleMatrix();
		for (int i = 0; i < modelMatrix.length; i++)
			modelMatrix[i] = fillMissingValues(modelMatrix[i], index, size);
		glm.setModelMatrix(modelMatrix);
		
		
		//hat, studRes, jackRes
		exp = re.eval("influence(model)$hat");
		if(exp == null) throw new RemoteException("Cannot get leverages");
		double[] hat = exp.asDoubleArray();
		hat = fillMissingValues(hat, index, size);
		glm.setHat(hat);
		
		double[] studRes = new double[0];
		glm.setStudRes(studRes);
		
		
		exp = re.eval("rstudent(model)");
		if(exp == null) throw new RemoteException("Cannot get jacknife residuals");
		double[] jackRes = exp.asDoubleArray();
		jackRes = fillMissingValues(jackRes, index, size);
		glm.setJackRes(jackRes);
		
		if(debug){
			System.out.println("\n hat " + hat.length);
			for(int i=0; i<size; i++) System.out.print(hat[i] + " ");
			System.out.println("\n jackRes " + jackRes.length);
			for(int i=0; i<size; i++) System.out.print(jackRes[i] + " ");
		}
		
		glm.setFormula(formula);
		glm.setFamily(family);
		glm.setLink(link);


		if(debug) System.out.println("\nserver method glm finished\n");
		
		
		return glm; 
	}
	
	private double[] fillMissingValues(double[] values, int[] index, int size){
		if (values.length != index.length) return null;
		
		double[] newvalues = new double[size];
		for(int i=0; i < newvalues.length; i++)
			newvalues[i] = Double.NaN;
		
		/*int i = 0, j = 0;
		while(i < newvalues.length){
			if (j >= index.length) break;
			int k = index[j] - 1;
			if (i < k) newvalues[i] = Double.NaN;
			else if(i == k){
				newvalues[i] = values[j];
				j++;
			}
			i++;
		}*/
		for (int i=0; i<index.length; i++){
			newvalues[index[i]-1] = values[i];
		}
				
		return newvalues;
	}
	
	/*private void replaceNaN(double[] values){
		for (int i=0; i<values.length; i++){
			if(Double.isNaN(values[i])) 
				values[i] = 0;
		}
	}*/
	
	private int firstIndexOf(int val, int[] array){
		for(int i=0; i < array.length; i++)
			if(val == array[i]) return i;
		return -1;
	}
	
	private int lastIndexOf(int val, int[] array){
		for(int i=array.length - 1; i >= 0; i--)
			if(val == array[i]) return i;
		return -1;
	}
	
	private int[] arrayToVector(int[][] array){
		int[] ret = new int[array.length * array[0].length];
		for(int i=0; i < array.length; i++)
			for(int j=0; j < array[0].length; j++)
				ret[ i * array[0].length + j ] = array[i][j];
		return ret;
	}
	
	public void importFiles(String clientid, String pathname, String objName) throws RemoteException{
		
		try {
			loadWorkSpace(clientid);
		} 
		catch (RemoteException rex) { // work space file does not exist
			re.eval("rm(list=ls(all=TRUE))");
		}
		
		int i = clientid.indexOf(SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval(objName + " <- ReadAffy(celfile.path='" + STORAGE_PATH + s + File.separator + pathname + File.separator + "')");
		if(exp == null) throw new RemoteException("Cannot read uploaded files");

		saveWorkSpace(clientid);	
		
		re.assign(".id", "");
		re.eval("save(.id, file='" + STORAGE_PATH + s + File.separator + "id.RData')");
	}
	
	public void importTxtFile(String clientid, String pathname, String filename, String objName) throws RemoteException{
		try {
			loadWorkSpace(clientid);
		} 
		catch (RemoteException rex) { // work space file does not exist
			re.eval("rm(list=ls(all=TRUE))");
		}
		
		int i = clientid.indexOf(SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		REXP exp = re.eval(objName + " <- read.table(file='" + STORAGE_PATH + s + File.separator + pathname + File.separator + filename + "',header=T,sep='\\t')");
		if(exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Cannot read the uploaded file " + err);
		}
		
		saveWorkSpace(clientid);	
		
		re.assign(".id", "");
		re.eval("save(.id, file='" + STORAGE_PATH + s + File.separator + "id.RData')");
	}	
	
	public void importExonFiles(String clientid, String pathname, String chipType, String objName) throws RemoteException {
		try {
			loadWorkSpace(clientid);
		} 
		catch (RemoteException rex) { // work space file does not exist
			re.eval("rm(list=ls(all=TRUE))");
		}
		
		int i = clientid.indexOf(SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		//Read data
		REXP exp = re.eval("library('aroma.affymetrix')");
		if(exp == null) throw new RemoteException("Cannot load library aroma.affymetrix");
		
		exp = re.eval("library('aroma.cn')");
		if(exp == null) throw new RemoteException("Cannot load library aroma.cn");
		
		re.eval("setwd('" + STORAGE_PATH + s + File.separator + pathname + "')");
		exp = re.eval(objName + " <- AffymetrixCelSet$byName('" + objName + "', chipType='" + chipType + "')");
		if(exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Cannot read the uploaded file " + err);
		}		
		
		saveWorkSpace(clientid);	
		
		re.assign(".id", "");
		re.eval("save(.id, file='" + STORAGE_PATH + s + File.separator + "id.RData')");
	}
	
	
	public Object exonRma(String clientid, String objName) throws RemoteException
	{		
		REXP exp = re.eval("library('aroma.affymetrix')");
		if(exp == null) throw new RemoteException("Cannot load library aroma.affymetrix");
		
		exp = re.eval("library('aroma.cn')");
		if(exp == null) throw new RemoteException("Cannot load library aroma.cn");
		
		exp = re.eval("class(" + objName + ")[1]");	
		if (exp == null || !exp.asString().equals("AffymetrixCelSet"))
			throw new RemoteException("Unsupported object");
		
		int i = clientid.indexOf(SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		re.eval("setwd('" + STORAGE_PATH + s + File.separator + objName + "')");
		
		re.eval(".verbose <- Arguments$getVerbose(-10, timestamp=TRUE)");
		re.eval(".chipType <- getChipType(" + objName + ")");
		re.eval(".cdf <- AffymetrixCdfFile$byChipType(.chipType)");
		re.eval(".ptf <- AffymetrixProbeTabFile$byChipType(.chipType)");
		re.eval(".cdf <- getCdf(" + objName + ")");
		re.eval(".cdfS <- AffymetrixCdfFile$byChipType(getChipType(.cdf, fullname=FALSE))");
		re.eval("setCdf(" + objName + ", .cdfS)");
		exp = re.eval(objName + ".RBC <- process(RmaBackgroundCorrection(" + objName + "), cdf=.cdf, ptf=.ptf, verbose=.verbose)");
		if(exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("RMA failed " + err);
		}	
		
		exp = re.eval(objName + ".RBC.QN <- process(QuantileNormalization(" + objName + ".RBC, typesToUpdate='pm'), verbose=.verbose)");
		if(exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("RMA failed " + err);
		}	
		
		re.eval("rm(.verbose, .chipType, .cdf, .ptf, .cdfS)");
		
		return new StringBuffer("RMA process is successful. Right click object to download normalized CEL files");
	}
	
	public Object exonGcRma(String clientid, String objName) throws RemoteException
	{		
		REXP exp = re.eval("library('aroma.affymetrix')");
		if(exp == null) throw new RemoteException("Cannot load library aroma.affymetrix");
		
		exp = re.eval("library('aroma.cn')");
		if(exp == null) throw new RemoteException("Cannot load library aroma.cn");
		
		exp = re.eval("class(" + objName + ")[1]");	
		if (exp == null || !exp.asString().equals("AffymetrixCelSet"))
			throw new RemoteException("Unsupported object");
		
		int i = clientid.indexOf(SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		re.eval("setwd('" + STORAGE_PATH + s + File.separator + objName + "')");
		
		re.eval(".verbose <- Arguments$getVerbose(-10, timestamp=TRUE)");
		re.eval(".chipType <- getChipType(" + objName + ")");
		re.eval(".cdf <- AffymetrixCdfFile$byChipType(.chipType)");
		re.eval(".ptf <- AffymetrixProbeTabFile$byChipType(.chipType)");
		re.eval(".cdf <- getCdf(" + objName + ")");
		re.eval(".cdfS <- AffymetrixCdfFile$byChipType(getChipType(.cdf, fullname=FALSE))");
		re.eval("setCdf(" + objName + ", .cdfS)");
		exp = re.eval(objName + ".GRBC <- process(GcRmaBackgroundCorrection(" + objName + ", type='affinities'), cdf=.cdf, ptf=.ptf, verbose=.verbose)");
		if(exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("GCRMA failed " + err);
		}	
		
		/*
		exp = re.eval(objName + ".GRBC.QN <- process(QuantileNormalization(" + objName + ".GRBC, typesToUpdate='pm'), verbose=.verbose)");
		if(exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("GCRMA failed " + err);
		}*/	
		
		re.eval("rm(.verbose, .chipType, .cdf, .ptf, .cdfS)");
		
		return new StringBuffer("GCRMA process is successful. Right click object to download normalized CEL files");
	}
	
	
	public String[] listObjects(String clientid) throws RemoteException{
		
		String s;
		
		int i = clientid.indexOf(SEPARATOR);
		s = i < 0 ? clientid : clientid.substring(0, i); // get ip address in clientid
		
		File dir = new File(STORAGE_PATH + s);
		if (!dir.exists() || !dir.isDirectory()) 
			throw new RemoteException("No workspace file on server");
		
		File file = new File(STORAGE_PATH + s + File.separator + "ls.RData");
		if (!file.exists() || !file.isFile()) 
			throw new RemoteException("No workspace file on server");
		
		re.eval("load(file='" + STORAGE_PATH + s + File.separator + "ls.RData')");
		
		REXP exp = re.eval(".ls");
		String[] lst = exp.asStringArray();
		

		return lst;
	}
	
	public Object summary(String objName) throws RemoteException{
		
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("library('multtest')");
		if(exp == null) throw new RemoteException("Cannot load library multtest");
		
		exp = re.eval("class(" + objName + ")[1]");
		if(debug) System.out.println("\nserver method summary called\n" + exp);
		
	
		if (exp != null && exp.asString().equals("AffyBatch")) 
		{
			AffyBatch affy = new AffyBatch();
			
			affy.setObjName(objName);
			exp = re.eval("length(geneNames(" + objName + "))");	
			if (exp == null) exp = re.eval("length(featureNames(" + objName + "))");	
			affy.setNumGene(exp.asInt());
			exp = re.eval("length(probeNames(" + objName + "))");
			affy.setNumProbe(exp.asInt());
			exp = re.eval("length(sampleNames(" + objName + "))");
			int num = exp.asInt();
			affy.setNumSample(num);
			
			double[][] intensityQtl = new double[num][];
			re.eval(".tmp <- unlist(indexProbes(" + objName + ", 'both'))");
			re.eval(".tmp <- .tmp[seq(1, length(.tmp), len = 5000)]");

			for (int i=0; i<num; i++)
			{
				exp = re.eval("fivenum(intensity(" + objName + ")[.tmp," + (i+1) + "])");				
				intensityQtl[i] = exp.asDoubleArray();
			}
			
			affy.setIntensityQtl(intensityQtl);
			
			if (debug) {
				for (int i=0; i<num; i++) {
					for (int j=0; j<5; j++)
						System.out.print(intensityQtl[i][j] + " ");
					System.out.println();
				}
			}
						
			exp = re.eval("sampleNames(" + objName + ")");
			affy.setSampleNames(exp.asStringArray());
			
			re.eval("rm(.tmp)");
			
			if(debug) System.out.println("\nserver method summary finished\n");
			
			return affy;			
		
		}
		else if (exp != null && (exp.asString().equals("exprSet") || exp.asString().equals("ExpressionSet")))
		{
			exprSet eset = new exprSet();
			
			eset.setObjName(objName);
			exp = re.eval("length(geneNames(" + objName + "))");	
			if (exp == null) exp = re.eval("length(featureNames(" + objName + "))"); //geneNames is defunct in R 2.7
			eset.setNumGene(exp.asInt());
			exp = re.eval("length(sampleNames(" + objName + "))");
			int num = exp.asInt();
			eset.setNumSample(num);
			
			double[][] intensityQtl = new double[num][];
			re.eval(".tmp <- seq(1, nrow(exprs(" + objName + ")), len = 5000)");
			
			for (int i=0; i<num; i++)
			{
				exp = re.eval("fivenum(exprs(" + objName + ")[.tmp," + (i+1) + "])");				
				intensityQtl[i] = exp.asDoubleArray();
			}
			
			eset.setIntensityQtl(intensityQtl);
			
			exp = re.eval("sampleNames(" + objName + ")");
			eset.setSampleNames(exp.asStringArray());
			
			re.eval("rm(.tmp)");
			
			return eset;			
		}
		else if (exp != null && exp.asString().equals("data.frame"))
		{
			exprSet eset = new exprSet();
			eset.setObjName(objName);
			exp = re.eval("nrow(" + objName + ")");
			eset.setNumGene(exp.asInt());
			re.eval(".type = sapply(" + objName + ",function(t) ifelse(class(t)=='numeric',T,F))");
			re.eval(".data = "+objName+"[,.type]");
			exp = re.eval("ncol(.data)");
			int num = exp.asInt();
			eset.setNumSample(num);
			
			double[][] intensityQtl = new double[num][];
			re.eval(".tmp <- seq(1, nrow(.data), len = 5000)");
			
			for (int i=0; i<num; i++)
			{
				exp = re.eval("fivenum(.data[.tmp," + (i+1) + "])");				
				intensityQtl[i] = exp.asDoubleArray();
			}
			
			eset.setIntensityQtl(intensityQtl);
			
			exp = re.eval("colnames(.data)");
			eset.setSampleNames(exp.asStringArray());
			
			re.eval("rm(.type, .data, .tmp)");

			return eset;		
		}
		else if (exp != null && exp.asString().equals("MTP"))
		{
			MTP mtp = new MTP();
			
			mtp.setObjName(objName);
			
			String name = objName.substring(0, objName.lastIndexOf(".")); 
			
			mtp.setExprObjName(name);
			
			exp = re.eval("length(" + objName + "@adjp)");
			int numHypo = exp.asInt();
			mtp.setNumHypo(numHypo);
			
			exp = re.eval(objName + "@adjp");
			mtp.setAdjp(exp.asDoubleArray());
			
			exp = re.eval(objName + "@rawp");
			mtp.setRawp(exp.asDoubleArray());
			
			exp = re.eval(objName + "@call$log2fc");
			mtp.setLog2fc(exp.asDoubleArray());
			
			exp = re.eval(objName + "@statistic");
			mtp.setStatistic(exp.asDoubleArray());
			
			exp = re.eval("names(" + objName + "@adjp)");
			mtp.setGenes(exp.asStringArray());
			
			exp = re.eval(objName + "@call$alpha");
			double alpha = exp.asDouble();
			mtp.setAlpha(alpha);

			exp = re.eval(objName + "@sampsize");
			mtp.setNumSample(exp.asInt());
			
			exp = re.eval(objName + "@call$typeone");
			mtp.setTypeone(exp.asString());
			
			exp = re.eval(objName + "@call$method");
			mtp.setMethod(exp.asString());		
			
			exp = re.eval(objName + "@call$y");
			mtp.setSampTypes(exp.asStringArray());
			
			exp = re.eval("names(" + objName + "@call$y)");
			mtp.setSampNames(exp.asStringArray());
			
			return mtp;
		}
		else if (exp != null && exp.asString().equals("AffymetrixCelSet"))
		{
			return new StringBuffer("No summary yet. Use contextual menu for its operation.");
		}
		else 
		{		
			throw new RemoteException("Unsupported object");
		}
	}
	
	
	public Object geneNames(String objName) throws RemoteException{
		
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("class(" + objName + ")");		

		if (exp != null && (exp.asString().equals("AffyBatch") || exp.asString().equals("exprSet") 
				|| exp.asString().equals("ExpressionSet") || exp.asString().equals("data.frame"))) 
		{
			if (exp.asString().equals("data.frame")) {
				exp = re.eval("if (class("+objName+"[[1]]) == 'factor') as.character("+objName+"[[1]]) else as.character(rownames("+objName+"))");
			} else {
				exp = re.eval("geneNames(" + objName + ")");		
				if (exp == null) exp = re.eval("featureNames(" + objName + ")");	
			}
			
			String[] geneNames = exp.asStringArray();
			
			ArrayList columns=new ArrayList();
        	ArrayList column=new ArrayList();
        	column.add("gene");
        	column.add(String.class);
        	column.add(new Integer(GDataSet.L_ROLE));
        	column.addAll(Arrays.asList(geneNames));
        	columns.add(column);
        	GDataSet gdata = new GDataSet("genes of " + objName, columns);
					
			return gdata;			
			
		} else {		
			throw new RemoteException("Unsupported object");
		}
	}
	
	
	public Object geneIntensity(String objName, String geneName) throws RemoteException{
				
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("class(" + objName + ")");		

		if (exp != null && exp.asString().equals("AffyBatch")) 
		{
			exp = re.eval("length(sampleNames(" + objName + "))");
			int num = exp.asInt();
			
			ArrayList columns=new ArrayList();
        	ArrayList sampleCol=new ArrayList();
        	sampleCol.add("array");
        	sampleCol.add(String.class);
        	sampleCol.add(new Integer(GDataSet.X_ROLE));
        	ArrayList probeCol=new ArrayList();
        	probeCol.add("probe");
        	probeCol.add(String.class);
        	probeCol.add(new Integer(GDataSet.X_ROLE));
        	ArrayList pmCol=new ArrayList();
        	pmCol.add("PM");
        	pmCol.add(Double.class);
        	pmCol.add(new Integer(GDataSet.Y_ROLE));
        	ArrayList mmCol=new ArrayList();
        	mmCol.add("MM");
        	mmCol.add(Double.class);
        	mmCol.add(new Integer(GDataSet.Y_ROLE));
        	
			for (int i = 1; i <= num; i++){
				exp = re.eval("pm(" + objName + ",'" + geneName + "')[," + i + "]");
				if (exp == null) throw new RemoteException("gene " + geneName + " does not exist");
				double[] pm = exp.asDoubleArray();
				
				exp = re.eval("mm(" + objName + ",'" + geneName + "')[," + i + "]");
				double[] mm = exp.asDoubleArray();
				
				for (int j = 0; j < pm.length; j++){
					sampleCol.add(String.valueOf(i));
					probeCol.add(String.valueOf(j+1));
					pmCol.add(new Double(pm[j]));
					mmCol.add(new Double(mm[j]));
				}				
			}
			
			columns.add(sampleCol);
			columns.add(probeCol);
			columns.add(pmCol);
			columns.add(mmCol);
			
			GDataSet gdata = new GDataSet(geneName + " of " + objName, columns);
			return gdata;	
		}
		else if (exp != null && (exp.asString().equals("exprSet") || exp.asString().equals("ExpressionSet"))) {
			
			if (geneName == null) return geneIntensity(objName);
			
			exp = re.eval("length(sampleNames(" + objName + "))");
			int num = exp.asInt();
			
			ArrayList columns=new ArrayList();
        	ArrayList sampleCol=new ArrayList();
        	sampleCol.add("array");
        	sampleCol.add(String.class);
        	sampleCol.add(new Integer(GDataSet.U_ROLE));
        	ArrayList exprCol=new ArrayList();
        	exprCol.add("intensity");
        	exprCol.add(Double.class);
        	exprCol.add(new Integer(GDataSet.Y_ROLE));
        	
        	exp = re.eval("exprs(" + objName + ")['" + geneName + "',]");
        	if (exp == null) throw new RemoteException("gene " + geneName + " does not exist");
        	double[] intensity = exp.asDoubleArray();
        	
        	for (int i = 1; i <= num; i++){
        		sampleCol.add(String.valueOf(i));
        		exprCol.add(new Double(intensity[i-1]));
        	}
        	
        	columns.add(sampleCol);
        	columns.add(exprCol);
			
			GDataSet gdata = new GDataSet(geneName + " of " + objName, columns);
			
			return gdata;	
		} 
		else if (exp != null && exp.asString().equals("data.frame")) {
			
			if (geneName == null) return geneIntensity(objName);
			
			re.eval(".data <- " + objName);
			re.eval("if (class(.data[[1]]) == 'factor') rownames(.data)<-as.character(.data[[1]])");
			re.eval(".type = sapply(.data,function(t) ifelse(class(t)=='numeric',T,F))");
			re.eval(".data = .data[,.type]");
			exp = re.eval("ncol(.data)");
			int num = exp.asInt();
		
			ArrayList columns=new ArrayList();
        	ArrayList sampleCol=new ArrayList();
        	sampleCol.add("array");
        	sampleCol.add(String.class);
        	sampleCol.add(new Integer(GDataSet.U_ROLE));
        	ArrayList exprCol=new ArrayList();
        	exprCol.add("intensity");
        	exprCol.add(Double.class);
        	exprCol.add(new Integer(GDataSet.Y_ROLE));       
        	
        	exp = re.eval("as.matrix(.data)['" + geneName + "',]");
        	if (exp == null) throw new RemoteException("gene " + geneName + " does not exist");
        	double[] intensity = exp.asDoubleArray();
      
        	for (int i = 1; i <= num; i++){
        		sampleCol.add(String.valueOf(i));
        		exprCol.add(new Double(intensity[i-1]));
        	}
        	
        	columns.add(sampleCol);
        	columns.add(exprCol);
			
			GDataSet gdata = new GDataSet(geneName + " of " + objName, columns);
			
			re.eval("rm(.type, .data)");
			
			return gdata;	
			
		} else {		
			throw new RemoteException("Unsupported object");
		}
	}
	
	
	public Object geneIntensity(String objName) throws RemoteException{
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("class(" + objName + ")");	
		
		if (exp != null && (exp.asString().equals("exprSet") || exp.asString().equals("ExpressionSet"))) {
			exp = re.eval("exprs(" + objName + ")");
			double[][] intensity = exp.asDoubleMatrix();
			
			exp = re.eval("rownames(exprs(" + objName + "))");
			String[] geneNames = exp.asStringArray();
			
			exp = re.eval("colnames(exprs(" + objName + "))");
			String[] sampleNames = exp.asStringArray();
			
			ArrayList columns=new ArrayList();
			ArrayList geneCol=new ArrayList();
        	geneCol.add("gene");
        	geneCol.add(String.class);
        	geneCol.add(new Integer(GDataSet.L_ROLE));
        	
        	for (int i = 0; i < geneNames.length; i++){
        		geneCol.add(geneNames[i]);
        	}
        	columns.add(geneCol);
        	
        	for (int i = 0; i < sampleNames.length; i++){
        		ArrayList exprCol=new ArrayList();
            	exprCol.add(sampleNames[i]);
            	exprCol.add(Double.class);
            	exprCol.add(new Integer(GDataSet.U_ROLE));
            	
            	for (int j = 0; j < geneNames.length; j++){
            		exprCol.add(new Double(intensity[j][i]));
            	}
            	columns.add(exprCol);
        	}
        	
        	GDataSet gdata = new GDataSet("intensity of " + objName, columns);
			return gdata;	
		
		} else if (exp != null && exp.asString().equals("data.frame")) {
			
			re.eval(".data <- " + objName);
			re.eval("if (class(.data[[1]]) == 'factor') rownames(.data)<-as.character(.data[[1]])");
			re.eval(".type = sapply(.data,function(t) ifelse(class(t)=='numeric',T,F))");
			re.eval(".data = .data[,.type]");
			
			exp = re.eval("as.matrix(.data)");
			double[][] intensity = exp.asDoubleMatrix();
			
			exp = re.eval("rownames(as.matrix(.data))");
			String[] geneNames = exp.asStringArray();
			
			exp = re.eval("colnames(as.matrix(.data))");
			String[] sampleNames = exp.asStringArray();
			
			ArrayList columns=new ArrayList();
			ArrayList geneCol=new ArrayList();
        	geneCol.add("gene");
        	geneCol.add(String.class);
        	geneCol.add(new Integer(GDataSet.L_ROLE));
        	
        	for (int i = 0; i < geneNames.length; i++){
        		geneCol.add(geneNames[i]);
        	}
        	columns.add(geneCol);
        	
        	for (int i = 0; i < sampleNames.length; i++){
        		ArrayList exprCol=new ArrayList();
            	exprCol.add(sampleNames[i]);
            	exprCol.add(Double.class);
            	exprCol.add(new Integer(GDataSet.U_ROLE));
            	
            	for (int j = 0; j < geneNames.length; j++){
            		exprCol.add(new Double(intensity[j][i]));
            	}
            	columns.add(exprCol);
        	}
        	
        	GDataSet gdata = new GDataSet("intensity of " + objName, columns);
        	
        	re.eval("rm(.type, .data)");
			return gdata;				
			
		} else {		
			throw new RemoteException("Unsupported object");
		}
	}
	
	public Object geneIntensityAsColumnByNames(String objName, ArrayList geneNames) throws RemoteException{
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("class(" + objName + ")");	
		
		if (exp != null && (exp.asString().equals("exprSet") || exp.asString().equals("ExpressionSet") || exp.asString().equals("data.frame"))) {
			
			if (exp.asString().equals("data.frame")) {
				re.eval(".data <- " + objName);
				re.eval("if (class(.data[[1]]) == 'factor') rownames(.data)<-as.character(.data[[1]])");
				re.eval(".type = sapply(.data,function(t) ifelse(class(t)=='numeric',T,F))");
				re.eval(".data = .data[,.type]");
				re.eval(".m <- as.matrix(.data)");
				exp = re.eval("colnames(.data)");
			} else {
				re.eval(".m <- exprs(" + objName + ")");
				exp = re.eval("sampleNames(" + objName + ")");
			}
			
			String[] sampleNames = exp.asStringArray();
			
			ArrayList columns = new ArrayList();
			
			//Add column for array names
			ArrayList sampleCol=new ArrayList();
        	sampleCol.add("array");
        	sampleCol.add(String.class);
        	sampleCol.add(new Integer(GDataSet.L_ROLE));
        	
        	for (int i = 0; i < sampleNames.length; i++){
        		sampleCol.add(sampleNames[i]);
        	}
        	columns.add(sampleCol);
        	
        	//Add intensity columns
        	for (int i = 0; i < geneNames.size(); i++){
        		
        		String geneName = (String)geneNames.get(i);
        		ArrayList exprCol=new ArrayList();
            	exprCol.add(geneName);
            	exprCol.add(Double.class);
            	exprCol.add(new Integer(GDataSet.Y_ROLE));
            	
            	exp = re.eval(".m['" + geneName + "',]");
            	if (exp == null) throw new RemoteException("gene " + geneName + " does not exist");
            	double[] intensity = exp.asDoubleArray();
            	
            	for (int j = 0; j < intensity.length; j++){
            		exprCol.add(new Double(intensity[j]));
            	}
            	columns.add(exprCol);
        	}
			
        	GDataSet gdata = new GDataSet("intensity of " + objName + " (gene as column)", columns);
        	re.eval("rm(.type, .data, .m)");
			return gdata;
					
		} else {		
			throw new RemoteException("Unsupported object");
		}
	}
	
	
	public Object geneIntensityAsRowByNames(String objName, ArrayList geneNames) throws RemoteException{
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("class(" + objName + ")");	
		
		if (exp != null && (exp.asString().equals("exprSet") || exp.asString().equals("ExpressionSet") || exp.asString().equals("data.frame"))) {
			
			if (exp.asString().equals("data.frame")) {
				re.eval(".data <- " + objName);
				re.eval("if (class(.data[[1]]) == 'factor') rownames(.data)<-as.character(.data[[1]])");
				re.eval(".type = sapply(.data,function(t) ifelse(class(t)=='numeric',T,F))");
				re.eval(".data = .data[,.type]");
				re.eval(".m <- as.matrix(.data)");
				exp = re.eval("colnames(.data)");
				
			} else {
				re.eval(".m <- exprs(" + objName + ")");
				exp = re.eval("sampleNames(" + objName + ")");
			}
			
			String[] sampleNames = exp.asStringArray();
			
			ArrayList columns = new ArrayList();
			
			//Add column for array names
			ArrayList firstCol=new ArrayList();
        	firstCol.add("gene");
        	firstCol.add(String.class);
        	firstCol.add(new Integer(GDataSet.L_ROLE));
        	
        	for (int i = 0; i < geneNames.size(); i++){
        		firstCol.add(geneNames.get(i));
        	}
        	columns.add(firstCol);
        	
        	//Add intensity columns
        	for (int i = 0; i < sampleNames.length; i++){
        		
        		ArrayList exprCol=new ArrayList();
            	exprCol.add(sampleNames[i]);
            	exprCol.add(Double.class);
            	exprCol.add(new Integer(GDataSet.Y_ROLE));
            	columns.add(exprCol);
        	}
            	
            for (int i = 0; i < geneNames.size(); i++){
            	String geneName = (String)geneNames.get(i);
            	exp = re.eval(".m['" + geneName + "',]");
            	if (exp == null) throw new RemoteException("gene " + geneName + " does not exist");
            	double[] intensity = exp.asDoubleArray();
            	
            	for (int j = 0; j < sampleNames.length; j++) {
            		ArrayList exprCol = (ArrayList)columns.get(j+1);
            		exprCol.add(new Double(intensity[j]));
            	}
        	}
			
        	GDataSet gdata = new GDataSet("intensity of " + objName + " (gene as row)", columns);
        	re.eval("rm(.type, .data, .m)");
			return gdata;
			
		} else {		
			throw new RemoteException("Unsupported object");
		}
	}
	
	public Object geneAnnotationByNames(String type, ArrayList geneNames) throws RemoteException{
		REXP exp = re.eval("library('DAVIDQuery')");
		if(exp == null) throw new RemoteException("Cannot load library DAVIDQuery");
		
		ArrayList columns = new ArrayList();
		
		//Add column for gene names
		ArrayList firstCol=new ArrayList();
    	firstCol.add("gene");
    	firstCol.add(String.class);
    	firstCol.add(new Integer(GDataSet.L_ROLE));
    	
    	for (int i = 0; i < geneNames.size(); i++){
    		firstCol.add(geneNames.get(i));
    	}
    	columns.add(firstCol);
    	
    	//Add annotation columns
    	String[] colnames = {"OFFICIAL_GENE_SYMBOL", "GENE_SYMBOL", "Gene Name", "URL", "Species", "CHROMOSOME", "CYTOBAND", "ENSEMBL_GENE_ID", "ENTREZ_GENE_ID"};
    	
    	for (int i = 0; i < colnames.length; i++) {
    		ArrayList col=new ArrayList();
    		col.add(colnames[i]);
    		col.add(String.class);
    		col.add(new Integer(GDataSet.U_ROLE));
    		columns.add(col);
    	}    	
    	
    	for (int i = 0; i < geneNames.size(); i++){
    		String geneName = (String)geneNames.get(i);
    		exp = re.eval(".result = DAVIDQuery(ids='" + geneName + "', type = '" + type + "', annot = NULL, tool = 'geneReportFull', formatIt=FALSE)");
        	if (exp == null) {
        		String err = re.eval("geterrmessage()").asString();
    			throw new RemoteException("Cannot get annotation of " + geneName + err);
        	}
        	else {
        		
        		String[] colcmds = {".result$DAVIDQueryResult$`OFFICIAL_GENE_SYMBOL`",
        							".result$DAVIDQueryResult$`GENE_SYMBOL`",
        							".result$DAVIDQueryResult$`Gene Name`",
        							".result$firstURL", 
        							".result$DAVIDQueryResult$`Species`",
        							".result$DAVIDQueryResult$`CHROMOSOME`",
        							".result$DAVIDQueryResult$`CYTOBAND`",
        							".result$DAVIDQueryResult$`ENSEMBL_GENE_ID`",
        							".result$DAVIDQueryResult$`ENTREZ_GENE_ID`"
        							}; 
        		
        		for (int j = 0; j < colcmds.length; j++) {
        			exp = re.eval(colcmds[j]);
        			String anno = exp == null ? "" : exp.asString();
        			ArrayList theCol = (ArrayList)columns.get(j + 1);
        			theCol.add(anno);
        		}
        	}
    	}
    	
    	GDataSet gdata = new GDataSet("annotation of rejected genes", columns);
    	re.eval("rm(.result)");
		return gdata;
	}
	
	public Object preprocess(String objName, String bgcorrect, String normalize, String pmcorrect, String summary) throws RemoteException
	{
		String newobj = null;
		
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("class(" + objName + ")");	
		if (exp == null || !exp.asString().equals("AffyBatch"))
			throw new RemoteException("Unsupported object");
		
		if (bgcorrect != null) {
			newobj = objName + "." + bgcorrect;
			exp = re.eval(newobj + " <- bg.correct(" + objName + ", method='" + bgcorrect + "')");
		}
		else if (normalize != null) {
			newobj = objName + "." + normalize;
			exp = re.eval(newobj + " <- normalize(" + objName + ", method='" + normalize + "')");
		}
		else if (pmcorrect != null && summary != null) {
			newobj = objName + "." + pmcorrect + "." + summary;
			exp = re.eval(newobj + " <- expresso(" + objName + ", bg.correct=FALSE, normalize=FALSE, pmcorrect.method='" + 
					pmcorrect + "', summary.method='" + summary + "')");
		}
		
		if (exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Preprocess error " + err);
		}
		
		return summary(newobj);
	}
	
	public Object gcrma(String objName) throws RemoteException
	{
		String newobj = null;
		
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		exp = re.eval("library('gcrma')");
		if(exp == null) throw new RemoteException("Cannot load library gcrma");
		
		exp = re.eval("class(" + objName + ")");	
		if (exp == null || !exp.asString().equals("AffyBatch"))
			throw new RemoteException("Unsupported object");
		
		newobj = objName + ".gcrma";
		exp = re.eval(newobj + " <- gcrma(" + objName + ")");
		
		if (exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("GCRMA error " + err);
		}
		
		re.eval("exprs(" + newobj + ") <- 2^(exprs(" + newobj + "))");
		
		return summary(newobj);
	}	
	
	/**
	 * 
	 * @param objName
	 * @param mtpName
	 * @param test
	 * @param typeone
	 * @param k
	 * @param q
	 * @param alpha
	 * @param method
	 * @param yInputs : Original inputs to classify tumor and normal groups. Missing values may be included.     "T","N",""
	 * @param yValues : Labels to classify tumor and normal groups without missing values.      "T","N"
	 * @param yIncluded : Boolean values to indicate missing values or not.      Boolean.TRUE,Boolean.FALSE
	 * @param filterfuns
	 * @return
	 * @throws RemoteException
	 */
	public Object mtp(String objName, String mtpName, String test, String typeone, int k, double q, double alpha,
			String method, String fdrMethod, boolean bootstrap, List yInputs, List yValues, List yIncluded, List filterfuns) throws RemoteException
	{
		REXP exp = re.eval("library('affy')");
		if(exp == null) throw new RemoteException("Cannot load library affy");
		
		exp = re.eval("library('genefilter')");
		if(exp == null) throw new RemoteException("Cannot load library genefilter");
		
		exp = re.eval("library('multtest')");
		if(exp == null) throw new RemoteException("Cannot load library multtest");
		
		exp = re.eval("class(" + objName + ")");	
		if (exp == null || (!exp.asString().equals("exprSet") && !exp.asString().equals("ExpressionSet") && !exp.asString().equals("data.frame")))
			throw new RemoteException("Unsupported object");
		
		if (exp.asString().equals("data.frame")) {
			re.eval(".type = sapply(" + objName + ",function(t) ifelse(class(t)=='numeric',T,F))");
			re.eval(".data = "+objName+"[,.type]");
			re.eval("if (class("+objName+"[[1]]) == 'factor') rownames(.data) <- as.character("+objName+"[[1]]) else rownames(.data) <- as.character(rownames("+objName+"))");
			re.eval(".m <- as.matrix(.data)");
		} else {
			re.eval(".m <- exprs(" + objName + ")");
		}
		
		for (int i = 0; i < filterfuns.size(); i++) {
			Filterfun ffun = (Filterfun)filterfuns.get(i);
			
			if (ffun.getName().equals(Filterfun.POVERA)) 
			{
				re.eval(".f" + i + " <- pOverA(" + ffun.getArg1() + "," + ffun.getArg2() + ")");
			} 
			else if  (ffun.getName().equals(Filterfun.IQR)) 
			{
				re.eval(".f" + i + " <- function(x) (IQR(x) > " + ffun.getArg1() + ")");
			} 
			else if  (ffun.getName().equals(Filterfun.CV)) 
			{
				re.eval(".f" + i + " <- cv(" + ffun.getArg1() + "," + ffun.getArg2() + ")");
			}
		}
		
		if (filterfuns.size() > 0) {
			String ff = ".ff = filterfun(";
			for (int i = 0; i < filterfuns.size(); i++) {
				ff += ".f" + i + ",";
			}
			ff = ff.substring(0, ff.length() - 1) + ")";
			
			re.eval(ff);
			re.eval(".selected = genefilter(.m, .ff)");
		} 
		else {
			re.eval(".selected = rep(TRUE, times=nrow(.m))");
		}
		
		String[] s = new String[yValues.size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = (String) yValues.get(i);
		}
		re.assign(".y", s);
		
		boolean[] t = new boolean[yIncluded.size()];
		for(int i = 0; i <t.length; i++) {
			t[i] = ((Boolean) yIncluded.get(i)).booleanValue();
		}
		re.assign(".incl", t);
		
		String[] u = new String[yInputs.size()];
		for(int i = 0; i < u.length; i++) {
			u[i] = (String) yInputs.get(i);
		}
		re.assign(".samp", u);		
		
		String mtpobj = objName + "." + mtpName;	
		String nulldist = bootstrap ? "boot.cs" : "ic";
		
		re.eval(".filter <- function(x) {a<-x[.y=='T']; b<-x[.y=='N']; a<-a[!is.na(a)]; b<-b[!is.na(b)]; length(unique(a))==length(a) && length(unique(b))==length(b); }");
		re.eval(".selected <- .selected & apply(.m[,.incl],1,.filter)");
		
		String command = mtpobj + "<- MTP(X=.m[.selected,.incl], Y=.y, B=100, test='" + test + "', typeone='" + 
					typeone + "', k=" + k + ", q=" + q + ", fdr.method='" + fdrMethod + "', alpha=" + alpha + ", method='" + method + 
					"', nulldist='" + nulldist + "')";
		System.out.println(command);
		
		exp = re.eval(command);
		
		if (exp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Multiple Testing error " + err);
		}
		
		re.eval(mtpobj + "@call$log2fc <- log2(rowMeans(.m[.selected,.incl][,.y=='T'])  / rowMeans(.m[.selected,.incl][,.y=='N']))");
		re.eval("names(.samp) <- colnames(.m)");
		re.eval(mtpobj + "@call$y <- .samp");
		
		re.eval("rm(.ff, .selected, .y, .incl, .type, .data, .m, .samp, .filter)");
		
		for (int i = 0; i < filterfuns.size(); i++) {
			re.eval("rm(.f" + i + ")");
		}
		
		return summary(mtpobj);	
	}
	
	public Object deleteObjects(String clientid, List objNames) throws RemoteException{
		REXP exp  = re.eval("ls()");
		String[] lst = exp.asStringArray();
		
		HashSet elems = new HashSet();
		for(int i = 0; i < objNames.size(); i++) {
			String s = (String) objNames.get(i);
			for (int j = 0; j < lst.length; j++) {
				if (lst[j].startsWith(s+".") || lst[j].equals(s))
					elems.add(lst[j]);
			}
		}
		
		int i = clientid.indexOf(SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		Iterator it = elems.iterator();
		while (it.hasNext()) {
			String str = (String) it.next();
			re.eval("rm(" + str + ")");
			
			try {
			if (str.indexOf('.') == -1)
				JRIServerImpl.delete(new File(STORAGE_PATH + s + File.separator + str));
			} catch (IOException ioex) {}
		}
		
		return new StringBuffer("Objects Deleted"); // a return object is required by AsyncJRIServer
	}
	
	public void saveWorkSpace(String clientid) throws RemoteException{
		String s, n;
	
		int i = clientid.indexOf(SEPARATOR);
		s = i < 0 ? clientid : clientid.substring(0, i); // get ip address in clientid
		
		File dir = new File(STORAGE_PATH + s);
		if (dir.exists()) {
		    if (!dir.isDirectory()) 
		    	throw new RemoteException("Cannot create user folder on server");
		} else {
			if (!dir.mkdir())
				throw new RemoteException("Cannot create user folder on server");
		}
		
		i = s.lastIndexOf(IPSEPARATOR);  // get postfix in ip address
		if (i >= 0 && i < s.length() - 1)
			n = s.substring(i + 1, s.length());
		else
			n = s;
		
		re.eval("save.image(file='" + STORAGE_PATH + s + File.separator + n + ".RData')");
		
		REXP exp  = re.eval("ls()");
		String[] lst = exp.asStringArray();
		Vector v = new Vector();
		
		for (i = 0; i < lst.length; i++) 
		{
			exp = re.eval("class(" + lst[i] + ")[1]");
			String cls = exp.asString();
			if (cls.equals("AffyBatch") || cls.equals("exprSet") ||
				cls.equals("ExpressionSet") || cls.equals("MTP") || cls.equals("data.frame") || cls.equals("AffymetrixCelSet")) 
			{
				 v.addElement(lst[i] + "(" + exp.asString() + ")");		
			}
		}
		
		lst = new String[v.size()];
		for (i = 0; i < v.size(); i++) {
			lst[i] = (String)v.elementAt(i);
		}
		
		re.assign(".ls", lst);	
		
		re.eval("save(.ls, file='" + STORAGE_PATH + s + File.separator + "ls.RData')");
		
	}
	
	public void loadWorkSpace(String clientid) throws RemoteException{

		String s, n;
		
		int i = clientid.indexOf(SEPARATOR);
		s = i < 0 ? clientid : clientid.substring(0, i); // get ip address in clientid
		
		File dir = new File(STORAGE_PATH + s);
		if (!dir.exists() || !dir.isDirectory()) 
			throw new RemoteException("No workspace file on server");
		
		i = s.lastIndexOf(IPSEPARATOR);  // get postfix in ip address
		if (i >= 0 && i < s.length() - 1)
			n = s.substring(i + 1, s.length());
		else
			n = s;
		
		File file = new File(STORAGE_PATH + s + File.separator + n + ".RData");
		if (!file.exists() || !file.isFile()) 
			throw new RemoteException("No workspace file on server");

		
		/* Check to see if we can skip load workspace file to improve efficiency */
		boolean f1 = false, f2 = false;
		REXP exp = re.eval(".id");
		if (exp != null && exp.asString().equals(clientid)) f1 = true;
		
		exp = re.eval("load(file='" + STORAGE_PATH + s + File.separator + "id.RData')");
		if (exp != null) {
			exp = re.eval(".id");
			if (exp != null && exp.asString().equals(clientid)) f2 = true;
		}
		if (f1 && f2) return;
		
		
		/* Load workspace in normal case */
		re.eval("rm(list=ls(all=TRUE))");
		re.eval("load(file='" + STORAGE_PATH + s + File.separator + n + ".RData')");
		
		
		/* Save value in order to skip load workspace next time */
		re.assign(".id", clientid);
		re.eval("save(.id, file='" + STORAGE_PATH + s + File.separator + "id.RData')");
	}

	public Object hclust(ArrayList columns, String method) throws RemoteException {
		if (debug) System.out.println("\nserver method called\n");
		
		re.eval("rm(list=ls(all=TRUE))");
		
		int ncols = columns.size();
		int nrows = ((List)columns.get(0)).size();
		double[] d = new double[nrows * ncols];
		
		for (int i = 0; i < ncols; i++) {
			List column = (List)columns.get(i);
			for (int j = 0; j < nrows; j++) {
				d[i*nrows + j] = ((Double)column.get(j)).doubleValue();
			}
		}
		long xp = re.rniPutDoubleArray(d);
		re.rniAssign("vals", xp, 0);
		re.eval("m=matrix(vals,"+nrows+","+ncols+",byrow=F)");
		re.eval("h=hclust(dist(scale(m)),method=\"" + method + "\")");
		
		REXP exp = re.eval("h$merge");
		HClustModel hcm = new HClustModel();
		double[][] array = exp.asDoubleMatrix();
		hcm.obsMerge = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[0].length; j++)
				hcm.obsMerge[i][j] = (int)array[i][j];
		
		exp = re.eval("h$order");
		hcm.obsOrder = exp.asIntArray();
		
		re.eval("m=t(m)");
		re.eval("h=hclust(dist(scale(m)),method=\"" + method + "\")");
		exp = re.eval("h$merge");
		array = exp.asDoubleMatrix();
		hcm.varMerge = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[0].length; j++)
				hcm.varMerge[i][j] = (int)array[i][j];
		
		exp = re.eval("h$order");
		hcm.varOrder = exp.asIntArray();
		
		return hcm;
	}
	
	public Object pca(ArrayList y, ArrayList z) throws RemoteException {
		if (debug) System.out.println("\nserver method called\n");
		re.eval("rm(list=ls(all=TRUE))");
		//load source
		/*String filename = System.getenv(JRIServerImpl.JRI_HOME) + File.separator + "pca.R";
		File file = new File(filename);
		if (!file.exists())
			throw new RemoteException("Source file pca.R not exist on server");
		re.eval("source(\"" + filename + "\")");
		*/
		
		REXP rexp = re.eval("library('mult')");
		if(rexp == null) throw new RemoteException("Cannot load library mult");
		
		//assign data y
		int ncols = y.size();
		int nrows = ((List)y.get(0)).size();
		double[] d = new double[nrows * ncols];
		
		for (int i = 0; i < ncols; i++) {
			List column = (List)y.get(i);
			for (int j = 0; j < nrows; j++) {
				d[i*nrows + j] = ((Double)column.get(j)).doubleValue();
			}
		}
		long xp = re.rniPutDoubleArray(d);
		re.rniAssign("values", xp, 0);
		re.eval("y=matrix(values,"+nrows+","+ncols+",byrow=F)");
		
		//assign data z
		if (z != null) {
			ncols = z.size();
			nrows = ((List)z.get(0)).size();
			d = new double[nrows * ncols];
			
			for (int i = 0; i < ncols; i++) {
				List column = (List)z.get(i);
				for (int j = 0; j < nrows; j++) {
					d[i*nrows + j] = ((Double)column.get(j)).doubleValue();
				}
			}
			xp = re.rniPutDoubleArray(d);
			re.rniAssign("values", xp, 0);
			re.eval("z=matrix(values,"+nrows+","+ncols+",byrow=F)");
		}
		
		//get results	
		PCAModel pcamdl = new PCAModel();
		if (z == null)
			rexp = re.eval("mdl = pca(y, scale=T)");
		else
			rexp = re.eval("mdl = pca(y, z, scale=T)");
			
		if(rexp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Cannot build pca model " + err);
		}
		
		REXP exp = re.eval("mdl$u");
		pcamdl.U = exp.asDoubleMatrix();
		
		exp = re.eval("mdl$v");
		pcamdl.A = exp.asDoubleMatrix();
		
		exp = re.eval("mdl$d");
		pcamdl.d = exp.asDoubleArray();
		
		return pcamdl;
	}
	
	public Object ca(ArrayList y) throws RemoteException {
		if (debug) System.out.println("\nserver method called\n");
		re.eval("rm(list=ls(all=TRUE))");
				
		REXP rexp = re.eval("library('mult')");
		if(rexp == null) throw new RemoteException("Cannot load library mult");
		
		//assign data y
		int ncols = y.size();
		int nrows = ((List)y.get(0)).size();
		double[] d = new double[nrows * ncols];
		
		for (int i = 0; i < ncols; i++) {
			List column = (List)y.get(i);
			for (int j = 0; j < nrows; j++) {
				d[i*nrows + j] = ((Double)column.get(j)).doubleValue();
			}
		}
		long xp = re.rniPutDoubleArray(d);
		re.rniAssign("values", xp, 0);
		re.eval("y=matrix(values,"+nrows+","+ncols+",byrow=F)");
		
		//get results	
		CAModel camdl = new CAModel();
		rexp = re.eval("mdl = ca(y)");
		if(rexp == null) {
			String err = re.eval("geterrmessage()").asString();
			throw new RemoteException("Cannot build ca model " + err);
		}
					
		REXP exp = re.eval("mdl$v.s");
		camdl.U = exp.asDoubleMatrix();
		
		exp = re.eval("mdl$a.s");
		camdl.A = exp.asDoubleMatrix();
		
		exp = re.eval("mdl$d");
		camdl.D = exp.asDoubleArray();
		
		return camdl;
	}
}
