package wvustat.simulation.fivestep;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.simulation.model.DataPair;
import wvustat.swing.DoubleField;
import wvustat.util.DataFileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 31, 2003
 * Time: 2:13:32 PM
 * To change this template use Options | File Templates.
 */
public class JPairedDataInputPanel extends JPanel implements ActionListener
{
    //private PairedDataTableModel tableModel;
    private FiveStepMediator mediator;
    //private JTable table;
    private JTextField[][] inputFields;
    private int maxLength = 30;
    private int numOfRows = 16;
    //private boolean modelChanged=false;

    public JPairedDataInputPanel(FiveStepMediator mediator)
    {
        this.mediator = mediator;
        //tableModel = new PairedDataTableModel();
        setLayout(new BorderLayout());
        //add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }


    private Component createCenterPanel()
    {
        inputFields = new DoubleField[2][maxLength];
        for (int i = 0; i < maxLength; i++)
        {
            inputFields[0][i] = new DoubleField(4);
            inputFields[1][i] = new DoubleField(4);
            inputFields[0][i].addActionListener(this);
            inputFields[1][i].addActionListener(this);
        }

        JPanel subPanel1 = new JPanel(new GridLayout(numOfRows, 2, 4, 2));
        JPanel subPanel2 = new JPanel(new GridLayout(numOfRows, 2, 4, 2));

        subPanel1.add(new JLabel("X-Value"));
        subPanel1.add(new JLabel("Y-Value"));
        subPanel2.add(new JLabel("X-Value"));
        subPanel2.add(new JLabel("Y-Value"));
        for (int i = 0; i < numOfRows - 1; i++)
        {
            subPanel1.add(inputFields[0][i]);
            subPanel1.add(inputFields[1][i]);
            subPanel2.add(inputFields[0][numOfRows - 1 + i]);
            subPanel2.add(inputFields[1][numOfRows - 1 + i]);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 10, 2));
        panel.add(subPanel1);
        panel.add(subPanel2);
        panel.setBorder(BorderFactory.createEmptyBorder(2,6,2,6));
        return panel;

    }

    private Component createBottomPanel()
    {
        Box panel = Box.createHorizontalBox();
        JButton button = new JButton("Clear model");
        button.setActionCommand("CLEAR_MODEL");
        button.addActionListener(this);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(4, 4)));
        JButton bData = new JButton("Data...");
        bData.setActionCommand("IMPORT_DATA");
        bData.addActionListener(this);
        panel.add(bData);
        panel.add(Box.createHorizontalGlue());
        return panel;
    }

    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("CLEAR_MODEL"))
        {
            mediator.reset();
        }
        else if (evt.getActionCommand().equals("IMPORT_DATA"))
        {
            DataSet dataSet = DataFileUtils.openFileAsDataSet(this);
            if (dataSet != null)
                importData(dataSet);
        }
    }

    public List getSpecifiedPairs()
    {
        List dataPairs = new ArrayList();

        for (int i = 0; i < maxLength; i++)
        {
            try
            {
                String xText = inputFields[0][i].getText();
                String yText = inputFields[1][i].getText();
                double x = Double.parseDouble(xText);
                double y = Double.parseDouble(yText);
                dataPairs.add(new DataPair(new Double(x), new Double(y)));
            }
            catch (NumberFormatException e)
            {

            }
        }

        return dataPairs;
    }

    public void clear()
    {
        for (int i = 0; i < maxLength; i++)
        {
            inputFields[0][i].setText("");
            inputFields[1][i].setText("");
        }
    }

    public boolean importData(DataSet dataSet)
    {

        Vector yVars = dataSet.getYVariables();
        Vector xVars = dataSet.getXVariables();
        if (yVars.size() == 0 || xVars.size() == 0)
        {
            JOptionPane.showMessageDialog(this, "No x or y variable in data file!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Variable xVar = (Variable) xVars.get(0);
        Variable yVar = (Variable) yVars.get(0);

        if (xVar.getType() != Variable.NUMERIC && yVar.getType() != Variable.NUMERIC)
        {
            JOptionPane.showMessageDialog(this, "X and y variables must be numeric!");
            return false;
        }
        double[] yValues = ((Variable) yVars.get(0)).getNumValues();
        double[] xValues = ((Variable) xVars.get(0)).getNumValues();

        if (xValues.length > 30)
        {
            JOptionPane.showMessageDialog(this, "At most 30 y values are allowed!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        for (int i = 0; i < yValues.length; i++)
        {
            inputFields[0][i].setText(String.valueOf(xValues[i]));
            inputFields[1][i].setText(String.valueOf(yValues[i]));
        }


        return true;
    }
}
