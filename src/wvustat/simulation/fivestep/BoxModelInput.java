/*
 * BoxModelInput.java
 *
 * Created on March 12, 2002, 2:02 PM
 */

package wvustat.simulation.fivestep;

import gnu.trove.TIntArrayList;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.plot.Histogram;
import wvustat.simulation.model.*;
import wvustat.statistics.InvalidDataError;
import wvustat.util.ComponentUtil;
import wvustat.util.DataFileUtils;
import wvustat.util.IntOnlyField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class BoxModelInput extends JComponent implements java.awt.event.ActionListener
{
    //private String dataUrl = "http://ideal.stat.wvu.edu/ideal/datasets/";
    private JTextField[][] fields;

    private JLabel param1Label, param2Label;
    private JTextField param1Input, param2Input;
    private JComboBox randomList;
    private Container paramPanel;
    private IGenerator randomGen;
    private JPanel inputPanel;
    private java.util.List changeListeners = new ArrayList();

    private String modelName = "";
    private String[] modelNames = {"", "Normal", "Binomial", "Geometric", "Poisson", "", "Data..."};
    private String[] modelNames2 = {"", "Normal", "Binomial", "Geometric", "Poisson"};//add by djluo
    private JButton adjustModelBttn;
    private double adjustAmount;
    private int maxLength = 30;

    private boolean modelAdjusted;
    private JPanel sample1Panel, sample2Panel;
    private boolean twoSamples = false;
    private FiveStepMediator mediator;
    private boolean modelChanged = false;

    /** Creates new BoxModelInput */
    public BoxModelInput(FiveStepMediator mediator)
    {
        this.mediator = mediator;
        setBackground(Color.lightGray);
        fields = new JTextField[2][maxLength];
        DataEnteredAction da = new DataEnteredAction();
        for (int i = 0; i < fields.length; i++)
        {
            for (int j = 0; j < fields[i].length; j++)
            {
                if (i == 0)
                    fields[i][j] = new IntOnlyField("1", 4);
                else
                    fields[i][j] = new JTextField(4);

                fields[i][j].setAction(da);
            }
        }

        initComponents();
    }

    public IGenerator getRandomGenerator() throws Exception
    {
        String modelName = (String) randomList.getSelectedItem();

        if (modelName.equals(modelNames[1]))
        {
            randomGen = new NormalGenerator();
            double[] array = new double[2];
            array[0] = Double.parseDouble(param1Input.getText());
            array[1] = Double.parseDouble(param2Input.getText());
            randomGen.setParam(array);
        }
        else if (modelName.equals(modelNames[2]))
        {
            randomGen = new BinomialGenerator();

            Double d = new Double(param1Input.getText());

            randomGen.setParam(d);
        }
        else if (modelName.equals(modelNames[3]))
        {
            randomGen = new GeometricGenerator();

            Double d = new Double(param1Input.getText());

            randomGen.setParam(d);
        }
        else if (modelName.equals(modelNames[4]))
        {
            randomGen = new PoissonGenerator();

            Double d = new Double(param1Input.getText());

            randomGen.setParam(d);
        }
        else
        {
            if (twoSamples)
            {
                randomGen = createTwoSampleGenerator();
            }
            else
            {
                BoxModel boxModel = new BoxModel();
                boxModel.setBoxes(getInput());
                randomGen = boxModel;
            }
        }


        return randomGen;
    }

    private TwoSampleGenerator createTwoSampleGenerator()
    {
        TIntArrayList indices1 = new TIntArrayList();
        TIntArrayList indices2 = new TIntArrayList();
        for (int i = 0; i < 15; i++)
        {
            if (!fields[0][i].getText().equals("") && !fields[1][i].getText().equals(""))
            {
                indices1.add(i);
            }
        }

        for (int i = 15; i < maxLength; i++)
        {
            if (!fields[0][i].getText().equals("") && !fields[1][i].getText().equals(""))
            {
                indices2.add(i);
            }
        }

        if (indices1.size() == 0 || indices2.size() == 0)
        {
            JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        double[][] data1 = new double[2][indices1.size()];
        for (int i = 0; i < indices1.size(); i++)
        {
            int index = indices1.get(i);
            data1[0][i] = Integer.parseInt(fields[0][index].getText());
            data1[1][i] = Double.parseDouble(fields[1][index].getText());

        }

        double[][] data2 = new double[2][indices2.size()];
        for (int i = 0; i < indices2.size(); i++)
        {
            int index = indices2.get(i);
            data2[0][i] = Integer.parseInt(fields[0][index].getText());
            data2[1][i] = Double.parseDouble(fields[1][index].getText());
        }

        BoxModel model1 = new BoxModel();
        BoxModel model2 = new BoxModel();
        model1.setBoxes(data1);
        model2.setBoxes(data2);
        return new TwoSampleGenerator(model1, model2);
    }

    public void addChangeListener(ChangeListener changeListener)
    {
        changeListeners.add(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener)
    {
        changeListeners.remove(changeListener);
    }

    protected void fireStateChanged()
    {
        ChangeEvent evt = new ChangeEvent(this);
        for (int i = 0; i < changeListeners.size(); i++)
        {
            ((ChangeListener) changeListeners.get(i)).stateChanged(evt);
        }
    }

    private void initComponents()
    {
        inputPanel = new JPanel(new GridLayout(1, 2, 10, 2));

        sample1Panel = new JPanel(new GridLayout(16, 2, 4, 2));
        sample2Panel = new JPanel(new GridLayout(16, 2, 4, 2));

        sample1Panel.add(new JLabel("Count"));
        sample1Panel.add(new JLabel("Value"));
        sample2Panel.add(new JLabel("Count"));
        sample2Panel.add(new JLabel("Value"));
        for (int i = 0; i < 30; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                if (i < 15)
                    sample1Panel.add(fields[j][i]);
                else
                {
                    sample2Panel.add(fields[j][i]);
                }
            }
        }

        inputPanel.add(sample1Panel, BorderLayout.WEST);
        inputPanel.add(sample2Panel, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(2,6,2,6));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        JButton button3 = new JButton("Clear Model");

        adjustModelBttn = new JButton("Adjust Model");
        JButton button1 = new JButton("Show Box");
        JButton button2 = new JButton("Histogram");
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        adjustModelBttn.addActionListener(this);
        buttonPanel.add(button3, new GridBagConstraints(0, 0, 1, 0, 0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 4), 0, 0));
        buttonPanel.add(adjustModelBttn, new GridBagConstraints(1, 0, 1, 0, 0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 4), 0, 0));
        buttonPanel.add(button1, new GridBagConstraints(2, 0, -1, 0, 0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 4), 0, 0));
        buttonPanel.add(button2, new GridBagConstraints(3, 0, 0, 0, 0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 4), 0, 0));

        this.setLayout(new GridBagLayout());
        JLabel title = new JLabel("1. Define the box model");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.black);

        //this.add(title, new GridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(inputPanel, new GridBagConstraints(0, 0, 0, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        this.add(buttonPanel, new GridBagConstraints(0, 1, 0, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        JLabel orLabel = new JLabel("Or");
        orLabel.setFont(new Font("Dialog", Font.BOLD, 14));

        
        /* modified by djluo 7/18/2005 
        randomList = new JComboBox(modelNames);          
         */
        if(FiveStep.trustedEnv == true)
        		randomList = new JComboBox(modelNames);
        else randomList = new JComboBox(modelNames2);
        
        
        randomList.addActionListener(this);

        this.add(orLabel, new GridBagConstraints(0, 2, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(new JLabel("Generate values from: "), new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(randomList, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        param1Label = new JLabel("mu= ");
        param2Label = new JLabel("sigma= ");

        param1Input = new JTextField(4);
        param2Input = new JTextField(4);

        //paramPanel=new JPanel(new GridLayout(1,4,2,2));
        Dimension div = new Dimension(20, 6);
        paramPanel = Box.createHorizontalBox();
        paramPanel.add(Box.createRigidArea(div));
        paramPanel.add(param1Label);//, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE,  new Insets(2,2,2,2), 0,0));
        paramPanel.add(param1Input);//, new GridBagConstraints(1,0, 1, 1, 0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        paramPanel.add(Box.createRigidArea(div));
        paramPanel.add(param2Label);//, new GridBagConstraints(2,0,-1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE,  new Insets(2,20,2,2), 0,0));
        paramPanel.add(param2Input);//, new GridBagConstraints(3,0, 0, 1, 0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));

        this.add(paramPanel, new GridBagConstraints(2, 3, 0, 0, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        paramPanel.setVisible(false);

        this.setBorder(new javax.swing.border.EmptyBorder(4, 4, 4, 4));

    }


    public double[][] getInput() throws InvalidDataError
    {
        java.util.Vector indices = new java.util.Vector(16);

        for (int i = 0; i < fields[0].length; i++)
        {
            if (!fields[0][i].getText().equals("") && !fields[1][i].getText().equals(""))
            {
                indices.addElement(new Integer(i));
            }
        }

        if (indices.size() == 0)
            throw new InvalidDataError("No box is specified!");

        double[][] ret = new double[2][indices.size()];

        try
        {
            for (int j = 0; j < indices.size(); j++)
            {
                int index = ((Integer) indices.elementAt(j)).intValue();
                ret[0][j] = Integer.parseInt(fields[0][index].getText());
                ret[1][j] = Double.parseDouble(fields[1][index].getText());
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println(e.getMessage());
            throw new InvalidDataError("Invalid input for box model!");
        }

        return ret;
    }

    public void setTwoSamplesOption()
    {
        sample1Panel.setBorder(BorderFactory.createLineBorder(Color.red));
        sample2Panel.setBorder(BorderFactory.createLineBorder(Color.blue));
        twoSamples = true;

    }

    public void disableTwoSamplesOption()
    {
        sample1Panel.setBorder(null);
        sample2Panel.setBorder(null);
        twoSamples = false;
    }


    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        String cmd = p1.getActionCommand();

        if (cmd.equals("Show Box"))
        {
            try
            {
                BoxDialog bDialog = new BoxDialog(ComponentUtil.getTopComponent(this), this.getInput());

                bDialog.show();
            }
            catch (InvalidDataError err)
            {
                System.out.println(err);
            }
        }
        else if (cmd.equals("Histogram"))
        {
            try
            {
                BoxModel boxModel = new BoxModel();
                double[][] input = this.getInput();
                boxModel.setBoxes(input);
                double[] cards = boxModel.getCards();

                Histogram plot = new Histogram();
                plot.setData(cards);
                JDialog jd = new JDialog(ComponentUtil.getTopComponent(this), "Box Histogram", true);
                jd.getContentPane().add(plot, BorderLayout.CENTER);
                jd.pack();

                ComponentUtil.setLocationRelativeTo(jd, jd.getOwner());
                jd.show();
            }
            catch (InvalidDataError err)
            {
                System.out.println(err);
            }
        }
        else if (cmd.equals("Adjust Model"))
        {

            JAdjustModelPanel adjustPanel = new JAdjustModelPanel();
            int option = JOptionPane.showOptionDialog(this, adjustPanel, "Adjust model", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (option == JOptionPane.OK_OPTION)
            {
                double d = adjustPanel.getInput();

                if (Double.isNaN(d))
                {
                    JOptionPane.showMessageDialog(this, "Invalid input!");
                    return;
                }
                modelAdjusted = true;
                adjustModel(d);
                adjustAmount = d;
                adjustModelBttn.setText("Reset Model");
                adjustModelBttn.setActionCommand("Reset");
                fireStateChanged();
            }
        }
        else if (cmd.equals("Reset"))
        {
            adjustModel(-adjustAmount);
            adjustAmount = 0.0;
            modelAdjusted = false;
            adjustModelBttn.setText("Adjust Model");
            adjustModelBttn.setActionCommand("Adjust Model");
            fireStateChanged();
        }
        else if (cmd.equals("Clear Model"))
        {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear everything?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION)
            {
                mediator.reset();
            }
            fireStateChanged();
        }

        String name = (String) randomList.getSelectedItem();
        if (modelName.equals(name) == false)
        {
            param1Input.setText("");
            param2Input.setText("");
        }

        if (name.equals(modelNames[1]))
        {
            param1Label.setText("mu= ");
            param2Label.setText("sigma= ");
            param2Label.setVisible(true);
            param2Input.setVisible(true);
            paramPanel.setVisible(true);
            inputPanel.setEnabled(false);
            this.revalidate();
        }
        else if (name.equals(modelNames[2]))
        {
            param1Label.setText("P= ");
            param2Label.setVisible(false);
            param2Input.setVisible(false);
            paramPanel.setVisible(true);
            inputPanel.setEnabled(false);
            this.revalidate();
        }
        else if (name.equals(modelNames[3]))
        {
            param1Label.setText("P= ");
            param2Label.setVisible(false);
            param2Input.setVisible(false);
            paramPanel.setVisible(true);
            inputPanel.setEnabled(false);
            this.revalidate();
        }
        else if (name.equals(modelNames[4]))
        {
            param1Label.setText("Lambda= ");
            param2Label.setVisible(false);
            param2Input.setVisible(false);
            paramPanel.setVisible(true);
            inputPanel.setEnabled(false);
            this.revalidate();
        }
        else if (name.equals(modelNames[6]))
        {
            DataSet dataSet = DataFileUtils.openFileAsDataSet(this);
            if (dataSet != null)
            {
                Vector yVars = dataSet.getYVariables();
                Vector xVars = dataSet.getXVariables();
                if (yVars.size() == 0)
                {
                    JOptionPane.showMessageDialog(this, "No y variable in data file!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double[] yValues = ((Variable) yVars.get(0)).getNumValues();
                Variable freqVar = dataSet.getFreqVariable();
                double freqs[]=null;
                if (freqVar == null)
                {
                    freqs=new double[yValues.length];
                    for(int i=0;i<freqs.length;i++)
                        freqs[i]=1;
                }
                else
                   freqs = freqVar.getNumValues();

                if (yValues.length > 30)
                {
                    JOptionPane.showMessageDialog(this, "At most 30 y values are allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (xVars.size() == 0)
                {
                    for (int i = 0; i < fields[0].length; i++)
                    {
                        fields[0][i].setText("1");
                        fields[1][i].setText("");
                    }
                    for (int i = 0; i < yValues.length; i++)
                    {
                        if (freqs != null)
                            fields[0][i].setText(NumberFormat.getInstance().format(freqs[i]));
                        fields[1][i].setText(String.valueOf(yValues[i]));
                    }
                }
                else
                {
                    Variable xVar = (Variable) xVars.get(0);
                    if (xVar.getType() == Variable.CATEGORICAL)
                    {
                        String[] levels = xVar.getDistinctCatValues();
                        String[] xValues = xVar.getCatValues();

                        if (levels.length == 2)
                        {
                            int sample1Index = 0;
                            int sample2Index = 15;
                            for (int i = 0; i < xValues.length; i++)
                            {
                                if (xValues[i].equals(levels[0]))
                                {
                                    fields[0][sample1Index].setText(String.valueOf(freqs[i]));
                                    fields[1][sample1Index++].setText(String.valueOf(yValues[i]));
                                    if (sample1Index >= 15)
                                    {
                                        JOptionPane.showMessageDialog(this, "Too many values in group 1!", "Error", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                                else
                                {
                                    fields[0][sample2Index].setText(String.valueOf(freqs[i]));
                                    fields[1][sample2Index++].setText(String.valueOf(yValues[i]));
                                    if (sample2Index >= 15)
                                    {
                                        JOptionPane.showMessageDialog(this, "Too many values in group 1!", "Error", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                            }

                            setTwoSamplesOption();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "X variable is numerical!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        else
        {
            paramPanel.setVisible(false);
            inputPanel.setEnabled(true);
            this.revalidate();
        }

        if (modelName.equals(name) == false)
        {
            modelName = name;
            fireStateChanged();
        }
    }

    private void adjustModel(double d)
    {
        NumberFormat nf = NumberFormat.getInstance();
        for (int i = 0; i < fields[1].length; i++)
        {
            try
            {
                double value = Double.parseDouble(fields[1][i].getText());
                fields[1][i].setText(nf.format(value + d));
            }
            catch (NumberFormatException ex)
            {

            }
        }
    }

    public void reset()
    {
        randomList.setSelectedIndex(0);
        paramPanel.setVisible(false);
        adjustModelBttn.setText("Adjust Model");
        adjustModelBttn.setActionCommand("Adjust Model");
        adjustAmount = 0;
        for (int i = 0; i < fields[0].length; i++)
        {
            fields[0][i].setText("1");
        }

        for (int i = 0; i < fields[1].length; i++)
        {
            fields[1][i].setText("");
        }
        disableTwoSamplesOption();
        revalidate();
    }


    public String getModelDescription()
    {
        try
        {
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setMaximumFractionDigits(4);
            StringBuffer buf = new StringBuffer();

            if (twoSamples)
            {
                TwoSampleGenerator twoSample = (TwoSampleGenerator) randomGen;
                buf.append("N1= " + twoSample.getSample1().getCapacity());
                buf.append(", Mean1= " + nf.format(twoSample.getSample1().getAverage()));
                buf.append(", SD1= " + nf.format(twoSample.getSample1().getSD()));
                buf.append(", N2= " + twoSample.getSample2().getCapacity());
                buf.append(", Mean2= " + nf.format(twoSample.getSample2().getAverage()));
                buf.append(", SD2= " + nf.format(twoSample.getSample2().getAverage()));
            }
            else
            {
                randomGen = getRandomGenerator();
                if(randomGen.getCapacity()<Integer.MAX_VALUE)
                    buf.append("N=" + randomGen.getCapacity()+", ");
                buf.append("Mean=");
                buf.append(nf.format(randomGen.getAverage() - adjustAmount));
                buf.append(", SD=" + nf.format(randomGen.getSD()));
                if (modelAdjusted)
                {
                    buf.append("; Adjusted Mean=");
                    buf.append(nf.format(randomGen.getAverage()));
                }
            }

            return buf.toString();
        }
        catch (Exception e)
        {
            return "Model not defined:" + e;
        }
    }

    public boolean isModelChanged()
    {
        return modelChanged;
    }

    class DataEnteredAction extends AbstractAction
    {
        public void actionPerformed(java.awt.event.ActionEvent e)
        {
            modelChanged = true;
            fireStateChanged();
        }
    }
}
