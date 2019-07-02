package wvustat.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.DateFormat;

/**
 * TableDefaults contains implementation of cell editors for difference column class. They
 * all support Excel like editing, i.e., it replaces the existing value in the cell instead of appending to it.
 */
public class TableDefaults
{
    public static class GenericEditor extends DefaultCellEditor
    {

        Class[] argTypes = new Class[]{String.class};
        java.lang.reflect.Constructor constructor;
        Object value;

        public GenericEditor()
        {
            super(new JTextField());
        }

        public GenericEditor(JTextField textField){
            super(textField);
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
                return super.stopCellEditing();
            }

            try
            {
                value = constructor.newInstance(new Object[]{s});
            }
            catch (Exception e)
            {
                ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
                Toolkit.getDefaultToolkit().beep();
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
            //((JTextField)editorComp).setText(value==null?"":value.toString());
            ((JTextField) editorComp).selectAll();
            return editorComp;
        }

        public Object getCellEditorValue()
        {
            return value;
        }
    }

    public static class NumberEditor extends GenericEditor
    {

        public NumberEditor()
        {
            ((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
        }

        public NumberEditor(JTextField textField){
            super(textField);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column)
        {
            if (value != null && value.equals(new Double(Double.NaN)))
                value="";
            Component editorComp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            ((JTextField) editorComp).selectAll();
            return editorComp;
        }

    }

    public static class DateEditor extends GenericEditor
    {
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected,
                                                     int row, int column)
        {
            Component comp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            ((JTextField) comp).setText(value == null ? "" : formatter.format(value));
            ((JTextField) comp).selectAll();
            return comp;
        }
    }

    public static class DateRenderer extends DefaultTableCellRenderer
    {
        DateFormat formatter;

        public DateRenderer()
        {
            super();
        }

        public void setValue(Object value)
        {
            if (formatter == null)
            {
                formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }

    public static class BooleanEditor extends DefaultCellEditor
    {
        private MyCheckBox checkBox;

        public BooleanEditor()
        {
            super(new JTextField());
            checkBox=new MyCheckBox();
            setClickCountToStart(1);
            editorComponent = checkBox;
            delegate = new DefaultCellEditor.EditorDelegate()
            {
                public void setValue(Object value)
                {
                    boolean selected = false;
                    if (value instanceof Boolean)
                    {
                        selected = ((Boolean) value).booleanValue();
                    }
                    else if (value instanceof String)
                    {
                        selected = value.equals("true");
                    }
                    checkBox.setState(selected);
                }

                public Object getCellEditorValue()
                {
                    return new Boolean(checkBox.getState());
                }
            };
            checkBox.addActionListener(delegate);
        }


    }

}
