package wvustat.simulation.relfreq;

import java.awt.*;
import javax.swing.*;

public class PlotCanvas extends JPanel {

  RelFreq containedBy;
  PlotCoord pc;
  int inset = 20;
  int supercounter=1;
  float	ymax = (float)1.0, ymin = (float)0.0;
  float	xmax = (float)100.0, xmin = (float)0.0;
  float	p;
  public float accumuResult[] = new float[10001];

  public boolean changeN = true;
  public boolean ifRedo = true;
  public int TimeInterval;
  public int xcur=1;
  int xcoord = 0, ycoord = 0;
  int getScale = 1;
  int tmpXmax;
  boolean ifNeedDrawTick = false;
  	
  	int xcoordPrev = 0, ycoordPrev = 0;
    	float ranNum;
    	int ifSucc=0;
    	float accumuProb=(float)0.0;
    	float xprev = (float)1.0, yprev;
    	int ifGreater700 = 0; 
    	int xsub;     
    	float ycur=0;
  
  PlotCanvas(RelFreq containedByIn) {
        super();
        containedBy = containedByIn;
  }


  public float getXmin() {
    return(xmax);
  }

  public void setXmax(float newXmax) {
    if ( Math.abs(xmax - newXmax) > 0.01 ){
    	xmax = newXmax;
    	changeN = true;
    }
  }
  
  

  public float getP() {
    return(p);
  }

  public void setP(float newP) {
    p = newP;
  }

  
 
  
  public void paintComponent(Graphics g) {
    
    super.paintComponent(g);
    
    if ( xmax < 10 )
    		tmpXmax = (int)xmax;
        else {
    		getScale = (int)Math.round(Math.log(xmax)/Math.log(10)+0.4999999);
    		tmpXmax = ((int)(xmax/10+0.999999))*10;
    		if ( tmpXmax != xmax ) ifNeedDrawTick = true;
    	}
    	ymin = 0; ymax = 1;
    	pc = new PlotCoord (tmpXmax, ymax, xmin,
        ymin, this.getSize().width, this.getSize().height-30, inset);
        
        p = getP();
        
        if( xmax <= 9999 )
    	containedBy.overFlowFlag = false;
    	
    g.setColor(Color.white);   
    /* Draw x axis. */
    g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               pc.xcoord(tmpXmax), pc.ycoord(ymin));
    Font f = new Font ("Dialog", Font.PLAIN, 10);
    g.setFont(f);
    g.drawString("Trials", pc.xcoord(xmax/2)-25, pc.ycoord(ymin)+35);

    /* Draw y axis. */
    g.drawLine(pc.xcoord(xmin), pc.ycoord(ymin),
               pc.xcoord(xmin), pc.ycoord(ymax));

    /* Draw p=... Line */
    g.setColor(Color.red);
    
    g.drawLine(pc.xcoord(xmin), pc.ycoord(p*ymax),
               pc.xcoord(tmpXmax), pc.ycoord(p*ymax));
    g.setColor(Color.white);
 
    /* This draws one tick mark on x */
    if(xmax<10) {
        for (int h=0; h<=xmax; h++) {
            xcoord = pc.xcoord(xmin + h*(xmax - xmin)/xmax);
            ycoord = pc.ycoord(ymin);

           g.drawLine(xcoord, ycoord + 3, xcoord , ycoord - 3);
           g.drawString(
           new Integer(Math.round(xmin + h*(xmax - xmin)/xmax)).toString(),
                 xcoord - 3, ycoord + 15);
        }
    }
    else {
        for (int h=0; h<=10; h++) {
            xcoord = pc.xcoord(xmin + h*tmpXmax/10);
            ycoord = pc.ycoord(ymin);
            if ( !containedBy.overFlowFlag || h < 10){
                g.drawLine(xcoord, ycoord + 3, xcoord , ycoord - 3);
                g.drawString(
                new Integer(Math.round(xmin + h*tmpXmax/10)).toString(),
                     xcoord - (int)5*getScale/2, ycoord + 15);
            }
        }
    }


    String tmpString=" ";

   /* here drawing one tick mark on y */
    for (int h = 0; h <= 4; h ++){
    	switch(h){
    		case 0: tmpString = "0.00"; break;
    		case 1: tmpString = "0.25"; break;
    		case 2: tmpString = "0.50"; break;
    		case 3: tmpString = "0.75"; break;
    		case 4: tmpString = "1.00"; break;
    	}
    	
    	ycoord = pc.ycoord((float)(1.0*ymax*h/4.0));
    	xcoord = pc.xcoord(xmin);
    	g.drawLine(xcoord+3, ycoord , xcoord - 3, ycoord );
  		g.drawString( tmpString, 18, ycoord + 4 );
		
	}
	
