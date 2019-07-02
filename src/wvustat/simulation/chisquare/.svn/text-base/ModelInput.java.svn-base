/*

 * ModelInput.java

 * author: Ximing Zhao

 */

package wvustat.simulation.chisquare;



import wvustat.statistics.InvalidDataError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;



public class ModelInput extends JComponent implements java.awt.event.ActionListener{

    private JTextField[][] fields;

    private JLabel label1, label2;

    private ActionListener clearAllListener;

    private double sum1,sum2,min1,min2;



    /** Creates new ModelInput */

    public ModelInput(ActionListener listener) {

    	this.clearAllListener=listener;

        setBackground(Color.lightGray);

        fields=new JTextField[2][12];

        for(int i=0;i<fields.length;i++){

            for(int j=0;j<fields[i].length;j++){

                fields[i][j]=new JTextField(4);

            }

        }

        

        initComponents();

        sum1=0;

        sum2=0;

        min1=0;

        min2=0;

    }

    

    

    

    private void initComponents(){

        JPanel inputPanel=new JPanel();

        inputPanel.setLayout(new GridLayout(13,2,2,2));

        label1=new JLabel("Expected");

        label2=new JLabel("Obtained");

        inputPanel.add(label1);

        inputPanel.add(label2);

        

        

        

        for(int i=0;i<12;i++){

            for(int j=0;j<2;j++){

                inputPanel.add(fields[j][i]);

            }

        }

        

        JPanel buttonPanel=new JPanel(new GridBagLayout());

        JButton button1=new JButton("Clear model");

        JButton button2=new JButton("Clear all");

        button1.addActionListener(this);

        button2.addActionListener(clearAllListener);

        buttonPanel.add(button1, new GridBagConstraints(0,0,1,0,0.2,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,4,2,4), 0, 0));

        buttonPanel.add(button2, new GridBagConstraints(1,0,1,0,0.2,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,4,2,4), 0, 0));

  

        

        this.setLayout(new GridBagLayout());

        JLabel title=new JLabel("1. Define the experiment model");

        title.setFont(new Font("Arial", Font.BOLD, 16));

        title.setForeground(Color.black);             

        

        this.add(title, new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));

        this.add(inputPanel, new GridBagConstraints(0,1,0,-1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));

        this.add(buttonPanel, new GridBagConstraints(0,2,0,0,1.0,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));

        

        this.setBorder(new javax.swing.border.EmptyBorder(4,4,4,4));

        

    }

    

    public double[][] getInput() throws InvalidDataError{

        java.util.Vector indices=new java.util.Vector(12);

        boolean test=true;

        

        for(int i=0;i<fields[0].length;i++){

            if(!fields[0][i].getText().equals("") && !fields[1][i].getText().equals(""))

                indices.addElement(new Integer(i));

            if (fields[0][i].getText().equals("")&& !fields[1][i].getText().equals(""))    

            test=false;

            if (!fields[0][i].getText().equals("")&& fields[1][i].getText().equals(""))

            test=false;

            

        }

        

        if (test==false) {

        	test=true;

        	throw new InvalidDataError("Invalid model input!");

        }; 

        

        if(indices.size()==0)

            throw new InvalidDataError("No model is specified!");

        

        double[][] ret=new double[2][indices.size()];

        

        

        

        try{            

            for(int j=0;j<indices.size();j++){

                int index=((Integer)indices.elementAt(j)).intValue();

                ret[0][j]=Double.parseDouble(fields[0][index].getText()); //get text and change to a double value

                ret[1][j]=Double.parseDouble(fields[1][index].getText());

            }

        }

        catch(NumberFormatException e){

            System.out.println(e.getMessage());

            throw new InvalidDataError("Invalid input for experiment model!");

        }

        

        for(int i=0;i<indices.size();i++){

        	sum1=sum1+ret[0][i];

        	sum2=sum2+ret[1][i];

        	if (ret[0][i]<min1) min1=ret[0][i];

        	if (ret[1][i]<min2) min2=ret[1][i];

        	if ((ret[0][i]==0)&&(ret[1][i]>0)) throw new InvalidDataError("When expected value is 0, obtained value should not be positive.");

        }

        

        if (min1<0) {

        	min1=0;

        	throw new InvalidDataError("Expected values should not be negative.");

        	

        }

        

        if (min2<0) {

        	min2=0;

        	throw new InvalidDataError("Obtained values should not be negative.");

        	

        }

        

        if(sum1!=sum2) {

        	sum1=0;

        	sum2=0;

        	throw new InvalidDataError("The expected and obtained values should have the same sums!");

        	

        }

        

        return ret;

    }           



    public void actionPerformed(final java.awt.event.ActionEvent p1) {

        String cmd=p1.getActionCommand();

        

        if(cmd.equals("Clear model")){

        	reset();

        }

        



    }

    

    public void reset(){

        

        for(int i=0;i<fields.length;i++){

            for(int j=0;j<fields[i].length;j++){

                fields[i][j].setText("");

            }

        }

   }    

}