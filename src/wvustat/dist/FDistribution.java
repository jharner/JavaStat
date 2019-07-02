package wvustat.dist;


import wvustat.interfaces.*;

/**
 * FDistribution is an implementation of the F distribution. The cdf is calculated by using expansion.
 *
 * @author: hengyi Xue
 * @version: 1.0, Mar. 1, 2000
 */

public class FDistribution implements Distribution
{

    /**
     * degrees of freedom
     */
    public int v1,v2; //degrees of freedom

    /**
     * Constructor
     * create a new F distribution for given degrees of freedom
     */
    public FDistribution(int v1, int v2)
    {
        this.v1 = v1;
        this.v2 = v2;
    }

    /**
     * Get probability density for given x
     */
    public double pdf(double x)
    {
        return -1;
    }

    /**
     * Get quantiles for probability x
     */
    public double quantile(double x)
    {
        return -1;
    }


    /**
     * Get cumulative probability for given x
     */
    public double cdf(double f)
    {
        double x = v2 * 1.0 / (v2 + v1 * f);
        double result = 0.0;

        if(v2==0||v1==0||Double.isNaN(f))
            return Double.NaN;
        else if (v1 % 2 == 0)
            result = 1 - QF1(x, v1);
        else if (v2 % 2 == 0)
            result = QF2(x, v2);
        else
            result = 1 - QF3(f);

        return result;
    }

    /**
     *   The following is the expansion when v1 is even, used for computing cdf
     */
    private double QF1(double x, int v)
    {
        if (v == 2)
            return Math.pow(x, v2 / 2.0);
        else
        {
            int part1 = v2,part2 = 1;
            double part3;

            for (int i = 2; i <= v - 4; i += 2)
            {
                part1 = part1 * (v2 + i);
            }

            for (int i = 2; i <= v - 2; i += 2)
            {
                part2 = part2 * i;
            }

            part3 = Math.pow(1 - x, (v - 2) / 2.0);

            double result = Math.pow(x, v2 / 2.0) * part1 / part2 * part3;

            return (result + QF1(x, v - 2));
        }
    }

    /** The following is the expansion when v2 is even, used of cdf
     */
    private double QF2(double x, int v)
    {
        if (v == 2)
            return Math.pow(1 - x, v1 / 2.0);
        else
        {
            int part1 = v1,part2 = 1;
            double part3;

            for (int i = 2; i <= v - 4; i += 2)
            {
                part1 = part1 * (v1 + i);
            }

            for (int i = 2; i <= v - 2; i += 2)
            {
                part2 = part2 * i;
            }

            part3 = Math.pow(x, (v - 2) / 2.0);

            double result = Math.pow(1 - x, v1 / 2.0) * part1 / part2 * part3;

            return (result + QF2(x, v - 2));
        }
    }

    private double QF3(double f)
    {
        double theta = Math.atan(Math.sqrt(v1 * f / v2));

        double result = 1 - A(theta, v2) + beta(theta, v1, v2);

        return result;

    }

    private double A(double theta, int v)
    {
        if (v == 1)
            return (2 * theta / Math.PI);
        else
        {
            double sum1 = 0.0;

            for (int i = 1; i <= v - 2; i += 2)
            {
                double part1 = 1.0,part2 = 1.0,part3;

                for (int j = 2; j < i + 1; j += 2)
                    part1 = part1 * j;

                for (int j = 3; j <= i; j += 2)
                    part2 = part2 * j;

                part3 = Math.pow(Math.cos(theta), i);

                sum1 = sum1 + part1 * part3 / part2;
            }

            double result = 2.0 / Math.PI * (theta + Math.sin(theta) * sum1);
            return result;
        }
    }

    private double beta(double theta, int v1, int v2)
    {
        double ret;

        if (v1 == 1)
            ret = 0.0;
        else
        {
            double sum = 1.0;

            for (int i = 2; i <= (v1 - 3); i += 2)
            {
                double part1 = 1.0,part2 = 1.0,part3;
                for (int j = 1; j <= i - 1; j += 2)
                    part1 = part1 * (v2 + j);

                for (int j = 1; j <= i + 1; j += 2)
                    part2 = part2 * j;

                part3 = Math.pow(Math.sin(theta), i);

                sum = sum + part1 / part2 * part3;
            }

            ret = sum * Math.pow(Math.cos(theta), v2);
            ret = ret * Math.sin(theta);
            ret = ret * factorial((v2 - 1) / 2) / factorial((v2 - 2) / 2.0);
            ret = ret * 2 / Math.sqrt(Math.PI);
        }

        return ret;
    }

    private int factorial(int n)
    {
        if (n == 0)
            return 1;
        else
            return (n * factorial(n - 1));
    }

    private double factorial(double x)
    {
        double ret = 0.0;

        if (x > 0.4999 && x < 0.5001)
            ret = 0.5 * Math.sqrt(Math.PI);
        else
            ret = x * factorial(x - 1);

        return ret;
    }
}


