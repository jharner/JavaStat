package wvustat.swing;

import wvustat.network.client.JRIClient;
import wvustat.interfaces.LaunchOption;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

public class JRIChooser extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;
    
    private int option = CANCEL_OPTION;
	
	// To remember the default value of HOST_NAME of JRIClient for second options in dialog
	private static String basic_host_text = JRIClient.BASIC_HOST_NAME;
	private static String host_text = JRIClient.HOST_NAME;
	
	// members for JavaRServer
	private String bsname, name;
	private JRadioButton btn1,btn2,btn3,btn4,btn5,btn6;
	private JTextField field1, field2;
	
	// members for Login
	private String username, password;
	private JTextField field3, field4;
	private JButton okButton;
	
	
	public JRIChooser(Frame parent){
		super(parent, true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		bsname = JRIClient.BASIC_HOST_NAME;
		name = JRIClient.HOST_NAME;
		
		initComponents();
		this.getRootPane().setDefaultButton(okButton);	
        pack();
	}
	
	private void initComponents(){
        Container content=this.getContentPane();
        content.setLayout(new GridBagLayout());
        
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        JLabel label=new JLabel("Choose JavaRServer Location: ");
        

        // create choice panel for basic architecture
        JPanel choicePanel1=new JPanel();
        choicePanel1.setLayout(new GridBagLayout());

        ButtonGroup group1=new ButtonGroup();
                
        btn1=new JRadioButton("localhost",false);
        btn2=new JRadioButton(basic_host_text,false);
        btn3=new JRadioButton("other:",false);
                
        group1.add(btn1);
        group1.add(btn2);
        group1.add(btn3);
        field1=new JTextField("", 15);
        
        if (bsname.equals("localhost"))
        	btn1.setSelected(true);
        else if (bsname.equals(basic_host_text))
        	btn2.setSelected(true);
        else{
        	btn3.setSelected(true);
        	field1.setText(bsname);
        }
        	
        choicePanel1.add(btn1,new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel1.add(btn2,new GridBagConstraints(0,1,0,-1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel1.add(btn3,new GridBagConstraints(0,2,-1,0,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel1.add(field1,new GridBagConstraints(1,2,0,0,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        
        Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Basic");
        choicePanel1.setBorder(titledBorder);
        
        
        // create choice panel for enhanced architecture
        JPanel choicePanel2=new JPanel();
        choicePanel2.setLayout(new GridBagLayout());

        ButtonGroup group2=new ButtonGroup();
                
        btn4=new JRadioButton("localhost",false);
        btn5=new JRadioButton(host_text,false);
        btn6=new JRadioButton("other:",false);
                
        group2.add(btn4);
        group2.add(btn5);
        group2.add(btn6);
        field2=new JTextField("", 15);
        
        if (name.equals("localhost"))
        	btn4.setSelected(true);
        else if (name.equals(host_text))
        	btn5.setSelected(true);
        else{
        	btn6.setSelected(true);
        	field2.setText(name);
        }
        	
        choicePanel2.add(btn4,new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel2.add(btn5,new GridBagConstraints(0,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel2.add(btn6,new GridBagConstraints(0,2,-1,-1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel2.add(field2,new GridBagConstraints(1,2,0,-1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        choicePanel2.add(createLoginPanel(),new GridBagConstraints(0,3,0,0,1,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        
        lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        titledBorder=BorderFactory.createTitledBorder(lineBorder, "Enhanced");
        choicePanel2.setBorder(titledBorder);
        
        Container buttonPanel = createButtonPanel();        
        
        if (!LaunchOption.workflowEnable)
        	choicePanel2.setVisible(false);
        
        mainPanel.add(label, new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        mainPanel.add(choicePanel1,new GridBagConstraints(0,1,0,1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        mainPanel.add(choicePanel2,new GridBagConstraints(0,2,0,-1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        mainPanel.add(buttonPanel,new GridBagConstraints(0,3,0,0,1,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));

        content.add(mainPanel,new GridBagConstraints(0,0,0,0,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(10,10,10,10),8,8));
    }
	
	private Container createLoginPanel(){
    	JLabel label1 = new JLabel("User Name: ",JLabel.LEFT);
    	JLabel label2 = new JLabel("Password: ",JLabel.LEFT);
    	field3 = new JTextField("", 10);
    	field4 = new JPasswordField(10);
    	
    	JPanel panel1=new JPanel(new GridLayout(2,2,2,2));
    	panel1.add(label1);
    	panel1.add(field3);
    	panel1.add(label2);
    	panel1.add(field4);
    	
    	Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Login");
    	panel1.setBorder(titledBorder);
    	return panel1;    	    	
    }
	
	private Container createButtonPanel()
    {
		JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,30,24));
        okButton=new JButton("Ok");
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        JButton cancelButton=new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
	
	
	public void actionPerformed(final java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();

        if(cmd.equals("Cancel")){
        	bsname = null;
        	name = null;
        	option = CANCEL_OPTION;
            this.dispose();
        }
        else if(cmd.equals("Ok")){
        	
        	if (btn1.isSelected())
        		bsname = "localhost";
        	else if (btn2.isSelected())
        		bsname = btn2.getText();
        	else if (btn3.isSelected())
        		bsname = field1.getText();
        	
        	if (btn4.isSelected())
        		name = "localhost";
        	else if (btn5.isSelected())
        		name = btn5.getText();
        	else if (btn6.isSelected())
        		name = field2.getText();
        	
        	if (bsname == null || bsname.trim().equals("")) {
        		JOptionPane.showMessageDialog(this, "You must input basic server");
				return;
        	}
        	
        	
        	String s = field3.getText(); //username
    		if (s != null && s.trim().length() != 0) {
    			
    			if (name == null || name.trim().equals("")) {
            		JOptionPane.showMessageDialog(this, "You must input enhanced server");
    				return;
            	}    
    			
    			if (field4.getText() == null || field4.getText().length() == 0) {
    				JOptionPane.showMessageDialog(this, "You must input password");
    				return;
    			}
    		}
    		this.username = field3.getText().trim();
    		this.password = field4.getText();
    		
    		option = OK_OPTION;
        	this.dispose();
        }

    }
	
	public String getBasicHost(){
		return bsname;
	}
	
	public String getEnhancedHost(){
		return name;
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
	
	public static void main(String[] args){
        JFrame f=new JFrame();
        JRIChooser td=new JRIChooser(f);
        td.show();
        System.out.println(td.getBasicHost() + "," + td.getEnhancedHost());
    }
}
