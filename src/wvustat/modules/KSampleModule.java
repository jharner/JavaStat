package wvustat.modules;


//Import java packages
import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;


//Import self defined packages
import wvustat.interfaces.*;
import wvustat.swing.StackLayout;

/**
 *	KSampleModule is a bundled component that displays a data set consisting of k groups in a plot. It contains
 * a menu bar, a DotPlot object, a EffectTestRpt object, a MeansRpt object and a PairCIRpt object.
 *
 */
public class KSampleModule extends GUIModule implements ChangeListener, ActionListener
{

    private Variable y_var, x_var, z_var;
    private Vector vz;

    private DotPlot plot;

    private OverlapSlicer chooser;

    //Adapter to connect this class and the remote data set

    private Palette palette;

    private Vector varNames = new Vector();
    private JMenuBar jmb;
    private JCheckBoxMenuItem meanChk, ciChk, boxChk;
    private JMenuItem mShowPoints;
    private JMenu optionMenu;

    private ReportBoard rptBoard;


    /**
     * Constructor
     * Creates a KSampleModule based on the data supplied
     *
     * @param data the data set this KSampleModule will be based on
     * @param y_var the y variable within the data set
     * @param x_var the x variable within the data set
     * @param z_var the z variable within the data set
     */
    public KSampleModule(DataSet data, Variable y_var, Variable x_var, Variable z_var)
    {
        this.data = data;
        this.y_var = y_var;
        this.x_var = x_var;
        this.z_var = z_var;
        
        this.vz = new Vector();
        this.vz.addElement(z_var);

        setBackground(Color.white);

            if (x_var == null)
                throw new IllegalArgumentException("Can't do k sample analysis without x variable");
            else if (x_var.getType() == Variable.NUMERIC)
                throw new IllegalArgumentException("X variable cannot be numeric");

            if (y_var == null)
                throw new IllegalArgumentException("Can't do regression analysis without y variable");
            else if (y_var.getType() != Variable.NUMERIC)
                throw new IllegalArgumentException("Y variable can not be categorical");

            varNames.addElement(x_var.getName());
            varNames.addElement(y_var.getName());
            if (z_var != null)
                varNames.addElement(z_var.getName());
            
        init();
    }

    public KSampleModule(DataSet data, Variable y_var, Variable x_var, Vector vz)
    {
        this.data = data;
        this.y_var = y_var;
        this.x_var = x_var;

        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);

        setBackground(Color.white);

            if (x_var == null)
                throw new IllegalArgumentException("Can't do k sample analysis without x variable");
            else if (x_var.getType() == Variable.NUMERIC)
                throw new IllegalArgumentException("X variable cannot be numeric");

            if (y_var == null)
                throw new IllegalArgumentException("Can't do regression analysis without y variable");
            else if (y_var.getType() != Variable.NUMERIC)
                throw new IllegalArgumentException("Y variable can not be categorical");

            varNames.addElement(x_var.getName());
            varNames.addElement(y_var.getName());

            if (z_var != null)
                varNames.addElement(z_var.getName());
            