    /* Here drawing one tick mark on x if needed */

    if ( ifNeedDrawTick && !containedBy.overFlowFlag){
        g.setColor(Color.red);
        xcoord = pc.xcoord(xmax);
        ycoord = pc.ycoord(ymin);

        g.drawLine(xcoord, ycoord, xcoord , ycoord-2);
        g.drawString(
        new Integer(Math.round(xmax)).toString(),
                 xcoord - (int)4*getScale/2, ycoord - 6);
        g.setColor(Color.white);
    } 
    /* end of tick marks at the max's. */

    // Drawing Curve
    g.setColor(Color.green);
   

 	
    if ( !containedBy.ifInitial ){
    
     
    	
 	//xcur old position	
   	
      if (xmax>700) ifGreater700 = 1;

      xcoordPrev = pc.xcoord(xprev);
      ranNum = (float)Math.random();
      if (ifRedo) {
    	if (ranNum<p) {
    		yprev = (float)1.0;
    		accumuProb = (float)1.0;
    	}
    	else { 
    		yprev = (float)0.0;
    		accumuProb = (float)0.0;
    	}
    	accumuResult[xcur] = accumuProb;
    	ycoordPrev = pc.ycoord(yprev);
        
     
	 
  		for (xcur = 2; xcur <= Math.round(xmax); xcur ++) {

      		// Generate a Random number
      		ranNum = (float)Math.random();
      		if (ranNum<p) ifSucc = 1;
      		else ifSucc = 0;
      		accumuProb = ((xcur-1)*accumuProb + ifSucc)/xcur;
      		accumuResult[xcur] = accumuProb;
    		
      	//	xcoord = pc.xcoord((float)xcur);
       	//	ycoord = pc.ycoord(accumuResult[xcur]);
     	  
      		/* ie, don't do the very first x */
 		if ( accumuResult[xcur] >= 0.995 )
			containedBy.labelPHat.setText("         p^ = 1.00      ") ;
		else 
			containedBy.labelPHat.setText("         p^ = 0." + (new Integer(Math.round(accumuResult[xcur] * 100F))).toString()+"      ");
  		
  			
			
		}// end of for (xcur=2)
		
	  } // end of if (ifRedo)
	  else {
 		ycoordPrev = pc.ycoord(accumuResult[1]);
   		for (xcur = 2; xcur <= Math.round(xmax); xcur ++) {
   			
   	//		xcoord = pc.xcoord((float)xcur);
   	//		ycoord = pc.ycoord(accumuResult[xcur]);
   			
     	//	g.drawLine(xcoordPrev+ifGreater700, ycoordPrev,
	//           	 	xcoord+ifGreater700, ycoord);
      	//	xcoordPrev = xcoord;
      	//	ycoordPrev = ycoord;
      	//	xprev = xcur;
      	//	yprev = ycur;
      	}
		if ( accumuResult[Math.round(xmax)] >= 0.995 )
			containedBy.labelPHat.setText("         p^ = 1.00      ");
                else
                    containedBy.labelPHat.setText("         p^ = 0." + (new Integer(Math.round(accumuResult[Math.round(xmax)] * 100F))).toString()+"      ");
	  }
	  
	  supercounter=containedBy.counter;
	  
	  
	  if (accumuResult[2]<=0.5)
	  g.drawLine(pc.xcoord((float)0),pc.ycoord((float)0),
	  pc.xcoord((float)2),pc.ycoord(accumuResult[2]));
	  if (accumuResult[2]>0.5)
	  g.drawLine(pc.xcoord((float)0),pc.ycoord((float)1),
	  pc.xcoord((float)2),pc.ycoord(accumuResult[2]));
	  
	  for (int i=2;i<supercounter;i++){
	       
	  	g.setColor(Color.green);
	  	xcoord = pc.xcoord((float)i);
   		ycoord = pc.ycoord(accumuResult[i]);
   		g.drawLine(pc.xcoord((float)i),pc.ycoord(accumuResult[i]),
   		pc.xcoord((float)(i+1)),pc.ycoord(accumuResult[i+1]));
      		//g.drawLine(xcoordPrev+ifGreater700, ycoordPrev,
	        //xcoord+ifGreater700, ycoord);
		xcoordPrev = xcoord;
      		ycoordPrev = ycoord;
      		
      	}
      	
   	  ifRedo = false;
   	}
   	//containedBy.canRedraw = true;
  } // End of method paint
  

} // End of Class

