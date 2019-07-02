package wvustat.modules;

import java.rmi.*;
import java.util.ArrayList;

import javax.swing.*;

import wvustat.interfaces.*;
import wvustat.table.MainPanel;

/**
 * GUIModule is a JPanel that implements the interfaces updatable, RemoteTask and Detachable. It has
 * one abstract method, exportAsTask()
 *
 * @author: Hengyi Xue
 * @version: 1.0, Apr. 11, 2000
 */

public abstract class GUIModule extends JPanel implements RemoteObserver{
    protected DataSet data;
    protected PlotMetaData metaData;
    protected static int instanceCnt = 0;//number of modules 
    protected int moduleId;//from 0 to instanceCnt-1
    protected MainPanel mainPanel;
    protected ArrayList dependentFrames = new ArrayList();

    public GUIModule(){ //added by djluo
    	moduleId = instanceCnt;
    	instanceCnt++;
    }
    
    public static int getInstanceCnt() {
    	return instanceCnt;
    }
    
    public int getModuleId() {
    	return moduleId;
    }
    
    public abstract JMenuBar getJMenuBar();
    
    public abstract JMenu getOptionMenu();

    /**
     * 	Causes a remote observer to update itself
     *
     * 	@msg a message about what has happened
 */
    public void update(String msg) throws RemoteException {
    }
    
    public DataSet getData() {
    	return data;
    }
    
    public void setMetaData(PlotMetaData pmd){
        this.metaData=pmd;
    }
    
    public PlotMetaData getMetaData(){
        return metaData;
    }
    
    public void destroy(){
    	if (data == null) return;//added for microarray
    	
        data.removeRemoteObserver(this);
        data.deleteModule(this.moduleId);//added for missing value
        closeDependentFrames();
    }
    
    public void setMainPanel(MainPanel panel){
    	this.mainPanel = panel;
    }
    
    public MainPanel getMainPanel(){
    	return mainPanel;
    }
    
    public void addDependentFrame(JInternalFrame jif)
    {
        dependentFrames.add(jif);
    }
    
    public void closeDependentFrames()
    {
        for (int i = 0; i < dependentFrames.size(); i++)
        {
            JInternalFrame jif = (JInternalFrame) dependentFrames.get(i);
            jif.dispose();
        }

        dependentFrames.clear();
    }
    
    public ArrayList getDependentFrames()
    {
    	return dependentFrames;
    }
}
