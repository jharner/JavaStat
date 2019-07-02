/*
 * Session.java
 *
 * Created on July 19, 2000, 2:25 PM
 */

package wvustat.interfaces;

/**
 * Session defines an interface for a remotely observable object.
 *
 * @author  Hengyi Xue
 * @version 1.0, July 19, 2000
 */
public interface Session{
    /**
     *	Add a remote observer to an object implementing this interface, @see RemoteObserver
     *
     *	@param obs the remote observer to be added
     */
    public void addRemoteObserver(RemoteObserver obs);

    /**
     *	Remove a remote observer from the implementing object
     *
     *	@param obs the remote observer to remove
     */
    public void removeRemoteObserver(RemoteObserver obs);
 

}
