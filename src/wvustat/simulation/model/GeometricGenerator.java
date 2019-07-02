package wvustat.simulation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Feb 23, 2003
 * Time: 4:28:02 PM
 * To change this template use Options | File Templates.
 */
public class GeometricGenerator extends BinomialGenerator
{
    public Object runTrial(int numOfRun, double goal)
    {
        this.numOfRuns = numOfRun;
        return drawUntil(goal);
    }

    public double[] drawUntil(double X){
        List v=new ArrayList();

        double d;
        do
        {
            d=getNext();
            v.add(new Double(d));
        }
        while(d!=X);

        double[] ret=new double[v.size()];
        for(int i=0;i<v.size();i++){
            ret[i]=((Double)v.get(i)).doubleValue();
        }

        return ret;
    }

    public boolean isGoalReachable(double goal)
    {
        return goal==0 || goal==1;
    }
}
