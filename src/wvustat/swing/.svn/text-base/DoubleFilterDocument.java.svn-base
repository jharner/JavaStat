package wvustat.swing;



import java.awt.*;
import javax.swing.text.*;


public class DoubleFilterDocument extends PlainDocument
{

    private StringBuffer scratchBuffer;

    public DoubleFilterDocument()
    {
        scratchBuffer = new StringBuffer();
    }

    public void insertString(int offset, String text, AttributeSet aset) throws BadLocationException
    {

        if (text == null)
        {
            return;
        }
        scratchBuffer.setLength(0);
        try
        {
            scratchBuffer.append(this.getText(0, getLength()));
            scratchBuffer.insert(offset, text);

            // Kludge: Append a 0 so that leading decimal points
            // and signs will be accepted
            scratchBuffer.append('0');
        }
        catch (BadLocationException ble)
        {
            ble.printStackTrace();
            return;
        }
        catch (StringIndexOutOfBoundsException sioobe)
        {
            sioobe.printStackTrace();
            return;
        }
        try
        {
            Double.parseDouble(scratchBuffer.toString());
        }
        catch (NumberFormatException nfe)
        {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        super.insertString(offset, text, aset);
    }
}


/*--- Formatted in Sun Java Convention Style on on, dec 6, '00 ---*/


/*------ Formatted by Jindent 3.23 Basic 1.0 --- http://www.jindent.de ------*/
