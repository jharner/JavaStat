package wvustat.modules.diagram;

import java.util.*;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 *
 * @author jtan
 *
 * This object represent a node in complex use/used-by diagram exist
 * among modules, cognitive attributes. In such a diagram, one node 
 * can be used by one or more node. At the same time the same node 
 * can also use one or more other nodes. All the nodes together form 
 * a acyclic diagram.
 */
public class DiagramNode implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String label;
    private int layer;
    private boolean dummy;
    private boolean selected;
    
    private HashSet inEdges; //<DiagramEdge>
    private HashSet outEdges; //<DiagramEdge>

    private transient int weight = 0; //used for cross reduction
    private transient int top = 0;
    private transient int left = 0;
    private transient int width = 0;
    private transient int height = 0;
    
    /** Creates a new instance of DiagramNode */
    DiagramNode(int id, String name, boolean dummy){
        this.id = id;
        this.name = name;
        this.dummy = dummy;
        
        selected = false;
        inEdges = new HashSet(); //<DiagramEdge>
        outEdges = new HashSet(); //<DiagramEdge>
    }

    /** Creates a non-dummy DiagramNode */
    public DiagramNode(int id, String name){
	this(id, name, false);
    }

    public int hashCode(){
	return id;
    }

    public boolean equals(Object obj){
	if(obj != null && obj instanceof DiagramNode)
	    return ((DiagramNode)obj).getId() == id;

	return false;
    }
    
    public String toString(){
    	return name;
    }
    
    public String getLabel(){
    	return label;
    }
    
    public void setLabel(String label){
    	this.label = label;
    }

    public int getLayer() {
	return layer;
    }

    void setLayer(int layer) {
	this.layer = layer;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public boolean isDummy() {
	return dummy;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

	HashSet/*<DiagramEdge>*/ getInEdges() {
		return inEdges;
	}

	HashSet/*<DiagramEdge>*/ getOutEdges() {
		return outEdges;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getLeft() {
		return left;
	}
	
	public void setLeft(int left) {
		this.left = left;
	}
	
	public int getTop() {
		return top;
	}
	
	public void setTop(int top) {
		this.top = top;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(left, top, width, height);
	}
}
