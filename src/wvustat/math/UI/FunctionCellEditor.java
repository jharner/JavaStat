package wvustat.math.UI;

import wvustat.math.expression.ExpressionObject;
import wvustat.math.expression.Parameter;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 27, 2003
 * Time: 5:35:04 PM
 * To change this template use Options | File Templates.
 */
public class FunctionCellEditor extends JScrollBar implements TableCellEditor
{
    private java.util.List listeners;
    private ExpressionObject function;
    private DoubleRangeModel model;

    public FunctionCellEditor()
    {
        super();
        this.setOrientation(JScrollBar.HORIZONTAL);
        model=new DoubleRangeModel(1,-10,10);
        this.setModel(model);
        listeners=new ArrayList();
        this.addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column)
    {
        if(value==null)
        {
            return this;
        }
        else
        {
            function = (ExpressionObject) value;
            Parameter parameter = (Parameter) function.getParameters().get(row);
            model.setDoubleValue(parameter.getValue());
        }

        return this;
    }

    public Object getCellEditorValue()
    {
        return new Double(model.getDoubleValue());
    }

    public boolean isCellEditable(EventObject anEvent)
    {
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent)
    {
        return true;
    }

    public boolean stopCellEditing()
    {
        fireEditingStopped();
        function=null;
        return true;
    }

    public void cancelCellEditing()
    {
        fireEditingCanceled();
    }

    public void addCellEditorListener(CellEditorListener l)
    {
        listeners.add(l);
    }

    public void removeCellEditorListener(CellEditorListener l)
    {
        listeners.remove(l);
    }

    protected void fireEditingCanceled()
    {
        ChangeEvent evt=new ChangeEvent(this);
        for(int i=0;i<listeners.size();i++)
        {
            ((CellEditorListener)listeners.get(i)).editingCanceled(evt);
        }
    }

    protected void fireEditingStopped()
    {
        ChangeEvent evt=new ChangeEvent(this);
        for(int i=0;i<listeners.size();i++)
        {
            ((CellEditorListener)listeners.get(i)).editingStopped(evt);
        }
    }
}
