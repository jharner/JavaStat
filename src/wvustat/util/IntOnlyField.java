/*
 * IntOnlyField.java
 *
 * Created on April 18, 2002, 2:12 PM
 */

package wvustat.util;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
/**
 *
 * @author  James Harner
 * @version 
 */
public class IntOnlyField extends javax.swing.JTextField {

    /** Creates new IntOnlyField */
    public IntOnlyField(int columns) {
        super(columns);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
    }
    
    public IntOnlyField(String text){
        super(text);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
    }
    
    public IntOnlyField(String text, int columns){
        super(text, columns);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
    }

    protected void processKeyEvent(java.awt.event.KeyEvent keyEvent) {
        int id=keyEvent.getID();
        
        switch(id){
            case KeyEvent.KEY_TYPED:
                char ch=keyEvent.getKeyChar();
                
                if(Character.isDigit(ch)){
                    super.processKeyEvent(keyEvent);
                }
                else if((int)ch==KeyEvent.VK_BACK_SPACE){
                    super.processKeyEvent(keyEvent);
                }
                
                break;
            default:
                super.processKeyEvent(keyEvent);
                break;
        }
    }
    
}
