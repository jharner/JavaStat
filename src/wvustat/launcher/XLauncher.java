package wvustat.launcher;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.EmptyBorder;

import java.util.*;
import java.io.*;

import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import wvustat.interfaces.LinkPolicy;
import wvustat.swing.ImageIcons;
import wvustat.swing.LauncherPlugin;
import wvustat.contentviewer.BrowserLaunch;
import wvustat.swing.LifeStatsPlugin;


/**
 * @author Jun Tan
 *
 * This is the main class of myJavaStat. It loads table of contents from
 * either defaultContent.xml or the server. It also creats and displays 
 * modules.
 * There are two configuration files:
 * 1. conf.xml specifies:
 *    server location, module name to class map, module attributes.
 *    In order for myJavaStat running correctly, this file is essential.
 * 
 * 2. defaultContent.xml specifies the default table of contents. 
 */
public class XLauncher extends Observable implements TreeSelectionListener{
	public final static String version = "0.7";
	private static boolean isApple;

	private Map paramMap = new HashMap();
	private Map moduleMap = new HashMap();
	
	private JFrame mainWindow;
	private JScrollPane treeContainer;
	private JPanel modulePane;
	private JLabel statusBar;
	
	/* the next one line added by djluo 5/20/2005 */
	private JMenuBar menuBar=new JMenuBar();
	private TreePath selectedPath; // old path
	private Object dataModule; // current loaded data module
	private Object lifeStatsModule; // current loaded data module or example module
	
	public XLauncher(){
		//Load configuration
		try{
			SAXParserFactory spFactory = SAXParserFactory.newInstance();
			spFactory.setValidating(true);
			spFactory.setNamespaceAware(true);
			SAXParser parser = spFactory.newSAXParser();
			
			InputStream config = getClass().getResourceAsStream("config.xml");
			parser.parse(new InputSource(config), new ConfigReader());
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "Connot load configurations",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		mainWindow = new JFrame("LifeStats " + version);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(1024, 768); //900x680
		
		/* the next one line added by djluo 5/20/2005 */
		mainWindow.setJMenuBar(menuBar);
		
		buildGUI();
		mainWindow.setVisible(true);
	}
	
	public void valueChanged(TreeSelectionEvent event){
		TreePath path = event.getNewLeadSelectionPath();
		if(path == null)
			return;
		
		if(path.equals(selectedPath))
			return;
		else
			selectedPath = path;
		
		DefaultMutableTreeNode node  =
			(DefaultMutableTreeNode)path.getLastPathComponent();
		if(node.getUserObject() instanceof NodeContent){
			NodeContent nc = (NodeContent)node.getUserObject();
			ModuleInfo moduleInfo = 
				(ModuleInfo)moduleMap.get(nc.getToolName());
			
			try {
				if (moduleInfo == null || moduleInfo.getImplClass() == BrowserLaunch.class) {
					JTree tree =  (JTree) event.getSource();
					selectedPath = event.getOldLeadSelectionPath();
					tree.setSelectionPath(event.getOldLeadSelectionPath());
					return;
				}
			} catch (ClassNotFoundException ex) {
			}
			
			ModuleLoader loader = new ModuleLoader(moduleInfo, 
					nc.getParameters());
			SwingUtilities.invokeLater(loader);
		}
	}
	
	private void buildGUI(){
		JPanel leftPanel = new JPanel(new BorderLayout());
		
		treeContainer = 
		    new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		treeContainer.setMinimumSize(new Dimension(100, 100));
		treeContainer.setBorder(new EmptyBorder(2, 5, 2, 5));
			
		JViewport viewport = treeContainer.getViewport();
		viewport.setView(loadContent());
		leftPanel.add(treeContainer, BorderLayout.CENTER);
		
		if(paramMap.get("serverURL") != null){
			//Add the connect button
		}
		
		modulePane = new JPanel(new BorderLayout());
		modulePane.setMinimumSize(new Dimension(200, 200));
		
		JSplitPane splitPane =
			new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, modulePane);
		splitPane.setOneTouchExpandable(true);
		mainWindow.getContentPane().add(splitPane, BorderLayout.CENTER);
		
	
		statusBar = new JLabel();	
		statusBar.setFont(new Font(null, Font.PLAIN, 10));
		statusBar.setBorder(new EmptyBorder(2, 20, 2, 2));
		if(paramMap.get("serverURL") != null){
		    statusBar.setText("IDEAL Login...");
		    statusBar.addMouseListener(new MouseAdapter(){
		    	public void mouseClicked(MouseEvent e)
		    	{
		    		if (!paramMap.containsKey("sessionId")){
		    			Map loginMap = LoginDialog.showDialog(mainWindow);
		    			if (loginMap != null){
		    
		    				//do connection
		    				PrintWriter out = null;
		    				BufferedReader in = null;
		    				try{
		    					URL url = new URL((String)paramMap.get("serverURL")); 
		    					URLConnection conn = url.openConnection();
		    					conn.setDoInput(true);
		    					conn.setDoOutput(true);
		    					out = new PrintWriter(conn.getOutputStream());
		    					out.println((String)loginMap.get("userId"));
		    					out.println((String)loginMap.get("password"));
		    					out.flush();
		    					in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    					long reply = Long.parseLong(in.readLine());
		    					
		    					if (reply >= 0){
		    						statusBar.setText("Connected");
		    						paramMap.put("sessionId", new Long(reply));
		    					}else{
		    						statusBar.setText("IDEAL Login...");
		    						paramMap.remove("sessionId");
		    						JOptionPane.showMessageDialog(mainWindow, "Error User ID or Password", "Error", JOptionPane.ERROR_MESSAGE);
		    					}
		    		
		    				}catch(Exception exp){
		    					JOptionPane.showMessageDialog(mainWindow, "Fail to connect to server: " + exp, "Error", JOptionPane.ERROR_MESSAGE);
		    					exp.printStackTrace();
		    				}finally {
		    					try {
		    						if (in != null) in.close();
		    						if (out != null) out.close();
		    					} catch (Exception ex) {}
		    				}
		    			}
		    		} else {
		    			int option = JOptionPane.showOptionDialog(mainWindow, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                        if (option != JOptionPane.YES_OPTION)
                            return;
                        statusBar.setText("IDEAL Login...");
                        paramMap.remove("sessionId");
		    		}
		    	}
		    });
		}
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(statusBar, BorderLayout.CENTER);
		
		/* Create icons for linking mode in the right bottom of the window. */
		JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
		
		final JLabel decFontIcon = new JLabel(ImageIcons.DecreaseFont);
		decFontIcon.setToolTipText("Decrease font size");
		iconPanel.add(decFontIcon);		
		
		final JLabel incFontIcon = new JLabel(ImageIcons.IncreaseFont);
		incFontIcon.setToolTipText("Increase font size");
		iconPanel.add(incFontIcon);
		
		incFontIcon.addMouseListener(new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		JTree comp = (JTree)treeContainer.getViewport().getView();
	    		Font f = comp.getFont(); 
	    		f = f.deriveFont(f.getSize2D() + 1);
	    		comp.setFont(f);
	    		comp.setRowHeight(comp.getRowHeight() + 1);
	    		
	    		if (lifeStatsModule != null)
	    			((LifeStatsPlugin) lifeStatsModule).setFontSize(f.getSize());
	    	}
		});
		
