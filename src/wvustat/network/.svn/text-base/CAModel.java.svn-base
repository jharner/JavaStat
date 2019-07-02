package wvustat.network;

import java.io.*;

public class CAModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public transient double[][] U; //n*d
	public transient double[][] A; //p*d
	public transient double[] D; //1*d
	
	public transient double[][] G; //n*d
	public transient double[][] H; //p*d
	
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		
		int n = U.length;
		int p = A.length;
		int d = D.length;
		
		stream.writeInt(n);
		stream.writeInt(p);
		stream.writeInt(d);
		
		for (int i=0; i<n; i++)
			for (int j=0; j<d; j++)
				stream.writeDouble(U[i][j]);
		
		for (int i=0; i<p; i++)
			for (int j=0; j<d; j++)
				stream.writeDouble(A[i][j]);
		
		for (int i=0; i<d; i++)
			stream.writeDouble(D[i]);
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
	
		int n = in.readInt();
		int p = in.readInt();
		int d = in.readInt();
		
		U = new double[n][d];
		for (int i=0; i<n; i++)
			for (int j=0; j<d; j++)
				U[i][j] = in.readDouble();
		
		A = new double[p][d];
		for (int i=0; i<p; i++)
			for (int j=0; j<d; j++)
				A[i][j] = in.readDouble();
		
		D = new double[d];
		for (int i=0; i<d; i++)
			D[i] = in.readDouble();
	}
}
