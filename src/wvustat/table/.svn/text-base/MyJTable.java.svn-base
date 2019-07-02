/*
 * MyJTable.java
 *
 * Created on October 4, 2000, 10:47 AM
 */

package wvustat.table;

import wvustat.interfaces.Variable;
import wvustat.swing.table.TableColumnHidableModel;
import wvustat.swing.table.JTableRowHeaderAdapter;

import javax.swing.*;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * MyJTable is a subclass of a javax.swing.JTable that fixes one of the problem with JTable.
 * The problem is that when the table header changes, the JTable class will remove all the custom
 * header renderers and install default renderers for the header. MyJTable fixes this problem by
 * reinstalling the custom renderer rather than the default.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 4, 2000
 */
public class MyJTable extends JTable
{
    private int movingColumnOldIndex;
    private int movingColumnNewIndex;

    /**
     * Constructs a default <code>MyJTable</code> that is initialized with a default
     * data model, a default column model, and a default selection
     * model.
     *
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public MyJTable()
    {
        super(null, null, null);
    }

    /**
     * Constructs a <code>MyJTable</code> that is initialized with
     * <code>dm</code> as the data model, a default column model,
     * and a default selection model.
     *
     * @param dm        the data model for the table
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public MyJTable(TableModel dm)
    {
        super(dm, new TableColumnHidableModel(), null);
        setDefaultRenderer(java.awt.Color.class, new ColorRenderer());
        setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
        setDefaultEditor(Boolean.class, new TableDefaults.BooleanEditor());
        setDefaultEditor(java.awt.Color.class, null);
    }

    /**
     * Constructs a <code>MyJTable</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code>
     * as the column model, and a default selection model.
     *
     * @param dm        the data model for the table
     * @param cm        the column model for the table
     * @see #createDefaultSelectionModel
     */
    public MyJTable(TableModel dm, TableColumnModel cm)
    {
        super(dm, cm, null);
    }

    /**
     * Constructs a <code>MyJTable</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code> as the
     * column model, and <code>sm</code> as the selection model.
     * If any of the parameters are <code>null</code> this method
     * will initialize the table with the corresponding default model.
     * The <code>autoCreateColumnsFromModel</code> flag is set to false
     * if <code>cm</code> is non-null, otherwise it is set to true
     * and the column model is populated with suitable
     * <code>TableColumns</code> for the columns in <code>dm</code>.
     *
     * @param dm        the data model for the table
     * @param cm        the column model for the table
     * @param sm        the row selection model for the table
     * @see #createDefaultDataModel
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public MyJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
        setDefaultRenderer(java.awt.Color.class, new ColorRenderer());
        setDefaultEditor(java.awt.Color.class, null);
    }

    /**
     * Constructs a <code>MyJTable</code> with <code>numRows</code>
     * and <code>numColumns</code> of empty cells using
     * <code>DefaultTableModel</code>.  The columns will have
     * names of the form "A", "B", "C", etc.
     *
     * @param numRows           the number of rows the table holds
     * @param numColumns        the number of columns the table holds
     * @see javax.swing.table.DefaultTableModel
     */
    public MyJTable(int numRows, int numColumns)
    {
        super(new DefaultTableModel(numRows, numColumns));
    }


    /**
     * Creates default columns for the table from
     * the data model using the <code>getColumnCount</code> method
     * defined in the <code>TableModel</code> interface.
     * <p>
     * Clears any existing columns before creating the
     * new columns based on information from the model.
     *
     * @see     #getAutoCreateColumnsFromModel
     */
    public void createDefaultColumnsFromModel()
    {
        TableModel m = getModel();
        if (m != null)
        {
            // Remove any current columns
            TableColumnModel cm = getColumnModel();
            TableCellRenderer tcr = null;
            if (cm.getColumnCount() > 0)
                tcr = cm.getColumn(0).getHeaderRenderer();
           
            while (cm.getColumnCount() > 0)
            {
                cm.removeColumn(cm.getColumn(0));
            }

            // Create new columns from the data model info
            for (int i = 0; i < m.getColumnCount(); i++)
            {
                TableColumn newColumn = new TableColumn(i);
                if (i < 2)
                    newColumn.setPreferredWidth(36);

                newColumn.setHeaderRenderer(tcr);
                addColumn(newColumn);

            }
        }
    }


    public void columnMoved(TableColumnModelEvent e)
    {
        super.columnMoved(e);
        int from=e.getFromIndex();
        int to=e.getToIndex();
        if(from==to)
            return;
        movingColumnOldIndex=from;
        movingColumnNewIndex=to;
    }

    /**
     * If a column has been moved, adjust the <code>TableModel</code> so that the column
     * ordering is the same in the <code>TableModel</code> as the table.
     */
    public void adjustModel()
    {
        if(movingColumnOldIndex!=movingColumnNewIndex)
        {
            ((DataSetTM)getModel()).moveColumn(movingColumnOldIndex, movingColumnNewIndex);
            movingColumnOldIndex=movingColumnNewIndex=0;
        }
    }

    public void setValueAt(Object value, int row, int column)
    {
        if (this.getColumnClass(column) == Double.class || this.getColumnClass(column) == Integer.class)
        {
            if (value != null && "".equals(value) == false)
            {
                try
                {
                    Double d = new Double(value.toString());
                    super.setValueAt(d, row, column);
                }
                catch (NumberFormatException nfe)
                {
                    String[] options = {"Avert"};
                    JOptionPane.showOptionDialog(this.getRootPane(), "Non-numeric value entered!", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    super.setValueAt(Variable.NUM_MISSING_VAL, row, column);
                }
            }
            else
                super.setValueAt(Variable.NUM_MISSING_VAL, row, column);

        }
        else{
            try
            {
                super.setValueAt(value, row, column);
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(this.getRootPane(),ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    public static void main(String[] args)
    {
        final JTable table=new JTable(5,5);
        table.setRowSelectionAllowed(false);
        JFrame jf=new JFrame("Test");
        jf.setContentPane(new JTableRowHeaderAdapter(table));
        jf.pack();
        ActionListener l=new ActionListener()
        {
            int count;
            public void actionPerformed(ActionEvent event)
            {
                table.getSelectionModel().addSelectionInterval(count, count);
                count++;
                if(count==5)
                    count=0;
            }
        };
        Timer timer=new Timer(1000, l);
        timer.start();
        jf.show();

    }

}
