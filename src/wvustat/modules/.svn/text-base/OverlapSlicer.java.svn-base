package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

import wvustat.interfaces.DividePolicy;
import wvustat.swing.DelayedDispatcher;
import wvustat.swing.DelayedDispatch;

import java.rmi.*;

/**
 * OverlapSlicer is a component like GroupChooser. But it supports overlapped shingles and 
 * multiple conditional variables.  It has the look and feel of a set of slider. It is used 
 * in data analysis modules in which observations can be divided into groups.
 *
 * @author: Dajie Luo
 * @version: 1.0, Jan. 31, 2006
 */
public class OverlapSlicer extends JComponent implements MouseListener, DelayedDispatcher{

    private EqualCountGrouper grouper;
    private String[] chooserLabel;

    private EventListenerList eList=new EventListenerList();
    private ChangeEvent event;

    private int cond; // number of slicers
    
    private int[] currentIndex, maxIndex;

    private int width=110, height=25, textht=15, gap=20;
    private int[] fillWidth={20,20,20};

    private Rectangle2D[] bar,fill;

    private java.text.NumberFormat formatter;
    
    private DelayedDispatch dispatcher;

    /**
     * Constructor
     * Creates a new OverlapSlicer based on the EqualCountGrouper object
     *
     * @param grouper the EqualCountGrouper object which contains grouping information about a data set
     */
    public OverlapSlicer(EqualCountGrouper grouper){
        this.grouper=grouper;
        this.cond = grouper.getCondCount();

        setBackground(Color.white);
        
        int totalWidth = width*cond+gap*(cond-1);
        setPreferredSize(new Dimension(totalWidth,height));
        setMinimumSize(new Dimension(totalWidth,height));
        setSize(new Dimension(totalWidth,height));
        setMaximumSize(new Dimension(totalWidth,height));

        setToolTipText("Click to select a group");

        addMouseListener(this);

        bar = new Rectangle2D[cond];
        fill = new Rectangle2D[cond];
        chooserLabel = new String[cond];
        currentIndex = new int[cond];
        maxIndex = new int[cond];
        
        init();
        
        formatter=java.text.NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(1);
    }

    private void init()
    {
    	    for(int i = 0; i < cond; i++)
        {
                
        		bar[i]=new Rectangle2D.Float((width+gap)*i,textht,width-1,height-textht-1);
        		
        		fillWidth[i]=(int)(width*1.0/grouper.getGroupCount(i));
        		chooserLabel[i]=grouper.getName(i);
        		maxIndex[i]=grouper.getGroupCount(i);
        		currentIndex[i]=0;
        		

        		fill[i]=new Rectangle2D.Float((width+gap)*i,textht,fillWidth[i]-1,height-textht-1);
        }    
    }
    
    
    /**
     * Render this component
     */
    public void paintComponent(Graphics g){
        Graphics2D g2=(Graphics2D)g;

        for(int i=0; i < cond; i++)
        {
        		if(grouper.getType(i) == DividePolicy.DISCRETE){
        			if(currentIndex[i]==maxIndex[i]-1){
        				double tmpX=currentIndex[i]*fillWidth[i];
        				double tmpW=bar[i].getWidth()-tmpX;
        				fill[i].setRect((width+gap)*i+tmpX,fill[i].getY(),tmpW,fill[i].getHeight());
        			}
        			else        
        				fill[i].setRect((width+gap)*i+currentIndex[i]*fillWidth[i], fill[i].getY(), fill[i].getWidth(), fill[i].getHeight());
        		}
        		else
        		{
        			if(currentIndex[i]==maxIndex[i]-1){
        				double tmpX=(grouper.getLowerLimit(currentIndex[i],i) - grouper.getLowerLimit(0,i)) / 
						        (grouper.getUpperLimit(maxIndex[i]-1,i) - grouper.getLowerLimit(0,i)) * bar[i].getWidth(); 
        				double tmpW=bar[i].getWidth()-tmpX;
        				fill[i].setRect((width+gap)*i+tmpX,fill[i].getY(),tmpW,fill[i].getHeight());
        			}
        			else{
        				double tmpX=(grouper.getLowerLimit(currentIndex[i],i) - grouper.getLowerLimit(0,i)) / 
				                (grouper.getUpperLimit(maxIndex[i]-1,i) - grouper.getLowerLimit(0,i)) * bar[i].getWidth(); 
        				double tmpW=(grouper.getUpperLimit(currentIndex[i],i) - grouper.getLowerLimit(currentIndex[i],i)) / 
		                         (grouper.getUpperLimit(maxIndex[i]-1,i) - grouper.getLowerLimit(0,i)) * bar[i].getWidth(); 
        				fill[i].setRect((width+gap)*i+tmpX, fill[i].getY(), tmpW, fill[i].getHeight());
        			}
        			
        		}

        		g2.draw(bar[i]);
        		g2.fill(fill[i]);

        		String name=chooserLabel[i];

        		if(name.length()>6)
        			name=name.substring(0,3)+"...";

        		FontMetrics metrics=getFontMetrics(g2.getFont());

        		g2.drawString(name, (width+gap)*i , textht-2);

        		if(grouper.getType(i) == DividePolicy.DISCRETE){
        			String tmps="";
        			
        			tmps=grouper.getGroupName(currentIndex[i],i);
        			
        			if(tmps.length()>9)
        				tmps=tmps.substring(0,6)+"...";
        			int tmpi=metrics.stringWidth(tmps);
        			g2.drawString(tmps, (width+gap)*i + width-tmpi-1,textht-2);
        		}
        		else{
                double lo=grouper.getLowerLimit(currentIndex[i],i);
                double hi=grouper.getUpperLimit(currentIndex[i],i);

                String tmps=formatter.format(lo)+"-"+formatter.format(hi);
                int tmpi=metrics.stringWidth(tmps);
                g2.drawString(tmps, (width+gap)*i + width-tmpi-1,textht-2);
        		}
        }

    }

