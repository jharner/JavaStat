/*
 * Table.java
 *
 * Created on March 7, 2002, 10:32 AM
 */

package wvustat.awtTable;

import java.awt.*;
import java.util.Vector;

import wvustat.math.UI.InputDialog;
import wvustat.plotUtil.ComponentUtil;
/**
 *
 * @author Hengyi Xue
 * @version
 */
public class Table extends Panel implements java.awt.event.MouseListener, java.awt.event.ActionListener, TableModelListener{
    private TableModel model;
    private int headerHeight=32;
    private int rowHeight=28;
    private int colWidth=60;
    private Insets insets=new Insets(2,2,8,6);
    private int margin=1;
    private TextField cellEditor;
    private TableCell activeCell;
    private int editRow=-9, editCol=-9;
    private TableCell[][] cells;
    private Menu menu;
    private ScrollPane scrollPane;

    /** Creates new Table */
    public Table(TableModel model) {
        this.model=model;
        model.addTableModelListener(this);
        initComponent();
        scrollPane=new ScrollPane();
        scrollPane.add(this);

    }

    public Table(int row, int col, Vector colNames){
        model=new TableModel(row, col, colNames);
        model.addTableModelListener(this);
        initComponent();       
        scrollPane=new ScrollPane();
        scrollPane.add(this);

    }
    
    public ScrollPane getScrollableTable(){
        int w=colWidth*(model.getColumnCount()+1)+insets.left+insets.right;
        int h=insets.top+insets.bottom+headerHeight+rowHeight*model.getRowCount();   
        scrollPane.setSize(w+10,h+10);
        return scrollPane;
    }

    public TableModel getModel(){
        return model;
    }

    public Dimension getPreferredSize(){
        int w=colWidth*(model.getColumnCount()+1)+insets.left+insets.right;
        int h=insets.top+insets.bottom+headerHeight+rowHeight*model.getRowCount();

        return new Dimension(w,h);
    }
    
    public Menu getMenu(){
        return menu;
    }
    
    private void buildMenu(){
        menu=new Menu("Edit");
        MenuHandler menuHandler=new MenuHandler();
        
        MenuItem item=new MenuItem("Add Rows...");
        item.addActionListener(menuHandler);
        menu.add(item);
        item=new MenuItem("Add Column...");
        item.addActionListener(menuHandler);
        menu.add(item);
        
        item=new MenuItem("Plot Columns...");
        item.addActionListener(menuHandler);
        menu.add(item);
    }

    private void initComponent(){
        setBackground(Color.gray);
        setLayout(null);

        cells=new TableCell[model.getRowCount()][model.getColumnCount()];
        //Add column headers

        int x=insets.left+colWidth+margin;
        int y=insets.top;
        for(int i=0;i<model.getColumnCount();i++){
            TableCell header=new TableCell(model.getColumnName(i),-1,i);
            header.setFont(new Font("Dialog", Font.BOLD, 12));
            header.addMouseListener(this);
            this.add(header);
            header.setBounds(x,y,colWidth, headerHeight);
            x+=margin+colWidth;
        }

        //Add row headers
        x=insets.left;
        y=insets.top;
        TableCell blank=new TableCell("", -1,-1);
        this.add(blank);
        blank.setBounds(x,y,colWidth, headerHeight);
        y+=headerHeight+margin;

        for(int i=0;i<model.getRowCount();i++){
            TableCell header=new TableCell(String.valueOf(i+1), i, -1);
            header.setFont(new Font("Dialog", Font.PLAIN, 12));
            this.add(header);
            header.setBounds(x,y,colWidth,rowHeight);
            y+=rowHeight+margin;
        }


        y=insets.top+headerHeight+margin;
        x=insets.left+colWidth+margin;

        for(int i=0;i<model.getRowCount();i++){
            for(int j=0;j<model.getColumnCount();j++){
                TableCell cell=new TableCell(model.getValueAt(i,j),i,j);

                cell.addMouseListener(this);
                this.add(cell);
                cell.setBounds(x,y,colWidth, rowHeight);
                x+=margin+colWidth;
                cells[i][j]=cell;
            }
            y+=rowHeight+margin;
            x=insets.left+colWidth+margin;
        }

        cellEditor=new TextField(6);
        cellEditor.setBackground(Color.white);
        cellEditor.setFont(new Font("Arial", Font.PLAIN, 14));
        this.add(cellEditor);
        cellEditor.setBounds(-100,-100,40,40); //make it invisible
        cellEditor.addActionListener(this);
        cellEditor.addKeyListener(new KeyHandler());
    
        buildMenu();
    }

