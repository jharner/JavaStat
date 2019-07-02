/*
 * Created on Feb 3, 2006
 */
package wvustat.modules.diagram;

import java.util.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import javax.swing.*;

/**
 * @author jtan
 *
 * DiagramDrawer is responsible for drawing a diagram on a drawable object.
 */
public class DiagramDrawer extends JPanel implements Observer{
	public static final long serialVersionUID = 1L;
	
	public static int HORIZONTAL=0;
	public static int VERTICAL=1;
	
	private Diagram diagram;
	private int dir;
	private int columnGap; //space between two columns
	private int rowGap; //space between two rows
	private Insets margins;
	private NodeRenderer nodeRenderer;
	private List layerList; //<DiagramLayer>
	
	private boolean mouseDragging = false;
	private Rectangle selectionRange = new Rectangle(0,0,0,0);
	
	/**
	 * @param d the diagram to be drawn
	 * @param dir hwo to arrange nodes within the same layer. Possible values are
	 *  DiagramDrawer.HORIZONTAL, DiagramDrawer.VERTICAL
	 */
	public DiagramDrawer(Diagram d, int dir){
		setBackground(Color.white);
		setDirection(dir);

		margins = new Insets(20, 5, 5, 5);
		nodeRenderer = new NodeRenderer();
		layerList = new Vector(); //<DiagramLayer>
		setDiagram(d);
	}
	
	public DiagramDrawer(Diagram d){
		this(d, VERTICAL);
	}

	public Diagram getDiagram(){
		return diagram;
	}
	
	public void setDiagram(Diagram diagram){
		if(this.diagram != null)
			diagram.deleteObserver(this);
		
		this.diagram = diagram;
		if(diagram != null)
			diagram.addObserver(this);
		updateDrawingInfo();
	}
	
	public int getDirection() {
		return dir;
	}
	
	public void setDirection(int dir) {
		this.dir = (dir == VERTICAL)?(VERTICAL):(HORIZONTAL);
		if(this.dir == VERTICAL){
			rowGap = 20;
			columnGap = 30;
		}else{
			rowGap = 30;
			columnGap = 20;
		}
	}
	
	public void updateDrawingInfo(){
		layerList.clear();
		if(diagram == null)
			return;
		
		Set visited = new HashSet(); //<DiagramNode>
		Iterator it = diagram.nodeIterator();
		//compute the layer number, start from source nodes
		while(it.hasNext()){
			DiagramNode node = (DiagramNode)it.next();
			if(node.getInEdges().size() > 0) //ignore, we are looking for source node
				continue;
			
			assignLayer(node, 0, visited);
		}
		
		//create the real layer structure
		it = visited.iterator();
		while(it.hasNext()){
			DiagramNode node = (DiagramNode)it.next();
			while(node.getLayer() >= layerList.size()){
				layerList.add(new DiagramLayer());
			}
			
			DiagramLayer layer = 
				(DiagramLayer)layerList.get(node.getLayer());
			layer.add(node);
		}
		
		
		int distance;
		if (dir == VERTICAL)
			distance = margins.top;
		else
			distance = margins.left;
		
		if (layerList.size() > 0){
			DiagramLayer l = (DiagramLayer)layerList.get(0);
			for (int i = 0; i < l.size(); i++){
				DiagramNode node = (DiagramNode)l.get(i);
				int span = assignLocation(node, distance);
				nodeRenderer.setValue(node, false);
				Component comp = nodeRenderer.getComponent();
				if (dir == VERTICAL)
					node.setTop(distance + (span - rowGap) / 2 - comp.getPreferredSize().height / 2);
				else
					node.setLeft(distance + (span - columnGap) / 2 - comp.getPreferredSize().width / 2);
				
				distance += span;
			}
		}
		
		int width = 0;
		int height = 0;
		for(int i = 0; i < layerList.size(); i++){
			DiagramLayer l = (DiagramLayer)layerList.get(i);
			
			Dimension layerSize = l.computeSize();
			if(dir == VERTICAL){
				width += (layerSize.width + columnGap);
				height = (height < layerSize.height)?(layerSize.height):(height);
			}
			else{
				height += (layerSize.height + rowGap);
				width = (width < layerSize.width)?(layerSize.width):(width);
			}
		}
		
		width += margins.left + margins.right;
		height += margins.top + margins.bottom;
		setPreferredSize(new Dimension(width, height));
		if(isValid())
			repaint();
		else
			revalidate();
	}
	
