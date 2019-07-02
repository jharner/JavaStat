package wvustat.modules.rmodels;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.*;

import wvustat.interfaces.*;
import wvustat.modules.*;
import wvustat.network.*;
import wvustat.network.client.*;
import wvustat.plot.AxisConfigPanel;
import wvustat.util.ComponentUtil;
import wvustat.table.MainPanel;;

/**
 * @author dajieluo
 *
 * LinearModelModule display regression diagnosis plots graphically. It uses R engine to calculate most of the statistics. 
 */
public class LinearModelModule extends GUIModule implements ActionListener, ChangeListener{
	private static final long serialVersionUID = 1L;
	
	protected Variable y_var, z_var;
	protected Vector vx, vz;
	protected String yname;
	protected Vector xname;
	protected RLinearModel rlm;
	
	protected ResidualsPlot rplot;
	protected ResidualsPlot lplot;
	protected ResidualsPlot prplot;
	protected IndexPlot ilplot;
	protected IndexPlot splot;
	protected IndexPlot jplot;
	protected JPanel plotPanel, rptPanel;
	protected CardLayout plotCard, rptCard;
	protected JTextPane regRpt, loessRpt, anovaRpt;
	
	protected Palette palette;
	
	protected OverlapSlicer chooser;
	protected EqualCountGrouper grouper;
	protected int currentIndex = 0;
	protected int plotIndex = 0;
	
	protected ArrayList list;
	protected String formula;
	protected String family;
	protected String link;
	protected boolean isGLM = false;
	
	protected JMenuBar jmb;
	protected JMenu optionMenu;
	protected JMenuItem savResMI, savFitMI, colorMI, resetMI, rescaleX, rescaleY;
	protected JMenuItem savLevMI, savSResMI, savJResMI;
	protected JRadioButtonMenuItem rplotMI, lplotMI, prplotMI, fit0, fit1, fit2;
	
	protected HyperlinkListener hyperlinkListener = new HyperlinkHandler();
	protected HyperlinkListener hyperlinkListener1 = new HyperlinkHandler1();

	
	public LinearModelModule(DataSet data, Variable y_var, Vector vx, Vector vz, String inputModel, String family, String link) throws IllegalArgumentException
	{
		this.data = data;
		this.y_var = y_var;
		this.vx = vx;
		this.vz = vz;
		
		if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
		
		if(vx.size() == 0)
			throw new IllegalArgumentException("Can't do regression analysis without x variable");
		if (y_var == null)
            throw new IllegalArgumentException("Can't do regression analysis without y variable");
        //else if (y_var.getType() != Variable.NUMERIC)
        //    throw new IllegalArgumentException("Y variable can not be categorical");
        
        if (z_var != null)
        {
            if (grouper == null)
                grouper = new EqualCountGrouper(vz, data);
        }
        
        this.yname = y_var.getName();
        this.xname = new Vector();
        for (int i=0; i < vx.size(); i++){
        	Variable x_var = (Variable)vx.elementAt(i);
        	xname.addElement(x_var.getName());
        }
        
        try {
        	if (grouper == null)
        		list = JRIClient.createDataFrame(data, y_var, vx, null, yname, xname);
        	else
        	{
        		boolean[] constrt = new boolean[data.getSize()]; 
        		for (int i = 0; i < data.getSize(); i++)
        			if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
        				constrt[i] = true;
        			else
        				constrt[i] = false;
         			
        		list = JRIClient.createDataFrame(data, y_var, vx, constrt, yname, xname);
        	}	
        
        	if (inputModel == null)
        		formula = JRIClient.createFormula(y_var, vx);
        	else
        		formula = JRIClient.createFormula(y_var, inputModel);
        
        	this.family = family;
        	this.link = link;
        
        
        	if ("gaussian".equals(family) && "identity".equals(link)){
        		this.isGLM = false;
        	}else{
        		this.isGLM = true;
        	}
        
        	rlm = JRIClient.fitLinearModel(formula, list, family, link);
        }
        catch (RemoteException rex) {
        	ComponentUtil.showErrorMessage(MainPanel.getDesktopPane(), rex.getMessage());
        	rlm = isGLM ? (new GlmModel()) : (new RLinearModel());
        }
        
        
        init();
        
	}
	
	public void reset(){
		if (plotIndex == ResidualsPlot.RESIDUALVSFITTED)
			rplot.resetPlot();
		else if (plotIndex == ResidualsPlot.LEVERAGEPLOT)
			lplot.resetPlot();
		else if (plotIndex == ResidualsPlot.PARTIALRESIDUAL)
			prplot.resetPlot();
		else if (plotIndex == IndexPlot.INDEXPLOTLEVERAGE)
			ilplot.resetPlot();
		else if (plotIndex == IndexPlot.STUDENTIZEDRESIDUAL)
			splot.resetPlot();
		else if (plotIndex == IndexPlot.JACKNIFE)
			jplot.resetPlot();
    }
	
