/*
* Grapher.java
*
* Created on November 5, 2001, 10:01 AM
*/

package wvustat.math.UI;

import wvustat.math.expression.*;
import wvustat.math.plot.FunctionPlotter;
import wvustat.plotUtil.ComponentUtil;
import wvustat.plotUtil.MyGridBagConstraints;
import wvustat.statistics.InvalidDataError;
import wvustat.statistics.NonlinearRegression;
import wvustat.swing.ComponentUtilities;
import wvustat.swing.DataChooser;
import wvustat.swing.RetargetedAction;
import wvustat.swing.table.DataMarshaller;
import wvustat.swing.table.DataSet;
import wvustat.swing.table.JDataTableDialog;
import wvustat.swing.table.XMLWriter;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;



/**
 *
 * @author  Hengyi Xue
 * @version
 */

public class Grapher extends JPanel implements ActionListener, ChangeListener, TableModelListener {

    private FunctionPlotter plotter;

    private JTextField inputField;

    private FunctionsCustomizer customizer;

    private boolean overridingFunction = true;

    private boolean runAsApplet = true;

    private DataSet dataSet;

    public ToggleButton toggle, clearButton, autoButton,  pclickButton;

    private String dataUrl = "http://ideal.stat.wvu.edu/ideal/datasets/";
    //private String dataUrl = "http://web.math.wvu.edu/grapher/datasets/";
    //private String dataUrl = "http://157.182.20.32/grapher/";

    private FunctionPlotter.PlotOptions plotOptions = new FunctionPlotter.PlotOptions();

    private JDataTableDialog tableDialog;

    private Action editFunctionAction, deleteFunctionAction, editModelAction,showModelAction, hideModelAction, showTableAction, hideTableAction;

    private ActionListener menuListener;

    private ActionMap actionMap = new ActionMap();

    private boolean overridingModel = true;

    private JDialog menuDialog;

    /** Creates new Grapher */

    public Grapher() {
	runAsApplet = false;
	init();
    }

    public Grapher(boolean runAsApplet) {

        this.runAsApplet = runAsApplet;
    }

    private void createActions() {

        editFunctionAction = new RetargetedAction("Modify Function...", "MODIFY_FUNCTION", menuListener);
        deleteFunctionAction = new RetargetedAction("Delete Function", "DELETE_FUNCTION", menuListener);
        showModelAction = new RetargetedAction("Show Models", "SHOW_MODELS", menuListener);
        hideModelAction = new RetargetedAction("Hide Model", "HIDE_MODEL", menuListener);
        showTableAction = new RetargetedAction("Show Table", "SHOW_TABLE", menuListener);
        hideTableAction = new RetargetedAction("Hide Table", "HIDE_TABLE", menuListener);
        editModelAction = new RetargetedAction("Modify Model...", "MODIFY_MODEL", menuListener);
        Action linearModelAction = new RetargetedAction("Linear Model", "LINEAR_MODEL", menuListener);
        Action quadraticModelAction = new RetargetedAction("Quadratic Model", "QUADRATIC_MODEL", menuListener);
        Action cubicModelAction = new RetargetedAction("Cubic Model", "CUBIC_MODEL", menuListener);
        Action addModelAction = new RetargetedAction("Add Model...", "ADD_MODEL", menuListener);

        actionMap.put("LINEAR_MODEL", linearModelAction);
        actionMap.put("QUADRATIC_MODEL", quadraticModelAction);
        actionMap.put("CUBIC_MODEL", cubicModelAction);
        actionMap.put("ADD_MODEL", addModelAction);

        linearModelAction.setEnabled(false);
        quadraticModelAction.setEnabled(false);
        cubicModelAction.setEnabled(false);
        addModelAction.setEnabled(false);

        Action openAction = new RetargetedAction("Open Table...", "OPEN", menuListener);
        Action openRemoteAction = new RetargetedAction("Open Remote Table...", "OPEN_REMOTE", menuListener);

        showTableAction.setEnabled(false);
        editFunctionAction.setEnabled(false);
        deleteFunctionAction.setEnabled(false);
        //showModelAction.setEnabled(false);
        hideModelAction.setEnabled(false);
        hideTableAction.setEnabled(false);
        editModelAction.setEnabled(false);

        actionMap.put(editFunctionAction.getValue(Action.ACTION_COMMAND_KEY), editFunctionAction);
        actionMap.put(deleteFunctionAction.getValue(Action.ACTION_COMMAND_KEY), deleteFunctionAction);
        actionMap.put(showModelAction.getValue(Action.ACTION_COMMAND_KEY), showModelAction);
        actionMap.put(hideModelAction.getValue(Action.ACTION_COMMAND_KEY), hideModelAction);
        actionMap.put(hideTableAction.getValue(Action.ACTION_COMMAND_KEY), hideTableAction);
        actionMap.put(editModelAction.getValue(Action.ACTION_COMMAND_KEY), editModelAction);
        actionMap.put(showTableAction.getValue(Action.ACTION_COMMAND_KEY), showTableAction);

        Action hideFunctionAction = new RetargetedAction("Hide Function", "HIDE_FUNCTION", menuListener);

        hideFunctionAction.setEnabled(false);

        Action showFunctionsAction = new RetargetedAction("Show Functions", "SHOW_FUNCTIONS", menuListener);
        //showFunctionsAction.setEnabled(false);
        Action deleteModelAction = new RetargetedAction("Delete Model", "DELETE_MODEL", menuListener);

        deleteModelAction.setEnabled(false);

        Action transformFunctionAction = new RetargetedAction("Transform...", "TRANSFORM_FUNCTION", menuListener);

        transformFunctionAction.setEnabled(false);

        Action transformModelAction = new RetargetedAction("Transform...", "TRANSFORM_MODEL", menuListener);

        transformModelAction.setEnabled(false);

        actionMap.put(hideFunctionAction.getValue(Action.ACTION_COMMAND_KEY), hideFunctionAction);
        actionMap.put(showFunctionsAction.getValue(Action.ACTION_COMMAND_KEY), showFunctionsAction);
        actionMap.put(deleteModelAction.getValue(Action.ACTION_COMMAND_KEY), deleteModelAction);
        actionMap.put(transformFunctionAction.getValue(Action.ACTION_COMMAND_KEY), transformFunctionAction);
        actionMap.put(transformModelAction.getValue(Action.ACTION_COMMAND_KEY), transformModelAction);
        actionMap.put(openAction.getValue(Action.ACTION_COMMAND_KEY), openAction);
        actionMap.put(openRemoteAction.getValue(Action.ACTION_COMMAND_KEY), openRemoteAction);

        Action saveAction = new RetargetedAction("Save Table", "SAVE", menuListener);
        Action saveAsAction = new RetargetedAction("Save Table As...", "SAVE_AS", menuListener);

        saveAction.setEnabled(false);
        saveAsAction.setEnabled(false);

        actionMap.put(saveAction.getValue(Action.ACTION_COMMAND_KEY), saveAction);
        actionMap.put(saveAsAction.getValue(Action.ACTION_COMMAND_KEY), saveAsAction);

        Action clearPointsAction = new RetargetedAction("Clear Points", "CLEAR_POINTS", menuListener);

        clearPointsAction.setEnabled(false);

        actionMap.put("CLEAR_POINTS", clearPointsAction);
    }

