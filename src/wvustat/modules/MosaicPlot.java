package wvustat.modules;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Vector;


public class MosaicPlot extends JPanel
{
    public static final int SHOW_COUNT = 0;
    public static final int SHOW_TOTAL_P = 1;
    public static final int SHOW_ROW_P = 2;
    public static final int SHOW_COL_P = 3;
    public static final int SHOW_CELL_CHI2 = 4;

    public Color[] myColors;

    private DataSet data;

    private Variable x_var, y_var, z_var;
    private Vector vz;

    private int width = 250, height = 200;

    private double[][] freqs = new double[0][0], expected;
    private double[][] hlfreqs;

    private int groupIndex = 0;

    private Insets insets = new Insets(20, 40, 40, 60);

    private Rectangle2D[][] blocks = new Rectangle2D[0][0];
    private Rectangle2D[][] hlblocks;
    private Color[] colors;
    private Rectangle2D[] colorBlocks = new Rectangle2D[0];

    private CoordConverter coord;

    private int xUnitWidth, yUnitWidth;

    private String[] xlevels, ylevels;
    private double[] xValues, yValues;

    private double[] row_sum = new double[0], col_sum = new double[0], chi_row_sum, chi_col_sum;

    private double total, ChiSquare = 0;

    //white space between plot and color strip
    private int whiteSpace = 65, stripWidth = 35;

    private CTableModel tableModel;

    //private GroupMaker grouper;
    private EqualCountGrouper grouper;
    
    private GUIModule module;
    
    //private GeneralPath popupArrow;

    public MosaicPlot(DataSet data, Variable x_var, Variable y_var, Vector vz)
    {
        this.data = data;
        this.x_var = x_var;
        this.y_var = y_var;
        
        this.vz = vz;
        if(vz.size() > 0)
            this.z_var = (Variable)vz.elementAt(0);

        if (z_var != null) {
            grouper = new EqualCountGrouper(vz, data);
        }


        myColors = new Color[10];
        //myColors[0] = new Color(91, 91, 255);
        //myColors[1] = new Color(255, 204, 51);
        //myColors[2] = new Color(255, 0, 204);
        myColors[0] = new Color(51, 51, 255);
        myColors[1] = new Color(255, 0, 204);
        myColors[2] = new Color(255, 204, 51);
        myColors[3] = new Color(255, 255, 51);
        myColors[4] = new Color(51, 255, 51);
        myColors[5] = new Color(255, 102, 102);
        myColors[6] = new Color(51, 153, 255);
        myColors[7] = new Color(255, 204, 51);
        myColors[8] = new Color(153, 153, 255);
        myColors[9] = new Color(255, 0, 102);

        retrieveData();

        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        insets.right = whiteSpace + stripWidth + 10;

        AllListener listener = new AllListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        tableModel = new CTableModel();
        
        /*popupArrow = new GeneralPath();
        popupArrow.moveTo(5,5);
        popupArrow.lineTo(17,5);
        popupArrow.lineTo(11,12);
        popupArrow.closePath();*/

    }

    public void setDisplayOption(int option)
    {
        tableModel.setCellOption(option);
    }


    private void retrieveData()
    {
        
        xlevels = x_var.getDistinctCatValues();
        ylevels = y_var.getDistinctCatValues();
        
        if (xlevels.length == 0 || ylevels.length == 0) return;

        xValues=x_var.getNumValues();
        yValues=y_var.getNumValues();
        
        int[] states = data.getStateVariable().getDummyValues();
        double[] stateConstrt = new double[states.length];
        for (int i = 0; i < states.length; i++) {
        	stateConstrt[i] = states[i];
        }

        if (z_var != null)
        {

            double[] zcol = grouper.getConstraintVector(groupIndex);

            freqs = data.getFreqMatrix(x_var.getName(), y_var.getName(), zcol, null);
            hlfreqs = data.getFreqMatrix(x_var.getName(), y_var.getName(), zcol, stateConstrt);
        }
        else {
            freqs = data.getFreqMatrix(x_var.getName(), y_var.getName(), null, null);
            hlfreqs = data.getFreqMatrix(x_var.getName(), y_var.getName(), null, stateConstrt);
        }
   
        colors = new Color[freqs[0].length];
        colorBlocks = new Rectangle2D[freqs[0].length];

        for (int i = 0; i < colors.length; i++)
        {
            if (i < myColors.length)
                colors[i] = myColors[i];
            else
                colors[i] = myColors[i % myColors.length];
        }

    }

