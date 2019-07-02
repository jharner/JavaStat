/*
 * TrialDefPanel.java
 *
 * Created on March 13, 2002, 10:32 AM
 */

package wvustat.simulation.fivestep;

import wvustat.simulation.model.*;
import wvustat.swing.StackLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author  hxue
 * @version
 */
public class TrialDefPanel extends JPanel implements java.awt.event.ActionListener, IStepPanel
{
    private JComboBox trialList;
    private JLabel prompt;
    private JTextField inputField;
    //private String[] choices;
    private JPanel inputPanel;
    private Map nameToIndexMap = new HashMap();

    /** Creates new TrialDefPanel */
    public TrialDefPanel()
    {


        for (int i = 0; i < IGenerator.trial_names.length; i++)
        {
            if (IGenerator.trial_names[i].equals("") == false)
                nameToIndexMap.put(IGenerator.trial_names[i], new Integer(i));
        }
        initComponents();
    }

    public void updatePanel()
    {
        if (FiveStep.getGenerator() instanceof PairedCategoricalDataGenerator)
        {
            trialList.removeAllItems();
            trialList.addItem(IGenerator.trial_names[IGenerator.PERMUTE]);

        }
        else if (FiveStep.getGenerator() instanceof PairedDataGenerator ||
                FiveStep.getGenerator() instanceof TwoSampleGenerator)
        {
            if (trialList.getItemCount() != 3)
                setupTwoSampleList();
        }
        else
        {
            if (trialList.getItemCount() != BoxModel.trial_names.length)
            {
                setupOneSampleList();
                if (FiveStep.getGenerator() instanceof GeometricGenerator)
                {
                    trialList.setSelectedIndex(IGenerator.DRAW_UNTIL_X);
                    trialList.setEditable(false);
                }
                else if (FiveStep.getGenerator().isDepletable() == false)
                {
                    trialList.setSelectedIndex(IGenerator.DRAW_N_W_REP);
                    trialList.setEditable(false);
                }
            }
        }
    }

    private void setupOneSampleList()
    {
        trialList.removeAllItems();
        trialList.setModel(new DefaultComboBoxModel(BoxModel.trial_names));
    }

    private void setupTwoSampleList()
    {
        trialList.removeAllItems();
        trialList.addItem("");
        trialList.addItem(IGenerator.trial_names[IGenerator.BOOTSTRAP]);
        trialList.addItem(IGenerator.trial_names[IGenerator.RANDOMIZE]);
    }

    private int itemToConstant(Object item)
    {
        Object intObj = nameToIndexMap.get(item);
        if (intObj == null)
            return -1;
        else
            return ((Integer) intObj).intValue();
    }

    private void initComponents()
    {
        trialList = new JComboBox();
        trialList.addActionListener(this);

        prompt = new JLabel("n= ");
        inputField = new JTextField(4);


        setLayout(new StackLayout(8));
        JLabel title = new JLabel("2. Define one simulation");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.black);

        this.add(title);
        this.add(new JLabel("Define the sample:"));
        this.add(trialList);

        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.add(prompt, new GridBagConstraints(0, 0, -1, 0, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        inputPanel.add(inputField, new GridBagConstraints(1, 0, 0, 0, 0.7, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        this.setBorder(new javax.swing.border.EmptyBorder(4, 8, 4, 4));
    }

    public int getSelectedIndex()
    {
        return itemToConstant(trialList.getSelectedItem());
    }

    public double getInput()
    {
        double ret;

        try
        {
            ret = Double.parseDouble(inputField.getText());
            return ret;
        }
        catch (NumberFormatException nfe)
        {
            ret = Double.NaN;
            return ret;
        }
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        int index = this.getSelectedIndex();

        if (index == IGenerator.DRAW_N_W_REP || index == IGenerator.DRAW_N_WO_REP)
        {
            prompt.setText("n= ");
            this.add(inputPanel);
        }
        else if (index == IGenerator.DRAW_UNTIL_X)
        {
            prompt.setText("X= ");
            this.add(inputPanel);
        }
        else if (index == IGenerator.BOOTSTRAP)
        {
            this.remove(inputPanel);
        }
        else if (index == IGenerator.RANDOMIZE)
        {
            this.remove(inputPanel);
        }
        else
        {
            this.remove(inputPanel);
        }

        invalidate();
        revalidate();
        repaint();
    }

    public void reset()
    {
        if (trialList.getItemCount() > 0)
            trialList.setSelectedIndex(0);
        inputField.setText("");
        this.remove(inputPanel);
    }
}
