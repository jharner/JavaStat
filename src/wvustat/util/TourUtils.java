package wvustat.util;

import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.util.Random;

/**
 * TourUtils generates projection frame to be used in GrandTour.
 * ** This class is no longer used.  Use TourUtil instead. *****
 */
public class TourUtils {
	
	/**
     * The threshold below which all values are considered zero.
     */
    public static double zeroThreshold = 0.000001;  
	
	private int p;
	private Matrix Aa, Az, Va, Vz, L, Ba, Bz;
	private double[] tao;
	
	/**
	 * Create a TourUtils object. Set the initial frame as identity matrix.
	 * @param p - number of dimension
	 */
	public TourUtils(int p) {
		this.p = p;
		Aa = Matrix.identity(p, 2);
	}
	
	/**
	 * Randomly generate a target frame.
	 */
	public void setTargetFrame() {
		Az = getRandomFrame(p);
		
		SingularValueDecomposition svd = Aa.transpose().times(Az).svd();
		Va = svd.getU();
		Vz = svd.getV();
		L = svd.getS();
		Ba = Aa.times(Va);
		Bz = Az.times(Vz);		
		Bz = orthonormalize(Ba, Bz, L);
		
		tao = new double[2];
		for (int i = 0; i < tao.length; i++) {
			tao[i] = Math.min(L.get(i, i), 1.0);
			tao[i] = Math.acos(tao[i]);
		}
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
		
		Matrix Bt = new Matrix(p, 2);
		for (int i = 0; i < 2; i++) {
			Matrix col1 = Ba.getMatrix(0, p-1, i, i);
			Matrix col2 = Bz.getMatrix(0, p-1, i, i);
			double a = tao[i] * t;
			Matrix col3 = col1.times(Math.cos(a)).plus(col2.times(Math.sin(a)));
			Bt.setMatrix(0, p-1, i, i, col3);
		}
		
		return Bt.times(Va.transpose());
	}	
	
	public Matrix getStartFrame() {
		return Aa;
	}
	
	public void setStartFrame(Matrix a) {
		Aa = a;
	}
	
	public Matrix getTargetFrame() {
		return Az;
	}
	
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
	 * orthonormalize B on A by each column.
	 * @param A
	 * @param B
	 * @return An orthonormalized matrix for B
	 */
	public static Matrix orthonormalize(Matrix A, Matrix B, Matrix L) {
		int p = A.getRowDimension();
		int d = A.getColumnDimension();
		Matrix m = new Matrix(p, d);
		
		for (int i = 0; i < d; i++) {
			double[] col1 = A.getMatrix(0, p-1, i, i).getColumnPackedCopy();
			double[] col2 = B.getMatrix(0, p-1, i, i).getColumnPackedCopy();
			if (Math.abs(L.get(i, i) - 1) < zeroThreshold) {
				m.setMatrix(0, p-1, i, i, new Matrix(col1, p));
			} else {
				Matrix cols = orthonormalize(col1, col2);
				m.setMatrix(0, p-1, i, i, cols.getMatrix(0, p-1, 1, 1));		
			}
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
		TourUtils tour = new TourUtils(3);
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
