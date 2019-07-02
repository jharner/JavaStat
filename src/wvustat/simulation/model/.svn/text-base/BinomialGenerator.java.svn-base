/*
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Nov 3, 2002
 * Time: 9:24:44 AM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package wvustat.simulation.model;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class BinomialGenerator extends AbstractGenerator
{
    protected double p;
    private Random random;
    protected int numOfRuns;
    protected double goal;

    public BinomialGenerator()
    {
        random = new Random();
    }

    public void setParam(Object obj)
    {
        p = ((Double) obj).doubleValue();
    }

    public void setTrialType(int type)
    {
    }

    public int getOutcomeCount()
    {
        return 2;
    }

    public Set getAllOutcomes()
    {
        Set set = new HashSet();
        set.add(new Integer(0));
        set.add(new Integer(1));

        return set;
    }

    public double getProbability(double val)
    {
        if (val == 0 || val == 1)
            return 0.5;
        else
            return 0;
    }

    public boolean isDiscrete()
    {
        return true;
    }

    public int getCapacity()
    {
        return Integer.MAX_VALUE;
    }

    public boolean isGoalReachable(double goal)
    {
        return true;
    }

    public boolean isDepletable()
    {
        return false;
    }

    public int getNumberOfDraws()
    {
        return numOfRuns;
    }

    public Object runTrial(int numOfRun, double goal)
    {
        this.numOfRuns = numOfRun;
        this.goal=goal;
        double[] array = new double[numOfRun];
        for (int i = 0; i < numOfRun; i++)
        {
            array[i] = getNext();
        }
        return array;
    }

    public double getNext()
    {

        double r = random.nextDouble();
        if (r <= p)
            return 1;
        else
            return 0;
    }

    public double getSD()
    {
        return Math.sqrt(p * (1 - p));
    }

    public double getAverage()
    {
        return p;
    }

    public int getObservationAttributeCount()
    {
        return 1;
    }

    public int getTrialType()
    {
        return IGenerator.DRAW_N_W_REP;
    }

    public String getModelDescription()
    {
        StringBuffer buffer=new StringBuffer();
        buffer.append("Binomial random sequence, p=").append(p);
        return buffer.toString();
    }

    public double getGoal()
    {
        return goal;
    }
}
