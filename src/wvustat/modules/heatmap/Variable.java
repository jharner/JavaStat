package wvustat.modules.heatmap;
/**
 * Definition of the variable. 
 * @author BoFu
 *
 */
public class Variable {
	private String id;
	
	/**
	 * All the variables should have an id
	 * @param id is the name of the variable.
	 */
	public Variable(String id){
		if (id == null){
			throw new IllegalArgumentException("Variable should have an id"); 
		}else{
			this.id = id;
		}
	}
	
	public void setId(String id){
		if (null == id){
			return;
		}else{
			this.id = id;
		}
	}
	
	public String getId(){
		return null == this.id? null : this.id;
	}
	
	public boolean equals(Object o){
		if (null == o && !(o instanceof Variable)){
			return false;
		}else{
			if (this == o)return true;
			else{
				Variable v = (Variable)o;
				if (this.id.equals(v.id)) return true;
				else{
					return super.equals(v);
				}
			}
		}
	}
	
	public int hashCode(){
		int hash = 0;
		hash = 31 * hash + this.id.hashCode();
		return hash;
	}
	
	public String toString(){
		return this.id;
	}
}
