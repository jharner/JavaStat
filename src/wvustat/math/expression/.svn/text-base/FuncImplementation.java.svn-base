package wvustat.math.expression;

import java.util.*;

public class FuncImplementation{
    private String funcName = null;
    
    private FuncImplementation(String funcName){
    	    this.funcName = funcName;    	
    }
    
    public static FuncImplementation getInstance(String funcName){
     	if ("sin".equals(funcName) ||
     		"cos".equals(funcName) ||
     		"tan".equals(funcName) ||
     		"cot".equals(funcName) ||
     		"sec".equals(funcName) ||
     		"csc".equals(funcName) ||
     		"arcsin".equals(funcName) ||
     		"arccos".equals(funcName) ||
     		"arctan".equals(funcName) ||
     		"exp".equals(funcName) ||
     		"ln".equals(funcName) ||
     		"log10".equals(funcName) ||
     		"log2".equals(funcName) ||
     		"abs".equals(funcName) ||
     	    "sqrt".equals(funcName) ||
     	    "if".equals(funcName))
     		return new FuncImplementation(funcName);
     	
     	return null;
    }
    
    public int getReturnType(List parameterList) throws ExecError{
    	    if ("sin".equals(funcName) ||
    	    	"cos".equals(funcName) ||
    	    	"tan".equals(funcName) ||
    	    	"cot".equals(funcName) ||
    	    	"sec".equals(funcName) ||
    	    	"csc".equals(funcName) ||
    	    	"arcsin".equals(funcName) ||
    	    	"arccos".equals(funcName) ||
    	    	"arctan".equals(funcName) ||
    	    	"exp".equals(funcName) ||
    	    	"ln".equals(funcName) ||
    	    	"log10".equals(funcName) ||
    	    	"log2".equals(funcName) ||
    	    	"abs".equals(funcName) ||
    	    	"sqrt".equals(funcName))
    	    	return Expression.NUMERICAL;
    	    
    	    else if ("if".equals(funcName)) {
    	    	if (parameterList.size() != 3) 
    	    	    throw new ExecError("Illegal parameter number");
    	    	
    	    	return ((Expression)parameterList.get(1)).getReturnType();
    	    }
    	    
    	    return Expression.UNKNOWN;    	
    }
    
    //public List getParameterTypes();
    
    public Object getValue(List parameterList) throws ExecError{
    	    double op1 = 0;
    	    
    	    if ("sin".equals(funcName) ||
    	    	"cos".equals(funcName) ||
    	    	"tan".equals(funcName) ||
    	    	"cot".equals(funcName) ||
    	    	"sec".equals(funcName) ||
    	    	"csc".equals(funcName) ||
    	    	"arcsin".equals(funcName) ||
    	    	"arccos".equals(funcName) ||
    	    	"arctan".equals(funcName) ||
    	    	"exp".equals(funcName) ||
    	    	"ln".equals(funcName) ||
    	    	"log10".equals(funcName) ||
    	    	"log2".equals(funcName) ||
    	    	"abs".equals(funcName) ||
    	    	"sqrt".equals(funcName))
    	    {
    	    	    if (parameterList.size() != 1) 
    	    	    	    throw new ExecError("Illegal parameter number");
    	    	    
    	    	    
    	    	    if (((Expression)parameterList.get(0)).getValue() instanceof Number)
    	    	    	    op1 = ((Number)((Expression)parameterList.get(0)).getValue()).doubleValue();
    	    	    else
    	    	    	    throw new ExecError("Illegal parameter type");	 
    	    }
    	    else if ("if".equals(funcName))
    	    {
    	    	if (parameterList.size() != 3) 
    	    	    throw new ExecError("Illegal parameter number");
    	    	
    	    	if (((Expression)parameterList.get(1)).getReturnType() != 
    	    		((Expression)parameterList.get(2)).getReturnType())
    	    		throw new ExecError("Unmatched parameter type");
    	    }
    	    
    	    if ("sin".equals(funcName))
    	        return new Double( Math.sin(op1) );
    	    else if ("cos".equals(funcName))
    	        return new Double( Math.cos(op1) );
    	    else if ("tan".equals(funcName))
    	        return new Double( Math.tan(op1) );
    	    else if ("cot".equals(funcName))
    	        return new Double( 1/Math.tan(op1) );
    	    else if ("sec".equals(funcName))
    	        return new Double( 1/Math.sin(op1) );
    	    else if ("csc".equals(funcName))
    	        return new Double( 1/Math.cos(op1) );
    	    else if ("arcsin".equals(funcName))
    	        return new Double( Math.asin(op1) );
    	    else if ("arccos".equals(funcName))
    	        return new Double( Math.acos(op1) );
    	    else if ("arctan".equals(funcName))
    	        return new Double( Math.atan(op1) );
    	    else if ("exp".equals(funcName))
    	        return new Double( Math.exp(op1) );
    	    else if ("ln".equals(funcName))
    	        return new Double( Math.log(op1) );
    	    else if ("log10".equals(funcName))
    	        return new Double( Math.log(op1)/Math.log(10.0) );
    	    else if ("log2".equals(funcName))
    	        return new Double( Math.log(op1)/Math.log(2.0) );
    	    else if ("abs".equals(funcName))
    	        return new Double( Math.abs(op1) );
    	    else if ("sqrt".equals(funcName))
    	        return new Double( Math.sqrt(op1) );
    	    else if ("if".equals(funcName))
    	    	return (((Expression)parameterList.get(0)).getBooleanValue())?
    	    			(((Expression)parameterList.get(1)).getValue()):
    	    			(((Expression)parameterList.get(2)).getValue());
    	    
    	    return null;    	
    }
    
