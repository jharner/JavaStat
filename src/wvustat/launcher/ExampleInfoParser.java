package wvustat.launcher;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;
import java.net.URL;
import java.util.*;


public class ExampleInfoParser {
	private Document document;
	private HashMap subjectMap = new HashMap(); //<String, <HashSet>>
	private HashMap fileMap = new HashMap(); //<String, String>
	private HashMap descriptionMap = new HashMap(); //<String, String>
	private Vector modules; //<String>
	private String examplesUrl = "http://ideal.stat.wvu.edu:8080/ideal/resource/examples.xml";

	public ExampleInfoParser(Vector modules) {
		this.modules = modules;
	}
	
	public void parse() throws ParserConfigurationException, IOException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//Set validation if you have DTD.
		//factory.setValidating(true);	   
		DocumentBuilder builder = factory.newDocumentBuilder();
		//InputStream stream = getClass().getResourceAsStream("examples.xml");
		InputStream stream = new URL(examplesUrl).openStream();
		document = builder.parse(stream);
		parseBook(document.getDocumentElement());
	}
	
	private void parseBook(Node node)
	{
		Element elmnt = (Element)node;
		NodeList nodeLst = elmnt.getElementsByTagName("example");
		
		for (int i = 0; i < nodeLst.getLength(); i++) 
		{
			Node cNode = nodeLst.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				parseExample(cNode);
			}
		}
	}
	
	private void parseExample(Node node)
	{		
		Element elmnt = (Element)node;
		String name = elmnt.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
		String file = elmnt.getElementsByTagName("file").item(0).getChildNodes().item(0).getNodeValue();
		String subject = elmnt.getElementsByTagName("subject").item(0).getChildNodes().item(0).getNodeValue();
		String module = elmnt.getElementsByTagName("module").item(0).getChildNodes().item(0).getNodeValue();
		String description = elmnt.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
		
		// Check modules
        if(module != null) {
        	List lst = Arrays.asList(module.replaceAll(" ", "").split(","));
        	ArrayList lst1 = new ArrayList(lst);
        	lst1.retainAll(this.modules);
        	if (lst1.size() == 0)
        		return;
        } else 
        	return;
        
        // Get subjects
        if(subject != null) {
        	StringTokenizer st = new StringTokenizer(subject, ",");
        	while (st.hasMoreTokens()) {
        		String s = st.nextToken();
        		putSubjectInMap(s.trim(), name);
        	}
        }
        
        // Get file
        if(file != null)
        	fileMap.put(name, file);
        
        // Get description
        if(description != null)
        	descriptionMap.put(name, description);
        
	}
	
	private void putSubjectInMap(String subject, String name) {
		if (subjectMap.containsKey(subject)) {
			HashSet set = (HashSet) subjectMap.get(subject);
			set.add(name);
		} else {
			HashSet set = new HashSet();
			set.add(name);
			subjectMap.put(subject, set);
		}
	}
	
	public HashMap getSubjectMap() {
		return subjectMap;
	}
	
	public HashMap getDescriptionMap() {
		return descriptionMap;
	}
	
	public HashMap getFileMap() {
		return fileMap;
	}
	
	public static void main(String[] args) {
		Vector modules = new Vector();
		modules.addElement("Histograms");
		ExampleInfoParser parser = new ExampleInfoParser(modules);
		
		try {
			parser.parse();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println(parser.getSubjectMap().toString());
		System.out.println(parser.getFileMap().toString());
		System.out.println(parser.getDescriptionMap().toString());

	}

}
