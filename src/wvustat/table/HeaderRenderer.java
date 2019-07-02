package wvustat.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import wvustat.interfaces.*;

/**
 *	HeaderRender implements CellRenderer and is used to reader header of a column
 *	in a table so that the role as well as the name of a variable can be displayed.
 *
 */

public class HeaderRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	public HeaderRenderer()
    {
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column)
    {

        JLabel label=(JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (table != null)
         {
             JTableHeader header = table.getTableHeader();
             if (header != null)
             {
                 if (table.isColumnSelected(column))
                 {
                     label.setForeground(Color.blue);
                     label.setBackground((Color)UIManager.get("Table.selectionBackground"));
                 }
                 else
                 {
                     label.setForeground(header.getForeground());
                     label.setBackground(new Color(240, 240, 240));
                     //label.setBackground(header.getBackground());
                     //label.setFont(header.getFont());
                 }
             }
         }
        //label.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        label.setBorder(BorderFactory.createLineBorder(Color.gray));
        if (column < 2)
        {
        	label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setIconTextGap(0);
            label.setIcon(null);

            return label;
        }
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setIconTextGap(2);
        TableModel tm = table.getModel();
        DataSetTM dtm = (DataSetTM) tm;
        TableColumn tc=table.getColumnModel().getColumn(column);
        Variable v = null;
        
        v = dtm.getVariable(tc.getModelIndex());

        int role = DataSet.U_ROLE;
        int type = Variable.NUMERIC;

        if (v != null)
        {
            role = v.getRole();
            type = v.getType();
        }
        else
        {
            System.err.println("Variable in " + column + "th column not found");
        }

        Icon icon = new RoleIcon(role, type);
        label.setIcon(icon);    

        return label;
    }
}

