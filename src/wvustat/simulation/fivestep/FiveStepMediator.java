package wvustat.simulation.fivestep;

import wvustat.simulation.model.IGenerator;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jul 19, 2003
 * Time: 11:01:57 AM
 * To change this template use Options | File Templates.
 */
public class FiveStepMediator implements ChangeListener
{
    private JModelInputPanel modelInputPanel;
    private TrialDefPanel trialDefPanel;
    private StatDefPanel statDefPanel;
    private RunTrialPanel runTrialPanel;
    private StatPanel statPanel;
    private JTextArea modelInfoComponent;
    private FiveStep fiveStep;
    private IGenerator model;

    public FiveStepMediator(FiveStep fiveStep)
    {
        this.fiveStep=fiveStep;
    }

    public FiveStep getFiveStep()
    {
        return fiveStep;
    }

    public JModelInputPanel getModelInputPanel()
    {
        return modelInputPanel;
    }

    public void setModelInputPanel(JModelInputPanel modelInputPanel)
    {
        this.modelInputPanel = modelInputPanel;
    }

    public TrialDefPanel getTrialDefPanel()
    {
        return trialDefPanel;
    }

    public void setTrialDefPanel(TrialDefPanel trialDefPanel)
    {
        this.trialDefPanel = trialDefPanel;
    }

    public StatDefPanel getStatDefPanel()
    {
        return statDefPanel;
    }

    public void setStatDefPanel(StatDefPanel statDefPanel)
    {
        this.statDefPanel = statDefPanel;
    }

    public RunTrialPanel getRunTrialPanel()
    {
        return runTrialPanel;
    }

    public void setRunTrialPanel(RunTrialPanel runTrialPanel)
    {
        this.runTrialPanel = runTrialPanel;
    }

    public StatPanel getStatPanel()
    {
        return statPanel;
    }

    public void setStatPanel(StatPanel statPanel)
    {
        this.statPanel = statPanel;
    }

    public void reset()
    {
        modelInputPanel.reset();
        trialDefPanel.reset();
        statDefPanel.reset();
        runTrialPanel.clearAll();
        statPanel.reset();
        FiveStep.getStatComputer().reset();
    }

    public void setModelInfoComponent(JTextArea textArea)
    {
        modelInfoComponent=textArea;
    }

    public JTextArea getModelInfoComponent()
    {
        return modelInfoComponent;
    }

    public void stateChanged(ChangeEvent event)
    {
        modelInfoComponent.setText(modelInputPanel.getModelDescription());
    }

    public IGenerator getModel()
    {
        return model;
    }

    public void setModel(IGenerator model)
    {
        this.model = model;
    }
}
