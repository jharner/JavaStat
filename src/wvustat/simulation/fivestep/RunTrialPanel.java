/*
 * RunTrialPanel.java
 *
 * Created on March 13, 2002, 2:49 PM
 */

package wvustat.simulation.fivestep;

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import wvustat.simulation.model.StatComputer;
import wvustat.simulation.model.PairedDataGenerator;

/**
 *
 * @author  hxue
 * @version
 */
public class RunTrialPanel extends JComponent implements ActionListener
{
    private JLabel avgLabel, seLabel, nLabel;
    private JTextArea currentOutcome;
    private JTextArea statHistory;
    private JRadioButton fastCheck, slowCheck;
    private JLabel statLabel;
    private ActionListener buttonListener;
    private JButton btn1, btn2, btn3, btn4, btn5, btn6;
    private JButton stopBtn, resetBtn;
    private JProgressBar pBar;
    private JLabel progressNote;
    private boolean isStopped = false;
    private JLabel sucLabel, failLabel, totalLabel, sProbLabel, fProbLabel, tProbLabel;
    private JPanel panel7, panel2, panel6;

    private StatComputer computeClass;

    /** Creates new RunTrialPanel */
    public RunTrialPanel(ActionListener listener, StatComputer computeClass)
    {
        this.buttonListener = listener;
        this.computeClass = computeClass;
        initComponents();
    }

