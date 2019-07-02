package wvustat.modules.heatmap;

import java.awt.*;
import javax.swing.*;

/**
 * A view of heatmap, combine the tree structure and color map in the middle.
 * @author BoFu
 *
 */

public class ClusterMap extends JPanel implements TreeSelectionListener{

	private JScrollPane scroller;
	
	/**
	 * A view of the heat map for two way clustering.
	 * @param clusterMapModel contains all the information needed for drawing the heatmap, 
	 *        including observation and variable tree structure, nodes selection etc. 
	 * @param colors is the series of color used in color map.
	 * @param obsTree is the observation tree.
	 * @param varTree is the variable tree.
	 */
	public ClusterMap(ClusterMapModel clusterMapModel, Color[] colors,
			Tree obsTree, Tree varTree){

		HeatMap hMap = new HeatMap(clusterMapModel, colors, clusterMapModel.getVarSelectionModel(), 
				clusterMapModel.getObsSelectionModel());
		this.scroller = new JScrollPane(hMap);

		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		TreeDiagram obsTreeDiag = new TreeDiagram(obsTree, 
				clusterMapModel.getObservationTreeMetricsModel(), 
				clusterMapModel.getObsSelectionModel());
		TreeDiagram varTreeDiag = new TreeDiagram(varTree, 
				clusterMapModel.getVariableTreeMetricsModel(), 
				clusterMapModel.getVarSelectionModel(), Orientation.VERTICAL);
		

		this.scroller.setColumnHeaderView(obsTreeDiag);
		this.scroller.setRowHeaderView(varTreeDiag);

		this.setLayout(new BorderLayout());
		this.add(this.scroller, BorderLayout.CENTER);

		clusterMapModel.getVarSelectionModel().addSelectionListener(this);
		clusterMapModel.getObsSelectionModel().addSelectionListener(this);
	}

	public void treeSelectionChanged(){
		repaint();
	}
}
