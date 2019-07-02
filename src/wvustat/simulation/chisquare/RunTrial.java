/*

 * RunTrial.java

 *

 * author: Ximing Zhao

 */



package wvustat.simulation.chisquare;



import java.awt.*;

import javax.swing.*;

import java.text.NumberFormat;

import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;









public class RunTrial extends JComponent implements ActionListener {

    

    private JTextField[][] fields;

    private JLabel label1, label2,label3;

    private JTextArea statHistory;

    private JRadioButton fastCheck, slowCheck;

    private JLabel statLabel; 

    private ActionListener buttonListener;

    private JButton btn1, btn2, btn3, btn4,btn5; //buttons of number of runs

    private JButton stopBtn, resetBtn;

    private boolean isStopped=false;

    private double[] column1,column2,column3;

//    private JLabel sucLabel, failLabel, totalLabel, sProbLabel, fProbLabel, tProbLabel;

    

    /** Creates new RunTrial */

    public RunTrial(ActionListener listener) {        

        this.buttonListener=listener;

        initComponents();

    }

    

    private void initComponents(){

        setLayout(new GridBagLayout());

        

        fields=new JTextField[3][12];

        for(int i=0;i<fields.length;i++){

            for(int j=0;j<fields[i].length;j++){

                fields[i][j]=new JTextField(4);

        //        fields[i][j].setEditable(false);

            }

        }

        

        // fieldspanel

        JPanel fieldsPanel=new JPanel();

        fieldsPanel.setLayout(new GridLayout(13,3,2,2));

        label1=new JLabel("Expected");

        label2=new JLabel("Obtained");

        label3=new JLabel("Simulated");

        fieldsPanel.add(label1);

        fieldsPanel.add(label2);

        fieldsPanel.add(label3);

        

        

        

        for(int i=0;i<12;i++){

            for(int j=0;j<3;j++){

                fieldsPanel.add(fields[j][i]);

                fields[j][i].setEditable(false);

                fields[j][i].setBackground(Color.white);

            }

        }

        

        

 	

	//  panel1 with number of trials

        btn1=new JButton("1");

        btn2=new JButton("10");

        btn3=new JButton("25");

        btn4=new JButton("100");

        btn5=new JButton("500");

        btn1.addActionListener(buttonListener);

        btn2.addActionListener(buttonListener);

        btn3.addActionListener(buttonListener);

        btn4.addActionListener(buttonListener);

        btn5.addActionListener(buttonListener);





        JPanel panel1=new JPanel(new GridBagLayout());

        panel1.add(new JLabel("# of Trials: "), new GridBagConstraints(0,0,1,0,0.4,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel1.add(btn1, new GridBagConstraints(1,0,1,0,0.1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel1.add(btn2, new GridBagConstraints(2,0,1,0,0.1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel1.add(btn3, new GridBagConstraints(3,0,1,0,0.1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel1.add(btn4, new GridBagConstraints(4,0,1,0,0.1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel1.add(btn5, new GridBagConstraints(5,0,1,0,0.1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel1.setBorder(new javax.swing.border.EtchedBorder());

        

        

        //optionPanel for speed

        fastCheck=new JRadioButton("Fast", true);

        slowCheck=new JRadioButton("Slow", false);

        ButtonGroup bgr=new ButtonGroup();

        bgr.add(fastCheck);

        bgr.add(slowCheck);

        

        JPanel optionPanel=new JPanel(new GridBagLayout());

        optionPanel.add(new JLabel("Speed: "), new GridBagConstraints(0,0,1,0,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

        optionPanel.add(fastCheck, new GridBagConstraints(1,0,-1,0,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

        optionPanel.add(slowCheck,new GridBagConstraints(2,0,0,0,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));



   	

   	//panel2 with statHistory

 	statLabel=new JLabel("Simulated Chi-square");

        statHistory=new JTextArea(6,3);

        statHistory.setEditable(false);

        

        JPanel panel2=new JPanel(new GridBagLayout());

        panel2.add(statLabel, new GridBagConstraints(0,0,0,-1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        panel2.add(new JScrollPane(statHistory), new GridBagConstraints(0,1,0,0,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));

        

        JLabel title=new JLabel("2. Run trials");

        title.setFont(new Font("Arial", Font.BOLD, 16));

        title.setForeground(Color.black);        

        

        resetBtn=new JButton("Reset");

        resetBtn.addActionListener(buttonListener);

        stopBtn=new JButton("Stop");

        stopBtn.setEnabled(false);

        stopBtn.addActionListener(this);

        JPanel actionPanel=new JPanel(new GridBagLayout());

        actionPanel.add(stopBtn, new GridBagConstraints(0,0,-1,0,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

        actionPanel.add(resetBtn,new GridBagConstraints(1,0,0,0,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));



        

        this.add(title, new GridBagConstraints(0,0,5,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

        this.add(panel1, new GridBagConstraints(0,1,5,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));

        this.add(optionPanel,new GridBagConstraints(0,2,5,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

        this.add(actionPanel,new GridBagConstraints(0,3,5,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));

        this.add(fieldsPanel, new GridBagConstraints(0,4,3,1,0.6,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));

        this.add(panel2, new GridBagConstraints(3,4,2,1,0.3,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0));

        

        

        this.setBorder(new javax.swing.border.EmptyBorder(4,4,4,4));

    }    

    

    



    public void setText1(double[] array1,double[] array2) {

	

	column1=new double[array1.length];

	column2=new double[array1.length];



	for(int i=0;i<array1.length;i++){

		column1[i]=array1[i];

                column2[i]=array2[i];

                

	};

	

	

	

	}

	

    public void setText2(double[] array3) {

	

	column3=new double[array3.length];

	

	for(int i=0;i<array3.length;i++){

		

                column3[i]=array3[i];

	};

	

	}

    	



    public boolean isFastRun(){

        return fastCheck.isSelected();

    }

    

    

    public void showText(){

    	for(int i=0;i<12;i++){

    		fields[0][i].setText("");

    		fields[1][i].setText("");

    	}

    	

    	for(int i=0;i<column1.length;i++){

    		fields[0][i].setText(String.valueOf(column1[i]));

    		fields[1][i].setText(String.valueOf(column2[i]));

    	};

    }

    

    public void showResults(){

    

        for(int i=0;i<column3.length;i++){

            fields[2][i].setText(String.valueOf(column3[i]));

                

        }

        

      

    }    	

   

   

    public void showStats(double[] stats, double chisquareobtained){

        NumberFormat nf=NumberFormat.getInstance();

        nf.setMaximumFractionDigits(2);

        

        

        StringBuffer buf=new StringBuffer();

        for(int i=0;i<stats.length;i++){

            buf.append(nf.format(stats[i]));

            if(stats[i]>chisquareobtained) {

            	buf.append("    Over");

            	

            }

            else

           	buf.append("  ");

            if(i<stats.length-1)

                buf.append("\n");

        }

        statHistory.setText(buf.toString());



    }    

   

    public void clearPart(){

        statHistory.setText("");

        for(int i=0;i<column1.length;i++){

            fields[2][i].setText("");

        }

        

    }

    

    public void clearAll(){

        statHistory.setText("");

        for(int i=0;i<12;i++){

            fields[0][i].setText("");

            fields[1][i].setText("");

            fields[2][i].setText("");

        }

        

    }

    

    public void setEnabled(boolean bl){

    	btn1.setEnabled(bl);

    	btn2.setEnabled(bl);

    	btn3.setEnabled(bl);

    	btn4.setEnabled(bl);

    	btn5.setEnabled(bl);

    	resetBtn.setEnabled(bl);

    	stopBtn.setEnabled(!bl);

    }

    

    public void restart(){

    	isStopped=false;

    }

    

    public boolean isStopped(){

    	return isStopped;

    }

    

    public void actionPerformed(ActionEvent evt){

    	isStopped=true;

    }



}