    private DataSet getDataPoints() {

        String xParam = null;  //this.getParameter("x");
     	String yParam = null; //this.getParameter("y");

        if (xParam != null && yParam != null) {

            StringTokenizer stx = new StringTokenizer(xParam, " ,");
            StringTokenizer sty = new StringTokenizer(yParam, " ,");

            int len1 = stx.countTokens();
            int len2 = sty.countTokens();

            int len = len1 > len2 ? len2 : len1;

            double[][] data = new double[2][len];

            for (int i = 0; i < len; i++) {
                try {
                    data[0][i] = new Double(stx.nextToken()).doubleValue();
                    data[1][i] = new Double(sty.nextToken()).doubleValue();
                } catch (NumberFormatException e) {
                }
            }

            return new DataSet(data[0], data[1]);
        } else
            return null;
    }

    public Image getImage(URL url) {
	/*
        if (runAsApplet)
            return super.getImage(url);
	*/

        Toolkit toolkit = this.getToolkit();
        return toolkit.getImage(url);
    }

    public void tableChanged(TableModelEvent e) {
        if (dataSet.getRowCount() > 0) {

            actionMap.get("OPEN").setEnabled(false);
            actionMap.get("OPEN_REMOTE").setEnabled(false);
            actionMap.get("SAVE").setEnabled(true);
            actionMap.get("SAVE_AS").setEnabled(true);
            actionMap.get("LINEAR_MODEL").setEnabled(true);
            actionMap.get("QUADRATIC_MODEL").setEnabled(true);
            actionMap.get("CUBIC_MODEL").setEnabled(true);
            actionMap.get("ADD_MODEL").setEnabled(true);

            if (tableDialog.isShowing()) {
                actionMap.get("SHOW_TABLE").setEnabled(false);
                actionMap.get("HIDE_TABLE").setEnabled(true);
            } else {
                actionMap.get("SAVE").setEnabled(false);
                actionMap.get("SAVE_AS").setEnabled(false);
                actionMap.get("SHOW_TABLE").setEnabled(true);
                actionMap.get("HIDE_TABLE").setEnabled(false);
            }
        } else {

            actionMap.get("LINEAR_MODEL").setEnabled(false);
            actionMap.get("QUADRATIC_MODEL").setEnabled(false);
            actionMap.get("CUBIC_MODEL").setEnabled(false);
            actionMap.get("ADD_MODEL").setEnabled(false);
            actionMap.get("OPEN").setEnabled(true);
            actionMap.get("OPEN_REMOTE").setEnabled(true);
        }
    }

