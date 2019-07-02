/*
 * TypedHeaderRenderer.java
 *
 * Created on October 3, 2000, 10:24 AM
 */

package wvustat.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * This is a renderer for table column header that lets a user choose the data type of the column, e.g.,
 * categorical or numeric.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 3, 2000
 */
public class TypedHeaderRenderer implements TableCellRenderer{
    
    public void TypedHeaderRenderer(){
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column){
    
        JPanel jp=new JPanel();
        
        jp.setLayout(new GridLayout(2,1,2,2));
        
        JLabel nameLabel=new JLabel();
        JLabel typeLabel=new JLabel();
 
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setForeground(new Color(189,9,30));
        typeLabel.setFont(new Font("Serif",Font.PLAIN, 12));
        
        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                nameLabel.setBackground(header.getBackground());
                nameLabel.setForeground(header.getForeground());
                typeLabel.setBackground(header.getBackground());
                //jp.setFont(header.getFont());
            }
            TableModel tm=table.getModel();
            if(tm!=null){
                Class columnClass=tm.getColumnClass(column);
                String desc=columnClass.toString();
                desc=desc.substring(desc.lastIndexOf('.')+1);
                typeLabel.setText(desc);
                if(columnClass==Double.class)
                    typeLabel.setText("Numeric");
                else if(columnClass==String.class)
                    typeLabel.setText("Categorical");
                //typeLabel.setText((columnClass==Double.class)?"Numeric":"Categorical");
            }
            else
                typeLabel.setText("Categorical");
        }

        nameLabel.setText((value == null) ? "" : value.toString());
        
        jp.add(nameLabel);
        jp.add(typeLabel);
        
        jp.setBorder(UIManager.getBorder("TableHeader.cellBorder"));


        return jp;
    }
}