    public void addChangeListener(ChangeListener l){
        eList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l){
        eList.remove(ChangeListener.class, l);
    }

    private void fireStateChanged(){
        Object[] list=eList.getListenerList();
        for(int i=list.length-2;i>=0;i-=2){
            if(list[i]==ChangeListener.class){
                if(event==null)
                event=new ChangeEvent(this);
                ((ChangeListener)list[i+1]).stateChanged(event);
            }
        }
    }

    /**
     * Get the current value the user has selected
     */
    public int getCurrentIndex(){
        return grouper.toGlobalIndex(currentIndex);
    }

    /**
     * Set the value the chooser displayed to the specified index so that this
     * component can be programmatically controlled
     */
    public void setCurrentIndex(int index){
        currentIndex=grouper.toGroupIndexArray(index);
        repaint();
    }

    public void respondToSingleClick(MouseEvent e){
    		int x=e.getX(), y=e.getY();

    		for(int i=0; i < cond; i++)
    		{
	    		if(bar[i].contains(x,y)&& !fill[i].contains(x,y)){
	    			if(x>fill[i].getX()){
	    				currentIndex[i]++;
	    			}
	    			else{
	    				currentIndex[i]--;
	    			}

	    			repaint();
	        		fireStateChanged();
	        	}
	    	}
    }
    
    public void mousePressed(MouseEvent evt){}
    
    public void mouseClicked(MouseEvent e){
  	    if (e.getClickCount() == 2)
  	    {
  	    		dispatcher.isDoubleClick = true;
  	    		boolean found = false;
  	    		int index = -1;
  	    		for(int i=0; i < cond; i++)
  	    		{
  	    			if(bar[i].contains(e.getPoint())){
  	    				index = i;
  	    				found = true;
  	    				break;
  	    			}
  	    		}
  	    		
  	    		if(found && grouper.getType(index) == DividePolicy.CONTINUOUS){
  	    			
  	    			SlicerConfigPanel configPanel = new SlicerConfigPanel();
  	      		configPanel.setGroupCount(grouper.getGroupCount(index));
  	      		configPanel.setOverlap(grouper.getOverlap(index)*100);
  	      		
  	      		int option = JOptionPane.showOptionDialog(OverlapSlicer.this, configPanel,
  	                      "interval settings",
  	                      JOptionPane.OK_CANCEL_OPTION,
  	                      JOptionPane.PLAIN_MESSAGE,
  	                      null,
  	                      null,
  	                      null);
  	             if (option == JOptionPane.OK_OPTION) {
  	                 int gpc = configPanel.getGroupCount();
  	                 float ovlp = configPanel.getOverlap();
  	                 if ( gpc < 1 || gpc > grouper.getValsCount(index)) 
  	                 	JOptionPane.showMessageDialog(OverlapSlicer.this, "# of intervals should between 1 and " + grouper.getValsCount(index) + "!", "Error", JOptionPane.ERROR_MESSAGE);
  	                 else if ( ovlp < 0 || ovlp > 90)
  	                 	JOptionPane.showMessageDialog(OverlapSlicer.this, "overlap fraction % should between 0 and 90!", "Error", JOptionPane.ERROR_MESSAGE);
  	                 else{
  	                 	ovlp /= 100;
  	                 	grouper.setGroupCount(gpc,index);
  	                 	grouper.setOverlap(ovlp,index);
  	                 	
  	                 	init();
  	                 	repaint();
  	                 	fireStateChanged();
  	                 }	
  	             }
  	    		}
  	    } else if (e.getClickCount() == 1){
  	    		dispatcher = new DelayedDispatch(e, OverlapSlicer.this);
  	    		dispatcher.start();
  	    }
  	}
    
    public void mouseEntered(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    

}
