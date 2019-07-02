/*
 * Created on Oct 8, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package wvustat.math.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import wvustat.math.expression.*;
import wvustat.swing.table.DataSet;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author rick holsberry
 * 
 *  
 */
public class ModelDialog implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	 
	private DataSet dataSet;
	private ExpressionObject model;
	private JDialog dialog;
	private String typeOfModel;
	 
	 //constructor which initializes the instance variables
	 public ModelDialog(ExpressionObject pModel, DataSet dataSet, String modelType)
	 {
	 	
	 	this.dataSet = dataSet;
	 	this.model = pModel;
	 	this.typeOfModel = modelType;
	 }
	 
	private JComponent createDialog()
	{
				//two lists are used to hold/manipulate the parameters
				List test, storeParameters;
				
				storeParameters = new ArrayList();
				//test = call to get the parameters
				test = model.getParameters();
				
				//pattern used to format the decimals on the JDialog
				DecimalFormat aFormatter = new DecimalFormat("#.####");
				
				//this for loop loops until all paramters are obtained
				for (int j=0; j<test.size(); j++)
					{
						Parameter p = (Parameter)test.get(j);
						String pDone = aFormatter.format(p.getValue());
						storeParameters.add(j,pDone);
					}
					
				
				//gets the plot data and stores in a multi-dimension array
				double[][] xy = dataSet.getPlotData();
				//datasetCount holds the actual number of rows in table
				int datasetCount = dataSet.getRowCount();
				
				//summed holds the sum of squares
				double summed = model.getSumOfSquares(xy[0], xy[1]);
				//summed1 holds the sum of squares divided by num of rows
				double summed1 = (summed / (double)datasetCount);
				//summed1 then is square rooted to obtain the standard deviation
				summed1 = Math.sqrt(summed1);

				//formatSS used to format the decimal of ssDone
				DecimalFormat formatSS = new DecimalFormat("#.##");
				String ssDone = formatSS.format(summed1);

				//create the JPanel in order to add it to the JDialog
				JPanel testing = new JPanel();
				testing.setLayout(new BoxLayout(testing, BoxLayout.Y_AXIS));
				testing.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

				//create the labels
				JLabel funcLabel = new JLabel();
				JLabel funcLabel2 = new JLabel();
				
				//here is where I determine what goes on the label
				//this could be made into a separate method
				if (typeOfModel == "Linear") 
				{
				funcLabel = new JLabel("Model: y=a+b*x");
				funcLabel2 = new JLabel("Function: y=" + storeParameters.get(0) + "+" + storeParameters.get(1) +"*x" );
				}
				else if (typeOfModel == "Quadratic")
					{
				funcLabel = new JLabel("Model: y=a+b*x+c*x^2");
				funcLabel2 = new JLabel("Function: y=" + storeParameters.get(0) + "+" + storeParameters.get(1) + "*x" + "+" + storeParameters.get(2) + "*x" + "^2" );
		
					}
				else if (typeOfModel == "Cubic")
                {
                funcLabel = new JLabel("Model: y=a+b*x+c*x^2+d*x^3");
                funcLabel2 = new JLabel("Function: y=" + storeParameters.get(0) + "+" + storeParameters.get(1) + "*x" + "+" + storeParameters.get(2) + "*x" + "^2" + "+" + storeParameters.get(3) + "*x" + "^3");
                }

				//this label never changes and doesn't need to be in the above if/else block
				JLabel sumofsquaresLabel = new JLabel("Standard Deviation =  " + ssDone);    
				
				JButton closeBttn=new JButton("Close");
        
				closeBttn.addActionListener(this);
				closeBttn.setActionCommand("CLOSE");
				
				//add everything to the JPanel
				testing.add(funcLabel);
				testing.add(funcLabel2);
				testing.add(sumofsquaresLabel);
				testing.add(closeBttn);
				//return the component to the calling method
				return testing;
		
	} 
	 
	public JDialog getDialog(Component comp, String title)
		{
		    //nice little trick to coerce a frame
		    //a dialog is created and after calling createDialog() it 
		    //returns the appropriate look to the calling object
		    Frame frame=(Frame)SwingUtilities.getWindowAncestor(comp);
			dialog=new JDialog(frame, title);
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(createDialog(), BorderLayout.CENTER);
			dialog.pack();
			return dialog;
		}
	 
	 //this method is simply for catching the actionPerformed events
	 //i.e. the close button
	public void actionPerformed(ActionEvent arg0) 
	{
		String cmd=arg0.getActionCommand();
		if(cmd.equals("CLOSE"))
			{
				dialog.dispose();
			}
		
	}

}
