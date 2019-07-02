package wvustat.math.UI;

import wvustat.math.expression.ExpressionObject;
import wvustat.swing.table.DataSet;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: May 11, 2003
 * Time: 10:03:23 AM
 * To change this template use Options | File Templates.
 */
public class JExpressionDialog implements ActionListener
{
    private ExpressionObject expression;
    private DataSet dataSet;
    private JFunctionComponent funcComp;
    private JDialog dialog;

    public JExpressionDialog(ExpressionObject expression, DataSet dataSet)
    {
        super();
        this.expression=expression;
        this.dataSet=dataSet;
    }

    private JComponent createCenterPanel()
    {
       funcComp=new JFunctionComponent(expression, dataSet);
        funcComp.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
       return funcComp;
    }

    //This method returns JComponent, I changed it to Component because
    //in Java 1.3, javax.swing.Box is not a JComponent. 
    private Component createButtonPanel()
    {
        Box box=Box.createHorizontalBox();
        JButton resetBttn=new JButton("Reset");
        resetBttn.setActionCommand("RESET");
        resetBttn.addActionListener(this);
        JButton closeBttn=new JButton("Close");
        closeBttn.setActionCommand("CLOSE");
        closeBttn.addActionListener(this);
        box.add(Box.createHorizontalGlue());
        box.add(resetBttn);
        box.add(Box.createRigidArea(new Dimension(4,4)));
        box.add(closeBttn);
        box.add(Box.createHorizontalGlue());
	//Comment out for Java 1.3.1 compatible problem
        //box.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        return box;

    }

    public JDialog getDialog(Component comp, String title)
    {
       Frame frame=(Frame)SwingUtilities.getWindowAncestor(comp);
        dialog=new JDialog(frame, title);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(createCenterPanel(), BorderLayout.CENTER);
        dialog.getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
        dialog.pack();
        return dialog;
    }

    public void actionPerformed(ActionEvent event)
    {
        String cmd=event.getActionCommand();
        if(cmd.equals("CLOSE"))
        {
            dialog.dispose();
        }
        else if(cmd.equals("RESET"))
        {
            funcComp.reset();
            dialog.validate();
        }
    }
}
