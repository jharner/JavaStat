/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 1, 2002
 * Time: 9:51:20 AM
 * To change this template use Options | File Templates.
 */
package wvustat.swing.table;

import wvustat.plotUtil.ColumnDefDialog;
import wvustat.plotUtil.RoleDefDialog;
import wvustat.swing.RetargetedAction;
import wvustat.table.ColorRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Map;
import java.util.HashMap;

public class JDataTableDialog extends JDialog implements ActionListener, ListSelectionListener
{
    private DataSet dataSet;
    private JTextField colNameTF;
    private JComboBox roleComboBox;
    private JTable table;
    private Map actionMap = new HashMap();
    private JDialog colorDialog;
    private JColorChooser colorChooser;
    private ColorCellEditor colorEditor;

    public JDataTableDialog(Frame owner, DataSet dataSet)
    {
        super(owner, "Data Tables", false);
        this.dataSet = dataSet;
        //headerRenderer = new RoleTableHeaderRenderer();
        createTable();
        createMenus();
    }

    public void show()
    {
        super.show();
        dataSet.fireTableChanged(null);
    }

    public void createTable()
    {
        table = new MyJTable(dataSet);
        table.setGridColor(Color.gray);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(Double.class, new MyDoubleRenderer(2));
        table.setDefaultEditor(Object.class, new GenericEditor());
        table.setDefaultEditor(Number.class, new NumberEditor(3));
        //final DataSet dataSet = (DataSet) table.getModel();
        table.getSelectionModel().addListSelectionListener(this);
        table.getColumnModel().getSelectionModel().addListSelectionListener(
                new ListSelectionListener()
                {
                    public void valueChanged(ListSelectionEvent evt)
                    {
                        table.getTableHeader().repaint();
                    }
                }
        );
        table.setDefaultRenderer(Color.class, new ColorRenderer());
        JButton button = new JButton();
        colorEditor = new ColorCellEditor(button);
        colorChooser = new JColorChooser();
        table.setDefaultEditor(Color.class, colorEditor);
        ActionListener okListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                colorEditor.setCurrentColor(colorChooser.getColor());
            }
        };

        colorDialog = JColorChooser.createDialog(this, "Choose a color", true, colorChooser, okListener, null);
        ActionListener buttonListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                colorDialog.show();
            }
        };
        button.addActionListener(buttonListener);

        table.getTableHeader().addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent evt)
            {
                if (evt.getClickCount() == 2)
                {
                    int col = table.getColumnModel().getColumnIndexAtX(evt.getX());
                    JPanel editPanel = createRolePanel();
                    colNameTF.setText(dataSet.getColumnName(col));
                    int option = JOptionPane.showOptionDialog(JDataTableDialog.this, editPanel, "Edit column", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (option == JOptionPane.OK_OPTION)
                    {
                        if (colNameTF.getText().length() > 0)
                            dataSet.setColumnName(colNameTF.getText(), col);
                        dataSet.setColumnRole(roleComboBox.getSelectedIndex(), col);
                        table.getTableHeader().repaint();
                    }
                }
                else
                {
                    if(!evt.isShiftDown())
                        table.getColumnModel().getSelectionModel().clearSelection();
                    table.getSelectionModel().clearSelection();
                    int column = table.columnAtPoint(evt.getPoint());
                    //table.getColumnModel().getSelectionModel().clearSelection();
                    table.getColumnModel().getSelectionModel().addSelectionInterval(column, column);
                    //table.getSelectionModel().addSelectionInterval(0, table.getRowCount() - 1);

                }
            }
        }
        );

        RoleTableHeaderRenderer renderer = new RoleTableHeaderRenderer();
        for (int i = 0; i < table.getColumnCount(); i++)
        {
            table.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
        }

        JTableRowHeaderAdapter adapter = new JTableRowHeaderAdapter(table);
        JLabel corner = new JLabel("Obs", SwingConstants.RIGHT);
        corner.setForeground(Color.black);
        corner.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
        corner.setFont(table.getFont());
        corner.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent evt)
            {
                table.clearSelection();

            }
        });
        adapter.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);
        adapter.setPreferredSize(new Dimension(350, 220));
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(adapter, BorderLayout.CENTER);
    }

    private void createMenus()
    {
        Action deleteRowAction = new RetargetedAction("Delete Rows", "DELETE_ROWS", this);
        deleteRowAction.setEnabled(false);
        Action colorRowAction = new RetargetedAction("Color...", "COLOR_ROW", this);
        colorRowAction.setEnabled(false);
        actionMap.put(deleteRowAction.getValue(Action.ACTION_COMMAND_KEY), deleteRowAction);
        actionMap.put(colorRowAction.getValue(Action.ACTION_COMMAND_KEY), colorRowAction);
        JMenu editMenu = new JMenu("Rows");
        JMenuItem item = editMenu.add("Add Rows...");
        item.setActionCommand("ADD_ROWS");
        item.addActionListener(this);
        editMenu.add(deleteRowAction);
        editMenu.addSeparator();
        editMenu.add(colorRowAction);

        JMenu colMenu = new JMenu("Columns");
        /*
        item = colMenu.add("Add Columns...");
        item.setActionCommand("ADD_COLUMNS");
        item.addActionListener(this);
        item = colMenu.add("Delete Columns");
        item.setActionCommand("DELETE_COLUMNS");
        item.addActionListener(this);
        colMenu.addSeparator();
        */
        item = colMenu.add("Transform...");
        item.setActionCommand("TRANSFORM");
        item.addActionListener(this);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(editMenu);
        menuBar.add(colMenu);
        setJMenuBar(menuBar);
    }

    private JPanel createRolePanel()
    {
        JPanel panel = new JPanel();
        GridBagLayout gridBag = new GridBagLayout();
        panel.setLayout(gridBag);

        colNameTF = new JTextField(8);
        roleComboBox = new JComboBox(new String[]{"None", "X", "Y"});
        panel.add(new JLabel("Column Name"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(colNameTF, new GridBagConstraints(1, 0, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(new JLabel("Column Role"), new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(roleComboBox, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        //ComponentUtilities.addLabelComponent(new JLabel("Column Name"), colNameTF, gridBag, panel);
        //ComponentUtilities.addLabelComponent(new JLabel("Column Role"), roleComboBox, gridBag, panel);

        return panel;
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (table.getSelectedRow() >= 0)
        {
            ((Action) actionMap.get("DELETE_ROWS")).setEnabled(true);
            ((Action) actionMap.get("COLOR_ROW")).setEnabled(true);
        }
        else
        {
            ((Action) actionMap.get("DELETE_ROWS")).setEnabled(false);
            ((Action) actionMap.get("COLOR_ROW")).setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("ADD_ROWS"))
        {
            String input = JOptionPane.showInputDialog(this,
                    "Number of rows to add:",
                    "Input # of rows",
                    JOptionPane.INFORMATION_MESSAGE);
            try
            {
                int num = Integer.parseInt(input);
                dataSet.addRows(num);
                //this.pack();
                if (table.getPreferredSize().height < 700 && this.getHeight() < table.getPreferredSize().height + 60)
                    this.setSize(this.getWidth(), table.getPreferredSize().height + 60);
                this.validate();
            }
            catch (NumberFormatException error)
            {

            }
        }
        else if (cmd.equals("ADD_COLUMNS") || cmd.equals("TRANSFORM"))
        {
            ColumnDefDialog dialog = new ColumnDefDialog(this, dataSet);
            dialog.show();
            RoleTableHeaderRenderer renderer = new RoleTableHeaderRenderer();
            for (int i = 1; i < table.getColumnCount(); i++)
            {
                table.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
            }
        }
        else if (cmd.equals("PLOT_COLUMNS"))
        {
            RoleDefDialog dialog = new RoleDefDialog(this, dataSet);
            dialog.show();
        }
        else if (cmd.equals("DELETE_ROWS"))
        {
            int minRow = table.getSelectionModel().getMinSelectionIndex();
            int maxRow = table.getSelectionModel().getMaxSelectionIndex();

            dataSet.deleteRows(minRow, maxRow);
            table.getSelectionModel().setSelectionInterval(-1, -1);
        }
        else if (cmd.equals("DELETE_COLUMNS"))
        {
            int minCol = table.getColumnModel().getSelectionModel().getMinSelectionIndex();
            int maxCol = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();

            dataSet.deleteColumns(minCol, maxCol);
            table.getColumnModel().getSelectionModel().setSelectionInterval(-1, -1);
        }
        else if (cmd.equals("COLOR_ROW"))
        {
            table.editCellAt(table.getSelectedRow(), 0);
            colorEditor = (ColorCellEditor) table.getCellEditor();
            colorDialog.show();
        }
    }

    static class GenericEditor extends DefaultCellEditor
    {

        Class[] argTypes = new Class[]{String.class};
        java.lang.reflect.Constructor constructor;
        Object value;

        public GenericEditor()
        {
            super(new JTextField());
        }

        public boolean stopCellEditing()
        {
            String s = (String) super.getCellEditorValue();
            // Here we are dealing with the case where a user
            // has deleted the string value in a cell, possibly
            // after a failed validation. Return null, so that
            // they have the option to replace the value with
            // null or use escape to restore the original.
            // For Strings, return "" for backward compatibility.
            if ("".equals(s))
            {
                if (constructor.getDeclaringClass() == String.class)
                {
                    value = s;
                }
                super.stopCellEditing();
            }

            try
            {
                value = constructor.newInstance(new Object[]{s});
            }
            catch (Exception e)
            {
                ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
                return false;
            }
            return super.stopCellEditing();
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column)
        {
            this.value = null;
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
            try
            {
                Class type = table.getColumnClass(column);
                // Since our obligation is to produce a value which is
                // assignable for the required type it is OK to use the
                // String constructor for columns which are declared
                // to contain Objects. A String is an Object.
                if (type == Object.class)
                {
                    type = String.class;
                }
                constructor = type.getConstructor(argTypes);
            }
            catch (Exception e)
            {
                return null;
            }
            Component editorComp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            ((JTextField) editorComp).setText(value == null ? "" : value.toString());
            ((JTextField) editorComp).selectAll();
            return editorComp;
        }

        public Object getCellEditorValue()
        {
            return value;
        }
    }

    static class NumberEditor extends GenericEditor
    {
        private NumberFormat formatter;

        public NumberEditor(int fractionDigits)
        {
            ((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
            formatter = NumberFormat.getInstance();
            formatter.setMaximumFractionDigits(fractionDigits);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column)
        {
            Component editorComp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            double d = ((Double) value).doubleValue();

            ((JTextField) editorComp).setText(value == null ? "" : formatter.format(d));

            ((JTextField) editorComp).selectAll();
            return editorComp;
        }

    }

    static class MyJTable extends JTable
    {

        public MyJTable(TableModel model)
        {
            super(model);

        }

        public void createDefaultColumnsFromModel()
        {
            TableModel m = getModel();
            if (m != null)
            {
                // Remove any current columns
                TableColumnModel cm = getColumnModel();
                TableCellRenderer tcr = null;
                if (cm.getColumnCount() > 0)
                    tcr = cm.getColumn(1).getHeaderRenderer();
                while (cm.getColumnCount() > 0)
                {
                    cm.removeColumn(cm.getColumn(0));
                }

                // Create new columns from the data model info
                for (int i = 0; i < m.getColumnCount(); i++)
                {
                    TableColumn newColumn = new TableColumn(i);
                    if (i == 0)
                        newColumn.setPreferredWidth(36);
                    else
                        newColumn.setHeaderRenderer(tcr);
                    addColumn(newColumn);

                }
            }
        }
    }

}
