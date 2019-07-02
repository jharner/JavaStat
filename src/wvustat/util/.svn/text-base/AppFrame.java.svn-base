/*
 * AppFrame.java
 *
 * Created on March 20, 2002, 9:48 AM
 */

package wvustat.util;

import javax.swing.*;
/**
 * AppFrame is a subclass of JFrame. The closing of an AppFrame will cause the JVM to exit.
 *
 * @author  Hengyi Xue
 * @version 
 */
public class AppFrame extends JFrame {

    public AppFrame(){
        this.enableEvents(java.awt.AWTEvent.WINDOW_EVENT_MASK);
    }
    
    public AppFrame(String title){
        super(title);
        this.enableEvents(java.awt.AWTEvent.WINDOW_EVENT_MASK);
    }
    
    protected void processWindowEvent(java.awt.event.WindowEvent evt){
        super.processWindowEvent(evt);
        
        if(evt.getID()==java.awt.event.WindowEvent.WINDOW_CLOSING){
            System.exit(0);
        }
    }

}