package wvustat.swing;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Apr 23, 2004
 * Time: 4:05:28 PM
 * To change this template use Options | File Templates.
 */
public class IconTreeNode extends DefaultMutableTreeNode
{
    public static final int CONTENT=0;
    public static final int FOLDER=1;

    private int type;
    private Icon icon;

    public IconTreeNode(Object userObj, int type, Icon icon, boolean isAllowChildren)
    {
        super(userObj,isAllowChildren);
        this.type = type;
        this.icon=icon;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public Icon getIcon()
    {
        return icon;
    }

    public void setIcon(Icon icon)
    {
        this.icon = icon;
    }
}
