/*
 * VariableChooser.java
 *
 * Created on December 21, 2001, 11:51 AM
 */

package wvustat.table;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.EtchedBorder;
import java.rmi.RemoteException;

import wvustat.interfaces.*;
/**
 *
 * @author  hxue
 * @version
 */
public class VariableChooser extends JDialog implements ActionListener, ListSelectionListener{
    public static final int Y_ONLY=0;
    public static final int X_Y=1;
    
    private DataSet dataSet;
    private String[] varNames, xVars, yVars, zVars;
    private int roleOption;
    private JList varList, xList, yList, zList;
    private boolean isCanceled=false;
    private JList selectedList;
    
    /** Creates new VariableChooser */
    public VariableChooser(Frame parent, String[] varNames, int option) {
        super(parent, "Choose variables", true);
        this.varNames=varNames;
        this.roleOption=option;
        
        DefaultListModel model=new DefaultListModel();
        for(int i=0;i<varNames.length;i++)
            model.addElement(varNames[i]);
        
        varList=new JList(model);
        xList=new JList(new DefaultListModel());
        //xList.setPreferredSize(new Dimension(100,60));
        yList=new JList(new DefaultListModel());
        //yList.setPreferredSize(new Dimension(100,60));
        zList=new JList(new DefaultListModel());
        
        varList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        xList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        yList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        zList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        initComponents();
        setSize(new Dimension(420,360));
        
        if(parent.isShowing()){
            Dimension d1=parent.getPreferredSize();
            Dimension d2=this.getPreferredSize();
            
            Point pt=parent.getLocationOnScreen();
            int tmpx=pt.x+d1.width/2-d2.width/2;
            int tmpy=pt.y+d1.height/2-d2.height/2;
            if(tmpx<0)
                tmpx=0;
            if(tmpy<0)
                tmpy=0;
            this.setLocation(tmpx, tmpy);
        }
    }
    
    public VariableChooser(Frame parent, DataSet dataSet, int option) {
        super(parent, "Choose variables", true);
        this.dataSet=dataSet;
        this.roleOption=option;
        
        /*try{*/
            Vector v1=dataSet.getVariables();
            Vector v2=dataSet.getYVariables();
            Vector v3=dataSet.getXVariables();
            Vector v4=dataSet.getZVariables();
            
            DefaultListModel model1=new DefaultListModel();
            DefaultListModel model2=new DefaultListModel();
            DefaultListModel model3=new DefaultListModel();
            DefaultListModel model4=new DefaultListModel();
            
            for(int i=0;i<v1.size();i++){
                Variable var=(Variable)v1.elementAt(i);
                if(!v2.contains(var) && !v3.contains(var) && !v4.contains(var)){
                    model1.addElement(var.getName());
                }
            }
            
            for(int i=0;i<v2.size();i++){
                Variable var=(Variable)v2.elementAt(i);
                model2.addElement(var.getName());
            }
            
            for(int i=0;i<v3.size();i++){
                Variable var=(Variable)v3.elementAt(i);
                model3.addElement(var.getName());
            }
            
            for(int i=0;i<v4.size();i++){
                Variable var=(Variable)v4.elementAt(i);
                model4.addElement(var.getName());
            }
            
            varList=new JList(model1);
            yList=new JList(model2);
            xList=new JList(model3);
            zList=new JList(model4);
            
            //xList.setPreferredSize(new Dimension(100,60));
            //yList.setPreferredSize(new Dimension(100,60));
            varList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            xList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            yList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            zList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
        /*}
        catch(RemoteException re){
        }*/
        
        
        initComponents();
        setSize(new Dimension(420,360));
       
        if(parent.isShowing()){
            
            Point pt=parent.getLocationOnScreen();
            this.setLocation(pt.x+40, pt.y+40);
        }
    }
    
