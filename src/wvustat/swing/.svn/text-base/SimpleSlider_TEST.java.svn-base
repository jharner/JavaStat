package wvustat.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class SimpleSlider_TEST extends JPanel implements ChangeListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private SimpleSlider slider;
	private JPopupMenu popup;
	
	public SimpleSlider_TEST() {
		slider = new SimpleSlider(0, 100, 50);
		slider.setFractionDigits(0);
		slider.setLabel("Bars");
		slider.addChangeListener(this);	

		setSize(new Dimension(300, 300)); // call setBounds()
		setPreferredSize(new Dimension(300, 300));
		setMinimumSize(new Dimension(300, 300));		
		
		setLayout(null);
		add(slider);
		//Dimension size = slider.getPreferredSize();
		//slider.setBounds((getSize().width - size.width) / 2, 10, size.width, size.height);
		
		popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Bars");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        menuItem = new JMenuItem("Shifts");
        menuItem.addActionListener(this);
        popup.add(menuItem);
        //slider.setPopup(popup);
	}
	
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		
		Dimension size = slider.getPreferredSize();
		slider.setBounds((w - size.width) / 2, 10, size.width, size.height);
		
	}
	
	public void stateChanged(ChangeEvent e) {
		repaint();
	}
	
	public void actionPerformed(ActionEvent ae){
		String arg = ae.getActionCommand();
		System.out.println(arg);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;		
		g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
		FontMetrics metrics = getFontMetrics(getFont());
		
		g2.drawString(slider.getLabel(), (getSize().width + slider.getSize().width) / 2 + slider.getGap(), slider.getLocation().y + slider.getSize().height);
		g2.drawString(slider.getTextValue(), slider.getLocation().x - metrics.stringWidth(slider.getTextValue()) - slider.getGap(), slider.getLocation().y + slider.getSize().height);
	}
	
	public static void main(String[] args) {
		SimpleSlider_TEST s = new SimpleSlider_TEST();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(s);
		frame.pack();
		frame.setVisible(true);
	}
}
