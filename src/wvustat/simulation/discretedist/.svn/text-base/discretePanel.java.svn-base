package wvustat.simulation.discretedist;

import java.awt.*;
import javax.swing.*;

public class discretePanel extends JPanel {

	static	int	xmax, xmin;
	static	double	ymax, ymin;
	discreteInfo	dInfo;
	discreteDist	distApplet;
  	int	paraIndex;
	private Dimension dim;
	int	xTickMarks = 0;
	int	yTickMarks = 5;
  	tickmarkManager	tManager;
  	String str,str2;
  
  public discretePanel(discreteInfo tmpInfo,discreteDist tmpApplet) {

	distApplet = tmpApplet;
  	dInfo = tmpInfo;
  	tManager = new tickmarkManager(1.0);
  	setBackground(Color.black);

  } 
  
  public void paintComponent(Graphics g) {
  	
  	super.paintComponent(g);
	int	scaleInterval;
	Insets	inset = new Insets(10, 60, 20, 30) ;
	int	xcoord, ycoord;
	boolean needPoissonApprox = distApplet.dInfo.isPoissonApprox(); 
	boolean needNormalApprox = distApplet.dInfo.isNormalApprox();
	String	tmpString = new String();
	PlotCoord pc;
	
	dim = getSize();
	
	distApplet.textFieldPara1.setText(distApplet.dInfo.getParaString(0));
	String str1=new Double(Math.round(distApplet.dInfo.quantile*100)/100.0).toString();
	if(str1.length()<=3) str1=str1+"0";
	distApplet.qPanel.label4.setText("X ( "+str1+" )=");
	if (distApplet.dInfo.isBinomial)
		distApplet.textFieldPara2.setText(distApplet.dInfo.getParaString(1));

	if(distApplet.dInfo.isPDF())	{
	//	distApplet.pdfButton.setSelected(true);
		distApplet.GEItem.setEnabled(true);
		distApplet.LEItem.setEnabled(true);
		distApplet.GELEItem.setEnabled(true);
		distApplet.EItem.setEnabled(true);
		distApplet.labelProb.setText("     "+distApplet.dInfo.getProbString()+distApplet.dInfo.getProbStringValue());
		distApplet.labelNorm.setText(distApplet.dInfo.getApproxProbString()+distApplet.dInfo.getApproxProbValue());
		((CardLayout)distApplet.resultPanel.getLayout()).show(distApplet.resultPanel, "prob");
		((CardLayout)distApplet.approxPanel.getLayout()).show(distApplet.approxPanel,"norm");
		
	}
	else {
	//	distApplet.cdfButton.setSelected(true);
		distApplet.GEItem.setEnabled(false);
		distApplet.LEItem.setEnabled(false);
		distApplet.GELEItem.setEnabled(false);
		distApplet.EItem.setEnabled(false);
		distApplet.labelQuantile.setText(distApplet.dInfo.getProbString()+distApplet.dInfo.getProbStringValue());
		distApplet.labelApprox.setText(distApplet.dInfo.getApproxProbString()+distApplet.dInfo.getApproxProbValue());
		((CardLayout)distApplet.resultPanel.getLayout()).show(distApplet.resultPanel, "quantile");
		if (distApplet.dInfo.needApprox()) {
			((CardLayout)distApplet.approxPanel.getLayout()).show(distApplet.approxPanel,"approx");}
	} 

	xmin = distApplet.dInfo.countXmin();
	ymin = distApplet.dInfo.countYmin();
	ymax = distApplet.dInfo.countYmax();
	xmax = distApplet.dInfo.countXmax(); 
	tManager.reset(ymax);
	ymax = tManager.getMaxValue();
	yTickMarks = tManager.getTickNumber();
	
	if ( xmax <= 10 ) {
		scaleInterval = 1;
		xTickMarks = xmax;
	}
	else {
		scaleInterval = (xmax-xmin)/10;
		xTickMarks = (xmax-xmin)/scaleInterval;
	}

	pc = new PlotCoord (xmax, ymax, xmin, ymin, dim.width, dim.height-10, inset);
    
    /* Draw x&y axis. */
	g.setColor(Color.white);
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               pc.xcoord(xmax), pc.ycoord(ymin));
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymax),
               pc.xcoord(xmin), pc.ycoord(ymin));
	g.drawLine(pc.xcoord(xmin)+1, pc.ycoord(ymax),
               pc.xcoord(xmin)+1, pc.ycoord(ymin));
	if ( distApplet.dInfo.needApprox() ) 
		g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               	   pc.xcoord(xmax+0.2), pc.ycoord(ymin));
	
	if ( needPoissonApprox ) { // Poisson
		g.setFont(new Font ("Dialog", Font.PLAIN, 9));
		g.setColor(Color.cyan);
		g.drawString("_____" + distApplet.dInfo.distName + ", ....." + 
			distApplet.dInfo.getApproxDistName(), 130, 7);
	}
	
	// Draw X' & Y' tickmarks
	g.setFont(new Font ("Dialog", Font.PLAIN, 11));
	g.setColor(Color.white);
	for (int i=0; i<=xTickMarks; i++) {

		xcoord = pc.xcoord( i * scaleInterval + xmin ) + 1;
		ycoord = pc.ycoord(ymin);

		g.drawLine(xcoord , ycoord + 3, xcoord , ycoord - 3);
		g.drawString(
			new Integer((int)Math.round( i * scaleInterval + xmin )).toString(),
                 xcoord - 3, ycoord + 15);
    }
	for (int i=0; i<=yTickMarks; i++) {
	
		xcoord = pc.xcoord(xmin);
		ycoord = pc.ycoord(ymax/yTickMarks*i);
 
		g.drawLine(xcoord - 3, ycoord, xcoord + 3 , ycoord);
		tmpString = tManager.getTickString(yTickMarks-i);
		g.drawString(tmpString, xcoord - tmpString.length()*5 - 8, ycoord + 3 );


    }
    
	// Draw Graph
 	Poisson poisson = new Poisson(distApplet.dInfo.paraValue[0]*distApplet.dInfo.paraValue[1]);	
	double mu = dInfo.paraValue[0] * dInfo.paraValue[1];
    	double sigma = Math.sqrt( mu * ( 1 - dInfo.paraValue[1] ) );
	Normal normal = new Normal(mu, sigma);

	if ( needNormalApprox ) drawNormalCurve(g, pc, true);
	if ( distApplet.dInfo.isPDF() ){

		
		        
        for (int i = xmin; i <= xmax; i++) {

			if ( distApplet.dInfo.isInterval(i) ) 
					g.setColor(Color.red);
			else 	g.setColor(Color.cyan);
			
            xcoord = pc.xcoord( i ) + 1;
            ycoord = pc.ycoord(dInfo.pointYvalue(i));

            if ( needNormalApprox ) { //Normal Approx, use histogram instead of straite line
				if ( distApplet.dInfo.isInterval(i) ) 
						g.setColor(Color.cyan);
				else 	g.setColor(Color.darkGray);
            	g.fillRect(pc.xcoord(Math.max(i-0.5, xmin)) + 1, ycoord,
            		pc.xcoord(Math.min(i+0.5, xmax)) - pc.xcoord(Math.max(i-0.5, xmin)), 
            		pc.ycoord(0) - ycoord);
				if ( distApplet.dInfo.isInterval(i) ) 
						g.setColor(Color.gray);
				else 	g.setColor(Color.cyan);
             	g.drawRect(pc.xcoord(Math.max(i-0.5, xmin)) + 1, ycoord,
            		pc.xcoord(Math.min(i+0.5, xmax)) - pc.xcoord(Math.max(i-0.5, xmin)), 
            		pc.ycoord(0) - ycoord);
            }		
            else g.drawLine(xcoord , pc.ycoord(0), xcoord , ycoord);
        
       		if ( needPoissonApprox ) { //Poisson
            	int tmpX = pc.xcoord( i + 0.2 ) + 1;
            	int tmpY = pc.ycoord( poisson.f( i ) );
            	for ( int j = tmpY; j <= pc.ycoord(0); j+=2 )
                	g.drawLine(tmpX, j, tmpX, j);
           	}
         }
		
		distApplet.labelApprox.setText(distApplet.dInfo.getApproxProbString());
	
    }

    else {
 	   	boolean isQuantileDrawed = false;
    	if ( Math.abs( distApplet.dInfo.quantile - 1.0 ) < 0.000001 ) {
            g.setColor(Color.red);
            g.drawLine(pc.xcoord(0), pc.ycoord(1.0), 
            			pc.xcoord(xmax)+1, pc.ycoord(1.0));
            g.drawLine(pc.xcoord(xmax)+1, pc.ycoord(0.0), 
            			pc.xcoord(xmax)+1, pc.ycoord(1.0));
			distApplet.labelQuantile.setText(new Integer(xmax).toString());
			isQuantileDrawed = true;
     	}
		g.setColor(Color.cyan);
    	
        double	tmpProb = 0.0;
        double	q = distApplet.dInfo.quantile;
        for (int i = xmin; i < xmax; i++) {
        	
        	tmpProb += dInfo.pointYvalue(i);
            xcoord = pc.xcoord( i ) + 1;
            ycoord = pc.ycoord(tmpProb);
            
            if ( !isQuantileDrawed && ( tmpProb > q )
            	&& ( Math.abs(tmpProb-q) > 0.00001 ) ) {
            	isQuantileDrawed = true;
            	g.setColor(Color.red);
            	g.drawLine(	pc.xcoord(0), pc.ycoord(q), 
            				pc.xcoord(i)+1, pc.ycoord(q));
             	g.drawLine(	pc.xcoord(i)+1, pc.ycoord(0.0), 
            				pc.xcoord(i)+1, pc.ycoord(q));
            	g.setColor(Color.cyan);
				distApplet.labelQuantile.setText(new Integer(i).toString());
            }

            g.drawLine(xcoord , ycoord, pc.xcoord(i+1)+1 , ycoord);
        	if ( needPoissonApprox ) 
            	for ( int j = xcoord; j <= pc.xcoord(i+1)+1; j+=2 )
                	g.drawLine(j, pc.ycoord(poisson.F(i)), j, pc.ycoord(poisson.F(i)));
        }
       	if ( distApplet.dInfo.needApprox() ) {
       		double quantile;
       		g.setColor(Color.red);
       		if ( needPoissonApprox )
       			quantile = poisson.Quantile(q); 
       		else quantile = normal.Quantile(q);
       		if ( q >= 0.999 ) quantile = xmax;
            for ( int i = pc.xcoord(0); i <= pc.xcoord(quantile); i+=2 )
                g.drawLine(i, pc.ycoord(q), i, pc.ycoord(q));
            for ( int i = pc.ycoord(0); i >= pc.ycoord(q); i-=2 )
                g.drawLine(pc.xcoord(quantile)+1, i, pc.xcoord(quantile)+1, i);
			if ( needPoissonApprox ){
				String str=new Integer((int)(quantile+1E-4)).toString();
				distApplet.labelApprox.setText("    Poisson Approx:   "+str);}
			else {
				String str2=new Double((int)(quantile*1000)/1000.0).toString();
				distApplet.labelApprox.setText("    Normal Approx:   "+str2 );
			};
			g.setColor(Color.cyan);
      	}	
       		
		g.drawLine(pc.xcoord(xmax) , pc.ycoord(1.0), pc.xcoord(xmax)+15 , pc.ycoord(1.0));
    }
	if ( needNormalApprox ) drawNormalCurve(g, pc, false);
    
    /* Draw x axis. again*/
	g.setColor(Color.white);
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               pc.xcoord(xmax), pc.ycoord(ymin));

	distApplet.dInfo.doRescale = false; 
     
  }
  	
  private void drawNormalCurve(Graphics  g, PlotCoord pc, boolean drawProb) {
  
	double mu = dInfo.paraValue[0] * dInfo.paraValue[1];
    	double sigma = Math.sqrt( mu * ( 1 - dInfo.paraValue[1] ) );
   	Normal normal = new Normal(mu,sigma);
  	double	tmpProb = 0.0;

	int		xprevcoord = pc.xcoord(xmin);
	int		yprevcoord = pc.ycoord(normal.pdf(xmin));
	int 	xcoord = 0, ycoord = 0;

 	int		steps = 600;
 	double	stepWidth = (xmax - xmin)/(double)steps;
 	double	xPosTmp;
 	int recX[] = new int[4];
	int recY[] = new int[4];
	
	g.setColor(Color.yellow); 
 	for (int i = 1; i <= steps; i++) {
 	
		xPosTmp = xmin + i * stepWidth;
		xcoord = pc.xcoord( xPosTmp ) + 1;
		if (distApplet.dInfo.isPDF()) {
			ycoord = pc.ycoord(normal.pdf(xPosTmp));

			if (( xPosTmp-stepWidth+1E-4 > Math.max(distApplet.dInfo.questionLower-0.5, xmin) ) &&
				( xPosTmp-1E-4 < Math.min(distApplet.dInfo.questionUpper+0.5, xmax) ) && drawProb) {
				recX[0] = recX[1] = xprevcoord;	
				recY[0] = recY[3] = pc.ycoord(0.0);
				recX[2] = recX[3] = xcoord;
				recY[1] = yprevcoord; recY[2] = ycoord;
				g.fillPolygon(recX, recY, 4);
				g.setColor(Color.yellow);
			}
		}
		else
 			ycoord = pc.ycoord(normal.cdf(xPosTmp));
            
		g.drawLine(xcoord , ycoord, xprevcoord, yprevcoord);
		xprevcoord = xcoord;
		yprevcoord = ycoord;
	}
	
  } 
  
    
}
