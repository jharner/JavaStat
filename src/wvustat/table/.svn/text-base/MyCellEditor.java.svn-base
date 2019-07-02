/*
 * MyCellEditor.java
 *
 * Created on April 30, 2002, 3:44 PM
 */

package wvustat.table;

import wvustat.interfaces.Variable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Component;

/**
 *
 * @author  James Harner
 * @version
 */
public class MyCellEditor extends DefaultCellEditor {

    protected JTextField jtf;

    /** Creates new MyCellEditor */
    public MyCellEditor(JTextField textField) {
        super(textField);

        jtf=textField;
    }

    public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable,java.lang.Object obj,boolean param,int param3,int param4) {
        Component comp=super.getTableCellEditorComponent(jTable, obj, param, param3, param4);
        
        if(obj==null)
            return comp;


        if(obj==Variable.NUM_MISSING_VAL){
            ((JTextField)comp).setText("");
        }

        if(obj.getClass()==Double.class)
            ((JTextField)comp).setHorizontalAlignment(SwingConstants.RIGHT);

        return comp;
    }
}