    private void moveRight(){
        this.editCol++;
        if(editCol==model.getColumnCount()){
            editCol=0;
            editRow++;
        }

        if(editRow<model.getRowCount()){
            editCell(cells[editRow][editCol]);
        }
    }

    private void editCell(TableCell cell){
        activeCell=cell;
        cellEditor.setForeground(Color.black);
        cellEditor.setText(activeCell.getText());
        cellEditor.setBounds(activeCell.getBounds());
        activeCell.setBounds(-100,-100,40,40);
        cellEditor.requestFocus();
        validate();
    }

    private void moveDown(){
        this.editRow++;
        if(editRow==model.getRowCount()){
            editRow=0;
            editCol++;
        }

        if(editCol<model.getColumnCount()){
            editCell(cells[editRow][editCol]);
        }
    }

    public static void main(String[] args){
        Vector v=new Vector();
        v.addElement("A");
        v.addElement("B");
        v.addElement("C");
        v.addElement("D");
        v.addElement("E");

        Table table=new Table(5,5, v);
        MenuBar mb=new MenuBar();
        mb.add(table.getMenu());

        Frame fr=new Frame();
        fr.setMenuBar(mb);
        fr.setLayout(new BorderLayout());
        
        fr.add(table.getScrollableTable(), BorderLayout.CENTER);
        fr.pack();
        fr.show();
    }

    public void mouseReleased(final java.awt.event.MouseEvent p1) {
    }

    public void mouseEntered(final java.awt.event.MouseEvent p1) {
    }

    public void mouseClicked(final java.awt.event.MouseEvent p1) {
        Object src=p1.getSource();

        if(src instanceof TableCell){
            TableCell cell=(TableCell)src;
            editRow=cell.getRow();
            editCol=cell.getColumn();

            if(activeCell!=null){
                activeCell.setBounds(cellEditor.getBounds());
            }

            editCell(cell);
        }
    }

    public void mousePressed(final java.awt.event.MouseEvent p1) {
    }
    public void mouseExited(final java.awt.event.MouseEvent p1) {
    }
    public void actionPerformed(final java.awt.event.ActionEvent p1) {
        if(editRow>=0){
            try{
                double val=new Double(cellEditor.getText()).doubleValue();
                activeCell.setValue(val);
                model.setValueAt(val, editRow, editCol);
                Rectangle rect=cellEditor.getBounds();
                cellEditor.setBounds(-100,-100,40,40);
                activeCell.setBounds(rect);
                moveDown();
            }
            catch(NumberFormatException e){
                cellEditor.setForeground(Color.red);
            }
        }
        else{
            activeCell.setText(cellEditor.getText());
            model.setColumnName(cellEditor.getText(),editCol);
            activeCell.setBounds(cellEditor.getBounds());
            cellEditor.setBounds(-100,-100,40,40);
            activeCell=null;
        }
            
    }

    public void tableModelChanged(TableModelEvent evt) {
        int id=evt.getEventId();

        if(id==TableModelEvent.STRUCTURAL_CHANGE){
            this.removeAll();
            initComponent();
            validate();
            scrollPane.doLayout();
        }
        else{
            int row=evt.getRow();
            int col=evt.getColumn();

            cells[row][col].setValue(model.getValueAt(row, col));
        }

    }
    class KeyHandler extends java.awt.event.KeyAdapter{
        public void keyPressed(final java.awt.event.KeyEvent p1) {
            int code=p1.getKeyCode();

            if(code==java.awt.event.KeyEvent.VK_TAB){
                activeCell.setBounds(cellEditor.getBounds());
                moveRight();
            }
        }
    }
    
    class MenuHandler implements java.awt.event.ActionListener{
        public void actionPerformed(java.awt.event.ActionEvent evt){
            String cmd=evt.getActionCommand();
            
            if(cmd.equals("Add Rows...")){
                String input=InputDialog.showInputDialog(ComponentUtil.getTopComponent(Table.this),"How many rows do you want to add?", "5");
                if(input==null)
                    return;
                
                try{
                    int num=Integer.parseInt(input);
                    model.addRows(num);
                }
                catch(NumberFormatException nfe){
                }
            }
            else if(cmd.equals("Add Column...")){
                ColumnDefDialog cd=new ColumnDefDialog(ComponentUtil.getTopComponent(Table.this),model); 
                
                cd.show();
            }
            else if(cmd.equals("Plot Columns...")){
                RoleDefDialog rd=new RoleDefDialog(ComponentUtil.getTopComponent(Table.this),model); 
                
                rd.show();                
            }
        }
    }
}