/*
 * NumberFormatter.java
 *
 * Created on May 30, 2002, 10:47 AM
 */

package wvustat.util;

/**
 *
 * @author  James Harner
 * @version 
 */
public class NumberFormatter extends java.lang.Object {
    private int n;
    private static NumberFormatter instance;
    /** Creates new NumberFormatter */
    protected NumberFormatter() {
    }
    
    public static NumberFormatter getInstance(){
        if(instance==null)
            instance=new NumberFormatter();
        
        return instance;
    }
    
    public void setFractionDigits(int n){
        this.n=n;
    }
    
    public String format(double number){
        double tmp=Math.round(number*Math.pow(10,n))/Math.pow(10,n);
        System.out.println(tmp);
        
        return new Double(tmp).toString();
    }
    
    public static void main(String[] args){
        double d=0.120456;
        
        NumberFormatter nf=NumberFormatter.getInstance();
        nf.setFractionDigits(3);
        
        System.out.println(nf.format(d));
    }
    

}
