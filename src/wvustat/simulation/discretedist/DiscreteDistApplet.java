package wvustat.simulation.discretedist;

import javax.swing.JApplet;

public class DiscreteDistApplet extends JApplet {
	final static String[][] pInfo = {
		{"DistributionName", "Binomial or Poisson", "Name of distribution"},
		{"display", "PDF or CDF", "The type of graph to show"},
		{"ApproxDistribution", "P or N", "Need poisson or normal approximation"},
		{"n", "Positive integer", "n parameter for binomial distribution"},
		{"p", "Number between 0 and 1", "p parameter for binomial distribution"},
		{"lambda", "Number", "lamda for poisson distribution"}
	};
	
	public void init(){
		java.util.Map params = new java.util.HashMap();
		for(int i = 0; i < pInfo.length; i++){
			String pv = getParameter(pInfo[i][0]);
			if(pv != null)
				params.put(pInfo[i][0], pv);
		}
		
		discreteDist dist = new discreteDist(params);
		getContentPane().add(dist);
	}
	
	public String[][] getParameterInfo(){
		return pInfo;
	}
}
