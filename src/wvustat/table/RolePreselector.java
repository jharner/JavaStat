package wvustat.table;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.util.Vector;
import java.rmi.RemoteException;

import wvustat.interfaces.*;
import wvustat.swing.ArrayListTransferHandler;


public class RolePreselector extends JDialog implements ActionListener{
	
	private static ImageIcon CatIcon = new ImageIcon(RolePreselector.class.getResource("/wvustat/swing/cat.gif")); 
	private static ImageIcon NumIcon = new ImageIcon(RolePreselector.class.getResource("/wvustat/swing/num.gif"));
	private static ImageIcon OrdIcon = new ImageIcon(RolePreselector.class.getResource("/wvustat/swing/ordinal.gif"));
	
	private DataSet data;
	private DataSetTM tableModel;
	
	private DefaultListModel[] listModel;
	private JList[] list;
	
	private final int nRoles = 7;
	private final int[] listHeight = {180, 80, 40, 25, 25, 525, 25}; //index by ROLE definition in wvustat/interfaces/DataSet.java
	
	private ArrayListTransferHandler arrayListHandler;
	
	public RolePreselector(JFrame frame, DataSetTM tableModel){
		super(frame, "Preselect Column Roles", true);
		this.tableModel = tableModel;
		this.data = this.tableModel.getDataSet();
		
		listModel = new DefaultListModel[nRoles];
		list = new JList[nRoles];
				
		for (int i=0; i<listModel.length; i++){
			listModel[i] = new DefaultListModel();
			list[i] = new JList(listModel[i]);
		}	
		
		this.arrayListHandler = new ArrayListTransferHandler();
		
		Vector v = data.getVariables();
		for (int i = 0; i < v.size(); i++) {
			Variable var = (Variable) v.elementAt(i);
			listModel[var.getRole()].addElement(var.getName());
		}
		
		
		initComponents();
	}
	
	private void initComponents(){
		
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		
		content.add(createUPanel(), new GridBagConstraints(0,0,1,5,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createXPanel(DataSet.X_ROLE), new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createXPanel(DataSet.Y_ROLE), new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createXPanel(DataSet.Z_ROLE), new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createXPanel(DataSet.L_ROLE), new GridBagConstraints(1,3,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createXPanel(DataSet.F_ROLE), new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createButtonPanel(), new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		
	}
	
	private Container createUPanel(){
		
		JPanel panel=new JPanel();
		
		list[DataSet.U_ROLE].setVisibleRowCount(-1);
		list[DataSet.U_ROLE].setTransferHandler(arrayListHandler);
		list[DataSet.U_ROLE].setDragEnabled(true);
		list[DataSet.U_ROLE].setCellRenderer(new IconedCellRenderer());

		JScrollPane listScroller = new JScrollPane(list[DataSet.U_ROLE]);
		listScroller.setPreferredSize(new Dimension(150,listHeight[DataSet.U_ROLE]));
		panel.add(listScroller);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, RoleEnum.getRoleEnum(DataSet.U_ROLE).getName());
        panel.setBorder(titledBorder);
		
		return panel;
	}
	
	private Container createXPanel(int role){
		
		JPanel panel=new JPanel(new FlowLayout());
				
		JButton btn=new JButton("<>");
		btn.setActionCommand(String.valueOf(role));
		btn.addActionListener(this);
		panel.add(btn);
		
		if(role == DataSet.L_ROLE || role == DataSet.F_ROLE)
			list[role].setVisibleRowCount(1);
		else
			list[role].setVisibleRowCount(-1);
		
		list[role].setTransferHandler(arrayListHandler);
		list[role].setDragEnabled(true);
		list[role].setCellRenderer(new IconedCellRenderer());
		
		JScrollPane listScroller = new JScrollPane(list[role]);
		listScroller.setPreferredSize(new Dimension(150,listHeight[role]));
		
		panel.add(listScroller);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, RoleEnum.getRoleEnum(role).getName());
        panel.setBorder(titledBorder);
		
		return panel;
	}
	
	private Container createButtonPanel(){
		JButton okBtn=new JButton("Ok");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JPanel panel=new JPanel(new GridLayout(2,1,20,20));
		panel.add(okBtn);		
        panel.add(cancelBtn);
        
        return panel;
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		int cmdcode = -1;
		
		try{
			cmdcode = Integer.parseInt(cmd);
		}
		catch(NumberFormatException ex){}
		
		if(cmdcode == DataSet.X_ROLE || cmdcode == DataSet.Y_ROLE || cmdcode == DataSet.Z_ROLE ||
		   cmdcode == DataSet.L_ROLE || cmdcode == DataSet.F_ROLE){
			
			Object[] val1 = list[DataSet.U_ROLE].getSelectedValues();
			Object[] val2 = list[cmdcode].getSelectedValues();
		
			if(cmdcode == DataSet.L_ROLE || cmdcode == DataSet.F_ROLE){
				
				if(val1.length > 1 || (val1.length == 1 && val2.length == 0 && listModel[cmdcode].size() == 1)){
					JOptionPane.showMessageDialog(this,"You only can define one "+RoleEnum.getRoleEnum(cmdcode).getName()+" variable!");
					return;
				}
				
				if(cmdcode == DataSet.L_ROLE && val1.length == 1){
					
					int col = tableModel.getColumnIndex((String)val1[0]);
					Variable v = tableModel.getVariable(col);
					//if (v.getDistinctCatValues().length != v.getSize())
					if (v.getNumOfDistinctCatValues() != v.getSize())
					{
						JOptionPane.showMessageDialog(this, "Label variable needs to have unique values!");
						return;
					}
					
				}
				
			}
			
			for(int i=0; i<val1.length; i++)
				listModel[DataSet.U_ROLE].removeElement(val1[i]);
			for(int i=0; i<val2.length; i++)
				listModel[cmdcode].removeElement(val2[i]);
			for(int i=0; i<val1.length; i++)
				listModel[cmdcode].addElement(val1[i]);
			for(int i=0; i<val2.length; i++)
				listModel[DataSet.U_ROLE].addElement(val2[i]);
			
		}
		else if(cmd.equals("Ok")){
			
			if (listModel[DataSet.L_ROLE].size() > 0){
				String varname = (String)listModel[DataSet.L_ROLE].elementAt(0);
				
				int col = tableModel.getColumnIndex(varname);
				Variable v = tableModel.getVariable(col);
				//if (v.getDistinctCatValues().length != v.getSize())
				if (v.getNumOfDistinctCatValues() != v.getSize())
				{
					JOptionPane.showMessageDialog(this, "Label variable needs to have unique values!");
					return;
				}
			}
			
			for(int i=0; i<nRoles; i++){
				for(int j=0; j<listModel[i].size(); j++){
					String varname = (String)listModel[i].elementAt(j);
					int col = tableModel.getColumnIndex(varname);
					tableModel.setColumnRole(i, col);
				}
			}
			dispose();
		}
		else if(cmd.equals("Cancel")){
			dispose();
		}
		
	}
	
	class IconedCellRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1L;

		public IconedCellRenderer() {
			setOpaque(true);
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
			setText(value.toString());
			
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			
			Variable v = data.getVariable(value.toString());			
			if (v.getType() == Variable.NUMERIC) {
				setIcon(NumIcon);
			} else {
				if (v.isOrdinal())
					setIcon(OrdIcon);
				else
					setIcon(CatIcon);
			}

			return this;
		}
	}

}
