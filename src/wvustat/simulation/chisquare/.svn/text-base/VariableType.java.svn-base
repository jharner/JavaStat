/*

*

* VariableType.java

*

* author: Ximing Zhao

*/



package wvustat.simulation.chisquare;

import java.util.BitSet;

import java.util.Vector;



public class VariableType extends Vector

{

    String name;

    BitSet mask;

    

    VariableType()

    {

        name = "";

    }



    VariableType(int i)

    {

        super(i);

        name = "";

        set_blank();

    }



    VariableType(int i, int j)

    {

        super(i, j);

        name = "";

        set_blank();

    }



    VariableType(Double double1)

    {

        super(1);

        name = "";

        addElement(double1);

    }



    VariableType(double d)

    {

        this(new Double(d));

    }



    VariableType(String s1)

    {

        this(new Double(s1));

    }



    public Object clone()

    {

        VariableType variable = (VariableType)super.clone();

        variable.name = name;

        return variable;

    }



    void set_blank()

    {

        for(int i = 0; i < capacity(); i++)

            addElement(new Double(0.0D));



    }



    void expand(int i)

    {

        int j = size();

        for(int k = j; k < j + i; k++)

            addElement(new Double(0.0D));



    }



    void expandTo(int i)

    {

        for(int j = size(); j < i; j++)

            addElement(new Double(0.0D));



    }



    double x(int i)

    {

        return ((Double)elementAt(i)).doubleValue();

    }



    Double X(int i)

    {

        return (Double)elementAt(i);

    }



    double x(double d, int i)

    {

        setElementAt(new Double(d), i);

        return d;

    }



    Double x(Double double1, int i)

    {

        setElementAt(double1, i);

        return double1;

    }



    double doubleValue()

    {

        return ((Double)elementAt(0)).doubleValue();

    }



    String x(String s1, int i)

    {

        try

        {

            Double double1 = new Double(s1.trim());

            setElementAt(double1, i);

        }

        catch(NumberFormatException _ex)

        {

            setElementAt(new Double((0.0D / 0.0D)), i);

        }

        return s1;

    }



    String s(int i)

    {

        return elementAt(i).toString();

    }



    void set_mask(BitSet bitset)

    {

        mask = bitset;

    }



    String name()

    {

        return name;

    }



    int n_ok()

    {

        if(mask == null)

            return size();

        int i = 0;

        for(int j = 0; j < mask.size(); j++)

            if(mask.get(j))

                i++;



        return i;

    }



    int n_not()

    {

        if(mask == null)

            return 0;

        else

            return size() - n_ok();

    }



    boolean ok(int i)

    {

        if(mask == null)

            return true;

        else

            return mask.get(i);

    }



    double sum()

    {

        int i = size();

        double d = 0.0D;

        for(int j = 0; j < i; j++)

            if(ok(j))

                d += x(j);



        return d;

    }



    double average()

    {

        int i = n_ok();

        if(i == 0)

            return 0.0D;

        else

            return sum() / (double)i;

    }



    double variance()

    {

        double d = average();

        double d1 = 0.0D;

        int i = size();

        int j = n_ok();

        if(j == 0)

            return 0.0D;

        for(int k = 0; k < i; k++)

            if(ok(k))

            {

                double d2 = x(k) - d;

                d1 += d2 * d2;

            }



        return d1 / (double)j;

    }



    double sd()

    {

        return Math.sqrt(variance());

    }



    double max()

    {

        double d = 0.0D;

        boolean flag = false;

        int i = size();

        int j = n_ok();

        if(j == 0)

            return 0.0D;

        for(int k = 0; k < i; k++)

            if(ok(k))

                if(!flag)

                {

                    d = x(k);

                    flag = true;

                } else

                if(x(k) > d)

                    d = x(k);



        return d;

    }



    double min()

    {

        double d = 0.0D;

        boolean flag = false;

        int i = size();

        int j = n_ok();

        if(j == 0)

            return 0.0D;

        for(int k = 0; k < i; k++)

            if(ok(k))

                if(!flag)

                {

                    d = x(k);

                    flag = true;

                } else

                if(x(k) < d)

                    d = x(k);



        return d;

    }



    public double median()

    {

        VariableType variable = strip();

        if(variable.size() == 0)

        {

            return 0.0D;

        } else

        {

            variable.sort();

            return variable.halfway(0, variable.size() - 1);

        }

    }



    public VariableType fivenum()

    {

        VariableType variable = new VariableType(5);

        VariableType variable1 = strip();

        int i = variable1.size();

        if(i == 0)

        {

            return variable;

        } else

        {

            int j = (i - 1) / 2;

            variable1.sort();

            variable.x(variable1.x(0), 0);

            variable.x(variable1.x(i - 1), 4);

            variable.x(variable1.halfway(0, i - 1), 2);

            variable.x(variable1.halfway(0, j), 1);

            variable.x(variable1.halfway(i - 1 - j, i - 1), 3);

            return variable;

        }

    }



