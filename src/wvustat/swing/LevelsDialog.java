package wvustat.swing;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

import java.util.*;

public class LevelsDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	
	private Vector levels;
	private DefaultListModel listModel;
	private JList levelsList;
	private JTextField field1;
	private int option = CANCEL_OPTION;
	
	public LevelsDialog(Frame parent, java.util.List levels) {
		super(parent, true);
		if (levels == null)
			this.levels = new Vector();
		else
			this.levels = new Vector(levels);
		initComponents();
	}

	private void initComponents(){
		field1 = new JTextField("", 10);
		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(this);
		
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		content.add(createListPanel(), new GridBagConstraints(0,0,-1,1,1,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,10,2,2),0,0));
		content.add(createButtonPanel(), new GridBagConstraints(1,0,0,1,0,1,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10,2,2,2),0,0));
		content.add(field1, new GridBagConstraints(0,1,-1,1,1,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4,10,2,2),0,0));
		content.add(addBtn, new GridBagConstraints(1,1,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4,2,2,2),0,0));
		content.add(createSubmitButtonPanel(), new GridBagConstraints(0,2,0,0,1,0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
	}
	
	private Container createListPanel() {
		listModel = new DefaultListModel();
		for (int i = 0; i < levels.size(); i++)
			listModel.addElement(levels.elementAt(i));
		levelsList = new JList(listModel);
		levelsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		JScrollPane listScroller = new JScrollPane(levelsList);
		listScroller.setPreferredSize(new Dimension(220,180));
		panel.add(listScroller, BorderLayout.CENTER);
		
		return panel;
	}
	
	private Container createButtonPanel(){
		JButton btn1 = new JButton("Remove");
		JButton btn2 = new JButton("Move Up");
		JButton btn3 = new JButton("Move Down");
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		
		JPanel panel = new JPanel(new GridLayout(3,1,20,20));
		panel.add(btn1);
		panel.add(btn2);
		panel.add(btn3);
		return panel;
	}
	
	private Container createSubmitButtonPanel(){
		JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel panel=new JPanel(new GridLayout(1,2,30,2));

        panel.add(okButton);
        panel.add(cancelButton);
        panel.setBorder(BorderFactory.createEmptyBorder(2,20,2,20));
        return panel;
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Remove")){
			int[] index = levelsList.getSelectedIndices();
			for (int i = index.length - 1; i >=0; i--) {
				listModel.remove(index[i]);
			}
		}
		else if(cmd.equals("Move Up")){
			moveUpBtnClick();
		}
		else if(cmd.equals("Move Down")){
			moveDownBtnClick();
		}
		else if(cmd.equals("Add")){
			String s = field1.getText().trim();
			if (s != null && s.length() != 0)
				if (listModel.indexOf(s) == -1)
					listModel.addElement(s);
		}
		else if(cmd.equals("Ok")){
			levels.removeAllElements();
			for (int i = 0; i < listModel.getSize(); i++)
				levels.addElement(listModel.get(i));
		
			option = OK_OPTION;
			dispose();
		}
		else{
			option = CANCEL_OPTION;
			dispose();
		}
	}
	
	private void moveDownBtnClick(){
		int i, j;
		boolean lastItem;
		Object temp;
		
		lastItem = true;
		
		for (i = listModel.getSize() - 1; i >=0; i--) {
			if (levelsList.isSelectedIndex(i)) {
				
				if (lastItem) {
					lastItem = false;
					
					if (i < listModel.getSize() - 1) {
						temp = listModel.get(i+1);
						listModel.set(i+1, listModel.get(i));
						listModel.set(i, temp);
						levelsList.removeSelectionInterval(i, i);
						levelsList.addSelectionInterval(i+1, i+1);
					}
				} else {
					j = i;
					while (!levelsList.isSelectedIndex(j+1)) {
						temp = listModel.get(j+1);
						listModel.set(j+1, listModel.get(j));
						listModel.set(j, temp);
						levelsList.removeSelectionInterval(j, j);
						levelsList.addSelectionInterval(j+1, j+1);
						j++;
					}					
				}
			}
		}
	}
	
	private void moveUpBtnClick(){
		int i, j;
		boolean firstItem;
		Object temp;
		
		firstItem = true;
		
		for (i = 0; i <= listModel.getSize() - 1; i++) {
			if (levelsList.isSelectedIndex(i)) {
				
				if (firstItem) {
					firstItem = false;
					
					if (i > 0) {
						temp = listModel.get(i-1);
						listModel.set(i-1, listModel.get(i));
						listModel.set(i, temp);
						levelsList.removeSelectionInterval(i, i);
						levelsList.addSelectionInterval(i-1, i-1);
					}
				} else {
					j = i;
					while (!levelsList.isSelectedIndex(j-1)) {
						temp = listModel.get(j-1);
						listModel.set(j-1, listModel.get(j));
						listModel.set(j, temp);
						levelsList.removeSelectionInterval(j, j);
						levelsList.addSelectionInterval(j-1, j-1);
						j--;
					}		
				}
				
			}
		}
	}
	
	public int getOption()
    {
        return option;
    }
	
	public java.util.List getLevels()
	{
		return levels;
	}
	
	public static void main(String[] args)
    {
        JFrame fr=new JFrame("Test");
        LevelsDialog dialog = new LevelsDialog(fr, new Vector());
        dialog.pack();
        dialog.show();
    }
}
