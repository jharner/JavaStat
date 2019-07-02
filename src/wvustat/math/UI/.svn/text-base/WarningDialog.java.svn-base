/*
 * WarningDialog.java
 *
 * Created on January 22, 2002, 10:17 AM
 */

package wvustat.math.UI;

import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author  hxue
 * @version 
 */
public class WarningDialog extends Dialog {

    protected String message;

    /** Creates new WarningDialog */
    public WarningDialog(Frame fr, String message) {
        super(fr, true);
        setBackground(Color.white);
        setTitle("Warning");
        this.message=message;
        initComponents();
        pack();
    }
    
    private void initComponents(){
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(8,8,8,8);
        gbc.gridx=GridBagConstraints.RELATIVE;
        gbc.gridy=GridBagConstraints.RELATIVE;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.gridheight=GridBagConstraints.RELATIVE;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.CENTER;
        
        Label msgLabel=new Label(message);
        msgLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        add(msgLabel, gbc);
        
        Button okButton=new Button("    Ok    ");
        okButton.setActionCommand("Ok");
        
        gbc.gridheight=GridBagConstraints.REMAINDER;
        gbc.fill=GridBagConstraints.NONE;
        add(okButton, gbc);
        
        okButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                closeDialog();
            }
        }
        );
    }
    
    private void closeDialog(){
        setVisible(false);
        dispose();
    }
    
    private void buttonActionPerformed(){
        closeDialog();
    }
    
    public static void showWarning(Frame parent, String message){
        WarningDialog wd=new WarningDialog(parent, message);
        Point pt=parent.getLocationOnScreen();
  
        Dimension d=parent.getSize();
        wd.setLocation(pt.x+d.width/2-wd.getSize().width/2, pt.y+d.height/2-wd.getSize().height/2);
        wd.show();
    }
    
    public static void main(String[] args){
        new WarningDialog(new Frame(), "This is a test of the warning dialog.\nThis is a test of the warning dialog.").show();
    }      
        

}