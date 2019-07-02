/*
 * NonlinearRegression.java
 *
 * Created on February 20, 2002, 9:22 AM
 */

package wvustat.statistics;
import java.util.Vector;
import java.util.Hashtable;
import java.text.NumberFormat;

import Jama.*;
import wvustat.dist.*;
import wvustat.math.expression.Expression;
import wvustat.math.expression.ExecError;
import wvustat.math.expression.ExpressionParser;
import wvustat.util.MathUtils;
/**
 *
 * @author  James Harner
 * @version
 */
public class NonlinearRegression extends java.lang.Object {
    private Expression expr;
    private Vector params;
    private double[] param_vals;
    private double[] param_SEs;
    private double[] x_vals;
    private double[] y_vals;
    private String predictor;
    private Vector derivs;
    private double[][] V;  //derivative matrix
    private double[] z; //Residual vector
    private double SS; //Sum of Squares
    private String unparsedExpr;
    
    private int iteration=0;
    /** Creates new NonlinearRegression */
    public NonlinearRegression(double[] x_vals, double[] y_vals, Expression expr, String predictor) throws InvalidDataError{
        this.x_vals=x_vals;
        this.y_vals=y_vals;
        this.expr=expr;
        this.predictor=predictor;
        
        params=expr.getVariableNames(); 
        params.removeElement(predictor);
        
        if(params.size()>=x_vals.length)
            throw new InvalidDataError("There is not enough data to estimate the parameters!");
        
        derivs=new Vector(params.size());
        V=new double[x_vals.length][params.size()];
        param_vals=new double[params.size()];
        param_SEs=new double[params.size()];
        
        z=new double[x_vals.length];
        try{
            for(int i=0;i<params.size();i++){
                String param=params.elementAt(i).toString();
                Expression d=expr.derivative(param);
                
                derivs.addElement(d);
            }
        }
        catch(ExecError e){
            e.printStackTrace();
            throw new InvalidDataError("Can not compute derivative for one of the parameters!");
        }
    }
    
    public void setUnparsedExpression(String formula){
        unparsedExpr=formula;
    }
    
    public String getUnparsedExpression(){
        return unparsedExpr;
    }
    
    public void setInitialEstimates(String[] names, double[] values) throws ExecError{
        for(int i=0;i<names.length;i++){
            int index=params.indexOf(names[i]);
            param_vals[index]=values[i];
        }
        
        Hashtable tab=new Hashtable(4);
        for(int i=0;i<params.size();i++){
            tab.put(params.elementAt(i),new Double(param_vals[i]));
        }
        
        for(int i=0;i<x_vals.length;i++){
            tab.put(predictor, new Double(x_vals[i]));
            
            z[i]=y_vals[i]-expr.value(tab);
            SS+=Math.pow(z[i],2);
        }
    }
    /**
     * Fill the derivative matrix with value and residual vector with value
     */
    private void generateV() throws ExecError{
        Hashtable tab=new Hashtable(4);
        for(int i=0;i<params.size();i++){
            tab.put(params.elementAt(i),new Double(param_vals[i]));
        }
        
        for(int i=0;i<x_vals.length;i++){
            tab.put(predictor, new Double(x_vals[i]));
            
            for(int j=0;j<params.size();j++){
                Expression d=(Expression)derivs.elementAt(j);
                V[i][j]=d.value(tab);
            }
        }
    }
    
    public void solve() throws ExecError{
        iteration=0;
        iterativeSolve();
    }
        
    public int getIteration(){
        return iteration;
    }
    
    public void iterativeSolve() throws ExecError{
        iteration++;
        generateV();
        Matrix mV=new Matrix(V);
        
        QRDecomposition qr=new QRDecomposition(mV);
        Matrix mQ=qr.getQ();
        Matrix mR=qr.getR();
        
        Matrix mR_1=mR.inverse();
        
        Matrix mZ=new Matrix(z, z.length);
        Matrix mW=mQ.transpose().times(mZ);
        
        Matrix mN=mQ.times(mW);
        Matrix mD=mR_1.times(mW);
        
        //double[][] n=mN.getArray();
        double[][] d=mD.getArray();
        
        double lambda=1.0;
        double delta;
        
        double SS1;
        
        double[] oldParams=new double[params.size()];
        System.arraycopy(param_vals,0,oldParams,0,param_vals.length);
        
        do{
            delta=0;
            SS1=0;
            for(int i=0;i<params.size();i++){
                param_vals[i]=oldParams[i]+d[i][0]*lambda;
                delta+=Math.pow(d[i][0]*lambda,2);
            }
            
            Hashtable tab=new Hashtable(4);
            for(int i=0;i<params.size();i++){
                tab.put(params.elementAt(i),new Double(param_vals[i]));
            }
            
            for(int i=0;i<x_vals.length;i++){
                tab.put(predictor, new Double(x_vals[i]));
                
                z[i]=y_vals[i]-expr.value(tab);
                SS1+=Math.pow(z[i],2);
            }
            lambda-=0.1;
        }
        while(SS1>SS && lambda>0);
        
        SS=SS1;

        if(iteration>100)
            throw new ExecError("The algorithm may never converge.");
        else if(delta<1e-6){ 
            double s=Math.sqrt(SS/(x_vals.length-params.size()));
            
            for(int i=0;i<param_SEs.length;i++){
                param_SEs[i]=0;
                for(int j=0;j<mR_1.getColumnDimension();j++){
                    param_SEs[i]+=Math.pow(mR_1.get(i,j),2);
                }
                
                param_SEs[i]=Math.sqrt(param_SEs[i])*s;
            }
            
            return;
        }
        else
            iterativeSolve();
        
    }
    