	protected int assignLocation(DiagramNode node, int distance) {
		Iterator itOutEdge = node.getOutEdges().iterator();
		if (!itOutEdge.hasNext()){
			nodeRenderer.setValue(node, false);
			Component comp = nodeRenderer.getComponent();
			if(dir == VERTICAL){
				node.setTop(distance);
				return comp.getPreferredSize().height + rowGap;
			}else{
				node.setLeft(distance);
				return comp.getPreferredSize().width + columnGap;
			}
		}
		
		int totalSpan = 0;
		while(itOutEdge.hasNext()){
			DiagramEdge edge = (DiagramEdge)itOutEdge.next();
			DiagramNode tNode = diagram.getNode(edge.getToId());
			int span = assignLocation(tNode, distance);
			distance += span;
			totalSpan += span;
		}
		
		nodeRenderer.setValue(node, false);
		Component comp = nodeRenderer.getComponent();
		if (dir == VERTICAL)
			node.setTop(distance - totalSpan + (totalSpan - rowGap) / 2 - comp.getPreferredSize().height / 2);
		else {
			int left = distance - totalSpan + (totalSpan - columnGap) / 2 - comp.getPreferredSize().width / 2;
			node.setLeft(left);
			if (left < distance - totalSpan) {
				shiftRight(node, distance - totalSpan - left);
				totalSpan = comp.getPreferredSize().width + columnGap;
			}
		}
		return totalSpan;
	}
	
