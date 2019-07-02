package wvustat.swing;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import javax.swing.*;
import wvustat.network.client.JRIClient;


public class ObjectChooser extends JDialog implements ActionListener{
	private JList dataList;
	private String selectedItem;
	private JButton okBtn;
	private String[] selectedItems;
	
	public static final int OK_OPTION = 0;
	public static final int DELETE_OPTION = 1;
    public static final int CANCEL_OPTION = 2;

    private int option = CANCEL_OPTION;
	
	
	public ObjectChooser(Frame parent) throws RemoteException 
	{
		super(parent, true);
		initComponents();
		pack();
	}
	
	private void initComponents() throws RemoteException
	{
		String[] sArray = JRIClient.listObjects();
		
		dataList=new JList(sArray);
		
		dataList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dataList.addMouseListener(new ClickListener());
		JLabel label1=new JLabel("Objects in user " + JRIClient.getUserName() + "'s workspace on server " + JRIClient.HOST_NAME);
		
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		content.add(label1, new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		content.add(new JScrollPane(dataList), new GridBagConstraints(0,1,0,-1,1,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		
		okBtn=new JButton("Open");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JButton deleteBtn=new JButton("Delete");
		deleteBtn.addActionListener(this);
		
		JPanel panel4=new JPanel(new GridLayout(1,3,30,20));
        panel4.add(cancelBtn);
        panel4.add(deleteBtn);
        panel4.add(okBtn);		
        
        content.add(panel4, new GridBagConstraints(0,2,0,0,1.0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,20,2,20), 0, 0));
			
		this.getRootPane().setDefaultButton(okBtn);	
		
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if (cmd.equals("Open")){
			ListSelectionModel model=dataList.getSelectionModel();
			int index=model.getMinSelectionIndex();
			
			if(index!=-1){
				ListModel dModel=dataList.getModel();
				selectedItem=dModel.getElementAt(index).toString();
				selectedItem = selectedItem.substring(0, selectedItem.indexOf("("));
				
				option = OK_OPTION;
				this.dispose();
			}			
		}
		else if (cmd.equals("Delete")){
			int[] indices=dataList.getSelectedIndices();
			
			if (indices.length != 0){
				ListModel dModel=dataList.getModel();
				selectedItems = new String[indices.length];
				
				for (int i = 0; i < indices.length; i++) {
					selectedItems[i] = dModel.getElementAt(indices[i]).toString();
					selectedItems[i] = selectedItems[i].substring(0, selectedItems[i].indexOf("("));
				}
				
				option = DELETE_OPTION;
				this.dispose();
			}
			
		}
		else {
			option = CANCEL_OPTION;
			dispose();
		}
	}
	

	public String getSelectedItem(){
		return selectedItem;
	}
	
	public String[] getSelectedItems(){
		return selectedItems;
	}
	
	public int getOption()
    {
        return option;
    }
	
	class ClickListener extends MouseAdapter{
		public void mouseClicked(MouseEvent evt){
			if(evt.getClickCount()!=2)
				return;
				
			ListSelectionModel model=dataList.getSelectionModel();
			int index=model.getLeadSelectionIndex();
			
			if(index!=-1){
				ListModel dModel=dataList.getModel();
				selectedItem=dModel.getElementAt(index).toString();
				selectedItem = selectedItem.substring(0, selectedItem.indexOf("("));
				
				option = OK_OPTION;
				ObjectChooser.this.dispose();
			}			
		}
	}

}
