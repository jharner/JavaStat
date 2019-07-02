package wvustat.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * ColorRenderer renders tables cells with value of type java.awt.Color. It does so
 * by drawing a solid oval with the color the table cell contains.
 */
public class ColorRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	/**
     * Constructor
     */
    public ColorRenderer()
    {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Get the component that actually renders the table cell
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Icon icon = new OvalIcon((MyColor) value);

        setIcon(icon);
        setText(null);

        return this;
    }

}
