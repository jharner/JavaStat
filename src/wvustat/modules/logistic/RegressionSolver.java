/*
 * RegressionSolver.java
 *
 * Created on December 19, 2001, 11:31 AM
 */

package wvustat.modules.logistic;

import Jama.*;
/**
 * This class implements the algorithm for calculating the coefficients for logistic regression.
 *
 * @author  Hengyi Xue
 * @version
 */
public class RegressionSolver extends Object implements java.io.Serializable{
    private double[][] x;
    private double[] y;
    
    private double[] beta;

    private double[][] X, Diag, B, Y_m, Z;
    
    //Convariance matrix
    private double[][] cov;
    
    private double precision=1e-6;
    
    public RegressionSolver(){
    }
    
    /** Creates new RegressionSolver */
    public RegressionSolver(double[][] x, double[] y){
        this.x=x;
        this.y=y;        
        init();
    }
    
    private void init(){
        X=new double[y.length][x.length+1];
        for(int i=0;i<X.length;i++){
            X[i][0]=1;
            for(int j=1;j<X[i].length;j++){
                X[i][j]=x[j-1][i];
            }
        }

        B=new double[x.length+1][1];
        Diag=new double[y.length][y.length];
        Y_m=new double[y.length][1];
        Z=new double[y.length][1];
    }
    
    public void setData(double[][] x, double[] y){
        this.x=x;
        this.y=y;
        init();
    }

    
    public void setInitialEstimates(double[] beta0){
        beta=new double[beta0.length];
        for(int i=0;i<beta.length;i++)
            beta[i]=beta0[i];
    }
    
    public void setPrecision(double p){
        this.precision=p;
    }
    
    public double getBeta0(){
        return beta[0];
    }
    
    public double getBeta1(){
        return beta[1];
    }
    
    public double[][] getXArray(){
        return x;
    }
    
    public double[] getYArray(){
        return y;
    }
    
    public int getObservationCount(){
        return y.length;
    }
    
    public double[] getParameterEstimates(){
        return beta;
    }
    
    public double[] getStdErrors(){
        double[] ret=new double[beta.length];
        
        for(int i=0;i<ret.length;i++){
            ret[i]=Math.sqrt(cov[i][i]);
        }
        
        return ret;
    }
    
    public double getPearsonChiSquare(){
        double sum=0;
        for(int i=0;i<y.length;i++){
            double pi=PI(X[i]);
            sum+=Math.pow((y[i]-pi),2)/(pi*(1-pi));
        }
        
        return sum;
    }        

    /**
     * computes predicted probability
     */
    public double PI(double[] xval){
        double tmp=0;
        for(int i=0;i<beta.length;i++){
            tmp+=beta[i]*xval[i];
        }
        
        tmp=Math.exp(tmp);

        return tmp/(1+tmp);
    }    

    /**
     * computes log likelihood value
     */
    public double logLikelihood(){
        double sum=0;

        for(int i=0;i<y.length;i++){
            double li=y[i]*Math.log(PI(X[i]))+(1-y[i])*Math.log(1-PI(X[i]));
            sum+=li;
        }        

        return sum;
    }
    
    public double logLikelihoodForNull(){
        int n=y.length;
        int n1=0, n0=0;
        for(int i=0;i<y.length;i++){
            n1+=y[i];
        }
        
        n0=n-n1;
        return n1*Math.log(n1)+n0*Math.log(n0)-n*Math.log(n);
    }

    public void iterativeSolve(){
        double L0=this.logLikelihood();
        
        for(int i=0;i<beta.length;i++){
            B[i][0]=beta[i];
        }
        
        for(int i=0;i<Diag.length;i++){
            for(int j=0;j<Diag[i].length;j++){
                Diag[i][j]=0;
            }
        }
        
        for(int i=0;i<y.length;i++){
            double pi=PI(X[i]);
           
            Diag[i][i]=pi*(1-pi);
            Y_m[i][0]=y[i]-pi;
            
            Z[i][0]=Math.log(pi/(1-pi))+(y[i]-pi)/(pi*(1-pi));
        }
        
        Matrix mX=new Matrix(X);
      
        Matrix mB=new Matrix(B);
        
        Matrix mDiag=new Matrix(Diag);
        Matrix mY_m=new Matrix(Y_m);
        Matrix mZ=new Matrix(Z);
       
        Matrix tmpM=(mX.transpose().times(mDiag).times(mX)).inverse();    
        cov=tmpM.getArray();
      
        Matrix mB1=tmpM.times(mX.transpose()).times(mDiag).times(mZ);
        
        double[][] array=mB1.getArray();
        for(int i=0;i<beta.length;i++){
            beta[i]=array[i][0];
        }

        
        double L1=this.logLikelihood();
        
        if(Math.abs(L1-L0)>precision)
            iterativeSolve();
        else            
            return;       
        
    }   
    
    public void sort(double[] xvals, double[] yvals){
      
        for(int i=1;i<xvals.length;i++){
            if(i>0 && xvals[i]<xvals[i-1]){
                double key=xvals[i];
                double tmpy=yvals[i];
                int j=i-1;
                while(j>=0 && xvals[j]>key){
                    xvals[j+1]=xvals[j];
                    yvals[j+1]=yvals[j];
                    j--;
                }
                xvals[j+1]=key;
                yvals[j+1]=tmpy;
            }            
        }
    }
    
    public static void main(String[] args){
        
        double[] x={8,8,10,10,12,12,12,14,14,14,16,16,16,18,20,20,20,22,22,24,26,28,32,34,38,38,38};
        double[] y={0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1};
        
        double[][] tmpx=new double[1][x.length];
        tmpx[0]=x;
        RegressionSolver solver=new RegressionSolver();
        solver.setData(tmpx,y);
        
        solver.setInitialEstimates(new double[]{0,0});        

        solver.setPrecision(1e-8);        
        solver.iterativeSolve();
        
        System.out.println("B0="+solver.getBeta0());
        System.out.println("B1="+solver.getBeta1());
        System.out.println("Chi2="+solver.getPearsonChiSquare());
        System.out.println("L1="+solver.logLikelihood());
        System.out.println("L0="+solver.logLikelihoodForNull());
        
        /*
        double[] x1={4,5,2,7,9,3,1,12,0,15,4};
        double[] y1={4.5,5.5, 2.5, 7.5, 9.5, 3.5, 1.5, 12.5, 0.5, 15.5, 4.5};
        solver.sort(x1,y1);
        for(int i=0;i<x1.length;i++){
            System.out.print(x1[i]+",");
        }
        System.out.println();
        for(int i=0;i<y1.length;i++){
            System.out.print(y1[i]+",");
        } 
        */
        
    }
}