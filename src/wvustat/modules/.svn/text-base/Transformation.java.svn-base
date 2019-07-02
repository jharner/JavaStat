/*
 * Transformation.java
 *
 * Created on Feb 23, 2006, 14:55 PM
 */

package wvustat.modules;

/**
 *
 * @author  djluo
 * @version
 */
public class Transformation extends Object {
    public static final int SQUARE=0;
    public static final int SQUARE_ROOT=1;
    public static final int LOGRITHMIC=2;
    public static final int INVERSE=3;
    public static final int CANONICAL=4;

    private int xMethod; //Transformation method
    private double constant;

    /** Creates new Transformation */
    public Transformation(int xMethod, double c) {
        this.xMethod=xMethod;
        constant=c;
    }

    public Transformation(int xMethod){
        this(xMethod,0);
    }

    public int getTransformationMethod(){
        return xMethod;
    }

    public double getConstant(){
        return constant;
    }

    public void setConstant(double c){
        constant=c;
    }

    public double transform(double x0) {
        double x1=Double.NaN;


        switch(xMethod){
            case SQUARE:
            x1=x0*x0;
            break;
            case SQUARE_ROOT:
            x1=Math.sqrt(x0+constant);
            break;
            case LOGRITHMIC:
            x1=Math.log(x0+constant)/Math.log(10);
            break;
            case INVERSE:
            x1=1/x0;
            break;
            case CANONICAL:
            x1=x0;
            break;
        }

        return x1;

    }
    
    public String getDescription(){
        String desc="";
        
        switch(xMethod){
            case SQUARE:
            desc="(";
            break;
            case SQUARE_ROOT:
            desc="sqrt(";
            break;
            case LOGRITHMIC:
            desc="log(";
            break;
            case INVERSE:
            desc="1/(";
            break;
        }        
        
        return desc;
    }
    
    public String getTransformedVarName(String varName){
        String label=getDescription()+varName;
        if(getConstant()!=0)
            label+="+"+getConstant();
        if(getTransformationMethod() != Transformation.CANONICAL)
            label+=")";
        if(getTransformationMethod()==Transformation.SQUARE)
            label+="^2";
        
        return label;
    }
        
}