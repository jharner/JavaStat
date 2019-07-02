/*
 * ColumnDefDialog.java
 *
 * Created on March 21, 2002, 10:10 AM
 */

package wvustat.plotUtil;

import wvustat.math.expression.Expression;
import wvustat.math.expression.ExpressionParser;
import wvustat.swing.table.DataSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class ColumnDefDialog extends JDialog implements ActionListener
{
    private JRadioButton check1, check2;
    private ButtonGroup checkGroup;
    private JTextField formulaBar;
    private JComboBox columnChoice;

    private DataSet tableModel;

    /** Creates new ColumnDefDialog */
    public ColumnDefDialog(Dialog parent, DataSet tableModel)
    {
        super(parent, true);
        this.tableModel = tableModel;
        setTitle("New Column");
        //setBackground(Color.white);
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
        checkGroup = new ButtonGroup();

        check1 = new JRadioButton("Transformation", false);
        check2 = new JRadioButton("Finite Difference", true);
        checkGroup.add(check1);
        checkGroup.add(check2);
        check1.setActionCommand("TRANSFORM");
        check1.addActionListener(this);
        check2.setActionCommand("FINITE_DIFF");
        check2.addActionListener(this);

        formulaBar = new JTextField(12);
        formulaBar.setEnabled(false);
        columnChoice = new JComboBox();
        for (int i = 0; i < tableModel.getColumnCount(); i++)
        {
            columnChoice.addItem(tableModel.getColumnName(i));
        }

        JButton ok = new JButton("Apply");
        ok.setActionCommand("Ok");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);

        JPanel content = (JPanel)this.getContentPane();
        content.setLayout(new GridBagLayout());

        content.add(check1, new GridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        content.add(formulaBar, new GridBagConstraints(0, 1, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        content.add(check2, new GridBagConstraints(0, 2, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        content.add(columnChoice, new GridBagConstraints(0, 3, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        /*
        Panel bPanel = new Panel();
        bPanel.setLayout(new GridLayout(1, 2, 20, 4));
        bPanel.add(cancel);
        bPanel.add(ok);
        */
        JPanel buttonPanel=new JPanel(new FlowLayout());
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        content.add(buttonPanel, new GridBagConstraints(0, 4, 0, 0, 0.2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(4, 2, 2, 2), 0, 0));
        content.setBorder(new EmptyBorder(10,20,2,20));
    }

    public void actionPerformed(ActionEvent evt)
    {
        String cmd = evt.getActionCommand();

        if (cmd.equals("TRANSFORM"))
        {
            formulaBar.setEnabled(true);
        }
        else if (cmd.equals("FINITE_DIFF"))
        {
            formulaBar.setEnabled(false);
        }

        else if (cmd.equals("Cancel"))
        {
            this.dispose();
        }
        else if (cmd.equals("Ok"))
        {
            if (check2.isSelected())
            {

                int index = columnChoice.getSelectedIndex() - 1;
                System.out.println(index);
                String name = columnChoice.getSelectedItem().toString();
                double[] colData = tableModel.getColumnData(index);
                double[] diff = finiteDiff(colData);
                tableModel.addColumn(name + "-diff", diff);

            }
            else if (check1.isSelected())
            {
                String input = formulaBar.getText();
                try
                {
                	   ExpressionParser ep = new ExpressionParser();
                    Expression expr = ep.parse(input);
                    Vector v = expr.getVariableNames();
                    String colName = v.elementAt(0).toString();

                    double[] src = tableModel.getColumnData(colName);
                    Hashtable ht = new Hashtable();
                    double[] newData = new double[tableModel.getRowCount()];
                    for (int i = 0; i < newData.length; i++)
                    {
                        ht.put(colName, new Double(src[i]));
                        newData[i] = expr.value(ht);
                    }

                    tableModel.addColumn(input, newData);

                }
                catch (Exception e)
                {
                    System.out.println(e);
                }
                catch (Error err)
                {
                    System.out.println(err);
                }
            }

            this.dispose();
        }


    }

    private double[] finiteDiff(double[] src)
    {
        double[] ret = new double[src.length];

        ret[0] = Double.NaN;
        for (int i = 1; i < ret.length; i++)
        {
            ret[i] = src[i] - src[i - 1];
        }

        return ret;
    }


}