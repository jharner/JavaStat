/*
 * RoleDefDialog.java
 *
 * Created on March 21, 2002, 10:54 AM
 */

package wvustat.plotUtil;

import wvustat.swing.table.DataSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author  hxue
 * @version
 */
public class RoleDefDialog extends JDialog implements ActionListener
{
    private JComboBox xChoice, yChoice;
    private DataSet tableModel;

    /** Creates new RoleDefDialog */
    public RoleDefDialog(Dialog parent, DataSet tableModel)
    {
        super(parent, true);
        this.tableModel = tableModel;
        setTitle("Define roles");
        setBackground(Color.white);
        initComponent();
        pack();

        if (parent.isShowing())
        {
            Point pt = parent.getLocationOnScreen();
            Dimension d = parent.getSize();
            setLocation(pt.x + d.width / 2 - getSize().width / 2, pt.y + d.height / 2 - getSize().height / 2);
        }
    }

    private void initComponent()
    {
        JLabel label1 = new JLabel("Choose column as x");
        JLabel label2 = new JLabel("Choose column as y");

        xChoice = new JComboBox();
        yChoice = new JComboBox();
        for (int i = 0; i < tableModel.getColumnCount(); i++)
        {
            xChoice.addItem(tableModel.getColumnName(i));
            yChoice.addItem(tableModel.getColumnName(i));
        }

        JButton ok = new JButton("Apply");
        ok.setActionCommand("Ok");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);

        JPanel content =(JPanel) this.getContentPane();
        content.setLayout(new GridBagLayout());

        content.add(label1, new MyGridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        content.add(xChoice, new MyGridBagConstraints(0, 1, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        content.add(label2, new MyGridBagConstraints(0, 2, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        content.add(yChoice, new MyGridBagConstraints(0, 3, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        JPanel buttonPanel=new JPanel(new FlowLayout());
        buttonPanel.add(ok);
        buttonPanel.add(cancel);

        content.add(buttonPanel, new MyGridBagConstraints(0, 4, 0, 0, 0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        content.setBorder(new EmptyBorder(10,20,2,20));
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        String cmd = p1.getActionCommand();

        if (cmd.equals("Ok"))
        {
            //tableModel.setXRole(xChoice.getSelectedIndex());
            //tableModel.setYRole(yChoice.getSelectedIndex());
        }

        dispose();

    }
}