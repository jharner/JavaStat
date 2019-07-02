package wvustat.modules.heatmap;
import java.util.HashMap;
import java.util.Map;

/**
 * Build the cluster map from information get from R in server.
 * @author BoFu
 */

public class RClusterMapBuilder {

	private DataSet dataSet;
	private int[] obsNodeOrder, varNodeOrder;
	private int[][] obsNodeMerge, varNodeMerge;
	
	/**
	 * Interface for drawing a heatmap. Every parameter in construct function cannot be null.
	 * @param dataSet the data set be analyzed;
	 * @param dataMerge the cluster way get from R, by command hclust;
	 * @param dataOrder the order get from R;
	 * @param orientation horizontal and vertical represent observation tree and variable tree, separately;
	 */
	public RClusterMapBuilder(DataSet dataSet, int[][] obsNodeMerge, int[] obsNodeOrder, 
			int[][] varNodeMerge, int[] varNodeOrder){
		if (null == dataSet || null == obsNodeOrder || null == obsNodeMerge
				|| null == varNodeOrder || null == varNodeMerge){
			throw new IllegalArgumentException(
					"A tree cannot be built without data, order and merge methods");
		}else{
			this.dataSet = dataSet;
			this.obsNodeMerge = obsNodeMerge;
			this.obsNodeOrder = obsNodeOrder;
			this.varNodeMerge = varNodeMerge;
			this.varNodeOrder = varNodeOrder;
		}
	}
	
	public Tree obsTreeBuilder(){
		
		Node[] tempNodes = new Node[this.obsNodeMerge.length];

		for(int i = 0; i < tempNodes.length; i++){
			int leftInt = this.obsNodeMerge[i][0];
			int rightInt = this.obsNodeMerge[i][1];
			Node leftNode, rightNode;

			if(leftInt < 0){
				TreeElement leftNodeElement = 
					new ObservationTreeElement(this.dataSet.getObservations()[-leftInt - 1]);
				leftNode = new Node(null, null, leftNodeElement);
			}else{
				leftNode = tempNodes[leftInt - 1];
			}
			
			if(rightInt < 0){
				TreeElement rightNodeElement = 
					new ObservationTreeElement(this.dataSet.getObservations()[-rightInt - 1]);
				rightNode = new Node(null, null, rightNodeElement);
			}else{
				rightNode = tempNodes[rightInt - 1];
			}
			
			tempNodes[i] = new Node(leftNode, rightNode, null);	
		}
		
		return new Tree(tempNodes[tempNodes.length - 1]);
	}
	
	public Tree varTreeBuilder(){
		
		Node[] tempNodes = new Node[this.varNodeMerge.length];
		
		for(int i = 0; i < tempNodes.length; i++){
			int leftInt = this.varNodeMerge[i][0];
			int rightInt = this.varNodeMerge[i][1];
			Node leftNode, rightNode;

			if(leftInt < 0){
				TreeElement leftNodeElement = 
					new VariableTreeElement(this.dataSet.getVariables()[-leftInt - 1]);
				leftNode = new Node(null, null, leftNodeElement);
			}else{
				leftNode = tempNodes[leftInt - 1];
			}
			
			if(rightInt < 0){
				TreeElement rightNodeElement = 
					new VariableTreeElement(this.dataSet.getVariables()[-rightInt - 1]);
				rightNode = new Node(null, null, rightNodeElement);
			}else{
				rightNode = tempNodes[rightInt - 1];
			}
			
			tempNodes[i] = new Node(leftNode, rightNode, null);	
		}
		
		return new Tree(tempNodes[tempNodes.length - 1]);
	}
	
	public ClusterMapModel getClusterMapModel(){
		Tree obsTree = this.obsTreeBuilder();
		Tree varTree = this.varTreeBuilder();

		return new ClusterMapModel(this.dataSet, varTree, obsTree);
	}
}

