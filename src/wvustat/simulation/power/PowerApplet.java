package wvustat.simulation.power;

import javax.swing.JApplet;
import java.util.HashMap;

public class PowerApplet extends JApplet{
		public final static String[][] pInfo={{"Delta"},{"N"},{"Sigma"},{"Alpha"}};
		

		public void init() {
			HashMap map =new HashMap();
			map.put("Delta",getParameter("Delta"));
			map.put("N",getParameter("N"));
			map.put("Sigma",getParameter("Sigma"));
			map.put("Alpha",getParameter("Alpha"));
			
			Power mypower=new Power(map);
			getContentPane().add(mypower);
		}
		
		public String[][] getParameterInfo(){
			return pInfo;
		}
}