package wvustat.simulation.fivestep;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 14, 2003
 * Time: 9:00:17 AM
 * To change this template use Options | File Templates.
 */
public class JAdjustModelPanel extends JPanel
{
    private JLabel inputPromptLabel;
    private JTextField inputField;

    public JAdjustModelPanel()
    {
        super(new BorderLayout());
        add(createCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel createCenterPanel()
    {
        JLabel label=new JLabel("Add c to each value");
        inputPromptLabel=new JLabel("c =");
        inputField=new JTextField(8);

        JPanel panel=new JPanel(new GridBagLayout());

        panel.add(label,new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel.add(inputPromptLabel, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel.add(inputField, new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));

        return panel;
    }

    public double getInput()
    {
        double d=Double.NaN;
        try
        {
            d=Double.parseDouble(inputField.getText());
        }
        catch(NumberFormatException e)
        {

        }

        return d;
    }

}
