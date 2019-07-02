package wvustat.network;

import java.io.*;

public class PCAModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public transient double[][] U; //n*p
	public transient double[][] A; //p*p
	public transient double[] d; //1*p
	
	public transient double[][] G; //p*n
	public transient double[][] H; //p*p
	public transient double[][] HI; //p*p, H inverse
	
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		
		int n = U.length;
		int p = A.length;
		
		stream.writeInt(n);
		stream.writeInt(p);
		
		for (int i=0; i<n; i++)
			for (int j=0; j<p; j++)
				stream.writeDouble(U[i][j]);
		
		for (int i=0; i<p; i++)
			for (int j=0; j<p; j++)
				stream.writeDouble(A[i][j]);
		
		for (int i=0; i<p; i++)
			stream.writeDouble(d[i]);
	}
	
	private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
	
		int n = in.readInt();
		int p = in.readInt();
		
		U = new double[n][p];
		for (int i=0; i<n; i++)
			for (int j=0; j<p; j++)
				U[i][j] = in.readDouble();
		
		A = new double[p][p];
		for (int i=0; i<p; i++)
			for (int j=0; j<p; j++)
				A[i][j] = in.readDouble();
		
		d = new double[p];
		for (int i=0; i<p; i++)
			d[i] = in.readDouble();
	}
}
