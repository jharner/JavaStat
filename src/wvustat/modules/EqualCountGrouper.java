package wvustat.modules;

import wvustat.interfaces.EqualCountPolicy;
import wvustat.interfaces.Variable;
import wvustat.interfaces.DataSet;

import java.text.NumberFormat;
import java.util.*;

/**
 * EqualCountGrouper is another implementation like GroupMaker. But it supports shingle and 
 * multiple conditional variables. It divides observations in a data set into equal size groups.
 * If the z variable is categorical, it groups observations according to their z values.
 * If z is continuous, it uses the equal-count algorithm which separates the range of z values 
 * into overlapped shingles and group observations according to these shingles.
 *
 */
public class EqualCountGrouper implements EqualCountPolicy
{
	public static final int MAXCOND=3;
	
    private int zCount;
    private Object[] grouping;   // store either Grouping or Vector element
    //	private Grouping grouping;
    //	private Vector levels;
    
    private Vector vz;
    private int[] type;
    private double[][] numVals;
    private int[] n;  // store # of groups for each conditional variable
    private DataSet data;

    /**
     * Constructor
     * Creates a new EqualCountGrouper based on the z variables specified
     */
    public EqualCountGrouper(Vector z_vars, DataSet data)
    {
        vz = z_vars;
        zCount = vz.size();
        if(zCount > MAXCOND) zCount = MAXCOND;
        this.data = data;
        
        type = new int[zCount];
        grouping = new Object[zCount];
        numVals = new double[zCount][];
        n = new int[zCount];
        
        for(int k=0; k < zCount; k++)
        {
        	Variable z = (Variable)z_vars.elementAt(k);
        
        	
        	type[k] = z.getType();

        	if (type[k] == Variable.NUMERIC)
        	{
        		grouping[k] = new Grouping(z.getSortedNumValues());
        		n[k] = ((Grouping)grouping[k]).getNumberOfRanges();
        	}
        	else
        	{
        		Vector levels = new Vector();
        		String[] vals = z.getSortedCatValues();
        		for (int i = 0; i < vals.length; i++)
        		{
        			if (!levels.contains(vals[i]))
        				levels.addElement(vals[i]);
        		}
        		grouping[k] = levels;
        		n[k] = levels.size();
        	}

        	numVals[k] = z.getNumValues();
        }
    }

    public int getValsCount(int condIndex)
    {
    	int size = 0;
    	Variable z = (Variable)vz.elementAt(condIndex);
    		
    	if (type[condIndex] == Variable.NUMERIC)
    		size = z.getSortedNumValues().length;
    	else
    		size = z.getSortedCatValues().length;
    		
    	return size;
    }
    
    public int getCondCount()
    {
    		return zCount;
    }
    
    /**
     * Get the name associates with this object, usually it is z variable's name
     */
    public String getName(int condIndex)
	{

    		Variable z = (Variable)vz.elementAt(condIndex);
    		return z.getName();

    }

    /**
     * Get the name associates with this object
     */
    public String getName()
	{
    		String name = "";
    		for(int i=0; i < zCount; i++)
    		{
    			Variable z = (Variable)vz.elementAt(i);
    			if( i== 0 )
    				name = z.getName();
    			else 
    				name = name + "&" + z.getName();
    		}
    		
    		return name;

    }
    
    /**
     * Get the total number of global groups
     */
    public int getGroupCount()
    {
        int totalGroup=1;

        for(int i=0; i<zCount; i++)
        {
        		totalGroup = totalGroup * n[i];
        }
                
        return totalGroup;
    }
    
    /**
     * Get the number of groups for individual conditional variable
     */
    public int getGroupCount(int condIndex)
    {
    		return n[condIndex];
    }

    /**
     * @param groupCount : # of groups for an individual conditional variable
     */
    public void setGroupCount(int groupCount, int condIndex)
    {
    	    if(type[condIndex] == Variable.NUMERIC){
    	    	    ((Grouping)grouping[condIndex]).setNumberOfRanges(groupCount);
    	    	    n[condIndex] = ((Grouping)grouping[condIndex]).getNumberOfRanges();
    	    	    data.clearRankTable();
    	    }
    }
    
    public float getOverlap(int condIndex)
    {
    	    if(type[condIndex] == Variable.NUMERIC)
    	        return ((Grouping)grouping[condIndex]).getOverlap();
    	    else
    	    	    return 0;
    }
    
    public void setOverlap(float f, int condIndex)
    {
        	if(type[condIndex] == Variable.NUMERIC){
    	        ((Grouping)grouping[condIndex]).setOverlap(f);
    	        data.clearRankTable();
        	}
    }
    
