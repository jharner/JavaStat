package wvustat.swing;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.util.*;

import wvustat.interfaces.*;

public class SortTableDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	
	private static ImageIcon UpIcon = new ImageIcon(SortTableDialog.class.getResource("/jlfgr/16x16/Up.gif")); 
	private static ImageIcon DownIcon = new ImageIcon(SortTableDialog.class.getResource("/jlfgr/16x16/Down.gif")); 
	
	private DataSet data;
	private JButton okBtn;
	
	private DefaultListModel listModel;
	private JList colList, varList;
	private JTextField field1;
	
	private Map varOrders;
	private String tableName;
	private int option = CANCEL_OPTION;
	
	
	public SortTableDialog(Frame parent, DataSet data) {
		super(parent, true);
		this.data = data;
		varOrders = new Hashtable();
		initComponents();
	}
	
	private void initComponents(){
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		content.add(createColumnsPanel(), new GridBagConstraints(0,0,1,0,0.5,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createSortPanel(), new GridBagConstraints(1,0,-1,-1,0.5,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createInputNamePanel(), new GridBagConstraints(1,1,-1,0,0.5,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		content.add(createSubmitButtonPanel(), new GridBagConstraints(2,0,0,0,0,1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		
		this.getRootPane().setDefaultButton(okBtn);	
	}
	
	private Container createColumnsPanel(){
		Vector v = data.getVariables();
		Vector names = new Vector();
		for (int i = 0; i < v.size(); i++) {
            Variable var = (Variable) v.elementAt(i);
            names.addElement(var.getName());
		}
		
		colList = new JList(names);
		colList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane listScroller = new JScrollPane(colList);
		listScroller.setPreferredSize(new Dimension(150,180));
		panel.add(listScroller, BorderLayout.CENTER);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); 
        panel.setBorder(lineBorder);
		
		return panel;
	}
	
	private Container createSubmitButtonPanel(){
		okBtn=new JButton("Ok");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JPanel panel=new JPanel(new GridLayout(2,1,20,20));
		panel.add(okBtn);		
        panel.add(cancelBtn);
        
        return panel;
	}
	
	private Container createByButtonPanel(){
		JButton byBtn = new JButton("By");
		byBtn.addActionListener(this);
		
		JButton rmBtn = new JButton("Remove");
		rmBtn.addActionListener(this);
		
		JPanel panel = new JPanel(new GridLayout(2,1,20,20));
		panel.add(byBtn);
		panel.add(rmBtn);
		
		return panel;
	}
	
	private Container createOrderButtonPanel(){
		
		JButton upBtn = new JButton(UpIcon);
		upBtn.setActionCommand("Ascending");
		upBtn.addActionListener(this);
		upBtn.setToolTipText("Ascending");
		
		JButton downBtn = new JButton(DownIcon);
		downBtn.setActionCommand("Descending");
		downBtn.addActionListener(this);
		downBtn.setToolTipText("Descending");
		
		JPanel panel = new JPanel(new GridLayout(1,2,20,20));
		panel.add(upBtn);
		panel.add(downBtn);
		
		return panel;
	}
	
	private Container createVarListPanel(){
	
		listModel = new DefaultListModel();
		varList = new JList(listModel);
		varList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		varList.setCellRenderer(new IconedCellRenderer());
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane listScroller = new JScrollPane(varList);
		listScroller.setPreferredSize(new Dimension(150,155));
		panel.add(listScroller, BorderLayout.CENTER);
		
		return panel;
	}
	
	private Container createSortPanel(){
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(createByButtonPanel(), new GridBagConstraints(0,0,1,-1,0,1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		panel.add(createVarListPanel(), new GridBagConstraints(1,0,0,-1,1,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		panel.add(createOrderButtonPanel(), new GridBagConstraints(1,1,0,0,1,0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
	
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        panel.setBorder(lineBorder);
        
        return panel;
	}
	
	private Container createInputNamePanel(){
		JPanel panel=new JPanel(new BorderLayout());
		JLabel label1 = new JLabel("Table Name: ",JLabel.LEFT);
		field1 = new JTextField("", 10);
		panel.add(label1, BorderLayout.WEST);
		panel.add(field1, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(2,0,4,0));
		return panel;
	}

	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Remove")){
			int index = varList.getSelectedIndex();
			if (index != -1){
				varOrders.remove(listModel.elementAt(index));
				listModel.remove(index);				
			}
		}
		else if(cmd.equals("By")){
			int index = colList.getSelectedIndex();
			if (index != -1){
				String name = (String)colList.getSelectedValue();
				if(!listModel.contains(name)){
					varOrders.put(name, Boolean.TRUE);
					listModel.addElement(name);					
				}
			}
		}
		else if(cmd.equals("Ascending")){
			String name = (String)varList.getSelectedValue();
			if (name != null){
				varOrders.put(name, Boolean.TRUE);
				varList.repaint();
			}
		}
		else if(cmd.equals("Descending")){
			String name = (String)varList.getSelectedValue();
			if (name != null){
				varOrders.put(name, Boolean.FALSE);
				varList.repaint();
			}
		}
		else if(cmd.equals("Ok")){
			int size = listModel.size();
			if (size == 0){
				JOptionPane.showMessageDialog(this, "You must define a sorting variable");
				return;
			}
			tableName = field1.getText();
			option = OK_OPTION;
			dispose();
		}
		else{
			option = CANCEL_OPTION;
			dispose();
		}
	}
	
	public int getOption()
    {
        return option;
    }
	
	public Vector getVarNames()
	{
		return new Vector(Arrays.asList(listModel.toArray()));
	}
	
	/**
	 * The sorting order for variables. The values in the map is of type Boolean. 
	 * The Boolean.TRUE value means ascending order sorting. The Boolean.FALSE means descending order sorting.
	 */
	public Map getVarOrders()
	{
		return varOrders;
	}
	
	public String getTableName()
	{
		return tableName;
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
			
			boolean b = ((Boolean)varOrders.get(value)).booleanValue();
			if (b) {
				setIcon(UpIcon);
			} else {
				setIcon(DownIcon);
			}

			return this;
		}
	}
}
