package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import wvustat.interfaces.*;
import wvustat.swing.StackLayout;
import wvustat.plot.AxisConfigPanel;

/**
 * HistogramModule is a bundled component that is used to analyze univariate data. It includes a menu bar, a HistogramPlot
 * a NormalPlot, a QuantilePlot and various report. To use it, a JFrame or JApplet must be supplied to contain HistogramModule
 * and the menu bar of the JFrame or JApplet should be set to the menu bar of the HistogramModule object.
 *
 */
public class HistogramModule extends GUIModule implements ChangeListener, ActionListener
{
	private static final long serialVersionUID = 1L;

	//constants that can be used to identify the type of plot in this component
    public static final int HISTOGRAM = 0;
    public static final int NORMAL_PLOT = 1;
    public static final int QUANTILE_PLOT = 2;
    public static final int ASHISTOGRAM = 3;
    //public static final int STEMANDLEAF = 4;


    private Variable y_var, z_var;
    private Vector vz;

    private HistogramPlot hplot;
    private QuantilePlot qplot;
    private NormalPlot nplot;
    private ASHistogramPlot ashplot;
    private StemAndLeafPloter splot;

    //private GroupChooser chooser;
    private OverlapSlicer chooser;

    private JMenuBar menuBar;

    //The following two variables are used to manage plots
    private CardLayout plotCard = new CardLayout(),rptCard = new CardLayout();
    private CardLayout histCard; //cardlayout used in report "histogram"
    private JPanel plotPanel,rptPanel, histPanel;
    //This variable keeps track of the current plot
    //histogram corresponds to 0, normalplot 1, quantile plot 2
    private int plotIndex = 0;

    //densityMI corresponds to menu item "normal density"
    //kernelMI corresponds to menu item "kernel density"
    //freqMI corresponds to menu item "frequency axis"
    //lineMI corresponds to menu item "quantile lines"
    //traceMI corresponds to menu item "show quantiles"
    //robustMI corresponds to menu item "robust fit"
    private JCheckBoxMenuItem densityMI, kernelMI, rugMI, areaLinkMI, lineMI1,lineMI2, traceMI,robustMI;
    private JRadioButtonMenuItem freqMI, relFreqMI, densityAxisMI;
    private JRadioButtonMenuItem ashBarMI, ashPointMI, ashRugMI;

    private JMenuItem resetMI, colorMI;
    private JMenuItem /*setBwMI,*/ xformMI;

    private JMenu optionMenu, ajm;

    //private MouseToolMenu toolMenu;
    private JRadioButtonMenuItem histMI, normPlotMI, quantPlotMI, ashistMI;
    private JRadioButtonMenuItem momentMI, ttestMI, ciMI, quantMI;
    private Report momentRpt, ttestRpt, ciRpt, quantRpt;

    //This is an object that contains everything about a hypothesis test
    private Hypothesis hypo = new Hypothesis();

    private JMenuItem rescaleX, rescaleY;
    
    private Vector xformMIList = new Vector();
    private Hashtable xform_table = new Hashtable();

    /**
     *	Constructor
     * Creates a new HistogramModule with given data set and variables
     *
     * @param data the data set this HistogramModule will be based on
     *	@param y_var the y variable within the data set
     * @param z_var the z variable withing the data set
     *
     */
    public HistogramModule(DataSet data, Variable y_var, Variable z_var)
    {
        this.data = data;
        this.y_var = y_var;
        this.z_var = z_var;
        
        this.vz = new Vector();
        this.vz.addElement(z_var);

        setBackground(Color.white);
        setBorder(new javax.swing.border.LineBorder(Color.black));

        init();

    }

    public HistogramModule(DataSet data, Variable y_var, Vector vz)
    {
        this.data = data;
        this.y_var = y_var;
        this.vz = vz;
        
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        
        setBackground(Color.white);
        setBorder(new javax.swing.border.LineBorder(Color.black));

        init();
    	
    }
    
