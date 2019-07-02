package wvustat.table;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import wvustat.interfaces.LinkPolicy;
import wvustat.interfaces.LaunchOption;
import wvustat.swing.ImageIcons;
import wvustat.swing.LauncherPlugin;


/**
 * @author djluo
 *
 * Launch the data module of myJavaStat as a stand alone application
 *
 */
public class Launcher {
	private JFrame mainWindow;
	private JPanel modulePane;
	private MainPanel mainPanel;
	private JMenuBar menuBar=new JMenuBar();
	private JLabel statusBar;
	
	public Launcher(){
		LaunchOption.microArrayEnable = true;
		
		if(System.getProperty("wvustat.workflow", "false").equals("true"))
			LaunchOption.workflowEnable = true;
		
		mainWindow = new JFrame("JavaStat");
		if (LaunchOption.workflowEnable) mainWindow.setTitle("JavaStat");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(900, 680);
		
		mainWindow.setJMenuBar(menuBar);
		
		modulePane = new JPanel(new BorderLayout());
		modulePane.setMinimumSize(new Dimension(200, 200));
		
		mainWindow.getContentPane().add(modulePane, BorderLayout.CENTER);
		
		mainPanel = new MainPanel();
		modulePane.add(mainPanel, BorderLayout.CENTER);

		if(mainPanel instanceof LauncherPlugin)
            ((LauncherPlugin)mainPanel).buildMenu(mainWindow.getJMenuBar());
		
		statusBar = new JLabel();	
		statusBar.setFont(new Font(null, Font.PLAIN, 10));
		statusBar.setBorder(new EmptyBorder(2, 10, 2, 2));
		statusBar.setText("R Login...");
		statusBar.addMouseListener(new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		if (statusBar.getText().equals("R Login...")) {
	    			if (mainPanel.login())
	    				statusBar.setText("Connected");
	    		} else {
	    			if (mainPanel.logout())
	    				statusBar.setText("R Login...");
	    		}
	    	}
	    });
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(statusBar, BorderLayout.CENTER);
		
		/* Create icons for linking mode in the right bottom of the window. */
		JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
		
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
		
		
		JLabel blankIcon = new JLabel(ImageIcons.BLANK);
		iconPanel.add(blankIcon);
		
		JLabel arrangeIcon = new JLabel(ImageIcons.ARRANGE_ALL);
		arrangeIcon.setToolTipText("Arrange Windows   ");
		arrangeIcon.setBorder(new EmptyBorder(0, 0, 0, 30));
		arrangeIcon.addMouseListener(new MouseAdapter(){
	    	public void mouseClicked(MouseEvent e)
	    	{
	    		mainPanel.arrangeAllWindows();
	    	}
		});
		iconPanel.add(arrangeIcon);
		
		bottomPanel.add(iconPanel, BorderLayout.EAST);
		
		mainWindow.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		mainWindow.setVisible(true);
		mainPanel.invalidate();
	}

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
	}
}
