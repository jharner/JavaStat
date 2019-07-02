package wvustat.launcher;

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.net.URL;

public class ChapterInfoParser {
	private Document document;
	private Map chapterModules = new HashMap(); //<String, Vector<String>>
	private String chapterUrl = "http://ideal.stat.wvu.edu:8080/ideal/resource/ChapterModule.xml";
	
	public ChapterInfoParser()
	{
	}

	public void parse() throws ParserConfigurationException, IOException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//Set validation if you have DTD.
		//factory.setValidating(true);	   
		DocumentBuilder builder = factory.newDocumentBuilder();
		//InputStream stream = getClass().getResourceAsStream("ChapterModule.xml");
		InputStream stream = new URL(chapterUrl).openStream();
		document = builder.parse(stream);
		parseBook(document.getDocumentElement());
	}
	
	public Map getChapterModules()
	{
		return chapterModules;
	}
	
	private void parseBook(Node node)
	{
		Element elmnt = (Element)node;
		NodeList nodeLst = elmnt.getElementsByTagName("chapter");
		
		for (int i = 0; i < nodeLst.getLength(); i++) 
		{
			Node cNode = nodeLst.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE) {
				parseChapter(cNode);
			}
		}
	}
	
	private void parseChapter(Node node)
	{
		String id = null;
		
		NamedNodeMap attlist = node.getAttributes();
		if (attlist != null)
		{
			Node att = attlist.getNamedItem("id");
			if (att != null)
				id = att.getNodeValue();
		}
		
		if (id != null) 
			chapterModules.put(id, new Vector());
		else
			throw new IllegalArgumentException("chapter tag without id attribute");
		
		Element elmnt = (Element)node;
		NodeList nodeLst = elmnt.getElementsByTagName("module");
		
		for (int i = 0; i < nodeLst.getLength(); i++) 
		{
			Node mNode = nodeLst.item(i);
			if (mNode.getNodeType() == Node.ELEMENT_NODE) {
				String name = mNode.getChildNodes().item(0).getNodeValue();
				Vector v = (Vector)chapterModules.get(id);
				v.addElement(name);
			}
		}
	}
	
	
	public static void main(String[] args) {
		ChapterInfoParser parser = new ChapterInfoParser();
		
		try {
			parser.parse();
		}
		catch (SAXException sxe) {
			sxe.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		

		Map map = parser.getChapterModules();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String chapterId = (String)it.next();
			Vector modules = (Vector) map.get(chapterId);
			for (int i = 0; i < modules.size(); i++)
				System.out.println(chapterId + " " + modules.elementAt(i));
		}
	}

}
