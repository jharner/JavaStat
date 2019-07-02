package wvustat.swing;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import wvustat.interfaces.DataSet;
import wvustat.table.DataEntryTable;
import wvustat.table.MainPanel;
import wvustat.table.DataSetTM;

public class MergeTableDialog extends JDialog implements ActionListener, WindowFocusListener {
	private static final long serialVersionUID = 1L;
	
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	
	private int option = CANCEL_OPTION;
	
	private JButton okBtn;
	private JTextField field1;
	private DefaultComboBoxModel dataOptions;
	private JComboBox dataList;
	private MainPanel mainPanel;
	private DataSet data;
	private DataSetTM dataMergeTo;
	private String tableName;
	private JCheckBox dupCheck;
	private JRadioButton btn1,btn2;
	private boolean dupRemoved, byKey;
		
	
	public MergeTableDialog(Frame parent, MainPanel mainPanel, DataSet data) {
		super(parent, true);
		this.mainPanel = mainPanel;
		this.data = data;
		dataOptions = new DefaultComboBoxModel();
		
		if (mainPanel != null) {	
			ArrayList alist = mainPanel.getDataSetList();
			for (int i = 0; i < alist.size(); i++) 
			{
				dataOptions.addElement( ( (DataSet) alist.get(i) ).getName() );
			}
		}
		
		initComponents();
		
		addWindowFocusListener(this);
	}
	
	private void initComponents(){
		
		JPanel content=(JPanel)this.getContentPane();
		content.setLayout(new GridBagLayout());
		
		JLabel label1 = new JLabel("Table name: ",JLabel.LEFT);
		field1 = new JTextField("", 10);
		JLabel label2 = new JLabel("<html><i>(optional)</i></html>",JLabel.LEFT);
		dupCheck = new JCheckBox("Remove variables with same names", true);
		
		content.add(label1, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(field1, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(label2, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(createDataPanel(), new GridBagConstraints(0,1,3,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(createMethodPanel(), new GridBagConstraints(0,2,3,1,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(6,0,2,2),0,0));
		content.add(dupCheck, new GridBagConstraints(0,3,3,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,0,2,2),0,0));
		content.add(createSubmitButtonPanel(), new GridBagConstraints(0,4,3,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		
		content.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
		
		this.getRootPane().setDefaultButton(okBtn);	
		pack();
        setResizable(false);
	}
	
	private Container createDataPanel(){
		
		JPanel panel=new JPanel(new BorderLayout());
		JLabel label = new JLabel("merge with data: ",JLabel.LEFT);
		dataList = new JComboBox(dataOptions);
		
		panel.add(label, BorderLayout.WEST);
		panel.add(dataList, BorderLayout.CENTER);
		return panel;    
	}
	
	private Container createMethodPanel() {
		JPanel choicePanel=new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
        ButtonGroup group=new ButtonGroup();
        btn1=new JRadioButton("Side By Side",true);
        btn2=new JRadioButton("By Key (Label)",false);
        btn2.setEnabled(true);
        group.add(btn1);
        group.add(btn2);
        choicePanel.add(btn1);
        choicePanel.add(btn2);    
        
        Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Method");
        Border emptyBorder=BorderFactory.createEmptyBorder(10,12,12,12);
        Border compositeBorder=BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
        choicePanel.setBorder(compositeBorder);
        
        return choicePanel;
	}
	
	private Container createSubmitButtonPanel(){
		okBtn=new JButton("Ok");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JPanel panel=new JPanel(new GridLayout(1,2,20,20));
		panel.add(okBtn);		
        panel.add(cancelBtn);
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,2,20));
        
        return panel;
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Ok")){
			tableName = field1.getText();
			
			int index = dataList.getSelectedIndex();
			
			if (index == -1) {
				JOptionPane.showMessageDialog(this, "There is no open data set on the desktop");
        		return;
			}
			
			byKey = btn2.isSelected();
			dupRemoved = dupCheck.isSelected();
			
			if (mainPanel != null) {
				DataSet ds = (DataSet) mainPanel.getDataSetList().get(index);
				if (ds.getName().equals(this.data.getName())) {
					JOptionPane.showMessageDialog(this, "The focused data set (" + this.data.getName() + ") cannot merge to itself (" + ds.getName() + 
														"). Please choose another data set.\n Or click title of the window to get the data set focused.");
					return;
				}
				if (!byKey && ds.getSize() != data.getSize()) {
					JOptionPane.showMessageDialog(this, "Merged data sets must have same size.");
					return;
				}
				if (byKey && (ds.getLabelVariable() == null || data.getLabelVariable() == null)) {
					JOptionPane.showMessageDialog(this, "Both data sets must have label variable.");
					return;
				}
				
				this.dataMergeTo = ((DataEntryTable)mainPanel.dataList.get(index)).getTableModel();
			}
									
			option = OK_OPTION;
			dispose();
		
		} else {
			option = CANCEL_OPTION;
			dispose();
		}
	}
	
	public void windowGainedFocus(WindowEvent e) 
	{
		//refresh combo box dataList of open dataset tables
		dataOptions.removeAllElements();
		if (mainPanel != null) {	
			ArrayList alist = mainPanel.getDataSetList();
			for (int i = 0; i < alist.size(); i++) 
			{
				dataOptions.addElement( ( (DataSet) alist.get(i) ).getName() );
			}
		}
	}
	
	public void windowLostFocus(WindowEvent e) 
	{
		
	}
	
	public int getOption()
    {
        return option;
    }
	
	public DataSetTM getDataMergeTo()
	{
		return dataMergeTo;
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	public boolean isDupRemoved()
	{
		return dupRemoved;
	}
	
	public boolean isByKey()
	{
		return byKey;
	}
	
}
