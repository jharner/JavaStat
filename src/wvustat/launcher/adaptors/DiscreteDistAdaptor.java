package wvustat.launcher.adaptors;

import java.awt.Component;
import java.util.Map;
import java.util.Observable;

import wvustat.launcher.ModuleAdaptor;
import wvustat.simulation.discretedist.discreteDist;
/**
 * @author ideal
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DiscreteDistAdaptor extends ModuleAdaptor{

    /* (non-Javadoc)
     * @see wvustat.launcher.ModuleAdaptor#initiateModule(java.util.Map)
     */
	public Component initiateModule(Map initParam) {
		return new discreteDist(initParam);
	}
	
	public void update(Observable o, Object arg){
		if("ModulePaneCleaned".equals(arg) && isInModulePane()){
			setInModulePane(false);
			o.deleteObserver(this);
		}
	}
}
