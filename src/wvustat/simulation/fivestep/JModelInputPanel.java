package wvustat.simulation.fivestep;

import wvustat.simulation.model.*;
import wvustat.statistics.InvalidDataError;

import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jul 19, 2003
 * Time: 10:33:58 AM
 * To change this template use Options | File Templates.
 */
public class JModelInputPanel extends JPanel implements ActionListener
{
    private BoxModelInput boxModel1, boxModel2;
    private JButton prevButton, nextButton;

    private CardLayout cardLayout;

    private JPanel centerPanel;
    private int panelIndex = 0;
    private FiveStepMediator mediator;
    private IGenerator model;
    private LinkLabel label1, label2, label3, label4;
    private JPairedDataInputPanel pairInputPanel;
    private JContingencyTablePanel contTablePanel;

    public JModelInputPanel(FiveStepMediator mediator)
    {
        this.mediator = mediator;
        setLayout(new BorderLayout());
        add(createNavigationPanel(), BorderLayout.NORTH);
        centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createNavigationPanel()
    {
        Box linksPanel = Box.createHorizontalBox();
        JLabel label = new JLabel("1. Define a box model: ");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        linksPanel.add(label);

        label1 = new LinkLabel("1", true);
        label2 = new LinkLabel("2", true);
        label3 = new LinkLabel("P", "3", true);
        label4=new LinkLabel("C","4",true);

        label1.addActionListener(this);
        label2.addActionListener(this);
        label3.addActionListener(this);
        label4.addActionListener(this);

        LinkGroup group = new LinkGroup();
        group.addLink(label1);
        group.addLink(label2);
        group.addLink(label3);
        group.addLink(label4);

        label1.setActive(true);

        linksPanel.add(label1);
        linksPanel.add(label2);
        linksPanel.add(Box.createRigidArea(new Dimension(10, 8)));
        linksPanel.add(label3);
        linksPanel.add(Box.createRigidArea(new Dimension(10, 8)));
        linksPanel.add(label4);
        linksPanel.add(Box.createHorizontalGlue());

        prevButton = new JButton("Prev");
        prevButton.setActionCommand("Previous");
        prevButton.setEnabled(false);
        prevButton.addActionListener(this);
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(linksPanel);
        //buttonPanel.add(prevButton);
        //buttonPanel.add(nextButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.WEST);
        return topPanel;
    }

    private JPanel createCenterPanel()
    {
        boxModel1 = new BoxModelInput(mediator);
        boxModel2 = new BoxModelInput(mediator);
        boxModel1.addChangeListener(mediator);
        boxModel2.addChangeListener(mediator);
        //boxModel3 = new BoxModelInput(mediator);

        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);

        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.add(new JLabel("Model 1"), BorderLayout.NORTH);
        subPanel1.add(boxModel1, BorderLayout.CENTER);

        JPanel subPanel2 = new JPanel(new BorderLayout());
        subPanel2.add(new JLabel("Model 2"), BorderLayout.NORTH);
        subPanel2.add(boxModel2, BorderLayout.CENTER);
        panel.add("1", subPanel1);
        panel.add("2", subPanel2);

        pairInputPanel = new JPairedDataInputPanel(mediator);
        panel.add("3", pairInputPanel);

        contTablePanel=new JContingencyTablePanel(mediator);
        panel.add("4", contTablePanel);

        return panel;
    }

