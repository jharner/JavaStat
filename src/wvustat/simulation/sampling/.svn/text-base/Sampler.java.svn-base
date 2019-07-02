package wvustat.simulation.sampling;

import java.awt.*;
import javax.swing.*;
import java.util.Map;
/**
 * @author jtan
 *
 * The main GUI component of the sampling program.
 */
public class Sampler extends JComponent{
	ControlPane controlPane;
	SamplePane samplePane;
	
	public Sampler(){
		this(null);
	}
	public Sampler(Map initParams){
		if(initParams == null)
			samplePane = new SamplePane();
		else{
			String ps = (String)initParams.get("populationSize");
			String ss = (String)initParams.get("sampleSize");
			String prs = (String)initParams.get("pRed");
			String rs = (String)initParams.get("withReplacement");
			
			int pSize = (ps == null)?(50):(Integer.parseInt(ps));
			int sSize = (ss == null)?(10):(Integer.parseInt(ss));
			double p = (prs == null)?(0.4):(Double.parseDouble(prs));
			boolean r = 
				(rs == null)?(true):(Boolean.valueOf(rs).booleanValue());
		    
			samplePane = new SamplePane(pSize, sSize, p, r);
		}
		
		controlPane = new ControlPane(samplePane);
		
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Sampling With or Without Replacement",
				SwingConstants.CENTER);
		title.setBackground(Color.black);
		title.setForeground(Color.white);
		title.setOpaque(true);
		
		samplePane.setBackground(Color.black);
		samplePane.setForeground(Color.white);
		
		add(title, BorderLayout.NORTH);
		add(samplePane, BorderLayout.CENTER);
		add(controlPane, BorderLayout.SOUTH);
	}
}