    private void initPlot()
    {
    	if (xlevels.length == 0 || ylevels.length == 0) return;
    	
        blocks = new Rectangle2D[freqs.length][freqs[0].length];
        hlblocks = new Rectangle2D[freqs.length][freqs[0].length];

        coord = new CoordConverter(width, height, 0, 1, 0, 1, insets);

        xUnitWidth = width - insets.left - insets.right;

        yUnitWidth = height - insets.top - insets.bottom;


        double h = 0;
        row_sum = new double[freqs.length];
        col_sum = new double[freqs[0].length];
        total = 0;

        for (int i = 0; i < freqs.length; i++)
        {
            row_sum[i] = 0;

            for (int j = 0; j < freqs[i].length; j++)
            {
                row_sum[i] += freqs[i][j];
                total += freqs[i][j];
            }
        }

        for (int i = 0; i < freqs[0].length; i++)
        {
            col_sum[i] = 0;
            for (int j = 0; j < freqs.length; j++)
            {
                col_sum[i] += freqs[j][i];
            }
        }

        for (int i = 0; i < freqs.length; i++)
        {
        	for (int j = 0; j < freqs[i].length; j++) {
        		blocks[i][j] = new Rectangle2D.Float(0,0,0,0);
        		hlblocks[i][j] = new Rectangle2D.Float(0,0,0,0);
        	}
        }
        
        
        float tmpX = coord.x(0);
        for (int i = 0; i < freqs.length; i++)
        {
        	if (row_sum[i] == 0) continue;
        	
            h = (height - insets.top - insets.bottom) * 1.0 / row_sum[i];

            double p_sum = 0;

            for (int j = 0; j < freqs[i].length; j++)
            {
                p_sum += freqs[i][j];

                blocks[i][j] = new Rectangle2D.Float(tmpX, coord.y(p_sum / row_sum[i]), (float) (row_sum[i] / total * xUnitWidth) - 1, (float) (yUnitWidth * freqs[i][j] / row_sum[i]) - 1);

                if (hlfreqs[i][j] > 0)
                	hlblocks[i][j] = new Rectangle2D.Float((int)blocks[i][j].getX(), (int)blocks[i][j].getY(), (int)(row_sum[i] / total * xUnitWidth * hlfreqs[i][j] / freqs[i][j]) - 1, (int)blocks[i][j].getHeight());
                
                //else
                //blocks[i][j]=new Rectangle2D.Float(coord.x(row_sum[i-1]),coord.y(p_sum/row_sum[i]), (float)(row_sum[i]/total*xUnitWidth), (float)(yUnitWidth*freqs[i][j]/row_sum[i]));
            }
            tmpX += (float) (row_sum[i] / total * xUnitWidth);
        }


        double p_sum = 0;

        h = (height - insets.top - insets.bottom) * 1.0 / total;

        for (int i = 0; i < freqs[0].length; i++)
        {
            p_sum += col_sum[i];

            colorBlocks[i] = new Rectangle2D.Float(width - insets.right + whiteSpace, coord.y(p_sum / total), stripWidth, (float) (yUnitWidth * col_sum[i] / total));
            //else
            //colorBlocks[i]=new Rectangle2D.Float(width-insets.right+whiteSpace, coord.y(p_sum/total), stripWidth,(int)(col_sum[i]*h));
        }

        //Calculate Chi Square
        ChiSquare = 0.0;
        expected = new double[freqs.length][freqs[0].length];
        chi_row_sum = new double[row_sum.length];
        chi_col_sum = new double[col_sum.length];

        for (int i = 0; i < freqs.length; i++)
        {
            chi_row_sum[i] = 0;
            for (int j = 0; j < freqs[i].length; j++)
            {
                expected[i][j] = row_sum[i] * col_sum[j] / total;

                double cellChi2 = Math.pow(freqs[i][j] - expected[i][j], 2) / expected[i][j];
                chi_row_sum[i] += cellChi2;
                ChiSquare += cellChi2;
                chi_col_sum[j] += cellChi2;
            }
        }


    }

