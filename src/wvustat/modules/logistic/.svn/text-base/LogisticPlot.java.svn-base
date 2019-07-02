/*
 * LogisticPlot.java
 *
 * Created on December 26, 2001, 12:39 PM
 */

package wvustat.modules.logistic;

import java.awt.*;

import javax.swing.*;

import java.text.NumberFormat;
import java.util.Arrays;
import java.awt.geom.*;
import java.awt.event.*;

import wvustat.modules.*;
import wvustat.interfaces.*;
import wvustat.swing.MarkerShape;
/**
 *
 * @author  hxue
 * @version
 */
public class LogisticPlot extends JPanel {
    private DataSet dataSet;
    private int width=200, height=270;
    private RegressionSolver solver;
    private BaseAxisModel xtm, ytm;
    private CoordConverter coord;
    private String xLabel;
    private double[] x_array, y_array;
    private int first_x, first_y, last_x, last_y, brushWidth = 10, brushHeight = 10;
    private Shape[] discs;
    private boolean draggingStarted=false;
    private int[] indices;
    
    private GUIModule module;
    private GeneralPath popupArrow;
    
    /** Creates new LogisticPlot */
    public LogisticPlot(RegressionSolver solver, double[] A1, double[] A2, int[] indices, DataSet dataSet) {
        this.solver=solver;
        this.x_array=A1;
        this.y_array=A2;
        this.indices=indices;
        this.dataSet=dataSet;
        setBackground(Color.white);
        MouseEventHandler handler=new MouseEventHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        //updateIndices();
        
        popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();
    }
    
    /*public void updateIndices(){
        indices=new int[dataSet.getSize()-dataSet.getTotalMasks()];
        int j=0;
        try{
            for(int i=0;i<dataSet.getSize();i++){
                if(dataSet.getMask(i)==false){
                    indices[j]=i;
                    j++;
                }
            }
        }
        catch(RemoteException re){
        }
    }*/
    
    public void setData(double[] x_array, double[] y_array, int[] indices){
        this.x_array=x_array;
        this.y_array=y_array;
        this.indices=indices;
        //updateIndices();
        initPlot();
        repaint();
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }
    
    
    public Dimension getSize(){
        return new Dimension(width, height);
    }
    
    public Dimension getPreferredSize(){
        return getSize();
    }
    
    public Dimension getMinimumSize(){
        return getSize();
    }
    
    public void setXLabel(String str){
        this.xLabel=str;
    }
    
    private void initPlot(){
        ytm=new BaseAxisModel(0,1.0, BaseAxisModel.MIN_FIXED);
        
        double xMax=Double.MIN_VALUE;
        double xMin=Double.MAX_VALUE;
        for(int i=0;i<x_array.length;i++){
            if(x_array[i]>xMax)
                xMax=x_array[i];
            if(x_array[i]<xMin)
                xMin=x_array[i];
        }
        
        xtm=new BaseAxisModel(xMin, xMax, 5);
        coord=new CoordConverter(width, height, xtm.getStartValue(), xtm.getEndValue(), 0, 1.0, new Insets(20,40,40,20));
        
        
    }
    
    public void setBounds(int x, int y, int w, int h){
        width=w;
        height=h;
        initPlot();
        super.setBounds(x,y,w,h);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.drawLine(0, 0, width - 1, 0);
        
        g2.fill(popupArrow);
        
        Stroke thinLine=new BasicStroke(1.0f);
        Stroke thickLine=new BasicStroke(2.0f);
        
        //Draw x and y axis
        g2.setStroke(thickLine);
        g2.drawLine(coord.x(xtm.getStartValue()), coord.y(0), coord.x(xtm.getEndValue()), coord.y(0));
        g2.drawLine(coord.x(xtm.getStartValue()), coord.y(0), coord.x(xtm.getStartValue()), coord.y(1.0));
        
        g2.drawString("Probability", coord.x(xtm.getStartValue())+4, coord.y(1.0)-4);
        g2.drawString(xLabel, coord.x(xtm.getEndValue())+4, coord.y(0));
        
        g2.setStroke(thinLine);
        
        //Draw ticmarks
        NumberFormat nf=NumberFormat.getInstance();
        FontMetrics metrics=g2.getFontMetrics();
        double tmpy=0;
        while(tmpy<=1.0){
            g2.drawLine(coord.x(xtm.getStartValue()), coord.y(tmpy), coord.x(xtm.getStartValue())-4, coord.y(tmpy));
            String tmpStr=nf.format(tmpy);
            g2.drawString(tmpStr, coord.x(xtm.getStartValue())-4-metrics.stringWidth(tmpStr), coord.y(tmpy));
            tmpy+=0.1;
        }
        
        double tmpx=xtm.getStartValue();
        
        while(tmpx<=xtm.getEndValue()){
            g2.drawLine(coord.x(tmpx), coord.y(0), coord.x(tmpx), coord.y(0)+4);
            String tmpStr=nf.format(tmpx);
            g2.drawString(tmpStr, coord.x(tmpx)-metrics.stringWidth(tmpStr)/2, coord.y(0)+4+metrics.getMaxAscent());
            tmpx+=xtm.getInterval();
        }
        
        
        discs=new Shape[x_array.length];
        
        for(int i=0;i<discs.length;i++){
            discs[i]=new MarkerShape(coord.x(x_array[i]),coord.y(y_array[i]),dataSet.getMarker(indices[i]).intValue());
        }
        
        
        for(int i=0;i<x_array.length;i++){
            
            g2.setColor(dataSet.getColor(i));
            if(dataSet.getState(indices[i])){
                g2.fill(discs[i]);
                g2.draw(discs[i]);
                if(dataSet.getLabel(i)!=null){
                    g2.setColor(Color.black);
                    g2.drawString(dataSet.getLabel(i),(int)discs[i].getBounds().getMaxX()+2, (int)discs[i].getBounds().getMinY()+2);
                }
            }
            else
                g2.draw(discs[i]);
            
        }
        
        g2.setColor(Color.black);
        int segs=500;
        double seg_len=(xtm.getEndValue()-xtm.getStartValue())/segs;
        tmpx=xtm.getStartValue();
        
        g2.setColor(Color.blue);
        for(int i=0;i<segs;i++){
            g2.drawLine(coord.x(tmpx), coord.y(solver.PI(new double[]{1,tmpx})), coord.x(tmpx+seg_len), coord.y(solver.PI(new double[]{1,tmpx+seg_len})));
            tmpx+=seg_len;
        }
        
        if(draggingStarted){
            Rectangle2D rect=new Rectangle2D.Float(first_x, first_y, last_x-first_x, last_y-first_y);
            g2.setColor(Color.lightGray);
            g2.draw(rect);
        }
    }//End paintComponent()
    
