package wvustat.simulation.centrallimit;

public class publicData {
	
	CtrLimit		host;
	int				distIndex = 0;
	public	final int[] 	paraUpper = { 25, 5000, 5000, 50 };
	public	final int[] 	paraLower = { 1, 50, 1, 2 };
	private int[]			paraInit = { 1, 1000, 200, 30 };
	private	int[] 			paraValue = { 5, 1000, 200, 30 };
	
	publicData( CtrLimit containedBy ){
		host = containedBy;
	}
	
	// "Set" Methods
	public final void setSampleSize(int sampleSize) {
		paraValue[0] = sampleSize;
	}	
	
	public final void setTotalSample(int totalSample) {
		paraValue[1] = totalSample;
		// Delete Feb-6-2000
		//host.hBar.reset(0, totalSample, 0);
	}
		
	public final void setSamplePerSecond(int samplePerSecond) {
		paraValue[2] = samplePerSecond;
	}
	
	public final void setBarNumber(int BarNumber) {
		paraValue[3] = BarNumber;
	}
	
	public final void setInitParaValue(int index, int value) {
		if ( isLegal(index, value) ) {
			paraInit[index] = value;
			paraValue[index] = value;
		}
	}
	
	public final void setParaValue(int index, int value) {
		if ( isLegal(index, value) )
			paraValue[index] = value;
	}
	
	public final void setAllParameter(int[] parameter) {
		paraValue = parameter;
	}
	
	// "Get" Methods
	public final int getSampleSize() {
		return(paraValue[0]);
	}
	
	public final int getTotalSample() {
		return(paraValue[1]);
	}

	public final int getSamplePerSecond() {
		return(paraValue[2]);
	}

	public final int getBarNumber() {
		return(paraValue[3]);
	}
	
	public final int getParaValue(int index) {
		return(paraValue[index]);
	}
	
	public final int[] getAllParameter() {
		return(paraValue);
	}
	
	public final int getDefaultValue(int index) {
		return(paraInit[index]);
	}
	
	public final boolean isLegal(int index, int value) {
		if ( ( paraValue[index] >= paraLower[index] )
			|| ( paraValue[index] <= paraUpper[index] ) )
				return true;
		else	return false;
	}
	 
	// pre-test if setting is valid
	public final boolean validParaSetting(int[] parameter) {
		boolean validFlag = true;
		for ( int i = 0; i <= 4; i++ ) 
			if ( ( parameter[i] > paraUpper[i] ) || ( parameter[i] < paraLower[i] ) )
				validFlag = false;
		
		if ( getSamplePerSecond() > getSampleSize() )
			validFlag = false;
		
		return(validFlag);
	}

	public final void reset() {

		distIndex = 0;
		for ( int i = 0; i < 4; i ++ )
			paraValue[i] = paraInit[i];
	}	
	
	public final void reset(int index) {
	
		distIndex = index;
		//paraValue = paraInit;
	}	
	
	public final int getDistIndex() {
	
		return(distIndex);
	}	
}
	
