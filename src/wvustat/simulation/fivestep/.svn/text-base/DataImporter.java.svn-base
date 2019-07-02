package wvustat.simulation.fivestep;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.util.DataFileUtils;

import javax.swing.*;
import java.util.Vector;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 29, 2003
 * Time: 1:02:18 PM
 * To change this template use Options | File Templates.
 */
public class DataImporter
{

    public static void importData(JComponent dialogParent, JTextField[][] fields)
    {
        DataSet dataSet = DataFileUtils.openFileAsDataSet(dialogParent);
        if (dataSet != null)
        {
            Vector yVars = dataSet.getYVariables();
            if (yVars.size() == 0)
            {
                JOptionPane.showMessageDialog(dialogParent, "No y variable in data file!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Variable yVar=(Variable)yVars.get(0);
            double[] yValues = yVar.getNumValues();


            Vector xVars = dataSet.getXVariables();
            if (xVars.size() == 0)
            {
                Variable freqVar = dataSet.getFreqVariable();
                if (yValues.length > 30)
                {
                    JOptionPane.showMessageDialog(dialogParent, "At most 30 y values are allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    for (int i = 0; i < fields[0].length; i++)
                    {
                        fields[0][i].setText("1");
                        fields[1][i].setText("");
                    }

                    double[] freqs = null;
                    if (freqVar != null)
                        freqs = freqVar.getNumValues();
                    for (int i = 0; i < yValues.length; i++)
                    {
                        if (freqs != null)
                            fields[0][i].setText(NumberFormat.getInstance().format(freqs[i]));
                        fields[1][i].setText(String.valueOf(yValues[i]));
                    }
                }
            }
            else
            {
                Variable xVar = (Variable) xVars.get(0);
                if (xVar.getType() != Variable.CATEGORICAL)
                {
                    JOptionPane.showMessageDialog(dialogParent, "X varaiable is numerical!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] xLevels=xVar.getDistinctCatValues();
                if(xLevels.length!=2)
                {
                    JOptionPane.showMessageDialog(dialogParent, "X variable doesn't have 2 levels!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] xValues=xVar.getCatValues();
                double[] freqs=dataSet.getFreqVariable()==null?null:dataSet.getFreqVariable().getNumValues();
                if(yVar.getType()==Variable.NUMERIC)
                {
                    for (int i = 0; i < xValues.length; i++)
                    {
                        if(xValues[i].equals(xLevels[0]))
                        {

                        }
                        else
                        {

                        }

                    }
                }

            }
        }
}
}