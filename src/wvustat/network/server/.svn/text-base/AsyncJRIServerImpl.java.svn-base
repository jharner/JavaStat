package wvustat.network.server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import wvustat.network.*;


public class AsyncJRIServerImpl extends UnicastRemoteObject implements AsyncJRIServer{
	
	public static String HOST_NAME = "stat.wvu.edu";
	public static final String SERVER_NAME = "ASYNCJRISERVER";
	public static final String CONFIG_FILE = "server.prop";
	public static final String JRI_HOME = "JRI_HOME";
	public static boolean debug = false;
	public static String STORAGE_PATH = "";
	
	 
	public static MsgQueue reqMsgQueue = new MsgQueue(100);
	public static IndexedQueue taskQueue = new IndexedQueue();
	
	private static RInstance ri;
	private int serverid;
	private static AsyncJRIServerImpl Server; //keep reference of Server to avoid garbage collection
	
	AsyncJRIServerImpl(int serverid) throws RemoteException{
		super();
		
		ri=new RInstance();
		ri.setDebug(debug);
		ri.setStoragePath(STORAGE_PATH);
		System.out.println("Rengine " + serverid + " created, waiting for R. " + new Date());
		
		this.serverid = serverid;
		
		new Thread(new ServerThread()).start();
	}
	
	public boolean isBusy() throws RemoteException{
		return !reqMsgQueue.isEmpty();
	}
	
	public boolean isFull() {
		return reqMsgQueue.isFull();
	}
	
	public static RInstance getRInstance(){
		return ri;
	}
	
	public void shutdown() throws RemoteException{
		try{
			Naming.unbind("//" + HOST_NAME + "/" + SERVER_NAME + serverid);
		}
		catch (java.net.MalformedURLException exc)
	    {
	        throw new RemoteException("Malformed URL: " + exc.toString());
	    }
		catch (java.rmi.NotBoundException exc)
	    {
	        throw new RemoteException("NotBound: " + exc.toString());
	    }
		
		reqMsgQueue.clear();
		taskQueue.clear();
		ri.end();
		System.out.println("shutdown! R server: " +  serverid + " " + new Date());
		
		Runnable runnable = new Runnable()
		{
            public void run()
            {
            	if(debug) System.out.println("wait 1 second");
            	try
		        {
		            Thread.sleep(1000);
		        }
		        catch (InterruptedException e)
		        {}
		        System.exit(0);
            }
        };
        
        Thread thread = new Thread(runnable);
        thread.start();
		
	}
	
	public String getIdleServer() throws RemoteException{
		try{
			String[] names = Naming.list("//" + HOST_NAME + "/");
			names = substrMatches(names, "/" + SERVER_NAME);
			
			if(debug) 
				for (int i = 0; i<names.length; i++)
					System.out.println(names[i]);
		
			AsyncJRIServer remoteObject;
			
			if (names.length == 0) throw new RemoteException("No server alive"); 
			
			if (names.length == 1){
				//remoteObject = (AsyncJRIServer)Naming.lookup(names[0]);
				//if (remoteObject.isBusy()) throw new RemoteException("Server is busy. Try again later.");
				return names[0];
			}
			
			int k = (int)Math.round(Math.random() * (names.length - 1));
			for (int i=k, j=0; j==0 || i!=k; i= (i + 1) % names.length){
				if(i==k) j++;
				remoteObject = (AsyncJRIServer)Naming.lookup(names[i]);
				if (!remoteObject.isBusy()) return names[i];
			}
			
			//throw new RemoteException("Server is busy. Try again later.");
			//remoteObject = (AsyncJRIServer)Naming.lookup(names[k]);
			return names[k];
			
		}
		catch (java.net.MalformedURLException exc)
	    {
	        throw new RemoteException("Malformed URL: " + exc.toString());
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	        throw new RemoteException("NotBound: " + exc.toString());
	    }
	    
	}
	
