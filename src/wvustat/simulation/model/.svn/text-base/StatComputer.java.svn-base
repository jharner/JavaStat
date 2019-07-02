/*
 * StatComputer.java
 *
 * Created on March 11, 2002, 4:18 PM
 */

package wvustat.simulation.model;

/**
 * StatComputer is a class that computes the statistics of the outcome of a trial.
 *
 * @author  Hengyi Xue
 * @version
 */

import gnu.trove.TDoubleArrayList;

import java.util.*;

import wvustat.statistics.CommonComputation;

public class StatComputer extends Object
{
    public static final String AVERAGE = "Mean";
    public static final String SUM = "Sum";
    public static final String MIN = "Min";
    public static final String MAX = "Max";
    public static final String MEDIAN = "Median";
    public static final String IQR = "Interquartile Range";
    public static final String SD = "Standard Deviation";
    public static final String SAMPLE_SIZE = "Sample Size";
    public static final String THE_DRAW = "The Draw";
    public static final String Z = "Z";
    public static final String t = "t";
    public static final String P_HAT = "P_hat";
    public static final String D = "D";
    public static final String CHI_SQUARE = "Chi-square";
    public static final String CORR_COEFF="Correlation Coefficient";

    public final static String[] eventChoices = {"", "X<=a", "X>=a", "X=a", "X>a", "X<a", "a<=X<=b", "a<=X<b", "a<X<=b", "a<X<b"};
    public static final String X_LE_A = "X<=a";
    public static final String X_GE_A = "X>=a";
    public static final String X_EQ_A = "X=a";
    public static final String X_GT_A = "X>a";
    public static final String X_LT_A = "X<a";
    public static final String A_LE_X_LE_B = "a<=X<=b";
    public static final String A_LE_X_LT_B = "a<=X<b";
    public static final String A_LT_X_LE_B = "a<X<=b";
    public static final String A_LT_X_LT_B = "a<X<b";

    private String statType;

    private static int eventIndex = -1;

    private double average, sd, SS = 0, sum = 0; //Average, Standard Deviation and Sum of Squares
    private double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
    private TDoubleArrayList statList;
    private double a=Double.NaN, b=Double.NaN;
    private int successCnt = 0;
    private ArrayList sucList = new ArrayList();
    private IGenerator numberGenerator;

    public StatComputer()
    {
        //statList = new double[500];
        statList = new TDoubleArrayList();
    }

    /*
    public void addRun(int runSize)
    {
        //this.runSize = runSize;
        if (currentIndex + runSize > statList.length)
        {
            double[] tmpArray = new double[statList.length + runSize];
            System.arraycopy(statList, 0, tmpArray, 0, statList.length);
            statList = tmpArray;
        }
    }
    */

    public void setNumberGenerator(IGenerator generator)
    {
        this.numberGenerator = generator;
    }

     public IGenerator getNumberGenerator()
     {
         return numberGenerator;
     }

    /**
     * Set the type of statistics that this class should compute
     *
     * @param statType This parameter should have one of the constant values defined in this class
     */
    public void setStatType(String statType)
    {
        this.statType = statType;
    }

    public static void setEventIndex(int index)
    {
        eventIndex = index;
    }

    public static int getEventIndex()
    {
        return eventIndex;
    }

    public int getSuccessCount()
    {
        return successCnt;
    }

    /**
     * Get the type of statistics that this class computes
     */
    public String getStatType()
    {
        return statType;
    }


    /**
     * Return the average of the statistic that this class is computing
     */
    public double getAverage()
    {
        return average;
    }

    /**
     * Return the standard deviation of the statistic that this class is computing
     */
    public double getSD()
    {
        return sd;
    }

    public double getStdError()
    {
        //double se = sd / Math.sqrt(currentIndex);
        double se = sd / Math.sqrt(statList.size());
        return se;
    }

    public double getCurrentStat()
    {
        //return statList[currentIndex - 1];
        return statList.get(statList.size() - 1);
    }

    public int getCurrentSuccess()
    {
        return ((Integer) sucList.get(sucList.size() - 1)).intValue();
    }

    public int getSampleSize()
    {
        return statList.size();
    }

    public double[] getStats()
    {
        /*
        double[] ret = new double[currentIndex];
        System.arraycopy(statList, 0, ret, 0, currentIndex);
        return ret;
        */
        return statList.toNativeArray();
    }

