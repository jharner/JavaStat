package wvustat.simulation.fivestep;

import wvustat.swing.IntegerField;
import wvustat.swing.JEditableLabel;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.util.DataFileUtils;
import wvustat.simulation.model.DataPair;
import wvustat.statistics.InvalidDataError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Arrays;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.CellConstraints;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Oct 19, 2003
 * Time: 1:51:20 PM
 * To change this template use Options | File Templates.
 */
public class JContingencyTablePanel extends JPanel implements ActionListener
{
    private FiveStepMediator mediator;
    private JTextField[][] inputFields;
    //private int maxLength = 30;
    private int numOfRows = 15;
    private boolean DEBUG=false;
    private List xLevels=new ArrayList(), yLevels=new ArrayList();
    private int numOfColumns=6;
    private JEditableLabel[] rowLabels;
    private JEditableLabel[] colLabels;

    public JContingencyTablePanel(FiveStepMediator mediator)
    {
        this.mediator = mediator;
        setLayout(new BorderLayout());
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private Container createTablePanel()
    {
        inputFields=new JTextField[numOfRows][numOfColumns];

        for (int i = 0; i < inputFields.length; i++)
        {
            for(int j=0;j<inputFields[i].length;j++)
            {
                inputFields[i][j]=new IntegerField(4);
            }
        }

        rowLabels=new JEditableLabel[numOfRows];
        colLabels=new JEditableLabel[numOfColumns];

        for(int i=0;i<rowLabels.length;i++)
            rowLabels[i]=new JEditableLabel(String.valueOf(i+1));

        for(int i=0;i<colLabels.length;i++)
            colLabels[i]=new JEditableLabel(String.valueOf(i+1));

        ColumnSpec[] colSpecs=new ColumnSpec[2*numOfColumns+1];
        for (int i = 0; i < colSpecs.length; i++)
        {
            if(i%2==0)
                colSpecs[i]=new ColumnSpec("40dlu");
            else if(i==1)
                colSpecs[i]=new ColumnSpec("8dlu");
            else
                colSpecs[i]=new ColumnSpec("4dlu");

        }
        RowSpec[] rowSpecs=new RowSpec[2*numOfRows+3];
        for (int i = 0; i < rowSpecs.length; i++)
        {
            if(i==3)
                rowSpecs[i]=new RowSpec("4dlu");
            else if(i%2==0)
                rowSpecs[i]=new RowSpec("pref");
            else
                rowSpecs[i]=new RowSpec("2dlu");

        }
        JPanel panel=new JPanel();
        FormLayout formLayout=new FormLayout(colSpecs, rowSpecs);
        panel.setLayout(formLayout);
        CellConstraints cc=new CellConstraints();
        panel.add(new JLabel(""), cc.xy(1,1));
        CellConstraints cc2=new CellConstraints(3,1,CellConstraints.CENTER, CellConstraints.DEFAULT);
        panel.add(new JLabel("Y-Category",JLabel.CENTER), cc2.xywh(3,1,2*numOfColumns-1, 1));
        panel.add(new JLabel("X-Category"), cc.xy(1,3));
        for(int i=0;i<numOfColumns;i++)
        {
            panel.add(colLabels[i], cc.xy(2*i+3,3));
        }

        for(int i=0;i<numOfRows;i++)
        {
            for(int j=0;j<=numOfColumns;j++)
            {
                if(j==0)
                    panel.add(rowLabels[i], cc.xy(1,2*i+5));
                else
                    panel.add(inputFields[i][j-1], cc.xy(2*j+1, 2*i+5));
            }
        }

        return panel;
    }

    private Component createBottomPanel()
    {
        Box panel = Box.createHorizontalBox();
        JButton button = new JButton("Clear model");
        button.setActionCommand("CLEAR_MODEL");
        button.addActionListener(this);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(4,4)));
        JButton bData=new JButton("Data...");
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
        else if(evt.getActionCommand().equals("IMPORT_DATA"))
        {
            DataSet dataSet = DataFileUtils.openFileAsDataSet(this);
            if(dataSet!=null)
                importData(dataSet);
        }
    }

    private List collectData() throws InvalidDataError
    {
        int specifiedRows=0;
        int specifiedColumns=0;
        for(int i=0;i<numOfRows;i++)
        {
            for(int j=0;j<numOfColumns;j++)
            {
                String text=inputFields[i][j].getText();
                if("".equals(text)==false)
                {
                    if(specifiedRows<i+1)
                        specifiedRows=i+1;
                    if(specifiedColumns<j+1)
                        specifiedColumns=j+1;
                }
            }
        }

        xLevels.clear();
        yLevels.clear();
        for(int i=0;i<specifiedRows;i++)
        {
            if(xLevels.contains(rowLabels[i].getText()))
                throw new InvalidDataError("X-categories must be unique!\n");
            xLevels.add(rowLabels[i].getText());
        }

       for(int i=0;i<specifiedColumns;i++)
        {
            if(yLevels.contains(colLabels[i].getText()))
                throw new InvalidDataError("Y-categories must be unique!\n");
            yLevels.add(colLabels[i].getText());
        }

        List dataList=new ArrayList();

        for(int i=0;i<specifiedRows;i++)
        {
            for(int j=0;j<specifiedColumns;j++)
            {
                String text=inputFields[i][j].getText();
                if("".equals(text))
                {
                    throw new InvalidDataError("Data are missing for the following X-Category Y-Category combinations:"+rowLabels[i].getText()+":"+colLabels[j].getText()+"\n");
                }

                int count=((IntegerField)inputFields[i][j]).getIntValue();
                for(int k=0;k<count;k++)
                    dataList.add(new DataPair(new Double(i), new Double(j)));
            }
        }

        return dataList;
    }

    public List getSpecifiedPairs() throws InvalidDataError
    {
        return collectData();
    }

    public void clear()
    {
        for(int i=0;i<numOfRows;i++)
            rowLabels[i].setText(String.valueOf(i+1));

        for(int i=0;i<numOfColumns;i++)
            colLabels[i].setText(String.valueOf(i+1));

        for(int i=0;i<numOfRows;i++)
        {
            for(int j=0;j<numOfColumns;j++)
            {
               inputFields[i][j].setText("");
            }
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
        Variable fVar=dataSet.getFreqVariable();

        if (xVar.getType() != Variable.NUMERIC && yVar.getType() != Variable.NUMERIC)
        {
            JOptionPane.showMessageDialog(this, "X and y variables must be numeric!");
            return false;
        }
        double[] yValues = ((Variable) yVars.get(0)).getNumValues();
        double[] xValues = ((Variable) xVars.get(0)).getNumValues();
        double[] freq=null;
        if(fVar==null)
        {
            freq=new double[xValues.length];
            Arrays.fill(freq, 1);
        }
        else
            freq=fVar.getNumValues();

        if (xValues.length > 30)
        {
            JOptionPane.showMessageDialog(this, "At most 30 y values are allowed!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        for (int i = 0; i < xValues.length; i++)
        {
            inputFields[0][i].setText(String.valueOf((int)freq[i]));
            inputFields[1][i].setText(String.valueOf(xValues[i]));
            inputFields[2][i].setText(String.valueOf(yValues[i]));
        }


        return true;
    }

    public List getXLevels()
    {
        return xLevels;
    }

    public List getYLevels()
    {
        return yLevels;
    }
}
