package wvustat.modules;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.math.expression.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.statistics.InvalidDataError;
import wvustat.statistics.NonlinearRegression;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 *	RegressionModule is a bundled component that is used to display regression analysis graphically. It contains
 *	severall other simpler components like ScatterPlot, RegressionRpt.
 *
 */
public class RegressionModule extends GUIModule implements ActionListener, ChangeListener{
    
    private Variable y_var, x_var,z_var;
    
    private Vector vz;
    
    private ScatterPlot plot;
    
    private RegressionRpt report;
    
    private Palette palette;
    
    //private GroupChooser chooser;
    private OverlapSlicer chooser;
    
    private JMenuBar jmb;
    
    private JMenu optionMenu;
    
    private JCheckBoxMenuItem ciMI, ellipseMI, moveMI;
    
    private JMenuItem colorMI, resetMI, fitMI, /*spanMI,*/ xformMI;
    
    //private String lastArgument="";
    private JRadioButtonMenuItem fit0,fit1,fit2,fit3,fit4; //added by djluo
    
    private JPanel rptPanel;
    private CardLayout cardLayout;
    private JTextPane nlRpt, momentsRpt, loessRpt;
    private JTextPane regRpt;
    //private String expressionInput;
    
    private Vector xformMIList = new Vector();
    private Hashtable xformx_table = new Hashtable(), xformy_table = new Hashtable();
    
    /**
     * Constructor
     * Creates a new RegressionModule with the given data set and its variables
     */
    public RegressionModule(DataSet data, Variable y_var, Variable x_var, Variable z_var) throws IllegalArgumentException{
        this.data=data;
        this.y_var=y_var;
        this.x_var=x_var;
        this.z_var=z_var;
        
        this.vz = new Vector();
        this.vz.addElement(z_var);

        if (x_var == null)
            throw new IllegalArgumentException("Can't do regression analysis without x variable");
        else if (x_var.getType() != Variable.NUMERIC)
            throw new IllegalArgumentException("X variable cannot be categorical");

        if (y_var == null)
            throw new IllegalArgumentException("Can't do regression analysis without y variable");
        else if (y_var.getType() != Variable.NUMERIC)
            throw new IllegalArgumentException("Y variable can not be categorical");

        init();
    }

    /**
     * Constructor
     * Creates a new RegressionModule with the given data set and its variables
     */
    public RegressionModule(DataSet data, Variable y_var, Variable x_var, Vector vz) throws IllegalArgumentException{
        this.data=data;
        this.y_var=y_var;
        this.x_var=x_var;
        this.vz = vz;
        
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        
        if (x_var == null)
            throw new IllegalArgumentException("Can't do regression analysis without x variable");
        else if (x_var.getType() != Variable.NUMERIC)
            throw new IllegalArgumentException("X variable cannot be categorical");

        if (y_var == null)
            throw new IllegalArgumentException("Can't do regression analysis without y variable");
        else if (y_var.getType() != Variable.NUMERIC)
            throw new IllegalArgumentException("Y variable can not be categorical");


        init();
    }    
    
    /**
     * Constructor
     * Creats a new RegressionModule with the given data set. The data set must ensure that it contains
     * numerical y variable and x variable.
     *
     */
    public RegressionModule(DataSet data) throws IllegalArgumentException{
        this.data=data;

        Vector v = data.getYVariables();
        y_var = (Variable) v.elementAt(0);
        v = data.getXVariables();
        x_var = (Variable) v.elementAt(0);

        vz = data.getZVariables();
        if (vz.size() > 0)
            z_var = (Variable) vz.elementAt(0);

        init();
    }
    
    public RegressionModule(DataSet data, PlotMetaData pmd){
        this.data=data;
        
        this.metaData=pmd;
        
        y_var=metaData.getYVariable();
        x_var=metaData.getXVariable();
        z_var=metaData.getZVariable();

        this.vz = new Vector();
        this.vz.addElement(z_var);
        
        init();
    }
    
