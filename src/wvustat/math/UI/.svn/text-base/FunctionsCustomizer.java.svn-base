/*
 * FunctionsCustomizer.java
 *
 * Created on November 19, 2001, 1:56 PM
 */

package wvustat.math.UI;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import wvustat.math.expression.*;

import javax.swing.*;

/**
 *
 * @author  hxue
 * @version
 */
public class FunctionsCustomizer extends JDialog implements ItemListener, ActionListener
{
    private java.util.List functions = new Vector();
    private JComboBox[] colorChoices;
    //private JComponent[] comps;
    //private JLabel[] ssLabels;
    //private DataSet dataSet;

    /** Creates new FunctionsCustomizer */
    public FunctionsCustomizer(Frame parent)
    {
        super(parent, "Customizer", false);
        addComponents();

        pack();
    }

    public void setFunctions(java.util.List functions)
    {
        this.functions = functions;
        if (functions != null)
        {
            addComponents();

            pack();
            center();
        }
    }

    /*
    public void setDataSet(DataSet dataSet)
    {
        this.dataSet = dataSet;
    }
    */

    private void addComponents()
    {
        Container content = new JPanel();
        content.setLayout(new GridBagLayout());

        Font headerFont = new Font("Monospaced", Font.BOLD, 14);

        JLabel header1 = new JLabel("Functions");
        header1.setFont(headerFont);

        JLabel header2 = new JLabel("Line Color");
        header2.setFont(headerFont);

        /*
        JLabel header3 = new JLabel("Constants");
        header3.setFont(headerFont);

        JLabel header4 = new JLabel("Sum of Squares");
        header4.setHorizontalAlignment(JLabel.RIGHT);
        header4.setFont(headerFont);

        */

        int n = functions.size();
        content.add(header1, new GridBagConstraints(0, 0, 1, 1, 0.4, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 20, 4, 4), 0, 0));
        content.add(header2, new GridBagConstraints(1, 0, 0, 1, 0.6, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 4, 4, 4), 0, 0));
        /*
        if (dataSet == null)
            content.add(header3, new GridBagConstraints(2, 0, 0, 1, 0.3, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 4, 4, 20), 0, 0));
        else
        {
            content.add(header3, new GridBagConstraints(2, 0, -1, 1, 0.3, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 4, 4, 4), 0, 0));
            content.add(header4, new GridBagConstraints(3, 0, 0, 1, 0.3, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 4, 4, 20), 0, 0));
        }
        */


        colorChoices = new JComboBox[functions.size()];

        Font cellFont = new Font("Courier", Font.PLAIN, 12);

        //comps=new JComponent[functions.size()];

        //ssLabels=new JLabel[functions.size()];

        for (int i = 0; i < functions.size(); i++)
        {
            ExpressionObject elem = (ExpressionObject) functions.get(i);
            JLabel label = null;
            if (elem.getUnparsedForm().startsWith("y="))
                label = new JLabel(elem.getUnparsedForm());
            else
                label = new JLabel("y=" + elem.getUnparsedForm());
            label.setFont(cellFont);

            content.add(label, new GridBagConstraints(0, (i + 1), 1, 1, 0.4, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 20, 4, 4), 0, 0));

            colorChoices[i] = new JComboBox();
            colorChoices[i].setFont(cellFont);
            colorChoices[i].addItem("Black");
            colorChoices[i].addItem("Blue");
            colorChoices[i].addItem("Cyan");
            colorChoices[i].addItem("Green");
            colorChoices[i].addItem("Orange");
            colorChoices[i].addItem("Red");

            colorChoices[i].setSelectedItem(unparseColor(elem.getLineColor()));

            colorChoices[i].addItemListener(this);
            content.add(colorChoices[i], new GridBagConstraints(1, (i + 1), 0, 1, 0.6, 1.0 / (n + 1), GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

            /*
            if(elem.getVariables().size()>1){
                comps[i]=new JButton("Change ...");
                ((JButton)comps[i]).addActionListener(this);
            }
            else
            comps[i]=new JLabel("None");
            */


            /*
            if(dataSet==null)
            content.add(comps[i], new GridBagConstraints(2,(i+1),0,1,0.5,1.0/(n+1),GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4,4,4,20),0,0));
            else{
                content.add(comps[i], new GridBagConstraints(2,(i+1),-1,1,0.25,1.0/(n+1),GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4,4,4,4),0,0));
                ssLabels[i]=new JLabel(nf.format(elem.getSumOfSquares(dataSet.getXValues(), dataSet.getYValues())), Label.RIGHT);
                content.add(ssLabels[i], new GridBagConstraints(3,(i+1),0,1,0.25,1.0/(n+1),GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4,4,4,20),0,0));
            }
            */


        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        buttonPanel.add(closeButton, new GridBagConstraints(0, 0, 0, 0, 0.4, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        content.add(buttonPanel, new GridBagConstraints(0, n + 1, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 4, 20, 4), 4, 4));

        this.setContentPane(content);
    }

    private void center()
    {
        Component parent = this.getParent();
        Point pt = parent.getLocationOnScreen();
        Dimension d1 = parent.getSize();
        Dimension d2 = this.getSize();
        this.setLocation(pt.x + d1.width / 2 - d2.width / 2, pt.y + d1.height / 2 - d2.height / 2);
    }


    public void itemStateChanged(final java.awt.event.ItemEvent p1)
    {
        Object src = p1.getSource();
        String item = p1.getItem().toString();

        int index = 0;
        while (index < colorChoices.length)
        {
            if (src == colorChoices[index])
                break;
            index++;
        }

        if (index < colorChoices.length)
        {
            ExpressionObject func = (ExpressionObject) functions.get(index);
            func.setLineColor(parseColor(item));
        }
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        String cmd = p1.getActionCommand();


        if (cmd.equals("Close"))
        {
            this.dispose();
        }
        /*
        else if (cmd.equals("Change ..."))
        {
            int index = 0;
            while (index < comps.length)
            {
                if (src == comps[index])
                    break;
                index++;
            }

            if (index < comps.length)
            {
                ExpressionObject func = (ExpressionObject) functions.elementAt(index);
                SliderGroup adjuster = new SliderGroup(ComponentUtil.getTopComponent(this), "Change constants");
                this.dispose();
                adjuster.setFunction(func);
                adjuster.show();
            }
        }
        */
    }

    private Color parseColor(String colorName)
    {
        String name = colorName.toLowerCase();

        if (name.equals("black"))
            return Color.black;
        else if (name.equals("red"))
            return Color.red;
        else if (name.equals("cyan"))
            return Color.cyan;
        else if (name.equals("orange"))
            return Color.orange;
        else if (name.equals("blue"))
            return Color.blue;
        else if (name.equals("gray"))
            return Color.gray;
        else if (name.equals("green"))
            return Color.green;
        else
            return Color.black;
    }

    private String unparseColor(Color color)
    {
        if (color.equals(Color.black))
            return "Black";
        else if (color.equals(Color.red))
            return "Red";
        else if (color.equals(Color.cyan))
            return "Cyan";
        else if (color.equals(Color.orange))
            return "Orange";
        else if (color.equals(Color.blue))
            return "Blue";
        else if (color.equals(Color.gray))
            return "Gray";
        else if (color.equals(Color.green))
            return "Green";
        else
            return "Black";
    }

    public static void main(String[] args)
    {
        JFrame jf = new JFrame();
        FunctionsCustomizer fc = new FunctionsCustomizer(jf);
        fc.show();
    }
}