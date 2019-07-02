package wvustat.modules.genome;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import javax.swing.*;

import java.util.*;

import wvustat.interfaces.*;
import wvustat.modules.*;
import wvustat.network.*;
import wvustat.network.client.*;
import wvustat.util.ComponentUtil;

/**
 *	MArrayModule is a bundled component that displays intensities of an AffyBatch object that 
 *  consists of k microarrays in a plot. It contains a menu bar, a BoxPlot object, a SummaryRpt object.
 *
 */
public class MArrayModule extends GUIModule implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private AffyBatch affy;
    private BoxPlot plot;

    private JMenuBar jmb;
    private JMenu optionMenu;
    private JCheckBoxMenuItem scaleMI;

    /**
     * Constructor
     * Creates a MArrayModule based on the object supplied
     *
     */
    public MArrayModule(AffyBatch affy)
    {
        this.affy = affy;
        setBackground(Color.white);
            
        init();
    }


    /**
     * Build the menu bar, add components to this MArrayModule object
     */
    private void init()
    {
        plot = new BoxPlot(affy);
        plot.setGUIModule(this);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weighty = 0.8;

        add(plot, gbc);

        gbc.gridy++;
        gbc.weighty = 0.2;

        add(buildReportPanel(), gbc);


        jmb = new JMenuBar();
        JMenu plotMenu = new JMenu("Plot");

        scaleMI = new JCheckBoxMenuItem("log2 scale");
        scaleMI.setSelected(plot.isLog2scale());
        scaleMI.setEnabled(plot.isLog2enabled());
        scaleMI.addActionListener(this);
        plotMenu.add(scaleMI);

        JMenu jm = new JMenu("Options");
        optionMenu = jm;
        
        JMenuItem SavArrayMI = new JMenuItem("Save Arrays");
        SavArrayMI.addActionListener(this);
        JMenuItem SavGeneMI = new JMenuItem("Save Genes");
        SavGeneMI.addActionListener(this);
        JMenuItem SavOneGeneMI = new JMenuItem("Save Gene Intensity...");
        SavOneGeneMI.addActionListener(this);
        
        optionMenu.add(SavArrayMI);
        optionMenu.add(SavGeneMI);
        optionMenu.add(SavOneGeneMI);

        //plotMenu.add(jm);

        jmb.add(plotMenu);

        JMenu analyzeMenu = new JMenu("Analyze");

        JRadioButtonMenuItem r1 = new JRadioButtonMenuItem("Summary", true);
        
        r1.addActionListener(this);
        
        ButtonGroup group = new ButtonGroup();
        group.add(r1);

        analyzeMenu.add(r1);
        analyzeMenu.addSeparator();
        
        JMenuItem bgMI = new JMenuItem("Background adjustment...");
        bgMI.addActionListener(this);
        JMenuItem normMI = new JMenuItem("Normalization...");
        normMI.addActionListener(this);
        JMenuItem sumMI = new JMenuItem("Summarization...");
        sumMI.addActionListener(this);
        JMenuItem gcrmaMI = new JMenuItem("GCRMA");
        gcrmaMI.addActionListener(this);
        
        analyzeMenu.add(bgMI);
        analyzeMenu.add(normMI);
        analyzeMenu.add(sumMI);
        analyzeMenu.add(gcrmaMI);
        
        StringTokenizer st = new StringTokenizer(affy.getObjName(), ".");
        switch (st.countTokens()) {
        	case 1: 
        		bgMI.setEnabled(true); 
        		normMI.setEnabled(false);
        		sumMI.setEnabled(false);
        		gcrmaMI.setEnabled(true);
        		break;
        	case 2:
        		bgMI.setEnabled(false); 
        		normMI.setEnabled(true);
        		sumMI.setEnabled(false);
        		gcrmaMI.setEnabled(false);
        		break;
        	case 3:
        		bgMI.setEnabled(false); 
        		normMI.setEnabled(false);
        		sumMI.setEnabled(true);
        		gcrmaMI.setEnabled(false);
        		break;
        	default:
        		bgMI.setEnabled(false); 
    			normMI.setEnabled(false);
    			sumMI.setEnabled(false);
    			gcrmaMI.setEnabled(false);
    			break;
        }
                	
        
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


    /**
     * Build ReportPanel for this MArrayModule
     */
    private JPanel buildReportPanel()
    {
    	JPanel jp=new JPanel(new BorderLayout());
    	
    	JTextPane summaryRpt = new JTextPane();
    	summaryRpt.setMargin(new Insets(2,4,10,8));
        summaryRpt.setEditable(false);
        summaryRpt.setContentType("text/html");
        summaryRpt.setText(getSummaryRpt());
        jp.add(new JScrollPane(summaryRpt), BorderLayout.CENTER);
        
        return jp;
    }

    
    public void actionPerformed(ActionEvent ae)
    {
        String arg = ae.getActionCommand();

        if (arg.equals("log2 scale"))
        {
        	plot.setLog2scale(scaleMI.isSelected());
        }
        else if (arg.equals("Save Arrays"))
        {
        	ArrayList columns=new ArrayList();
        	ArrayList column=new ArrayList();
        	column.add("array");
        	column.add(String.class);
        	column.add(new Integer(DataSet.L_ROLE));
        	column.addAll(Arrays.asList(affy.getSampleNames()));
        	columns.add(column);
        	GDataSet gdata = new GDataSet("arrays of " + affy.getObjName(), columns);
        	this.getMainPanel().callback(gdata);
        	
        }
        else if (arg.equals("Save Genes"))
        {
        	try
        	{
        		JRIClient.getGeneNames(getMainPanel(), affy.getObjName());
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        else if (arg.equals("Save Gene Intensity..."))
        {
        	String input = JOptionPane.showInputDialog(this, "The name of gene to save:", "Input", JOptionPane.INFORMATION_MESSAGE);
        	if (input == null || input.trim().equals(""))
        		return;
        	
        	try
        	{
        		JRIClient.getGeneIntensity(getMainPanel(), affy.getObjName(), input.trim());
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        	
        }
        else if (arg.equals("Background adjustment...") || 
        		arg.equals("Normalization...") ||
        		arg.equals("Summarization..."))
        {
        	
        	int type;
        	
        	if (arg.equals("Background adjustment..."))
        		type = PreprocessDialog.BGCORRECT;
        	else if (arg.equals("Normalization..."))
        		type = PreprocessDialog.NORMALIZE;
        	else 
        		type = PreprocessDialog.SUMMARY;
        		
        	PreprocessDialog pd = new PreprocessDialog(ComponentUtil.getTopComponent(this), type);
        	pd.setLocationRelativeTo(this);
        	pd.pack();
        	pd.show();
        	if (pd.getOption() == PreprocessDialog.OK_OPTION)
        	{
        		try
            	{
            		JRIClient.preprocess(getMainPanel(), affy.getObjName(), pd.getBgcorrectMethod(),
            							pd.getNormalizeMethod(), pd.getPmcorrectMethod(), pd.getSummaryMethod());
            	}
            	catch(RemoteException rex){
            		ComponentUtil.showErrorMessage(this, rex.getMessage());
            	}
        	}
        	
        }
        else if (arg.equals("GCRMA")) {
        	try {
        		JRIClient.gcrma(getMainPanel(), affy.getObjName());
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        
    }

    
    
    protected String getSummaryRpt()
    {
    	StringBuffer buf = new StringBuffer();
    	buf.append("<html><body><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n");
    	buf.append("<tr><td width=\"30%\"><font size=3>Name of Object</font></td>");
        buf.append("<td width=\"70%\"><font size=3>").append(affy.getObjName()).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>Number of Gene</font></td>");
        buf.append("<td><font size=3>").append(String.valueOf(affy.getNumGene())).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>Number of Probe</font></td>");
        buf.append("<td><font size=3>").append(String.valueOf(affy.getNumProbe())).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>Number of Sample</font></td>");
        buf.append("<td><font size=3>").append(String.valueOf(affy.getNumSample())).append("</font></td></tr>");
        buf.append("</table>\n");
        buf.append("</body></html>");
        return buf.toString();
    }
}
