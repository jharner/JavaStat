package wvustat.util;

import java.awt.*;
import java.io.Serializable;
/**
 *	CoordConverter converts the coordinate system used in a mathematical plot into
 *	the bitmap system.
 */
public class CoordConverter implements Serializable{
    private double factorX, factorY;
    private double xMin, xMax;
    private double xShift,yShift;
    private double yLength;

    private Insets insets;

    /**
     * Constructor
     *
     * @param width the width of the drawing surface
     * @param height the height of the drawing surface
     * @param minX the minimum x value
     * @param maxX the maximum x value
     * @param minY the minimum y value
     * @param maxY the maximum y value
     * @param insets the margin
     */
    public CoordConverter(int width,int height, double minX,double maxX, double minY, double maxY, Insets insetIn){

        insets=insetIn;

        factorX=(width-(insets.left+insets.right))/(maxX-minX);
        factorY=(height-(insets.top+insets.bottom))/(maxY-minY);
        xShift=-minX;
        yShift=-minY;
        xMin=minX;
        xMax=maxX;
        yLength=maxY-minY;
    }

    /**
     * get the plot x coordinate for a given x
     */
    public int x(double inX){
        double xCo=(inX+xShift)*factorX+insets.left;
        return((int)Math.round(xCo));
    }

    /**
     * get the plot y coordinate for a given y
     */
    public int y(double inY){
        double yCo=(yLength-inY-yShift)*factorY+insets.top;

        return ((int)Math.round(yCo));
    }

    /**
     * Get x value for a given x coordinate
     */
    public double inverseX(int x){

        double RealX=(x-insets.left)/factorX-xShift;
        return RealX;
    }

    /**
     * Get y value for a given y coordinate
     */
    public double inverseY(int x)
    {
        double RealY=yLength-(x-insets.top)/factorY-yShift;
        return RealY;
    }


}
