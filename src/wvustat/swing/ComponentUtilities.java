/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 22, 2002
 * Time: 3:06:36 PM
 * To change this template use Options | File Templates.
 */
package wvustat.swing;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.StringTokenizer;

public class ComponentUtilities
{
    //public final static StatusBar statusBar = new StatusBar();
    public static final int STRING_DOCUMENT = 0;
    public static final int INTEGER_DOCUMENT = 1;
    public static final int DOUBLE_DOCUMENT = 2;


    public static final int MNEMONIC_ADD = 'A';
    public static final int MNEMONIC_APPLY = 'A';
    public static final int MNEMONIC_EDIT = 'E';
    public static final int MNEMONIC_NEW = 'N';
    public static final int MNEMONIC_REMOVE = 'R';
    public static final int MNEMONIC_OK = 'O';
    public static final int MNEMONIC_CANCEL = 'C';
    public static final int MNEMONIC_CLOSE = 'L';
    public static final int MNEMONIC_NEXT = 'N';
    public static final int MNEMONIC_BACK = 'B';
    public static final int MNEMONIC_FINISH = 'F';
    public static final int MNEMONIC_MOVEUP = 'U';
    public static final int MNEMONIC_MOVEDOWN = 'D';
    //public static final String    JLF_IMAGE_DIR       = "/toolbarButtonGraphics/general/";

    // Size of a standard Button Size
    public static final int BUTTON_WIDTH = 40;
    public static final int BUTTON_HEIGHT = 20;

    // Size of a standard toolbar button
    public static final int BUTTCON_WIDTH = 28;
    public static final int BUTTCON_HEIGHT = 28;

    // Size of a smaller Button Size
    public static final int SM_BUTTON_WIDTH = 72;
    public static final int SM_BUTTON_HEIGHT = 26;

    // Size of a label
    public static final int LABEL_WIDTH = 100;
    public static final int LABEL_HEIGHT = 20;

    // Size of a textfield
    public static final int TEXT_WIDTH = 150;
    public static final int TEXT_HEIGHT = 20;

    // Preferred size for buttons
    public static Dimension buttonPrefSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    public static Dimension buttconPrefSize = new Dimension(BUTTCON_WIDTH, BUTTCON_HEIGHT);
    public static Dimension smbuttonPrefSize = new Dimension(SM_BUTTON_WIDTH, SM_BUTTON_HEIGHT);

    // Preferred size for labels
    public static Dimension labelPrefSize = new Dimension(LABEL_WIDTH, LABEL_HEIGHT);

    // Preferred size for textfields
    public static Dimension textPrefSize = new Dimension(TEXT_WIDTH, TEXT_HEIGHT);

    ///////////////////////////////////////////////////////////////////////////
    //
    // Widget Factory methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Creates a label which will be displayed.
     *
     * @param text Text for the label
     * @param mnemonic Hot key
     * @param comp Component that this label represents.
     */
    public static JLabel createLabel(String text, int mnemonic, Component comp)
    {

        JLabel label = new JLabel(/*NOI18N*/"  " + text);
        //label.setFont(new java.awt.Font(label.getFont().getName(),java.awt.Font.BOLD,label.getFont().getSize()));

        label.setMinimumSize(labelPrefSize);
        if (mnemonic != -1)
        {
            label.setDisplayedMnemonic(mnemonic);
        }
        if (comp != null)
        {
            label.setLabelFor(comp);
        }
        if (text == null || text.length() == 0)
        {
            label.setPreferredSize(labelPrefSize);
        }
        return label;
    }

    public static JLabel createLabel(String text)
    {
        return createLabel(text, -1, null);
    }

