/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Nov 24, 2002
 * Time: 8:59:53 AM
 * To change this template use Options | File Templates.
 */
package wvustat.swing.table;

import javax.swing.*;
import javax.swing.table.*;

public class TableCornerHeader extends JLabel
{

	public TableCornerHeader(JTable table)
	{
      if (table != null)
      {
         JTableHeader header = table.getTableHeader();

         if (header != null)
         {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
         }
      }
      setBorder(UIManager.getBorder("TableHeader.cellBorder"));
   }
}

