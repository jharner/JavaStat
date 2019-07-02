package wvustat.network;

import java.io.*;

public class RawFileData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected transient byte[] bytes;
	protected String filename;
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		stream.writeInt(bytes.length);
		stream.write(bytes);
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		
		int size = in.readInt();
		bytes = new byte[size];
		in.readFully(bytes);
	}
	
	public RawFileData(){}
	
	public RawFileData(byte[] b, String filename){
		setBytes(b);
		setFileName(filename);
	}
	
	public byte[] getBytes(){
		return bytes;
	}
	
	public void setBytes(byte[] b){
		this.bytes = b;
	}
	
	public String getFileName(){
		return filename;
	}
	
	public void setFileName(String filename){
		this.filename = filename;
	}

}