    /**
     * Constructor
     * Creats a new HistogramModule object with give data set. The data set provided must have
     * a numerical y variable with or without z variable.
     *
     * @param data the data set that contains the source data
     */
    public HistogramModule(DataSet data)
    {
        this.data = data;

        setBackground(Color.white);
        setBorder(new javax.swing.border.LineBorder(Color.black));

        Vector vy = data.getYVariables();
        y_var = (Variable) vy.elementAt(0);
        this.vz = data.getZVariables();
        if (vz.size() > 0)
            z_var = (Variable) vz.elementAt(0);
        
        init();
    }


    public void setGroup(Integer index)
    {
        int val = index.intValue();

        chooser.setCurrentIndex(val);
        hplot.setGroup(val);
        nplot.setGroup(val);
        qplot.setGroup(val);
        ashplot.setGroup(val);
        splot.setGroup(val);

        momentRpt.setGroup(val);
        ttestRpt.setGroup(val);
        ciRpt.setGroup(val);
        quantRpt.setGroup(val);
    }

    public void setCursorPosition(Point2D pt)
    {
        qplot.setCursorPosition(pt);
    }


    public void enableNormalDensity(Boolean bl)
    {
        hplot.enableNormalDensity(bl.booleanValue());
    }

    public void enableKernelDensity(Boolean bl)
    {
        this.kernelMI.setSelected(bl.booleanValue());
        hplot.enableKernelDensity(bl.booleanValue());
    }
    
    public void enableAreaLinking(Boolean bl)
    {
    	this.areaLinkMI.setSelected(bl.booleanValue());
    	hplot.enableAreaLinking(bl.booleanValue());
    }
    
    public void enableQuantileTracing(Boolean bl)
    {
        qplot.enableQuantileTracing(bl.booleanValue());
    }

    public void enableQuantileLine(Boolean bl)
    {
        switch (plotIndex)
        {
            case NORMAL_PLOT:
                nplot.enableQuantLine(bl.booleanValue());
                break;
            case QUANTILE_PLOT:
                qplot.enableQuantLine(bl.booleanValue());
                break;
        }
    }

    public void showPlot(String plotName)
    {
    	//switch case added by djluo in order to keep the axis consistant in all plots
    	BaseAxisModel axisModel = null;
    	switch (plotIndex) {
    		case HISTOGRAM: 
    			axisModel = hplot.getXAxisModel();
    			break;
    		case NORMAL_PLOT:
    			axisModel = nplot.getXAxisModel();
    			break;
    		case QUANTILE_PLOT:
    			axisModel = qplot.getXAxisModel();
    			break;
    		case ASHISTOGRAM:
    			axisModel = ashplot.getXAxisModel();
    			break;
    	}
    	
    	
        if (plotName.equals("Quantile Plot"))
        {
            quantPlotMI.setSelected(true);
            plotCard.show(plotPanel, "quantileplot");
            rptCard.show(rptPanel, "normal-quantile");
            ajm.removeAll();
            ajm.add(quantMI);
            setMenuForPlot(QUANTILE_PLOT);
            plotIndex = QUANTILE_PLOT;
            
            if(axisModel!=null)
                qplot.setXAxisModel(axisModel);

        }
        else if (plotName.equals("Histogram"))
        {
            histMI.setSelected(true);
            plotCard.show(plotPanel, "histogram");
            rptCard.show(rptPanel, "histogram");
            ajm.removeAll();
            ajm.add(momentMI);
            ajm.addSeparator();
            ajm.add(ttestMI);
            ajm.add(ciMI);
            setMenuForPlot(HISTOGRAM);
            plotIndex = HISTOGRAM;
            
            if(axisModel!=null)
                hplot.setXAxisModel(axisModel);
            
        }
        else if (plotName.equals("Normal Plot"))
        {
            normPlotMI.setSelected(true);
            plotCard.show(plotPanel, "normalplot");
            rptCard.show(rptPanel, "normal-quantile");
            //histControl.setVisible(false);
            ajm.removeAll();
            ajm.add(quantMI);
            setMenuForPlot(NORMAL_PLOT);
            plotIndex = NORMAL_PLOT;
            
            if(axisModel!=null)
                nplot.setXAxisModel(axisModel);
        }
        else if (plotName.equals("ASH Plot"))
        {
        	ashistMI.setSelected(true);
        	plotCard.show(plotPanel, "ashistogram");
        	rptCard.show(rptPanel, "histogram");
        	histCard.show(histPanel, "Moments");
        	ajm.removeAll();
        	ajm.add(momentMI);
        	setMenuForPlot(ASHISTOGRAM);
        	plotIndex = ASHISTOGRAM;
        	
        	if(axisModel!=null)
                ashplot.setXAxisModel(axisModel);
        }
        else if (plotName.equals("Stem And Leaf"))
        {
        	plotCard.show(plotPanel, "stemandleaf");
        	rptCard.show(rptPanel, "histogram");
        	histCard.show(histPanel, "Moments");
        	ajm.removeAll();
        	ajm.add(momentMI);      	
        }
    }
    
