/*
 * MainPanel.java
 *
 * Created on May 9, 2001, 2:38 PM
 */

package wvustat.table;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.interfaces.LaunchOption;
import wvustat.modules.*;
import wvustat.modules.genome.*;
import wvustat.swing.*;
import wvustat.util.*;
import wvustat.network.client.JRIClient;
import wvustat.network.*;
import wvustat.data.DataChangeEvent;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import javax.swing.tree.*;


/**
 * @author Hengyi Xue
 */
public class MainPanel extends JPanel implements ActionListener, LauncherPlugin, CallbackRemote {
	private static final long serialVersionUID = 1L;

	protected static JDesktopPane deskTop;
    protected static TaskTable taskPane;
    protected DataTree treePane;
    
    private File lastPath;
    private FrameListener frameListener;

    private DataSetTM activeData; 
    private DataEntryTable activeTable;
    private String dataUrl = "http://ideal.stat.wvu.edu/ideal/datasets/";
        
    public ArrayList dataList = new ArrayList();
    private ArrayList independentFrameList = new ArrayList(); // for object plot window in microarray analysis
    private GUIModule diagramModule; // to update diagram of objects in microarray analysis 
    private int anchorX = 30, anchorY = 30;
    private Action saveAction, saveAsAction, newAction, openAction, openRemoteAction,  
    			   openObjectsAction, diagramObjectsAction, downloadAnnoAction;
    private JMenu uploadMenu;


    /**
     * Creates new MainFrame
     */
    public MainPanel() {
        //This following line was added by JunTan
        init();
    }

    public static JDesktopPane getDesktopPane() {
        return deskTop;
    }
    
    public static TaskTable getTaskPane() {
    	return taskPane;
    }

