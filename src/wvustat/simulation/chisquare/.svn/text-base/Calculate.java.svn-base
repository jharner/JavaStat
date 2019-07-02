/*

*   Calculate.java

*   author: Ximing Zhao

*/



package wvustat.simulation.chisquare;



import java.util.Random;



public class Calculate extends Object {



	private double[][] model;

	private double[] expected;

	private double[] observed;

        private double[] simulated;

	private FiniteDistribution box;

    	private int nDraws;

    	private int nOver;

        private double chiSquareObtained;

    	private double chiSquareRounded;

	private VariableType outcome;

   

	public Calculate() {

	

	}

	

	public void setModel(double[][] array) {

	

	this.model=array;

	

	expected=new double[model[0].length];

	observed=new double[model[0].length];

	

	for(int i=0;i<model[0].length;i++){

		expected[i]=model[0][i];

		observed[i]=model[1][i];

	};

	

	}



	public double[] getExpected(){

		return expected;

	}

	

	public double[] getObserved(){

		return observed;

	}

	

	public double[] getSimulated(){

		return simulated;

	}

	

	public int getLength(){

		return expected.length;

	}

	

	public double getSum(){

		double sum=0;

		for(int i=0;i<observed.length;i++){

			sum=sum+observed[i];

		}

		return sum;

	}

	

	

	public double getchiSquareObtained(){

		

		chiSquareObtained = chisquare(expected.length, observed, expected);

		return chiSquareObtained;

	}

	

	public double getchiSquareRounded(){

		

		chiSquareRounded = round(getchiSquareObtained(), 2);

		return chiSquareRounded;

	}

/*	

        int i = model[0].length - 1;

//        chiMax = Math.max(chiSquareObtained + 1.0D, (double)i + 3D * Math.sqrt(2D * (double)i));

        

        messageArea.setForeground(Color.black);

        messageArea.setText("Obtained Chi-square = " + chiSquareObtainedRounded + ". " + nCells + "-outcome experiment. Choose the # of trials.");

        chisqTextArea.setText("");

        ok = true;

    } */



    public double chisquare(int i, double[] observed, double[] expected)

    {

        double d = 0.0D;

        for(int j = 0; j < i; j++)

        {

            double d1 = observed[j];

            double d2 = expected[j];

            if(d2 > 0.0D)

                d += ((d1 - d2) * (d1 - d2)) / d2;

        }



        return d;

    }



    

    public double round(double d, int i)

    {

        int j = 1;

        for(int k = 0; k < Math.abs(i); k++)

            j *= 10;



        double d1;

        if(i >= 0)

        {

            d1 = (int)((double)j * d + 0.5D);

            d1 /= j;

        } else

        {

            d1 = (int)(d / (double)j + 0.5D);

            d1 *= j;

        }

        return d1;

    }	



    public double oneSimulation()

    {   box = new FiniteDistribution();

        simulated=new double[getLength()];

        for(int j = 0; j < getLength(); j++)

            box.addValue(j, expected[j]);



        nDraws = (int)(getSum() + 0.001D);

        box.collectDraws(true);

        box.collectDrawsHist(true);

        box.randomSample(nDraws);

        for(int i = 0; i < getLength(); i++)

        {

            double d = box.drawsHist().densityI(i);

            simulated[i]=d;

            

        }

        

        double d1 = chisquare(getLength(), simulated, expected);

        d1 = round(d1, 2);

        return d1;

    }

    

    public double[] getChisquare(VariableType variable) {

    	double[] d=new double[variable.size()];

    	for (int i=0;i<variable.size();i++){

    		d[i]=variable.x(i);

    	};

    	return d;

    }

    

    public double getProbability(double[] stats,double chisquare){

    	double nOver=0;

    	for(int i=0;i<stats.length;i++){

    		if (stats[i]>=chisquare) nOver++;

    	};

    	

    	return nOver;

    }



}