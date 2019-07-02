/*

*

* NewDistribution.java

*

* author: Ximing Zhao

*/



package wvustat.simulation.chisquare;

import java.util.Random;

import java.util.Vector;



public class NewDistribution

    implements Cloneable

{	

    protected String name;

    protected double min;

    protected double max;

    protected double ave;

    protected double sd;

    protected double practicalMin;

    protected double practicalMax;

    protected int nDistinct;

    double minGap;

    int currentDrawIndex;

    protected Random random;

    protected int nDraws;

    protected double sumDraws;

    protected double ssDraws;

    VariableType theDraws;

    FiniteDistribution drawsHist;

    double currentDraw;

    boolean degenerate;

    double degenerateValue;

    int nBinsDraws;



    NewDistribution()

    {

        random = new Random();

        degenerate = false;

        nBinsDraws = 10;

    }



    public void collectDraws(boolean flag)

    {

        if(flag)

        {

            theDraws = new VariableType();

            theDraws.ensureCapacity(100);

            return;

        } else

        {

            theDraws = null;

            return;

        }

    }



    public void collectDrawsHist(boolean flag)

    {

        if(flag)

            drawsHist = new FiniteDistribution();

        setupDrawsHist();

    }



    public FiniteDistribution drawsHist()

    {

        return drawsHist;

    }



    public void setupDrawsHist()

    {

    }



    public Object clone()

    {

        return new NewDistribution();

    }



    public void setName(String s)

    {

        name = s;

    }



    public double min()

    {

        return min;

    }



    public double max()

    {

        return max;

    }



    public double practicalMin()

    {

        return practicalMin;

    }



    public double practicalMax()

    {

        return practicalMax;

    }



    public void setPracticalMin(double d)

    {

        practicalMin = d;

    }



    public void setPracticalMax(double d)

    {

        practicalMax = d;

    }



    public double average()

    {

        return ave;

    }



    public double sd()

    {

        return sd;

    }



    public double variance()

    {

        return sd * sd;

    }



    public String name()

    {

        return name;

    }



    public String fullName()

    {

        return name;

    }



    public String title()

    {

        return name;

    }



    public int nDistinct()

    {

        return nDistinct;

    }



    public void setNDistinct(int i)

    {

        nDistinct = Math.max(0, i);

    }



    public int currentDrawIndex()

    {

        return currentDrawIndex;

    }



    public double currentDraw()

    {

        return currentDraw;

    }



    public double minGap()

    {

        return minGap;

    }



    public double area()

    {

        return 1.0D;

    }



    public double sum()

    {

        return area();

    }



    public int nDraws()

    {

        return nDraws;

    }



    public double sumDraws()

    {

        return sumDraws;

    }



    public double aveDraws()

    {

        return sumDraws / (double)nDraws;

    }



    public double sdDraws()

    {

        if(nDraws <= 1)

            return 0.0D;

        else

            return Math.sqrt((ssDraws - (sumDraws * sumDraws) / (double)nDraws) / (double)nDraws);

    }



    public VariableType draws()

    {

        return theDraws;

    }



    public void setStats()

    {

    }





    public double randValue()

    {

        return 0.0D;

    }



    public void rand()

    {

        currentDraw = randValue();

        nDraws++;

        sumDraws += currentDraw;

        ssDraws += currentDraw * currentDraw;

        if(theDraws != null)

            theDraws.addElement(new Double(currentDraw));

        if(drawsHist != null)

            updateDrawsHist();

    }



    public void updateDrawsHist()

    {

    }



    public void randomSample(int i)

    {

        reset();

        for(int j = 0; j < i; j++)

            rand();



    }



    public void addlRandomSample(int i)

    {

        for(int j = 0; j < i; j++)

            rand();



    }



    public void reset()

    {

        nDraws = 0;

        sumDraws = 0.0D;

        ssDraws = 0.0D;

        if(theDraws != null)

            collectDraws(true);

        if(drawsHist != null)

            collectDrawsHist(true);

    }



    



    public double density(double d)

    {

        return 0.0D;

    }



    public double densityI(int i)

    {

        return 0.0D;

    }



    public double value(int i)

    {

        return 0.0D;

    }



    public double maxDensity()

    {

        return 0.0D;

    }



    

}