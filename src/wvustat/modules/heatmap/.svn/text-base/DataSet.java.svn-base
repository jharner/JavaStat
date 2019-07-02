package wvustat.modules.heatmap;
import java.util.*;

/**
 * The class contains data values. Numeric values can be get by the corresponding observation
 *  and variable in other class.
 * @author BoFu
 *
 */
public class DataSet {

	private Map dataMap;
	private Observation[] obs;
	private Variable[] var;

	/**
	 * Map should be map from variable to maps for observation to double numerical data;
	 * @param dataMap maps all the practical data values with observations and variables.
	 */
	public DataSet(Map dataMap){
		this.dataMap = dataMap;
		Set varset      = dataMap.keySet();
		var = new Variable[varset.size()];
		var = (Variable[]) varset.toArray(var);

		Map obsmap = (HashMap)dataMap.get(var[0]);
		Set obsset = obsmap.keySet();
		obs = new Observation[obsset.size()];
		obs = (Observation[])obsset.toArray(obs);
	}

	public double getValue(Variable var, Observation obs){
		Map m = new HashMap();
		m = (HashMap)dataMap.get(var);
		return ((Double)m.get(obs)).doubleValue();

	}

	public Observation[] getObservations(){
		return this.obs;
	}

	public Variable[] getVariables(){
		return this.var;
	}
}
