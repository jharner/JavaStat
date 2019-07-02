package wvustat.math.plot;

/**
 * CoordConverter converts the coordinates that is common in mathematics to pixel coordinates in Java.
 *
 * Author Hengyi Xue
 */

import java.awt.Insets;

public class CoordConverter{

    private int x0, y0; //This is the origin of the axises with pixel coordinates
    private double ratioX, ratioY; //This is the real value to pixel ratio for x and y coordinates
    private double rx0=0, ry0=0; //This is the origin of the axises with real coordinates
    
    public CoordConverter(int x0, int y0, double ratioX, double ratioY){
        this.x0=x0;
        this.y0=y0;
        this.ratioX=ratioX;
        this.ratioY=ratioY;
    }
    
    public CoordConverter(int x0, int y0, double ratioX, double ratioY, double rx0, double ry0){
        this(x0,y0,ratioX, ratioY);
        this.rx0=rx0;
        this.ry0=ry0;
    }
    
    public void moveOrigin(double deltaX, double deltaY){
        this.rx0+=deltaX;
        this.ry0+=deltaY;
    }
    
    public void moveOriginTo(double x, double y){
        this.rx0=x;
        this.ry0=y;
    }
    
    public void setXRatio(double ratioX){
        this.ratioX=ratioX;
    }
    
    public void setYRatio(double ratioY){
        this.ratioY=ratioY;
    }
    
    public double getXOrigin(){
        return rx0;
    }
    
    public double getYOrigin(){
        return ry0;
    }

    /**
     * Convert a double x value into pixel x value
     */
    public int xcoord(double rx){
        long ret=Math.round((rx-rx0)/ratioX)+x0;
        
        if(ret<=Integer.MAX_VALUE || ret>=Integer.MIN_VALUE)
        return (int)ret;
        else if(ret>Integer.MAX_VALUE)
        return Integer.MAX_VALUE;
        else if(ret<Integer.MIN_VALUE)
        return Integer.MIN_VALUE;
        else 
        return 0;
    }    
    /**
     * Convert a double y value into pixel y value.
     */
    public int ycoord(double ry){
        long ret=y0-Math.round((ry-ry0)/ratioY);
        
        if(ret<=Integer.MAX_VALUE || ret>=Integer.MIN_VALUE)
        return (int)ret;
        else if(ret>Integer.MAX_VALUE)
        return Integer.MAX_VALUE;
        else if(ret<Integer.MIN_VALUE)
        return Integer.MIN_VALUE;
        else 
        return 0;        
    }

    /**
     * Convert a pixel x value into double x value
     */
    public double getRealX(int x){
        return rx0+(x-x0)*ratioX;
    }

    /**
     * Convert a pixel y value into double x value
     */
    public double getRealY(int y){
        return ry0+(y0-y)*ratioY;
    }
}

