package wvustat.network;

import java.rmi.Remote; 

public interface CallbackRemote extends Remote{
	
	public void callback(Object result);

}
