package wvustat.simulation.model;

import wvustat.statistics.CommonComputation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Oct 19, 2003
 * Time: 3:20:33 PM
 * To change this template use Options | File Templates.
 */
public class PairedCategoricalDataGenerator extends PairedDataGenerator
{
    private List xLevels;
    private List yLevels;
    private double chiSquare;

    public PairedCategoricalDataGenerator(List dataPairList, List xLevels, List yLevels)
    {
        super(dataPairList);
        this.xLevels = xLevels;
        this.yLevels = yLevels;
        int[][] data = new int[2][dataPairList.size()];
        for (int i = 0; i < dataPairList.size(); i++)
        {
            DataPair dataPair = (DataPair) dataPairList.get(i);
            data[0][i] = (int) dataPair.getX().doubleValue();
            data[1][i] = (int) dataPair.getY().doubleValue();
        }

        chiSquare = CommonComputation.computeChiSquare(data, xLevels.size(), yLevels.size());
    }

    public Object runTrial(int numOfRun, double goal)
    {
        List sample = new ArrayList();

        double[] cards = new double[dataPairList.size()];
        for (int i = 0; i < cards.length; i++)
        {
            DataPair pair = (DataPair) dataPairList.get(i);
            cards[i] = pair.getY().doubleValue();
        }
        BoxModel boxModel = new BoxModel();
        boxModel.setCards(cards);
        boxModel.setTrialType(IGenerator.DRAW_N_WO_REP);
        double[] permutatedY = (double[]) boxModel.runTrial(cards.length, 0);
        for (int i = 0; i < dataPairList.size(); i++)
        {
            DataPair pair = (DataPair) dataPairList.get(i);
            sample.add(new DataPair(pair.getX().doubleValue(),permutatedY[i]));
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

    public String getModelDescription()
    {
        String desc = "Paired categorical data: chi-square=" + NumberFormat.getInstance().format(chiSquare);
        return desc;
    }

    public int getXLevelCount()
    {
        return xLevels.size();
    }

    public int getYLevelCount()
    {
        return yLevels.size();
    }
}
