/*
 * FreqModule.java
 *
 * Created on August 9, 2000, 10:49 AM
 */

package wvustat.modules;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.util.MathUtils;
import wvustat.dist.Chi2Dist;
import wvustat.dist.Normal;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;
import java.text.NumberFormat;

/**
 * FreqModule is used to graphically display data when only x variables are specified.
 *
 * @author Hengyi Xue
 */
public class FreqModule extends GUIModule implements ActionListener, ChangeListener {
    private static final int FREQ_REPORT = 0;
    private static final int HYPO_REPORT = 1;
    private static final int Z_REPORT=2;

    private Variable x1, x2;
    private Vector vx2;
    private FreqChart fchart;
    private ParetoChart pchart;
    private PieChart pieChart;
    private CardLayout plotCard;
    private JPanel plotPanel;
    private JMenuBar jmb;
    private JMenu optionMenu;
    private ReportPanel reportPanel;

    private OverlapSlicer chooser;
    private double[] probArray;
    private HyperlinkListener hyperlinkListener = new HyperlinkHandler();
    private int reportType = FREQ_REPORT;

    /**
     * Creates new FreqModule
     */
    public FreqModule(DataSet data) {
        this.data = data;

        try {
            Vector v = data.getYVariables();

            x1 = (Variable) v.elementAt(0);

            //Vector v2 = data.getXVariables();
            vx2 = data.getXVariables();
            if (vx2.size() > 0)
                x2 = (Variable) vx2.elementAt(0);
        } catch (Exception re) {
            re.printStackTrace();
            return;
        }

        init();
    }

    /**
     * The implementation of this module has changed since its creation. Instead of having two
     * x variables, we are using a y variable and a z variable to do the same kind of analysis.
     * The names of the fields are left unchanged.
     *
     * @param data
     * @param yVar
     * @param xVar
     */
    public FreqModule(DataSet data, Variable yVar, Variable xVar) {
        this.data = data;
        x1 = yVar;
        x2 = xVar;
        
        vx2 = new Vector();
        vx2.addElement(x2);

        init();
    }

    public FreqModule(DataSet data, Variable yVar, Vector vx) {
        this.data = data;
        x1 = yVar;
        vx2 = vx;
        
        if(vx2.size() > 0)
            this.x2 = (Variable)vx2.elementAt(0);

        init();
    }
    
    public FreqModule(DataSet data, PlotMetaData mdata) {
        this.data = data;

        this.metaData = mdata;

        x1 = metaData.getYVariable();
        x2 = metaData.getXVariable();

        vx2 = new Vector();
        vx2.addElement(x2);
        
        init();
    }

    private void init() {
        
        data.addRemoteObserver(this);

        if (x2 == null)
            fchart = new FreqChart(data, x1);
        else
            fchart = new FreqChart(data, x1, vx2);
        
        fchart.setGUIModule(this);


        if (x2 == null) {
            pchart = new ParetoChart(data, x1);

            pieChart = new PieChart();
        }

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Container controlPanel=createTopPanel();
        if(controlPanel!=null)
            add(controlPanel);

        plotPanel = buildPlotPanel();

        add(plotPanel);

        reportPanel = new ReportPanel();
        reportPanel.addHyperLinkListener(hyperlinkListener);
        //reportPanel.setBorder(BorderFactory.createEmptyBorder(5,10,0,0));
        add(reportPanel);

        buildMenuBar();

        String content = composeReport(FREQ_REPORT);
        reportPanel.setText(content);

        String[] xLevels = x1.getDistinctCatValues();
        probArray = new double[xLevels.length];
        double d = 1.0 / xLevels.length;
        for (int i = 0; i < probArray.length; i++) {
            probArray[i] = d;
        }

    }

