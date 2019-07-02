/*
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Oct 29, 2002
 * Time: 8:56:01 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package wvustat.util;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseListener;
import java.awt.*;

public class TableAdapter extends JScrollPane
{
    private JTable table;
    private JTable rowHeaderTable;
    private RowHeaderTableModel rowHeaderModel;

    public TableAdapter(JTable table)
    {
        super(table);
        this.table = table;

        rowHeaderModel=new RowHeaderTableModel();
        rowHeaderTable=new JTable(rowHeaderModel);
        rowHeaderTable.setOpaque(true);
        rowHeaderTable.createDefaultColumnsFromModel();;

        /*
        for(int i=0;i<table.getModel().getRowCount();i++)
        {
            rowHeaderTable.setRowHeight(i, table.getRowHeight(i));
        }
        */

        JTableHeader header = table.getTableHeader();
        //rowHeaderTable.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        rowHeaderTable.setBackground(header.getBackground());
        rowHeaderTable.setForeground(header.getForeground());
        rowHeaderTable.setFont(header.getFont());
        rowHeaderTable.setSelectionBackground(java.awt.SystemColor.textHighlight);
        //rowHeaderTable.setSelectionForeground(Color.white);
        rowHeaderTable.setRowSelectionAllowed(true);
        TableCellRenderer renderer=rowHeaderTable.getDefaultRenderer(String.class);
        if(renderer!=null)
        {
            ((JLabel)renderer).setHorizontalAlignment(JLabel.RIGHT);
        }

        JViewport jvp = new JViewport();
        jvp.setView(rowHeaderTable);
        this.setViewportView(table);
        this.setRowHeader(jvp);

        rowHeaderTable.setSelectionModel(table.getSelectionModel());
        table.getModel().addTableModelListener(rowHeaderModel);
        calcWidth();
    }

    private void calcWidth()
    {
        FontMetrics fm = table.getFontMetrics(table.getFont());
        //String sRowCount = String.valueOf(getRowCount());
        Dimension d = new Dimension(fm.stringWidth("999")+4 , table.getMinimumSize().height);
        rowHeaderTable.setPreferredScrollableViewportSize(d);
    }

    class RowHeaderTableModel extends AbstractTableModel implements TableModelListener
    {

        public int getRowCount()
        {
            return table.getModel().getRowCount();
        }

        public int getColumnCount()
        {
            return 1;
        }

        public Object getValueAt(int row, int column)
        {
            return new Integer(row+1);
        }

        public void tableChanged(TableModelEvent e)
        {
            if (table.getModel().getRowCount() != getRowCount())
            {
                this.fireTableDataChanged();
            }
        }
    }
}
