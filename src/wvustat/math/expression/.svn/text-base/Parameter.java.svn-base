package wvustat.math.expression;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Jan 11, 2003
 * Time: 11:44:46 AM
 * To change this template use Options | File Templates.
 */
public class Parameter implements Serializable
{
    private String name;
    private double value;
    private double SE;
    private double pValue;

    public Parameter(String name, double value)
    {
        this.name=name;
        this.value=value;
    }

    public Parameter(String name)
    {
        this(name,0);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public double getSE()
    {
        return SE;
    }

    public void setSE(double SE)
    {
        this.SE = SE;
    }

    public double getpValue()
    {
        return pValue;
    }

    public void setpValue(double pValue)
    {
        this.pValue = pValue;
    }

    public boolean equals(Object obj)
    {
        if(obj==null)
            return false;

        if(obj instanceof Parameter)
        {
            Parameter another=(Parameter)obj;
            return name.equals(another.name);
        }
        else
            return false;
    }

    public Object clone()
    {
        return new Parameter(name, value);
    }

    public static class ParameterComparator implements Comparator
    {
        public int compare(Object o1, Object o2)
        {
            if(o1 instanceof Parameter && o2 instanceof Parameter)
            {
                Parameter p1=(Parameter)o1;
                Parameter p2=(Parameter)o2;
                return p1.name.compareTo(p2.name);
            }
            else
                return 0;
        }
    }
}
