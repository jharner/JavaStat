package wvustat.interfaces;

import java.util.*;
import java.awt.Color;

import wvustat.data.BoolVariable;
import wvustat.data.DataChangeEvent;
import wvustat.data.DataChangeListener;
import wvustat.modules.Transformation;
import wvustat.modules.EqualCountGrouper;

/**
 *	DataSet defines interface for a data set. A data set contains a collection of Variables and visual attributes. The interface
 *	define method for accessing and modifying these variables and visual attributes.
 *
 *	@author: Hengyi Xue
 *	@version: 1.7, Mar. 8, 2000
 */


public interface DataSet extends Session, DataChangeListener{


    /**
     * constants for property 'role' of variable, @see getRole() and setRole()
     */
    public final static int X_ROLE=0;
    public final static int Y_ROLE=1;
    public final static int Z_ROLE=2;
    //role for label variable
    public final static int L_ROLE=3;
    //role for weight variable
    public final static int W_ROLE=4;
    //'u_role' stands for undefined role
    public final static int U_ROLE=5;
    //'F_ROLE' stands for frequency role
    public final static int F_ROLE=6;


    /**
     * Get the identifier of the data set.
     */
    public int getId();
    
    /**
     *	Accessor method to get indexed property
     */
    public Variable getVariable(int index) ;

    /**
     *	Get the total number of variables the data set contains.
     */
    public int getVariableCount();

    /**
     * Get number of observations in the dataset
     */
    public int getSize() ;

    /**
     *	Get a varialbe identified by the parameter name
     *	@param	name the name of the variable
     */
    public Variable getVariable(String name);

    /**
     *	Get all variables in a data set
     *
     *	@return a vector containing all the variables
     */
    public Vector getVariables(); 

    
    public String[] getVariableNames();
    
    /**
     *	Get all variables with role 'X'
     */
    public Vector getXVariables() ;

    /**
     *	Get all varialbes with role 'Y'
     */
    public Vector getYVariables();

    /**
     *	Get all variables with role 'Z'
     */
    public Vector getZVariables() ;

    /**
     *	Get the name of the data set
     */
    public String getName() ;
    
    public void setName(String name);

    /**
     *	Get the state of observation indexed by index
     *
     *	@param	index the index of the observation
     */
    public boolean getState(int index);

    /**
     *	Get the states of every observation
     *
     *	@return	an array that contains all states of observations
     */
    public boolean[] getStates();
    
    public BoolVariable getStateVariable();

    /**
     *	Get all the color values associated with each observation
     */
    public Vector getColors();

    /**
     *	Get all the mask values associated with each observation
     */
    //public Vector getMasks();

    /**
     *	Get the color for the observation at index
     */
    public Color getColor(int index);

    /**
     *	Get the mask(missing) value for the indexed observation.
     *  In order to avoid replace lots of invoke statements in related 
     *  program, use getMask() to replace getMissing() as interface to 
     *  implement missing value function easily.
     */
    public boolean getMask(int index);

    /**
     *	Change the value identified by index in a variable identified by varName
     *
     *	@param	varName	the name of the Variable
     *	@param	val	the new value
     *	@param	index	the index of the value to be changed
     *	@param	clientID	the identifier string for the client
     */
    public void setVarValue(String varName, Object val, int index);


    /**
     *	Change the state of the observation at index
     *
     *	@param bl	the new state value
     *	@param	index the index of the observation
     *	@param	clientID the id string for the client
     */
    public void setState(boolean bl, int index);
    
    /**
     * Change the states of the observations at indices
     * 
     * @param bl the new state values
     * @param indices the indices of the observations
     */
    public void setStates(boolean[] bl, int[] indices);
    
    /**
     * Set the states for all the observations in dataset
     * @param bl the new state values in array whose size is same as dataset.
     */
    public void setStates(boolean[] bl);

    /**
     *	Set all state values to false, i.e., no observation is selected
     *
     *	@param clientID the id string for the client
     */
    public void clearStates();

    /**
     *	Change the color of the observation at index
     *
     *	@param c the new color
     *	@param index the index of the observation
     *	@param clientID the id string for the client
     */
    public void setColor(Color c, int index);

    /**
     *	Change the mask value of the observation at index
     *
     *	@param bl the new mask value
     *	@param index the index of the observation
     *	@clientID the id string for the client
     */
    public void setMask(boolean bl, int index);
    public void setMasks(boolean bl, int[] index);


    /**
     *	Sort the observation based on the numerical variable identified by num_var and optionally
     *	divide them into groups based on policy and return ranks of each observation to the client. Each group of observations has its own rank.
     *
     *	@param policy	the algorithm to divide observation into groups @see DividePolicy
     *	@param	num_var the name of the numerical variable
     */
    public int[] getRanks(DividePolicy policy, String num_var);

