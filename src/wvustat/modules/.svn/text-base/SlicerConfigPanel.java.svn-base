/**
 * Author: Dajie Luo
 * Date: Jan 31, 2006
 */
package wvustat.modules;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class SlicerConfigPanel extends JPanel
{
    private JTextField groupCountInput;
    private JTextField overlapInput;

    public SlicerConfigPanel()
    {
        groupCountInput=new JTextField(4);
        overlapInput=new JTextField(4);
        
        GridBagLayout gridBagLayout=new GridBagLayout();

        setLayout(gridBagLayout);
        addLabelComponent(new JLabel("# of intervals:"),groupCountInput,gridBagLayout, this);
        addLabelComponent(new JLabel("Overlap fraction %:"), overlapInput, gridBagLayout, this);
    }

    public void setGroupCount(int groupCount)
    {
        groupCountInput.setText(Integer.toString(groupCount));
    }

    public void setOverlap(float overlap)
    {
        NumberFormat nf=new DecimalFormat("########.##");
        nf.setMaximumFractionDigits(2);

        overlapInput.setText(nf.format(overlap));
    }
    
    public int getGroupCount()
    {
    		int n;
    		try{
    			n = Integer.parseInt(groupCountInput.getText());
    		}catch(NumberFormatException e){
    			n = -1;
    		}
    		return n;
    }
    
    public float getOverlap()
    {
    		float f;
    		try{
    			f =  Float.parseFloat(overlapInput.getText());
    		}catch(NumberFormatException e){
    			f = -1;
    		}
    		return f;
    }

    public static void addLabelComponent(Component label, Component component, GridBagLayout gridbag, Container container)
    {

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(1, 1, 1, 5);
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(label, c);
        container.add(label);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridwidth = GridBagConstraints.REMAINDER;    //end row
        //c.fill = GridBagConstraints.HORIZONTAL;
        //c.weightx = 0.5;
        gridbag.setConstraints(component, c);
        container.add(component);
        c.weightx = 0.0;
    }
    
    public static void main(String[] args)
    {
    		JFrame fr=new JFrame("Test");
    		SlicerConfigPanel configPanel = new SlicerConfigPanel();
    		configPanel.setGroupCount(5);
    		configPanel.setOverlap(20.0f);
    		int option = JOptionPane.showOptionDialog(fr, configPanel,
                    "interval settings",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
         if (option == JOptionPane.OK_OPTION) {
             int groupCount = configPanel.getGroupCount();
             float overlap = configPanel.getOverlap();
             if (groupCount > 0 && overlap >0) {
                 System.out.println("groupCount="+groupCount+",Overlap="+overlap);
             } else
                 JOptionPane.showMessageDialog(fr, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
         }
    }

}