    class MouseEventHandler extends MouseAdapter implements MouseMotionListener{
        
        public void mousePressed(MouseEvent e){
            int x=e.getX(), y=e.getY();
            
            if (popupArrow.getBounds().contains(x, y)) {
                JPopupMenu popupOptionMenu = ((LogisticModule) module).getOptionMenu().getPopupMenu();
                popupOptionMenu.show(e.getComponent(), x, y);
                return;
            }
            
            if (LinkPolicy.mode != LinkPolicy.SELECTING)
            {
            	last_x = x;
            	last_y = y;
            	first_x = x - brushWidth;
            	first_y = y - brushHeight;
            	
            	
                //dataSet.clearStates();
                boolean[] states = new boolean[dataSet.getSize()];
            	Arrays.fill(states, false);
                
                Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);

                for (int i = 0; i < discs.length; i++)
                {
                    if (rect.contains(discs[i].getBounds()))
                    {
                        //dataSet.setState(true, indices[i]);
                        states[indices[i]] = true;
                    }
                }
                dataSet.setStates(states);
            	
                draggingStarted = true;
                LogisticPlot.this.repaint();
                return;
            }
            
            // when LinkPolicy.mode == SELECTING
            first_x=x;
            first_y=y;
            last_x=first_x;
            last_y=first_y;
            
            boolean multipleSelection=false;
            if(e.getModifiers()==17){
                multipleSelection=true;
            }
            
            for(int i=0;i<discs.length;i++){
                if(discs[i].contains(first_x, first_y)){
                    
                    if(e.getClickCount()==2){
                        Color c=JColorChooser.showDialog(LogisticPlot.this,"Palette", Color.black);
                        if(c!=null && dataSet.getState(indices[i]))
                            dataSet.setColor(c,indices[i]);
                        return;
                    }
                        
                    if(!multipleSelection)
                        dataSet.clearStates();
                        
                    boolean bl=dataSet.getState(indices[i]);
                    dataSet.setState(!bl, indices[i]);
                    return;
                    
                }
            }
            
            
            dataSet.clearStates();
            
            
            draggingStarted=true;
        }
        
        public void mouseReleased(MouseEvent e){
            
            if(draggingStarted){
                Rectangle2D rect=new Rectangle2D.Float(first_x, first_y, last_x-first_x, last_y-first_y);
                
                boolean[] states = dataSet.getStates();
                for(int i=0;i<discs.length;i++){
                    if(rect.contains(discs[i].getBounds())){
                        
                        //dataSet.setState(true, indices[i]);
                        states[indices[i]] = true;
                    }
                }
                dataSet.setStates(states);
            }
            
            draggingStarted=false;
            LogisticPlot.this.repaint();
        }
        
        public void mouseDragged(MouseEvent e){
            
            last_x=e.getX();
            last_y=e.getY();
            
            if (draggingStarted && LinkPolicy.mode != LinkPolicy.SELECTING)
            {
            	if (!e.isAltDown())
            	{	
            		first_x = last_x - brushWidth;
            		first_y = last_y - brushHeight;
            	} else {
            		brushWidth = (last_x - first_x > 10 ? last_x - first_x : 10);
            		brushHeight = (last_y - first_y > 10 ? last_y - first_y : 10);
            	}
            	
            	boolean[] states = dataSet.getStates();
                if (LinkPolicy.mode == LinkPolicy.BRUSHING && !e.isShiftDown()) 
                	Arrays.fill(states, false); //dataSet.clearStates();
                    
                Rectangle2D rect = new Rectangle2D.Float(first_x, first_y, last_x - first_x, last_y - first_y);

                for (int i = 0; i < discs.length; i++)
                {
                    if (rect.contains(discs[i].getBounds()))
                    {
                    	states[indices[i]] = true; //dataSet.setState(true, indices[i]);
                    }
                }
                dataSet.setStates(states);
                
            }  
            
            LogisticPlot.this.repaint();
        }
        
        public void mouseMoved(MouseEvent e){}
    }
    
    
}