package wvustat.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 27, 2004
 * Time: 2:04:51 PM
 * To change this template use Options | File Templates.
 */
public class JEditableLabel extends JPanel implements ActionListener, MouseListener
{
    private JLabel label;
    private JTextField txtField;
    private CardLayout cardLayout=new CardLayout();

    public JEditableLabel(String text)
    {
        label=new JLabel(text);
        txtField=new JTextField(text);
        setLayout(cardLayout);
        add(label, "1");
        add(txtField, "2");
        cardLayout.show(this, "1");
        txtField.addActionListener(this);
        this.addMouseListener(this);
        this.setToolTipText("Click to edit text");
    }


    public void actionPerformed(ActionEvent event)
    {
        label.setText(txtField.getText());
        cardLayout.show(this, "1");
    }

    public void mouseClicked(MouseEvent event)
    {
        cardLayout.show(this, "2");
        txtField.requestFocus();
    }

    public void mousePressed(MouseEvent event)
    {
    }

    public void mouseReleased(MouseEvent event)
    {
    }

    public void mouseEntered(MouseEvent event)
    {
    }

    public void mouseExited(MouseEvent event)
    {
        //label.setText(txtField.getText());
        //cardLayout.show(this, "1");
    }

    public String getText()
    {
        return label.getText();
    }

    public void setText(String text)
    {
        label.setText(text);
        txtField.setText(text);
        setToolTipText(text);
    }

    public static void main(String[] args)
    {
        JFrame jf=new JFrame("Test");
        JEditableLabel label=new JEditableLabel("This is a test");
        jf.getContentPane().add(label);
        jf.pack();
        jf.show();

    }
}
