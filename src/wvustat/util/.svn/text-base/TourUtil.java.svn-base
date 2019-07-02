package wvustat.util;

import Jama.Matrix;
import java.util.Random;
import wvustat.swing.TourBox;

/**
 * TourUtil generates projection frame to be used in GrandTour.
 */
public class TourUtil {
	
	/**
     * The threshold below which all values are considered zero.
     */
    public static double zeroThreshold = 0.000001;  
	
	private int p;
	private Matrix U0, U1, UStar;
	private double alpha, beta;
	private int[] constraints;
	
	/**
	 * Create a TourUtil object. Set the initial frame as identity matrix.
	 * @param p - number of dimension
	 */
	public TourUtil(int p) {
		this.p = p;
		U0 = Matrix.identity(p, 2);
		U1 = U0;
	}
	
	/**
	 * Randomly generate a target frame.
	 */
	public void setTargetFrame() {
		U1 = getRandomFrame(p);
		
		if (constraints != null) {
			for (int i = 0; i < p; i++) {
				switch (constraints[i]) {
					case TourBox.X: 
						U1.set(i, 1, 0.0); 
						break;
					case TourBox.Y:
						U1.set(i, 0, 0.0); 
						break;
					case TourBox.O:
						U1.set(i, 0, 0.0);
						U1.set(i, 1, 0.0);
						break;
				}
			}
		}
					
		UStar = orthogonalize(U0, U1);
		
		double[] a0 = U0.getMatrix(0, p-1, 0, 0).getColumnPackedCopy();
		double[] b0 = U0.getMatrix(0, p-1, 1, 1).getColumnPackedCopy();
		double[] a1 = U1.getMatrix(0, p-1, 0, 0).getColumnPackedCopy();
		double[] b1 = U1.getMatrix(0, p-1, 1, 1).getColumnPackedCopy();
		
		alpha = vectorAngle(a0, a1);
		beta = vectorAngle(b0, b1);
		
	}
	
	public void setConstraints(int[] ctr) {
		this.constraints = ctr;
	}
	
	/**
	 * Get interpolating projection <br>
	 * Attention: interpolate(1.0) may not equal to getTargetFrame()
	 * 
	 * @param t - must be 0 <= t <= 1.
	 * @return a projection matrix. <br>Return initial matrix when t=0.
	 */
	public Matrix interpolate(double t) {
		
		if (t == 0) return getStartFrame();		
		
		Matrix Ut = new Matrix(p, 2);
		
		for (int i = 0; i < 2; i++) 
		{
			Matrix col1 = U0.getMatrix(0, p-1, i, i);
			Matrix col2 = UStar.getMatrix(0, p-1, i, i);
			double a =  (i == 0) ? (t * alpha) : (t * beta);
			Matrix col3 = col1.times(Math.cos(a)).plus(col2.times(Math.sin(a)));
			Ut.setMatrix(0, p-1, i, i, col3);
		}
		
		return Ut;
	}	
	
	public Matrix getStartFrame() {
		return U0;
	}
	
	public void setStartFrame(Matrix a) {
		U0 = a;
	}
	
	public Matrix getTargetFrame() {
		return U1;
	}
	
	
	//************************* mathematical functions ***************************
	
	/**
	 * Get the inner product of two vectors
	 */
	public static double innerProduct(double[] a, double[] b) {
		double ret = 0;
		for (int i = 0; i < a.length; i++) 
			ret += a[i] * b[i];
		return ret;
	}
	
	/**
	 * Get the length of a vector
	 */
	public static double vectorLength(double[] a) {
		return Math.sqrt(innerProduct(a, a));
	}
	
	public static double vectorAngle(double[] a, double[] b) {
		return Math.acos(innerProduct(a, b) / (vectorLength(a) * vectorLength(b)));
	}
	
	/**
	 * Project vector v orthogonally onto the vector u.
	 */
	public static double[] project(double[] u, double[] v) {		
		double r = innerProduct(u, v) / innerProduct(u, u);
		double[] ret = new double[u.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = u[i] * r;
		return ret;
	}
	
	/**
	 * Get a orthogonal projection from vector v to vector u.
	 * First vector u is unchanged. The return vector is v*.
	 */
	public static double[] orthogonalProject(double[] u, double[] v) {
		double[] proj = project(u, v);
		double[] ret = new double[u.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = v[i] - proj[i];
		return ret;
	}
	
	public static double[] standardize(double[] a) {
		double d = vectorLength(a);		
		double[] ret = new double[a.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = a[i] / d;
		return ret;
	}
	
	/**
	 * orthonormalize u, v with Gram-Schmidt process
	 * @return a two column matrix
	 */
	public static Matrix orthonormalize(double[] u, double[] v) {
		Matrix m = new Matrix(u.length, 2);
		double[] e1 = standardize(u);
		double[] e2 = standardize(orthogonalProject(u, v));
		
		for (int i = 0; i < m.getRowDimension(); i++) {
			m.set(i, 0, e1[i]);
			m.set(i, 1, e2[i]);
		}
		return m;
	}
	
	/**
	 * orthogonalize B on A by each column.
	 * @param A
	 * @param B
	 * @return An orthogonalized matrix for B
	 */
	public static Matrix orthogonalize(Matrix A, Matrix B) {
		int p = A.getRowDimension();
		int d = A.getColumnDimension();
		Matrix m = new Matrix(p, d);
		
		for (int i = 0; i < d; i++) {
			double[] col1 = A.getMatrix(0, p-1, i, i).getColumnPackedCopy();
			double[] col2 = B.getMatrix(0, p-1, i, i).getColumnPackedCopy();
			
			double[] nCol = orthogonalProject(col1, col2);
			
			for (int j = 0; j < p; j++)
				m.set(j, i, nCol[j]);		
			
		}
		return m;
	}
	
	/**
	 * Randomly generate a frame that is orthonormal 
	 * @param p
	 * @return a two column matrix
	 */
	public static Matrix getRandomFrame(int p) {
		Random r = new Random();
		double[] u = new double[p];
		double[] v = new double[p];
		for (int i = 0; i < p; i++)
			u[i] = r.nextGaussian();
		for (int i = 0; i < p; i++)
			v[i] = r.nextGaussian();
		return orthonormalize(u, v);
	}
	
	public static void main(String[] argv) {
		TourUtil tour = new TourUtil(3);
		System.out.println("Start");
		tour.getStartFrame().print(3, 2);
		
		tour.setTargetFrame();
		System.out.println("Target");
		tour.getTargetFrame().print(3, 2);		
		
		double step = 0.2;
		System.out.println("Interpolate by step=" + step);
		double t = step;
		while (t <= 1) {
			Matrix proj = tour.interpolate(t);
			proj.print(3, 2);
			t += step;
		}
	}
	
}
