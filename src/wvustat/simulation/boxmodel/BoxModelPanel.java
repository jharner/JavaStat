/*
 * BoxModel.java
 *
 * Created on April 19, 2002, 10:50 AM
 */

package wvustat.simulation.boxmodel;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import wvustat.simulation.model.BoxModel;
import wvustat.util.*;
import wvustat.statistics.InvalidDataError;
/**
 *
 * @author  James Harner
 * @version 
 */
public class BoxModelPanel extends javax.swing.JPanel implements ChangeListener{
    private BoxModelInput modelInput;
    private DrawPanel drawPanel;
    private CIPanel ciPanel;
    private BoxModel boxModel;
    private JTabbedPane tabPane;
    private int lastIndex=0;
    private JLabel boxStat;

    /** Creates new BoxModel */
    public BoxModelPanel() {
        init();
    }

    public void init() {
        modelInput=new BoxModelInput();
        drawPanel=new DrawPanel();
        ciPanel=new CIPanel();
        
        tabPane=new JTabbedPane(SwingConstants.TOP);
        tabPane.addChangeListener(this);
        tabPane.add("Define box model", modelInput);
        tabPane.add("Draw from the box", drawPanel);
        tabPane.add("C.I.", ciPanel);
        
        this.setLayout(new GridBagLayout());
        this.add(tabPane, new GridBagConstraints(0,0,0,-1,1.0,1.0,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        
        boxStat=new JLabel("Box model not defined.");
        this.add(boxStat, new GridBagConstraints(0,1,0,0,1.0,0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2),2,2));
        boxModel=new BoxModel();
    }
    
    private void showBoxModelStat(){
        java.text.NumberFormat nf=java.text.NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        StringBuffer buf=new StringBuffer();
        buf.append("Box model average=");
        buf.append(nf.format(boxModel.getAverage()));
        buf.append(", SD="+nf.format(boxModel.getSD()));

        boxStat.setText(buf.toString());
    }
    
    public void stateChanged(javax.swing.event.ChangeEvent p1) {
        int index=tabPane.getSelectedIndex();        
        
        if(lastIndex==0 && index>0){
            try{
                double[][] boxes=modelInput.getInput();
                boxModel.setBoxes(boxes);
                drawPanel.setBoxModel(boxModel);
                ciPanel.setBoxModel(boxModel);
                showBoxModelStat();
            }
            catch(InvalidDataError e){
                JOptionPane.showMessageDialog(this,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                tabPane.setSelectedIndex(0);
                return;
            }
        }
        
        lastIndex=index;
    }
    
    public static void main(String[] args){
        JFrame jf=new AppFrame("Box Model");
        
        BoxModelPanel bma=new BoxModelPanel();
        bma.init();
        jf.getContentPane().add(bma);
        jf.pack();
        jf.show();
    }
    
}
