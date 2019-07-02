package wvustat.modules.heatmap;
/**
 * A view of tree structure. Used to Construct observation tree and variable tree.
 * @author BoFu
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import javax.swing.*;

public class TreeDiagram extends JPanel implements TreeSelectionListener{

	private static final long serialVersionUID = 1L;

	private TreeMetricsModel treeMetricsModel;
	private Orientation orientation = Orientation.HORIZONTAL;
    
	private int stringLength; // the maximum displayed length of observation/variable id.
	private int elementId;
    private Map nodeToPointMap ;
	private Color bg = Color.white;
    private Color fg = Color.black;

    private Tree myTree;
    private TreeSelectionModel selectionModel;

    /**
     * A view of tree structure. Used to Construct observation tree and variable tree.
     * @param myTree is the tree been processing.
     * @param metricsModel contains the information of the stem length and element width.
     * @param selectionModel provides the nodes selected.
     * @param orientation notifys the tree type, variable tree or observation tree, with 
     * two value, VERTICAL and HORIZONTAL.
     */

    public TreeDiagram(Tree myTree, TreeMetricsModel metricsModel,
    		TreeSelectionModel selectionModel, Orientation orientation)
    {
    	nodeToPointMap = new HashMap();
    	if (orientation != Orientation.HORIZONTAL && orientation != Orientation.VERTICAL){
    		throw new IllegalArgumentException(
    				"the orientation should either equals to Horizontal or Vertical");
    	}else{
    		this.orientation = orientation;
    		this.myTree = myTree ;
    		this.treeMetricsModel = metricsModel ;
    		this.selectionModel = selectionModel ;

			this.bg=Color.white;
			this.fg=Color.black;
			
			this.addMouseListener(new SelectionMouseListener());			
			this.stringLength = calculateStringLength();
    	}
    	selectionModel.addSelectionListener(this);
    	ToolTipManager.sharedInstance().registerComponent(this);
	}

    /**
     * A view of tree structure. Used to Construct observation tree and variable tree.
     * @param tree is the tree been processing.
     * @param metricsModel metricsModel contains the information of the stem length and element width.
     * @param selectionModel selectionModel provides the nodes selected.
     */
    public TreeDiagram(Tree tree, TreeMetricsModel metricsModel, TreeSelectionModel selectionModel)
    {
    	this(tree, metricsModel, selectionModel, Orientation.HORIZONTAL);
	}

    public void setStringLength(int length){
    	this.stringLength = length;
    }
    
    public int getStringLength(){
    	return this.stringLength;
    }

    public void setTreeMetricsModel(TreeMetricsModel treeMetricsModel){
    	this.treeMetricsModel = treeMetricsModel;
    }

    public TreeMetricsModel getMetricsModel(){
    	return this.treeMetricsModel;
    }

    // Updates the foreground color. Calls repaint() when finished.
    public void setColorForeground(Color fg)
    {
        this.fg = fg;
        repaint();
    }

    // Updates the background color. Calls repaint() when finished.
    public void setColorBackground(Color bg)
    {
        this.bg = bg;
        repaint();
    }
    
    private int calculateStringLength(){
		int sLength = 0;
		TreeElement[] treeElement = myTree.getRoot().getElementOrdered();
		if (orientation == Orientation.HORIZONTAL){
			ObservationTreeElement[] obsElt= new ObservationTreeElement[treeElement.length];
			obsElt[0] = (ObservationTreeElement)treeElement[0];
			for (int i = 1; i < obsElt.length; i++){
				obsElt[i] = (ObservationTreeElement)treeElement[i];
				sLength = Math.max(obsElt[i].getObservation().getId().length(), 
						obsElt[i - 1].getObservation().getId().length());
			}			
		}else{
			VariableTreeElement[] varElt= new VariableTreeElement[treeElement.length];
			varElt[0] = (VariableTreeElement)treeElement[0];
			for (int i = 1; i < varElt.length; i++){
				varElt[i] = (VariableTreeElement)treeElement[i];
				sLength = Math.max(varElt[i].getVariable().getId().length(), 
						varElt[i - 1].getVariable().getId().length());
			}
		}
		return Math.min(sLength * 7 + 14, 70);
    }
    
    // compute the coordinates for nodes and put all nodes and their corresponding points into a hashmap.
    private Point computePoints(Node node, int nodesLeftOfBranch){

    	int x ;
        if (node.isleaf()) {
			x = nodesLeftOfBranch * treeMetricsModel.getElementWidth() +
			(int)((double)treeMetricsModel.getElementWidth()/2) ;
		} else {
			Point leftP  = computePoints(node.getLeftChild(), nodesLeftOfBranch) ;
			Point rightP = computePoints(node.getRightChild(), nodesLeftOfBranch +
					node.getLeftChild().getElementOrdered().length);
			x = (leftP.x + rightP.x) / 2;
		}

	    int y = (myTree.getRoot().getDepth()-node.getDepth() + 1) * 
	    	treeMetricsModel.getStemLength();

	    nodeToPointMap.put(node, new Point(x, y));
        return new Point(x, y);
    }
    

	private void drawNode(Node node, Graphics2D g){

		if (null != node){
			if (selectionModel.getSelectedSet().contains(node)){
				g.setColor(Color.red);
			}else{
				g.setColor(fg);
			}
			Point nPoint = (Point)nodeToPointMap.get(node);
			if (!node.isleaf()){
				Point leftPoint = (Point)nodeToPointMap.get(node.getLeftChild());
				Point rightPoint = (Point)nodeToPointMap.get(node.getRightChild());

				if (node == myTree.getRoot()){
					g.drawLine(nPoint.x, nPoint.y, nPoint.x, nPoint.y - treeMetricsModel.getStemLength());
				}else{
					Point parentPoint = (Point)nodeToPointMap.get(myTree.getParent(node));
					g.drawLine(nPoint.x, nPoint.y, nPoint.x, parentPoint.y);
				}
				g.drawLine(leftPoint.x, leftPoint.y, leftPoint.x,
						Math.min(leftPoint.y, rightPoint.y) - treeMetricsModel.getStemLength());
				g.drawLine(leftPoint.x, Math.min(leftPoint.y, rightPoint.y) - treeMetricsModel.getStemLength(),
						rightPoint.x, Math.min(leftPoint.y, rightPoint.y) - treeMetricsModel.getStemLength());
				g.drawLine(rightPoint.x, Math.min(leftPoint.y, rightPoint.y) - treeMetricsModel.getStemLength(),
						rightPoint.x, rightPoint.y);

				drawNode(node.getLeftChild(), g);
				drawNode(node.getRightChild(), g);
			}else{
				Point parentPoint = (Point)nodeToPointMap.get(myTree.getParent(node));
				g.drawLine(nPoint.x, nPoint.y, nPoint.x, parentPoint.y);

				FontMetrics fontMetrics = g.getFontMetrics();
				int height = fontMetrics.getHeight();
				
				String elementId = node.getElement().toString();
				if((elementId.length() * 7 + 14) > 70){
					elementId = elementId.substring(0, 6) + "...";
				}
				
				if (orientation == Orientation.HORIZONTAL){
					g.rotate(Math.toRadians(90));				
					g.drawString(elementId, nPoint.y + 3, -(nPoint.x - height/4));
					g.rotate(-Math.toRadians(90));
				}else{
					
					g.rotate(Math.toRadians(90));	
					g.scale(1.0, -1.0);
					g.drawString(elementId, nPoint.y + 3, (nPoint.x + height/4));
					g.scale(1.0, -1.0);
					g.rotate(-Math.toRadians(90));
					
				}
			}
		}else {return;}
	}

	private class SelectionMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e){
			Node node = null;

            Point p = (orientation == Orientation.VERTICAL)?
            		new Point(e.getPoint().y,e.getPoint().x) : e.getPoint();

			node = detectPoint(myTree.getRoot(), p);

			if (node != null){
				if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK){
					selectionModel.selectNode(node);
				}else{
					selectionModel.clearSelection();
				    selectionModel.selectNode(node);
				}
			}else{
				selectionModel.clearSelection();
			}
		}
	}

    /**
     * Detect the which node selected by the mouse click.
     * @param node, usually is the root of the tree.
     * @param p is the point got by mouse click.
     * @return the node detected, return null if no rode selected.
     */
	private Node detectPoint(Node node, Point p){

		Point nPoint     = (Point)nodeToPointMap.get(node);
		if (false == node.isleaf()){
			Point leftPoint   = (Point)nodeToPointMap.get(node.getLeftChild());
			Point rightPoint  = (Point)nodeToPointMap.get(node.getRightChild());
			// vertical rectangle: two case, 1) if node is root; 2) if not but not leaf either.
			Rectangle verticalrect;

			if (node == myTree.getRoot()){
				verticalrect   = new Rectangle(nPoint.x - 5,
						nPoint.y - treeMetricsModel.getStemLength(), 10, treeMetricsModel.getStemLength() - 5);
			}else{
				Point parentPoint = (Point)nodeToPointMap.get(myTree.getParent(node));
				verticalrect   = new Rectangle(nPoint.x - 5,
						parentPoint.y + 3, 10, nPoint.y - parentPoint.y - 8);
				}
			// horizontal rectangle is the same for node (not leaf).
			Rectangle horizontalrect = new Rectangle(leftPoint.x - 3,
					nPoint.y - 5, rightPoint.x - leftPoint.x + 6, 8);
			// see if the rectangles contain the point.
			if (verticalrect.contains(p) || horizontalrect.contains(p)){
				return node;
			}else{
				return (detectPoint(node.getLeftChild(), p) == null) ?
						detectPoint(node.getRightChild(), p) :
							detectPoint(node.getLeftChild(), p);
			}

		}else{
			Point parentPoint = (Point)nodeToPointMap.get(myTree.getParent(node));
			// if leaf, there is only vertical rectangle.
			Rectangle verticalrect = new Rectangle(nPoint.x - 5,
					parentPoint.y + 3, 10, nPoint.y - parentPoint.y - 3 + this.stringLength);
			// see if the rectangles contain the point.
			if (verticalrect.contains(p)) {
				return node;
			}
			else{
				return null;
			}
		}
	}

	public void paintComponent(Graphics g1){

		Graphics2D g = (Graphics2D)g1;

		g.setColor(bg);
		g.fillRect(0,0,this.getSize().width, this.getSize().height);

		computePoints(myTree.getRoot(), 0);

		g.setColor(fg);

		if(orientation == Orientation.VERTICAL){	
			g.translate(this.getSize().width/2, this.getSize().height/2);		
			g.scale(-1.0, 1.0);
			g.rotate(Math.toRadians(90));
			g.translate(-this.getSize().height/2, -this.getSize().width/2);
			
		}

		if (null!=myTree.getRoot()){
    		drawNode(myTree.getRoot(), g);
    	}

    	if (orientation == Orientation.VERTICAL) {
    		
			g.translate(this.getSize().height/2, this.getSize().width/2);
			g.rotate(-Math.toRadians(90));
			g.scale(-1.0, 1.0);
			g.translate(-this.getSize().width/2, -this.getSize().height/2);
		}
	}

	public void treeSelectionChanged() {
		repaint();
	}

	public Dimension getPreferredSize() {
		int width = treeMetricsModel.getElementWidth() * myTree.getRoot().getElementOrdered().length;
		int height = treeMetricsModel.getStemLength() * myTree.getRoot().getDepth() + stringLength;
		if (orientation == Orientation.HORIZONTAL) {
			return new Dimension(width, height);
		} else {
			return new Dimension(height, width);
		}
	}
	
	public String getToolTipText (MouseEvent e){
		
		Point p = (orientation == Orientation.VERTICAL)?
        		new Point(e.getPoint().y,e.getPoint().x) : e.getPoint();
        TreeElement[] treeElement = this.myTree.getRoot().getElementOrdered();
        for (int i = 0; i < this.myTree.getRoot().getLeafNumber(); i++){
        	Rectangle rect = new Rectangle(i * treeMetricsModel.getElementWidth() + 
        			(int)((double)treeMetricsModel.getElementWidth()/2) - 5,
        			this.myTree.getRoot().getDepth() * (treeMetricsModel.getStemLength() - 1), 
        			10, treeMetricsModel.getStemLength() - 3 + this.stringLength);

        	if (rect.contains(p)) {      		
        		return "<html>" + treeElement[i].toString();
        	}
        }
        return null;
	}
}

class Orientation{
    private final String name;

    private Orientation(String name) { this.name = name; }

    public String toString()  { return name; }

    public static final Orientation HORIZONTAL = new Orientation("Horizontal");
    public static final Orientation VERTICAL = new Orientation("Vertical");
}