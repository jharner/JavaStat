package wvustat.statistics;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 31, 2003
 * Time: 4:35:03 PM
 * To change this template use Options | File Templates.
 */
public class CommonComputation
{
    public static double computeR(double[] x, double[] y)
    {
        double[] xy = new double[x.length];
        double[] x2 = new double[x.length];
        double[] y2 = new double[y.length];

        int n = x.length;
        for (int i = 0; i < x.length; i++)
        {
            xy[i] = x[i] * y[i];
            x2[i] = x[i] * x[i];
            y2[i] = y[i] * y[i];
        }

        double r = computeSum(xy) - computeSum(x) * computeSum(y) / n;
        double d = ((computeSum(x2) - Math.pow(computeSum(x), 2) / n) * (computeSum(y2) - Math.pow(computeSum(y), 2) / n));
        if (d == 0)
            r = 0;
        else
            r = r / Math.sqrt(d);
        return r;
    }

    public static double computeSum(double[] data)
    {
        double ret = 0;

        for (int i = 0; i < data.length; i++)
            ret += data[i];

        return ret;
    }

    public static double computeAverage(double[] data)
    {
        return computeSum(data) / data.length;
    }

    public static double computeMin(double[] data)
    {
        double ret = Double.MAX_VALUE;

        for (int i = 0; i < data.length; i++)
        {
            if (data[i] < ret)
                ret = data[i];
        }

        return ret;
    }

    public static double computeMax(double[] data)
    {
        double ret = Double.MIN_VALUE;

        for (int i = 0; i < data.length; i++)
        {
            if (data[i] > ret)
                ret = data[i];
        }

        return ret;
    }

    public static double computeMedian(double[] data)
    {
        Arrays.sort(data);

        int len = data.length;
        if (len % 2 == 0)
            return (data[len / 2 - 1] + data[len / 2]) / 2.0;
        else
            return data[len / 2];

    }

    /**
     * This method computes the inter quartile range of an array of double. The parameters lower quartile and
     * upper quartile are set during the course of computation.
     */
    public static double computeIQR(double[] data)
    {
        int len = data.length;
        double up, lp;
        int index;

        Arrays.sort(data);
        if (len == 1)
        {
            lp = 0;
            up = 0;
        }
        else if (len == 2)
        {
            lp = data[0];
            up = data[1];
        }
        else if (len == 3)
        {
            up = (data[2] + data[1]) / 2.0;
            lp = (data[1] + data[0]) / 2.0;
        }
        else if (len % 4 == 0 || len % 4 == 1)
        {
            index = len / 4;
            up = (data[len / 2 + len / 4] + data[len / 2 + len / 4 - 1]) / 2.0;
            lp = (data[index] + data[index - 1]) / 2.0;
        }
        else if (len % 4 == 2)
        {
            index = len / 4;
            lp = data[index];
            up = data[len / 2 + len / 4 + 1];
            return (up - lp);
        }
        else if (len % 4 == 3)
        {
            index = len / 4;
            lp = (data[index] + data[index + 1]) * 0.5;
            up = (data[len / 2 + len / 4] + data[len / 2 + len / 4 + 1]) * 0.5;
        }
        else
        {
            return Double.NaN;
        }

        return up - lp;
    }

    public static double computeSD(double[] data)
    {
        if (data.length == 1)
            return 0;

        double avg = computeAverage(data);
        double s2 = 0;
        for (int i = 0; i < data.length; i++)
        {
            s2 += Math.pow(data[i] - avg, 2);
        }

        double ret = Math.sqrt(s2 / (data.length - 1));
        return ret;
    }

    /**
     * Calculate chi square given an 2-d array representing the joint outcome of two
     * categorical variables
     *
     * @param data 0 index represents level of the first categorical variable 1 index represetns the second
     * @param xTotal total levels of the first categorical variable
     * @param yTotal total levels of the second categorical variable
     * @return
     */
    public static double computeChiSquare(int[][] data, int xTotal, int yTotal)
    {
        int[][] cTable=getContingencyTable(data,xTotal,yTotal);

        int N = data[0].length;
        int[] xMargins = new int[xTotal];
        int[] yMargins = new int[yTotal];
        for (int i = 0; i < xTotal; i++)
        {
            for (int j = 0; j < yTotal; j++)
            {
                xMargins[i] += cTable[i][j];
                yMargins[j] += cTable[i][j];
            }
        }

        double chiSquare = 0;
        for (int i = 0; i < xTotal; i++)
        {
            for (int j = 0; j < yTotal; j++)
            {
                double expected = 1.0 * xMargins[i] * yMargins[j] / N;
                chiSquare += Math.pow(cTable[i][j] - expected, 2) / expected;
            }
        }

        return chiSquare;
    }

    public static int[][] getContingencyTable(int[][] data, int xTotal, int yTotal)
    {
        int[][] cTable = new int[xTotal][yTotal]; //Contingency table


        for (int j = 0; j < data[0].length; j++)
        {
            int xIndex = data[0][j];
            int yIndex = data[1][j];
            cTable[xIndex][yIndex]++;
        }

        return cTable;
    }

}
