/*
 * CIPanel.java
 *
 * Created on April 26, 2002, 11:47 AM
 */

package wvustat.simulation.boxmodel;

import java.awt.*;
import javax.swing.*;
import java.text.NumberFormat;

import wvustat.plot.CIPlot;
import wvustat.simulation.model.*;
import wvustat.util.IntOnlyField;
import wvustat.dist.StudentT;
import wvustat.statistics.CommonComputation;
/**
 * 
 * @author  Hengyi Xue
 * @version 
 */
public class CIPanel extends javax.swing.JPanel implements java.awt.event.ActionListener, Runnable {
    private JTextField drawInput, exprInput;
    private CIPlot plot;
    private BoxModel boxModel;
    private int m, n;
    private JTextArea rptArea;
    private double tStat;
    private JLabel rptLabel;
    private int missCnt=0;
    private JButton startBtn;
    /** Creates new CIPanel */
    public CIPanel() {        
        initComponents();
    }
    
    public void setBoxModel(BoxModel boxModel){
        this.boxModel=boxModel;
        plot.setStats(boxModel.getAverage(), boxModel.getSD());
    }
    
    private void initComponents(){
        JLabel label1=new JLabel("# of Draws per Experiment: ");
        JLabel label2=new JLabel("# of Experiments: ");
        drawInput=new IntOnlyField("10",4);
        exprInput=new IntOnlyField("20",4);
        
        startBtn=new JButton("Start");
        startBtn.addActionListener(this);
        plot=new CIPlot(0,1);
        rptArea=new JTextArea(4,10);
        rptLabel=new JLabel("        ");
        
        JPanel panel1=new JPanel(new GridBagLayout());
        
        panel1.add(label1, new GridBagConstraints(0,0,-1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel1.add(drawInput, new GridBagConstraints(1,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        panel1.add(label2, new GridBagConstraints(0,1,-1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
        panel1.add(exprInput, new GridBagConstraints(1,1,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        panel1.add(startBtn, new GridBagConstraints(0,2,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        panel1.add(new JScrollPane(rptArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), new GridBagConstraints(0,3,0,1,1.0,0.4, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        panel1.add(rptLabel, new GridBagConstraints(0,4,0,1,1.0,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        
        panel1.add(plot, new GridBagConstraints(0,5,0,0,1.0,0.6,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        
        panel1.setBorder(new javax.swing.border.EtchedBorder());
        
        this.setLayout(new BorderLayout());
        this.add(panel1, BorderLayout.CENTER);
    }

    public void actionPerformed(java.awt.event.ActionEvent p1) {   
        startBtn.setEnabled(false);
        rptArea.setText("");
        rptLabel.setText("  ");
        String input1=drawInput.getText();
        String input2=exprInput.getText();

        m=Integer.parseInt(input1);
        n=Integer.parseInt(input2);
        
        plot.setXLength(n);
        StudentT tdist=new StudentT(m-2);
        tStat=tdist.quantile(0.95);
        
        plot.setTStat(tStat);
        
        Thread thread=new Thread(this);
        thread.start();
    }
    
    public void run() {
        double[] array1=new double[m];
        double[] avgs=new double[n];
        double[] sds=new double[n];
        
        StatComputer sc=new StatComputer();
        NumberFormat nf=NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        
        missCnt=0;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                array1[j]=boxModel.drawOne(true);
            }           
            
            avgs[i]=CommonComputation.computeAverage(array1);
            sds[i]=CommonComputation.computeSD(array1);
            plot.addPoint(avgs[i], sds[i]);
            
            if(boxModel.getAverage()>avgs[i]+tStat*sds[i] || boxModel.getAverage()<avgs[i]-tStat*sds[i])
                missCnt++;
            
            double se=sds[i]/Math.sqrt(m);
            StringBuffer sb=new StringBuffer();
            sb.append("Avg="+nf.format(avgs[i]));
            sb.append(", sd="+nf.format(sds[i]));
            sb.append(", se="+nf.format(se));
            sb.append(", interval ("+nf.format(avgs[i]-tStat*sds[i]));
            sb.append(", "+nf.format(avgs[i]+tStat*sds[i]));
            sb.append(")\n");
            
            rptArea.append(sb.toString());
            try{
                Thread t=Thread.currentThread();
                t.sleep(1);
            }
            catch(InterruptedException extn){
            }
        }
        
        
        rptLabel.setText((n-missCnt)+" intervals contained the average and " +missCnt+" did not.");
      
        
        startBtn.setEnabled(true);
    }
    
}
