package wvustat.launcher.adaptors;

import java.awt.Component;
import java.util.Map;
import java.util.Observable;

import wvustat.launcher.ModuleAdaptor;
import wvustat.simulation.confint.confint;

public class ConfintAdaptor extends ModuleAdaptor{
	private confint conf;
	
	/* (non-Javadoc)
	 * @see wvustat.launcher.ModuleAdaptor#initiateModule(java.util.Map)
	 */
	public Component initiateModule(Map initParam) {
		conf = new confint(initParam);
		return conf;
	}
	
	public void update(Observable o, Object arg){
		if("ModulePaneCleaned".equals(arg) && isInModulePane()){
			setInModulePane(false);
			o.deleteObserver(this);
		}
	}
}
