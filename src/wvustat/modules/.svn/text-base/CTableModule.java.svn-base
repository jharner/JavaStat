package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;
import java.text.NumberFormat;

import wvustat.interfaces.*;
import wvustat.swing.*;
import wvustat.dist.Normal;

public class CTableModule extends GUIModule implements ActionListener, ChangeListener, Runnable {
	private static final long serialVersionUID = 1L;
	
	private Variable x_var, y_var, z_var;
    private Vector vz;

    //private GroupChooser chooser;
    private OverlapSlicer chooser;

    private MosaicPlot plot;
    private CATour caTour;

    private JMenuBar jmb;
    private JMenu optionMenu;

    private Chi2Report report;
    
    private CardLayout rptCard=new CardLayout();
    private JPanel reportPanel;
    
    private JInternalFrame contTable;
    private JFrame contTableOfApplet; //added for applet
    
    private TwoWayLabel tableLabel;
    
    private CardLayout plotCard = new CardLayout();
    private JPanel plotPanel;
    
    //For Tour
    private boolean quit = false;
	private boolean pause = true;
	private int speedInterval = 100;
	private JCheckBoxMenuItem hideAxisMI;
	private JMenuItem scaleMI;
	private JRadioButtonMenuItem plotMI, caTourMI;


    public CTableModule(DataSet data){
        this.data=data;
                
        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));

        Vector v = data.getYVariables();
        y_var = (Variable) v.elementAt(0);
        v = data.getXVariables();
        x_var = (Variable) v.elementAt(0);
        vz = data.getZVariables();
        if (vz.size() > 0)
            z_var = (Variable) vz.elementAt(0);

        init();
        
        (new Thread(this)).start();
    }
    
    public CTableModule(DataSet data, Variable yVar, Variable xVar, Variable zVar){
        this.data=data;
        this.y_var=yVar;
        this.x_var=xVar;
        this.z_var=zVar;
        
        this.vz = new Vector();
        this.vz.addElement(z_var);
        
        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));

        init();
        
        (new Thread(this)).start();
    }    

    public CTableModule(DataSet data, Variable yVar, Variable xVar, Vector vz){
        this.data=data;
        this.y_var=yVar;
        this.x_var=xVar;
        this.vz=vz;
        
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        
        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));

        init();
        
        (new Thread(this)).start();
    }
    
    public CTableModule(DataSet data, PlotMetaData pmd){
        this.data=data;
        this.metaData=pmd;

        setBackground(Color.white);
        setBorder(new LineBorder(Color.black));

        y_var=metaData.getYVariable();
        x_var=metaData.getXVariable();
        z_var=metaData.getZVariable();
        
        this.vz = new Vector();
        this.vz.addElement(z_var);

        init();
        
        (new Thread(this)).start();
    }

    private void init(){
        
        data.addRemoteObserver(this);
        
        plot=new MosaicPlot(data, x_var, y_var, vz);
        plot.setGUIModule(this);

        caTour = new CATour(plot.getFreqMatrix(), plot.getXLevels(), plot.getYLevels());
        caTour.setGUIModule(this);
        
        setLayout(new GridBagLayout());

        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.weightx=1.0;
        gbc.fill=GridBagConstraints.BOTH;

        JPanel jp=buildControlPanel();
        gbc.insets=new Insets(2,10,2,2);

        if(jp!=null){
            add(buildControlPanel(), gbc);
            gbc.gridy++;
        }

        gbc.insets=new Insets(0,0,0,0);
        gbc.weighty=0.8;
        plotPanel = buildPlotPanel();
        add(plotPanel, gbc);
        gbc.gridy++;
        gbc.weighty=0.2;

        reportPanel=buildReportPanel();
        add(reportPanel, gbc);

        jmb=new JMenuBar();

        JMenu plotMenu=new JMenu("Plot");

        ButtonGroup grp = new ButtonGroup();
        
        JRadioButtonMenuItem jri=new JRadioButtonMenuItem("Mosaic", true);
        plotMI = jri;
        jri.addActionListener(this);
        grp.add(jri);
        grp.setSelected(jri.getModel(), true);
        plotMenu.add(jri);
        
        jri = new JRadioButtonMenuItem("CA Tour");
        caTourMI = jri;
        jri.addActionListener(this);
        grp.add(jri);
        plotMenu.add(jri);
        plotMenu.addSeparator();
        
        JMenu jm=new JMenu("Options");
        this.optionMenu = jm;
        JMenuItem item=jm.add("Contingency table");
        item.addActionListener(this);

        hideAxisMI = new JCheckBoxMenuItem("Hide Axis", false);
        hideAxisMI.addActionListener(this);
        optionMenu.add(hideAxisMI);
        
        scaleMI = new JMenuItem("Set Scale...");
        scaleMI.addActionListener(this);
        optionMenu.add(scaleMI);
        
        plotMenu.add(optionMenu);

        jmb.add(plotMenu);

        JMenu analyzeMenu=new JMenu("Analyze");

        JRadioButtonMenuItem r1=new JRadioButtonMenuItem("Independence Test",true);
        r1.setActionCommand("INDEPENDENCE_TEST");
        JRadioButtonMenuItem r2=new JRadioButtonMenuItem("Proportion Test",false);
        r2.setActionCommand("PROPORTION_TEST");
        JRadioButtonMenuItem r3=new JRadioButtonMenuItem("Z-test", false);
        r3.setActionCommand("Z_TEST");
        r1.addActionListener(this);
        r2.addActionListener(this);
        r3.addActionListener(this);

        ButtonGroup group=new ButtonGroup();
        group.add(r1);
        group.add(r2);
        group.add(r3);

        analyzeMenu.add(r1);
        analyzeMenu.addSeparator();
        analyzeMenu.add(r2);
        analyzeMenu.add(r3);

        jmb.add(analyzeMenu);

        if(x_var.getDistinctCatValues().length==2 && y_var.getDistinctCatValues().length==2)
            r3.setEnabled(true);
        else
            r3.setEnabled(false);
    }

    private JPanel buildControlPanel(){
        if(z_var==null)
        	return null;
        
        JPanel jp=new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new StackLayout());

        EqualCountGrouper grouper=plot.getGroupMaker();
        chooser=new OverlapSlicer(grouper);
        chooser.addChangeListener(this);
        jp.add(chooser);
        
        return jp;        
    }

    private JPanel buildPlotPanel() {
    	JPanel jp = new JPanel();
        jp.setLayout(plotCard);
        jp.add("Mosaic", plot);
        jp.add("CA Tour", caTour);
        return jp;
    }
    
    private JPanel buildReportPanel(){
        JPanel jp=new JPanel();
        jp.setLayout(rptCard);
        
        String[] zlevels=null;

        if(z_var!=null){    
            //zlevels=z_var.getDistinctCatValues();
            zlevels = plot.getGroupMaker().getGroupNames();
        }

        report=new Chi2Report(plot, zlevels);
        jp.add("1", report);

        MultinomialReport rpt=new MultinomialReport(plot.getFreqMatrix(), plot.getRowSums(), plot.getColSums());
        jp.add("2", new JScrollPane(rpt));

        ReportPanel zTestReport=new ReportPanel();
        if (x_var.getDistinctCatValues().length == 2 && y_var.getDistinctCatValues().length == 2) {
            jp.add("3", new JScrollPane(zTestReport));
            zTestReport.setText(composeZTest());
        }
        rptCard.show(jp,"1");
        return jp;
    }

    private String composeZTest(){
        double[][] data=plot.getFreqMatrix();

        double[] xArray=plot.getRowSums();
        double p1=data[0][0]/xArray[0];
        double p2=data[1][0]/xArray[1];
        double pHat= (data[0][0]+data[1][0])/(xArray[0]+xArray[1]);
        double z=(p1-p2)/Math.sqrt(pHat*(1-pHat)*(1/xArray[0]+1/xArray[1]));
        Normal normal = new Normal(0, 1);
        double pLeft = normal.cdf(z);
        double pRight = 1 - pLeft;
        double pTwoside = 0;
        if (pLeft < 0.5)
            pTwoside = 2 * pLeft;
        else
            pTwoside = 2 * pRight;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(4);
        StringBuffer buffer=new StringBuffer();
        buffer.append("<html>\n");
        buffer.append("<table>\n");
        buffer.append("<tr><td>").append("H<sub>0</sub>: p<sub>11</sub>=p<sub>12</sub>  and p<sub>21</sub>=p<sub>22</sub>").append("</td></tr>\n");
        buffer.append("<tr><td>").append("Z-value:").append("</td><td>");
        buffer.append(nf.format(z)).append("</td></tr></n>");
        buffer.append("<tr><td>").append("P(|Z| &gt Z-value):").append("</td><td>");
        buffer.append(nf.format(pTwoside)).append("</td></tr></n>");
        buffer.append("<tr><td>").append("P(Z &gt Z-value):").append("</td><td>");
        buffer.append(nf.format(pRight)).append("</td></tr></n>");
        buffer.append("<tr><td>").append("P(Z &lt Z-value):").append("</td><td>");
        buffer.append(nf.format(pLeft)).append("</td></tr></n>");
        buffer.append("</table></html>\n");
        return buffer.toString();
    }


    public void update(String msg){
        //if("obs_deleted".equals(msg) || "yymask".equals(msg) ){
            plot.updatePlot(msg);
            report.updateReport();
            
            if ("obs_deleted".equals(msg) || "yymask".equals(msg)) {
            	caTour.setData(plot.getFreqMatrix(), plot.getXLevels(), plot.getYLevels());
            	//caTour.updatePlot(msg);
            }
        //}
    }


    public void createContTable(){
    	
    	if(contTable==null)
    	{
    		contTable=new JInternalFrame();
    		JMenuBar mBar=new JMenuBar();
        	JMenu jm1=new JMenu("Display");
        
        	JRadioButtonMenuItem jr1=new JRadioButtonMenuItem("Count",true);
        	JRadioButtonMenuItem jr2=new JRadioButtonMenuItem("Total %", false);
        	JRadioButtonMenuItem jr3=new JRadioButtonMenuItem("Row %", false);
        	JRadioButtonMenuItem jr4=new JRadioButtonMenuItem("Col %", false);
        	JRadioButtonMenuItem jr5=new JRadioButtonMenuItem("Cell Chi-Square", false);
        
        	ButtonGroup group=new ButtonGroup();
        	group.add(jr1);
        	group.add(jr2);
        	group.add(jr3);
        	group.add(jr4);
        	group.add(jr5);
        
        	jr1.addActionListener(this);
        	jr2.addActionListener(this);
        	jr3.addActionListener(this);
        	jr4.addActionListener(this);
        	jr5.addActionListener(this);
        
        	jm1.add(jr1);
        	jm1.add(jr2);
        	jm1.add(jr3);
        	jm1.add(jr4);
        	jm1.add(jr5);
        
        	mBar.add(jm1);
        
        	contTable.setJMenuBar(mBar);
        	contTable.setClosable(true);
            contTable.setResizable(true);

        	Container jComp=this.getParent();
        	while (!(jComp instanceof JDesktopPane)) {
        		if (jComp == null) break;
        		jComp=jComp.getParent();
        	}
        
        	if (jComp != null)
        		jComp.add(contTable);	
        	
        	contTable.addInternalFrameListener(new FrameListener());
        	
        	addDependentFrame(contTable);
        }   
        
        String cTitle="Contigency Table";
        if(z_var!=null)
        	cTitle+=" for "+plot.getGroupName();
        	
        contTable.setTitle(cTitle);        
        contTable.setContentPane(buildTable());        
        contTable.pack();
        
        Point pt=this.getLocation();
        contTable.setLocation(pt.x+20,pt.y+20);
        
        //contTable.show();
    }

    /* 
     * This method is used to adapt contingency table rendering to Applet environment.
     * Developed for myJavaPlots Applet by djluo on 11/9/2005 
     * 
     */
    public void createContTableOfApplet(){
    	
    	if(contTableOfApplet==null)
    		contTableOfApplet=new JFrame();
    	
    	createContTable();
    	contTableOfApplet.setContentPane(contTable.getContentPane());      
        contTableOfApplet.setJMenuBar(contTable.getJMenuBar());
        contTableOfApplet.setTitle(contTable.getTitle());
        contTableOfApplet.setResizable(true);
        contTableOfApplet.pack();
        Point pt=this.getLocation();
        contTableOfApplet.setLocation(pt.x+20,pt.y+20);   
    }
    
    private JPanel buildTable(){

        TableModel model=plot.getTableModel();

        JTable table=new JTable(model);

        String xlabel="x";
        String ylabel="y";

        xlabel = x_var.getName();
        ylabel = y_var.getName();

        TwoWayLabel label1=new TwoWayLabel(xlabel, true);
        TwoWayLabel label2=new TwoWayLabel(ylabel+" (Count)", false);

		tableLabel=label2;

        JPanel jp=new JPanel();

        jp.setLayout(new GridBagLayout());

        GridBagConstraints gbc=new GridBagConstraints();

        gbc.gridx=1;
        gbc.gridy=0;
        gbc.gridwidth=1;
        gbc.anchor=GridBagConstraints.CENTER;
        //gbc.fill=GridBagConstraints.HORIZONTAL;

        jp.add(label2, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        //gbc.fill=GridBagConstraints.VERTICAL;
        jp.add(label1, gbc);

        gbc.gridx=1;
        gbc.fill=GridBagConstraints.BOTH;

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Dimension d=table.getPreferredSize();
        table.setPreferredScrollableViewportSize(new Dimension(d.width+40, d.height+40));
        JScrollPane jsp=new JScrollPane(table);
        jsp.setColumnHeaderView(table.getTableHeader());
        jsp.getColumnHeader().setPreferredSize(new Dimension(d.width+40,30));
        //Dimension d=table.getPreferredSize();
        //jsp.setPreferredSize(new Dimension(d.width+40, d.height+40));
        gbc.gridwidth=GridBagConstraints.REMAINDER;
        gbc.gridheight=GridBagConstraints.REMAINDER;
        gbc.weightx=1.0;
        gbc.weighty=1.0;
        jp.add(jsp, gbc);

        return jp;

    }

    public void actionPerformed(ActionEvent ae){
        String arg=ae.getActionCommand();

        if(arg.equals("Contingency table")){
        	//this.showContTable();
    			
    		Container jComp=this.getParent();
    		while(!(jComp instanceof JDesktopPane)){
    			if(jComp == null) break;
    			jComp=jComp.getParent();        			
    		}
    		
    		if(jComp != null) {
    			this.createContTable();
    			contTable.show();
    		} else {
    			this.createContTableOfApplet();
    			contTableOfApplet.show();
    		}
        
        }
        else if(arg.equals("INDEPENDENCE_TEST")){
            rptCard.show(reportPanel,"1");
        }
        else if(arg.equals("PROPORTION_TEST")){
            rptCard.show(reportPanel,"2");
        }
        else if(arg.equals("Z_TEST")){
            rptCard.show(reportPanel, "3");
        }
        else if(arg.equals("Count")){
            plot.setDisplayOption(MosaicPlot.SHOW_COUNT);
            tableLabel.setText(y_var.getName()+" (Count)");
        }
        else if(arg.equals("Total %")){
            plot.setDisplayOption(MosaicPlot.SHOW_TOTAL_P);
			tableLabel.setText(y_var.getName()+" (Total %)");
        }
        else if(arg.equals("Row %")){
            plot.setDisplayOption(MosaicPlot.SHOW_ROW_P);
            tableLabel.setText(y_var.getName()+" (Row %)");
        }
        else if(arg.equals("Col %")){
            plot.setDisplayOption(MosaicPlot.SHOW_COL_P);
            tableLabel.setText(y_var.getName()+" (Col %)");
        }
        else if(arg.equals("Cell Chi-Square")){
            plot.setDisplayOption(MosaicPlot.SHOW_CELL_CHI2);
            tableLabel.setText(y_var.getName()+" (Cell Chi-Square)");
        }
        else if(arg.equals("Mosaic")){
        	plotCard.show(plotPanel, "Mosaic");
        }
        else if(arg.equals("CA Tour")){
        	plotCard.show(plotPanel, "CA Tour");
        }
        else if (arg.equals("Hide Axis"))
        {
            caTour.enableAxisDrawing(!hideAxisMI.isSelected());
        
        } 
        else if (arg.equals("Set Scale..."))
        {
        	ScaleConfigPanel scalePanel = new ScaleConfigPanel(0, 1, caTour.getScale());
        	int option=JOptionPane.showOptionDialog(this, scalePanel, "Choose scale", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        	if(option==JOptionPane.OK_OPTION)
        		caTour.setScale(scalePanel.getValue());
        }
    }

    public JMenuBar getJMenuBar(){
        return jmb;
    }
    
    public JMenu getOptionMenu()
    {
    	return optionMenu;
    }
    
    public void setGroup(Integer index){
        chooser.setCurrentIndex(index.intValue());
        plot.setGroup(index.intValue());

        caTour.setData(plot.getFreqMatrix(), plot.getXLevels(), plot.getYLevels());
        
        report.setGroup(index.intValue());
        reportPanel.removeAll();
        reportPanel.add("1", report);
        
        MultinomialReport rpt=new MultinomialReport(plot.getFreqMatrix(), plot.getRowSums(), plot.getColSums());
        reportPanel.add("2", new JScrollPane(rpt));
        
        ReportPanel zTestReport=new ReportPanel();
        if (x_var.getDistinctCatValues().length == 2 &&
                y_var.getDistinctCatValues().length == 2) {
            reportPanel.add("3", new JScrollPane(zTestReport));
            zTestReport.setText(composeZTest());
        }
        
        reportPanel.validate();
        rptCard.show(reportPanel, "1");

        if(contTable!=null){
        	String cTitle="Contigency Table";
        	if(z_var!=null)
        		cTitle+=" for "+plot.getGroupName();
        	
        	contTable.setTitle(cTitle);
        	contTable.validate();
        }
        
        if(contTableOfApplet!=null){
        	contTableOfApplet.setTitle(contTable.getTitle());
        	contTableOfApplet.validate();
        }
    }
    
    public void stateChanged(final javax.swing.event.ChangeEvent p1) {
       if(p1.getSource() instanceof OverlapSlicer){
            Integer val=new Integer(chooser.getCurrentIndex());
            this.setGroup(val);
        }    
       else if (p1.getSource() instanceof StopControl)
		{
			StopControl ctrl = (StopControl)p1.getSource();
			if (pause && (checkTourBoxTypes() == false)) 
				return;
			
			pause = !pause;
			ctrl.setStopped(pause);
			scaleMI.setEnabled(pause);
	    	caTour.setTourBoxStatus(pause);
		}
		else if (p1.getSource() instanceof SimpleSlider)
		{
			SimpleSlider slider = (SimpleSlider)p1.getSource();
			this.speedInterval = (int)(slider.getMaximum() + slider.getMinimum() - slider.getValue());
		}
    }
    
    class FrameListener extends InternalFrameAdapter{
    	public void internalFrameClosing(InternalFrameEvent evt){
    		dependentFrames.clear();
    		contTable=null;
    	}
    }
    
    public void run(){
		while (!quit) {
			try
	        {
	            Thread.sleep(speedInterval); // millisecond
	        }
	        catch (InterruptedException e){}
	        
	        if (pause) continue;
	        
	        caTour.rotatePlot();
		}
	}
	
	public void destroy(){
		super.destroy();
		quit = true;
	}
	
	private boolean checkTourBoxTypes() {
		int[] types = caTour.getTourBoxTypes();
		int na, nx, ny, no;
		na = nx = ny = no = 0;
		
		for (int i = 0; i < types.length; i++) {
			switch (types[i]) {
				case TourBox.A: na++; break;
				case TourBox.X: nx++; break;
				case TourBox.Y: ny++; break;
				case TourBox.O: no++; break;
			}
		}
		
		if ((nx > 0 && ny == 0) || (nx == 0 && ny > 0)) {
			JOptionPane.showMessageDialog(this, "Both X and Y variables are required!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if ((na + nx + ny) < 2) {
			JOptionPane.showMessageDialog(this, "Too few active variables!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}
