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


public class ExprSetModule extends GUIModule implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private exprSet eset;
    private BoxPlot plot;

    private JMenuBar jmb;
    private JMenu optionMenu;
    private JCheckBoxMenuItem scaleMI;

    /**
     * Constructor
     * Creates a ExprSetModule based on the object supplied
     *
     */
    public ExprSetModule(exprSet eset)
    {
        this.eset = eset;
        setBackground(Color.white);
            
        init();
    }

    
    /**
     * Build the menu bar, add components to this ExprSetModule object
     */
    private void init()
    {
        plot = new BoxPlot(eset);
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
        JMenuItem SavAllGeneMI = new JMenuItem("Save All Gene Intensity");
        SavAllGeneMI.addActionListener(this);
        
        optionMenu.add(SavArrayMI);
        optionMenu.add(SavGeneMI);
        optionMenu.add(SavOneGeneMI);
        optionMenu.add(SavAllGeneMI);

        //plotMenu.add(jm);

        jmb.add(plotMenu);

        JMenu analyzeMenu = new JMenu("Analyze");

        JRadioButtonMenuItem r1 = new JRadioButtonMenuItem("Summary", true);
        
        r1.addActionListener(this);
        
        ButtonGroup group = new ButtonGroup();
        group.add(r1);

        analyzeMenu.add(r1);
        analyzeMenu.addSeparator();
        
        JMenuItem diffMI = new JMenuItem("Differential Gene Expression...");
        diffMI.addActionListener(this);
        analyzeMenu.add(diffMI);
        
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
     * Build ReportPanel for this ExprSetModule
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
        	column.addAll(Arrays.asList(eset.getSampleNames()));
        	columns.add(column);
        	GDataSet gdata = new GDataSet("arrays of " + eset.getObjName(), columns);
        	this.getMainPanel().callback(gdata);
        	
        }
        else if (arg.equals("Save Genes"))
        {
        	try
        	{
        		JRIClient.getGeneNames(getMainPanel(), eset.getObjName());
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
        		JRIClient.getGeneIntensity(getMainPanel(), eset.getObjName(), input.trim());
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        	
        }
        else if (arg.equals("Save All Gene Intensity"))
        {
        	try
        	{
        		JRIClient.getGeneIntensity(getMainPanel(), eset.getObjName(), null);
        	}
        	catch(RemoteException rex){
        		ComponentUtil.showErrorMessage(this, rex.getMessage());
        	}
        }
        else if (arg.equals("Differential Gene Expression..."))
        {
        	MultTestDialog md = new MultTestDialog(ComponentUtil.getTopComponent(this), this.getMainPanel(), eset.getObjName(), eset.getSampleNames());
        	md.setLocationRelativeTo(this);
        	md.pack();
        	md.show();        	
        }
        
    }

    
    
    protected String getSummaryRpt()
    {
    	StringBuffer buf = new StringBuffer();
    	buf.append("<html><body><table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n");
    	buf.append("<tr><td width=\"30%\"><font size=3>Name of Object</font></td>");
        buf.append("<td width=\"70%\"><font size=3>").append(eset.getObjName()).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>Number of Gene</font></td>");
        buf.append("<td><font size=3>").append(String.valueOf(eset.getNumGene())).append("</font></td></tr>");
        buf.append("<tr><td><font size=3>Number of Sample</font></td>");
        buf.append("<td><font size=3>").append(String.valueOf(eset.getNumSample())).append("</font></td></tr>");
        buf.append("</table>\n");
        buf.append("</body></html>");
        return buf.toString();
    }
}
