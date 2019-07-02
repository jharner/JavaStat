package wvustat.swing;

import javax.swing.*;


public class IntegerField extends JTextField
{

    public IntegerField(String text)
    {
        super(text);
        setDocument(new IntegerFilterDocument());
    }

    public IntegerField(int columns)
    {
        super(columns);
        setDocument(new IntegerFilterDocument());
    }

    public IntegerField(String text, int columns)
    {
        super(text, columns);
        setDocument(new IntegerFilterDocument());
    }

    public int getIntValue()
    {
        try
        {
            return Integer.parseInt(getText());
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    public void setIntValue(int val)
    {
        setText(String.valueOf(val));
    }
}


/*--- Formatted in Sun Java Convention Style on on, dec 6, '00 ---*/


/*------ Formatted by Jindent 3.23 Basic 1.0 --- http://www.jindent.de ------*/
