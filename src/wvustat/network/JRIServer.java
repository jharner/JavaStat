/*
 * Created on Oct 5, 2006
 *
 */
package wvustat.network;

import java.rmi.Remote; 
import java.rmi.RemoteException; 
import java.util.ArrayList;
import java.util.List;

/**
 * @author dajieluo
 *
 */
public interface JRIServer extends Remote{
	
	public Object lm(String formula, ArrayList data) throws RemoteException;
	
	public Object glm(String formula, ArrayList data, String family, String link) throws RemoteException; 
	
	public Object hclust(ArrayList data, String method) throws RemoteException;
	
	public Object pca(ArrayList y, ArrayList z) throws RemoteException;
	
	public Object ca(ArrayList y) throws RemoteException;
	
	public JRIServer getIdleServer() throws RemoteException;
	
	public boolean isBusy() throws RemoteException;
	
	public void shutdown() throws RemoteException;
	
	public void uploadFile(String clientid, RawFileData data, String pathname) throws RemoteException;
	
	public RawFileData downloadFile(String clientid, String path) throws RemoteException;
	
	public void deleteFolder(String clientid, String pathname) throws RemoteException;
	
	public void importFiles(String clientid, String pathname, String objName) throws RemoteException;
	
	public void importTxtFile(String clientid, String pathname, String filename, String objName) throws RemoteException;
	
	public void importExonFiles(String clientid, String pathname, String chipType, String objName) throws RemoteException;
	
	public List listFilesOfObject(String clientid, String objName) throws RemoteException;
	
	public void userLogon(String username, String password) throws RemoteException;
	
	public ArrayList listObjects(String clientid) throws RemoteException;
}