	/**
     * Initialize the component
     */
    private void init(){
        
        data.addRemoteObserver(this);        
      
        this.setBackground(Color.white);
        rplot=new ResidualsPlot(data, vx, rlm, grouper, ResidualsPlot.RESIDUALVSFITTED);
        rplot.setGUIModule(this);
        rplot.addActionListener(this);
        
        if (!isGLM){
        lplot=new ResidualsPlot(data, vx, rlm, grouper, ResidualsPlot.LEVERAGEPLOT);
        lplot.setGUIModule(this);
        lplot.addActionListener(this);
        }
        
        prplot=new ResidualsPlot(data, vx, rlm, grouper, ResidualsPlot.PARTIALRESIDUAL);
        prplot.setGUIModule(this);
        prplot.addActionListener(this);
        
        ilplot=new IndexPlot(data, vx, rlm, grouper, IndexPlot.INDEXPLOTLEVERAGE);
        ilplot.setGUIModule(this);
        
        
        if (!isGLM){
        splot=new IndexPlot(data, vx, rlm, grouper, IndexPlot.STUDENTIZEDRESIDUAL);
        splot.setGUIModule(this);
        
        }
        
        jplot=new IndexPlot(data, vx, rlm, grouper, IndexPlot.JACKNIFE);
        jplot.setGUIModule(this);
        
        
        setLayout(new BorderLayout());
        
        //Add control panel to this component
        JPanel controlPanel=buildControlPanel();
        if(controlPanel!=null){
        	add(controlPanel, BorderLayout.NORTH);
        }
        
        
        //Add plot panel to this component
        plotPanel=buildPlotPanel();

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
        
        rplotMI = new JRadioButtonMenuItem("Residuals vs Fitted values");
        rplotMI.addActionListener(this);
        group.add(rplotMI);
        group.setSelected(rplotMI.getModel(), true);
        plotMenu.add(rplotMI);
        
        if (!isGLM){
        lplotMI = new JRadioButtonMenuItem("Leverage Plot");
        lplotMI.addActionListener(this);
        group.add(lplotMI);
        plotMenu.add(lplotMI);
        }
        
        prplotMI = new JRadioButtonMenuItem("Partial Residual Plot");
        prplotMI.addActionListener(this);
        group.add(prplotMI);
        plotMenu.add(prplotMI);
        
        JRadioButtonMenuItem jmi = new JRadioButtonMenuItem("Index Plot of Leverages");
        jmi.addActionListener(this);
        group.add(jmi);
        plotMenu.add(jmi);
        
        if (!isGLM){
        jmi = new JRadioButtonMenuItem("Studentized Residuals");
        jmi.addActionListener(this);
        group.add(jmi);
        plotMenu.add(jmi);
        }
        
        jmi = new JRadioButtonMenuItem("Jacknife Residuals");
        jmi.addActionListener(this);
        group.add(jmi);
        plotMenu.add(jmi);
        
        
        optionMenu=new JMenu("Options");
        
        savResMI = new JMenuItem("Save Residuals");
        savResMI.addActionListener(this);
        savFitMI = new JMenuItem("Save Fitted values");
        savFitMI.addActionListener(this);
        
        savLevMI = new JMenuItem("Save Leverages");
        savLevMI.addActionListener(this);
        savSResMI = new JMenuItem("Save Studentized Residuals");
        savSResMI.addActionListener(this);
        savJResMI = new JMenuItem("Save Jacknife Residuals");
        savJResMI.addActionListener(this);
                
        optionMenu.add(savResMI);
        optionMenu.add(savFitMI);
        optionMenu.addSeparator();
        
        colorMI=new JMenuItem("Color...");
        resetMI=new JMenuItem("Reset");
        colorMI.addActionListener(this);
        resetMI.addActionListener(this);
        

        rescaleX=optionMenu.add("Rescale X Axis...");
        rescaleX.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(rplot.getXAxisModel());
                int option = JOptionPane.showOptionDialog(LinearModelModule.this, configPanel,
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
                        rplot.setXAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(LinearModelModule.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rescaleY=optionMenu.add("Rescale Y Axis...");
        rescaleY.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                AxisConfigPanel configPanel = new AxisConfigPanel(AxisConfigPanel.MANUAL_MODEL);
                configPanel.setAxisModel(rplot.getYAxisModel());
                int option = JOptionPane.showOptionDialog(LinearModelModule.this, configPanel,
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
                        rplot.setYAxisModel(model);
                    }
                    else
                        JOptionPane.showMessageDialog(LinearModelModule.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        optionMenu.addSeparator();
        optionMenu.add(resetMI);
        optionMenu.add(colorMI);

        
        //plotMenu.add(optionMenu);
        jmb.add(plotMenu);
        
        JMenu analyzeMenu=new JMenu("Analyze");
        
        /*JRadioButtonMenuItem*/ fit0=new JRadioButtonMenuItem("ANOVA",true);
        /*JRadioButtonMenuItem*/ fit1=new JRadioButtonMenuItem("Coefficients",false);
        /*JRadioButtonMenuItem*/ fit2 = new JRadioButtonMenuItem("Loess Fit", false);
                
        ButtonGroup group2=new ButtonGroup();
        
        group2.add(fit0);
        group2.add(fit1);
        group2.add(fit2);
        
        fit0.addActionListener(this);
        fit1.addActionListener(this);
        fit2.addActionListener(this);
        
        analyzeMenu.add(fit0);
        analyzeMenu.add(fit1);
        analyzeMenu.add(fit2);
                
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
    
    public void setMenuForPlot(int index)
    {
        switch (index)
        {
            case ResidualsPlot.RESIDUALVSFITTED:
            	optionMenu.removeAll();
            	optionMenu.add(savResMI);
                optionMenu.add(savFitMI);
                optionMenu.addSeparator();
                optionMenu.add(rescaleX);
                optionMenu.add(rescaleY);
                optionMenu.addSeparator();
                optionMenu.add(resetMI);
                optionMenu.add(colorMI);
            	break;
            case ResidualsPlot.LEVERAGEPLOT:
            	optionMenu.removeAll();
            	optionMenu.add(resetMI);
                optionMenu.add(colorMI);
            	break;
            case ResidualsPlot.PARTIALRESIDUAL:
            	optionMenu.removeAll();
            	optionMenu.add(resetMI);
                optionMenu.add(colorMI);
            	break;
            case IndexPlot.INDEXPLOTLEVERAGE:
            	optionMenu.removeAll();
            	optionMenu.add(savLevMI);
            	optionMenu.addSeparator();
            	optionMenu.add(resetMI);
                optionMenu.add(colorMI);
            	break;
            case IndexPlot.STUDENTIZEDRESIDUAL:
            	optionMenu.removeAll();
            	optionMenu.add(savSResMI);
            	optionMenu.addSeparator();
            	optionMenu.add(resetMI);
                optionMenu.add(colorMI);
            	break;
            case IndexPlot.JACKNIFE:
            	optionMenu.removeAll();
            	optionMenu.add(savJResMI);
            	optionMenu.addSeparator();
            	optionMenu.add(resetMI);
                optionMenu.add(colorMI);
            	break;
        }
    }
    
    public void showPlot(String plotName)
    {
        if (plotName.equals("Residuals vs Fitted values"))
        {
        	plotCard.show(plotPanel, "0");
        	rplotMI.setSelected(true);
        	setMenuForPlot(ResidualsPlot.RESIDUALVSFITTED);
        	fit2.setEnabled(true);
        	plotIndex = ResidualsPlot.RESIDUALVSFITTED;
        }
        else if (plotName.equals("Leverage Plot"))
        {
        	plotCard.show(plotPanel, "1");
        	rptCard.show(rptPanel, "0");
        	rplot.showLoessFit(false);
        	lplotMI.setSelected(true);
        	setMenuForPlot(ResidualsPlot.LEVERAGEPLOT);
        	fit0.setSelected(true);
        	fit2.setEnabled(false);
        	plotIndex = ResidualsPlot.LEVERAGEPLOT;
        }
        else if (plotName.equals("Partial Residual Plot"))
        {
        	plotCard.show(plotPanel, "2");
        	rptCard.show(rptPanel, "1");
        	rplot.showLoessFit(false);
        	prplotMI.setSelected(true);
        	setMenuForPlot(ResidualsPlot.PARTIALRESIDUAL);
        	fit1.setSelected(true);
        	fit2.setEnabled(false);
        	plotIndex = ResidualsPlot.PARTIALRESIDUAL;
        }
        else if (plotName.equals("Index Plot of Leverages"))
        {
        	plotCard.show(plotPanel, "3");
        	setMenuForPlot(IndexPlot.INDEXPLOTLEVERAGE);
        	fit2.setEnabled(false);
        	plotIndex = IndexPlot.INDEXPLOTLEVERAGE;
        }
        else if (plotName.equals("Studentized Residuals"))
        {
        	plotCard.show(plotPanel, "4");
        	setMenuForPlot(IndexPlot.STUDENTIZEDRESIDUAL);
        	fit2.setEnabled(false);
        	plotIndex = IndexPlot.STUDENTIZEDRESIDUAL;
        }
        else if (plotName.equals("Jacknife Residuals"))
        {
        	plotCard.show(plotPanel, "5");
        	setMenuForPlot(IndexPlot.JACKNIFE);
        	fit2.setEnabled(false);
        	plotIndex = IndexPlot.JACKNIFE;
        }
    }
    
    protected JPanel buildControlPanel(){
        if(z_var==null)
            return null;
        
        JPanel jp=new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        chooser=new OverlapSlicer(rplot.getGroupMaker());
        chooser.addChangeListener(this);
        chooser.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        jp.add(chooser);
        
        return jp;
    }
    
    private JPanel buildPlotPanel(){
    	JPanel jp = new JPanel();
    	plotCard=new CardLayout();
        jp.setLayout(plotCard);
        
        jp.add(rplot, "0");
        if(!isGLM) jp.add(lplot, "1");
        jp.add(prplot, "2");
        jp.add(ilplot, "3");
        if(!isGLM) jp.add(splot, "4");
        jp.add(jplot, "5");
        
        return jp;
    }
	
    protected JPanel buildReportPanel(){
    	
        JPanel jp=new JPanel();
        rptCard=new CardLayout();
        jp.setLayout(rptCard);
        
        anovaRpt = new JTextPane();
        anovaRpt.setMargin(new Insets(2,4,10,8));
        anovaRpt.setEditable(false);
        anovaRpt.setContentType("text/html");
        anovaRpt.setText(rplot.getAnovaRpt());
        anovaRpt.addHyperlinkListener(this.hyperlinkListener);
        
        jp.add(new JScrollPane(anovaRpt), "0");
        
        regRpt = new JTextPane();
        regRpt.setMargin(new Insets(2,4,10,8));
        regRpt.setEditable(false);
        regRpt.setContentType("text/html");
        regRpt.setText(rplot.getRegressionRpt());
        regRpt.addHyperlinkListener(this.hyperlinkListener1);
        
        jp.add(new JScrollPane(regRpt), "1");
        
        loessRpt=new JTextPane();
        loessRpt.setMargin(new Insets(2,4,10,8));
        loessRpt.setEditable(false);
        loessRpt.setContentType("text/html");
        loessRpt.setText(rplot.getLoessRpt());
        
        jp.add(new JScrollPane(loessRpt), "2");  
        
        return jp;
    }
    
    /**
     * Change the color of selected observations
     */
    protected void changeColor(Color c){
        
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
    		
    	currentIndex=chooser.getCurrentIndex();
    			
    	try{
    		if (grouper == null)
    			list = JRIClient.createDataFrame(data, y_var, vx, null, yname, xname);
    		else{
    			boolean[] constrt = new boolean[data.getSize()]; 
    			for (int i = 0; i < data.getSize(); i++)
    				if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
    					constrt[i] = true;
    				else
    					constrt[i] = false;
         				
    			list = JRIClient.createDataFrame(data, y_var, vx, constrt, yname, xname);
    		}	
    			
    		rlm = JRIClient.fitLinearModel(formula, list, family, link);
    			
   		}catch(RemoteException rex){
   			ComponentUtil.showErrorMessage(this, rex.getMessage());
   			rlm.clear();
   	    }
    		
   		rplot.setRLinearModel(rlm);
   		rplot.setGroup(currentIndex);
   		if(!isGLM) lplot.setRLinearModel(rlm);
   		if(!isGLM) lplot.setGroup(currentIndex);
   		prplot.setRLinearModel(rlm);
   		prplot.setGroup(currentIndex);
    		
   		ilplot.setRLinearModel(rlm);
   		ilplot.setGroup(currentIndex);
   		if(!isGLM) splot.setRLinearModel(rlm);
   		if(!isGLM) splot.setGroup(currentIndex);
   		jplot.setRLinearModel(rlm);
   		jplot.setGroup(currentIndex);
    		
   		loessRpt.setText(rplot.getLoessRpt());
   		regRpt.setText(rplot.getRegressionRpt());
   		anovaRpt.setText(rplot.getAnovaRpt());
    }
    
    public void actionPerformed(ActionEvent ae){
        String arg=ae.getActionCommand();
        Object src=ae.getSource();
        
        if(arg.equals("Reset")){
            this.reset();            
        }
        else if(arg.equals("Residuals vs Fitted values")){
        	showPlot(arg);
        }
        else if(arg.equals("Leverage Plot")){
        	showPlot(arg);
        }
        else if(arg.equals("Partial Residual Plot")){
        	showPlot(arg);
        }
        else if(arg.equals("Index Plot of Leverages") || arg.equals("Studentized Residuals") || arg.equals("Jacknife Residuals")){
        	showPlot(arg);
        }
        else if(arg.equals("ANOVA")){
    	    rplot.showLoessFit(false);
    	    rptCard.show(rptPanel, "0");
        }
        else if(arg.equals("Coefficients")){
        	rplot.showLoessFit(false);
            rptCard.show(rptPanel, "1");
        }
        else if(arg.equals("Loess Fit")){
            rplot.showLoessFit(true);
            rptCard.show(rptPanel, "2");  
        }
        else if(arg.equals("move loess")){
    		loessRpt.setText(rplot.getLoessRpt());
        }
        else if(arg.equals("Save Residuals")){
        	
        	data.deleteVariable("Residuals");
        	data.addVariable("Residuals", rlm.getResiduals());
        	
        }
        else if(arg.equals("Save Fitted values")){
        	
        	data.deleteVariable("Fitted values");
        	data.addVariable("Fitted values", rlm.getFittedValues());
        	
        }
        else if(arg.equals("Save Leverages")){
        	
        	data.deleteVariable("Leverages");
        	data.addVariable("Leverages", rlm.getHat());
        	
        }
        else if(arg.equals("Save Studentized Residuals")){
        	
        	data.deleteVariable("Studentized Residuals");
        	data.addVariable("Studentized Residuals", rlm.getStudRes());
        	
        }
        else if(arg.equals("Save Jacknife Residuals")){
        	
        	data.deleteVariable("Jacknife Residuals");
        	data.addVariable("Jacknife Residuals", rlm.getJackRes());
        	
        }
        else if(arg.equals("Color...")){
            Color c;
            c=JColorChooser.showDialog(this.getParent(), "Palette", Color.black);
            if(c!=null)
                changeColor(c);
        }
    	
    }
    
    public void update(String arg){
        
        if(arg.equals("yymask")||arg.equals("obs_deleted")){
            try
            {
            	if (grouper == null)
            		list = JRIClient.createDataFrame(data, y_var, vx, null, yname, xname);
            	else
            	{
            		boolean[] constrt = new boolean[data.getSize()]; 
            		for (int i = 0; i < data.getSize(); i++)
            			if (grouper.getGroupIndex(i).contains(new Integer(currentIndex)))
            				constrt[i] = true;
            			else
            				constrt[i] = false;
             				
            	    list = JRIClient.createDataFrame(data, y_var, vx, constrt, yname, xname);
            	}	
            		
           		rlm = JRIClient.fitLinearModel(formula, list, family, link);
            		
            }catch(RemoteException rex){
            	ComponentUtil.showErrorMessage(this, rex.getMessage());
                rlm.clear();
            }
            
            rplot.setRLinearModel(rlm);
            rplot.updatePlot(arg);
            if(!isGLM) lplot.setRLinearModel(rlm);
            if(!isGLM) lplot.updatePlot(arg);
            prplot.setRLinearModel(rlm);
            prplot.updatePlot(arg);
            
            ilplot.setRLinearModel(rlm);
            ilplot.updatePlot(arg);
            if(!isGLM) splot.setRLinearModel(rlm);
            if(!isGLM) splot.updatePlot(arg);
            jplot.setRLinearModel(rlm);
            jplot.updatePlot(arg);
            
            regRpt.setText(rplot.getRegressionRpt());
            loessRpt.setText(rplot.getLoessRpt());
            anovaRpt.setText(rplot.getAnovaRpt());
            
        }else{
            rplot.updatePlot(arg);
            if(!isGLM) lplot.updatePlot(arg);
            prplot.updatePlot(arg);
            ilplot.updatePlot(arg);
            if(!isGLM) splot.updatePlot(arg);
            jplot.updatePlot(arg);
        }
        
    }
    
    private class HyperlinkHandler implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent event) {
        	if (!event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                return;
        	lplot.setTerm(Integer.parseInt(event.getDescription()));
        	showPlot("Leverage Plot");
        }
    }
    
    protected class HyperlinkHandler1 implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent event) {
        	if (!event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                return;
        	prplot.setTerm(Integer.parseInt(event.getDescription()));
        	showPlot("Partial Residual Plot");
        }
    }

}
