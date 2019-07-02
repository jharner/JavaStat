package wvustat.modules;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.*;
import java.util.*;
import java.io.*;

import wvustat.interfaces.*;
import wvustat.util.MathUtils;
import wvustat.dist.*;
import Jama.*;


public class StatEngineImpl implements StatEngine{
    
    public StatEngineImpl() {
        super();      
    }

    public Distribution getDistribution(String symbol, double[] params) throws RemoteException{
        Distribution d=null;

        if(symbol.equalsIgnoreCase("normal")){
            if(params.length==2)
            d=new Normal(params[0], params[1]);
        }
        else if(symbol.equalsIgnoreCase("t")){
            if(params.length==1)
            d=new StudentT(params[0]);
        }
        else if(symbol.equalsIgnoreCase("f")){
            if(params.length==2)
            d=new FDistribution((int)params[0], (int)params[1]);
        }
        else if(symbol.equalsIgnoreCase("chi square")){
            if(params.length==1)
            d=new Chi2Dist((int)params[0]);
        }
        else if(symbol.equalsIgnoreCase("kernel")){
        	    if(params.length>=2){
        	    		double[] x= new double[params.length - 1];
        	    		for(int i=1; i<params.length; i++)
        	    			x[i-1] = params[i];
        	    		d=new Kernel(params[0],x);
        	    }
        }
        
        return d;
    }

    public double getMean(Vector v) throws RemoteException{
        double[] vals=new double[v.size()];

        for(int i=0;i<v.size();i++){
            vals[i]=((Double)v.elementAt(i)).doubleValue();
        }

        return MathUtils.getMean(vals);
    }

    public double getMean(double[] vals) throws RemoteException{
        return MathUtils.getMean(vals);
    }


    public double getStdDev(Vector v) throws RemoteException{
        double[] vals=new double[v.size()];

        for(int i=0;i<v.size();i++){
            vals[i]=((Double)v.elementAt(i)).doubleValue();
        }

        return MathUtils.getStdDev(vals);
    }

    public double getStdDev(double[] vals) throws RemoteException{
        return MathUtils.getStdDev(vals);
    }

    //Find the min, lower quartile, median, upper quartile and max of a vector of values
    public double[] getQuantiles(Vector v) throws RemoteException{
        double[] vals=new double[v.size()];

        for(int i=0;i<v.size();i++){
            vals[i]=((Double)v.elementAt(i)).doubleValue();
        }

        return MathUtils.getQuantiles(vals);

    }

    public double[] getQuantiles(double[] vals) throws RemoteException{
        return MathUtils.getQuantiles(vals);
    }

    public double[] sortArray(double[] array) throws RemoteException{
        double[] sorted=new double[array.length];

        for(int i=0;i<array.length;i++) sorted[i]=array[i];

        MathUtils.InsertionSort(sorted);

        return sorted;
    }

    public int[] getRanks(double[] array) throws RemoteException{
        return MathUtils.getRanks(array);
    }

    public double getMin(double[] array) throws RemoteException{
        return MathUtils.getMin(array);
    }

    public double getMax(double[] array) throws RemoteException{
        return MathUtils.getMax(array);
    }

    /**
     * Perform a linear regression on arrays y and x and return coefficients a and b,  correlation
     * coefficient rou, standard error of b, p value of b=0, sum of squared error for x, sum of residual for prediction
     * divided by degree of freedom
     */
    public double[] regress(double[] y, double[] x) throws RemoteException, IllegalArgumentException{
        if(x.length!=y.length)
        throw new IllegalArgumentException();

        return MathUtils.regress(y,x);
    }

    public double[][] getFrequencyMatrix(Variable v1, Variable v2, double[] d) throws RemoteException{

        double[][] array1=v1.getDummyValues();
        double[][] array2=v2.getDummyValues();

        if(d!=null){
            for(int i=0;i<array1.length;i++){
                for(int j=0;j<array1[i].length;j++){
                    array1[i][j]=array1[i][j]*d[i];
                }
            }

        }

        Matrix m1=new Matrix(array1);

        Matrix m2=new Matrix(array2);

        Matrix m3=m1.transpose().times(m2);

        return m3.getArray();

    }    

}

