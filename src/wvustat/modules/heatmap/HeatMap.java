package wvustat.modules.heatmap;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.text.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.Color;

/**
 * A view for the color map in the middle. Convert all the data values to pixels for visualization.
 * @author BoFu
 *
 */

public class HeatMap extends JPanel implements HeatMapModelListener, Scrollable{
	private ClusterMapModel clusterMapModel;
	private Set selectPoint;

    private int[][] dataColorIndices;
    
    private TreeSelectionModel varSelectionModel, obsSelectionModel;
	TreeElement[] obsElement;
	TreeElement[] varElement;
	
    // colors
    private Color[] colors;
    private Color bg = Color.white;
    private Color fg = Color.black;

    // Produces a gradient from one color(low value) to the other(high value)
    public final static Color[] GRADIENT_RED_TO_GREEN   = createGradient(Color.RED, Color.GREEN, 500);

    /**
     * A view for the color map in the middle. Convert all the data values to pixels for visualization.
     * @param clusterMapModel contains all information for drawing heatmap.
     * @param colors is the series of color for converting data to pixels.
     * @param varSelectionModel is the selection model for variable tree.
     * @param obsSelectionModel is the selection model for observation tree.
     */

    public HeatMap(ClusterMapModel clusterMapModel, Color[] colors, 
    		TreeSelectionModel varSelectionModel, TreeSelectionModel obsSelectionModel){
        super();

        this.clusterMapModel = clusterMapModel ;
        this.varSelectionModel = varSelectionModel;
        this.obsSelectionModel = obsSelectionModel;
        
    	this.obsElement = clusterMapModel.getObservationTree().getRoot().getElementOrdered();

    	this.varElement = clusterMapModel.getVariableTree().getRoot().getElementOrdered();
      
        updateGradient(colors);

        this.bg = Color.white;
        this.fg = Color.black;
        this.selectPoint = new HashSet();
        
        ToolTipManager.sharedInstance().registerComponent(this);
    }
    
    public void setSelectPoint(HashSet selectPoint){
        this.selectPoint = selectPoint;	
    } 	
    
    public HashSet getSelectPoint(){
        return (HashSet)this.selectPoint;	
    }

    public void setColorForeground(Color fg){
        this.fg = fg;
        repaint();
    }

    public void setColorBackground(Color bg){
        this.bg = bg;
        repaint();
    }

    public void updateGradient(Color[] colors){
        this.colors = (Color[]) colors.clone();

        if (clusterMapModel.getDataSet()!= null){
            updateDataColors();
            repaint();
        }
    }
    
    public void updateData(ClusterMapModel clusterMapModel){
    	this.clusterMapModel = clusterMapModel;
    	updateDataColors();
        repaint();
    }

    public void updateDataColors() {
    	
        double largest = Double.MIN_VALUE;
        double smallest = Double.MAX_VALUE;
    	for (int i = 0; i < varElement.length; i++){
    		Variable var = ((VariableTreeElement)varElement[i]).getVariable();
    		for (int j = 0; j < obsElement.length; j++){
    			Observation obs = ((ObservationTreeElement)obsElement[j]).getObservation();
                largest = Math.max(clusterMapModel.getDataSet().getValue(var, obs), largest);
                smallest = Math.min(clusterMapModel.getDataSet().getValue(var, obs), smallest);
            }
        }
        double range = largest - smallest;

        dataColorIndices = new int[varElement.length][obsElement.length];

        //assign a Color to each data point
    	for (int i = 0; i < varElement.length; i++){
    		Variable var = ((VariableTreeElement)varElement[i]).getVariable();
    		for (int j = 0; j < obsElement.length; j++){
    			Observation obs = ((ObservationTreeElement)obsElement[j]).getObservation();
            	double norm = (clusterMapModel.getDataSet().getValue(var, obs) - smallest) 
            	    / range; // 0 < norm < 1
                int colorIndex = (int) Math.floor(norm * (colors.length - 1));
                dataColorIndices[i][j] = colorIndex;
            }
        }
    }

