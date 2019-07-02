package wvustat.modules.heatmap;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;
import java.util.List;

public class TreeTest extends JFrame implements TreeSelectionListener{

	private TreeDiagram treediagram;
	private TreeSelectionModel selectionmodel;
	private ClusterMapModel clusterMapModel;
	
	public TreeTest(String string){
		super(string);
		setSize(500,500);
		setLocation(100,100);
		
		double[][] data = new double[][]{{1.0370576, 1.6128100, 0.6499660}, 
				{0.2230880, -0.1792011, 1.0767967}, {-0.1386763, -0.7406979, -1.0024183}, 
				{-1.6158805, -0.8840588, -1.1207476}, {0.4944112, 0.1911479, 0.3964032}};

		// variables and observations
		Variable[] var = new Variable[data.length];
        Observation[] obs = new Observation[data[0].length];
  
        for (int i = 0; i < var.length; i++){
        	var[i] = new Variable("var" + Integer.toString(i));
        }
        for (int j = 0; j < obs.length; j++){
        	obs[j] = new Observation("obs" + Integer.toString(j));
        }
        
        // dataset, with double HashMap
        Map[] obsDataMap  = new HashMap[var.length];
        Map varObsDataMap = new HashMap();
        for (int i = 0;i < var.length; i++){
        	obsDataMap[i] = new HashMap();
        	for (int j = 0; j < obs.length; j++){
        		obsDataMap[i].put(obs[j], new Double(data[i][j]));
        	}
        	varObsDataMap.put(var[i], obsDataMap[i]);
        }
        DataSet dataSet = new DataSet(varObsDataMap);
        
        // simulate order and merge matrix for both observation and variable trees
        int[][] varNodeMerge = new int[][]{{-2, -5}, {-3, -4}, {-1, 1}, {2, 3}};
        int[]   varNodeOrder = new int[]{3, 4, 1, 2, 5};
        int[][] obsNodeMerge = new int[][]{{-1, -2}, {-3,1}};
        int[]   obsNodeOrder = new int[]{3, 1, 2};
        
        RClusterMapBuilder rClusterMapBuilder = new RClusterMapBuilder(dataSet, 
        		obsNodeMerge, obsNodeOrder, varNodeMerge, varNodeOrder);
      
        
        this.clusterMapModel = rClusterMapBuilder.getClusterMapModel();
	    
        clusterMapModel.getObsSelectionModel().addSelectionListener(this);
        
	    ClusterMap clustermap = new ClusterMap(clusterMapModel,  HeatMap.GRADIENT_RED_TO_GREEN,
	    		rClusterMapBuilder.obsTreeBuilder(), rClusterMapBuilder.varTreeBuilder());
	    
	    Tree obsTree = clusterMapModel.getObservationTree();
	    
	    List obsId = new ArrayList();
	    obsId.add("obs0");
	    obsId.add("obs2");
	    /**************************************/
	    List nodeSelected = getNodesSelected(obsId);
	    
	    if(null != nodeSelected){
	    	clusterMapModel.getObsSelectionModel().clearSelection();
	    	
	    	Iterator it = nodeSelected.iterator();
	    	while(it.hasNext()){
	    		clusterMapModel.getObsSelectionModel().selectNode((Node)it.next());
	    	}
	    }
        /**************************************/
        this.getContentPane().add(clustermap);
		setVisible(true);
	}
	
	public static void main(String[] args) {

	    TreeTest treetest = new TreeTest("treetest");
	    
	    treetest.addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		System.exit(0);
	    	}
	    });
	}
	
	public void treeSelectionChanged(){
		
		if(!clusterMapModel.getObsSelectionModel().getSelectedSet().isEmpty()){
	    	
        	List obsId = new ArrayList();
        	Set selectObsSet = clusterMapModel.getObsSelectionModel().getSelectedSet();
        	Iterator obsIt = selectObsSet.iterator();
        	while(obsIt.hasNext()){
        		Node nodeSelected = (Node)obsIt.next();
        		if(nodeSelected.isleaf()){
        			String obsSelId = ((ObservationTreeElement)(nodeSelected.getElement())).getObservation().getId();
        			obsId.add(obsSelId);
        		}
        	}
        	Iterator it = obsId.iterator();
        	while(it.hasNext()){
        		System.out.println((String)it.next());
        	}
        }else{
        	System.out.println("nothing selected");
        }
		repaint();
	}
	
	public List getNodesSelected(List obsList){
		
		Node[] obsTreeLeafNodes = clusterMapModel.getObservationTree().getRoot().getAllLeafNodes();
		
		List obsSelected = new ArrayList();
		
		Iterator it = obsList.iterator();
		while(it.hasNext()){
			String obsId = (String)it.next();
			for(int i = 0; i < obsTreeLeafNodes.length; i++){
				if(obsId.equals(((ObservationTreeElement)obsTreeLeafNodes[i].getElement()).getObservation().getId())){
					obsSelected.add(obsTreeLeafNodes[i]);
				}
			}
		}
		return null == obsSelected ? null : obsSelected;
	}
}

