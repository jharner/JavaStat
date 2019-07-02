package wvustat.network.client;

import java.io.IOException;
import java.net.*;
import java.rmi.server.RMISocketFactory;

public class TimeoutFactory extends RMISocketFactory {
	
	private int cTimeout, rTimeout;
	
	public TimeoutFactory(int connectTimeout, int readTimeout) {
		this.cTimeout = connectTimeout;
		this.rTimeout = readTimeout;
	}

	public Socket createSocket(String host, int port) throws IOException {
		Socket s = new Socket();
        s.connect(new InetSocketAddress(host,port), cTimeout * 1000);
		s.setSoTimeout(rTimeout * 1000);
		return s;
	}
	
	public ServerSocket createServerSocket(int port) throws IOException {
		//port = (port == 0 ? 1098 : port);
		return getDefaultSocketFactory().createServerSocket(port);
	}
	
	public int hashCode() {
		int hashcode = (getClass().toString() + Integer.toString(cTimeout) + "|" + Integer.toString(rTimeout)).hashCode(); 
		return hashcode;
	}
	
	public boolean equals(Object object) {
		if (object != null && object.getClass().equals(this.getClass()) &&
			cTimeout == ((TimeoutFactory)object).cTimeout && rTimeout == ((TimeoutFactory)object).rTimeout) 
			return true;
		else 
			return false;
	}
}
