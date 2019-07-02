package wvustat.swing;

import javax.swing.*;

public class DoubleField extends JTextField
{

    public DoubleField(String text)
    {
        super(text);
        setDocument(new DoubleFilterDocument());
    }

    public DoubleField(int columns)
    {
        super(columns);
        setDocument(new DoubleFilterDocument());
    }

    public DoubleField(String text, int columns)
    {
        super(text, columns);
        setDocument(new DoubleFilterDocument());
    }



    public String getText()
    {
      return super.getText();
    }
}