    public void setXAxisModel(BaseAxisModel axisModel) {
    	switch (plotIndex) {
			case HISTOGRAM:
				hplot.setXAxisModel(axisModel);
				break;
			case NORMAL_PLOT:
				nplot.setXAxisModel(axisModel);
				break;
			case QUANTILE_PLOT:
				qplot.setXAxisModel(axisModel);
				break;
			case ASHISTOGRAM:
				ashplot.setXAxisModel(axisModel);
				break;
    	}				
    }
    

    public void enableRobustFit(Boolean bl)
    {
        nplot.enableRobustFit(bl.booleanValue());
    }

    

    public void showHistogramReport(String rptName)
    {
        if (rptName.equals("Moments"))
        {
            momentMI.setSelected(true);
            histCard.show(histPanel, "Moments");
            hplot.enableCI(false);
        }
        else if (rptName.equals("t-test"))
        {
            ttestMI.setSelected(true);
            histCard.show(histPanel, "t-test");
            hplot.enableNull(true);
        }
        else if (rptName.equals("Confidence Interval"))
        {
            ciMI.setSelected(true);
            histCard.show(histPanel, "C.I.");
            hplot.enableCI(true);
        }
    }

    /**
     * build the menu bar, add components to this HistogramModule object
     */
    private void init()
    {
        data.addRemoteObserver(this);

        hplot = new HistogramPlot(data, y_var, vz);
        hplot.setGUIModule(this);
        hplot.setHypothesis(hypo);

        qplot = new QuantilePlot(data, y_var, vz);
        qplot.setGUIModule(this);
        qplot.addChangeListener(this);
        if (hplot.getGroupMaker() != null)
            qplot.setGroupMaker(hplot.getGroupMaker());

        nplot = new NormalPlot(data, y_var, vz);
        nplot.setGUIModule(this);
        if (hplot.getGroupMaker() != null)
            nplot.setGroupMaker(hplot.getGroupMaker());
        
        ashplot = new ASHistogramPlot(data, y_var, vz);
        ashplot.setGUIModule(this);
        if (hplot.getGroupMaker() != null)
        	ashplot.setGroupMaker(hplot.getGroupMaker());
        
        splot = new StemAndLeafPloter(data, y_var, vz);
        if (hplot.getGroupMaker() != null)
        	splot.setGroupMaker(hplot.getGroupMaker());

        buildJMenuBar();

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        //Add the control panel
        JPanel jp = buildControl();
        if (jp != null)
            add(jp, new GridBagConstraints(0, 0, 0, 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 4, 2, 2), 0, 0));