    /**
     * Creates a text field
     *
     * @param text Text for the field
     * @param listener KeyListener
     * @param columns Indicates that this field represents numbers only
     */
    public static JTextField createTextField(String text, KeyListener listener, int columns, int maxLength, int documentType)
    {

        JTextField field;

        if (columns == 0)
        {
            columns = 20;
        }
        switch (documentType)
        {
            case STRING_DOCUMENT:
                field = new JTextField(columns);
                if (maxLength > 0)
                {
                    //field.setDocument(new MaxlengthDocument(maxLength));
                }
                break;
            case INTEGER_DOCUMENT:
                field = new JTextField(columns);
                break;
            case DOUBLE_DOCUMENT:
                field = new JTextField(columns);
                break;
            default :
                field = new JTextField(columns);
                break;
        }

        if (listener != null)
        {
            field.addKeyListener(listener);
        }

        if (text != null && text.length() > 0)
        {
            field.setText(text);
            field.setMinimumSize(field.getPreferredSize());
        }
        else
        {
            field.setPreferredSize(textPrefSize);
            field.setMinimumSize(textPrefSize);
        }

        field.setMinimumSize(field.getPreferredSize());

        return field;
    }

    public static JTextField createTextField(String text, int columns, int maxLength, int documentType)
    {
        return createTextField(text, null, columns, maxLength, documentType);
    }

    public static JTextField createTextField(String text, KeyListener listener)
    {
        return createTextField(text, listener, 20, 0, STRING_DOCUMENT);
    }

    public static JTextField createTextField(String text, int columns, int maxLength)
    {
        return createTextField(text, null, columns, maxLength, STRING_DOCUMENT);
    }

    public static JTextField createTextField(int columns, int maxLength)
    {
        return createTextField(null, null, columns, maxLength, STRING_DOCUMENT);
    }

    public static JTextField createTextField(String text, int columns)
    {
        return createTextField(text, null, columns, 0, STRING_DOCUMENT);
    }




    /**
     * Creates a Radio Button
     * @param text Text to display
     * @param mnemonic Hot key
     * @param listener ActionListener
     * @param selected Flag to indicate if button should be selected
     */
    public static JRadioButton createRadioButton(String text, int mnemonic, ActionListener listener, boolean selected, String actionCommand)
    {

        JRadioButton button = new JRadioButton(text);

        button.setMnemonic(mnemonic);
        button.setSelected(selected);
        button.setMinimumSize(labelPrefSize);
        button.setActionCommand(actionCommand);
        if (listener != null)
        {
            button.addActionListener(listener);
        }
        if (text.length() == 0)
        {
            button.setPreferredSize(labelPrefSize);
        }
        return button;
    }

    public static JRadioButton createRadioButton(String text, int mnemonic, boolean selected)
    {
        return createRadioButton(text, mnemonic, null, selected, "");
    }

    public static JRadioButton createRadioButton(String text, int mnemonic, ActionListener listener)
    {
        return createRadioButton(text, mnemonic, listener, false, "");
    }

    public static JRadioButton createRadioButton(String text, int mnemonic)
    {
        return createRadioButton(text, mnemonic, null, false, "");
    }

    public static JRadioButton createRadioButton(String text)
    {
        return createRadioButton(text, -1, null, false, "");
    }

    /**
     * Create a checkbox
     * @param text to display
     * @param mnemonic Hot key
     * @param listener ActionListener
     * @param selected Flag to indicate if button should be selected
     */
    public static JCheckBox createCheckBox(String text, int mnemonic, ChangeListener listener, boolean selected)
    {

        JCheckBox checkbox = new JCheckBox(text);

        checkbox.setMinimumSize(labelPrefSize);
        if (mnemonic != -1)
        {
            checkbox.setMnemonic(mnemonic);
        }
        checkbox.setSelected(selected);
        if (text.length() == 0)
        {
            checkbox.setPreferredSize(labelPrefSize);
        }
        if (listener != null)
        {
            checkbox.addChangeListener(listener);
        }
        return checkbox;
    }

    public static JCheckBox createCheckBox(String text, int mnemonic, ChangeListener listener)
    {
        return createCheckBox(text, mnemonic, listener, false);
    }

    public static JCheckBox createCheckBox(String text, int mnemonic, boolean selected)
    {
        return createCheckBox(text, mnemonic, null, selected);
    }

    public static JCheckBox createCheckBox(String text, int mnemonic)
    {
        return createCheckBox(text, mnemonic, null, false);
    }

    public static JCheckBox createCheckBox(String text)
    {
        return createCheckBox(text, -1, null, false);
    }

