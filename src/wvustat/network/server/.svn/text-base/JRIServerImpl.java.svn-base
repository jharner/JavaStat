package wvustat.network.server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.nio.channels.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import wvustat.network.*;

/**
 * @author dajieluo
 *
 */
public class JRIServerImpl extends UnicastRemoteObject implements JRIServer{
	
	public static String HOST_NAME = "stat.wvu.edu";
	public static final String SERVER_NAME = "JRISERVER";
	public static final String CONFIG_FILE = "server.prop";
	public static final String USER_FILE = "user.prop";
	public static final String JRI_HOME = "JRI_HOME";
	public static boolean debug = false;
	public static boolean busy = false;
	public static String STORAGE_PATH = "";
	
	private RInstance ri;
	private int serverid;
	private static JRIServerImpl Server; //keep reference of Server to avoid garbage collection
	
	JRIServerImpl(int serverid) throws RemoteException{
		super();
		
		ri=new RInstance();
		ri.setDebug(debug);
		ri.setStoragePath(STORAGE_PATH);
		System.out.println("Rengine " + serverid + " created, waiting for R. " + new Date());
		
		this.serverid = serverid;
	}
	
	public boolean isBusy() throws RemoteException{
		return busy;
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
	
	public JRIServer getIdleServer() throws RemoteException{
		try{
			String[] names = Naming.list("//" + HOST_NAME + "/");
			names = substrMatches(names, "/" + SERVER_NAME);
			
			if(debug) 
				for (int i = 0; i<names.length; i++)
					System.out.println(names[i]);
		
			JRIServer remoteObject;
			
			if (names.length == 0) throw new RemoteException("No server alive"); 
			
			if (names.length == 1){
				remoteObject = (JRIServer)Naming.lookup(names[0]);
				return remoteObject;
			}
			
			int k = (int)Math.round(Math.random() * (names.length - 1));
			for (int i=k, j=0; j==0 || i!=k; i= (i + 1) % names.length){
				if(i==k) j++;
				remoteObject = (JRIServer)Naming.lookup(names[i]);
				if (!remoteObject.isBusy()) return remoteObject;
			}
			
			remoteObject = (JRIServer)Naming.lookup(names[k]);
			return remoteObject;
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
	
	public synchronized Object lm(String formula, ArrayList columns) throws RemoteException{
		busy = true;
		Object obj = ri.lm(formula, columns);
		busy = false;
		
		return obj;
	}
	
	public Object hclust(ArrayList columns, String method) throws RemoteException {
		busy = true;
		Object obj = ri.hclust(columns, method);
		busy = false;
		
		return obj;
	}
	
	public Object pca(ArrayList y, ArrayList z) throws RemoteException {
		busy = true;
		Object obj = ri.pca(y, z);
		busy = false;
		
		return obj;
	}
	
	public Object ca(ArrayList y) throws RemoteException {
		busy = true;
		Object obj = ri.ca(y);
		busy = false;
		
		return obj;
	}
	
	public synchronized Object glm(String formula, ArrayList columns, String family, String link) throws RemoteException{
		busy = true;
		Object obj = ri.glm(formula, columns, family, link); 
		busy = false;
		
		return obj;
	}
	
	public synchronized void uploadFile(String clientid, RawFileData data, String pathname) throws RemoteException{
		busy = true;
		
		FileOutputStream out = null;
		byte[] bytes = data.getBytes();
		String filename = data.getFileName();
		
		
		int i = clientid.indexOf(RInstance.SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		
		File dir = new File(STORAGE_PATH + s);
		if (dir.exists()) {
		    if (!dir.isDirectory()) 
		    	throw new RemoteException("Cannot create user folder on server");
		} else {
			if (!dir.mkdir())
				throw new RemoteException("Cannot create user folder on server");
		}
		
		dir = new File(STORAGE_PATH + s + File.separator + pathname);
		if (dir.exists()) {
		    if (!dir.isDirectory()) 
		    	throw new RemoteException("Cannot create folder " + pathname + " on server");
		} else {
			if (!dir.mkdir())
				throw new RemoteException("Cannot create folder " + pathname + " on server");
		}
		
		try {
			out = new FileOutputStream(STORAGE_PATH + s + File.separator + pathname + File.separator + filename);
			out.write(bytes);			
		
		} catch (IOException ex) {
			throw new RemoteException(ex.getMessage());
		
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException ex) {}
		}
		
		busy = false;		
	}
	
	public synchronized RawFileData downloadFile(String clientid, String path) throws RemoteException {
		busy = true;
		
		File file = new File(path);
		if (!file.exists())
			throw new RemoteException("Cannot locate file on server");
		
		FileInputStream in = null;
		byte[] bytes;
		
		try {
			in = new FileInputStream(file);
			int len = in.available();
			bytes = new byte[len];
			in.read(bytes);
		}
		catch (IOException ex) {
			throw new RemoteException(ex.getMessage());
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException ex) {}
		}
		
		RawFileData data = new RawFileData(bytes, file.getName());
		
		busy = false;
		
		return data;
	}
	
	public synchronized void deleteFolder(String clientid, String pathname) throws RemoteException{
		busy = true;
		
		int i = clientid.indexOf(RInstance.SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);
		File dir = new File(STORAGE_PATH + s + File.separator + pathname);
		
		if (dir.exists() && dir.isDirectory()) {
			/*
			File[] files = dir.listFiles();
			for (int j = 0; j < files.length; j++) {
				if (!files[j].delete())
					throw new RemoteException("Cannot create folder " + pathname + " on server");
			}
			if (!dir.delete())
				throw new RemoteException("Cannot create folder " + pathname + " on server");
			*/
			throw new RemoteException("The object " + pathname + " already exists on server. Cannot create the folder.");
		}
		
		busy = false;
	}
	
	public synchronized void importFiles(String clientid, String pathname, String objName) throws RemoteException{
		busy = true;
		ri.importFiles(clientid, pathname, objName);
		busy = false;
	}
	
	public synchronized void importTxtFile(String clientid, String pathname, String filename, String objName) throws RemoteException {
		busy = true;
		ri.importTxtFile(clientid, pathname, filename, objName);
		busy = false;
	}
	
	public synchronized void importExonFiles(String clientid, String pathname, String chipType, String objName) throws RemoteException {
		busy = true;
		
		int i = clientid.indexOf(RInstance.SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);		
		
		File srcFD = new File(System.getenv(JRI_HOME) + File.separator + "resources");
		File dstFD = new File(STORAGE_PATH + s + File.separator + pathname + File.separator + 
						"annotationData" + File.separator + "chipTypes" + File.separator + 
						chipType);
		if (!dstFD.mkdirs())
			throw new RemoteException("Cannot create cdf folder on server.");
		
		File src, dst;
		try {
			src = new File(srcFD.getPath() + File.separator + chipType + ".cdf");
			dst = new File(dstFD.getPath() + File.separator + chipType + ".cdf");
			copyFile(src, dst);
			src = new File(srcFD.getPath() + File.separator + chipType + ".probe.tab");
			dst = new File(dstFD.getPath() + File.separator + chipType + ".probe.tab");
			copyFile(src, dst);
		} catch (IOException ioex) {
			throw new RemoteException("Cannot copy cdf file on server " + ioex.getMessage());
		}
		
		dst = new File(STORAGE_PATH + s + File.separator + pathname + File.separator + 
				"rawData" + File.separator + objName + File.separator + chipType);
		if (!dst.mkdirs())
			throw new RemoteException("Cannot create data folder on server.");
		
		src = new File(STORAGE_PATH + s + File.separator + pathname);
		File[] files = src.listFiles();
		for (int k = 0; k < files.length; k++){
			File f = files[k];
			if (!f.isFile()) continue;
			String name = f.getName();
			File f2 = new File(dst.getPath() + File.separator + name);
			f.renameTo(f2);
		}
		
		ri.importExonFiles(clientid, pathname, chipType, objName);
		busy = false;
	}
	
	public List listFilesOfObject(String clientid, String objName) throws RemoteException {
		
		int i = clientid.indexOf(RInstance.SEPARATOR);
		String s = i < 0 ? clientid : clientid.substring(0, i);	
		
		// Get directory
		String rawObject = objName.substring(0, objName.indexOf('.'));
		String folderName = objName.replace('.', ',');
		File file = new File(STORAGE_PATH + s + File.separator + rawObject + File.separator + "probeData" + File.separator + folderName);
		if (!file.exists() || file.isFile())
			throw new RemoteException("Folder of this object does not exist on server.");
		
		// Search all CEL files in sub folders
		ArrayList pathes = new ArrayList();
		
		File[] cdfFolders = file.listFiles();
		for (i = 0; i < cdfFolders.length; i++) {
			if (cdfFolders[i].isFile())
				continue;
		
			File[] celFiles = cdfFolders[i].listFiles();
				
			for (int j = 0; j < celFiles.length; j++) {
				File f = celFiles[j];
				String name = f.getPath();
				if (f.isFile() && name.toUpperCase().endsWith(".CEL")) 
					pathes.add(name);	
			}
		}	
		
		if (pathes.size() == 0)
			throw new RemoteException("Cannot find any CEL files for this object on server.");
		
		return pathes;
	}
	
	public synchronized ArrayList listObjects(String clientid) throws RemoteException{
		busy = true;
		
		String[] str = ri.listObjects(clientid);
		ArrayList lst = new ArrayList();
		for (int i = 0; i < str.length; i++)
			lst.add(str[i]);
		
		busy = false;
		
		return lst;
	}
	
	public synchronized void userLogon(String username, String password) throws RemoteException{
		busy = true;
		
		Properties prop = new Properties();
		try{
			if ((new File(USER_FILE)).exists())
				prop.load(new FileInputStream(USER_FILE));
			else
				prop.load(new FileInputStream(System.getenv(JRI_HOME) + File.separator + USER_FILE));
		}
		catch(IOException ex)
		{
			System.out.println("Fail to load config file: " + ex.getMessage());
			throw new RemoteException("Fail to load user list on server");			
		}
		
		String pwd = prop.getProperty(username);
		if (pwd == null)
			throw new RemoteException("User not found");
		else if (!pwd.equals(password))
			throw new RemoteException("Password not correct");
		
		busy = false;
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
	    			System.out.println("Usage: wvustat.network.server.JRIServerImpl [start|stop] [SERVERID]");
	    			return;
	    		}
	    		
	    		if (!"start".equals(args[0]) && !"stop".equals(args[0])){
	    			System.out.println("Usage: wvustat.network.server.JRIServerImpl [start|stop] [SERVERID]");
	    			return;
	    		}
	    			
	    		if ("start".equals(args[0])){
	    			if (args.length == 1){
		    			System.out.println("Usage: wvustat.network.server.JRIServerImpl [start|stop] [SERVERID]");
		    			System.out.println("SERVERID is empty");
		    			return;
		    		}
	    			int serverid = Integer.parseInt(args[1]);
	    		
	    			if (System.getSecurityManager() == null){
	    				System.setSecurityManager(new RMISecurityManager());
	    			}

	    			
	    			RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory(1098));
	    			
	    			//create a local instance of the object
	    			Server = new JRIServerImpl(serverid);
	    			
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
	    			
	    			JRIServer remoteObject;
	    			for (int i=0; i < names.length; i++){
	    				if(debug) System.out.println(names[i]);
	    				
	    				remoteObject = (JRIServer)Naming.lookup(names[i]);
	    				remoteObject.shutdown();
	    			}
	    		}
	    }
	    catch (NumberFormatException nfe)
		{
	    		System.out.println("Usage: wvustat.network.server.JRIServerImpl [start|stop] [SERVERID]");
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
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		 if(!destFile.exists()) {
			 destFile.createNewFile();
		 }
	
		 FileChannel source = null;
		 FileChannel destination = null;
		 try {
			 source = new FileInputStream(sourceFile).getChannel();
			 destination = new FileOutputStream(destFile).getChannel();
			 destination.transferFrom(source, 0, source.size());
		 }
		 finally {
			 if(source != null) {
				 source.close();
			 }
			 if(destination != null) {
				 destination.close();
			 }
		}
	}
	
	public static boolean delete(File resource) throws IOException {
		if (!resource.exists()) return true;
		
		if (resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for(int i = 0; i < childFiles.length; i++) {
				File child = childFiles[i];
				delete(child);
			}			
		}
		return resource.delete();
	}
}