        //Add the plot panel
        gbc.gridy++;
        gbc.weightx = 1.0;
        gbc.weighty = 0.75;
        gbc.fill = GridBagConstraints.BOTH;

        plotPanel = buildPlotPanel();
        //toolMenu.setTarget(plotPanel);
        add(plotPanel, gbc);

        //Add the report panel

        //This is the report panel for 'histogram'
        momentRpt = new MomentsReport(data, y_var, vz, hplot.getGroupMaker());

        TtestReport tmpRpt = new TtestReport(data, y_var, vz, hplot.getGroupMaker());
        tmpRpt.setHypothesis(hypo);

        hypo.addChangeListener(hplot);

        hypo.addChangeListener(tmpRpt);
        ttestRpt = tmpRpt;

        CIReport tmpRpt2 = new CIReport(data, y_var, vz, hplot.getGroupMaker());
        tmpRpt2.setHypothesis(hypo);
        hypo.addChangeListener(tmpRpt2);
        ciRpt = tmpRpt2;

        histPanel = new JPanel();
        histCard = new CardLayout();
        histPanel.setLayout(histCard);
        histPanel.add("Moments", momentRpt);
        histPanel.add("t-test", ttestRpt);
        histPanel.add("C.I.", ciRpt);

        //This is the report panel for 'normalPlot' and 'quantile plot'
        quantRpt = new QuantileReport(data, y_var, vz, hplot.getGroupMaker());

        rptPanel = new JPanel();
        rptPanel.setLayout(rptCard);
        rptPanel.add("histogram", histPanel);
        rptPanel.add("normal-quantile", quantRpt);
        rptCard.show(rptPanel, "histogram");