    /**
     * Creates a JComboBox
     * @param items Object array
     * @param listener The action listener which handles events
     * @param editable Flag that indicates if this combo box is editable
     */
    public static JComboBox createComboBox(Object[] items, ActionListener listener, boolean editable)
    {

        JComboBox comboBox = new JComboBox(items);

        if (listener != null)
        {
            comboBox.addActionListener(listener);
        }
        comboBox.setEditable(editable);
        return comboBox;
    }

    public static JComboBox createComboBox(Object[] items, boolean editable)
    {
        return createComboBox(items, null, editable);
    }

    /**
     * Creates a JComboBox
     * @param items Vector of items.
     * @param listener The action listener which handles events
     * @param editable Flag that indicates if this combo box is editable
     */
    public static JComboBox createComboBox(Vector items, ActionListener listener, boolean editable)
    {

        JComboBox comboBox = new JComboBox(items);

        if (listener != null)
        {
            comboBox.addActionListener(listener);
        }
        comboBox.setEditable(editable);
        return comboBox;
    }

    public static JComboBox createComboBox(Vector items, boolean editable)
    {
        return createComboBox(items, null, editable);
    }

    public static JButton createButton(String text, ActionListener listener, String actionCommand, ImageIcon icon)
    {
        return createButton(text, listener, actionCommand, -1, icon);
    }

    public static JButton createButton(String text, ActionListener listener, String actionCommand, int mnemonic)
    {
        return createButton(text, listener, actionCommand, mnemonic, null);
    }

    public static JButton createButton(String text, ActionListener listener, String actionCommand, int mnemonic, ImageIcon icon)
    {

        JButton button = new JButton(text);

        button.setMinimumSize(button.getPreferredSize());
        if (icon != null)
        {
            button.setIcon(icon);
        }
        if (listener != null)
        {
            button.addActionListener(listener);
        }
        if (mnemonic != -1)
        {
            button.setMnemonic(mnemonic);
        }
        if (actionCommand.length() > 0)
        {
            button.setActionCommand(actionCommand);
        }
        return button;
    }

    public static JButton createButton(String text, ActionListener listener, String actionCommand)
    {
        return createButton(text, listener, actionCommand, -1, null);
    }

    public static JButton createButton(String text, ActionListener listener)
    {
        return createButton(text, listener, "", -1, null);
    }

    /**
     * Creates a Smaller JButton
     * @param text to display
     * @param listener The action listener which handles events
     * @param mnemonic Letter combination
     */
    public static JButton createSmallButton(String text, ActionListener listener, int mnemonic)
    {

        JButton button = ComponentUtilities.createButton(text, listener, "", mnemonic, null);

        button.setMinimumSize(smbuttonPrefSize);
        if (text.length() == 0)
        {
            button.setPreferredSize(smbuttonPrefSize);
        }
        else
        {
            Dimension size = button.getPreferredSize();

            if (size.width < smbuttonPrefSize.width)
            {
                button.setPreferredSize(smbuttonPrefSize);
            }
        }
        return button;
    }

    public static JButton createSmallButton(String text, ActionListener listener)
    {
        return createSmallButton(text, listener, -1);
    }

    public static void addLabelComponentRows(Component[] labels, Component[] components, GridBagLayout gridbag, Container container)
    {

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(1, 1, 1, 5);
        int numLabels = labels.length;

        c.weighty = 0.0;
        c.weightx = 0.0;
        for (int i = 0; i < numLabels; i++)
        {

            //c.gridy = i;
            c.gridx = 0;
            c.anchor = GridBagConstraints.NORTHEAST;
            c.gridwidth = GridBagConstraints.RELATIVE;
            c.fill = GridBagConstraints.NONE;
            gridbag.setConstraints(labels[i], c);
            container.add(labels[i]);
            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridx = 1;
            c.gridwidth = GridBagConstraints.REMAINDER;    //end row

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            gridbag.setConstraints(components[i], c);
            container.add(components[i]);
            c.weightx = 0.0;
        }
    }

    public static void addLabelComponent(Component label, Component component, GridBagLayout gridbag, Container container)
    {

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(1, 1, 1, 5);
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.anchor = GridBagConstraints.NORTHEAST;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(label, c);
        container.add(label);
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridwidth = GridBagConstraints.REMAINDER;    //end row
        //c.fill = GridBagConstraints.HORIZONTAL;
        //c.weightx = 0.5;
        gridbag.setConstraints(component, c);
        container.add(component);
        c.weightx = 0.0;
    }

