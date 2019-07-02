package wvustat.util;

import java.io.*;
import java.util.Vector;
import java.rmi.RemoteException;

import wvustat.network.client.JRIClient;
import wvustat.network.RawFileData;

public class RawFileUploader {
	
	private String ext = "CEL";  //file extension
	
	public RawFileUploader(){
	}
		
	public File[] matchedFiles(File path){
		if (path.isFile()) {
			return new File[] {path};
		}
			
		File[] files = path.listFiles();
		
		if (files == null)
			return new File[0];
		
		Vector v = new Vector();
		for (int i = 0; i < files.length; i++){
			File f = files[i];
			String name = f.getName();
			if (f.isFile() && name.toUpperCase().endsWith("." + ext)) {
				v.addElement(f);				
			}
		}
		
		File[] ret = new File[v.size()];
		for (int i = 0; i < v.size(); i++)
			ret[i] = (File)v.elementAt(i);
		
		return ret;
	}
	
	/**
	 * file: file to upload
	 * pathname: folder name on the server under user's account
	 */
	public void upload(File file, String pathname) throws IOException, RemoteException{
		FileInputStream in = null;
		byte[] bytes;
		
		try {
			in = new FileInputStream(file);
			int len = in.available();
			bytes = new byte[len];
			in.read(bytes);
			JRIClient.uploadFile(bytes, pathname, file.getName());
			
		} finally {
			if (in != null)
				in.close();
		}
	}
	
	public void download(String path, String save2path) throws IOException, RemoteException {
		RawFileData data = JRIClient.downloadFile(path);
		
		FileOutputStream out = null;
		byte[] bytes = data.getBytes();
		
		try {
			out = new FileOutputStream(save2path);
			out.write(bytes);			
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException ex) {}
		}
	}

}
