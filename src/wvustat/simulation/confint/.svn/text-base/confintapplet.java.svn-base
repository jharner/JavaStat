package wvustat.simulation.confint;

import javax.swing.JApplet;
import java.util.HashMap;

public class confintapplet extends JApplet{
		public final static String[][] pInfo={{"mu"},{"sigma"},{"alpha"},{"n"}};
		

		public void init() {
			HashMap map =new HashMap();
			map.put("mu",getParameter("mu"));
			map.put("sigma",getParameter("sigma"));
			map.put("alpha",getParameter("alpha"));
			map.put("n",getParameter("n"));
			
			confint myconfint=new confint(map);
			getContentPane().add(myconfint);
		}
		
		public String[][] getParameterInfo(){
			return pInfo;
		}
}