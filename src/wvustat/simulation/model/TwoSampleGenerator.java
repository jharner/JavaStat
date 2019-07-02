package wvustat.simulation.model;

import java.util.Set;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jul 12, 2003
 * Time: 9:09:23 AM
 * To change this template use Options | File Templates.
 */
public class TwoSampleGenerator extends AbstractGenerator
{
    private IGenerator sample1;
    private IGenerator sample2;
    private int trialType;
    private double goal;

    public TwoSampleGenerator(IGenerator sample1, IGenerator sample2)
    {
        this.sample1 = sample1;
        this.sample2 = sample2;
    }

    public IGenerator getSample1()
    {
        return sample1;
    }

    public IGenerator getSample2()
    {
        return sample2;
    }

    public void setTrialType(int type)
    {
        this.trialType = type;
    }

    public void setParam(Object param)
    {
        sample1.setParam(param);
        sample2.setParam(param);
    }

    public Object runTrial(int numOfRun, double goal)
    {
        this.goal=goal;
        if (trialType == IGenerator.BOOTSTRAP)
        {
            sample1.setTrialType(IGenerator.BOOTSTRAP);
            sample2.setTrialType(IGenerator.BOOTSTRAP);
            double[] array1 = (double[]) sample1.runTrial(numOfRun, goal);
            double[] array2 = (double[]) sample2.runTrial(numOfRun, goal);
            double[][] outcome = new double[2][];
            outcome[0] = array1;
            outcome[1] = array2;
            return outcome;
        }
        else if (trialType == IGenerator.RANDOMIZE)
        {
            int n1 = sample1.getCapacity();
            int N = sample1.getCapacity() + sample2.getCapacity();
            double[] cards = new double[N];
            BoxModel combined = new BoxModel();
            double[] subCards1 = ((BoxModel) sample1).getCards();
            double[] subCards2 = ((BoxModel) sample2).getCards();
            for (int i = 0; i < N; i++)
            {
                if (i < subCards1.length)
                    cards[i] = subCards1[i];
                else
                    cards[i] = subCards2[i - subCards1.length];
            }
            combined.setCards(cards);
            combined.setTrialType(IGenerator.DRAW_N_WO_REP);
            double[] array1 = (double[]) combined.runTrial(n1, 0);
            double[] array2 = combined.getRemainingBoxes();
            double[][] outcome = new double[2][];
            outcome[0] = array1;
            outcome[1] = array2;
            return outcome;
        }
        else
            return null;
    }

    public double getAverage()
    {
        return sample1.getAverage() - sample2.getAverage();
    }

    public int getNumberOfDraws()
    {
        return sample1.getNumberOfDraws();
    }

    public boolean isDepletable()
    {
        return sample1.isDepletable() || sample2.isDepletable();
    }

    public double getSD()
    {
        double sd = Math.pow(sample1.getSD(), 2) * (sample1.getCapacity() - 1);
        sd += Math.pow(sample2.getSD(), 2) * (sample2.getCapacity() - 1);
        sd = Math.sqrt(sd / (sample1.getCapacity() + sample2.getCapacity() - 2));
        return sd;
    }

    public boolean isGoalReachable(double goal)
    {
        return false;
    }

    public int getCapacity()
    {
        return sample1.getCapacity() < sample2.getCapacity() ? sample1.getCapacity() : sample2.getCapacity();
    }

    public boolean isDiscrete()
    {
        return true;
    }

    public int getOutcomeCount()
    {
        return Integer.MAX_VALUE;
    }

    public double getProbability(double val)
    {
        return 0;
    }

    public Set getAllOutcomes()
    {
        return null;
    }

    public int getObservationAttributeCount()
    {
        return sample1.getObservationAttributeCount();
    }

    public String[] getAttribueNames()
    {
        return sample1.getAttribueNames();
    }

    public int getTrialType()
    {
        return trialType;
    }

    public String getModelDescription()
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        StringBuffer buf = new StringBuffer();
        buf.append("Two sample model, ");
        buf.append("N1= " + getSample1().getCapacity());
        buf.append(", Mean1= " + nf.format(getSample1().getAverage()));
        buf.append(", SD1= " + nf.format(getSample1().getSD()));
        buf.append(", N2= " + getSample2().getCapacity());
        buf.append(", Mean2= " + nf.format(getSample2().getAverage()));
        buf.append(", SD2= " + nf.format(getSample2().getAverage()));
        return buf.toString();
    }

    public double getGoal()
    {
        return goal;
    }
}
