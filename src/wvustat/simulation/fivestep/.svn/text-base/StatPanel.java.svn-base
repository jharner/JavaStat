/*
 * StatPanel.java
 *
 * Created on March 19, 2002, 1:38 PM
 */

package wvustat.simulation.fivestep;

import wvustat.plot.Histogram;
import wvustat.simulation.model.StatComputer;
import wvustat.swing.table.JTableRowHeaderAdapter;
import wvustat.util.Bucket;
import wvustat.util.BucketTableModel;
import wvustat.util.ComponentUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This is the last panel in the five step method. It displays summary statistics and a histogram.
 *
 * @author  Hengyi Xue
 * @version
 */
public class StatPanel extends JPanel implements ActionListener
{
    private Histogram histogram;
    private StatComputer computeClass;
    private JPanel rptPanel;
    private double[] data;
    private int numOfSuccess = 0;

    /** Creates new StatPanel */
    public StatPanel(StatComputer computeClass)
    {
        this.computeClass = computeClass;
        initComponent();
    }


    public void setData(double[] data, int numOfSuccess)
    {
        if (data == null || data.length == 0)
            return;

        this.data = data;
        this.numOfSuccess = numOfSuccess;
        if (FiveStep.getStatComputer().getStatType() == StatComputer.P_HAT)
        {
            double mean = FiveStep.getGenerator().getAverage();
            int n = FiveStep.getGenerator().getNumberOfDraws();
            double ticWidth = 1;
            if (n < 125)
                ticWidth = 1.0 / n;
            else if (n < 500)
                ticWidth = 4.0 / n;
            else if (n < 2000)
                ticWidth = 8.0 / n;
            else
                ticWidth = 1.0 / (5 * Math.sqrt(n));
            histogram.setData(data, mean, ticWidth);
        }
        else if (FiveStep.getStatComputer().getStatType() == StatComputer.AVERAGE)
        {
            double mean = FiveStep.getGenerator().getAverage();
            double sd = FiveStep.getGenerator().getSD() / Math.sqrt(FiveStep.getGenerator().getNumberOfDraws());
            histogram.setData(data, mean, sd / 4);
        }
        else if (FiveStep.getGenerator().isDiscrete())
            histogram.setData(data);
        else if (FiveStep.getStatComputer().getStatType() == StatComputer.SUM)
        {
            double mean = FiveStep.getGenerator().getAverage() * FiveStep.getGenerator().getNumberOfDraws();
            double sd = FiveStep.getGenerator().getSD() * Math.sqrt(FiveStep.getGenerator().getNumberOfDraws());
            histogram.setData(data, mean, sd / 2);
        }
        else
            histogram.setData(data);
        makeReport();
        revalidate();
    }

    private void initComponent()
    {
        //txtRpt=new JTextArea(8,9);
        //txtRpt.setEditable(false);
        histogram = new Histogram();
        histogram.setBorder(new javax.swing.border.EtchedBorder());
        makeReport();

        this.setLayout(new GridBagLayout());

        JLabel title = new JLabel("5. Examine the statistics for the simulated values of the Statistic of Interest");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.black);

