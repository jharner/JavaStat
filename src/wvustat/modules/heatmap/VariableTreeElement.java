package wvustat.modules.heatmap;
/**
 * Definition of variable tree element.
 *
 */

public class VariableTreeElement implements TreeElement{
	
	private Variable var;
	
	/**
	 * An variable tree element should contain a variable
	 * @param var is the variable.
	 */
	public VariableTreeElement(Variable var){
		this.var = var;
	}
    
	public void setVariable(Variable var){
		this.var = var;
	}
	
	public String toString(){
		return this.var.toString();
	}
	
	public Variable getVariable(){
		return this.var;
	}
	
	public boolean equals(Object o){
		if((o == null) || (o.getClass() != this.getClass())){
    		return false;
    	}else{
    		VariableTreeElement Ele = (VariableTreeElement)o;
    		if (this == o){
    			return true;
    		}else{ 
    			if (this.var == Ele.var){
    				return true;
    			}else{
    				return super.equals(Ele);
    			}
    		}
    	}
	}
	
	public int hashCode(){
		return var.hashCode();
	}
}
