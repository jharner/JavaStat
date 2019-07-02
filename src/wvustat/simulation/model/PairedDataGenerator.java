package wvustat.simulation.model;

import wvustat.util.RandomInt;
import wvustat.statistics.CommonComputation;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 31, 2003
 * Time: 4:04:04 PM
 * To change this template use Options | File Templates.
 */
public class PairedDataGenerator implements IGenerator
{
    protected List dataPairList = new ArrayList();
    protected int trialType;
    private double goal;

    public PairedDataGenerator()
    {
    }

    public PairedDataGenerator(double[] x, double[] y)
    {
        for (int i = 0; i < x.length; i++)
        {
            dataPairList.add(new DataPair(x[i], y[i]));
        }
    }

    public PairedDataGenerator(List dataPairs)
    {
        for (int i = 0; i < dataPairs.size(); i++)
        {
            dataPairList.add(dataPairs.get(i));
        }
    }

    public void setTrialType(int type)
    {
        this.trialType = type;
    }

    public void setParam(Object param)
    {
    }

    public Object runTrial(int numOfRun, double goal)
    {
        this.goal=goal;
        List sample = new ArrayList();
        switch (trialType)
        {
            case IGenerator.BOOTSTRAP:
                RandomInt ri = new RandomInt(0, dataPairList.size());
                for (int i = 0; i < dataPairList.size(); i++)
                {
                    int index = ri.nextRandom();
                    sample.add(dataPairList.get(index));
                }
                break;
            case IGenerator.RANDOMIZE:
                double[] cards = new double[dataPairList.size()];
                for (int i = 0; i < cards.length; i++)
                {
                    DataPair pair = (DataPair) dataPairList.get(i);
                    cards[i] = pair.getX().doubleValue();
                }
                BoxModel boxModel = new BoxModel();
                boxModel.setCards(cards);
                boxModel.setTrialType(IGenerator.DRAW_N_WO_REP);
                double[] permutatedX = (double[]) boxModel.runTrial(cards.length, 0);
                for (int i = 0; i < dataPairList.size(); i++)
                {
                    DataPair pair = (DataPair) dataPairList.get(i);
                    sample.add(new DataPair(permutatedX[i], pair.getY().doubleValue()));
                }
                break;

        }

        double[][] array = new double[2][sample.size()];
        for (int i = 0; i < sample.size(); i++)
        {
            DataPair pair = (DataPair) sample.get(i);
            array[0][i] = pair.getX().doubleValue();
            array[1][i] = pair.getY().doubleValue();
        }

        return array;
    }

    public double getAverage()
    {
        return 0;
    }

    public int getNumberOfDraws()
    {
        return 0;
    }

    public boolean isDepletable()
    {
        return true;
    }

    public double getSD()
    {
        return 0;
    }

    public boolean isGoalReachable(double goal)
    {
        return false;
    }

    public int getCapacity()
    {
        return dataPairList.size();
    }

    public boolean isDiscrete()
    {
        return true;
    }

    public int getOutcomeCount()
    {
        return dataPairList.size();
    }

    public double getProbability(double val)
    {
        return 0;
    }

    public Set getAllOutcomes()
    {
        return null;
    }

    public double[] getXValues()
    {
        double[] array = new double[dataPairList.size()];
        for (int i = 0; i < dataPairList.size(); i++)
        {
            DataPair pair = (DataPair) dataPairList.get(i);
            array[i] = pair.getX().doubleValue();
        }
        return array;
    }

    public double[] getYValues()
    {
        double[] array = new double[dataPairList.size()];
        for (int i = 0; i < dataPairList.size(); i++)
        {
            DataPair pair = (DataPair) dataPairList.get(i);
            array[i] = pair.getY().doubleValue();
        }
        return array;
    }

    public String getModelDescription()
    {
        if (dataPairList.size() == 0)
            return "Paired model not defined";

        StringBuffer buffer = new StringBuffer();
        double[] x = getXValues();
        double[] y = getYValues();
        DecimalFormat df = new DecimalFormat("#.###");

        buffer.append("Paired data: mean(x)=");
        buffer.append(df.format(CommonComputation.computeAverage(x)));
        buffer.append(", SD(x)=");
        buffer.append(df.format(CommonComputation.computeSD(x)));
        buffer.append(", mean(y)=");
        buffer.append(df.format(CommonComputation.computeAverage(y)));
        buffer.append(", SD(y)=");
        buffer.append(df.format(CommonComputation.computeSD(y)));
        buffer.append("; r=");
        buffer.append(df.format(CommonComputation.computeR(x, y)));
        return buffer.toString();
    }

    public int[] getCount()
    {
        int[] array = new int[dataPairList.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = 1;
        }
        return array;
    }

    public Object[] getAttributes(int index)
    {
        if (index == 0)
        {
            double[] values = getXValues();
            Number[] array = new Number[values.length];
            for (int i = 0; i < array.length; i++)
            {
                array[i] = new Double(values[i]);
            }
            return array;
        }
        else if (index == 1)
        {
            double[] values = getYValues();
            Number[] array = new Number[values.length];
            for (int i = 0; i < array.length; i++)
            {
                array[i] = new Double(values[i]);
            }
            return array;
        }
        else
            return null;
    }


    public int getObservationAttributeCount()
    {
        return 2;
    }

    public String[] getAttribueNames()
    {
        return new String[]{"X Value", "Y Value"};
    }

    public int getTrialType()
    {
        return trialType;
    }

    public double getGoal()
    {
        return goal;
    }
}
