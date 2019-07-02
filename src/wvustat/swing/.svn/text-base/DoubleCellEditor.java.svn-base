/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Nov 30, 2002
 * Time: 11:03:57 AM
 * To change this template use Options | File Templates.
 */
package wvustat.swing;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.text.NumberFormat;

public class DoubleCellEditor extends DefaultCellEditor implements TableCellEditor
{
    private NumberFormat formatter;

    public DoubleCellEditor(int fractionDigits)
    {
        super(new JTextField());
        ((JTextField) getComponent()).setHorizontalAlignment(JTextField.RIGHT);
        formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(fractionDigits);
    }

    public Object getCellEditorValue()
    {
        String text=((JTextField)getComponent()).getText();
        Double d=new Double(text);
        return d;
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