    public String derivative(List expList, String symbol) throws ExecError{
    		
    		if ("sin".equals(funcName) ||
    	    	"cos".equals(funcName) ||
    	    	"tan".equals(funcName) ||
    	    	"cot".equals(funcName) ||
    	    	"sec".equals(funcName) ||
    	    	"csc".equals(funcName) ||
    	    	"arcsin".equals(funcName) ||
    	    	"arccos".equals(funcName) ||
    	    	"arctan".equals(funcName) ||
    	    	"exp".equals(funcName) ||
    	    	"ln".equals(funcName) ||
    	    	"log10".equals(funcName) ||
    	    	"log2".equals(funcName) ||
    	    	"abs".equals(funcName) ||
    	    	"sqrt".equals(funcName))
    	    {
    			if (expList.size() != 1) 
        			throw new ExecError("Illegal parameter number");
    	    }
    		else if ("if".equals(funcName))
    		{
    			if (expList.size() != 3) 
    	    	    throw new ExecError("Illegal parameter number");
    		}
    	
    		
    		Expression p1 = (Expression)expList.get(0);
    		String part1, part2, part3;
    		
    		if ("sin".equals(funcName)){
    			part1 = "cos(" + p1.toString() + ")";
    			return "(" + part1 + ")" + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("cos".equals(funcName)){
    			part1 = "-sin(" + p1.toString() + ")";
    			return "(" + part1 + ")" + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("tan".equals(funcName)){
    			part1 = "sec(" + p1.toString() + ")";
    			part2 = "(" + part1 + ")" + "^2";
    			return "(" + part2 + ")" + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("cot".equals(funcName)){
    			part1 = "csc(" + p1.toString() + ")";
    			part2 = "(" + part1 + ")" + "^2";
    			part3 = "-" + "(" + part2 + ")";
    			return "(" + part3 + ")" + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("sec".equals(funcName)){
    			part1 = "sec(" + p1.toString() + ")";
    			part2 = "tan(" + p1.toString() + ")";
    			part3 = part1 + "*" + part2;
    			return part3 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("csc".equals(funcName)){
    			part1 = "-csc(" + p1.toString() + ")";
    			part2 = "cot(" + p1.toString() + ")";
    			part3 = "(" + part1 + ")" + "*" + "(" + part2 + ")";
    			return part3 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("arcsin".equals(funcName)){
    			part1 = "(" + p1.toString() + ")^2";
    			part2 = "1 - " + part1;
    			part3 = "sqrt(" + part2 + ")";
    			return "1 / " + part3; 
    		}
    		else if ("arccos".equals(funcName)){
    			throw new ExecError("Can compute derivative for arccos function");
    		}
    		else if ("arctan".equals(funcName)){
    			part1 = "(" + p1.toString() + ")^2";
    			part2 = "1 + " + part1;
    			return "1 / (" + part2 + ")";
    		}
    		else if ("exp".equals(funcName)){
    			part1 = "exp(" + p1.toString() + ")";
    			return part1 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("ln".equals(funcName)){
    			part1 = "1 / (" + p1.toString() + ")";
    			return part1 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("log10".equals(funcName)){
    			part1 = "1 / (" + p1.toString() + ")";
    			part2 = "ln(10)";
    			part3 = "(" + part1 + ") / " + part2;
    			return part3 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("log2".equals(funcName)){
    			part1 = "1 / (" + p1.toString() + ")";
    			part2 = "ln(2)";
    			part3 = "(" + part1 + ") / " + part2;
    			return part3 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("abs".equals(funcName)){
    			throw new ExecError("Can not compute derivative for function 'abs'");
    		}
    		else if ("sqrt".equals(funcName)){
    			part1 = "sqrt(" + p1.toString() + ")";
    			part2 = "2 * " + part1;
    			part3 = "1 / (" + part2 + ")";
    			return part3 + "*" + "(" + p1.derivativeString(symbol) + ")";
    		}
    		else if ("if".equals(funcName)){
    			throw new ExecError("Can not compute derivative for function 'if'");
    		}
    		
    		throw new ExecError("Unimplemented function");
    }
}