    public int[][] getRanks(EqualCountPolicy policy, String num_var);

    /**
     * Get the ranks of observations in the data set. The observations will be ranked by variables whose names are specified in the Vector varNames. There is no grouping, each observation has an unique rank value.
     */
    public int[] getRanks(Vector varNames);
    
    /**
    * Get the ranks of observations excluding the ones that has been masked out
    */
    public int[] getUnmaskedRanks(Vector varNames);

    /**
     *	Add a new observation into the data set
     *
     *	@param row a vector of vectors that contains all the new observations' values
     */
    public void addObservations(Vector rows);

    /**
     * Delete an observation from the data set
     *
     * @param indices an array of integers containing the index of the observation to be deleted
     */
    public void deleteObservations(int[] index);


    public void deleteVariable(int index);
    public void deleteVariable(String varname);
    
    /**
     *	change the role of a variable
     *
     *	@param v the variable
     *	@param role	the new role
     */
    public void setRole(Variable v, int role);

    
    
    /**
    * Get the frequency matrix for given categorical variables.
    *
    * @param catVar1 the name of the first categorical variable
    * @param catVar2 the name of the second categorical variable
    */
    public double[][] getFreqMatrix(String catVar1, String catVar2); 

    /**
     * Get the frequency matrix for given categorical variables.
     *
     * @param catVar1 the name of the first categorical variable
     * @param grouper the collection of the conditional variables
     */
    public double[][] getFreqMatrix(String catVar1, EqualCountGrouper grouper);
	
    /**
    * Get the frequency matrix for given categorical variables with constraints.
    *
    * @param catVar1 the name of the first categorical variable
    * @param catVar2 the name of the second categorical variable
    * @param constrt1 a double array with values 0 or 1
    * @param constrt2 a double array with values 0 or 1
    */
    public double[][] getFreqMatrix(String catVar1, String catVar2, double[] constrt1, double[] constrt2);
    
    /**
    * Get the summation of values at the 'crossing' of two categorical variables and possible constraints.
    *
    * @param numVar the numerical variable to be summed.
    * @param catVar1 the name of the first categorical variable
    * @param catVar2 the name of the second categorical variable
    * @param constrt1 a double array with values 0 or 1
    * @param constrt2 a double array with values 0 or 1
    * @return a 2d array that is organized like a contingency table. The array has the row dimension same as number of levels in cat variable 1 and
    * column dimension that of the number of levels in cat variable 2.
    */    
    public double[][] getSumMatrix(String numVar, String catVar1, String catVar2, double[] constrt1, double[] constrt2);
    
    /**
    * Add a new variable to the data set.
    *
    * @param var the new variable to add to the data set.
    */
    public void addVariable(String varName, double[] vals);
    
    public void addVariable(String varName, String[] vals);
    
    public void addVariable(Variable v);
    
    /**
     * Replace the variable at the index with a new variable. Happened when user changes column type.
     * 
     */
    public void setVariableAt(Variable var, int index);
    
    public void setColumnName(String name, Variable v);

    public Variable getLabelVariable();
    
    public Variable getFreqVariable();
    
    public String getLabel(int index);
    
    public void setRowNumberEnabled(boolean value);

    public boolean isRowNumberEnabled();

    public int getTotalMasks();
    
    public void setRole(int role, int index);

    public void moveVariableTo(Variable v, int index);

    public void moveVariablesTo(List variableList, int index);

    public String getDescription();

    public String getAuthorization();

    public String getReference();

    public String getSubject();

    public void setDescription(String description);

    public void setReference(String reference);

    public void setAuthorization(String authorization);
    
    public void setSubject(String subject);
    
    public boolean getMissing(int i);
    
    public void scanVariable(Variable xVar, Variable x2, Vector xv, Variable yVar, 
            Variable zVar, Variable z2, Vector zv, Variable fVar, int moduleId);
    
    
    public void deleteModule(int moduleId);
    
    
    public boolean getTrueMask(int index);
   
    public void clearRankTable();
    
    public void transform(String varName, Transformation xform);
    
    public Variable getTransformedVar(String varName);    
    
    public int[] getSortOrders(Vector varNames, Map varOrders);
    
    public void tableChanged(DataChangeEvent e);
    
    public void addDataChangeListener(DataChangeListener l);
    
    public void removeDataChangeListener(DataChangeListener l);
    
    public void fireDataChanged(DataChangeEvent e);
    
    public boolean hasParent();
    
    public boolean hasChildren();
    
    public int[] getLinkedRows();
    
    public int[] getLinkedColumns();
    
    public void setVarLevels(boolean ordinal, boolean levelCheck, List levels, int index);
    
    public Integer getMarker(int index);
    
    public void setMarker(Integer m, int index);
}


