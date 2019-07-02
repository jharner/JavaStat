package wvustat.modules;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * ChartModule is a bundled GUI component that includes a Bar chart, a subgrouped
 * Bar chart and a Pie chart.
 *
 * author: Hengyi Xue
 * version: 1.0, June 22, 2000
 */

public class ChartModule extends GUIModule implements ActionListener, ChangeListener{

    private Variable x1_var, y_var, z1_var,x2_var,z2_var;

    private GroupChooser z1Chooser, z2Chooser;

    private Vector varNames=new Vector();

    private BarChart barChart;

    private PieChart pieChart;

    private JMenuBar jmb;
    private JMenu optionMenu;

    private JPanel plotPanel;

    private CardLayout plotCard;

    public ChartModule(DataSet data){
        this.data=data;


        Vector v = data.getYVariables();
        y_var = (Variable) v.elementAt(0);
        v = data.getXVariables();
        x1_var = (Variable) v.elementAt(0);
        if (v.size() > 1)
            x2_var = (Variable) v.elementAt(1);

        v = data.getZVariables();
        if (v.size() > 0)
            z1_var = (Variable) v.elementAt(0);
        if (v.size() > 1)
            z2_var = (Variable) v.elementAt(1);

        varNames.addElement(x1_var.getName());
        varNames.addElement(y_var.getName());
        if (x2_var != null)
            varNames.addElement(x2_var.getName());
        if (z1_var != null)
            varNames.addElement(z1_var.getName());
        if (z2_var != null)
            varNames.addElement(z2_var.getName());

        init();

    }
    
    public ChartModule(DataSet data, Variable yVar, Variable xVar, Variable x2Var){
        this.data=data;
        y_var=yVar;
        x1_var=xVar;
        if(x2Var!=null)
            x2_var=x2Var;

        Vector v = data.getZVariables();
        if (v.size() > 0)
            z1_var = (Variable) v.elementAt(0);
        if (v.size() > 1)
            z2_var = (Variable) v.elementAt(1);

        varNames.addElement(x1_var.getName());
        varNames.addElement(y_var.getName());
        if (x2_var != null)
            varNames.addElement(x2_var.getName());
        if (z1_var != null)
            varNames.addElement(z1_var.getName());
        if (z2_var != null)
            varNames.addElement(z2_var.getName());

        init();

    }    

    public ChartModule(DataSet data,  PlotMetaData pmd){
        this.data=data;
        
        this.metaData=pmd;

        y_var=metaData.getYVariable();
        x1_var=metaData.getXVariable();
        z1_var=metaData.getZVariable();
        x2_var=metaData.getX2Variable();
        z2_var=metaData.getZ2Variable();

        varNames.addElement(x1_var.getName());
        varNames.addElement(y_var.getName());

        if (z1_var != null)
            varNames.addElement(z1_var.getName());
        if (x2_var != null)
            varNames.addElement(x2_var.getName());
        if (z2_var != null)
            varNames.addElement(z2_var.getName());

        init();

    }