		decFontIcon.addMouseListener(new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		JTree comp = (JTree)treeContainer.getViewport().getView();
	    		Font f = comp.getFont(); 
	    		if (f.getSize() <= 10) return;
	    		f = f.deriveFont(f.getSize2D() - 1);
	    		comp.setFont(f);
	    		comp.setRowHeight(comp.getRowHeight() - 1);
	    		
	    		if (lifeStatsModule != null)
	    			((LifeStatsPlugin) lifeStatsModule).setFontSize(f.getSize());
	    	}
		});
		
		JLabel blankIcon = new JLabel(ImageIcons.BLANK);
		iconPanel.add(blankIcon);
		
		final JLabel arrowIcon = new JLabel(ImageIcons.ArrowGrey);
		arrowIcon.setToolTipText("Selecting");
		iconPanel.add(arrowIcon);
		
		final JLabel brushIcon = new JLabel(ImageIcons.Brush);
		brushIcon.setToolTipText("Brushing");
		iconPanel.add(brushIcon);
		
		final JLabel paintIcon = new JLabel(ImageIcons.Paint);
		paintIcon.setToolTipText("Painting");
		iconPanel.add(paintIcon);
		
		MouseListener iconHandler = new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		Object obj = e.getSource();
	    		if (obj == arrowIcon) {
	    			LinkPolicy.mode = LinkPolicy.SELECTING;
	    			arrowIcon.setIcon(ImageIcons.ArrowGrey);
	    			brushIcon.setIcon(ImageIcons.Brush);
	    			paintIcon.setIcon(ImageIcons.Paint);
	    		}
	    		else if (obj == brushIcon) {
	    			LinkPolicy.mode = LinkPolicy.BRUSHING;
	    			arrowIcon.setIcon(ImageIcons.Arrow);
	    			brushIcon.setIcon(ImageIcons.BrushGrey);
	    			paintIcon.setIcon(ImageIcons.Paint);
	    		}
	    		else if (obj == paintIcon) {
	    			LinkPolicy.mode = LinkPolicy.PAINTING;
	    			arrowIcon.setIcon(ImageIcons.Arrow);
	    			brushIcon.setIcon(ImageIcons.Brush);
	    			paintIcon.setIcon(ImageIcons.PaintGrey);
	    		}
	    	}
		};
		arrowIcon.addMouseListener(iconHandler);
		brushIcon.addMouseListener(iconHandler);
		paintIcon.addMouseListener(iconHandler);
		
		blankIcon = new JLabel(ImageIcons.BLANK);
		iconPanel.add(blankIcon);
		
		JLabel arrangeIcon = new JLabel(ImageIcons.ARRANGE_ALL);
		arrangeIcon.setToolTipText("Arrange Windows   ");
		arrangeIcon.setBorder(new EmptyBorder(0, 0, 0, 30));
		arrangeIcon.addMouseListener(new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		if (dataModule != null) {
	    			((LauncherPlugin) dataModule).arrangeAllWindows();
	    		}
	    	}
		});
		iconPanel.add(arrangeIcon);
		bottomPanel.add(iconPanel, BorderLayout.EAST);
		

		mainWindow.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private JComponent loadContent(){
		InputStream dataIn = null;
		JComponent content = null;
		
		try{
		    Integer sid;
		    if((sid = (Integer)paramMap.get("N/A"))!= null){ //this key does not exist. always execute else statement.
		    		try{
		    			String s = (String)paramMap.get("serverURL") + 
						"/servlet/jstatserver.Router";
		    			/*
		    			 ServerEntrance tranport = new ServerEntrance(new URL(s));
		    			 JStatMessage request = 
		    			 new JStatMessage(JStatMessage.NORMAL, "contents", sid);
		    			 JStatMessage reply = tranport.sendRequest(request);
		    			 if(reply.getStatus() == JStatMessage.ERROR)
		    			 throw new Exception(reply.getData().toString());

			    		 String contents = reply.getData().toString();
			    		 dataIn = new ByteArrayInputStream(contents.getBytes()); */
		    		}catch(Exception e){
		    			System.out.println(e.toString());
		    			JOptionPane.showMessageDialog(mainWindow, 
		    					"Cannot load content", "Error", 
							JOptionPane.ERROR_MESSAGE);
		    		}
		    }
		    else{
		    		Class c = getClass();
		    		dataIn = c.getResourceAsStream("DefaultContent.xml");
		    }
		    
		    if(dataIn == null){ //not executed. always go to else statement
		    		DefaultMutableTreeNode root = 
		    			new DefaultMutableTreeNode("myJavaStat modules");
			
		    		Iterator it = moduleMap.values().iterator();
		    		while(it.hasNext()){
		    			ModuleInfo moduleInfo = (ModuleInfo)it.next();
		    			if(!"StatTool".equals(moduleInfo.getType()))
		    				continue;
			    
		    			NodeContent nodeContent = new NodeContent();
		    			nodeContent.setTitle(moduleInfo.getName());
		    			nodeContent.setToolName(moduleInfo.getName());
		    			DefaultMutableTreeNode node = 
		    				new DefaultMutableTreeNode(nodeContent);
		    			root.add(node);
		    		}
		    		
		    		content = new JTree(root);
		    		((JTree)content).addTreeSelectionListener(this);
		    }
		    else{
		    		SAXParserFactory spFactory = SAXParserFactory.newInstance();
		    		spFactory.setValidating(true);
		    		spFactory.setNamespaceAware(true);
		    		SAXParser parser = spFactory.newSAXParser();
			
		    		ContentTreeBuilder treeBuilder = new ContentTreeBuilder();
		    		parser.parse(new InputSource(dataIn), treeBuilder);
		    		content = treeBuilder.getTree();
		    		((JTree)content).addTreeSelectionListener(this);
		    		((JTree)content).addMouseListener(new TreeMouseListener());
		    }
		}catch(Exception exp){
		    JOptionPane.showMessageDialog(mainWindow, "Cannot load content: " +
						  exp.toString(), "Error", 
						  JOptionPane.ERROR_MESSAGE);
		    exp.printStackTrace();
		}finally{
		    if(dataIn != null){
		    		try{
		    			dataIn.close();
		    		}catch(Exception e){
		    		}
		    }
		}
		
		return content;
	}
	
	public static void main(String[] args) {
		isApple = System.getProperty("os.name").startsWith("Mac");
		if(isApple){
			//The properties can be set by giving appropriate Java VM
			//arguments which is more flexible.
			//System.setProperty("com.apple.mrj.application.apple.menu.about.name",
			//		"myJavaStat");
			//System.setProperty("apple.laf.useScreenMenuBar", "true");
			//System.setProperty("apple.awt.brushMetalLook", "true");
		}
		
		XLauncher launcher = new XLauncher();
	}
	
	class ConfigReader extends DefaultHandler{
		static final int SERVERURL = 1;
		static final int MODULE = 2;
		static final int MODULE_NAME = 3;
		static final int MODULE_TYPE = 4;
		static final int MODULE_CLASS = 5;
		static final int OTHER = 0;
			
		private int state = OTHER;
		private String name, type, implClass;
		private StringBuffer text = new StringBuffer();
		/*private Map globalMap;
			
		public void startDocument() throws SAXException{
		    globalMap = getParamMap();
		}

		public void endDocument() throws SAXException{
		    globalMap = null;
		}*/
			
		public void startElement(String uri, String localName, String qName,
					 Attributes attrib) throws SAXException{
		    text.delete(0, text.length());
		    if("serverURL".equals(localName) && state == OTHER)
		    		state = SERVERURL;
		    else if("module".equals(localName) && state == OTHER){
		    		name = null;
		    		type = null;
		    		implClass = null;
		    		state = MODULE;
		    }
		    else if("name".equals(localName) && state == MODULE)
		    		state = MODULE_NAME;
		    else if("type".equals(localName) && state == MODULE)
		    		state = MODULE_TYPE;
		    else if("class".equals(localName) && state == MODULE)
		    		state = MODULE_CLASS;
		}
			
		public void characters(char[] ch, int start, int length)
		    throws SAXException{
		    text.append(ch, start, length);
		}
			
		public void endElement(String uri, String localName, 
				       String qName) throws SAXException{
		    switch(state){
		    case SERVERURL:
			    paramMap.put("serverURL", text.toString());
			    state = OTHER;
			    break;
		    case MODULE_NAME:
		    		name = text.toString();
		    		state = MODULE;
		    		break;
		    case MODULE_TYPE:
		    		type = text.toString();
		    		state = MODULE;
		    		break;
		    case MODULE_CLASS:
		    		implClass = text.toString();
		    		state = MODULE;
		    		break;
		    case MODULE:
		    		ModuleInfo moduleInfo = 
		    			new ModuleInfo(name, type, implClass);
		    		moduleMap.put(moduleInfo.getName(), moduleInfo);
		    		state = OTHER;
		    		break;
		    	default:
		    		break;
		    }
		}
			
		public InputSource resolveEntity(String publicId, String systemId)
		    throws SAXException{
		    String dtdURL = "http://stat.wvu.edu/myJavaStat/dtd/config.dtd";
		    if(dtdURL.equals(systemId)){
			InputStream inStream = 
			    getClass().getResourceAsStream("dtd/config.dtd");
			return new InputSource(inStream);
		    }
		    
		    return null;
		}
			
		public void error(SAXParseException exp) throws SAXException{
		    exp.printStackTrace(System.err);
		}
		
		public void fatalError(SAXParseException exp) throws SAXException{
		    error(exp);
		}
			
		public void warning(SAXParseException exp) throws SAXException{
		    error(exp);
		}
	}
	
	private class ModuleLoader implements Runnable{
		private ModuleInfo moduleInfo;
		private Map params;

		public ModuleLoader(ModuleInfo moduleInfo, Map params){
		    this.moduleInfo = moduleInfo;
		    this.params = params;
		}
		
		public void run(){
			try{
				mainWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				/* the next two lines were added by djluo 5/20/2005 */
				mainWindow.getJMenuBar().removeAll();
				mainWindow.getJMenuBar().setVisible(false);
								
				modulePane.removeAll();
				dataModule = null;
				lifeStatsModule = null;
				setChanged();
				notifyObservers("ModulePaneCleaned");
				
				if(moduleInfo == null)
					return;

				Class moduleClass = moduleInfo.getImplClass();
				Object module = moduleClass.newInstance();
				Component component = null;
				if(module instanceof ModuleAdaptor){
					ModuleAdaptor adaptor = (ModuleAdaptor)module;
					component = adaptor.initiateModule(params);
					adaptor.setInModulePane(true);
					addObserver(adaptor);
				}
				else if(module instanceof Component)
					component = (Component)module;
				else if(module instanceof BrowserLaunch){
					/*
					if ("true".equals(params.get("auth")) && !paramMap.containsKey("sessionId")) return;
					BrowserLaunch viewer = (BrowserLaunch)module;
					viewer.openResource((String)params.get("content"), "true".equals(params.get("auth")));
					*/
					return;
				}
				else
					component = new JLabel("Module " + moduleInfo.getName() +
							" has been loaded.");
				modulePane.add(component, BorderLayout.CENTER);
				
				/* the next lines were added by djluo 5/20/2005 */
				if(component instanceof LauncherPlugin){
					mainWindow.getJMenuBar().setVisible(true);
                    ((LauncherPlugin)component).buildMenu(mainWindow.getJMenuBar());
                    dataModule = component;
				}
				
				/* the following were added on 10/3/2007 */
				if(component instanceof LifeStatsPlugin){
					((LifeStatsPlugin)component).setChapterId((String)params.get("id"));
					lifeStatsModule = component;
					
					// change the font size of the subject panel for data/example module
					JTree tr = (JTree)treeContainer.getViewport().getView();
					((LifeStatsPlugin)component).setFontSize(tr.getFont().getSize()); 
				}

				
				component.invalidate();
			}catch(Exception e){
				JOptionPane.showMessageDialog(mainWindow, "Cannot load module",
						"Error", JOptionPane.ERROR_MESSAGE);
				//System.out.println(e.toString());
				e.printStackTrace(System.out);
			}finally{
				modulePane.validate();
				mainWindow.repaint();
				mainWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	
	private class TreeMouseListener extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			Object obj = e.getSource();
			
			if (obj instanceof JTree) {
				JTree tree = (JTree) obj;
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				
				if(selRow != -1){ 
					
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
					if(node.getUserObject() instanceof NodeContent){
						try{	
							NodeContent nc = (NodeContent)node.getUserObject();
							ModuleInfo moduleInfo = (ModuleInfo)moduleMap.get(nc.getToolName());
							
							if(moduleInfo == null)
								return;
							if(moduleInfo.getImplClass() == BrowserLaunch.class){
								if ("true".equals(nc.getParameter("auth")) && !paramMap.containsKey("sessionId")) return;
									
								if((SwingUtilities.isRightMouseButton(e) || e.isControlDown()) ) {
									JPopupMenu popup = new JPopupMenu();
									JMenuItem menuItem = new JMenuItem("Save As...");
									menuItem.addActionListener(
											new TreeActionListener(
													(String)nc.getParameter("content"), 
													"true".equals(nc.getParameter("auth")) ));
									popup.add(menuItem);
									popup.show(e.getComponent(), e.getX(), e.getY());
									//tree.setSelectionPath(selPath);
								}else{
									(new BrowserLaunch()).openResource(
											(String)nc.getParameter("content"), 
											"true".equals(nc.getParameter("auth")) );
									//tree.setSelectionPath(selPath);
								}
							}	
						}catch(Exception ex){}
					}
				}//selRow
			}//JTree
			
		}//mousePressed
	}//TreeMouseListener
	
	private class TreeActionListener implements ActionListener {
		private String content;
		private boolean crypt;
		
		public TreeActionListener(String resource, boolean crypt){
			this.content = resource;
			this.crypt = crypt;
		}
		
		public void actionPerformed(ActionEvent ae){
			String arg = ae.getActionCommand();
			if (arg.equals("Save As...")){
				String filename = content.substring(content.lastIndexOf("/") + 1, content.length());
				File homePath = new File(System.getProperty("user.home"));
				JFileChooser fc = new JFileChooser(homePath);
				File suggestedFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + filename);
				fc.setSelectedFile(suggestedFile);
				int option = fc.showSaveDialog(mainWindow);
				if (option == JFileChooser.CANCEL_OPTION)
                    return;
				File saveFile = fc.getSelectedFile();
				if (saveFile.getName().endsWith(".pdf") == false)
                    saveFile = new File(saveFile.getAbsolutePath() + ".pdf");
				BrowserLaunch bl = new BrowserLaunch();
				if(crypt)
					bl.decryptResourceTo(content, saveFile.getAbsolutePath());
				else
					bl.copyResourceTo(content, saveFile.getAbsolutePath());
			}
		}
	}//TreeActionListener
}
