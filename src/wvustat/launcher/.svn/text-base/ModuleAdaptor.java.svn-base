package wvustat.launcher;

import java.util.Observable;
import java.util.Observer;
import java.util.Map;
import java.awt.Component;
/**
 * @author Jun Tan
 * The ModuleAdaptor class is used to integrate other objects into myJavaStat
 * easily. Initial parameters can be sent to the adaptor through a Map.
 * The change of state in XLauncher will be sent to ModuleAdaptor by calling
 * the update method.
 */
public abstract class ModuleAdaptor implements Observer{
	private boolean inModulePane = false;
	
	public abstract Component initiateModule(Map initParam);
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		//By default the adaptor will do nothing
	}
	
	protected void setInModulePane(boolean bv){
		inModulePane = bv;
	}
	
	public boolean isInModulePane(){
		return inModulePane;
	}
}