    private void initComponents()
    {
        setLayout(new GridBagLayout());

        avgLabel = new JLabel("N/A");
        avgLabel.setForeground(Color.blue);
        seLabel = new JLabel("N/A");
        seLabel.setForeground(Color.blue);
        nLabel = new JLabel("N/A");
        nLabel.setForeground(Color.blue);

        sucLabel = new JLabel("N/A");
        sucLabel.setForeground(Color.blue);
        failLabel = new JLabel("N/A");
        failLabel.setForeground(Color.blue);
        totalLabel = new JLabel("N/A");
        totalLabel.setForeground(Color.blue);

        sProbLabel = new JLabel("N/A");
        sProbLabel.setForeground(Color.blue);
        fProbLabel = new JLabel("N/A");
        fProbLabel.setForeground(Color.blue);
        tProbLabel = new JLabel("N/A");
        tProbLabel.setForeground(Color.blue);

        statLabel = new JLabel("Statistic");

        currentOutcome = new JTextArea(1, 20);
        statHistory = new JTextArea(6, 10);
        currentOutcome.setEditable(false);
        statHistory.setEditable(false);

        btn1 = new JButton("1");
        btn2 = new JButton("10");
        btn3 = new JButton("100");
        btn4 = new JButton("1000");
        btn6 = new JButton("5000");
        btn5 = new JButton("10,000");
        btn5.setActionCommand("10000");
        btn1.addActionListener(buttonListener);
        btn2.addActionListener(buttonListener);
        btn3.addActionListener(buttonListener);
        btn4.addActionListener(buttonListener);
        btn5.addActionListener(buttonListener);
        btn6.addActionListener(buttonListener);

        JPanel panel1 = new JPanel(new GridBagLayout());
        panel1.add(new JLabel("# of Simulations N: "), new GridBagConstraints(0, 0, 1, 0, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel1.add(btn1, new GridBagConstraints(1, 0, 1, 0, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel1.add(btn2, new GridBagConstraints(2, 0, 1, 0, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel1.add(btn3, new GridBagConstraints(3, 0, 1, 0, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel1.add(btn4, new GridBagConstraints(4, 0, 1, 0, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel1.add(btn6, new GridBagConstraints(5, 0, 1, 0, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel1.add(btn5, new GridBagConstraints(7, 0, 0, 0, 0.1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        //panel1.add(fastCheck, new GridBagConstraints(7,0,0,0,0.2,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
        panel1.setBorder(new javax.swing.border.EtchedBorder());

        fastCheck = new JRadioButton("Fast", true);
        slowCheck = new JRadioButton("Slow", false);
        ButtonGroup bgr = new ButtonGroup();
        bgr.add(fastCheck);
        bgr.add(slowCheck);

        JPanel optionPanel = new JPanel(new GridBagLayout());
        optionPanel.add(new JLabel("Speed: "), new GridBagConstraints(0, 0, 1, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        optionPanel.add(fastCheck, new GridBagConstraints(1, 0, -1, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        optionPanel.add(slowCheck, new GridBagConstraints(2, 0, 0, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        panel2 = new JPanel(new GridLayout(4, 2, 20, 2));

        JLabel jl = new JLabel("Statistic of Interest");
        Font bold = new Font("Dialog", Font.BOLD, 12);
        jl.setFont(bold);
        panel2.add(jl);

        jl = new JLabel("Summary");
        jl.setFont(bold);
        panel2.add(jl);

        jl = new JLabel("# of Simulations");

        panel2.add(jl);
        panel2.add(nLabel);
        panel2.add(new JLabel("Mean"));
        panel2.add(avgLabel);
        panel2.add(new JLabel("Standard Deviation"));
        panel2.add(seLabel);

        panel6 = new JPanel(new GridBagLayout());

        jl = new JLabel("Event of Interest");
        jl.setFont(bold);
        panel6.add(jl, new GridBagConstraints(0, 0, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jl = new JLabel("Count");
        jl.setFont(bold);
        panel6.add(jl, new GridBagConstraints(1, 0, 1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jl = new JLabel("Experimental Probability");
        jl.setFont(bold);
        panel6.add(jl, new GridBagConstraints(2, 0, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        panel6.add(new JLabel("Success"), new GridBagConstraints(0, 1, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel6.add(sucLabel, new GridBagConstraints(1, 1, 1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel6.add(sProbLabel, new GridBagConstraints(2, 1, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        panel6.add(new JLabel("Failure"), new GridBagConstraints(0, 2, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel6.add(failLabel, new GridBagConstraints(1, 2, 1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel6.add(fProbLabel, new GridBagConstraints(2, 2, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        panel6.add(new JLabel("Total"), new GridBagConstraints(0, 3, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel6.add(totalLabel, new GridBagConstraints(1, 3, 1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel6.add(tProbLabel, new GridBagConstraints(2, 3, 1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        panel2.setBorder(new javax.swing.border.EtchedBorder());
        panel6.setBorder(new javax.swing.border.EtchedBorder());


        panel7 = new JPanel(new GridLayout(1, 2, 2, 8));
        panel7.add(panel2);
        panel7.add(panel6);

        JPanel panel3 = new JPanel(new GridBagLayout());
        panel3.add(new JLabel("Current Simulation:"), new GridBagConstraints(0, 0, 0, -1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel3.add(new JScrollPane(currentOutcome, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), new GridBagConstraints(0, 1, 0, 0, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        JPanel panel4 = new JPanel(new GridBagLayout());
        panel4.add(statLabel, new GridBagConstraints(0, 0, 0, -1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        panel4.add(new JScrollPane(statHistory), new GridBagConstraints(0, 1, 0, 0, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        JLabel title = new JLabel("4. Run simulations");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.black);

        resetBtn = new JButton("Reset");
        resetBtn.addActionListener(buttonListener);
        stopBtn = new JButton("Stop");
        stopBtn.setEnabled(false);
        stopBtn.addActionListener(this);

        pBar = new JProgressBar(0, 100);
        progressNote = new JLabel("           ");

        this.add(title, new GridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(panel1, new GridBagConstraints(0, 1, 0, 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(optionPanel, new GridBagConstraints(0, 2, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(stopBtn, new GridBagConstraints(0, 3, -1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(resetBtn, new GridBagConstraints(1, 3, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(progressNote, new GridBagConstraints(0, 4, 0, 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(pBar, new GridBagConstraints(0, 5, 0, 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(panel7, new GridBagConstraints(0, 6, 0, 1, 1.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        this.add(panel3, new GridBagConstraints(0, 7, 0, -1, 1.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        this.add(panel4, new GridBagConstraints(0, 8, 0, 0, 1.0, 0.8, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        this.setBorder(new javax.swing.border.EmptyBorder(4, 4, 4, 4));
    }

    public boolean isFastRun()
    {
        return fastCheck.isSelected();
    }

    public void showResults(int n, double avg, double se, Object outcome)
    {
        nLabel.setText(String.valueOf(n));

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        avgLabel.setText(nf.format(avg));

        seLabel.setText(nf.format(se));

        StringBuffer buf = new StringBuffer();

        if (outcome instanceof double[])
        {
            double[] data = (double[]) outcome;


            for (int i = 0; i < data.length; i++)
            {
                buf.append(nf.format(data[i]));
                buf.append("  ");
            }
        }
        else if (outcome instanceof double[][])
        {
            double[][] data = (double[][]) outcome;
            if (computeClass.getNumberGenerator() instanceof PairedDataGenerator)
            {
                for (int i = 0; i < data[0].length; i++)
                {
                    if (i > 0)
                        buf.append(", ");
                    buf.append("(" + data[0][i] + ", " + data[1][i] + ")");
                }
            }
            else
            {

                buf.append("(");
                for (int i = 0; i < data[0].length; i++)
                {
                    if (i > 0)
                        buf.append(", ");
                    buf.append(nf.format(data[0][i]));

                }
                buf.append("), (");
                for (int i = 0; i < data[1].length; i++)
                {
                    if (i > 0)
                        buf.append(", ");
                    buf.append(nf.format(data[1][i]));

                }
                buf.append(")");
            }
        }
        currentOutcome.setText(buf.toString());
    }

    public void updateReport()
    {
        if (StatComputer.getEventIndex() > 0)
        {
            panel6.setVisible(true);
        }
        else
        {
            panel6.setVisible(false);
        }
    }

    private String stringReplace(String string, String substring1, String substring2)
    {
        if (string.indexOf(substring1) >= 0)
        {
            StringBuffer buf = new StringBuffer(string);
            int start = string.indexOf(substring1);
            int end = start + substring1.length();
            buf.replace(start, end, substring2);
            return buf.toString();
        }
        else
            return string;
    }

    public void showEventResults(int eventIndex, int success, int total)
    {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        String eventName = StatComputer.eventChoices[eventIndex];
        String statType = computeClass.getStatType();
        String eventLabelString = stringReplace(eventName, "a", nf.format(computeClass.getA()));
        eventLabelString = stringReplace(eventLabelString, "b", nf.format(computeClass.getB()));
        eventLabelString = stringReplace(eventLabelString, "X", statType);


        statLabel.setText(eventLabelString);

        sucLabel.setText(String.valueOf(success));
        failLabel.setText(String.valueOf(total - success));
        totalLabel.setText(String.valueOf(total));
        sProbLabel.setText(nf.format(success * 1.0 / total));
        fProbLabel.setText(nf.format((total - success) * 1.0 / total));
        tProbLabel.setText("1.00");
    }

    public void showStatName(String name)
    {
        statLabel.setText(name);
    }


    public void showStats(double[] stats)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < stats.length; i++)
        {
            buf.append(nf.format(stats[i]));

            if (i < stats.length - 1)
                buf.append("\n");
        }


        statHistory.setText(buf.toString());
    }

    public void showStats(double[] stats, int[] success)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < stats.length; i++)
        {
            buf.append(nf.format(stats[i]));
            if (success[i] == 1)
                buf.append(" (Success)");
            else
                buf.append(" (Failure)");
            if (i < stats.length - 1)
                buf.append("\n");
        }


        statHistory.setText(buf.toString());
    }

    public void addStat(double stat)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        if (statHistory.getText().equals(""))
            statHistory.setText(nf.format(stat));
        else
            statHistory.setText(statHistory.getText() + "\n" + nf.format(stat));
    }

    public void addStat(double stat, int success)
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        String str = success == 1 ? " (Success)" : " (Failure)";
        if (statHistory.getText().equals(""))
            statHistory.setText(nf.format(stat) + str);
        else
            statHistory.setText(statHistory.getText() + "\n" + nf.format(stat) + str);
    }

    public void clearAll()
    {
        currentOutcome.setText("");
        statHistory.setText("");
        avgLabel.setText("N/A");
        seLabel.setText("N/A");
        nLabel.setText("0");
        sucLabel.setText("N/A");
        failLabel.setText("N/A");
        totalLabel.setText("N/A");
        sProbLabel.setText("N/A");
        fProbLabel.setText("N/A");
        tProbLabel.setText("N/A");
        pBar.setValue(0);
        statLabel.setText("Statistics");
    }

    public void setEnabled(boolean bl)
    {
        btn1.setEnabled(bl);
        btn2.setEnabled(bl);
        btn3.setEnabled(bl);
        btn4.setEnabled(bl);
        btn5.setEnabled(bl);
        btn6.setEnabled(bl);
        resetBtn.setEnabled(bl);
        stopBtn.setEnabled(!bl);
    }

    public void restart()
    {
        isStopped = false;
        pBar.setValue(0);
    }

    public boolean isStopped()
    {
        return isStopped;
    }

    public void actionPerformed(ActionEvent evt)
    {
        isStopped = true;
        setEnabled(true);
    }

    public void setProgress(int value)
    {
        pBar.setValue(value);
    }

    public void setNote(String note)
    {
        progressNote.setText(note);
    }
}