package wvustat.swing;

import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

public class ScaleConfigPanel extends JPanel implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private static final double precision = 0.01;
	
	private JSlider slider;
	private JLabel label;
	private NumberFormat nf;

	public ScaleConfigPanel(double min, double max, double value) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		slider = new JSlider(JSlider.HORIZONTAL, (int)(min/precision), (int)(max/precision), (int)(value/precision));
		slider.addChangeListener(this);
		slider.setMajorTickSpacing((slider.getMaximum() - slider.getMinimum()) / 2);
		slider.setMinorTickSpacing(slider.getMajorTickSpacing() / 5);
		slider.setPaintTicks(true);
		
		nf=new DecimalFormat("########.##");
		nf.setMaximumFractionDigits(2);
		label = new JLabel(nf.format(value), JLabel.LEFT);
		
		add(label);
		add(slider);
	}
	
	public void stateChanged(ChangeEvent e){
		JSlider source = (JSlider)e.getSource();
		int p = (int)source.getValue();
		double value = p * precision;
		label.setText(nf.format(value));
	}
	
	public double getValue() {
		return slider.getValue() * precision;
	}
	
}
