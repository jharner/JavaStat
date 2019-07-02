/*
 * TransformDialog.java
 *
 * Created on Feb 23, 2006, 15:05 PM
 */

package wvustat.modules;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 *
 * @author  djluo
 * @version
 */
public class TransformDialog extends JDialog implements ActionListener{
    private Transformation xform;
    private JRadioButton btn1,btn2,btn3,btn4,btn5;
    private JTextField sqrtField, logField;
    private String varName;
    private char role = 'x';
    
    /** Creates new TransformDialog */
    public TransformDialog(Frame parent, String varName) {
        super(parent,"Transformation", true);
        this.varName=varName;
        //this.setBackground(Color.white);
        initComponents();
        pack();
    }
    
    public TransformDialog(Frame parent, String varName, char role) {
        super(parent,"Transformation", true);
        this.varName=varName;
        this.role=role;
        //this.setBackground(Color.white);
        initComponents();
        pack();
    }

    private void initComponents(){
        Container content=this.getContentPane();
        content.setLayout(new GridBagLayout());
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        JLabel varLabel=new JLabel("Variable '"+varName+"'");
        varLabel.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(varLabel, new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        JLabel label=new JLabel("Transformation methods: ");
        //label.setFont(new Font("Arial",Font.BOLD,12));
        mainPanel.add(label, new GridBagConstraints(0,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));

        JPanel choicePanel=new JPanel();
        choicePanel.setLayout(new GridBagLayout());

        ButtonGroup group=new ButtonGroup();
        /*btn1=new JRadioButton("x^2",false);
        btn2=new JRadioButton("sqrt(x+c)",false);
        btn3=new JRadioButton("log(x+c)",false);
        btn4=new JRadioButton("1/x",false);
        btn5=new JRadioButton("None", false);*/
        
        btn1=new JRadioButton(role+"^2",false);
        btn2=new JRadioButton("sqrt("+role+"+c)",false);
        btn3=new JRadioButton("log("+role+"+c)",false);
        btn4=new JRadioButton("1/"+role,false);
        btn5=new JRadioButton("None", false);
        
        group.add(btn1);
        group.add(btn2);
        group.add(btn3);
        group.add(btn4);
        group.add(btn5);

        choicePanel.add(btn1,new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn2,new GridBagConstraints(0,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn3,new GridBagConstraints(0,2,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn4,new GridBagConstraints(0,3,0,-1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        choicePanel.add(btn5,new GridBagConstraints(0,4,0,0,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        
        JPanel constPanel=new JPanel();
        sqrtField=new JTextField("0",3);
        logField=new JTextField("0", 3);
        
        constPanel.setLayout(new GridBagLayout());
        constPanel.add(new JLabel(""),new GridBagConstraints(0,0,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));    
        constPanel.add(new JLabel("( c="),new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));        
        constPanel.add(sqrtField,new GridBagConstraints(1,1,-1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        constPanel.add(new JLabel(")"),new GridBagConstraints(2,1,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0)); 
        
        constPanel.add(new JLabel("( c="),new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        constPanel.add(logField,new GridBagConstraints(1,2,-1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));  
        constPanel.add(new JLabel(")"),new GridBagConstraints(2,2,0,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0)); 
        
        constPanel.add(new JLabel(""),new GridBagConstraints(0,3,0,-1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));  
        constPanel.add(new JLabel(" "),new GridBagConstraints(0,4,0,0,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2),0,0)); 

        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,16,24));
        JButton okButton=new JButton("Ok");
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        JButton cancelButton=new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(choicePanel,new GridBagConstraints(0,2,-1,-1,0.5,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        mainPanel.add(constPanel,new GridBagConstraints(1,2,0,-1,0.5,1,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),2,2));
        mainPanel.add(buttonPanel,new GridBagConstraints(0,3,0,0,1,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));

        content.add(mainPanel,new GridBagConstraints(0,0,0,0,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(10,10,10,10),8,8));
    }



    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();

        if(cmd.equals("Cancel")){
            this.dispose();
        }
        else if(cmd.equals("Ok")){
            int xId=-1;
            if(btn1.isSelected())
            xId=Transformation.SQUARE;
            else if(btn2.isSelected())
            xId=Transformation.SQUARE_ROOT;
            else if(btn3.isSelected())
            xId=Transformation.LOGRITHMIC;
            else if(btn4.isSelected())
            xId=Transformation.INVERSE;
            else if(btn5.isSelected())
            xId=Transformation.CANONICAL;

            if(xId>-1)
            xform=new Transformation(xId);

            if(xId==Transformation.SQUARE_ROOT || xId==Transformation.LOGRITHMIC){
                String input=null;
                if(xId==Transformation.SQUARE_ROOT)
                    input=sqrtField.getText();
                else if(xId==Transformation.LOGRITHMIC)
                    input=logField.getText();
                if(input!=null){
                    try{
                        double val=new Double(input).doubleValue();
                        xform.setConstant(val);
                    }
                    catch(NumberFormatException e){
                        xform=null;
                    }
                }
                
            }


            this.dispose();
        }

    }

    public Transformation getTransformation(){
        return xform;
    }

    public static Transformation showTransformDialog(Frame parent, String varName){
        TransformDialog td=new TransformDialog(parent,varName);
        //td.pack();

        Point pt=parent.getLocationOnScreen();
        Dimension d=parent.getSize();
        td.setLocation(pt.x+d.width/2-td.getSize().width/2, pt.y+d.height/2-td.getSize().height/2);

        td.show();
        return td.getTransformation();
    }
    
    public static Transformation showTransformDialog(Frame parent, String varName, char role){
        TransformDialog td=new TransformDialog(parent,varName,role);
        //td.pack();

        Point pt=parent.getLocationOnScreen();
        Dimension d=parent.getSize();
        td.setLocation(pt.x+d.width/2-td.getSize().width/2, pt.y+d.height/2-td.getSize().height/2);

        td.show();
        return td.getTransformation();
    }

    public static void main(String[] args){
        JFrame f=new JFrame();
        TransformDialog td=new TransformDialog(f,"var");
        //td.pack();
        td.show();
    }
}