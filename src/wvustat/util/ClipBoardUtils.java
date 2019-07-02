/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Feb 12, 2003
 * Time: 3:33:51 PM
 * To change this template use Options | File Templates.
 */
package wvustat.util;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class ClipBoardUtils
{
// If a string is on the system clipboard, this method returns it;
    // otherwise it returns null.
    public static String getClipboard()
    {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try
        {
            if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor))
            {
                String text = (String) t.getTransferData(DataFlavor.stringFlavor);
                return text;
            }
        }
        catch (UnsupportedFlavorException e)
        {
        }
        catch (IOException e)
        {
        }
        return null;
    }

    // This method writes a string to the system clipboard.
    // otherwise it returns null.
    public static void setClipboard(String str)
    {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

}
