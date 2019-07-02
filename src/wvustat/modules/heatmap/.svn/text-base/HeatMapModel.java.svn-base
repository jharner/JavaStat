package wvustat.modules.heatmap;
/**
 * A model for updating heatmap when the data changed.
 * @author BoFu
 *
 */
public class HeatMapModel implements HeatMapModelListener{

	HeatMap heatmap;
	
	public void dataChanged(ClusterMapModel clusterMapModel)
	{
		// repaint
		heatmap.updateData(clusterMapModel);
	}
}
