package wvustat.simulation.centrallimit;

public class random {
	
	public double lowerBd;
	public double upperBd;
	public String distName = new String("uniform");
	private int	df = 4;
	
	random(double lb, double ub) {
		
		lowerBd = lb;
		upperBd = ub;
	}
	
	random(){
	
		this(0.0, 1.0);
	}
	
	public void setDistribution(String name) {
	
		distName = name;
		if ( distName.equals("normal") )	setBound(-5.0, 5.0);
		if ( distName.equals("chisq") )		setBound(0.0, 10.0);
		if ( distName.equals("uniform") )	setBound(-1.0, 1.0);
		if ( distName.equals("bowtie") )	setBound(-1.0, 1.0);
		if ( distName.equals("wedge_L") )	setBound(0.0, 2.0);
		if ( distName.equals("wedge_R") )	setBound(0.0, 2.0);
		if ( distName.equals("triangle") )	setBound(-1.0, 1.0);	
	}

	public void setBound(double lb, double ub) {
		
		if ( lb < ub ) { lowerBd = lb; upperBd = ub; }
		else { lowerBd = lb; upperBd = ub; }
	}

	public double getRandomNumber() {
		return(getRandomNumber(distName));
	}
			
	public double getRandomNumber(String name, double lb, double ub) {
	
		setBound(lb, ub);
		return(getRandomNumber(name));
	}
	
	public double getRandomNumber(String name) {
		
		if 		( name.equals("normal"))	return(normal());
		else if ( name.equals("chisq"))		return(chisq());
		else if ( name.equals("uniform"))	return(uniform());
		else if ( name.equals("wedge_L"))	return(wedge_L());
		else if ( name.equals("wedge_R"))	return(wedge_R());
		else if ( name.equals("bowtie"))	return(bowtie());
		else if ( name.equals("triangle"))	return(triangle());
		
		else { return (0.0); }
		
	}
	
	private double uniform(){
		
		double model;
		
		model = Math.random();
		return(model*(upperBd-lowerBd)+lowerBd);
	}
	
	private double triangle(){
		
		double model;
		double height	= 2.0/(upperBd-lowerBd);
		//double middle	= (upperBd+lowerBd)/2.0;
		double middle = 0;
		model = Math.random();
		if ( model <= 0.50 )
				return ( Math.sqrt(model*2.0) - 1 );
				//return(Math.sqrt(model*2.0)*(middle-lowerBd)+lowerBd);
		else 	//return(upperBd-Math.sqrt((1.0-model)*2.0)*(upperBd-middle)+lowerBd);
				return ( 1.0 - Math.sqrt((1.0-model)*2.0));
	}
	
	private double wedge_R(){
		
		double model;
		double height = 2.0/(upperBd-lowerBd);
		
		model = Math.random();
		return(Math.sqrt(model)*2);
	}
	
	private double wedge_L(){
		
		double model;
		double height = 2.0/(upperBd-lowerBd);
		 
		model = wedge_R();
		return(upperBd+lowerBd-model);
	}
	
	private double bowtie(){
		
		double model;
		
		model = triangle();
		if ( model < 0 )
				return(lowerBd - model);
		else	return(upperBd - model);
	}
	
	private double normal(){
		
		double model;
		double sum = 0;
		
		for ( int i = 0; i < 16; i ++ )
			sum += Math.random();
		return((sum-8)/1.1547);
	}
	
	public void setDF(int value){
		
		df = value;
	}
	
	public int getDF(){
		
		return(df);
	}
	
	private double chisq(){
		
		double model;
		double sum = 0.0;
		for ( int i = 0; i < df; i++ )
			sum += Math.pow(normal(),2.0);
		return(sum);
	}
	
	
}
