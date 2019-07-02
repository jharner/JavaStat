package wvustat.simulation.continuousdist;

import javax.swing.JApplet;

public class ContinuousDistApplet extends JApplet{
	final static String[][] pInfo = {
		{"DistributionName", "Normal or Chisquare", "Name of distribution"},
		{"display", "PDF or CDF", "The type of graph to show"},
		{"mu", "Number", "The mean parameter for normal distribution"},
		{"sd", "Positive number", "The standard deviation for normal distribution"},
		{"df", "Positive integer", "Degree of freedom of ChiSquare distribution"},
	};
	
	public void init(){
		java.util.Map params = new java.util.HashMap();
		for(int i = 0; i < pInfo.length; i++){
			String pv = getParameter(pInfo[i][0]);
			if(pv != null)
				params.put(pInfo[i][0], pv);
		}
		
		continuousDist dist = new continuousDist(params);
		getContentPane().add(dist);
	}

	public String[][] getParameterInfo(){
		return pInfo;
	}
}
