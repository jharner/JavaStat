package wvustat.xmldocviewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ExampleViewer{
	private String sourceURL;
	private Document document;
	private JEditorPane editorPane;
	
	public ExampleViewer(){
		editorPane = new JEditorPane();
		editorPane.setContentType("text/html");
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(new HyperActionHandler());
	}
	
	public String getSourceURL() {
		return sourceURL;
	}

	public void setSourceURL(String sourceURL) throws IOException, SAXException{
		this.sourceURL = sourceURL;
		try{
			DocumentBuilder DOMBuilder = 
				DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = DOMBuilder.parse(sourceURL);
			DOMSource source = new DOMSource(document);
			InputStream xslStream =
				getClass().getResourceAsStream("appletFilter.xsl");
			StreamSource xslSource = new StreamSource(xslStream);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(byteStream);
			Transformer trans =
				TransformerFactory.newInstance().newTransformer(xslSource);
			trans.transform(source, result);
			
			HTMLDocument doc = (HTMLDocument)editorPane.getDocument();
			doc.setBase(new URL(sourceURL));
			editorPane.setText(byteStream.toString());
		}catch(IOException exp){
			throw exp;
		}catch(SAXException exp){
			throw exp;
		}catch(Exception exp){
			throw new IllegalArgumentException(exp.getMessage());
		}
	}

	public JEditorPane getEditorPane() {
		return editorPane;
	}
	
	public class HyperActionHandler implements HyperlinkListener{
		public void hyperlinkUpdate(HyperlinkEvent e) {
			
			if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
				if(e.getDescription() != null && 
						e.getDescription().startsWith("applet://")){
					try{
						launchApplet(e.getDescription());
					}catch(Exception exp){
					}
				}
				else if(e instanceof HTMLFrameHyperlinkEvent){
					HTMLFrameHyperlinkEvent event = (HTMLFrameHyperlinkEvent)e;
					HTMLDocument doc = (HTMLDocument)editorPane.getDocument();
					doc.processHTMLFrameHyperlinkEvent(event);
				}
				else{
					ExternalAppLauncher.openURL(e.getURL());
				}
			}
		}
		
		protected void launchApplet(String name) throws Exception{
			if(name == null || name.length() < "applet://".length())
				return;
			
			String appletName = name.substring("applet://".length());
			NodeList nodeList = document.getElementsByTagName("applet");
			Element node = null;
			for(int i = 0; i < nodeList.getLength(); i++){
				Element element = (Element)nodeList.item(i);
				if(appletName.equals(element.getAttribute("name"))){
					node = element;
					break;
				}
			}
			
			AppletInfo appletInfo = new AppletInfo(node);
			appletInfo.loadApplet();
		}
	}
}