    public EqualCountGrouper getGroupMaker()
    {
        return grouper;
    }

    /**
     * This method is overriden so that the plot can be resized dynamically
     */
    public void setBounds(int x, int y, int w, int h)
    {
        width = w;
        height = h;
        initPlot();
        super.setBounds(x, y, w, h);
    }
    
    public void setGUIModule(GUIModule module) {
        this.module = module;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        FontMetrics metrics = g2.getFontMetrics();

        g2.drawRect(0, 0, width - 1, height - 1);
        
        if (xlevels.length == 0 || ylevels.length == 0 || total == 0) {
        	String errString = "Error getting data, please try again later.";
            g2.drawString(errString, (int) (getSize().width / 2.0 - metrics.stringWidth(errString) / 2.0),
                    (int) (getSize().height / 2.0 - metrics.getHeight() / 2.0));
            return;
        }
        
        
        
        //g2.fill(popupArrow);

        //draw x ticmarks
        double p_sum = 0;
        for (int i = 0; i < xlevels.length; i++)
        {
        	if (row_sum[i] == 0) continue;
            p_sum += row_sum[i];
            g2.drawLine(coord.x(p_sum / total), coord.y(0), coord.x(p_sum / total), coord.y(0) + 4);
            int len = metrics.stringWidth(xlevels[i]);
            g2.drawString(xlevels[i], coord.x(p_sum / total) - (int) blocks[i][0].getWidth() / 2 - len / 2, coord.y(0) + metrics.getHeight());
        }

        if (total > 0) {
        	NumberFormat nf = NumberFormat.getInstance();
        	nf.setMaximumFractionDigits(1);
        	nf.setMinimumFractionDigits(1);

        	//Draw x, y label
        	String xName = x_var.getName();
        	String yName = y_var.getName();
        	g2.drawString(xName, (width - insets.right) / 2 - metrics.stringWidth(xName) / 2, height - 8);
        	g2.drawString(yName, width - metrics.stringWidth(yName) - 4, metrics.getHeight());
        

        	//draw y ticmarks
        	for (int i = 0; i <= 5; i++)
        	{
        		g2.drawLine(coord.x(0), coord.y(i * 0.2), coord.x(0) - 4, coord.y(i * 0.2));

        		String label = nf.format(i * 0.2);
        		int len = metrics.stringWidth(label);

        		g2.drawString(label, coord.x(0) - 4 - len, coord.y(i * 0.2) + metrics.getMaxAscent() / 2);
        	}
        }

        //draw y levels
        p_sum = 0;
        for (int i = 0; i < ylevels.length; i++)
        {
        	if (col_sum[i] == 0) continue;
            p_sum += col_sum[i];
            g2.drawLine(coord.x(1), coord.y(p_sum / total), coord.x(1) + 4, coord.y(p_sum / total));
            String tmp = ylevels[i];
            if (tmp.length() > 6)
                tmp = tmp.substring(0, 3) + "...";
            g2.drawString(tmp, coord.x(1) + 6, coord.y(p_sum / total));
        }

        for (int i = 0; i < blocks.length; i++)
        {
            for (int j = 0; j < blocks[i].length; j++)
            {
                g2.setColor(colors[j]);
                g2.fill(blocks[i][j]);
                g2.setColor(new Color(0, 0, 0, 128));
                g2.fill(hlblocks[i][j]);
            }
        }

        for (int i = 0; i < colorBlocks.length; i++)
        {
            g2.setColor(colors[i]);
            g2.fill(colorBlocks[i]);
        }

    }

