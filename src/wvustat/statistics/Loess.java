/*
 * Created on Mar 24, 2006
 */
package wvustat.statistics;

import wvustat.util.MathUtils;
import Jama.*;
import java.util.*;

/**
 * @author dajieluo
 *
 */
public class Loess {
	
	private double span;
	private double[] xvals, yvals;
	private int q;

	public Loess(double span, double[] x, double[] y) throws InvalidDataError
	{
		this.span = span;
		this.xvals = x;
		this.yvals = y;
		
		q = (int)(this.span * xvals.length);
		if (q < 3){ 
			throw new InvalidDataError("span too small");
		}
	}
	
	public double getMinSpan()
	{
		return 3.0/xvals.length;
	}
	
	public double getSpan()
	{
		return span;
	}
	
	public void setSpan(double span) throws InvalidDataError
	{
		this.span = span;
		q = (int)(this.span * xvals.length);
		if (q < 3){ 
			throw new InvalidDataError("span too small");
		}
	}
	
	public double getFit(double x) throws InvalidDataError
	{
		
		double[] distance = new double[xvals.length];
		
		for(int i = 0; i < distance.length; i++){
			distance[i] = Math.abs(xvals[i] - x);
		}
		
		MathUtils.InsertionSort(distance);
		
		double denom;
		
		if(q <= xvals.length){
			denom = distance[q - 1];
		}else{
			denom = distance[xvals.length - 1] * span;
		}
		
		
		double[] weights = new double[xvals.length];
		for(int i = 0; i < weights.length; i++){
			weights[i] = tricube( Math.abs(xvals[i] - x) / denom );
		}
		

		Vector iv = new Vector();
		
		for(int i = 0; i < weights.length; i++){
			if(weights[i] > 0){
                 iv.addElement(new Integer(i));
			}
		}
		
		if(iv.size() < 2) 
			throw new InvalidDataError("span too small");
		
		double[][] arrayw = new double[iv.size()][iv.size()];
		double[][] arrayx = new double[iv.size()][2];
		double[] arrayy = new double[iv.size()];
		
		for (int i = 0; i < iv.size(); i++){
			int index = ((Integer) iv.elementAt(i)).intValue();
			
			for( int j = 0; j < iv.size(); j++){
				arrayw[i][j] = 0;
			}
			arrayw[i][i] = 1/weights[index];
			
			arrayx[i][0] = 1;
			arrayx[i][1] = xvals[index];
			arrayy[i] = yvals[index];
		}

		Matrix mw = new Matrix(arrayw);
		Matrix mx = new Matrix(arrayx);
		Matrix my = new Matrix(arrayy, arrayy.length);
		
		Matrix mxt = mx.transpose();
		Matrix mwi = mw.inverse();
		Matrix mxt_mwi = mxt.times(mwi);
		Matrix mxt_mwi_mx = mxt_mwi.times(mx);
		
		if(mxt_mwi_mx.rank() < mxt_mwi_mx.getRowDimension())
			throw new InvalidDataError("singular matrix");
		
		Matrix mcoef = (mxt_mwi_mx).inverse().times(mxt_mwi).times(my);
		
		double a = mcoef.getArray()[0][0];
		double b = mcoef.getArray()[1][0];
		double fit = a + b*x;
	
		return fit;
	}
	
	private double tricube(double u)
	{
		if(Math.abs(u) < 1)
			return Math.pow(1 - Math.pow(Math.abs(u),3), 3);
		else
			return 0.0;
	}
	
	public static void main(String[] argv){
		/*double[][] vals = {{1.,2.,3},{4.,5.,6.}};
		Matrix A = new Matrix(vals);
		System.out.println(A.getRowDimension());
		System.out.println(A.getColumnDimension());
		System.out.println(vals.length);
		System.out.println(vals[0].length);
		A.print(2,1);*/
		
		double[] x = {13,21,24,23,64,38,33,61,39,71,112,88,54};
		double[] y = {4,18,14,18,26,26,21,30,28,36,65,62,40};
		
		try{
			Loess lo = new Loess(0.4,x,y);
			System.out.println(lo.getFit(36.0));
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}