    public void init() {
        createActions();
        
        deskTop = new JDesktopPane();
        deskTop.setBackground(new Color(172, 172, 172));
        deskTop.setMinimumSize(new Dimension(200,200));
        this.setLayout(new BorderLayout());
        //this.add(deskTop, BorderLayout.CENTER);

        frameListener = new FrameListener();

        try {
            lastPath = new File(System.getProperty("user.home"));
        }
        catch (Exception e) {
            lastPath = new File(".");
        }

        /* the next one line was commented by djluo 5/20/2005 
        this.add(createToolBar(), BorderLayout.NORTH); 
        */
        /* The following code is added by djluo 3/2/07 */
        
        taskPane = new TaskTable();
        deskTop.add(taskPane);
        taskPane.addInternalFrameListener(frameListener);
        
        treePane = new DataTree();
        DataTreeListener dataTreeListener = new DataTreeListener();
        treePane.addTreeSelectionListener(dataTreeListener);
        
        if (!LaunchOption.microArrayEnable)
        	this.add(deskTop, BorderLayout.CENTER);
        else {
        	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, deskTop);
        	splitPane.setOneTouchExpandable(true);
        	splitPane.setDividerLocation(200);
        	splitPane.setBorder(null);
        	this.add(splitPane, BorderLayout.CENTER);
        }
    }

    private void createActions() {
        saveAction = new DelegateAction("Save", ImageIcons.SAVE, this);
        saveAction.setEnabled(false);
        newAction = new DelegateAction("New", ImageIcons.NEW, this);
        saveAsAction = new DelegateAction("Save As...", ImageIcons.SAVE_AS, this);
        saveAsAction.setEnabled(false);
        openAction = new DelegateAction("Open...", ImageIcons.OPEN, this);
        openRemoteAction = new DelegateAction("Open Remote...", ImageIcons.OPEN_REMOTE, this);
        //uploadAction = new DelegateAction("Upload...", ImageIcons.UPLOAD, this);
        openObjectsAction = new DelegateAction("Open Objects...", ImageIcons.OPEN, this);
        diagramObjectsAction = new DelegateAction("Diagram Objects", ImageIcons.DIAGRAM, this);
        downloadAnnoAction = new DelegateAction("Download Annotations...", ImageIcons.OPEN_REMOTE, this);
    }

    public void buildMenu(JMenuBar menuBar) {
        JMenu menu = new JMenu("File");
        menu.add(newAction);
        menu.add(saveAction);
        menu.add(saveAsAction);
        menu.add(openAction);
        menu.add(openRemoteAction);
        menuBar.add(menu);
        
        if (LaunchOption.microArrayEnable) {
        	treePane.buildMenu(menuBar);
        	
        	if(LaunchOption.workflowEnable){
        		menu = new JMenu("Workflow");
        		
        		uploadMenu = new JMenu("Upload...");
        		menu.add(uploadMenu);
        		JMenuItem item = new JMenuItem("Upload 3' Affymetrix CEL files");
        		item.setActionCommand("UPLOAD_3PRIME_CELS");
        		item.addActionListener(this);
        		uploadMenu.add(item);
        		item = new JMenuItem("Upload txt file exported from Expression Console (2^ scale)");
        		item.setActionCommand("UPLOAD_TXT_FILE");
        		item.addActionListener(this);
        		uploadMenu.add(item);
        		item = new JMenuItem("Upload Exon or 3' CEL files for Probe Level Analysis");
        		item.setActionCommand("UPLOAD_EXON_CELS");
        		item.addActionListener(this);
        		uploadMenu.add(item);
        		
        		menu.add(openObjectsAction);
        		menu.add(diagramObjectsAction);
        		menu.addSeparator();
        		menu.add(downloadAnnoAction);
        		setLogonSensitiveMenu(false);
        		menuBar.add(menu);
        	}
        } 
        
        menuBar.revalidate();
    }

    protected Container createToolBar() {
        JToolBar box = new JToolBar();
        box.setFloatable(false);
        Font font = new Font("System", Font.PLAIN, 14);
        //Component separator=Box.createRigidArea(new Dimension(10,10));
        JButton bNew = new JButton(newAction);
        bNew.setText(null);
        bNew.setBorder(null);
        bNew.setFont(font);
        bNew.setActionCommand("New");
        bNew.setToolTipText("New Data Set");
        box.add(bNew);
        box.addSeparator();

        JButton bSave = new JButton(saveAction);
        bSave.setText(null);
        bSave.setEnabled(false);
        bSave.setFont(font);
        bSave.setBorder(null);
        bSave.setActionCommand("Save");
        bSave.setToolTipText("Save Data Set");
        box.add(bSave);
        box.addSeparator();
        
        JButton bSaveAs = new JButton(saveAsAction);
        bSaveAs.setText(null);
        bSaveAs.setEnabled(false);
        bSaveAs.setBorder(null);
        bSaveAs.setFont(font);
        bSaveAs.setActionCommand("Save As...");
        bSaveAs.setToolTipText("Save Data Set As...");
        box.add(bSaveAs);
        box.addSeparator();
        
        JButton bOpen = new JButton(openAction);
        bOpen.setText(null);
        bOpen.setBorder(null);
        bOpen.setFont(font);
        bOpen.setActionCommand("Open...");
        bOpen.setToolTipText("Open Data Set...");
        box.add(bOpen);
        box.addSeparator();
        bOpen.setEnabled(true);
        
        JButton bOpenRemote = new JButton(openRemoteAction);
        bOpenRemote.setText(null);
        bOpenRemote.setBorder(null);
        bOpenRemote.setFont(font);
        bOpenRemote.setActionCommand("Open Remote...");
        bOpenRemote.setToolTipText("Open Remote Data Set...");
        box.add(bOpenRemote);
        return box;
    }

    public void buildPlot(String arg) {
        GUIModule module = null;
        JInternalFrame jif = null;
        DataSet dataSet = activeData.getDataSet();

        PlotResolver builder = PlotResolver.getInstance();
        Vector vz = dataSet.getZVariables();

        if (arg.equals("y|z plot")) {
            try {
                Vector vy = dataSet.getYVariables();
                if (vy.size() == 0) {
                    showWarning("You must define a y variable for y|z plot.", this);
                    return;
                }

                for (int i = 0; i < vy.size(); i++) {
                    if (vz.size() == 0) {
                        try {
                            module = builder.buildPlot(dataSet, PlotResolver.Y_ZPLOT, i, 0, null);
                            jif = displayModule(module);
                            jif.addInternalFrameListener(frameListener);
                            activeTable.addDependentFrame(jif);

                        }
                        catch (IllegalArgumentException iex) {
                            showWarning(iex.getMessage(), this);
                        }
                    } else {
                        /*for (int j = 0; j < vz.size(); j++) {
                            Variable zVar = (Variable) vz.elementAt(j);
                          modified by djluo for multiple conditional variables
                            */
                    	       Variable zVar = (Variable) vz.elementAt(0);
                            try {
                                module = builder.buildPlot(dataSet, PlotResolver.Y_ZPLOT, i, 0, zVar);
                                jif = displayModule(module);
                                jif.addInternalFrameListener(frameListener);
                                activeTable.addDependentFrame(jif);

                            }
                            catch (IllegalArgumentException iex) {
                                showWarning(iex.getMessage(), this);
                            }
                        /*}*/
                    }
                }
            }
            catch (Exception re) {
                re.printStackTrace();
            }
        } else if (arg.equals("Parallel Coordinates")) {
        	try {
        		Vector vy = dataSet.getYVariables();
        		if (vy.size() <= 1) {
        			showWarning("You must define at least two y variables for parallel coordinates.", this);
        			return;
        		}
        		try {
        			module = builder.buildPlot(dataSet, PlotResolver.PARALLEL_COORDINATES, 0, 0, null);
        			jif = displayModule(module);
                    jif.addInternalFrameListener(frameListener);
                    activeTable.addDependentFrame(jif);
        		}
        		catch (IllegalArgumentException iex) {
                    showWarning(iex.getMessage(), this);
                }
        	}
        	catch (Exception re) {
        		re.printStackTrace();
        	}        	
        } else if (arg.equals("Grand Tour")) {
        	try {
        		Vector vy = dataSet.getYVariables();
        		Vector vx = dataSet.getXVariables();
        		
        		if ((vx.size() + vy.size()) <= 2) {
        			showWarning("You must define at least three x or y variables for grand tour.", this);
        			return;
        		}
        		try {
        			module = builder.buildPlot(dataSet, PlotResolver.GRAND_TOUR, 0, 0, null);
        			jif = displayModule(module);
                    jif.addInternalFrameListener(frameListener);
                    activeTable.addDependentFrame(jif);
        		}
        		catch (IllegalArgumentException iex) {
                    showWarning(iex.getMessage(), this);
                }
        	}
        	catch (Exception re) {
        		re.printStackTrace();
        	}  
        } else if (arg.equals("Heatmap")) {	
        	try {
    			module = builder.buildPlot(dataSet, PlotResolver.HEATMAP, 0, 0, null);
    			jif = displayModule(module);
                jif.addInternalFrameListener(frameListener);
                activeTable.addDependentFrame(jif);
    		}
    		catch (IllegalArgumentException iex) {
                showWarning(iex.getMessage(), this);
            }
        	
        } else if (arg.equals("xy|z plot")) {
            try {
                Vector vy = dataSet.getYVariables();
                Vector vx = dataSet.getXVariables();


                if (vy.size() == 0 || vx.size() == 0) {
                    showWarning("You must define a y variable and x variable for xy|z plot", this);
                    return;
                }


                for (int i = 0; i < vy.size(); i++) {
                    for (int j = 0; j < vx.size(); j++) {
                        if (vz.size() == 0) {
                            try {
                                module = builder.buildPlot(dataSet, PlotResolver.XY_ZPLOT, i, j, null);
                                jif = displayModule(module);
                                jif.addInternalFrameListener(frameListener);
                                activeTable.addDependentFrame(jif);

                            }
                            catch (IllegalArgumentException iex) {
                                showWarning(iex.getMessage(), this);
                            }
                        } else {
                            /*for (int k = 0; k < vz.size(); k++) {

                                Variable zVar = (Variable) vz.elementAt(k);*/
                        	
                                Variable zVar = (Variable) vz.elementAt(0);
                                try {
                                    module = builder.buildPlot(dataSet, PlotResolver.XY_ZPLOT, i, j, zVar);
                                    jif = displayModule(module);
                                    jif.addInternalFrameListener(frameListener);
                                    activeTable.addDependentFrame(jif);

                                }
                                catch (IllegalArgumentException iex) {
                                    showWarning(iex.getMessage(), this);
                                }
                            /*}*/
                        }
                    }
                }

            }
            catch (Exception error) {
                System.err.println("Exception occured while constructing xy|z plot");
                error.printStackTrace();
            }
        } else if (arg.equals("Chart")) {
            try {
                Vector vy = dataSet.getYVariables();
                Vector vx = dataSet.getXVariables();


                if (vy.size() == 0) {
                    showWarning("You must define at least a y variable for chart", this);
                    return;
                }


                if (vx.size() == 0) {
                	   showWarning("You must define at least a x variable for chart", this);
                    return;
                	 
                } else {
                    for (int i = 0; i < vy.size(); i++) {
                        for (int j = 0; j < vx.size(); j++) {
                            if (vz.size() == 0) {
                                try {
                                    module = builder.buildPlot(dataSet, PlotResolver.CHART, i, j, null);
                                    jif = displayModule(module);
                                    jif.addInternalFrameListener(frameListener);
                                    activeTable.addDependentFrame(jif);

                                }
                                catch (IllegalArgumentException iex) {
                                    showWarning(iex.getMessage(), this);
                                }
                            } else {
                                /*for (int k = 0; k < vz.size(); k++) {
                                    Variable zVar = (Variable) vz.elementAt(k);*/
                            	
                                    Variable zVar = (Variable) vz.elementAt(0);
                                    try {
                                        module = builder.buildPlot(dataSet, PlotResolver.CHART, i, j, zVar);
                                        jif = displayModule(module);
                                        jif.addInternalFrameListener(frameListener);
                                        activeTable.addDependentFrame(jif);

                                    }
                                    catch (IllegalArgumentException iex) {
                                        showWarning(iex.getMessage(), this);
                                    }
                                /*}*/
                            }
                        }
                    }
                }
            }
            catch (Exception re) {
                System.err.println("Exception occured while trying to construct bar chart.");
                re.printStackTrace();
            }
        } else if (arg.equals("Control Chart")) {
            try {
                Vector vy = dataSet.getYVariables();
                Vector vx = dataSet.getXVariables();

                if (vy.size() == 0 || vx.size() == 0) {
                    showWarning("You must define a y variable and x variable for control chart", this);
                    return;
                }


                for (int i = 0; i < vy.size(); i++) {
                	 for (int j = 0; j < vx.size(); j++) {

                		if (vz.size() == 0) {
                        try {
                            module = builder.buildPlot(dataSet, PlotResolver.CONTROL_CHART, i, j, null);
                            jif = displayModule(module);
                            jif.addInternalFrameListener(frameListener);
                            activeTable.addDependentFrame(jif);

                        }
                        catch (IllegalArgumentException iex) {
                            showWarning(iex.getMessage(), this);
                        }
                    } else {
                        /*for (int k = 0; k < vz.size(); k++) {
                            Variable zVar = (Variable) vz.elementAt(k);*/
                    	
                    	       Variable zVar = (Variable) vz.elementAt(0);
                            try {
                                module = builder.buildPlot(dataSet, PlotResolver.CONTROL_CHART, i, j, zVar);
                                jif = displayModule(module);
                                jif.addInternalFrameListener(frameListener);
                                activeTable.addDependentFrame(jif);

                            }
                            catch (IllegalArgumentException iex) {
                                showWarning(iex.getMessage(), this);
                            }

                        /*}*/
                    }
                	 }
                }
            }
            catch (Exception error) {
                System.err.println("Exception occured while constructing control chart");
                error.printStackTrace();
            }
        } else if (arg.equals("Fit Model...")) {
            try {            	
                Vector vy = dataSet.getYVariables();
                Vector vx = dataSet.getXVariables();


                if (vy.size() == 0 || vx.size() == 0) {
                    showWarning("You must define a y variable and x variable for linear model", this);
                    return;
                }
                
                for (int i = 0; i < vx.size(); i++){
                	String name = ((Variable)vx.elementAt(i)).getName();
                	if(!JRIClient.isIdentifier(name)){
                		showWarning("illegal variable name \"" + name + "\"", this);
                		return;                	
                	}
                }
                
                for (int i = 0; i < vy.size(); i++){
                	String name = ((Variable)vy.elementAt(i)).getName();
                	if(!JRIClient.isIdentifier(name)){
                		showWarning("illegal variable name \"" + name + "\"", this);
                		return;                	
                	}
                }

      
                ModelConstructor mc = new ModelConstructor(ComponentUtil.getTopComponent(this), dataSet);
                mc.pack();
                mc.setLocationRelativeTo(this);
                mc.show();
                String input = mc.getModel();
                if (input == null)
                    return;
                else
                {
                	builder.setInputModel(input);
                	builder.setModelFamily(mc.getFamily());
                	builder.setModelLink(mc.getLink());
                }

                for (int i = 0; i < vy.size(); i++) {
                    if (vz.size() == 0) {
                        try {
                            module = builder.buildPlot(dataSet, PlotResolver.LINEAR_MODEL, i, 0, null);
                            module.setMainPanel(this);
                            jif = displayModule(module);
                            jif.addInternalFrameListener(frameListener);
                            activeTable.addDependentFrame(jif);
                        }
                        catch (IllegalArgumentException iex) {
                            showWarning(iex.getMessage(), this);
                        }
                        
                    } else {
                        Variable zVar = (Variable) vz.elementAt(0);
                        try {
                            module = builder.buildPlot(dataSet, PlotResolver.LINEAR_MODEL, i, 0, zVar);
                            module.setMainPanel(this);
                            jif = displayModule(module);
                            jif.addInternalFrameListener(frameListener);
                            activeTable.addDependentFrame(jif);
                        }
                        catch (IllegalArgumentException iex) {
                            showWarning(iex.getMessage(), this);
                        }
                        
                    }
                }

            }
            catch (Exception error) {
                System.err.println("Exception occured while constructing linear model");
                error.printStackTrace();
            }
        }
        

    }
    
    public void callback(Object result){
    
    	GUIModule module = null;
    	JInternalFrame jif = null;
    	PlotResolver builder = PlotResolver.getInstance();
    	
    	if (diagramModule != null){
    		try{
    			diagramModule.update("");
    		}catch(RemoteException ex){}
    	}
    	
    	if (result instanceof String) {    	    		
    		ComponentUtil.showErrorMessage(this, (String)result);
    	}
    	else if (result instanceof StringBuffer) {
    		JOptionPane.showMessageDialog(this, ((StringBuffer)result).toString());
    	}
    	else if (result instanceof Biobase) {
    		Biobase base = (Biobase)result;
    		
    		try {
    			module = builder.buildPlot(base);
    			module.setMainPanel(this);
    			jif = displayModule(module);
                jif.addInternalFrameListener(frameListener);
                independentFrameList.add(jif);
    		}
    		catch (IllegalArgumentException iex) {
                showWarning(iex.getMessage(), this);
            }
    		catch (Exception error) {
    			System.err.println("Exception occured while constructing object window");
                error.printStackTrace();
            }
    		
    	}
    	else if (result instanceof GDataSet) {
    		GDataSet gdata = (GDataSet)result;
    		
    		try {
    			DataSetTM ds = DatasetFactory.convertGDataSet(gdata); 			
                    
                displayDataTable(ds);
                treePane.addNode(activeTable);
    		}
    		catch (Exception error) {
    			System.err.println("Exception occured while constructing data window");
                error.printStackTrace();
            }
    		
    	}
    	
    }

    private JInternalFrame displayModule(GUIModule module) {
        JInternalFrame cjf = new JInternalFrame();

        cjf.setTitle(module.getMetaData().getPlotDescription());

        cjf.setClosable(true);
        cjf.setMaximizable(true);
        cjf.setResizable(true);
        cjf.setIconifiable(true);

        cjf.setContentPane(module);
        cjf.setJMenuBar(module.getJMenuBar());

        deskTop.add(cjf);

        if (!(module instanceof GrandTourModule))
        	cjf.setBounds(anchorX, anchorY, 420, 490);
        else
        	cjf.setBounds(anchorX, anchorY, 520, 490); //for GrandTour

        anchorX += 20;
        anchorY += 20;

        cjf.show();
        
        return cjf;
    }
    

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String arg = e.getActionCommand();

        if (arg.equals("New")) {
            DataEntryTable dt = new DataEntryTable();
            dt.setGraphHandler(this);
            activeData = dt.getTableModel();
            activeTable = dt;
            deskTop.add(dt);
            dt.addInternalFrameListener(frameListener);
            dt.setBounds(30, 30, dt.getPreferredSize().width, dt.getPreferredSize().height);
            dt.show();

            dataList.add(activeTable);
            treePane.addNode(activeTable);

            setSensitiveMenu(true);
        } else if (arg.equals("Save")) {
            if (activeData != null) {
                File saveFile = activeData.getFile();
                if (saveFile == null) {
                    JFileChooser fc = new JFileChooser(lastPath);
                    fc.addChoosableFileFilter(new XMLFileFilter());
                    File suggestedFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + activeData.getName() + ".xml");
                    fc.setSelectedFile(suggestedFile);
                    int option = fc.showSaveDialog(this);
                    if (option == JFileChooser.CANCEL_OPTION)
                        return;

                    saveFile = fc.getSelectedFile();
                    if (saveFile.getName().endsWith(".xml") == false)
                        saveFile = new File(saveFile.getAbsolutePath() + ".xml");
                    activeData.setFile(saveFile);
                    String fname = saveFile.getName();
                    //String fs=System.getProperty("file.separator");
                    String dataName = fname;
                    if (fname.indexOf('.') > -1)
                        dataName = fname.substring(0, fname.lastIndexOf('.'));
                    activeData.setName(dataName);
                    activeTable.setTitle(dataName);
                }

                String content = new TableModelToXML().toXML(activeData.getDataSet(), activeData.getName());
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
                    bw.write(content);
                    bw.close();
                    activeData.save();
                }
                catch (IOException error) {
                    error.printStackTrace();
                }
            }

        } else if (arg.equals("Save As...")) {
            JFileChooser fc = new JFileChooser(lastPath);
            fc.addChoosableFileFilter(new CsvFileFilter());
            fc.addChoosableFileFilter(new TextFileFilter());
            fc.addChoosableFileFilter(new XMLFileFilter());            

            if (activeData.getFile() == null) {
                File suggestedFile = new File(System.getProperty("user.home") + System.getProperty("file.separator") + activeData.getName() + ".xml");
                fc.setSelectedFile(suggestedFile);
            } else {
                fc.setSelectedFile(activeData.getFile());
            }
            
            int option = fc.showSaveDialog(this);
            
            if (option == JFileChooser.APPROVE_OPTION) {
                File saveFile = fc.getSelectedFile();
                lastPath = saveFile.getParentFile();

                if (fc.getFileFilter() instanceof XMLFileFilter) {
                    if (!saveFile.getName().toLowerCase().endsWith(".xml")) {
                        saveFile = new File(saveFile.toString() + ".xml");
                    }

                    if (saveFile.exists()) {
                        option = JOptionPane.showOptionDialog(this, "File exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                        if (option != JOptionPane.YES_OPTION)
                            return;
                    }

                    String fname = saveFile.getName();
                    //String fs=System.getProperty("file.separator");
                    String dataName = fname.substring(0, fname.lastIndexOf('.'));
                    activeData.setName(dataName);
                    activeTable.setTitle(dataName);
                    String content = new TableModelToXML().toXML(activeData.getDataSet(), dataName);

                    try {
                        BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
                        bw.write(content);
                        bw.close();
                        activeData.save();
                        activeData.setFile(saveFile);
                    }
                    catch (IOException error) {
                        error.printStackTrace();
                    }
                }
                else if (fc.getFileFilter() instanceof CsvFileFilter) {
                    if (saveFile.getName().endsWith(".csv") == false)
                        saveFile = new File(saveFile.getAbsolutePath() + ".csv");
                    DataSet dataset = activeData.getDataSet();
                    try {
                        DataFileUtils.exportCsv(dataset, saveFile, ",");
                    }
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                else {
                	if (saveFile.getName().endsWith(".txt") == false)
                        saveFile = new File(saveFile.getAbsolutePath() + ".txt");
                    DataSet dataset = activeData.getDataSet();
                    try {
                        DataFileUtils.exportCsv(dataset, saveFile, "\t");
                    }
                    catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }

        } else if (arg.equals("Open...")) {
            JFileChooser fc = new JFileChooser(lastPath);
            fc.addChoosableFileFilter(new CsvFileFilter());
            fc.addChoosableFileFilter(new TextFileFilter());
            fc.addChoosableFileFilter(new XMLFileFilter());
            int option = fc.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File openFile = fc.getSelectedFile();
                lastPath = openFile.getParentFile();
                try {
                    DataSetTM ds = readFile(openFile);
                    if (ds == null)
                        return;

                    displayDataTable(ds);
                    activeData.setFile(openFile);
                    treePane.addNode(activeTable);
                }
                catch (FileNotFoundException error2) {
                    error2.printStackTrace();
                }
                catch (IOException error) {
                    JOptionPane.showMessageDialog(this, error.getMessage());
                    error.printStackTrace();
                }
                catch (ParseException error3) {
                    JOptionPane.showMessageDialog(this, error3.getMessage());
                    error3.printStackTrace();
                }
                catch (Exception error) {
                    JOptionPane.showMessageDialog(this, error.getMessage());
                    error.printStackTrace();
                }
            }

        } else if (arg.equals("Open Remote...")) {
            try {
                DataChooser dc = new DataChooser(ComponentUtil.getTopComponent(this), dataUrl);
                dc.pack();
                dc.setLocationRelativeTo(this);
                dc.show();

                //String input=JOptionPane.showInputDialog(this,"Please input the URL of the data file:", "Input", JOptionPane.QUESTION_MESSAGE);
                String input = dc.getSelectedItem();
                if (input == null)
                    return;

                openRemoteXMLFile(input.trim());
                
            }
            catch (Exception error) {
                JOptionPane.showMessageDialog(this, error.getMessage());
                error.printStackTrace();
            }

        } else if (arg.equals("UPLOAD_3PRIME_CELS") || arg.equals("UPLOAD_TXT_FILE") || arg.equals("UPLOAD_EXON_CELS")) {
        	
        	if (taskPane.containRunningTask()) {
            	JOptionPane.showMessageDialog(this, "Cannot upload while task is running.", "Error", JOptionPane.ERROR_MESSAGE);
            	return;
            }        	
        	
        	int type;
        	if (arg.equals("UPLOAD_3PRIME_CELS"))
        		type = FileUploader.TYPE_3PRIME_CELS;
        	else if (arg.equals("UPLOAD_TXT_FILE"))
        		type = FileUploader.TYPE_TXT_FILE;
        	else
        		type = FileUploader.TYPE_EXON_CELS;        	
        	
        	final FileUploader fu = new FileUploader(ComponentUtil.getTopComponent(this), type);
    		fu.setLocationRelativeTo(this);
    		fu.show();
    		final String objName = fu.getName();
    		if (objName == null || objName.trim().equals(""))
    			return;
    		else {
    			final RawFileUploader uploader = new RawFileUploader();
    			final File[] files = uploader.matchedFiles(fu.getPath());
    			
    			Runnable runnable = new Runnable()
    			{
    	            public void run()
    	            {
    			
    	            	ProgressMonitor progressMonitor = new ProgressMonitor(MainPanel.this,
    	            										"Uploading...", "", 0, files.length + 2);
    	            	progressMonitor.setMillisToDecideToPopup(0);
    	            	progressMonitor.setMillisToPopup(0);
    	            	progressMonitor.setProgress(0);
    			
    	            	try {
    	            		progressMonitor.setNote("Prepare");
    	            		String pathname = objName;
    	            		JRIClient.deleteFolder(pathname);
    	            		progressMonitor.setProgress(1);
    				
    	            		for (int i = 0; i < files.length; i++){
    	            			if (progressMonitor.isCanceled()) 
    	            				throw new Exception("user canceled");
    					
    	            			progressMonitor.setNote(files[i].getName());
    	            			
    	            			uploader.upload(files[i], pathname);
    	            			progressMonitor.setProgress(i + 2);
    	            		}
    				
    	            		progressMonitor.setNote("Create Workspace");
    	            		
    	            		if (files.length > 0) {
    	            			if (fu.getType() == FileUploader.TYPE_3PRIME_CELS)
    	            				JRIClient.importFiles(pathname, objName);
    	            			else if (fu.getType() == FileUploader.TYPE_TXT_FILE)
    	            				JRIClient.importTxtFile(pathname, fu.getPath().getName(), objName);
    	            			else
    	            				JRIClient.importExonFiles(pathname, fu.getChipType(), objName);
    	            		} else {
    	            			throw new Exception("no file matches");
    	            		}
    	            		
    	            		progressMonitor.close();
    	            		
    	            		if (diagramModule != null)
    	            			diagramModule.update("");
    				
    	            	} catch (Exception ex) {
    	            		ex.printStackTrace();
    	            		progressMonitor.close();
    	            		ComponentUtil.showErrorMessage(MainPanel.this, ex.getMessage(), "Upload failed");
    	            		return;
    	            	}	
    			
    	            	JOptionPane.showMessageDialog(MainPanel.this, "Upload " + files.length + " files successfully!");

    	            }
    	        };
    	        
    	        Thread thread = new Thread(runnable);
    	        thread.start();
    	        
    		}
    		
        } else if (arg.equals("Open Objects...")) {	
        	try {
        		ObjectChooser oc = new ObjectChooser(ComponentUtil.getTopComponent(this));
        		oc.setLocationRelativeTo(this);
        		oc.show();
        		
        		if (oc.getOption() == ObjectChooser.DELETE_OPTION) {
        			String[] input = oc.getSelectedItems();
        			if (input == null || input.length == 0)
        				return;
        			
        			JRIClient.deleteObjects(this, input);
        			
        		} else if (oc.getOption() == ObjectChooser.OK_OPTION) {
        			String input = oc.getSelectedItem();
        			if (input == null)
        				return;
        			
        			JRIClient.getObjectSummary(this, input.trim());
        		} 
        		
        	} catch (Exception ex) {
        		ComponentUtil.showErrorMessage(this, ex.getMessage());
        	}        	
        	
        } else if (arg.equals("Diagram Objects")) {
        	if (diagramModule == null) {
	        	PlotMetaData pmd = new PlotMetaData();
	        	diagramModule = new DiagramModule();
	        	diagramModule.setMainPanel(this);
	        	pmd.setPlotDescription("Diagram");
	        	diagramModule.setMetaData(pmd);
	
	        	JInternalFrame jif = displayModule(diagramModule);
	        	jif.addInternalFrameListener(frameListener);
	        	independentFrameList.add(jif);
        	}
        } 
        else if (arg.equals("SUBSET_TABLE"))
        {
        	int[] rowRange = activeTable.getSelectedRows();
        	int[] colRange = activeTable.getSelectedColumns();
            
            SubsetTableDialog std = new SubsetTableDialog(ComponentUtil.getTopComponent(this), rowRange, colRange, 
            											activeTable.getRowCount(), activeTable.getColumnCount());
            std.pack();
            std.setLocationRelativeTo(this);
            std.show();
            
            if (std.getOption() == SubsetTableDialog.CANCEL_OPTION)
            	return;
            
            try {
            	//String input = JOptionPane.showInputDialog(this, "The name of new table:", "Input", JOptionPane.QUESTION_MESSAGE);
            	//if (input == null) return;//click cancel button 
            	
    			DataSetTM ds = DatasetFactory.subsetDataSet(activeData, std.getRowRange(), std.getColumnRange(), std.getTableName(), std.isTableLinked()); 			
                
    			boolean[] st = activeData.getDataSet().getStates();
    			int parentId = activeData.getDataSet().getId();
                
    			displayDataTable(ds);
                
    			rowRange = std.getRowRange();
                for (int i = 0; i < rowRange.length; i++)
                	activeData.getDataSet().setState(st[rowRange[i]], i);
                
                //Add a node to data tree. 
                if (std.isTableLinked()) 
                	treePane.addNode(parentId, activeTable);
                else
                	treePane.addNode(activeTable);
    		}
    		catch (Exception error) {
    			showWarning(error.getMessage(), this);
                //error.printStackTrace();
            }
            
        }
        else if (arg.equals("SORT_TABLE"))
        {
        	SortTableDialog st = new SortTableDialog(ComponentUtil.getTopComponent(this), activeData.getDataSet());
        	st.pack();
        	st.setLocationRelativeTo(this);
        	st.show();
        	
        	if (st.getOption() == SortTableDialog.CANCEL_OPTION)
        		return;
        	
        	try {
        		int[] orders = activeData.getDataSet().getSortOrders(st.getVarNames(), st.getVarOrders());
        		DataSetTM ds = DatasetFactory.sortDataSet(activeData, orders, st.getTableName());
        		
        		displayDataTable(ds);
        		treePane.addNode(activeTable);
        	}
        	catch (Exception error) {
        		showWarning(error.getMessage(), this);
        	}
        }
        else if (arg.equals("MERGE_TABLE"))
        {
        	MergeTableDialog mt = new MergeTableDialog(ComponentUtil.getTopComponent(this), this, activeData.getDataSet());
        	mt.pack();
        	mt.setLocationRelativeTo(this);
        	mt.show();
        	
        	if (mt.getOption() == MergeTableDialog.CANCEL_OPTION)
        		return;
        	
        	try {
        		DataSetTM ds;
        		if (mt.isByKey())
        			ds = DatasetFactory.mergeDataSetByLabel(activeData, mt.getDataMergeTo(), mt.getTableName(), mt.isDupRemoved());
        		else 
        			ds = DatasetFactory.mergeDataSetSideBySide(activeData, mt.getDataMergeTo(), mt.getTableName(), mt.isDupRemoved());
        		displayDataTable(ds);
        		treePane.addNode(activeTable);
        	}
        	catch (Exception error) {
        		showWarning(error.getMessage(), this);
        	}
        }
        else if (arg.equals("Download Annotations..."))
        {
        	JFileChooser fc = new JFileChooser(lastPath);
        	fc.setDialogTitle("Open a txt file that contains a gene ID column:");
        	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    		fc.addChoosableFileFilter(new TextFileFilter());
    		
    		int retval = fc.showOpenDialog(this);
        	
        	if (retval == JFileChooser.APPROVE_OPTION) {
        		File openFile = fc.getSelectedFile();
        		lastPath = openFile.getParentFile();
        		
        		try {
                    DataSetTM ds = readFile(openFile);
                    if (ds == null)
                        return;

                    AnnotateSelector selector = new AnnotateSelector(ComponentUtil.getTopComponent(this), ds.getDataSet());
                	selector.pack();
                	selector.setLocationRelativeTo(this);
                	selector.setVisible(true);
                	
                	if (selector.getOption() == AnnotateSelector.CANCEL_OPTION)
                		return;
                	
                	JRIClient.getGeneAnnotationByNames(this, selector.getType(), selector.getIDs());
                }
                catch (Exception error) {
                    //JOptionPane.showMessageDialog(this, error.getMessage());
                	ComponentUtil.showErrorMessage(this, error.getMessage());
                    error.printStackTrace();
                }
        	}
        }
        else if (arg.equals("Exit")) {
            processExit();
        }

        else {
            buildPlot(arg);
        }
    }
    
    public void displayDataTable(DataSetTM ds) {
    	DataEntryTable dt = new DataEntryTable(ds);
        dt.setGraphHandler(this);
        activeData = dt.getTableModel();
        activeTable = dt;

        dataList.add(activeTable);

        Dimension d1 = deskTop.getSize();
        Dimension d2 = dt.getPreferredSize();
        dt.addInternalFrameListener(frameListener);
        deskTop.add(dt);
        dt.setBounds(d1.width / 2 - d2.width / 2, d1.height / 2 - d2.height / 2, dt.getPreferredSize().width, dt.getPreferredSize().height);

        dt.show();
        setSensitiveMenu(true);
    }

    public void openRemoteXMLFile(String filename) {
    	try {
    		URL url = new URL(dataUrl + filename.trim());

    		InputStreamReader isr = new InputStreamReader(url.openStream());
    		XMLDataParser parser = new XMLDataParser();
    		DataSetTM ds = parser.parse(isr);

    		displayDataTable(ds);
    		
    		//Add a node to data tree. 
    		treePane.addNode(activeTable);
    	
    	} catch (Exception error) {
            JOptionPane.showMessageDialog(this, error.getMessage());
            error.printStackTrace();
        }
    }
    
    
    /*
     * return boolean: login successful or not
     */
    public boolean login() {
    	JRIChooser jc = new JRIChooser(ComponentUtil.getTopComponent(this));
		jc.setLocationRelativeTo(this);
		jc.show();
		
		if (jc.getOption() == JRIChooser.OK_OPTION) {
			
			String input = jc.getBasicHost();
			JRIClient.BASIC_HOST_NAME = input.trim();
		
			input = jc.getEnhancedHost();
			JRIClient.HOST_NAME = input.trim();
			
			input = jc.getUsername();
			if (input != null && input.length() != 0) {
				try {
	    			JRIClient.userLogon(jc.getUsername(), jc.getPassword());
	    			setLogonSensitiveMenu(true);
	    			return true;
	    		} catch (Exception ex) {
	    			ComponentUtil.showErrorMessage(this, ex.getMessage());
	    		}
			}
		}
		return false;
    }
    
    /*
     * return boolean: logout successful or not
     */
    public boolean logout() {
    	int option = JOptionPane.showOptionDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (option != JOptionPane.YES_OPTION)
            return false;
        
        if (dataList.size() > 0 || independentFrameList.size() > 0) {
        	JOptionPane.showMessageDialog(this, "Unclosed window exists. Please close all windows.", "Error", JOptionPane.ERROR_MESSAGE);
        	return false;
        }
        
        taskPane.deleteFinishedTask();
        if (taskPane.getTaskCount() > 0) {
        	JOptionPane.showMessageDialog(this, "Unfinished task exists. Cannot logout now.", "Error", JOptionPane.ERROR_MESSAGE);
        	return false;
        }
    	    	
    	JRIClient.userLogout();
    	setLogonSensitiveMenu(false);
    	return true;
    }
    
    
    public void arrangeAllWindows() {
    	
		Dimension d1 = deskTop.getSize();
		Dimension d2 = taskPane.getSize();
		taskPane.setLocation(d1.width - d2.width, d1.height - d2.height);	
		
		
		anchorX = 30; 
		anchorY = 30;
		
		for (int i = 0; i < dataList.size(); i++){
			DataEntryTable dt = (DataEntryTable) dataList.get(i);
			d1 = deskTop.getSize();
			d2 = dt.getPreferredSize();
			dt.setLocation(d1.width / 2 - d2.width / 2, d1.height / 2 - d2.height / 2);
			dt.toFront();
			
			ArrayList dependentFrames = dt.getDependentFrames();
			
			if(i == dataList.size() - 1 && dependentFrames.size() == 0 && independentFrameList.size() == 0){
				try{
					dt.setSelected(true);
				}catch(Exception ex){}
			}
			
			for (int j = 0; j < dependentFrames.size(); j++){
				JInternalFrame jif = (JInternalFrame) dependentFrames.get(j);
				jif.setLocation(anchorX, anchorY);
				jif.toFront();
				anchorX += 20;
				anchorY += 20;
				
				ArrayList depdepFrames = ((GUIModule)jif.getContentPane()).getDependentFrames();
				for (int k = 0; k < depdepFrames.size(); k++){
					jif = (JInternalFrame) depdepFrames.get(k);
					jif.setLocation(anchorX, anchorY);
					jif.toFront();
					anchorX += 20;
					anchorY += 20;
				}
				
				if(i == dataList.size() - 1 && j == dependentFrames.size() - 1 && independentFrameList.size() == 0){
    				try{
    					jif.setSelected(true);
    				}catch(Exception ex){}
    			}
			}
			
		}
		
		for (int i = 0; i < independentFrameList.size(); i++){
			JInternalFrame jif = (JInternalFrame) independentFrameList.get(i);
			jif.setLocation(anchorX, anchorY);
			jif.toFront();
			anchorX += 20;
			anchorY += 20;
			
			if(i == independentFrameList.size() - 1){
				try{
					jif.setSelected(true);
				}catch(Exception ex){}
			}
		}

    }
    
    protected void processExit() {

        if (activeData != null && activeData.isChanged()) {
            int input = JOptionPane.showConfirmDialog(this, "The table is changed, are you sure you want to discard the changes?", "Warning", JOptionPane.YES_NO_OPTION);
            if (input == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else
            System.exit(0);

    }

    class FrameListener extends InternalFrameAdapter {
        public void internalFrameActivated(InternalFrameEvent evt) {
            Object obj = evt.getSource();

            if (obj instanceof DataEntryTable) {
                DataEntryTable dt = (DataEntryTable) evt.getSource();
                activeData = dt.getTableModel();
                activeTable = dt;

                setSensitiveMenu(true);
            }
        }

        public void internalFrameClosing(InternalFrameEvent evt) {
            JInternalFrame obj = (JInternalFrame) evt.getSource();

            if (obj instanceof DataEntryTable) {
                DataEntryTable dt = (DataEntryTable) evt.getSource();
                if (dt.getTableModel().isChanged() && !dt.getTableModel().isSaved()) {
                    int input = JOptionPane.showConfirmDialog(dt, "The table is changed, are you sure you want to discard the changes?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (input == JOptionPane.YES_OPTION) {
                    	treePane.closeNode(dt); 
                    	dt.getTableModel().getDataSet().fireDataChanged(new DataChangeEvent(this));
                        dt.dispose();
                        dt.closeDependentFrames();
                        
                        anchorX = 30;
                        anchorY = 30;
                        dataList.remove(dt);
                        //activeData = null;

                        //activeTable = null;
                        if (dataList.size() > 1 || dataList.size() == 0)
                            setSensitiveMenu(false);
                        else {
                            activeTable = (DataEntryTable) dataList.get(0);
                            activeData = activeTable.getTableModel();
                        }
                    }
                } else {
                	treePane.closeNode(dt); //must do before fire invalid data change event.
                	dt.getTableModel().getDataSet().fireDataChanged(new DataChangeEvent(this));
                    dt.dispose();
                    dt.closeDependentFrames();
                    
                    anchorX = 30;
                    anchorY = 30;
                    dataList.remove(dt);
                    //activeData = null;
                    //activeTable = null;

                    if (dataList.size() > 1 || dataList.size() == 0) {
                        setSensitiveMenu(false);
                    } else {
                        activeTable = (DataEntryTable) dataList.get(0);
                        activeData = activeTable.getTableModel();
                    }
                }
            } else if (obj instanceof TaskTable) {
            	
            	//taskPane.deleteFinishedTask(); 	

            } else {
                GUIModule module = (GUIModule) obj.getContentPane();
                module.destroy();

                if (anchorX > 20)
                    anchorX -= 10;
                if (anchorY > 20)
                    anchorY -= 10;
                
                //added by djluo to delete reference on diagramModule
                if (module instanceof DiagramModule){
                	diagramModule = null;
                }
                
                //added by djluo to delete module from dependentFrames
                for (int i = 0; i < dataList.size(); i++){
        			DataEntryTable dt = (DataEntryTable) dataList.get(i);
        			ArrayList dependentFrames = dt.getDependentFrames();
        			if(dependentFrames.contains(obj))
        				dependentFrames.remove(obj);
                }
                
                if (independentFrameList.contains(obj))
                	independentFrameList.remove(obj);
            }


        }
    }
    
    class DataTreeListener extends MouseAdapter/*implements TreeSelectionListener*/ {
    	//public void valueChanged(TreeSelectionEvent event){
    	public void mousePressed(MouseEvent e) {
    		
    		if((SwingUtilities.isRightMouseButton(e) || e.isControlDown())) return;
    		
    		//TreePath path = event.getNewLeadSelectionPath();
    		TreePath path = treePane.getTree().getPathForLocation(e.getX(), e.getY());
    		if(path == null)
    			return;
    		
    		Object[] nodes = path.getPath();
    		DefaultMutableTreeNode node;
    		DataTree.NodeInfo nodeInfo;
    		int k = nodes.length - 1;
    		
    		//find the deepest node whose table is open in the tree
    		while (k >= 1) {
    			node = (DefaultMutableTreeNode)nodes[k];
    			nodeInfo = (DataTree.NodeInfo)node.getUserObject();
    			if (nodeInfo.getTable() != null || k == 1)
    				break;
    			k--;
    		}
    		
    		for (int i = k; i < nodes.length; i++) {
    			node = (DefaultMutableTreeNode)nodes[i];
    			nodeInfo = (DataTree.NodeInfo)node.getUserObject();
    		
    			if (nodeInfo.getTable() != null) { //table already exist
    				nodeInfo.getTable().toFront();
    			} else { 
    				//table does not exist
    				try {   		
    					if (((DefaultMutableTreeNode)node.getRoot()).isNodeChild(node)) { //top level node

    						if (nodeInfo.getData() != null) { //table in memory
    							displayDataTable(nodeInfo.getData());
    							treePane.openNode(node, activeTable);
    						} else { //table not in memory
    							DataSetTM ds = readFile(new File(nodeInfo.getFileName()));
    							displayDataTable(ds);
    							treePane.openNode(node, activeTable);
    						}
    					} else { //node representing subset data
    					
    						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
    						DataTree.NodeInfo pinfo = (DataTree.NodeInfo)parentNode.getUserObject();
    						if (pinfo.getTable() != null) {
    							DataSetTM ds = DatasetFactory.subsetDataSet(pinfo.getTable().getTableModel(), 
    											nodeInfo.getLinkedRows(), nodeInfo.getLinkedCols(), nodeInfo.getName(), true);
    							displayDataTable(ds);
    							treePane.openNode(node, activeTable);
    						}
    					}
    					
    				} catch (Exception error) {
						JOptionPane.showMessageDialog(MainPanel.this, "Error occured while loading data: " + error.getMessage());
						error.printStackTrace();
					}
    			}
    		}
    	}
    }

    private void setSensitiveMenu(boolean val) {
        saveAction.setEnabled(val);
        saveAsAction.setEnabled(val);
    }
    
    private void setLogonSensitiveMenu(boolean val) {
    	uploadMenu.setEnabled(val);
    	openObjectsAction.setEnabled(val);
    	diagramObjectsAction.setEnabled(val);
    	downloadAnnoAction.setEnabled(val);
    }
    
    public ArrayList getDataSetList() {
    	ArrayList list = new ArrayList();
    	
    	for (int i = 0; i < dataList.size(); i++){
    		DataEntryTable dt = (DataEntryTable) dataList.get(i);
    		list.add(dt.getTableModel().getDataSet());
    	}
    	return list;
    }


    private void showWarning(String msg, Component rootComp) {
        JOptionPane.showMessageDialog(rootComp, msg);
    }

    private DataSetTM readFile(File file) throws Exception {
        String name = file.getName();
        String dataSetName = name.substring(0, name.lastIndexOf("."));
        DataSetTM dataSet = null;
        if (name.toUpperCase().endsWith(".CSV")) {
            TextFileParser parser = new TextFileParser();
            parser.setDelimiter(",");
            java.util.List columns = parser.parse(new FileInputStream(file));
            dataSet = DatasetFactory.createDataSet(dataSetName, columns);
        } else if (name.toUpperCase().endsWith(".TXT")) {
            int fileFormat = JFileFormatPanel.showFileFormatDialog(this);
            if (fileFormat == JFileFormatPanel.TAB_DELIMITED) {
                TextFileParser parser = new TextFileParser();
                parser.setDelimiter("\t");
                java.util.List columns = parser.parse(new FileInputStream(file));
                dataSet = DatasetFactory.createDataSet(dataSetName, columns);
            } else if (fileFormat == JFileFormatPanel.FIXED_WIDTH) {
                TextFileParser parser = new TextFileParser();
                parser.setDelimiter(" ");
                java.util.List columns = parser.parse(new FileInputStream(file));
                dataSet = DatasetFactory.createDataSet(dataSetName, columns);
            } else if (fileFormat == JFileFormatPanel.COMMA_DELIMITED) {
                TextFileParser parser = new TextFileParser();
                parser.setDelimiter(",");
                java.util.List columns = parser.parse(new FileInputStream(file));
                dataSet = DatasetFactory.createDataSet(dataSetName, columns);
            }
        } else //Default format is XML
        {
            FileReader reader = new FileReader(file);
            XMLDataParser parser = new XMLDataParser();
            dataSet = parser.parse(reader);
        }
        return dataSet;
    }
}
