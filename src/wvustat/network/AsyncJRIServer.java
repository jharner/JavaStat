package wvustat.network;

import java.rmi.Remote; 
import java.rmi.RemoteException; 
import java.util.ArrayList;
import java.util.List;

public interface AsyncJRIServer extends Remote{
	
	public String getIdleServer() throws RemoteException;
	
	public boolean isBusy() throws RemoteException;
	
	public void shutdown() throws RemoteException;
	
	public Object poll(String clientid, int taskId) throws RemoteException;
	
	public void lm(String clientid, int taskId, String formula, ArrayList data) throws RemoteException;
	
	public void glm(String clientid, int taskId, String formula, ArrayList data, String family, String link) throws RemoteException; 
	
	public void summary(String clientid, int taskId, String objName) throws RemoteException;
	
	public void geneNames(String clientid, int taskId, String objName) throws RemoteException;
	
	public void geneIntensity(String clientid, int taskId, String objName, String geneName) throws RemoteException;
	
	public void geneIntensityAsColumnByNames(String clientid, int taskId, String objName, ArrayList geneNames) throws RemoteException;
	
	public void geneIntensityAsRowByNames(String clientid, int taskId, String objName, ArrayList geneNames) throws RemoteException;
	
	public void geneAnnotationByNames(String clientid, int taskId, String type, ArrayList geneNames) throws RemoteException;
	
	public void preprocess(String clientid, int taskId, String objName, String bgcorrect, String normalize, String pmcorrect, 
			String summary)	throws RemoteException;
	
	public void gcrma(String clientid, int taskId, String objName) throws RemoteException;
	
	public void exonRma(String clientid, int taskId, String objName) throws RemoteException;
	
	public void exonGcRma(String clientid, int taskId, String objName) throws RemoteException;
	
	public void mtp(String clientid, int taskId, String objName, String mtpName, String test, String typeone, int k, double q, double alpha,
			String method, String fdrMethod, boolean bootstrap, List yInputs, List yValues, List yIncluded, List filterfuns) throws RemoteException;
	
	public void deleteObjects(String clientid, int taskId, List objNames) throws RemoteException;
}
