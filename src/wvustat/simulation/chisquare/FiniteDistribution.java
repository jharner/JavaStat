/*

*

* FiniteDistribution.java

* author: Ximing Zhao

*/



package wvustat.simulation.chisquare;

import java.util.Random;

import java.util.Vector;



public class FiniteDistribution extends NewDistribution

{

    protected VariableType density;

    protected VariableType value;

    boolean scrunch;

    double maxDensity;

    

    FiniteDistribution()

    {

        density = new VariableType();

        value = new VariableType();

        scrunch= true;

    }



    public void setValues(VariableType variable, VariableType variable1)

    {

        density = variable1;

        for(int i = 0; i < density.size(); i++)

            if(density.x(i) < 0.0D)

                density.x(0.0D, i);



        value = variable;

        setStats();

    }



    public void setScrunch(boolean flag)

    {

        scrunch= flag;

    }



    public VariableType getValue()

    {

        return value;

    }



    public VariableType getDensity()

    {

        return density;

    }



    public double densityI(int i)

    {

        if(i < 0 || i > super.nDistinct)

            return 0.0D;

        else

            return density.x(i);

    }



    public double value(int i)

    {

        if(i < 0 || i > super.nDistinct)

            return 0.0D;

        else

            return value.x(i);

    }



    public double density(double d)

    {

        int i = 0;

        double d1 = Math.abs(d - value.x(0));

        for(int j = 1; j < super.nDistinct; j++)

        {

            double d2 = Math.abs(d - value.x(j));

            if(d2 < d1)

            {

                d1 = d2;

                i = j;

            }

        }



        if(d1 < super.minGap / 1000D)

            return density.x(i);

        else

            return 0.0D;

    }



    public double area()

    {

        if(super.degenerate)

            return density.sum();

        else

            return super.minGap * density.sum();

    }



    public double sum()

    {

        return density.sum();

    }



    public void addValue(double d, double d1)

    {

        value.addElement(new Double(d));

        density.addElement(new Double(d1));

        setStats();

    }



    public void cumValueI(int i, double d)

    {

        if(i < 0 || i > super.nDistinct)

        {

            return;

        } else

        {

            density.x(density.x(i) + Math.max(d, 0.0D), i);

            return;

        }

    }



    public void setStats()

    {

        scrunch();

        super.min = value.min();

        super.max = value.max();

        super.practicalMin = super.min;

        super.practicalMax = super.max;

        super.ave = 0.0D;

        super.sd = 0.0D;

        double d = density.sum();

        for(int i = 0; i < super.nDistinct; i++)

        {

            super.ave += value.x(i) * density.x(i);

            super.sd += value.x(i) * value.x(i) * density.x(i);

        }



        super.ave /= d;

        super.sd /= d;

        super.sd -= super.ave * super.ave;

        super.sd = Math.sqrt(Math.max(0.0D, super.sd));

        if(super.nDistinct == 1)

        {

            super.degenerate = true;

            super.degenerateValue = value.x(0);

            super.minGap = 0.0D;

        } else

        {

            super.degenerate = false;

            VariableType variable = (VariableType)value.clone();

            variable.sort();

            super.minGap = variable.x(1) - variable.x(0);

            for(int j = 2; j < super.nDistinct; j++)

                super.minGap = Math.min(super.minGap, variable.x(j) - variable.x(j - 1));



        }

        maxDensity = density.max();

    }



    public double maxDensity()

    {

        return maxDensity;

    }



    public void setupDrawsHist()

    {

        super.drawsHist.setScrunch(false);

        super.drawsHist.setValues(value, new VariableType(value.size()));

        super.drawsHist.minGap = super.minGap;

    }



    public void updateDrawsHist()

    {

        super.drawsHist.cumValueI(super.currentDrawIndex, 1.0D);

    }



    public void scrunch()

    {

        int i = Math.min(value.size(), density.size());

        super.nDistinct = i;

        if(scrunch)

        {

            for(int j = 0; j < i - 1; j++)

            {

                for(int k = j + 1; k < i; k++)

                    if(value.x(j) == value.x(k))

                    {

                        density.x(density.x(j) + density.x(k), j);

                        density.x(0.0D, k);

                    }



            }



            for(int l = i - 1; l >= 0; l--)

                if(density.x(l) == 0.0D)

                {

                    density.removeElementAt(l);

                    value.removeElementAt(l);

                    super.nDistinct--;

                }



        }

        chop(value);

        chop(density);

    }



    public void chop(VariableType variable)

    {

        for(int i = variable.size() - 1; i >= super.nDistinct; i++)

            variable.removeElementAt(i);



    }



    public Object clone()

    {

        FiniteDistribution finitedistribution = new FiniteDistribution();

        finitedistribution.setValues(value, density);

        finitedistribution.setName(name());

        return finitedistribution;

    }



 

    public double randValue()

    {

        double d = density.sum();

        if(d <= 0.0D)

            return 0.0D;

        double d1 = super.random.nextDouble() * d;

        double d2 = density.x(0);

        super.currentDrawIndex = 0;

        for(; d1 >= d2; d2 += density.x(super.currentDrawIndex))

            super.currentDrawIndex++;



        if(super.currentDrawIndex != -1)

            return value.x(super.currentDrawIndex);

        else

            return 0.0D;

    }









    public boolean contains(double d)

    {

        return value.contains(new Double(d));

    }







    public static void main(String args[])

    {

        FiniteDistribution finitedistribution = new FiniteDistribution();

        finitedistribution.addValue(1.0D, 0.25D);

        finitedistribution.addValue(2D, 0.5D);

        finitedistribution.addValue(2.2999999999999998D, 0.25D);

        finitedistribution.addValue(1.0D, 0.050000000000000003D);

        finitedistribution.collectDraws(true);

        

       

    }



    

}