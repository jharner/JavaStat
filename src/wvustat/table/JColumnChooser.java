package wvustat.table;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jan 28, 2004
 * Time: 9:37:57 AM
 * To change this template use Options | File Templates.
 */
public class JColumnChooser extends JPanel
{
    private List columnNames;
    private JRadioButton radioBefore;
    private JRadioButton radioAfter;
    private JComboBox comboColumns;

    public JColumnChooser(List columnNames)
    {
        this.columnNames=columnNames;
        setLayout(new BorderLayout());
        add(createLeftPanel(),BorderLayout.NORTH);
        add(createRightPanel(), BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
    }

    private Container createLeftPanel()
    {
        radioBefore=new JRadioButton("Before", true);
        radioAfter=new JRadioButton("After", false);

        ButtonGroup group=new ButtonGroup();
        group.add(radioBefore);
        group.add(radioAfter);

        JPanel panel=new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(radioBefore);
        panel.add(radioAfter);

        Border lineBorder=BorderFactory.createLineBorder(panel.getForeground());
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Position");
        Border emptyBorder=BorderFactory.createEmptyBorder(10,12,10,12);
        Border compositeBorder=BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
        panel.setBorder(compositeBorder);

        return panel;
    }

    private Container createRightPanel()
    {
        comboColumns=new JComboBox(columnNames.toArray());

        JPanel panel=new JPanel();
        panel.add(comboColumns);

        Border lineBorder=BorderFactory.createLineBorder(panel.getForeground());
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Column");
        Border emptyBorder=BorderFactory.createEmptyBorder(10,12,10,12);
        Border compositeBorder=BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
        panel.setBorder(compositeBorder);

        return panel;
    }

    public String getSelectedColumnName()
    {
        return (String)comboColumns.getSelectedItem();
    }

    public int getSelectedColumnIndex()
    {
        return comboColumns.getSelectedIndex();
    }

    public boolean isBefore()
    {
        return radioBefore.isSelected();
    }

    public static void main(String[] args)
    {
        JFrame jf=new JFrame("Test");
        List columns=new ArrayList();
        columns.add("Column 1");
        columns.add("Column 2");
        columns.add("Column 3");
        columns.add("Column 4");
        JColumnChooser chooser=new JColumnChooser(columns);

        jf.setContentPane(chooser);
        jf.pack();
        jf.show();
    }
}