    private Container createTopPanel() {

        if (x2 == null)
            return null; //new JPanel();

        JPanel jp = new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));

        EqualCountGrouper grouper = fchart.getGroupMaker();
        chooser = new OverlapSlicer(grouper);
        chooser.addChangeListener(this);
        jp.add(chooser);

        jp.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 0));
        return jp;
    }

    public void stateChanged(ChangeEvent event) {
        int index = chooser.getCurrentIndex();
        fchart.setX2Index(index);
        reportPanel.setText(composeReport(reportType));
    }

    private void buildMenuBar() {
        jmb = new JMenuBar();


        JMenu jm = new JMenu("Plot");

        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem jrmi1 = new JRadioButtonMenuItem("Bar Chart");
        group.add(jrmi1);
        jrmi1.addActionListener(this);
        jm.add(jrmi1);
        group.setSelected(jrmi1.getModel(), true);

        if (x2 == null) {
            JRadioButtonMenuItem jrmi2 = new JRadioButtonMenuItem("Pareto Chart");
            group.add(jrmi2);
            jrmi2.addActionListener(this);
            jm.add(jrmi2);

            JRadioButtonMenuItem jrmi3 = new JRadioButtonMenuItem("Pie Chart");
            group.add(jrmi3);
            jrmi3.addActionListener(this);
            jm.add(jrmi3);
        }

        optionMenu = new JMenu("Options");
        JRadioButtonMenuItem radio1 = new JRadioButtonMenuItem("Freq Axis", true);
        JRadioButtonMenuItem radio2 = new JRadioButtonMenuItem("Rel Freq Axis");
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radio1);
        group2.add(radio2);
        radio1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fchart.setPlotOption(FreqChart.PLOT_FREQ);
            }
        });
        radio2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fchart.setPlotOption(FreqChart.PLOT_REL_FREQ);
            }
        });
        optionMenu.add(radio1);
        optionMenu.add(radio2);
        //jm.add(optionMenu);

        JMenu analyzeMenu = new JMenu("Analyze");
        JRadioButtonMenuItem radio3 = new JRadioButtonMenuItem("Frequency", true);
        radio3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                reportType = FREQ_REPORT;
                String report = composeReport(FREQ_REPORT);
                reportPanel.setText(report);
            }
        });
        JRadioButtonMenuItem radio4 = new JRadioButtonMenuItem("Chi-Square Test", false);
        radio4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                reportType = HYPO_REPORT;
                String report = composeReport(HYPO_REPORT);
                reportPanel.setText(report);
            }
        });
        JRadioButtonMenuItem radio5 = new JRadioButtonMenuItem("Z-Test");
                if(x1.getDistinctCatValues().length==2)
                    radio5.setEnabled(true);
                else
                    radio5.setEnabled(false);
        radio5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                reportType = Z_REPORT;
                String report = composeReport(Z_REPORT);
                reportPanel.setText(report);
            }
        });

        analyzeMenu.add(radio3);
        analyzeMenu.addSeparator();
        analyzeMenu.add(radio4);
        analyzeMenu.add(radio5);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(radio3);
        group3.add(radio4);
        group3.add(radio5);

        //jm.add(analyzeMenu);
        jmb.add(jm);
        jmb.add(analyzeMenu);

    }

    private JPanel buildPlotPanel() {
        JPanel jp = new JPanel();

        plotCard = new CardLayout();
        jp.setLayout(plotCard);

        jp.add(fchart, "bar chart");
        if (x2 == null) {
            jp.add(pchart, "pareto chart");
            jp.add(pieChart, "pie chart");
        }
        return jp;
    }

    public void showChart(String chartName) {
        if (chartName.equalsIgnoreCase("pareto chart")) {
            plotCard.show(plotPanel, "pareto chart");
        } else if (chartName.equalsIgnoreCase("bar chart")) {
            plotCard.show(plotPanel, "bar chart");
        } else if (chartName.equalsIgnoreCase("pie chart")) {
            pieChart.setValues(fchart.getValues());
            pieChart.setLabels(fchart.getLabels());
            plotCard.show(plotPanel, "pie chart");
        }
    }

    public void actionPerformed(ActionEvent ae) {
        String arg = ae.getActionCommand();

        showChart(arg);
    }

    public JMenuBar getJMenuBar() {
        return jmb;
    }
    
    public JMenu getOptionMenu()
    {
    		return optionMenu;
    }

    public void update(String arg) {
        fchart.updatePlot();
        
        if(pchart != null){
        	pchart.updatePlot();
        }
        
        if(pieChart != null){
            pieChart.setValues(fchart.getValues());
            pieChart.setLabels(fchart.getLabels());
        }
        
        String report = composeReport(reportType);
        reportPanel.setText(report);
    }

    private String composeReport(int type) {
        String[] xLevels = x1.getDistinctCatValues();
        StringBuffer buffer = new StringBuffer();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        double[] freqs = null;
        if(x2==null)
            freqs=fchart.getValues();
        else
            freqs=fchart.getCurrentFreqs();

        double sum = 0;
        for(int i=0;i<freqs.length;i++){
            sum+=freqs[i];
        }
        if (type == FREQ_REPORT) {
            buffer.append("<html>");
            buffer.append("<table width=\"100%\">");
            buffer.append("<tr><th width=\"40%\" align=\"left\">Levels</th>");
            buffer.append("<th width=\"30%\" align=\"left\">Freq</th>");
            buffer.append("<th width=\"30%\" align=\"left\">Rel Freq</th></tr>\n");
            for (int i = 0; i < xLevels.length; i++) {
                buffer.append("<tr><td>").append(xLevels[i]).append("</td>");
                buffer.append("<td>").append((int) freqs[i]).append("</td>");
                buffer.append("<td>").append(nf.format(freqs[i] / sum)).append("</td></tr>\n");

            }
            buffer.append("</table></html>");

        } else if (type == HYPO_REPORT) {
            int[] tmp=new int[freqs.length];
            for(int i=0;i<tmp.length;i++){
                tmp[i]=(int)freqs[i];
            }
            double chiSquare=MathUtils.computeChiSquare(tmp, probArray);
            Chi2Dist chiDist=new Chi2Dist(tmp.length-1);
            double pvalue=chiDist.cdf(chiSquare);
            buffer.append("<html>");
            buffer.append("<table width=\"100%\">");
            buffer.append("<tr><th width=\"40%\" align=\"left\">Levels</th>");
            buffer.append("<th width=\"30%\" align=\"left\">Est Prob</th>");
            buffer.append("<th width=\"30%\" align=\"left\">Hyp Prob</th>");
            buffer.append("</tr>\n");

            for (int i = 0; i < xLevels.length; i++) {
                buffer.append("<tr><td>").append(xLevels[i]).append("</td>");
                buffer.append("<td>").append(nf.format(freqs[i] / sum)).append("</td>\n");
                buffer.append("<td><a href=\"").append(xLevels[i]).append("\">").append(nf.format(probArray[i]));
                buffer.append("</a></td></tr>");
            }
            buffer.append("<tr></tr></n>");
            nf.setMaximumFractionDigits(2);
            buffer.append("<tr><td>").append("Chi-Square:").append("</td><td>");
            buffer.append(nf.format(chiSquare)).append("</td></tr></n>");
            nf.setMaximumFractionDigits(4);
            buffer.append("<tr><td>").append("P-Value:").append("</td><td>");
            buffer.append(nf.format(pvalue));
            buffer.append("</td></tr></n>");
            buffer.append("</table></html>");
        }
        else{
           int[] tmp=new int[freqs.length];
            for(int i=0;i<tmp.length;i++){
                tmp[i]=(int)freqs[i];
            }

            double pHat=freqs[0]/sum;
            double z=(pHat-probArray[0]);
            double z2=Math.sqrt(probArray[0]*(1-probArray[0])/sum);
            z=z/z2;
            Normal normal=new Normal(0,1);
            double pLeft=normal.cdf(z);
            double pRight=1-pLeft;
            double pTwoside=0;
            if(pLeft<0.5)
                pTwoside=2*pLeft;
            else
                pTwoside=2*pRight;

            buffer.append("<html>");
            buffer.append("<table width=\"100%\">");
            buffer.append("<tr><th width=\"40%\" align=\"left\">Levels</th>");
            buffer.append("<th width=\"30%\" align=\"left\">Est Prob</th>");
            buffer.append("<th width=\"30%\" align=\"left\">Hyp Prob</th>");
            buffer.append("</tr>\n");

            for (int i = 0; i < xLevels.length; i++) {
                buffer.append("<tr><td>").append(xLevels[i]).append("</td>");
                buffer.append("<td>").append(nf.format(freqs[i] / sum)).append("</td>\n");
                buffer.append("<td><a href=\"").append(xLevels[i]).append("\">").append(nf.format(probArray[i]));
                buffer.append("</a></td></tr>");
            }
            buffer.append("<tr></tr></n>");
            nf.setMaximumFractionDigits(4);
            buffer.append("<tr><td>").append("Z-value:").append("</td><td>");
            buffer.append(nf.format(z)).append("</td></tr></n>");
            buffer.append("<tr><td>").append("P(|Z| &gt Z-value):").append("</td><td>");
            buffer.append(nf.format(pTwoside)).append("</td></tr></n>");
            buffer.append("<tr><td>").append("P(Z &gt Z-value):").append("</td><td>");
            buffer.append(nf.format(pRight)).append("</td></tr></n>");
            buffer.append("<tr><td>").append("P(Z &lt Z-value):").append("</td><td>");
            buffer.append(nf.format(pLeft)).append("</td></tr></n>");
            buffer.append("</table></html>\n");
        }

        return buffer.toString();
    }

    private class HyperlinkHandler implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (!event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                return;

            String[] xLevels = x1.getDistinctCatValues();
            PolynomialProbInputPanel inputPanel = new PolynomialProbInputPanel(xLevels);
            int result = inputPanel.showDialog(FreqModule.this.getRootPane(), "Please specify the hypothesized probabilities:");
            if (result == PolynomialProbInputPanel.OK_RESULT) {
                double[] values = inputPanel.getHypoProbs();
                for (int i = 0; i < probArray.length; i++) {
                    probArray[i] = values[i];
                }
                reportPanel.setText(composeReport(reportType));
            }

        }
    }
}
