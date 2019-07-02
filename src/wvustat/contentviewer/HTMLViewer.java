package wvustat.contentviewer;

/**
 * This class is used to show simple HTML content within myJavaStat using
 * JEditPane. For showing complicated HTML pages, please conside 
 * WebBrowserLauncher
 */

import java.util.Map;
import java.util.Observable;
import java.util.Vector;
import java.net.URL;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

import wvustat.launcher.ModuleAdaptor;

public class HTMLViewer extends ModuleAdaptor implements HyperlinkListener{
	private JEditorPane editorPane;
	private JPanel container;
	private JComboBox historyBox;
	private HistoryManager manager = new HistoryManager();
	
	public HTMLViewer(){
		container = new JPanel(new BorderLayout());
		
		JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bar.add(new JLabel("History:"));
		historyBox = new JComboBox(manager.getModel());
		historyBox.addActionListener(manager);
		bar.add(historyBox);
		container.add(bar, BorderLayout.NORTH);
		
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(this);
		JScrollPane scrollPane = new JScrollPane(editorPane);
		container.add(scrollPane, BorderLayout.CENTER);
	}
	
	public Component initiateModule(Map initParam){
		String content = (String)initParam.get("content");
		
		try{
			URL url = null;
			if(content == null)
				editorPane.setText("No content to show");
			else if(content.startsWith("/") || content.startsWith(".")){
				//The content is stored in myJavaStat jar files
				url = getClass().getResource(content);
				editorPane.setPage(url);
			}
			else{
				url = new URL(content);
				editorPane.setPage(url);
			}
			
			if(url != null)
				manager.addToHistory();
		}catch(Exception e){
			JOptionPane.showMessageDialog(container, "Cannot load content",
				"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return container;
	}
	
	public void update(Observable o, Object arg){
		if("ModulePaneCleaned".equals(arg) && isInModulePane()){
			setInModulePane(false);
			o.deleteObserver(this);
		}
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e){
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
			if(e instanceof HTMLFrameHyperlinkEvent){
				HTMLFrameHyperlinkEvent hfevt = (HTMLFrameHyperlinkEvent)e;
				HTMLDocument doc = (HTMLDocument)editorPane.getDocument();
				doc.processHTMLFrameHyperlinkEvent(hfevt);
			}
			else{
				try{
					editorPane.setPage(e.getURL());
					manager.addToHistory();
				}catch(Exception exp){
					JOptionPane.showMessageDialog(container, "Cannot load content",
						"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	class HistoryManager implements ActionListener{
		int size = 10;
		Vector urlList = new Vector();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		public HistoryManager(){
			this(10);
		}
		
		public HistoryManager(int size){
			super();
			if(size <= 0)
				throw new IllegalArgumentException("Negative or zero size");
			this.size = size;
		}
		
		public void addToHistory(){
			URL url = editorPane.getPage();
			if(url == null || urlList.contains(url))
				return;
			
			urlList.add(url);
			String title = 
				(String)editorPane.getDocument().getProperty("Title");
			if(title == null)
				title = url.toExternalForm();
			model.addElement(title);
			if(urlList.size() > size){
				urlList.remove(0);
				model.removeElementAt(0);
			}
			model.setSelectedItem(title);
		}
		
		public ComboBoxModel getModel(){
			return model;
		}
		
		public void actionPerformed(ActionEvent event){
			try{
				JComboBox comboBox = (JComboBox)event.getSource();
				int selectedIndex = comboBox.getSelectedIndex();
				if(selectedIndex >=0)
					editorPane.setPage((URL)urlList.get(selectedIndex));
			}catch(Exception exp){
				JOptionPane.showMessageDialog(container, "Cannot load content",
					"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
	/*
    public final static int MAC = 1;
    public final static int WIN = 2;
    public final static int OTHER = 3;

    //The default commands that the will used to start web browser 
    //under unix platform. @ARG will be replaced by the URL that will
    //be passed to the command.
    public final static String browserCommand = "netscape @ARG";

    public void init(){
	String contentURL = getParameter("contentURL");
	if(contentURL == null)
	    add(new JLabel("No content was specified"));
	else{
	    JButton launchButton = new JButton("Relaunch browser window");
	    launchButton.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			String url = getParameter("contentURL");
			launch(url);
		    }
		});
	    add(launchButton);
	    launch(contentURL);
	}
    }

    final static String mesg = "Input the command to launch web browser ( " +
	"Use @ARG to represent the URL to opened):";

    protected void launch(String url){
	if(url == null)
	    return;

	try{
	    switch(checkPlatform()){
	    case MAC:
		String cmd = "open " + url;
		Process p = Runtime.getRuntime().exec(cmd);
		break;
	    case WIN:
		cmd = "rundll32 url.dll,FileProtocolHandler " + url;
		p = Runtime.getRuntime().exec(cmd);
		break;
	    case OTHER:
		cmd = JOptionPane.showInputDialog(this, mesg, browserCommand);
		p = Runtime.getRuntime().exec(substitute(cmd, "@ARG", url));
		break;
	    }
	}catch(Exception e){
	    JOptionPane.showMessageDialog(this, "Cannot show content: ",
					  "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    protected int checkPlatform(){
	String osName = System.getProperty("os.name");
	if(osName == null)
	    return OTHER;
	else if(osName.startsWith("Mac"))
	    return MAC;
	else if(osName.startsWith("Win"))
	    return WIN;
	else
	    return OTHER;
    }

    private String substitute(String s, String oldP, String newP){
	StringBuffer sb = new StringBuffer();
	int index = 0;
	int base = 0;
	while((index = s.indexOf(oldP, base)) >= 0){
	    sb.append(s.substring(base, index));
	    sb.append(newP);
	    base = index + oldP.length();
	}

	if(base < s.length())
	    sb.append(s.substring(base));

	return sb.toString();
    }
}
*/