    // Creates a BufferedImage of the actual data plot.
    private void drawData(Graphics2D g)
    {
    	// draw the data into the bufferedImage on startup
    	
    	for (int i = 0; i < varElement.length; i++){
    		Variable currentVar = ((VariableTreeElement)varElement[i]).getVariable();
    		for (int j = 0; j < obsElement.length; j++){
    			Observation currentObs = ((ObservationTreeElement)obsElement[j]).getObservation();
    			
    			double value = clusterMapModel.getDataSet().getValue(currentVar, currentObs);
    			
    			g.setColor(colors[dataColorIndices[i][j]]);
    			if ((clusterMapModel.getObsSelectionModel().getSelectedSet()).isEmpty() &&
    					(clusterMapModel.getVarSelectionModel().getSelectedSet()).isEmpty()){
    				g.fillRect(j * clusterMapModel.getElementWidth(),
                    		i * clusterMapModel.getElementHeight(),
                    		clusterMapModel.getElementWidth(),
                    		clusterMapModel.getElementHeight());
    			}else{
    				if(isSelected(currentObs, currentVar)){
    					g.fillRect(j * clusterMapModel.getElementWidth(),
                        		i * clusterMapModel.getElementHeight(),
                        		clusterMapModel.getElementWidth(),
                        		clusterMapModel.getElementHeight());
                    }else{
                    	g.setColor(Color.gray);
                    	g.fill3DRect(j * clusterMapModel.getElementWidth(),
                    			i * clusterMapModel.getElementHeight(),
                    			clusterMapModel.getElementWidth(),
                    			clusterMapModel.getElementHeight(), false);                 	
        			}
    			}
    		}
    	}
    }
    
    public boolean isSelected(Observation obs, Variable var){
    	
    	Set selectedObsNodes = clusterMapModel.getObsSelectionModel().getSelectedSet();
    	Set selectedVarNodes = clusterMapModel.getVarSelectionModel().getSelectedSet();
    	
    	Iterator obsIt = selectedObsNodes.iterator();
    	while(obsIt.hasNext()){
    		TreeElement[] obsEle = ((Node)obsIt.next()).getElementOrdered();
    		for(int i = 0; i < obsEle.length; i++){
    			if(((ObservationTreeElement)obsEle[i]).getObservation().equals(obs)){
        			return true;
        		}
    		}
    	}
    	
    	Iterator varIt = selectedVarNodes.iterator();
    	while(varIt.hasNext()){
    		TreeElement[] varEle = ((Node)varIt.next()).getElementOrdered();
    		for(int i = 0; i < varEle.length; i++){
    			if(((VariableTreeElement)varEle[i]).getVariable().equals(var)){
        			return true;
        		}
    		}
    	}
    	return false;
    }

    /* The overridden painting method, now optimized to simply draw the data
       plot to the screen, letting the drawImage method do the resizing.*/
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = this.getWidth();
        int height = this.getHeight();

        this.setOpaque(true);

        g2d.setColor(bg);
        g2d.fillRect(0, 0, width, height);

