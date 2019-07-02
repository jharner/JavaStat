/*
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Nov 3, 2002
 * Time: 9:13:50 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package wvustat.simulation.model;

import java.util.Random;
import java.util.Set;

public class NormalGenerator extends AbstractGenerator{
    private Random random;
    private double mu, sigma;
    private int numOfRuns;
    private double goal;

    public NormalGenerator()
    {
        long seed=System.currentTimeMillis();
        random=new Random(seed);
    }

    public void setParam(Object obj)
    {
        double[] array=(double[])obj;
        mu=array[0];
        sigma=array[1];
    }

    public double getNext()
    {

        return random.nextGaussian()*sigma+mu;
    }

    public double getSD()
    {
        return sigma;
    }

    public double getAverage()
    {
        return mu;
    }

    public void setTrialType(int type)
    {
    }

    public boolean isGoalReachable(double goal)
    {
        return true;
    }

    public boolean isDiscrete()
    {
        return false;
    }

    public int getOutcomeCount()
    {
        return Integer.MAX_VALUE;
    }

    public Set getAllOutcomes()
    {
        return null;
    }

    public double getProbability(double val)
    {
        return 0;
    }

    public int getCapacity()
    {
        return Integer.MAX_VALUE;
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
        this.numOfRuns=numOfRun;
        this.goal=goal;
        double[] array=new double[numOfRun];
        for(int i=0;i<numOfRun;i++)
        {
            array[i]=getNext();
        }
        return array;
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
        buffer.append("Normal(Gaussian) random sequence, mu=").append(mu);
        buffer.append(", sigma=").append(sigma);
        return buffer.toString();
    }

    public double getGoal()
    {
        return goal;
    }
}
