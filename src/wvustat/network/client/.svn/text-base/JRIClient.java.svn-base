
package wvustat.network.client;

import wvustat.interfaces.*;
import wvustat.network.*;
import wvustat.table.MainPanel;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.net.*;

/**
 * @author dajieluo
 */
public class JRIClient {
	
	public static String BASIC_HOST_NAME = "stat.wvu.edu";
	public static String HOST_NAME = "stat.wvu.edu";
	public static final String SERVER_NAME = "JRISERVER";
	public static final String ASYNC_SERVER_NAME = "ASYNCJRISERVER";
	private static int taskCnt = 0;
	private static RMISocketFactory rsf = null;
	private static String ServerURL = null;
	private static String clientid = null; 
	private static String username = null;
	
	public static String getUserName() {
		return username;
	}	
	
	public static ArrayList createDataFrame(DataSet dataset,Variable y_var, Vector vx, boolean[] constrt, String yname, Vector xname) throws RemoteException
	{
		ArrayList columns=new ArrayList();
		ArrayList column=new ArrayList();
		column.add(".M");
		column.add(Boolean.class);
		columns.add(column);
		for (int i=0; i<dataset.getSize(); i++)
			column.add(new Boolean( constrt==null? !dataset.getMask(i) : !dataset.getMask(i) && constrt[i] ));
		
		column=new ArrayList();
		//column.add(y_var.getName());
		column.add(yname);
		column.add(y_var.getType() == Variable.NUMERIC ? Double.class : String.class);
		columns.add(column);
		for (int i=0; i<dataset.getSize(); i++)
			column.add(y_var.getValue(i));
		
		for (int j=0; j < vx.size(); j++){
			Variable x_var = (Variable)vx.elementAt(j);
			
			column=new ArrayList();
			//column.add(x_var.getName());
			column.add((String)xname.elementAt(j));
			column.add(x_var.getType() == Variable.NUMERIC ? Double.class : String.class);
			columns.add(column);
			for (int i=0; i<dataset.getSize(); i++)
				column.add(x_var.getValue(i));
		}
		
		return columns;
	}
	
	public static String createFormula(Variable y_var, Vector vx) throws RemoteException
	{
		String formula, name;
		name = y_var.getName();
		
		if(!isIdentifier(name)) throw new RemoteException("illegal variable name \"" + name + "\"");
		
		formula = name + "~";
		
		for (int j=0; j < vx.size(); j++){
			Variable x_var = (Variable)vx.elementAt(j);
			name = x_var.getName();
			if(!isIdentifier(name)) throw new RemoteException("illegal variable name \"" + name + "\"");
			formula += name;
			if (j < (vx.size() - 1)) formula += "+";
		}
		
		return formula;
	}
	
	public static String createFormula(Variable y_var, String inputModel) throws RemoteException
	{
		String name = y_var.getName();
		if(!isIdentifier(name)) throw new RemoteException("illegal variable name \"" + name + "\"");
		return name + "~" + inputModel;
	}
	
	public static boolean isIdentifier(String s){
  		if ( !Character.isLetter(s.charAt(0)) )
  			return false;
  		
  		boolean ret = true;
  		int i = 1;
  		while (ret && i < s.length()){
  			if ( s.charAt(i) != '_' && /*s.charAt(i) != '.' &&*/ !Character.isLetterOrDigit(s.charAt(i)) )
  				ret = false;
  			i++;
  		}
  		return ret;
  	}
	
	public static String getIPAddr() throws java.net.UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		
		// Get IP Address
	    String ipAddr = addr.getHostAddress();

