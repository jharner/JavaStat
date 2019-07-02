/*
 * FormulaDialog.java
 *
 * Created on September 24, 2001, 1:59 PM
 */

package wvustat.modules;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.Vector;
/**
 *
 * @author  hxue
 * @version 
 */
public class FormulaDialog extends JDialog implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 1L;
	private String formula, varName;
    private JTextField nameField;
    private JTextArea inputField;
    private JList vList, funcList;
    private Vector vnames;
    private JPopupMenu popup;
    private final String[] category = {"Numeric", "Transcendental", "Trigonometric", "Comparison", "Conditional"};
    private final String[][] funcs = {{"abs()", "sqrt()"},
    								  {"exp()", "ln()", "log10()", "log2()"},
    								  {"sin()", "cos()", "tan()", "cot()", "sec()", "csc()", "arcsin()", "arccos()", "arctan()"},
    								  {">", "<", "==", ">=", "<=", "!="},
    								  {"&&", "||", "!", "true", "false", "if(,,)"}};
    
    /** Creates new FormulaDialog */
    public FormulaDialog(JFrame parent, Vector vnames, String formula) {
        super(parent, "Formula Input", true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.vnames = vnames;
        this.formula = formula;
        initComponents();
        pack();
    }
    
    private void initComponents(){
        JLabel jl1   =new JLabel("Variable Name");
        JLabel label1=new JLabel("Formula:");
        JLabel label2=new JLabel("For example, to perform a square root transformation, input");
        JLabel label3=new JLabel("'sqrt(variable)' and replace 'variable' with the actual variable name.");
        
        Font smallFont=new Font("Serif", Font.PLAIN, 12);
        label2.setFont(smallFont);
        label3.setFont(smallFont);
        
        nameField=new JTextField(8);
        nameField.setFont(new Font("Arial",Font.PLAIN,14));
        
        inputField=new JTextArea(formula, 3, 20);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        //inputField.addActionListener(this);
        inputField.setLineWrap(true);
        
        JButton okButton=new JButton("Ok");
        JButton cancelButton=new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        Container con=this.getContentPane();
        con.setLayout(new GridBagLayout());
        
        con.add(jl1,new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        con.add(nameField, new GridBagConstraints(0,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
        con.add(label1, new GridBagConstraints(0,2,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        //con.add(new JLabel(" "), new GridBagConstraints(0,2,0,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        con.add(label2, new GridBagConstraints(0,3,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        con.add(label3, new GridBagConstraints(0,4,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        con.add(new JScrollPane(inputField), new GridBagConstraints(0,5,0,1,1,0.5,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,8,2),0,0));
        
        vList = new JList(vnames);
        vList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vList.addListSelectionListener(this);
        vList.setVisibleRowCount(5);
        
        funcList = new JList(category);
        funcList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        funcList.addListSelectionListener(this);
        funcList.setVisibleRowCount(5);
        popup = new JPopupMenu();
        
        con.add(new JScrollPane(vList), new GridBagConstraints(0,6,1,-1,0.5,0.5,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
        con.add(createCalcPanel(), new GridBagConstraints(1,6,-1,-1,0,0.5,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        con.add(new JScrollPane(funcList), new GridBagConstraints(2,6,0,-1,0.5,0.5,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
        
        JPanel buttonPane=new JPanel(new GridLayout(1,2,30,8));
        buttonPane.add(okButton);
        buttonPane.add(cancelButton);
        okButton.requestFocus();
        buttonPane.setBorder(new javax.swing.border.EmptyBorder(14,28,8,28));
        
        con.add(buttonPane, new GridBagConstraints(0,7,0,0,1,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));
        ((JPanel)con).setBorder(new javax.swing.border.EmptyBorder(8,8,8,8));
        
    }
    
    private JPanel createCalcPanel(){
    	JPanel panel = new JPanel(new GridLayout(3,3,0,0));
    	
    	JButton btn = new JButton(new CharIcon('+'));
    	btn.setActionCommand("+");
    	btn.addActionListener(this);
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon('-'));
    	btn.setActionCommand("-");
    	btn.addActionListener(this);
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon('*'));
    	btn.setActionCommand("*");
    	btn.addActionListener(this);
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon('/'));
    	btn.setActionCommand("/");
    	btn.addActionListener(this);
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon('^'));
    	btn.setActionCommand("^");
    	btn.addActionListener(this);
    	btn.setToolTipText("Power");
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon('('));
    	btn.setActionCommand("(");
    	btn.addActionListener(this);
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon(')'));
    	btn.setActionCommand(")");
    	btn.addActionListener(this);
    	panel.add(btn);
    	
    	btn = new JButton(new CharIcon('\"'));
    	btn.setActionCommand("\"");
    	btn.addActionListener(this);
    	btn.setToolTipText("String");
    	panel.add(btn);  
    	
    	btn = new JButton(new CharIcon('e'));
    	btn.setActionCommand("e");
    	btn.addActionListener(this);
    	btn.setToolTipText("Scientific notation");
    	panel.add(btn);  
    	
    	return panel;
    }
    
    public String getFormula(){
        return formula;
    }
    
    public String getName(){
    	return varName;
    }

    /** Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        String arg=e.getActionCommand();
        
        /* Object obj=e.getSource();
        if(obj instanceof JTextField){
            varName=nameField.getText();
            formula=inputField.getText();
            this.dispose();
        }        
        else */ 
        
        if(arg.equals("Ok")){
            varName=nameField.getText();
            if(inputField.getText()!=null){
                formula=inputField.getText();
                this.dispose();
            }
        }
        else if(arg.equals("Cancel")){
        	formula = null;
            this.dispose();
        }
        else {
        	inputField.insert(arg, inputField.getCaretPosition());
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
    	if (e.getValueIsAdjusting())
			return;
    	
    	JList theList = (JList)e.getSource();
    	if (theList == vList) {
    		String value = (String)theList.getSelectedValue();
    		if (value != null) {
    			inputField.insert(value, inputField.getCaretPosition());
    		}
    		
    	} else if (theList == funcList) {
    		int index = theList.getSelectedIndex();
    		if (index != -1) {
    			popup.removeAll();
    			for (int i=0; i<funcs[index].length; i++) {
    				JMenuItem jm = new JMenuItem(funcs[index][i]);
    				jm.addActionListener(this); 
    				popup.add(jm);
    			}
    			Rectangle rec = theList.getCellBounds(index, index);
    			popup.show(theList, rec.x+rec.width, rec.y);
    		}
    	}
    }
    
    public static String showFormulaDialog(JFrame parent){
        FormulaDialog fd=new FormulaDialog(parent, new Vector(), "");
        fd.show();
        
        return fd.getFormula();
    }
    
    public static void main(String[] args){
        FormulaDialog fd=new FormulaDialog(new JFrame(), new Vector(), "");
        fd.show();
    }
}
