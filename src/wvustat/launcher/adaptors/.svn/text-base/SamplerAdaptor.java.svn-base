package wvustat.launcher.adaptors;

import java.awt.Component;
import java.util.Map;
import java.util.Observable;

import wvustat.launcher.ModuleAdaptor;
import wvustat.simulation.sampling.Sampler;

public class SamplerAdaptor extends ModuleAdaptor{
	private Sampler sampler;
	
	/* (non-Javadoc)
	 * @see wvustat.launcher.ModuleAdaptor#initiateModule(java.util.Map)
	 */
	public Component initiateModule(Map initParam) {
		sampler = new Sampler(initParam);
		return sampler;
	}
	
	public void update(Observable o, Object arg){
		if("ModulePaneCleaned".equals(arg) && isInModulePane()){
			setInModulePane(false);
			o.deleteObserver(this);
		}
	}
}
