/*
 * BooleanCellRenderer.java
 *
 * Created on May 29, 2002, 10:46 AM
 */

package wvustat.table;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 *
 * @author  James Harner
 * @version
 */
public class BooleanCellRenderer extends MyCheckBox implements javax.swing.table.TableCellRenderer
{

    private Color unselectedForeground;
    private Color unselectedBackground;
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    /** Creates new BooleanCellRenderer */
    public BooleanCellRenderer()
    {
    }

    public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                            boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected)
        {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        }
        else
        {
            super.setForeground((unselectedForeground != null) ? unselectedForeground
                    : table.getForeground());
            super.setBackground((unselectedBackground != null) ? unselectedBackground
                    : table.getBackground());
        }

        setFont(table.getFont());

        if (hasFocus)
        {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            if (table.isCellEditable(row, column))
            {
                super.setForeground(UIManager.getColor("Table.focusCellForeground"));
                super.setBackground(UIManager.getColor("Table.focusCellBackground"));
            }
        }
        else
        {
            setBorder(noFocusBorder);
        }

        this.setState(((Boolean) value).booleanValue());
        return this;
    }

}
