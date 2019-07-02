package wvustat.modules.heatmap;

import javax.swing.*;
import java.awt.*;
import java.util.*;


class HeatMapFrame extends JFrame
{
    HeatMap myheatmap;
    ClusterMapModel clusterMapModel;
    DataSet dataSet;
    Tree varTree, obsTree;

    public HeatMapFrame() throws Exception
    {
        super("Heat Map Frame");
        
        Variable[] var = new Variable[3];
        Observation[] obs = new Observation[4];
        
        for (int i = 0; i < var.length; i++){
        	var[i] = new Variable(Integer.toString(i));
        }
        for (int j = 0; j < obs.length; j++){
        	obs[j] = new Observation(Integer.toString(j));
        }
        
        double[][] data=new double[var.length][obs.length];
        for (int i=0;i<data.length;i++){
        	for (int j=0;j<data[0].length;j++){
        		// data[i][j]=Math.abs(Math.sin(i*j/data.length));
        		data[i][j] = (i + 1) * (j + 1);
        	}
        }
        
        Map[] obsDataMap  = new HashMap[var.length];
        Map varObsDataMap = new HashMap();
        for (int i=0;i<var.length;i++){
        	obsDataMap[i] = new HashMap();
        	for (int j=0;j<obs.length;j++){
        		obsDataMap[i].put(obs[j], new Double(data[i][j]));
        	}
        	varObsDataMap.put(var[i], obsDataMap[i]);
        }
        
        dataSet = new DataSet(varObsDataMap);
        
        TreeElement[] varelt = new VariableTreeElement[var.length];
        TreeElement[] obselt = new ObservationTreeElement[obs.length];
        for (int i = 0; i < var.length; i++){
        	varelt[i] = new VariableTreeElement(var[i]);
        }
        for (int j = 0; j < obs.length; j++){
        	obselt[j] = new ObservationTreeElement(obs[j]);
        }
        
        // Variable Tree Nodes and Observation Tree Nodes
        Node[] vnode = new Node[var.length];
        Node[] onode = new Node[obs.length];
        for (int i = 0; i < var.length; i++){
        	vnode[i]  = new Node(null, null, varelt[i]);
        }
        
        for (int i = 0; i < var.length; i++){
        	onode[i]  = new Node(null, null, obselt[i]);
        }
        
//        Node vnode1 = new Node(null, null, varelt[0]);
//        Node vnode2 = new Node(null, null, varelt[1]);
//        Node vnode3 = new Node(null, null, varelt[2]);
//        Node vnode4 = new Node(vnode1, vnode2, null);
//        Node vnode5 = new Node(vnode4, vnode3, null);
//        
//        Node onode1 = new Node(null, null, obselt[0]);
//        Node onode2 = new Node(null, null, obselt[1]);
//        Node onode3 = new Node(null, null, obselt[2]);
//        Node onode4 = new Node(null, null, obselt[3]);
//        Node onode5 = new Node(onode1, onode2, null);
//        Node onode6 = new Node(onode3, onode4, null);
//        Node onode7 = new Node(onode5, onode6, null);
        
        varTree = new Tree(vnode[1]);
        obsTree = new Tree(onode[1]);
        
        clusterMapModel = new ClusterMapModel(dataSet, varTree, obsTree);
        
        TreeSelectionModel varSelectionModel = new TreeSelectionModel();
		TreeSelectionModel obsSelectionModel = new TreeSelectionModel();
        // pre-defined gradient:
        myheatmap = new HeatMap(clusterMapModel, HeatMap.GRADIENT_RED_TO_GREEN,
        		varSelectionModel, obsSelectionModel);
        
        // or make a custom gradient:
        /*Color[] gradientColors = new Color[]{Color.blue, Color.green, Color.yellow};
        Color[] customGradient = HeatMap.createMultiGradient(gradientColors, 500);
        myheatmap.updateGradient(customGradient);*/
//
        myheatmap.setColorForeground(Color.black);
        myheatmap.setColorBackground(Color.white);

        this.getContentPane().add(myheatmap);
    }
    
    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception
    {
        HeatMapFrame hmf = new HeatMapFrame();
        hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hmf.setSize(600,600);
        hmf.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    createAndShowGUI();
                }
                catch (Exception e)
                {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }
}
