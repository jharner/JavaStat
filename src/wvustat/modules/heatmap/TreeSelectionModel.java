package wvustat.modules.heatmap;
import java.util.*;

/**
 * This model put selected nodes into a set, and synchronize views on observation.
 * @author BoFu
 */

public class TreeSelectionModel {

	private Set selectedNodes;
	private List listeners ;
	private Set preSelectedNodes;

	/**
	 * This model put selected nodes into a set, and synchronize views on observation.
	 * and variable selection.
	 */
	public TreeSelectionModel(){

		this.selectedNodes = new HashSet();
		this.listeners     = new ArrayList();
		this.preSelectedNodes = new HashSet();
	}

	public boolean isNodeSelected (Node node){
		return selectedNodes.contains(node);
	}

	public Set getSelectedSet(){
		return Collections.unmodifiableSet(this.selectedNodes);
	}

	public void selectNode(Node node){

			selectedNodes.add(node);
			if (node != null) {
				selectedNodes.add(node);
				if (false == node.isleaf()) {
					selectedNodes.add(node.getLeftChild());
					selectedNodes.add(node.getRightChild());
					selectNode(node.getLeftChild());
					selectNode(node.getRightChild());
				}
			} else return;
		fireSelectionChanged();
	}
	

	public void deSelectNode(Node node){

		if (selectedNodes.contains(node)) {
			selectedNodes.remove(node);
			if (false == node.isleaf()) {
				selectedNodes.remove(node.getLeftChild());
				selectedNodes.remove(node.getRightChild());
			}
			fireSelectionChanged();
		}
	}

	public void addSelectionListener(TreeSelectionListener listener) {
		listeners.add(listener);
	}

	public void removeSelectionListener(TreeSelectionListener listener) {
		listeners.remove(listener);
	}

	public void clearSelection(){
		if (! selectedNodes.isEmpty()){
			selectedNodes.clear();
			fireSelectionChanged();
		}
	}
	
	public void setSelectedNodes(Collection nodes) {
		Set newNodes = new HashSet(nodes);
		if( ! selectedNodes.equals(nodes)){
			selectedNodes = newNodes ;
			fireSelectionChanged();
		}
	}

	private void fireSelectionChanged() {
		//if (!this.preSelectedNodes.equals(selectedNodes)){
			preSelectedNodes = selectedNodes;
			Iterator it = listeners.iterator();
			while(it.hasNext()) {
				TreeSelectionListener listener = (TreeSelectionListener) it.next();
				listener.treeSelectionChanged();
			}
		//}else return;
	}
	
}
