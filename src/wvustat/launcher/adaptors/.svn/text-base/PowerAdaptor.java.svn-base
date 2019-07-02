package wvustat.launcher.adaptors;

import java.awt.Component;
import java.util.Map;
import java.util.Observable;

import wvustat.launcher.ModuleAdaptor;
import wvustat.simulation.power.Power;

public class PowerAdaptor extends ModuleAdaptor{
	private Power power;
	
	/* (non-Javadoc)
	 * @see wvustat.launcher.ModuleAdaptor#initiateModule(java.util.Map)
	 */
	public Component initiateModule(Map initParam) {
		power = new Power(initParam);
		return power;
	}
	
	public void update(Observable o, Object arg){
		if("ModulePaneCleaned".equals(arg) && isInModulePane()){
			setInModulePane(false);
			o.deleteObserver(this);
		}
	}
}
