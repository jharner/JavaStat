/*
 * GrapherMenu.java
 *
 * Created on January 29, 2002, 1:46 PM
 */

package wvustat.table;

import java.awt.event.*;
import javax.swing.*;

import java.rmi.RemoteException;
import java.util.Vector;
import java.awt.*;

import wvustat.interfaces.*;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class GraphMenu extends javax.swing.JMenu {
    private JMenuItem yzItem, xyzItem, chartItem, controlItem, paraCoordItem, tourItem, heatmapMI;
    
    
    /** Creates new GrapherMenu */
    public GraphMenu() {
        super("Graph");
                
        yzItem=new JMenuItem("y|z plot");
        xyzItem=new JMenuItem("xy|z plot");        
        chartItem=new JMenuItem("Chart");        
        controlItem=new JMenuItem("Control Chart");
        paraCoordItem = new JMenuItem("Parallel Coordinates");
        tourItem = new JMenuItem("Grand Tour");
        heatmapMI = new JMenuItem("Two way Cluster");
        heatmapMI.setActionCommand("Heatmap");
        
                
        this.add(yzItem);
        this.add(xyzItem);
        this.addSeparator();
        this.add(paraCoordItem);
        this.add(tourItem);
        this.add(heatmapMI);
        this.addSeparator();
        this.add(chartItem);
        this.add(controlItem);    
        
    }
   
    
    
    public void addActionListener(ActionListener listener){
    	yzItem.addActionListener(listener);
    	xyzItem.addActionListener(listener);
    	chartItem.addActionListener(listener);
    	controlItem.addActionListener(listener);
    	paraCoordItem.addActionListener(listener);
    	tourItem.addActionListener(listener);
    	heatmapMI.addActionListener(listener);
    }
    
    
    
    public void setupMenu(DataSet dataSet){ //this method is not used
        
        try{
            Vector vx=dataSet.getXVariables();
            Vector vy=dataSet.getYVariables();
            
            if(vy.size()==0 && vx.size()>0){
                chartItem.setEnabled(true);
            }
            else if(vx.size()>0 && vy.size()>0){
                Variable y0=(Variable)vy.elementAt(0);
                Variable x0=(Variable)vx.elementAt(0);
                yzItem.setEnabled(true);
                xyzItem.setEnabled(true);
                if(x0.getType()==Variable.CATEGORICAL && y0.getType()==Variable.NUMERIC){
                    chartItem.setEnabled(true);
                    controlItem.setEnabled(true);
                }
            }
            else{
                yzItem.setEnabled(true);
                xyzItem.setEnabled(true);
            }
        }
        catch(Exception re){
            re.printStackTrace();
        }
    }
    
    public void updateMenu(DataSet dataSet){ //this method is not used
        yzItem.setEnabled(false);
        xyzItem.setEnabled(false);
        chartItem.setEnabled(false);
        controlItem.setEnabled(false);
        
        try{
            Vector vx=dataSet.getXVariables();
            Vector vy=dataSet.getYVariables();
            
            if(vy.size()>0 && vx.size()==0){
                yzItem.setEnabled(true);
                chartItem.setEnabled(true);
            }
            else if(vx.size()>0 && vy.size()>0){
                Variable y0=(Variable)vy.elementAt(0);
                Variable x0=(Variable)vx.elementAt(0);
                yzItem.setEnabled(true);
                xyzItem.setEnabled(true);
                chartItem.setEnabled(true);
                if(x0.getType()==Variable.CATEGORICAL && y0.getType()==Variable.NUMERIC){
                    controlItem.setEnabled(true);
                }
                
            }
            
        }
        catch(Exception re){
            re.printStackTrace();
        }
    }
    

}
