package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import wvustat.interfaces.*;
import wvustat.network.*;
import wvustat.network.client.*;
import wvustat.table.MainPanel;
import wvustat.util.ComponentUtil;

public class HeatmapModule extends GUIModule implements ChangeListener, wvustat.modules.heatmap.TreeSelectionListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private Vector vy, vz;
	protected OverlapSlicer chooser;
	protected EqualCountGrouper grouper;
	protected int currentIndex = 0;
	protected HClustModel hcm;
	protected JPanel plot;
	protected Vector indices;
	protected wvustat.modules.heatmap.ClusterMapModel clusterMapModel;
	protected JMenuBar menuBar;
	protected String clusterLinkageMethod = "complete";
	

	protected Set preObsId;
	
	public HeatmapModule(DataSet data, Vector vy, Vector vz) {
		
		this.data = data;
		
		//vy is collection of all y variables. Use y variables to draw the plot.
		this.vy = vy;
		//vz is collection of all z variables. Use z variables to contruct conditioning control.
		this.vz = vz;
		
		if (this.vy.size() < 2)
            throw new IllegalArgumentException("At least two y variables are needed to do heatmap");
	
		if(vz.size() > 0)
			if (grouper == null)
                grouper = new EqualCountGrouper(vz, data);
		this.preObsId = new HashSet();
		init();
	}
	
	private void init() {
		data.addRemoteObserver(this);
		
		setLayout(new BorderLayout());
		
		//Add control panel to this component
        JPanel controlPanel=buildControlPanel();
        if(controlPanel!=null){
        	add(controlPanel, BorderLayout.NORTH);
        }
		
        plot = buildPlotPanel();
		add(plot, BorderLayout.CENTER);
		
		menuBar = new JMenuBar();
		ButtonGroup group = new ButtonGroup();
        JMenu jm = new JMenu("Methods");
		JRadioButtonMenuItem jmi = new JRadioButtonMenuItem("ward");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), false);
        jm.add(jmi);
        
        jmi = new JRadioButtonMenuItem("single");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), false);
        jm.add(jmi);
		
        jmi = new JRadioButtonMenuItem("complete");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), true);
        jm.add(jmi);
        
        jmi = new JRadioButtonMenuItem("average");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), false);
        jm.add(jmi);
        
        jmi = new JRadioButtonMenuItem("mcquitty");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), false);
        jm.add(jmi);
        
        jmi = new JRadioButtonMenuItem("median");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), false);
        jm.add(jmi);
        
        jmi = new JRadioButtonMenuItem("centroid");
		jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), false);
        jm.add(jmi);
        
        menuBar.add(jm);
	}
	
	public JMenuBar getJMenuBar()
	{
		return menuBar;
	}
	
	public JMenu getOptionMenu()
	{
		return null;
	}
	
	protected JPanel buildControlPanel(){
        if(vz.size() == 0)
            return null;
        
        JPanel jp=new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        chooser=new OverlapSlicer(grouper);
        chooser.addChangeListener(this);
        chooser.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        jp.add(chooser);
        
        return jp;
    }

	protected JPanel buildPlotPanel(){
		try {
			ArrayList columns=this.getDataList();
			hcm = JRIClient.hclust(columns, clusterLinkageMethod);
			double[][] hdata = this.getDataArray(columns);
			
			//variables and observations
			wvustat.modules.heatmap.Variable[] var = new wvustat.modules.heatmap.Variable[hdata[0].length];
			wvustat.modules.heatmap.Observation[] obs = new wvustat.modules.heatmap.Observation[hdata.length];
	  
	        for (int i = 0; i < var.length; i++){
	        	var[i] = new wvustat.modules.heatmap.Variable(((Variable)vy.elementAt(i)).getName());   //("var" + Integer.toString(j)); 
	        }
	        for (int j = 0; j < obs.length; j++){
	        	obs[j] = new wvustat.modules.heatmap.Observation("obs" + (((Integer)indices.elementAt(j)).intValue() + 1));  //("obs" + Integer.toString(i));      
	        }
	        
	        // dataset, with double HashMap
	        Map[] obsDataMap  = new HashMap[var.length];
	        Map varObsDataMap = new HashMap();
	        for (int i = 0;i < var.length; i++){
	        	obsDataMap[i] = new HashMap();
	        	for (int j = 0; j < obs.length; j++){
	        		obsDataMap[i].put(obs[j], new Double(hdata[j][i]));
	        	}
	        	varObsDataMap.put(var[i], obsDataMap[i]);
	        }
	        wvustat.modules.heatmap.DataSet dataSet = new wvustat.modules.heatmap.DataSet(varObsDataMap);
	        
	        int[][] varNodeMerge = hcm.varMerge;
	        int[]   varNodeOrder = hcm.varOrder;
	        int[][] obsNodeMerge = hcm.obsMerge;
	        int[]   obsNodeOrder = hcm.obsOrder;
	        
	        wvustat.modules.heatmap.RClusterMapBuilder rClusterMapBuilder = new wvustat.modules.heatmap.RClusterMapBuilder(dataSet, 
	        		obsNodeMerge, obsNodeOrder, varNodeMerge, varNodeOrder);
	      
	        
	        clusterMapModel = rClusterMapBuilder.getClusterMapModel();
		    
	        wvustat.modules.heatmap.ClusterMap clustermap = new wvustat.modules.heatmap.ClusterMap(clusterMapModel,  wvustat.modules.heatmap.HeatMap.GRADIENT_RED_TO_GREEN,
		    		rClusterMapBuilder.obsTreeBuilder(), rClusterMapBuilder.varTreeBuilder());

	        clusterMapModel.getObsSelectionModel().addSelectionListener(this);

	        return clustermap;

		} catch (RemoteException rex) {
			ComponentUtil.showErrorMessage(MainPanel.getDesktopPane(), rex.getMessage());
			return new JPanel();
		}		
	}
	
	public void stateChanged(ChangeEvent ce){
        Object obj=ce.getSource();
        
        if(obj instanceof OverlapSlicer){
            changeGroup();
        }
        
    }
	
	public void changeGroup(){
		
    	currentIndex=chooser.getCurrentIndex();
    	
    	if (plot != null)
    		remove(plot);
    	plot = buildPlotPanel();
		add(plot, BorderLayout.CENTER);
    	validate();
	}
	
	public void update(String arg){
		Set obsId = new HashSet();
		if(arg.equals("yymask")||arg.equals("obs_deleted")){
			if (plot != null)
	    		remove(plot);
	    	plot = buildPlotPanel();
			add(plot, BorderLayout.CENTER);
	    	validate();
		} else if (arg.equals("yystate")) {
			
			for (int i = 0; i < data.getSize(); i++) {
				if (grouper == null) {
					if (data.getState(i) && !data.getMask(i))
						obsId.add("obs" + (i+1));

				}
				else if (grouper.getGroupIndex(i).contains(new Integer(currentIndex))) {
					if (data.getState(i) && !data.getMask(i))
						obsId.add("obs" + (i+1));
				}
			}
		}

	    	this.preObsId = obsId;	    	
		Set nodeSelected = getNodesSelected(obsId);		
			
		if(null != nodeSelected){	    	
		    clusterMapModel.getObsSelectionModel().setSelectedNodes(nodeSelected);	    	
		}		
	}

	private ArrayList getDataList() {
		indices = new Vector();
		ArrayList columns=new ArrayList();
		for (int i = 0; i < vy.size(); i++) {
			ArrayList column = new ArrayList();
			Variable v = (Variable)vy.elementAt(i);
			for (int j = 0; j <v.getSize(); j++) {
				
				if(!data.getMask(j)){
					if(grouper==null){
						column.add(new Double(v.getNumValues()[j]));
						indices.addElement(new Integer(j));
					} else if(grouper.getGroupIndex(j).contains(new Integer(currentIndex))){
						column.add(new Double(v.getNumValues()[j]));
						indices.addElement(new Integer(j));
					}
				}
			}
			columns.add(column);
		}
		return columns;
	}
	
	private double[][] getDataArray(ArrayList columns) {
		int ncols = columns.size();
		int nrows = ((java.util.List)columns.get(0)).size();
		double[][] d = new double[nrows][ncols];
		
		for (int i = 0; i < ncols; i++) {
			java.util.List column = (java.util.List)columns.get(i);
			for (int j = 0; j < nrows; j++) {
				d[j][i] = ((Double)column.get(j)).doubleValue();
			}
		}
		return d;
	}
	
	public void treeSelectionChanged() {
		boolean[] states = new boolean[data.getSize()];
		Arrays.fill(states, false);
		
		if(!clusterMapModel.getObsSelectionModel().getSelectedSet().isEmpty()){
        	java.util.List obsId = new ArrayList();
        	Set selectObsSet = clusterMapModel.getObsSelectionModel().getSelectedSet();
        	
        	
        	Iterator obsIt = selectObsSet.iterator();
        	while(obsIt.hasNext()){
        		wvustat.modules.heatmap.Node nodeSelected = 
        			(wvustat.modules.heatmap.Node)obsIt.next();
        		if(nodeSelected.isleaf()){
        			String obsSelId = ((wvustat.modules.heatmap.ObservationTreeElement)
        					(nodeSelected.getElement())).getObservation().getId();
        			obsId.add(obsSelId);
        		}
        	}
        	for (int i = 0; i < obsId.size(); i++) {
        		String obsSelId = (String)obsId.get(i);
        		int j = Integer.parseInt(obsSelId.substring(3)) - 1;
        		states[j] = true;
        	}
        }
		
		data.setStates(states);
	}
	
	public void actionPerformed(ActionEvent ae)
    {
        String arg = ae.getActionCommand();
        clusterLinkageMethod = arg;
        
        if (plot != null)
    		remove(plot);
    	plot = buildPlotPanel();
		add(plot, BorderLayout.CENTER);
    	validate();
    }
	
	public Set getNodesSelected(Set obsList){
		
		wvustat.modules.heatmap.Node[] obsTreeLeafNodes = clusterMapModel.getObservationTree().getRoot().getAllLeafNodes();
		
		Set obsSelected = new HashSet();
		
		Iterator it = obsList.iterator();
		while(it.hasNext()){
			String obsId = (String)it.next();
			for(int i = 0; i < obsTreeLeafNodes.length; i++){
				if(obsId.equals(((wvustat.modules.heatmap.ObservationTreeElement)obsTreeLeafNodes[i].getElement()).getObservation().getId())){
					obsSelected.add(obsTreeLeafNodes[i]);
				}
			}
		}
		return null == obsSelected ? null : obsSelected;
	}
}
