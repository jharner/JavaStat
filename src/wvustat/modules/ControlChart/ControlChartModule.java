/*
 * ControlChartModule.java
 *
 * Created on August 28, 2000, 3:51 PM
 */

package wvustat.modules.ControlChart;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import javax.swing.*;

import java.util.Vector;

import wvustat.interfaces.*;
import wvustat.modules.*;
/**
 * ControlChart module is a module that displays control charts.
 *
 * @author  Hengyi Xue
 * @version 1.0
 */
public class ControlChartModule extends GUIModule implements ActionListener {
    
    private Variable x_var, y_var;
    
    private Vector varNames=new Vector();
    
    private ControlChart chart;
    
    private MomentsReport mreport;
    
    private PlainReport rReport,sReport;
    
    private JMenuBar jmb;
    
    private CardLayout reportCard;
    
    private JPanel reportPanel, ewRpt, cRpt;
    
    /** Creates new ControlChartModule */
    public ControlChartModule(DataSet data) {
        this.data=data;

        Vector v = data.getYVariables();
        y_var = (Variable) v.elementAt(0);
        v = data.getXVariables();
        x_var = (Variable) v.elementAt(0);

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());

        init();
    }
    
    public ControlChartModule(DataSet data, Variable yVar, Variable xVar) {
        this.data=data;
        y_var=yVar;
        x_var=xVar;

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());

        init();
    }    
    
    public ControlChartModule(DataSet data, PlotMetaData pmd){
        this.data=data;
        
        this.metaData=pmd;
        
        y_var=metaData.getYVariable();
        x_var=metaData.getXVariable();

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());
        init();
    }
    
    private void init(){
        setBackground(Color.white);
        setBorder(new javax.swing.border.LineBorder(Color.black));
        
        
        data.addRemoteObserver(this);
        
        
        chart=new ControlChart(data,x_var, y_var);
        
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
        
        gbc.weighty=0.85;
        JPanel plotPanel=buildPlotPanel();
        add(plotPanel, gbc);
        gbc.gridy++;
        gbc.weighty=0.15;
        JPanel legendPanel=buildReportPanel();
        
        add(new JScrollPane(legendPanel), gbc);
        buildMenuBar();
    }
    
    public JMenuBar getJMenuBar(){
        return jmb;
    }
    
    public JMenu getOptionMenu(){
    	return null;
    }
    
    private void buildMenuBar(){
        jmb=new JMenuBar();
        
        JMenu jm=new JMenu("Plot");
        
        ButtonGroup group=new ButtonGroup();
        
        String[] tmp=ControlChart.display_options;
        for(int i=0;i<tmp.length;i++){
            JRadioButtonMenuItem jrmi=new JRadioButtonMenuItem(tmp[i]);
            jrmi.addActionListener(this);
            group.add(jrmi);
            if(i==0)
                group.setSelected(jrmi.getModel(),true);
            jm.add(jrmi);
        }
        
        jm.addSeparator();
        JMenu optionMenu=new JMenu("Options");
        jm.add(optionMenu);
        jmb.add(jm);
        
        jm=new JMenu("Analyze");
        JMenuItem item=jm.add("Summary");
        item.addActionListener(this);
        
        jmb.add(jm);
    }
    
    private JPanel buildControlPanel(){
        return null;
    }
    
    private JPanel buildPlotPanel(){
        JPanel jp=new JPanel();
        jp.setLayout(new BorderLayout());
        jp.add(chart, BorderLayout.CENTER);
        
        return jp;
    }
    
    private JPanel buildReportPanel(){
        reportPanel=new JPanel();
        reportCard=new CardLayout();
        
        reportPanel.setLayout(reportCard);
        mreport=new MomentsReport(chart);
        
        rReport=new PlainReport(2);
        rReport.setText(new String[]{"D3="+chart.getD3(),"D4="+chart.getD4()});
        
        sReport=new PlainReport(2);
        sReport.setText(new String[]{"B3="+chart.getB3(),"B4="+chart.getB4()});
        
        ewRpt=new JPanel();
        cRpt=new JPanel();
        ewRpt.setBackground(Color.white);
        cRpt.setBackground(Color.white);
        
        reportPanel.add("Mean",mreport);
        reportPanel.add("R",rReport);
        reportPanel.add("S",sReport);
        reportPanel.add("EWMA", ewRpt);
        reportPanel.add("Cusum",cRpt);
        
        reportCard.show(reportPanel,"Mean");
        
        return reportPanel;
    }
    
    public void actionPerformed(ActionEvent evt){
        String arg=evt.getActionCommand();       
        
        int index=0;
        String[] options=ControlChart.display_options;
        while(index<options.length && !options[index].equals(arg))
            index++;
        
        if(index<options.length){
            setDisplay(index);
            
        }
        
        
    }
    
    public void setDisplay(int index){
        String[] options=ControlChart.display_options;
        chart.setDisplayOption(index);
        if(index<=1){
            reportCard.show(reportPanel,"Mean");
            mreport.updateReport();
        }
        else{
            reportCard.show(reportPanel,options[index]);
        }
    }
    
    
    public void update(String msg){
        chart.updatePlot(msg);
    }
}