    public double interquartilerange()

    {

        VariableType variable = fivenum();

        return variable.x(3) - variable.x(1);

    }



    public double halfway(int i, int j)

    {

        if(size() == 0)

            return 0.0D;

        if(i > j)

        {

            int k = i;

            i = j;

            j = k;

        }

        if(i < 0)

            i = 0;

        if(j > size() - 1)

            j = size() - 1;

        int l = (i + j) / 2;

        int i1 = (i + j + 1) / 2;

        return (x(l) + x(i1)) / 2D;

    }



    



    public void reverse_mask()

    {

        if(mask == null)

        {

            mask = new BitSet(size());

            return;

        }

        for(int i = 0; i < size(); i++)

            if(mask.get(i))

                mask.clear(i);

            else

                mask.set(i);



    }



    public BitSet s_equals(String s1)

    {

        BitSet bitset = new BitSet(size());

        for(int i = 0; i < size(); i++)

            if(s1.equals(s(i)))

                bitset.set(i);



        return bitset;

    }



    public BitSet x_equals(double d)

    {

        BitSet bitset = new BitSet(size());

        for(int i = 0; i < size(); i++)

            if(x(i) == d)

                bitset.set(i);



        return bitset;

    }



    public BitSet lt(double d)

    {

        BitSet bitset = new BitSet(size());

        for(int i = 0; i < size(); i++)

            if(x(i) < d)

                bitset.set(i);



        return bitset;

    }



    public BitSet gt(double d)

    {

        BitSet bitset = new BitSet(size());

        for(int i = 0; i < size(); i++)

            if(x(i) > d)

                bitset.set(i);



        return bitset;

    }



    public static BitSet reverse(BitSet bitset, int i)

    {

        BitSet bitset1 = new BitSet(i);

        for(int j = 0; j < i; j++)

            if(!bitset.get(j))

                bitset1.set(j);



        return bitset1;

    }



    void swap(int i, int j)

    {

        Double double1 = X(i);

        x(X(j), i);

        x(double1, j);

    }



    void ramp(int i, int j)

    {

        int k = i;

        for(int l = 0; l < size(); l++)

        {

            x(k, l);

            k += j;

        }



    }



    void ramp(int i)

    {

        ramp(i, 1);

    }



    void ramp()

    {

        ramp(0, 1);

    }



    public void quickSort(int i, int j, VariableType variable)

    {

        int k = i;

        int l = j;

        if(j > i)

        {

            double d = x((i + j) / 2);

            while(k <= l) 

            {

                while(k < j && x(k) < d) 

                    k++;

                for(; l > i && x(l) > d; l--);

                if(k <= l)

                {

                    swap(k, l);

                    variable.swap(k, l);

                    k++;

                    l--;

                }

            }

            if(i < l)

                quickSort(i, l, variable);

            if(k < j)

                quickSort(k, j, variable);

        }

    }



    public VariableType sort()

    {

        VariableType variable = new VariableType(size());

        if(size() == 0)

        {

            return variable;

        } else

        {

            variable.ramp();

            quickSort(0, size() - 1, variable);

            return variable;

        }

    }



    VariableType strip()

    {

        int i = n_ok();

        if(i == 0)

            return new VariableType();

        if(i == size())

            return (VariableType)clone();

        VariableType variable = new VariableType(i);

        int j = 0;

        for(int k = 0; k < size(); k++)

            if(ok(k))

            {

                variable.x(x(k), j);

                j++;

            }



        return variable;

    }



    VariableType subVariable(BitSet bitset, boolean flag, String s1)

    {

        int i = 0;

        int j = size();

        for(int k = 0; k < j; k++)

            if(bitset.get(k) == flag)

                i++;



        VariableType variable = new VariableType(i);

        if(mask != null)

            variable.set_mask(new BitSet());

        int i1 = -1;

        for(int l = 0; l < j; l++)

            if(bitset.get(l) == flag)

            {

                i1++;

                variable.x(X(l), i1);

                if(mask != null && ok(l))

                    variable.mask.set(i1);

            }



        variable.name = s1;

        return variable;

    }



    VariableType subVariable(BitSet bitset)

    {

        return subVariable(bitset, true, "");

    }



    VariableType subVariable(BitSet bitset, boolean flag)

    {

        return subVariable(bitset, flag, "");

    }



    VariableType subVariable(BitSet bitset, String s1)

    {

        return subVariable(bitset, true, s1);

    }



   

    public static void main(String args[])

    {

        VariableType variable = new VariableType(5);

        for(int i = 0; i < 5; i++)

            variable.x(i, i);



        

    }



   

}