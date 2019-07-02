package wvustat.modules.genome;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.rmi.*;

import javax.swing.*;

import java.util.*;

import wvustat.modules.*;
import wvustat.modules.diagram.*;
import wvustat.network.client.JRIClient;
import wvustat.util.ComponentUtil;
import wvustat.util.RawFileUploader;

import org.gui.JDirectoryDialog;

public class DiagramModule extends GUIModule implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private DiagramDrawer canvas;
	private Diagram diagram;
	private JMenuBar jmb;
	private JRadioButtonMenuItem vplotMI, hplotMI;
	
	private JPopupMenu popup;
	private JMenuItem bgMI, normMI, sumMI, gcrmaMI, diffMI, delMI, aromaRmaMI, aromaGcRmaMI, downloadMI;
	private String selectedObjName;
	
	private JDirectoryDialog directoryDialog;
	
	
	public DiagramModule()
	{
		setBackground(Color.white);
		init();
	}
	
	private void init()
    {
		setLayout(new BorderLayout());
		canvas = new DiagramDrawer(null);
		
		MouseHandler handler = new MouseHandler();
		canvas.addMouseListener(handler);
		canvas.addMouseMotionListener(handler);
		
		JScrollPane sp = new JScrollPane(canvas/*, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS*/);
		
		add(sp, BorderLayout.CENTER);
		
		jmb = new JMenuBar();
        JMenu plotMenu = new JMenu("Plot");
        
        ButtonGroup group=new ButtonGroup();
        
        vplotMI = new JRadioButtonMenuItem("VERTICAL");
        vplotMI.addActionListener(this);
        group.add(vplotMI);
        group.setSelected(vplotMI.getModel(), true);
        plotMenu.add(vplotMI);
        
        hplotMI = new JRadioButtonMenuItem("HORIZONTAL");
        hplotMI.addActionListener(this);
        group.add(hplotMI);
        plotMenu.add(hplotMI);
        
        jmb.add(plotMenu);
        
        retrieveData();
        canvas.setDiagram(diagram);		
        
        // create contextual menu
        popup = new JPopupMenu();
        bgMI = new JMenuItem("Background adjustment...");
        bgMI.addActionListener(this);
        normMI = new JMenuItem("Normalization...");
        normMI.addActionListener(this);
        sumMI = new JMenuItem("Summarization...");
        sumMI.addActionListener(this);
        gcrmaMI = new JMenuItem("GCRMA");
        gcrmaMI.addActionListener(this);
        
        aromaRmaMI = new JMenuItem("RMA");
        aromaRmaMI.setActionCommand("AROMA_RMA");
        aromaRmaMI.addActionListener(this);
        aromaGcRmaMI = new JMenuItem("GCRMA");
        aromaGcRmaMI.setActionCommand("AROMA_GCRMA");
        aromaGcRmaMI.addActionListener(this);
        
        diffMI = new JMenuItem("Differential Gene Expression...");
        diffMI.addActionListener(this);
        delMI = new JMenuItem("Delete...");
        delMI.addActionListener(this);
        downloadMI = new JMenuItem("Download CEL files...");
        downloadMI.addActionListener(this);
    }
	
	private void retrieveData()
	{
		try{
			HashMap nodeMap = new HashMap(); //<String, DiagramNode>
			
			String[] sArray = JRIClient.listObjects();
			String[] names = new String[sArray.length];
			
			for (int i = 0; i < names.length; i++)
				names[i] = sArray[i].substring(0, sArray[i].indexOf("("));
			
			diagram = new Diagram();
			
			for (int i = 0; i < sArray.length; i++) {
				DiagramNode node = new DiagramNode(diagram.nextId(), getShortName(names[i]));
				node.setLabel(sArray[i]);
				diagram.addNode(node);
				nodeMap.put(names[i], node);
			}
			
			for (int i = 0; i < sArray.length; i++) {
				String child = names[i];
				
				for (int j = i - 1; j >= 0; j--) {
					String s = names[j];
					if (isParent(s, child)) {
						diagram.connect(((DiagramNode) nodeMap.get(s)).getId(), ((DiagramNode) nodeMap.get(child)).getId());
						break;
					}
				}
			}

		} catch (RemoteException rex) {
			diagram = null;
		}
	}
	
	private boolean isParent(String parent, String child)
	{
		if (child.startsWith(parent)){
			StringTokenizer stc = new StringTokenizer(child, ".");
			StringTokenizer stp = new StringTokenizer(parent, ".");
			
			if (stc.countTokens() == stp.countTokens() + 1 || (stc.countTokens() == 5 && stp.countTokens() == 3))
				return true;
		}
		
		return false;
	}
	
	private String getShortName(String name)
	{		
		StringTokenizer st = new StringTokenizer(name, ".");
		String[] tokens = new String[st.countTokens()];		
		
		int i = 0;
		while (st.hasMoreTokens())
			tokens[i++] = st.nextToken();
		
		if (tokens.length == 5) 
			return tokens[3] + "." + tokens[4];
		else
			if (tokens.length > 0)
			return tokens[tokens.length - 1];
		else 
			return "";
	}
	
	
	/**
     * Get the menu bar contained in this object
     */
    public JMenuBar getJMenuBar()
    {
        return jmb;
    }
    
    public JMenu getOptionMenu()
    {
    	return null;
    }
    
    public void setMenuForNode(String label)
    {
    	String name = label.substring(0, label.indexOf("("));
    	String objClass = label.substring(label.indexOf("(") + 1, label.indexOf(")")); 
    	
    	popup.removeAll();
    	
    	if (objClass.equals("AffyBatch")) {
    		
    		StringTokenizer st = new StringTokenizer(name, ".");
    		switch (st.countTokens()) {
        		case 1: 
        			popup.add(bgMI);
        			popup.add(gcrmaMI);
        			break;
        		case 2:
        			popup.add(normMI);
        			break;
        		case 3:
        			popup.add(sumMI);
        			break;
    		}
    		
    	} else if (objClass.equals("exprSet") || objClass.equals("ExpressionSet") || objClass.equals("data.frame")) {
    		popup.add(diffMI);
    	} else if (objClass.equals("AffymetrixCelSet")) {
    		if (name.indexOf(".") == -1) {
    			popup.add(aromaRmaMI);
    			popup.add(aromaGcRmaMI);
    		} else
    			popup.add(downloadMI);
    	}
    	
    	if (popup.getComponentCount() > 0) popup.addSeparator();
    	popup.add(delMI);
    	
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        String arg = ae.getActionCommand();
        
        if (arg.equals("VERTICAL")) {
        	canvas.setDirection(DiagramDrawer.VERTICAL);
        	canvas.updateDrawingInfo();
        	
        } else if (arg.equals("HORIZONTAL")) {
        	canvas.setDirection(DiagramDrawer.HORIZONTAL);
        	canvas.updateDrawingInfo();     
        	
        } else if (arg.equals("Background adjustment...") || 
        		arg.equals("Normalization...") ||
        		arg.equals("Summarization...")) {
        	
        	int type;
        	
        	if (arg.equals("Background adjustment..."))
        		type = PreprocessDialog.BGCORRECT;
        	else if (arg.equals("Normalization..."))
        		type = PreprocessDialog.NORMALIZE;
        	else 
        		type = PreprocessDialog.SUMMARY;
        		
        	PreprocessDialog pd = new PreprocessDialog(ComponentUtil.getTopComponent(this), type);
        	pd.setLocationRelativeTo(this);
        	pd.pack();
        	pd.show();
        	if (pd.getOption() == PreprocessDialog.OK_OPTION)
        	{
        		try
            	{
            		JRIClient.preprocess(getMainPanel(), selectedObjName, pd.getBgcorrectMethod(),
            							pd.getNormalizeMethod(), pd.getPmcorrectMethod(), pd.getSummaryMethod());
            	}
            	catch(RemoteException rex){
            		ComponentUtil.showErrorMessage(this, rex.getMessage());
            	}
        	}
        	
        } else if (arg.equals("GCRMA")) {
        	try {
        		JRIClient.gcrma(getMainPanel(), selectedObjName);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        } else if (arg.equals("AROMA_RMA")) {
        	try {
        		JRIClient.exonRma(getMainPanel(), selectedObjName);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        	
        } else if (arg.equals("AROMA_GCRMA")) {
        	try {
        		JRIClient.exonGcRma(getMainPanel(), selectedObjName);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        	
        } else if (arg.equals("Differential Gene Expression...")) {
        	
        	JOptionPane.showMessageDialog(this, "Please load the object first, then use the 'Analyze' menu in the object window!");        	      	
        	
        } else if (arg.equals("Delete...")) {
        	int option = JOptionPane.showOptionDialog(this, "Are you sure you want to delete selected object and its offspring?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option == JOptionPane.YES_OPTION)
            {
            	try{
            		JRIClient.deleteObjects(getMainPanel(), new String[] {selectedObjName});
            	}
            	catch(RemoteException rex){
            		ComponentUtil.showErrorMessage(this, rex.getMessage());
            	}
            }
        } else if (arg.equals("Download CEL files...")) {
        	try {
        		final java.util.List remoteFiles = JRIClient.listFilesOfObject(selectedObjName);
        		
        		if (directoryDialog == null)
    			{
        			directoryDialog = new JDirectoryDialog((JFrame)ComponentUtil.getTopComponent(getMainPanel()));
        			directoryDialog.setMessage("Select a directory:");
    			}
        		if (directoryDialog.showDirectoryDialog() && directoryDialog.getSelectedFolder() != null)
    			{
    				final File savePath = directoryDialog.getSelectedFolder();
    				final RawFileUploader uploader = new RawFileUploader();
    				
    				Runnable runnable = new Runnable()
        			{
        	            public void run()
        	            {
        			
        	            	ProgressMonitor progressMonitor = new ProgressMonitor(getMainPanel(),
        	            										"Downloading...", "", 0, remoteFiles.size());
        	            	progressMonitor.setMillisToDecideToPopup(0);
        	            	progressMonitor.setMillisToPopup(0);
        	            	progressMonitor.setProgress(0);
        	            	
        	            	try {
        	            		for (int i = 0; i < remoteFiles.size(); i++){
        	            			if (progressMonitor.isCanceled()) 
        	            				throw new Exception("user canceled");
        					
        	            			String filepath = (String)remoteFiles.get(i);
        	            			String filename = filepath.substring(filepath.lastIndexOf('/')+1);
        	            			String savepath = savePath.getPath() + File.separator + filename;
        	            			progressMonitor.setNote(filename);
        	            			
        	            			uploader.download(filepath, savepath);
        	            			
        	            			progressMonitor.setProgress(i + 1);
        	            		}
        	            		progressMonitor.close();
        	            	
        	            	} catch (Exception ex) {
        	            		ex.printStackTrace();
        	            		progressMonitor.close();
        	            		ComponentUtil.showErrorMessage(getMainPanel(), ex.getMessage(), "Download failed");
        	            		return;
        	            	}	
        	            	
        	            	JOptionPane.showMessageDialog(getMainPanel(), "Download " + remoteFiles.size() + " files successfully!");
        	            }
        			};
    				
        			Thread thread = new Thread(runnable);
        	        thread.start();    				
    			}
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        
    }

    public void update(String arg)
    {
    	retrieveData();
    	canvas.setDiagram(diagram);		
    }
    
    class MouseHandler extends MouseAdapter implements MouseMotionListener {
    	private int first_x, first_y, last_x, last_y;
    	private boolean mouseDragging = false;
    	private Rectangle selectionRange = new Rectangle(0,0,0,0);
    	
    	public void mousePressed(MouseEvent e) {
    		int x = e.getX(), y = e.getY();
    		first_x = x;
            first_y = y;
            mouseDragging = true;
            canvas.setMouseDragging(mouseDragging);
            
            boolean multipleSelection = false;

            if (e.getModifiers() == 17) { //Shift Key on Mac
                multipleSelection = true;
            }
            
            boolean found = false;
            Iterator it = diagram.nodeIterator();
            while(it.hasNext() && !found){
    			DiagramNode node = (DiagramNode)it.next();
    			found = node.getBounds().contains(x, y);
    			if (found) {
    				
    				if ( SwingUtilities.isRightMouseButton(e) || e.isControlDown() ) {
    					diagram.clearSelection();
    					node.setSelected(true);
    					selectedObjName = node.getLabel().substring(0, node.getLabel().indexOf("("));
    					
    					// Reconstruct popup menu to close any open popup and show a new popup menu. 
    					// By default, a mouse click outside of the popup is considered as to close existing popup.	
    					popup = new JPopupMenu();    
    								
    					setMenuForNode(node.getLabel());
    					popup.show(e.getComponent(), e.getX(), node.getTop() + node.getHeight());
    					return;
    				}
    				
    				if (e.getClickCount() == 2) {
    					try{
    						JRIClient.getObjectSummary(DiagramModule.this.mainPanel, node.getLabel().substring(0, node.getLabel().indexOf("(")));
    					}
    					catch (Exception ex){
    						ComponentUtil.showErrorMessage(DiagramModule.this, ex.getMessage());
    					}
    					return;
    				}
    				
    				if (!multipleSelection) {
    					diagram.clearSelection();
    				}
    				boolean bl = node.isSelected();
    				node.setSelected(!bl);
    			}
            }
            if(!found)
            	diagram.clearSelection();            
    	}
    	
    	public void mouseClicked(MouseEvent e) {
    	}
    	
    	public void mouseEntered(MouseEvent e) {
        }
    	
    	public void mouseReleased(MouseEvent e) {
    		boolean found = false;
    		Iterator it = diagram.nodeIterator();
            while(it.hasNext()){
            	DiagramNode node = (DiagramNode)it.next();
            	found = selectionRange.intersects(node.getBounds());
            	if(found)
            		node.setSelected(true);
            }
            mouseDragging = false;
            selectionRange.setRect(0, 0, 0, 0);
            canvas.setMouseDragging(mouseDragging);
            canvas.setSelectionRange(selectionRange);
            canvas.repaint();
    	}
    	
    	public void mouseExited(MouseEvent e) {
        }
    	
    	public void mouseDragged(MouseEvent e) {
    		last_x = e.getX();
            last_y = e.getY();
            selectionRange.setRect(first_x, first_y, last_x - first_x, last_y - first_y);
            canvas.setSelectionRange(selectionRange);
            canvas.repaint();
    	}
    	
    	public void mouseMoved(MouseEvent e) {
        }
    }
}
