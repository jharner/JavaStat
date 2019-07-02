package wvustat.data;

import java.util.*;
import java.awt.Color;
import java.rmi.*;

import javax.swing.event.EventListenerList;

import wvustat.interfaces.*;
import wvustat.util.MathUtils;
import wvustat.modules.GroupMaker;
import wvustat.modules.EqualCountGrouper;
import wvustat.modules.Transformation;
import Jama.Matrix;
import gnu.trove.TIntArrayList;


/**
 *	DataSetImpl implements the DataSet interface. 
 *
 *
 */

public class DataSetImpl implements DataSet
{

    //name of this dataset
    private String name;

    private static int numberOfDatasets = 0;
    private int id;

    private Vector variables, x_vars, y_vars, z_vars;

    //Variable state and mask and missing value
    private BoolVariable stateVar, maskVar, missingVar;
    
    /**
     * variable maps to a number which is the frequency it is used in plots.  
     * null or zero entry means the variable is not used in any plots.
     */
    private Hashtable plot_vars = new Hashtable();
    											  
    /**
     * module id maps to a vector of all the variables it uses.
     */
    private Hashtable module_vars = new Hashtable();

    //Variable color;
    private Vector colorVar;
    private Vector markerVar;

    private Vector observers;
        
    /**
     *  <pre>
     *  yylabel
     *  yyweight
     *  *yymask: change value, change mask, change missing value
     *  *yystate
     *  *yycolor
     *  new_obs_added
     *  *obs_deleted
     *  yyrole: change the role
     *  *add_variable
     *  change_variable: change variable type (numerical, categorical)
     *  *delete_variable
     *  yyrownumber
     *  
     *  * means the msg needs to be handled in observer's update method.
     *  </pre>
     */
    private String msg;

    //The number of observations in this data set
    private int size;

    private Variable labelVar, freqVar; 

    /**
     * Uppercase variable name maps to the variable.
     */
    private Hashtable vars_table = new Hashtable();
    private Hashtable rank_table = new Hashtable();//rank_table is used for ranking efficiently

    private boolean useRowNumber = false;

    private String description, reference, authorization, subject;
    
    //for transformation
    private Hashtable xform_table = new Hashtable();
    
    
    //the following are used for linkage between table and its subset tables
    private DataSet parent;
    private int[] linkedRows, linkedCols;
    private EventListenerList eList = new EventListenerList();

    
    /**
     * Constructor
     * Creates a new DataSetImpl object with variables supplied in a vector
     *
     */
    public DataSetImpl(String name, Vector vars)
    {
        super();
        this.id = ++numberOfDatasets;
        this.name = name;

        stateVar = (BoolVariable) vars.elementAt(0);
        vars.remove(stateVar);
        colorVar = (Vector) vars.elementAt(0);
        vars.remove(colorVar);
        maskVar = (BoolVariable) vars.elementAt(0);
        vars.remove(maskVar);

        observers = new Vector();

        x_vars = new Vector();
        y_vars = new Vector();
        z_vars = new Vector();

        variables = vars;

        for (int i = 0; i < variables.size(); i++)
        {
            Variable singleVar = (Variable) variables.elementAt(i);
            ((VariableImpl) singleVar).setDataSet(this);

            vars_table.put(singleVar.getName().toUpperCase(), singleVar);

            switch (singleVar.getRole())
            {
                case DataSet.X_ROLE:
                    x_vars.addElement(singleVar);
                    break;
                case DataSet.Y_ROLE:
                    y_vars.addElement(singleVar);
                    break;
                case DataSet.Z_ROLE:
                    z_vars.addElement(singleVar);
                    break;
                case DataSet.L_ROLE:
                    if (labelVar != null)
                    {
                        labelVar.setRole(DataSet.U_ROLE);
                    }
                    labelVar = singleVar;
                    break;
                case DataSet.F_ROLE:
                	if(freqVar != null) 
                	{
                		freqVar.setRole(DataSet.U_ROLE);
                	}
                    freqVar = singleVar;
            }
        }

        size = ((Variable) variables.elementAt(0)).getSize();
        
        
        markerVar = new Vector();
        for (int i = 0; i < size; i++)
            markerVar.addElement(new Integer(0));
        
        
        missingVar = new BoolVariable(size);
        updateMissing();

    }
    
    /**
     * Constructor
     * Creates a subset DataSetImpl object with indics in parent DataSet
     *
     */
    public DataSetImpl(String name, Vector vars, DataSet parent, int[] rows, int[] cols) 
    {
    	this(name, vars);
    	this.parent = parent;
    	this.linkedRows = rows;
    	this.linkedCols = cols;
    	parent.addDataChangeListener(this);
    }

    public int getId()
    {
    	return id;
    }
    
    public BoolVariable getStateVariable()
    {
        return stateVar;
    }

    /*public BoolVariable getMaskVariable()
    {
        return maskVar;
    }*/

    public Vector getColorVariable()
    {
        return this.colorVar;
    }

    /**
     *	Get the name of this DataSetImpl instance
     */
    public synchronized String getName()
    {
        return name;
    }
    
    public synchronized void setName(String name)
    {
    	this.name = name;
    }

    /**
     * Get the total number of variables in this DataSetImpl instance
     */
    public synchronized int getVariableCount()
    {
        return variables.size();
    }

    /**
     * Get the indexed variable
     */
    public synchronized Variable getVariable(int index)
    {
        Variable v = (Variable) variables.elementAt(index);

        return v;
    }

    public synchronized String[] getVariableNames()
    {
        Enumeration e = vars_table.keys();
        String[] ret = new String[vars_table.size()];

        int index = 0;
        while (e.hasMoreElements())
            ret[index++] = e.nextElement().toString();

        return ret;
    }


    /**
     * Get the dimension  of this DataSetImpl instance
     */
    public synchronized int getSize()
    {
        return size;
    }

    /**
     * Get all variables in this DataSetImpl instance
     */
    public synchronized Vector getVariables() 
    {
    	//modified by djluo on Nov,2006
    	
    	Vector v = new Vector();
    	for (int i=0; i < variables.size(); i++){
    		v.addElement((Variable) variables.elementAt(i));
    	}
    	return v;
    	
        //return variables;
    }

    /**
     * Get the variable identified by varname
     */
    public synchronized Variable getVariable(String varname) 
    {
        return (Variable) (vars_table.get(varname.toUpperCase()));
    }

    /**
     * Get all x varaibles
     */
    public synchronized Vector getXVariables()
    {
    	//modified by djluo on Nov,2006 to avoid change of variable role from influence plot. 
    	
    	Vector v = new Vector();
    	for (int i=0; i < x_vars.size(); i++){
    		v.addElement((Variable) x_vars.elementAt(i));
    	}
    	return v;
    	
        //return x_vars;
    }

    /**
     * Get all y variables
     */
    public synchronized Vector getYVariables()
    {
    	//modified by djluo on Nov,2006
    	
    	Vector v = new Vector();
    	for (int i=0; i < y_vars.size(); i++){
    		v.addElement((Variable) y_vars.elementAt(i));
    	}
    	return v;
    	
        //return y_vars;
    }

