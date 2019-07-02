/*
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Oct 29, 2002
 * Time: 8:36:38 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package wvustat.util;

public class Bucket
{
    private int size=0;
    private double minValue;
    private double width;

    public Bucket(double minValue, double width)
    {
        this.minValue=minValue;
        this.width=width;
    }

    public void setSize(int size)
    {
        this.size=size;
    }

    public int getSize()
    {
        return size;
    }

    public double getMidPoint()
    {
        return minValue+width/2.0;
    }

    public double getMinValue()
    {
        return minValue;
    }

    public double getWidth()
    {
        return width;
    }
}
