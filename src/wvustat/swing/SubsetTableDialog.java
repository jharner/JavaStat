package wvustat.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


public class SubsetTableDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	
	private JButton okBtn;
	private JTextField field1;
	private JCheckBox linkCheck;
	private JRadioButton btn1,btn2,btn3,btn4;
	
	private String tableName;
	private boolean isLinked;
	private int[] rowRange, colRange;
	private int rowCount, colCount;
	private int option = CANCEL_OPTION;
	
	public SubsetTableDialog(Frame parent, int[] rowRange, int[] colRange, int rowCount, int colCount) {
		super(parent, true);
		this.rowRange = rowRange;
		this.colRange = colRange;
		this.rowCount = rowCount;
		this.colCount = colCount;
		initComponents();
	}
	
	private void initComponents(){
		JPanel content=(JPanel)this.getContentPane();
		content.setLayout(new GridBagLayout());
				
		linkCheck = new JCheckBox("Link to original data table", true);
		JLabel label1 = new JLabel("Table name: ",JLabel.LEFT);
		field1 = new JTextField("", 10);
		JLabel label2 = new JLabel("<html><i>(optional)</i></html>",JLabel.LEFT);
		
		
		content.add(label1, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(field1, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(label2, new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		content.add(createRowsPanel(), new GridBagConstraints(0,1,3,1,1,0.5,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(6,0,2,2),0,0));
		content.add(createColumnsPanel(), new GridBagConstraints(0,2,3,1,1,0.5,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,0,6,2),0,0));
		content.add(linkCheck, new GridBagConstraints(0,3,3,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,0,2,2),0,0));
		content.add(createSubmitButtonPanel(), new GridBagConstraints(0,4,3,1,1,0,GridBagConstraints.CENTER,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		
		content.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
		
		this.getRootPane().setDefaultButton(okBtn);	
		pack();
        setResizable(false);
	}
	
	private Container createRowsPanel() {
		// create choice panel for rows
        JPanel choicePanel1=new JPanel();
        choicePanel1.setLayout(new BoxLayout(choicePanel1, BoxLayout.Y_AXIS));

        ButtonGroup group1=new ButtonGroup();
        btn1=new JRadioButton("All rows",false);
        btn2=new JRadioButton("Selected rows",false);
        group1.add(btn1);
        group1.add(btn2);
        
        if (isEmptyRows())
        	btn1.setSelected(true);
        else
        	btn2.setSelected(true);
        
        choicePanel1.add(btn1);
        choicePanel1.add(btn2);       
        
        Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Rows");
        Border emptyBorder=BorderFactory.createEmptyBorder(10,12,12,12);
        Border compositeBorder=BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
        choicePanel1.setBorder(compositeBorder);
        
        return choicePanel1;
	}
	
	private Container createColumnsPanel() {
		// create choice panel for rows
        JPanel choicePanel2=new JPanel();
        choicePanel2.setLayout(new BoxLayout(choicePanel2, BoxLayout.Y_AXIS));

        ButtonGroup group2=new ButtonGroup();
        btn3=new JRadioButton("All columns",false);
        btn4=new JRadioButton("Selected columns",false);
        group2.add(btn3);
        group2.add(btn4);
        
        if (isEmptyColumns())
        	btn3.setSelected(true);
        else
        	btn4.setSelected(true);
        
        choicePanel2.add(btn3);
        choicePanel2.add(btn4);
        
        Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Columns");
        Border emptyBorder=BorderFactory.createEmptyBorder(10,12,12,12);
        Border compositeBorder=BorderFactory.createCompoundBorder(titledBorder, emptyBorder);
        choicePanel2.setBorder(compositeBorder);
        
        return choicePanel2;
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
			isLinked = linkCheck.isSelected();
			option = OK_OPTION;
			dispose();
		
		} else {
			option = CANCEL_OPTION;
			dispose();
		}
	}
	
	private boolean isEmptyRows() {
		if (rowRange == null || rowRange.length == 0) 
			return true;
		else
			return false;
	}
	
	private boolean isEmptyColumns() {
		if (colRange == null || colRange.length == 0) 
			return true;
		
		boolean found = false;
        
        for(int i=0;i<colRange.length;i++) 
        {
        	if (colRange[i] < 2) continue;
        	found = true;
        }
		
        if (!found) return true;
        
		return false;
	}
	
	public int getOption()
    {
        return option;
    }
	
	public String getTableName()
	{
		return tableName;
	}
	
	public boolean isTableLinked()
	{
		return isLinked;
	}
	
	public int[] getRowRange()
	{
		if (btn1.isSelected()) {
			int[] a = new int[rowCount];
			for (int i = 0; i < rowCount; i++)
				a[i] = i;
			return a;
		} else {
			return rowRange;
		}
	}
	
	public int[] getColumnRange()
	{
		if (btn3.isSelected()) {
			int[] a = new int[colCount];
			for (int i = 0; i < colCount; i++)
				a[i] = i;
			return a;
		} else {
			return colRange;
		}
	}
	
	public static void main(String[] args) {
		JFrame fr=new JFrame("Test");
		SubsetTableDialog std = new SubsetTableDialog(fr, new int[0], new int[]{2}, 3, 3);
		std.pack();
		std.show();
	}

}
