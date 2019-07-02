/*
 * DrawPanel.java
 *
 * Created on April 18, 2002, 3:29 PM
 */

package wvustat.simulation.boxmodel;

import java.awt.*;
import java.text.NumberFormat;
import javax.swing.*;

import wvustat.simulation.model.*;
import wvustat.plot.Histogram;
import wvustat.util.IntOnlyField;
import wvustat.statistics.CommonComputation;
/**
 *
 * @author  James Harner
 * @version 
 */
public class DrawPanel extends javax.swing.JComponent implements java.awt.event.ActionListener{
    private BoxModel boxModel;
    private JRadioButton wRep, woRep;
    private JRadioButton fastChk, slowChk;
    private Histogram histogram;
    private JTextField numOfDrawInput;
    private JLabel label1;
    private JTextArea outcomeArea;
    private ButtonGroup sample, control;
    private JButton startButton, resetButton;
    private double[] outcome;
    private JLabel avgLabel, nLabel, sdLabel;
    
    /** Creates new DrawPanel */
    public DrawPanel() {
        initComponents();
    }
    
    public void setBoxModel(BoxModel boxModel){
        this.boxModel=boxModel;
    }
    
    protected void initComponents(){
        label1=new JLabel("Number of draws: ");
        numOfDrawInput=new IntOnlyField("100",6);
        
        startButton=new JButton("Start");
        resetButton=new JButton("Start Over");
        resetButton.setActionCommand("Reset");
        
        startButton.addActionListener(this);
        resetButton.addActionListener(this);
        
        wRep=new JRadioButton("With Replacement", true);
        woRep=new JRadioButton("Without Replacement", false);
        wRep.setActionCommand("RepYes");
        woRep.setActionCommand("RepNo");
        wRep.addActionListener(this);
        woRep.addActionListener(this);
        sample=new ButtonGroup();
        sample.add(wRep);
        sample.add(woRep);
        
        fastChk=new JRadioButton("Fast", true);
        slowChk=new JRadioButton("Slow", false);
        fastChk.addActionListener(this);
        slowChk.addActionListener(this);
        control=new ButtonGroup();
        control.add(fastChk);
        control.add(slowChk);
        
        histogram=new Histogram();
        outcomeArea=new JTextArea(10,4); 
        
        nLabel=new JLabel("# of Draws: N/A");
        avgLabel=new JLabel("Average: N/A");
        sdLabel=new JLabel("SD: N/A");
        
        JPanel panel1=new JPanel(new GridBagLayout());
        panel1.add(new JLabel("Sampling"), new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel1.add(wRep, new GridBagConstraints(0,1,0,-1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
        panel1.add(woRep, new GridBagConstraints(0,2,0,0,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
        panel1.setBorder(new javax.swing.border.EtchedBorder());
        
        JPanel panel2=new JPanel(new GridBagLayout());
        panel2.add(new JLabel("Control"), new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel2.add(fastChk, new GridBagConstraints(0,1,0,-1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
        panel2.add(slowChk, new GridBagConstraints(0,2,0,0,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
        panel2.setBorder(new javax.swing.border.EtchedBorder());
        
        JPanel panel3=new JPanel(new GridBagLayout());
        panel3.add(label1, new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        panel3.add(numOfDrawInput, new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel3.add(startButton, new GridBagConstraints(2,0,-1,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel3.add(resetButton, new GridBagConstraints(3,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        JPanel panel4=new JPanel(new GridBagLayout());
        panel4.add(new JScrollPane(outcomeArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), new GridBagConstraints(0,0,-1,0,0.4,1.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        panel4.add(histogram, new GridBagConstraints(1,0,0,-1,0.6,1.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        
        JPanel panel5=new JPanel(new GridBagLayout());
        panel5.add(nLabel, new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        panel5.add(avgLabel, new GridBagConstraints(0,1,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
        panel5.add(sdLabel, new GridBagConstraints(0,2,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        panel4.add(panel5, new GridBagConstraints(1,1,0,0,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        JPanel main=new JPanel(new GridBagLayout());
        main.add(panel3, new GridBagConstraints(0,0,0,1,1.0,0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        main.add(panel1, new GridBagConstraints(0,1,-1,-1,0.6,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        main.add(panel2, new GridBagConstraints(1,1,0,-1, 0.4,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),0,0));
        main.add(panel4, new GridBagConstraints(0,2,0,0,1.0,1.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        
        main.setBorder(new javax.swing.border.EtchedBorder());
        this.setLayout(new BorderLayout());
        this.add(main, "Center");
    }
    
    public static void main(String[] args){
        JFrame jf=new JFrame("Test");
        DrawPanel dp=new DrawPanel();
        
        jf.getContentPane().add(dp);
        jf.pack();
        jf.show();
    }
    
    private void updateStats(){
        NumberFormat nf=NumberFormat.getInstance();
        StatComputer com=new StatComputer();
        
        String tmp="# of Draws: "+outcome.length;
        nLabel.setText(tmp);
        tmp="Average: "+nf.format(CommonComputation.computeAverage(outcome));
        avgLabel.setText(tmp);
        tmp="SD: "+nf.format(CommonComputation.computeSD(outcome));
        sdLabel.setText(tmp);
    }
    
    private void clearStats(){
        nLabel.setText("# of Draws: N/A");
        avgLabel.setText("Average: N/A");
        sdLabel.setText("SD: N/A");
    }
        
    public void actionPerformed(java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Start")){
            int numRun=Integer.parseInt(numOfDrawInput.getText());
            boolean repYes=wRep.isSelected();
                            
            double[] tmp=new double[numRun];
            StringBuffer buf=new StringBuffer();
            NumberFormat nf=NumberFormat.getInstance();
 
            for(int i=0;i<numRun;i++){
            	try {
            		tmp[i]=boxModel.drawOne(repYes);
            	} catch (ArithmeticException aex) { //try block added by djluo
            		JOptionPane.showMessageDialog(this, aex.getMessage());
            		return;
            	}
            	
                buf.append(nf.format(tmp[i]));
                if(i<numRun-1)
                    buf.append("\n");
            }
            
            if(outcome==null)
                outcome=tmp;
            else{
                double[] tmp2=new double[outcome.length+numRun];
                System.arraycopy(outcome,0,tmp2,0,outcome.length);
                System.arraycopy(tmp,0,tmp2,outcome.length,numRun);
                outcome=tmp2;
            }
            
            outcomeArea.setText(buf.toString());
            histogram.setData(outcome);
            updateStats();
        }
        else if(cmd.equals("Reset")){
            outcome=null;
            histogram.clear();
            outcomeArea.setText("");
            clearStats();
        }
    }    

}
