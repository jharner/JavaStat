package wvustat.swing;

import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * In Java up to JDK1.4.2, a single mouse click event is alwasy
 * sent before a double click event. The purpose of this class is to
 * filter out single mouse click
 * events sent before a double click event is sent. The purpose is to achieve
 *  distinct behaviors for single clicks and double clicks.
 */
public class DelayedDispatch extends Thread
{
    private int sleepTime = 300;
    public boolean isDoubleClick = false;
    private MouseEvent evt;
    private DelayedDispatcher dispatcher;

    public DelayedDispatch(MouseEvent evt, DelayedDispatcher comp)
    {
        this.evt = evt;
        this.dispatcher = comp;
    }

    public void run()
    {
        try
        {
            sleep(sleepTime);
        }
        catch (InterruptedException e)
        {

        }

        if (isDoubleClick == false)
        {
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    dispatcher.respondToSingleClick(evt);
                }
            };

            try
            {
                SwingUtilities.invokeAndWait(runnable);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

}
