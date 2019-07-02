package wvustat.data;

import java.util.*;

import wvustat.interfaces.*;

/**
 *	VariableImpl is an abstract class that implements interface Variable.
 *
 *	@author: Hengyi Xue
 *	@version: 1.0, Oct. 25, 1999
 */

public abstract class VariableImpl implements Variable{
    
    /**
     * Class variable that keeps track of how many instances have been instantiated
     */
    protected static int numVariables=0;
    
    protected int id;
    protected String name;
    protected int type;
    protected int role=DataSet.U_ROLE;
    protected Vector objValues = new Vector();
    
    protected DataSet parent;
    
    /**
     *  Constructor with no parameters
     */
    public VariableImpl(){
        super();
        numVariables++;
        id = numVariables;
        name = "Var" + numVariables;
    }
    
    /**
     * Constructor
     * Create a new VariableImpl instance for the given name and values
     *
     * @param n the name of the variable
     * @param v the values contained in the variable
     */
    public VariableImpl(String n, Vector v){
        super();
        numVariables++;
        id = numVariables;
        name = n;
        objValues = v;
    }
    
    /**
     * Set the dataset who owns this varaible.
     */
    public void setDataSet(DataSet ds){
        parent=ds;
    }
    
    public Variable getClonedCopy(){
        Variable v=null;
        
        System.out.println("cloning...");
        
        if(type==NUMERIC){
            v=new NumVariable(name, objValues);
            v.setRole(role);
        }
        else{
            v=new CatVariable(name, objValues);
            v.setRole(role);
        }
        
        return v;
    }
    
    
    // Readers
    
    /**
     * Get the ID of this VariableImpl instance
     */
    public int getId() {
    	return id;
    }    
    
   /**
    * Get the name of this VariableImpl instance
    */
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name=name;
    }
    
  /**
   * Get the type of this VariableImpl instance
   */
    public int getType() {
        return type;
    }
    
  /**
   * Get the role of this VariableImpl instance
   */
    public int getRole(){
        return role;
    }
    
  /**
   * get the values contained in this VariableImpl instance
   */
    public Vector getValues (){
        return objValues;
    }
    
  /**
   * Get the indexed value of this VariableImpl instance
   */
    public Object getValue(int index){
        return objValues.elementAt(index);
    }
    
  /**
   * Get the size of this VariableImpl instance
   */
    public int getSize(){
        return objValues.size();
    }
    
  /**
   * Get the numerical values, only makes sense to numeric variable
   */
    public abstract double[] getNumValues() ;
    
  /**
   * Get the categorical values, only makes sense to categorical variable
   */
    public abstract String[] getCatValues() ;
    
    // Writers
  /**
   * Assign role to this VariableImpl instance
   */
    public void setRole(int newRole){        
        role=newRole;    
    }
    
    //This method is not defined in the 'Variable' interface,
    //It is meant to be used on the server side
  /**
   * Set role for this variable specified by a character
   */
    public void setRole(char newRole){
        switch(newRole){
            case 'x':
            case 'X':
                role=DataSet.X_ROLE;
                break;
            case 'y':
            case 'Y':
                role=DataSet.Y_ROLE;
                break;
            case 'z':
            case 'Z':
                role=DataSet.Z_ROLE;
                break;
            case 'l':
            case 'L':
                role=DataSet.L_ROLE;
                break;
            case 'w':
            case 'W':
                role=DataSet.W_ROLE;
                break;
            case 'u':
            case 'U':
                role=DataSet.U_ROLE;
                break;
            case 'f':
            case 'F':
            	role=DataSet.F_ROLE;
            	break;
        }
    }
    
    /**
     * change the index value
     */
    public abstract void setValue(Object obj, int i);
    
    public void removeValueAt(int index){
        objValues.removeElementAt(index);
    }
    
    
    // Other Methods
    
    /**
     * print out all values to standard output
     */
    public void print(){
        System.out.println("Name: " + name);
        System.out.println("Class: " + this.getClass().getName());
        
        for(int i=0;i<objValues.size();i++)
            System.out.print(objValues.elementAt(i)+" ");
        
        System.out.println();
    }
    
    
    /**
     * Get dummy variable values, default implementation returns null
     */
    public double[][] getDummyValues(){
        return null;
    }
    
    public abstract void setOrdinal(boolean b);
    
    public abstract boolean isOrdinal();
    
    public abstract void setLevelCheck(boolean b);
    
    public abstract boolean isLevelCheck();
    
    public abstract void setLevels(List levels);
    
    public abstract List getLevels();
    
}
