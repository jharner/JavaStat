/*
 * StatDefPanel.java
 *
 * Created on March 13, 2002, 2:11 PM
 */

package wvustat.simulation.fivestep;

import wvustat.simulation.model.*;
import wvustat.swing.StackLayout;
import wvustat.swing.JCardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * This component is used to define the statistic in the five step program.
 *
 * @author  Hengyi Xue
 * @version
 */
public class StatDefPanel extends JComponent implements java.awt.event.ActionListener, IStepPanel
{
    public static String SEPARATOR = "                    ";
    private JComboBox statList, eventList;
    private JPanel muPanel;
    private JPanel sigmaPanel;
    private JCardPanel abPanel;
    private JTextField muInput;
    private JTextField sigmaInput;
    private JTextField aInput, bInput;
    private java.util.List statChoices;
    private JLabel bLabel;
    private FiveStepMediator mediator;


    public StatDefPanel(FiveStepMediator mediator)
    {
        this.mediator = mediator;
        initComponents();
    }

    private void setupOneSampleStats()
    {
        statChoices = new ArrayList();
        statChoices.add(" ");
        statChoices.add(StatComputer.AVERAGE);
        statChoices.add(StatComputer.SUM);
        statChoices.add(StatComputer.SD);
        statChoices.add(StatComputer.MEDIAN);
        statChoices.add(StatComputer.IQR);
        statChoices.add(StatComputer.MIN);
        statChoices.add(StatComputer.MAX);
        statChoices.add(SEPARATOR);
        statChoices.add(StatComputer.D);
        statChoices.add(SEPARATOR);
        statChoices.add(StatComputer.SAMPLE_SIZE);
        statChoices.add(StatComputer.THE_DRAW);
        statChoices.add(SEPARATOR);

        statChoices.add(StatComputer.CHI_SQUARE);
        statList.setModel(new DefaultComboBoxModel(statChoices.toArray()));
    }

    private void setupTwoSampleStats(boolean isPaired)
    {
        statChoices = new ArrayList();
        if (isPaired)
            statChoices.add(StatComputer.CORR_COEFF);
        else
            statChoices.add(StatComputer.D);

        statList.setModel(new DefaultComboBoxModel(statChoices.toArray()));
    }