    public void setGroup(int index)
    {
        if (index < 0 || index >= grouper.getGroupCount())
            return;

        this.groupIndex = index;
        retrieveData();
        initPlot();
        repaint();
        tableModel.fireTableDataChanged();

    }

    public String getGroupName()
    {
        if (grouper == null)
            return "";
        else
        {
            try
            {
                return grouper.getGroupName(groupIndex);
            }
            catch (Exception e)
            {
                return "";
            }
        }
    }


    private Color randomColor()
    {
        int c1 = (int) (Math.random() * 256);
        int c2 = (int) (Math.random() * 256);
        int c3 = (int) (Math.random() * 256);

        return new Color(c1, c2, c3);
    }

    /**
     * Get the degree of freedom for the chi square distribution
     */
    public int getDf()
    {
        int m = xlevels.length;
        int n = ylevels.length;

        return m * n - m - n + 1;
    }

    public double getChiSquare()
    {
        return ChiSquare;
    }

    public TableModel getTableModel()
    {
        return tableModel;
    }

    public double[][] getFreqMatrix()
    {
        return freqs;
    }
    
    public String[] getXLevels() {
    	return xlevels;
    }
    
    public String[] getYLevels() {
    	return ylevels;
    }

    public double[] getRowSums()
    {
        return row_sum;
    }

    public double[] getColSums()
    {
        return col_sum;
    }


    public void updatePlot(String msg)
    {
        //if ("obs_deleted".equals(msg) || "yymask".equals(msg) )
        {
            retrieveData();
            initPlot();
            repaint();
        }
    }

    /**
     * CTableModel is an inner class that implements TableModel interface. It is used to construct a contigency table
     * using the data already contained in MosaicPlot class.
     */
    class CTableModel extends AbstractTableModel
    {
        private int cellOption = MosaicPlot.SHOW_COUNT;

        public void setCellOption(int index)
        {
            cellOption = index;
            this.fireTableStructureChanged();
            //this.fireTableDataChanged();
        }

        public int getColumnCount()
        {
            if (cellOption == SHOW_ROW_P)
                return freqs[0].length + 1;
            else
                return freqs[0].length + 2;
        }

        public int getRowCount()
        {
            if (cellOption == SHOW_COL_P)
                return freqs.length;
            else
                return freqs.length + 1;
        }

        public Object getValueAt(int row, int col)
        {
            Object obj = null;


            if (col == 0 && row < freqs.length)
                return xlevels[row];

            if (col == 0 && row == freqs.length)
            {
                if (cellOption == SHOW_COUNT)
                    return "Col Sum";
                else
                    return "Col %";
            }

            col--;
            if (row < freqs.length && col < freqs[0].length)
            {
                switch (cellOption)
                {
                    case SHOW_TOTAL_P:
                        obj = new Double(100 * freqs[row][col] / total);
                        break;
                    case SHOW_ROW_P:
                        obj = new Double(100 * freqs[row][col] / row_sum[row]);
                        break;
                    case SHOW_COL_P:
                        obj = new Double(100 * freqs[row][col] / col_sum[col]);
                        break;
                    case SHOW_CELL_CHI2:
                        obj = new Double(Math.pow(freqs[row][col] - expected[row][col], 2) / expected[row][col]);
                        break;
                    default:
                        obj = new Double(freqs[row][col]);
                }
            }
            else if (row == freqs.length && col < freqs[0].length)
            {
                switch (cellOption)
                {
                    case SHOW_TOTAL_P:
                        obj = new Double(100 * col_sum[col] / total);
                        break;
                    case SHOW_COL_P:
                        obj = new Double(0);
                        break;
                    case SHOW_ROW_P:
                        obj = new Double(100 * col_sum[col] / total);
                        break;
                    case SHOW_CELL_CHI2:
                        obj = new Double(chi_col_sum[col]);
                        break;
                    default:
                        obj = new Double(col_sum[col]);
                }
            }
            else if (row < freqs.length && col == freqs[0].length)
            {
                switch (cellOption)
                {
                    case SHOW_TOTAL_P:
                        obj = new Double(100 * row_sum[row] / total);
                        break;
                    case SHOW_COL_P:
                        obj = new Double(100 * row_sum[row] / total);
                        break;
                    case SHOW_ROW_P:
                        obj = new Double(0);
                        break;
                    case SHOW_CELL_CHI2:
                        obj = new Double(chi_row_sum[row]);
                        break;
                    default:
                        obj = new Double(row_sum[row]);
                }
            }
            else if (row == freqs.length && col == freqs[0].length)
            {
                if (cellOption == SHOW_CELL_CHI2)
                    obj = new Double(ChiSquare);
                else if (cellOption == SHOW_TOTAL_P)
                    obj = new Double(100);
                else
                    obj = new Double(total);
            }

            return obj;
        }

