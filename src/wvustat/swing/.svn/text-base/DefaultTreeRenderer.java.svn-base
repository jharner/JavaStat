package wvustat.swing;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Apr 23, 2004
 * Time: 4:08:07 PM
 * To change this template use Options | File Templates.
 */
public class DefaultTreeRenderer extends DefaultTreeCellRenderer
{
    public Component getTreeCellRendererComponent(JTree jTree, Object o, boolean b, boolean b1, boolean b2, int i, boolean b3)
    {
        super.getTreeCellRendererComponent(jTree, o, b, b1, b2, i, b3);
        if(o instanceof IconTreeNode)
        {
            IconTreeNode node=(IconTreeNode)o;
            if(node.getIcon()!=null)
                setIcon(node.getIcon());
        }

        return this;
    }
}
