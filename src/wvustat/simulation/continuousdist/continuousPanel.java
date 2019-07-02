package wvustat.simulation.continuousdist;

import java.awt.*;
import javax.swing.*;

public class continuousPanel extends JPanel{

  static	double	ymax, ymin, xmax, xmin, xmid = 0;
  continuousInfo	cInfo;
  continuousDist	distApplet;
  int				paraIndex;
  private			Dimension dim; 
  int		xTickMarks = 5;
  int		yTickMarks = 5;
  tickmarkManager tManager;
  String tmpString = new String();
  
  public continuousPanel(continuousInfo tmpInfo, continuousDist tmpApplet) {
    	super();
	distApplet = tmpApplet;
  	cInfo = tmpInfo;
  	tManager = new tickmarkManager(1.0);
  	setBackground(Color.black);

  }
  
  public void paintComponent(Graphics g) {
  	
  	super.paintComponent(g);
	Insets	inset = new Insets(10, 60, 20, 30) ;	
	double	xTickInterval;
	int		xcoord, ycoord;
	
	PlotCoord pc;
	
	dim = getSize();
	
	distApplet.textFieldPara1.setText(distApplet.cInfo.getParaString(0));
	String str=new Double(Math.round(distApplet.cInfo.quantile*100)/100.0).toString();
	if(str.length()<=3) str=str+"0";
	distApplet.qPanel.label4.setText("X ( "+str+" )=");
	if (!distApplet.cInfo.isNormal)
		distApplet.cInfo.setChiSqCont();
	if (distApplet.cInfo.isNormal)
		distApplet.textFieldPara2.setText(distApplet.cInfo.getParaString(1));

	if(distApplet.cInfo.isPDF())	{
		
		distApplet.GEItem.setEnabled(true);
		distApplet.LEItem.setEnabled(true);
		distApplet.GELEItem.setEnabled(true);
		distApplet.EItem.setEnabled(true);
		if (distApplet.ifMenu==false) {
			distApplet.rowLabel1.setText("");
			distApplet.rowLabel2.setText("");
			}
		else {	
		    distApplet.rowLabel1.setForeground(Color.red);
		    distApplet.rowLabel2.setForeground(Color.red);
		    distApplet.rowLabel1.setText(distApplet.cInfo.getProbString()+distApplet.cInfo.getProbStringValue());
		 	distApplet.rowLabel2.setText(distApplet.cInfo.getApproxProbString()+distApplet.cInfo.getApproxProbValue());
		}
		((CardLayout)distApplet.resultPanel.getLayout()).show(distApplet.resultPanel, "prob");
		((CardLayout)distApplet.approxPanel.getLayout()).show(distApplet.approxPanel, "norm");
	}
	else {
		
		distApplet.GEItem.setEnabled(false);
		distApplet.LEItem.setEnabled(false);
		distApplet.GELEItem.setEnabled(false);
		distApplet.EItem.setEnabled(false);
		distApplet.labelQuantile.setText(distApplet.cInfo.getProbStringValue());
		distApplet.labelApprox.setText("");
		((CardLayout)distApplet.resultPanel.getLayout()).show(distApplet.resultPanel, "quantile");
		((CardLayout)distApplet.approxPanel.getLayout()).show(distApplet.approxPanel, "approx");
	}
	
	
	ymax = distApplet.cInfo.countYmax();
	xmax = distApplet.cInfo.countXmax();
	xmin = distApplet.cInfo.countXmin();
	ymin = distApplet.cInfo.countYmin();
	xmid = (Math.round((xmax+xmin)/2*10))/10.0;
	tManager.reset(ymax);
	ymax = tManager.getMaxValue();
	yTickMarks = tManager.getTickNumber();

	xTickInterval = distApplet.cInfo.setXTickMarkInterval(xmax-xmin);
	pc = new PlotCoord (xmax, ymax, xmin, ymin, dim.width, dim.height-10, inset);
	g.setFont(new Font ("Dialog", Font.PLAIN, 11));

    /* Draw x&y axis. */
	g.setColor(Color.white);
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               pc.xcoord(xmax), pc.ycoord(ymin));
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymax),
               pc.xcoord(xmin), pc.ycoord(ymin));

	String tmpString;
	// Draw X' & Y' tickmarks
	int halfNumberXTick = xTickMarks/2;
	double tmpDouble = 0.0;
	int tmpStringLength = 3;
	for (int i=-halfNumberXTick; i<=halfNumberXTick; i++) {
		
		tmpStringLength = 3;
		tmpDouble = xmid + i * xTickInterval;
		tmpString = new String(new Double( tmpDouble ).toString());
		xcoord = pc.xcoord( tmpDouble ) + 1;
		ycoord = pc.ycoord(ymin);

		g.drawLine(xcoord , ycoord - 3, xcoord , ycoord + 3);

		if (tmpDouble < 0) tmpStringLength++;
		if (Math.abs(tmpDouble) > 10.0) tmpStringLength++;
		if (Math.abs(tmpDouble) > 100.0) tmpStringLength++;
		
			g.drawString (
				new String (tmpString.substring(0, Math.min(tmpStringLength, tmpString.length()))),
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
	g.setColor(Color.cyan);

	double	tmpProb = 0.0;
	int		xprevcoord = pc.xcoord(xmin);
	int		yprevcoord;
 	int		steps = 600;
 	double	stepWidth = (xmax - xmin)/steps;
 	double	xPosTmp;
 	int recX[] = new int[4];
	int recY[] = new int[4];
	
	if (distApplet.cInfo.isPDF())
		yprevcoord = pc.ycoord(cInfo.pointYvalue(xmin));
	else
		yprevcoord = pc.ycoord(0);

 	for (int i = 1; i <= steps; i++) {
 	
		
		xPosTmp = xmin + i * stepWidth;
		xcoord = pc.xcoord( xPosTmp ) + 1;
		if (distApplet.cInfo.isPDF()) {
			ycoord = pc.ycoord(cInfo.pointYvalue(xPosTmp));
			if (( xPosTmp-stepWidth+1E-4 > distApplet.cInfo.questionLower ) &&
				( xPosTmp-1E-4 < distApplet.cInfo.questionUpper ) ) {
				g.setColor(Color.red);
				recX[0] = recX[1] = xprevcoord;	
				recY[0] = recY[3] = pc.ycoord(0.0);
				recX[2] = recX[3] = xcoord;
				recY[1] = yprevcoord; recY[2] = ycoord;
				g.fillPolygon(recX, recY, 4);
				g.setColor(Color.cyan);
			}
		}
		else
 			ycoord = pc.ycoord(cInfo.accumYvalue(xPosTmp));
            
		g.drawLine(xcoord , ycoord, xprevcoord, yprevcoord);
		xprevcoord = xcoord;
		yprevcoord = ycoord;
	}
	
	if (!distApplet.cInfo.isPDF()) {
		
		int xcoordQuantile = pc.xcoord(distApplet.cInfo.computeQuantile());
		int ycoordQuantile = pc.ycoord(distApplet.cInfo.quantile);
		
		g.setColor(Color.red);
		g.drawLine(pc.xcoord(xmin), ycoordQuantile, xcoordQuantile, ycoordQuantile);
		g.drawLine(xcoordQuantile, pc.ycoord(0.0), xcoordQuantile, ycoordQuantile);
		distApplet.labelQuantile.setForeground(Color.red);
		distApplet.labelApprox.setForeground(Color.red);
		distApplet.labelQuantile.setText(new Double((int)(distApplet.cInfo.computeQuantile()*1000)
					/1000.0).toString());
	}
      	
    /* Draw x&y axis. */
	g.setColor(Color.white);
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               pc.xcoord(xmax), pc.ycoord(ymin));
	g.drawLine(pc.xcoord(xmin), pc.ycoord(ymax),
               pc.xcoord(xmin), pc.ycoord(ymin));
	g.drawLine(pc.xcoord(xmin)+1, pc.ycoord(ymax),
               pc.xcoord(xmin)+1, pc.ycoord(ymin));

	if ( distApplet.cInfo.isPDF() ) {
	    distApplet.labelQuantile.setForeground(Color.red);
		distApplet.labelApprox.setForeground(Color.red);
 		distApplet.labelQuantile.setText(distApplet.cInfo.getProbString());
		distApplet.labelApprox.setText(distApplet.cInfo.getApproxProbString());
	}
			
	distApplet.cInfo.doRescale = false;
  }
  
}


