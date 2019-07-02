package wvustat.modules.diagram;

import java.io.Serializable;
import java.util.*;

/**
 * @author jtan
 *
 * A diagram is a collection of DiagramNode and edges associated with them
 */
public class Diagram extends Observable implements Serializable{
	public static final long serialVersionUID = 1L;
	
	private HashMap nodeMap; //<Integer, DiagramNode>
	private int nextId;
	
	public Diagram(){
		nodeMap = new HashMap(); //<Integer, DiagramNode>
		nextId = 0;
	}

	public boolean containsNode(DiagramNode node){
		return nodeMap.containsValue(node);
	}
	
	public int size() {
		return nodeMap.size();
	}
	
	public Iterator nodeIterator(){
		return nodeMap.values().iterator();
	}
	
	public void addNode(DiagramNode node){
		nodeMap.put(new Integer(node.getId()), node);
		synchronized(this){
			if(node.getId() >= nextId)
				nextId = node.getId() + 1;
		}
		
		setChanged();
		notifyObservers(node);
	}
	
	public DiagramNode getNode(int id){
		return (DiagramNode)nodeMap.get(new Integer(id));
	}
	
	/**
	 * Build an edge between two nodes
	 * @param fromId the id of the node from which the edge begins
	 * @param toId the id of the node to which the edge ends
	 */
	public void connect(int fromId, int toId){
		DiagramNode node1 = getNode(fromId);
		DiagramNode node2 = getNode(toId);
		if(node1 != null && node2 != null){
			DiagramEdge edge = new DiagramEdge(fromId, toId);
			node1.getOutEdges().add(edge);
			node2.getInEdges().add(edge);
			setChanged();
			notifyObservers(edge);
		}
	}
	
	/**
	 * Remove an edge between two nodes
	 * @param fromId the id of the node from which the edge begins
	 * @param toId the id of the node to which the edge ends
	 */
	public void disconnect(int fromId, int toId){
		DiagramNode node1 = getNode(fromId);
		DiagramNode node2 = getNode(toId);
		if(node1 != null && node2 != null){
			DiagramEdge edge = new DiagramEdge(fromId, toId);
			node1.getOutEdges().remove(edge);
			node2.getInEdges().remove(edge);
			setChanged();
			notifyObservers();
		}		
	}
	
	/**
	 * @param node Diagram Node
	 * @return A list of Diagram nodes that have out edges point to the give node.
	 */
	public List getSources(DiagramNode node){
		List nodes = new Vector(); //<DiagramNode>
		
		Iterator it = node.getInEdges().iterator();
		while(it.hasNext()){
			DiagramEdge edge = (DiagramEdge)it.next();
			nodes.add(getNode(edge.getFromId()));
		}
		
		return nodes;
	}
	
	/**
	 * @param node Diagram Node
	 * @return A list of Diagram nodes that have in edges start from the give node.
	 */
	public List getDestinations(DiagramNode node){
		List nodes = new Vector(); //<DiagramNode>
		
		Iterator it = node.getOutEdges().iterator();
		while(it.hasNext()){
			DiagramEdge edge = (DiagramEdge)it.next();
			nodes.add(getNode(edge.getToId()));
		}
		
		return nodes;
	}
	
	/**
	 * @param node1 the start node
	 * @param node2 the end node
	 * @return the distance from node1 to node2. If node2 is not reachable, -1 will be
	 * returned
	 * Notiec: I assume the diagram is acyclic. it will fail if cycle is present
	 */
	public int distance(DiagramNode node1, DiagramNode node2){
		return depthFirstSearch(node1, node2, 0);		
	}
	
	protected int depthFirstSearch(DiagramNode source, DiagramNode target, int d){
		if(source.equals(target))
			return d;
		
		int depth = -1;
		Iterator it = source.getOutEdges().iterator();
		while(it.hasNext()){
			DiagramEdge edge = (DiagramEdge)it.next();
			depth = depthFirstSearch(getNode(edge.getToId()), target, d + 1);
			if(depth >= 0)
				break;
		}
		
		return depth;
	}
	
	public synchronized int nextId(){
		return nextId++;
	}
	
	public void clearSelection(){
		Iterator it = nodeIterator();
		while(it.hasNext()){
			DiagramNode node = (DiagramNode)it.next();
			node.setSelected(false);
		}
	}
}
