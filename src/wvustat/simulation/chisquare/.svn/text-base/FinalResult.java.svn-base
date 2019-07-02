/*

 *    

 *   FinalResult.java

 * 	 author: Ximing Zhao

 */

package wvustat.simulation.chisquare;



import javax.swing.*;

import java.awt.*;

import wvustat.simulation.model.StatComputer;

import wvustat.plot.Histogram;
import wvustat.statistics.CommonComputation;





public class FinalResult extends JPanel {

    private JTextArea txtRpt;

    private Histogram histogram;

    private StatComputer computeClass;



   

    public FinalResult(StatComputer computeClass) {

        initComponent();

        this.computeClass=computeClass;

    }

    

    public void setData(double[] data){

    	if(data==null || data.length==0)

    		return;

    	
        /*
    	int barNum=5;

    	if(data.length>=100)

    		barNum=5*(int)(Math.log(data.length)/Math.log(10));
            */

        histogram.setData(data);



        java.text.NumberFormat nf=java.text.NumberFormat.getInstance();

        nf.setMaximumFractionDigits(3);

        StringBuffer buf=new StringBuffer();

        buf.append("n: \t\t"+data.length+"\n");

        buf.append("Average: \t\t"+nf.format(CommonComputation.computeAverage(data))+"\n");

        buf.append("SD: \t\t"+nf.format(CommonComputation.computeSD(data))+"\n");

        buf.append("Median: \t\t"+nf.format(CommonComputation.computeMedian(data))+"\n");

        buf.append("Min: \t\t"+nf.format(CommonComputation.computeMin(data))+"\n");

        buf.append("Max: \t\t"+nf.format(CommonComputation.computeMax(data))+"\n");

        buf.append("Inter Quartile Range: \t"+nf.format(CommonComputation.computeIQR(data))+"\n");





        txtRpt.setText(buf.toString());

    }



    private void initComponent(){

        txtRpt=new JTextArea(8,9);

        txtRpt.setEditable(false);

        histogram=new Histogram();



        this.setLayout(new GridBagLayout());



        JLabel title=new JLabel("3. Examine Statistics");

        title.setFont(new Font("Arial", Font.BOLD, 16));

        title.setForeground(Color.black);



        this.add(title, new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4,4,4,4), 0, 0));

        this.add(txtRpt, new GridBagConstraints(0,1,0,-1,1.0,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(4,4,4,4), 0, 0));

        this.add(histogram, new GridBagConstraints(0,2,0,0,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4,4,4,4), 0, 0));



        this.setBorder(new javax.swing.border.EtchedBorder());

    }



    public void reset(){

    	txtRpt.setText("");

    	histogram.clear();

    }



}