        this.add(title, new GridBagConstraints(0, 0, 0, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        this.add(rptPanel, new GridBagConstraints(0, 1, 0, 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(histogram, new GridBagConstraints(0, 2, 0, -1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));

        JButton showTableBtn = new JButton("Show Distribution");
        showTableBtn.setActionCommand("SHOW_TABLE");
        showTableBtn.addActionListener(this);

        JButton showQuantilesBttn = new JButton("Show Quantiles");
        showQuantilesBttn.setActionCommand("SHOW_QUANTILES");
        showQuantilesBttn.addActionListener(this);

        JButton editAxisBttn = new JButton("Rescale Axis");
        editAxisBttn.setActionCommand("RESCALE");
        editAxisBttn.addActionListener(this);

        this.add(showTableBtn, new GridBagConstraints(0, 3, 1, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(showQuantilesBttn, new GridBagConstraints(1, 3, -1, 0, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(editAxisBttn, new GridBagConstraints(2, 3, 0, 0, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.setBorder(new javax.swing.border.EtchedBorder());
    }

    private void makeReport()
    {
        if (rptPanel == null)
            rptPanel = new JPanel();
        else
            rptPanel.removeAll();

        if (data == null)
            return;

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);

        Font bold = new Font("Dialog", Font.BOLD, 12);

        JPanel jp1 = new JPanel(new GridBagLayout());
        JLabel jl1 = new JLabel("Statistic of Interest");
        jl1.setFont(bold);
        JLabel jl11 = new JLabel("Summary");
        jl11.setFont(bold);

        jp1.add(jl1, new GridBagConstraints(0, 0, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(jl11, new GridBagConstraints(1, 0, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new JLabel("# of Simulations"), new GridBagConstraints(0, 1, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(String.valueOf(data.length)), new GridBagConstraints(1, 1, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));


        jp1.add(new JLabel("Mean"), new GridBagConstraints(0, 2, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(nf.format(computeClass.getAverage())), new GridBagConstraints(1, 2, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new JLabel("Standard Deviation"), new GridBagConstraints(0, 3, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(nf.format(computeClass.getSD())), new GridBagConstraints(1, 3, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new JLabel("Median"), new GridBagConstraints(0, 4, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(nf.format(computeClass.getQuantile(0.5))), new GridBagConstraints(1, 4, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new JLabel("Interquartile Range"), new GridBagConstraints(0, 5, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(nf.format(computeClass.getIQR())), new GridBagConstraints(1, 5, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new JLabel("Min"), new GridBagConstraints(0, 6, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(nf.format(computeClass.getMin())), new GridBagConstraints(1, 6, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new JLabel("Max"), new GridBagConstraints(0, 7, -1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp1.add(new BlueLabel(nf.format(computeClass.getMax())), new GridBagConstraints(1, 7, 0, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        JPanel jp2 = new JPanel(new GridBagLayout());
        JLabel jl2 = new JLabel("Event of Interest");
        jl2.setFont(bold);

        JLabel jl3 = new JLabel("Count");
        jl3.setFont(bold);

        JLabel jl4 = new JLabel("Experimental Probability");
        jl4.setFont(bold);

        jp2.add(jl2, new GridBagConstraints(0, 0, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp2.add(jl3, new GridBagConstraints(1, 0, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp2.add(jl4, new GridBagConstraints(2, 0, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp2.add(new JLabel("Success"), new GridBagConstraints(0, 1, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        nf.setMaximumFractionDigits(4);

        int eventIndex = computeClass.getEventIndex();

        if (eventIndex >= 1)
        {
            jp2.add(new BlueLabel(String.valueOf(numOfSuccess)), new GridBagConstraints(1, 1, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
            jp2.add(new BlueLabel(nf.format(numOfSuccess * 1.0 / data.length)), new GridBagConstraints(2, 1, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }
        else
        {
            jp2.add(new BlueLabel("N/A"), new GridBagConstraints(1, 1, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
            jp2.add(new BlueLabel("N/A"), new GridBagConstraints(2, 1, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }

        jp2.add(new JLabel("Failure"), new GridBagConstraints(0, 2, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        if (eventIndex >= 1)
        {
            int fail = data.length - numOfSuccess;
            jp2.add(new BlueLabel(String.valueOf(fail)), new GridBagConstraints(1, 2, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
            jp2.add(new BlueLabel(nf.format(fail * 1.0 / data.length)), new GridBagConstraints(2, 2, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }
        else
        {
            jp2.add(new JLabel("N/A"), new GridBagConstraints(1, 2, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
            jp2.add(new JLabel("N/A"), new GridBagConstraints(2, 2, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }

        jp2.add(new JLabel("Total"), new GridBagConstraints(0, 3, 1, 1, 0.4, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        if (eventIndex >= 1)
        {
            jp2.add(new BlueLabel(String.valueOf(data.length)), new GridBagConstraints(1, 3, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
            jp2.add(new BlueLabel("1.00"), new GridBagConstraints(2, 3, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }
        else
        {
            jp2.add(new JLabel("N/A"), new GridBagConstraints(1, 3, -1, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
            jp2.add(new JLabel("N/A"), new GridBagConstraints(2, 3, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        }
        jp2.add(new JLabel(" "), new GridBagConstraints(0, 4, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp2.add(new JLabel(" "), new GridBagConstraints(0, 5, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp2.add(new JLabel(" "), new GridBagConstraints(0, 6, 0, 1, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        jp2.add(new JLabel(" "), new GridBagConstraints(0, 7, 0, 0, 0.3, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

        jp1.setBorder(new javax.swing.border.EtchedBorder());
        jp2.setBorder(new javax.swing.border.EtchedBorder());

        rptPanel.setLayout(new GridLayout(1, 2, 2, 8));
        rptPanel.add(jp1);
        rptPanel.add(jp2);
        if (eventIndex < 1)
            jp2.setVisible(false);

    }

    public void reset()
    {
        rptPanel.removeAll();
        histogram.clear();
        histogram.getAxisModel().setManual(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("SHOW_TABLE"))
        {
            Bucket[] buckets = histogram.getBuckets();
            JTable table = new JTable(new BucketTableModel(buckets));
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setDefaultRenderer(Double.class, new DoubleRenderer());
            JTableRowHeaderAdapter adapter = new JTableRowHeaderAdapter(table);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            adapter.setPreferredSize(new Dimension(400, 300));
            JFrame tableFrame = new JFrame("Probability Distribution");
            tableFrame.getContentPane().add(adapter, BorderLayout.CENTER);
            tableFrame.pack();
            ComponentUtil.setLocationRelativeTo(tableFrame, this);
            tableFrame.show();
        }
        else if (e.getActionCommand().equals("RESCALE"))
        {
            histogram.configureAxis();
        }
        else if (e.getActionCommand().equals("SHOW_QUANTILES"))
        {
            QuantileTableModel tableModel = new QuantileTableModel(computeClass);
            JTable table = new JTable(tableModel);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JTableRowHeaderAdapter adapter = new JTableRowHeaderAdapter(table);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            table.setDefaultRenderer(Double.class, new DoubleRenderer());
            adapter.setPreferredSize(new Dimension(230, 280));
            JFrame tableFrame = new JFrame("Quantiles");
            tableFrame.getContentPane().add(adapter, BorderLayout.CENTER);
            //tableFrame.pack();
            tableFrame.setSize(adapter.getPreferredSize());
            ComponentUtil.setLocationRelativeTo(tableFrame, this);
            tableFrame.show();
        }

    }

    class BlueLabel extends JLabel
    {
        public BlueLabel(String text)
        {
            super(text);
            setForeground(Color.blue);
        }

        public BlueLabel(String text, int orientation)
        {
            super(text, orientation);
            setForeground(Color.blue);
        }
    }

    class DoubleRenderer extends DefaultTableCellRenderer
    {
        NumberFormat formatter = new DecimalFormat("#.####");

        public DoubleRenderer()
        {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1)
        {
            JLabel label = (JLabel) super.getTableCellRendererComponent(jTable, o, b, b1, i, i1);
            label.setText((o == null) ? "" : formatter.format(o));
            return label;
        }

    }

    public Histogram getHistogram()
    {
        return histogram;
    }

}