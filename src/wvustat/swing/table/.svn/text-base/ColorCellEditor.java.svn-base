package wvustat.swing.table;

import wvustat.table.OvalIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorCellEditor extends DefaultCellEditor
{
    private Color currentColor;

    public ColorCellEditor(JButton b)
    {
        super(new JCheckBox());
        editorComponent = b;
        setClickCountToStart(2); //This is usually 1 or 2.

        //Must do this so that editing stops when appropriate.
        b.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fireEditingStopped();
            }
        });
    }

    public Object getCellEditorValue()
    {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor)
    {
        this.currentColor = currentColor;
    }

    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column)
    {
        ((JButton) editorComponent).setText(null);
        ((JButton) editorComponent).setIcon(new OvalIcon((Color) value));
         currentColor=(Color)value;
        return editorComponent;
    }
}
