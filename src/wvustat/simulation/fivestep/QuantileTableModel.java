package wvustat.simulation.fivestep;

import wvustat.simulation.model.StatComputer;

import javax.swing.table.AbstractTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 22, 2003
 * Time: 10:34:09 AM
 * To change this template use Options | File Templates.
 */
public class QuantileTableModel extends AbstractTableModel
{
    private StatComputer statistics;
    private double[] quantiles={0.005, 0.01,0.025,0.05,0.1,0.25,0.5,0.75,0.9,0.95,0.975,0.99,0.995};

    public QuantileTableModel(StatComputer statistics)
    {
        this.statistics=statistics;
    }

    public int getRowCount()
    {
        return quantiles.length;
    }

    public int getColumnCount()
    {
        return 2;
    }

    public Object getValueAt(int row, int col)
    {
        switch(col)
        {
            case 0:
                return new Double(quantiles[row]);
            case 1:
                double q=Math.round(10000*statistics.getQuantile(quantiles[row]))/10000.0;
                return new Double(q);
        }
        return null;
    }

    public String getColumnName(int col)
    {
        switch(col)
        {
            case 0:
                return "Probability";
            case 1:
                return "Quantile";
        }
        return null;
    }

}
