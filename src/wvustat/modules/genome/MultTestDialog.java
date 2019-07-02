package wvustat.modules.genome;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;

import java.rmi.RemoteException;
import java.util.*;

import wvustat.table.MainPanel;
import wvustat.interfaces.*;
import wvustat.network.Filterfun;
import wvustat.network.client.JRIClient;
import wvustat.util.ComponentUtil;


public class MultTestDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final String[] testOptions = {"t.twosamp.unequalvar", "t.twosamp.equalvar"};
	private final String[] typeoneOptions = {"fwer", "gfwer", "tppfp", "fdr"};
	private final String[] methodOptions = {"ss.maxT", "ss.minP", "sd.maxT", "sd.minP"};
	private final String[] fdrMethodOptions = {"conservative", "restricted"};
	
	private JComboBox testList, methodList, fdrMethodList;
	private JCheckBox bootstrapBtn;
	private JTextField kField, qField, alphaField, nameField;
	private JRadioButton btn1,btn2,btn3,btn4;
	private MainPanel mainPanel;
	private JTable table, sampleTable;
	
	private String objName, test, typeone, method, mtpName, fdrMethod; 
	private String[] sampleNames;
	private boolean bootstrap;
	private int k;
	private double q, alpha;  
	private ArrayList filters;
	
	public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    private int option = CANCEL_OPTION;
	
	public MultTestDialog(Frame frame, MainPanel mainPanel, String objName, String[] sampleNames){
		super(frame, false); //non-modal
		this.mainPanel = mainPanel;
		this.objName = objName;
		this.sampleNames = sampleNames;

		JPanel content=(JPanel)this.getContentPane();
        content.setLayout(new GridBagLayout());
        
        content.add(createTypeonePanel(), new GridBagConstraints(0,0,1,1,0.5,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		content.add(createOptionsPanel(), new GridBagConstraints(1,0,1,1,0.5,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(createFilterPanel(), new GridBagConstraints(0,0,-1,1,1,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		panel.add(createButtonPanel(), new GridBagConstraints(1,0,0,1,0,0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));		
		
		content.add(createSampleTypePanel(), new GridBagConstraints(0,1,0,-1,1,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(panel, new GridBagConstraints(0,2,0,0,1,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
	
	}
	
	
	private Container createFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		table = new JTable(new FilterTableModel()){
			private static final long serialVersionUID = 1L;
			
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				FilterTableModel model = (FilterTableModel)getModel();
				return model.getRowToolTip(rowIndex);
			}
		};
		
		table.setPreferredScrollableViewportSize(new Dimension(400, 80));
		
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(220);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); 
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Filter functions");
        panel.setBorder(titledBorder);
        
		return panel;
	}
	
	private Container createSampleTypePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		sampleTable = new JTable(new SampleTypeTableModel());
		
		sampleTable.setPreferredScrollableViewportSize(new Dimension(550, 200));
		sampleTable.getColumnModel().getColumn(0).setPreferredWidth(350);
		sampleTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		sampleTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		
		JScrollPane scrollPane = new JScrollPane(sampleTable);
		panel.add(scrollPane, BorderLayout.CENTER);		
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); 
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Sample types");
        panel.setBorder(titledBorder);
        
		return panel;
	}
	
	private Container createTypeonePanel() {
		JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        JPanel choicePanel=new JPanel();
        choicePanel.setLayout(new GridBagLayout());

        ButtonGroup group=new ButtonGroup();
        
        btn1=new JRadioButton(typeoneOptions[0], true);
        btn2=new JRadioButton(typeoneOptions[1], false);
        btn3=new JRadioButton(typeoneOptions[2], false);
        btn4=new JRadioButton(typeoneOptions[3], false);
        
        group.add(btn1);
        group.add(btn2);
        group.add(btn3);
        group.add(btn4);
        
        choicePanel.add(btn1,new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn2,new GridBagConstraints(0,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn3,new GridBagConstraints(0,2,0,-1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn4,new GridBagConstraints(0,3,0,0,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        JPanel constPanel=new JPanel();
        kField = new JTextField("0", 3);
		qField = new JTextField("0.1", 3);
		fdrMethodList = new JComboBox(fdrMethodOptions);
		
		constPanel.setLayout(new GridBagLayout());
        constPanel.add(new JLabel(" "),new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));    
        
        constPanel.add(new JLabel("( k="),new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));        
        constPanel.add(kField,new GridBagConstraints(1,1,-1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        constPanel.add(new JLabel(")"),new GridBagConstraints(2,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0)); 
        
        constPanel.add(new JLabel("( q="),new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        constPanel.add(qField,new GridBagConstraints(1,2,-1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));  
        constPanel.add(new JLabel(")"),new GridBagConstraints(2,2,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0)); 
        
        constPanel.add(fdrMethodList,new GridBagConstraints(0,3,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));  
        
        
        mainPanel.add(choicePanel,new GridBagConstraints(0,0,-1,0,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        mainPanel.add(constPanel,new GridBagConstraints(1,0,0,0,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        
        Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); 
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Type I error rate");
        mainPanel.setBorder(titledBorder);
        
		return mainPanel;
	}
	
	private Container createOptionsPanel(){
		JPanel panel1=new JPanel(new GridLayout(5,1,0,2));
		
		JLabel label = new JLabel("name: ",JLabel.LEFT);
		panel1.add(label);
		label = new JLabel("alpha: ",JLabel.LEFT);
		panel1.add(label);
		label = new JLabel("test: ",JLabel.LEFT);
		panel1.add(label);
		label = new JLabel("bootstrapping:",JLabel.LEFT);
		panel1.add(label);
		label = new JLabel("multiple method",JLabel.LEFT);
		panel1.add(label);
		
		JPanel panel2=new JPanel(new GridLayout(5,1,0,2));
		
		nameField = new JTextField("mtp01", 10);
		alphaField = new JTextField("0.05", 4);
		testList = new JComboBox(testOptions);
		bootstrapBtn = new JCheckBox();
		bootstrapBtn.setSelected(true);
		methodList = new JComboBox(methodOptions);
		panel2.add(nameField);
		panel2.add(alphaField);
		panel2.add(testList);
		panel2.add(bootstrapBtn);
		panel2.add(methodList);
		
		JPanel panel=new JPanel(new BorderLayout());
    	panel.add(panel1, BorderLayout.WEST);
    	panel.add(panel2, BorderLayout.CENTER);
    	
    	Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); 
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Options");
        panel.setBorder(titledBorder);
    	return panel;    
	}

	
	private Container createButtonPanel()
    {
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel panel=new JPanel(new GridLayout(2,1,20,20));

        panel.add(okButton);
        panel.add(cancelButton);
      
        panel.setBorder(BorderFactory.createEmptyBorder(2,20,2,20));
        return panel;
    }
	
	public void actionPerformed(ActionEvent e)
    {
		if (e.getActionCommand() == "Ok")
        {
			try {
				this.filters = ((FilterTableModel)table.getModel()).getFilterFun();
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
        		return;
			}
			
			if(btn1.isSelected())
				typeone = typeoneOptions[0];
			else if(btn2.isSelected())
				typeone = typeoneOptions[1];
			else if(btn3.isSelected())
				typeone = typeoneOptions[2];
			else if(btn4.isSelected())
				typeone = typeoneOptions[3];
			
			test = (String)testList.getSelectedItem();
			method = (String)methodList.getSelectedItem();
			fdrMethod = (String)fdrMethodList.getSelectedItem();
			bootstrap = bootstrapBtn.isSelected();
			
			try {
				k = Integer.parseInt(kField.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "k must be an integer");
        		return;
			}
			
			try {
				q = Double.parseDouble(qField.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "q must be a number");
        		return;
			}
			
			try {
				alpha = Double.parseDouble(alphaField.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "alpha must be a number");
        		return;
			}
			
			
			mtpName = nameField.getText().trim();
			if (mtpName.length() == 0) {
				JOptionPane.showMessageDialog(this, "You must input name");
        		return;
			} else if (!JRIClient.isIdentifier(mtpName)) {
        		JOptionPane.showMessageDialog(this, "illegal name");
        		return;
        	}
			
			String[] sampleTypes = ((SampleTypeTableModel)sampleTable.getModel()).getSampleTypes();
			ArrayList yIncluded = new ArrayList();
			ArrayList yValues = new ArrayList();
			
			for (int i = 0; i < sampleTypes.length; i++) {
				if (sampleTypes[i].trim().equals("")) 
					yIncluded.add(Boolean.FALSE); 
				else {
					yIncluded.add(Boolean.TRUE);
					yValues.add(sampleTypes[i].trim());
				}
			}
						
			option = OK_OPTION;
			dispose();
			
			/* this dialog is non-modal. the module that calls this dialog does not get the control after dialog is disposed.
			 * so we need to put the afterwards code here. 
			 */
			try
    		{        		
    			JRIClient.mtp(mainPanel, objName, mtpName, getTest(), getTypeone(),
    					getK(), getQ(), getAlpha(), getMethod(), getFdrMethod(), isBootstrap(), 
    					Arrays.asList(sampleTypes), yValues, yIncluded, getFilterFun());
    		}
    		catch(RemoteException rex){
    			ComponentUtil.showErrorMessage(mainPanel, rex.getMessage());
        	}
            
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
	
	public String getTest()
	{
		return test;
	}
	
	public String getTypeone()
	{
		return typeone;
	}
	
	public int getK()
	{
		return k;
	}
	
	public double getQ()
	{
		return q;
	}
	
	public String getName()
	{
		return mtpName;
	}	
	
	public double getAlpha()
	{
		return alpha;
	}
	
	public String getMethod()
	{
		return method;
	}
	
	public String getFdrMethod()
	{
		return fdrMethod;
	}
	
	public boolean isBootstrap()
	{
		return bootstrap;
	}
	
	public ArrayList getFilterFun()
	{
		return filters;
	}
	
	class FilterTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columnNames = {"", "filter", "p1", "p2"};
		private Object[][] data = { 
									{new Boolean(true), Filterfun.POVERA, new Double(0.25), new Double(100)},
									{new Boolean(true), Filterfun.IQR, new Double(1.4), null},
									{new Boolean(true), Filterfun.CV, new Double(0.7), new Double(10)}
								  };
		private Class[] cls = {Boolean.class, String.class, Double.class, Double.class};
		private String[] tooltips = {"at least p1 * 100% of the samples have expression measurements > p2",
									 "interquartile range across the samples > p1",
									 "coefficient of variation of intensities across samples is between p1 and p2"
									};
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public int getRowCount() {
			return data.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}
		
		public Class getColumnClass(int c) {
			return cls[c];
		}
		
		public boolean isCellEditable(int row, int col) {
			if (col == 1)
				return false;
			else
				return true;
		}
		
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
		
		public ArrayList getFilterFun() {
			ArrayList lst = new ArrayList();
			
			for (int i = 0; i < data.length; i++) 
				if (((Boolean)data[i][0]).booleanValue() == true) {
					Filterfun f = new Filterfun((String)data[i][1], (Double)data[i][2], (Double)data[i][3]);
					lst.add(f);
				}
			
			return lst;
		}
		
		public String getRowToolTip(int row) {
			return tooltips[row];
		}
		
	}
	
	class SampleTypeTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columnNames = {"Sample Name", "Treatment", "Normal"};
		private Class[] cls = {String.class, Boolean.class, Boolean.class};
		private boolean[][] data;
		
		public SampleTypeTableModel() {
			data = new boolean[2][sampleNames.length];
			for (int i = 0; i < data.length; i++)
				for (int j = 0; j < data[i].length; j++)
					data[i][j] = false;
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public int getRowCount() {
			return sampleNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		public Object getValueAt(int row, int col) {
			if (col == 0)
				return sampleNames[row];
			else
				return new Boolean(data[col - 1][row]);
		}
		
		public Class getColumnClass(int c) {
			return cls[c];
		}
		
		public boolean isCellEditable(int row, int col) {
			if (col == 0)
				return false;
			else
				return true;
		}
		
		public void setValueAt(Object value, int row, int col) {
			data[col-1][row] = ((Boolean)value).booleanValue();
			if (data[0][row] && data[1][row]) {
				data[col % 2][row] = false;
			}
			fireTableRowsUpdated(row, row);
		}
		
		public String[] getSampleTypes() {
			String[] lst = new String[sampleNames.length];
			for (int i = 0; i < data[0].length; i++) {
				if (data[0][i] == true)	lst[i] = "T";
				else if (data[1][i] == true) lst[i] = "N";
				else lst[i] = "";
			}
			return lst;
		}
	}
	
	public static void main(String[] args)
    {
        JFrame fr=new JFrame("Test");
        MultTestDialog md = new MultTestDialog(fr, null, null, new String[]{"array1", "array2", "array3"});
        md.pack();
        md.show();
    }
}