	protected void shiftRight(DiagramNode node, int d) {
		node.setLeft(node.getLeft() + d);
		
		Iterator itOutEdge = node.getOutEdges().iterator();
		while(itOutEdge.hasNext()){
			DiagramEdge edge = (DiagramEdge)itOutEdge.next();
			DiagramNode tNode = diagram.getNode(edge.getToId());
			shiftRight(tNode, d);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		int x = margins.left;
		int y = margins.top;
		for(int i = 0; i < layerList.size(); i++){
			int sizeTemp = 0;
			DiagramLayer l = (DiagramLayer)layerList.get(i);
			for(int j = 0; j < l.size(); j++){
				DiagramNode node = l.get(j);
				
				if(dir == VERTICAL)
					node.setLeft(x);
				else
					node.setTop(y);				
				
				if(!node.isDummy()){
					nodeRenderer.setValue(node, node.isSelected());
					Component comp = nodeRenderer.getComponent();
					SwingUtilities.paintComponent(g, comp, this, node.getLeft(), node.getTop(), 
							node.getWidth(), node.getHeight());
					if(node.isSelected())
						g.drawString(node.getLabel(), node.getLeft() + node.getWidth() + 2, node.getTop() - 2);
				}
				if(i > 0)
					drawEdge(g, node, (DiagramLayer)layerList.get(i - 1));
				
				if(dir == VERTICAL){
					if(sizeTemp < node.getWidth())
						sizeTemp = node.getWidth();
				}
				else{
					if(sizeTemp < node.getHeight())
						sizeTemp = node.getHeight();
				}
			}
		
			if(dir == VERTICAL){
				x += (sizeTemp + columnGap);
			}
			else{
				y += (sizeTemp + rowGap);
			}
		
		}
		
		if (mouseDragging) {
            g.setColor(Color.lightGray);
            g.drawRect(selectionRange.x, selectionRange.y, selectionRange.width, selectionRange.height);
        }
	}
	
	public void setMouseDragging(boolean b){
		mouseDragging = b;
	}
	
	public void setSelectionRange(Rectangle rect){
		selectionRange = rect;
	}
	
	public void update(Observable source, Object arg){
		if(diagram.equals(source))
			updateDrawingInfo();
	}
	
	protected void drawEdge(Graphics g, DiagramNode node, DiagramLayer previousLayer){
		Graphics2D g2 = (Graphics2D)g;
		Iterator it = node.getInEdges().iterator();
		while(it.hasNext()){
			DiagramEdge edge = (DiagramEdge)it.next();
			DiagramNode from = diagram.getNode(edge.getFromId());
			if(!previousLayer.contains(from)) //Get the dummy node
				from = previousLayer.getDummy(edge);
			
			GeneralPath path = new GeneralPath();
			GeneralPath arrowHead = null;
			Point start = new Point();
			Point end = new Point();
			Point mid = new Point();
			
			if(dir == HORIZONTAL){
				start.x = from.getLeft() + from.getWidth() / 2;
				start.y = from.getTop() + from.getHeight();
				end.x = node.getLeft() + node.getWidth() / 2;
				end.y = node.getTop();
				path.moveTo(start.x, start.y);
				path.lineTo(end.x, end.y);
				
				if(!node.isDummy())
					arrowHead = getArrowHead(start, end);
			}
			else{
				start.x = from.getLeft() + from.getWidth();
				start.y = from.getTop() + from.getHeight() / 2;
				end.x = node.getLeft();
				end.y = node.getTop() + node.getHeight() / 2;
				mid.x = node.getLeft() - columnGap * 2 / 3;

				if(from.getTop() > end.y)
					mid.y = from.getTop();
				else if(end.y > from.getTop() + from.getHeight())
					mid.y = from.getTop() + from.getHeight();
				else
					mid.y = start.y;
				
				if(!node.isDummy())
					arrowHead = getArrowHead(mid, end);
				
				path.moveTo(start.x, start.y);
				path.lineTo(mid.x, mid.y);
				path.lineTo(end.x, end.y);
			}			
			
			g2.draw(path);
			
			if(arrowHead != null)
				g2.fill(arrowHead);
		}
	}
	
	protected GeneralPath getArrowHead(Point start, Point end){
		float arrowLen = 10;
		float angle = (float)(Math.PI / 9);
		
		GeneralPath arrow = new GeneralPath();
		arrow.moveTo(end.x, end.y);
		
		float dy = start.y - end.y;
		float dx = start.x - end.x;
		float theta = (float)Math.atan2(dy, dx);
		
		float x = end.x + arrowLen * (float)Math.cos(theta + angle);
		float y = end.y + arrowLen * (float)Math.sin(theta + angle);
		arrow.lineTo(x, y);
		
		x = end.x + arrowLen * (float)Math.cos(theta - angle);
		y = end.y + arrowLen * (float)Math.sin(theta - angle);
		
		arrow.lineTo(x, y);
		arrow.lineTo(end.x, end.y);
		return arrow;
	}
	
	protected void assignLayer(DiagramNode node, int layer, 
			Set visited /*<DiagramNode>*/ ){
		if(visited.contains(node) && node.getLayer() >= layer)
			return;
		
		node.setLayer(layer++);
		visited.add(node);
		Iterator it = node.getOutEdges().iterator();
		while(it.hasNext()){
			DiagramEdge outEdge = (DiagramEdge)it.next();
			DiagramNode subNode = diagram.getNode(outEdge.getToId());
			assignLayer(subNode, layer, visited);
		}
	}
	
	class DiagramLayer{
		//private int layerId;
		private List nodes; //<DiagramNode>
		private Map dummyMap; //<DiagramEdge, DiagramNode>
		
		public DiagramLayer(){
			nodes = new Vector(); //<DiagramNode>
			dummyMap = new HashMap(); //<DiagramEdge, DiagramNode>
		}

		public boolean add(DiagramNode n) {
			return nodes.add(n);
		}

		public DiagramNode get(int index){
			return (DiagramNode)nodes.get(index);
		}
		
		public void clear() {
			nodes.clear();
		}

		public boolean contains(DiagramNode n) {
			return nodes.contains(n);
		}

		public int indexOf(DiagramNode n) {
			return nodes.indexOf(n);
		}

		public Object remove(int index) {
			return nodes.remove(index);
		}

		public boolean remove(DiagramNode n) {
			return nodes.remove(n);
		}

		public int size() {
			return nodes.size();
		}
		
		public void across(DiagramEdge edge){
			if(dummyMap.get(edge) == null){
				DiagramNode dNode = 
					new DiagramNode(diagram.nextId(), "", true);
				dNode.getInEdges().add(edge);
				dNode.getOutEdges().add(edge);
				dummyMap.put(edge, dNode);
				nodes.add(dNode);
			}
		}
		
		public DiagramNode getDummy(DiagramEdge edge){
			return (DiagramNode)dummyMap.get(edge);
		}
		
		public Dimension computeSize(){
			Dimension d = new Dimension(0, 0);
			
			for(int j = 0; j < size(); j++){
				DiagramNode node = get(j);
				if(node.isDummy()){
					node.setWidth(0);
					node.setHeight(0);
				}
				else{
					nodeRenderer.setValue(node, false);
					Component comp = nodeRenderer.getComponent();
					node.setWidth(comp.getPreferredSize().width);
					node.setHeight(comp.getPreferredSize().height);
				}
				
				if(dir == VERTICAL){
					if(d.height < node.getTop() + node.getHeight() - margins.top) 
						d.height = node.getTop() + node.getHeight() - margins.top;	
							
					if(d.width < node.getWidth())
						d.width = node.getWidth();
				}
				else{
					if(d.width < node.getLeft() + node.getWidth() - margins.left)
						d.width = node.getLeft() + node.getWidth() - margins.left;
					
					if(d.height < node.getHeight())
						d.height = node.getHeight();
				}
			}
			
			return d;
		}
		
	} //End of the inner class
}
