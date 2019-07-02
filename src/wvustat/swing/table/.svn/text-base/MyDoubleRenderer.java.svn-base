/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Nov 30, 2002
 * Time: 10:53:33 AM
 * To change this template use Options | File Templates.
 */
package wvustat.swing.table;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.text.NumberFormat;

public class MyDoubleRenderer extends DefaultTableCellRenderer
{
    private int fractionDigits;
    private NumberFormat formatter;

    public MyDoubleRenderer(int fractionDigits)
    {
        this.fractionDigits = fractionDigits;
        this.setHorizontalAlignment(JLabel.RIGHT);
    }

    public void setValue(Object value)
    {
        if (formatter == null)
        {
            formatter = NumberFormat.getInstance();
            formatter.setMaximumFractionDigits(fractionDigits);
        }
        setText((value == null) ? "" : formatter.format(value));
    }

}
