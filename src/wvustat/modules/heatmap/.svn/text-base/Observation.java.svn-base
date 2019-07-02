package wvustat.modules.heatmap;
/**
 * The definition of the observation. 
 * @author Bo Fu
 *
 */
public class Observation {
	private String id;
		
	/**
	 * All the Observations should have an id
	 * @param id is the name of the observation
	 */
	public Observation(String id){
		if (id == null){
			throw new IllegalArgumentException("Observation should have an id"); 
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
		if (null == o && !(o instanceof Observation)){
			return false;
		}else{
			if (this == o)return true;
			else{
				Observation v = (Observation)o;
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
