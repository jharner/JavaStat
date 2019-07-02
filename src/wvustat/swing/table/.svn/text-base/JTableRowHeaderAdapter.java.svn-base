/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Nov 24, 2002
 * Time: 8:58:33 AM
 * To change this template use Options | File Templates.
 */
package wvustat.swing.table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


public class JTableRowHeaderAdapter extends JScrollPane implements ListSelectionListener
{
	private static final long serialVersionUID = 1L;
	
    protected JTable mainTable;
    protected JTable rowHeaderTable;
    protected int currentRowCount = -1;

    public JTableRowHeaderAdapter(JTable table)
    {
        super(table);
        mainTable = table;
        RowNumberTableModel rowNumberTableModel = new RowNumberTableModel();
        rowHeaderTable = new JTable(rowNumberTableModel);
        
        table.setGridColor(Color.gray);
        rowHeaderTable.setGridColor(Color.gray);
        rowHeaderTable.setSelectionModel(table.getSelectionModel());
        rowHeaderTable.getSelectionModel().addListSelectionListener(this);
        rowHeaderTable.setGridColor(table.getGridColor());
        JTableHeader header = mainTable.getTableHeader();
        rowHeaderTable.setBackground(header.getBackground());
        rowHeaderTable.setForeground(header.getForeground());
        rowHeaderTable.setFont(header.getFont());
        rowHeaderTable.setSelectionBackground(java.awt.SystemColor.textHighlight);
        rowHeaderTable.setSelectionForeground(Color.gray);
        rowHeaderTable.setColumnSelectionAllowed(false);
        rowHeaderTable.setRowSelectionAllowed(true);
        //rowHeaderTable.setCellSelectionEnabled(true);
        rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JViewport jvp = new JViewport();
        jvp.setView(rowHeaderTable);
        jvp.setPreferredSize(rowHeaderTable.getPreferredSize());
        this.setRowHeader(jvp);
        this.setCorner(JScrollPane.UPPER_LEFT_CORNER, new TableCornerHeader(mainTable));
        this.setCorner(JScrollPane.LOWER_LEFT_CORNER, new TableCornerHeader(mainTable));
        mainTable.getModel().addTableModelListener(rowNumberTableModel);
        calcWidth();
    }

    private void calcWidth()
    {
        FontMetrics fm = mainTable.getFontMetrics(mainTable.getFont());
        //String sRowCount = String.valueOf(getRowCount());
        Dimension d = new Dimension(fm.stringWidth("9999") + 40, mainTable.getPreferredSize().height);
        rowHeaderTable.setPreferredScrollableViewportSize(d);
    }

    class RowNumberTableModel extends AbstractTableModel implements TableModelListener
    {
		private static final long serialVersionUID = 1L;

		public int getRowCount()
        {
            return mainTable.getModel().getRowCount();
        }

        public int getColumnCount()
        {
            return 1;
        }

        public Object getValueAt(int row, int column)
        {
            return new Integer(row + 1);
        }

        public void tableChanged(TableModelEvent e)
        {
            if (currentRowCount != getRowCount())
            {
                //calcWidth();
                currentRowCount = getRowCount();
                this.fireTableDataChanged();
            }
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        //mainTable.getColumnModel().getSelectionModel().addSelectionInterval(0,mainTable.getColumnCount()-1);
    }

}

