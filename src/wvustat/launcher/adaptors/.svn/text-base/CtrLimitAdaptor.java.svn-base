package wvustat.launcher.adaptors;

import java.awt.Component;
import java.util.Map;
import java.util.Observable;

import wvustat.launcher.ModuleAdaptor;
import wvustat.simulation.centrallimit.CtrLimit;

public class CtrLimitAdaptor extends ModuleAdaptor {
	final static String distNames[] = {
		"normal", "chisq", "uniform", "bowtie", "wedge_L", "wedge_R",
		"triangle"
	};
	
	/* (non-Javadoc)
	 * @see wvustat.launcher.ModuleAdaptor#initiateModule(java.util.Map)
	 */
	public Component initiateModule(Map initParam) {
		return new CtrLimit(initParam, distNames);
	}
	
	public void update(Observable o, Object arg){
		if("ModulePaneCleaned".equals(arg) && isInModulePane()){
			setInModulePane(false);
			o.deleteObserver(this);
		}
	}
}
