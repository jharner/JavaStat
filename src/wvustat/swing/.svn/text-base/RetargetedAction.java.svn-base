package wvustat.swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 25, 2003
 * Time: 10:16:45 AM
 * To change this template use Options | File Templates.
 */
public class RetargetedAction extends AbstractAction
{
    private ActionListener l;

    public RetargetedAction(String name, String actionCommand, ActionListener l)
    {
        super();
        putValue(Action.NAME, name);
        putValue(Action.ACTION_COMMAND_KEY, actionCommand);
        this.l=l;
    }

    public void actionPerformed(ActionEvent evt)
    {
        if (l != null)
        {
            ActionEvent e;
            e = new ActionEvent(evt.getSource(), evt.getID(), (String) getValue(Action.ACTION_COMMAND_KEY));
            l.actionPerformed(e);
        }
    }
}
