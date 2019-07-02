/* Generated By:JJTree: Do not edit this line. ASTComparisonExp.java */

package wvustat.math.expression;

public class ASTComparisonExp extends Expression{
    private String operator = null;

    public ASTComparisonExp(int id) {
	super(id);
    }

    public ASTComparisonExp(ExpressionParser p, int id) {
	super(p, id);
    }

    public int getReturnType(){
	return Expression.BOOLEAN;
    }

    public void setOperator(String op){
	operator = op;
    }

    public String getOperator(){
	return operator;
    }

    public Object getValue() throws ExecError{
	return new Boolean(getBooleanValue());
    }

    public double getDoubleValue() throws ExecError{
	return (getBooleanValue())?(1.0):(0.0);
    }

    public boolean getBooleanValue() throws ExecError{
	Object o1 = ((Expression)jjtGetChild(0)).getValue();
	Object o2 = ((Expression)jjtGetChild(1)).getValue();

	if("==".equals(operator))
	    return o1.equals(o2);
	else if("!=".equals(operator))
	    return  !(o1.equals(o2));
	else if(o1 instanceof Comparable){
	    try{
		int diff = ((Comparable)o1).compareTo(o2);
		if(">".equals(operator))
		    return (diff > 0);
		else if(">=".equals(operator))
		    return (diff >= 0);
		else if("<=".equals(operator))
		    return (diff <= 0);
		else if("<".equals(operator))
		    return (diff < 0);
	    }catch(Exception e){
		throw new ExecError("Cannot compare " + 
					       o1.toString() +
					       " with " + o2.toString());
	    }
	}

	throw new ExecError("Cannot compare " + o1.toString() +
				       " with " + o2.toString());
    }

    public String toString(){
	return "(" + jjtGetChild(0).toString() + ") " + operator + 
	    " (" + jjtGetChild(1).toString() + ")";
    }
    
    public String derivativeString(String symbol) throws ExecError{
    	throw new ExecError("Can not take derivative of this function!");
    }
}