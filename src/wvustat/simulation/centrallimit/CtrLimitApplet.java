package wvustat.simulation.centrallimit;

import javax.swing.JApplet;

public class CtrLimitApplet extends JApplet {
	final static String distNames[] = {
		"normal", "chisq", "uniform", "bowtie", "wedge_L", "wedge_R",
		"triangle"
	};
	
	final static String[][] pInfo = {
		{"SampleSize", "Positive integer", "Sample Size"},
		{"TotalSamples", "Larger than sample size", "Total Samples"},
		{"SamplePerSecond", "Positive integer", "# of Sample/click"},
		{"BarsNumber", "Positive integer", "Bars of histogram"}
	};
	
	public void init(){
		java.util.Map params = new java.util.HashMap();
		for(int i = 0; i < pInfo.length; i++){
			String pv = getParameter(pInfo[i][0]);
			if(pv != null)
				params.put(pInfo[i][0], pv);
		}
		
		CtrLimit ctrLimit = new CtrLimit(params, distNames);
		getContentPane().add(ctrLimit);
	}
	
	public String[][] getParameterInfo(){
			return pInfo;
	}
}
