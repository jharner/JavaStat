package wvustat.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: May 16, 2004
 * Time: 1:44:36 PM
 * To change this template use Options | File Templates.
 */
public class JFileFormatPanel extends JPanel
{
    public static final int COMMA_DELIMITED=0;
    public static final int TAB_DELIMITED=1;
    public static final int FIXED_WIDTH=2;


    private JRadioButton rbTabbed;
    private JRadioButton rbFixedWidth;
    private JRadioButton rbComma;

    public JFileFormatPanel()
    {
        super(new BorderLayout());
        add(createTopPanel(), BorderLayout.CENTER);
    }

    private Container createTopPanel()
    {
        rbComma=new JRadioButton("Comma separated", true);
        rbTabbed=new JRadioButton("Tab delimited");
        rbFixedWidth=new JRadioButton("Spaces separated");

        ButtonGroup group=new ButtonGroup();
        group.add(rbComma);
        group.add(rbTabbed);
        group.add(rbFixedWidth);

        Box pane=Box.createVerticalBox();
        pane.add(new JLabel("Choose the format of the text file:"));
        pane.add(rbComma);
        pane.add(rbTabbed);
        pane.add(rbFixedWidth);

        return pane;
    }

    public int getFileFormat()
    {
        int option=-1;
        if(rbComma.isSelected())
            option=COMMA_DELIMITED;
        else if(rbTabbed.isSelected())
            option=TAB_DELIMITED;
        else
            option=FIXED_WIDTH;
        return option;
    }

    public static int showFileFormatDialog(Component rootComp)
    {
        JFileFormatPanel formatPanel=new JFileFormatPanel();
        int option=JOptionPane.showOptionDialog(rootComp, formatPanel, "Choose a format", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if(option==JOptionPane.OK_OPTION)
            return formatPanel.getFileFormat();
        else
            return -1;
    }
}
