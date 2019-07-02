/*
 * BoxModelInput.java
 *
 * Created on March 12, 2002, 2:02 PM
 */

package wvustat.simulation.boxmodel;

import java.awt.*;
import javax.swing.*;

import wvustat.util.*;
import wvustat.simulation.model.BoxModel;
import wvustat.plot.Histogram;
import wvustat.simulation.fivestep.*;
import wvustat.statistics.InvalidDataError;
/**
 *
 * @author  Hengyi Xue
 * @version 
 */
public class BoxModelInput extends JComponent implements java.awt.event.ActionListener{
    private JTextField[][] fields;
    private JLabel label1, label2, label3, label4;

    /** Creates new BoxModelInput */
    public BoxModelInput() {
        setBackground(Color.lightGray);
        fields=new JTextField[2][30];
        for(int i=0;i<fields.length;i++){
            for(int j=0;j<fields[i].length;j++){
                if(i==0)
                fields[i][j]=new IntOnlyField("1",4);
                else
                fields[i][j]=new JTextField(4);
            }
        }
        
        initComponents();
    }
    
    
    
    private void initComponents(){
        JPanel inputPanel=new JPanel();
        inputPanel.setLayout(new GridLayout(16,4,2,2));
        label1=new JLabel("Count");
        label2=new JLabel("Value");
        label3=new JLabel("Count");
        label4=new JLabel("Value");
        inputPanel.add(label1);
        inputPanel.add(label2);
        inputPanel.add(label3);
        inputPanel.add(label4);
        
        
        for(int i=0;i<30;i++){
            for(int j=0;j<2;j++){
                inputPanel.add(fields[j][i]);
            }
        }
        
        JPanel buttonPanel=new JPanel(new GridBagLayout());
        JButton button1=new JButton("Show boxes");
        JButton button2=new JButton("Histogram");
        button1.addActionListener(this);
        button2.addActionListener(this);
        buttonPanel.add(button1, new GridBagConstraints(0,0,-1,0,0.2,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,4,2,4), 0, 0));
        buttonPanel.add(button2, new GridBagConstraints(1,0,0,0,0.2,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,4,2,4), 0, 0));
        
        this.setLayout(new GridBagLayout());
     
        
        //this.add(title, new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        this.add(inputPanel, new GridBagConstraints(0,0,0,-1,1.0,1.0,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        this.add(buttonPanel, new GridBagConstraints(0,1,0,0,1.0,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));
        
        this.setBorder(new javax.swing.border.EmptyBorder(4,4,4,4));
        
    }
    
    public double[][] getInput() throws InvalidDataError{
        java.util.Vector indices=new java.util.Vector(16);
        
        for(int i=0;i<fields[0].length;i++){
            if(!fields[0][i].getText().equals("") && !fields[1][i].getText().equals("")){
                indices.addElement(new Integer(i));
            }
        }
        
        if(indices.size()==0)
            throw new InvalidDataError("No box is specified!");
        
        double[][] ret=new double[2][indices.size()];
        
        try{            
            for(int j=0;j<indices.size();j++){
                int index=((Integer)indices.elementAt(j)).intValue();
                ret[0][j]=Integer.parseInt(fields[0][index].getText());
                ret[1][j]=Double.parseDouble(fields[1][index].getText());
            }
        }
        catch(NumberFormatException e){
            System.out.println(e.getMessage());
            throw new InvalidDataError("Invalid input for box model!");
        }
        
        return ret;
    }           

    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Show boxes")){
            try{                
                BoxDialog bDialog=new BoxDialog(ComponentUtil.getTopComponent(this),this.getInput());
                               
                bDialog.show();
            }
            catch(InvalidDataError err){
                System.out.println(err);
            }                            
        }
        else if(cmd.equals("Histogram")){
            try{
                BoxModel boxModel=new BoxModel();
                double[][] input=this.getInput();
                boxModel.setBoxes(input);
                double[] cards=boxModel.getCards();
                
                Histogram plot=new Histogram();
                plot.setData(cards);
                
                JDialog jd=new JDialog(ComponentUtil.getTopComponent(this),"Histogram", true);                
                jd.getContentPane().add(plot, BorderLayout.CENTER);
                jd.pack();
                ComponentUtil.setLocationRelativeTo(jd, jd.getOwner());
                
                jd.show();
            }
            catch(InvalidDataError err){
                System.out.println(err);
            }                
        }
    }
}