    private void initComponents()
    {
        setLayout(new StackLayout(8));

        statList = new JComboBox();

        statList.addActionListener(this);

        eventList = new JComboBox(new String[]{" ", "X<=a", "X>=a", "X=a", "X>a", "X<a", "a<=X<=b", "a<=X<b", "a<X<=b", "a<X<b"});
        eventList.addActionListener(this);

        muInput = new JTextField(4);
        sigmaInput = new JTextField(4);

        bLabel = new JLabel("b=");
        aInput = new JTextField(4);
        bInput = new JTextField(4);

        muPanel = new JPanel(new GridBagLayout());
        muPanel.add(new JLabel("mu="), new GridBagConstraints(0, 0, -1, 0, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        muPanel.add(muInput, new GridBagConstraints(1, 0, 0, 0, 0.7, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        sigmaPanel = new JPanel(new GridBagLayout());
        sigmaPanel.add(new JLabel("sigma="), new GridBagConstraints(0, 0, -1, 0, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        sigmaPanel.add(sigmaInput, new GridBagConstraints(1, 0, 0, 0, 0.7, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel("a="), new GridBagConstraints(0, 0, -1, -1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(aInput, new GridBagConstraints(1, 0, 0, -1, 0.7, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(bLabel, new GridBagConstraints(0, 1, -1, 0, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel.add(bInput, new GridBagConstraints(1, 1, 0, 0, 0.7, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        abPanel=new JCardPanel(panel);
        abPanel.hideVisiblePanel();

        JLabel label1 = new JLabel("Statistic of Interest (X):");

        JLabel title = new JLabel("3. Define statistic/event of interest");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.black);

        JLabel label2 = new JLabel("Event of Interest (if required):");

        this.add(title);
        this.add(label1);
        this.add(statList);
        this.add(label2);
        this.add(eventList);
        this.add(abPanel);

        this.setBorder(new javax.swing.border.EmptyBorder(4, 8, 4, 4));
    }

    public Object getSelectedItem()
    {
        return statList.getSelectedItem();
    }

    public int getEventIndex()
    {
        return eventList.getSelectedIndex();
    }

    public double getMu()
    {
        double ret;
        try
        {
            ret = Double.parseDouble(muInput.getText());
            return ret;
        }
        catch (Exception e)
        {
            return Double.NaN;
        }
    }

    public double getSigma()
    {
        double ret;
        try
        {
            ret = Double.parseDouble(sigmaInput.getText());
            return ret;
        }
        catch (Exception e)
        {
            return Double.NaN;
        }
    }

    public double getA()
    {
        double ret;
        try
        {
            ret = Double.parseDouble(aInput.getText());
            return ret;
        }
        catch (Exception e)
        {
            return Double.NaN;
        }
    }

    public double getB()
    {
        double ret;
        try
        {
            ret = Double.parseDouble(bInput.getText());
            return ret;
        }
        catch (Exception e)
        {
            return Double.NaN;
        }
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        int index2 = eventList.getSelectedIndex();

        if (p1.getSource() == statList)
        {
            Object obj = statList.getSelectedItem();
            if(obj==null)
                return;
            String statInfo = "";
            NumberFormat formatter = new DecimalFormat("#.###");
            if (obj.equals(StatComputer.AVERAGE))
            {
                statInfo = "Mean= " + formatter.format(mediator.getModel().getAverage());
            }
            else if (obj.equals(StatComputer.SD))
            {
                statInfo = "SD= " + formatter.format(mediator.getModel().getSD());
            }
            else if (obj.equals(StatComputer.D))
            {
                if (mediator.getModel() instanceof TwoSampleGenerator)
                {
                    TwoSampleGenerator twoSample = (TwoSampleGenerator) mediator.getModel();
                    double d = twoSample.getSample1().getAverage() - twoSample.getSample2().getAverage();
                    statInfo = "D0= " + formatter.format(d);
                }

            }

            if (statInfo.equals("") == false)
            {
                mediator.getModelInfoComponent().setText(mediator.getModelInputPanel().getModelDescription() + "; " + statInfo);
            }
        }
        else
        {

            if (index2 >= 1 && index2 <= 5)
            {
                bLabel.setVisible(false);
                bInput.setVisible(false);

                abPanel.showVisiblePanel();
                validate();
            }
            else if (index2 >= 6)
            {
                bLabel.setVisible(true);
                bInput.setVisible(true);
                abPanel.showVisiblePanel();
                validate();
            }
            else
            {
                abPanel.hideVisiblePanel();
            }
        }


    }

    public void reset()
    {
        if(statList.getItemCount()>0)
            statList.setSelectedIndex(0);
        eventList.setSelectedIndex(0);
        aInput.setText("");
        bInput.setText("");
        this.remove(sigmaPanel);
        this.remove(muPanel);
        revalidate();
    }

    public void updatePanel()
    {
        if (FiveStep.getGenerator() instanceof PairedCategoricalDataGenerator)
        {
            statChoices=new ArrayList();
            statChoices.add(StatComputer.CHI_SQUARE);
            statList.setModel(new DefaultComboBoxModel(statChoices.toArray()));
        }
        else if (FiveStep.getGenerator() instanceof PairedDataGenerator ||
                FiveStep.getGenerator() instanceof TwoSampleGenerator)
        {
            setupTwoSampleStats(FiveStep.getGenerator() instanceof PairedDataGenerator);
        }
        else
        {
            setupOneSampleStats();
            if (FiveStep.getGenerator().getOutcomeCount() == 2)
            {
                if (statChoices.contains(StatComputer.P_HAT) == false)
                {
                    statChoices.add(StatComputer.P_HAT);
                    statList.insertItemAt(StatComputer.P_HAT, 1);
                    statList.insertItemAt(SEPARATOR, 2);
                    statList.setSelectedItem(StatComputer.P_HAT);
                }
            }
            else if (statChoices.contains(StatComputer.P_HAT))
            {
                statChoices.remove(StatComputer.P_HAT);
                statList.removeItem(StatComputer.P_HAT);
                statList.removeItem(SEPARATOR);
                statList.setSelectedIndex(0);
            }
        }
    }
}