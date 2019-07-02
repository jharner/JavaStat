/**
 * DataEntryTable.java
 *
 * Revised on June 3, 2008, 3:49 PM
 */

package wvustat.table;

import gnu.trove.TIntArrayList;
import wvustat.contentviewer.BrowserLaunch;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.RemoteObserver;
import wvustat.interfaces.Variable;
import wvustat.interfaces.LaunchOption;
import wvustat.modules.GUIModule;
import wvustat.modules.TransformAction;
import wvustat.swing.DoubleField;
import wvustat.swing.table.JTableRowHeaderAdapter;
import wvustat.swing.table.TableColumnHidableModel;
import wvustat.swing.MarkerMenuElement;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.*;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class DataEntryTable extends JInternalFrame implements ActionListener, MouseListener, RemoteObserver
{
	private static final long serialVersionUID = 1L;

	protected DataSetTM tableModel;
    protected JMenuBar jmb;
    protected JTable table;
    protected GraphMenu graphMenu;
    protected JMenuItem fitModelMI, subsetTableMI, sortTableMI, mergeTableMI; 
    protected MarkerMenuElement markerMI;
    protected TransformAction xformAction;

    private Vector frameListeners;
    private ActionListener graphHandler;
    private ArrayList dependentFrames = new ArrayList();
    private JMenuItem hideColumnMI;
    private ListSelectionListener rowSelectionListener;
    private boolean rowToggle = false, colToggle = false; // all rows/columns selected indicator for corner

    
    /** Creates new DataEntryTable */
    public DataEntryTable()
    {
        super("untitled");

        tableModel = new DataSetTM(10, 1);
        setTitle(tableModel.getDataSet().getName());
        init();

    }

    public DataEntryTable(DataSet dataSet)
    {
        super(dataSet.getName());
        tableModel = new DataSetTM(dataSet);
        init();
    }
    

    public DataEntryTable(DataSetTM tableModel)
    {
        super(tableModel.getDataSet().getName());
        this.tableModel=tableModel;
        init();
    }

    public DataEntryTable(int row, int col)
    {
        super("untitled");
        tableModel = new DataSetTM(row, col);
        init();
    }
    
    
    public void setGraphHandler(ActionListener listener)
    {
        graphHandler = listener;
        graphMenu.addActionListener(graphHandler);
        fitModelMI.addActionListener(graphHandler);
        subsetTableMI.addActionListener(graphHandler);
        sortTableMI.addActionListener(graphHandler);
        mergeTableMI.addActionListener(graphHandler);
    }
    

    public void addInternalFrameListener(InternalFrameListener listener)
    {
        if (frameListeners == null)
            frameListeners = new Vector();

        frameListeners.add(listener);
        super.addInternalFrameListener(listener);
    }

    public void removeInternalFrameListener(InternalFrameListener listener)
    {
        if (frameListeners != null)
            frameListeners.remove(listener);

        super.removeInternalFrameListener(listener);
    }

    protected void fireFrameClosingEvent()
    {
        InternalFrameEvent evt = new InternalFrameEvent(this, InternalFrameEvent.INTERNAL_FRAME_CLOSING);
        for (int i = 0; i < frameListeners.size(); i++)
        {
            InternalFrameListener l = (InternalFrameListener) frameListeners.elementAt(i);
            l.internalFrameClosing(evt);
        }
    }


    public JMenuBar getJMenuBar()
    {
        return jmb;
    }

    public DataSetTM getTableModel()
    {
        return tableModel;
    }
    
    public JTable getTable()
    {
    	return table;
    }    

    protected void init()
    {
        tableModel.getDataSet().addRemoteObserver(this);
        
        table = new MyJTable(tableModel);
        table.addMouseListener(this);
        rowSelectionListener = new RowSelectionListener();
        table.getSelectionModel().addListSelectionListener(rowSelectionListener);
        table.setAutoCreateColumnsFromModel(true);
        //table.createDefaultColumnsFromModel();
        table.setCellSelectionEnabled(true);
        //table.setBorder(new EmptyBorder(0, 0, 0, 2));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(Number.class, new NumberRenderer());
        table.setDefaultRenderer(Double.class, new NumberRenderer());//added by djluo
        table.setDefaultRenderer(String.class, new StringRenderer());//added by djluo
        table.setDefaultEditor(Number.class, new TableDefaults.NumberEditor(new DoubleField(8)));
        table.setDefaultEditor(String.class, new TableDefaults.GenericEditor());
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnSelectionListener());

        new JTableExcelAdapter(table);

        TableColumnModel tcm = table.getColumnModel();
        table.setTableHeader(new EditableHeader(tcm, table));

        JScrollPane scrollPane = new JTableRowHeaderAdapter(table);
        
        final JLabel leftLabel = new JLabel("Obs", SwingConstants.LEFT);
        final JLabel rightLabel = new JLabel(new CornerIcon());
        leftLabel.setForeground(Color.black);
        leftLabel.setFont(table.getFont());
        leftLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent evt)
            {
                if (evt.getClickCount() == 2)
                {
                    int input = JOptionPane.showConfirmDialog(table.getRootPane(), "Do you want to use the row numbers as observation labels?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (input == JOptionPane.YES_OPTION)
                    {
                        leftLabel.setIcon(new RoleIcon(DataSet.L_ROLE));
                        leftLabel.setIconTextGap(2);
                        tableModel.setRowNumberEnabled(true);
                    }
                    else
                    {
                        leftLabel.setIcon(null);
                        leftLabel.setIconTextGap(0);
                        tableModel.setRowNumberEnabled(false);
                    }
                }
                else {
                	table.editingCanceled(new ChangeEvent(this));
                	table.setRowSelectionInterval(0, 0);
                	table.setColumnSelectionInterval(0, 0);
                	table.clearSelection();     
                	rowToggle = false;
                	colToggle = false;
                	rightLabel.setIcon(new CornerIcon());                	
                }
            }
        });

        rightLabel.addMouseListener(new MouseAdapter()
        {
        	public void mouseClicked(MouseEvent evt)
        	{
        		if (CornerIcon.getUpperArea().contains(evt.getPoint())) {
        			if (!colToggle) {
        				table.setColumnSelectionInterval(0, table.getColumnCount() - 1);
        				colToggle = true; 
        			} else {
        				table.removeColumnSelectionInterval(0, table.getColumnCount() - 1);
        				colToggle = false;
        			}
        		} else if (CornerIcon.getLowerArea().contains(evt.getPoint())) {
        			if (!rowToggle) {
        				table.setRowSelectionInterval(0, table.getRowCount() - 1);   
        				rowToggle = true;
        			} else {
        				table.removeRowSelectionInterval(0, table.getRowCount() - 1);
        				rowToggle = false;
        			}
        		}
        		rightLabel.setIcon(new CornerIcon(rowToggle, colToggle));
        	}        	
        });
        
        JComponent corner = new CornerComponent(leftLabel, rightLabel);
        corner.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
        
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corner);

        this.setContentPane(scrollPane);

        Dimension d1 = table.getPreferredSize();
        Dimension d2 = scrollPane.getRowHeader().getPreferredSize();

        if (LaunchOption.trustedEnv == false)
        	return; // do not create menu; do not access MainPanel
        
        Dimension fullSize = MainPanel.getDesktopPane().getSize();
        
        int width = d1.width + d2.width + 35;
        int height = d1.height + 105;

        if (height > fullSize.height - 150)
        {
            height = fullSize.height - 150;
            width += 20;
        }
        if (width > fullSize.width - 120)
        {
            width = fullSize.width - 120;
            height += 20;
        }

        setPreferredSize(new Dimension(width, height));

        this.setResizable(true);
        this.setClosable(true);
        this.setMaximizable(false);
        this.setIconifiable(true);

        createMenuBar();
    }

    public void doDefaultCloseAction()
    {
        this.fireFrameClosingEvent();
    }

    public int[] getSelectedColumns()
    {
        return table.getSelectedColumns();
    }

    public int[] getSelectedRows()
    {
        return table.getSelectedRows();
    }
    
    public int getRowCount()
    {
    	return table.getRowCount();
    }
    
    public int getColumnCount()
    {
    	return table.getColumnCount();
    }

    protected void createMenuBar()
    {
        jmb = new JMenuBar();

        JMenu jm = new JMenu("Table");
        subsetTableMI = jm.add("Subset...");
        subsetTableMI.setActionCommand("SUBSET_TABLE");
        sortTableMI = jm.add("Sort...");
        sortTableMI.setActionCommand("SORT_TABLE");
        mergeTableMI = jm.add("Merge...");
        mergeTableMI.setActionCommand("MERGE_TABLE");
        jmb.add(jm);
        
        
        jm = new JMenu("Rows");
        JMenuItem jmi = jm.add("Add Rows ...");
        jmi.addActionListener(this);
        jmi = jm.add("Delete Rows");
        jmi.addActionListener(this);
        jm.addSeparator();
        
        JMenu jm2 = new JMenu("Select Rows");
        jmi = jm2.add("Invert Row Selection");
        jmi.addActionListener(this);
        jm2.addSeparator();
        jmi = jm2.add("Select All Rows");
        jmi.addActionListener(this);
        jmi = jm2.add("Select Where...");
        jmi.setEnabled(false);
        jm2.addSeparator();
        jmi = jm2.add("Clear Row Selection");
        jmi.addActionListener(this);
        jm.add(jm2);
        
        jm.addSeparator();
        jmi = jm.add("Color...");
        jmi.addActionListener(this);
        
        jm2 = new JMenu("Marker");
        markerMI = new MarkerMenuElement();
        markerMI.addActionListener(this);
        jm2.add(markerMI);
        jm.add(jm2);
        
        jm.addSeparator();
        jmi = jm.add("Mask selected rows");
        jmi.setActionCommand("MASK_SELECTED_ROWS");
        jmi.addActionListener(this);
        jm.addSeparator();
        jmi = jm.add("Clear row states");
        jmi.setActionCommand("CLEAR_ROW_STATES");
        jmi.addActionListener(this);


        jmb.add(jm);

        jm = new JMenu("Columns");
        jmi = jm.add("Add Column...");

        jmi.addActionListener(this);

        jmi = jm.add("Delete Columns...");
        jmi.setActionCommand("DELETE_COLUMNS");
        jmi.addActionListener(this);

        this.hideColumnMI = new JMenuItem("Hide Columns");
        hideColumnMI.setActionCommand("HIDE_COLUMNS");
        hideColumnMI.addActionListener(this);
        jm.add(hideColumnMI);

        jm.addSeparator();

        jmi = jm.add("Column Properties...");
        jmi.setActionCommand("SHOW_ATTRIBUTES");
        jmi.addActionListener(this);

        jm.addSeparator();
        jmi = jm.add("Preselect Column Roles...");
        jmi.setActionCommand("PRESELECT_COLUMN_ROLES");
        jmi.addActionListener(this);
        
        jmi = jm.add("Remove Column Roles");
        jmi.setActionCommand("REMOVE_COLUMN_ROLES");
        jmi.addActionListener(this);
        jm.addSeparator();
        jmi=jm.add("Move Selected Columns...");
        jmi.setActionCommand("MOVE_COLUMNS");
        jmi.addActionListener(this);

        jm.addSeparator();
        xformAction = new TransformAction(tableModel.getDataSet(), this);
        jm.add(xformAction);

        jmb.add(jm);

        jm = new JMenu("Analyze");
        fitModelMI = new JMenuItem("Fit Model...");
        jm.add(fitModelMI);
        
        //fitModelMI.addActionListener(graphHandler);
        if (LaunchOption.microArrayEnable) 
        	jmb.add(jm);
        
        
        graphMenu = new GraphMenu();
        //graphMenu.addActionListener(graphHandler);
        jmb.add(graphMenu);

        this.setJMenuBar(jmb);
    }


    public void actionPerformed(ActionEvent e)
    {
        String arg = e.getActionCommand();

        if (arg.equals("Add Rows ..."))
        {
        	if (tableModel.getDataSet().hasParent()) {
        		JOptionPane.showMessageDialog(this, "Can not add rows for a subset table!");
        		return;
        	}
        	
            String input = JOptionPane.showInputDialog(this, "The number of rows to add:", "Input", JOptionPane.INFORMATION_MESSAGE);
            try
            {
                int n = Integer.parseInt(input);
                tableModel.addRows(n);
            }
            catch (NumberFormatException nfe)
            {
            }
        }
        else if (arg.equals("Add Column..."))
        {
        	if (tableModel.getDataSet().hasParent()) {
        		JOptionPane.showMessageDialog(this, "Can not add column for a subset table!");
        		return;
        	}
        	
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            ColumnAttributes attr = new ColumnAttributes();
            ColumnEditor editor = new ColumnEditor(frame, attr);
            editor.pack();
            editor.setLocationRelativeTo(this);
            editor.setVisible(true);
            if (editor.getOption() == ColumnEditor.OK_OPTION)
            {
                attr = editor.getColumnAttributes();
                tableModel.addColumn(attr);
            }
        }
        else if (arg.equals("Delete Rows"))
        {
            int[] range = table.getSelectedRows();
            if(range==null || range.length==0)
                return;
            
            tableModel.deleteRows(range);

        }
        else if (arg.equals("DELETE_COLUMNS"))
        {
            int[] range = table.getSelectedColumns();
            if(range==null || range.length==0)
                return;
            
            StringBuffer sb = new StringBuffer();
            sb.append("Are you sure you want to delete selected columns?");
            
            for (int i = 0; i < range.length; i++)
            {
                sb.append("\n");
                sb.append(table.getColumnName(range[i]));
            }
            
            int option = JOptionPane.showOptionDialog(this, sb.toString(), "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option != JOptionPane.YES_OPTION)
                return;
            
            int[] indices = new int[range.length];
            for (int i = 0; i < range.length; i++)
            {
                indices[i] = table.getColumnModel().getColumn(range[i]).getModelIndex();
            }
            Arrays.sort(indices);
            for (int i = indices.length - 1; i >= 0; i--)
            {
                tableModel.deleteColumn(indices[i]);
            }

        }
        else if (arg.equals("Color..."))
        {
            Color color = JColorChooser.showDialog(this, "Choose a color", Color.black);
            if (color != null)
            {
                int[] rows = table.getSelectedRows();
                for (int i = 0; i < rows.length; i++)
                {
                    tableModel.setValueAt(color, rows[i], 0);
                }
            }
        }
        else if (arg.equals("MARKER_SELECTED"))
        {
        	int t = markerMI.getSelectedMarkerType();
        	int[] rows = table.getSelectedRows();
            for (int i = 0; i < rows.length; i++)
            {
            	tableModel.setValueAt(new Integer(t), rows[i], 0);
            }
        }
        else if (arg.equals("HIDE_COLUMNS"))
        {
            int[] columns = table.getColumnModel().getSelectedColumns();
            if (columns == null || columns.length == 0)
                return;

            TableColumnHidableModel tcm = (TableColumnHidableModel) table.getColumnModel();
            java.util.List theList = new ArrayList();
            boolean arrayValid = true;
            for (int i = 0; i < columns.length; i++)
            {
                theList.add(tcm.getColumn(columns[i]));
                if (i > 0)
                    arrayValid = arrayValid && columns[i] == columns[i - 1] + 1;
            }
            if (!arrayValid)
            {
                JOptionPane.showMessageDialog(this, "Can not hide non-consecutive columns!");
                return;
            }
            tcm.hideColumns(theList);
            hideColumnMI.setText("Display Hiden Columns");
            hideColumnMI.setActionCommand("DISPLAY_COLUMNS");
        }
        else if (arg.equals("DISPLAY_COLUMNS"))
        {
            TableColumnHidableModel tcm = (TableColumnHidableModel) table.getColumnModel();
            tcm.unhideColumns();
            hideColumnMI.setText("Hide Columns");
            hideColumnMI.setActionCommand("HIDE_COLUMNS");

        }
        else if (arg.equals("SHOW_ATTRIBUTES"))
        {
            int c = table.getSelectedColumn();
            if (c == -1) return;
       
            Variable v = tableModel.getVariable(table.getColumnModel().getColumn(c).getModelIndex());
            ColumnAttributes attributes = new ColumnAttributes();
            attributes.setName(v.getName());
            attributes.setRole(v.getRole());
            attributes.setNumOfDigits(tableModel.getColumnFormat(table.getColumnModel().getColumn(c).getModelIndex()));
            attributes.setType(v.getType());
            attributes.setOrdinal(v.isOrdinal());
            attributes.setLevelCheck(v.isLevelCheck());
            /*if (v.isLevelCheck())
            	attributes.setLevels(v.getLevels());
            else
            	attributes.setLevels(Arrays.asList(v.getDistinctCatValues()));*/

            ColumnEditor editor = new ColumnEditor((JFrame) table.getTopLevelAncestor(), attributes);
            editor.pack();
            editor.setLocationRelativeTo(table);
            editor.setVisible(true);
            
            if (editor.getOption() == ColumnEditor.OK_OPTION)
            {
             	if (attributes.getRole() == DataSet.L_ROLE)
                {
                    //if (v.getDistinctCatValues().length != v.getSize())
             		if (v.getNumOfDistinctCatValues() != v.getSize())
                    {
                        JOptionPane.showMessageDialog(table.getRootPane(), "Label variable needs to have unique values!");
                        return;
                    }
                    if (tableModel.getDataSet().getLabelVariable() != null)
                    {
                        int option = JOptionPane.showOptionDialog(table.getRootPane(), "Replace the previous label column?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                        if (option == JOptionPane.CANCEL_OPTION)
                        {
                            return;
                        }
                    }
                }
              	
                tableModel.setColumnAttributes(attributes, c);
            }
      
        }
        else if (arg.equals("PRESELECT_COLUMN_ROLES"))
        {
        	RolePreselector editor = new RolePreselector((JFrame) table.getTopLevelAncestor(), tableModel);
            editor.pack();
            editor.setLocationRelativeTo(table.getTopLevelAncestor());
            editor.setVisible(true);
            
        }
        else if (arg.equals("REMOVE_COLUMN_ROLES"))
        {
            for (int i = 2; i < tableModel.getColumnCount(); i++)
            {
                tableModel.setColumnRole(DataSet.U_ROLE, i);
            }
        }
        else if(arg.equals("MOVE_COLUMNS"))
        {
            int[] columns = table.getColumnModel().getSelectedColumns();
            if (columns == null || columns.length == 0)
                return;

            boolean arrayValid = true;
            for (int i = 0; i < columns.length; i++)
            {
                if (i > 0)
                    arrayValid = arrayValid && columns[i] == columns[i - 1] + 1;
            }
            if (!arrayValid)
            {
                JOptionPane.showMessageDialog(this, "Can not move non-consecutive columns!");
                return;
            }

            if(columns[0]==0 || columns[0]==1)
            {
                JOptionPane.showMessageDialog(this, "Can not move 'Color' column and 'Mask' column");
                return;
            }

            TIntArrayList indexList=new TIntArrayList(columns);

            java.util.List columnNames=new ArrayList();
            for(int i=2;i<tableModel.getColumnCount();i++)
            {
                if(!indexList.contains(i))
                    columnNames.add(tableModel.getColumnName(i));
            }
            JColumnChooser chooser=new JColumnChooser(columnNames);
            int option=JOptionPane.showOptionDialog(this,chooser, "Options", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,null,null);
            if(option==JOptionPane.OK_OPTION)
            {
                String name=chooser.getSelectedColumnName();
                int moveTo=tableModel.getColumnIndex(name);
                if(!chooser.isBefore())
                    moveTo++;

                tableModel.moveColumns(columns[0], columns[columns.length-1], moveTo-2);
            }
        }
        else if (arg.equals("MASK_SELECTED_ROWS"))
        {
            int[] rows = table.getSelectedRows();
            /*for (int i = 0; i < rows.length; i++)
            {
                tableModel.setValueAt(new Boolean(true), rows[i], 1);
            }*/
            
            tableModel.setMasks(new Boolean(true), rows);
        }
        else if (arg.equals("CLEAR_ROW_STATES"))
        {
            for (int i = 0; i < tableModel.getRowCount(); i++)
            {
                tableModel.setValueAt(Color.black, i, 0);
                //tableModel.setValueAt(new Boolean(false), i, 1);
            }
            
            int[] rows = new int[tableModel.getRowCount()];
            for (int i = 0; i < rows.length; i++)
            	rows[i] = i;
            tableModel.setMasks(new Boolean(false), rows);
        }
        else if (arg.equals("Invert Row Selection"))
        {
        	int[] rows = table.getSelectedRows();
        	try{
        		table.getSelectionModel().removeListSelectionListener(rowSelectionListener);
        		tableModel.getDataSet().removeRemoteObserver(DataEntryTable.this);
        		
        		table.setRowSelectionInterval(0, table.getRowCount() - 1);
        		for (int i = 0; i < rows.length; i++) {
        			table.getSelectionModel().removeSelectionInterval(rows[i], rows[i]);
        		}
        		
        		for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.getDataSet().setState(true, i);
                }
                for (int i = 0; i < rows.length; i++) {
                    tableModel.getDataSet().setState(false, rows[i]);
                }
        	}
        	finally
        	{
        		table.getSelectionModel().addListSelectionListener(rowSelectionListener);
        		tableModel.getDataSet().addRemoteObserver(DataEntryTable.this);
        	}
        }
        else if (arg.equals("Select All Rows"))
        {
        	table.setRowSelectionInterval(0, table.getRowCount() - 1);  
        	
        	rowToggle = true;
        	JScrollPane scrollPane = (JScrollPane)this.getContentPane();
            JLabel corner = ((CornerComponent)scrollPane.getCorner(JScrollPane.UPPER_LEFT_CORNER)).getRightPart();
            corner.setIcon(new CornerIcon(rowToggle, colToggle));
        }
        else if (arg.equals("Clear Row Selection"))
        {
        	table.removeRowSelectionInterval(0, table.getRowCount() - 1);
        	
        	rowToggle = false;
        	JScrollPane scrollPane = (JScrollPane)this.getContentPane();
            JLabel corner = ((CornerComponent)scrollPane.getCorner(JScrollPane.UPPER_LEFT_CORNER)).getRightPart();
            corner.setIcon(new CornerIcon(rowToggle, colToggle));
        }
        
    }

    protected void processExit()
    {
        if (tableModel.isChanged())
        {
            int input = JOptionPane.showConfirmDialog(this, "The table is changed, are you sure you want to discard the changes?", "Warning", JOptionPane.YES_NO_OPTION);
            if (input == JOptionPane.YES_OPTION)
            {
                Component comp = getParent();
                while (comp != null && !(comp instanceof JFrame))
                    comp = comp.getParent();

                ((JFrame) comp).dispose();
                System.exit(0);
            }
        }

    }

    public void addDependentFrame(JInternalFrame jif)
    {
        dependentFrames.add(jif);
    }

    public void hideDependentFrames()
    {
        for (int i = 0; i < dependentFrames.size(); i++)
        {
            JInternalFrame jif = (JInternalFrame) dependentFrames.get(i);
            jif.setVisible(false);
        }
    }

    public void showDependentFrames()
    {
        for (int i = 0; i < dependentFrames.size(); i++)
        {
            JInternalFrame jif = (JInternalFrame) dependentFrames.get(i);
            jif.setVisible(true); 
            //jif.show();
        }
    }

    public void closeDependentFrames()
    {
        for (int i = 0; i < dependentFrames.size(); i++)
        {
            JInternalFrame jif = (JInternalFrame) dependentFrames.get(i);
            ((GUIModule) jif.getContentPane()).destroy();
            jif.dispose();
        }

        dependentFrames.clear();
    }
    
    public ArrayList getDependentFrames()
    {
    	return dependentFrames;
    }

    public void update(String msg) throws RemoteException
    {
        if (msg.equals("yystate"))
        {
            table.getSelectionModel().removeListSelectionListener(rowSelectionListener);

            try
            {
                boolean[] v = tableModel.getDataSet().getStates();
                for (int i = 0; i < v.length; i++)
                {

                    Boolean b = new Boolean(v[i]);
                    if (b.booleanValue())
                    {
                    	//comment this in order not to highlight the whole row when selected.
                        //table.getColumnModel().getSelectionModel().addSelectionInterval(0, table.getColumnCount()-1);
                    	
                        table.getSelectionModel().addSelectionInterval(i, i);
                    }
                    else
                        table.getSelectionModel().removeSelectionInterval(i, i);

                }
            }
            finally
            {
                table.getSelectionModel().addListSelectionListener(rowSelectionListener);
            }
        }
        else if(msg.equals("yyrownumber"))
        {
        	JScrollPane scrollPane = (JScrollPane)this.getContentPane();
            JLabel corner = ((CornerComponent)scrollPane.getCorner(JScrollPane.UPPER_LEFT_CORNER)).getLeftPart();
            if(tableModel.getDataSet().isRowNumberEnabled())
                corner.setIcon(new RoleIcon(DataSet.L_ROLE));
            else
                corner.setIcon(null);
        }
    }

    static class NumberRenderer extends DefaultTableCellRenderer
    {
		private static final long serialVersionUID = 1L;

		public NumberRenderer()
        {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value.equals(Variable.NUM_MISSING_VAL))
            {
                this.setText("");
            }

            return this;
        }

    }
    
    static class StringRenderer extends DefaultTableCellRenderer
    {
		private static final long serialVersionUID = 1L;

		public StringRenderer()
        {
            super();
            setHorizontalAlignment(JLabel.LEFT);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null)
            	if (((String)value).startsWith("http://"))
            	    this.setText("<html><font color=blue><u>"+value+"</u></font></html>");

            return this;
        }

    }

    class RowSelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent event)
        {
            int[] rows = table.getSelectedRows();
            try
            {
                tableModel.getDataSet().removeRemoteObserver(DataEntryTable.this);
                for (int i = 0; i < tableModel.getRowCount(); i++)
                {
                    tableModel.getDataSet().setState(false, i);
                }
                for (int i = 0; i < rows.length; i++)
                {
                    int row = rows[i];
                    tableModel.getDataSet().setState(true, row);
                }
            }
            finally
            {
                tableModel.getDataSet().addRemoteObserver(DataEntryTable.this);
            }
        }
    }


    public void mouseReleased(java.awt.event.MouseEvent p1)
    {
    }

    public void mouseEntered(java.awt.event.MouseEvent p1)
    {
    }

    public void mousePressed(java.awt.event.MouseEvent p1)
    {
    	JTable table = (JTable) p1.getSource();
    	Point pt = p1.getPoint();
        int r = table.rowAtPoint(pt);
        int c = table.columnAtPoint(pt);
        
        if (table.getColumnClass(c) != String.class)
            return;
        String value = (String)tableModel.getValueAt(r, c);
        if (value != null && value.startsWith("http://")) {
        	(new BrowserLaunch()).openURL(value);
        }
    }

    public void mouseExited(MouseEvent p1)
    {
    }


    public void mouseClicked(MouseEvent me)
    {
        if (me.getClickCount() == 2)
        {

            JTable table = (JTable) me.getSource();
            Point pt = me.getPoint();
            int r = table.rowAtPoint(pt);
            int c = table.columnAtPoint(pt);

            if (table.getColumnClass(c) != Color.class)
                return;
            Color color;
            color = JColorChooser.showDialog(table.getRootPane(), "Palette", Color.black);
            if (color != null)
                tableModel.setValueAt(color, r, c);
            return;
        }
        /*
        else
        {
            if (me.isShiftDown() == false)
                table.getColumnModel().getSelectionModel().clearSelection();
            int c = table.columnAtPoint(me.getPoint());
            table.getSelectionModel().clearSelection();
            table.getColumnModel().getSelectionModel().addSelectionInterval(c, c);
        }
        */
    }

    class ColumnSelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            table.getTableHeader().repaint();
            /*
            int minIndex = table.getColumnModel().getSelectionModel().getMinSelectionIndex();
            int maxIndex = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();

            if (minIndex == maxIndex && minIndex > 0 && (tableModel.getColumnClass(minIndex) == Double.class ||
            		                                        tableModel.getColumnClass(minIndex) == Integer.class))
            {
                xformAction.setEnabled(true);
            }
            else
                xformAction.setEnabled(false);
            */
        }
    }
    
    class CornerComponent extends JPanel
    {	
    	private static final long serialVersionUID = 1L;
    	private JLabel leftPart, rightPart;
    	
    	public CornerComponent(JLabel label1, JLabel label2) {
    		this.leftPart = label1;
    		this.rightPart = label2;
    		setLayout(new BorderLayout());
    		setBackground(Color.white);
    		
    		add(leftPart, BorderLayout.CENTER);
    		add(rightPart, BorderLayout.EAST);
    	}
    	
    	public JLabel getLeftPart() {
    		return leftPart;
    	}
    	
    	public JLabel getRightPart() {
    		return rightPart;
    	}    	
    }

    
    
    public static void main(String[] args)
    {
        JTable table=new JTable(5,5);
        JFrame jf=new JFrame();
        jf.setContentPane(new JTableRowHeaderAdapter(table));
        jf.pack();
        jf.show();
    }
}
