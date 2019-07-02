package wvustat.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Apr 3, 2005
 * Time: 3:46:44 PM
 * To change this template use Options | File Templates.
 */
public class DelegateAction extends AbstractAction
{
    private ActionListener l;

    public DelegateAction(String name, Icon icon, ActionListener l)
    {
        super(name, icon);
        this.l=l;
    }

    public void actionPerformed(ActionEvent event)
    {
        if(l!=null)
            l.actionPerformed(event);
    }
}
