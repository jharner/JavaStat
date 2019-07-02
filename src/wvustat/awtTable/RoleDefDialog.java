/*
 * RoleDefDialog.java
 *
 * Created on March 21, 2002, 10:54 AM
 */

package wvustat.awtTable;

import java.awt.*;
import java.awt.event.*;

import wvustat.plotUtil.MyGridBagConstraints;
/**
 *
 * @author  hxue
 * @version 
 */
public class RoleDefDialog extends Dialog implements ActionListener{
    private Choice xChoice, yChoice;
    private TableModel tableModel;
    
    /** Creates new RoleDefDialog */
    public RoleDefDialog(Frame parent, TableModel tableModel) {
        super(parent, true);
        this.tableModel=tableModel;
        setTitle("Define roles");
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
        Label label1=new Label("Choose x");
        Label label2=new Label("Choose y");
        
        xChoice=new Choice();
        yChoice=new Choice();
        for(int i=0;i<tableModel.getColumnCount();i++){
            xChoice.addItem(tableModel.getColumnName(i));
            yChoice.addItem(tableModel.getColumnName(i));
        }
        
        Button ok=new Button("Ok");
        Button cancel=new Button("Cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);        
        
        Container content=new Panel();
        content.setLayout(new GridBagLayout());
        
        content.add(label1, new MyGridBagConstraints(0,0,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        content.add(xChoice, new MyGridBagConstraints(0,1,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        content.add(label2, new MyGridBagConstraints(0,2,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        content.add(yChoice, new MyGridBagConstraints(0,3,0,1,0,0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
        
        Panel bPanel=new Panel();
        bPanel.setLayout(new GridLayout(1,2,20,4));
        bPanel.add(cancel);
        bPanel.add(ok);
        
        content.add(bPanel, new MyGridBagConstraints(0,4,0,0,0.2,0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20,2,2,2), 0, 0));
 
        this.setLayout(new GridBagLayout());
        this.add(content, new MyGridBagConstraints(0,0,0,0,1.0,1.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10,30,10,30), 0, 0));        
    }
    
    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        String cmd=p1.getActionCommand();
        
        if(cmd.equals("Ok")){
            tableModel.setXRole(xChoice.getSelectedIndex());
            tableModel.setYRole(yChoice.getSelectedIndex());
        }
        
        dispose();
            
    }
}