    public void reset(){
        plot.resetPlot();
        report.resetReport();
    }
    
    public void setAnalysisMode(String arg){
        
        if(arg.equals("LS Fit")){
            fit1.setSelected(true);
            cardLayout.show(rptPanel, "1");
            plot.showRegression();
            report.showReport(RegressionRpt.REGRESSION);
            ciMI.setEnabled(true);
            ellipseMI.setSelected(false);
            ellipseMI.setEnabled(false);
        }
        else if(arg.equals("Correlation")){
            fit2.setSelected(true);
            cardLayout.show(rptPanel, "2");
            plot.showCorrelation();
            plot.enableEllipse(true);
            report.showReport(RegressionRpt.CORRELATION);
            ciMI.setSelected(false);
            ciMI.setEnabled(false);
            ellipseMI.setEnabled(true);
            ellipseMI.setSelected(true);
        }
        
    }
    
    public void enableCI(Boolean bl){
        plot.enableCI(bl.booleanValue());
    }
    
    public void enableEllipse(Boolean bl){
        plot.enableEllipse(bl.booleanValue());
    }
    
    public void movePoint(String arg){
        int index;
        double x, y;
        
        int tmpInt=arg.indexOf(':');
        String val=arg.substring(tmpInt+1, arg.indexOf(','));
        index=Integer.parseInt(val);
        val=arg.substring(arg.indexOf(',')+1,arg.lastIndexOf(','));
        x=Double.parseDouble(val);
        val=arg.substring(arg.lastIndexOf(',')+1);
        y=Double.parseDouble(val);
        
        plot.movePoint(index, x, y);
        
        report.setRegressionStatistics(plot.getRegressionStatistics(), false);
    }
    
    public void moveScanner(String arg){
        double val=Double.parseDouble(arg.substring(arg.indexOf(':')+1));
        plot.moveScannerLine(val);
    }
    
    /**
     * Initialize the component
     */
    private void init(){
        
        data.addRemoteObserver(this);
      
        this.setBackground(Color.white);
        plot=new ScatterPlot(data, y_var, x_var, vz);
        plot.setGUIModule(this);
        
        //plot.addChangeListener(this);
        plot.addActionListener(this);
        
        setLayout(new BorderLayout());
        
        //Add control panel to this component
        JPanel controlPanel=buildControlPanel();
        if(controlPanel!=null){
            //add(controlPanel, new GridBagConstraints(0,0,0,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,8,0,0),0,0));

            add(controlPanel, BorderLayout.NORTH);
        }
        
        //Add plot panel to this component
        JPanel plotPanel=buildPlotPanel();

        //Add report panel to this component
        rptPanel=buildReportPanel();

        rptPanel.setPreferredSize(new Dimension(320,120));


        JSplitPane splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT, plotPanel, rptPanel);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(1);
        add(splitPane, BorderLayout.CENTER);

        jmb=new JMenuBar();
        
        
        JMenu plotMenu=new JMenu("Plot");
        
        ButtonGroup group=new ButtonGroup();
        