        gbc.gridy++;
        gbc.weighty = 0.25;
        JScrollPane jsp = new JScrollPane(rptPanel);
        jsp.setPreferredSize(new Dimension(250, 100));
        add(jsp, gbc);

    }

    /**
     * Build the menu bar contained by this HistogramModule object
     */
    private void buildJMenuBar()
    {

        menuBar = new JMenuBar();

        //"Plot" menu
        ButtonGroup group = new ButtonGroup();
        JMenu jm = new JMenu("Plot");

        JRadioButtonMenuItem jmi = new JRadioButtonMenuItem("Histogram");
        histMI = jmi;
        jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), true);
        jm.add(jmi);
        
        jm.addSeparator();
        jmi = new JRadioButtonMenuItem("Normal Plot");
        normPlotMI = jmi;
        jmi.addActionListener(this);
        group.add(jmi);
        jm.add(jmi);
        jmi = new JRadioButtonMenuItem("Quantile Plot");
        quantPlotMI = jmi;
        jmi.addActionListener(this);
        group.add(jmi);
        jm.add(jmi);
        
        jm.addSeparator();
        jmi = new JRadioButtonMenuItem("ASH Plot");
        ashistMI = jmi;
        jmi.addActionListener(this);
        group.add(jmi);
        jm.add(jmi);
        
        jm.addSeparator();
        jmi = new JRadioButtonMenuItem("Stem And Leaf");
        jmi.addActionListener(this);
        group.add(jmi);
        jm.add(jmi);
        
        

        //"Options" menu
        optionMenu = new JMenu("Options");

        //Iniatilize all menu items
        densityMI = new JCheckBoxMenuItem("Normal Density", false);
        densityMI.setActionCommand("normal density");
        densityMI.setEnabled(false);
        
        kernelMI = new JCheckBoxMenuItem("Kernel Density", false);
        kernelMI.setActionCommand("kernel density");
        kernelMI.setEnabled(false);

        rugMI = new JCheckBoxMenuItem("Show Rug", false);
        rugMI.setActionCommand("show rug");
        
        areaLinkMI = new JCheckBoxMenuItem("Area Linking", false);
        areaLinkMI.setActionCommand("area linking");
        
        /*setBwMI = new JMenuItem("Set Bandwidth...");
        setBwMI.setEnabled(false);
        setBwMI.addActionListener(this);*/
        
        lineMI1 = new JCheckBoxMenuItem("Outlier Boxplot");
        lineMI1.setActionCommand("outlier boxplot");
        lineMI2 = new JCheckBoxMenuItem("Quantile Boxplot");
        lineMI2.setActionCommand("quantile boxplot");
        traceMI = new JCheckBoxMenuItem("Quantile Values");
        traceMI.setActionCommand("quantile values");
        robustMI = new JCheckBoxMenuItem("Robust Fit");
        robustMI.setActionCommand("robust fit");

        freqMI = new JRadioButtonMenuItem("Frequency Axis", true);
        freqMI.setActionCommand("frequency axis");
        relFreqMI = new JRadioButtonMenuItem("Rel Freq Axis");
        relFreqMI.setActionCommand("rel freq");
        densityAxisMI = new JRadioButtonMenuItem("Density Axis");
        densityAxisMI.setActionCommand("density axis");
        
        ashBarMI = new JRadioButtonMenuItem("Show Bars", true);
        ashBarMI.setActionCommand("show bars");
        ashPointMI = new JRadioButtonMenuItem("Show Points");
        ashPointMI.setActionCommand("show points");
        ashRugMI = new JRadioButtonMenuItem("Show Rug");
        ashRugMI.setActionCommand("show rug");

        ButtonGroup bg = new ButtonGroup();
        bg.add(freqMI);
        bg.add(relFreqMI);
        bg.add(densityAxisMI);
        
        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(ashBarMI);
        bg2.add(ashPointMI);
        bg2.add(ashRugMI);

        densityMI.addActionListener(this);
        kernelMI.addActionListener(this);
        rugMI.addActionListener(this);
        areaLinkMI.addActionListener(this);
        lineMI1.addActionListener(this);
        lineMI2.addActionListener(this);
        traceMI.addActionListener(this);
        robustMI.addActionListener(this);

        relFreqMI.addActionListener(this);
        densityAxisMI.addActionListener(this);
        freqMI.addActionListener(this);
        
        ashBarMI.addActionListener(this);
        ashPointMI.addActionListener(this);
        ashRugMI.addActionListener(this);

        resetMI = new JMenuItem("Reset");
        resetMI.addActionListener(this);

        colorMI = new JMenuItem("Color...");
        colorMI.addActionListener(this);
        
        xformMI = new JMenuItem("Transform...");
        xformMI.addActionListener(this);


        //jm.add(optionMenu);

        menuBar.add(jm);

        //"Analysis" menu
        ButtonGroup group2 = new ButtonGroup();
        ajm = new JMenu("Analyze");

        momentMI = new JRadioButtonMenuItem("Moments");
        momentMI.addActionListener(this);
        group2.add(momentMI);
        group2.setSelected(momentMI.getModel(), true);
        ajm.add(momentMI);
        ajm.addSeparator();

        ttestMI = new JRadioButtonMenuItem("t-test");
        ttestMI.addActionListener(this);
        group2.add(ttestMI);
        ajm.add(ttestMI);

        ciMI = new JRadioButtonMenuItem("Confidence Interval");
        ciMI.addActionListener(this);
        group2.add(ciMI);
        ajm.add(ciMI);

        menuBar.add(ajm);

        quantMI = new JRadioButtonMenuItem("Quantiles");
        quantMI.addActionListener(this);
        quantMI.setSelected(true);

        rescaleX = new JMenuItem("Rescale X Axis...");
        rescaleX.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(hplot.getXAxisModel());
                int option = JOptionPane.showOptionDialog(HistogramModule.this, configPanel,
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
                        hplot.setXAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(HistogramModule.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        );

        rescaleY = new JMenuItem("Rescale Y Axis...");
        rescaleY.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {

            }
        });

        setMenuForPlot(HISTOGRAM);

    }

    /**
     *	This method set the proper menu for the current plot
     *
     *	@param index	the index that identifies the plot, the value should be either HISTOGRAM,NORMAL_PLOT or QUANTILE_PLOT
     */
    public void setMenuForPlot(int index)
    {
        switch (index)
        {
            case HISTOGRAM:
                optionMenu.removeAll();

                optionMenu.add(freqMI);
                optionMenu.add(relFreqMI);
                optionMenu.add(densityAxisMI);

                optionMenu.addSeparator();
                optionMenu.add(densityMI);
                optionMenu.add(kernelMI);
                //optionMenu.add(setBwMI);
                optionMenu.addSeparator();
                optionMenu.add(rugMI);
                optionMenu.addSeparator();
                
                optionMenu.add(areaLinkMI);
                
                optionMenu.addSeparator();
                optionMenu.add(rescaleX);

                optionMenu.addSeparator();
                optionMenu.add(resetMI);
                optionMenu.add(colorMI);
                //toolMenu.setItemEnabled(1, true);
                break;
                
            case NORMAL_PLOT:
                optionMenu.removeAll();
                optionMenu.add(lineMI1);
                optionMenu.add(robustMI);

                optionMenu.addSeparator();

                optionMenu.add(colorMI);
                //toolMenu.setItemEnabled(1, false);

                break;
                
            case QUANTILE_PLOT:
                optionMenu.removeAll();
                optionMenu.add(lineMI2);
                optionMenu.add(traceMI);

                optionMenu.addSeparator();

                optionMenu.add(colorMI);
                //toolMenu.setItemEnabled(1, false);

                break;
                
            case ASHISTOGRAM:
            	optionMenu.removeAll();
            	optionMenu.add(ashBarMI);
            	optionMenu.add(ashPointMI);
            	optionMenu.add(ashRugMI);

                optionMenu.addSeparator();
                
                optionMenu.add(colorMI);
                break;
        }
        
        optionMenu.addSeparator();
        optionMenu.add(xformMI);
        for (int i=0; i < xformMIList.size(); i++) {
        	optionMenu.add((JCheckBoxMenuItem)xformMIList.elementAt(i));
        }
    }

    /**
     * Build the control panel for this HistogramModule object
     */
    private JPanel buildControl()
    {
        if (z_var == null)
            return null;

        JPanel jp = new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new StackLayout());

        EqualCountGrouper grouper = hplot.getGroupMaker();
        chooser = new OverlapSlicer(grouper);
        chooser.addChangeListener(this);
        jp.add(chooser);

        return jp;
    }

    /**
     * Build the plot panel
     */
    private JPanel buildPlotPanel()
    {
        JPanel jp = new JPanel();
        jp.setLayout(plotCard);

        jp.add("histogram", hplot);
        jp.add("quantileplot", qplot);
        jp.add("normalplot", nplot);
        jp.add("ashistogram", ashplot);
        jp.add("stemandleaf", splot);

        return jp;
    }

    /**
     * Get the menu bar contained in this object
     */
    public JMenuBar getJMenuBar()
    {
        return menuBar;
    }
    
    
    public JMenu getOptionMenu()
    {
    	return optionMenu;
    }

    /**
     * Change the color of selected observations to newColor
     */
    public void changeColor(Color newColor)
    {
        for (int i = 0; i < data.getSize(); i++)
        {
            if (data.getState(i) && !data.getMask(i))
                data.setColor(newColor, i);
        }
    }

    public void stateChanged(ChangeEvent e)
    {
        Object obj = e.getSource();

        if (obj instanceof OverlapSlicer)
        {
            Integer val = new Integer(chooser.getCurrentIndex());
            this.setGroup(val);
        }
        else if (obj instanceof Palette)
        {
            Color color = ((Palette) obj).getColor();
            changeColor(color);
        }
        else if (obj instanceof QuantilePlot)
        {
            Point2D pt = ((QuantilePlot) obj).getCursorPosition();
            this.setCursorPosition(pt);

        }
    }

    public void update(String arg)
    {


        if (arg.equals("yystate") || arg.equals("yycolor"))
        {
            hplot.updatePlot(arg);
            qplot.updatePlot(arg);
            nplot.updatePlot(arg);
            ashplot.updatePlot(arg);
            splot.updatePlot(arg);
        }
        else
        {
            hplot.updatePlot(arg);
            
            //added by djluo. set the x axises consistent in different plots
            BaseAxisModel axisModel=hplot.getXAxisModel();
            
            qplot.updatePlot(arg);
            if(axisModel!=null)
                qplot.setXAxisModel(axisModel);
            
            nplot.updatePlot(arg);
            if(axisModel!=null)
                nplot.setXAxisModel(axisModel);
            
            ashplot.updatePlot(arg);
            if(axisModel!=null)
                ashplot.setXAxisModel(axisModel);
            
            splot.updatePlot(arg);
            
            momentRpt.updateReport();
            ttestRpt.updateReport();
            ciRpt.updateReport();
            quantRpt.updateReport();
        }

    }

    public void actionPerformed(ActionEvent ae)
    {
        String arg = ae.getActionCommand();
        Object src = ae.getSource();

        //This action should come from "Data" button
        if (arg.equals("Data Table"))
        {
            //showDataTable(data);
        }
        else if (arg.equals("Transform..."))
        {
            Component comp=this.getParent();
            while(!(comp instanceof Frame))
                comp=comp.getParent();
       
            Transformation xform=TransformDialog.showTransformDialog((Frame)comp,y_var.getName());
            
            if(xform != null && xform.getTransformationMethod() != Transformation.CANONICAL)
            {
               
            	data.transform(y_var.getName(), xform);
            	String newVarName = xform.getTransformedVarName(y_var.getName());
            	       
            	if(xformMIList.isEmpty()){
            	    JCheckBoxMenuItem jmi = new JCheckBoxMenuItem(y_var.getName(), false);
            	    jmi.addActionListener(this);
            	    optionMenu.add(jmi);
            	    xformMIList.add(jmi);
            	    xform_table.put(y_var.getName(), new Transformation(Transformation.CANONICAL));
            	}
            	       
            	if(!xform_table.containsKey(newVarName)){
            	    JCheckBoxMenuItem jmi = new JCheckBoxMenuItem(newVarName, true);
        	        jmi.addActionListener(this);
        	        optionMenu.add(jmi);
        	        xformMIList.add(jmi);
        	        xform_table.put(newVarName, xform);
            	}
            	       
            	for(int i=0; i < xformMIList.size(); i++){
            	    JCheckBoxMenuItem jmi = (JCheckBoxMenuItem)xformMIList.elementAt(i);
    	            if(jmi.getText().equals(newVarName))
    	                jmi.setSelected(true);
    	            else
    	                jmi.setSelected(false);
            	}
            	   
            }
        }
        //these actions should come from 'option' menu
        else if (arg.equals("frequency axis"))
        {
            hplot.setAxisOption(HistogramPlot.FREQ_AXIS);
            densityMI.setSelected(false);
            densityMI.setEnabled(false);
            kernelMI.setSelected(false);
            kernelMI.setEnabled(false);
            //setBwMI.setEnabled(false);
        }
        else if (arg.equals("rel freq"))
        {
            hplot.setAxisOption(HistogramPlot.REL_FREQ_AXIS);
            densityMI.setEnabled(false);
            densityMI.setSelected(false);
            kernelMI.setEnabled(false);
            kernelMI.setSelected(false);
            //setBwMI.setEnabled(false);
        }
        else if (arg.equals("density axis"))
        {
            hplot.setAxisOption(HistogramPlot.DENSITY_AXIS);
            densityMI.setEnabled(true);
            kernelMI.setEnabled(true);
            //setBwMI.setEnabled(true);
        }
        else if (arg.equals("normal density"))
        {
            Boolean state = new Boolean(densityMI.getState());
            this.enableNormalDensity(state);
        }
        else if (arg.equals("kernel density"))
        {
            Boolean state = new Boolean(kernelMI.getState());
            this.enableKernelDensity(state);
        }
        else if (arg.equals("area linking"))
        {
        	Boolean state = new Boolean(areaLinkMI.getState());
        	this.enableAreaLinking(state);
        }
        else if (arg.equals("show bars"))
        {
        	ashplot.enableBarDrawing(true);
        	ashplot.enableRugDrawing(false);
        }
        else if (arg.equals("show points"))
        {
        	ashplot.enableBarDrawing(false);
        	ashplot.enableRugDrawing(false);
        }
        /*else if (arg.equals("Set Bandwidth..."))
        {
        		kernelMI.setSelected(true);
        		this.enableKernelDensity(new Boolean(true));
        		
        		BandwidthConfigPanel configPanel = new BandwidthConfigPanel(hplot);
        		JOptionPane.showMessageDialog(histPanel, configPanel,
	                      "bandwidth settings",
	                      JOptionPane.PLAIN_MESSAGE
	                      );
        }*/
        else if (arg.equals("show rug") && plotIndex == HISTOGRAM)
        {
        	hplot.enableRugDrawing(rugMI.isSelected());
        }
        else if (arg.equals("show rug") && plotIndex == ASHISTOGRAM)
        {
        	ashplot.enableBarDrawing(false);
        	ashplot.enableRugDrawing(true);
        }
        else if (arg.equals("quantile values"))
        {
            Boolean state = new Boolean(traceMI.getState());
            this.enableQuantileTracing(state);

        }
        else if (arg.equals("outlier boxplot"))
        {
            Boolean state = new Boolean(lineMI1.getState());
            this.enableQuantileLine(state);

        }
        else if (arg.equals("quantile boxplot"))
        {
            Boolean state = new Boolean(lineMI2.getState());
            this.enableQuantileLine(state);

        }
        else if (arg.equals("robust fit"))
        {
            Boolean state = new Boolean(robustMI.getState());
            this.enableRobustFit(state);
        }
        //these actions should come from "plot" menu
        else if (arg.equals("Quantile Plot") || arg.equals("Histogram") || arg.equals("Normal Plot") || arg.equals("ASH Plot") || arg.equals("Stem And Leaf"))
        {
            showPlot(arg);
        }
        else if (arg.equals("Reset") && plotIndex == HISTOGRAM)
        {
            hplot.resetPlot();
        }
        else if (arg.equals("Color..."))
        {
            Color c;
            c = JColorChooser.showDialog(this.getParent(), "Palette", Color.black);
            if (c != null)
                changeColor(c);
        }
        else if (arg.equals("Moments") || arg.equals("t-test") || arg.equals("Confidence Interval"))
        {
            showHistogramReport(arg);

        }
        else if (xform_table.containsKey(arg))
        { 
        	data.transform(y_var.getName(), (Transformation)xform_table.get(arg));
        	for(int i=0; i < xformMIList.size(); i++){
        	    JCheckBoxMenuItem jmi = (JCheckBoxMenuItem)xformMIList.elementAt(i);
        	    if(jmi.getText().equals(arg))
        	   	    jmi.setSelected(true);
        	    else
        	   	    jmi.setSelected(false);
        	    }    
        }
    }


    private Point2D parsePoint(String str)
    {
        double x, y;

        int p = str.indexOf(',');
        String sub = str.substring(0, p);
        x = Double.parseDouble(sub);
        sub = str.substring(p + 1);
        y = Double.parseDouble(sub);

        return new Point2D.Double(x, y);
    }
    
    public Hypothesis getHypothesis()
    {
    		return hypo;
    }

}
