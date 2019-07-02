/*
 * EditableHeader.java
 *
 * Created on October 3, 2000, 3:36 PM
 */

package wvustat.table;

/**
 * EditableHeader implements a table header that can be edited manually. A user can double click on the header,
 * a dialog will appear that lets the user define the column properties.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 3, 2000
 */

import wvustat.data.NumVariable;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;
import wvustat.interfaces.LaunchOption;
import wvustat.swing.DelayedDispatcher;
import wvustat.swing.DelayedDispatch;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class EditableHeader extends JTableHeader implements MouseListener, DelayedDispatcher
{
	private static final long serialVersionUID = 1L;
	
	protected JTable table;
    private DelayedDispatch dispatcher;

    public EditableHeader(JTable table)
    {
        super(table.getColumnModel());
        setTable(table);
        this.table = table;
        this.setReorderingAllowed(false);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        addMouseListener(this);
    }

    public EditableHeader(TableColumnModel tcm, JTable table)
    {
        super(tcm);
        setTable(table);
        this.table = table;
        this.setReorderingAllowed(false);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        addMouseListener(this);
    }


    protected TableCellRenderer createDefaultRenderer()
    {
    	return new HeaderRenderer();
    }


    /** Invoked when the mouse has been clicked on a component.
     */
    public void mouseClicked(MouseEvent e)
    {
        int c = columnAtPoint(e.getPoint());

        // Test if this is an applet
        Container comp = table.getParent();
        while (comp != null && !(comp instanceof JFrame))
            comp = comp.getParent();

        if (comp == null)
            return;
        
        if (LaunchOption.trustedEnv == false)
        	return;

        if (e.getClickCount() == 2)
        {
            if (dispatcher != null) 
            	dispatcher.isDoubleClick = true;
            
            if (table.getModel() instanceof DataSetTM)
            {
                
                int modelIndex = table.getColumnModel().getColumn(c).getModelIndex();
                if ( modelIndex < 2 ) return;
                    
                DataSetTM tm = (DataSetTM) table.getModel();
                Variable v = tm.getVariable(modelIndex);
                ColumnAttributes attributes = new ColumnAttributes();
                attributes.setName(v.getName());
                attributes.setRole(v.getRole());
                attributes.setType(((v instanceof NumVariable) ? 0 : 1));
                attributes.setNumOfDigits(tm.getColumnFormat(modelIndex));
                attributes.setOrdinal(v.isOrdinal());
                attributes.setLevelCheck(v.isLevelCheck());
                /*if (v.isLevelCheck())
                	attributes.setLevels(v.getLevels());
                else
                	attributes.setLevels(Arrays.asList(v.getDistinctCatValues()));*/
                
                table.getColumnModel().getSelectionModel().clearSelection();
                
                ColumnEditor editor = new ColumnEditor((JFrame) comp, attributes);
                editor.pack();
                editor.setLocationRelativeTo(comp);
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
                        if (tm.getDataSet().getLabelVariable() != null)
                        {
                            int option = JOptionPane.showOptionDialog(table.getRootPane(), "Replace the previous label column?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                            if (option == JOptionPane.CANCEL_OPTION)
                            {
                                return;
                            }
                        }

                    }
                    tm.setColumnAttributes(attributes, modelIndex);
                }               
            }
            
        }

        else if (e.getClickCount() == 1)
        {
            dispatcher = new DelayedDispatch(e, EditableHeader.this);
            dispatcher.start();
        }
    }


    public void respondToSingleClick(MouseEvent e)
    {

        int c = columnAtPoint(e.getPoint());

        if (e.isShiftDown())
        {
            int startIndex = getTable().getColumnModel().getSelectionModel().getMinSelectionIndex();
            int endIndex = getTable().getColumnModel().getSelectionModel().getMaxSelectionIndex();
            if (c < startIndex)
            {
                startIndex = c;
            }
            else if (c > endIndex)
                endIndex = c;
            getTable().setColumnSelectionInterval(startIndex, endIndex);
        }
        else if (e.isMetaDown())
        {
        	if (getTable().getColumnModel().getSelectionModel().isSelectedIndex(c))
        		getTable().removeColumnSelectionInterval(c, c);
        	else	
        		getTable().addColumnSelectionInterval(c, c);
        }
        else
        {
        	if (respondToClickOnIcon(e)) return; //added for the click on icon of table header
        		//if return is true, the respond is done, so no need to handle the header any more.
        	
            getTable().setColumnSelectionInterval(c, c);
        }
        //getTable().setRowSelectionInterval(0, table.getModel().getRowCount() - 1);
    }
    
    /**
     * Invoked when a mouse is clicked on the tripod icon of table header. 
     * Column role will be changed if click is on the axis of the tripod.
     */
    private boolean respondToClickOnIcon(MouseEvent e)
    {
    	int c = columnAtPoint(e.getPoint());
    	Rectangle rect = getHeaderRect(c);

    	int x = e.getPoint().x - rect.x;
    	int y = e.getPoint().y - rect.y;
    	
    	int modelIndex = table.getColumnModel().getColumn(c).getModelIndex();
    	if ( modelIndex < 2 ) return false;
    		
    	DataSetTM tm = (DataSetTM) table.getModel();
    	Variable v = tm.getVariable(modelIndex);
    	int role = v.getRole();
    		
    	if (role != DataSet.U_ROLE && role != DataSet.X_ROLE && role != DataSet.Y_ROLE && role != DataSet.Z_ROLE) {
    		return false;
    	}
    		
    	if (RoleIcon.getXAxisArea().contains(x, y)) {
    		tm.setColumnRole(DataSet.X_ROLE, modelIndex);
    	} 
    	else if (RoleIcon.getYAxisArea().contains(x, y)) {
    		tm.setColumnRole(DataSet.Y_ROLE, modelIndex);
    	}
    	else if (RoleIcon.getZAxisArea().contains(x, y)) {
    		tm.setColumnRole(DataSet.Z_ROLE, modelIndex);
    	}
    	else if (RoleIcon.getArea().contains(x, y)) {
    		tm.setColumnRole(DataSet.U_ROLE, modelIndex);
    	} 
    	else {
    		return false;
    	}

    	return true;
    }

    /** Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e)
    {
    }

    /** Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e)
    {
        if (getTable() instanceof MyJTable)
        {
            ((MyJTable) getTable()).adjustModel();
        }
    }

    /** Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e)
    {
    }

    /** Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e)
    {
    }

}