        public String getColumnName(int col)
        {
            if (col == 0)
                return null;
            if (col == ylevels.length + 1)
            {
                if (cellOption == SHOW_COUNT)
                    return "Row Sum";
                else
                    return "Row %";
            }

            return ylevels[col - 1];

        }

        public Class getColumnClass(int col)
        {
            if (col == 0)
                return String.class;

            return Double.class;
        }

        public boolean isCellEditable(int row, int col)
        {
            return false;
        }
    }


    /**
     * Inner class AllListener listens for all mouse event and mouse motion event.
     */
    class AllListener extends MouseAdapter implements MouseMotionListener
    {

        public void mouseMoved(MouseEvent me)
        {
            Point pt = me.getPoint();

            for (int i = 0; i < colorBlocks.length; i++)
            {
                if (colorBlocks[i] != null && colorBlocks[i].contains(pt))
                {
                    MosaicPlot.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    return;
                }
            }

            MosaicPlot.this.setCursor(Cursor.getDefaultCursor());
        }

        public void mouseDragged(MouseEvent me)
        {
        }

        public void mousePressed(MouseEvent me)
        {
            Point pt = me.getPoint();
            
            /*if (popupArrow.getBounds().contains(me.getX(), me.getY())) {
                JPopupMenu popupOptionMenu = ((CTableModule) module).getOptionMenu().getPopupMenu();
                popupOptionMenu.show(me.getComponent(), me.getX(), me.getY());
                return;
            }*/

            for (int i = 0; i < colorBlocks.length; i++)
            {
                if (colorBlocks[i].contains(pt))
                {
                    Color c = null;

                    c = JColorChooser.showDialog(MosaicPlot.this.getParent(), "Palette", Color.black);

                    if (c != null)
                    {
                        colors[i] = c;
                        repaint();
                    }
                    return;
                }
            }

            Rectangle2D rect=null;
            int xLevel=-1;
            int yLevel=-1;
            for(int i=0;i<blocks.length;i++){
                 for(int j=0;j<blocks[i].length;j++){
                     if(blocks[i][j].contains(pt)){
                         rect=blocks[i][j];
                         xLevel=i;
                         yLevel=j;
                         break;
                     }
                 }
            }

            if (rect != null)
            {
                for(int i=0;i<data.getSize();i++){
                    data.setState(false, i);
                }

                for (int i = 0; i < xValues.length; i++)
                {
                    if (data.getMask(i) == false && xValues[i] == xLevel && yValues[i] == yLevel)
                    {
                     	if(z_var == null)
                            data.setState(true, i);
                      	else if(grouper.getGroupIndex(i).contains(new Integer(groupIndex)))
                      	  	data.setState(true, i);
                    }
                }
            }
        }
    }
}
