package wvustat.launcher;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class LoginDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	private String server;
    private boolean serverSetting;
    private boolean enableGuestLogin;

    private JTextField userIdField;
    private JPasswordField passwordField;
    private boolean guest = false;
    private boolean canceled = false;

    public static HashMap showDialog(Frame owner){
	return showDialog(owner, null, false, false);
    }

    public static HashMap showDialog(Frame owner, String defaultServer,
				     boolean enableServerSetting, boolean enableGuestLogin){
	LoginDialog d = 
	    new LoginDialog(owner, defaultServer, enableServerSetting, enableGuestLogin);
	d.setVisible(true);

	if(d.canceled)
	    return null;

	HashMap map = new HashMap();
	map.put("userId", d.getUserId());
	map.put("password", d.getPassword());
	if(enableServerSetting)
	    map.put("serverURL", d.getServer());
	return map;
    }

    protected LoginDialog(Frame owner, String defaultServer, 
			  boolean enableServerSetting, boolean enableGuestLogin){
	super(owner, "Login", true);

	server = defaultServer;
	serverSetting = enableServerSetting;
	this.enableGuestLogin = enableGuestLogin;

	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	buildGUI();
	pack();
	if(owner != null){
	    Rectangle r = owner.getBounds();
	    setLocation(r.x + (r.width - getWidth()) / 2, 
			r.y + (r.height - getHeight()) / 2);
	}
	setResizable(false);
    }

    public String getUserId(){
	if(guest)
	    return "guest";
	else
	    return userIdField.getText().trim();
    }

    public String getPassword(){
	char[] p = passwordField.getPassword();
	return new String(p);
    }

    public String getServer(){
	return server;
    }

    private void buildGUI(){
	Container contentPane = getContentPane();
	JLabel label = new JLabel("Connect to server as:");
	label.setBorder(new EmptyBorder(2, 10, 2, 10));
	contentPane.add(label, BorderLayout.NORTH);

	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.insets = new Insets(5, 5, 5, 5);
	constraints.anchor = GridBagConstraints.WEST;

	JPanel infoArea = new JPanel(gridbag);
	infoArea.setBorder(new EmptyBorder(2, 10, 2, 10));
	
	if(enableGuestLogin){
	ButtonGroup buttonGroup = new ButtonGroup();

	JRadioButton radioButton = new JRadioButton("Guest", false);
	radioButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    userIdField.setEnabled(false);
		    passwordField.setEnabled(false);
		    guest = true;
		}
	    });
	constraints.gridwidth = GridBagConstraints.REMAINDER;
	gridbag.setConstraints(radioButton, constraints);
	buttonGroup.add(radioButton);
	infoArea.add(radioButton);

	radioButton = new JRadioButton("Registered User", true);
	radioButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    userIdField.setEnabled(true);
		    passwordField.setEnabled(true);
		    guest = false;
		}
	    });
	gridbag.setConstraints(radioButton, constraints);
	buttonGroup.add(radioButton);
	infoArea.add(radioButton);
	}
	
	label = new JLabel(" User ID:");
	constraints.gridwidth = 1;
	constraints.anchor = GridBagConstraints.EAST;
	gridbag.setConstraints(label, constraints);
	infoArea.add(label);

	userIdField = new JTextField(15);
	userIdField.setEnabled(true);
	constraints.gridwidth = GridBagConstraints.REMAINDER;
	constraints.anchor = GridBagConstraints.WEST;
	gridbag.setConstraints(userIdField, constraints);
	infoArea.add(userIdField);

	label = new JLabel("Password:");
	constraints.gridwidth = 1;
	constraints.anchor = GridBagConstraints.EAST;
	gridbag.setConstraints(label, constraints);
	infoArea.add(label);

	passwordField = new JPasswordField(15);
	passwordField.setEnabled(true);
	constraints.gridwidth = GridBagConstraints.REMAINDER;
	constraints.anchor = GridBagConstraints.WEST;
	gridbag.setConstraints(passwordField, constraints);
	infoArea.add(passwordField);

	contentPane.add(infoArea, BorderLayout.CENTER);

	JPanel controlArea = new JPanel();
	JButton okButton = new JButton(new LoginAction("OK"));
	controlArea.add(okButton);

	controlArea.add(new JButton(new LoginAction("Cancel")));

	if(serverSetting){
	    JButton settingButton = 
		new JButton(new SetServerAction("Server Setting..."));
	    controlArea.add(settingButton);
	}

	contentPane.add(controlArea, BorderLayout.SOUTH);
	getRootPane().setDefaultButton(okButton);
    }

    class LoginAction extends AbstractAction{
    private static final long serialVersionUID = 1L;
	public LoginAction(String label){
	    super(label);
	}

	public void actionPerformed(ActionEvent e){
	    if("Cancel".equals(e.getActionCommand())){
		canceled = true;
		setVisible(false);
		return;
	    }

	    canceled = false;
	    String id = getUserId();
	    String p = getPassword();
	    if(id == null || id.length() == 0){
		JOptionPane.showMessageDialog(LoginDialog.this, 
					      "Empty User ID.", "Error", 
					      JOptionPane.ERROR_MESSAGE);
	    }
	    else if(!"guest".equals(id) && (p == null || p.length() == 0)){
		JOptionPane.showMessageDialog(LoginDialog.this, 
					      "Empty User Password.", "Error", 
					      JOptionPane.ERROR_MESSAGE);
	    }
	    else
		LoginDialog.this.setVisible(false);
	}
    }

    class SetServerAction extends AbstractAction{
    private static final long serialVersionUID = 1L;
	public SetServerAction(String label){
	    super(label);
	}

	public void actionPerformed(ActionEvent e){
	    Object newServer = 
		JOptionPane.showInputDialog(LoginDialog.this, 
					    "URL of Server:", "Input", 
					    JOptionPane.INFORMATION_MESSAGE, 
					    null, null, server);
	    if(newServer != null)
		server = newServer.toString();
	}
    }
}
