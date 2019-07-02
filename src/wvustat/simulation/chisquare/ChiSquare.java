/*

 *	ChiSquare.java

 *	author: Ximing Zhao

 *

 */

package wvustat.simulation.chisquare;


import wvustat.plot.Histogram;
import wvustat.simulation.model.StatComputer;
import wvustat.util.AppFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;


public class ChiSquare

        extends javax.swing.JPanel

        implements ActionListener, Runnable
{

    private StatComputer computeClass;

    private Calculate calculate;

    private JPanel tabPane; // big panel to contain different steps

    private CardLayout cardLayout;

    private ModelInput modelInput;// step 1 panel

    private RunTrial runTrial; //step 2 panel

    private FinalResult finalResult;

    private int numOfDraws;// number of trials get from step 2 (n)

    private double boxX; // get from step 2 , use for boxmodel

    private int numOfRuns; // number of runs get from step 4

    private boolean fastRun; //boolean of fast/slow

    private long idleTime = 700; //run time

    private JLabel boxStat; //the bottom label

    private JLabel sumLabel;

    private Histogram histogram; //the histogram

    private JButton prevButton, nextButton; //the buttons

    private int stepIndex = 0; //step index from 0 to 4

    private LinkLabel2[] links;// small labels for steps

    private VariableType outcome;

    private int counter;// for run

    private NumberFormat nf;// for run


    /** Creates new FiveStepFrame */

    public ChiSquare()
    {
	init();
    }


    public void init()
    {

        calculate = new Calculate();

        computeClass = new StatComputer();

        outcome = new VariableType();

        initComponents();

    }


    private void initComponents()
    {

        JPanel linksPanel = new JPanel(new GridBagLayout()); //panel to put step links and buttons


        links = new LinkLabel2[3];

        for (int i = 0; i < links.length; i++)
        {

            links[i] = new LinkLabel2(String.valueOf(i + 1));

            links[i].addActionListener(this);

            linksPanel.add(links[i], new GridBagConstraints(i, 0, 1, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        }

        links[0].setEnabled(true);


        prevButton = new JButton("Prev");

        prevButton.setActionCommand("Previous");

        prevButton.addActionListener(this);

        nextButton = new JButton("Next");

        nextButton.addActionListener(this);


        linksPanel.add(prevButton, new GridBagConstraints(5, 0, -1, 0, 0.5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        linksPanel.add(nextButton, new GridBagConstraints(7, 0, 0, 0, 0.5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        linksPanel.setBorder(new javax.swing.border.EmptyBorder(0, 200, 0, 0));


        cardLayout = new CardLayout();

        tabPane = new JPanel(cardLayout);


        modelInput = new ModelInput(this);

        runTrial = new RunTrial(this);

        finalResult = new FinalResult(computeClass);


        tabPane.add("1", modelInput);

        tabPane.add("2", runTrial);

        tabPane.add("3", finalResult);


        Container content = this;

        content.setLayout(new GridBagLayout());

        content.add(linksPanel, new GridBagConstraints(0, 0, 0, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        content.add(tabPane, new GridBagConstraints(0, 1, 0, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));


        boxStat = new JLabel("Fill in the expected and obtained values.");

        boxStat.setBorder(new javax.swing.border.EmptyBorder(4, 4, 4, 4));

        sumLabel = new JLabel("");

        sumLabel.setBorder(new javax.swing.border.EmptyBorder(4, 4, 4, 4));

        content.add(sumLabel, new GridBagConstraints(0, 2, 0, 1, 1.0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        content.add(boxStat, new GridBagConstraints(0, 3, 0, 1, 1.0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));


    }


    /**

     * Check if the input is valid before going to the next step

     */

    private boolean checkStep()
    {

        int index = stepIndex;



        //Check step 1 and go to step 2

        if (index == 0)
        {

            try
            {

                double[][] model = modelInput.getInput();

                calculate.setModel(model);

                runTrial.setText1(calculate.getExpected(), calculate.getObserved());


            }

            catch (wvustat.statistics.InvalidDataError e)
            {

                JOptionPane.showMessageDialog(this, e.getMessage() + " Can not go to step 2.", "Error", JOptionPane.ERROR_MESSAGE);

                return false;

            }


            showLabel();

            runTrial.clearAll();

            runTrial.showText();

            computeClass.reset();

        }

        return true;

    }


    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {

        Object src = p1.getSource();

        String cmd = p1.getActionCommand();


        if (cmd.equals("Previous"))
        {

            if (checkStep())
            {

                stepIndex--;

                cardLayout.show(tabPane, String.valueOf(stepIndex + 1));

            }

            if (stepIndex == 0)

                prevButton.setEnabled(false);

            if (stepIndex < 2)

                nextButton.setEnabled(true);

        }

        else if (cmd.equals("Next"))
        {

            if (checkStep())
            {

                stepIndex++;

                cardLayout.show(tabPane, String.valueOf(stepIndex + 1));

            }

            if (stepIndex > 0)

                prevButton.setEnabled(true);

            if (stepIndex == 2)
            {

                nextButton.setEnabled(false);

                showLabel2();

                finalResult.setData(calculate.getChisquare(outcome));

            }

            links[stepIndex].setEnabled(true);

        }

        else if (cmd.equals("Clear model"))
        {

            hideLabel();

            runTrial.clearAll();

            computeClass.reset();

        }

        else if (cmd.equals("Reset"))
        {

            runTrial.clearPart();

            computeClass.reset();

        }

        else if (cmd.equals("Clear all"))
        {

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear everything?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION)
            {

                modelInput.reset();

                hideLabel();

                runTrial.clearAll();

                finalResult.reset();

            }

        }

        else if (src instanceof LinkLabel2)
        {

            cardLayout.show(tabPane, cmd);

            stepIndex = Integer.parseInt(cmd) - 1;


            prevButton.setEnabled(stepIndex > 0);

            nextButton.setEnabled(stepIndex < 2);

        }

        else
        {


            runTrial.setEnabled(false);

            runTrial.restart();

            numOfRuns = Integer.parseInt(p1.getActionCommand());

            fastRun = runTrial.isFastRun();

            //computeClass.addRun(numOfRuns);



            Thread thread1 = new Thread(this);

            thread1.start();


        }

    }


    public void run()
    {


        nf = NumberFormat.getInstance();

        nf.setMaximumFractionDigits(2);

        if (fastRun)
        {

            int i = 0;


            while (i < numOfRuns && !runTrial.isStopped())
            {

                outcome.addElement(new Double(calculate.oneSimulation()));



                // 	computeClass.addData(outcome, statDef.getMu(), statDef.getSigma());

                i++;

                counter = i;


                SwingUtilities.invokeLater(new Runnable()
                {

                    public void run()
                    {

                        runTrial.setText2(calculate.getSimulated());

                        runTrial.showResults();

                        runTrial.showStats(calculate.getChisquare(outcome), calculate.getchiSquareObtained());

                    }

                });

                try
                {

                    Thread.currentThread().sleep(50);

                }

                catch (InterruptedException e)
                {

                }

            }


            runTrial.setEnabled(true);


        }

        else
        {

            int i = 0;


            while (i < numOfRuns && !runTrial.isStopped())
            {


                outcome.addElement(new Double(calculate.oneSimulation()));

                //	computeClass.addData(outcome, statDef.getMu(), statDef.getSigma());

                i++;

                counter = i;

                SwingUtilities.invokeLater(new Runnable()
                {

                    public void run()
                    {

                        runTrial.setText2(calculate.getSimulated());

                        runTrial.showResults();

                        runTrial.showStats(calculate.getChisquare(outcome), calculate.getchiSquareObtained());

                    }

                });


                try
                {

                    Thread.currentThread().sleep(idleTime);

                }

                catch (InterruptedException e)
                {

                }


            }

            runTrial.setEnabled(true);


        }


    }


    private void showLabel()
    {

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();

        nf.setMaximumFractionDigits(4);

        StringBuffer buf = new StringBuffer();

        StringBuffer buf2 = new StringBuffer();

        buf.append("Sum(expected)=");

        buf.append(nf.format(calculate.getSum()));

        buf.append(",    Sum(obtained)=" + nf.format(calculate.getSum()));

        sumLabel.setText(buf.toString());


        buf2.append("Obtained Chi-square=");

        buf2.append(nf.format(calculate.getchiSquareRounded()));

        buf2.append(",  " + nf.format(calculate.getLength()) + "--outcome experiment");

        boxStat.setText(buf2.toString());

    }


    private void hideLabel()
    {

        sumLabel.setText("");

        boxStat.setText("Fill in the expected and obtained values.");

    }


    private void showLabel2()
    {

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();

        nf.setMaximumFractionDigits(4);

        StringBuffer buf = new StringBuffer();

        StringBuffer buf2 = new StringBuffer();


        buf2.append(nf.format(calculate.getProbability(calculate.getChisquare(outcome), calculate.getchiSquareObtained())));

        buf2.append(" out of  " + outcome.size());

        buf2.append(" exceed  " + nf.format(calculate.getchiSquareRounded()) + ",   P(Chi-square>=");

        buf2.append(nf.format(calculate.getchiSquareRounded()) + ")=");

        buf2.append(nf.format((double) calculate.getProbability(calculate.getChisquare(outcome), calculate.getchiSquareObtained()) / (double) (outcome.size())));

        sumLabel.setText(buf2.toString());

        ;


        buf.append("Obtained Chi-square=");

        buf.append(nf.format(calculate.getchiSquareRounded()));

        buf.append(",  " + nf.format(calculate.getLength()) + "--outcome experiment");

        boxStat.setText(buf.toString());

    }


    public static void main(String[] args)
    {

        System.setErr(System.out);

        ChiSquare fs = new ChiSquare();

        fs.init();


        AppFrame fr = new AppFrame("Chi-Square");

        fr.getContentPane().add(fs);

        fr.pack();

        fr.show();

    }

}

