/*
 * ParamInitializer.java
 *
 * Created on February 22, 2002, 11:46 AM
 */

package wvustat.modules;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
/**
 *
 * @author  James Harner
 * @version 
 */
public class ParamInitializer extends javax.swing.JDialog implements java.awt.event.ActionListener{
    private Vector params;
    private JTextField[] inputs;
    private double[] values;
    private boolean isCanceled=false;
    private String formula;
    /** Creates new form ParamInitializer */
    public ParamInitializer(java.awt.Frame parent,boolean modal) {
        super (parent, modal);
        initComponents ();
        pack ();
        
        
    }
    
    public ParamInitializer(Frame parent, Vector params, String formula){
        super(parent, true);
        this.params=params;
        this.formula=formula;
        values=new double[params.size()];
        initComponents();
        pack();
        

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        }
        );
        
        jLabel1.setText("Parameter");
        jLabel1.setFont(new java.awt.Font ("Dialog", 1, 12));
        
        JLabel prompt1=new JLabel("Please specify the initial values for the following");
        JLabel prompt2=new JLabel("parameters in the formula '"+formula+"'");
        
        getContentPane().add(prompt1, new GridBagConstraints(0,0,0,1,0,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
        getContentPane().add(prompt2, new GridBagConstraints(0,1,0,1,0,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
        getContentPane().add(jLabel1, new GridBagConstraints(0,2,-1,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
        
        
        jLabel2.setText("Value");
        jLabel2.setFont(new java.awt.Font ("Dialog", 1, 12));
        
        getContentPane().add(jLabel2, new GridBagConstraints(1,2,0,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
        
        inputs=new JTextField[params.size()];
        for(int i=0;i<params.size();i++){
            JLabel jl=new JLabel(params.elementAt(i).toString()+"=");
            getContentPane().add(jl, new GridBagConstraints(0,i+3,-1,1,0.5,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
            JTextField jt=new JTextField(10);
            getContentPane().add(jt, new GridBagConstraints(1,i+3,0,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
            inputs[i]=jt;
        }
        
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,30,8));
        JButton okButton=new JButton("Ok");
        JButton cancelButton=new JButton("Cancel");
        
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        getContentPane().add(buttonPanel, new GridBagConstraints(0,params.size()+3,0,0,1.0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(20,2,2,2),6,0));
        JPanel panel=(JPanel)getContentPane();
        panel.setBorder(new javax.swing.border.EmptyBorder(20,10,20,10));
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible (false);
        dispose ();
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        Vector v=new Vector();
        v.addElement("A1");
        v.addElement("A2");
        v.addElement("A3");
        new ParamInitializer (new javax.swing.JFrame (), v, "A1*x^2+A2*x+A3").show ();
    }

    public void actionPerformed(java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Ok")){
            for(int i=0;i<values.length;i++){
                try{
                    values[i]=Double.parseDouble(inputs[i].getText());
                }
                catch(NumberFormatException err){
                    JOptionPane.showMessageDialog(this,"Invalid input for parameter "+params.elementAt(i).toString(),"Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        else{
            isCanceled=true;
        }
        
        setVisible(false);
        dispose();
    }
    
    public double[] getValues(){
        if(!isCanceled)
            return values;
        else 
            return null;
    }
        

    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration

}