        drawData(g2d);
    }

    /* Creates an array of Color objects for use as a gradient, using a linear interpolation between the two specified colors.
    one: Color used for the bottom of the gradient
    two: Color used for the top of the gradient
    numSteps: The number of steps in the gradient. 250 is a good number.*/
    public static Color[] createGradient(final Color one, final Color two, final int numSteps)
    {
        int r1 = one.getRed();
        int g1 = one.getGreen();
        int b1 = one.getBlue();
        int a1 = one.getAlpha();

        int r2 = two.getRed();
        int g2 = two.getGreen();
        int b2 = two.getBlue();
        int a2 = two.getAlpha();

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;

        Color[] gradient = new Color[numSteps];
        double iNorm;
        for (int i = 0; i < numSteps; i++)
        {
            iNorm = i / (double)numSteps; //a normalized [0:1] variable
            newR = (int) (r1 + iNorm * (r2 - r1));
            newG = (int) (g1 + iNorm * (g2 - g1));
            newB = (int) (b1 + iNorm * (b2 - b1));
            newA = (int) (a1 + iNorm * (a2 - a1));
            gradient[i] = new Color(newR, newG, newB, newA);
        }
        return gradient;
    }
    /* Creates an array of Color objects for use as a gradient, using an array of Color objects. It uses a linear interpolation between each pair of points. The parameter numSteps defines the total number of colors in the returned array, not the number of colors per segment.
    colors: An array of Color objects used for the gradient. The Color at index 0 will be the lowest color.
    numSteps: The number of steps in the gradient. 250 is a good number.*/
    public static Color[] createMultiGradient(Color[] colors, int numSteps){
        //we assume a linear gradient, with equal spacing between colors
        //The final gradient will be made up of n 'sections', where n = colors.length - 1

        int numSections = colors.length - 1;
        int gradientIndex = 0; //points to the next open spot in the final gradient
        Color[] gradient = new Color[numSteps];
        Color[] temp;

        if (numSections <= 0) {
            throw new IllegalArgumentException("You must pass in at least 2 colors in the array!");
        }

        for (int section = 0; section < numSections; section++){
            //we divide the gradient into (n - 1) sections, and do a regular gradient for each
            temp = createGradient(colors[section], colors[section+1], numSteps / numSections);
            for (int i = 0; i < temp.length; i++){
                //copy the sub-gradient into the overall gradient
                gradient[gradientIndex++] = temp[i];
            }
        }

        if (gradientIndex < numSteps){
            for (/* nothing to initialize */; gradientIndex < numSteps; gradientIndex++) {
                gradient[gradientIndex] = colors[colors.length - 1];
            }
        }
        return gradient;
    }

    /**
     * Implements from HeatMapModelListener
     */
    public void dataChanged(ClusterMapModel clusterMapModel){
    	repaint();
    }

	private Point detectPoint(Point p){
		int x, y;

		for(x = 0; x < varElement.length; x++){
			for(y = 0; y < obsElement.length; y++){
				Rectangle rect = new Rectangle(y * clusterMapModel.getElementWidth(),
                		x * clusterMapModel.getElementHeight(),
                		clusterMapModel.getElementWidth(),
                		clusterMapModel.getElementHeight());
				if(rect.contains(p)){return new Point(x, y); }
			}
		}
		return null;
	}

	public Dimension getPreferredSize() {
		return new Dimension(clusterMapModel.getObservationTree().getRoot().getLeafNumber()
				* clusterMapModel.getElementWidth(), 
				clusterMapModel.getVariableTree().getRoot().getLeafNumber() * 
				clusterMapModel.getElementHeight());
	}
	
	/**
	 * implemented from Scrollable
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(obsElement.length * clusterMapModel.getElementWidth(),
				varElement.length * clusterMapModel.getElementHeight());
	}
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction){
		if (orientation == SwingConstants.HORIZONTAL){
			return clusterMapModel.getElementWidth() * (int)((double)obsElement.length / 5);
		}else{
			return clusterMapModel.getElementHeight() * (int)((double)varElement.length / 5);
		}
	}
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL){
			return clusterMapModel.getElementWidth();
		}else{
			return clusterMapModel.getElementHeight();
		}
	}

	public String getToolTipText (MouseEvent e){
		Point p = e.getPoint();
		Point pSelected = detectPoint(p);
		
		return (null == pSelected)? null : "<html> Observation: " + 
			((ObservationTreeElement)obsElement[pSelected.y]).getObservation().getId() + 
			"<br> Variable: " + 
			((VariableTreeElement)varElement[pSelected.x]).getVariable().getId() +
			"<br> Value: " + 
			clusterMapModel.getDataSet().getValue(((VariableTreeElement)varElement[pSelected.x]).getVariable(), 
					((ObservationTreeElement)obsElement[pSelected.y]).getObservation()) ;
	}
}