    /**
     * Get the global group index for a certain observation on the specified row
     */
    public Set getGroupIndex(int index)
    {
    	    Set s = new HashSet();
    	    Set[] pos = new Set[zCount];

    	    for(int i=0; i<zCount; i++)
    	    {
    	    	switch (type[i])
    	    	{
            		case Variable.NUMERIC:
            			//double tmp = numVals[i][index];
            			double tmp = ((Variable)vz.elementAt(i)).getNumValues()[index];
            			pos[i] = ((Grouping)grouping[i]).getRangeIndex(tmp);
            			break;
            		case Variable.CATEGORICAL:
            			String tmps = (String)((Variable) vz.elementAt(i)).getValue(index);
            			int tmpindex = ((Vector)grouping[i]).indexOf(tmps);
            			pos[i] = new HashSet();
            			if(tmpindex != -1) pos[i].add(new Integer(tmpindex));
            			break;
    	    	}
    	    }

    	    Integer[][] x = new Integer[zCount][];
    	    if(zCount >= 1){
    	    		x[0] = (Integer[])pos[0].toArray(new Integer[0]);
    	    		s.addAll(pos[0]);
    	    }
    	    
    	    if(zCount >= 2)
    	    {
    	    		s.clear();
    	    		x[1] = (Integer[])pos[1].toArray(new Integer[0]);
    	    		for(int i = 0; i<x[0].length; i++)
    	    			for(int j=0; j<x[1].length; j++){
    	    				int tmp = ((Integer)x[0][i]).intValue() + n[0]*((Integer)x[1][j]).intValue();
    	    				s.add(new Integer(tmp));
    	    			}
    	    }
    	    
    	    if(zCount >=3)
    	    {
    	    		x[1] = (Integer[])s.toArray(new Integer[0]);
    	    		s.clear();
    	    		x[2] = (Integer[])pos[2].toArray(new Integer[0]);
    	    		for(int i = 0; i<x[1].length; i++)
    	    			for(int j=0; j<x[2].length; j++){
    	    				int tmp = ((Integer)x[1][i]).intValue() + n[0]*n[1]*((Integer)x[2][j]).intValue();
    	    				s.add(new Integer(tmp));
    	    			}    	    	
    	    }
    	    	
        return s;
    }

    /**
     * Get the names for all the groups
     */
    public String[] getGroupNames(int condIndex)
    {
        String[] s = null;

        if (type[condIndex] == Variable.CATEGORICAL)
        {
            s = new String[((Vector)grouping[condIndex]).size()];
            ((Vector)grouping[condIndex]).copyInto(s);
        }
        else
        {
            s=new String[getGroupCount(condIndex)];
            NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);
            for(int i=0;i<s.length;i++)
            {
                s[i]="("+nf.format(getLowerLimit(i,condIndex))+" - "+nf.format(getUpperLimit(i,condIndex))+")";
            }
        }

