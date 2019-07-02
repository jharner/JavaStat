package wvustat.math.expression;

public interface VariableExpression{
    public String getVarName();
    public Object getVarValue();
    public void setVarValue(Object v);
    public void assign(VariableExpression v);
}
