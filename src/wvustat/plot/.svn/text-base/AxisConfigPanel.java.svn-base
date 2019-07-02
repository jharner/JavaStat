/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 8, 2002
 * Time: 9:57:48 AM
 * To change this template use Options | File Templates.
 */
package wvustat.plot;

import wvustat.modules.BaseAxisModel;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class AxisConfigPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static final int BASIC_MODEL=0;
    public static final int MANUAL_MODEL=1;

    private JTextField startValueInput, endValueInput;
    private JTextField incrementInput;
    private JTextField minorTickInput;
    private JCheckBox manualCheck;
    private int type;

    /**
     * @param type - Use MANUAL_MODEL when it is a data/plot linked module. Use BASIC_MODEL for module without data linking such as microarray module. 
     */
    public AxisConfigPanel(int type)
    {
        this.type=type;
        startValueInput=new JTextField(4);
        endValueInput=new JTextField(4);
        incrementInput=new JTextField(4);
        minorTickInput=new JTextField(4);

        manualCheck=new JCheckBox("Keep these settings when data change");

        GridBagLayout gridBagLayout=new GridBagLayout();

        setLayout(gridBagLayout);
        addLabelComponent(new JLabel("Starting value:"),startValueInput,gridBagLayout, this);
        addLabelComponent(new JLabel("End value:"), endValueInput, gridBagLayout, this);
        addLabelComponent(new JLabel("Increment:"), incrementInput, gridBagLayout, this);
        
        if(type==MANUAL_MODEL)
        {
        	addLabelComponent(new JLabel("Number of minor ticks:"), minorTickInput, gridBagLayout, this);
            add(manualCheck, new GridBagConstraints(0,4,0,0,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2),0,0));
        }
    }

    public void setAxisModel(BaseAxisModel model)
    {
        NumberFormat nf=new DecimalFormat("########.##");
        nf.setMaximumFractionDigits(2);

        startValueInput.setText(nf.format(model.getStartValue()));
        endValueInput.setText(nf.format(model.getEndValue()));
        incrementInput.setText(nf.format(model.getInterval()));
        if(model instanceof ManualAxisModel)
        {
            ManualAxisModel manualModel=(ManualAxisModel)model;
            minorTickInput.setText(nf.format(manualModel.getNumOfMinorTicks()));
            manualCheck.setSelected(manualModel.isManual());
        }
    }


    public BaseAxisModel getAxisModel()
    {
        BaseAxisModel model=null;

        try{
            if(type==BASIC_MODEL)
                model=new BaseAxisModel();
            else
                model=new ManualAxisModel();

            double d=Double.parseDouble(startValueInput.getText());
            model.setStartValue(d);
            d=Double.parseDouble(endValueInput.getText());
            model.setEndValue(d);
            d=Double.parseDouble(incrementInput.getText());
            model.setInterval(d);
            
            if(model.getStartValue() >= model.getEndValue() || model.getInterval() <= 0 || model.getNumOfIntervals() <= 0)
            	throw new NumberFormatException();

            // make sure the end value is consistent with interval.
            d = model.getStartValue();
            while (d < model.getEndValue())
        		d += model.getInterval();
            model.setEndValue(d);
            
            if(type==MANUAL_MODEL)
            {
                int n=0;
                if(!minorTickInput.getText().trim().equals(""))
                    n=Integer.parseInt(minorTickInput.getText());
                
                if(n < 0) throw new NumberFormatException();
                
                ((ManualAxisModel)model).setNumOfMinorTicks(n);
                ((ManualAxisModel)model).setManual(manualCheck.isSelected());
            }
            return model;
        }
        catch(NumberFormatException e)
        {
            return null;
        }
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

}
