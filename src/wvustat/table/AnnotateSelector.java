package wvustat.table;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import java.util.Vector;
import java.util.ArrayList;

import wvustat.interfaces.*;

public class AnnotateSelector extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	
	private int option = CANCEL_OPTION;
	
	private DataSet data;
	private DefaultListModel listModel;
	private JList columnNameList;
	private JList typeList;
	
	private String type;
	private ArrayList ids;
	
	private final String[] typeOptions = {"AFFYMETRIX_3PRIME_IVT_ID", 
			"AFFYMETRIX_EXON_GENE_ID",
			"AFFYMETRIX_SNP_ID",
			"AGILENT_CHIP_ID",
			"AGILENT_ID",
			"AGILENT_OLIGO_ID",
			"ENSEMBL_GENE_ID",
			"ENSEMBL_TRANSCRIPT_ID",
			"ENTREZ_GENE_ID",
			"FLYBASE_GENE_ID",
			"FLYBASE_TRANSCRIPT_ID",
			"GENBANK_ACCESSION",
			"GENPEPT_ACCESSION",
			"GENOMIC_GI_ACCESSION",
			"PROTEIN_GI_ACCESSION",
			"ILLUMINA_ID",
			"IPI_ID",
			"MGI_ID",
			"GENE_SYMBOL",
			"PFAM_ID",
			"PIR_ACCESSION",
			"PIR_ID",
			"PIR_NREF_ID",
			"REFSEQ_GENOMIC",
			"REFSEQ_MRNA",
			"REFSEQ_PROTEIN",
			"REFSEQ_RNA",
			"RGD_ID",
			"SGD_ID",
			"TAIR_ID",
			"UCSC_GENE_ID",
			"UNIGENE",
			"UNIPROT_ACCESSION",
			"UNIPROT_ID",
			"UNIREF100_ID",
			"WORMBASE_GENE_ID",
			"WORMPEP_ID",
			"ZFIN_ID"};
	
	public AnnotateSelector(Frame parent, DataSet data) {
		super(parent, true);
		this.data = data;
		
		listModel = new DefaultListModel();
		columnNameList = new JList(listModel);
		
		Vector v = data.getVariables();
		for (int i = 0; i < v.size(); i++) {
			Variable var = (Variable) v.elementAt(i);
			listModel.addElement(var.getName());
		}
		
		initComponents();
	}
	
	private void initComponents(){
		
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		
		content.add(createColumnNamePanel(), new GridBagConstraints(0,0,1,2,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createTypePanel(), new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		content.add(createButtonPanel(), new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
		
	}
	
	private Container createColumnNamePanel(){
		
		JPanel panel=new JPanel();
		
		columnNameList.setVisibleRowCount(-1);
		columnNameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane listScroller = new JScrollPane(columnNameList);
		listScroller.setPreferredSize(new Dimension(150,300));
		panel.add(listScroller);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Select the ID column:");
        panel.setBorder(titledBorder);
		
		return panel;
	}
	
	private Container createTypePanel() {
		JPanel panel=new JPanel();
		
		typeList = new JList(typeOptions);
		typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listScroller = new JScrollPane(typeList);
		listScroller.setPreferredSize(new Dimension(250,250));
		panel.add(listScroller);
		
		Border lineBorder=BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border titledBorder=BorderFactory.createTitledBorder(lineBorder, "Select type of ID:");
        panel.setBorder(titledBorder);
		
		return panel;
	}
	
	private Container createButtonPanel(){
		JButton okBtn=new JButton("Ok");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JPanel panel=new JPanel(new GridLayout(1,2,20,20));
		panel.add(okBtn);		
        panel.add(cancelBtn);
        
        return panel;
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Cancel")){
			option = CANCEL_OPTION;
			dispose();
		} else if(cmd.equals("Ok")){
			
			if (typeList.getSelectedIndex() == -1)
			{
				JOptionPane.showMessageDialog(this, "You must select a type!");
				return;
			}
		
			if (columnNameList.getSelectedIndex() == -1)
			{
				JOptionPane.showMessageDialog(this, "You must select an ID column!");
				return;
			}
			
			type = typeOptions[typeList.getSelectedIndex()];
			
			ids = new ArrayList();
			Vector v = data.getVariables();
			Variable var = (Variable) v.elementAt(columnNameList.getSelectedIndex());
			
			for (int i = 0; i < var.getSize(); i++) {
				Object value = var.getValue(i);
				if (value.equals(Variable.NUM_MISSING_VAL))
					ids.add(Variable.CAT_MISSING_VAL);
				else if (value instanceof Double) {
					Double num = (Double)value;
					if (num.doubleValue() == num.intValue())
						ids.add(String.valueOf(num.intValue()));
					else
						ids.add(String.valueOf(num.doubleValue()));
				}
				else
					ids.add(value.toString());
			}
			
			option = OK_OPTION;
			dispose();
		}
	}

	public String getType() {
		return type;
	}
	
	public ArrayList getIDs() {
		return ids;
	}
	
	public int getOption()
    {
        return option;
    }
}
