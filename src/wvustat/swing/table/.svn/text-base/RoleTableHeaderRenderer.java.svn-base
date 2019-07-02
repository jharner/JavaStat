/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 22, 2002
 * Time: 10:10:04 AM
 * To change this template use Options | File Templates.
 */
package wvustat.swing.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class RoleTableHeaderRenderer extends DefaultTableCellRenderer
{
    protected static int diameter=14;
    protected static Font font=new Font("Helvetica",Font.PLAIN,8);

    protected static BufferedImage xImage, yImage, zImage, uImage;

    static
    {
        xImage=createImage('X');
        yImage=createImage('Y');
        zImage=createImage('Z');
        uImage=createImage('U');
    }

    protected static BufferedImage createImage(char roleChar)
    {
        BufferedImage img = new BufferedImage(diameter,diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, diameter, diameter);
        g.fill(rect);
        g.setComposite(AlphaComposite.SrcOver);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(Color.black);
        //Ellipse2D circle = new Ellipse2D.Double(1, 1, diameter - 2, diameter - 2);
        //g.draw(circle);

        g.setStroke(new BasicStroke(2.0f));
        //g.drawString(String.valueOf(roleChar), 6, diameter-2);
        double ratio=0.7;
        int originX=(int)Math.round(diameter*(1-ratio));
        int originY=(int)Math.round(diameter*ratio);

        g.drawLine(originX,originY,diameter, originY);
        g.drawLine(originX, originY, originX,0);
        //g.drawLine(originX, originY, 0, diameter);

        g.setPaint(Color.red);
        if(roleChar=='X')
            g.drawLine(originX,originY,diameter, originY);
        else if(roleChar=='Y')
            g.drawLine(originX, originY, originX,0);
        else if(roleChar=='Z')
            g.drawLine(originX, originY, 0, diameter);

        return img;
    }

    public RoleTableHeaderRenderer()
    {
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column)
    {
        DefaultTableCellRenderer label = this;

        if (table != null)
        {
            JTableHeader header = table.getTableHeader();
            if (header != null)
            {
                if(table.isColumnSelected(column))
                {
                    label.setForeground(Color.gray);
                    label.setBackground(java.awt.SystemColor.textHighlight);
                }
                else
                {
                    label.setForeground(header.getForeground());
                    label.setBackground(header.getBackground());
                }
                label.setFont(header.getFont());
            }
        }
        label.setText((value == null) ? "" : " " + value.toString());
        TableModel tableModel=table.getModel();
        if(tableModel instanceof DataSet)
        {
            DataSet dataSet=(DataSet)tableModel;
            int role=dataSet.getColumnRole(column);
            if(role==DataSet.X_ROLE)
                label.setIcon(new ImageIcon(xImage));
            else if(role==DataSet.Y_ROLE)
                label.setIcon(new ImageIcon(yImage));
            else if(role==DataSet.Z_ROLE)
                label.setIcon(new ImageIcon(zImage));
            else
                label.setIcon(new ImageIcon(uImage));
        }

        return label;
    }
}
