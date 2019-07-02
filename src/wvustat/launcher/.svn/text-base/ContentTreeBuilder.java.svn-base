package wvustat.launcher;

import java.io.InputStream;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultTreeSelectionModel;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class ContentTreeBuilder extends DefaultHandler{
    final static int INIT = 0;
    final static int CONTENT = 1;
    final static int CTITLE  = 2;
    final static int NODE = 3;
    final static int NTITLE = 4;
    final static int STATTOOL = 5;
    final static int TOOLNAME = 6;
    final static int PARAMLIST = 7;
    final static int PARAM = 10;
    final static int PNAME = 8;
    final static int PVALUE = 9;

    private int status = INIT;
    private Locator locator;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode current;
    private StringBuffer text = new StringBuffer();

    private String name;
    private String value;

    public JTree getTree(){
	JTree tree = null;

	if(status != INIT || root == null)
	    tree = new JTree(new DefaultMutableTreeNode("Empty"));
        else{
	    tree = new JTree(root);
	    root = null;
	}

	tree.setRootVisible(true);
	DefaultTreeSelectionModel sModel = new DefaultTreeSelectionModel();
	sModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.setSelectionModel(sModel);

	return tree;
    }

    public void setDocumentLocator(Locator locator){
	this.locator = locator;
    }

    public void startDocument() throws SAXException{
	root = null;
	current = null;
	status = INIT;
    }

    public void endDocument() throws SAXException{
	status = INIT;
    }

    public void startElement(String uri, String localName, String qName,
			     Attributes attrib) throws SAXException{
	text.delete(0, text.length());

	if("contents".equals(localName) && status == INIT){
	    status = CONTENT;
	    root = new DefaultMutableTreeNode();
	    root.setUserObject(new NodeContent());
	    current = root;
	}else if("title".equals(localName)){
	    status = (status == CONTENT)?(CTITLE):(NTITLE);
	}
	else if("node".equals(localName) && 
		(status == CONTENT || status == NODE)){
	    status = NODE;
	    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
	    node.setUserObject(new NodeContent());
	    current.add(node);
	    current = node;
	}
	else if("javaStatTool".equals(localName) && (status == NODE || status == CONTENT)) //djluo
	    status = STATTOOL;
	else if("toolName".equals(localName) && status == STATTOOL)
	    status = TOOLNAME;
	else if("paramList".equals(localName) && status == STATTOOL)
	    status = PARAMLIST;
	else if("param".equals(localName) && status == PARAMLIST){
	    name = null;
	    value = null;
	    status = PARAM;
	}
	else if("name".equals(localName) && status == PARAM)
	    status = PNAME;
	else if("value".equals(localName) && status == PARAM)
	    status = PVALUE;
    }

    public void endElement(String namespaceURI, String localName,
			   String qName) throws SAXException{
	NodeContent content;

	switch(status){
	case CONTENT:
	    current = null;
	    status = INIT;
	    break;
	case NODE:
	    current = (DefaultMutableTreeNode)current.getParent();
	    if(current.getParent() == null)
		status = CONTENT;
	    else
		status = NODE;
	    break;
	case CTITLE:
	case NTITLE:
	    content = (NodeContent)current.getUserObject();
	    content.setTitle(text.toString().trim());
	    status = (status == CTITLE)?(CONTENT):(NODE);
	    break;
	case STATTOOL:
		//modified by djluo
	    if(current.getParent() == null)
		status = CONTENT;
	    else
		status = NODE;
	    //status = NODE;
	    break;
	case TOOLNAME:
	    content = (NodeContent)current.getUserObject();
	    content.setToolName(text.toString().trim());
	    status = STATTOOL;
	    break;
	case PARAMLIST:
	    status = STATTOOL;
	    break;
	case PARAM:
	    content = (NodeContent)current.getUserObject();
	    content.setParameter(name, value);
	    status = PARAMLIST;
	    break;
	case PNAME:
	    name = text.toString().trim();
		status = PARAM;
	    break;
	case PVALUE:
	    value = text.toString().trim();
		status = PARAM;
	    break;
	default:
	    break;
	}
    }

    public void characters(char[] ch, int start, int length)
	throws SAXException{
	text.append(ch, start, length);
    }

    public InputSource resolveEntity(String publicId, String systemId)
	throws SAXException{
	String dtdURL = "http://stat.wvu.edu/myJavaStat/dtd/contents.dtd";

	if(dtdURL.equals(systemId)){
	    InputStream inStream = 
		getClass().getResourceAsStream("dtd/contents.dtd");
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