        return s;
    }
    
    /**
     * Get the name of the indexed group
     * @param index : group index of an individual conditional variable
     */
    public String getGroupName(int index, int condIndex)
    {
        String str = null;

        if (type[condIndex] == Variable.CATEGORICAL)
            str = (String) ((Vector)grouping[condIndex]).elementAt(index);
        else
        {
        	NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(3);
            str="("+nf.format(getLowerLimit(index,condIndex))+" - "+nf.format(getUpperLimit(index,condIndex))+")";
        }

        return str;
    }
    
    /**
     * Get the abbreviated name of the indexed group
     * @param index : global group index of a set of conditional variables
     */
    public String getGroupName(int index)
	{
    	int[] indexes = toGroupIndexArray(index);
    		
    	if(zCount == 0) return null;
    		
    	String str = null;

    	if (type[0] == Variable.CATEGORICAL)
            str = (String) ((Vector)grouping[0]).elementAt(indexes[0]);
    	else
    	{
            NumberFormat nf=NumberFormat.getInstance();
            nf.setMaximumFractionDigits(1);
            str="("+nf.format(getLowerLimit(indexes[0],0))+" - "+nf.format(getUpperLimit(indexes[0],0))+")";
        }
    		
    		if(zCount > 1) str = str + "... ";
    		
        return str;
    	
	}

    /**
     * Get the abbreviated names of all the groups
     */
    public String[] getGroupNames()
    {
        String[] s = new String[getGroupCount()];
        for(int i = 0; i < s.length; i++)
        {
        		s[i] = getGroupName(i);
        }
        return s;
    }

    /**
     * Get the minimum z value for an indexed group
     * @param index : group index of an individual conditional variable
     */
    public double getLowerLimit(int index, int condIndex)
    {
        return ((Grouping)grouping[condIndex]).getRange(index).from;
    }

    /**
     * Get the maximum z value for an indexed group
     * @param index : group index of an individual conditional variable
     */
    public double getUpperLimit(int index, int condIndex)
    {
        return ((Grouping)grouping[condIndex]).getRange(index).to;
    }

    /**
     * Get the type of this GroupMaker, CONTINUOUS or DISCRETE
     */
    public int getType(int condIndex)
    {
        if (type[condIndex] == Variable.NUMERIC)
            return CONTINUOUS;
        else
            return DISCRETE;
    }

    /**
     * Transform a global group index into individual group indexes for each conditional variables
     */
    public int[] toGroupIndexArray(int index)
    {
    	int[] indexes = new int[zCount];
    		
    	if(zCount == 3 ){
    		indexes[2] = index / (n[0]*n[1]);
    		indexes[1] = (index % (n[0]*n[1])) / n[0];
    	   	indexes[0] = (index % (n[0]*n[1])) % n[0];
    	}
    	else if(zCount == 2 ){
    		indexes[1] = index / n[0];
    	   	indexes[0] = index % n[0];
    	}
    	else if(zCount == 1 ){
		   	indexes[0] = index;
		} 
    	
    	return indexes;
    }
    
    /**
     * Transform a set of individual group indexes into one global group index 
     */
    public int toGlobalIndex(int[] index)
    {
    		int g = 0;
    		if(zCount >=1 ) g += index[0];
    		if(zCount >=2 ) g += index[1]*n[0];
    		if(zCount >=3 ) g += index[2]*n[0]*n[1];
    		
    		return g;
    }
    
    
    /**
     * @param index : global group index
     */
    public double[] getConstraintVector(int index)
    {
    	for(int k=0; k < zCount; k++)
    		numVals[k] =  ((Variable)vz.elementAt(k)).getNumValues(); //added by djluo for case z changes. 3/29/07
    	
    		
        double[][] array = new double[numVals[0].length][zCount];
        double[] result = new double[numVals[0].length];
        int[] x = toGroupIndexArray(index);
        
        for(int j=0; j < zCount; j++)
        {
        		for (int i = 0; i < array.length; i++)
        		{
        			if (type[j] == Variable.NUMERIC)
        			{
        				Set r = ((Grouping)grouping[j]).getRangeIndex(numVals[j][i]);
        				if (r.contains(new Integer(x[j])))
        					array[i][j] = 1;
        				else
        					array[i][j] = 0;
        			}
        			else
        			{
        				if (numVals[j][i] == x[j])
        					array[i][j] = 1;
        				else
        					array[i][j] = 0;
        			}
        		}
        }

        for(int i = 0; i < array.length; i++)
        {
        		result[i] = 1;
        		for (int j=0; j < zCount; j++)
        		{
        			result[i] = result[i] * array[i][j];
        		}
        }
        
        return result;
    }

    public double[][] getConstraintMatrix()
    {
    	for(int k=0; k < zCount; k++)
    		numVals[k] =  ((Variable)vz.elementAt(k)).getNumValues(); //added by djluo for case z changes. 3/29/07

        double[][] ret=new double[numVals[0].length][getGroupCount()];
            
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
        private float overlap;
        private List rangeList=new ArrayList();
        private double[] values;


        Grouping(double[] values)
        {
            groupCount=getBeginGroupCount(values.length);
            overlap=0;
            this.values = values;
            calcFields();

        }

        private void calcFields()
        {
        	   /* r=groupSize, n=values.length, k=groupCount, f=overlap
        	    * r = n / [ k(1-f) +f]
        	    * ith lower bound = value at (i-1)(1-f)r 
        	    * ith upper bound = value at [r+(i-1)(1-f)r - 1] 
        	    */
            int groupSize=Math.round(values.length/(groupCount*(1-overlap)+overlap));
            int lowerIndex,upperIndex;

            rangeList.clear();
            for(int i=1; i<=groupCount; i++)
            {
            	   lowerIndex = Math.round((i-1)*(1-overlap)*groupSize);
            	   if(i < groupCount)
            	       upperIndex = Math.round(groupSize+(i-1)*(1-overlap)*groupSize-1);
            	   else
            	   	   upperIndex = values.length - 1;

            	   DoubleRange range=new DoubleRange(values[lowerIndex], values[upperIndex]);
            	   rangeList.add(range);
            }        	
        }
        
        public DoubleRange getRange(int groupIndex)
        {
            return (DoubleRange)rangeList.get(groupIndex);
        }

        public int getNumberOfRanges()
        {
            return rangeList.size();
        }
        
        public void setNumberOfRanges(int count)
        {
        	    groupCount = count;
        	    calcFields();
        }

        public float getOverlap()
        {
        	    return overlap;
        }
        
        public void setOverlap(float f)
        {
        	    overlap = f;
        	    calcFields();
        }
        
        
        public Set getRangeIndex(double value)
        {
            HashSet indexes = new HashSet();
            for(int i=0;i<rangeList.size();i++)
            {
                DoubleRange range=(DoubleRange)rangeList.get(i);
                if(range.from <=value && range.to>=value)
                {
                    indexes.add(new Integer(i));
                }
            }

            return indexes;
        }

        private int getBeginGroupCount(int totalElements)
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
        grouping.setOverlap(0.5f);
        int n=grouping.getNumberOfRanges();
        System.out.println("n="+n);
        for(int i=0; i<n; i++)
        {
        	    System.out.print(grouping.getRange(i).from +"-"+ grouping.getRange(i).to + ",");
        }
    }

}