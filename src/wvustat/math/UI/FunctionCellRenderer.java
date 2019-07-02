package wvustat.math.UI;

import wvustat.math.expression.ExpressionObject;
import wvustat.math.expression.Parameter;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 27, 2003
 * Time: 5:30:33 PM
 * To change this template use Options | File Templates.
 */
public class FunctionCellRenderer extends JScrollBar implements TableCellRenderer
{
    private DoubleRangeModel model;
    public FunctionCellRenderer()
    {
        super();
        this.setOrientation(JScrollBar.HORIZONTAL);
        model=new DoubleRangeModel(1,-10,10);
        this.setModel(model);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        if(value==null)
        {
            return this;
        }
        else
        {
            ExpressionObject function = (ExpressionObject) value;
            Parameter parameter = (Parameter) function.getParameters().get(row);

            model.setDoubleValue(parameter.getValue());
        }

        return this;
    }
}
