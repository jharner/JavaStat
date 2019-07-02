package wvustat.modules.heatmap;
/**
 * Definition of the node used in tree structure.
 * @author BoFu
 */

public class Node {
	// define left and right leaf
    private Node left=null; 
    private Node right=null; 

    // define content of Node
    private TreeElement element;
    private int depth;
    // for count dim in getElement()
    private int count = 0;
    
    /**
     * A Node must either have two children and no element, or no children and an element, or an exception would be thrown out.
     * @param left is the left child.
     * @param right is the right child.
     * @param element is a property of leaf nodes.
     */
    public Node(Node left, Node right, TreeElement element)
    { 
    	//initialization
		if (!((left==null && right==null && element!=null) || 
				(left!=null && right!=null && element==null))) {
			throw new IllegalArgumentException("A Node must either have two children and " +
					"no element, or no children and an element");
		}else{
			this.left    = left; 
			this.right   = right; 
			this.element = element;
		}
    }    
    
    public void setLeftChild(Node left)
    {
    	if (null == left && null == this.element){
    		throw new IllegalArgumentException("Left node cannot be null since the element " +
    				"of this node is null");
    	}else{
    		this.left = left;
    	}
    }
    
    public void setRightChild(Node right)
    {
    	if (null == right && null == this.element){
    		throw new IllegalArgumentException("Right node cannot be null since the element " +
    				"of this node is null");
    	}else{
    		this.right = right;
    	}
    }
    
    public void setChild(Node left, Node right)
    {
    	if ((null == right || null == left) && null == this.element){
    		throw new IllegalArgumentException("A Node must either have two children and no " +
    				"element, or no children and an element");
    	}else{
	    	this.left =left;
	    	this.right=right;
    	}
    }
    
    public void setElement(TreeElement element)
    {
    	if (null == element && (null == this.left || null == this.right)){
    		throw new IllegalArgumentException("A Node must either have two children " +
    				"and no element, or no children and an element");
    	}else{
        	this.element=element;
    	}
    }
    
    public TreeElement getElement(){
    	return (this.isleaf() && (null != this.element))?
    			this.element : null;
    }

	public Node getLeftChild()
	{    
		if ((null == this) || (this.isleaf())){
				return null;
		}else{
			return this.left;
		}
	}
	
	public Node getRightChild()
	{
		if ((null == this) || (this.isleaf())){
			return null;
		}else{
			return this.right;
		}
	}
	
	public TreeElement getLeafElement()
	{
		return (this.isleaf())? this.element : null; 
	}
	
	/**
	 * a leaf has depth 1, and plus one when reach one level upper.
	 * @return the integer value of node depth
	 */
	public int getDepth()
	{
		
		return depth=1+Math.max(this.getLeftChild() == null? 
				0: this.getLeftChild().getDepth(),
				this.getRightChild() == null ? 0 : this.getRightChild().getDepth());
	}
		
	/**
	 * An array contains all leaf element belong to this node is got, with leaf elements 
	 * ordered from left to right.
	 * @return TreeElement[] is the array with all elements of the node ordered left to right.
	 */
	public TreeElement[] getElementOrdered()
	{
		if (this.isleaf()){
			return new TreeElement[] {this.element};
		}else{
			TreeElement[] leftNodeElts = this.left.getElementOrdered();
			TreeElement[] rightNodeElts = this.right.getElementOrdered();
            
			TreeElement[] elementArray = new TreeElement[leftNodeElts.length +
			                                             rightNodeElts.length];
			System.arraycopy(leftNodeElts, 0, elementArray, 0, leftNodeElts.length);
			System.arraycopy(rightNodeElts, 0, elementArray, 
					leftNodeElts.length, rightNodeElts.length);
			return elementArray;
		}	
	}
	
	public Node[] getAllLeafNodes(){
		if(this.isleaf()){
			return new Node[]{this};
		}else{
			Node[] leftLeafNodes = this.left.getAllLeafNodes();
			Node[] rightLeafNodes = this.right.getAllLeafNodes();
			
			Node[] nodeArray = new Node[leftLeafNodes.length + 
			                            rightLeafNodes.length];
			
			System.arraycopy(leftLeafNodes, 0, nodeArray, 0, leftLeafNodes.length);
			System.arraycopy(rightLeafNodes, 0, nodeArray, 
					leftLeafNodes.length, rightLeafNodes.length);
			return nodeArray;
		}
	}
	
	public boolean isleaf()
	{
		return this.left == null;
	}
	
    public int getLeafNumber(){
    	if (this == null){
    		return 0;
    	}else{
    		if(this.isleaf()){
    			return 1;
    			}else{
    			return (this.left == null? 0 : this.left.getLeafNumber())+
    			          (this.right == null? 0 : this.right.getLeafNumber());
    			}
    	}
    }
	
    public boolean equals(Object o){
    	
    	if((o == null) || !(o instanceof Node)){
    		return false;
    	}else{
    		Node node = (Node)o;
    		if (this == o) return true;
    		else{ 
    			if (this.isleaf()){
    				return this.getLeafElement() == node.getLeafElement();
    			}else{
    			if ((this.getLeftChild().equals(node.getRightChild())) &&
    					this.getRightChild().equals(node.getRightChild()) &&
    	    			this.getElementOrdered() == node.getElementOrdered()){
    	    		return true;
    			}else{
    				return super.equals(o);
    			}
    			}
    		}
    	}
    }
    
    public int hashCode()
    {
    	int hash = 0;
    	hash = 31 * hash + (null == this.getLeftChild() ? 0 : this.getLeftChild().hashCode());
    	hash = 31 * hash + (null == this.getRightChild() ? 0 : this.getRightChild().hashCode());
    	hash = 31 * hash + (null == this.getLeafElement() ? 0 : this.getLeafElement().hashCode());
    	return hash;
    }
}
