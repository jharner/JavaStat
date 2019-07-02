package wvustat.swing;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import wvustat.network.client.JRIClient;
import wvustat.util.TextFileFilter;

import org.gui.JDirectoryDialog;

public class FileUploader extends JDialog implements ActionListener{
		
	public static final int TYPE_3PRIME_CELS = 0;
	public static final int TYPE_TXT_FILE = 1;
	public static final int TYPE_EXON_CELS = 2;
	
	private JFileChooser fc;
	private JDirectoryDialog directoryDialog;
	private JFrame parent;
	private JTextField field1, field2;
	private JComboBox list;
	private String[] options = {"MoEx-1_0-st-v1", "Mouse430_2", "HuEx-1_0-st-v2", "RaEx-1_0-st-v1"};
	
	private File path;
	private String name, chipType;
	private int type;
	
	public FileUploader(Frame parent, int type){
		super(parent, "Upload Raw Data Files", true);
		this.parent = (JFrame)parent;
		this.type = type;
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.addChoosableFileFilter(new TextFileFilter());
		initComponents();
        pack();
	}

	private void initComponents(){
        Container content=this.getContentPane();
        content.setLayout(new GridBagLayout());
        
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        JPanel choicePanel=new JPanel();
        choicePanel.setLayout(new GridBagLayout());
        
        JLabel label1 = new JLabel("Object Name: ");
        field1 = new JTextField("", 15);
        
        JLabel label2 = new JLabel("Chip Type: ");
        list = new JComboBox(options);
        list.setSelectedIndex(0);
        chipType = (String)list.getSelectedItem();
        if (type != TYPE_EXON_CELS) {
        	label2.setVisible(false);
        	list.setVisible(false);
        }
        
        JLabel label3 = new JLabel();
        if (type == TYPE_TXT_FILE)
        	label3.setText("Choose a File:");
        else 
            label3.setText("Choose a Directory:");
                
        field2 = new JTextField("", 30);
        field2.setEditable(false);
        JButton browseButton=new JButton("Browse");
        browseButton.setActionCommand("Browse");
        browseButton.addActionListener(this);
        
        choicePanel.add(label1,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(field1,new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(label2,new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(list,  new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(label3,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(field2,new GridBagConstraints(1,2,-1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        choicePanel.add(browseButton,new GridBagConstraints(2,2,0,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,80,20));
        JButton okButton=new JButton("Ok");
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        JButton cancelButton=new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(choicePanel,new GridBagConstraints(0,0,0,-1,1,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        mainPanel.add(buttonPanel,new GridBagConstraints(0,1,0,0,1,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(10,50,10,50),0,0));

        content.add(mainPanel,new GridBagConstraints(0,0,0,0,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(10,10,10,10),8,8));
        
	}
	
	public void actionPerformed(ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Cancel")){
            this.dispose();
        }
        else if(cmd.equals("Ok")){
        	
        	String s = field1.getText();
        	if (s == null || s.trim().length() == 0) {
        		JOptionPane.showMessageDialog(this, "You must input a name");
        		return;
        	} else if (!JRIClient.isIdentifier(s.trim())) {
        		JOptionPane.showMessageDialog(this, "illegal object name");
        		return;
        	}
        		
        	if (field2.getText() == null || field2.getText().length() == 0) {
        		JOptionPane.showMessageDialog(this, "You must choose a path");
        		return;
        	}
        	
        	this.name = s.trim();
        	this.chipType = (String)list.getSelectedItem();
        	this.dispose();
        }
        else if(cmd.equals("Browse")){
        	
        	if (type != TYPE_TXT_FILE) {
        		if (directoryDialog == null)
    			{
    				directoryDialog = new JDirectoryDialog(this.parent);
    				directoryDialog.setMessage("Select a directory:");
    			}
    			if (directoryDialog.showDirectoryDialog() && directoryDialog.getSelectedFolder() != null)
    			{
    				path = directoryDialog.getSelectedFolder();
    				field2.setText(path.getAbsolutePath());
    			}	
        	} else {
	        	int retval = fc.showOpenDialog(this);
	        	
	        	if (retval == JFileChooser.APPROVE_OPTION) {
	        		path = fc.getSelectedFile();
	        		field2.setText(path.getAbsolutePath());
	        	}
        	}        	
        }
        
	}
	
	public String getName(){
		return name;
	}
	
	public String getChipType(){
		return chipType;
	}
	
	public int getType(){
		return type;
	}
	
	public File getPath(){
		return path;
	}
	
	public static void main(String[] args){
        JFrame f=new JFrame();
        FileUploader td=new FileUploader(f, 2);
        td.show();
        System.out.println(td.getName());
        System.out.println(td.getPath().getAbsolutePath());
    }
}
