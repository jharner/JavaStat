package wvustat.modules;

import wvustat.interfaces.DividePolicy;
import wvustat.interfaces.Variable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Arrays;

/**
 * GroupMaker is a implementation of DividePolicy. It divides observations in a data set into
 * groups. It uses a simple grouping strategy. If the z variable is categorical, it groups observations
 * according to their z values. If z is continuous, it separates the range of z values into smaller
 * ranges and group observations according to these ranges.
 *
 *
 */
public class GroupMaker implements DividePolicy
{

    //private BaseAxisModel tm;
    private Grouping grouping;
    private Variable z;
    private Vector levels;
    private int type;
    private double[] numVals;

    /**
     * Constructor
     * Creates a new GroupMaker based on the z variable specified
     */
    public GroupMaker(Variable z_var)
    {
        z = z_var;
        type = -1;

        
        type = z.getType();

        if (type == Variable.NUMERIC)
        {
            //tm = new BaseAxisModel(z.getMin(), z.getMax(), defaultNum);
            grouping=new Grouping(z_var.getSortedNumValues());

        }
        else
        {
            levels = new Vector();
            String[] vals = z.getSortedCatValues();
            for (int i = 0; i < vals.length; i++)
            {
                if (!levels.contains(vals[i]))
                    levels.addElement(vals[i]);
            }

        }

        numVals = z.getNumValues();
        
    }

    /**
     * Get the name associates with this object, usually it is z variable's name
     */
    public String getName() 
    {
        return z.getName();
    }

    /**
     * Get the total number of groups
     */
    public int getGroupCount()
    {
        int n = 1;

        switch (type)
        {
            case Variable.CATEGORICAL:
                n = levels.size();
                break;
            case Variable.NUMERIC:
                //n = tm.getNumOfIntervals();
                n=grouping.getNumberOfRanges();
                break;
        }

        return n;
    }

    /**
     * Get the group index for a certain observation based on its z value
     */
    public int getGroupIndex(Object val) 
    {
        int index = 0;

        switch (type)
        {
            case Variable.NUMERIC:
                double tmp = ((Double) val).doubleValue();
                index = grouping.getRangeIndex(tmp);
                break;
            case Variable.CATEGORICAL:
                String tmps = (String) val;
                index = levels.indexOf(tmps);
                break;
        }

        return index;
    }

    /**
     * Get the names for all the groups, should only be used when z is categorical
     */
    public String[] getGroupNames()
    {
        String[] s = null;

        if (type == Variable.CATEGORICAL)
        {
            s = new String[levels.size()];
            levels.copyInto(s);
        }
        else
        {
            s=new String[getGroupCount()];
            NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);
            for(int i=0;i<s.length;i++)
            {
                s[i]="("+nf.format(getLowerLimit(i))+" - "+nf.format(getUpperLimit(i))+")";
            }
        }

        return s;
    }

    /**
     * Get the name of the indexed group
     */
    public String getGroupName(int index)
    {
        String str = null;

        if (type == Variable.CATEGORICAL)
            str = (String) levels.elementAt(index);

        return str;
    }

    /**
     * Get the minimum z value for an indexed group
     */
    public double getLowerLimit(int index)
    {
        return grouping.getRange(index).from;
    }

    /**
     * Get the maximum z value for an indexed group
     */
    public double getUpperLimit(int index)
    {
        return grouping.getRange(index).to;
    }

    /**
     * Get the type of this GroupMaker, CONTINUOUS or DISCRETE
     */
    public int getType()
    {
        if (type == Variable.NUMERIC)
            return CONTINUOUS;
        else
            return DISCRETE;
    }

    public double[] getConstraintVector(int index)
    {
    	numVals = z.getNumValues(); //added by djluo for case z changes. 3/29/07
        double[] array = new double[numVals.length];

        for (int i = 0; i < array.length; i++)
        {
            if (type == Variable.NUMERIC)
            {
                //int r = grouping.getRangeIndex(i);  modified by djluo
            	int r = grouping.getRangeIndex(numVals[i]);
                if (r == index)
                    array[i] = 1;
                else
                    array[i] = 0;
            }
            else
            {
                if (numVals[i] == index)
                    array[i] = 1;
                else
                    array[i] = 0;
            }
        }

        return array;
    }

    public double[][] getConstraintMatrix()
    {
    	numVals = z.getNumValues(); //added by djluo. 3/29/07
        double[][] ret=new double[numVals.length][getGroupCount()];
        for(int i=0;i<getGroupCount();i++)
        {
            double[] array=getConstraintVector(i);
            for(int j=0;j<array.length;j++)
            {
                ret[j][i]=array[j];
            }
        }
        return ret;
    }

    private static class Grouping
    {

        private int groupCount;
        private List rangeList=new ArrayList();


        Grouping(double[] values)
        {

            groupCount=getGroupCount(values.length);
            int groupSize=values.length/groupCount;

            int count=0;
            int startIndex=0;

            int i=0;
            int groupCounter=0;
            while(i<values.length && groupCounter<groupCount-1)
            {
                count++;
                if(count>=groupSize)
                {
                    DoubleRange range=new DoubleRange(values[startIndex], values[i]);
                    rangeList.add(range);
                    count=0;
                    startIndex=i+1;
                    groupCounter++;
                }
                i++;
            }

            rangeList.add(new DoubleRange(values[startIndex], values[values.length-1]));

        }

        public DoubleRange getRange(int groupIndex)
        {
            return (DoubleRange)rangeList.get(groupIndex);
        }

        public int getNumberOfRanges()
        {
            return rangeList.size();
        }

        public int getRangeIndex(double value)
        {
            int index=0;
            for(int i=0;i<rangeList.size();i++)
            {
                DoubleRange range=(DoubleRange)rangeList.get(i);
                if(range.from <=value && range.to>=value)
                {
                    index=i;
                    break;
                }
            }

            return index;
        }

        private int getGroupCount(int totalElements)
        {
            if(totalElements<5)
                return 1;
            else
                return 3;
        }
    }


    private static class DoubleRange
    {
        double from;
        double to;

        public DoubleRange(double from, double to)
        {
            this.from = from;
            this.to = to;
        }
    }


    public static void main(String[] args)
    {
        double[] value={1, 2.1, 0.5, 2.7, 8, 4, 5, 3, 6, 4.4, 3.5, 6};

        Arrays.sort(value);
        Grouping grouping=new Grouping(value);
        int n=grouping.getNumberOfRanges();
        System.out.println("n="+n);
    }

}

