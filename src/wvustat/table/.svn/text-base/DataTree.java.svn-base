package wvustat.table;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import wvustat.interfaces.*;

/**
 * An internal frame showing history of datasets and their subsetting datasets.
 * 
 */
public class DataTree extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String APPNAME = "JavaStat";
	private static final int INVALID_ID = 0;
	
	private DefaultMutableTreeNode root;
	private JTree tree;
	private DefaultTreeModel treeModel;
	private transient Hashtable map = new Hashtable(); //map the id of data set to the node in tree

	public DataTree(){
		
		this.setLayout(new GridLayout(1,0));

		root = new DefaultMutableTreeNode("");
		treeModel = new DefaultTreeModel(root);
		
		try {
			load();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		tree = new JTree(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setClosedIcon(renderer.getDefaultLeafIcon());
		renderer.setOpenIcon(renderer.getDefaultLeafIcon());
		tree.setCellRenderer(renderer);
		tree.setShowsRootHandles(true);
		tree.addMouseListener(new DataTreeMouseListener());
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setMinimumSize(new Dimension(0, 0));
		this.add(scrollPane);	
		
			
	}
	
	public void buildMenu(JMenuBar menuBar) {
		//create menus
		JMenu jm = new JMenu("Tables");
		JMenuItem menuItem = new JMenuItem("Save Snapshot");
		DataTreeActionListener dlistener = new DataTreeActionListener();
		menuItem.addActionListener(dlistener);
		jm.add(menuItem);
		menuItem = new JMenuItem("Clear...");
		menuItem.addActionListener(dlistener);
		jm.add(menuItem);
		menuBar.add(jm);
	}
	
	
	public void addTreeSelectionListener(MouseListener tsl) {
		tree.addMouseListener(tsl);
	}
	
	public JTree getTree() {
		return tree;
	}
	
	/**
	 * Insert a node to be a child of root
	 * @param filename - name of source file
	 * @param table - node to be inserted
	 */
	public void addNode(DataEntryTable table) {
		NodeInfo child = new NodeInfo();
		child.setId(table.getTableModel().getDataSet().getId());
		child.setName(table.getTableModel().getName());
		child.setTable(table);
		child.setData(table.getTableModel());
		
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);	
		treeModel.insertNodeInto(childNode, root, root.getChildCount());
		map.put(new Integer(child.getId()), childNode);
		tree.scrollPathToVisible(new TreePath(childNode.getPath()));
	}
	
	/**
	 * Insert a node to be a child of the specified parent node
	 * @param pid - the id of parent node
	 * @param table - node to be inserted
	 */
	public void addNode(int pid, DataEntryTable table) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)map.get(new Integer(pid));
		
		if (parentNode != null) {
			NodeInfo child = new NodeInfo();
			child.setId(table.getTableModel().getDataSet().getId());
			child.setName(table.getTableModel().getName());
			child.setLinkedRows(table.getTableModel().getDataSet().getLinkedRows());
			child.setLinkedCols(table.getTableModel().getDataSet().getLinkedColumns());
			child.setTable(table);
			child.setData(table.getTableModel());
			
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
			treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
			map.put(new Integer(child.getId()), childNode);
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
	}
	
	public void closeNode(DataEntryTable table) {
		int id = table.getTableModel().getDataSet().getId();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)map.get(new Integer(id));
		
		if (node != null) { //node is not deleted
			NodeInfo nodeInfo = (NodeInfo)node.getUserObject();
			nodeInfo.setId(INVALID_ID);
			nodeInfo.setTable(null);
			
			DataSet ds = table.getTableModel().getDataSet();
			if (ds.getLinkedRows() != null) { //linked row and columns may have been changed by interactive data manipulation.
				nodeInfo.setLinkedRows(ds.getLinkedRows());
				nodeInfo.setLinkedCols(ds.getLinkedColumns());
			}
			map.remove(new Integer(id));
		}
	}
	
	public void openNode(DefaultMutableTreeNode node, DataEntryTable table) {
		int id = table.getTableModel().getDataSet().getId();
		NodeInfo nodeInfo = (NodeInfo)node.getUserObject();
		nodeInfo.setId(id);
		nodeInfo.setTable(table);
		nodeInfo.setData(table.getTableModel());
		map.put(new Integer(id), node);
	}
	
	
	class NodeInfo implements Serializable{
		private static final long serialVersionUID = 1L;
		protected transient int id;
		protected String name, filename;
		protected transient int[] linkedRows, linkedCols;
		protected transient DataSetTM data;
		protected transient DataEntryTable table;
		
		private void writeObject(ObjectOutputStream out) throws IOException{
			out.defaultWriteObject();
			
			out.writeInt(linkedRows.length);
			for (int i = 0; i < linkedRows.length; i++)
				out.writeInt(linkedRows[i]);
			
			out.writeInt(linkedCols.length);
			for (int i = 0; i < linkedCols.length; i++)
				out.writeInt(linkedCols[i]);
		}
		
		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
			in.defaultReadObject();
			
			int size = in.readInt();
			linkedRows = new int[size];
			for (int i = 0; i < linkedRows.length; i++)
				linkedRows[i] = in.readInt();
			
			size = in.readInt();
			linkedCols = new int[size];
			for (int i = 0; i < linkedCols.length; i++)
				linkedCols[i] = in.readInt();
		}
		
		public String toString(){
			return name;
		}
		
		public void setId(int id){
	        this.id = id;
	    }

	    public int getId(){
	        return id;
	    }
	    
	    public void setName(String name){
	    	this.name = name;
	    }
	    
	    public String getName(){
	    	return name;
	    }
	    
	    public void setFileName(String filename){
	    	this.filename = filename;
	    }
	    
	    public String getFileName(){
	    	return filename;
	    }
	    
	    public void setLinkedRows(int[] linkedRows){
	    	this.linkedRows = linkedRows;
	    }
	    
	    public int[] getLinkedRows(){
	    	return linkedRows;
	    }
	    
	    public void setLinkedCols(int[] linkedCols){
	    	this.linkedCols = linkedCols;
	    }
	    
	    public int[] getLinkedCols(){
	    	return linkedCols;
	    }
	    
	    public void setData(DataSetTM data){
	    	this.data = data;
	    }
	    
	    public DataSetTM getData(){
	    	return data;
	    }
	    
	    public void setTable(DataEntryTable table){
	    	this.table = table;
	    }
	    
	    public DataEntryTable getTable(){
	    	return table;
	    }
	}
	
	private class DataTreeMouseListener extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			
			int selRow = tree.getRowForLocation(e.getX(), e.getY());
			TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
			
			if((SwingUtilities.isRightMouseButton(e) || e.isControlDown()) ) {
				if(selRow != -1){ 
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
					JPopupMenu popup = new JPopupMenu();
					JMenuItem menuItem = new JMenuItem("Delete");
					menuItem.addActionListener(new DataTreeActionListener(node));
					popup.add(menuItem);
					popup.show(e.getComponent(), e.getX(), e.getY());
				
				} 
			}
		}
	}
	
	private class DataTreeActionListener implements ActionListener {
		private DefaultMutableTreeNode node;
		
		public DataTreeActionListener(DefaultMutableTreeNode node) {
			this.node = node;
		}
		
		public DataTreeActionListener() {
		}
		
		public void actionPerformed(ActionEvent ae){
			String arg = ae.getActionCommand();
			if (arg.equals("Delete")) {
				treeModel.removeNodeFromParent(node);
				NodeInfo nodeInfo = (NodeInfo)node.getUserObject();
				map.remove(new Integer(nodeInfo.getId()));
			} 
			else if (arg.equals("Clear...")) {
				int input = JOptionPane.showConfirmDialog(DataTree.this, "Are you sure you want to delete all trees?", "Warning", JOptionPane.YES_NO_OPTION);
				if (input == JOptionPane.YES_OPTION) {
					root.removeAllChildren();
					treeModel.reload();
					map.clear();
				}
			}
			else if (arg.equals("Save Snapshot")) {
				try {
					save();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(tree, ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void save() throws IOException, ParseException {
		String dir = getDirectory();
		BufferedWriter bw;
		Enumeration e = root.children();
		int i = 0;		
		
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			NodeInfo nodeInfo = (NodeInfo) node.getUserObject();
			if (nodeInfo.getData() == null) {
				FileReader reader = new FileReader(nodeInfo.getFileName());
	            XMLDataParser parser = new XMLDataParser();
	            nodeInfo.setData(parser.parse(reader));
			}
			
			String content = new TableModelToXML().toXML(nodeInfo.getData().getDataSet(), nodeInfo.getName());
			String filename = dir + File.separator + i + ".xml";
			bw = new BufferedWriter(new FileWriter(filename));
			bw.write(content);
			bw.close();
			nodeInfo.setFileName(filename);
			i++;
		}
		
		bw = new BufferedWriter(new FileWriter(dir + File.separator + "tree.xml"));
		bw.write(treeToXML(root));
		bw.close();
		
	}
	
	private String treeToXML(DefaultMutableTreeNode node) {
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        
        if (node.isRoot()) {
        	pw.println("<?xml version=\"1.0\"?>");
        	pw.println("<node>");
        } 
        
        for (int i = 0; i < node.getChildCount(); i++) {
        	DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(i);
        	NodeInfo nodeInfo = (NodeInfo) child.getUserObject();
        	
        	StringBuffer buffer=new StringBuffer();
        	
        	buffer.append("<node name=\"").append(nodeInfo.getName()).append("\"");
        	
        	if (nodeInfo.getFileName() != null) //top level node
        		buffer.append(" filename=\"").append(nodeInfo.getFileName()).append("\"");
        	
        	if (nodeInfo.getLinkedRows() != null) { //not top level node
        		int[] tmp = nodeInfo.getData()==null||nodeInfo.getData().getDataSet().getLinkedRows()==null? 
        					nodeInfo.getLinkedRows(): nodeInfo.getData().getDataSet().getLinkedRows();
        		buffer.append(" linkedRows=\"").append(codeValues(tmp)).append("\"");
        		tmp = nodeInfo.getData()==null||nodeInfo.getData().getDataSet().getLinkedColumns()==null? 
        			  nodeInfo.getLinkedCols(): nodeInfo.getData().getDataSet().getLinkedColumns();
        		buffer.append(" linkedCols=\"").append(codeValues(tmp)).append("\"");
        	}
        	buffer.append(">");
        	pw.println(buffer.toString());
        	
        	pw.print(treeToXML(child));
        	pw.println("</node>");
        }
        
        if (node.isRoot()) {
        	pw.println("</node>");
        }
        
        pw.flush();
        pw.close();
        return sw.toString();
	}
	
	
	private void load() throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream stream = new FileInputStream(getDirectory() + File.separator + "tree.xml");
		Document document = builder.parse(stream);
		parse(document.getDocumentElement(), root);
	}
	
	private void parse(Node node, DefaultMutableTreeNode treeNode) {
		NodeList nodeLst = node.getChildNodes();
		
		for (int i = 0; i < nodeLst.getLength(); i++) 
		{
			Node child = nodeLst.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				
				NodeInfo nodeInfo = new NodeInfo();
				NamedNodeMap attlist = child.getAttributes();
				
				if (attlist != null)
				{
					Node att = attlist.getNamedItem("name");
					if (att != null)
						nodeInfo.setName(att.getNodeValue());
					att = attlist.getNamedItem("filename");
					if (att != null)
						nodeInfo.setFileName(att.getNodeValue());
					att = attlist.getNamedItem("linkedRows");
					if (att != null)
						nodeInfo.setLinkedRows(decodeValues(att.getNodeValue()));
					att = attlist.getNamedItem("linkedCols");
					if (att != null)
						nodeInfo.setLinkedCols(decodeValues(att.getNodeValue()));
				}
				
				DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(nodeInfo);
				treeModel.insertNodeInto(childTreeNode, treeNode, treeNode.getChildCount());
				parse(child, childTreeNode);
			}
		}
	}
	
	private String getDirectory() throws IOException {
		String home =  System.getProperty("user.home");
		if (home == null)
			throw new IOException("Cannot locate user home.");
		
		File dir = new File(home + File.separator + APPNAME);
		if (dir.exists()) {
		    if (!dir.isDirectory()) 
		    	throw new IOException("Cannot create folder in user home.");
		} else {
			if (!dir.mkdir())
				throw new IOException("Cannot create folder in user home.");
		}
		
		return dir.getPath();
	}
	
	private String codeValues(int[] a) {
		if (a == null) return null;
		
		String s = "";
		for (int i = 0; i < a.length; i++)
			s += a[i] + ",";
		
		if (a.length > 0)
			s = s.substring(0,s.length()-1);
		
		return s;		
	}
	
	private int[] decodeValues(String values){
		StringTokenizer tokenizer=new StringTokenizer(values, ",");
		int[] a = new int[tokenizer.countTokens()];
    	int i = 0;
		while(tokenizer.hasMoreTokens())
    	{
    		String value=tokenizer.nextToken().trim();
    		a[i] = Integer.parseInt(value);
    		i++;
    	}
    	return a;
	}
	
}
