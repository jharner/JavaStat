/*
 * LogisticModule.java
 *
 * Created on December 22, 2001, 12:49 PM
 */

package wvustat.modules.logistic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;
import java.rmi.RemoteException;

import wvustat.modules.*;
import wvustat.interfaces.*;
import wvustat.swing.StackLayout;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class LogisticModule extends GUIModule implements ActionListener, ChangeListener
{
    private JMenuBar menuBar;
    private JMenu optionMenu;
    private Variable x_var, y_var, z_var;
    private Vector vz;
    
    private double[] x_array, y_array;
    private int[] n_array;
    private double[] xvals, yvals;

    private LogisticPlot plot;
    private StatsReport report;
    private RegressionSolver solver;
    private int zIndex;
    private EqualCountGrouper groupMaker;
    private OverlapSlicer chooser;
    private JScrollPane scrollPane;

    /** Creates new LogisticModule */
    public LogisticModule(DataSet dataSet)
    {
        this.data = dataSet;

        solver = new RegressionSolver();
        try
        {
            Vector v1 = data.getYVariables();
            Vector v2 = data.getXVariables();

            y_var = (Variable) v1.elementAt(0);
            x_var = (Variable) v2.elementAt(0);
            
            this.vz = data.getZVariables();
            if (vz.size() > 0)
                z_var = (Variable) vz.elementAt(0);
            
            if (z_var != null)
                groupMaker = new EqualCountGrouper(vz, data);
            
            prepareVariables();
        }
        catch (RemoteException re)
        {
            re.printStackTrace();
        }

        initComponents();
    }

    public LogisticModule(DataSet dataSet, Variable yVar, Variable xVar, Variable zVar)
    {
        this.data = dataSet;
        this.y_var = yVar;
        this.x_var = xVar;
        this.z_var = zVar;
        this.vz = new Vector();
        this.vz.addElement(z_var);
        
        if (z_var != null)
            groupMaker = new EqualCountGrouper(vz, data);

        solver = new RegressionSolver();
        try
        {
            prepareVariables();
        }
        catch (RemoteException re)
        {
            re.printStackTrace();
        }

        initComponents();
    }

    public LogisticModule(DataSet dataSet, Variable yVar, Variable xVar, Vector vz)
    {
        this.data = dataSet;
        this.y_var = yVar;
        this.x_var = xVar;
        this.vz = vz;
        
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);
        
        if (z_var != null)
            groupMaker = new EqualCountGrouper(vz, data);

        solver = new RegressionSolver();
        try
        {
            prepareVariables();
        }
        catch (RemoteException re)
        {
            re.printStackTrace();
        }

        initComponents();
    }
    
    public LogisticModule(DataSet dataSet, PlotMetaData pmd)
    {
        this.data = dataSet;

        this.metaData = pmd;

        solver = new RegressionSolver();
        try
        {
            y_var = metaData.getYVariable();
            x_var = metaData.getXVariable();
            z_var = metaData.getZVariable();
            this.vz = new Vector();
            this.vz.addElement(z_var);
            if (z_var != null)
                groupMaker = new EqualCountGrouper(vz, data);
            prepareVariables();
        }
        catch (RemoteException re)
        {
            re.printStackTrace();
        }

        initComponents();
    }

    private void prepareVariables() throws RemoteException
    {
        xvals = x_var.getNumValues();
        //this should give us an array of "0" and "1"s.
        yvals = y_var.getNumValues();

        int m = data.getSize() - data.getTotalMasks();
        x_array = new double[m];
        y_array = new double[m];
        n_array = new int[m];

        int j = 0;
        for (int i = 0; i < data.getSize(); i++)
        {
            if (data.getMask(i) == false && (groupMaker == null || groupMaker.getGroupIndex(i).contains(new Integer(zIndex))))
            {
                x_array[j] = xvals[i];
                y_array[j] = yvals[i];
                n_array[j] = i;
                j++;
            }
        }
        
        x_array = (double[])truncateArray(x_array, j);
        y_array = (double[])truncateArray(y_array, j);
        n_array = (int[])truncateArray(n_array, j);

        solver.setData(new double[][]{x_array}, y_array);
        solver.setInitialEstimates(new double[]{0, 0});
        solver.setPrecision(1e-8);
        solver.iterativeSolve();
    }

    public void setZIndex(int zIndex)
    {
        this.zIndex = zIndex;
        try
        {
            this.prepareVariables();
            this.remove(plot);
            this.remove(scrollPane);
            createContent();
            this.add(plot, BorderLayout.CENTER);
            this.add(scrollPane, BorderLayout.SOUTH);
            validate();
        }
        catch (RemoteException re)
        {

        }
    }

    private Object truncateArray(Object array, int length)
    {
        if (array instanceof double[])
        {
            double[] newArray = new double[length];
            System.arraycopy(array, 0, newArray, 0, length);
            return newArray;
        }
        else if (array instanceof int[])
        {
            int[] newArray = new int[length];
            System.arraycopy(array, 0, newArray, 0, length);
            return newArray;
        }
        else
            return array;
    }


    private void initComponents()
    {
        
        data.addRemoteObserver(this);


        createContent();
        this.setLayout(new BorderLayout());
        Container topPanel = createTopPanel();
        if (topPanel != null)
            this.add(topPanel, BorderLayout.NORTH);
        this.add(plot, BorderLayout.CENTER);
        this.add(scrollPane, BorderLayout.SOUTH);

        menuBar = new JMenuBar();

        JMenu jm = new JMenu("Plot");
        //jm.setMnemonic('P');
        JMenuItem item = jm.add("Logistic");
        item.addActionListener(this);
        //jm.addSeparator();
        JMenu subMenu = new JMenu("Options");
        this.optionMenu = subMenu;
        item = subMenu.add("Color...");
        item.addActionListener(this);
        //jm.add(subMenu);

        menuBar.add(jm);

        JMenu jm2 = new JMenu("Analyze");
        //jm2.setMnemonic('A');
        JRadioButtonMenuItem radio1 = new JRadioButtonMenuItem("Whole-model Test", true);
        jm2.add(radio1);
        radio1.addActionListener(this);
        JRadioButtonMenuItem radio2 = new JRadioButtonMenuItem("Parameter Estimates", false);
        radio2.addActionListener(this);
        jm2.add(radio2);
        menuBar.add(jm2);

        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);

    }

    private void createContent()
    {
        plot = new LogisticPlot(solver, x_array, y_array, n_array, data);
        plot.setGUIModule(this);
        report = new StatsReport(solver);
        plot.setXLabel(x_var.getName());
        report.setXVarName(x_var.getName());

        scrollPane = new JScrollPane(report);
        scrollPane.setPreferredSize(report.getPreferredSize());

    }

    private Container createTopPanel()
    {
        if (groupMaker == null)
            return null;

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.white);
        chooser = new OverlapSlicer(groupMaker);
        chooser.addChangeListener(this);
        chooser.setBorder(BorderFactory.createEmptyBorder(0,2,2,0));
        panel.add(chooser);
        return panel;
    }

    public void stateChanged(ChangeEvent event)
    {
        this.setZIndex(chooser.getCurrentIndex());
    }

    public JMenuBar getJMenuBar()
    {
        return menuBar;
    }
    
    public JMenu getOptionMenu()
    {
    		return optionMenu;
    }

    public void update(String arg)
    {

        if (arg.equals("yymask"))
        {
            try
            {
                prepareVariables();
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }

            plot.setData(x_array, y_array, n_array);

            report.updateReport();
        }
        else if (arg.equals("yystate") || arg.equals("yycolor"))
        {
            plot.repaint();
        }
    }


    public void setReportIndex(int index)
    {
        report.setReportIndex(index);
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        String arg = p1.getActionCommand();


        if (arg.equals("Whole-model Test"))
        {
            setReportIndex(StatsReport.WHOLE_MODEL_REPORT);

        }
        else if (arg.equals("Parameter Estimates"))
        {
            setReportIndex(StatsReport.PARAMETER_REPORT);
        }
        else if (arg.equals("Color..."))
        {
            Color c;
            c = JColorChooser.showDialog(this.getParent(), "Palette", Color.black);

            if (c == null)
                return;
            
            for (int i = 0; i < data.getSize(); i++)
            {
                if (data.getState(i) == true && data.getMask(i) == false)
                {
                    data.setColor(c, i);
                }
            }
            
        }
    }
}