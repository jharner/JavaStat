package wvustat.swing;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

public class LogonDialog extends JDialog implements ActionListener{

	public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    
    private int option = CANCEL_OPTION;
	
    private String username, password;
    private JTextField field1, field2;
    private JButton okButton;
    
    public LogonDialog(Frame frame){
    	super(frame, "Logon", true);
    	
    	JPanel contentPane=(JPanel)this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);	
        
        this.getRootPane().setDefaultButton(okButton);	
        pack();
        setResizable(false);
    }
    
    private Container createCenterPanel(){
    	JLabel label1 = new JLabel("User Name: ",JLabel.LEFT);
    	JLabel label2 = new JLabel("Password: ",JLabel.LEFT);
    	field1 = new JTextField("", 10);
    	field2 = new JPasswordField(10);
    	
    	JPanel panel1=new JPanel(new GridLayout(2,2,2,2));
    	panel1.add(label1);
    	panel1.add(field1);
    	panel1.add(label2);
    	panel1.add(field2);
    	
    	panel1.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
    	return panel1;    	    	
    }
    
    private Container createButtonPanel()
    {
        okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel panel=new JPanel(new GridLayout(1,2,30,2));

        panel.add(okButton);
        panel.add(cancelButton);
        panel.setBorder(BorderFactory.createEmptyBorder(2,20,2,20));
        return panel;
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	if (e.getActionCommand() == "Ok")
        {
    		String s = field1.getText();
    		if (s == null || s.trim().length() == 0) {
        		JOptionPane.showMessageDialog(this, "You must input user name");
        		return;
        	}
    		
    		if (field2.getText() == null || field2.getText().length() == 0) {
        		JOptionPane.showMessageDialog(this, "You must input password");
        		return;
        	}
    		
    		this.username = field1.getText().trim();
    		this.password = field2.getText();
    		
    		option = OK_OPTION;
            dispose();
        }
    	else if (e.getActionCommand() == "Cancel")
        {
            option = CANCEL_OPTION;
            dispose();
        }
    }
	
    public int getOption()
    {
        return option;
    }
    
    public String getUsername()
    {
    	return username;
    }
    
    public String getPassword()
    {
    	return password;
    }
    
	public static void main(String[] args) {
		JFrame fr=new JFrame("Test");
		LogonDialog ld = new LogonDialog(fr);
		ld.pack();
		ld.show();
	}

}
