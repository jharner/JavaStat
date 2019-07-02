/*
 * ConstantsTable.java
 *
 * Created on September 13, 2000, 3:07 PM
 */

package wvustat.modules.ControlChart;

/**
 * ConstantsTable stores constants such as D3, D4, B5, B6 and provides a convenient way of
 * accessing them.
 *
 * @author  Hengyi Xue
 * @version 1.0, Sep. 13, 2000
 */
public class ConstantsTable extends Object {

    protected double[] D3_values={0,0,0,0,0,0.076,0.136,0.184,0.223,0.256,0,283,0.307,0.328,0.347,
    0.363,0.378,0.391,0.403,0.415,0.425,0.434,0.443,0.451,0.459};
    protected double[] D4_values={3.267,2.575,2.282,2.115,2.004,1.924,1.864,1.816,1.777,1.744,1.717,
    1.693,1.672,1.653,1.637,1.622,1.608,1.597,1.585,1.575,1.566,1.577,1.548,1.541};
    protected double[] B5_values={0,0,0,0,0.029,0.113,0.179,0.232,0.276,0.313,0.346,0.374,0.399,0.421,
    0.44,0.458,0.475,0.49,0.504,0.516,0.528,0.539,0.549,0.559};
    protected double[] B6_values={2.606,2.276,2.088,1.964,1.874,1.806,1.751,1.707,1.669,1.637,1.610,
    1.585,1.563,1.544,1.526,1.511,1.496,1.483,1.470,1.459,1.448,1.438,1.429,1.420};
    protected double[] B3_values={0,0,0,0,0.030,0.118,0.185,0.239,0.284,0.321,0.354,0.382,0.406,0.428,
        0.448,0.466,0.482,0.497,0.510,0.523,0.534,0.545,0.555,0.565};
    protected double[] B4_values={3.267,2.568,2.266,2.089,1.97,1.882,1.815,1.761,1.716,1.679,1.646,1.618,
        1.594,1.572,1.552,1.534,1.518,1.503,1.49,1.477,1.466,1.455,1.445,1.435};
    
    /* Creates new ConstantsTable */
    public ConstantsTable() {
    }

    public double getD3(int n){
        if(n<2||n>25)
        return Double.NaN;
        else
        return D3_values[n-2];
    }

    public double getD4(int n){
        if(n<2||n>25)
        return Double.NaN;
        else
        return D4_values[n-2];
    }

    public double getB5(int n){
        if(n<2)
        return Double.NaN;
        else if(n<=25)
        return B5_values[n-2];
        else{
            double c4=4.0*(n-1.0)/(4.0*n-3.0);
            return (c4-3.0/Math.sqrt(2.0*(n-1.0)));
        }
    }

    public double getB6(int n){
        if(n<2)
        return Double.NaN;
        else if(n<=25)
        return B6_values[n-2];
        else{
            double c4=4.0*(n-1.0)/(4.0*n-3.0);
            return (c4+3.0/Math.sqrt(2.0*(n-1.0)));
        }
    }
    
    public double getB3(int n){
        if(n<2)
        return Double.NaN;
        else if(n<=25)
        return B3_values[n-2];
        else{
            double c4=4.0*(n-1.0)/(4.0*n-3.0);
            double ret=1-3.0/c4/Math.sqrt(2*(n-1.0));
            return ret;
        }    
    }
    
    public double getB4(int n){
        if(n<2)
        return Double.NaN;
        else if(n<=25)
        return B4_values[n-2];
        else{
            double c4=4.0*(n-1.0)/(4.0*n-3.0);
            double ret=1+3.0/c4/Math.sqrt(2*(n-1.0));
            return ret;
        }    
    }
}