    public double getParameterValue(String name){
        int index=params.indexOf(name);
        //System.out.println(name+"="+param_vals[index]);
        return param_vals[index];
    }
    
    public double getParameterSE(String name){
        int index=params.indexOf(name);
        return param_SEs[index];
    }
    
    public String getHTMLReport(){
        StringBuffer buf=new StringBuffer();
        
        buf.append("<html><body bgcolor=\"white\">\n");
        buf.append("<font size=3><b>Model</b></font><br>\n");
        buf.append("<font size=3>"+unparsedExpr+"</font>");
        buf.append("<table width=\"100%\">\n");
        buf.append("<tr><th width=\"20%\" align=\"right\"><font size=3>Parameter</font></th>");
        buf.append("<th width=\"20%\" align=\"right\"><font size=3>Estimate</font></th>");
        buf.append("<th width=\"20%\" align=\"right\"><font size=3>SE</font></th>");
        buf.append("<th width=\"20%\" align=\"right\"><font size=3>t</font></th>");
        buf.append("<th width=\"20%\" align=\"right\"><font size=3>P-value</font></th></tr>\n"); 
        
        NumberFormat nf=NumberFormat.getInstance();
        
        StudentT t=new StudentT(x_vals.length-params.size());
        
        
        for(int i=0;i<params.size();i++){
            nf.setMaximumFractionDigits(3);
            buf.append("<tr><td width=\"20%\" align=\"right\" ><font size=3>");
            buf.append(params.elementAt(i));
            buf.append("</font></td><td width=\"20%\" align=\"right\" ><font size=3>");
            buf.append(nf.format(param_vals[i]));
            buf.append("</font></td><td width=\"20%\" align=\"right\" ><font size=3>");
            buf.append(nf.format(param_SEs[i]));
            buf.append("</font></td><td width=\"20%\" align=\"right\" ><font size=3>");
            double tVal=param_vals[i]/param_SEs[i];
            buf.append(nf.format(tVal));
            buf.append("</font></td><td width=\"20%\" align=\"right\" ><font size=3>");
            double pVal=1-t.cdf(Math.abs(tVal));
            nf.setMaximumFractionDigits(6);
            if(pVal<1e-6)
            buf.append("&lt 1e-6");
            else
            buf.append(nf.format(pVal));
            buf.append("</font></td></tr>\n");
        }
        
        buf.append("</table></body></html>");
        
        return buf.toString();
    }
    
    public double getSS(){
        return SS;
    }
    
    public double getRMSE(){
    	return Math.sqrt(SS/(x_vals.length-2));
    }
    
    public double getCorrCoeff(){
    	double total=MathUtils.getSS(y_vals);
    	return Math.sqrt(1-SS/total);
    }
    
    public Vector getParams(){
    	return params;
    }
    
    public static void main(String[] args){
        double[] y0={3.93,5.31,7.24,9.64,12.87,17.07,23.19,31.44,39.82,50.16,62.95,75.99,91.97,105.71,122.78,131.67,151.32,179.32,203.21,226.5,248.7};
        double[] x0={1790,1800,1810,1820,1830,1840,1850,1860,1870,1880,1890,1900,1910,1920,1930,1940,1950,1960,1970,1980,1990};
        double[] y={0.07739, 0.06887,0.08194,0.0737,0.07388,0.07124,0.06504,0.05477,0.04971,0.06427,0.0613,0.06436,0.03939};
        double[] x={0.417,0.417, 0.417,0.833,0.833,0.833,1.67,1.67,3.75,3.75,6.25,6.25,6.25};
        
        String formula="V/(K+x)*x";
        String formula0="B0*exp(B1*(x-1790))";
        try{
        	   ExpressionParser ep = new ExpressionParser();
            Expression expr=ep.parse(formula);
            
            NonlinearRegression nr=new NonlinearRegression(x,y,expr,"x");
            
            nr.setInitialEstimates(new String[]{"V", "K"},new double[]{0.08,-0.2});
            
            nr.iterativeSolve();
            
            /*
            System.out.println("V="+nr.getParameterValue("V"));
            System.out.println("SE of V= "+nr.getParameterSE("V"));
            System.out.println("K="+nr.getParameterValue("K"));
            System.out.println("SE of K= "+nr.getParameterSE("K"));
            */
            System.out.println(nr.getHTMLReport());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        catch(Error err){
            err.printStackTrace();
        }
    }
    
    
}