    /**
     * Get all z variables
     */
    public synchronized Vector getZVariables()
    {
    	//modified by djluo on Nov,2006
    	
    	Vector v = new Vector();
    	for (int i=0; i < z_vars.size(); i++){
    		v.addElement((Variable) z_vars.elementAt(i));
    	}
    	return v;
    	
        //return z_vars;
    }

    /**
     * Get the label variable
     */
    public synchronized Variable getLabelVariable()
    {
        return labelVar;
    }

    public synchronized Variable getFreqVariable()
    {
        return freqVar;
    }


    /**
     * Set the label variable
     */
    public synchronized void setLabelVariable(Variable var)
    {
    	if (labelVar != null)  //added by djluo. Reset the old label variable.
        {
    	    labelVar.setRole(DataSet.U_ROLE);
        }
        labelVar = var; 
        msg = "yylabel";
        asyncNotify();
    }


    /**
     * Get the state value for all observations in a vector
     */
    public synchronized boolean[] getStates()
    {
        Vector v = stateVar.getValues();
        boolean[] bl = new boolean[v.size()];
        for (int i = 0; i < v.size(); i++)
        	bl[i] = ((Boolean)v.elementAt(i)).booleanValue();
        return bl;
    }

    /**
     * Get the color values for all observations in a vector
     */
    public synchronized Vector getColors() 
    {
        return colorVar;
    }

    /**
     * Get the mask values for all observations in a vector
     */
    /*public synchronized Vector getMasks()
    {
        return maskVar.getValues();
    }*/

    /**
     * Get the mask(missing) value for the indexed observation.
     * In order to avoid replace lots of invoke statements in related 
     * program, use getMask() to replace getMissing() as interface to 
     * implement missing value function easily.
     * 
     * @see getMissing(int i)
     * @author djluo
     */
    public synchronized boolean getMask(int i)
    {
        //return maskVar.getValue(i);
    	
    	//modified by djluo
    	return getMissing(i);
    }

    /**
     * Get the mask value for the indexed observation
     * Substitute for the old getMask(int i) method
     * @see getMask(int i)
     * @author djluo
     */
    public synchronized boolean getTrueMask(int i)
    {
        return maskVar.getValue(i);
    }
    
    /**
     * Get the color value for the indexed observation
     */
    public synchronized Color getColor(int i) 
    {
        return (Color) (colorVar.elementAt(i));
    }

    public Integer getMarker(int i)
    {
    	return (Integer) (markerVar.elementAt(i));
    }
    
    /**
     * Get the state value for the indexed observation
     */
    public synchronized boolean getState(int i) 
    {
        return stateVar.getValue(i);
    }

