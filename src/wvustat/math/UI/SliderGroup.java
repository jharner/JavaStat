/*
 * SliderGroup.java
 *
 * Created on November 5, 2001, 11:57 AM
 */

package wvustat.math.UI;

import wvustat.math.expression.ExpressionObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
/**
 *
 * @author  hxue
 * @version
 */
public class SliderGroup extends JDialog implements ActionListener, ChangeListener {
    private ExpressionObject function;
    private Slider[] sliders;
    private JLabel[] labels;
    private int min=-10, max=10;
    private Vector changeListeners;    

    /** Creates new SliderGroup */
    public SliderGroup(Frame parent, String title) {
        super(parent);
        setTitle(title);
        setModal(false);
        setBackground(Color.white);
    }

    public void setFunction(ExpressionObject function){
        this.function=function;
        initComponents();
    }
    
    public void addChangeListener(ChangeListener listener){
        if(changeListeners==null)
        changeListeners=new Vector();

        changeListeners.addElement(listener);
    }

    public void removeChangeListener(ChangeListener listener){
        changeListeners.remove(listener);
    }

    protected void fireChangeEvent(ChangeEvent evt){
        if(changeListeners==null)
        return;

        for(int i=0;i<changeListeners.size();i++)
        ((ChangeListener)changeListeners.elementAt(i)).stateChanged(evt);
    }

    private void initComponents(){
        Map varsTable=function.getVariables();
        int n=varsTable.size()-1;

        sliders=new Slider[n];
        labels=new JLabel[n];
        String[] array=new String[n];

        Iterator iter=varsTable.keySet().iterator();

        java.text.NumberFormat nf=java.text.NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);


        int index=0;
        while(iter.hasNext()){
            String var=iter.next().toString();
            if(!var.equals("x")){
                array[index]=var;
                index++;
            }
        }

        sortArray(array);

        for(int i=0;i<array.length;i++){
            double value=((Double)varsTable.get(array[i])).doubleValue();
            sliders[i]=new Slider(min, max, (float)value);            
            sliders[i].addChangeListener(this);
            labels[i]=new JLabel(array[i]+"= "+nf.format(value));
            labels[i].setFont(new Font("Serif", Font.PLAIN, 14));
            labels[i].setForeground(new Color(102,102,153));
        }



        JButton closeButton=new JButton("Close");
        closeButton.addActionListener(this);

        JPanel content=(JPanel)this.getContentPane();
        content.setLayout(new GridBagLayout());
        
        JLabel exprLabel=new JLabel("y="+function.getUnparsedForm());
        exprLabel.setFont(new Font("Arial", Font.BOLD, 14));
        content.add(exprLabel, new GridBagConstraints(0,0,0,1,0,1.0/(n+2), GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 2,2));
        
        for(int i=0;i<n;i++){
            content.add(labels[i], new GridBagConstraints(0,2*i+1,0,1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0,0));
            content.add(sliders[i], new GridBagConstraints(0,2*i+2,0,1,1.0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,2,2,2),0,0));
        }

        JPanel subPanel=new JPanel();
        subPanel.setLayout(new GridBagLayout());
        subPanel.add(closeButton, new GridBagConstraints());

        content.add(subPanel, new GridBagConstraints(0,n*2+1,0,0,1.0,1.0/(n+2), GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4,4,4,4), 0, 0));
        content.setBorder(new EmptyBorder(10,20,10,10));

        Component parent=this.getParent();
        Point pt=parent.getLocationOnScreen();
        Dimension d1=parent.getSize();
        this.pack();
        Dimension d2=this.getSize();
        this.setLocation(pt.x+d1.width/2-d2.width/2, pt.y+d1.height/2-d2.height/2);
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        this.dispose();
    }

    public void stateChanged(final ChangeEvent p1) {
        Object src=p1.getSource();

        int index=0;

        while(index<sliders.length && sliders[index]!=src){
            index++;
        }

        if(index<sliders.length){
            java.text.NumberFormat nf=java.text.NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(3);

            String str=labels[index].getText();
            str=str.substring(0,str.indexOf('=')+1);
            labels[index].setText(str+" "+nf.format(sliders[index].getValue()));
            //varsTable.put(str.substring(0,str.indexOf('=')),new Double(sliders[index].getValue()));
            function.setVariableValue(str.substring(0,str.indexOf('=')),sliders[index].getValue());
            ChangeEvent evt=new ChangeEvent(this);
            fireChangeEvent(evt);
        }
    }

    private void sortArray(String[] array){
        String key;
        boolean inserting;

        for(int i=1;i<array.length;i++){
            key=array[i];
            int j=0;
            inserting=false;
            while(j<i && inserting==false){
                if(array[j].compareTo(key)>0)
                inserting=true;
                else
                j++;
            }

            if(inserting){
                for(int k=i;k>j;k--)
                array[k]=array[k-1];
                array[j]=key;
            }
        }
    }

}