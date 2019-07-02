/*
 * Created on Mar 28, 2006
 *
 */
package wvustat.modules;

import javax.swing.*;
import javax.swing.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author dajieluo
 *
 */
public class LoessSpanConfigPanel extends JPanel implements ChangeListener{
	private static final int BW_MAX = 100;
	private int bw_min;
	private int bw_init;
	
	private ScatterPlot plot;
	private JSlider bandwidthChooser;
	private double initBandwidth;
	private JLabel label;
	private NumberFormat nf;
	
	public LoessSpanConfigPanel(ScatterPlot plot){
		this.plot = plot;
		initBandwidth = plot.getLoessSpan();
		
		bw_init = (int)(initBandwidth * 100);
		bw_min = (int)(plot.getMinLoessSpan() * 100);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		bandwidthChooser = new JSlider(JSlider.HORIZONTAL, bw_min, BW_MAX, bw_init);
		bandwidthChooser.addChangeListener(this);
		bandwidthChooser.setMajorTickSpacing(25);
		bandwidthChooser.setMinorTickSpacing(5);
		bandwidthChooser.setPaintTicks(true);
		
		nf=new DecimalFormat("########.##");
		nf.setMaximumFractionDigits(2);
		label = new JLabel(nf.format(initBandwidth), JLabel.LEFT);
		
		add(label);
		add(bandwidthChooser);
		//setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	public void stateChanged(ChangeEvent e){
		JSlider source = (JSlider)e.getSource();
		int p = (int)source.getValue();
		double bw = p/100.0;
		label.setText(nf.format(bw));
		if (!source.getValueIsAdjusting()) {
			plot.setLoessSpan(bw);
		}
	}

}