	    return ipAddr;	    
	}
	
	private static String getClientId() throws java.net.UnknownHostException {
		if (clientid != null)
			return clientid;
		
		String s = username == null? "" : username + "|";
		s = s + getIPAddr() + "|" + (new Date()).getTime() + "|" + Math.round(Math.random() * 10000);
	
		return s;
	}
	
	public static JRIServer getJRIServer() throws RemoteException
	{
		String url = "//" + HOST_NAME + "/" + SERVER_NAME + "0";
        JRIServer remoteObject = null;
        
        try{
        	if (rsf == null){
        		rsf = new TimeoutFactory(5, 300);
        		RMISocketFactory.setSocketFactory(rsf);
        	}
            remoteObject = (JRIServer)Naming.lookup(url);
            remoteObject = remoteObject.getIdleServer();
            
            if (clientid == null) {
        		clientid = getClientId();
        	}
        }
        catch (java.rmi.ConnectException exc){
        	throw new RemoteException("Connection refused to host " + HOST_NAME);
        }
        catch (java.net.MalformedURLException exc)
	    {
	        throw new RemoteException("Malformed URL " + url);
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	        throw new RemoteException("Cannot connect to service " + HOST_NAME);
	    }
	    catch (java.net.UnknownHostException exc)
	    {
	    	throw new RemoteException("Cannot get local IP address");
	    }
	    catch (java.io.IOException exc)
	    {
	    	throw new RemoteException("Connection timed out to host " + HOST_NAME);
	    }
	    
	    return remoteObject;
	}
	
	public static JRIServer getBasicJRIServer() throws RemoteException
	{
		String url = "//" + BASIC_HOST_NAME + "/" + SERVER_NAME + "0";
        JRIServer remoteObject = null;
        
        try{
        	if (rsf == null){
        		rsf = new TimeoutFactory(5, 300);
        		RMISocketFactory.setSocketFactory(rsf);
        	}
            remoteObject = (JRIServer)Naming.lookup(url);
            remoteObject = remoteObject.getIdleServer();
            
            if (clientid == null) {
        		clientid = getClientId();
        	}
        }
        catch (java.rmi.ConnectException exc){
        	throw new RemoteException("Connection refused to host " + BASIC_HOST_NAME);
        }
        catch (java.net.MalformedURLException exc)
	    {
	        throw new RemoteException("Malformed URL " + url);
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	        throw new RemoteException("Cannot connect to service " + BASIC_HOST_NAME);
	    }
	    catch (java.net.UnknownHostException exc)
	    {
	    	throw new RemoteException("Cannot get local IP address");
	    }
	    catch (java.io.IOException exc)
	    {
	    	throw new RemoteException("Connection timed out to host " + BASIC_HOST_NAME);
	    }
	    
	    return remoteObject;
	}
	
	public static RLinearModel fitLinearModel(String formula, ArrayList columns, String family, String link) throws RemoteException
	{
        JRIServer remoteObject = getBasicJRIServer();
        		  
        if ("gaussian".equals(family) && "identity".equals(link)){
        	RLinearModel rlm = (RLinearModel)remoteObject.lm(formula,columns);
        	return rlm;
        }else{
        	GlmModel glm = (GlmModel)remoteObject.glm(formula,columns,family,link);
            return glm;
        }
	}
	
	public static HClustModel hclust(ArrayList columns, String method) throws RemoteException
	{
		JRIServer remoteObject = getBasicJRIServer();
		HClustModel hcm = (HClustModel)remoteObject.hclust(columns, method);
		return hcm;		
	}
	
	public static PCAModel pca(ArrayList y, ArrayList z) throws RemoteException
	{
		JRIServer remoteObject = getBasicJRIServer();
		PCAModel pcamdl = (PCAModel)remoteObject.pca(y, z);
		return pcamdl;		
	}
	
	public static CAModel ca(ArrayList y) throws RemoteException
	{
		JRIServer remoteObject = getBasicJRIServer();
		CAModel camdl = (CAModel)remoteObject.ca(y);
		return camdl;		
	}
	
	public static void uploadFile(byte[] bytes, String pathname, String filename) throws RemoteException
	{
		RawFileData data = new RawFileData(bytes, filename);
		JRIServer remoteObject = getJRIServer();
		remoteObject.uploadFile(clientid, data, pathname);		
	}
	
	public static RawFileData downloadFile(String path) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		return remoteObject.downloadFile(clientid, path);
	}
	
	public static void deleteFolder(String pathname) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		remoteObject.deleteFolder(clientid, pathname);
	}
	
	public static void importFiles(String pathname, String objName) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		remoteObject.importFiles(clientid, pathname, objName);
	}
	
	public static void importTxtFile(String pathname, String filename, String objName) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		remoteObject.importTxtFile(clientid, pathname, filename, objName);
	}
	
	public static void importExonFiles(String pathname, String chipType, String objName) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		remoteObject.importExonFiles(clientid, pathname, chipType, objName);
	}
	
	public static List listFilesOfObject(String objName) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		return remoteObject.listFilesOfObject(clientid, objName);
	}
	
	public static String[] listObjects() throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		return (String[])remoteObject.listObjects(clientid).toArray(new String[0]);
	}
	
	public static void userLogon(String username, String password) throws RemoteException
	{
		JRIServer remoteObject = getJRIServer();
		remoteObject.userLogon(username, password);
		JRIClient.username = username;
		JRIClient.clientid = null; //in order to renew the clientid
	}
	
	public static void userLogout()
	{
		JRIClient.username = null;
		JRIClient.clientid = null;
		JRIClient.ServerURL = null; //in order to use possible new setting of server
	}
	
	public static AsyncJRIServer getAsyncJRIServer() throws RemoteException
	{
		String url = "//" + HOST_NAME + "/" + ASYNC_SERVER_NAME + "0";
		AsyncJRIServer remoteObject = null;		
        
        try{
        	if (rsf == null) {
        		rsf = new TimeoutFactory(5, 300);
        		RMISocketFactory.setSocketFactory(rsf);
        	}
        	
        	if (ServerURL == null) {
        		remoteObject = (AsyncJRIServer)Naming.lookup(url);
        		ServerURL = remoteObject.getIdleServer();
        	}
        	
        	remoteObject = (AsyncJRIServer)Naming.lookup(ServerURL);
        	
        	if (clientid == null) {
        		clientid = getClientId();
        	}
        }
        catch (java.rmi.ConnectException exc){
        	throw new RemoteException("Connection refused to host " + HOST_NAME);
        }
        catch (java.net.MalformedURLException exc)
	    {
	        throw new RemoteException("Malformed URL " + url);
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	        throw new RemoteException("Cannot connect to service " + HOST_NAME);
	    }
	    catch (java.net.UnknownHostException exc)
	    {
	    	throw new RemoteException("Cannot get local IP address");
	    }
	    catch (java.io.IOException exc)
	    {
	    	throw new RemoteException("Connection timed out to host " + HOST_NAME);
	    }
	    
	    return remoteObject;
	}
	
	
	public static void fitLinearModel(CallbackRemote callee, String dataSetName, String formula, ArrayList columns, String family, String link) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();        	
	    
        if ("gaussian".equals(family) && "identity".equals(link)){
        	remoteObject.lm(clientid, taskCnt, formula,columns);
        	MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "lm(" + dataSetName + ")");
        }else{
        	remoteObject.glm(clientid, taskCnt, formula,columns,family,link);
        	MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "glm(" + dataSetName + ")");
        }   
        
        taskCnt++;
	}
	
	public static void getObjectSummary(CallbackRemote callee, String objName) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();      
		remoteObject.summary(clientid, taskCnt, objName);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "summary(" + objName + ")");
		taskCnt++;
	}
	
	public static void getGeneNames(CallbackRemote callee, String objName) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();   
		remoteObject.geneNames(clientid, taskCnt, objName);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "geneNames(" + objName + ")");
		taskCnt++;		
	}
	
	public static void getGeneIntensity(CallbackRemote callee, String objName, String geneName) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();   
		remoteObject.geneIntensity(clientid, taskCnt, objName, geneName);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "geneIntensity(" + objName + ")");
		taskCnt++;
	}
	
	public static void getGeneIntensityAsColumnByNames(CallbackRemote callee, String objName, ArrayList geneNames) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();   
		remoteObject.geneIntensityAsColumnByNames(clientid, taskCnt, objName, geneNames);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "geneIntensity(" + objName + ")");
		taskCnt++;
	}
	
	public static void getGeneIntensityAsRowByNames(CallbackRemote callee, String objName, ArrayList geneNames) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();   
		remoteObject.geneIntensityAsRowByNames(clientid, taskCnt, objName, geneNames);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "geneIntensity(" + objName + ")");
		taskCnt++;
	}
	
	public static void getGeneAnnotationByNames(CallbackRemote callee, String type, ArrayList geneNames) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();   
		remoteObject.geneAnnotationByNames(clientid, taskCnt, type, geneNames);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "geneAnnotation");
		taskCnt++;
	}
	
	public static void preprocess(CallbackRemote callee, String objName, String bgcorrect, String normalize, String pmcorrect, String summary) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();   
		remoteObject.preprocess(clientid, taskCnt, objName, bgcorrect, normalize, pmcorrect, summary);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "preprocess(" + objName + ")");
		taskCnt++;
	}
	
	public static void gcrma(CallbackRemote callee, String objName) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();  
		remoteObject.gcrma(clientid, taskCnt, objName);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "gcrma(" + objName + ")");
		taskCnt++;
	}
	
	public static void exonRma(CallbackRemote callee, String objName) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();  
		remoteObject.exonRma(clientid, taskCnt, objName);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "Rma(" + objName + ")");
		taskCnt++;
	}
	
	public static void exonGcRma(CallbackRemote callee, String objName) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();  
		remoteObject.exonGcRma(clientid, taskCnt, objName);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "GcRma(" + objName + ")");
		taskCnt++;
	}
	
	public static void mtp(CallbackRemote callee, String objName, String mtpName, String test, String typeone, int k, double q, double alpha,
			String method, String fdrMethod, boolean bootstrap, List yInputs, List yValues, List yIncluded, List filterfuns) throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer();  
		remoteObject.mtp(clientid, taskCnt, objName, mtpName, test, typeone, k, q, alpha, method, fdrMethod, bootstrap, yInputs, yValues, yIncluded, filterfuns);
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "mtp(" + objName + ")");
		taskCnt++;		
	}
	
	public static void deleteObjects(CallbackRemote callee, String[] objNames)  throws RemoteException
	{
		AsyncJRIServer remoteObject = getAsyncJRIServer(); 
		remoteObject.deleteObjects(clientid, taskCnt, Arrays.asList(objNames));
		MainPanel.getTaskPane().addTask(callee, clientid, taskCnt, "rm(" + objNames[0] + "...)");
		taskCnt++;		
	}
	
	public static Object pollTask(String clientid, int taskId) throws RemoteException
	{
		AsyncJRIServer remoteObject = null;		
		
		try{        	
        	remoteObject = (AsyncJRIServer)Naming.lookup(ServerURL);        	
        }
        catch (java.rmi.ConnectException exc){
        	throw new RemoteException("Connection refused to host " + HOST_NAME);
        }
        catch (java.net.MalformedURLException exc)
	    {
	        throw new RemoteException("Malformed URL " + ServerURL);
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	        throw new RemoteException("Cannot connect to service " + HOST_NAME);
	    }
	    catch (java.io.IOException exc)
	    {
	    	throw new RemoteException("Connection timed out to host " + HOST_NAME);
	    }
		
		return remoteObject.poll(clientid, taskId);	
	}
	
	

	public static void main(String[] args) {		
		
		//Create dataset
		ArrayList columns=new ArrayList();
		ArrayList column=new ArrayList();
		column.add(".M");
		column.add(Boolean.class);
		columns.add(column);
		for (int i=0; i<6; i++){
			column.add(Boolean.TRUE);
		}
		
		column=new ArrayList();
		column.add("y");
		column.add(Double.class);
		columns.add(column);
		decodeValues("2 3 5 10 23 12", column);
		
		column=new ArrayList();
		column.add("x");
		column.add(Double.class);
		columns.add(column);
		decodeValues("8 9 10 16 20 27", column);
		
		
	    try
	    {
	    	//set the security manager for the client
			//if (System.getSecurityManager() == null){
			//	System.setSecurityManager(new RMISecurityManager());
			//}
		    //get the remote object from the registry
			
	        //System.out.println("Security Manager loaded");
	        String url = "//" + HOST_NAME + "/" + SERVER_NAME + "0";
	        JRIServer remoteObject = (JRIServer)Naming.lookup(url);
	        System.out.println("Got remote object");
	        remoteObject = remoteObject.getIdleServer();
	        	
	        RLinearModel rlm = (GlmModel)remoteObject.glm("y~x",columns,"poisson","log");
	        //RLinearModel rlm = (RLinearModel)remoteObject.lm("y~x",columns);
	        
	        System.out.println("residuals");	        
	        double[] residuals = rlm.getResiduals();
	        for (int i=0; i < residuals.length; i++){
	        		System.out.print(residuals[i] + " ");
	        }
	        
	        System.out.println("\nfittedValues");
	        double[] fittedValues = rlm.getFittedValues();
	        for (int i=0; i < fittedValues.length; i++){
	        		System.out.print(fittedValues[i] + " ");
	        }
	        
	    }
	    catch (RemoteException exc)
	    {
	        System.out.println("Error in lookup: " + exc.toString());
	        exc.printStackTrace();
	    }
	    catch (java.net.MalformedURLException exc)
	    {
	        System.out.println("Malformed URL: " + exc.toString());
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	        System.out.println("NotBound: " + exc.toString());
	    }
	}
	
	private static void decodeValues(String values, ArrayList column){
		StringTokenizer tokenizer=new StringTokenizer(values, " ");
    		while(tokenizer.hasMoreTokens())
    		{
    			String value=tokenizer.nextToken().trim();
    			column.add(new Double(value));
    		}
	}
}





