package wvustat.table;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Map;

import javax.swing.*;

import wvustat.launcher.ChapterInfoParser;
import wvustat.swing.*;
import wvustat.util.ComponentUtil;


public class ExampleDataPanel extends JPanel implements LauncherPlugin, LifeStatsPlugin{
	private static final long serialVersionUID = 1L;
	private String dataUrl = "http://ideal.stat.wvu.edu/ideal/datasets/";
	private MainPanel mainPanel;
	private SubjectPanel topPanel;

	public ExampleDataPanel(){
		
		setLayout(new BorderLayout());
		
		topPanel = new SubjectPanel(SubjectPanel.DATASET);
		topPanel.getList().addMouseListener(new ExampleDataPanel.ClickListener());
		topPanel.setMinimumSize(new Dimension(0, 0));
		
		mainPanel = new MainPanel();
		mainPanel.setMinimumSize(new Dimension(200, 200));
		
		JSplitPane splitPane =
			new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, mainPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(120);
		splitPane.setBorder(null);
		
		add(splitPane, BorderLayout.CENTER);
		mainPanel.invalidate();
	}	
	
	public void buildMenu(JMenuBar menuBar) {
		mainPanel.buildMenu(menuBar);
	}
	
	public void arrangeAllWindows() {
		mainPanel.arrangeAllWindows();
	}
	
	public void setFontSize(int c) {
		topPanel.setFontSize(c);
	}
	
	public void setChapterId(String id) {
		
		try {
			ChapterInfoParser parser = new ChapterInfoParser();
			parser.parse();
			Vector modules = (Vector) parser.getChapterModules().get(id);
			
			XMLDataClassifier classifier = new XMLDataClassifier(dataUrl, modules);
			Map subjects = classifier.getSubjectMap();
			Map descriptions = classifier.getDescriptionMap();	
		
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
				mainPanel.openRemoteXMLFile(name.trim());				
			}
		}
	}
}