    public static void addLabelComponent(Component label1, Component component, Component label2, GridBagLayout gridbag, Container container)
    {

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(1, 1, 1, 5);
        c.weighty = 0.0;
        c.weightx = 0.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        gridbag.setConstraints(label1, c);
        container.add(label1);
        c.insets = new Insets(0, 0, 0, 0);
        //c.anchor = GridBagConstraints.NORTHWEST;
        //c.fill = GridBagConstraints.HORIZONTAL;
        //c.weightx = 0.5;
        gridbag.setConstraints(component, c);
        container.add(component);
        c.gridwidth = GridBagConstraints.REMAINDER;    //end row
        gridbag.setConstraints(label2, c);
        container.add(label2);
        c.weightx = 0.0;
    }


    public static Box createLabelFieldBox(Component[] labels,Component[] fields)
    {
        Box contentBox = Box.createHorizontalBox();
        Box labelBox = Box.createVerticalBox();
        Box fieldBox = Box.createVerticalBox();
        Dimension separator = new Dimension(10,4);
        for (int i=0;i<labels.length;i++)
        {
            if (i>0)
                labelBox.add(Box.createRigidArea(separator));
            labelBox.add(labels[i]);
        }
        labelBox.add(Box.createVerticalGlue());
        for (int i=0;i<fields.length;i++)
        {
            if (i>0)
                fieldBox.add(Box.createRigidArea(separator));
            fieldBox.add(fields[i]);
        }
        fieldBox.add(Box.createVerticalGlue());
        contentBox.add(labelBox);
        contentBox.add(fieldBox);
        contentBox.add(Box.createHorizontalGlue());
        return contentBox;
    }


    public static Border createBorder(String text)
    {

        Border border = BorderFactory.createEtchedBorder();

        return BorderFactory.createTitledBorder(border, text, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
    }

    /**
     * Creates an blank border with the displayed text
     */
    public static Border createBorder()
    {
        return BorderFactory.createEmptyBorder(4, 4, 4, 4);
    }

    /**
     * Parses the string into an array of strings
     * <p>
     * @param string - String to parse
     * @param delim - Delimiter to use. If this is null or empty then
     *        then the set [ ' ', '\t', '\n', '\r' ] is used.
     */
    public static String[] stringArrayFromString(String string, String delim)
    {

        StringTokenizer st;
        String[] strings;

        if ((delim == null) || delim.equals(/*NOI18N*/""))
        {
            st = new StringTokenizer(string);
        }
        else
        {
            st = new StringTokenizer(string, delim);
        }
        int numTokens = st.countTokens();

        strings = new String[numTokens];
        int index = 0;

        while (st.hasMoreTokens())
        {
            strings[index++] = st.nextToken();
        }
        return strings;
    }

    /**
     * Centers a component (source) in it's parent component. If parent is null
     * then the window is centered in screen.
     *
     * The source and parent components should be correctly sized
     */
    public static void centerComponent(Component source, Component parent)
    {

        Rectangle rect;
        Dimension dim = source.getSize();

        if (parent != null)
        {
            rect = parent.getBounds();
        }
        else
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

            rect = new Rectangle(0, 0, d.width, d.height);
        }
        int x = rect.x + (rect.width - dim.width) / 2;
        int y = rect.y + (rect.height - dim.height) / 2;

        source.setLocation(x, y);
    }

    public static Window getParentWindow(Component source)
    {

        if (source == null)
        {
            return null;
        }
        Container parent = source.getParent();

        while (parent != null)
        {
            if (parent instanceof Window)
            {
                break;
            }
            else
            {
                parent = parent.getParent();
            }
        }
        if (parent == null)
        {
            return null;
        }
        else
        {
            return (Window) parent;
        }
    }

    // Cursor operations
    public static void setWaitCursor(Component comp)
    {
        if (comp == null)
            return;
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public static void setDefaultCursor(Component comp)
    {
        if (comp == null)
            return;
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static JMenuItem createMenuItem(JMenu menu, String name, String actionCommand, ActionListener l)
    {
        JMenuItem item=menu.add(name);
        item.setActionCommand(actionCommand);
        item.addActionListener(l);
        return item;
    }
}
