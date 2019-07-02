/*
 * LinkLabel.java
 *
 * Created on March 18, 2002, 1:50 PM
 */

package wvustat.simulation.fivestep;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author  hxue
 * @version
 */
public class LinkLabel extends JComponent
{
    private String text;
    private boolean enabled = false, active = false;
    private Color disabledColor, enabledColor, visitedColor;
    private Insets margin = new Insets(4, 4, 6, 4);
    private int width, height;
    //private Font font = new Font("Serif", Font.BOLD, 14);
    private Vector actionListeners = new Vector();
    private String actionCmd;
    private List changeListeners=new ArrayList();

    /** Creates new LinkLabel */
    public LinkLabel(String text)
    {
        this(text, text, true);
    }

    public LinkLabel(String text, boolean enabled)
    {
        this(text, text, enabled);
    }

    public LinkLabel(String text, String actionCmd, boolean enabled)
    {
        this.text = text;
        this.actionCmd = actionCmd;
        this.enabled = enabled;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);

        disabledColor = Color.gray;
        enabledColor = Color.black;
        visitedColor = Color.blue;

        setBorder(new javax.swing.border.EtchedBorder());
        if (enabled)
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void addNotify()
    {
        super.addNotify();
        measure();
    }

    public void addActionListener(ActionListener listener)
    {
        actionListeners.addElement(listener);
    }

    public void removeActionListener(ActionListener listener)
    {
        actionListeners.removeElement(listener);
    }

    protected void fireActionEvent(ActionEvent evt)
    {
        for (int i = 0; i < actionListeners.size(); i++)
        {
            ActionListener al = (ActionListener) actionListeners.elementAt(i);
            al.actionPerformed(evt);
        }
    }

    public void setEnabled(boolean value)
    {
        enabled = value;
        if (enabled)
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        repaint();
    }

    private void measure()
    {
        FontMetrics metrics;

        metrics = this.getFontMetrics(getFont());
        if (metrics == null)
            return;


        width = margin.left + margin.right + metrics.stringWidth(text);
        height = metrics.getHeight() + margin.top + margin.bottom;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(width, height);
    }


    public void paintComponent(Graphics g)
    {
        FontMetrics metrics = getFontMetrics(getFont());

        int x = margin.left;
        int y = height-margin.bottom;

        if (!enabled)
            g.setColor(disabledColor);
        else if (active)
            g.setColor(visitedColor);
        else
            g.setColor(enabledColor);

        g.setFont(getFont());
        g.drawString(text, x, y);
        y += 2;
        g.drawLine(x, y, x + metrics.stringWidth(text), y);
    }

    public void setActive(boolean active)
    {
        this.active=active;
        repaint();
        firePropertyChangeEvent(new PropertyChangeEvent(this, "active", null,null));
    }

    public boolean isActive()
    {
        return active;
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        changeListeners.add(l);
    }

    protected void firePropertyChangeEvent(PropertyChangeEvent evt)
    {
        for(int i=0;i<changeListeners.size();i++)
        {
            PropertyChangeListener l=(PropertyChangeListener)changeListeners.get(i);
            l.propertyChange(evt);
        }
    }

    protected void processMouseEvent(java.awt.event.MouseEvent evt)
    {
        if (!enabled)
            return;
        int id = evt.getID();

        if (id == MouseEvent.MOUSE_CLICKED)
        {
            setActive(true);
            repaint();
            ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCmd);
            fireActionEvent(ae);
        }

        super.processMouseEvent(evt);
    }
}