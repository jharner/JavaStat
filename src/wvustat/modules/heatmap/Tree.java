package wvustat.modules.heatmap;
/** 
 * This class define the a tree. 
 * @author BoFu
 *
 */

public class Tree 
{
	private Node root;
	
	/**
	 * Tree should have a root. If the node is equals to null, an exception would be thrown out.
	 * @param root: the root node of the tree
	 */
	public Tree(Node root){
		if (root == null) {
			throw new IllegalArgumentException("A tree should have a root");
		}else{
			this.root = root; 
		}
	}
	
	public void setRoot(Node node) {
		if (node == null) {
			throw new IllegalArgumentException("A tree should have a root not null");
		}else{
			this.root = node; 
		}
	}
	
	public Node getRoot(){
		return this.root;
	}
	
	public Node getParent(Node n) {
		return ((n == null) || (n.equals(root)))? null : seekParent(root, n);
	}

	private Node seekParent(Node possibleparent, Node n){
		if (possibleparent.isleaf()){
			return null;
		}else{
			if (possibleparent.getLeftChild().equals(n) || possibleparent.getRightChild().equals(n)){
				return possibleparent;
			}else{
				return seekParent(possibleparent.getLeftChild(), n) == null ? 
						seekParent(possibleparent.getRightChild(), n) : 
							seekParent(possibleparent.getLeftChild(), n);
			}
		}
	}
}
