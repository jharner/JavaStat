/*
 * MultinomialReport.java
 *
 * Created on February 6, 2002, 10:25 AM
 */

package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.text.NumberFormat;


import wvustat.dist.Chi2Dist;
/**
 *
 * @author  James Harner
 * @version
 */
public class MultinomialReport extends javax.swing.JPanel implements java.awt.event.MouseListener {
    private int width=200, height=80;
    private Insets insets=new Insets(8,10,10,10);
    private double[][] data;
    private double[] row_sums, col_sums;
    private double[] p;
    private double ChiSquare, pValue;
    private Chi2Dist dist;
    private Rectangle2D[] rects;
    /** Creates new MultinomialReport */
    public MultinomialReport(double[][] data, double[] row_sums, double[] col_sums) {
        this.data=data;
        this.row_sums=row_sums;
        this.col_sums=col_sums;
        
        p=new double[row_sums.length];
        
        double total=0;
        for(int i=0;i<row_sums.length;i++)
        	total+=row_sums[i];
        	
        for(int i=0;i<p.length;i++)
            p[i]=row_sums[i]/total;
        
        ChiSquare=computeChiSquare();
        dist=new Chi2Dist((row_sums.length-1)*(col_sums.length-1));
        if(ChiSquare==0)
            pValue=0;
        else
            pValue=1-dist.cdf(ChiSquare);
        if (p.length > 0)
        rects=new Rectangle2D[p.length-1];
        
        setBackground(Color.white);
        
        
    }
    
    public java.awt.Dimension getPreferredSize() {
        FontMetrics metrics=this.getFontMetrics(new Font("Dialog", Font.PLAIN, 12));
        
        height=(p.length+2)*metrics.getHeight()+insets.top+insets.bottom+4;
        return new Dimension(width, height);
    }
    
    private double computeChiSquare(){
        double ret=0;
        
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[i].length;j++){
                double exp=col_sums[j]*p[i];
                ret+=Math.pow(data[i][j]-exp,2)/exp;
            }
        }
        
        return ret;
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        Graphics2D g2=(Graphics2D)g;
        FontMetrics metrics=g2.getFontMetrics();
        int x0=insets.left;
        int y0=insets.top+metrics.getMaxAscent();
        NumberFormat nf=NumberFormat.getInstance();
        
        String text="Chi Square="+nf.format(ChiSquare);
        g2.drawString(text, x0, y0);
        
        x0+=metrics.stringWidth(text)+60;
        g2.drawString("P Value="+nf.format(pValue), x0, y0);
        y0+=metrics.getHeight()+4;
        x0=insets.left;
        g2.drawString("Hypothesis:",x0,y0);
        y0+=metrics.getHeight();
        //g2.setPaint(Color.darkGray);
        
        for(int i=0;i<p.length;i++){
            for(int j=0;j<col_sums.length;j++){
                x0+=drawText(g2,"P",i+1,j+1, x0, y0, (j==col_sums.length-1));
            }
            //g2.setPaint(Color.blue);
            //String s=" "+nf.format(p[i]);
            //g2.drawString(s, x0, y0);
            //rects[i]=new Rectangle2D.Float(x0,y0-metrics.getMaxAscent(),metrics.stringWidth(s),metrics.getMaxAscent());
            //g2.setPaint(Color.darkGray);
            y0+=metrics.getHeight();
            x0=insets.left;
        }
    }
    
    private int drawText(Graphics g, String str, int i, int j, int x, int y, boolean last){
        int len=0;
        FontMetrics metrics=g.getFontMetrics();
        Font oldFont=g.getFont();
        len=metrics.stringWidth(str);
        g.drawString(str, x, y);
        x+=len;
        Font subFont=new Font("Dialog", Font.PLAIN, 9);
        FontMetrics metrics2=g.getFontMetrics(subFont);
        g.setFont(subFont);
        String subText=String.valueOf(i)+","+String.valueOf(j);
        g.drawString(subText,x,y+4);
        len+=metrics2.stringWidth(subText);
        g.setFont(oldFont);
        x+=metrics2.stringWidth(subText);
        if(last==false){
        	g.drawString("=", x, y);
        	len+=metrics.stringWidth("=");
        }
        return len;
    }
    
    private boolean checkPValues(){
        double sum=0;
        for(int i=0;i<p.length-1;i++)
            sum+=p[i];
        
        if(sum>1)
            return false;
        else{
            p[p.length-1]=1-sum;
            return true;
        }
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Point pt=mouseEvent.getPoint();
        
        for(int i=0;i<rects.length;i++){
            if(rects[i].contains(pt)){
                String input=JOptionPane.showInputDialog(this,"Please input the null p value:","Input",JOptionPane.INFORMATION_MESSAGE);
                try{
                    double tmpD=Double.parseDouble(input);
                    
                    p[i]=tmpD;
                    if(!checkPValues()){
                        JOptionPane.showMessageDialog(this,"The sum of all the p values must be less than or equal to 1.0","Warning", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        ChiSquare=computeChiSquare();
                        pValue=1-dist.cdf(ChiSquare);
                        repaint();
                    }
                }
                catch(NumberFormatException nfe){
                }
                return;
            }
        }
        
    }
    
    public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
    }
}