        init();
    }
    
    public KSampleModule(DataSet data)
    {
        this.data = data;
        setBackground(Color.white);

        Vector v = data.getYVariables();
        y_var = (Variable) v.elementAt(0);

        v = data.getXVariables();
        x_var = (Variable) v.elementAt(0);

        this.vz = data.getZVariables();
        if (vz.size() > 0)
            z_var = (Variable) vz.elementAt(0);

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());

        if (z_var != null)
            varNames.addElement(z_var.getName());

        init();
    }

    public KSampleModule(DataSet data, PlotMetaData pmd)
    {
        this.data = data;
        this.metaData = pmd;

        setBackground(Color.white);

        x_var = metaData.getXVariable();
        y_var = metaData.getYVariable();
        z_var = metaData.getZVariable();
        
        this.vz = new Vector();
        this.vz.addElement(z_var);

        varNames.addElement(x_var.getName());
        varNames.addElement(y_var.getName());

        if (z_var != null)
            varNames.addElement(z_var.getName());

        init();
    }

    public void enableFitMean(Boolean bl)
    {
        plot.enableFitMean(bl.booleanValue());
    }

    public void enableFitCI(Boolean bl)
    {
        plot.enableFitCI(bl.booleanValue());
    }

    public void enableFitBox(Boolean bl)
    {
        //plot.enableFitBox(bl.booleanValue());
        plot.setFitOutlierBox(bl.booleanValue());
    }

    public void changeGroup()
    {
        int index = chooser.getCurrentIndex();
        plot.setGroup(index);
    }

    public void showReport(String rptName)
    {
        rptBoard.showReport(rptName);
    }

    /**
     * Build the menu bar, add components to this HistogramModule object
     */
    private void init()
    {
        data.addRemoteObserver(this);
        

        plot = new DotPlot(data, y_var, x_var, vz);
        plot.setGUIModule(this);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel jp = buildControlPanel();

        if (jp != null)
        {
            add(buildControlPanel(), gbc);
            gbc.gridy++;
        }

        gbc.weighty = 0.8;

        add(plot, gbc);

        gbc.gridy++;
        gbc.weighty = 0.2;

        add(buildReportPanel(), gbc);


        jmb = new JMenuBar();
        JMenu plotMenu = new JMenu("Plot");

        /*
        JCheckBoxMenuItem checkItem = new JCheckBoxMenuItem("Dotplot", true);
        checkItem.addActionListener(this);
        plotMenu.add(checkItem);
        */
        plotMenu.add("Dotplot");

        JMenu jm = new JMenu("Options");
        optionMenu = jm;

        mShowPoints=jm.add("Hide points");
        mShowPoints.setActionCommand("HIDE_POINTS");
        mShowPoints.addActionListener(this);

        jm.addSeparator();

        meanChk = new JCheckBoxMenuItem("Fit Means", false);
        meanChk.setActionCommand("FIT_MEANS");
        meanChk.addActionListener(this);

        jm.add(meanChk);

        ciChk = new JCheckBoxMenuItem("Fit CI", false);
        ciChk.setActionCommand("FIT_CI");
        ciChk.addActionListener(this);

        jm.add(ciChk);

        boxChk = new JCheckBoxMenuItem("Fit Outlier Boxplot", false);
        boxChk.setActionCommand("FIT_BOXPLOT");
        boxChk.addActionListener(this);
        jm.add(boxChk);


        jm.addSeparator();

        JMenuItem item = jm.add("Color...");
        item.addActionListener(this);

        //plotMenu.add(jm);

        jmb.add(plotMenu);

        JMenu analyzeMenu = new JMenu("Analyze");

        JRadioButtonMenuItem r1 = new JRadioButtonMenuItem("Moments", true);
        JRadioButtonMenuItem r2 = new JRadioButtonMenuItem("Effect Test", false);
        JRadioButtonMenuItem r3 = new JRadioButtonMenuItem("Pairwise CIs", false);
        r1.addActionListener(this);
        r2.addActionListener(this);
        r3.addActionListener(this);

        ButtonGroup group = new ButtonGroup();
        group.add(r1);
        group.add(r2);
        group.add(r3);

        analyzeMenu.add(r1);
        analyzeMenu.addSeparator();
        analyzeMenu.add(r2);
        analyzeMenu.add(r3);

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
     * build control panel for this component
     */
    private JPanel buildControlPanel()
    {
        if (z_var == null)
            return null;

        JPanel jp = new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        chooser = new OverlapSlicer(plot.getGroupMaker());
        chooser.addChangeListener(this);
        chooser.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        jp.add(chooser);
        return jp;
    }


    /**
     * Build ReportPanel for this HistogramModule
     */
    private JPanel buildReportPanel()
    {
        EffectTestRpt etr = new EffectTestRpt(plot.getSummaryStat());

        MeansRpt mr = new MeansRpt(plot.getSummaryStat());

        PairCIRpt pcr = new PairCIRpt(plot.getSummaryStat());

        ReportBoard bb = new ReportBoard(new String[]{"Moments", "Effect Test", "Pairwise CIs"}, new Report[]{mr, etr, pcr});

        bb.selectReport(0);

        rptBoard = bb;

        bb.setPreferredSize(new Dimension(250, 100));

        return bb;
    }


    public void stateChanged(ChangeEvent e)
    {

        Object obj = e.getSource();

        if (obj instanceof Palette)
        {
            changeColor(palette.getColor());
        }

        else if (obj instanceof OverlapSlicer)
        {
            this.changeGroup();
        }
    }

    /**
     * Change color for selected observations
     */
    private void changeColor(Color c)
    {
        //Change color for selected observations

        for (int i = 0; i < data.getSize(); i++)
        {
            if (data.getState(i))
            {
                data.setColor(c, i);
            }
        }
    }


    public void actionPerformed(ActionEvent ae)
    {

        String arg = ae.getActionCommand();

        if (arg.equals("HIDE_POINTS"))
        {
            boolean value=plot.isDisplayDots();
            plot.setDisplayDots(!value);
            if(value)
                mShowPoints.setText("Show Points");
            else
                mShowPoints.setText("Hide Points");
        }
        else if (arg.equals("FIT_MEANS"))
        {
            Boolean state = new Boolean(meanChk.getState());
            this.enableFitMean(state);
        }

        else if (arg.equals("FIT_CI"))
        {
            Boolean state = new Boolean(ciChk.getState());
            this.enableFitCI(state);
        }
        else if (arg.equals("FIT_BOXPLOT"))
        {
            Boolean state = new Boolean(boxChk.getState());
            this.enableFitBox(state);
        }
        else if (arg.equals("Color..."))
        {
            Color c;
            c = JColorChooser.showDialog(this.getParent(), "Palette", Color.black);
            if (c != null)
                changeColor(c);
        }
        else if (arg.equals("Data Table"))
        {

        }
        else
        {
            this.showReport(arg);
        }
    }

    public void update(String arg)
    {
        plot.updatePlot(arg);
    }
}
