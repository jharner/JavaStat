/*
 * FiveStepModel.java
 *
 * Created on March 11, 2002, 10:37 AM
 */

package wvustat.simulation.model;

import gnu.trove.TDoubleIntHashMap;

import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
import java.text.NumberFormat;

/**
 *
 * @author  hxue
 * @version
 */
public class BoxModel extends Object implements IGenerator
{


    private double[][] boxModel;
    private int trialType = -1;


    private double[] lastOutcome;

    private DrawBox box;
    private double[] cards;
    private int numOfDraws = -1;
    private double goal;

    /** Creates new FiveStepModel */
    public BoxModel()
    {
    }

    public boolean isDepletable()
    {
        return true;
    }

    public void setParam(Object obj)
    {
        double[][] array = (double[][]) obj;
        setBoxes(array);
    }

    public int getCapacity()
    {
        return getBoxCount();
    }

    public void setBoxes(double[][] array)
    {
        this.boxModel = array;
        int total = 0;
        for (int i = 0; i < boxModel[0].length; i++)
        {
            total += (int) boxModel[0][i];
        }

        cards = new double[total];
        int index = 0;
        for (int i = 0; i < boxModel[0].length; i++)
        {
            for (int j = 0; j < boxModel[0][i]; j++)
            {
                cards[index++] = boxModel[1][i];
            }
        }

        box = new DrawBox(cards);

    }

    public void setCards(double[] cards)
    {
        TDoubleIntHashMap countsMap = new TDoubleIntHashMap();

        for (int i = 0; i < cards.length; i++)
        {
            double card = cards[i];
            if (countsMap.containsKey(card))
            {
                int count = countsMap.get(card);
                count++;
                countsMap.put(card, count);
            }
            else
                countsMap.put(card, 1);
        }

        double[][] array = new double[2][countsMap.size()];
        double[] keys = new double[countsMap.size()];
        for (int i = 0; i < keys.length; i++)
        {
            keys[i] = countsMap.keys()[i];

        }
        Arrays.sort(keys);

        for (int i = 0; i < keys.length; i++)
        {

            array[0][i] = countsMap.get(keys[i]);
            array[1][i] = keys[i];
        }

        boxModel = array;
        this.cards = cards;
        box = new DrawBox(cards);

    }

    public double[][] getBoxes()
    {
        return boxModel;
    }

    public int getBoxCount()
    {
        return cards.length;
    }


    public boolean isGoalReachable(double box)
    {
        boolean ret = false;
        for (int i = 0; i < boxModel[1].length; i++)
        {
            if (boxModel[1][i] == box)
            {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public void setTrialType(int index)
    {
        trialType = index;
    }

    public int getTrialType()
    {
        return trialType;
    }

    public void setNumberOfDraws(int num)
    {
        this.numOfDraws = num;
    }

    public int getNumberOfDraws()
    {
        return numOfDraws;
    }

    public String getTrialDescription(int index)
    {
        return trial_names[index];
    }


    public double[] getCards()
    {
        return cards;
    }

    /**
     * @param numOfDraws The number of boxes to be drawn for each run, enter -1 if it is not fixed
     * @param X  The outcome that will cause the trial to stop
     *
     * @return the outcome of this trial
     */
    public Object runTrial(int numOfDraws, double X)
    {
        this.numOfDraws = numOfDraws;
        this.goal=X;
        double[] outcome = null;
        box = new DrawBox(cards);

        switch (trialType)
        {
            case DRAW_N_W_REP:
                outcome = new double[numOfDraws];
                for (int j = 0; j < numOfDraws; j++)
                {
                    outcome[j] = box.draw(true);
                }
                break;
            case DRAW_N_WO_REP:
                if (numOfDraws > box.size())
                {
                    throw new IllegalArgumentException("Can not draw a sample bigger than the population!");
                }
                outcome = new double[numOfDraws];
                for (int j = 0; j < numOfDraws; j++)
                {
                    outcome[j] = box.draw(false);
                }

                break;
            case DRAW_ONE:
                outcome = new double[1];
                outcome[0] = box.draw(false);

                break;
            case CEREAL_BOX:
                outcome = box.cerealBoxDraw();

                break;
            case DRAW_UNTIL_X:
                outcome = box.drawUntil(X);
                break;

            case BOOTSTRAP:
                outcome = new double[getCapacity()];
                for (int i = 0; i < outcome.length; i++)
                {
                    outcome[i] = box.draw(true);
                }
                break;
            case RANDOMIZE:
                outcome = new double[getCapacity()];
                int index = 0;
                for (int i = 0; i < boxModel[0].length; i++)
                {
                    for (int j = 0; j < boxModel[0][i]; j++)
                    {
                        double factor = Math.random() > 0.5 ? 1 : -1;
                        outcome[index++] = boxModel[1][i] * factor;
                    }
                }
                break;
        }
        lastOutcome = outcome;


        return outcome;
    }

    public double[] getRemainingBoxes()
    {
        return box.getRemaining();
    }

    public double drawOne(boolean withReplacement)
    {
        return box.draw(withReplacement);
    }

    public double getAverage()
    {
        return box.getAverage();
    }

    public double getSD()
    {
        return box.getSD();
    }

    public boolean isDiscrete()
    {
        return true;
    }

    public static void main(String[] args)
    {
        BoxModel model = new BoxModel();
        model.setBoxes(new double[][]{{1, 1, 1, 1, 1, 1}, {1, 2, 3, 4, 5, 6}});

        model.setTrialType(CEREAL_BOX);

        int numOfTrials = 10;
        for (int i = 0; i < numOfTrials; i++)
        {
            double[] outcome = (double[]) model.runTrial(-1, -1);
            for (int j = 0; j < outcome.length; j++)
            {
                System.out.print(outcome[j] + "  ");
            }
            System.out.println();
        }

    }

    public int getOutcomeCount()
    {
        return boxModel[0].length;
    }

    public Set getAllOutcomes()
    {
        Set treeSet = new TreeSet();
        for (int i = 0; i < boxModel[1].length; i++)
            treeSet.add(new Double(boxModel[1][i]));

        return treeSet;
    }

    public double getProbability(double val)
    {
        double p = 0;
        for (int i = 0; i < boxModel[1].length; i++)
        {
            if (val == boxModel[1][i])
            {
                p = boxModel[0][i] / cards.length;
                break;
            }
        }
        return p;
    }

    public int[] getCount()
    {
        int[] array = new int[boxModel[0].length];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = (int) boxModel[0][i];

        }
        return array;
    }

    public Object[] getAttributes(int index)
    {
        Number[] array = new Number[boxModel[1].length];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = new Double(boxModel[1][i]);
        }
        return array;
    }

    public int getObservationAttributeCount()
    {
        return 1;
    }

    public String[] getAttribueNames()
    {
        return new String[]{"Value"};
    }

    public String getModelDescription()
    {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        StringBuffer buf = new StringBuffer();
        buf.append("N=").append(getCapacity()).append(", ");
        buf.append("Mean=");
        buf.append(nf.format(getAverage()));
        buf.append(", SD=" + nf.format(getSD()));

        return buf.toString();
    }

    public double getGoal()
    {
        return goal;
    }

}