package wvustat.xmldocviewer;

import java.applet.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.*;

public class AppletInfo implements AppletStub{
	private String code;
	private String codeBase;
	private int width;
	private int height;
	private Map parameterMap; //<String, String>
	private Applet applet;
	
	public AppletInfo(){
		width = 100;
		height = 100;
	}
	
	public AppletInfo(String c, String cb, int w, int h, Map pm){
		setCode(c);
		setCodeBase(cb);
		setWidth(w);
		setHeight(h);
		setParameterMap(pm);
	}
	
	public AppletInfo(Element node){
		setCodeBase(node.getAttribute("codebase"));
		setCode(node.getAttribute("code"));
		setWidth(Integer.parseInt(node.getAttribute("width")));
		setHeight(Integer.parseInt(node.getAttribute("height")));
		
		HashMap pm = new HashMap(); //<String, String>
		NodeList nList = node.getElementsByTagName("param");
		for(int i = 0; i < nList.getLength(); i++){
			Element element = (Element)nList.item(i);
			pm.put(element.getAttribute("name").toLowerCase(), 
					element.getAttribute("value"));
		}
		setParameterMap(pm);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		if(code == null || code.trim().length() == 0)
			throw new IllegalArgumentException("Empty class name");
		
		this.code = code;
	}

	public URL getCodeBase() {
		try{
			return new URL(codeBase);
		}catch(Exception exp){
			return null;
		}
	}

	public void setCodeBase(String codeBase) {
		this.codeBase = codeBase;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if(height <= 0)
			throw new IllegalArgumentException("Nonpositive height");
		
		this.height = height;
	}

	public Map getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map parameterMap) {
		this.parameterMap = parameterMap;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if(width <= 0)
			throw new IllegalArgumentException("Nonpositive width");
		
		this.width = width;
	}

	public void appletResize(int width, int height) {
		if(applet != null){
			applet.resize(width, height);
		}
	}

	public AppletContext getAppletContext() {
		return null;
	}

	public URL getDocumentBase() {
		return getCodeBase();
	}

	public String getParameter(String name) {
		if(parameterMap == null)
			return null;
		
		return (String)parameterMap.get(name.toLowerCase());
	}

	public boolean isActive() {
		if(applet == null)
			return false;
		
		return applet.isActive();
	}
	
	public void loadApplet() throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException{
		Class c = Class.forName(getCode());
		applet = (Applet)c.newInstance();
		
		Frame frame = new Frame();
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				applet.stop();
				applet.destroy();
				((Window)event.getSource()).dispose();
			}
		});
		
		frame.add(applet, BorderLayout.CENTER);
		frame.setVisible(true);
		applet.setStub(this);
		applet.init();
		applet.start();
		frame.pack();
	}
}
