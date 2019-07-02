
package wvustat.modules;

import javax.swing.*;
import javax.swing.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
 * @author dajieluo
 *
 */
public class BandwidthConfigPanel extends JPanel implements ChangeListener{
	private static final int BW_MIN = 10;
	private static final int BW_MAX = 250;
	private int bw_init = 100; 
	
	private HistogramPlot hplot;
	private JSlider bandwidthChooser;
	private double initBandwidth, defaultBandwidth;
	private JLabel label;
	private NumberFormat nf;
	
	public BandwidthConfigPanel(HistogramPlot hplot){
		this.hplot = hplot;
		initBandwidth = hplot.getKernelBandwidth();
		defaultBandwidth = hplot.getDefaultKernelBandwidth();
		bw_init = (int)(initBandwidth * 100 / defaultBandwidth);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		bandwidthChooser = new JSlider(JSlider.HORIZONTAL, BW_MIN, BW_MAX, bw_init);
		bandwidthChooser.addChangeListener(this);
		bandwidthChooser.setMajorTickSpacing(60);
		bandwidthChooser.setMinorTickSpacing(10);
		bandwidthChooser.setPaintTicks(true);
		
		nf=new DecimalFormat("########.#####");
		nf.setMaximumFractionDigits(5);
		label = new JLabel(nf.format(initBandwidth), JLabel.LEFT);
		
		add(label);
		add(bandwidthChooser);
		//setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	public void stateChanged(ChangeEvent e){
		JSlider source = (JSlider)e.getSource();
		int p = (int)source.getValue();
		double bw = defaultBandwidth*p/100;
		label.setText(nf.format(bw));
		//if (!source.getValueIsAdjusting()) 	
			hplot.setKernelBandwidth(bw);
	}

}
