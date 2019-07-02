/*
 * ColumnEditor.java
 *
 * Created on October 3, 2000, 3:16 PM
 */

package wvustat.table;

/**
 * ColumnEditor is a dialog component that lets a user defines the name of a table column and the
 * data typed for cells in the column.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 3, 2000
 */

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import wvustat.swing.LevelsDialog;

public class ColumnEditor extends JDialog implements ActionListener
{	private static final long serialVersionUID = 1L;
	
	private JTextField nameField;
    private JComboBox typeChooser;
    private JComboBox roleChooser;
    private JComboBox comboDigits;
    private ColumnAttributes columnAttributes;
    private JCheckBox levelChecker;
    private JFrame parent;    

    public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    private int option = CANCEL_OPTION;

    public ColumnEditor(JFrame frame, ColumnAttributes attributes)
    {
        super(frame, "Column Properties", true);
        parent = frame;
        columnAttributes = attributes;
        JPanel contentPane=(JPanel)this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private Container createCenterPanel()
    {
        JLabel nameLabel = new JLabel("Column Name:");
        JLabel typeLabel = new JLabel("Data Type:");
        JLabel roleLabel = new JLabel("Column Role:");

        nameField = new JTextField(15);
        nameField.setText(columnAttributes.getName());

        comboDigits=new JComboBox();
        comboDigits.addItem(new ComboElement("Default", -1));
        comboDigits.addItem(new ComboElement("0 decimal point", 0));
        comboDigits.addItem(new ComboElement("1 decimal point", 1));
        comboDigits.addItem(new ComboElement("2 decimal points",2));
        comboDigits.addItem(new ComboElement("3 decimal points", 3));
        comboDigits.addItem(new ComboElement("4 decimal points", 4));

        levelChecker = new JCheckBox("Check Levels", columnAttributes.isLevelCheck());
        levelChecker.setEnabled(false);//changed on Sept, 2010
        final JButton levelEditor = new JButton("Edit Levels");
        levelChecker.addItemListener(new ItemListener(){
        	public void itemStateChanged(ItemEvent e) {
        		if (e.getStateChange() == ItemEvent.SELECTED)
        			levelEditor.setEnabled(true);
        		else
        			levelEditor.setEnabled(false);
        	}
        });
                
        levelEditor.setEnabled(levelChecker.isSelected());
        levelEditor.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e) {
        		LevelsDialog ld = new LevelsDialog(parent, columnAttributes.getLevels());
        		ld.pack();
        		ld.setLocationRelativeTo(parent);
        		ld.show();
        		if (ld.getOption() == LevelsDialog.OK_OPTION)
        			columnAttributes.setLevels(ld.getLevels());       			
        	}
        });        
        
        typeChooser = new JComboBox(new String[]{"Numerical", "Categorical"/*, "Ordinal"*/});
        typeChooser.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                if(typeChooser.getSelectedIndex()==0) {
                    comboDigits.setEnabled(true);
                    levelChecker.setEnabled(false);
                    levelEditor.setEnabled(false);
                } else {
                    comboDigits.setEnabled(false);
                    //levelChecker.setEnabled(true);
                    levelChecker.setEnabled(false);
                    if(levelChecker.isSelected()) levelEditor.setEnabled(true);
                }
            }
        });
        if (columnAttributes.getType() == 1 && columnAttributes.isOrdinal())
        	typeChooser.setSelectedIndex(2);
        else	
        	typeChooser.setSelectedIndex(columnAttributes.getType());

        roleChooser = new JComboBox();
        roleChooser.addItem(RoleEnum.U_ROLE);
        roleChooser.addItem(RoleEnum.X_ROLE);
        roleChooser.addItem(RoleEnum.Y_ROLE);
        roleChooser.addItem(RoleEnum.Z_ROLE);
        roleChooser.addItem(RoleEnum.L_ROLE);
        roleChooser.addItem(RoleEnum.F_ROLE);
        roleChooser.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                if(roleChooser.getSelectedItem().equals(RoleEnum.F_ROLE)){
                    comboDigits.setSelectedIndex(1);
                }
            }
        });
        roleChooser.setSelectedItem(RoleEnum.getRoleEnum(columnAttributes.getRole()));

        DefaultComboBoxModel model=(DefaultComboBoxModel)comboDigits.getModel();
        int index=model.getIndexOf(new ComboElement("", columnAttributes.getNumOfDigits()));
        comboDigits.setSelectedIndex(index);
        
        JPanel panel=new JPanel();
        FormLayout formLayout=new FormLayout("pref,4dlu, 60dlu", "pref,2dlu,pref,2dlu,pref,2dlu, pref,2dlu,pref");
        CellConstraints cc=new CellConstraints();
        panel.setLayout(formLayout);
        panel.add(nameLabel, cc.xy(1,1));
        panel.add(nameField, cc.xy(3,1));
        panel.add(typeLabel, cc.xy(1,3));
        panel.add(typeChooser, cc.xy(3,3));
        panel.add(roleLabel, cc.xy(1,5));
        panel.add(roleChooser, cc.xy(3,5));
        panel.add(new JLabel("Column Format"), cc.xy(1,7));
        panel.add(comboDigits, cc.xy(3,7));
        panel.add(levelChecker, cc.xy(1, 9));
        panel.add(levelEditor, cc.xy(3, 9));

        panel.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
        return panel;
     }

    private Container createButtonPanel()
    {
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

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "Ok")
        {
            if (nameField.getText().equals(""))
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid name!");
                return;
            }
            columnAttributes.setName(nameField.getText());
            RoleEnum enum1 = (RoleEnum) roleChooser.getSelectedItem();
            columnAttributes.setRole(enum1.getRole());
            
            if (typeChooser.getSelectedIndex()==2){ //ordinal
            	columnAttributes.setType(1);
            	columnAttributes.setOrdinal(true);
            } else {
            	columnAttributes.setType(typeChooser.getSelectedIndex());
            	columnAttributes.setOrdinal(false);
            }
            
            ComboElement elem=(ComboElement)comboDigits.getSelectedItem();
            columnAttributes.setNumOfDigits(elem.getValue());

            columnAttributes.setLevelCheck(levelChecker.isSelected());
            
            option = OK_OPTION;
            dispose();
        }
        else if (e.getActionCommand() == "Cancel")
        {
            option = CANCEL_OPTION;
            dispose();
        }
    }

    public ColumnAttributes getColumnAttributes()
    {
        return columnAttributes;
    }

    public int getOption()
    {
        return option;
    }

    static class ComboElement
    {
        private String name;
        private int value;

        public ComboElement(String name, int value)
        {
            this.name = name;
            this.value=value;
        }
        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getValue()
        {
            return value;
        }

        public void setValue(int value)
        {
            this.value = value;
        }

        public String toString()
        {
            return name;
        }

        public boolean equals(Object obj)
        {
            if(obj instanceof ComboElement)
            {
                return this.value==((ComboElement)obj).value;
            }
            else
                return false;
        }
    }

    public static void main(String[] args)
    {
        JFrame fr=new JFrame("Test");
        ColumnEditor editor=new ColumnEditor(fr, new ColumnAttributes());
        editor.pack();
        editor.show();
    }

}
