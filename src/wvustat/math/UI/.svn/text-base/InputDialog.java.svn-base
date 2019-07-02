/*
 * InputDialog.java
 *
 * Created on January 22, 2002, 9:52 AM
 */

package wvustat.math.UI;

import java.awt.*;
import java.awt.event.*;

import wvustat.plotUtil.*;
/**
 *
 * @author  hxue
 * @version 
 */
public class InputDialog extends Dialog implements ActionListener{
    private TextField field;
    private String message;
    private String input, defaultInput;
    
    /** Creates new IdInputDialog */
    public InputDialog(Frame parent, String message, String defaultInput) {
        super(parent, true);
        setTitle("Input");
        setBackground(Color.white);
        this.message=message;
        this.defaultInput=defaultInput;
        
        initComponents();
        pack();

        if(parent.isShowing()){
            Point pt=parent.getLocationOnScreen();
            Dimension d=parent.getSize();
            setLocation(pt.x+d.width/2-getSize().width/2, pt.y+d.height/2-getSize().height/2);
        }
    }
    
   
    private void initComponents(){
        setLayout(new GridBagLayout());

        Label msgLabel=new Label(message);
        msgLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        add(msgLabel, new MyGridBagConstraints(0,0,0,1,1.0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4,4,4,4),2,2));
        
        field=new TextField();
        if(defaultInput!=null){
            field.setText(defaultInput);
        }
        

        add(field, new MyGridBagConstraints(0,1,0,1,0.5,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4,4,4,4),0,4));
        
        Button okButton=new Button("Ok");
        Button cancelButton=new Button("Cancel");
        
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        Panel buttonPanel=new Panel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.add(cancelButton, new MyGridBagConstraints(0,0,-1,0,0.5,0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4,4,4,4),8,2));
        buttonPanel.add(okButton, new MyGridBagConstraints(1,0,0,0,0.5,0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4,4,4,4),2,2));
        
        add(buttonPanel, new MyGridBagConstraints(0,2,0,0,1.0,0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4,4,4,4),0,0));        
    }

    public void closeDialog() {
        setVisible(false);
        dispose();
    }
    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Ok")){
            if(field.getText()!=null && field.getText().length()>0){
                input=field.getText();
                closeDialog();
            }
        }
        else if(cmd.equals("Cancel")){
            closeDialog();
        }
    }
    
    public String getInput(){
        return input;
    }
    
    public static String showInputDialog(Frame parent, String msg, String defaultInput){
        InputDialog d=new InputDialog(parent, msg, defaultInput);
        d.show();
        return d.getInput();
    }
    
    public static void main(String[] args){
        Frame fr=new Frame("Test");
        
        InputDialog d=new InputDialog(fr, "How many rows do you want to add?", null);
        d.show();
    }
            
    
}