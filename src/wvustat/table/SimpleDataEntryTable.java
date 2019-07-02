package wvustat.table;

import javax.swing.*;
import java.awt.*;

/**
 * A table in JFrame used by PlotsApplet. 
 * It has a simplified table feature similar to DataEntryTable.
 * <p>
 * Created on July 21, 2005<p>
 * Revised on June 4, 2008
 * 
 * @see     DataEntryTable
 * @author  djluo
 * 
 */
public class SimpleDataEntryTable extends JFrame
{
	private static final long serialVersionUID = 1L;
	private DataEntryTable dt;

	
    public SimpleDataEntryTable(DataSetTM tableModel)
    {
        super(tableModel.getName());
        dt = new DataEntryTable(tableModel);
        init();
    }

    protected void init()
    {
        JScrollPane scrollPane = (JScrollPane) dt.getContentPane();

        this.setContentPane(scrollPane);
        
        Dimension d1 = dt.getTable().getPreferredSize();
        Dimension d2 = scrollPane.getRowHeader().getPreferredSize();
        
        int width = d1.width + d2.width + 35;
        int height = d1.height + 105;

        setSize(new Dimension(width, height));

        this.setResizable(true);
    }
}
