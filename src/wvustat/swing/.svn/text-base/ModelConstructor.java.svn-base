package wvustat.swing;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.util.Vector;

import wvustat.interfaces.*;

public class ModelConstructor extends JDialog implements ActionListener{
	private DataSet data;
	private JButton okBtn;
	private String model;
	private String family;
	private String link;
	
	private DefaultListModel listModel;
	private JList xList;
	private Vector xNames;
	
	private final String[] familyOptions = {"gaussian","binomial","poisson","Gamma","inverse.gaussian"}; 
	private final String[][] linkOptions = {{"identity","log","inverse"},
			                                {"logit","probit","cauchit","log","cloglog"},
			                                {"log","identity","sqrt"},
			                                {"inverse","identity","log"},
			                                {"1/mu^2","inverse","identity","log"}
			                               };
	
	private DefaultComboBoxModel linkModel;
	private JComboBox familyList;
	private JComboBox linkList;
	
	
	public ModelConstructor(Frame parent, DataSet data){
		super(parent, true);
		this.data = data;
		initComponents();
	}
	
	private void initComponents(){
		
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		
		content.add(createYPanel(), new GridBagConstraints(0,0,1,1,0.5,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		content.add(createZPanel(), new GridBagConstraints(1,0,1,1,0.5,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		content.add(createFamilyPanel(), new GridBagConstraints(2,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,2,0,2),0,0));
		
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(createModelPanel());
		panel.add(createSubmitButtonPanel());		
		
		content.add(panel, new GridBagConstraints(0,1,0,0,1,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		
		this.getRootPane().setDefaultButton(okBtn);	
	}
	
	private Container createYPanel(){
		Vector vy = data.getYVariables();
		Vector yNames = new Vector();
		for (int i = 0; i < vy.size(); i++) {
            Variable yVar = (Variable) vy.elementAt(i);
            yNames.addElement(yVar.getName());
		}
		
		JList yList = new JList(yNames);
		yList.setVisibleRowCount(4);
		
		JPanel panel=new JPanel();
		JScrollPane listScroller = new JScrollPane(yList);
		listScroller.setPreferredSize(new Dimension(150,80));
		panel.add(listScroller);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); //createLineBorder(panel.getForeground());
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Y");
        panel.setBorder(titledBorder);
		
		return panel;
	}
	
	private Container createZPanel(){
		Vector vz = data.getZVariables();
		Vector zNames = new Vector();
		for (int i = 0; i < vz.size(); i++) {
            Variable zVar = (Variable) vz.elementAt(i);
            zNames.addElement(zVar.getName());
		}
		
		JList zList = new JList(zNames);
		zList.setVisibleRowCount(4);
		
		JPanel panel=new JPanel();
		JScrollPane listScroller = new JScrollPane(zList);
		listScroller.setPreferredSize(new Dimension(150,80));
		panel.add(listScroller);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Z");
        panel.setBorder(titledBorder);
		
		return panel;
	}
	
	private Container createXPanel(){
		Vector vx = data.getXVariables();
		xNames = new Vector();
		listModel = new DefaultListModel();
		
		for (int i = 0; i < vx.size(); i++) {
            Variable xVar = (Variable) vx.elementAt(i);
            xNames.addElement(xVar.getName());
            listModel.addElement(xVar.getName());
		}
		
		xList = new JList(listModel);
		JPanel panel=new JPanel();
		JScrollPane listScroller = new JScrollPane(xList);
		listScroller.setPreferredSize(new Dimension(300,200));
		panel.add(listScroller);
		
		return panel;
	}
	
	private Container createSubmitButtonPanel(){
		okBtn=new JButton("Go");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JPanel panel=new JPanel(new GridLayout(2,1,20,20));
		panel.add(okBtn);		
        panel.add(cancelBtn);
        
        return panel;
	}
	
	private Container createFamilyPanel(){
		Font smallFont=new Font("Arial", Font.PLAIN, 11);
		
		JLabel label1 = new JLabel("Family:",JLabel.LEFT);
		label1.setFont(smallFont);
		JLabel label2 = new JLabel("Link Function:",JLabel.LEFT);
		label2.setFont(smallFont);
		familyList = new JComboBox(familyOptions);
		familyList.setSelectedIndex(0);
		familyList.addActionListener(this);
		familyList.setActionCommand("Family");
		familyList.setFont(smallFont);
		
		linkModel = new DefaultComboBoxModel(linkOptions[0]);
		linkList = new JComboBox(linkModel);
		linkList.setFont(smallFont);
		
		JPanel panel=new JPanel(new GridLayout(4,1,20,0));
		panel.add(label1);
		panel.add(familyList);
		panel.add(label2);
		panel.add(linkList);
		
		return panel;		
	}
	
	private Container createDownButtonPanel(){
		JButton rmBtn=new JButton("Remove");
		rmBtn.addActionListener(this);
		
		JButton inBtn=new JButton("Interaction");
		inBtn.addActionListener(this);
		
		JButton nestBtn=new JButton("Nest");
		nestBtn.addActionListener(this);
		
		JButton refBtn=new JButton("Refresh");
		refBtn.addActionListener(this);
		
		JPanel panel=new JPanel(new GridLayout(4,1,20,20));
		panel.add(rmBtn);		
        panel.add(inBtn);
        panel.add(nestBtn);
        panel.add(refBtn);
        
        return panel;
	}
	
	private Container createModelPanel(){
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(createDownButtonPanel(), new GridBagConstraints(0,0,1,0,0,1,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		panel.add(createXPanel(), new GridBagConstraints(1,0,0,0,1,1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
	
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "X");
        panel.setBorder(titledBorder);
        
        return panel;
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Family")){
			int index = familyList.getSelectedIndex();
			linkModel.removeAllElements();
			for(int i=0; i<linkOptions[index].length; i++){
				linkModel.addElement(linkOptions[index][i]);
			}
		}
		else if(cmd.equals("Remove")){
			int index = xList.getSelectedIndex();
			if (index != -1){
				int[] indices = xList.getSelectedIndices();
				for(int i=indices.length - 1; i>=0; i--)
					listModel.remove(indices[i]);
			}
		}
		else if(cmd.equals("Interaction") || cmd.equals("Nest")){
			
			int[] indices = xList.getSelectedIndices();
			if (indices.length >= 2){
				String term=(String)listModel.elementAt(indices[0]);
				String op = cmd.equals("Interaction")? ":" : " %in% ";
				
				for(int i=1; i<indices.length; i++)
					term += op + (String)listModel.elementAt(indices[i]);
				
				if(listModel.contains(term)){
					int index = listModel.indexOf(term);
					xList.setSelectedIndex(index);
					xList.ensureIndexIsVisible(index);
				}else{
					listModel.addElement(term);
					int index = listModel.size();
					xList.setSelectedIndex(index-1);
					xList.ensureIndexIsVisible(index-1);
				}
			}
		}
		else if(cmd.equals("Refresh")){
			listModel.removeAllElements();
			for (int i = 0; i < xNames.size(); i++) {
				listModel.addElement((String)xNames.elementAt(i));
			}
		}
		else if(cmd.equals("Go")){
			
			int size = listModel.size();
			if (size == 0){
				JOptionPane.showMessageDialog(this, "You must define a x variable");
				return;
			}else{
				model = (String)listModel.elementAt(0);
				for (int i=1; i<size; i++)
					model += "+" + (String)listModel.elementAt(i);
				
				family = (String)familyList.getSelectedItem();
				link = (String)linkList.getSelectedItem();
				dispose();
			}
			
		}
		else{
			model = null;
			family = null;
			link = null;
			dispose();
		}
	}
	
	public String getModel(){
		return model;
	}
	
	public String getFamily(){
		return family;
	}
	
	public String getLink(){
		return link;
	}

}