    public int[] getSuccessList()
    {
        int[] array = new int[sucList.size()];

        for (int i = 0; i < sucList.size(); i++)
        {
            array[i] = ((Integer) sucList.get(i)).intValue();
        }

        return array;
    }

    public void ensureCapacity(int size)
    {
        statList.ensureCapacity(statList.size()+size);
    }

    /**
     * This method adds the outcome of a trial to this class and update the statistics
     *
     * @param outcome the outcome of a trial
     * @param mu  double  This is only used for computing Z or t
     * @param sigma double This is only used for computing Z
     *
     */
    public void addData(Object outcome, double mu, double sigma)
    {
        if (outcome instanceof double[])
        {
            double[] data = (double[]) outcome;

            if (statType.equals(SUM))
            {
                statList.add(CommonComputation.computeSum(data));
            }
            else if (statType.equals(AVERAGE))
            {
                statList.add(CommonComputation.computeAverage(data));
            }
            else if (statType.equals(MIN))
            {
                //statList[currentIndex] = computeMin(data);
                statList.add(CommonComputation.computeMin(data));
            }
            else if (statType.equals(MAX))
            {
                //statList[currentIndex] = computeMax(data);
                statList.add(CommonComputation.computeMax(data));
            }
            else if (statType.equals(MEDIAN))
            {
                //statList[currentIndex] = computeMedian(data);
                statList.add(CommonComputation.computeMedian(data));
            }
            else if (statType.equals(IQR))
            {
                statList.add(CommonComputation.computeIQR(data));
            }
            else if (statType.equals(SAMPLE_SIZE))
            {
                statList.add(data.length);
            }
            else if (statType.equals(SD))
            {
                statList.add(CommonComputation.computeSD(data));
            }
            else if (statType.equals(THE_DRAW))
            {
                statList.add(data[0]);
            }
            else if (statType.equals(Z))
            {
                statList.add(computeZ(data, mu, sigma));
            }
            else if (statType.equals(t))
            {
                statList.add(computeT(data, mu));
            }
            else if (statType.equals(P_HAT))
            {
                statList.add(CommonComputation.computeAverage(data));
            }
            else if (statType.equals(D))
            {
                statList.add(computeD(data));
                //statList.add(data[0]);
            }
            else if (statType.equals(CHI_SQUARE))
            {
                statList.add(computeChi2(data));
            }
        }
        else if (outcome instanceof double[][])
        {
            double[][] data=(double[][])outcome;

            if(numberGenerator instanceof PairedCategoricalDataGenerator)
            {
                PairedCategoricalDataGenerator tmp=(PairedCategoricalDataGenerator)numberGenerator;
                int[][] array=new int[data.length][data[0].length];
                for(int i=0;i<data.length;i++)
                {
                    for(int j=0;j<data[i].length;j++)
                    {
                        array[i][j]=(int)data[i][j];
                    }
                }
                statList.add(CommonComputation.computeChiSquare(array, tmp.getXLevelCount(), tmp.getYLevelCount()));
            }
            else if(numberGenerator instanceof PairedDataGenerator)
            {
                statList.add(CommonComputation.computeR(data[0],data[1]));
            }
            else if (statType.equals(D))
            {
                double avg1=CommonComputation.computeAverage(data[0]);
                double avg2=CommonComputation.computeAverage(data[1]);
                statList.add(avg1-avg2);
            }

        }

        double tmpD = statList.get(statList.size() - 1);
        sum += tmpD;

        boolean success = false;

        switch (eventIndex)
        {
            case 1:

                if (tmpD <= a)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 2:
                if (tmpD >= a)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 3:
                if (tmpD == a)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 4:
                if (tmpD > a)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 5:
                if (tmpD < a)
                {
                    success = true;
                    successCnt++;
                }
                break;
            case 6:
                if (tmpD >= a && tmpD <= b)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 7:
                if (tmpD >= a && tmpD < b)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 8:
                if (tmpD > a && tmpD <= b)
                {
                    successCnt++;
                    success = true;
                }
                break;
            case 9:
                if (tmpD > a && tmpD < b)
                {
                    successCnt++;
                    success = true;
                }
                break;
        }

        //currentIndex++;
        if (success)
            sucList.add(new Integer(1));
        else
            sucList.add(new Integer(0));

        average = sum / statList.size();

        for (int i = 0; i < statList.size(); i++)
        {
            SS += Math.pow(statList.get(i) - average, 2);
        }

        if (statList.size() > 1)
            sd = Math.sqrt(SS / (statList.size() - 1));
        else
            sd = 0;

        if (min > statList.get(statList.size() - 1))
            min = statList.get(statList.size() - 1);
        if (max < statList.get(statList.size() - 1))
            max = statList.get(statList.size() - 1);

    }

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
    }


    public void computeStatistics()
    {
        statList.sort();
        double[] rawData = statList.toNativeArray();
        sd = CommonComputation.computeSD(rawData);
    }

    public double getQuantile(double value)
    {
        double point = value * statList.size();
        if (point > statList.size() - 1)
            return statList.get(statList.size() - 1);
        if (isWholeNumber(point))
            return statList.get((int) point);
        else
        {
            int left = (int) Math.floor(point);
            int right = (int) Math.ceil(point);
            return 0.5 * (statList.get(left) + statList.get(right));
        }
    }

    public double getIQR()
    {
        double lowerQuartile = getQuantile(0.25);
        double upperQuartile = getQuantile(0.75);
        return upperQuartile - lowerQuartile;
    }

    private boolean isWholeNumber(double value)
    {
        return value == (int) value;
    }



    public void setA(double a)
    {
        this.a = a;
    }

    public double getA()
    {
        return a;
    }


    public void setB(double b)
    {
        this.b = b;
    }

    public double getB()
    {
        return b;
    }

    public double computeProb(double[] data, int index)
    {
        int cnt = 0;

        for (int i = 0; i < data.length; i++)
        {
            if (index == 1 && data[i] <= a)
                cnt++;
            else if (index == 2 && data[i] >= a)
                cnt++;
            else if (index == 3 && data[i] == a)
                cnt++;
            else if (index == 4 && data[i] > a)
                cnt++;
            else if (index == 5 && data[i] < a)
                cnt++;
            else if (index == 6 && data[i] >= a && data[i] <= b)
                cnt++;
            else if (index == 7 && data[i] >= a && data[i] < b)
                cnt++;
            else if (index == 8 && data[i] > a && data[i] <= b)
                cnt++;
            else if (index == 9 && data[i] > a && data[i] < b)
                cnt++;
        }

        return cnt * 1.0 / data.length;

    }


    private double computeZ(double[] data, double mu, double sigma)
    {
        double avg = CommonComputation.computeAverage(data);
        return (avg - mu) / sigma;
    }

    private double computeT(double[] data, double mu)
    {
        double avg =CommonComputation.computeAverage(data);
        double sigma = CommonComputation.computeSD(data);

        return (avg - mu) / sigma * Math.sqrt(data.length);
    }


    public void insertionSort(double[] data)
    {
        double key;
        for (int i = 1; i < data.length; i++)
        {
            key = data[i];
            int j = i - 1;
            while (j >= 0 && data[j] > key)
            {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }

    public double computeD(double[] data)
    {
        double dSum = 0;
        Map map = getValueCountMap(data, numberGenerator.getAllOutcomes());
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext())
        {
            Double key = (Double) iterator.next();
            int observed = ((Integer) map.get(key)).intValue();
            double expected = numberGenerator.getNumberOfDraws() *
                    numberGenerator.getProbability(key.doubleValue());
            dSum += Math.abs(observed - expected);
        }
        return dSum;
    }

    public double computeChi2(double[] data)
    {
        double chSum = 0;
        Map map = getValueCountMap(data, numberGenerator.getAllOutcomes());
        Iterator iterator = map.keySet().iterator();

        while (iterator.hasNext())
        {
            Double key = (Double) iterator.next();
            int observed = ((Integer) map.get(key)).intValue();
            double expected = numberGenerator.getNumberOfDraws() *
                    numberGenerator.getProbability(key.doubleValue());
            chSum += Math.pow(observed - expected, 2) / expected;
        }
        return chSum;
    }

    public Map getValueCountMap(double[] data, Set allOutcomes)
    {
        Map map = new HashMap();
        Object[] array = allOutcomes.toArray();

        for (int i = 0; i < array.length; i++)
        {
            Object key = array[i];
            map.put(key, new Integer(0));
        }

        for (int i = 0; i < data.length; i++)
        {
            Double key = new Double(data[i]);
            if (map.containsKey(key))
            {
                int count = ((Integer) map.get(key)).intValue();
                count++;
                map.put(key, new Integer(count));
            }
        }

        return map;
    }

    public void reset()
    {
        sum = 0;
        SS = 0;
        successCnt = 0;
        sucList.clear();
        statList.clear();
        average = 0;
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
    }

}
