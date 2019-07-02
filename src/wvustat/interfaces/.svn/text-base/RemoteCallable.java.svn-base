/*
 * RemoteCallable.java
 *
 * Created on October 25, 2001, 4:14 PM
 */

package wvustat.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * RemoteCallable defines an interface for objects whose methods can be invoked remotely.
 *
 * @author  Hengyi Xue
 * @version 
 */
public interface RemoteCallable extends Remote {
    
    /**
    * This method is used to group objects together for collaboration. Objects that returns the same
    * getCallId() will be grouped together to be synchronized.
    */
    public String getCallId() throws RemoteException;
    
    /**
    * Remotely invoke a method by passing the name of the method as a String and the arguments as 
    * an array of Objects.
    */
    public void methodInvoked(String methodName, Object[] args) throws RemoteException;
}
