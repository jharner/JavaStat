package wvustat.modules.heatmap;
/**
 * The definition for observation tree element.
 * @author BoFu
 *
 */

public class ObservationTreeElement implements TreeElement{
	
	private Observation obs;
	
	/**
	 * An observation tree element should contain an observation
	 * @param var
	 */
	public ObservationTreeElement(Observation obs){
		this.obs = obs;
	}

	public void setObservation(Observation obs){
		this.obs = obs;
	}
	
	public String toString(){
		return this.obs.toString();
	}
	
	public Observation getObservation(){
		return this.obs;
	}
	
	public boolean equals(Object o){
		if((o == null) || (o.getClass() != this.getClass())){
    		return false;
    	}else{
    		ObservationTreeElement Ele = (ObservationTreeElement)o;
    		if (this == o){
    			return true;
    		}else{ 
    			if (this.obs == Ele.obs){
    				return true;
    			}else{
    				return super.equals(Ele);
    			}
    		}
    	}
	}
	
	public int hashCode(){
		return obs.hashCode();
	}
}
