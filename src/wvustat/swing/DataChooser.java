package wvustat.swing;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import java.util.Vector;

import wvustat.util.MathUtils;
import wvustat.table.SimpleHTMLParser;
import wvustat.table.AnchorTag;

public class DataChooser extends JDialog implements ActionListener{
	private JList dataList;
	private String dataUrl;
	private String selectedItem;
	private JButton okBtn;
	
	public DataChooser(Frame parent, String dataUrl)  throws MalformedURLException, IOException
    {
		super(parent, true);
		this.dataUrl=dataUrl;
		initComponents();
	}

    public DataChooser(Dialog dialog, String dataUrl) throws MalformedURLException, IOException
    {
        super(dialog, true);
        this.dataUrl=dataUrl;
        initComponents();
    }

	private void initComponents() throws MalformedURLException, IOException
    {
		Vector dataTags=new Vector();

			URL url=new URL(dataUrl);
			InputStreamReader isr=new InputStreamReader(url.openStream());
			BufferedReader bufReader=new BufferedReader(isr);
			
			StringBuffer buf=new StringBuffer();
			String line=null;
			while((line=bufReader.readLine())!=null){
				buf.append(line);
				
			}
			
			SimpleHTMLParser parser=new SimpleHTMLParser(buf.toString());
			Vector tags=parser.getTags();
			
			
			for(int i=0;i<tags.size();i++){
				AnchorTag aTag=(AnchorTag)tags.elementAt(i);
				if(aTag.getAnchorText().endsWith("xml")){
					dataTags.addElement(aTag.getAnchorText());
				}
			}

		
		String[] sArray=new String[dataTags.size()];
		dataTags.copyInto(sArray);
		MathUtils.InsertionSort(sArray);
		
		dataList=new JList(sArray);
		
		dataList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dataList.addMouseListener(new ClickListener());
		JLabel label1=new JLabel("Datasets in "+dataUrl);
		
		Container content=this.getContentPane();
		content.setLayout(new GridBagLayout());
		content.add(label1, new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
		content.add(new JScrollPane(dataList), new GridBagConstraints(0,1,0,-1,1,1,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
		okBtn=new JButton("Open");
		okBtn.addActionListener(this);
		
		JButton cancelBtn=new JButton("Cancel");
		cancelBtn.addActionListener(this);
		
		JPanel panel4=new JPanel(new GridLayout(1,2,60,20));
		
        
        panel4.add(cancelBtn);
        panel4.add(okBtn);		
        
        content.add(panel4, new GridBagConstraints(0,2,0,0,1.0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,20,2,20), 0, 0));
			
		this.getRootPane().setDefaultButton(okBtn);	
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Open")){
			ListSelectionModel model=dataList.getSelectionModel();
			int index=model.getLeadSelectionIndex();
			
			if(index!=-1){
				ListModel dModel=dataList.getModel();
				selectedItem=dModel.getElementAt(index).toString();
				this.dispose();
			}
			
			
		}
		else 
			dispose();
	}
	

	public String getSelectedItem(){
		return selectedItem;
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
				DataChooser.this.dispose();
			}			
		}
	}
}