    public void actionPerformed(ActionEvent event)
    {
        Object src = event.getSource();
        String cmd = event.getActionCommand();

        if (cmd.equals("Previous"))
        {
            cardLayout.previous(centerPanel);
            panelIndex--;
        }
        else if (cmd.equals("Next"))
        {
            mediator.getModelInfoComponent().setText(boxModel1.getModelDescription());
            cardLayout.next(centerPanel);
            panelIndex++;
            if (panelIndex == 2)
                mediator.getFiveStep().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Next"));
        }
        else if (src instanceof LinkLabel)
        {
            if (cmd.equals("2"))
            {
                try
                {
                    IGenerator generator=boxModel1.getRandomGenerator();
                    if((generator instanceof BoxModel)==false)
                    {
                        JOptionPane.showMessageDialog(this, "Only one model is allowed for a specific distribution!");
                        return;
                    }
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "You have to define model 1 first!", "Error", JOptionPane.ERROR_MESSAGE);
                    label1.setActive(true);
                    return;
                }
            }
            else if (panelIndex == 2 && cmd.equals("1"))
            {
                if (pairInputPanel.getSpecifiedPairs().size() > 0)
                {
                    int option = JOptionPane.showOptionDialog(this, "Clear current model?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (option == JOptionPane.YES_OPTION)
                    {
                        mediator.reset();
                    }
                    else
                        return;
                }
            }
            else if (panelIndex != 2 && cmd.equals("3"))
            {
                try
                {
                    boxModel1.getRandomGenerator();
                    int option = JOptionPane.showOptionDialog(this, "Clear current model?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (option == JOptionPane.YES_OPTION)
                    {
                        mediator.reset();
                    }
                    else
                        return;
                }
                catch (Exception ex)
                {
                }
            }
            else if(panelIndex!=3 && cmd.equals("4"))
            {

            }

            cardLayout.show(centerPanel, cmd);
            panelIndex = Integer.parseInt(cmd) - 1;
            mediator.stateChanged(new ChangeEvent(boxModel1));
        }

        prevButton.setEnabled(panelIndex > 0);
    }

    public IGenerator getModel() throws Exception
    {
        if (panelIndex == 2)
        {
            List dataPairs = pairInputPanel.getSpecifiedPairs();
            if (dataPairs.size() == 0)
            {
                throw new InvalidDataError("You have to specify valid x and y values!");
            }

            PairedDataGenerator model = new PairedDataGenerator(dataPairs);
            return model;
        }
        else if(panelIndex==3)
        {
            List dataPairs = contTablePanel.getSpecifiedPairs();
            if (dataPairs.size() == 0)
            {
                throw new InvalidDataError("You have to specify valid x and y values!");
            }

            PairedCategoricalDataGenerator model = new PairedCategoricalDataGenerator(dataPairs, contTablePanel.getXLevels(), contTablePanel.getYLevels());
            return model;
        }
        IGenerator model1 = null, model2 = null;
        try
        {
            model1 = boxModel1.getRandomGenerator();
            model2 = boxModel2.getRandomGenerator();
            //model3=boxModel3.getRandomGenerator();
        }
        catch (Exception e)
        {

        }

        if (model1 == null)
            throw new InvalidDataError("You have to define at least the first model");

        if (model2 == null)
            model = model1;
        else
            model = new TwoSampleGenerator(model1, model2);

        return model;
    }

    public String getModelDescription()
    {
        if(panelIndex==2)
        {
            try
            {
                PairedDataGenerator generator=new PairedDataGenerator(pairInputPanel.getSpecifiedPairs());
                return generator.getModelDescription();
            }
            catch(Exception e)
            {
                return "Model not defined";
            }
        }
        else if(panelIndex==3)
        {
            try
            {
                PairedDataGenerator generator=new PairedCategoricalDataGenerator(contTablePanel.getSpecifiedPairs(), contTablePanel.getXLevels(), contTablePanel.getYLevels());
                return generator.getModelDescription();
            }
            catch(Exception e)
            {
                return "Model not defined";
            }
        }
        IGenerator model1 = null, model2 = null;
        try
        {
            model1 = boxModel1.getRandomGenerator();
            model2 = boxModel2.getRandomGenerator();
            //model3=boxModel3.getRandomGenerator();
        }
        catch (Exception e)
        {

        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("Model 1:");
        if (model1 == null)
            buffer.append("not defined");
        else
            buffer.append(boxModel1.getModelDescription());

        buffer.append("; Model 2:");
        if (model2 == null)
            buffer.append("not defined");
        else
            buffer.append(boxModel2.getModelDescription());


        return buffer.toString();

    }

    public void reset()
    {
        panelIndex = 0;
        cardLayout.show(centerPanel, "1");
        model = null;
        boxModel1.reset();
        boxModel2.reset();
        pairInputPanel.clear();
    }
}