	public synchronized void lm(String clientid, int taskId, String formula, ArrayList columns) throws RemoteException{
		
		if (debug) System.out.println("lm() on server is called");
		
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("lm");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(formula);
		v.addElement(columns);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void glm(String clientid, int taskId, String formula, ArrayList columns, String family, String link) throws RemoteException{
		
		if (debug) System.out.println("glm() on server is called");
		
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("glm");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(formula);
		v.addElement(columns);
		v.addElement(family);
		v.addElement(link);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void summary(String clientid, int taskId, String objName) throws RemoteException{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("summary");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void geneNames(String clientid, int taskId, String objName) throws RemoteException{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("geneNames");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void geneIntensity(String clientid, int taskId, String objName, String geneName) throws RemoteException{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("geneIntensity");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		v.addElement(geneName);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public void geneIntensityAsColumnByNames(String clientid, int taskId, String objName, ArrayList geneNames) throws RemoteException {
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("geneIntensityAsColumnByNames");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		v.addElement(geneNames);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public void geneIntensityAsRowByNames(String clientid, int taskId, String objName, ArrayList geneNames) throws RemoteException {
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("geneIntensityAsRowByNames");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		v.addElement(geneNames);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public void geneAnnotationByNames(String clientid, int taskId, String type, ArrayList geneNames) throws RemoteException {
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("geneAnnotationByNames");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(type);
		v.addElement(geneNames);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void preprocess(String clientid, int taskId, String objName, String bgcorrect, String normalize, String pmcorrect, String summary) throws RemoteException
	{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("preprocess");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		v.addElement(bgcorrect);
		v.addElement(normalize);
		v.addElement(pmcorrect);
		v.addElement(summary);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void gcrma(String clientid, int taskId, String objName) throws RemoteException
	{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("gcrma");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void exonRma(String clientid, int taskId, String objName) throws RemoteException
	{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("exonRma");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void exonGcRma(String clientid, int taskId, String objName) throws RemoteException
	{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("exonGcRma");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	public synchronized void mtp(String clientid, int taskId, String objName, String mtpName, String test, String typeone, int k, double q, double alpha,
			String method, String fdrMethod, boolean bootstrap, List yInputs, List yValues, List yIncluded, List filterfuns) throws RemoteException
	{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		
		Vector v = new Vector();
		v.addElement("mtp");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objName);
		v.addElement(mtpName);
		v.addElement(test);
		v.addElement(typeone);
		v.addElement(new Integer(k));
		v.addElement(new Double(q));
		v.addElement(new Double(alpha));
		v.addElement(method);
		v.addElement(fdrMethod);
		v.addElement(new Boolean(bootstrap));
		v.addElement(yInputs);
		v.addElement(yValues);
		v.addElement(yIncluded);
		v.addElement(filterfuns);
		
		reqMsgQueue.addMsg(v);
		
		return;		
	}
	
	public synchronized void deleteObjects(String clientid, int taskId, List objNames) throws RemoteException
	{
		if (isFull()) throw new RemoteException("Server is busy. Try again later.");
		Vector v = new Vector();
		v.addElement("rm");
		v.addElement(clientid);
		v.addElement(new Integer(taskId));
		v.addElement(objNames);
		
		reqMsgQueue.addMsg(v);
		
		return;
	}
	
	
	
	public synchronized Object poll(String clientid, int taskId) throws RemoteException {
		
		if (debug) System.out.println("polled" + clientid + " " + taskId + " " + new Date());
		
		if (taskQueue.contains(clientid, taskId))
			return taskQueue.getMsg(clientid, taskId);
		else
			return null;
	}

	
	private static void loadProperties(){
		Properties prop = new Properties();
		try{
			if ((new File(CONFIG_FILE)).exists())
				prop.load(new FileInputStream(CONFIG_FILE));
			else
				prop.load(new FileInputStream(System.getenv(JRI_HOME) + File.separator + CONFIG_FILE));
			
		}catch(IOException ex)
		{
			System.out.println("Fail to load config file: " + ex.getMessage());
		}
		HOST_NAME = prop.getProperty("hostname", HOST_NAME);
		if (prop.getProperty("debug", "false").equals("true")) debug = true;
		STORAGE_PATH = prop.getProperty("storage_path");
		if (STORAGE_PATH == null)
			STORAGE_PATH = "";
		else
			STORAGE_PATH += File.separator;
	}
	
	public static void main(String[] args) {
		
		//set the security manager
	    try
	    {
	    		loadProperties();
	    		
	    		if (args.length == 0){
	    			System.out.println("Usage: wvustat.network.server.AsyncJRIServerImpl [start|stop] [SERVERID]");
	    			return;
	    		}
	    		
	    		if (!"start".equals(args[0]) && !"stop".equals(args[0])){
	    			System.out.println("Usage: wvustat.network.server.AsyncJRIServerImpl [start|stop] [SERVERID]");
	    			return;
	    		}
	    			
	    		if ("start".equals(args[0])){
	    			if (args.length == 1){
		    			System.out.println("Usage: wvustat.network.server.AsyncJRIServerImpl [start|stop] [SERVERID]");
		    			System.out.println("SERVERID is empty");
		    			return;
		    		}
	    			int serverid = Integer.parseInt(args[1]);
	    		
	    			if (System.getSecurityManager() == null){
	    				System.setSecurityManager(new RMISecurityManager());
	    			}

	    			RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory(1097));
	    			
	    			//create a local instance of the object
	    			Server = new AsyncJRIServerImpl(serverid);

	    			//Registry reg = LocateRegistry.createRegistry(1099);
	    			//UnicastRemoteObject.exportObject(Server);
	    			
	    			//put the local instance in the registry
	    			String url = "//" + HOST_NAME + "/" + SERVER_NAME + serverid;
	    			Naming.rebind(url, Server);

	    			System.out.println("Server " + serverid + " waiting.....");
	    		}
	    		else if("stop".equals(args[0])){
	    			String[] names = Naming.list("//" + HOST_NAME + "/");
	    			names = substrMatches(names, "/" + SERVER_NAME);
	    			
	    			AsyncJRIServer remoteObject;
	    			for (int i=0; i < names.length; i++){
	    				if(debug) System.out.println(names[i]);
	    				
	    				remoteObject = (AsyncJRIServer)Naming.lookup(names[i]);
	    				remoteObject.shutdown();
	    			}
	    		}
	    }
	    catch (NumberFormatException nfe)
		{
	    		System.out.println("Usage: wvustat.network.server.AsyncJRIServerImpl [start|stop] [SERVERID]");
	    		System.out.println("SERVERID is not a number: " + nfe.getMessage());
		}
	    catch (java.net.MalformedURLException me)
	    {
	         System.out.println("Malformed URL: " + me.toString());
	    }
	    catch (java.rmi.NotBoundException exc)
	    {
	         System.out.println("NotBound: " + exc.toString());
	    }
	    catch (RemoteException re)
	    {
	         System.out.println("Remote exception: " + re.toString());
	         re.printStackTrace();
	    }
	    catch (IOException ioex)
	    {
	    	ioex.printStackTrace();
	    }
		
	}
	
	public static String[] substrMatches(String[] strs, String substr){
		Vector v = new Vector();
		for (int i = 0; i < strs.length; i++)
			if (strs[i].indexOf(substr) >= 0)
				v.addElement(strs[i]);
		
		String[] s = new String[v.size()];
		for (int i = 0; i < s.length; i++)
			s[i] = (String)v.elementAt(i);
		
		return s;
	}

}
