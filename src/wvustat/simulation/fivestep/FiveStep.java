/*
 * FiveStepFrame.java
 *
 * Created on March 12, 2002, 1:59 PM
 */

package wvustat.simulation.fivestep;

import wvustat.simulation.model.IGenerator;
import wvustat.simulation.model.StatComputer;
import wvustat.util.AppFrame;
import wvustat.swing.SwingWorker;
import wvustat.statistics.CommonComputation;
import wvustat.plot.ManualAxisModel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class FiveStep extends JPanel implements java.awt.event.ActionListener, Runnable
{
    private static StatComputer computeClass;
    private JPanel centerPanel;
    private CardLayout cardLayout;
    private JModelInputPanel modelInputPanel;
    private TrialDefPanel trialDef;
    private StatDefPanel statDef;
    private RunTrialPanel runTrial;
    private StatPanel statPanel;
    private int numOfDraws;
    private double boxX;
    private int numOfRuns;
    private boolean fastRun;
    private long idleTime = 200;
    private JTextArea taModelInfo;
    private JButton prevButton, nextButton;
    private int stepIndex = 0;
    private LinkLabel[] links;
    private Object outcome;

    private int counter;
    private NumberFormat nf;

    private static IGenerator numberGenerator;

    private FiveStepMediator mediator = new FiveStepMediator(this);
    private JButton bPrint;

    //trustedEnv added by djluo 7/18/2005
    public static boolean trustedEnv = true;
    
    public static IGenerator getGenerator()
    {
        return numberGenerator;
    }

    public static StatComputer getStatComputer()
    {
        return computeClass;
    }

    /** Creates new FiveStepFrame */
    public FiveStep()
    {
        init();
    }

    public void init()
    {
        computeClass = new StatComputer();
        initComponents();
    }

    private JPanel createNavigationPanel()
    {
        //JPanel linksPanel = new JPanel(new GridBagLayout());
        Box linksPanel = Box.createHorizontalBox();
        linksPanel.add(new JLabel("Step:  "));
        links = new LinkLabel[5];
        LinkGroup group = new LinkGroup();

        for (int i = 0; i < links.length; i++)
        {

            links[i] = new LinkLabel(String.valueOf(i + 1));
            links[i].setFont(new Font("System", Font.PLAIN, 14));

            links[i].addActionListener(this);
            linksPanel.add(links[i]);

            group.addLink(links[i]);
        }

        links[0].setActive(true);

        prevButton = new JButton("Prev");
        prevButton.setActionCommand("Previous");
        prevButton.addActionListener(this);
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(linksPanel);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        //topPanel.add(linksPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        return topPanel;
    }

    private JPanel createCenterPanel()
    {
        cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        modelInputPanel = new JModelInputPanel(mediator);
        mediator.setModelInputPanel(modelInputPanel);
        cardPanel.add("1", modelInputPanel);
        return cardPanel;
    }

    private void loadOtherPanels()
    {
        SwingWorker worker = new SwingWorker()
        {
            public Object construct()
            {
                trialDef = new TrialDefPanel();
                statDef = new StatDefPanel(mediator);
                runTrial = new RunTrialPanel(FiveStep.this, computeClass);
                statPanel = new StatPanel(computeClass);

                mediator.setTrialDefPanel(trialDef);
                mediator.setStatDefPanel(statDef);
                mediator.setStatPanel(statPanel);
                mediator.setRunTrialPanel(runTrial);
                centerPanel.add("2", trialDef);
                centerPanel.add("3", statDef);
                centerPanel.add("4", runTrial);
                centerPanel.add("5", statPanel);
                return null;
            }

            public void finished()
            {

            }
        };

        worker.start();
    }

    private void initComponents()
    {
        JPanel topPanel = createNavigationPanel();
        centerPanel = createCenterPanel();
        Container content = this;

        taModelInfo = new JTextArea(2, 40);
        taModelInfo.setEditable(false);
        taModelInfo.setWrapStyleWord(true);
        taModelInfo.setLineWrap(true);
        taModelInfo.setOpaque(false);
        taModelInfo.setText("Model not defined");

        bPrint=new JButton("Print");
        bPrint.setActionCommand("PRINT");
        bPrint.addActionListener(this);
        bPrint.setEnabled(false);

        mediator.setModelInfoComponent(taModelInfo);
        Box box=Box.createHorizontalBox();
        box.add(taModelInfo);
        box.add(Box.createHorizontalGlue());
        box.add(bPrint);
        box.setBorder(BorderFactory.createEmptyBorder(0,0,0,6));
        content.setLayout(new BorderLayout());
        content.add(topPanel, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(box, BorderLayout.SOUTH);
    }


    public void addNotify()
    {
        super.addNotify();
        loadOtherPanels();
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
                numberGenerator = modelInputPanel.getModel();
                mediator.setModel(numberGenerator);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage() + " Can not go to step 2.", "Error", JOptionPane.ERROR_MESSAGE);
                //tabPane.setSelectedIndex(0);
                return false;
            }
            trialDef.updatePanel();
            showBoxModelStat(modelInputPanel.getModelDescription());
            //runTrial.clearAll();
            //computeClass.reset();
            computeClass.setNumberGenerator(numberGenerator);
        }
        else if (index == 1)
        {
            int trialIndex = trialDef.getSelectedIndex();
            if (trialIndex <= 0 || trialIndex == 6)
            {
                JOptionPane.showMessageDialog(this, "You have to choose a trial!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            else
            {
                numberGenerator.setTrialType(trialIndex);
                if (trialIndex == IGenerator.DRAW_N_W_REP || trialIndex == IGenerator.DRAW_N_WO_REP)
                {
                    numOfDraws = (int) trialDef.getInput();

                    if (numOfDraws <= 0)
                    {
                        JOptionPane.showMessageDialog(this, "Value for n invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    else if (trialIndex == IGenerator.DRAW_N_WO_REP && numOfDraws > numberGenerator.getCapacity())
                    {
                        JOptionPane.showMessageDialog(this, "Can not draw " + numOfDraws + " without replacement!", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    //numberGenerator.setNumberOfDraws(numOfDraws);
                }
                else if (trialIndex == IGenerator.DRAW_UNTIL_X)
                {
                    boxX = trialDef.getInput();
                    if (!numberGenerator.isGoalReachable(boxX))
                    {
                        JOptionPane.showMessageDialog(this, boxX + " is not one of the possible outcomes!", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
                else if(trialIndex==IGenerator.DRAW_ONE){
                    numOfDraws=1;
                }
            }

            //runTrial.clearAll();
            //computeClass.reset();
            statDef.updatePanel();
        }
        else if (index == 2)
        {
            Object statChoice = statDef.getSelectedItem();
            int eventIndex = statDef.getEventIndex();

            if (statChoice.equals("") || statChoice.equals(StatDefPanel.SEPARATOR))
            {
                JOptionPane.showMessageDialog(this, "You have to choose a statistic!", "Error", JOptionPane.ERROR_MESSAGE);

                return false;
            }

            computeClass.setStatType(statDef.getSelectedItem().toString());
            if(statDef.getSelectedItem().equals(StatComputer.CORR_COEFF))
            {
                ManualAxisModel axisModel=new ManualAxisModel();
                axisModel.setStartValue(-1);
                axisModel.setEndValue(1);
                axisModel.setInterval(0.2);
                axisModel.setManual(true);
                statPanel.getHistogram().setAxisModel(axisModel);
            }
            StatComputer.setEventIndex(eventIndex);

            //runTrial.showStatName(computeClass.getStatType());
            //runTrial.clearAll();
            //computeClass.reset();

            runTrial.updateReport();
            if (eventIndex >= 1 && eventIndex <= 5)
            {
                double a = statDef.getA();
                computeClass.setA(a);
            }
            else if (eventIndex >= 6)
            {
                computeClass.setA(statDef.getA());
                computeClass.setB(statDef.getB());
            }

        }

        return true;
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        Object src = p1.getSource();
        String cmd = p1.getActionCommand();

        if (cmd.equals("Previous"))
        {

            stepIndex--;
            links[stepIndex].setActive(true);
            cardLayout.show(centerPanel, String.valueOf(stepIndex + 1));

            if (stepIndex == 0)
                prevButton.setEnabled(false);
            if (stepIndex < 5)
            {
                nextButton.setEnabled(true);
                bPrint.setEnabled(false);
            }
        }
        else if (cmd.equals("Next"))
        {
            if (checkStep())
            {
                stepIndex++;
                cardLayout.show(centerPanel, String.valueOf(stepIndex + 1));
                links[stepIndex].setActive(true);
            }
            if (stepIndex > 0)
                prevButton.setEnabled(true);
            if (stepIndex == 4)
            {
                nextButton.setEnabled(false);
                //statPanel.setData(computeClass.getStats(), computeClass.getSuccessCount());
                bPrint.setEnabled(true);
            }
            links[stepIndex].setEnabled(true);
        }
        else if (cmd.equals("Reset"))
        {
            runTrial.clearAll();
            statPanel.reset();
            computeClass.reset();
        }
        else if (cmd.equals("Clear Model"))
        {
            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear everything?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION)
            {
                trialDef.reset();
                statDef.reset();
                runTrial.clearAll();
                statPanel.reset();
                computeClass.reset();
                taModelInfo.setText("Model not defined");
            }
        }
        else if(cmd.equals("PRINT"))
        {
            new PrintManager(mediator, computeClass).print();
        }
        else if (src instanceof LinkLabel)
        {
            cardLayout.show(centerPanel, cmd);
            stepIndex = Integer.parseInt(cmd) - 1;

            prevButton.setEnabled(stepIndex > 0);
            nextButton.setEnabled(stepIndex < 4);
        }
        else
        {
            numOfRuns = Integer.parseInt(p1.getActionCommand());
            try
            {
                computeClass.ensureCapacity(numOfRuns);
            }
            catch(OutOfMemoryError error)
            {
                JOptionPane.showMessageDialog(this, "There is not enough memory to run "+numOfRuns+" simulations!");
                return;
            }
            runTrial.setEnabled(false);
            runTrial.restart();

            fastRun = runTrial.isFastRun();
            //computeClass.addRun(numOfRuns);


            Thread thread = new Thread(this);
            thread.start();

        }
    }

    public void run()
    {
        nf = NumberFormat.getPercentInstance();
        //nf.setMaximumFractionDigits(2);
        if (fastRun)
        {
            int i = 0;
            while (i < numOfRuns && !runTrial.isStopped())
            {
                outcome = numberGenerator.runTrial(numOfDraws, boxX);
                computeClass.addData(outcome, statDef.getMu(), statDef.getSigma());
                i++;
                counter = i;

                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        double tmpD = counter * 1.0 / numOfRuns;
                        runTrial.setProgress((int) (tmpD * 100));


                        runTrial.setNote(nf.format(tmpD) + " completed");
                    }
                });
            }

            SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    runTrial.showResults(computeClass.getSampleSize(), computeClass.getAverage(), CommonComputation.computeSD(computeClass.getStats()), outcome);

                    if (computeClass.getEventIndex() > 0)
                    {
                        runTrial.showEventResults(computeClass.getEventIndex(), computeClass.getSuccessCount(), computeClass.getSampleSize());
                        runTrial.showStats(computeClass.getStats(), computeClass.getSuccessList());
                    }
                    else
                        runTrial.showStats(computeClass.getStats());

                    runTrial.setEnabled(true);
                    computeClass.computeStatistics();
                    statPanel.setData(computeClass.getStats(), computeClass.getSuccessCount());
                    runTrial.setProgress(0);
                    runTrial.setNote(" ");
                }
            });
        }
        else
        {
            int i = 0;
            while (i < numOfRuns && !runTrial.isStopped())
            {
                outcome = numberGenerator.runTrial(numOfDraws, boxX);
                computeClass.addData(outcome, statDef.getMu(), statDef.getSigma());
                i++;
                counter = i;
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        runTrial.showResults(computeClass.getSampleSize(), computeClass.getAverage(), CommonComputation.computeSD(computeClass.getStats()), outcome);

                        if (computeClass.getEventIndex() > 0)
                        {
                            runTrial.showEventResults(computeClass.getEventIndex(), computeClass.getSuccessCount(), computeClass.getSampleSize());
                            runTrial.addStat(computeClass.getCurrentStat(), computeClass.getCurrentSuccess());
                        }
                        else
                            runTrial.addStat(computeClass.getCurrentStat());

                        double tmpD = counter * 100.0 / numOfRuns;
                        runTrial.setProgress((int) tmpD);

                        runTrial.setNote(nf.format(tmpD) + "% completed");
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
            computeClass.computeStatistics();
            statPanel.setData(computeClass.getStats(), computeClass.getSuccessCount());
            runTrial.setProgress(0);
            runTrial.setNote(" ");
        }

    }

    private void showBoxModelStat(String text)
    {
        taModelInfo.setText(text);
    }

    public int getNumOfDraws()
    {
        return numOfDraws;
    }

    public static void main(String[] args)
    {
        System.setErr(System.out);
        FiveStep fs = new FiveStep();
        fs.init();

        AppFrame fr = new AppFrame("Five Step");
        fr.getContentPane().add(fs);
        fr.pack();
        //fr.setSize(550,480);
        fr.show();
    }
}
