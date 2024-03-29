/* Generated By:JJTree: Do not edit this line. ASTAddictiveExp.java */

package wvustat.math.expression;

public class ASTAddictiveExp extends Expression{
    private String operator = null;

    public ASTAddictiveExp(int id) {
	super(id);
    }

    public ASTAddictiveExp(ExpressionParser p, int id) {
    super(p, id);
    }

    public int getReturnType(){
	return Expression.NUMERICAL;
    }

    public void setOperator(String op){
	operator = op;
    }

    public String getOperator(){
	return operator;
    }

    public Object getValue() throws ExecError{
	return new Double(getDoubleValue());
    }

    public double getDoubleValue() throws ExecError{
	double op1 = ((Expression)jjtGetChild(0)).getDoubleValue();
	double op2 = ((Expression)jjtGetChild(1)).getDoubleValue();

	if("+".equals(operator))
	    return op1 + op2;
	else if("-".equals(operator))
	    return op1 - op2;

	throw new ExecError("Unsupport operator: " + operator);
    }

    public boolean getBooleanValue() throws ExecError{
	Double d = new Double(getDoubleValue());

	return d.intValue() != 0;
    }

    public String toString(){
	return "(" + jjtGetChild(0).toString() + ") " + operator + " (" +
	    jjtGetChild(1) + ")";
    }
    
    public String derivativeString(String symbol) throws ExecError{
    	return "(" + ((Expression)jjtGetChild(0)).derivativeString(symbol) + ") " + operator + " (" +
    		((Expression)jjtGetChild(1)).derivativeString(symbol) + ")";
    }
}
