/*
 * ColumnDefDialog.java
 *
 * Created on March 21, 2002, 10:10 AM
 */

package wvustat.awtTable;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import wvustat.plotUtil.MyGridBagConstraints;
import wvustat.math.expression.ExpressionParser;
import wvustat.math.expression.Expression;
/**
 *
 * @author  Hengyi Xue
 * @version 
 */
public class ColumnDefDialog extends Dialog implements ActionListener, ItemListener{
    private Checkbox check1, check2;
    private CheckboxGroup checkGroup;
    private TextField formulaBar;
    private Choice columnChoice;
    
    private TableModel tableModel;
    /** Creates new ColumnDefDialog */
    public ColumnDefDialog(Frame parent, TableModel tableModel) {
        super(parent, true);
        this.tableModel=tableModel;
        setTitle("New Column");
        setBackground(Color.white);
        initComponent();
        pack();
        
        if(parent.isShowing()){
            Point pt=parent.getLocationOnScreen();
            Dimension d=parent.getSize();
            setLocation(pt.x+d.width/2-getSize().width/2, pt.y+d.height/2-getSize().height/2);
        }        
    }
    
    private void initComponent(){
        checkGroup=new CheckboxGroup();
        
        check1=new Checkbox("Transformation", false, checkGroup);
        check2=new Checkbox("Finite Difference", true, checkGroup);
        check1.addItemListener(this);
        check2.addItemListener(this);
        
        formulaBar=new TextField(12);
        formulaBar.setEnabled(false);
        columnChoice=new Choice();
        for(int i=0;i<tableModel.getColumnCount();i++){
            columnChoice.addItem(tableModel.getColumnName(i));
        }
        
        Button ok=new Button("Ok");
        Button cancel=new Button("Cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);
        
        Container content=new Panel();
        content.setLayout(new GridBagLayout());
        
        content.add(check1, new MyGridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        content.add(formulaBar, new MyGridBagConstraints(0,1,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        content.add(check2, new MyGridBagConstraints(0,2,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        content.add(columnChoice, new MyGridBagConstraints(0,3,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        
        Panel bPanel=new Panel();
        bPanel.setLayout(new GridLayout(1,2,20,4));
        bPanel.add(cancel);
        bPanel.add(ok);
        
        content.add(bPanel, new MyGridBagConstraints(0,4,0,0,0.2,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20,2,2,2), 0, 0));
 
        this.setLayout(new GridBagLayout());
        this.add(content, new MyGridBagConstraints(0,0,0,0,1.0,1.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,30,10,30), 0, 0));
    }
    
    public void actionPerformed(ActionEvent evt){
        String cmd=evt.getActionCommand();        
        
        if(cmd.equals("Cancel")){
            this.dispose();
            return;
        }
        
        if(check2.getState()){
            int index=columnChoice.getSelectedIndex();
            String name=columnChoice.getSelectedItem();
            double[] colData=tableModel.getColumnData(index);
            double[] diff=finiteDiff(colData);
            tableModel.addColumn(name+"-diff", diff);                        
        }
        else if(check1.getState()){
            String input=formulaBar.getText();
            try{
            	   ExpressionParser ep = new ExpressionParser();
                Expression expr=ep.parse(input);
                Vector v=expr.getVariableNames();
                String colName=v.elementAt(0).toString();
                double[] src=tableModel.getColumnData(colName);
                Hashtable ht=new Hashtable();
                double[] newData=new double[tableModel.getRowCount()];
                for(int i=0;i<newData.length;i++){
                    ht.put(colName, new Double(src[i]));
                    newData[i]=expr.value(ht);
                }
                
                tableModel.addColumn(input, newData);
            }
            catch(Exception e){
                System.out.println(e);
            }
            catch(Error e){
                System.out.println(e);
            }
        }
        
        this.dispose();
    }
    
    private double[] finiteDiff(double[] src){
        double[] ret=new double[src.length];
        
        ret[0]=Double.NaN;
        for(int i=1;i<ret.length;i++){
            ret[i]=src[i]-src[i-1];
        }
        
        return ret;
    }

    public void itemStateChanged(final java.awt.event.ItemEvent p1) {
        formulaBar.setEnabled(check1.getState());
    }
}