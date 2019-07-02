/*
 * SyncDirector.java
 *
 * Created on October 25, 2001, 4:10 PM
 */

package wvustat.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * SyncDirector defines an interface for registering and managing RemoteCallable (@see RemoteCallable) objects. The purpose is
 * make the behavior of a group of objects synchronized among multiple platforms. This is essential for a 
 * high level of collaboration required in JavaStat.
 *
 * @author  Hengyi Xue
 * @version 
 */
public interface SyncDirector extends Remote {

    public void register(RemoteCallable obj) throws RemoteException;
    
    public void unregister(RemoteCallable obj) throws RemoteException;
    
    public void remoteInvoke(String commonId, String methodName, Object[] args) throws RemoteException;

}