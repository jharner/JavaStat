package wvustat.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
*	StatEngine defines an intwrface for a distributed statistical computing server. Clients
*	can send request to the server to do computation and get results over network.
*
*	@author: Hengyi Xue
*	@version: 1.5, Mar. 9, 2000
*/

public interface StatEngine extends Remote{
	
  /**
   *	Obtain a distribution from the server which is identified by a symbol such as
   *	"normal", "t", etc. and the parameters supplied.
   *
   *	@param symbol symbol for the distribution, "normal", "t" and "F" are supported values
   *	@param params	the array that stores the parameters for the distribution.
   */
  public Distribution getDistribution(String symbol, double[] params) throws RemoteException;
  
  /**
  *	Compute the mean of data contained in a vector.
  */
  public double getMean(Vector v) throws RemoteException;

  /**
  *	Compute the mean of values contained in an array
  */
  public double getMean(double[] vals) throws RemoteException;
  
  /**
  * Compute standard deviation for values contained in a vector
  */  
  public double getStdDev(Vector v) throws RemoteException;
  
  /**
  *	Compute standard deviation for values contained in an array
  */
  public double getStdDev(double[] vals) throws RemoteException;
  
  /**
  *	Find quantiles (min, lower quartile, median, upper quartile and max) for values
  *	contained in a vector.
  */
  public double[] getQuantiles(Vector v) throws RemoteException;

  /**
  *	Find quantiles (min, lower quartile, median, upper quartile and max) for values
  *	contained in an array.
  */ 
  public double[] getQuantiles(double[] vals) throws RemoteException;
  
  /**
  *	Sort an array and return the sorted array
  */
  public double[] sortArray(double[] array) throws RemoteException;
  
  /**
  * Find the ranks for each value in an array of double
  */
  public int[] getRanks(double[] array) throws RemoteException;

  /**
  *	Find the minimum value in an array
  */
  public double getMin(double[] array) throws RemoteException;

  /**
  *	Find the maximum value in an array
  */
  public double getMax(double[] array) throws RemoteException;

    /**
     *	Perform linear regression on values given by arrays y and x and return coefficients a and b,  correlation
     * coefficient rou, standard error of b, p value of b=0, sum of squared error for x, sum of residual for prediction
     * divided by degree of freedom
     */
    public double[] regress(double[] y, double[] x) throws RemoteException;
    
    /**
     * Get the frequency matrix given two categorical variable and a constraint vector.
     */
    public double[][] getFrequencyMatrix(Variable v1, Variable v2, double[] constraints) throws RemoteException;
    
}
