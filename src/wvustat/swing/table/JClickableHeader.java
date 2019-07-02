package wvustat.swing.table;

import javax.swing.table.JTableHeader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Mar 30, 2003
 * Time: 10:10:10 AM
 * To change this template use Options | File Templates.
 */
public class JClickableHeader extends JTableHeader
{
    public JClickableHeader()
    {
        super();

        this.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                int column=getTable().columnAtPoint(e.getPoint());
                getTable().getColumnModel().getSelectionModel().clearSelection();
                getTable().getColumnModel().getSelectionModel().addSelectionInterval(column,column);
            }
        });
    }
}