    private void initComponents(){
        Container content=this.getContentPane();
        content.setLayout(new GridBagLayout());
        
        JLabel label1=new JLabel("Variables available");
        JLabel label2=new JLabel("X variables");
        JLabel label3=new JLabel("Y variables");
        JLabel label4=new JLabel("Z variables");
        
        JButton b1=new JButton(">>X>>");
        b1.addActionListener(this);
        JButton b2=new JButton(">>Y>>");
        b2.addActionListener(this);
        JButton b3=new JButton(">>Z>>");
        b3.addActionListener(this);
        
        JButton b4=new JButton("Remove");
        b4.addActionListener(this);
        
        JButton okButton=new JButton("Ok");
        okButton.addActionListener(this);
        JButton cancelButton=new JButton("Cancel");
        cancelButton.addActionListener(this);
        
        JPanel panel1=new JPanel(new GridBagLayout());
        panel1.add(label1, new GridBagConstraints(0,0,0,-1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        panel1.add(new JScrollPane(varList), new GridBagConstraints(0,1,0,0,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        
        JPanel panel2=new JPanel(new GridBagLayout());
        panel2.add(new JLabel("     "), new GridBagConstraints(0,0,0,1,0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));
        panel2.add(b1, new GridBagConstraints(0,1,0,1,0,0.3,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));
        panel2.add(b2, new GridBagConstraints(0,2,0,-1,0,0.3,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));
        panel2.add(b3, new GridBagConstraints(0,3,0,0,0,0.4,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));
        
        JPanel panel3=new JPanel(new GridBagLayout());
        panel3.add(label2, new GridBagConstraints(0,0,0,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        panel3.add(new JScrollPane(xList), new GridBagConstraints(0,1,0,3,1.0,0.3,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        panel3.add(label3, new GridBagConstraints(0,4,0,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        panel3.add(new JScrollPane(yList), new GridBagConstraints(0,5,0,3,1.0,0.3,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        panel3.add(label4, new GridBagConstraints(0,8,0,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        panel3.add(new JScrollPane(zList), new GridBagConstraints(0,9,0,3,1.0,0.3,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        panel3.add(b4, new GridBagConstraints(0,12,0,0,1.0,0.1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        
        
        
        JPanel panel4=new JPanel(new GridLayout(1,2,60,20));
        
        panel4.add(cancelButton);
        panel4.add(okButton);
        
        JPanel upperPanel=new JPanel(new GridBagLayout());
        upperPanel.add(panel1, new GridBagConstraints(0,0,1,-1,0.4,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        upperPanel.add(panel2, new GridBagConstraints(1,0,-1,-1,0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL, new Insets(2,2,2,2), 0, 0));
        upperPanel.add(panel3, new GridBagConstraints(2,0,0,-1,0.6,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        upperPanel.setBorder(new javax.swing.border.EtchedBorder(EtchedBorder.RAISED,Color.lightGray, Color.gray));
        
        content.add(upperPanel, new GridBagConstraints(0,0,0,-1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,2,2,2), 0, 0));
        
        content.add(panel4, new GridBagConstraints(0,1,0,0,1.0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2,40,2,40), 0, 0));
        
        
        if(roleOption!=X_Y){
            label3.setEnabled(false);
            xList.setEnabled(false);
            b3.setEnabled(false);
        }
        
        xList.addListSelectionListener(this);
        yList.addListSelectionListener(this);
        zList.addListSelectionListener(this);
        
    }
    
    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Ok")){
            this.dispose();
        }
        else if(cmd.equals("Cancel")){
            isCanceled=true;
            this.dispose();
        }
        else if(cmd.equals(">>Y>>")){
            ListSelectionModel model=varList.getSelectionModel();
            int index=model.getLeadSelectionIndex();
            
            if(index!=-1){
                DefaultListModel varModel=(DefaultListModel)varList.getModel();
                Object obj=varModel.remove(index);
                
                DefaultListModel yModel=(DefaultListModel)yList.getModel();
                yModel.insertElementAt(obj,yModel.getSize());
            }
        }
        else if(cmd.equals(">>X>>")){
            ListSelectionModel model=varList.getSelectionModel();
            int index=model.getLeadSelectionIndex();
            
            if(index!=-1){
                DefaultListModel varModel=(DefaultListModel)varList.getModel();
                Object obj=varModel.remove(index);
                
                DefaultListModel xModel=(DefaultListModel)xList.getModel();
                xModel.insertElementAt(obj,xModel.getSize());
            }
        }
        else if(cmd.equals(">>Z>>")){
            ListSelectionModel model=varList.getSelectionModel();
            int index=model.getLeadSelectionIndex();
            
            if(index!=-1){
                DefaultListModel varModel=(DefaultListModel)varList.getModel();
                Object obj=varModel.remove(index);
                
                DefaultListModel zModel=(DefaultListModel)zList.getModel();
                zModel.insertElementAt(obj,zModel.getSize());
            }
        }
        else if(cmd.equals("Remove")){
            ListSelectionModel model=null;
            int index=-1;
            
            model=selectedList.getSelectionModel();
            index=model.getLeadSelectionIndex();
            DefaultListModel listModel=(DefaultListModel)selectedList.getModel();
            if(index!=-1 && listModel.size()>0 ){                
                DefaultListModel varModel=(DefaultListModel)varList.getModel();
                Object obj=listModel.remove(index);
                
                varModel.insertElementAt(obj,varModel.getSize());
            }
            
        }
    }
    
    public String[] getXVariables(){
        Object[] array=((DefaultListModel)xList.getModel()).toArray();
        
        if(array==null || array.length==0)
            return null;
        
        String[] ret=new String[array.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=array[i].toString();
        
        return ret;
    }
    
    public String[] getYVariables(){
        Object[] array=((DefaultListModel)yList.getModel()).toArray();
        
        if(array==null || array.length==0)
            return null;
        
        String[] ret=new String[array.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=array[i].toString();
        
        return ret;
    }
    
    public String[] getZVariables(){
        Object[] array=((DefaultListModel)zList.getModel()).toArray();
        
        if(array==null || array.length==0)
            return null;
        
        String[] ret=new String[array.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=array[i].toString();
        
        return ret;
    }    
    
    public String[] getUndefinedVariables(){
        Object[] array=((DefaultListModel)varList.getModel()).toArray();
        
        if(array==null || array.length==0)
            return null;
        
        String[] ret=new String[array.length];
        for(int i=0;i<ret.length;i++)
            ret[i]=array[i].toString();
        
        return ret;
    }
    
    public boolean isCanceled(){
        return isCanceled;
    }
    
    
    public static void main(String[] args){
        String[] list={"Var1", "Var2", "Var3", "Var4", "Variable 5", "Variable 6"};
        
        JFrame jf=new JFrame();
        VariableChooser chooser=new VariableChooser(jf, list, VariableChooser.X_Y);
        chooser.show();
        if(!chooser.isCanceled()){
            String[] array=chooser.getYVariables();
            for(int i=0;i<array.length;i++)
                System.out.println(array[i]);
        }
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent p1) {
       selectedList=(JList)p1.getSource();
        
    }
    
}