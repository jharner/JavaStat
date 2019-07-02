package wvustat.table;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Map;
import java.io.*;

import javax.swing.*;

import wvustat.swing.LifeStatsPlugin;
import wvustat.util.ComponentUtil;
import wvustat.launcher.ChapterInfoParser;
import wvustat.launcher.ExampleInfoParser;
import wvustat.xmldocviewer.ExampleViewer;
import wvustat.contentviewer.BrowserLaunch;


public class ExamplePanel extends JPanel implements LifeStatsPlugin{
	private static final long serialVersionUID = 1L;
	
	private ExampleViewer exampleViewer;
	private SubjectPanel topPanel;
	private Map files;

	public ExamplePanel(){
		
		setLayout(new BorderLayout());
		
		topPanel = new SubjectPanel(SubjectPanel.EXAMPLE);
		topPanel.getList().addMouseListener(new ExamplePanel.ClickListener());
		topPanel.setMinimumSize(new Dimension(0, 0));
		
		exampleViewer = new ExampleViewer();
		exampleViewer.getEditorPane().setMinimumSize(new Dimension(200, 200));
		exampleViewer.getEditorPane().setBackground(new Color(172, 172, 172));
		
		JSplitPane splitPane =
			new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, new JScrollPane(exampleViewer.getEditorPane()));
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(120);
		splitPane.setBorder(null);
		
		add(splitPane, BorderLayout.CENTER);
		
	}	
	
	public void setFontSize(int c) {
		topPanel.setFontSize(c);
	}
	
	public void setChapterId(String id) {
		
		try {
			ChapterInfoParser parser = new ChapterInfoParser();
			parser.parse();
			Vector modules = (Vector) parser.getChapterModules().get(id);
			
			ExampleInfoParser exparser = new ExampleInfoParser(modules);
			exparser.parse();
			
			Map subjects = exparser.getSubjectMap();
			Map descriptions = exparser.getDescriptionMap();	
			files = exparser.getFileMap();
		
			topPanel.setContents(subjects, descriptions);
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(ComponentUtil.getTopComponent(this), ex, "", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}
	
	class ClickListener extends MouseAdapter{
		public void mouseClicked(MouseEvent evt){
			if(evt.getClickCount()!=2)
				return;
			
			JList theList = (JList)evt.getSource();
			if (!theList.isSelectionEmpty()) {
				String name = (String)theList.getSelectedValue();
				String resource = (String)files.get(name);
				try {
					//File tmpfile = (new BrowserLaunch()).copyResourceToTmpFile(resource, false);
					exampleViewer.getEditorPane().setBackground(Color.white);
					//exampleViewer.setSourceURL(tmpfile.toURL().toString());	
					exampleViewer.setSourceURL(resource);
					exampleViewer.getEditorPane().setCaretPosition(0);
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(ExamplePanel.this, exp, "Open Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
