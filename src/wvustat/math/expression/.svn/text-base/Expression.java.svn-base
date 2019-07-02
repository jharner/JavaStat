package wvustat.math.expression;

import java.util.*;

/**
 * Expression is the superclass of all nodes the parsed
 * from a mathematical expression.
 */
public abstract class Expression extends SimpleNode{
    public final static int NUMERICAL = 0;
    public final static int BOOLEAN = 1;
    public final static int CATEGORICAL = 2;
    public final static int UNKNOWN = 3;

    public Expression(int id){
	super(id);
    }

    public Expression(ExpressionParser p, int id){
	super(p, id);
    }

    /** 
     * @return type of the expresion. It is one the four possible values:
     * Expression.NUMERICAL, Expression.BOOLEAN, Expression.CATEGORICAL, Expression.UNKNOWN.
     */
    public abstract int getReturnType();

    /**
     * @return the result as a Object.
     * @exception ExecError will be thrown if there is any error.
     */
    public abstract Object getValue() throws ExecError;

    /**
     * @return the result as a double
     * @exception ExecError will be thrown if there is any error.
     */
    public abstract double getDoubleValue() throws ExecError;

    /**
     * @return the result as a boolean
     * @exception ExecError will be thrown if there is any error.
     */
    public abstract boolean getBooleanValue() throws ExecError;

    public abstract String derivativeString(String symbol) throws ExecError;
    
    /**
     * @return a name to variable map containing all the Varaiables in the
     * Expression.
     */
    public Map getVariables(){
        HashMap map = new HashMap();

        fillVariables(map);
        return map;
    }

    void fillVariables(Map map){
    		if(this instanceof VariableExpression){
    			VariableExpression v = (VariableExpression)this;
    			map.put(v.getVarName(), v);  
    		}

    		for(int i = 0; i < jjtGetNumChildren(); i++){
    			Node node = jjtGetChild(i);
    			((Expression)node).fillVariables(map);
    		}
    }

    /**
     * This method searches for variables in the expression and return their names in a vector.
     */
     public Vector getVariableNames(){
     	Vector v = new Vector();
     	Map varMap = getVariables();
     	Iterator it = varMap.keySet().iterator();
     	while(it.hasNext()){
            String varName = (String)it.next();
            v.addElement(varName);
     	}
     	return v;
     }
    
    
    /**
     * @param map is a name to variable map. The values of the map
     * should be a object that is either Double or Boolean. 
     * @Exception IllegalArgumentException will be thrown if any varialbe 
     * in the map is not compatible with that in the expression.
     */
    public void setVariables(Map map){
    		if(map == null)
    			return;

    		if(this instanceof VariableExpression){
    			VariableExpression v = (VariableExpression)this;
    			Object o = map.get(v.getVarName());
    			v.setVarValue(o);
    		}

    		for(int i = 0; i < jjtGetNumChildren(); i++){
    			Node node = jjtGetChild(i);
    			if(node instanceof Expression)
    				((Expression)node).setVariables(map);
    		}
    }
    
    public String unparse(){
    		return toString();
    }
    
    public boolean functionOf(String symbol){
        Vector v=getVariableNames();
        return v.contains(symbol);
    }
    
    public Expression derivative(String symbol) throws ExecError{
    		String s = this.derivativeString(symbol);
    		ExpressionParser ep = new ExpressionParser();
    		try{
    			Expression exp = ep.parse(s);
    			return exp;
    		}catch(Exception e){
    			throw new ExecError(e.getMessage());
    		}catch(Error err){
    			throw new ExecError(err.getMessage());
    		}
    }
    
    public double value(Map map) throws ExecError{
    		setVariables(map);
    		return getDoubleValue();    	
    }
}