    public void init() {
        //Test if this object is really running as a applet
	/*
        try {
            this.getParameter("x");
        } catch (Exception e) {
            runAsApplet = false;
        }
	*/

        plotter = new FunctionPlotter();
        plotter.setPlotOptions(plotOptions);
        plotter.addChangeListener(this);

        plotter.getPointsSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {

                if (plotter.getPointsSelectionModel().isSelectionEmpty())
                    actionMap.get("CLEAR_POINTS").setEnabled(false);
                else
                    actionMap.get("CLEAR_POINTS").setEnabled(true);
            }
        }
        );

        if (runAsApplet) {
            dataSet = this.getDataPoints();
        }

        if (dataSet == null) {
            dataSet = new DataSet();

            //dataSet.setColumnRole(DataSet.X_ROLE, 1);
            //dataSet.setColumnRole(DataSet.Y_ROLE, 2);
        }

        plotter.setDataSet(dataSet);

        dataSet.addTableModelListener(this);

        tableDialog = new JDataTableDialog((Frame) SwingUtilities.getWindowAncestor(this), dataSet);

        tableDialog.addWindowListener(new WindowListener());

        inputField = new JTextField(20);

        //inputField.setFont(new Font("TimesRoman", Font.PLAIN, 14));

        inputField.addActionListener(this);

        menuListener = new MenuListener();

        //createMenus();

        JPanel zoomPanel = new JPanel();

        zoomPanel.setLayout(new GridBagLayout());

        ClassLoader loader = this.getClass().getClassLoader();

        ZoomButton xyIn = new ZoomButton(getImage(loader.getResource("images/xyZoomIn.gif")), ZoomButton.ZOOM_IN_XY);
        ZoomButton xyOut = new ZoomButton(getImage(loader.getResource("images/xyZoomOut.gif")), ZoomButton.ZOOM_OUT_XY);
        ZoomButton xIn = new ZoomButton(getImage(loader.getResource("images/xZoomIn.gif")), ZoomButton.ZOOM_IN_X);
        ZoomButton xOut = new ZoomButton(getImage(loader.getResource("images/xZoomOut.gif")), ZoomButton.ZOOM_OUT_X);
        ZoomButton yIn = new ZoomButton(getImage(loader.getResource("images/yZoomIn.gif")), ZoomButton.ZOOM_IN_Y);
        ZoomButton yOut = new ZoomButton(getImage(loader.getResource("images/yZoomOut.gif")), ZoomButton.ZOOM_OUT_Y);
        ZoomButton reset = new ZoomButton(getImage(loader.getResource("images/reset.gif")), ZoomButton.RESET);

        xyIn.setFunctionPlotter(plotter);
        xyOut.setFunctionPlotter(plotter);
        xIn.setFunctionPlotter(plotter);
        xOut.setFunctionPlotter(plotter);
        yIn.setFunctionPlotter(plotter);
        yOut.setFunctionPlotter(plotter);

        reset.setFunctionPlotter(plotter);

        autoButton = new ToggleButton(getImage(loader.getResource("images/autoscale.gif")), true);
        autoButton.addActionListener(this);

        pclickButton = new ToggleButton(getImage(loader.getResource("images/pclick.gif")));
        pclickButton.addActionListener(this);

        toggle = new ToggleButton(getImage(loader.getResource("images/magnify.gif")));
        toggle.addActionListener(this);

        clearButton = new ToggleButton(getImage(loader.getResource("images/clear.gif")), true);
        clearButton.addActionListener(this);

        zoomPanel.add(xyIn, new MyGridBagConstraints(0, 0, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(xyOut, new MyGridBagConstraints(0, 1, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(xIn, new MyGridBagConstraints(0, 2, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(xOut, new MyGridBagConstraints(0, 3, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(yIn, new MyGridBagConstraints(0, 4, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(yOut, new MyGridBagConstraints(0, 5, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(reset, new MyGridBagConstraints(0, 6, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(autoButton, new MyGridBagConstraints(0, 7, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(pclickButton, new MyGridBagConstraints(0, 8, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(toggle, new MyGridBagConstraints(0, 9, 1, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));
        zoomPanel.add(clearButton, new MyGridBagConstraints(0, 10, 0, 1/* GridBagConstraints.RELATIVE*/, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 1, 2, 1), 0, 0));

        setLayout(new BorderLayout());
        
        //added by djluo
        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);
        buildMenu(menuBar);
        //end add
        
        Container content = new JPanel(); //this.getContentPane();
        add(content, BorderLayout.CENTER);

        content.setBackground(Color.white);
        content.setLayout(new GridBagLayout());

        JLabel equationLabel = new JLabel("Please Type Your Equation: ");

        content.add(plotter, new MyGridBagConstraints(0, 0, 1/*GridBagConstraints.REMAINDER*/, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 4, 4, 4), 0, 0));
        content.add(zoomPanel, new MyGridBagConstraints(1, 0, GridBagConstraints.RELATIVE, -1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        content.add(equationLabel, new MyGridBagConstraints(0, 1, 0, GridBagConstraints.RELATIVE, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        content.add(inputField, new MyGridBagConstraints(0, 1, 0, GridBagConstraints.RELATIVE, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    //modified by djluo
    //private void createMenus() {
    	public void buildMenu(JMenuBar menuBar) {

        createActions();

        JMenu dataMenu = new JMenu("Data");

        ComponentUtilities.createMenuItem(dataMenu, "New Table", "NEW_DATASET", menuListener);

        dataMenu.addSeparator();
        dataMenu.add(actionMap.get("OPEN"));
        dataMenu.add(actionMap.get("OPEN_REMOTE"));
        dataMenu.addSeparator();
        dataMenu.add(showTableAction);
        dataMenu.add(hideTableAction);
        dataMenu.addSeparator();
        dataMenu.add(actionMap.get("SAVE"));
        dataMenu.add(actionMap.get("SAVE_AS"));

        JMenu graphMenu = new JMenu("Graph");
        JMenuItem addPoint = new JMenuItem("Add Points...");

        addPoint.setActionCommand("ADD_POINTS");
        addPoint.addActionListener(menuListener);

        graphMenu.add(addPoint);
        //ComponentUtilities.createMenuItem(graphMenu, "Add Points...", "ADD_POINTS", menuListener);
        graphMenu.addSeparator();

        ButtonGroup group1 = new ButtonGroup();

        JRadioButtonMenuItem radio1 = new JRadioButtonMenuItem("Show Points", true);
        JRadioButtonMenuItem radio2 = new JRadioButtonMenuItem("Hide Points");

        group1.add(radio1);
        group1.add(radio2);

        radio1.setActionCommand("SHOW_POINTS");
        radio2.setActionCommand("HIDE_POINTS");
        radio1.addActionListener(menuListener);
        radio2.addActionListener(menuListener);

        graphMenu.add(radio1);
        graphMenu.add(radio2);
        graphMenu.addSeparator();
        graphMenu.add(actionMap.get("CLEAR_POINTS"));

        ComponentUtilities.createMenuItem(graphMenu, "Clear All Points", "CLEAR_ALL_POINTS", menuListener);
        graphMenu.addSeparator();
        ComponentUtilities.createMenuItem(graphMenu, "Open Function", "OPEN_FUNCTION", menuListener);
        ComponentUtilities.createMenuItem(graphMenu, "Open Remote Function...", "OPEN_REMOTE_FUNCTION", menuListener);

        //graphMenu.addSeparator();
        //ComponentUtilities.createMenuItem(graphMenu, "Customize...", "CUSTOMIZE", menuListener);

        JMenu functionMenu = new JMenu("Function");
        ComponentUtilities.createMenuItem(functionMenu, "Add Function...", "ADD_FUNCTION", menuListener);

        functionMenu.add(editFunctionAction);
        functionMenu.addSeparator();
        functionMenu.add(actionMap.get("SHOW_FUNCTIONS"));
        functionMenu.add(actionMap.get("HIDE_FUNCTION"));
        functionMenu.addSeparator();
        functionMenu.add(deleteFunctionAction);
        functionMenu.addSeparator();

        //ComponentUtilities.createMenuItem(functionMenu, "Transform...", "TRANSFORM", menuListener);

        functionMenu.add(actionMap.get("TRANSFORM_FUNCTION"));
        functionMenu.addSeparator();

        JCheckBoxMenuItem checkItem = new JCheckBoxMenuItem("Override Functions", true);

        checkItem.setActionCommand("OVERRIDE_FUNCTION");
        checkItem.addActionListener(menuListener);

        functionMenu.add(checkItem);

        JMenu modelMenu = new JMenu("Model");

        modelMenu.add(actionMap.get("LINEAR_MODEL"));
        modelMenu.add(actionMap.get("QUADRATIC_MODEL"));
        modelMenu.add(actionMap.get("CUBIC_MODEL"));
        //ComponentUtilities.createMenuItem(modelMenu, "Linear Model", "LINEAR_MODEL", menuListener);
        //ComponentUtilities.createMenuItem(modelMenu, "Quadratic Model", "QUADRATIC_MODEL", menuListener);
        modelMenu.addSeparator();
        //ComponentUtilities.createMenuItem(modelMenu, "Add Model...", "ADD_MODEL", menuListener);
        modelMenu.add(actionMap.get("ADD_MODEL"));
        modelMenu.add(editModelAction);
        modelMenu.addSeparator();
        modelMenu.add(showModelAction);
        modelMenu.add(hideModelAction);
        modelMenu.addSeparator();
        modelMenu.add(actionMap.get("DELETE_MODEL"));
        modelMenu.addSeparator();
        modelMenu.add(actionMap.get("TRANSFORM_MODEL"));

        checkItem = new JCheckBoxMenuItem("Override Last Model", true);
        checkItem.setActionCommand("OVERRIDE_MODEL");
        checkItem.addActionListener(menuListener);

        modelMenu.add(checkItem);

        //commented by djluo
        //JMenuBar menuBar = new JMenuBar();
        menuBar.add(dataMenu);
        menuBar.add(graphMenu);
        menuBar.add(functionMenu);
        menuBar.add(modelMenu);

        //commented by djluo
        //add(menuBar, BorderLayout.NORTH);
        //setJMenuBar(menuBar);
    }

    public void actionPerformed(final java.awt.event.ActionEvent p1) {

        String arg = p1.getActionCommand();
        Object obj = p1.getSource();

        if (obj == inputField) {

            String input = inputField.getText();
            ExpressionObject function = createFunction(input);

            if (function != null) {
                if (overridingFunction)
                	   plotter.clearFunctions();
                	   plotter.addFunction(function);
            }
        }

        //added the code for pclickButton obj

        else if (obj == pclickButton) {
            plotter.enablePointClickZoom(pclickButton.isPressed());

        } else if (obj == toggle) {
            plotter.enableZoom(toggle.isPressed());
        
        } else if (obj == clearButton) {
            //dataSet.clear();
            plotter.clearFunctions();
            inputField.setText("");

        } else if (obj == autoButton) {
            plotter.autoScale();

        } else if (arg.equals("Zoom In")) {
            plotter.zoomIn();

        } else if (arg.equals("Zoom Out")) {
            plotter.zoomOut();
        }
    }

    private ExpressionObject createFunction(String unparsedForm) {

        if (unparsedForm != null && unparsedForm.trim().length() > 0) {

            String input = unparsedForm.toLowerCase();

            if (input.startsWith("y="))
                input = input.substring(2);

            try {
            	   ExpressionParser ep = new ExpressionParser();
                Expression expr = ep.parse(input);

                if (!input.equals("x") && (expr instanceof ConstantExpression || expr instanceof VariableExpression)) {
                    WarningDialog.showWarning(Grapher.getTopComponent(this), "Your entered a constant expression");
                }

                return new ExpressionObject(expr, unparsedForm, ExpressionObject.FUNCTION);

            } catch (ParseException se) {

                WarningDialog.showWarning(Grapher.getTopComponent(this), "Your input contains syntax error!");
                se.printStackTrace();

                return null;
            } catch (TokenMgrError err) {

                WarningDialog.showWarning(Grapher.getTopComponent(this), "Your input contains syntax error!");
                err.printStackTrace();

                return null;
            }
        } else
            return null;
    }

    private void fitLinear() {

        ExpressionObject model = fitModel("y=a+b*x", new String[]{"a", "b"}, new double[]{1, 1});
        String modelType = "Linear";

        ModelDialog dialogTest = new ModelDialog(model, dataSet, modelType);
        JDialog dialog = dialogTest.getDialog(Grapher.this, "Linear");
        dialog.setLocationRelativeTo(Grapher.this);
        dialog.show();

        if (model != null) {

            if (overridingModel)
                plotter.clearModels();

            plotter.addModel(model);
        }
    }

    private void fitQuadratic() {

        ExpressionObject model = fitModel("y=a+b*x+c*x^2", new String[]{"a", "b", "c"}, new double[]{1, 1, 1});

        String modelType = "Quadratic";

        ModelDialog dialogTest = new ModelDialog(model, dataSet, modelType);
        JDialog dialog = dialogTest.getDialog(Grapher.this, "Quadratic");
        dialog.setLocationRelativeTo(Grapher.this);
        dialog.show();

        if (model != null) {
            if (overridingModel)
                plotter.clearModels();
            plotter.addModel(model);
        }
    }

    private void fitCubic() {

        ExpressionObject model = fitModel("y=a+b*x+c*x^2+d*x^3", new String[]{"a", "b", "c", "d"}, new double[]{1, 1, 1, 1});

        String modelType = "Cubic";

        ModelDialog dialogTest = new ModelDialog(model, dataSet, modelType);
        JDialog dialog = dialogTest.getDialog(Grapher.this, "Cubic");
        dialog.setLocationRelativeTo(Grapher.this);
        dialog.show();

        if (model != null) {

            if (overridingModel)
                plotter.clearModels();

            plotter.addModel(model);
        }
    }

    private ExpressionObject fitModel(String model, String[] paramNames, double[] paramValues) {

        if (dataSet == null || dataSet.getRowCount() == 0) {

            JOptionPane.showMessageDialog(this, "No data to fit!");
            return null;
        }

        try {
        	   ExpressionParser ep = new ExpressionParser();
            Expression expr = ep.parse(model.substring(model.indexOf('=') + 1).toLowerCase());
            Vector params = expr.getVariableNames();
            params.removeElement("x");
            NonlinearRegression nr = new NonlinearRegression(dataSet.getXValues(), dataSet.getYValues(), expr, "x");
            String[] array;

            if (paramNames == null) {

                ParamInitializer inizer = new ParamInitializer(ComponentUtil.getTopComponent(Grapher.this), params);
                inizer.show();

                if (inizer.isCanceled())
                    return null;

                array = new String[params.size()];
                params.copyInto(array);

                if (inizer.getValues() != null) {
                    nr.setInitialEstimates(array, inizer.getValues());
                }
            } else {

                nr.setInitialEstimates(paramNames, paramValues);
                array = paramNames;
            }

            nr.iterativeSolve();
            //plotter.clearFunctions();

            ExpressionObject mf = new ExpressionObject(expr, model, ExpressionObject.MODEL);

            //Hashtable paramTable = new Hashtable(params.size());
            for (int i = 0; i < array.length; i++) {

                Parameter param = mf.getParameter(array[i]);
                param.setValue(nr.getParameterValue(array[i]));
                param.setSE(nr.getParameterSE(array[i]));
                //paramTable.put(array[i], new Double(nr.getParameterValue(array[i])));
            }

            mf.setLineColor(Color.red);
            //plotter.addModel(mf);
            return mf;

            //double sd = mf.getSumOfSquares(dataSet.getXValues(), dataSet.getYValues()) / (dataSet.getSize() - 1);
            //sd = Math.sqrt(sd);
            //new ReportDialog(ComponentUtil.getTopComponent(Grapher.this), input, paramTable, sd).show();
        } catch (InvalidDataError err1) {
            WarningDialog.showWarning(ComponentUtil.getTopComponent(Grapher.this), "You have too many parameters in your function. The number of parameters can not exceed the number of data points!");
        } catch (ParseException err2) {
            WarningDialog.showWarning(ComponentUtil.getTopComponent(Grapher.this), "The function you input is invalid!");
        } catch (TokenMgrError err3) {
            WarningDialog.showWarning(ComponentUtil.getTopComponent(Grapher.this), "The function you input is invalid!");
        } catch (ExecError err4) {
            err4.printStackTrace();
            WarningDialog.showWarning(ComponentUtil.getTopComponent(Grapher.this), "Unexpected error occurred!");
        } catch (Exception others) {
            others.printStackTrace();
            WarningDialog.showWarning(ComponentUtil.getTopComponent(Grapher.this), "Unexpected error occurred!");
        }
        return null;
    }

    class ClosebListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            if (cmd.equals("CLOSE")) {
                menuDialog.dispose();
            }
        }
    }


    public void stateChanged(final ChangeEvent p1) {

        Object src = p1.getSource();

        if (src != plotter) {
            plotter.repaint();
        } else {
            ExpressionObject expression = plotter.getSelectedExpression();
            if (expression != null) {

                if (expression.getType() == ExpressionObject.FUNCTION) {

                    editFunctionAction.setEnabled(true);
                    deleteFunctionAction.setEnabled(true);
                    //actionMap.get("TRANSFORM_FUNCTION").setEnabled(true);
                    actionMap.get("HIDE_FUNCTION").setEnabled(true);
                    editModelAction.setEnabled(false);
                    //showModelAction.setEnabled(false);

                    if (expression.getParameters().size() > 0)
                        actionMap.get("TRANSFORM_FUNCTION").setEnabled(true);
                    else
                        actionMap.get("TRANSFORM_FUNCTION").setEnabled(false);
                    actionMap.get("TRANSFORM_MODEL").setEnabled(false);
                    actionMap.get("HIDE_MODEL").setEnabled(false);
                    actionMap.get("DELETE_MODEL").setEnabled(false);
                } else {

                    editFunctionAction.setEnabled(false);
                    deleteFunctionAction.setEnabled(false);
                    //actionMap.get("TRANSFORM_FUNCTION").setEnabled(false);
                    actionMap.get("HIDE_FUNCTION").setEnabled(false);
                    editModelAction.setEnabled(true);
                    //showModelAction.setEnabled(true);
                    actionMap.get("TRANSFORM_MODEL").setEnabled(true);
                    actionMap.get("HIDE_MODEL").setEnabled(true);
                    actionMap.get("DELETE_MODEL").setEnabled(true);
                }
            } else {
                editFunctionAction.setEnabled(false);
                deleteFunctionAction.setEnabled(false);
                editModelAction.setEnabled(false);
                //showModelAction.setEnabled(false);
                //actionMap.get("TRANSFORM_FUNCTION").setEnabled(false);
                actionMap.get("TRANSFORM_MODEL").setEnabled(false);
                actionMap.get("HIDE_MODEL").setEnabled(false);
                actionMap.get("DELETE_MODEL").setEnabled(false);
                actionMap.get("HIDE_FUNCTION").setEnabled(false);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
    }

    public static void main(String[] args) {

        Grapher grapher = new Grapher(false);
        grapher.init();

        final JFrame fr = new JFrame("Grapher");
        //JMenuBar menuBar=new JMenuBar();

        fr.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        }
        );

        fr.getContentPane().setLayout(new BorderLayout());
        fr.getContentPane().add(grapher, BorderLayout.CENTER);
        //fr.setJMenuBar(menuBar);
        //grapher.buildMenu(menuBar);
        fr.pack();
        fr.show();
    }

    /*
    public void itemStateChanged(final java.awt.event.ItemEvent p1)
    {
    Object src = p1.getItem();
    JRadioButtonMenuItem item = (JRadioButtonMenuItem) p1.getSource();

    if (src.equals("Override Last Function"))
    {
    //trigger.releaseTrigger();
    overridingFunction = item.getModel().isSelected();
    }
    else if (src.equals("Add Data Points"))
    {
    //trigger.releaseTrigger();
    plotter.setMouseMode(item.getModel().isSelected());
    if (item.getModel().isSelected())
    {
    plotter.enableZoom(false);
    toggle.releaseButton();
    }
    }
    }*/

    public void removeNotify() {

        if (runAsApplet == false)
            tableDialog.dispose();
        super.removeNotify();
    }

    private void addExpression(int type) {

        String name = type == ExpressionObject.FUNCTION ? "function" : "model";
        String input = JOptionPane.showInputDialog(Grapher.this, "Please input a " + name);

        if (input != null && input.length() > 0) {
            input = input.toLowerCase();

            if (input.startsWith("y="))
                input = input.substring(2);
            try {
            	   ExpressionParser ep = new ExpressionParser();
                Expression expr = ep.parse(input);

                if (!input.equals("x") && (expr instanceof ConstantExpression || expr instanceof VariableExpression)) {
                    JOptionPane.showMessageDialog(Grapher.this, "Your entered a constant expression");
                }

                if (type == ExpressionObject.FUNCTION && overridingFunction)
                    plotter.clearFunctions();

                if (type == ExpressionObject.MODEL && overridingModel)
                    plotter.clearModels();

                if (type == ExpressionObject.FUNCTION)
                    plotter.addFunction(new ExpressionObject(expr, "y=" + input, type));
                else
                    plotter.addModel(new ExpressionObject(expr, "y=" + input, type));

                //inputField.setText("");
            } catch (ParseException se) {

                JOptionPane.showMessageDialog(Grapher.this, "Your input contains syntax error!");
                se.printStackTrace();
            } catch (TokenMgrError err) {

                JOptionPane.showMessageDialog(Grapher.this, "Your input contains syntax error!");
                err.printStackTrace();
            }
        }
    }

    public static Frame getTopComponent(Component comp) {

        Component parent = comp.getParent();

        if (parent instanceof Frame)
            return (Frame) parent;
        else
            return getTopComponent(parent);
    }

    class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String arg = e.getActionCommand();

            if (arg.equals("SAVE"))// || arg.equals("SAVE_AS"))
            {
                actionMap.get("SAVE").setEnabled(false);

                if (dataSet.getDataFile() == null) {

                    JFileChooser fc = new JFileChooser();
                    fc.setDialogTitle("Save dataset");

                    if (fc.showSaveDialog(Grapher.this) == JFileChooser.APPROVE_OPTION) {
                        try {

                            File f = fc.getSelectedFile();
                            XMLWriter.saveDataSetInXML(dataSet, f);
                            dataSet.setDataFile(f);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(Grapher.this, ex.getMessage());
                        }
                    }
                } else {
                    try {
                        XMLWriter.saveDataSetInXML(dataSet, dataSet.getDataFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Grapher.this, ex.getMessage());
                    }
                }
            } else if (arg.equals("SAVE_AS")) {

                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Save dataset");

                if (fc.showSaveDialog(Grapher.this) == JFileChooser.APPROVE_OPTION) {
                    try {

                        File f = fc.getSelectedFile();
                        XMLWriter.saveDataSetInXML(dataSet, f);
                        dataSet.setDataFile(f);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(Grapher.this, ex.getMessage());
                    }
                }
            } else if (arg.equals("OPEN")) {

                JFileChooser fc = new JFileChooser();

                fc.setDialogTitle("Select a file");

                if (fc.showOpenDialog(Grapher.this) == JFileChooser.APPROVE_OPTION) {
                    try {

                        File f = fc.getSelectedFile();
                        dataSet.clear();

                        DataMarshaller marshaller = new DataMarshaller(dataSet);
                        marshaller.collect(new FileInputStream(f));
                        dataSet.setDataFile(f);

                        //set to true so you could open
                        //multiple tables
                        actionMap.get("OPEN").setEnabled(true);

                        //set to true so you could open
                        //multiple tables
                        actionMap.get("OPEN_REMOTE").setEnabled(true);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Grapher.this, ex.getMessage());
                    }
               }
            } else if (arg.equals("OPEN_REMOTE")) {
                try {

                    DataChooser dc = new DataChooser(ComponentUtil.getTopComponent(Grapher.this), dataUrl);
                    dc.pack();
                    dc.setLocationRelativeTo(Grapher.this);
                    dc.show();

                    String input = dc.getSelectedItem();

                    if (input == null)
                        return;

                    URL url = new URL(dataUrl + input.trim());

                    //   dataSet.clearAllRows();
                    dataSet.clear();

                    DataMarshaller marshaller = new DataMarshaller(dataSet);
                    marshaller.collect(url.openStream());

                    //set to true in order to open more than one table
                    actionMap.get("OPEN").setEnabled(true);

                    //set to true in order to open more than one table
                    actionMap.get("OPEN_REMOTE").setEnabled(true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Grapher.this, "Unable to connect to " + dataUrl);
                }
            }

            if (arg.equals("CUSTOMIZE")) {

                if (customizer == null) {
                    customizer = new FunctionsCustomizer((Frame) SwingUtilities.getWindowAncestor(Grapher.this));
                }

                java.util.List combinedList = new ArrayList(plotter.getFunctions());

                combinedList.addAll(plotter.getModels());
                //customizer.setDataSet(plotter.getDataSet());
                customizer.setFunctions(plotter.getFunctions());
                customizer.show();

            } else if (arg.equals("NEW_DATASET")) {

                dataSet.clearAllRows();

                tableDialog.pack();

                ComponentUtil.setLocationRelativeTo(tableDialog, Grapher.this);

                tableDialog.show();

            } else if (arg.equals("ADD_MODEL")) {

                String input = JOptionPane.showInputDialog(Grapher.this, "Please input a function with parameters:");//, inputField.getText());

                if (input == null)
                    return;

                ExpressionObject model = fitModel(input, null, null);

                if (model != null) {
                    if (overridingModel)
                        plotter.clearModels();

                    plotter.addModel(model);
                }
            } else if (arg.equals("ADD_POINTS")) {

                String input = JOptionPane.showInputDialog(Grapher.this, "Number of points to add:");

                if (input != null && input.trim().length() > 0) {
                    try {
                        int num = Integer.parseInt(input);
                        plotOptions.setPointsToAdd(num);
                        plotOptions.setAddingPoint(true);
                        showTableAction.setEnabled(true);
                        //plotter.enableZoom(!bttn.isSelected());
                        plotter.repaint();
                    } catch (NumberFormatException ex) {
                    }
                }
            } else if (arg.equals("OVERRIDE_FUNCTION")) {

                AbstractButton jrmi = (AbstractButton) e.getSource();
                overridingFunction = jrmi.isSelected();

            } else if (arg.equals("OVERRIDE_MODEL")) {

                AbstractButton jrmi = (AbstractButton) e.getSource();
                overridingModel = jrmi.isSelected();

            } else if (arg.equals("ADD_FUNCTION")) {

                addExpression(ExpressionObject.FUNCTION);

            } else if (e.getActionCommand().equals("TRANSFORM_MODEL")) {

                ExpressionObject function = plotter.getSelectedExpression();

                if (function != null && function.getType() == ExpressionObject.MODEL) {

                    JExpressionDialog dialogOwner = new JExpressionDialog(function, dataSet);
                    JDialog dialog = dialogOwner.getDialog(Grapher.this, "Model transform");
                    dialog.setLocationRelativeTo(Grapher.this);
                    dialog.show();
                }
            } else if (e.getActionCommand().equals("TRANSFORM_FUNCTION")) {

                ExpressionObject function = plotter.getSelectedExpression();

                if (function != null && function.getType() == ExpressionObject.FUNCTION) {

                    JExpressionDialog dialogOwner = new JExpressionDialog(function, dataSet);
                    JDialog dialog = dialogOwner.getDialog(Grapher.this, "Function transform");
                    dialog.setLocationRelativeTo(Grapher.this);
                    dialog.show();
                }
            } else if (e.getActionCommand().equals("HIDE_MODEL") || e.getActionCommand().equals("HIDE_FUNCTION")) {

                if (plotter.getSelectedExpression() != null) {
                    plotter.getSelectedExpression().setVisible(false);
                    //actionMap.get("SHOW_FUNCTIONS").setEnabled(true);
                    //actionMap.get("SHOW_MODELS").setEnabled(true);
                }
            } else if (e.getActionCommand().equals("SHOW_FUNCTIONS")) {

                java.util.List v = plotter.getFunctions();
                for (int i = 0; i < v.size(); i++) {
                    ExpressionObject mf = (ExpressionObject) v.get(i);
                    mf.setVisible(true);
                }
            } else if (e.getActionCommand().equals("SHOW_MODELS")) {

                java.util.List v = plotter.getModels();
                for (int i = 0; i < v.size(); i++) {
                    ExpressionObject mf = (ExpressionObject) v.get(i);
                    mf.setVisible(true);
                }
            } else if (e.getActionCommand().equals("SHOW_TABLE")) {

                showTableAction.setEnabled(false);
                hideTableAction.setEnabled(true);
                actionMap.get("OPEN").setEnabled(false);
                actionMap.get("OPEN_REMOTE").setEnabled(false);
                tableDialog.pack();
                ComponentUtil.setLocationRelativeTo(tableDialog, Grapher.this);
                tableDialog.show();
            } else if (e.getActionCommand().equals("HIDE_TABLE")) {
                showTableAction.setEnabled(true);
                hideTableAction.setEnabled(false);
                tableDialog.hide();

            } else if (e.getActionCommand().equals("DELETE_FUNCTION") || e.getActionCommand().equals("DELETE_MODEL")) {

                ExpressionObject object = plotter.getSelectedExpression();

                if (object != null && object.getType() == ExpressionObject.FUNCTION)
                    plotter.deleteFunction(object);
                else
                    plotter.deleteModel(object);

                deleteFunctionAction.setEnabled(false);
                editFunctionAction.setEnabled(false);

                //showModelAction.setEnabled(false);
                editModelAction.setEnabled(false);
                actionMap.get("DELETE_MODEL").setEnabled(false);

            } else if (e.getActionCommand().equals("MODIFY_FUNCTION") ) {

                String functionString = plotter.getSelectedExpression().getUnparsedForm();
                //the code below is 1.3 java api but works in java 1.4
                //cause: for 1.3 compatibility and other reasons.

                String input =
                        (String) JOptionPane.showInputDialog(Grapher.this,
                                "Edit mathematical expression:",
                                "Input",
                                JOptionPane.INFORMATION_MESSAGE,
                                null, null, functionString);

                ExpressionObject newFunction = createFunction(input);

                if (newFunction != null) {
                    plotter.getSelectedExpression().setExpression(newFunction.getExpression());
                    plotter.getSelectedExpression().setUnparsedForm(input);
                    plotter.repaint();
                }

            } else if (e.getActionCommand().equals("MODIFY_MODEL")) { //added by djluo
            	
            	  ExpressionObject object = plotter.getSelectedExpression();
            	  String functionString = object.getUnparsedForm();
            	  
            	  String input =
                    (String) JOptionPane.showInputDialog(Grapher.this,
                            "Edit mathematical expression:",
                            "Input",
                            JOptionPane.INFORMATION_MESSAGE,
                            null, null, functionString);
            	  
            	  if (input == null)
                    return;

                ExpressionObject model = fitModel(input, null, null);
                
                if (model != null) {
                	   if (object != null && object.getType() == ExpressionObject.MODEL)
                	   	   plotter.deleteModel(object);
                	   plotter.addModel(model);
                }
            	
            } else if (e.getActionCommand().equals("CLEAR_ALL_POINTS")) {

                dataSet.clearAllRows();
                actionMap.get("OPEN").setEnabled(true);
                actionMap.get("OPEN_REMOTE").setEnabled(true);
            } else if (e.getActionCommand().equals("CLEAR_POINTS")) {

                plotter.clearSelectedPoints();
            } else if (e.getActionCommand().equals("SHOW_POINTS")) {
                plotOptions.setShowData(true);
                plotter.repaint();
            } else if (e.getActionCommand().equals("HIDE_POINTS")) {
                plotOptions.setShowData(false);
                plotter.repaint();
            } else if (e.getActionCommand().equals("LINEAR_MODEL")) {
                fitLinear();
            } else if (e.getActionCommand().equals("QUADRATIC_MODEL")) {
                fitQuadratic();
            }
             else if (e.getActionCommand().equals("CUBIC_MODEL")) {
                fitCubic();
            }
        }
    }

    class WindowListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            showTableAction.setEnabled(true);
            hideTableAction.setEnabled(false);
        }

        public void windowLostFocus(WindowEvent e) {
            tableDialog.toFront();
        }
    }

}




