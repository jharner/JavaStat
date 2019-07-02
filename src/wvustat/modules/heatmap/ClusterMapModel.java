package wvustat.modules.heatmap;
/**
 * A model have all information for drawing heatmap, including observation and variable trees, 
 * selection models, metrics models etc.
 * @author BoFu
 *
 */
public class ClusterMapModel implements HeatMapMetricsModel{

	private DataSet dataset;
	private Tree variableTree;
	private Tree observationTree;

	private TreeMetricsModel varTreeMetricsModel ;
	private TreeMetricsModel obsTreeMetricsModel ;
	
	private TreeSelectionModel varSelectionModel ;
	private TreeSelectionModel obsSelectionModel ;

	private int elementWidth  = 20;
	private int elementHeight = 20;
    private int stemLength    = 10;

	/**
	 * A model have all information for drawing heatmap, should have at least one among 
	 * the data set, variable tree or observation tree
	 * @param data is the data set used to be analyzed.
	 * @param vartree is the clustering tree from the variables (rows of data).
	 * @param obstree is the clustering tree from the observations (columns of data).
	 */
	public ClusterMapModel(DataSet dataset, Tree vartree, Tree obstree){
		if (null == dataset || null == vartree || null == obstree){
			throw new IllegalArgumentException(
					"The ClusterMapModel should have one among the data set, variable tree or observation tree");
		}
		this.dataset         = dataset;
		this.variableTree    = vartree;
		this.observationTree = obstree;
		this.varTreeMetricsModel = new VariableTreeMetricsModel();
		this.obsTreeMetricsModel = new ObservationTreeMetricsModel();
		this.varSelectionModel = new TreeSelectionModel();
		this.obsSelectionModel = new TreeSelectionModel();
	}
	
	public void setVarSelectionModel(TreeSelectionModel varSelectionModel){
		this.varSelectionModel = varSelectionModel;
	}
	
	public TreeSelectionModel getVarSelectionModel(){
		return this.varSelectionModel;
	}
	
	public void setObsSelectionModel(TreeSelectionModel obsSelectionModel){
		this.obsSelectionModel = obsSelectionModel;
	}
	
	public TreeSelectionModel getObsSelectionModel(){
		return this.obsSelectionModel;
	}

	public void setElementWidth(int elementWidth){
		this.elementWidth = elementWidth;
	}

	public void setElementHeight(int elementHeight){
		this.elementHeight = elementHeight;
	}

	public void setStemLength(int stemLength){
		this.stemLength = stemLength;
	}

    public int getElementWidth(){
    	return elementWidth;
    }

	public int getElementHeight(){
		return elementHeight;
	}

	public int getStemLength(){
		return stemLength;
	}

	public Variable[] getAllVariables(){

		TreeElement[] velts = variableTree.getRoot().getElementOrdered();
		Variable[] vars = new Variable[velts.length];

		for (int i = 0; i < velts.length; i++){
			vars[i] = ((VariableTreeElement)velts[i]).getVariable();
		}
		return vars;
	}

	public Observation[] getAllObservations(){

		TreeElement[] oelts = observationTree.getRoot().getElementOrdered();
		Observation[] obss = new Observation[oelts.length];
		for (int i = 0; i < oelts.length; i++){
			obss[i] = ((ObservationTreeElement)oelts[i]).getObservation();
		}
		return obss;
	}

	public void setDataSet(DataSet dataset){
		if(null == dataset){
			return;
		}else{
			this.dataset = dataset;
		}
	}

	public void setVariableTree(Tree t){
		if(null == t){
			return;
		}else{
			this.variableTree = t;
		}
	}

	public void setObservationTree(Tree t){
		if(null == t){
			return;
		}else{
			this.observationTree = t;
		}
	}

	public DataSet getDataSet(){
		return this.dataset;
	}

	public Tree getVariableTree(){
		return this.variableTree;
	}

	public Tree getObservationTree(){
		return this.observationTree;
	}

	public TreeMetricsModel getVariableTreeMetricsModel() {
		return varTreeMetricsModel;
	}

	public TreeMetricsModel getObservationTreeMetricsModel() {
			return obsTreeMetricsModel;
	}

	class VariableTreeMetricsModel implements TreeMetricsModel{

		public int getElementWidth(){
			return elementHeight;
		}

		public int getStemLength(){
			return stemLength;
		}
	}

	class ObservationTreeMetricsModel implements TreeMetricsModel{
		public int getElementWidth(){
			return elementWidth;
		}

		public int getStemLength(){
			return stemLength;
		}
	}
}
