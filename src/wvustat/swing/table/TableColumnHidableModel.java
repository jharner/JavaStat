package wvustat.swing.table;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.event.TableColumnModelEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Dec 13, 2003
 * Time: 10:12:51 AM
 * To change this template use Options | File Templates.
 */
public class TableColumnHidableModel extends DefaultTableColumnModel
{
    protected List hidenColumns;
    private int insertionPoint=0;

    public TableColumnHidableModel()
    {
        super();
        hidenColumns=new ArrayList();
    }

    public void hideColumns(List columns)
    {
        if(columns.size()==0)
            return;
        TableColumn tc1=(TableColumn)columns.get(0);
        TableColumn tc2=(TableColumn)columns.get(columns.size()-1);
        int from=getColumnIndex(tc1.getIdentifier());
        insertionPoint=from;
        int to=getColumnIndex(tc2.getIdentifier());
        this.hidenColumns.addAll(columns);
        this.tableColumns.removeAll(columns);
        this.fireColumnRemoved(new TableColumnModelEvent(this, from,to));
    }

    public void unhideColumns()
    {
        int n=hidenColumns.size();
        if(n==0)
            return;
        for(int i=0;i<n;i++)
        {
            TableColumn tc=(TableColumn)hidenColumns.get(i);
            tableColumns.insertElementAt(tc, insertionPoint+i);
        }
        hidenColumns.clear();

        this.fireColumnAdded(new TableColumnModelEvent(this, insertionPoint, insertionPoint+n));
    }

    public TableColumn getColumn(int index)
    {
        return super.getColumn(index);
    }

}
