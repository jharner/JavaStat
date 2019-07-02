package wvustat.table;

import wvustat.util.ClipBoardUtils;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Constructor;
import java.util.StringTokenizer;


public class JTableExcelAdapter implements ActionListener
{


    private String rowstring,value;
    private StringSelection stsel;
    private JTable table;


    public JTableExcelAdapter(JTable table)
    {
        this.table = table;
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //table.setColumnSelectionAllowed(true);
        //table.setCellSelectionEnabled(true);
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false);
        KeyStroke cut = KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK, false);

        table.registerKeyboardAction(this, "COPY_CELLS", copy, JComponent.WHEN_FOCUSED);
        table.registerKeyboardAction(this, "CUT_CELLS", cut, JComponent.WHEN_FOCUSED);
        table.registerKeyboardAction(this, "PASTE_CELLS", paste, JComponent.WHEN_FOCUSED);
        table.registerKeyboardAction(this, "DELETE_CELLS", delete, JComponent.WHEN_FOCUSED);
    }


    public JTable getJTable()
    {
        return table;
    }

    public void setJTable(JTable jTable1)
    {
        this.table = jTable1;
    }


    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("COPY_CELLS"))
        {
            copyCells();
        }
        else if (e.getActionCommand().equals("CUT_CELLS"))
        {
            copyCells();
            deleteCells();
        }
        else if (e.getActionCommand().equals("PASTE_CELLS"))
        {
            pasteCells();
        }
        else if (e.getActionCommand().equals("DELETE_CELLS"))
        {
            deleteCells();
        }
    }

    private Object constructObject(Class objectClass, String stringValue)
    {
        try
        {
            Constructor constructor = objectClass.getConstructor(new Class[]{String.class});
            return constructor.newInstance(new Object[]{stringValue});
        }
        catch (Exception e)
        {
            return stringValue;
        }
    }

    public void copyCells()
    {
        if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1)
            return;
        StringBuffer sbf = new StringBuffer();
        int numcols = table.getSelectedColumnCount();
        int numrows = table.getSelectedRowCount();
        int[] rowsselected = table.getSelectedRows();
        int[] colsselected = table.getSelectedColumns();
        if (!((numrows - 1 == rowsselected[rowsselected.length - 1] - rowsselected[0] &&
                numrows == rowsselected.length) &&
                (numcols - 1 == colsselected[colsselected.length - 1] - colsselected[0] &&
                numcols == colsselected.length)))
        {
            JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
                    "Invalid Copy Selection",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < numrows; i++)
        {
            for (int j = 0; j < numcols; j++)
            {
                Object value = table.getValueAt(rowsselected[i], colsselected[j]);
                if (value == null)
                    sbf.append("");
                else
                    sbf.append(value);
                if (j < numcols - 1)
                    sbf.append("\t");
            }
            if (numrows > 1)
                sbf.append("\n");
        }

        stsel = new StringSelection(sbf.toString());
        ClipBoardUtils.setClipboard(sbf.toString());
    }

    public void pasteCells()
    {
        if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1)
            return;
        int startRow = (table.getSelectedRows())[0];
        int startCol = (table.getSelectedColumns())[0];
        int selectedRowCount = table.getSelectedRowCount();
        int selectedColumnCount = table.getSelectedColumnCount();

        try
        {
            String clipString = ClipBoardUtils.getClipboard();
            if (clipString.indexOf('\t') == -1 && clipString.indexOf('\n') == -1)
            {
                for (int i = 0; i < selectedRowCount; i++)
                {
                    for (int j = 0; j < selectedColumnCount; j++)
                    {
                        if (table.isCellEditable(startRow + i, startCol + j))
                        {
                            if (table.getColumnClass(startCol + j) != String.class)
                            {
                                table.setValueAt(constructObject(table.getColumnClass(startCol + j), clipString), startRow + i, startCol + j);
                            }
                            else
                                table.setValueAt(clipString, startRow + i, startCol + j);
                        }
                    }
                }
            }
            else
            {
                StringTokenizer st1 = new StringTokenizer(clipString, "\n");
                for (int i = 0; st1.hasMoreTokens(); i++)
                {
                    rowstring = st1.nextToken();
                    StringTokenizer st2 = new StringTokenizer(rowstring, "\t");
                    for (int j = 0; st2.hasMoreTokens(); j++)
                    {
                        value = st2.nextToken();
                        if (startRow + i < table.getRowCount() &&
                                startCol + j < table.getColumnCount())
                        {
                            if(table.getColumnClass(startCol+j)!=String.class)
                                table.setValueAt(constructObject(table.getColumnClass(startCol + j), value), startRow + i, startCol + j);
                            else
                                table.setValueAt(value, startRow + i, startCol + j);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void deleteCells()
    {
        if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1)
            return;
        int startRow = (table.getSelectedRows())[0];
        int startCol = (table.getSelectedColumns())[0];
        int selectedRowCount = table.getSelectedRowCount();
        int selectedColumnCount = table.getSelectedColumnCount();

        try
        {
            for (int i = 0; i < selectedRowCount; i++)
            {
                for (int j = 0; j < selectedColumnCount; j++)
                {
                    table.setValueAt(null, startRow + i, startCol + j);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

