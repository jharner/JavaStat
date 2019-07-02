package wvustat.xmldocviewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class ViewerTester {
	JFrame mainWindow;
	ExampleViewer exampleViewer;
	
	public ViewerTester(){
		mainWindow = new JFrame("LifeStats XML document viewer test");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		exampleViewer = new ExampleViewer();
		mainWindow.getContentPane().add(new JScrollPane(exampleViewer.getEditorPane()),
				BorderLayout.CENTER);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		p.add(new JButton(new OpenAction()));
		mainWindow.getContentPane().add(p, BorderLayout.SOUTH);
		mainWindow.setSize(400, 300);
	}
	
	public void setVisible(boolean v){
		mainWindow.setVisible(v);
	}
	
	public static void main(String[] args){
		ViewerTester tester = new ViewerTester();
		tester.setVisible(true);
	}
	
	public class OpenAction extends AbstractAction{
		private static final long serialVersionUID = 1L;

		public OpenAction(){
			super("Open");
		}
		
		public void actionPerformed(ActionEvent event){
			JFileChooser fChooser = new JFileChooser();
			int retval = fChooser.showOpenDialog(mainWindow);
			if(retval == JFileChooser.APPROVE_OPTION){
				try{
					String url = fChooser.getSelectedFile().toURL().toString();
					exampleViewer.setSourceURL(url);
				}catch(Exception exp){
					JOptionPane.showMessageDialog(mainWindow, exp, "Open Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
