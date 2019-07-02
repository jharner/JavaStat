package wvustat.math.UI;

import wvustat.math.expression.ExpressionObject;
import wvustat.math.expression.Parameter;
import wvustat.swing.StackLayout;
import wvustat.swing.table.DataSet;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 11, 2003
 * Time: 12:20:59 PM
 * To change this template use Options | File Templates.
 */
public class JFunctionComponent extends JComponent implements ChangeListener
{
    private ExpressionObject function;
    private JLabel[] nameLabels;
    private JLabel[] seLabels;
    private JSlider[] sliders;
    private List parameters;
    private NumberFormat numberFormat;
    private DataSet dataSet;
    private JLabel ssLabel;
    private Map paramTable;

    public JFunctionComponent(ExpressionObject function, DataSet dataSet)
    {
        this.function = function;
        this.dataSet = dataSet;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(3);
        copyMap(function.getParamTable());
        initComponent();
    }

    private void copyMap(Map map)
    {
        paramTable=new Hashtable();
        Set keySet=map.keySet();

        Iterator iter=keySet.iterator();
        while(iter.hasNext())
        {
            Object key=iter.next();
            Parameter value=(Parameter)map.get(key);
            paramTable.put(key, value.clone());
        }
    }

    private void initComponent()
    {
        parameters = function.getParameters();

        nameLabels = new JLabel[parameters.size()];
        seLabels = new JLabel[parameters.size()];
        sliders = new JSlider[parameters.size()];

        for (int i = 0; i < parameters.size(); i++)
        {
            Parameter param = (Parameter) parameters.get(i);
            double value = param.getValue();
		
            nameLabels[i] = new JLabel(param.getName() + " = " + numberFormat.format(value));
            seLabels[i] = new JLabel("SE(" + param.getName() + ") = "+numberFormat.format(param.getSE()));
            DoubleRangeModel model = new DoubleRangeModel(value, -10, 10);
            sliders[i] = new JSlider(model);
            sliders[i].addChangeListener(this);
        }

        this.setLayout(new StackLayout());
        this.add(new JLabel("Model: " + function.getUnparsedForm()));
        this.add(new JLabel(" "));
        for (int j = 0; j < this.parameters.size(); j++)
        {

            this.add(nameLabels[j]);
            this.add(sliders[j]);
            //this.add(seLabels[j]);

            this.add(Box.createRigidArea(new Dimension(20, 20)));
        }

        this.add(new JLabel(""));
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        ssLabel = new JLabel("Sum of Squares = " + nf.format(computeSumOfSquares()));
        if(function.getType()==ExpressionObject.MODEL)
            this.add(ssLabel);
        //this.add(new JLabel("R Square = "));
    }

    public void stateChanged(ChangeEvent e)
    {
        Object obj = e.getSource();

        int index = 0;
        while (index < parameters.size() && sliders[index] != obj)
        {
            index++;
        }

        if (index < parameters.size())
        {
            DoubleRangeModel model = (DoubleRangeModel) sliders[index].getModel();
            double value = model.getDoubleValue();
            Parameter param = (Parameter) parameters.get(index);
            function.setVariableValue(param.getName(), value);
            nameLabels[index].setText(param.getName() + " = " + numberFormat.format(value));
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            ssLabel.setText("Sum of Squares = " + nf.format(computeSumOfSquares()));
        }
    }

    private double computeSumOfSquares()
    {
        double[][] xy = dataSet.getPlotData();
        return function.getSumOfSquares(xy[0], xy[1]);
    }

    public void reset()
    {
        function.setParamTable(paramTable);
        this.removeAll();
        this.initComponent();
        this.validate();
    }

}
