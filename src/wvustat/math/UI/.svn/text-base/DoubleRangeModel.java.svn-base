package wvustat.math.UI;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 28, 2003
 * Time: 5:20:27 PM
 * To change this template use Options | File Templates.
 */
public class DoubleRangeModel extends  DefaultBoundedRangeModel
{
    private static double ratio;
    private double min;
    private static int intMin=0, intMax=100;

    public DoubleRangeModel(double value, double min, double max)
    {
        super(0,1,intMin, intMax);
        this.min=min;
        ratio=(max-min)/(intMax-intMin);
        setDoubleValue(value);
    }

    public double getDoubleValue()
    {
        return inverseTransform(getValue());
    }

    public void setDoubleValue(double value)
    {
        if( transform(value) > getMaximum())
        {
            this.setMaximum(transform(value));
        }
        setValue(transform(value));
    }

    private int transform(double value)
    {
        return (int)((value-min)/ratio)+intMin;
    }

    private double inverseTransform(int value)
    {
        return (value-intMin)*ratio+min;
    }
}