        JRadioButtonMenuItem jmi=new JRadioButtonMenuItem("Scatter Plot");
        jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), true);
        plotMenu.add(jmi);
        
        optionMenu=new JMenu("Options");
        
        ciMI=new JCheckBoxMenuItem("95% Confidence Limits", false);
        ciMI.setEnabled(false);
        ellipseMI=new JCheckBoxMenuItem("95% Correlation Ellipse", false);
        ciMI.addActionListener(this);
        ellipseMI.addActionListener(this);
        
        colorMI=new JMenuItem("Color...");
        resetMI=new JMenuItem("Reset Points");
        colorMI.addActionListener(this);
        resetMI.addActionListener(this);
        
        optionMenu.add(ciMI);
        optionMenu.add(ellipseMI);
        optionMenu.addSeparator();
        
        moveMI=new JCheckBoxMenuItem("Move Points", false); // not allow dragging points initially
        plot.enablePointDrag(moveMI.getState());           
        moveMI.addActionListener(this);
        optionMenu.add(moveMI);
        
        optionMenu.add(resetMI);
        optionMenu.addSeparator();
        JMenuItem rescaleX=optionMenu.add("Rescale X Axis...");
        rescaleX.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(plot.getXAxisModel());
                int option = JOptionPane.showOptionDialog(RegressionModule.this, configPanel,
                        "x-axis settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if (option == JOptionPane.OK_OPTION)
                {
                    BaseAxisModel model = configPanel.getAxisModel();
                    if (model != null)
                    {
                        plot.setXAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(RegressionModule.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem rescaleY=optionMenu.add("Rescale Y Axis...");
        rescaleY.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(plot.getYAxisModel());
                int option = JOptionPane.showOptionDialog(RegressionModule.this, configPanel,
                        "y-axis settings",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if (option == JOptionPane.OK_OPTION)
                {
                    BaseAxisModel model = configPanel.getAxisModel();
                    if (model != null)
                    {
                        plot.setYAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(RegressionModule.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        optionMenu.addSeparator();
        optionMenu.add(colorMI);
        
        optionMenu.addSeparator();
        /*spanMI = new JMenuItem("Set Smoothing...");
        spanMI.addActionListener(this);
        spanMI.setEnabled(false);
        optionMenu.add(spanMI);*/
        
        fitMI=new JMenuItem("New Fit");
        fitMI.addActionListener(this);
        fitMI.setEnabled(false);
        optionMenu.add(fitMI);
        
        xformMI = new JMenuItem("Transform...");
        xformMI.addActionListener(this);
        optionMenu.addSeparator();
        optionMenu.add(xformMI);
        
        //plotMenu.add(optionMenu);
        jmb.add(plotMenu);
        
        JMenu analyzeMenu=new JMenu("Analyze");
        
        /*JRadioButtonMenuItem*/ fit0=new JRadioButtonMenuItem("Moments",true);
        /*JRadioButtonMenuItem*/ fit1=new JRadioButtonMenuItem("LS Fit", false);
        /*JRadioButtonMenuItem*/ fit2=new JRadioButtonMenuItem("Correlation", false);
        /*JRadioButtonMenuItem*/ fit3=new JRadioButtonMenuItem("Nonlinear Fit", false);
        fit4 = new JRadioButtonMenuItem("Loess Fit", false);
        
        ButtonGroup group2=new ButtonGroup();
        
        group2.add(fit0);
        group2.add(fit2);
        group2.add(fit1);
        group2.add(fit4);
        group2.add(fit3);
        fit0.addActionListener(this);
        fit1.addActionListener(this);
        fit2.addActionListener(this);
        fit3.addActionListener(this);
        fit4.addActionListener(this);
        
        analyzeMenu.add(fit0);
        analyzeMenu.addSeparator();
        analyzeMenu.add(fit2);
        analyzeMenu.addSeparator();
        analyzeMenu.add(fit1);
        analyzeMenu.add(fit4);
        analyzeMenu.add(fit3);
        
        jmb.add(analyzeMenu);
        
    }
    
    /**
     * Get the menu bar contained in this component
     */
    public JMenuBar getJMenuBar(){
        return jmb;
    }
    
    public JMenu getOptionMenu()
    {
    	return optionMenu;
    }
    
    private JPanel buildControlPanel(){
        if(z_var==null)
            return null;
        
        JPanel jp=new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        chooser=new OverlapSlicer(plot.getGroupMaker());
        chooser.addChangeListener(this);
        chooser.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        jp.add(chooser);
        
        return jp;
    }
    
    private JPanel buildPlotPanel(){
        return plot;
    }
    
    private JPanel buildReportPanel(){
    	
        JPanel jp=new JPanel();
        cardLayout=new CardLayout();
        jp.setLayout(cardLayout);
        
        momentsRpt=new JTextPane();
        momentsRpt.setMargin(new Insets(2,4,10,8));
        momentsRpt.setEditable(false);
        momentsRpt.setContentType("text/html");
        momentsRpt.setText(plot.getMomentsRpt());
        
        jp.add(new JScrollPane(momentsRpt), "0");  
        
        regRpt = new JTextPane();
        regRpt.setMargin(new Insets(2,4,10,8));
        regRpt.setEditable(false);
        regRpt.setContentType("text/html");
        regRpt.setText(plot.getRegressionRpt());
        
        jp.add(new JScrollPane(regRpt), "1");
        
        report=new RegressionRpt(data, y_var, x_var, vz, plot.getGroupMaker());
        report.setRegressionStatistics(plot.getRegressionStatistics(), true);        
        JScrollPane scroll=new JScrollPane(report);        
        jp.add(scroll, "2");
        
        nlRpt=new JTextPane();
        nlRpt.setMargin(new Insets(0,4,10,8));
        nlRpt.setEditable(false);
        nlRpt.setContentType("text/html");
        jp.add(new JScrollPane(nlRpt), "3");
        
        loessRpt=new JTextPane();
        loessRpt.setMargin(new Insets(2,4,10,8));
        loessRpt.setEditable(false);
        loessRpt.setContentType("text/html");
        loessRpt.setText(plot.getLoessRpt());
        
        jp.add(new JScrollPane(loessRpt), "4");  
        
        return jp;
    }
   
    
    /**
     * Change the color of selected observations
     */
    private void changeColor(Color c){
        
        //Change color for selected observations
        for(int i=0;i<data.getSize();i++){
            if(data.getState(i)){
                data.setColor(c, i);
            }
        }
        
    }
    
    
    public void stateChanged(ChangeEvent ce){
        Object obj=ce.getSource();
        
        if(obj instanceof Palette){
            changeColor(palette.getColor());
        }
        else if(obj instanceof OverlapSlicer){
            changeGroup();
        }
        
    }
    
    
    public void changeGroup(){
        int index=chooser.getCurrentIndex();
        plot.setGroup(index);
        momentsRpt.setText(plot.getMomentsRpt());
        loessRpt.setText(plot.getLoessRpt());
        regRpt.setText(plot.getRegressionRpt());
        report.setRegressionStatistics(plot.getRegressionStatistics(), true);
        report.setGroup(index);
    }
    
    public void actionPerformed(ActionEvent ae){
        String arg=ae.getActionCommand();
        Object src=ae.getSource();
        
        
        if(arg.equals("Reset Points")){
            this.reset();
            
        }
        else if(arg.equals("Moments")){
            moveMI.setEnabled(true);
            plot.enableEllipse(false);
            ellipseMI.setEnabled(false);
            cardLayout.show(rptPanel, "0");
        }
        //This indicates the event is from the "plot" menu
        else if(arg.equals("LS Fit")|| arg.equals("Correlation")){
            moveMI.setEnabled(true);
            setAnalysisMode(arg);
            fitMI.setEnabled(false);
            //spanMI.setEnabled(false);
        }
        else if(arg.equals("Loess Fit")){
            moveMI.setEnabled(true);
            plot.enableEllipse(false);
            ellipseMI.setEnabled(false);
            ciMI.setEnabled(false);
            fitMI.setEnabled(false);
            //spanMI.setEnabled(true);
            plot.showLoessFit();
            cardLayout.show(rptPanel, "4");
        	
        }
        else if(arg.equals("Nonlinear Fit")){
            ellipseMI.setEnabled(false);
            ciMI.setEnabled(false);
            moveMI.setEnabled(false);
            //spanMI.setEnabled(false);
            
            if(plot.getFittedFunction()!=null){
                int input=JOptionPane.showConfirmDialog(this,"Do you want to fit a new function?", "Confirm",JOptionPane.YES_NO_OPTION);
                if(input==JOptionPane.YES_OPTION){                    
                    nonlinearFit();                    
                }
                else{
                    plot.showNonlinearFit();
                    cardLayout.show(rptPanel, "3");                    
                }
            }
            else
                nonlinearFit();
        }
        else if(arg.equals("New Fit")){
            nonlinearFit();
        }
        else if(arg.equals("Color...")){
            Color c;
            c=JColorChooser.showDialog(this.getParent(), "Palette", Color.black);
            if(c!=null)
                changeColor(c);
        }
        else if(arg.equals("95% Confidence Limits")){
            Boolean state=new Boolean(ciMI.getState());
            this.enableCI(state);
            
        }
        else if(arg.equals("95% Correlation Ellipse")){
            Boolean state=new Boolean(ellipseMI.getState());
            this.enableEllipse(state);
        }
        else if(arg.equals("Move Points")){
            JCheckBoxMenuItem item=(JCheckBoxMenuItem)src;
            
            plot.enablePointDrag(item.getState());
        }
        else if(arg.indexOf("move point")>-1){
            this.movePoint(arg);
            
        }
        else if(arg.startsWith("move scanner")){
            this.moveScanner(arg);   
        }
        else if(arg.equals("move loess")){
        		loessRpt.setText(plot.getLoessRpt());
        }
        /*else if(arg.equals("Set Smoothing...")){
            LoessSpanConfigPanel configPanel = new LoessSpanConfigPanel(plot);
            JOptionPane.showMessageDialog(rptPanel, configPanel,
                      "smoothing settings",
                      JOptionPane.PLAIN_MESSAGE
                      );
        }*/
        else if (arg.equals("Transform..."))
        {
            Component comp=this.getParent();
            while(!(comp instanceof Frame))
                comp=comp.getParent();
            
            Transformation xform_x=TransformDialog.showTransformDialog((Frame)comp,x_var.getName());
            Transformation xform_y=TransformDialog.showTransformDialog((Frame)comp,y_var.getName(),'y');
            
            if(xform_x != null && xform_y != null && (xform_x.getTransformationMethod() != Transformation.CANONICAL || 
            		xform_y.getTransformationMethod() != Transformation.CANONICAL))
            {

         	    data.transform(x_var.getName(), xform_x);
         	    data.transform(y_var.getName(), xform_y);
         	    String varPairName = xform_y.getTransformedVarName(y_var.getName()) + " vs " +
         	     					  xform_x.getTransformedVarName(x_var.getName());
         	       
         	    if(xformMIList.isEmpty()){
         	        JCheckBoxMenuItem jmi = new JCheckBoxMenuItem(y_var.getName()+" vs "+x_var.getName(), false);
         	        jmi.addActionListener(this);
         	        optionMenu.add(jmi);
         	        xformMIList.add(jmi);
         	        xformx_table.put(y_var.getName()+" vs "+x_var.getName(), new Transformation(Transformation.CANONICAL));
         	        xformy_table.put(y_var.getName()+" vs "+x_var.getName(), new Transformation(Transformation.CANONICAL));
         	    }
         	       
         	    if(!xformx_table.containsKey(varPairName)){
         	        JCheckBoxMenuItem jmi = new JCheckBoxMenuItem(varPairName, true);
     	            jmi.addActionListener(this);
     	            optionMenu.add(jmi);
     	            xformMIList.add(jmi);
     	            xformx_table.put(varPairName, xform_x);
     	            xformy_table.put(varPairName, xform_y);
         	    }
         	       
         	    for(int i=0; i < xformMIList.size(); i++){
         	        JCheckBoxMenuItem jmi = (JCheckBoxMenuItem)xformMIList.elementAt(i);
 	        	    if(jmi.getText().equals(varPairName))
 	        	        jmi.setSelected(true);
 	        	    else
 	        	        jmi.setSelected(false);
         	    }            	
            }
        }
        else if (xformx_table.containsKey(arg))
        {
        	data.transform(x_var.getName(), (Transformation)xformx_table.get(arg));
        	data.transform(y_var.getName(), (Transformation)xformy_table.get(arg));
        	for(int i=0; i < xformMIList.size(); i++){
        	    JCheckBoxMenuItem jmi = (JCheckBoxMenuItem)xformMIList.elementAt(i);
        	    if(jmi.getText().equals(arg))
        	   	    jmi.setSelected(true);
        	    else
        	   	    jmi.setSelected(false);
        	}    
        }
    }
    
    public void update(String arg){
        plot.updatePlot(arg);
        if (arg.equals("yymask")||arg.equals("obs_deleted")|| 
            arg.equals("add_variable")||arg.equals("delete_variable"))
        {
            regRpt.setText(plot.getRegressionRpt());
            report.setRegressionStatistics(plot.getRegressionStatistics(), true);
            momentsRpt.setText(plot.getMomentsRpt());
            loessRpt.setText(plot.getLoessRpt());
        }
        
    }
    
    
    private void nonlinearFit(){
        String xVarName, yVarName;
        xVarName = x_var.getName();
        yVarName = y_var.getName();

        String input=JOptionPane.showInputDialog(this,"Please input the model with parameters. For example, "+yVarName+"=A+B*"+xVarName+"+C*"+xVarName+"^2.", "Formula", JOptionPane.INFORMATION_MESSAGE);
        if(input!=null){
            try{
            	ExpressionParser ep = new ExpressionParser();
                Expression expr=ep.parse(input.substring(input.indexOf('=')+1));
                Vector params=expr.getVariableNames();
                params.removeElement(xVarName);
                
                Component parent=this.getParent();
                while(!(parent instanceof Frame))
                    parent=parent.getParent();
                ParamInitializer inizer=new ParamInitializer((Frame)parent,params, input);
                inizer.setLocationRelativeTo(this);
                inizer.show();
                String[] array=new String[params.size()];
                params.copyInto(array);
                NonlinearRegression nr=new NonlinearRegression(plot.getXValues(), plot.getYValues(), expr, xVarName);
                nr.setUnparsedExpression(input);
                if(inizer.getValues()!=null){
                    nr.setInitialEstimates(array, inizer.getValues());
                }
                nr.iterativeSolve();
                
                double[] linearStat=plot.getRegressionStatistics();
                double ssLinear=linearStat[9]*(plot.getXValues().length-2);
                if(ssLinear<nr.getSS()){
                    JOptionPane.showMessageDialog(this,"Nonlinear regression yielded worse results than linear regression. "+
                    "The algorithm may not have converged.","Warning",JOptionPane.WARNING_MESSAGE);
                }
                    
                    
                Hashtable paramTable=new Hashtable(5);
                String[] text=new String[params.size()+1];
                text[0]="Parameter Estimates";
                for(int i=0;i<array.length;i++){
                    paramTable.put(array[i],new Double(nr.getParameterValue(array[i])));
                    text[i+1]=array[i]+"=   "+String.valueOf(nr.getParameterValue(array[i]));
                }
                
                nlRpt.setText(nr.getHTMLReport());
                plot.setNonlinearFit(expr,paramTable);
                cardLayout.show(rptPanel, "3");
            }
            catch(InvalidDataError err1){
                JOptionPane.showMessageDialog(this,"You have too many parameters in your function. The number of parameters can not exceed the number of data points!","Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(ParseException err2){
                JOptionPane.showMessageDialog(this, "The function you input is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(TokenMgrError err3){
                JOptionPane.showMessageDialog(this, "The function you input is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(ExecError err4){
                JOptionPane.showMessageDialog(this, "Algorithm failed to converge! Try different formula/initial values.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            catch(Exception err5){
                JOptionPane.showMessageDialog(this, "A good fit can not be computed for the formula and the values. Please try again.","Warning", JOptionPane.WARNING_MESSAGE);                
            }
            
        }
    }
    
}
