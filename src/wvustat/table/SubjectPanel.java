package wvustat.table;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.*;

import wvustat.util.MathUtils;


public class SubjectPanel extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 1L;
	
	/*
	 * constants for header type 
	 */
	public final static int DATASET=0;
	public final static int EXAMPLE=1;
	
	private JList aList, bList;
	private JTextPane descPane;
	private JLabel label1, label2, label3;
	private DefaultListModel aListModel, listModel;
	private Map subjects, descriptions;
	private String[] allitems;
	private String header;
	
	public SubjectPanel(int type) {
		if (type == 0) 
			header = "Dataset";
		else
			header = "Example";
		
		subjects = new HashMap();
		descriptions = new HashMap();
		
		aListModel = new DefaultListModel();
		aList = new JList(aListModel);
		aList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		aList.addListSelectionListener(this);
		JScrollPane aScrollPane = new JScrollPane(aList);
		
		listModel = new DefaultListModel();
		bList = new JList(listModel);
		bList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bList.addListSelectionListener(this);
		JScrollPane bScrollPane = new JScrollPane(bList);
		
		descPane = new JTextPane();
		descPane.setMargin(new Insets(2,2,2,2));
		descPane.setEditable(false);
		descPane.setContentType("text/html");
		
		setLayout(new GridLayout(1,3));
		
		JPanel p = new JPanel(new BorderLayout());
		Border border = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.lightGray);
		label1 = new JLabel("Subject", JLabel.CENTER);
		label1.setOpaque(true);
		label1.setBorder(border);
		p.add(label1, BorderLayout.PAGE_START);
		p.add(aScrollPane, BorderLayout.CENTER);
		add(p);
		
		p = new JPanel(new BorderLayout());
		label2 = new JLabel(header, JLabel.CENTER);
		label2.setOpaque(true);
		label2.setBorder(border);
		p.add(label2, BorderLayout.PAGE_START);
		p.add(bScrollPane, BorderLayout.CENTER);
		add(p);
		
		p = new JPanel(new BorderLayout());
		label3 = new JLabel("Description", JLabel.CENTER);
		label3.setOpaque(true);
		label3.setBorder(border);
		p.add(label3, BorderLayout.PAGE_START);
		p.add(new JScrollPane(descPane), BorderLayout.CENTER);
		add(p);
	}
	
	public void setContents(Map subjects, Map descriptions) {
		
		this.subjects = subjects;
		this.descriptions = descriptions;		
			
		// retrieve all dataset items in subjects
		HashSet set = new HashSet();
		Iterator it = subjects.keySet().iterator();
		while (it.hasNext()) 
			set.addAll((Set)subjects.get(it.next()));
		allitems = (String[])set.toArray(new String[0]);
		MathUtils.InsertionSort(allitems);
		
		// retrieve all subjects
		String[] sArray=(String[])subjects.keySet().toArray(new String[0]);
		MathUtils.InsertionSort(sArray);
		aListModel.removeAllElements();
		aListModel.addElement("All ("+subjects.size()+" Subjects)");
		for (int i = 0; i < sArray.length; i++)
			aListModel.addElement(sArray[i]);		
	}
	
	public JList getList() {
		return bList;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		
		JList theList = (JList)e.getSource();
		if (theList == aList) {
			if (theList.isSelectionEmpty()) {
				listModel.removeAllElements();
			} else {
				String name = (String)theList.getSelectedValue();
				listModel.removeAllElements();
				
				if (!name.equals("All ("+subjects.size()+" Subjects)")) {
					String[] sArray = (String[])((Set)subjects.get(name)).toArray(new String[0]);
					MathUtils.InsertionSort(sArray);
					for (int i = 0; i < sArray.length; i++)
						listModel.addElement(sArray[i]);
				} else {
				
					for (int i = 0; i < allitems.length; i++)
						listModel.addElement(allitems[i]);	
				}
			}
		} 
		else if (theList == bList) {
			if (theList.isSelectionEmpty()) {
				descPane.setText("");
			} else {
				String name = (String)theList.getSelectedValue();
				String desc = (String)descriptions.get(name);
				desc = (desc == null? "": desc);
				descPane.setText(desc);		
				setFontSize(theList.getFont().getSize());
			}
		}
	}
	
	public void setFontSize(int c) {
		
		Font f = label1.getFont();
		f = f.deriveFont((float)c);
		
		label1.setFont(f);
		label2.setFont(f);
		label3.setFont(f);
		aList.setFont(f);
		bList.setFont(f);
		
		StyledDocument doc = descPane.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		Style s = doc.addStyle(c+"", def);
		StyleConstants.setFontSize(s, c);
		doc.setCharacterAttributes(0, doc.getLength(), s, false);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SubjectPanel subjectPanel = new SubjectPanel(SubjectPanel.DATASET);
		subjectPanel.setPreferredSize(new Dimension(400, 200));
		frame.getContentPane().add(subjectPanel);
		
		frame.pack();
		frame.setVisible(true);
	}
}
