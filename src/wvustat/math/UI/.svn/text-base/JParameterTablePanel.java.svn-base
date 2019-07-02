package wvustat.math.UI;

import wvustat.math.expression.ExpressionObject;
import wvustat.math.expression.ParameterTableModel;
import wvustat.swing.table.DataSet;
import wvustat.swing.table.JTableRowHeaderAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 25, 2003
 * Time: 9:10:28 AM
 * To change this template use Options | File Templates.
 */
public class JParameterTablePanel extends JPanel implements ChangeListener, TableModelListener
{
    private ExpressionObject function;
    private DataSet dataSet;
    private JLabel ssLabel;

    public JParameterTablePanel( ExpressionObject function, DataSet dataSet)
    {

        this.function = function;
        this.dataSet = dataSet;

        function.addChangeListener(this);
        dataSet.addTableModelListener(this);

        this.setLayout(new BorderLayout());

        JLabel label=new JLabel("Model: "+function.getUnparsedForm());
        ssLabel=new JLabel();
        showSumOfSquares();
        this.add(label, BorderLayout.NORTH);
        this.add(createTablePanel(), BorderLayout.CENTER);
        this.add(ssLabel, BorderLayout.SOUTH);

        this.setBorder(new EmptyBorder(10,10,10,10));

    }

    private double computeSumOfSquares()
    {
        double[][] xy=dataSet.getPlotData();
        return function.getSumOfSquares(xy[0], xy[1]);
    }

    private Container createTablePanel()
    {
        ParameterTableModel tableModel = new ParameterTableModel(function);
        JTable table = new JTable(tableModel);
        table.setDefaultRenderer(ExpressionObject.class, new FunctionCellRenderer());
        table.setDefaultEditor(ExpressionObject.class, new FunctionCellEditor());

        Dimension dim=table.getPreferredSize();

        JScrollPane scrollPane=new JTableRowHeaderAdapter(table);
        scrollPane.setPreferredSize(new Dimension(dim.width+60, dim.height+80));
        return scrollPane;
    }

    private void showSumOfSquares()
    {
        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        ssLabel.setText("Sum of Squares: "+nf.format(computeSumOfSquares()));
    }

    public void stateChanged(ChangeEvent e)
    {
        showSumOfSquares();
    }

    public void tableChanged(TableModelEvent e)
    {
        showSumOfSquares();
    }
}
