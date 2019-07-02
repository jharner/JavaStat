package wvustat.simulation.sampling;

import javax.swing.JApplet;
import java.util.HashMap;

public class SamplerApplet extends JApplet{
	public final static String[][] pInfo ={
			{"populationSize", "positive integer", "Population size"},
			{"sampleSize", "positive integer", "Sample size"},
			{"pRed", "double between 0 and 1", "Percentage of red balls"},
			{"withReplacement", "boolean", "Draw sample with replacement"}
	};
	
	public void init(){
		HashMap map = new HashMap();
		map.put("populationSize", getParameter("populationSize"));
		map.put("sampleSize", getParameter("sampleSize"));
		map.put("pRed", getParameter("pRed"));
		map.put("withReplacement", getParameter("withReplacement"));
		
		Sampler sampler = new Sampler(map);
		getContentPane().add(sampler);
	}
	
	public String[][] getParameterInfo(){
		return pInfo;
	}
}