    /**
     * Change the value for a indexed observation, 
     * 
     */
    private synchronized void setVarValue2(String varName, Object obj, int i) 
    {
        Variable var = getVariable(varName);
        if (var == null) return;

        var.setValue(obj, i);

        updateMissing(i);
        rank_table.clear();
                
        msg="yymask";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, i, variables.indexOf(var) + 2, obj));
    }

    /**
     * Change the value for a indexed observation, 
     * 
     */
    public synchronized void setVarValue(String varName, Object obj, int i)
    {
    	((DataSetImpl)getRoot()).setVarValue2(varName, obj, getRootRowId(i));
    }

    
    private synchronized void setState2(boolean bl, int i)
    {    	
        stateVar.setValueAt(bl, i);

        msg = "yystate";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, i, bl));
    }
    
    /**
     * change state for the indexed observation
     *
     * @param bl the new stat value
     * @param i the index of the observation

     */
    public synchronized void setState(boolean bl, int i)
    {
    	((DataSetImpl)getRoot()).setState2(bl, getRootRowId(i));
    }

    /**
     * Set all state values to false, i.e., deselect all observations
     *
     */
    public synchronized void clearStates() 
    {
    	//for (int i = 0; i < size; i++)
    	//	setState(false, i);
    	
    	//set rows to number from 0 to (size - 1).
    	int[] rows = new int[size];
    	boolean[] bl = new boolean[size];
    	for (int i = 0; i < size; i++) {
    		rows[i] = i;
    		bl[i] = false;
    	}
    	setStates(bl, rows);
    }
    
    public void setStates(boolean[] bl)
    {
    	//set rows to number from 0 to (size - 1).
    	int[] rows = new int[size];
    	for (int i = 0; i < size; i++) 
    		rows[i] = i;
    	
    	setStates(bl, rows);
    }
    
    public void setStates(boolean[] bl, int[] indices)
    {
    	int[] rows = new int[indices.length];
    	for (int i = 0; i < indices.length; i++)
    		rows[i] = getRootRowId(indices[i]);
    	
    	((DataSetImpl)getRoot()).setStates2(bl, rows);
    }
    
    private void setStates2(boolean[] bl, int[] indices)
    {
    	for (int i = 0; i < indices.length; i++)
    		stateVar.setValueAt(bl[i], indices[i]);
    	
    	msg = "yystate";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, indices, bl));
    }

    private synchronized void setColor2(Color c, int i) 
    {
        colorVar.setElementAt(c, i);

        msg = "yycolor";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, i, 0, c));
    }
    
    /**
     * Change the color for the indexed observation
     */
    public synchronized void setColor(Color c, int i) 
    {
    	((DataSetImpl)getRoot()).setColor2(c, getRootRowId(i));
    }
    
    private synchronized void setMarker2(Integer m, int i) 
    {
        markerVar.setElementAt(m, i);

        msg = "yycolor";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, i, 0, m));
    }
    
    /**
     * Change the color for the indexed observation
     */
    public synchronized void setMarker(Integer m, int i) 
    {
    	((DataSetImpl)getRoot()).setMarker2(m, getRootRowId(i));
    }
    

    private synchronized void setMask2(boolean bl, int i) 
    {
        maskVar.setValueAt(bl, i);
        
        updateMissing(i);
        rank_table.clear();
        
        msg = "yymask";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, i, 1, new Boolean(bl)));
    }
    
    /**
     * Change the true mask value for the indexed observation
     */
    public synchronized void setMask(boolean bl, int i)
    {
    	((DataSetImpl)getRoot()).setMask2(bl, getRootRowId(i));
    }
    
    
    private synchronized void setMasks2(boolean bl, int[] index) 
    {
    	for (int i=0; i<index.length; i++){
    		maskVar.setValueAt(bl, index[i]);
    		updateMissing(index[i]);
    	}
    	rank_table.clear();
    	
    	msg = "yymask";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, index, bl));
    }
    
    /**
     * Change the true mask values for the indexed observations
     */
    public synchronized void setMasks(boolean bl, int[] index) 
    {
    	int[] rows = new int[index.length];
    	for (int i = 0; i < index.length; i++)
    		rows[i] = getRootRowId(index[i]);
    	
    	((DataSetImpl)getRoot()).setMasks2(bl, rows);
    }


    /**
     * Add remote observer to this DataSetImpl instance
     */
    public synchronized void addRemoteObserver(RemoteObserver obs) 
    {
        observers.addElement(obs);
    }

    /**
     * Remove remote observer from this DataSetImpl instance
     */
    public synchronized void removeRemoteObserver(RemoteObserver obs) 
    {
        observers.removeElement(obs);
    }

    /**
     * Remove all remote observers from this DataSetImpl instance.
     */
    public synchronized void removeAllObservers()
    {
        observers.removeAllElements();
    }

    /**
     * Get an Enumeration of the remote observers
     */
    public synchronized Enumeration getRemoteObservers()
    {
        return observers.elements();
    }

    public synchronized void addObservations(Vector rows) 
    {
        for (int i = 0; i < rows.size(); i++)
        {
            addObservation((Vector) rows.elementAt(i));
        }

        size += rows.size();

        msg = "new_obs_added";
        asyncNotify();
    }


    private void addObservation(Vector row) 
    {
        if (row.size() != getVariableCount() + 3)
            return; //potential bug which will cause inconsistency in dataset size and variable size.

        try
        {   // Sequence of obs should be: color, mask, state, other variables.
        	
            Object obj = row.firstElement();
            colorVar.addElement(obj);
            row.removeElement(obj);      
            
            markerVar.addElement(new Integer(0));

            obj = row.firstElement();
            maskVar.addValue(obj);
            row.removeElement(obj);

            obj = row.firstElement();
            stateVar.addValue(obj);
            row.removeElement(obj);

            for (int i = 0; i < row.size(); i++)
            {
                Variable v = (Variable) variables.elementAt(i);
                v.addValue(row.elementAt(i));
            }

            missingVar.addValue(new Boolean(true));//added by djluo
            
            rank_table.clear();//added by djluo
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("Error adding observation to dataset");
            System.err.println(e);
            return;
        }

    }

    private void deleteObservation(int index) 
    {

        if (index >= 0 && index < size)
        {
            stateVar.removeValueAt(index);
            maskVar.removeValueAt(index);
            colorVar.removeElementAt(index);
            markerVar.removeElementAt(index);

            for (int j = 0; j < variables.size(); j++)
            {
                Variable v = (Variable) variables.elementAt(j);
                v.removeValueAt(index);
            }

            missingVar.removeValueAt(index);//added by djluo
            rank_table.clear();//added by djluo
            
            size--;

        }
    }
    
    public synchronized void deleteObservations(int[] index)
	{    	
    	for(int i=0; i < index.length; i++){
    		deleteObservation(index[i] - i); //index[i]
    	}
    		
    	msg = "obs_deleted";
    	asyncNotify();
    	
    	fireDataChanged(new DataChangeEvent(this, index));
	}
    

    /**
     * change the role of a variable
     */
    public synchronized void setRole(Variable v, int role)
    {
        if (!vars_table.containsKey(v.getName().toUpperCase())) {
            System.out.println("Variable with name " + v.getName() + " not in data set.");
            return;
        }

        int old_role = v.getRole();

        if (old_role == role) return;
        if (role == DataSet.L_ROLE)
        {
            setRowNumberEnabled(false);
            if (labelVar != null)
                labelVar.setRole(DataSet.U_ROLE);
        }

        if (role == DataSet.F_ROLE)  // added by djluo
        {
            if (freqVar != null) 
     	       freqVar.setRole(DataSet.U_ROLE);
        }
        
        switch (old_role)
        {
            case DataSet.X_ROLE:
                x_vars.removeElement(v);
                break;
            case DataSet.Y_ROLE:
                y_vars.removeElement(v);
                break;
            case DataSet.Z_ROLE:
                z_vars.removeElement(v);
                break;
            case DataSet.L_ROLE:
                labelVar = null;
                break;
            case DataSet.F_ROLE:
                freqVar = null;
                break;
        }

        v.setRole(role);

        switch (role)
        {
            case DataSet.X_ROLE:
                x_vars.addElement(v);
                break;
            case DataSet.Y_ROLE:
                y_vars.addElement(v);
                break;
            case DataSet.Z_ROLE:
                z_vars.addElement(v);
                break;
            case DataSet.L_ROLE:
                labelVar = v;
                break;
            case DataSet.F_ROLE:
                freqVar = v;
                break;
        }

        msg = "yyrole";
        asyncNotify();
    }
    
    /**
     * Notify all remote observers
     *
     *
     */
    public void notifyAll(String msg)
    {

        RemoteObserver ro = null;
        int i = 0;
        while (i < observers.size())
        {
            try
            {
                ro = ((RemoteObserver) observers.elementAt(i));
                ro.update(msg);
                i++;
            }
            catch (RemoteException error)
            {
                error.printStackTrace();
                observers.removeElement(ro);

            }
        }

    }

    private void asyncNotify()
    {
        if (msg != null)
        {
            notifyAll(msg);
        }
        msg = null;
    }


    /**
     * Get the rank of each observation. Observations are ranked by variables
     * whose names are specified in Vector varNames. The order of variable in
     * that vector determines the order of sorting.
     * 
     */
    public int[] getRanks(Vector varNames)
    {
        int[] rank = new int[size];

        double[][] values = new double[varNames.size()][];

        for (int i = 0; i < varNames.size(); i++)
        {
            Variable v = getVariable((String) varNames.elementAt(i));
            values[i] = v.getNumValues();
        }

        Observation[] obs = new Observation[size];

        for (int i = 0; i < size; i++)
        {
            double[] tmp = new double[varNames.size()];

            for (int j = 0; j < tmp.length; j++)
            {
                tmp[j] = values[j][i];
            }

            obs[i] = new Observation(tmp, i);
        }

        MathUtils.InsertionSort(obs);

        for (int i = 0; i < obs.length; i++)
        {
            rank[obs[i].getID()] = i;
        }

        return rank;
    }

    /**
     * Get the ranks of observations excluding the ones that has been masked out
     * The returned array does not always have corresponding sequence number to 
     * dataset records. 
     */
    public int[] getUnmaskedRanks(Vector varNames)
    {
        int[] rank;

        double[][] values = new double[varNames.size()][];

        for (int i = 0; i < varNames.size(); i++)
        {
            Variable v = getVariable((String) varNames.elementAt(i));
            values[i] = v.getNumValues();
        }


        int[] maskValues = missingVar.getDummyValues();
        int totalMasked = 0;
        for (int i = 0; i < size; i++)
        {
            totalMasked += maskValues[i];
        }

        Observation[] obrs = new Observation[size - totalMasked];
        int index = 0;
        for (int i = 0; i < size; i++)
        {
            if (getMask(i) == false)
            {
                double[] tmp = new double[varNames.size()];

                for (int j = 0; j < tmp.length; j++)
                {
                    tmp[j] = values[j][i];
                }

                obrs[index] = new Observation(tmp, index);
                index++;
            }
        }

        MathUtils.InsertionSort(obrs);

        rank = new int[obrs.length];
        for (int i = 0; i < obrs.length; i++)
        {
            rank[obrs[i].getID()] = i;
        }

        return rank;
    }


    /**
     * get the rank of each observation. An observation is ranked based on the value of num_var with possible grouping determined by DividePolicy.
     *
     * @param policy the algorithm used to divide obs into groups @see DividePolicy
     * @param num_var the name of the numerical variable that is used to sort obs
     */
    public int[] getRanks(DividePolicy policy, String num_var) 
    {
        Object obj = null;
        int[] result = null;

        //result = rankObs(policy, num_var, maskVar.getValues());
        
        //result = rankObs(policy, num_var, missingVar.getValues());
        
        /* modified by djluo
         
        if (maskChanged)
        {
            result = rankObs(policy, num_var, maskVar.getValues());
            maskChanged = false;
        }
        else
        {
        	     * The following block was commented by djluo 6/20/2005.
	         * use storage rank will cause problem for new inserted rows.
	    */       
            if (policy != null)
                obj = rank_table.get(policy.getName() + num_var);
            else
                obj = rank_table.get(num_var);
            

            if (obj != null)
                result = (int[]) obj;
            else
            {
                result = rankObs(policy, num_var, missingVar.getValues());
            }
        /*}*/
        
            //System.out.println("\n Rank");
            //for(int i=0;i<result.length;i++) System.out.print(result[i]+" ");

        return result;
    }

    public int[][] getRanks(EqualCountPolicy policy, String num_var) 
	{
    	Object obj = null;
        int[][] result = null;
        if (policy != null)
            obj = rank_table.get(policy.getName() + num_var);
        else
            obj = rank_table.get(num_var);
        

        if (obj != null)
            result = (int[][]) obj;
        else
        {
            result = rankObs(policy, num_var, missingVar.getValues());
        } 
        return result;
	}
    
    private int[][] rankObs(EqualCountPolicy policy, String num_var, Vector masks) 
    	{
    		int[][] rank = null;
    		
    		Variable nVar = getVariable(num_var);
    		Variable cVar = null;
         if (policy != null)
                cVar = getVariable(policy.getName(0));
    		
    		if (nVar == null || nVar.getType() != Variable.NUMERIC)
             return null;
    		if (policy != null && cVar == null)
             return null;
    		
    		if(policy == null)
    			rank = new int[1][nVar.getSize()];
    		else
    			rank = new int[policy.getGroupCount()][nVar.getSize()];
    		
    		for (int i = 0; i < rank.length; i++)
    			for (int j = 0; j < rank[i].length; j++)
        		{
                rank[i][j] = 1;//rank start from 1
             }

         double[] nums = nVar.getNumValues();
         Vector cats = null;
         if (cVar != null)
             cats = cVar.getValues();

            for (int i = 0; i < nums.length; i++)
            {
                boolean maski = false;
                if (masks != null)
                    maski = ((Boolean) masks.elementAt(i)).booleanValue();
                
                for (int j = i + 1; j < nums.length; j++)
                {
                    boolean maskj = false;
                    if (masks != null)
                        maskj = ((Boolean) masks.elementAt(j)).booleanValue();

                    if (!maski && !maskj)
                    {

                        if (cats != null)
                        {
                        	  Set intersection = policy.getGroupIndex(i);
                        	  intersection.retainAll(policy.getGroupIndex(j));
                        	  Iterator it = intersection.iterator();
                        	  
                        	  while(it.hasNext()){
                        	  	  int k = ((Integer)it.next()).intValue();
                        	  	  if(nums[i] > nums[j])
                                    rank[k][i]++;
                                else   
                                    rank[k][j]++;
                        	  }
                        	  /* The codes above are equivalent to the following commented codes, but more efficient.  
                        	  for (int k = 0; k < policy.getGroupCount(); k++){
                        	  	  // if(policy.getGroupIndex(i).contains(new Integer(k)) &&
                        	  	  //      policy.getGroupIndex(j).contains(new Integer(k)))
                        	  	   
                                if (intersection.contains(new Integer(k)))
							  {
							       if(nums[i] > nums[j])
                                         rank[k][i]++;
                                     else   
                                         rank[k][j]++;
                                }
                        	  }*/ 
                        }
                        else
                        {
                            if (nums[i] > nums[j])
                                rank[0][i]++;
                            else    /* (nums[i] <= nums[j]) */
                                rank[0][j]++;
                        }
                     }

                  }//for j
             }//for i
            
            if (policy != null)
                rank_table.put(policy.getName() + num_var, rank);
            else
                rank_table.put(num_var, rank);
            
    		return rank;
    	}
    
    
    /**
     * Get the frequency matrix for given categorical variables.
     *
     * @param catVar1 the name of the first categorical variable
     * @param catVar2 the name of the second categorical variable
     */
    public double[][] getFreqMatrix(String catVar1, String catVar2) 
    {
        Variable v1 = getVariable(catVar1);
        Variable v2 = getVariable(catVar2);

        double[][] array1=null;
        double[][] array2=null;

        if(v1.getType()==Variable.NUMERIC)
        {
            GroupMaker groupMaker=new GroupMaker(v1);
            array1=groupMaker.getConstraintMatrix();
        }
        else
        {
            array1=v1.getDummyValues();
        }

        if(v2.getType()==Variable.NUMERIC)
        {
            GroupMaker groupMaker=new GroupMaker(v2);
            array2=groupMaker.getConstraintMatrix();
        }
        else
        {
            array2=v2.getDummyValues();
        }

        int[] maskValues = missingVar.getDummyValues();
        double[] constrt = new double[maskValues.length];
        for (int i = 0; i < constrt.length; i++)
        {
            constrt[i] = 1 - maskValues[i];
        }

        array1 = applyConstraints(array1, constrt);
        if (getFreqVariable() != null)
        {
            double[] fVals = getFreqVariable().getNumValues();
            array1 = applyConstraints(array1, fVals);
        }

        Matrix m1 = new Matrix(array1);
        Matrix m2 = new Matrix(array2);

        Matrix result = m1.transpose().times(m2);

        return result.getArray();
    }

    /**
     * Get the frequency matrix for given categorical variables.
     *
     * @param catVar1 the name of the first categorical variable
     * @param grouper the collection of the conditional variables
     */
    public double[][] getFreqMatrix(String catVar1, EqualCountGrouper grouper) 
    {
        Variable v1 = getVariable(catVar1);
        //Vector v2 = catVar2;

        double[][] array1=null;
        double[][] array2=null;

        if(v1.getType()==Variable.NUMERIC)
        {
            GroupMaker groupMaker=new GroupMaker(v1);
            array1=groupMaker.getConstraintMatrix();
        }
        else
        {
            array1=v1.getDummyValues();
        }

        
        //EqualCountGrouper grouper=new EqualCountGrouper(v2, this);
        array2=grouper.getConstraintMatrix();
        

        int[] maskValues = missingVar.getDummyValues();
        double[] constrt = new double[maskValues.length];
        for (int i = 0; i < constrt.length; i++)
        {
            constrt[i] = 1 - maskValues[i];
        }

        array1 = applyConstraints(array1, constrt);
        if (getFreqVariable() != null)
        {
            double[] fVals = getFreqVariable().getNumValues();
            array1 = applyConstraints(array1, fVals);
        }

        Matrix m1 = new Matrix(array1);
        Matrix m2 = new Matrix(array2);

        Matrix result = m1.transpose().times(m2);

        return result.getArray();
    }    
    
    public double[][] getFreqMatrix(String catVar1, String catVar2, double[] constrt1, double[] constrt2) 
    {
        Variable v1 = getVariable(catVar1);
        Variable v2 = getVariable(catVar2);

        if (v1.getType() == Variable.NUMERIC || v2.getType() == Variable.NUMERIC)
            return null;


        double[][] array1 = v1.getDummyValues();
        double[][] array2 = v2.getDummyValues();
        

        int[] maskValues = missingVar.getDummyValues();
        double[] maskConstrt = new double[maskValues.length];
        for (int i = 0; i < maskConstrt.length; i++)
        {
            maskConstrt[i] = 1 - maskValues[i];
        }

        array1 = applyConstraints(array1, maskConstrt);
        array1 = applyConstraints(array1, constrt1);
        array1 = applyConstraints(array1, constrt2);

        if (getFreqVariable() != null)
        {
            double[] fVals = getFreqVariable().getNumValues();
            for (int i = 0; i < fVals.length; i++)
            	if (Double.isNaN(fVals[i]))	
            		fVals[i] = 0;
            
            array1 = applyConstraints(array1, fVals);
        }

        Matrix m1 = new Matrix(array1);
        Matrix m2 = new Matrix(array2);

        Matrix result = m1.transpose().times(m2);

        return result.getArray();
    }

    public double[][] getSumMatrix(String numVar, String catVar1, String catVar2, double[] constrt1, double[] constrt2) 
    {
        double[][] sums = null;

        Variable nVar = getVariable(numVar);
        Variable v1 = getVariable(catVar1);
        Variable v2 = getVariable(catVar2);

        if (nVar == null)
            return null;

        if (v1 == null && v2 == null)
            return null;

        if (v1.getType() == Variable.NUMERIC || v2.getType() == Variable.NUMERIC)
            return null;

        int dim1 = v1.getDistinctCatValues().length;
        int dim2 = v2.getDistinctCatValues().length;

        sums = new double[dim2][dim1];

        double[][] values = new double[1][size];
        values[0] = nVar.getNumValues();
        
        for(int i=0; i< values[0].length; i++){
        	    if(Double.isNaN(values[0][i])) values[0][i]=0; //added by djluo for missing value
        }

        double[][] array1 = v1.getDummyValues();
        double[][] array2 = v2.getDummyValues();

        int[] maskValues = missingVar.getDummyValues();
        double[] maskConstrt = new double[maskValues.length];
        for (int i = 0; i < maskConstrt.length; i++)
        {
            maskConstrt[i] = 1 - maskValues[i];
        }

        array1 = applyConstraints(array1, maskConstrt);
        array1 = applyConstraints(array1, constrt1);
        array1 = applyConstraints(array1, constrt2);

        Matrix m2 = new Matrix(array2).transpose();
        array2 = m2.getArray();

        Matrix yM = new Matrix(values);
        Matrix m1 = null;
        double[][] tmpArray = null;
        for (int i = 0; i < array2.length; i++)
        {
            tmpArray = applyConstraints(array1, array2[i]);
            m1 = new Matrix(tmpArray);

            double[][] oneRow = yM.times(m1).getArray();
            sums[i] = oneRow[0];
        }

        return new Matrix(sums).transpose().getArray();


    }

    private double[][] applyConstraints(double[][] array, double[] constrt)
    {
        if (constrt == null)
            return array;

        double[][] ret = new double[array.length][array[0].length];

        for (int i = 0; i < constrt.length; i++)
        {
            for (int j = 0; j < array[0].length; j++)
            {
                ret[i][j] = array[i][j] * constrt[i];
            }
        }

        return ret;

    }

    /**
     * Ranks observations
     *
     * @param policy the algorithm to divide obs into groups
     * @param num_var the name of the numerical variable
     * @param masks the mask values of all observations, used to determine if obs shoudl be excluded or not
     */
    private int[] rankObs(DividePolicy policy, String num_var, Vector masks) 
    {
        int[] rank = null;

        Variable nVar = getVariable(num_var);
        Variable cVar = null;
        if (policy != null)
            cVar = getVariable(policy.getName());

        if (nVar == null || nVar.getType() != Variable.NUMERIC)
            return null;
        if (policy != null && cVar == null)
            return null;

        rank = new int[nVar.getSize()];
        for (int i = 0; i < rank.length; i++)
        {
            rank[i] = 1;//rank start from 1
        }

        double[] nums = nVar.getNumValues();
        Vector cats = null;
        if (cVar != null)
            cats = cVar.getValues();

        for (int i = 0; i < nums.length; i++)
        {
            boolean maski = false;
            if (masks != null)
                maski = ((Boolean) masks.elementAt(i)).booleanValue();
            
            //if (maski) rank[i] = 0; //added by djluo, otherwise mask obs has rank at 1. not a bug.
            
            for (int j = i + 1; j < nums.length; j++)
            {
                boolean maskj = false;
                if (masks != null)
                    maskj = ((Boolean) masks.elementAt(j)).booleanValue();

                if (!maski && !maskj)
                {

                    if (cats != null)
                    {
                        if (policy.getGroupIndex(cats.elementAt(i)) == policy.getGroupIndex(cats.elementAt(j)) && nums[i] > nums[j])
                        {
                            rank[i]++;
                        }
                        else if (policy.getGroupIndex(cats.elementAt(i)) == policy.getGroupIndex(cats.elementAt(j)) && nums[i] <= nums[j])
                        {
                            rank[j]++;
                        }
                    }
                    else
                    {
                        if (nums[i] > nums[j])
                            rank[i]++;
                        else if (nums[i] <= nums[j])
                            rank[j]++;
                    }
                }

            }

        }

        if (policy != null)
            rank_table.put(policy.getName() + num_var, rank);
        else
            rank_table.put(num_var, rank);

        //System.out.println("\n Numeric Variable");
        //for(int i=0;i<nums.length;i++) System.out.print(nums[i]+" ");        
        
        return rank;
    }
    

    public void addVariable(String varName, double[] vals)
    {
        Vector vec = new Vector(vals.length);
        for (int i = 0; i < vals.length; i++)
            vec.addElement(new Double(vals[i]));

        NumVariable var = new NumVariable(varName, vec);


        variables.addElement(var);
        vars_table.put(var.getName().toUpperCase(), var);
        var.setDataSet(this);

        msg = "add_variable";
        asyncNotify();
    }
    
    public void addVariable(String varName, String[] vals)
    {
        Vector vec = new Vector(vals.length);
        for (int i = 0; i < vals.length; i++)
            vec.addElement(vals[i]);

        CatVariable var = new CatVariable(varName, vec);


        variables.addElement(var);
        vars_table.put(var.getName().toUpperCase(), var);
        var.setDataSet(this);

        msg = "add_variable";
        asyncNotify();
    }

    public void setVariableAt(Variable var, int index)
    {
        Variable v = (Variable) variables.elementAt(index);
        vars_table.remove(v.getName().toUpperCase());
        x_vars.remove(v);
        y_vars.remove(v);
        z_vars.remove(v);

        variables.setElementAt(var, index);
        if(var.getRole()==DataSet.X_ROLE)
            x_vars.add(var);
        else if(var.getRole()==DataSet.Y_ROLE)
            y_vars.add(var);
        else if(var.getRole()==DataSet.Z_ROLE)
            z_vars.add(var);
        else if(var.getRole()==DataSet.F_ROLE)
            freqVar=var;
        else if(var.getRole()==DataSet.L_ROLE)//added by djluo
         	labelVar=var;

        vars_table.put(var.getName().toUpperCase(), var);
            
        ((VariableImpl) var).setDataSet(this); //added by djluo. Fix bug occured when changing column type (new variable created).
            
        msg = "change_variable";
        asyncNotify();
            
        fireDataChanged(new DataChangeEvent(this));
    }

    public void deleteVariable(int index)
    {

        Variable v = (Variable) variables.elementAt(index);
        vars_table.remove(v.getName().toUpperCase());
        x_vars.remove(v);
        y_vars.remove(v);
        z_vars.remove(v);
        
        if(v.getRole() == DataSet.F_ROLE)
            freqVar = null;
        if(v.getRole() == DataSet.L_ROLE)
            labelVar = null;
        
        variables.removeElementAt(index);

        msg = "delete_variable";
        asyncNotify();
        
        fireDataChanged(new DataChangeEvent(this, index + 2));
    }

    public void deleteVariable(String varname)
    {
		int index=-1;
		for(int i=0; i<variables.size(); i++){
			Variable v = (Variable) variables.elementAt(i);
			if(v.getName().equals(varname))
				index = i;
		}
		
		if(index > 0)
		    deleteVariable(index);
	}
    	   

    public void addVariable(Variable v) {

        variables.addElement(v);
        vars_table.put(v.getName().toUpperCase(), v);
        ((VariableImpl) v).setDataSet(this);
        
        // added by djluo
        
        if(v.getRole()==DataSet.X_ROLE)
            x_vars.add(v);
        else if(v.getRole()==DataSet.Y_ROLE)
            y_vars.add(v);
        else if(v.getRole()==DataSet.Z_ROLE)
            z_vars.add(v);
        else if(v.getRole()==DataSet.F_ROLE)
        {
         	if (freqVar != null) freqVar.setRole(DataSet.U_ROLE);
            freqVar=v;
        }
        else if(v.getRole()==DataSet.L_ROLE)
        {
          	if (labelVar != null) labelVar.setRole(DataSet.U_ROLE);
            labelVar=v;
        }
        
        msg = "add_variable";
        asyncNotify();
    }

    public void setColumnName(String name, Variable v) {
        vars_table.remove(v.getName().toUpperCase());
        v.setName(name);
        vars_table.put(name.toUpperCase(), v);
    }

    public String getLabel(int index)
    {
        if (useRowNumber)
        {
            return String.valueOf(index + 1);
        }
        else if (labelVar != null)
        {
            return labelVar.getValue(index).toString();   
        }
        else
            return null;
    }

    public void setRowNumberEnabled(boolean value)
    {
        useRowNumber = value;
        if (useRowNumber && labelVar != null)
        {
            labelVar.setRole(DataSet.U_ROLE);
            labelVar = null;
        }
        msg = "yyrownumber";
        asyncNotify();
    }

    public boolean isRowNumberEnabled()
    {
        return useRowNumber;
    }

    public int getTotalMasks()
    {
        int[] array = missingVar.getDummyValues(); //maskVar.getDummyValues();  modified by djluo

        int sum = 0;
        for (int i = 0; i < array.length; i++)
            sum += array[i];

        return sum;
    }

    public void setRole(int role, int index)
    {
        Variable v = (Variable) variables.elementAt(index);
        this.setRole(v, role);
    }

    public void moveVariableTo(Variable v, int index)
    {

        int oldIndex = variables.indexOf(v);
        if (oldIndex == index)
            return;
        variables.remove(v);
        variables.add(index, v);
        
        fireDataChanged(new DataChangeEvent(this));
    }

    public void moveVariablesTo(List variableList, int index)
    {
        if (variableList == null || variableList.size() == 0)
            return;

        Variable v = (Variable) variableList.get(0);
        int oldIndex = variables.indexOf(v);
        
        //if (oldIndex == index - 1)  modified by djluo, make following code correct
        if (oldIndex == index)
            return;

        variables.removeAll(variableList);
        if (oldIndex > index)
        {
            variables.addAll(index, variableList);
        }
        else
        {
            variables.addAll(index - variableList.size(), variableList);
        }
        
        fireDataChanged(new DataChangeEvent(this));
    }


    public String getDescription() {
        return description;
    }


    public String getAuthorization() {
        return authorization;
    }

    public String getReference() {
        return reference;
    }

    public String getSubject() {
        return subject;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Update missingVar
     * @author djluo
     */
    private void updateMissing()
    {
    	for (int i = 0; i < missingVar.getValues().size(); i++)
    	{
    		updateMissing(i);
    	}
    }
    
    /**
     * Update missingVar for the indexed observation
     * @author djluo
     */
    private void updateMissing(int index)
    {
    	missingVar.setValueAt(false,index);
    	
    	Enumeration e = plot_vars.keys();
        
    	while (e.hasMoreElements())
        {
            Variable v = (Variable)e.nextElement();

            int cnt = ((Integer)plot_vars.get(v)).intValue();
    
            //the variable is used in some plots
            if (cnt > 0)
            {
            	Object value = v.getValue(index);
            	if ((Variable.NUM_MISSING_VAL.equals(value) || Variable.CAT_MISSING_VAL.equals(value)))
            	{
            		missingVar.setValueAt(true,index);
            	}	
            }
        }
    	
        if (maskVar.getValue(index))
        {
        	missingVar.setValueAt(true,index);
        }        
    }
    
    
    /**
     * Get the missing value for the indexed observation
     * @author djluo
     */
    public boolean getMissing(int i)
    {
        return missingVar.getValue(i);
    }
    
    
    /**
     * Delete the displayed module and corresponding variables
     * @author djluo
     */
    public void deleteModule(int moduleId)
    {
    	rank_table.clear();
    	
    	Vector vars = (Vector)module_vars.get(new Integer(moduleId));
    	
    	for (int i = 0; i < vars.size(); i++)
    	{
    		Variable v = (Variable)vars.elementAt(i);
    		Integer cnt = (Integer)plot_vars.get(v);
    		plot_vars.put(v, new Integer(cnt.intValue() - 1));
    	}
    	
    	module_vars.remove(new Integer(moduleId));
    	
        int[] array1 = missingVar.getDummyValues();
    	updateMissing();
    	int[] array2 = missingVar.getDummyValues();
    	
    	if ( Arrays.equals(array1, array2) == false ) {
    		msg = "yymask";
    		asyncNotify();   	
    	}
        
    }
    
    
    public void scanVariable(Variable xVar, Variable x2, Vector xv, Variable yVar, 
    		                 Variable zVar, Variable z2, Vector zv, Variable fVar, int moduleId)
    {
    	rank_table.clear();
    	
    	Vector vars = new Vector();
    	Integer cnt = null;
    	
    	if (xVar != null) {
    		cnt = (Integer)plot_vars.get(xVar);
    		if (cnt == null) 
    			plot_vars.put(xVar, new Integer(1));
    		else
    			plot_vars.put(xVar, new Integer(cnt.intValue() + 1));
    		vars.addElement(xVar);		
    	}
    	
    	if (x2 != null) {
    		cnt = (Integer)plot_vars.get(x2);
    		if (cnt == null) 
    			plot_vars.put(x2, new Integer(1));
    		else
    			plot_vars.put(x2, new Integer(cnt.intValue() + 1));
    		vars.addElement(x2);		
    	}
    	
    	if (xv != null) {
    		for (int i = 0; i < xv.size(); i++) {
        		Variable x = (Variable)(xv.elementAt(i));
        		cnt = (Integer)plot_vars.get(x);
        		if (cnt == null) 
        			plot_vars.put(x, new Integer(1));
        		else
        			plot_vars.put(x, new Integer(cnt.intValue() + 1));
        		vars.addElement(x);
    		}
    	}
    	
    	if (yVar != null) {
    		cnt = (Integer)plot_vars.get(yVar);
    		if (cnt == null) 
    			plot_vars.put(yVar, new Integer(1));
    		else
    			plot_vars.put(yVar, new Integer(cnt.intValue() + 1));
    		vars.addElement(yVar);		
    	}
    	
    	if (zVar != null) {
    		cnt = (Integer)plot_vars.get(zVar);
    		if (cnt == null) 
    			plot_vars.put(zVar, new Integer(1));
    		else
    			plot_vars.put(zVar, new Integer(cnt.intValue() + 1));
    		vars.addElement(zVar);		
    	}
    	
    	if (z2 != null) {
    		cnt = (Integer)plot_vars.get(z2);
    		if (cnt == null) 
    			plot_vars.put(z2, new Integer(1));
    		else
    			plot_vars.put(z2, new Integer(cnt.intValue() + 1));
    		vars.addElement(z2);		
    	}
    	
    	if (zv != null) {
    		for (int i = 0; i < zv.size() && i < EqualCountGrouper.MAXCOND; i++) {
        		Variable z = (Variable)(zv.elementAt(i));
        		cnt = (Integer)plot_vars.get(z);
        		if (cnt == null) 
        			plot_vars.put(z, new Integer(1));
        		else
        			plot_vars.put(z, new Integer(cnt.intValue() + 1));
        		vars.addElement(z);
    		}
    	}
    	
    	if (fVar != null) {
    		cnt = (Integer)plot_vars.get(fVar);
    		if (cnt == null) 
    			plot_vars.put(fVar, new Integer(1));
    		else
    			plot_vars.put(fVar, new Integer(cnt.intValue() + 1));
    		vars.addElement(fVar);		
    	}
    	
    	module_vars.put(new Integer(moduleId),  vars);
    	
    	int[] array1 = missingVar.getDummyValues();
    	updateMissing();
    	int[] array2 = missingVar.getDummyValues();
    	
    	if ( Arrays.equals(array1, array2) == false ) {
    		msg = "yymask";
    		asyncNotify();   	
    	}
    		
    }
    
    /**
     * clear temporarily stored ranks in cache
     * @author djluo
     */
    public void clearRankTable(){
    	rank_table.clear();
    }
    
    public void transform(String varName, Transformation xform) {
    	String xformVarName = (String)xform_table.get(varName);

    	if(xformVarName != null){
    			
    		int index=-1;
    		for(int i=0; i<variables.size(); i++){
    			Variable v = (Variable) variables.elementAt(i);
    			if(v.getName().equals(xformVarName))
    				index = i;
    		}
    		
    		if(index > 0)
    		    deleteVariable(index);
    	}
    		
    	if(xform.getTransformationMethod() == Transformation.CANONICAL){
    		xform_table.remove(varName);
    		return;
    	}
    		
    	double[] vals = null;
    	Variable var = getVariable(varName);
    	vals=var.getNumValues();
    		
    	double[] vals2 = new double[vals.length];
    	for(int i=0; i < vals.length; i++){
    		vals2[i] = xform.transform(vals[i]);
    	}
    		
    	String newVarName = xform.getTransformedVarName(varName);
    	xform_table.put(varName, newVarName);
    	addVariable(newVarName,vals2); 		
    
    }
    
    public Variable getTransformedVar(String varName) {
    	String xformVarName = (String)xform_table.get(varName);
		
    	if(xformVarName != null){
			Variable xformVar = getVariable(xformVarName);
			return xformVar;
		} 
    	else {
			return null;
		}
    }
    
    
    
    public int[] getSortOrders(Vector varNames, Map varOrders) 
    {
    	int[] rank = new int[size];
        boolean[] orders = new boolean[varOrders.size()];
        double[][] values = new double[varNames.size()][];

        for (int i = 0; i < varNames.size(); i++)
        {
            Variable v = getVariable((String) varNames.elementAt(i));
            values[i] = v.getNumValues();
            orders[i] = ((Boolean)varOrders.get(varNames.elementAt(i))).booleanValue();
        }

        Observation[] obs = new Observation[size];

        for (int i = 0; i < size; i++)
        {
            double[] tmp = new double[varNames.size()];

            for (int j = 0; j < tmp.length; j++)
            {
                tmp[j] = values[j][i];
            }

            obs[i] = new Observation(tmp, orders, i);
        }

        MathUtils.InsertionSort(obs);

        for (int i = 0; i < obs.length; i++)
        {
            rank[i] = obs[i].getID();
        }

        return rank;
    }
    
    public void addDataChangeListener(DataChangeListener l) {
    	eList.add(DataChangeListener.class, l);
    }
    
    public void removeDataChangeListener(DataChangeListener l) {
    	eList.remove(DataChangeListener.class, l);
    }
    
    /**
     * Update linked tables top-down, starting from root.
     */
    public void fireDataChanged(DataChangeEvent e) {
   		Object[] list = eList.getListenerList();
  		for (int i = list.length - 2; i >= 0; i -= 2) {
   			if (list[i] == DataChangeListener.class) {
   				((DataChangeListener) list[i + 1]).tableChanged(e);
   			}
   		}
  		
  		if (parent == null) return;
  		
  		if (e.getType() == DataChangeEvent.INVALID) {
			parent.removeDataChangeListener(this);
			parent = null;
			linkedRows = null;
			linkedCols = null;
		}
  		else if (e.getType() == DataChangeEvent.DELETE_ROWS) {
  			TIntArrayList rows = new TIntArrayList(linkedRows);
  			for (int i = e.getRows().length - 1; i >= 0; i--) {
  				rows.remove(e.getRows()[i]);
  			}
  			linkedRows = rows.toNativeArray();
  		}
  		else if (e.getType() == DataChangeEvent.DELETE_COLUMN) {
  			TIntArrayList cols = new TIntArrayList(linkedCols);
  			cols.remove(e.getColumn());
  			linkedCols = cols.toNativeArray();
  		}
    }

    /**
     * Handle change issued by parent table
     */
    public void tableChanged(DataChangeEvent e) {
    	
    	if (e.getType() == DataChangeEvent.SET_STATE) {
    			
    		int i = Arrays.binarySearch(linkedRows, e.getRow());
    		if (i >= 0)
    			setState2(e.getState(), i);
    	} 
    	
    	else if (e.getType() == DataChangeEvent.SET_STATES) {
    		TIntArrayList rows = new TIntArrayList();
    		ArrayList states = new ArrayList();
    		for (int i = 0; i < e.getRows().length; i++) {
    			int j = Arrays.binarySearch(linkedRows, e.getRows()[i]);
    			if (j >= 0) {
    				rows.add(j);
    				states.add(new Boolean(e.getStates()[i]));
    			}
    		}
    		
    		int[] indices = rows.toNativeArray();
    		boolean[] bl = new boolean[states.size()];
    		for (int i = 0; i < bl.length; i++)
    			bl[i] = ((Boolean)states.get(i)).booleanValue();
    		
    		setStates2(bl, indices);
    	}
    	
    	else if (e.getType() == DataChangeEvent.SET_MASKS) {
    		// find the row indics in this dataset
    		TIntArrayList rows = new TIntArrayList();
    		for (int i = 0; i < e.getRows().length; i++) {
    			int j =  Arrays.binarySearch(linkedRows, e.getRows()[i]);
    			if (j >= 0)
    				rows.add(j);
    		}
    			
    		// convert vector to array
    		int[] index = rows.toNativeArray();
    			
    		setMasks2(e.getState(), index);
    	}
    		
    	else if (e.getType() == DataChangeEvent.UPDATE_VALUE) {
    			
    		int i = Arrays.binarySearch(linkedRows, e.getRow());
    		int j = Arrays.binarySearch(linkedCols, e.getColumn());
    			
    		if (i >= 0) {
    			if (e.getColumn() == 0) {
    				if (e.getValue() instanceof Color)
    					setColor2((Color)e.getValue(), i);    		
    				else
    					setMarker2((Integer)e.getValue(), i);
    			} else if (e.getColumn() == 1) {
    				setMask2(e.getState(), i);
    				
    			} else if (j >= 0) {
    					
    				setVarValue2(getVariable(j-2).getName(), e.getValue(), i);
    			}
    		}
    				
    	}
    	else if (e.getType() == DataChangeEvent.DELETE_ROWS) {

    		// find the row indics in this dataset
    		TIntArrayList rows = new TIntArrayList();
    		for (int i = 0; i < e.getRows().length; i++) {
    			int j =  Arrays.binarySearch(linkedRows, e.getRows()[i]);
    			if (j >= 0)
    				rows.add(j);
    		}
    			
    		// convert vector to array
    		int[] index = rows.toNativeArray();
    			
    		deleteObservations(index);
    			
    			
    		// update the linkedRows reference. Pay attention that the observations in parent dataset are deleted too.
    		for (int i = e.getRows().length - 1; i >= 0; i--) {
    			TIntArrayList v = new TIntArrayList(linkedRows);
    			    				
    			int j = Arrays.binarySearch(linkedRows, e.getRows()[i]);
    			if (j >= 0) {
    				v.remove(j);
    				for (int k = j; k < v.size(); k++)
    					v.set(k, v.get(k)-1);
    			}
    			else {
    				for (int k = -j-1; k < v.size(); k++)
    					v.set(k, v.get(k)-1);
    			}
    				    				
    			linkedRows = v.toNativeArray();
    		}
    			
    	}
    	else if (e.getType() == DataChangeEvent.DELETE_COLUMN) {

    		int j = Arrays.binarySearch(linkedCols, e.getColumn());

    		if (j >= 0)
    			deleteVariable(j - 2);
    			
    		// update the linkedCols reference.
    		TIntArrayList v = new TIntArrayList(linkedCols);
				
    		j = Arrays.binarySearch(linkedCols, e.getColumn()); //linkedCols changes after delete variable.
			if (j >= 0) {
				v.remove(j);
				for (int k = j; k < v.size(); k++)
					v.set(k, v.get(k)-1);
			}
			else {
				for (int k = -j-1; k < v.size(); k++)
					v.set(k, v.get(k)-1);
			}
				
			linkedCols = v.toNativeArray();
				
    	}
    	else if (e.getType() == DataChangeEvent.INVALID) {
    		parent.removeDataChangeListener(this);
    		parent = null;
    		linkedRows = null;
    		linkedCols = null;
    	}
    	
    }
    
    private DataSet getRoot() {
    	if (parent == null)
    		return this;
    	else
    		return ((DataSetImpl)parent).getRoot();
    }
    
    private int getRootRowId(int i) {
    	if (parent == null)
    		return i;
    	else 
    		return ((DataSetImpl)parent).getRootRowId(linkedRows[i]);
    }
    
    private int getRootColumnId(int i) {
    	if (parent == null)
    		return i;
    	else
    		return ((DataSetImpl)parent).getRootColumnId(linkedCols[i]);
    }
    
    public boolean hasParent() {
    	return parent != null;
    }
    
    public boolean hasChildren() {
    	return eList.getListenerCount(DataChangeListener.class) > 0;
    }
    
    public int[] getLinkedRows() {
    	return linkedRows;
    }
    
    public int[] getLinkedColumns() {
    	return linkedCols;
    }
    
    private void printArray(int[] a) {
    	for (int i = 0; i < a.length; i++) 
    		System.out.print(a[i] + ",");
    	System.out.println();
    }
    
    public static void printArray(double[][] a) {
    	for (int i = 0; i < a.length; i++) {
    		for (int j = 0; j < a[0].length; j++)
    			System.out.print(a[i][j] + ",");
    		System.out.println();
    	}
    	System.out.println();
    }
    
    public void setVarLevels(boolean ordinal, boolean levelCheck, List levels, int index) {
    	Variable v = (Variable) variables.elementAt(index);
    	
    	if (v instanceof CatVariable) {
    		v.setOrdinal(ordinal);
    		v.setLevelCheck(levelCheck);
    		v.setLevels(levels);
    		
    		if (levelCheck == true) {
    			for (int i = 0; i < v.getSize(); i++) {
    				Object value = v.getValue(i);
    				if (!Variable.CAT_MISSING_VAL.equals(value) && !levels.contains(value)) {
    					setVarValue(v.getName(), Variable.CAT_MISSING_VAL, i);
    				}
    			}
    		}
    		
    		msg = "change_variable";
            asyncNotify();
    	}
    }
    
}