    private void init(){
        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));
          
        data.addRemoteObserver(this);            
        
        barChart=new BarChart(data, x1_var,x2_var, y_var, z1_var,z2_var);     
        barChart.setGUIModule(this);

        if(x2_var==null)
        pieChart=new PieChart();        

        setLayout(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.weightx=1.0;
        gbc.fill=GridBagConstraints.BOTH;
        JPanel controlPanel=buildControlPanel();
        if(controlPanel!=null){
            add(controlPanel, gbc);
            gbc.gridy++;
        }

        gbc.weighty=0.8;
        plotPanel=buildPlotPanel();
        add(plotPanel, gbc);
        gbc.gridy++;
        gbc.weighty=0.2;
        JPanel legendPanel=buildLegendPanel();

        add(new JScrollPane(legendPanel), gbc);

        buildMenuBar();
    }

    private void buildMenuBar(){
        jmb=new JMenuBar();

        JMenu jm=new JMenu("Plot");
        ButtonGroup group=new ButtonGroup();
        JRadioButtonMenuItem jrmi1=new JRadioButtonMenuItem("Bar Chart");
        group.add(jrmi1);
        jrmi1.addActionListener(this);
        jm.add(jrmi1);
        group.setSelected(jrmi1.getModel(), true);

        JRadioButtonMenuItem jrmi3=new JRadioButtonMenuItem("Pie Chart");
        group.add(jrmi3);
        jrmi3.addActionListener(this);
        jm.add(jrmi3);
        if(x2_var!=null)
            jrmi3.setEnabled(false);

        //jm.addSeparator();
        JMenu statMenu=new JMenu("Options");
        this.optionMenu = statMenu;
        /*JMenuItem jmi = new JMenuItem("Summary Table");
        jmi.addActionListener(this);
        statMenu.add(jmi);*/
        
        ButtonGroup statGroup=new ButtonGroup();
        for(int i=0;i<BarChart.stats_supported.length;i++){
            jrmi1=new JRadioButtonMenuItem(BarChart.stats_supported[i]);
            statMenu.add(jrmi1);
            statGroup.add(jrmi1);
            jrmi1.addActionListener(this);
            if(i==0)
            statGroup.setSelected(jrmi1.getModel(),true);
        }

        //jm.add(statMenu);
        
        jmb.add(jm);
    }

    private JPanel buildControlPanel(){
        if(z1_var==null)
        return null;

        JPanel jp2=new JPanel();
        jp2.setBackground(Color.white);
        jp2.setLayout(new GridBagLayout());

        String tmpStr1=" ";
        String tmpStr2=" ";

        tmpStr1 = z1_var.getName();
        if (z2_var != null)
            tmpStr2 = z2_var.getName();

        z1Chooser=new GroupChooser(tmpStr1,barChart.getZ1Levels());
        z1Chooser.addChangeListener(this);
        if(z2_var!=null){
            z2Chooser=new GroupChooser(tmpStr2,barChart.getZ2Levels());
            z2Chooser.addChangeListener(this);
        }

        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(2,2,2,2);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.weightx=0.5;
        jp2.add(z1Chooser, gbc);

        gbc.gridx++;
        if(z2_var!=null)
        jp2.add(z2Chooser,gbc);

        jp2.setBorder(BorderFactory.createLineBorder(Color.black));

        return jp2;

    }

    private JPanel buildPlotPanel(){
        plotCard=new CardLayout();

        JPanel jp=new JPanel();
        jp.setLayout(plotCard);

        jp.add(barChart, "Bar Chart");
        if(x2_var==null)
        jp.add(pieChart,"Pie Chart");

        return jp;
    }

    private JPanel buildLegendPanel(){
        JPanel jp1;
        if(x2_var!=null){
            ColorLegendPanel clp=new ColorLegendPanel(barChart.getColors(), barChart.getColorLabels());
            jp1=clp;
        }
        else{
            jp1=new JPanel();
            jp1.setBackground(Color.white);
            jp1.setPreferredSize(new Dimension(100, 40));
        }

        return jp1;

    }


    public void update(String msg){
        if(varNames.contains(msg)||msg.equals("yymask")){
            barChart.updatePlot(msg);
            //report.updateReport();
            
            if(pieChart!=null){
                pieChart.setValues(barChart.getYValues());
                pieChart.setLabels(barChart.getXValues());
            }
        }
    }


    public void changeGroup(String whichVar, Integer index){
        int val=index.intValue();

        if(whichVar.equals("z1")){
            z1Chooser.setCurrentIndex(val);
            barChart.setZ1Index(val);
        }
        else if(whichVar.equals("z2")){
            z2Chooser.setCurrentIndex(val);
            barChart.setZ2Index(val);
        }
    }

    public void showBarChart(){
        plotCard.show(plotPanel, "Bar Chart");
    }

    public void showPieChart(){
        if(pieChart==null)
            return;
        
        pieChart.setValues(barChart.getYValues());
        pieChart.setLabels(barChart.getXValues());

        plotCard.show(plotPanel,"Pie Chart");
    }

    public void showMeanStat(){
        barChart.setStatOption(BarChart.MEAN_STAT);
        if(pieChart!=null){
            pieChart.setValues(barChart.getYValues());
            pieChart.setLabels(barChart.getXValues());
        }
    }

    public void showSumStat(){
        barChart.setStatOption(BarChart.SUM_STAT);
        if(pieChart!=null){
            pieChart.setValues(barChart.getYValues());
            pieChart.setLabels(barChart.getXValues());
        }
    }

    public void actionPerformed(ActionEvent ae){
        String arg=ae.getActionCommand();


        if(arg.equals("Summary Table")){
            SummaryTableModel model=new SummaryTableModel(barChart.getSumMatrix(), barChart.getX1Levels(), barChart.getX2Levels());
            JTable sumTable=new JTable(model);
            JFrame fr=new JFrame("Summary");
            
            JScrollPane scrollPane=new JScrollPane(sumTable);
            
            fr.getContentPane().add(scrollPane);
            fr.pack();
            Dimension d=fr.getPreferredSize();
            fr.setSize(d.width, 200);
            fr.show();
        }            
        else if(arg.equals("Bar Chart")){
            this.showBarChart();
            
        }
        else if(arg.equals("Pie Chart")){
            this.showPieChart();
            
        }
        else if(arg.equals("Mean")){
            this.showMeanStat();
            
        }
        else if(arg.equals("Sum")){
            this.showSumStat();            
        }
        else if(arg.equals("Percentage")){
            barChart.setStatOption(BarChart.PERCENT_STAT);   
        }
    }

    public JMenuBar getJMenuBar(){
        return jmb;
    }
    
    public JMenu getOptionMenu()
    {
    		return optionMenu;
    }

    public void stateChanged(ChangeEvent evt){

        Object obj=evt.getSource();

        int val=-1;
        String whichVar=null;
        if(obj==z1Chooser){
            val=z1Chooser.getCurrentIndex();
            whichVar="z1";
        }
        else if(obj==z2Chooser){
            val=z2Chooser.getCurrentIndex();
            whichVar="z2";
        }

        this.changeGroup(whichVar,new Integer(val));
        Object[] args=new Object[2];
        args[0]=whichVar;
        args[1]=new Integer(val);     

    }

}
