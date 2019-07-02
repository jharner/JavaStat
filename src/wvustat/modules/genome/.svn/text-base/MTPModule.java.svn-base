package wvustat.modules.genome;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.rmi.*;
import java.util.*;

import wvustat.interfaces.*;
import wvustat.modules.*;
import wvustat.network.*;
import wvustat.network.client.*;
import wvustat.util.ComponentUtil;

public class MTPModule extends GUIModule implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private MTP mtp;
    private LineChart plot;
    private LineChart pplot;
    private LineChart splot;
    
    private JPanel plotPanel;
    private CardLayout plotCard;

    private JMenuBar jmb;
    private JMenu optionMenu;
    private JRadioButtonMenuItem plotMI, pplotMI, splotMI;

    private MTPRpt mtpRpt;

    /**
     * Constructor
     * Creates a MTPModule based on the object supplied
     *
     */
    public MTPModule(MTP mtp)
    {
        this.mtp = mtp;
        setBackground(Color.white);
            
        init();
    }
    

    /**
     * Build the menu bar, add components to this MTPModule object
     */
    private void init()
    {
        plot = new LineChart(mtp, LineChart.REJECTION_VS_PVALUE);
        plot.setGUIModule(this);
        mtp.addChangeListener(plot);
        
        pplot = new LineChart(mtp, LineChart.PVALUE_VS_REJECTION);
        pplot.setGUIModule(this);
        
        splot = new LineChart(mtp, LineChart.PVALUE_VS_STATISTICS);
        splot.setGUIModule(this);

        //Add plot panel to this component
        plotPanel=buildPlotPanel();
        
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weighty = 0.75;

        add(plotPanel, gbc);

        gbc.gridy++;
        gbc.weighty = 0.25;

        add(new JScrollPane(buildReportPanel()), gbc);


        jmb = new JMenuBar();
        JMenu plotMenu = new JMenu("Plot");
        
        ButtonGroup group=new ButtonGroup();
        
        plotMI = new JRadioButtonMenuItem("Rejections vs Error Rate");
        plotMI.addActionListener(this);
        group.add(plotMI);
        group.setSelected(plotMI.getModel(), true);
        plotMenu.add(plotMI);
        
        pplotMI = new JRadioButtonMenuItem("Sorted Adjusted p-values vs Rejections");
        pplotMI.addActionListener(this);
        group.add(pplotMI);
        plotMenu.add(pplotMI);
        
        splotMI = new JRadioButtonMenuItem("Adjusted p-values vs Statistics");
        splotMI.addActionListener(this);
        group.add(splotMI);
        plotMenu.add(splotMI);
        
        // Options Menu
        JMenu jm = new JMenu("Options");
        optionMenu = jm;
                
        JMenuItem SavGeneMI = new JMenuItem("Save Rejected Genes");
        SavGeneMI.addActionListener(this);
        optionMenu.add(SavGeneMI);
        
        SavGeneMI = new JMenuItem("Save Intensity of Rejected Genes (Gene As Row)");
        SavGeneMI.addActionListener(this);
        optionMenu.add(SavGeneMI);
        
        SavGeneMI = new JMenuItem("Save Annotation of Rejected Genes");
        SavGeneMI.addActionListener(this);
        optionMenu.add(SavGeneMI);
        
        optionMenu.addSeparator();
        
        JMenuItem SavArrayMI = new JMenuItem("Save Arrays");
        SavArrayMI.addActionListener(this);
        optionMenu.add(SavArrayMI);
        
        SavGeneMI = new JMenuItem("Save Intensity of Rejected Genes (Gene As Column)");
        SavGeneMI.addActionListener(this);
        optionMenu.add(SavGeneMI);
        

        jmb.add(plotMenu);

        JMenu analyzeMenu = new JMenu("Analyze");

        JRadioButtonMenuItem r1 = new JRadioButtonMenuItem("Summary", true);
        
        r1.addActionListener(this);
        
        ButtonGroup group2 = new ButtonGroup();
        group2.add(r1);

        analyzeMenu.add(r1);
               
        jmb.add(analyzeMenu);
    }


    /**
     * Get the menu bar contained in this object
     */
    public JMenuBar getJMenuBar()
    {
        return jmb;
    }
    
    public JMenu getOptionMenu()
    {
    	return optionMenu;
    }

    private JPanel buildPlotPanel(){
    	JPanel jp = new JPanel();
    	plotCard=new CardLayout();
        jp.setLayout(plotCard);
        
        jp.add(plot, "0");
        jp.add(pplot, "1");
        jp.add(splot, "2");
        
        return jp;
    }

    public void showPlot(String plotName)
    {
        if (plotName.equals("Rejections vs Error Rate")) 
        {
        	plotCard.show(plotPanel, "0");
        	plotMI.setSelected(true);
        }
        else if (plotName.equals("Sorted Adjusted p-values vs Rejections")) 
        {
        	plotCard.show(plotPanel, "1");
        	pplotMI.setSelected(true);
        }
        else if (plotName.equals("Adjusted p-values vs Statistics")) 
        {
        	plotCard.show(plotPanel, "2");
        	splotMI.setSelected(true);
        }
        
    }
    
    /**
     * Build ReportPanel for this MTPModule
     */
    private JPanel buildReportPanel()
    {
    	mtpRpt = new MTPRpt(mtp);
    	mtp.addChangeListener(mtpRpt);

        //mtpRpt.setPreferredSize(new Dimension(250, 100));

        return mtpRpt;
    }

    
    public void actionPerformed(ActionEvent ae)
    {
        String arg = ae.getActionCommand();

        if (arg.equals("Save Arrays")) {
        	ArrayList columns=new ArrayList();
        	ArrayList column=new ArrayList();
        	column.add("array");
        	column.add(String.class);
        	column.add(new Integer(DataSet.L_ROLE));
        	column.addAll(Arrays.asList(mtp.getSampNames()));
        	columns.add(column);
        	
        	column=new ArrayList();
        	column.add("type");
        	column.add(String.class);
        	column.add(new Integer(DataSet.Y_ROLE));
        	column.addAll(Arrays.asList(mtp.getSampTypes()));
        	columns.add(column);
        	
        	GDataSet gdata = new GDataSet("arrays of " + mtp.getObjName(), columns);
        	this.getMainPanel().callback(gdata);
        	
        }
        else if (arg.equals("Save Rejected Genes"))
        {
        	ArrayList columns=new ArrayList();
        	
        	ArrayList columnOne=new ArrayList();
        	columnOne.add("gene");
        	columnOne.add(String.class);
        	columnOne.add(new Integer(DataSet.L_ROLE));
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		columnOne.add(mtp.getRejectedGenes()[i]);
        	}
        	columns.add(columnOne);
        	
        	ArrayList columnTwo=new ArrayList();
        	columnTwo.add("adjp");
        	columnTwo.add(Double.class);
        	columnTwo.add(new Integer(DataSet.Y_ROLE));
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		columnTwo.add(new Double(mtp.getRejectedAdjp()[i]));
        	}
        	columns.add(columnTwo);
        	
        	ArrayList columnThree=new ArrayList();
        	columnThree.add("rawp");
        	columnThree.add(Double.class);
        	columnThree.add(new Integer(DataSet.Y_ROLE));
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		columnThree.add(new Double(mtp.getRejectedRawp()[i]));
        	}
        	columns.add(columnThree);
        	
        	ArrayList column4=new ArrayList();
        	column4.add("log2fc");
        	column4.add(Double.class);
        	column4.add(new Integer(DataSet.Y_ROLE));
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		column4.add(new Double(mtp.getRejectedLog2fc()[i]));
        	}
        	columns.add(column4);
        	
        	GDataSet gdata = new GDataSet("rejected genes of " + mtp.getObjName(), columns);
        	
        	this.getMainPanel().callback(gdata);
        	
        }
        else if (arg.equals("Save Intensity of Rejected Genes (Gene As Column)"))
        {
        	ArrayList geneNames = new ArrayList();
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		geneNames.add(mtp.getRejectedGenes()[i]);
        	}
        	
        	try
        	{
        		JRIClient.getGeneIntensityAsColumnByNames(getMainPanel(), mtp.getExprObjName(), geneNames);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        else if (arg.equals("Save Intensity of Rejected Genes (Gene As Row)"))
        {
        	ArrayList geneNames = new ArrayList();
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		geneNames.add(mtp.getRejectedGenes()[i]);
        	}
        	
        	try
        	{
        		JRIClient.getGeneIntensityAsRowByNames(getMainPanel(), mtp.getExprObjName(), geneNames);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        else if (arg.equals("Save Annotation of Rejected Genes"))
        {
        	ArrayList geneNames = new ArrayList();
        	for (int i = 0; i < mtp.getNumRejection(); i++){
        		geneNames.add(mtp.getRejectedGenes()[i]);
        	}
        	
        	try
        	{
        		JRIClient.getGeneAnnotationByNames(getMainPanel(), "AFFYMETRIX_3PRIME_IVT_ID", geneNames);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        else if (arg.equals("Rejections vs Error Rate") || arg.equals("Sorted Adjusted p-values vs Rejections") ||
        		arg.equals("Adjusted p-values vs Statistics")) 
        {
        	showPlot(arg);
        }

    }

}
