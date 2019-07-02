package wvustat.modules.heatmap;
/**
 * Used for updating the heatmap model when data changed
 * @author BoFu
 *
 */
public interface HeatMapModelListener {

	public void dataChanged(ClusterMapModel clusterMapModel);
	
}
